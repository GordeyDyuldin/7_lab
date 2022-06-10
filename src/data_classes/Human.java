package data_classes;

import exceptions.IncorrectValueException;

import java.io.Serializable;

public class Human implements Serializable {
    /**
     * age of human (need to be greater than zero)
     */
    private Long age; //Значение поля должно быть больше 0

    /**
     * birthday of human
     */
    private java.time.LocalDateTime birthday;

    /**
     * Set age for this human
     *
     * @param age for this human (value is greater than 0)
     * @throws IncorrectValueException if age is not greater than 0
     */
    public void setAge(final Long age) throws IncorrectValueException {
        if (age <= 0)
            throw new IncorrectValueException("Возраст должен быть больше 0");
        this.age = age;
    }

    /**
     * Set birthday for this human
     */
    public void setBirthday(final java.time.LocalDateTime birthday) {
        this.birthday = birthday;
    }

    /**
     * Get age
     */
    public Long getAge() {
        return age;
    }

    /**
     * Get birthday
     */
    public java.time.LocalDateTime getBirthday() {
        return birthday;
    }

    /**
     * Convert human data to string
     *
     * @return string with age and birthday
     */
    @Override
    public String toString() {
        return "\tage: " + age + "\n\tbirthday: " +
                (birthday.getDayOfMonth() < 10 ? "0" : "") + birthday.getDayOfMonth() + "-" +
                (birthday.getMonthValue() < 10 ? "0" : "") + birthday.getMonthValue() + "-" +
                birthday.getYear() + " " +
                (birthday.getHour() < 10 ? "0" : "") + birthday.getHour() + ":" +
                (birthday.getMinute() < 10 ? "0" : "") + birthday.getMinute();
    }
}
