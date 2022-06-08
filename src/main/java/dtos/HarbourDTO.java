package dtos;

import entities.Boat;
import entities.Harbour;

import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public class HarbourDTO {
    private Long id;
    private String name;
    private String address;
    private int capacity;
    private List<String> boats;

    public HarbourDTO(Harbour harbour) {
        this.id = harbour.getId();
        this.name = harbour.getName();
        this.address = harbour.getAddress();
        this.capacity = harbour.getCapacity();
        this.boats = getBoats(harbour.getBoats());
    }

    public List<String> getBoats(List<Boat> boats){
        List<String> stringBoats = new ArrayList<>();
        for (Boat boat : boats)
        {
            stringBoats.add(boat.getName());
        }
        return stringBoats;
    }

}
