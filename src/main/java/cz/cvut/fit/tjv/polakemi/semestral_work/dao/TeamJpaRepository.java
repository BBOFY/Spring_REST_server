package cz.cvut.fit.tjv.polakemi.semestral_work.dao;

import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface TeamJpaRepository extends JpaRepository<Team, Integer> {
    
    Collection<Team> findAllByTeamName(String teamName);
    Collection<Team> findAllByScore(Integer score);

    @Query(nativeQuery = true, value = "select ts.id_team from tjv_team_sponsor ts where exists" +
            "(select s from tjv_sponsor s where s.id_sponsor = ts.id_sponsor and s.industry = :industryType)")
    Collection<Integer> findAllTeamsIdsFromTeamsThatHaveAnySponsorInIndustryTypeNative(String industryType);

}
