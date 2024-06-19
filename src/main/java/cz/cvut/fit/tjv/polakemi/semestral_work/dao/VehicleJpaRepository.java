package cz.cvut.fit.tjv.polakemi.semestral_work.dao;

import cz.cvut.fit.tjv.polakemi.semestral_work.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleJpaRepository extends JpaRepository<Vehicle, Integer> {
//    Collection<Vehicle> findByName(String FirstName);
//    List<Vehicle> findAll();
}
