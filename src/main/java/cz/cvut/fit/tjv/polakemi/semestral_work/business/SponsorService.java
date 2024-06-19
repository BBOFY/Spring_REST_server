package cz.cvut.fit.tjv.polakemi.semestral_work.business;

import cz.cvut.fit.tjv.polakemi.semestral_work.dao.SponsorJpaRepository;
import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Sponsor;
import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Team;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class SponsorService extends AbstractCrudService<Integer, Sponsor, SponsorJpaRepository> {
    public SponsorService(SponsorJpaRepository sponsorJpaRepository) {
        super(sponsorJpaRepository);
    }

    public void addTeam(Integer id, Team team) throws EntityStateException {
        Sponsor sponsor = repository.getById(id);
        if (sponsor.getSponsoredTeams().contains(team)) {
            return;
        }
        sponsor.addSponsoredTeam(team);
        update(sponsor);
    }

    public void removeTeam(Integer id, Team team) throws EntityStateException {
        Sponsor sponsor = repository.getById(id);
        if (!sponsor.getSponsoredTeams().contains(team)) {
            return;
        }
        sponsor.removeSponsoredTeam(team);
        update(sponsor);
    }

    @Override
    public boolean exists(Sponsor entity) { return repository.existsById(entity.getIdSponsor()); }
}
