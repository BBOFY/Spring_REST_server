package cz.cvut.fit.tjv.polakemi.semestral_work.dao;

import java.util.Collection;
import java.util.Optional;

public interface AbstractCrudRepository<K, E> {
    void createOrUpdate(E entity);

    Optional<E> readById(K id);

    Collection<E> readAll();

    void deleteById(K id);

    boolean exists(E entity);

}
