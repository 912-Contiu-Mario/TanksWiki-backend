package dev.tankswikibackend.Entity;


import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Entity @Getter  @Setter
@Table(name = "Modules")
public class Module
{

    public Module(Long tankId, String name, String type, Integer weight, Integer horsepower){
        this.tankId = tankId;
        this.moduleName = name;
        this.moduleType = type;
        this. moduleWeight = weight;
        this.moduleHorsepower = horsepower;
    }

    public Module(Long tankId,String name, Integer loadLimit, String type, Integer weight){
        this.tankId = tankId;
        this.moduleName = name;
        this.moduleType = type;
        this. moduleWeight = weight;
        this.moduleLoadLimit = loadLimit;
    }

    public Module(Long tankId,String name, String type, Integer weight, Integer damage, Integer rateOfFire, Integer penetration){
        this.tankId = tankId;
        this.moduleName = name;
        this.moduleType = type;
        this. moduleWeight = weight;
        this.moduleDamage = damage;
        this.modulePenetration = penetration;
        this.moduleRateOfFire = rateOfFire;
    }

    public Module(Long tankId,String name,  Integer weight, Integer signalRange, String type){
        this.tankId = tankId;
        this.moduleName = name;
        this.moduleType = type;
        this. moduleWeight = weight;
        this.moduleSignalRange = signalRange;
    }
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    private String moduleName;
    private String moduleType;
    private Long tankId;


    private Integer moduleWeight;


    @Nullable

    private Integer moduleHorsepower;
    @Nullable

    private Integer moduleLoadLimit;
    @Nullable

    private Integer moduleDamage;
    @Nullable


    private Integer moduleRateOfFire;
    @Nullable


    private Integer modulePenetration;

    @Nullable

    private Integer moduleSignalRange;
}
