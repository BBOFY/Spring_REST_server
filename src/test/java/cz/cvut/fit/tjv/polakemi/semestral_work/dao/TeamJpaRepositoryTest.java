package cz.cvut.fit.tjv.polakemi.semestral_work.dao;

import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Team;
import cz.cvut.fit.tjv.polakemi.semestral_work.RestMain;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestMain.class)
@DirtiesContext
public class TeamJpaRepositoryTest {

    @Autowired
    TeamJpaRepository teamJpaRepository;

    @Test
    public void testCreateReadDelete() {
        Team team = new Team("Test team", 150, 20);

        teamJpaRepository.save(team);

        Iterable<Team> teams = teamJpaRepository.findAll();
        Assertions.assertThat(teams).extracting(Team::getTeamName).containsOnly("Test team");
        Assertions.assertThat(teams).extracting(Team::getScore).containsOnly(150);
        Assertions.assertThat(teams).extracting(Team::getNumberOfPlayers).containsOnly(20);

        teamJpaRepository.deleteAll();
        Assertions.assertThat(teamJpaRepository.findAll()).isEmpty();
    }
}
