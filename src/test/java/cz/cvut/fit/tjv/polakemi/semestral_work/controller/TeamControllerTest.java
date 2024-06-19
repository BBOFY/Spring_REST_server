package cz.cvut.fit.tjv.polakemi.semestral_work.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fit.tjv.polakemi.semestral_work.business.EntityStateException;
import cz.cvut.fit.tjv.polakemi.semestral_work.business.SponsorService;
import cz.cvut.fit.tjv.polakemi.semestral_work.business.TeamService;
import cz.cvut.fit.tjv.polakemi.semestral_work.business.VehicleService;
import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Team;
import cz.cvut.fit.tjv.polakemi.semestral_work.api.controller.TeamController;
import cz.cvut.fit.tjv.polakemi.semestral_work.business.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DirtiesContext
@RunWith(SpringRunner.class)
@WebMvcTest(TeamController.class)
public class TeamControllerTest {

    @MockBean
    TeamService teamService;

    @MockBean
    SponsorService sponsorService;

    @MockBean
    VehicleService vehicleService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testFindAllTeams() throws Exception {
        Team team1 = new Team("TestTeam1", 150, 10, null, null);
        Team team2 = new Team("TestTeam2", 250, 20, null, null);
        List<Team> teams = List.of(team1, team2);

        Mockito.when(teamService.readAll()).thenReturn(teams);

        mockMvc.perform(get("/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].teamName", Matchers.is("TestTeam1")))
                .andExpect(jsonPath("$[0].score", Matchers.is(150)))
                .andExpect(jsonPath("$[0].numberOfPlayers", Matchers.is(10)))
                .andExpect(jsonPath("$[1].teamName", Matchers.is("TestTeam2")))
                .andExpect(jsonPath("$[1].score", Matchers.is(250)))
                .andExpect(jsonPath("$[1].numberOfPlayers", Matchers.is(20)));
    }

    @Test
    public void testAddOne() throws Exception {

        Team team = new Team("testTeam", 100, 5);
        team.setIdTeam(1);
        String json = mapper.writeValueAsString(team);

        // Mocking readById, because it is used for reading team after creation
        Mockito.when(teamService.readById(not(eq(1)))).thenReturn(Optional.empty());
        Mockito.when(teamService.readById(1)).thenReturn(Optional.of(team));

        // After creating new team, return 200 with information about newly created team
        mockMvc.perform(post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teamName", Matchers.is("testTeam")))
                .andExpect(jsonPath("$.score", Matchers.is(100)))
                .andExpect(jsonPath("$.numberOfPlayers", Matchers.is(5)));

        ArgumentCaptor<Team> argumentCaptor = ArgumentCaptor.forClass(Team.class);
        Mockito.verify(teamService, Mockito.times(1)).create(argumentCaptor.capture());
        Team userProvidedToService = argumentCaptor.getValue();
        assertEquals("testTeam", userProvidedToService.getTeamName());
        assertEquals(100, userProvidedToService.getScore());
        assertEquals(5, userProvidedToService.getNumberOfPlayers());
    }

    @Test
    public void testGetOne() throws Exception {
        Team team = new Team("testTeam", 100, 5);
        Mockito.when(teamService.readById(1)).thenReturn(Optional.of(team));
        mockMvc.perform(get("/teams/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teamName", Matchers.is("testTeam")));
        Mockito.when(teamService.readById(not(eq(1)))).thenReturn(Optional.empty());
        mockMvc.perform(get("/teams/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreate() throws Exception {
        Team team = new Team("new Team", 0, 0);
        String json = mapper.writeValueAsString(team);

        Mockito.when(teamService.readById(not(eq(1)))).thenReturn(Optional.empty());
        Mockito.when(teamService.readById(1)).thenReturn(Optional.of(team));

        mockMvc.perform(post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teamName", Matchers.is("new Team")))
                .andExpect(jsonPath("$.score", Matchers.is(0)))
                .andExpect(jsonPath("$.numberOfPlayers", Matchers.is(0)));

        ArgumentCaptor<Team> argumentCaptor = ArgumentCaptor.forClass(Team.class);
        Mockito.verify(teamService, Mockito.times(1)).create(argumentCaptor.capture());
        Team teamProvidedToService = argumentCaptor.getValue();
        assertEquals("new Team", teamProvidedToService.getTeamName());
        assertEquals(0, teamProvidedToService.getScore());
        assertEquals(0, teamProvidedToService.getNumberOfPlayers());
    }

    @Test
    public void testCreateExisting() throws Exception {
        Team team = new Team("new Team", 0, 0);
        String json = mapper.writeValueAsString(team);

        doThrow(new EntityStateException(null)).when(teamService).create(any(Team.class));
        Mockito.when(teamService.readById(not(eq(1)))).thenReturn(Optional.empty());
        Mockito.when(teamService.readById(1)).thenReturn(Optional.of(team));

        mockMvc.perform(post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateOne() throws Exception {
        Team newT = new Team("new team", 100, 5);
        String newJson = mapper.writeValueAsString(newT);

        Mockito.when(teamService.readById(not(eq(1)))).thenReturn(Optional.empty());
        Mockito.when(teamService.readById(1)).thenReturn(Optional.of(newT));

        mockMvc.perform(put("/teams/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teamName", Matchers.is("new team")))
                .andExpect(jsonPath("$.score", Matchers.is(100)))
                .andExpect(jsonPath("$.numberOfPlayers", Matchers.is(5)));

        // if action is not set using when, nothing will occur and null will be returned
        // update returns void, so there is no need to mock it behaviour with when
        // ArgumentCaptor class will check, if update was called exactly one time with correct team entity its attributes
        ArgumentCaptor<Team> argumentCaptor = ArgumentCaptor.forClass(Team.class);
        Mockito.verify(teamService, Mockito.times(1)).update(argumentCaptor.capture());
        Team teamProvidedToService = argumentCaptor.getValue();
        assertEquals(1, teamProvidedToService.getIdTeam());
        assertEquals(100, teamProvidedToService.getScore());
        assertEquals(5, teamProvidedToService.getNumberOfPlayers());
    }

    @Test
    public void testUpdateNotExisting() throws Exception {
        Team team = new Team("new team", 100, 5);
        String json = mapper.writeValueAsString(team);

        Mockito.when(teamService.readById(not(eq(1)))).thenReturn(Optional.empty());
        Mockito.when(teamService.readById(1)).thenReturn(Optional.of(team));
        doThrow(new EntityStateException(null)).when(teamService).update(any(Team.class));
        // throw exception from calling update with any team entity

        mockMvc.perform(put("/teams/-5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDelete() throws Exception {
        Team team = new Team("new team", 100, 5);

        Mockito.when(teamService.readById(not(eq(1)))).thenReturn(Optional.empty());
        Mockito.when(teamService.readById(1)).thenReturn(Optional.of(team));

        // When trying to delete non-existing team, HTTP status should be 200
        // I implemented it by result
        // Since I don't want team with id -1 to be in the database and this team is not in db to begin with,...
        // ... my request from server is fulfilled ...
        mockMvc.perform(delete("/teams/-1"))
                .andExpect(status().isOk());
        // ... but deleteById is not called, since there is nothing to delete
        verify(teamService, never()).deleteById(any());

        // Deleting existing team
        mockMvc.perform(delete("/teams/1"))
                .andExpect(status().isOk());
        // Should be called only once
        verify(teamService, times(1)).deleteById(1);
    }
}
