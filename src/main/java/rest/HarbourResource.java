package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.BoatDTO;
import dtos.HarbourDTO;
import entities.Boat;
import entities.Harbour;
import entities.User;
import facades.BoatFacade;
import facades.HarbourFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("harbour")
public class HarbourResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final HarbourFacade FACADE = HarbourFacade.getFacadeExample(EMF);
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

    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{hid}/addboat/{bid}")
    public String addBoat(@PathParam("hid") long harbourId, @PathParam("bid") long boatId) {
        Boat boat = FACADE.addBoatToHarbour(harbourId, boatId);
        BoatDTO boatDTO = new BoatDTO(boat);

        return GSON.toJson(boatDTO);
    }


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/all")
    public String allHarbours() {
        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<Harbour> query = em.createQuery("select h from Harbour h", Harbour.class);
            List<Harbour> harbours = query.getResultList();
            List<HarbourDTO> harbourDTOS = HarbourDTO.getDtos(harbours);
            return GSON.toJson(harbourDTOS);
        } finally {
            em.close();
        }
    }

}
