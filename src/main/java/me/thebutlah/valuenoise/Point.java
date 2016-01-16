package me.thebutlah.valuenoise;

import java.util.Arrays;

/**
 * Created by ryan on 1/15/16.
 */
public class Point {
    public final int[] pos;
    public Point (int... values) {
        this.pos = values;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(pos);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof Point))  return false;
        Point otherPoint = (Point) other;
        return Arrays.equals(otherPoint.pos, this.pos);
    }
}
