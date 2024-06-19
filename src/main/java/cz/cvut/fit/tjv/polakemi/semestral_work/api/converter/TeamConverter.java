package cz.cvut.fit.tjv.polakemi.semestral_work.api.converter;

import cz.cvut.fit.tjv.polakemi.semestral_work.api.controller.TeamDto;
import cz.cvut.fit.tjv.polakemi.semestral_work.api.exception.InvalidBodyException;
import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class TeamConverter {
    public static Team toModel(TeamDto teamDto) {
        if (Objects.isNull(teamDto.teamName)) {
            throw new InvalidBodyException();
        }
        return new Team(teamDto.teamName, teamDto.score, teamDto.numberOfPlayers, teamDto.teamSponsors, teamDto.ownedVehicles);
    }

    public static TeamDto fromModel(Team team) {
        return new TeamDto(team.getIdTeam(), team.getTeamName(), team.getScore(), team.getNumberOfPlayers(), team.getTeamSponsors(), team.getTeamVehicles());
    }

    public static Collection<Team> toModelAny(Collection<TeamDto> teamDtos) {
        Collection<Team> teams = new ArrayList<>();
        teamDtos.forEach((t) -> teams.add(toModel(t)));
        return teams;
    }

    public static Collection<TeamDto> fromModelAny(Collection<Team> teams) {
        Collection<TeamDto> teamDtos = new ArrayList<>();
        teams.forEach((t) -> teamDtos.add(fromModel(t)));
        return teamDtos;
    }
}
