package facades;

import dtos.BoatDTO;
import entities.Boat;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

//import errorhandling.RenameMeNotFoundException;
import entities.Harbour;
import entities.User;
import utils.EMF_Creator;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class BoatFacade {

    private static BoatFacade instance;
    private static EntityManagerFactory emf;
    
    //Private Constructor to ensure Singleton
    private BoatFacade() {}
    
    
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static BoatFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new BoatFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public BoatDTO create(BoatDTO boatDTO){
        Boat boat = new Boat();
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(boat);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new BoatDTO(boat);
    }
    public BoatDTO getById(long id) { //throws RenameMeNotFoundException {
        EntityManager em = emf.createEntityManager();
        Boat boat = em.find(Boat.class, id);
//        if (boat == null)
//            throw new RenameMeNotFoundException("The RenameMe entity with ID: "+id+" Was not found");
        return new BoatDTO(boat);
    }
    
    public List<BoatDTO> getAll(){
        EntityManager em = emf.createEntityManager();
        TypedQuery<Boat> query = em.createQuery("SELECT r FROM Boat r", Boat.class);
        List<Boat> rms = query.getResultList();
        return BoatDTO.getDtos(rms);
    }

    public List<User> getAllOwnersByID(long id) {
        EntityManager em = emf.createEntityManager();
        Boat boat = em.find(Boat.class, id);
        return boat.getOwners();
    }

    public Boat createBoat(BoatDTO boatDTO) {
        EntityManager em = emf.createEntityManager();

        List<User> owners = new ArrayList<>();
        for (String owner : boatDTO.getOwners()) {
            User user = em.find(User.class,owner);
            owners.add(user);
        }
        Harbour harbour = em.find(Harbour.class, boatDTO.getHarbourID());
        Boat boat = new Boat(boatDTO.getBrand(), boatDTO.getMake(), boatDTO.getName(), boatDTO.getImage(), owners, harbour);

        try {
            em.getTransaction().begin();
            harbour.addBoat(boat);
            em.merge(harbour);

            for (User owner : owners) {
                owner.addBoat(boat);
                em.merge(owner);
            }

            em.persist(boat);
            em.getTransaction().commit();

            return boat;
        } finally {
            em.close();
        }
    }

    public Boat updateBoat(long id, BoatDTO boatDTO) {
        EntityManager em = emf.createEntityManager();

        Boat oldBoat = em.find(Boat.class, id);

        List<User> owners = new ArrayList<>();
        for (String owner : boatDTO.getOwners()) {
            User user = em.find(User.class, owner);
            owners.add(user);
        }

        Harbour oldHarbour = em.find(Harbour.class, oldBoat.getHarbour().getId());
        Harbour newHarbour = em.find(Harbour.class, boatDTO.getHarbourID());
        oldHarbour.removeBoat(oldBoat);

        Boat newBoat = new Boat(boatDTO.getBrand(), boatDTO.getMake(), boatDTO.getName(), boatDTO.getImage(), owners, newHarbour);
        newBoat.setId(id);
        newHarbour.addBoat(newBoat);

        em.getTransaction().begin();
        em.merge(newBoat);
        em.merge(newHarbour);
        em.merge(oldHarbour);

        for (User owner : oldBoat.getOwners()) {
            owner.removeBoat(oldBoat);
            em.merge(owner);
        }
        for (User owner : owners) {
            owner.addBoat(newBoat);
            em.merge(newBoat);
        }
        em.getTransaction().commit();

        return newBoat;
    }

    public Boat deleteBoat(long id) {
        EntityManager em = emf.createEntityManager();

        Boat boat = em.find(Boat.class, id);

        em.getTransaction().begin();

        boat.getHarbour().removeBoat(boat);
        em.merge(boat.getHarbour());

        for (User owner: boat.getOwners()) {
            owner.removeBoat(boat);
            em.merge(owner);
        }

        em.remove(boat);

        em.getTransaction().commit();

        return boat;
    }
}
