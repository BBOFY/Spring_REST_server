package cz.cvut.fit.tjv.polakemi.semestral_work.api.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Team;

import java.util.Objects;

public class VehicleDto {

    @JsonView(Views.Public.class)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    public Integer idVehicle;

    @JsonView(Views.Public.class)
    public String licensePlate;

    @JsonView(Views.Public.class)
    public String vehicleName;

    @JsonView(Views.Public.class)
    public String vehicleType;

    @JsonView(Views.Public.class)
    public String nickname;

    @JsonView(Views.Public.class)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    public Integer ownerId;

    @JsonIgnore
    public Team owner;

    public VehicleDto () {}

    public VehicleDto(Integer idVehicle, String licensePlate, String name, String type, String nickname, Team owner) {
        this.idVehicle = idVehicle;
        this.licensePlate = licensePlate;
        this.vehicleName = name;
        this.vehicleType = type;
        this.nickname = nickname;
        this.owner = owner;
        if (Objects.nonNull(owner)) {
            this.ownerId = owner.getIdTeam();
        }
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

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Team getOwner() {
        return owner;
    }

    public void setOwner(Team owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "VehicleDto{" +
                "licensePlate='" + licensePlate + '\'' +
                ", name='" + vehicleName + '\'' +
                ", type='" + vehicleType + '\'' +
                ", nickname='" + nickname + '\'' +
                ", owner=" + owner +
                '}';
    }
}
