# Backend

Spring Boot backend application.

See `backend/AGENTS.md` before implementing backend tasks.

## Project Structure

Backend documentation lives in this folder. Spring Boot applications live in
dedicated subfolders so this area can host multiple backend applications over
time.

The first application is `ms-family-360`, a Maven reactor named `family-360`.

Current `ms-family-360` modules:

- `family-360-application`: deployable Spring Boot application. It depends on
  the functional modules that are exposed by the backend.
- `family-360-persistence`: shared persistence module for JPA entities,
  repositories, and database-specific configuration. Database migrations are
  owned independently under `database/`.
- `family`: functional module for the family bounded area.

The intended direction is to keep each functional area in its own Maven module
inside the application reactor. The deployable application module composes those
functional modules and the shared persistence module.

## Requirements

The backend is intended to run with:

- Java 25
- Maven 3.9 or newer

Java 25 is the target runtime for the Spring Boot backend. Maven is required to
build, test, package, and run the project from the command line.

Verify the local installation:

```bash
java -version
mvn -version
```

The Java version should report `25.x`. The Maven output should show the Maven
version and the Java runtime Maven is using.

## Installing Java 25

Install a Java 25 JDK, not only a JRE. Any standard JDK distribution is suitable
as long as it provides Java 25.

Common options:

- Oracle JDK: https://www.oracle.com/java/technologies/downloads/
- Eclipse Temurin: https://adoptium.net/
- SDKMAN for Unix-like environments: https://sdkman.io/

On Windows, after installation:

1. Set `JAVA_HOME` to the JDK installation directory.
2. Add `%JAVA_HOME%\bin` to `Path`.
3. Open a new terminal and run `java -version`.

Example `JAVA_HOME` value on Windows:

```text
C:\Program Files\Java\jdk-25
```

## Installing Maven

Maven can be installed globally or used through a project-local Maven Wrapper
once the backend scaffold includes one.

Common global installation options:

- Apache Maven binary distribution: https://maven.apache.org/download.cgi
- Windows Package Manager:

```powershell
winget install Apache.Maven
```

- Chocolatey:

```powershell
choco install maven
```

After installation, open a new terminal and run:

```bash
mvn -version
```

Check that Maven is using Java 25. If Maven reports a different Java version,
review `JAVA_HOME` and the terminal `Path`.

## Maven Wrapper

Once the Spring Boot project is scaffolded, prefer adding the Maven Wrapper
(`mvnw` and `mvnw.cmd`) to the repository. The wrapper lets contributors build
the backend without installing Maven globally, while still requiring a Java 25
JDK.

Expected commands after the backend is scaffolded:

```bash
cd ms-family-360
./mvnw clean verify
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
cd ms-family-360
.\mvnw.cmd clean verify
.\mvnw.cmd spring-boot:run
```

Until the Maven Wrapper is added, use the globally installed Maven command:

```bash
cd ms-family-360
mvn clean verify
mvn -pl family-360-application spring-boot:run
```
