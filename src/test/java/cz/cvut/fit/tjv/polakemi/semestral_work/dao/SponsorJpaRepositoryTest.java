package cz.cvut.fit.tjv.polakemi.semestral_work.dao;

import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Sponsor;
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
public class SponsorJpaRepositoryTest {

    @Autowired
    SponsorJpaRepository sponsorJpaRepository;

    @Test
    public void testCreateReadDelete() {
        Sponsor sponsor = new Sponsor("Cola", "food");

        sponsorJpaRepository.save(sponsor);

        Iterable<Sponsor> sponsors = sponsorJpaRepository.findAll();
        Assertions.assertThat(sponsors).extracting(Sponsor::getSponsorName).containsOnly("Cola");
        Assertions.assertThat(sponsors).extracting(Sponsor::getIndustry).containsOnly("food");

        sponsorJpaRepository.deleteAll();
        Assertions.assertThat(sponsorJpaRepository.findAll()).isEmpty();
    }
}
