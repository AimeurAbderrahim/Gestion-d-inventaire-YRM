/**
 * Generic interface for database operations.
 * 
 * @param <T> The type of entity this interface will operate on
 * 
 * Why this interface exists:
 * - Provides a standardized way to interact with different database entities
 * - Ensures consistency across all database operations in the application
 * - Makes the code more maintainable and easier to test
 * - Allows for easy swapping of database implementations
 * 
 * How it should be used:
 * - Implement this interface for each entity type that needs database operations
 * - Use the implemented classes through this interface type for loose coupling
 * - Handle exceptions appropriately in implementations
 *
 *   @author Souane Abdenour , Berrached Maroua
 */

package db.java;

import java.util.List;
import db.errors.OperationFailedException;

public interface Operation<T> {
    
    /**
     * Adds a new object to the database.
     * @param obj The object to be added
     * @throws OperationFailedException if the operation fails
     */
    void add(T obj) throws OperationFailedException;
    
    /**
     * Updates an existing object in the database.
     * @param oldObj The current version of the object in the database
     * @param newObj The new version of the object with updated values
     * @throws OperationFailedException if the operation fails or object isn't found
     */
    void update(T oldObj, T newObj) throws OperationFailedException;
    
    /**
     * Removes an object from the database.
     * @param obj The object to be removed
     * @return true if removal was successful, false otherwise
     * @throws OperationFailedException if the operation fails
     */
    boolean remove(T obj) throws OperationFailedException;
    
    /**
     * Searches for an object in the database.
     * @param obj The object to search for
     * @return true if the object exists, false otherwise
     * @throws OperationFailedException if the operation fails
     */
    boolean search(T obj) throws OperationFailedException;
    
    /**
     * Finds an object by its unique identifier.
     * @param id The unique identifier of the object
     * @return The found object, or null if not found
     * @throws OperationFailedException if the operation fails
     */
    T findById(String id) throws OperationFailedException;
    
    /**
     * NOTE: this method might be dangerous in case we have big data we can't load all the data to memory so be carefull
     * Retrieves all objects of type T from the database.
     * @return A list of all objects, empty list if none found
     * @throws OperationFailedException if the operation fails
     */
    List<T> findAll() throws OperationFailedException;
    
    /**
     * Counts all objects of type T in the database.
     * @return The total count of objects
     * @throws OperationFailedException if the operation fails
     */
    long countAll() throws OperationFailedException;
    
    /**
     * Checks if an object exists by its ID.
     * @param id The unique identifier to check
     * @return true if an object with this ID exists, false otherwise
     * @throws OperationFailedException if the operation fails
     */
    boolean existsById(String id) throws OperationFailedException;

    /**
     * Get last index value of the data in ID.
     * @return index of the last item or data
     * @throws OperationFailedException if the operation fails
     */
    long getLastIndexID() throws OperationFailedException;
}
