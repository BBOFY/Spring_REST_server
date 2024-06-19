package cz.cvut.fit.tjv.polakemi.semestral_work.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "tjv_vehicle")
public class Vehicle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int idVehicle;

    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Column(name = "vehicle_name", nullable = false)
    private String vehicleName;

    private String type;

    private String nickname;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_team_id", nullable = true)
    private Team owner;

    public Vehicle() {}

    public Vehicle(String licensePlate, String vehicleName, String type) {
        this.licensePlate = Objects.requireNonNull(licensePlate);
        this.vehicleName = Objects.requireNonNull(vehicleName);
        this.type = Objects.requireNonNull(type);
        this.nickname = null;
        this.owner = null;
    }

    public Vehicle(String licensePlate, String vehicleName, String type, String nickname, Team owner) {
        this.licensePlate = Objects.requireNonNull(licensePlate);
        this.vehicleName = Objects.requireNonNull(vehicleName);
        this.type = Objects.requireNonNull(type);
        this.nickname = nickname;
        this.owner = owner;
    }

    public Vehicle(Integer id, String licensePlate, String vehicleName, String type, String nickname, Team owner) {
        this.idVehicle = id;
        this.licensePlate = Objects.requireNonNull(licensePlate);
        this.vehicleName = Objects.requireNonNull(vehicleName);
        this.type = Objects.requireNonNull(type);
        this.nickname = nickname;
        this.owner = owner;
    }

    public Integer getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(Integer idVehicle) {
        this.idVehicle = idVehicle;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String name) {
        this.vehicleName = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Team getOwner() {
        return owner;
    }

    public void setOwner(Team owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vehicle vehicle = (Vehicle) o;

        return licensePlate.equals(vehicle.licensePlate);
    }

    @Override
    public int hashCode() {
        return licensePlate.hashCode();
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "licensePlate='" + licensePlate + '\'' +
                ", name='" + vehicleName + '\'' +
                ", type='" + type + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
