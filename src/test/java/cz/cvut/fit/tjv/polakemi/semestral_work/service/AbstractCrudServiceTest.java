package cz.cvut.fit.tjv.polakemi.semestral_work.service;

import cz.cvut.fit.tjv.polakemi.semestral_work.business.EntityStateException;
import cz.cvut.fit.tjv.polakemi.semestral_work.business.TeamService;
import cz.cvut.fit.tjv.polakemi.semestral_work.dao.TeamJpaRepository;
import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class AbstractCrudServiceTest {

    @MockBean
    private TeamJpaRepository teamJpaRepository;

    @Autowired
    private TeamService teamService;

    private final Team existingTeam = new Team("team1", 0, 0);
    private final Team nonExistingTeam = new Team("team2", 0, 0);

    @BeforeEach
    void setUp() {
        existingTeam.setIdTeam(1);
        Mockito.when(teamJpaRepository.existsById(existingTeam.getIdTeam())).thenReturn(true);
        Mockito.when(teamJpaRepository.existsById(nonExistingTeam.getIdTeam())).thenReturn(false);
        Mockito.when(teamJpaRepository.findById(existingTeam.getIdTeam())).thenReturn(Optional.of(existingTeam));
        Mockito.when(teamJpaRepository.findById(nonExistingTeam.getIdTeam())).thenReturn(Optional.empty());
        Mockito.when(teamJpaRepository.findAll()).thenReturn(List.of(existingTeam));
        Mockito.when(teamJpaRepository.save(existingTeam)).thenReturn(existingTeam);
        Mockito.when(teamJpaRepository.save(nonExistingTeam)).thenReturn(nonExistingTeam);
    }

    @Test
    void create() throws Exception{
        Assertions.assertThrows(EntityStateException.class, () -> teamService.create(existingTeam));
        Mockito.verify(teamJpaRepository, Mockito.never()).save(existingTeam);

        Assertions.assertEquals(nonExistingTeam, teamService.create(nonExistingTeam));
        Mockito.verify(teamJpaRepository, Mockito.atLeast(1)).save(nonExistingTeam);
    }

    @Test
    void readById() {
        Assertions.assertEquals(Optional.of(existingTeam), teamService.readById(existingTeam.getIdTeam()));
        Mockito.verify(teamJpaRepository, Mockito.atLeast(1)).findById(existingTeam.getIdTeam());

        Assertions.assertEquals(Optional.empty(), teamService.readById(nonExistingTeam.getIdTeam()));
        Mockito.verify(teamJpaRepository, Mockito.atLeast(1)).findById(nonExistingTeam.getIdTeam());
    }

    @Test
    void readAll() {
        var retCol = List.copyOf(teamService.readAll());
        var exp = List.of(existingTeam);
        Assertions.assertEquals(exp, retCol);
        Mockito.verify(teamJpaRepository, Mockito.atLeast(1)).findAll();
    }

    @Test
    void update() throws Exception {
        Assertions.assertThrows(EntityStateException.class, () -> teamService.update(nonExistingTeam));
        Mockito.verify(teamJpaRepository, Mockito.never()).save(nonExistingTeam);

        teamService.update(existingTeam);
        Mockito.verify(teamJpaRepository, Mockito.atLeast(1)).save(existingTeam);
    }

    @Test
    void deleteById() {
        teamService.deleteById(existingTeam.getIdTeam());
        Mockito.verify(teamJpaRepository, Mockito.atLeast(1)).deleteById(existingTeam.getIdTeam());
    }
}
