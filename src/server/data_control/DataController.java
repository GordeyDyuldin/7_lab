package server.data_control;

import exceptions.ConfigFileNotFoundException;
import data_classes.City;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Control all data manipulations
 */
public class DataController {

    /**
     * Last time when collection was modified
     */
    private LocalDateTime modificationTime;

    /**
     * Program's collection
     */
    private final HashMap<Long, City> map;

    /**
     * Control database
     */
    private final DataBaseController dataBaseController;
    /**
     * Collection lock for concurrent
     */
    private final ReentrantReadWriteLock mapLock = new ReentrantReadWriteLock();
    /**
     * User creation lock for concurrent
     */
    private final ReentrantReadWriteLock authLock = new ReentrantReadWriteLock();

    /**
     * Create collection and make connection with database
     *
     * @throws SQLException                if connection with database is not created
     * @throws ConfigFileNotFoundException if config file database.config does not find
     */
    public DataController() throws SQLException, ConfigFileNotFoundException {
        map = new HashMap<>();
        dataBaseController = new DataBaseController(this, "jdbc:postgresql://127.0.0.1:5432/postgres", "postgres");
        // jdbc:postgresql://pg:5432/studs s338861
        // jdbc:postgresql://127.0.0.1:5432:5432/postgres postgres
        refreshMap();
    }

    /**
     * Update collection from data of database
     *
     * @throws SQLException if something got wrong with database
     */
    public void refreshMap() throws SQLException {
        mapLock.writeLock().lock();
        map.clear();
        dataBaseController.getAllCities().forEach(city -> map.put(city.getId(), city));
        mapLock.writeLock().unlock();
    }

    /**
     * Add new city to collection and database
     *
     * @param city  that need to add
     * @param login of owner of this city
     * @throws SQLException if something got wrong with database
     */
    public void addCity(City city, String login) throws SQLException {
        mapLock.writeLock().lock();
        dataBaseController.addCity(city, login);
        map.put(city.getId(), city);
        mapLock.writeLock().unlock();
    }

    /**
     * Remove all city from collection and database which this user was owner
     *
     * @param login of user
     * @throws SQLException if something got wrong with database
     */
    public void clearMap(String login) throws SQLException {
        mapLock.writeLock().lock();
        dataBaseController.clearAll(login);
        refreshMap();
        mapLock.writeLock().unlock();
    }

    /**
     * Delete city from collection and database
     *
     * @param id of city
     * @throws SQLException if something got wrong with database
     */
    public void removeCity(Long id) throws SQLException {
        mapLock.writeLock().lock();
        dataBaseController.removeCity(id);
        map.remove(id);
        mapLock.writeLock().unlock();
    }

    /**
     * Update city's data
     *
     * @param city that need to update
     * @throws SQLException if something got wrong with database
     */
    public void updateCity(City city) throws SQLException {
        mapLock.writeLock().lock();
        dataBaseController.updateCity(city);
        map.put(city.getId(), city);
        mapLock.writeLock().unlock();
    }

    /**
     * Create new user to database
     *
     * @param login of this user
     * @param hash  of password of this user
     * @param salt  of hash
     * @throws SQLException if something got wrong with database
     */
    public void createUser(String login, String hash, String salt) throws SQLException {
        authLock.writeLock().lock();
        dataBaseController.createUser(login, hash, salt);
        authLock.writeLock().unlock();
    }

    /**
     * Create lock for read collection
     */
    public void readLock() {
        mapLock.readLock().lock();
    }

    /**
     * Unlock previous lock for reading collection
     */
    public void readUnlock() {
        mapLock.readLock().unlock();
    }

    /**
     * Get collection
     *
     * @return hashmap
     */
    public HashMap<Long, City> getMap() {
        return map;
    }

    /**
     * Update time of collection modification
     * <p>Set time from LocalDateTime.now()</p>
     */
    public void updateModificationTime() {
        mapLock.writeLock().lock();
        modificationTime = LocalDateTime.now();
        mapLock.writeLock().unlock();
    }

    /**
     * Check uniqueness of id on collection
     *
     * @param id that need to check
     * @return true if id does not find in collection else false
     */
    public boolean isUniqueId(Long id) {
        return !map.containsKey(id);
    }

    /**
     * Check that collection is empty
     *
     * @return true is collection is empty else false
     */
    public boolean isMapEmpty() {
        return map.isEmpty();
    }

    /**
     * Get modification time
     */
    public LocalDateTime getModificationTime() {
        return modificationTime;
    }

    /**
     * Get database controller
     *
     * @return database controller
     */
    public DataBaseController getDataBaseController() {
        return dataBaseController;
    }
}
