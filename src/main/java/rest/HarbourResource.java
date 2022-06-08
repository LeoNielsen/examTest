package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.BoatDTO;
import entities.Boat;
import facades.BoatFacade;
import facades.HarbourFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("harbour")
public class HarbourResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final HarbourFacade FACADE =  HarbourFacade.getFacadeExample(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World from harbour\"}";
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("{id}/allboats")
    public String getBoats(@PathParam("id") long id) {
        List<Boat> boatList = FACADE.getAllBoats(id);
        List<BoatDTO> boatDTOS = BoatDTO.getDtos(boatList);

        return GSON.toJson(boatDTOS);
    }

}
