# quarkus-recipe-catalog
This is a simple Java Web Service project that exemplifies how to use Quarkus, GraalVM, Truffle, Panache, JWT and Observability tracing tools, in order to run and monitor a Polyglot (Java + Python) Rest Web Application, with basic CRUD (Postgres) Persistence

© 2025 Daniel Pinheiro Maia All Rights Reserved<br>
(see Copyright© License at the end of this text).

[**Description of this repository**]<br>
This is a simple Java Web Service project that exemplifies how to use Quarkus, GraalVM, Truffle, Panache, JWT and Observability tracing tools, in order to run and monitor a Polyglot (Java + Python) Rest Web Application, with basic CRUD (Postgres) Persistence. It also shows how to compile your code both using traditional compilation (JVM bytecode), as well as using native-code compilation (processor-level binary code).

Here we present a REST Web Service, written in Java and Python, and built using Quarkus framework + Maven + Oracle GraalVM. GraalVM's JIT (Just-In-Time) and AOT (Ahead-Of-Time) compilers are used in compilation. GraalVM is a high-performance Java Virtual Machine - with its runtime and compilers - which not only supports Java, Scala, and Groovy, but also enables the execution of other popular programming languages in one same application. This polyglot capability is made possible by additional frameworks, such as Truffle and Sulong, which can be integrated with GraalVM.

With GraalVM, you can run code written in C, C++, Node.js, JavaScript, Python, R, Ruby, and more, all within the same Java Virtual Machine (JVM). This eliminates the considerable overhead typically associated with the communication between different VMs and with serialization/deserialization, as there’s no need to exchange serialized messages over HTTP between different-language apps running in separate VMs. As a result, both the compilation and execution of this single polyglot code are significantly faster and more interoperable, allowing for seamless sharing of resources, libraries and tools across multiple languages, inside one single app and runtime platform.

When using GraalVM, you can opt for traditional compilation of your source code to JVM bytecode (OS and platform-agnostic) or directly compiling the same source code into a native machine code executable (OS and platform-specific), by using the GraalVM native-image builder. Quarkus performance optimizations and extensions (officially validated dependencies) do not depend on which compiling strategy you choose, and are fully compatible with both. Additional optimizations are present, though, when using GraalVM native-image compiling. 

With native images, you don’t need a JVM to run your application - it's machine code already. The application will start or restart in milliseconds on the embedded server (JBoss by default). All the overhead is shifted to the build phase, with AOT compilation, and once the native image is built, you have an executable binary that runs quite faster, demanding far less resources, at the machine processor level, without requiring JVM, JIT or further interpreters or compilers. Additionally, as only the classes effectively used (at runtime) are selected for AOT compilation, the final native image is much shorter in size, compared to traditional JVM compiled artifacts. Security is also enhanced, with reduced attack surface and fewer exposed components. All resources needed at runtime are embedded into the final native image, including your own classes, JDK classes, external dependency classes, static resources, in-built servers and a micro-runtime environment called Substrate VM. For the image to run, there's no need of classloading - class loading, linking and initialization is already done at the built image. And, as said, there's no bytecode being interpreted by JVMs or JIT compilation at runtime. Even part of the heap is generated at the build phase. Everything at the image is ready to be run at once, as machine code, by your machine processor. This way, memory footprint is significantly reduced as well, as there's no metadata for loading classes at runtime, no profiling data for JIT optimizations, no cache for interpreted bytecode and no JIT structures. Although OS and architecture-specific, with Quarkus containers-first approach, native images can be easily deployed at any machine with Docker or Kubernetes support - specially at Cloud Servers - without the need of compiling multiple native images, one for each OS or hardware. Write once, run everywhere maintains, although with a different architecture solution (using containers instead of traditional JVMs). 

Native images are specially useful when running microservices, serverless architectures (e.g. Cloud lambda functions), resource-constrained environments, command-line (CLI) tools, high-performance applications (real-time), as well as for testing and prototyping. The disadvantages of native images are: a much bigger overhead in the build phase, with more resources and complexity needed at this particular phase - compensated by the light-weight execution at runtime. As we only need to build once, for running multiple times, that trade-off generally is positive. If you change anything on the source code though, you need to rebuild the native image - because it's immutable, it can't be changed or optimized after the build is complete. Also, you might face some issues if you need to use reflection, JIT or dynamic class loading features at your code. Additionally, there's no support for head or thread dump (at least on GraalVM Community Edition). JVM features, as JVM Tool Interface, Java Agents and JMX are not supported, as well. Another issue is the Garbage Collector - which is a serial limited one - not a full JVM GC - and thus may freeze your application if its heap is too large (you gotta keep the smallest possible heap size)... that makes native images unsuitable for big monolithic enterprise applications, which inevitably demand high dynamic memory allocation. Community support is also limited compared to traditional JVM compilation support. Finally, there's no support yet for polyglot code when using native-image compilation - if you need polyglot support, you gotta use the traditional compilation to JVM bytecode, at least for now. Hope they solve that soon...

This example project demonstrates both the traditional compilation approach (with polyglot support via GraalVM+Truffle) as the native image compilation approach (without a JVM). You can observe the performance differences at each lifecycle phase and evaluate the trade-offs based on your needs.

In terms of business logic, this app basically creates a Rest Client, i.e., a proxy, that access an external recipe app API, and returns one or more recipes as a JSON. This Rest Client is configured as a Service interface, which is injected at concrete Resource classes, that, in turn, call the methods from this Service: one for retrieving all recipes and another for retrieving one recipe per id. A Response object is then mounted and returned (for each endpoint), based on the String responses from Service Rest Client proxy methods, containing the JSON data from the external API calls. Fault tolerance management is fully implemented, with features as: timeout, fallback methods, circuit breakers, liveness and readiness check. 

Regarding Persistence, MyClient entity data is mapped into a local PostgreSQL DB, which runs in a local container, with the help of Testcontainers, from Quarkus Dev Services. Panache framework (which is implemented on top of Hibernate / JPA - ORM) is used here to implement an easy CRUD (Repository), used by MyClientResource endpoints directly (we should create a Service interface between, in a real-business app). This DB keeps the client id, name, email and a list of favorite recipes' ids at persistent data registries. By using these locally-stored favorite recipes' ids, one is able to fetch (at a frontend app, for instance) each client's favorite recipe from the remote API, through the Rest Client proxy - so that you'd have both MyClient data as each client's favorite recipes to display altogether. Resource endpoints, for managing the remote API recipes, as well as the local MyClient entities (and DB registries), have been implemented. You can test these through the Swagger interface, as below described. Again, in a real business app, a better separation of layers should be designed, regarding the Entities, Repositories, Services and Controllers - as you'd have a lot more classes, entities and services to manage. The objective here is simply to show the basics of Quarkus, GraalVM, Truffle and Panache.

As to Security, Json Web Token (JWT), along with public/private RSA-256 encryption keys, has been chosen for securing Http endpoints using bearer token authorization and role-based access control (RBAC). Quarkus own-validated extensions (and not Spring ones) should be used here, which are in accordance with Jakarta EE and MicroProfile specifications. There are other libs and Security implementation flows, with different specific extensions (e.g. quarkus-oidc), which could be used as well, depending on architecture decisions. OAUTH2 could also be added. Here, we used the simplest and most native way of implementing Security for Quarkus web apps. Read the [Red Hat documentation](https://docs.redhat.com/en/documentation/red_hat_build_of_quarkus/3.20/html/microprofile_json_web_token_jwt_authentication/security-jwt) for more.

For Observability (logging, tracing, metrics), of both the REST app endpoints as of the SQL JDBC/JPA DB calls, OpenTelemetry, Quarkus Micrometer Metrics (Prometheus) and JAEGER UI have been used. The first two as Quarkus extensions, the latter as a local container through the command below (you can use either podman or docker, likewise). These are essential observability capabilities, specially for modern cloud-native microservice apps (distributed apps), and have been created or adopted as official projects (among many others) by the [Cloud Native Computing Foundation (CNCF)](https://insights.linuxfoundation.org/collection/cncf), which, in turn, is part of the [Linux Foundation](https://www.linuxfoundation.org).<br>
> podman run --name=jaeger -d -p 16686:16686 -p 4317:4317 -e COLLECTOR_OTLP_ENABLED=true jaegertracing/all-in-one:latest

[**Content and Run**]<br>
Source code available at github.com, through the following link:<br>
[https://github.com/danielpm1982/quarkus-recipe-catalog](https://github.com/danielpm1982/quarkus-recipe-catalog)<br>

After cloning this project, configuring a project module at your IDE (e.g. IDEA IntelliJ) that points out to your GraalVM installation (e.g. graalvm-jdk-21.0.8+12.1), and from the root folder of this app, at your console...

For building and running the app using the traditional jvm Maven Profile (default):
1) Firstly, comment in all package imports and their uses at the code regarding org.graalvm.polyglot extension - as this is considered when using the classic jvm Profile. This dependency is totally supported and compatible when using jvm Profile. Also, comment out any DBMS extension at pom.xml (including H2), except for PostgreSQL. Comment in the line at application.properties that sets the kind of datasource to postgresql. If you wanna connect the app with a real DB (at Production), also set its url manually (Quarkus won't use Dev Services or Testcontainers, if you do that). 
2) Building:<br>
   ./mvnw -Pjvm clean package<br>
   or<br>
   ./mvnw clean package
3) Running:<br>
   ./mvnw quarkus:dev<br>
   (this runs the mvn profile jvm in "dev" mode, either with H2 or PostgreSQL)<br>
   or<br>
   ./mvnw quarkus:run<br>
   (this runs the mvn profile jvm in "production" mode, either with H2 or PostgreSQL)<br>
   or<br>
   java -jar ./target/quarkus-app/quarkus-run.jar<br>
   (running the built jar directly with "java -jar" only works if you're running H2 DBMS, or if you have already started manually your PostgreSQL DBMS with the DB - it won't start Testcontainers automatically, differently from if you start the app by using Quarkus plugin, as above - which are preferable)

If running on the "dev" mode, you can visualize quarkus dev UI at:<br>
http://localhost:8080/q/dev-ui <br>
If running either on the "dev" or "production" modes, you can visualize Swagger-UI interface at (you can also disable it for production at application.properties file):<br>
http://localhost:8080/q/dev-ui/quarkus-smallrye-openapi/swagger-ui <br>
If running either on the "dev" or "production" modes, you can also visualize the following traceability endpoints, as well (you gotta have already started JAEGER UI container manually as above described):<br>
http://localhost:16686/search <br>
http://localhost:8080/q/metrics <br>

For building and running the app using the native Maven Profile (always on "production" mode - there's no "dev" mode here):
1) Firstly, comment out all package imports and their uses at the code regarding org.graalvm.polyglot extension - as this is only considered when using the classic jvm Profile. This dependency is not supported or compatible when using native-image Profile. Also, as there is no Dev Services or Testcontainers at native images, you gotta create and start the DBMS/DB manually. If you wanna use H2, comment in its extension at the pom.xml and its props at application.properties (comment out all props for PostgreSQL). Conversely, if you wanna use PostgreSQL, comment in its extension at pom.xml and its kind of datasource at the application.properties (comment out all props for H2).     
2) Building:<br>
   ./mvnw -Pnative clean package<br>
   or<br>
   ./mvnw -Dnative clean package<br>
3) Running:<br>
   ./target/quarkus-recipe-catalog-1.0.0-SNAPSHOT-runner<br>
   (this is a standalone executable native image which can be shared anywhere else, for running natively at similar-architecture machines - does not depend on JVMs or Java)<br>
   or<br>
   java -jar ./target/quarkus-recipe-catalog-1.0.0-SNAPSHOT-native-image-source-jar/quarkus-recipe-catalog-1.0.0-SNAPSHOT-runner.jar <br>
   (both running cases above only work if you're running H2 DBMS, or if you have already started manually your PostgreSQL DBMS with the DB - running a native image or its corresponding running jar won't start Testcontainers automatically. DBMS containers are not in-built at native images - only in the case of H2)

More about Quarkus and GraalVM at:<br>
https://quarkus.io and https://www.graalvm.org .

[**Printscreen samples**]<br>

![graalvm-polyglot-code.png](https://raw.githubusercontent.com/danielpm1982/quarkus-recipe-catalog/refs/heads/master/img/graalvm-polyglot-code.png)

![dev-ui.png](https://raw.githubusercontent.com/danielpm1982/quarkus-recipe-catalog/refs/heads/master/img/dev-ui.png)

![health-check.png](https://raw.githubusercontent.com/danielpm1982/quarkus-recipe-catalog/refs/heads/master/img/health-check.png)

![swagger-ui.png](https://raw.githubusercontent.com/danielpm1982/quarkus-recipe-catalog/refs/heads/master/img/swagger-ui.png)

![java+python-output.png](https://raw.githubusercontent.com/danielpm1982/quarkus-recipe-catalog/refs/heads/master/img/java%2Bpython-output.png)

![recipes-output.png](https://raw.githubusercontent.com/danielpm1982/quarkus-recipe-catalog/refs/heads/master/img/recipes-output.png)

![secure-endpoint-call.png](https://raw.githubusercontent.com/danielpm1982/quarkus-recipe-catalog/refs/heads/master/img/secure-endpoint-call.png)

![secure-endpoint-call2.png](https://raw.githubusercontent.com/danielpm1982/quarkus-recipe-catalog/refs/heads/master/img/secure-endpoint-call2.png)

![authenticate-token.png](https://raw.githubusercontent.com/danielpm1982/quarkus-recipe-catalog/refs/heads/master/img/authenticate-token.png)

![dev-services-postgresql.png](https://raw.githubusercontent.com/danielpm1982/quarkus-recipe-catalog/refs/heads/master/img/dev-services-postgresql.png)

![dev-services-postgresql2.png](https://raw.githubusercontent.com/danielpm1982/quarkus-recipe-catalog/refs/heads/master/img/dev-services-postgresql2.png)

![jaeger.png](https://raw.githubusercontent.com/danielpm1982/quarkus-recipe-catalog/refs/heads/master/img/jaeger.png)

![jaeger2.png](https://raw.githubusercontent.com/danielpm1982/quarkus-recipe-catalog/refs/heads/master/img/jaeger2.png)

![jaeger3.png](https://raw.githubusercontent.com/danielpm1982/quarkus-recipe-catalog/refs/heads/master/img/jaeger3.png)

![jaeger4.png](https://raw.githubusercontent.com/danielpm1982/quarkus-recipe-catalog/refs/heads/master/img/jaeger4.png)

![jaeger-request.png](https://raw.githubusercontent.com/danielpm1982/quarkus-recipe-catalog/refs/heads/master/img/jaeger-request.png)

![jaeger-connection.png](https://raw.githubusercontent.com/danielpm1982/quarkus-recipe-catalog/refs/heads/master/img/jaeger-connection.png)

![jaeger-sql.png](https://raw.githubusercontent.com/danielpm1982/quarkus-recipe-catalog/refs/heads/master/img/jaeger-sql.png)

![micrometer-prometheus.png](https://raw.githubusercontent.com/danielpm1982/quarkus-recipe-catalog/refs/heads/master/img/micrometer-prometheus.png)

[**Support**]<br>
If you have any suggestion or correction about the content of this repository, please, feel free to open an issue at the project issues' section:<br>
https://github.com/danielpm1982/quarkus-recipe-catalog/issues

[**Copyright© License**]<br>
© 2025 Daniel Pinheiro Maia All Rights Reserved<br>
This GitHub repository - and all code (software) available inside - is exclusively for academic and individual learning purposes, and is **NOT AVAILABLE FOR COMMERCIAL USE**, nor has warranty of any type. You're authorized to fork, clone, run, test, modify, branch and merge it, at your own risk and using your own GitHub account, for individual learning purposes only, but you're **NOT ALLOWED to distribute, sublicense and/or sell copies of the whole or of parts of it** without explicit and written consent from its owner / author. You can fork this repository to your individual account at GitHub, clone it to your personal notebook or PC, analyse, run and test its code, modify and extend it locally or remotely (exclusively at your own GitHub account and as a forked repository), as well as send issues or pull-requests to this parent (original) repository for eventual approval. GitHub is in charge of explicitly showing whom this respository has been forked from. **If you wish to use any of this repository content in any way other than what is expressed above, or publish it anyway or anywhere other than as a forked repository at your own GitHub account, please contact this repository owner / author** using GitHub or the contact info below. For the meaning of the technical terms used at this license, please refer to GitHub documentation, at https://help.github.com/en/github .

[**Owner and Author of this GitHub Repository**]<br>
Daniel Pinheiro Maia<br>
[danielpm1982.com](https://www.danielpm1982.com)<br>
danielpm1982@gmail.com<br>
[linkedin.com/in/danielpm1982](https://www.linkedin.com/in/danielpm1982)<br>
Brazil<br>
.
