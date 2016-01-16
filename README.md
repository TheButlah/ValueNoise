# ValueNoise
An implementation of a Value Noise generator in Java. This project began as a school project and has been updated to make use of many Java 8 features to improve upon performance.

### What is Value Noise?
Value noise is a type of noise function. A noise function can be thought of as another term for a random number generator. However, not all noise functions are created equally. Value noise is a type of noise function that has several important properties:

    -It is a coherent noise function, meaning that its values flow continuously and can accept decimal inputs
    -It is fractal in nature, meaning that it has self-similar properties that repeat at any detail/zoom level
    -It is highly customizeable, allowing the user to fine-tune the properties of the noise generator

Value noise is well suited to generating heightmaps, textures, particle effects, terrain (both for 2D and 3D applications), volumetric clouds that change over time, and more.

### How do I build this library and use it?
This project uses gradle as its build tool. If you don't already have gradle, you should follow the directions to download it from http://gradle.org/gradle-download/. You will also need to have both the JRE and JDK for Java 8 or later installed. Java 8 added a lot of cool features to Java, and this project uses them. If you haven't installed it yet, go get it.

Once gradle is installed, go ahead and clone this repo to whatever directory you choose, then run <b>gradle jar</b> to get the jarfile, which will be under build/libs/valuenoise-VERSION.jar
At this point, you can use this jarfile in any projects you may wish, by adding it to your IDE. Then go ahead and see for yourself the cool features that ValueNoise provides!