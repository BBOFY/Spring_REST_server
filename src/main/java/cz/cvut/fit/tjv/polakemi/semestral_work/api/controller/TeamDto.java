package cz.cvut.fit.tjv.polakemi.semestral_work.api.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Vehicle;
import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Sponsor;

import java.util.Collection;
import java.util.HashSet;

public class TeamDto {

    @JsonView(Views.Public.class)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    public Integer idTeam;

    @JsonView(Views.Public.class)
    public String teamName;

    @JsonView(Views.Public.class)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    public Integer score;

    @JsonView(Views.Public.class)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    public Integer numberOfPlayers;

    @JsonView(Views.Public.class)
    public Collection<Integer> ownedVehiclesIds = new HashSet<>();

    @JsonView(Views.Public.class)
    public Collection<Integer> teamSponsorsIds = new HashSet<>();

    @JsonIgnore
    public Collection<Vehicle> ownedVehicles;

    @JsonIgnore
    public Collection<Sponsor> teamSponsors;

    public TeamDto () {}

    public TeamDto(Integer idTeam, String teamName, Integer score, Integer numberOfPlayers) {
        this.idTeam = idTeam;
        this.teamName = teamName;
        this.score = score;
        this.numberOfPlayers = numberOfPlayers;
    }

    public TeamDto(Integer idTeam, String teamName, Integer score, Integer numberOfPlayers, Collection<Sponsor> teamSponsors, Collection<Vehicle> ownedVehicles) {
        this.idTeam = idTeam;
        this.teamName = teamName;
        this.score = score;
        this.numberOfPlayers = numberOfPlayers;
        this.teamSponsors = teamSponsors;
        this.ownedVehicles = ownedVehicles;
        if (teamSponsors != null && !teamSponsors.isEmpty()) {
            teamSponsors.forEach((s) -> teamSponsorsIds.add(s.getIdSponsor()));
        }
        if (ownedVehicles != null && !ownedVehicles.isEmpty()) {
            ownedVehicles.forEach((v) -> ownedVehiclesIds.add(v.getIdVehicle()));
        }
    }

    public Integer getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(Integer idTeam) {
        this.idTeam = idTeam;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(Integer numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }
}
