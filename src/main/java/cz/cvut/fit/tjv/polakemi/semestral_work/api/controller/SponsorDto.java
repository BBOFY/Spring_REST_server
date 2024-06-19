package cz.cvut.fit.tjv.polakemi.semestral_work.api.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Team;

import java.util.Collection;
import java.util.HashSet;

public class SponsorDto {

    @JsonView(Views.Public.class)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    public Integer idSponsor;

    @JsonView(Views.Public.class)
    public String sponsorName;

    @JsonView(Views.Public.class)
    public String industry;

    @JsonIgnore
    public Collection<Team> sponsoredTeams;

    @JsonView(Views.Public.class)
    public Collection<Integer> sponsoredTeamsIds = new HashSet<>();

    public SponsorDto() {}

    public SponsorDto(Integer idSponsor, String sponsorName, String industry) {
        this.idSponsor = idSponsor;
        this.sponsorName = sponsorName;
        this.industry = industry;
    }

    public SponsorDto(Integer idSponsor, String sponsorName, String industry, Collection<Team> sponsoredTeams) {
        this.idSponsor = idSponsor;
        this.sponsorName = sponsorName;
        this.industry = industry;
        this.sponsoredTeams = sponsoredTeams;
        if (!sponsoredTeams.isEmpty()) {
            sponsoredTeams.forEach((t) -> sponsoredTeamsIds.add(t.getIdTeam()));
        }
    }

    public Integer getIdSponsor() {
        return idSponsor;
    }

    public void setIdSponsor(Integer idSponsor) {
        this.idSponsor = idSponsor;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public Collection<Team> getSponsoredTeams() {
        return sponsoredTeams;
    }

    public void setSponsoredTeams(Collection<Team> sponsoredTeams) {
        this.sponsoredTeams = sponsoredTeams;
    }

    public Collection<Integer> getSponsoredTeamsIds() {
        return sponsoredTeamsIds;
    }

    public void setSponsoredTeamsIds(Collection<Integer> sponsoredTeamsIds) {
        this.sponsoredTeamsIds = sponsoredTeamsIds;
    }
}
