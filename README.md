# Seven Senders SQS Java Example

A simple Java application illustrating usage of Seven Senders SQS Proxy with the Java AWS SDK.

## Requirements

The only requirement of this application is Maven. All other dependencies can
be installed by building the maven package:

```bash
mvn package
```

## Credentials

Enter your Seven Senders API key in the `SQSSample.java` file.

## Running The Sample

This sample application connects to The Seven Senders SQS Proxy, retrieves the Queue Url and the messages from the queue (if there are any).

```bash
mvn clean compile exec:java
```
