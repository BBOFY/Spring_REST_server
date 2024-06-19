package cz.cvut.fit.tjv.polakemi.semestral_work.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.polakemi.semestral_work.api.converter.SponsorConverter;
import cz.cvut.fit.tjv.polakemi.semestral_work.api.converter.TeamConverter;
import cz.cvut.fit.tjv.polakemi.semestral_work.business.TeamService;
import cz.cvut.fit.tjv.polakemi.semestral_work.api.exception.NoEntityFoundException;
import cz.cvut.fit.tjv.polakemi.semestral_work.business.EntityStateException;
import cz.cvut.fit.tjv.polakemi.semestral_work.business.SponsorService;
import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Sponsor;
import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Team;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;

@RestController
public class SponsorController {
    private final SponsorService sponsorService;
    private final TeamService teamService;

    public SponsorController(SponsorService sponsorService, TeamService teamService) {
        this.sponsorService = sponsorService;
        this.teamService = teamService;
    }

    /**
     * Returns all sponsors saved in database
     * @return Collection od sponsor dtos
     */
    @JsonView(Views.Public.class)
    @GetMapping("/sponsors")
    public Collection<SponsorDto> all() { return SponsorConverter.fromModelAny(sponsorService.readAll());}

    @JsonView(Views.Internal.class)
    @GetMapping("/secured/sponsors")
    public Collection<SponsorDto> allSecured() { return SponsorConverter.fromModelAny(sponsorService.readAll()); }

    /**
     * Create and add new sponsor to database
     * @param newSponsor Json body defining new sponsor's attributes
     * @return sponsor dto
     */
    @JsonView(Views.Public.class)
    @PostMapping("/sponsors")
    public SponsorDto newSponsor(@RequestBody SponsorDto newSponsor) {
        Sponsor sponsormodel = SponsorConverter.toModel(newSponsor);
        try {
            this.sponsorService.create(sponsormodel);
        }
        catch (EntityStateException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sponsor ID is not unique", exception);
        }
        sponsormodel = this.sponsorService.readById(sponsormodel.getIdSponsor()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sponsor not found")
        );
        return SponsorConverter.fromModel(sponsormodel);
    }

    /**
     * Returns sponsor saved in database
     * @param id key of the sponsor
     * @return sponsor dto
     */
    @GetMapping("/sponsors/{id}")
    public SponsorDto one(@PathVariable Integer id) {
        return SponsorConverter.fromModel(
                sponsorService.readById(id).orElseThrow(NoEntityFoundException::new)
        );
    }

    /**
     * Mapping for updating attributes of entity Sponsor
     * Collection type attributes (other entities in relations with entity Sponsor) will not be changed
     * These attributes must be changed by other mappings
     * @param sponsorDto as Json body with parameters to change
     * @param id of entity Sponsor, which will be changed
     */
    @PutMapping("/sponsors/{id}")
    public SponsorDto updateSponsor(@RequestBody SponsorDto sponsorDto, @PathVariable Integer id) {
        Sponsor s = sponsorService.readById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sponsor not found")
        );
        Sponsor sponsor = SponsorConverter.toModel(sponsorDto);
        sponsor.setIdSponsor(id);
        sponsor.setSponsoredTeams(s.getSponsoredTeams());
        try {
            this.sponsorService.update(sponsor);
        }
        catch (EntityStateException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sponsor ID is not unique", exception);
        }
        return sponsorDto;
    }

    /**
     * Removes sponsor from the database
     * @param id key of the sponsor
     */
    @DeleteMapping("/sponsors/{id}")
    public void deleteSponsor(@PathVariable Integer id) {
        Optional<Sponsor> sponsorOptional = sponsorService.readById(id);
        if (sponsorOptional.isEmpty()) {
            return;
        }
        Sponsor sponsor = sponsorOptional.get();

        if (sponsor.getSponsoredTeams() != null)
            sponsor.getSponsoredTeams().forEach(
                    (t) -> {
                        try {
                            teamService.removeSponsor(t.getIdTeam(), sponsor);
                        } catch (EntityStateException exception) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team ID is not unique", exception);
                        }
                    });
        sponsorService.deleteById(id);
    }

    /**
     * Returns all teams sponsored by sponsor
     * @param id key of the sponsor
     * @return Collection of team dtos
     */
    @GetMapping("/sponsors/{id}/teams")
    public Collection<TeamDto> getSponsoredTeams(@PathVariable Integer id) {
        Sponsor sponsor = sponsorService.readById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sponsor not found")
        );
        return TeamConverter.fromModelAny(sponsor.getSponsoredTeams());
    }

    /**
     * Creates relation between sponsor and team, i.e. assigns team to sponsor
     * @param id_s key of the sponsor
     * @param id_t key of the team
     */
    @PutMapping("/sponsors/{id_s}/teams/{id_t}")
    public void addSponsoredTeam(@PathVariable Integer id_s, @PathVariable Integer id_t) {
        Sponsor sponsor = sponsorService.readById(id_s).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sponsor not found")
        );
        Team team = teamService.readById(id_t).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found")
        );
        try {
            sponsorService.addTeam(id_s, team);
        } catch (EntityStateException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sponsor ID is not unique", exception);
        }
        try {
            teamService.addSponsor(id_t, sponsor);
        } catch (EntityStateException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle ID is not unique", exception);
        }
    }

    /**
     * Removes relation between sponsor and team
     * @param id_s key of the sponsor
     * @param id_t key of the team
     */
    @DeleteMapping("/sponsors/{id_s}/teams/{id_t}")
    public void removeSponsoredTeam(@PathVariable Integer id_s, @PathVariable Integer id_t) {
        Sponsor sponsor = sponsorService.readById(id_s).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sponsor not found")
        );
        Team team = teamService.readById(id_t).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found")
        );
        try {
            sponsorService.removeTeam(id_s, team);
        } catch (EntityStateException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sponsor ID is not unique", exception);
        }
        try {
            teamService.removeSponsor(id_t, sponsor);
        } catch (EntityStateException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle ID is not unique", exception);
        }
    }

}
