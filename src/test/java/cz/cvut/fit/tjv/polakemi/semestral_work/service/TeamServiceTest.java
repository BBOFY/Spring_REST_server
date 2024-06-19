package cz.cvut.fit.tjv.polakemi.semestral_work.service;

import cz.cvut.fit.tjv.polakemi.semestral_work.business.TeamService;
import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Team;
import cz.cvut.fit.tjv.polakemi.semestral_work.business.EntityStateException;
import cz.cvut.fit.tjv.polakemi.semestral_work.dao.TeamJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@DirtiesContext
public class TeamServiceTest {

    @InjectMocks
    TeamService teamService;

    @Mock
    TeamJpaRepository dao;

    @Test
    public void testFindAllTeams() {
        List<Team> list = new ArrayList<>();
        Team team1 = new Team("team1", 0, 0);
        Team team2 = new Team("team2", 0, 0);
        Team team3 = new Team("team3", 0, 0);

        list.add(team1);
        list.add(team2);
        list.add(team3);

        when(dao.findAll()).thenReturn(list);

        Collection<Team> teamList = teamService.readAll();
        assertEquals(3, teamList.size());
        verify(dao, times(1)).findAll();
    }

    @Test
    public void testCreateTeam() {
        Team team = new Team("team", 0, 0);
        try {
            teamService.create(team);
        } catch (EntityStateException e) {
            System.err.println("Error"); // this should never occur
        }
        verify(dao, times(1)).save(team);
    }
}
