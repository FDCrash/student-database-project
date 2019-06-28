package com.netcracker.denisik.entities;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseEntity {

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "password")
    @ColumnDefault("")
    private String password;

    @Column(name = "login", unique = true)
    @ColumnDefault("")
    private String login;

    @Column(name = "name")
    private String name;

    @Builder
    public User(long id, Role role, String password, String login, String name) {
        super(id);
        this.role = role;
        this.password = password;
        this.login = login;
        this.name = name;
    }
}
