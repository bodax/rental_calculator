## Run the Rent Calculator from IntelliJ IDEA or Terminal

You can run the program using Gradle with sample rental records:

```bash
./gradlew run --args="\"E-Van, 95 km, 20 kWh, Motorway Vignette, Gubrist Tunnel x3\" 
\"Compact Van, 40 km, 5 liters\" 
\"Large Van, 180 km, 15 liters, City Congestion Fee (30 km city driving)\""
```

Run as a JAR file:

#### Building the JAR file

```bash
./gradlew clean build
```
Move to the build directory

```bash 
cd build/libs
```

```bash
 java -jar .\rent_calculator-0.1.jar 'E-Van, 95 km, 20 kWh, Motorway Vignette, Gubrist Tunnel x3', 'Compact Van, 40 km, 5 liters','Large Van, 180 km, 15 liters, City Congestion Fee 30 km city driving'
 ```

#### Run tests:

```bash
./gradlew test
```
