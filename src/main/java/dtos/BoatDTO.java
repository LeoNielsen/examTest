/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import entities.Boat;
import entities.Role;
import entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tha
 */
public class BoatDTO {
    private long id;
    private String brand;
    private String make;
    private String name;
    private String image;
    private List<String> owners;
    private String harbour;

    public BoatDTO(Boat boat) {
        if (boat.getId() != null)
            this.id = boat.getId();
        this.brand = boat.getBrand();
        this.make = boat.getMake();
        this.name = boat.getName();
        this.image = boat.getImage();
        this.owners = getOwners(boat.getOwners());
        this.harbour = boat.getHarbour().getName();
    }

    public List<String> getOwners(List<User> owners) {
        List<String> ownerNames = new ArrayList<>();
        for (User owner : owners) {
            ownerNames.add(owner.getName());
        }
        return ownerNames;
    }

    public static List<BoatDTO> getDtos(List<Boat> boats) {
        List<BoatDTO> boatDTOS = new ArrayList();
        boats.forEach(boat -> boatDTOS.add(new BoatDTO(boat)));
        return boatDTOS;
    }
}
