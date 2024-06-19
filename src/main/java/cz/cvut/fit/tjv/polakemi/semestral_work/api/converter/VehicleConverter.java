package cz.cvut.fit.tjv.polakemi.semestral_work.api.converter;

import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Vehicle;
import cz.cvut.fit.tjv.polakemi.semestral_work.api.controller.VehicleDto;
import cz.cvut.fit.tjv.polakemi.semestral_work.api.exception.InvalidBodyException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class VehicleConverter {

    public static Vehicle toModel(VehicleDto vehicleDto) throws InvalidBodyException {
        if (Objects.isNull(vehicleDto.licensePlate)
         || Objects.isNull(vehicleDto.vehicleName)) {
            throw new InvalidBodyException();
        }
        return new Vehicle(vehicleDto.licensePlate, vehicleDto.vehicleName, vehicleDto.vehicleType, vehicleDto.nickname, vehicleDto.owner);
    }

    public static VehicleDto fromModel(Vehicle vehicle) {
        return new VehicleDto(vehicle.getIdVehicle(), vehicle.getLicensePlate(), vehicle.getVehicleName(), vehicle.getType(), vehicle.getNickname(), vehicle.getOwner());
    }

    public static Collection<Vehicle> toModelAny(Collection<VehicleDto> vehicleDtos) {
        Collection<Vehicle> vehicles = new ArrayList<>();
        vehicleDtos.forEach((v) -> vehicles.add(toModel(v)));
        return vehicles;
    }

    public static Collection<VehicleDto> fromModelAny(Collection<Vehicle> vehicles) {
        Collection<VehicleDto> vehicleDtos = new ArrayList<>();
        vehicles.forEach((v) -> vehicleDtos.add(fromModel(v)));
        return vehicleDtos;
    }
}
