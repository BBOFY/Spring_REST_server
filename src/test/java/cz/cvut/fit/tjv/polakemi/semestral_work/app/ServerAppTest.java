package cz.cvut.fit.tjv.polakemi.semestral_work.app;

import cz.cvut.fit.tjv.polakemi.semestral_work.api.controller.SponsorController;
import cz.cvut.fit.tjv.polakemi.semestral_work.api.controller.TeamController;
import cz.cvut.fit.tjv.polakemi.semestral_work.api.controller.VehicleController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ServerAppTest {

    @Autowired
    TeamController teamController;

    @Autowired
    SponsorController sponsorController;

    @Autowired
    VehicleController vehicleController;

    @Test
    public void contextLoadTests() {
        Assertions.assertThat(teamController).isNotNull();
        Assertions.assertThat(sponsorController).isNotNull();
        Assertions.assertThat(vehicleController).isNotNull();
    }
}
