package utils;


import java.util.List;
import edu.emory.mathcs.backport.java.util.Arrays;
import entities.Boat;
import entities.Harbour;
import entities.Role;
import entities.User;
import facades.UserFacade;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;

public class SetupTestUsers {

  public static void main(String[] args) {

    EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
    EntityManager em = emf.createEntityManager();
    
    // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
    // CHANGE the three passwords below, before you uncomment and execute the code below
    // Also, either delete this file, when users are created or rename and add to .gitignore
    // Whatever you do DO NOT COMMIT and PUSH with the real passwords

    User user = new User("user", "test123");
    User admin = new User("admin", "test123");
    User both = new User("user_admin", "test123");

    Harbour harbour = new Harbour("Hamborg Havn", "hamborgvej 2", 150, new ArrayList<>());

    Boat boat = new Boat("Bertram", "2011", "ludvig", "image", List.of(admin), harbour);
    Boat boat1 = new Boat("Chaparral", "2000", "Elisabeth", "image", List.of(both), harbour);

    harbour.addBoat(boat);
    harbour.addBoat(boat1);
    admin.addBoat(boat);
    both.addBoat(boat1);


    if(admin.getUserPass().equals("test")||user.getUserPass().equals("test")||both.getUserPass().equals("test"))
    {
      throw new UnsupportedOperationException("You have not changed the passwords");
    }

    em.getTransaction().begin();
    Role userRole = new Role("user");
    Role adminRole = new Role("admin");
//    user.addRole(userRole);
    admin.addRole(adminRole);
    both.addRole(userRole);
    both.addRole(adminRole);
    em.persist(userRole);
    em.persist(adminRole);
//    em.persist(user);
    em.persist(admin);
    em.persist(both);

    em.persist(harbour);
    em.persist(boat);
    em.persist(boat1);

    em.getTransaction().commit();
    UserFacade.getUserFacade(emf).registerNewUser(user);
    System.out.println("PW: " + user.getUserPass());
    System.out.println("Testing user with OK password: " + user.verifyPassword("test123"));
    System.out.println("Testing user with wrong password: " + user.verifyPassword("test1"));
    System.out.println("Created TEST Users");
   
  }

}
