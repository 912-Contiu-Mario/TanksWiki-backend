package dev.tankswikibackend.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Tank {
    private @Id @GeneratedValue Long id;
    private String tankName;
    private String tankCountry;
    private String tankType;
    private Integer tankYear;
    private Integer tankFirepower;
    private Integer tankSpeed;
}
