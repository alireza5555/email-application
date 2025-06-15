package aut.ap.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "code")
public class Code {
    @Id
    String current_code;
}
