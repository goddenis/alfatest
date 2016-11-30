package com.alfatest.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "usrs")
public class User implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Size(min = 4, max = 20)
    private String login;
    @Size(min = 4, max = 10)
    private String password;

    private Boolean active;

    public User() {
    }

    public User(Long id, String login, String password, Boolean active){

        this.id = id;
        this.login = login;
        this.password = password;
        this.active = active;
    }


}
