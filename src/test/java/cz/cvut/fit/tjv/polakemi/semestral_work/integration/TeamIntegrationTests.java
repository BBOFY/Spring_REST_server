package cz.cvut.fit.tjv.polakemi.semestral_work.integration;

import cz.cvut.fit.tjv.polakemi.semestral_work.api.controller.TeamDto;
import cz.cvut.fit.tjv.polakemi.semestral_work.api.exception.NoEntityFoundException;
import cz.cvut.fit.tjv.polakemi.semestral_work.RestMain;
import cz.cvut.fit.tjv.polakemi.semestral_work.api.controller.TeamController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestMain.class)
@DirtiesContext
@Transactional
public class TeamIntegrationTests {

    @Autowired
    TeamController teamController;

    final Integer teamId = 1;

    @Test
    public void testCreateRead() {

        TeamDto teamDto = new TeamDto();
        teamDto.setTeamName("Test team");
        teamDto.setScore(0);
        teamDto.setNumberOfPlayers(0);

        TeamDto teamResult = teamController.newTeam(teamDto);
        System.out.println(teamResult.getIdTeam());

        Iterable<TeamDto> teams = teamController.all();
        Assertions.assertThat(teams).anyMatch(team -> Objects.equals(team.getIdTeam(), teamId));

        teamController.deleteTeam(teamResult.getIdTeam());
    }

    @Test
    public void errorHandlingNoEntityFoundExceptionThrown() {
        Assertions.assertThatExceptionOfType(NoEntityFoundException.class).isThrownBy(
                () -> teamController.one(teamId)
        );
    }

}
