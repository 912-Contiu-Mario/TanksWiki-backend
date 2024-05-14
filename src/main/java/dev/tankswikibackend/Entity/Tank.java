package dev.tankswikibackend.Entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
@Table(name = "Tanks")
public class Tank {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    private String tankName;
    private String tankCountry;
    private String tankType;
    private Integer tankYear;
    private Integer tankFirepower;
    private Integer tankSpeed;
}

