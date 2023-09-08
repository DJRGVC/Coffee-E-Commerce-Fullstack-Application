package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.DomainObject;

/**
 * The Service class is responsible for providing CRUD operations against the
 * database. It gives us a way to save objects, update them, retrieve the, or
 * delete them. You should extend this class with your own Service for each
 * persistent class (see the `models` package) that you create. Your new
 * *Service can then be `Autowired` into the API controllers and tests that need
 * it.
 *
 * Each Service class requires an appropriate *Repository instance (ie,
 * UserService needs a UserRepository; IngredientService needs an
 * IngredientRepository) with the `@Autowired` annotation on it. You'll also
 * need to implement `getRepository()` to return this field.
 *
 * @param <T>
 *            Type of entity that will be handled by this service
 * @param <K>
 *            Type of the key for this entity
 *
 * @author Kai Presler-Marshall
 * @author Bruno Volpato da Cunha
 *
 */
abstract public class Service <T extends DomainObject, K> {

    /**
     * Returns the Repository that Spring uses for interacting with the
     * database. This is the only method that _must_ be overridden in child
     * Service classes.
     *
     * @return The Repository instance from your subclass.
     */
    abstract protected JpaRepository<T, K> getRepository ();

    /**
     * Saves the provided object into the database. If the object already
     * exists, `save()` will perform an in-place update, overwriting the
     * existing record.
     *
     * @param obj
     *            The object to save into the database.
     */
    public void save ( final T obj ) {
        getRepository().saveAndFlush( obj );
    }

    /**
     * Returns all records of this type that exist in the database. If you want
     * more precise ways of retrieving an individual record (or collection of
     * records) see `findBy(Example)`
     *
     * @return All records stored in the database.
     */
    public List<T> findAll () {
        return getRepository().findAll();
    }

    /**
     * Saves a collection of elements to the database. If an error occurs saving
     * any of them, no objects will be saved. This makes it handy for ensuring
     * database consistency where all records should exist together.
     *
     * @param objects
     *            A List of objects to save to the database.
     */
    public void saveAll ( final List<T> objects ) {
        getRepository().saveAll( objects );
        getRepository().flush();
    }

    /**
     * Deletes an object from the database. This will remove the object from the
     * database, but not from memory. Trying to save it again after deletion is
     * undefined behaviour. YMMV.
     *
     * @param obj
     *            The object to delete from the database.
     */
    public void delete ( final T obj ) {
        getRepository().delete( obj );
    }

    /**
     * Removes all records of a given type from the database. For example,
     * `UserService.deleteAll()` would delete all Users. Be very careful when
     * calling this.
     */
    public void deleteAll () {
        getRepository().deleteAll();
    }

    /**
     * Returns a count of the number of records of a given type stored in the
     * database. This is faster than, and should be preferred to,
     * `findAll().size()` if you don't care about the actual records themselves.
     *
     * @return The number of records in the DB.
     */
    public long count () {
        return getRepository().count();
    }

    /**
     * Provides support for retrieving one or several elements from the database
     * by passing in an Example describing what you want to see. See
     * RecipeService.findByName()` for an example of how to use this.
     *
     * @param example
     *            The example to match against.
     * @return All matching records found, an empty list if none were.
     */
    protected List<T> findBy ( final Example<T> example ) {
        return getRepository().findAll( example );

    }

    /**
     * Checks to see if an object with the provided ID exists or not
     * 
     * @param id
     *            Id to check for existence of
     * @return If the object was found
     */
    public boolean existsById ( final K id ) {
        return getRepository().existsById( id );
    }

    /**
     * Finds an object with the provided ID
     * 
     * @param id
     *            ID of the object to find
     * @return The found object, null if none
     */
    public T findById ( final K id ) {
        if ( null == id ) {
            return null;
        }
        final Optional<T> res = getRepository().findById( id );
        if ( res.isPresent() ) {
            return res.get();
        }
        return null;
    }

}
