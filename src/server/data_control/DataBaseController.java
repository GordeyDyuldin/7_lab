package server.data_control;

import data_classes.City;
import exceptions.ConfigFileNotFoundException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * Class for get and set data to database
 */
public class DataBaseController {
    /**
     * Current connection with database
     */
    private final Connection connection;

    /**
     * Data controller that create this instance
     */
    private final DataController dataController;

    /**
     * Create connection with database
     *
     * @param dataController that was created this instance
     * @param dbUrl          where database is deployed
     * @param dbUser         which use for login to database
     * @throws SQLException                if connection with database is not created
     * @throws ConfigFileNotFoundException if configuration file database.config does not exist
     */
    DataBaseController(DataController dataController, String dbUrl, String dbUser) throws SQLException,
            ConfigFileNotFoundException {
        this.dataController = dataController;
        connection = DriverManager.getConnection(dbUrl, dbUser, FilesController.readDBPassword());
    }

    /**
     * Get all cities from database
     *
     * @return array list with all cities from database
     * @throws SQLException if something got wrong with database
     */
    public ArrayList<City> getAllCities() throws SQLException {
        ResultSet results = connection.createStatement().executeQuery("SELECT * FROM cities");
        PreparedStatement psCoords = connection.prepareStatement("SELECT * FROM coordinates WHERE id=?");
        PreparedStatement psHum = connection.prepareStatement("SELECT * FROM humans WHERE id=?");
        ResultSet resultPs;
        City city;
        ArrayList<City> cities = new ArrayList<>();
        while (results.next()) {
            city = new City();
            city.setId(results.getLong("id"));
            city.setName(results.getString("name"));
            psCoords.setLong(1, city.getId());

            resultPs = psCoords.executeQuery();
            resultPs.next();
            city.getCoordinates().setX(resultPs.getFloat("x"));
            city.getCoordinates().setY(resultPs.getInt("y"));

            city.setCreationDate(ZonedDateTime.parse(results.getString("creationdate")));
            city.setArea(results.getInt("area"));
            city.setPopulation(results.getInt("population"));
            city.setMetersAboveSeaLevel(results.getLong("metersabovesealevel"));
            city.setEstablishmentDate(LocalDate.parse(results.getString("establishmentdate")));
            city.setClimate(results.getString("climate"));
            city.setGovernment(results.getString("government"));

            psHum.setLong(1, city.getId());
            resultPs = psHum.executeQuery();
            resultPs.next();

            city.getGovernor().setAge(resultPs.getLong("age"));
            city.getGovernor().setBirthday(LocalDateTime.parse(resultPs.getString("birthday")));

            cities.add(city);
        }

        return cities;
    }

    /**
     * Get user hash of password from database
     *
     * @param login of user
     * @return hash of password
     * @throws SQLException if something got wrong with database
     */
    public String getUserPassword(String login) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT password FROM users WHERE login = ?");
        ps.setString(1, login);
        ResultSet result = ps.executeQuery();
        result.next();
        return result.getString(1);
    }

    /**
     * Create new user in database
     *
     * @param login that user had
     * @param hash  of password that user used
     * @param salt  that add to hash
     * @throws SQLException if something got wrong with database
     */
    protected void createUser(String login, String hash, String salt) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO users VALUES(?, ?, ?)");
        ps.setString(1, login);
        ps.setString(2, hash);
        ps.setString(3, salt);
        ps.execute();
    }

    /**
     * Get user salt of password
     *
     * @param login of user
     * @return salt from database
     * @throws SQLException if something got wrong with database
     */
    public String getUserSalt(String login) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT salt FROM users WHERE login = ?");
        ps.setString(1, login);
        ResultSet result = ps.executeQuery();
        result.next();
        return result.getString(1);
    }

    /**
     * Check owner for city with id
     *
     * @param login of user
     * @param id    of city
     * @return true if user is owner of this city else false
     * @throws SQLException if something got wrong with database
     */
    public boolean isOwner(String login, long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT owner FROM cities WHERE id=?");
        ps.setLong(1, id);
        ResultSet resultSet = ps.executeQuery();
        resultSet.next();
        return login.equals(resultSet.getString(1));
    }

    /**
     * Create new city in database
     *
     * @param city  that need to create
     * @param login of owner of this city
     * @throws SQLException if something got wrong with database
     */
    protected void addCity(City city, String login) throws SQLException {
        PreparedStatement subPs;
        long id;
        if (city.getId() == null) {
            subPs = connection.prepareStatement("INSERT INTO coordinates(x, y) VALUES(?, ?) RETURNING id");
            subPs.setFloat(1, city.getCoordinates().getX());
            subPs.setInt(2, city.getCoordinates().getY());
            ResultSet result = subPs.executeQuery();
            result.next();
            id = result.getLong(1);
        } else {
            id = city.getId();
            subPs = connection.prepareStatement("INSERT INTO coordinates(id, x, y) VALUES(?, ?, ?)");
            subPs.setLong(1, id);
            subPs.setFloat(2, city.getCoordinates().getX());
            subPs.setInt(3, city.getCoordinates().getY());
            subPs.execute();
        }
        subPs = connection.prepareStatement("INSERT INTO humans(id, age, birthday) VALUES(?, ?, ?)");
        subPs.setLong(1, id);
        subPs.setLong(2, city.getGovernor().getAge());
        subPs.setString(3, city.getGovernor().getBirthday().toString());
        subPs.execute();

        PreparedStatement ps = connection.prepareStatement("INSERT INTO cities(id, name, coordinatesid, creationdate, "
                + "area, population, metersabovesealevel, establishmentdate, climate, government, governor, owner)" +
                " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setLong(1, id);
        ps.setString(2, city.getName());
        ps.setLong(3, id);
        ps.setString(4, city.getCreationDate().toString());
        ps.setLong(5, city.getArea());
        ps.setInt(6, city.getPopulation());
        ps.setLong(7, city.getMetersAboveSeaLevel());
        ps.setString(8, city.getEstablishmentDate().toString());
        ps.setObject(9, city.getClimate(), Types.OTHER);
        ps.setObject(10, city.getGovernment(), Types.OTHER);
        ps.setLong(11, id);
        ps.setString(12, login);

        ps.execute();
    }

    /**
     * Update city in database
     *
     * @param city that need to update
     * @throws SQLException if something got wrong with database
     */
    protected void updateCity(City city) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("UPDATE cities " +
                "SET name = ?, " +
                "creationdate = ?, " +
                "area = ?, " +
                "population = ?, " +
                "metersabovesealevel = ?, " +
                "establishmentdate = ?, " +
                "climate = ?, " +
                "government = ? " +
                "WHERE id = ?");
        ps.setString(1, city.getName());

        PreparedStatement subPs = connection.prepareStatement("UPDATE coordinates SET x=?, y=? WHERE id = ?");
        subPs.setFloat(1, city.getCoordinates().getX());
        subPs.setInt(2, city.getCoordinates().getY());
        subPs.setLong(3, city.getId());
        subPs.execute();

        ps.setString(2, city.getCreationDate().toString());
        ps.setLong(3, city.getArea());
        ps.setInt(4, city.getPopulation());
        ps.setLong(5, city.getMetersAboveSeaLevel());
        ps.setString(6, city.getEstablishmentDate().toString());
        ps.setObject(7, city.getClimate(), Types.OTHER);
        ps.setObject(8, city.getGovernment(), Types.OTHER);
        ps.setLong(9, city.getId());

        subPs = connection.prepareStatement("UPDATE humans SET age=?, birthday=? WHERE id = ?");
        subPs.setLong(1, city.getGovernor().getAge());
        subPs.setString(2, city.getGovernor().getBirthday().toString());
        subPs.setLong(3, city.getId());
        subPs.execute();

        ps.execute();
    }

    /**
     * Delete city from database
     *
     * @param id of city
     * @throws SQLException if something got wrong with database
     */
    protected void removeCity(long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM cities WHERE id=?");
        ps.setLong(1, id);
        ps.execute();
        PreparedStatement subPs = connection.prepareStatement("DELETE FROM coordinates WHERE id=?");
        subPs.setLong(1, id);
        subPs.execute();
        subPs = connection.prepareStatement("DELETE FROM humans WHERE id=?");
        subPs.setLong(1, id);
        subPs.execute();
    }

    /**
     * Remove all cities which user has got
     *
     * @param login of user
     * @throws SQLException if something got wrong with database
     */
    protected void clearAll(String login) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT FROM cities * WHERE owner=?");
        ps.setString(1, login);
        ResultSet resultSet = ps.executeQuery();
        ps = connection.prepareStatement("DELETE FROM cities * WHERE owner=?");
        ps.setString(1, login);
        ps.execute();
        PreparedStatement ps1 = connection.prepareStatement("DELETE FROM humans * WHERE id=?");
        ps = connection.prepareStatement("DELETE FROM coordinates * WHERE id=?");
        while (resultSet.next()) {
            ps.setLong(1, resultSet.getLong("id"));
            ps.execute();
            ps1.setLong(1, resultSet.getLong("id"));
            ps1.execute();
        }
    }

    /**
     * Get data controller that create this instance
     *
     * @return data controller
     */
    public DataController getDataController() {
        return dataController;
    }
}
