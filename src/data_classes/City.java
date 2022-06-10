package data_classes;

import exceptions.EmptyValueException;
import exceptions.IncorrectValueException;
import exceptions.NullValueException;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public class City implements Comparable<City>, Serializable {
    /**
     * unique id of this city
     * <p>Can't be null, value is unique and greater than zero</p>
     * <p>Generate automatically</p>
     */
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля
    // должно быть уникальным, Значение этого поля должно генерироваться автоматически

    /**
     * name of this city
     * <p>Can't be null and empty</p>
     */
    private String name; //Поле не может быть null, Строка не может быть пустой

    /**
     * coordinates of this city
     * <p>Can't be null</p>
     * <p>Create with constructor of city</p>
     */
    private final Coordinates coordinates = new Coordinates(); //Поле не может быть null

    /**
     * date of creation city object
     * <p>Can't be null</p>
     * <p>Generate automatically</p>
     */
    private java.time.ZonedDateTime creationDate = java.time.ZonedDateTime.now();//Поле не может быть null,
    // Значение этого поля должно генерироваться автоматически

    /**
     * area of city
     * <p>Value is greater than zero</p>
     */
    private long area; //Значение поля должно быть больше 0

    /**
     * population of city
     * <p>Value is greater than zero</p>
     */
    private int population; //Значение поля должно быть больше 0
    /**
     * meters above sea level of this city
     */
    private Long metersAboveSeaLevel;

    /**
     * date of establish city
     */
    private java.time.LocalDate establishmentDate;

    /**
     * climate of city
     * <p>Can't be null</p>
     */
    private Climate climate; //Поле может быть null

    /**
     * government of city
     * <p>Can't be null</p>
     */
    private Government government; //Поле может быть null

    /**
     * governor of city
     * <p>Can't be null</p>
     * <p>Create in constructor of city</p>
     */
    private final Human governor = new Human(); //Поле не может быть null

    public City() {

    }
    // подаётся уже уникальный ID!

    /**
     * Set id value in object
     *
     * @param id unique id in desired collection for city
     * @throws NullValueException      if input is null
     * @throws IncorrectValueException if input value is incorrect (&lt;= 0)
     */
    public void setId(final Long id) throws NullValueException, IncorrectValueException {
        if (id == null)
            throw new NullValueException();
        if (id <= 0)
            throw new IncorrectValueException("значение ID должно быть больше 0");
        this.id = id;
    }

    /**
     * Get id of this city
     */
    public Long getId() {
        return id;
    }

    /**
     * Set name for this city (value is not empty and not null)
     *
     * @param name of this city
     * @throws NullValueException  if name is null
     * @throws EmptyValueException if name is empty
     */
    public void setName(final String name) throws NullValueException, EmptyValueException {
        if (name == null)
            throw new NullValueException();
        if (name.equals(""))
            throw new EmptyValueException("имя");
        this.name = name;
    }

    /**
     * Get name of this city
     */

    public String getName() {
        return name;
    }

    /**
     * Get Coordinates object
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Set CreationDate for this city (always create automatically); value is not null
     *
     * @param creationDate date of city creation
     * @throws NullValueException if creationDate is empty
     */
    public void setCreationDate(final java.time.ZonedDateTime creationDate) throws NullValueException {
        if (creationDate == null)
            throw new NullValueException();
        this.creationDate = creationDate;
    }

    /**
     * Get CreationDate of this city
     */
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Set Area
     *
     * @param area of city (values is greater than 0)
     * @throws IncorrectValueException if area is not greater than 0
     */
    public void setArea(final long area) throws IncorrectValueException {
        if (area <= 0)
            throw new IncorrectValueException("значение площади должно быть больше 0");
        this.area = area;
    }

    /**
     * Get area
     */
    public long getArea() {
        return area;
    }

    /**
     * Set population
     *
     * @param population of city (values is greater than 0)
     * @throws IncorrectValueException if population is not greater than 0
     */
    public void setPopulation(final int population) throws IncorrectValueException {
        if (population <= 0)
            throw new IncorrectValueException("значение числа жителей должно быть больше 0");
        this.population = population;
    }

    /**
     * Get population of this city
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Set meters above sea level
     */
    public void setMetersAboveSeaLevel(final Long metersAboveSeaLevel) {
        this.metersAboveSeaLevel = metersAboveSeaLevel;
    }

    /**
     * Get meters above sea level
     */
    public Long getMetersAboveSeaLevel() {
        return metersAboveSeaLevel;
    }

    /**
     * Set establishment date for this city
     */
    public void setEstablishmentDate(final java.time.LocalDate establishmentDate) {
        this.establishmentDate = establishmentDate;
    }

    /**
     * Get establishment date
     */
    public LocalDate getEstablishmentDate() {
        return establishmentDate;
    }

    /**
     * Set climate for this city
     */
    public void setClimate(final Climate climate) {
        this.climate = climate;
    }

    /**
     * Set climate
     *
     * @param climate that transform to enum type
     */
    public void setClimate(String climate) {
        if (climate == null)
            return;
        for (Climate cl : Climate.values()) {
            if (climate.equals(cl.name())) {
                this.climate = cl;
                break;
            }
        }
    }

    /**
     * Get climate in string (upper case)
     *
     * @return <b>empty string</b> if climate is null else return <b>climate.toString()</b>
     */
    public String getClimateString() {
        if (climate == null)
            return "";
        return climate.toString();
    }

    /**
     * Get climate
     *
     * @return climate
     */
    public Climate getClimate() {
        return climate;
    }

    /**
     * Set government
     */
    public void setGovernment(final Government government) {
        this.government = government;
    }

    /**
     * Set government
     *
     * @param government that convert to enum type
     */
    public void setGovernment(String government) {
        if (government == null)
            return;
        for (Government gov : Government.values()) {
            if (government.equals(gov.name())) {
                this.government = gov;
                break;
            }
        }
    }

    /**
     * Get government in string (upper case)
     *
     * @return <b>empty string</b> if government is null else return <b>government.toString() </b>
     */
    public String getGovernmentString() {
        if (government == null)
            return "-";
        return government.toString();
    }

    /**
     * Get government
     */
    public Government getGovernment() {
        return government;
    }

    /**
     * Get governor
     */
    public Human getGovernor() {
        return governor;
    }

    /**
     * Transform date to string (human viewable)
     *
     * @return string of this date
     */
    private String creationDateToString() {
        return (creationDate.getDayOfMonth() < 10 ? "0" : "") + creationDate.getDayOfMonth() + "-" +
                (creationDate.getMonthValue() < 10 ? "0" : "") + creationDate.getMonthValue() +
                "-" + creationDate.getYear() + " " +
                (creationDate.getHour() < 10 ? "0" : "") + creationDate.getHour() + ":" +
                (creationDate.getMinute() < 10 ? "0" : "") + creationDate.getMinute() + ":" +
                (creationDate.getSecond() < 10 ? "0" : "") + creationDate.getSecond() + "." +
                creationDate.getNano() + " Zone: " + creationDate.getOffset().toString() + " "
                + creationDate.getZone().toString();
    }

    /**
     * Get all data of city in string
     *
     * @return string with all fields of city
     */
    @Override
    public String toString() {
        return "id: " + id + "\n" +
                "name: " + name + "\n" +
                "coordinates:\n" + coordinates + "\n" +
                "creationDate: " + creationDateToString() + "\n" +
                "area: " + area + "\n" +
                "population: " + population + "\n" +
                "metersAboveSeaLevel: " + metersAboveSeaLevel + "\n" +
                "establishmentDate: " + establishmentDate + "\n" +
                "climate: " + (climate != null ? climate : "-") + "\n" +
                "government: " + (government != null ? government : "-") + "\n" +
                "governor:\n" + governor + "\n";
    }

    @Override
    public int compareTo(City o) {
        return (id < o.id ? -1 : (id.equals(o.id)) ? 0 : 1);
    }
}
