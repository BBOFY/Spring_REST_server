package cz.cvut.fit.tjv.polakemi.semestral_work.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.polakemi.semestral_work.api.converter.SponsorConverter;
import cz.cvut.fit.tjv.polakemi.semestral_work.api.converter.TeamConverter;
import cz.cvut.fit.tjv.polakemi.semestral_work.api.converter.VehicleConverter;
import cz.cvut.fit.tjv.polakemi.semestral_work.api.exception.NoEntityFoundException;
import cz.cvut.fit.tjv.polakemi.semestral_work.business.SponsorService;
import cz.cvut.fit.tjv.polakemi.semestral_work.business.TeamService;
import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Sponsor;
import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Team;
import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Vehicle;
import cz.cvut.fit.tjv.polakemi.semestral_work.business.EntityStateException;
import cz.cvut.fit.tjv.polakemi.semestral_work.business.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;

@RestController
public class TeamController {

    private final TeamService teamService;
    private final SponsorService sponsorService;
    private final VehicleService vehicleService;

    public TeamController(TeamService teamService, SponsorService sponsorService, VehicleService vehicleService) {
        this.teamService = teamService;
        this.sponsorService = sponsorService;
        this.vehicleService = vehicleService;
    }

    /**
     * Returns all teams saved in database
     * @return Collection of team dtos
     */
    @JsonView(Views.Public.class)
    @GetMapping("/teams")
    public Collection<TeamDto> all () {
        return TeamConverter.fromModelAny(teamService.readAll());
    }

    @JsonView(Views.Internal.class)
    @GetMapping("/secured/teams")
    public Collection<TeamDto> allSecured () {
        return TeamConverter.fromModelAny(teamService.readAll());
    }

    /**
     * Create and add new team to database
     * @param newTeam Json body defining new team's attributes
     * @return team dto
     */
    @PostMapping("/teams")
    public TeamDto newTeam(@RequestBody TeamDto newTeam) {
        Team teamModel = TeamConverter.toModel(newTeam);
        try {
            this.teamService.create(teamModel);
        }
        catch (EntityStateException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team ID (teamName) is not unique", exception);
        }
//        teamModel = this.teamService.readById(teamModel.getIdTeam()).orElseThrow(
//                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found")
//        );
        return TeamConverter.fromModel(teamModel);
    }

    /**
     * Returns team saved in database
     * @param id key of the team
     * @return team dto
     */
    @GetMapping("/teams/{id}")
    public TeamDto one(@PathVariable Integer id) {
        return TeamConverter.fromModel(
                teamService.readById(id).orElseThrow(NoEntityFoundException::new)
        );
    }

    /**
     * Mapping for updating attributes of entity Team
     * Collection type attributes (other entities in relations with entity Team) will not be changed
     * These attributes must be changed by other mappings
     * @param teamDto as Json body with parameters to change
     * @param id of entity Team, which will be changed
     */
    @PutMapping("/teams/{id}")
    public TeamDto updateTeam(@RequestBody TeamDto teamDto, @PathVariable Integer id) {
        Team t = teamService.readById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Team Not Found")
                );
        Team team = TeamConverter.toModel(teamDto);
        team.setIdTeam(id);
        team.setTeamSponsors(t.getTeamSponsors());
        team.setTeamVehicles(t.getTeamVehicles());

        try {
            this.teamService.update(team);
        }
        catch (EntityStateException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team ID is not unique", exception);
        }
        return teamDto;
    }

    /**
     * Removes team from the database
     * @param id key of the team
     */
    @DeleteMapping("/teams/{id}")
    public void deleteTeam(@PathVariable Integer id) {
        Optional<Team> teamOptional = teamService.readById(id);
        if (teamOptional.isEmpty()) {
            return;
        }
        Team team = teamOptional.get();

        if (team.getTeamSponsors() != null)
            team.getTeamSponsors().forEach(
                    (s) -> {
                        try {
                            sponsorService.removeTeam(s.getIdSponsor(), team);
                        } catch (EntityStateException exception) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sponsor ID is not unique", exception);
                        }
                    });

        if (team.getTeamVehicles() != null)
            team.getTeamVehicles().forEach(
                    (v) -> {
                        try {
                            vehicleService.setOwner(v.getIdVehicle(), null);
                        } catch (EntityStateException exception) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle ID is not unique", exception);
                        }
                    });
        teamService.deleteById(id);
    }

    /**
     * Returns all vehicles owned by team
     * @param id key of the team
     * @return Collection of vehicle dtos
     */
    @GetMapping("/teams/{id}/vehicles")
    public Collection<VehicleDto> getTeamVehicles(@PathVariable Integer id) {
        return VehicleConverter.fromModelAny(
                teamService.readById(id).orElseThrow(NoEntityFoundException::new).getTeamVehicles()
        );
    }

    /**
     * Returns all sponsors that sponsor team
     * @param id key of the team
     * @return Collection of sponsor dtos
     */
    @GetMapping("/teams/{id}/sponsors")
    public Collection<SponsorDto> getTeamSponsors(@PathVariable Integer id) {
        return SponsorConverter.fromModelAny(
                teamService.readById(id).orElseThrow(NoEntityFoundException::new).getTeamSponsors()
        );
    }

    /**
     * Creates relation between team and vehicle, i.e. assigns vehicle to team's ownership
     * @param id_t key of the team
     * @param id_v key of the vehicle
     */
    @PutMapping("/teams/{id_t}/vehicles/{id_v}")
    public void addVehicle(@PathVariable Integer id_t, @PathVariable Integer id_v) {
        teamService.readById(id_t).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found")
        );

        Vehicle vehicle = vehicleService.readById(id_v).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found")
        );
        try {
            teamService.addVehicle(id_t, vehicle);
        } catch (EntityStateException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle ID is not unique", exception);
        }
    }

    /**
     * Removes relation between team and vehicle, i.e. remove vehicle from team's ownership
     * @param id_t key of the team
     * @param id_v key of the vehicle
     * @return team dto
     */
    @DeleteMapping("/teams/{id_t}/vehicles/{id_v}")
    public TeamDto removeVehicle(@PathVariable Integer id_t, @PathVariable Integer id_v) {
        Team team = teamService.readById(id_t).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found")
        );
        Vehicle vehicle = vehicleService.readById(id_v).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found")
        );
        try {
            teamService.removeVehicle(id_t, vehicle);
        } catch (EntityStateException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle ID is not unique", exception);
        }
        return TeamConverter.fromModel(team);
    }

    /**
     * Creates relation between team and sponsor, i.e. assigns sponsor to team
     * @param id_t key of the team
     * @param id_s key of the sponsor
     */
    @PutMapping("/teams/{id_t}/sponsors/{id_s}")
    public void addSponsor(@PathVariable Integer id_t, @PathVariable Integer id_s) {
        Team team = teamService.readById(id_t).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found")
        );
        Sponsor sponsor = sponsorService.readById(id_s).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sponsor not found")
        );
        try {
            teamService.addSponsor(id_t, sponsor);
        } catch (EntityStateException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle ID is not unique", exception);
        }
        try {
            sponsorService.addTeam(id_s, team);
        } catch (EntityStateException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sponsor ID is not unique", exception);
        }
    }

    /**
     * Removes relation between team and sponsor
     * @param id_t key of the team
     * @param id_s key of the sponsor
     */
    @DeleteMapping("/teams/{id_t}/sponsors/{id_s}")
    public void removeSponsor(@PathVariable Integer id_t, @PathVariable Integer id_s) {
        Team team = teamService.readById(id_t).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found")
        );
        Sponsor sponsor = sponsorService.readById(id_s).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sponsor not found")
        );
        try {
            teamService.removeSponsor(id_t, sponsor);
        } catch (EntityStateException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle ID is not unique", exception);
        }
        try {
            sponsorService.removeTeam(id_s, team);
        } catch (EntityStateException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sponsor ID is not unique", exception);
        }
    }

    /**
     * Returns all teams, which sponsors are in industryType industry
     * @param industryType name of industry the sponsors are in
     * @return Collection of team dtos
     */
    @GetMapping("/teams_have_any_sponsor_where_industry={industryType}")
    public Collection<TeamDto> teamsWhereSponsorIn(@PathVariable String industryType) {
        return TeamConverter.fromModelAny(teamService.getTeamsWhereSponsorIn(industryType));
    }
}
