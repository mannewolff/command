# SimpleCommandFramework

The SimpleCommandFramework is a lightweight Java framework that simplifies the implementation of business logic based on SOLID principles. It provides a flexible and extensible architecture for creating command chains and modeling complex process flows.

## Core Concepts

The framework is based on a set of central interfaces that form the basic building blocks for application logic:

### Command

The `Command` interface is the heart of the framework. It defines a single, atomic action that can be executed. A `Command` accepts a parameter object and returns a `CommandTransition` indicating the success or failure of the operation.

```java
@FunctionalInterface
public interface Command<T extends Object> {
    CommandTransition executeCommand(T parameterObject);
}
```

### ChainCommand

The `ChainCommand` interface extends the `Command` interface and is intended for use in a "Chain of Responsibility". Each `ChainCommand` in the chain has the option to handle the request or pass it on to the next link in the chain.

```java
public interface ChainCommand<T extends Object> extends Command<T> {
    CommandTransition executeCommandAsChain(T parameterObject);
}
}
```

### ProcessCommand

The `ProcessCommand` interface extends `ChainCommand` and is used to model complex, stateful process flows. `ProcessCommands` can be connected to each other via `Transitions` to form a directed graph of states and transitions.

```java
public interface ProcessCommand<T extends Object> extends ChainCommand<T> {
    String executeAsProcess(String startCommand, T context);
    // ... further methods
}
```

### CommandContainer

A `CommandContainer` is a special type of `Command` that can contain other `Commands`. It implements the Composite pattern, so that a group of `Commands` can be treated as a single `Command`. `CommandContainer` can be used to create and manage command chains or process flows.

```java
public interface CommandContainer<T extends Object> extends ChainCommand<T>, ProcessCommand<T> {
    CommandContainer<T> addCommand(Command<T> parameterObject);
    // ... further methods
}
```

### Transition

A `Transition` defines the transition from one `ProcessCommand` to the next. It consists of a return value and a target `ProcessCommand`.

```java
public interface Transition {
    String getReturnValue();
    String getTarget();
    // ... further methods
}
```

## Usage

### Creating a Simple Command Chain

```java
// 1. Create parameter object
DefaultParameterObject context = new DefaultParameterObject();

// 2. Create CommandContainer
CommandContainer<DefaultParameterObject> commandChain = new DefaultCommandContainer<>();

// 3. Add Commands
commandChain.addCommand(new FirstCommand());
commandChain.addCommand(new SecondCommand());
commandChain.addCommand(new ThirdCommand());

// 4. Execute command chain
commandChain.executeCommandAsChain(context);
```

### Creating a Process Flow

```java
// 1. Create parameter object
DefaultParameterObject context = new DefaultParameterObject();

// 2. Create CommandContainer as a process
CommandContainer<DefaultParameterObject> process = new DefaultCommandContainer<>();

// 3. Create and add process steps (Commands)
AbstractDefaultProcessCommand<DefaultParameterObject> start = new StartCommand();
start.setProcessID("START");
start.addTransition(new DefaultTransition("SUCCESS", "NEXT"));
process.addCommand(start);

AbstractDefaultProcessCommand<DefaultParameterObject> next = new NextCommand();
next.setProcessID("NEXT");
next.addTransition(new DefaultTransition("SUCCESS", "END"));
process.addCommand(next);

// 4. Execute process
process.executeAsProcess("START", context);
```

## XML Configuration

The framework offers the possibility to load command chains and process flows from XML files. This allows for loose coupling between the application logic and the configuration of the command chains.

```xml
<CommandChain>
    <Command className="org.mwolff.command.sample.FirstCommand"/>
    <Command className="org.mwolff.command.sample.SecondCommand"/>
    <Command className="org.mwolff.command.sample.ThirdCommand"/>
</CommandChain>
```

The `XMLChainBuilder` class can be used to parse such an XML file and create a `CommandContainer`.

```java
XMLChainBuilder<DefaultParameterObject> builder = new XMLChainBuilder<>();
CommandContainer<DefaultParameterObject> commandChain = builder.build(new FileInputStream("command-chain.xml"));
commandChain.executeCommandAsChain(new DefaultParameterObject());
```

## SOLID Principles

The SimpleCommandFramework was designed to promote adherence to SOLID principles:

*   **Single Responsibility Principle:** Each `Command` has a single, clearly defined task.
*   **Open/Closed Principle:** The framework is open for extensions (new `Commands` can be easily added), but closed for modifications (existing `Commands` do not need to be changed).
*   **Liskov Substitution Principle:** `CommandContainer` can be used in place of individual `Commands` without affecting the correctness of the program.
*   **Interface Segregation Principle:** The different `Command` interfaces (`Command`, `ChainCommand`, `ProcessCommand`) provide specific functionalities for different use cases.
*   **Dependency Inversion Principle:** The framework depends on abstractions (`Interfaces`), not on concrete implementations.

## License

The SimpleCommandFramework is licensed under the GNU Lesser General Public License v2.1.