# Backend

Spring Boot backend application.

See `backend/AGENTS.md` before implementing backend tasks.

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
./mvnw clean verify
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd clean verify
.\mvnw.cmd spring-boot:run
```
