package dtos;

import entities.Boat;
import entities.Role;
import entities.User;

import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

public class UserDTO
{
    String userName;
    List<String> roles;
    String userPass;
    private String name;
    private String address;
    private String phone;
    private List<String> boats = new ArrayList<>();

    public UserDTO(User user)
    {
        this.userName = user.getUserName();
        this.userPass = user.getUserPass();
        this.roles = getRoles(user.getRoleList());
        this.name = user.getName();
        this.address = user.getAddress();
        this.phone = user.getPhone();
        this.boats = getBoats(user.getBoats());
    }

    public User toUser () {
        return new User(this.userName, this.userPass);
    }

    public List<String> getRoles(List<Role> roles){
        List<String> stringRoles = new ArrayList<>();
        for (Role role : roles)
        {
            stringRoles.add(role.getRoleName());
        }
        return stringRoles;
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
