package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.BoatDTO;
import dtos.UserDTO;
import entities.Boat;
import entities.User;
import utils.EMF_Creator;
import facades.BoatFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.awt.*;
import java.lang.reflect.Type;
import java.util.List;

@Path("boat")
public class BoatResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
       
    private static final BoatFacade FACADE =  BoatFacade.getFacadeExample(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World from boats\"}";
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{id}/allowners")
    public String getAllOwnersByID(@PathParam("id") long id){
        List<User> owners = FACADE.getAllOwnersByID(id);
        List<UserDTO> userDTOList = UserDTO.getUsers(owners);
        return GSON.toJson(userDTOList);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/createboat")
    public String createBoat(String data){
        BoatDTO boatDTO = GSON.fromJson(data, BoatDTO.class);
        Boat boat = FACADE.createBoat(boatDTO);
        return GSON.toJson(new BoatDTO(boat));
    }

}
