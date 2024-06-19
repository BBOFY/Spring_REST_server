package cz.cvut.fit.tjv.polakemi.semestral_work.api.converter;

import cz.cvut.fit.tjv.polakemi.semestral_work.api.controller.SponsorDto;
import cz.cvut.fit.tjv.polakemi.semestral_work.api.exception.InvalidBodyException;
import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Sponsor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class SponsorConverter {
    public static Sponsor toModel(SponsorDto sponsorDto) {
        if (Objects.isNull(sponsorDto.sponsorName)) {
            throw new InvalidBodyException();
        }
        return new Sponsor(sponsorDto.sponsorName, sponsorDto.industry);
    }

    public static SponsorDto fromModel(Sponsor sponsor) {
        return new SponsorDto(sponsor.getIdSponsor(), sponsor.getSponsorName(), sponsor.getIndustry(), sponsor.getSponsoredTeams());
    }

    public static Collection<Sponsor> toModelAny(Collection<SponsorDto> sponsorDtos) {
        Collection<Sponsor> sponsors = new ArrayList<>();
        sponsorDtos.forEach((s) -> sponsors.add(toModel(s)));
        return sponsors;
    }

    public static Collection<SponsorDto> fromModelAny(Collection<Sponsor> sponsors) {
        Collection<SponsorDto> sponsorDtos = new ArrayList<>();
        sponsors.forEach((s) -> sponsorDtos.add(fromModel(s)));
        return sponsorDtos;
    }
}
