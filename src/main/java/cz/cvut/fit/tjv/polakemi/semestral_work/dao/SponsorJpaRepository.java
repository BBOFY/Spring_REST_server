package cz.cvut.fit.tjv.polakemi.semestral_work.dao;

import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SponsorJpaRepository extends JpaRepository<Sponsor, Integer> {

}
