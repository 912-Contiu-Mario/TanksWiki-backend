package dev.tankswikibackend.Entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString

@Table(name = "tanks")
public class Tank {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @Column(name = "user_idd")
    private Long userId;
    private String tankName;
    private String tankCountry;
    private String tankType;
    private Integer tankYear;
    private Integer tankFirepower;
    private Integer tankSpeed;

    public Tank(long l, String s, String sovietUnion, String heavyTank, int i, int i1, int i2) {
    }
}

