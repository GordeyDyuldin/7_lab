package data_classes;

import exceptions.IncorrectValueException;
import exceptions.NullValueException;

import java.io.Serializable;

public class Coordinates implements Serializable {
    /**
     * Flag value for checking initialization
     */
    public final static float X_INIT_VALUE = -407f;

    /**
     * x coordinate of city that need to be greater than -407.0
     * <p>If not init x is @see X_INIT_VALUE</p>
     */
    private float x = X_INIT_VALUE; //Значение поля должно быть больше -407

    /**
     * y coordinate of city
     * <p>Can't be null</p>
     */
    private Integer y; //Поле не может быть null

    /**
     * Set x
     *
     * @param x is greater than -407
     * @throws IncorrectValueException if x is lower/equal -407
     */
    public void setX(final float x) throws IncorrectValueException {
        if (x <= -407)
            throw new IncorrectValueException("Значение координаты X должно быть больше -407");
        this.x = x;
    }

    /**
     * Set y
     *
     * @param y not null
     * @throws NullValueException if y is null
     */
    public void setY(final Integer y) throws NullValueException {
        if (y == null)
            throw new NullValueException();
        this.y = y;
    }

    /**
     * Get x coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * Get y coordinate
     */
    public Integer getY() {
        return y;
    }

    /**
     * documentation
     * Convert coordinates data to string
     *
     * @return string with coordinate's data
     */
    @Override
    public String toString() {
        return "\tx: " + x + "" + "\n\ty: " + y;
    }
}
