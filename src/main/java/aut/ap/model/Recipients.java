package aut.ap.model;


import jakarta.persistence.*;

@Entity
@Table(name = "recipients")
public class Recipients {
    public enum Status {
        READ,
        UNREAD
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;
    private String email;
    @Enumerated(EnumType.STRING)
    private Status status;


    public Recipients(){};

    public Recipients(String code, String email ){
        this.code = code;
        this.email = email;
        status = Status.UNREAD;
    }

    public String getCode() {
        return code;
    }

    public String getEmail() {
        return email;
    }


    public void setCode(String code) {
        this.code = code;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
