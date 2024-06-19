package cz.cvut.fit.tjv.polakemi.semestral_work.business;

import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Team;
import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Vehicle;
import cz.cvut.fit.tjv.polakemi.semestral_work.dao.VehicleJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class VehicleService extends AbstractCrudService<Integer, Vehicle, VehicleJpaRepository> {

    public VehicleService(VehicleJpaRepository vehicleDbRepository) {
        super(vehicleDbRepository);
    }

    @Override
    public boolean exists(Vehicle entity) { return repository.existsById(entity.getIdVehicle()); }

    public void setOwner(Integer idVehicle, Team team) throws EntityStateException {
        Vehicle vehicle = repository.getById(idVehicle);
        if (vehicle.getOwner().equals(team)) {
            return;
        }
        vehicle.setOwner(team);
        if (team != null) {
            team.addVehicle(vehicle);
        }
        update(vehicle);
    }

    public String getOwnerId(Integer idVehicle) {
        Vehicle vehicle = repository.getById(idVehicle);
        return vehicle.getOwner().getTeamName();
    }
}
