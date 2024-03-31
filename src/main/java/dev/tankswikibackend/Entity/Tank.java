package dev.tankswikibackend.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class Tank {
    private @Id @GeneratedValue Long id;
    private String tankName;
    private String tankCountry;
    private String tankType;
    private Integer tankYear;
    private Integer tankFirepower;
    private Integer tankSpeed;
}
