package com.kani.hotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users = new HashSet<>();

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public void assignRoleToUser(User user){
        user.getRoles().add(this);
        this.getUsers().add(user);
    }

    public void removeUserFromRole(User user){
        user.getRoles().remove(this);
        this.getUsers().remove(user);
    }


    public void removeAllUsersFromRole(){
       if(this.getUsers() != null){
           List<User> roleUsers = this.getUsers().stream().toList();
           roleUsers.forEach(this::removeUserFromRole);
       }
    }

    public String getRoleName(){
        return roleName != null ? roleName : "";
    }
}
