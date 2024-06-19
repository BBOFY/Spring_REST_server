package cz.cvut.fit.tjv.polakemi.semestral_work.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity(name = "tjv_team")
public class Team implements Serializable {
    /**
     * the primary key of Team
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonIgnore
    private int idTeam;
    private String teamName;
    private Integer score;
    private Integer numberOfPlayers;

    @JsonIgnore
    @OneToMany(mappedBy = "owner")
    private Collection<Vehicle> teamVehicles = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "tjv_team_sponsor",
            joinColumns = @JoinColumn(name = "idTeam"),
            inverseJoinColumns = @JoinColumn(name = "idSponsor"))
    private Collection<Sponsor> teamSponsors = new HashSet<>();

    public Team() {}

    public Team(String teamName, Integer score, Integer numberOfPlayers) {
        this.teamName = Objects.requireNonNull(teamName);
        this.score = score;
        this.numberOfPlayers = numberOfPlayers;
    }

    public Team(String teamName, Integer score, Integer numberOfPlayers, Collection<Sponsor> teamSponsors, Collection<Vehicle> ownedVehicles) {
        this.teamName = Objects.requireNonNull(teamName);
        this.score = score;
        this.numberOfPlayers = numberOfPlayers;
        this.teamSponsors = teamSponsors;
        this.teamVehicles = ownedVehicles;
    }

    public Team(Integer id, String teamName, Integer score, Integer numberOfPlayers, Collection<Sponsor> teamSponsors, Collection<Vehicle> ownedVehicles) {
        this.idTeam = id;
        this.teamName = Objects.requireNonNull(teamName);
        this.score = score;
        this.numberOfPlayers = numberOfPlayers;
        this.teamSponsors = teamSponsors;
        this.teamVehicles = ownedVehicles;
    }

    public Integer getIdTeam() { return idTeam; }

    public void setIdTeam(Integer idTeam) {
        this.idTeam = idTeam;
    }

    public String getTeamName() {
        return teamName;
    }

    public Integer getScore() {
        return score;
    }

    public Integer getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setNumberOfPlayers(Integer numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public void setTeamVehicles(Collection<Vehicle> teamVehicles) {
        this.teamVehicles = teamVehicles;
    }

    public void setTeamSponsors(Collection<Sponsor> teamSponsors) {
        this.teamSponsors = teamSponsors;
    }

    public Collection<Vehicle> getTeamVehicles() { return teamVehicles/*Collections.unmodifiableCollection(teamVehicles)*/; }

    public Collection<Sponsor> getTeamSponsors() { return teamSponsors/*Collections.unmodifiableCollection(teamSponsors)*/; }

    public void addVehicle(Vehicle vehicle) { teamVehicles.add(Objects.requireNonNull(vehicle)); }

    public void addSponsor(Sponsor sponsor) {
        teamSponsors.add(Objects.requireNonNull(sponsor));
        sponsor.addSponsoredTeam(this);
    }

    public void removeVehicle(Vehicle vehicle) {
        teamVehicles.remove(vehicle);
    }

    public void removeSponsor(Sponsor sponsor) { teamSponsors.remove(sponsor); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

        return teamName.equals(team.teamName);
    }

    @Override
    public int hashCode() {
        return teamName.hashCode();
    }

    @Override
    public String toString() {
        return "Team{" +
                "idTeam=" + idTeam +
                ", teamName='" + teamName + '\'' +
                ", score=" + score +
                ", numberOfPlayers=" + numberOfPlayers +
                '}';
    }
}
