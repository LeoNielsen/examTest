package facades;

import entities.Boat;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class HarbourFacade {

    private static HarbourFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private HarbourFacade() {}


    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static HarbourFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new HarbourFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }


    public List<Boat> getAllBoats(long id) {
        EntityManager em = emf.createEntityManager();

        List<Boat> boats = new ArrayList<>();

        TypedQuery<Boat> query = em.createQuery("SELECT b FROM Boat b WHERE b.harbour.id = :id", Boat.class);
        query.setParameter("id", id);

        return query.getResultList();
    }
}
