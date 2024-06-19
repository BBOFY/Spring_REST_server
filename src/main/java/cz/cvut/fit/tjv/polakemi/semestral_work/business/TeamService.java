package cz.cvut.fit.tjv.polakemi.semestral_work.business;

import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Vehicle;
import cz.cvut.fit.tjv.polakemi.semestral_work.dao.TeamJpaRepository;
import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Sponsor;
import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Team;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;


@Component
@Transactional
public class TeamService extends AbstractCrudService<Integer, Team, TeamJpaRepository> {
    private final VehicleService vehicleService;

    public TeamService(TeamJpaRepository teamDbRepository, VehicleService service) {
        super(teamDbRepository);
        this.vehicleService = service;
    }

    public void addSponsor(Integer idTeam, Sponsor sponsor) throws EntityStateException {
        Team team = repository.getById(idTeam);
        if (team.getTeamSponsors().contains(sponsor)) {
            return;
        }
        team.addSponsor(sponsor);
        update(team);
    }

    public void removeSponsor(Integer idTeam, Sponsor sponsor) throws EntityStateException {
        Team team = repository.getById(idTeam);
        if (!team.getTeamSponsors().contains(sponsor)) {
            return;
        }
        team.removeSponsor(sponsor);
        update(team);
    }

    public void addVehicle(Integer idTeam, Vehicle vehicle) throws EntityStateException {
        Team team = repository.getById(idTeam);
        if (team.getTeamVehicles().contains(vehicle)) {
            return;
        }
        Team oldOwner = vehicle.getOwner();
        if (!Objects.isNull(oldOwner)) {
            removeVehicle(oldOwner.getIdTeam(), vehicle);
        }
        team.addVehicle(vehicle);
        vehicle.setOwner(team);
        update(team);
        vehicleService.update(vehicle);
    }

    public void updateVehicle(Integer idTeam, Vehicle vehicle) throws EntityStateException {
        Team team = repository.getById(idTeam);
        Team oldOwner = vehicle.getOwner();
        if (!Objects.isNull(oldOwner)) {
            removeVehicle(oldOwner.getIdTeam(), vehicle);
        }

        Optional<Vehicle> vehicleOptional = vehicleService.readById(vehicle.getIdVehicle());
        team.addVehicle(vehicle);
        vehicle.setOwner(team);
        update(team);
        vehicleService.update(vehicle);
    }

    public void removeVehicle(Integer idTeam, Vehicle vehicle) throws EntityStateException {
        Team team = repository.getById(idTeam);
        if (!team.getTeamVehicles().contains(vehicle)) {
            return;
        }
        team.removeVehicle(vehicle);
        vehicle.setOwner(null);
        update(team);
        vehicleService.update(vehicle);
    }

    public Collection<Team> getTeamsWhereSponsorIn(String industryType) {
        Collection<Integer> teamsIds = repository.findAllTeamsIdsFromTeamsThatHaveAnySponsorInIndustryTypeNative(industryType);
        Collection<Team> teams = new HashSet<>();
        teamsIds.forEach(
                (id) -> teams.add(repository.getById(id))
        );
        return teams;
    }

    @Override
    public boolean exists(Team entity) { return repository.existsById(entity.getIdTeam()); }

}

