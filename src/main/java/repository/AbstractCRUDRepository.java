package repository;

import domain.*;
import validation.*;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCRUDRepository<ID, E extends HasID<ID>> implements CRUDRepository<ID, E>{
    Map<ID, E> entities;
    Validator<E> validator;

    public AbstractCRUDRepository(Validator validator) {
        entities = new HashMap<ID, E>();
        this.validator = validator;
    }

    @Override
    public E findOne(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null! \n");
        }
        else {
            return entities.get(id);
        }
    }

    @Override
    public Iterable<E> findAll() { return entities.values(); }

    @Override
    public E save(E entity) throws ValidationException {
//        try {
            validator.validate(entity);
            return entities.putIfAbsent(entity.getID(), entity);
            //            Returns:
            //the previous value associated with the specified key,
            // or null if there was no mapping for the key. (A null return can also indicate that the map previously
            // associated null with the key, if the implementation supports null values.)
//        }
//        catch (ValidationException ve) {
//            System.out.println("Entity is not valid! \n");
//            return null;
//        }

//        if given in signature, why not throw?

    }

    @Override
    public E delete(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null! \n");
        }
        else {
            return entities.remove(id);
        }
    }

    @Override
    public E update(E entity) {
        try {
            validator.validate(entity);
            return entities.replace(entity.getID(), entity);
        }
        catch (ValidationException ve) {
            System.out.println("Entity is not valid! \n");
            return null;
        }
    }
}
