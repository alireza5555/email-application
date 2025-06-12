package aut.ap.model;

import jakarta.persistence.*;


@Entity
@Table(name = "user")
public class User {
    @Id
            @GeneratedValue (strategy = GenerationType.IDENTITY)
    int id;

    private String name;
    private String lastName;
    private int age;
    private String email;
    private String password;

    public User(String email, int age, String lastName, String name, String password) {
        this.email = email;
        this.age = age;
        this.lastName = lastName;
        this.name = name;
        this.password = password;
    }

    public User(){};

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
