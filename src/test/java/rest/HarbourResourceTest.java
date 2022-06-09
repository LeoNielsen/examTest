//package rest;
//
//import entities.Boat;
//import entities.Harbour;
//import io.restassured.RestAssured;
//import io.restassured.parsing.Parser;
//import org.glassfish.grizzly.http.server.HttpServer;
//import org.glassfish.grizzly.http.util.HttpStatus;
//import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
//import org.glassfish.jersey.server.ResourceConfig;
//import org.junit.jupiter.api.*;
//import utils.EMF_Creator;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.ws.rs.core.UriBuilder;
//import java.net.URI;
//import java.util.ArrayList;
//
//import static io.restassured.RestAssured.given;
//import static org.hamcrest.Matchers.*;
//
//class HarbourResourceTest {
//
//    private static final int SERVER_PORT = 7777;
//    private static final String SERVER_URL = "http://localhost/api";
//    private static Boat boat, boat1;
//    private static Harbour harbour;
//
//    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
//    private static HttpServer httpServer;
//    private static EntityManagerFactory emf;
//
//    static HttpServer startServer() {
//        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
//        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
//    }
//
//    @BeforeAll
//    public static void setUpClass() {
//        //This method must be called before you request the EntityManagerFactory
//        EMF_Creator.startREST_TestWithDB();
//        emf = EMF_Creator.createEntityManagerFactoryForTest();
//
//        httpServer = startServer();
//        //Setup RestAssured
//        RestAssured.baseURI = SERVER_URL;
//        RestAssured.port = SERVER_PORT;
//        RestAssured.defaultParser = Parser.JSON;
//    }
//
//    @AfterAll
//    public static void closeTestServer() {
//        //System.in.read();
//
//        //Don't forget this, if you called its counterpart in @BeforeAll
//        EMF_Creator.endREST_TestWithDB();
//        httpServer.shutdownNow();
//    }
//
//    @BeforeEach
//    void setUp() {
//        EntityManager em = emf.createEntityManager();
//        harbour = new Harbour("hamborg", "hamborg vej 1", 150, new ArrayList<>());
//        boat = new Boat("Some txt", "More text", "name", "image", new ArrayList<>(), harbour);
//        boat1 = new Boat("aaa", "bbb", "ccc", "ddd", new ArrayList<>(), harbour);
//
//        harbour.addBoat(boat);
//        harbour.addBoat(boat1);
//        harbour.setId(1L);
//        try {
//            em.getTransaction().begin();
//            em.createNamedQuery("Boat.deleteAllRows").executeUpdate();
//            em.createNamedQuery("Harbour.deleteAllRows").executeUpdate();
//            em.createNamedQuery("User.deleteAllRows").executeUpdate();
//            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
//            em.persist(boat);
//            em.persist(boat1);
//            em.persist(harbour);
//            em.getTransaction().commit();
//        } finally {
//            em.close();
//        }
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    void getBoats() {
//        given()
//                .contentType("application/json")
//                .when()
//                .get("/harbour/{id}/allboats", 1).then()
//                .assertThat()
//                .statusCode(HttpStatus.OK_200.getStatusCode())
//                .body("name" , hasItems("ccc", "name"));
//    }
//
//
//}