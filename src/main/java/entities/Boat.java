package entities;

import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;
import javax.persistence.*;


@Entity
@NamedQuery(name = "Boat.deleteAllRows", query = "DELETE from Boat")
public class Boat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String brand;
    private String make;
    private String name;
    private String image;
    @ManyToMany (mappedBy = "boats")
    private List<User> owners = new ArrayList<>();
    @ManyToOne
    private Harbour harbour;

    public Boat() {
    }

    public Boat(String brand, String make, String name, String image, List<User> owners, Harbour harbour) {
        this.brand = brand;
        this.make = make;
        this.name = name;
        this.image = image;
        this.owners = owners;
        this.harbour = harbour;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<User> getOwners() {
        return owners;
    }

    public void setOwners(List<User> owners) {
        this.owners = owners;
    }

    public Harbour getHarbour() {
        return harbour;
    }

    public void setHarbour(Harbour harbour) {
        this.harbour = harbour;
    }
}