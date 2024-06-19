package cz.cvut.fit.tjv.polakemi.semestral_work.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.polakemi.semestral_work.api.converter.TeamConverter;
import cz.cvut.fit.tjv.polakemi.semestral_work.api.converter.VehicleConverter;
import cz.cvut.fit.tjv.polakemi.semestral_work.business.TeamService;
import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Vehicle;
import cz.cvut.fit.tjv.polakemi.semestral_work.api.exception.InvalidBodyException;
import cz.cvut.fit.tjv.polakemi.semestral_work.api.exception.NoEntityFoundException;
import cz.cvut.fit.tjv.polakemi.semestral_work.business.EntityStateException;
import cz.cvut.fit.tjv.polakemi.semestral_work.business.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;

@RestController
public class VehicleController {
    private final VehicleService vehicleService;
    private final TeamService teamService;

    public VehicleController(VehicleService vehicleService, TeamService teamService) {
        this.vehicleService = vehicleService;
        this.teamService = teamService;
    }

    /**
     * Returns all vehicles saved in database
     * @return Collection of vehicle dtos
     */
    @JsonView(Views.Public.class)
    @GetMapping("/vehicles")
    public Collection<VehicleDto> all() {
        return VehicleConverter.fromModelAny(vehicleService.readAll());
    }

    @JsonView(Views.Internal.class)
    @GetMapping("/secured/vehicles")
    public Collection<VehicleDto> allSecured() {
        return VehicleConverter.fromModelAny(vehicleService.readAll());
    }

    /**
     * Create and add new vehicle to database
     * @param newVehicle Json body defining new vehicle's attributes
     * @return vehicle dto
     */
    @PostMapping("/vehicles")
    public VehicleDto newVehicle(@RequestBody VehicleDto newVehicle) {
        Vehicle vehicleModel = VehicleConverter.toModel(newVehicle);
        try {
            this.vehicleService.create(vehicleModel);
        }
        catch (EntityStateException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle ID (license plate) is not unique", exception);
        }
        vehicleModel = this.vehicleService.readById(vehicleModel.getIdVehicle()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found")
        );
        return VehicleConverter.fromModel(vehicleModel);
    }

    /**
     * Returns vehicle saved in database
     * @param id key of the vehicle
     * @return vehicle dto
     */
    @GetMapping("/vehicles/{id}")
    public VehicleDto one(@PathVariable Integer id) {
        return VehicleConverter.fromModel(
                vehicleService.readById(id).orElseThrow(NoEntityFoundException::new)
        );
    }

    /**
     * Mapping for updating attributes of entity Vehicle
     * Owner cannot be changed with this mapping
     * @param vehicleDto as Json body with parameters to change
     * @param id of entity Vehicle, which will be changed
     */
    @PutMapping("/vehicles/{id}")
    public void updateVehicle(@RequestBody VehicleDto vehicleDto, @PathVariable Integer id) {
        Vehicle v = vehicleService.readById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found")
        );
        Vehicle vehicle = VehicleConverter.toModel(vehicleDto);
        vehicle.setIdVehicle(id);
        vehicle.setOwner(v.getOwner());
        System.out.println(vehicle);
        try {
            this.vehicleService.update(vehicle);
        }
        catch (EntityStateException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle ID is not unique", exception);
        }
        catch (InvalidBodyException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong request body for entity Vehicle" +
                    "Only non-relational parameters can be changed here", exception);
        }
    }

    /**
     * Removes vehicle from the database
     * @param id key of the vehicle
     */
    @DeleteMapping("/vehicles/{id}")
    public void deleteVehicle(@PathVariable Integer id) {
        Optional<Vehicle> vehicleOptional = vehicleService.readById(id);
        if (vehicleOptional.isEmpty()) {
            return;
        }
        Vehicle vehicle = vehicleOptional.get();
        if (vehicle.getOwner() != null) {
            try {
                teamService.removeVehicle(vehicle.getOwner().getIdTeam(), vehicle);
            } catch (EntityStateException exception) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle ID is not unique", exception);
            }
        }
        vehicleService.deleteById(id);
    }

    /**
     * Returns team owning the vehicle
     * @param id key of the vehicle
     * @return team dto
     */
    @GetMapping("/vehicles/{id}/owner")
    public TeamDto getVehicleOwner(@PathVariable Integer id) {
        Vehicle vehicle = vehicleService.readById(id).orElseThrow(NoEntityFoundException::new);
        return TeamConverter.fromModel(vehicle.getOwner());
    }

}
