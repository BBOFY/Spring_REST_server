package cz.cvut.fit.tjv.polakemi.semestral_work.business;

public class EntityStateException extends Exception{
    public <E> EntityStateException(E entity) {
        super("Illigal state of entity " + entity);
    }
}
