# Spring Annotation Context Initializer

A small reusable Java library for bootstrapping a Spring annotation-based application context without requiring a larger application framework bootstrap.

The library provides the abstract `SpringAnnotationContextInitializer` base class. A concrete implementation supplies one base package; constructing that implementation creates an `AnnotationConfigApplicationContext`, scans the package and its subpackages, refreshes the context, and exposes the resulting Spring `ApplicationContext`.

## Requirements

- Java 17 or newer
- Spring Framework 6.x
- Maven for building the project

## Maven coordinates

```xml
<dependency>
    <groupId>template</groupId>
    <artifactId>spring-annotation-context-initializer-template</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

The snapshot artifact must be available in your configured Maven repository, for example through a local `mvn install` or an internal artifact repository.

## Usage

Create a small concrete initializer and return the root package that contains your Spring configuration classes and components:

```java
package com.example.bootstrap;

import template.annotation.initializer.SpringAnnotationContextInitializer;

public final class ApplicationContextInitializer extends SpringAnnotationContextInitializer {

    private static final String BASE_PACKAGE = "com.example.application";

    @Override
    protected String getBasePackages() {
        return BASE_PACKAGE;
    }
}
```

Then create the initializer and retrieve the context:

```java
ApplicationContextInitializer initializer = new ApplicationContextInitializer();
ApplicationContext context = initializer.getApplicationContext();

MyService service = context.getBean(MyService.class);
```

Spring scans the configured package recursively, so normal `@Component`, `@Service`, `@Repository`, `@Controller`, and `@Configuration` classes below that package can be discovered according to standard Spring component-scanning rules.

## Lifecycle

Initialization is eager. During construction the initializer:

1. creates an `AnnotationConfigApplicationContext`,
2. configures the root Log4j level to `INFO`,
3. obtains the base package from `getBasePackages()`,
4. scans that package,
5. refreshes the Spring context, and
6. exposes the refreshed context through `getApplicationContext()`.

Because `getBasePackages()` is called from the superclass constructor, implementations should return a constant or another value that is safe to access before subclass instance fields have been initialized.

The configured base package must not be blank. Invalid configuration fails immediately with an `IllegalStateException`.

## `ApplicationContextAware`

`SpringAnnotationContextInitializer` implements Spring's `ApplicationContextAware` interface. If Spring supplies another application context through `setApplicationContext(...)`, that context becomes the context returned by `getApplicationContext()`. The setter only replaces the exposed reference; it does not scan or refresh the supplied context.

## Testing and coverage

Run the tests with:

```bash
mvn test
```

The integration tests cover context creation and refresh, component scanning, configuration-defined singleton beans, dependency injection, the `ApplicationContextAware` callback, and validation of an invalid base package.

JaCoCo is configured through Maven and generates an HTML coverage report after the test phase at:

```text
target/site/jacoco/index.html
```

## Design scope

This project intentionally stays small. It is meant to provide a lightweight reusable bootstrap around Spring's annotation configuration rather than replace Spring Boot or other full application frameworks. Applications remain responsible for their own lifecycle management, configuration strategy, dependency versions, and shutdown behavior.
