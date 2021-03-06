/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import entities.Boat;
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
    private long harbourID;

    public BoatDTO(Boat boat) {
        if (boat.getId() != null)
            this.id = boat.getId();
        this.brand = boat.getBrand();
        this.make = boat.getMake();
        this.name = boat.getName();
        this.image = boat.getImage();
        this.owners = getOwners(boat.getOwners());
        this.harbourID = boat.getHarbour().getId();
    }

    public List<String> getOwners(List<User> owners) {
        List<String> ownerNames = new ArrayList<>();
        for (User owner : owners) {
            ownerNames.add(owner.getUserName());
        }
        return ownerNames;
    }

    public static List<BoatDTO> getDtos(List<Boat> boats) {
        List<BoatDTO> boatDTOS = new ArrayList();
        boats.forEach(boat -> boatDTOS.add(new BoatDTO(boat)));
        return boatDTOS;
    }

    public long getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getMake() {
        return make;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public List<String> getOwners() {
        return owners;
    }

    public long getHarbourID() {
        return harbourID;
    }
}
