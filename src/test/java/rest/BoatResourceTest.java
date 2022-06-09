package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.BoatDTO;
import entities.Boat;
import entities.Harbour;
import entities.User;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;

import io.restassured.parsing.Parser;

import java.util.List;
import java.net.URI;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
//Uncomment the line below, to temporarily disable this test
//@Disabled

public class BoatResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Boat boat, boat1;
    private static Harbour harbour;
    private static User user;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {

        EntityManager em = emf.createEntityManager();
        boat = new Boat("Some txt", "More text", "name", "image", new ArrayList<>(), null);
        boat1 = new Boat("aaa", "bbb", "ccc", "image", new ArrayList<>(), null);

        user = new User("Henny", "test123", new ArrayList<>());
        user.addBoat(boat);
        boat.getOwners().add(user);
        harbour = new Harbour("h", "h", 150, new ArrayList<>());

        try {
            em.getTransaction().begin();
            em.createNamedQuery("Harbour.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            em.createNamedQuery("Boat.deleteAllRows").executeUpdate();

            em.persist(boat);
            em.persist(user);
            em.persist(harbour);

            em.getTransaction().commit();


        } finally {
            em.close();
        }
    }

    @AfterEach
    void tearDown() {
        EntityManager em = emf.createEntityManager();
        em.createNativeQuery("DROP TABLE *");
    }

    //This test assumes the database contains two rows
    @Test
    public void testDummyMsg() throws Exception {
        given()
                .contentType("application/json")
                .get("/boat/").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("msg", equalTo("Hello World from boats"));
    }

    @Test
    void getAllOwnersByID() {
        given()
                .contentType("application/json")
                .get("/boat/{id}/allowners", boat.getId()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("userName", hasItem("Henny"));
    }

    @Test
    void createBoat() {
        List<User> owner = new ArrayList<>();
        owner.add(user);
        Boat boat = new Boat("hammer", "2000","Lis","image",owner,harbour);
        user.addBoat(boat);
        BoatDTO boatDTO = new BoatDTO(boat);


        given()
                .contentType("application/json")
                .and()
                .body(GSON.toJson(boatDTO))
                .when()
                .post("/boat/createboat")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("name", equalTo("Lis"));


    }
}
