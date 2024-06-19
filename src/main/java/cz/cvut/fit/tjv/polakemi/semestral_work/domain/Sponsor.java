package cz.cvut.fit.tjv.polakemi.semestral_work.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@Entity(name = "tjv_sponsor")
public class Sponsor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int idSponsor;

    @Column(name = "sponsor_name", nullable = false)
    private String sponsorName;

    @Column(name = "industry")
    private String industry;

    @ManyToMany(mappedBy = "teamSponsors")
    private Collection<Team> sponsoredTeams = new HashSet<>();

    public Sponsor() {}

    public Sponsor(String sponsorName) {
        this.sponsorName = Objects.requireNonNull(sponsorName);
        this.industry = null;
    }

    public Sponsor(String sponsorName, String industry) {
        this.sponsorName = Objects.requireNonNull(sponsorName);
        this.industry = industry;
    }

    public Sponsor(Integer idSponsor, String sponsorName, String industry) {
        this.idSponsor = Objects.requireNonNull(idSponsor);
        this.sponsorName = Objects.requireNonNull(sponsorName);
        this.industry = industry;
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

    public Collection<Team> getSponsoredTeams() { return sponsoredTeams; }

    public void setSponsoredTeams(Collection<Team> sponsoredTeams) {
        this.sponsoredTeams = sponsoredTeams;
    }

    public void addSponsoredTeam(Team team) {
        sponsoredTeams.add(team);
    }

    public void removeSponsoredTeam(Team team) {
        sponsoredTeams.remove(team);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sponsor sponsor = (Sponsor) o;

        if (idSponsor != sponsor.idSponsor) return false;
        if (!sponsorName.equals(sponsor.sponsorName)) return false;
        if (industry != null ? !industry.equals(sponsor.industry) : sponsor.industry != null) return false;
        return sponsoredTeams != null ? sponsoredTeams.equals(sponsor.sponsoredTeams) : sponsor.sponsoredTeams == null;
    }

    @Override
    public int hashCode() {
        int result = idSponsor;
        result = 31 * result + sponsorName.hashCode();
        result = 31 * result + (industry != null ? industry.hashCode() : 0);
        result = 31 * result + (sponsoredTeams != null ? sponsoredTeams.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Sponsor{" +
                "idSponsor=" + idSponsor +
                ", sponsorName='" + sponsorName + '\'' +
                ", industry='" + industry + '\'' +
                '}';
    }
}
