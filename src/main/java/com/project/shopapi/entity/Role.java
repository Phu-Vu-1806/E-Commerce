package com.project.shopapi.entity;

import com.project.shopapi.entity.enums.ERole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private ERole name;

    @OneToMany(mappedBy = "role")
    private List<User> users;

    public Role(ERole name) {
        this.name = name;
    }
}
