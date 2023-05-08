[![Maven Central](https://img.shields.io/maven-central/v/io.github.yagato/HolodexWrapper.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.yagato%22%20AND%20a:%22HolodexWrapper%22)

# HolodexWrapper
A Java wrapper for the [Holodex API](https://docs.holodex.net/docs/holodex/f4e6fa31af431-getting-started) developed with Java 11. 

This project is heavily inspired by [Holodex.NET](https://github.com/EBro912/Holodex.NET), you could even interpret it as an almost 1:1 translation of it.

# Features
* Support for all the currently available GET and POST endpoints.
* Objects to easily customize your requests.
* All request parameters are included as constants (+70 VTuber groups and organizations). 

# Dependencies
* dotenv-java 2.3.2
* unirest-java 1.4.9
* lombok 1.18.26
* junit-jupiter-engine 5.9.3
* jackson-databind 2.15.0
* jackson-datatype-jdk8 2.14.2
* jackson-datatype-jsr318 2.15.0

# Download
```xml
<dependency>
    <groupId>io.github.yagato</groupId>
    <artifactId>HolodexWrapper</artifactId>
    <version>1.0.0</version>
</dependency>
```

```gradle
implementation group: 'io.github.yagato', name: 'HolodexWrapper', version: '1.0.0'
```

# Getting Started

1. Generate your API key (follow the [guide](https://docs.holodex.net/docs/holodex/f4e6fa31af431-getting-started) at the official Holodex API documentation). 
2. Add the dependency to your project.
3. Enjoy!

```java
HolodexClient holodexClient = new HolodexClient("YOUR_API_KEY");
Channel channel = holodexClient.getChannelInformation("UC5CwaMl1eIgY8h02uZw7u8A");
System.out.println(channel.getName()); // Suisei Channel
System.out.println(channel.getOrg()); // Hololive
System.out.println(channel.getPublishedAt()); // 2018-03-18T08:32:39Z7
```

# Documentation
* [HolodexWrapper Documentation](https://yagato.gitbook.io/holodexwrapper/): Documentation for the current release.
* [Holodex API documentation](https://docs.holodex.net/docs/holodex/f4e6fa31af431-getting-started): The official documentation for the Holodex API.

The documentation also comes included as Javadocs. Just hover over a method name and you'll see everything you need to know about it!

# Contributing
Feel free to contribute if you want to!

To contribute, just fork the repository, make a branch, commit all your changes and make a pull request!

But please, use the following commit message format:
```
Type(scope): Description
```
Where:
- `Type` is the type of change you made (e.g. `Bug`, `Feat`, `Refactor`, `Docs`, etc.)
- `scope` are the files/packages you are editing (e.g. `main`, `HolodexClient`, `PostQueryParameters`, etc.)
- `Description` is a short description of the change (e.g. `Added a new organization`)