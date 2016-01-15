package me.thebutlah.valuenoise;

import java.util.Random;

public class ValueNoise {
    private static int intfunc; //these two vars are up here so that they can be used in methods down the line.
    private static int function; //this var is to be used as a sort of counter to switch between different random functions every recursion
    public static double generateValueNoise2D(double x, double y, int recursions, double persistence, int interpolationfunc, double seed) { //higher persistence = lower amplitude (I reccomend using around 1/2), more recursions = more detail
        double finalvalue = 0;
        double range = 0;
        intfunc = interpolationfunc;
        function = -1;
        for (int i=0; i<recursions; i++) {
            function++;
            int frequency = (int) Math.pow(2, i);		//multiply the frequency of 2 every recursion
            double amplitude = Math.pow(persistence, i);//multiply the persistence by itself every recursion
            range += amplitude; //calculating max or min value the final output can be
            finalvalue = finalvalue + interpolatedNoise((x + seed) * frequency, (y + seed) * frequency) * amplitude;
            if (function >=1) function = 0;
        }
        return finalvalue/range; //divide by range to ensure output is between -1 and 1
    }


    private static double randomNoise(int x, int y) { //generates the initial random points
        if (function == 1) {
            Random random = new Random(x + y*57);
            random.nextDouble();
            return random.nextDouble() * 2 - 1;
        } else {
            int n = x + y*57;
            n = (n<<13) ^ n;
            return (1.0 - ((n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0);
        }
    }


    private static double interpolatedNoise(double x, double y) { //interpolates the points using a cosine interpolation
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