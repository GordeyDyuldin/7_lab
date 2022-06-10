package client.data_control;

import data_classes.*;
import data_classes.Climate;
import data_classes.Government;
import exceptions.EmptyValueException;
import exceptions.IncorrectValueException;
import exceptions.NullValueException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

/**
 * Control all console data entering by user
 */
public class ConsoleController {

    /**
     * Get city from console input by user
     *
     * @param isFieldCanBeSkipped boolean that can skip all fields
     * @return city from creation by user's console entering
     */
    public City createCityByUser(final boolean isFieldCanBeSkipped) {
        Scanner scanner = new Scanner(System.in);
        City city = new City();
        String input;
        try {
            System.out.println("Создание нового города...");

            System.out.print("Введите имя города: ");
            try {
                city.setName(scanner.nextLine());
            } catch (EmptyValueException e) {
                if (!isFieldCanBeSkipped)
                    throw new EmptyValueException(e.getMessage());
                else
                    System.out.println("Поле было пропущено.");
            }
            System.out.println("Введение координат города...");
            System.out.print("Введите координату x(>-407): ");
            input = scanner.nextLine();
            if (input.equals("") && !isFieldCanBeSkipped)
                throw new EmptyValueException("координата x");
            try {
                city.getCoordinates().setX(Float.parseFloat(input));
            } catch (NumberFormatException e) {
                if (!isFieldCanBeSkipped)
                    throw new IncorrectValueException("координата x - число с плавающей точкой");
                else
                    System.out.println("Поле было пропущено.");
            }
            System.out.print("Введите координату y: ");
            input = scanner.nextLine();
            if (input.equals("") && !isFieldCanBeSkipped)
                throw new EmptyValueException("координата y");
            try {
                city.getCoordinates().setY(Integer.parseInt(input));
            } catch (NumberFormatException e) {
                if (!isFieldCanBeSkipped)
                    throw new IncorrectValueException("координата y - целое число");
                else
                    System.out.println("Поле было пропущено.");
            }

            System.out.print("Введите значение площади всего города: ");
            input = scanner.nextLine();
            if (input.equals("") && !isFieldCanBeSkipped)
                throw new EmptyValueException("площадь города");
            try {
                city.setArea(Long.parseLong(input));
            } catch (NumberFormatException e) {
                if (!isFieldCanBeSkipped)
                    throw new IncorrectValueException("площадь города - целое число");
                else
                    System.out.println("Поле было пропущено.");
            }
            System.out.print("Введите количество жителей города: ");
            input = scanner.nextLine();
            if (input.equals("") && !isFieldCanBeSkipped)
                throw new EmptyValueException("население");
            try {
                city.setPopulation(Integer.parseInt(input));
            } catch (NumberFormatException e) {
                if (!isFieldCanBeSkipped)
                    throw new IncorrectValueException("количество жителей - целое число");
                else
                    System.out.println("Поле было пропущено.");
            }
            System.out.print("Введите высоту над уровнем моря: ");
            input = scanner.nextLine();
            if (input.equals("") && !isFieldCanBeSkipped)
                throw new EmptyValueException("высота над уровнем моря");
            try {
                city.setMetersAboveSeaLevel(Long.parseLong(input));
            } catch (NumberFormatException e) {
                if (!isFieldCanBeSkipped)
                    throw new IncorrectValueException("высота над уровнем моря - целое число");
                else
                    System.out.println("Поле было пропущено.");
            }
            System.out.println("Введение даты основания города...");
            city.setEstablishmentDate(dateCreatorByUser(scanner, "основания города", isFieldCanBeSkipped));

            System.out.print("Введите климат города (");
            System.out.print(Climate.values()[0]);
            for (int i = 1; i < Climate.values().length; i++)
                System.out.print(", " + Climate.values()[i]);
            System.out.print("): ");
            input = scanner.nextLine().toUpperCase();
            if (input.equals("") && !isFieldCanBeSkipped) {
                System.out.println("Поле было пропущено.");
            } else {
                for (Climate i : Climate.values()) {
                    if (i.toString().equals(input)) {
                        city.setClimate(i);
                        break;
                    }
                }
                if (city.getClimateString().equals(""))
                    System.out.println("Значение поля некорректно. Оно было пропущено.");
            }

            System.out.print("Введите тип правительства (");
            System.out.print(Government.values()[0]);
            for (int i = 1; i < Government.values().length; i++)
                System.out.print(", " + Government.values()[i]);
            System.out.print("): ");
            input = scanner.nextLine().toUpperCase();
            if (input.equals("") && !isFieldCanBeSkipped)
                System.out.println("Поле было пропущено.");
            else {
                for (Government i : Government.values()) {
                    if (i.toString().equals(input)) {
                        city.setGovernment(i);
                        break;
                    }
                }
                if (city.getGovernmentString().equals(""))
                    System.out.println("Значение поля некорректно. Оно было пропущено.");
            }

            System.out.println("Введение данных о правителе...");
            System.out.print("Введите возраст правителя: ");
            input = scanner.nextLine();
            if (input.equals("") && !isFieldCanBeSkipped)
                throw new EmptyValueException("возраст правителя");
            try {
                city.getGovernor().setAge(Long.parseLong(input));
            } catch (NumberFormatException e) {
                if (!isFieldCanBeSkipped)
                    throw new IncorrectValueException("возраст - целое число");
                else
                    System.out.println("Поле было пропущено.");
            }
            System.out.println("Введение даты рождения правителя...");
            LocalDate localDate = dateCreatorByUser(scanner, "рождения правителя", isFieldCanBeSkipped);
            LocalTime localTime = localTimeCreateByUser(scanner, "рождения правителя", isFieldCanBeSkipped);
            if (localDate != null && localTime != null)
                city.getGovernor().setBirthday(LocalDateTime.of(localDate, localTime));
        } catch (NullValueException e) {
            e.printStackTrace();
        } catch (IncorrectValueException e) {
            System.out.println("Некорректное значение: " + e.getMessage());
            if (isRepeatCreateCityByUser(scanner))
                return createCityByUser(isFieldCanBeSkipped);
            else {
                System.out.println("Отмена создания города...");
                return null;
            }
        } catch (EmptyValueException e) {
            System.out.println("Поле " + e.getMessage() + " не может быть пустым");
            if (isRepeatCreateCityByUser(scanner))
                return createCityByUser(isFieldCanBeSkipped);
            else {
                System.out.println("Отмена создания города...");
                return null;
            }
        }
        return city;
    }

    /**
     * When city input is false program ask user for repeating entering
     *
     * @param scanner that uses for input by user
     * @return <b>true</b> if user want to repeat city input else <b>false</b>
     */
    private boolean isRepeatCreateCityByUser(final Scanner scanner) {
        System.out.println("Хотите повторить ввод (y/n)? ");
        String input = scanner.nextLine().toLowerCase();
        return input.equals("y");
    }

    /**
     * Create date from user input
     *
     * @param scanner              that uses for entering city by user
     * @param dateName             Name of event. Example: день *рождения*, день *основания* (в родительном падеже)
     * @param isFieldsCanBeSkipped if <b>true</b> this field of city can be skipped
     * @return LocalDate of this event
     * @throws IncorrectValueException if date doesn't exist
     * @throws EmptyValueException     if day, month, year is empty
     */
    private LocalDate dateCreatorByUser(final Scanner scanner, final String dateName,
                                        boolean isFieldsCanBeSkipped)
            throws IncorrectValueException, EmptyValueException {
        int day, month, year;
        String input;
        System.out.print("Введите день " + dateName + "(число): ");
        input = scanner.nextLine();
        if (input.equals("")) {
            if (!isFieldsCanBeSkipped)
                throw new EmptyValueException("день " + dateName);
            else {
                System.out.println("Все поля даты были пропущены.");
                return null;
            }
        }
        try {
            day = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            if (!isFieldsCanBeSkipped)
                throw new IncorrectValueException("день - целое число");
            else {
                System.out.println("Все поля даты были пропущены.");
                return null;
            }
        }
        if (day > 31 || day < 1) {
            if (!isFieldsCanBeSkipped)
                throw new IncorrectValueException("день - число от 1 до 31");
            else {
                System.out.println("Все поля даты были пропущены.");
                return null;
            }
        }
        System.out.print("Введите месяц " + dateName + "(число): ");
        input = scanner.nextLine();
        if (input.equals("")) {
            if (!isFieldsCanBeSkipped)
                throw new EmptyValueException("месяц " + dateName);
            else {
                System.out.println("Все поля даты были пропущены.");
                return null;
            }
        }
        try {
            month = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            if (!isFieldsCanBeSkipped)
                throw new IncorrectValueException("месяц - целое число");
            else {
                System.out.println("Все поля даты были пропущены.");
                return null;
            }
        }
        if (month > 12 || month < 1) {
            if (!isFieldsCanBeSkipped)
                throw new IncorrectValueException("месяц - число от 1 до 12");
            else {
                System.out.println("Все поля даты были пропущены.");
                return null;
            }
        }
        System.out.print("Введите год " + dateName + "(число): ");
        input = scanner.nextLine();
        if (input.equals(""))
            if (!isFieldsCanBeSkipped)
                throw new EmptyValueException("год " + dateName);
            else {
                System.out.println("Все поля даты были пропущены.");
                return null;
            }
        try {
            year = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            if (!isFieldsCanBeSkipped)
                throw new IncorrectValueException("год - целое число");
            else {
                System.out.println("Все поля даты были пропущены.");
                return null;
            }
        }
        if (year <= 0) {
            if (!isFieldsCanBeSkipped)
                throw new IncorrectValueException("год - число положительное");
            else {
                System.out.println("Все поля даты были пропущены.");
                return null;
            }
        }
        if (!isNormalDate(day, month, year)) {
            if (!isFieldsCanBeSkipped)
                throw new IncorrectValueException("невозможная дата");
            else {
                System.out.println("Все поля даты были пропущены.");
                return null;
            }
        }
        return LocalDate.of(year, month, day);
    }

    /**
     * Date validation
     *
     * @param day   integer day of month
     * @param month integer month
     * @param year  integer year (greater than 0)
     * @return <b>true</b> if date can exist else <b>false</b> else
     */
    private boolean isNormalDate(final int day, final int month, final int year) {
        if (month == 2 && day == 29 && year % 4 == 0 && year % 400 == 0)
            return true;
        return ((month != 4 && month != 6 && month != 9 && month != 11) || day <= 30) && (month != 2 || day <= 29) && year > 0;
    }

    /**
     * @param scanner              that uses for data entering by user
     * @param timeName             Name of event. Example: час *рождения* минута *основания* (в дательном падеже)
     * @param isFieldsCanBeSkipped if <b>true</b> this field can be skipped
     * @return LocalTime object
     * @throws IncorrectValueException if hour/minute is incorrect
     * @throws EmptyValueException     if hour/minute is empty
     */
    private LocalTime localTimeCreateByUser(final Scanner scanner, final String timeName, boolean isFieldsCanBeSkipped)
            throws IncorrectValueException, EmptyValueException {
        int minute, hour;
        String input;
        System.out.print("Введите час " + timeName + "(число): ");
        input = scanner.nextLine();
        if (input.equals("")) {
            if (!isFieldsCanBeSkipped)
                throw new EmptyValueException("час " + timeName);
            else {
                System.out.println("Все поля времени были пропущены.");
                return null;
            }
        }
        try {
            hour = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            if (!isFieldsCanBeSkipped)
                throw new IncorrectValueException("час - целое число");
            else {
                System.out.println("Все поля времени были пропущены.");
                return null;
            }
        }
        if (hour < 0 || hour > 23) {
            if (!isFieldsCanBeSkipped)
                throw new IncorrectValueException("час - число от 0 до 23");
            else {
                System.out.println("Все поля времени были пропущены.");
                return null;
            }
        }
        System.out.print("Введите минуту " + timeName + "(число): ");
        input = scanner.nextLine();
        if (input.equals("")) {
            if (!isFieldsCanBeSkipped)
                throw new EmptyValueException("минута " + timeName);
            else {
                System.out.println("Все поля времени были пропущены.");
                return null;
            }
        }
        try {
            minute = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            if (!isFieldsCanBeSkipped)
                throw new IncorrectValueException("минута - целое число");
            else {
                System.out.println("Все поля времени были пропущены.");
                return null;
            }
        }
        if (minute < 0 || minute >= 60) {
            if (!isFieldsCanBeSkipped)
                throw new IncorrectValueException("минута - число от 0 до 59");
            else {
                System.out.println("Все поля времени были пропущены.");
                return null;
            }
        }
        return LocalTime.of(hour, minute);
    }

}
