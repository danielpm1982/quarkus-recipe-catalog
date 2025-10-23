# quarkus-recipe-catalog

This project uses Quarkus, the Supersonic Subatomic Java Framework.

From the root of the app, at the console...

For building and running the app using the traditional jvm Profile (default):
1) Firstly, comment in all package imports and their uses at the code regarding org.graalvm.polyglot dependency - as this is considered when using the classic jvm Profile. This dependency is totally supported and compatible when using jvm Profile;
2) Building:<br>
   ./mvnw -Pjvm clean package<br>
   or<br>
   ./mvnw clean package
3) Running:<br>
   ./mvnw quarkus:dev<br>
   or<br>
   ./mvnw quarkus:run<br>
   or<br>
   java -jar ./target/quarkus-app/quarkus-run.jar

For building and running the app using the native-image Profile:
1) Firstly, comment out all package imports and their uses at the code regarding org.graalvm.polyglot dependency - as this is only considered when using the classic jvm Profile. This dependency is not supported or compatible when using native-image Profile;
2) Building:<br>
   ./mvnw -Pnative clean package<br>
   or<br>
   ./mvnw -Dnative clean package<br>
3) Running:<br>
   ./target/quarkus-recipe-catalog-1.0.0-SNAPSHOT-runner<br>
   or<br>
   java -jar ./target/quarkus-recipe-catalog-1.0.0-SNAPSHOT-native-image-source-jar/quarkus-recipe-catalog-1.0.0-SNAPSHOT-runner.jar

PS.: This README.md will be updated with a better presentation soon.
