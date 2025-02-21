# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5kvDrco67F8H5LCBALnAWspqig5QIAePKwvuh6ouisTYgmhgumGbpkhSBq0uWo4mkS4YWhyMDcryBqCsKMCvmIrrSkml6weURraDAABmvicI6zoErhLLlJ63pBgGQYhsx5qRpRwA2jA07xnK2HJlBJYoTy2a5pg-4gjBJRXAMxGjCBNZBrOzb-O2BmFNkPYwP2g69CZI5mZ8Fl1o21mLmY-GeN4fiBF4KDoHuB6+Mwx7pJkmAOReRTUNe0gAKK7ql9Spc0LQPqoT7dNOVloLZbL6eURU+SV+lslh8GRb6yENWAaEYphqkasJpIwOS8iWVVkneXOpFMm6FGiakKAgA2MBINxSn9XOMAoAAHtCGgyRGhnggtQ3hbxPj8VhnVkXhRgoNwmSDTOVUjWaEaFJa52XYYlVzgJibqUCJbSBdFIoDpCB5hpdlJUZMA5uGpWsV2ORgH2A5Dgc-kroF66BJCtq7tCMAAOKjqyMWnvF57MIZ1645lOX2KOhWLeg0N-iDFX09VIO1apZIE8h0L46MqitRhH3YUJp0iTAICTdNKEoBwACSaDXcVd3kXJE1TTNc27TdS2retoZi7J23ytrxU8XxK7HThhvuj1FIKMqfNqAbo0sY9lHcTytrAMqRhcbE0Cy7NSQ0-zLv3Z2dU9QT5uHZbHUw+Ve68wTgNYHp7Mwz+xmh2o4wwN0FQ9LncvSPnACMvYAMwACxPCemQGhWnlTDoCCgA2TfAS3fS5wAct3i4wM035XqDCXw05iOubnqj54XxejqXFfV3XUwN-qpn3NWrft9NXceTvvejgPh-7MPMBLgFa7BQE2A+FA2DcPAuqZHjo4pLFZ5wxzyVpjeDRqa02CKzIc-dRyjz-mVZmpsqqzEXqMU+KBILfV-GDHaudYRiUyE7QWWJhYnVdt1SWGsZby0Vm9dAKsxpqwllLTW81KFJD1rEDaXUtpsSdCbJhscApW1FkQ222CUBO0ZLCcBoxqFu3ZBVRSudw6q2NnBaOoxS6lFMFbL6qZRKvxEaObM2BEgGAzqgyOY8c5LzLuUSutdIHHBhhPBGLl+glysTAGxNcr6oxvhuSwF0ELJBgAAKQgDyd+oxAhtw7iTH+5N-5VEpHeFouc6Z7WfL0OY7c-FQDgBABCUAVgAHUWByyyi0AAQruBQcAADSXxXEr1rpfRmZxUEszSUOTJwBsm5PyUUkpZTKnVLqU8Bp1jV7NJqko2RHAqhZMoLCAAVqEtAeD2qwUIfddW0sDyywVkrW6CiaHu22Qw2Bus1qsKOSxaZ5z9oWw0QnARWy7ZgCduIyxUjZInJ4l7FRhh2g8kcK465RtOEYI-gdPhTyZDsPKMsnk7zXGzBAHk6AswbhfIejIpSikn7dI7JAf5BDE4wJCdpNQukplxPKJDN00NTiOKns45Gy5VxBQ3F4bpXYvSwGANgJ+hB4iJE-sTCev9wYVDShlLKOVjAtKThwP6b95VTPBSbJVL1YRaHkHoAwv0EBrJJc88MoluB4FjMGbQsJ1lcJFrCm2ZreWiOtbawSDrBFOrwLRV1oLsVPRAOaqArIUAlEsH6IFENICYxJVo4E5QdW6H0CgA1acTGpjMX-WlPh6U-k7Ey5ySMUaYCAA