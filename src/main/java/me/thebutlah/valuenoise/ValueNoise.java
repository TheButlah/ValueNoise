package me.thebutlah.valuenoise;

import net.openhft.hashing.LongHashFunction;
import java.util.*;

public class ValueNoise {

    private final int recursions;
    private final double persistence;
    private final long seed;
    private final LongHashFunction hashFunction;
    private final HashMap<Point, Double> randomValues;

    public ValueNoise(int recursions, double persistence, Long seed, int pointsPerSide) {
        this.recursions = recursions;
        this.persistence = persistence;
        if (seed == null) {
            this.seed = (new Random()).nextLong();
        } else {
            this.seed = seed;
        }
        //Before any recursions, there will be numPoints unique integer inputs. There will be 2^(recursions*2) inputs per unique integer input.
        int size = (int)(Math.pow(2, pointsPerSide + 1) * Math.pow(2, 2*(recursions-1)));
        randomValues = new HashMap<>(size);
        hashFunction = LongHashFunction.xx_r39(this.seed);
    }

    public ValueNoise(int recursions, Long seed, int numPoints) {
        this(recursions, 0.5, seed, numPoints);
    }

    private static int intfunc; //these two vars are up here so that they can be used in methods down the line.



    public double generateValueNoise2D(double x, double y, int interpolationfunc) { //higher persistence = lower amplitude (I reccomend using around 1/2), more recursions = more detail
        double finalvalue = 0;
        double range = 0;
        intfunc = interpolationfunc;
        for (int i=0; i<recursions; i++) {
            int frequency = (int) Math.pow(2, i);		//multiply the frequency of 2 every recursion
            double amplitude = Math.pow(persistence, i);//multiply the persistence by itself every recursion
            range += amplitude; //calculating max or min value the final output can be
            finalvalue += interpolatedNoise(x * frequency, y * frequency) * amplitude;
        }
        return finalvalue/range; //divide by range to ensure output is between -1 and 1
    }


    private double randomNoise(int... values) { //generates the initial random points
        Point point = new Point(values);
        //tempSeed = Integer.hashCode(y) | (tempSeed<<40); //make the seed into the least significant digits of both x and y
        return randomValues.computeIfAbsent(point, this::genRand);
        //System.out.format("X: %d, Y: %d, Seed: %s\n", x, y, Long.toBinaryString(tempSeed));
    }

    private double genRand(Point point) {
        double temp = hashFunction.hashInts(point.pos);
        double result = temp/Long.MAX_VALUE;
        return result;
    }


    private double interpolatedNoise(double x, double y) { //interpolates the points using a cosine interpolation
        int x_int = (int) x;			//splitting x into its integer and decimal components
        double x_decimal = x - x_int;

        int y_int = (int) y;			//doing the same to y
        double y_decimal = y - y_int;

        byte signX;
        byte signY;

        if(x_decimal >= 0) {
            signX = 1;
        } else {
            signX = -1;
        }

        if (y_decimal >= 0) {
            signY = 1;
        } else {
            signY = -1;
        }

        double a1 = randomNoise(x_int, y_int);			//making a square using consecutive x and y points
        double b1 = randomNoise(x_int + signX, y_int);
        double a2 = randomNoise(x_int, y_int + signY);
        double b2 = randomNoise(x_int + signX, y_int + signY);

        double i1 = interpolate(a1, b1, Math.abs(x_decimal), intfunc);	//interpolating both sides of the square
        double i2 = interpolate(a2, b2, Math.abs(x_decimal), intfunc);

        return interpolate(i1, i2, Math.abs(y_decimal), intfunc); //now interpolating between the two sides
    }

    private static double interpolate(double a, double b, double x, int interpolationfunc) { //x will be between 0 and 1, with 1 being the b value and 0 being the a value
        x = Math.abs(x);
        if (interpolationfunc == 1) {
            //a linear function will be used to smooth the connections between the points. (Fastest)
            return a*(1-x) + b*x;
        } else if (interpolationfunc == 2) {
            //a cosine formula is used to smooth the connections between the points
            double theta = x * Math.PI; 			//essentially converts the decimal between 0 and 1 to a value on the unit circle between 0 and PI
            double f = (1-Math.cos(theta)) * 0.5; 	//converts cos(theta) from giving -1 to 1 into it giving 0 (theta = 0) to 1 (theta = PI)
            return a * (1-f) + b * (f); 			//because we want it to return a if we originally inputted 0, and b for 1, then be have to do this line of code
        }
        return 0;
    }

}