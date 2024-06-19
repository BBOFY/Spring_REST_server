package cz.cvut.fit.tjv.polakemi.semestral_work.business;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

public abstract class AbstractCrudService<K, E, R extends JpaRepository<E, K>> {
    protected final R repository;

    protected AbstractCrudService(R repository) {
        this.repository = repository;
    }

    protected abstract boolean exists(E entity);

    @Transactional
    public E create(E entity) throws EntityStateException {
        if (exists(entity)) {
            throw new EntityStateException(entity);
        }
        else {
            return repository.save(entity);
        }
    }

    public Optional<E> readById(K id) {
        return repository.findById(id);
    }

    public Collection<E> readAll() {
        return repository.findAll();
    }

    @Transactional
    public void update(E entity) throws EntityStateException {
        if (exists(entity)) {
            repository.save(entity);
        }
        else {
            throw new EntityStateException(entity);
        }
    }

    public void deleteById(K id) {
        repository.deleteById(id);
    }
}
