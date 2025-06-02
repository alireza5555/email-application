package aut.ap.model;

import jakarta.persistence.*;


@Entity
@Table(name = "user")
public class User {
    @Id
            @GeneratedValue (strategy = GenerationType.IDENTITY)
    int id;

    String name;
    String lastName;
    int Age;
    String email;
    String password;

    public User(String email, int age, String lastName, String name, String password) {
        this.email = email;
        Age = age;
        this.lastName = lastName;
        this.name = name;
        this.password = password;
    }

    public User(){};
}
