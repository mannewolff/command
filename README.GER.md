# SimpleCommandFramework

Das SimpleCommandFramework ist ein leichtgewichtiges Java-Framework, das die Implementierung von Geschäftslogik auf Basis der SOLID-Prinzipien vereinfacht. Es bietet eine flexible und erweiterbare Architektur zur Erstellung von Befehlsketten (Command Chains) und zur Modellierung von komplexen Prozessabläufen.

## Kernkonzepte

Das Framework basiert auf einer Reihe von zentralen Interfaces, die die Grundbausteine für die Anwendungslogik bilden:

### Command

Das `Command`-Interface ist das Herzstück des Frameworks. Es definiert eine einzelne, atomare Aktion, die ausgeführt werden kann. Ein `Command` nimmt ein Parameterobjekt entgegen und gibt eine `CommandTransition` zurück, die den Erfolg oder Misserfolg der Operation anzeigt.

```java
@FunctionalInterface
public interface Command<T extends Object> {
    CommandTransition executeCommand(T parameterObject);
}
```

### ChainCommand

Das `ChainCommand`-Interface erweitert das `Command`-Interface und ist für die Verwendung in einer "Chain of Responsibility" (Verantwortungskette) vorgesehen. Jedes `ChainCommand` in der Kette hat die Möglichkeit, die Anfrage zu bearbeiten oder sie an das nächste Glied in der Kette weiterzugeben.

```java
public interface ChainCommand<T extends Object> extends Command<T> {
    CommandTransition executeCommandAsChain(T parameterObject);
}
```

### ProcessCommand

Das `ProcessCommand`-Interface erweitert `ChainCommand` und dient zur Modellierung von komplexen, zustandsbehafteten Prozessabläufen. `ProcessCommands` können über `Transitions` miteinander verbunden werden, um einen gerichteten Graphen von Zuständen und Übergängen zu bilden.

```java
public interface ProcessCommand<T extends Object> extends ChainCommand<T> {
    String executeAsProcess(String startCommand, T context);
    // ... weitere Methoden
}
```

### CommandContainer

Ein `CommandContainer` ist eine spezielle Art von `Command`, das andere `Commands` enthalten kann. Er implementiert das Composite-Pattern, sodass eine Gruppe von `Commands` wie ein einzelnes `Command` behandelt werden kann. `CommandContainer` können verwendet werden, um Befehlsketten oder Prozessabläufe zu erstellen und zu verwalten.

```java
public interface CommandContainer<T extends Object> extends ChainCommand<T>, ProcessCommand<T> {
    CommandContainer<T> addCommand(Command<T> parameterObject);
    // ... weitere Methoden
}
```

### Transition

Eine `Transition` definiert den Übergang von einem `ProcessCommand` zum nächsten. Sie besteht aus einem Rückgabewert und einem Ziel-`ProcessCommand`.

```java
public interface Transition {
    String getReturnValue();
    String getTarget();
    // ... weitere Methoden
}
```

## Verwendung

### Erstellen einer einfachen Befehlskette

```java
// 1. Parameterobjekt erstellen
DefaultParameterObject context = new DefaultParameterObject();

// 2. CommandContainer erstellen
CommandContainer<DefaultParameterObject> commandChain = new DefaultCommandContainer<>();

// 3. Commands hinzufügen
commandChain.addCommand(new FirstCommand());
commandChain.addCommand(new SecondCommand());
commandChain.addCommand(new ThirdCommand());

// 4. Befehlskette ausführen
commandChain.executeCommandAsChain(context);
```

### Erstellen eines Prozessablaufs

```java
// 1. Parameterobjekt erstellen
DefaultParameterObject context = new DefaultParameterObject();

// 2. CommandContainer als Prozess erstellen
CommandContainer<DefaultParameterObject> process = new DefaultCommandContainer<>();

// 3. Prozessschritte (Commands) erstellen und hinzufügen
AbstractDefaultProcessCommand<DefaultParameterObject> start = new StartCommand();
start.setProcessID("START");
start.addTransition(new DefaultTransition("SUCCESS", "NEXT"));
process.addCommand(start);

AbstractDefaultProcessCommand<DefaultParameterObject> next = new NextCommand();
next.setProcessID("NEXT");
next.addTransition(new DefaultTransition("SUCCESS", "END"));
process.addCommand(next);

// 4. Prozess ausführen
process.executeAsProcess("START", context);
```

## XML-Konfiguration

Das Framework bietet die Möglichkeit, Befehlsketten und Prozessabläufe aus XML-Dateien zu laden. Dies ermöglicht eine lose Kopplung zwischen der Anwendungslogik und der Konfiguration der Befehlsketten.

```xml
<CommandChain>
    <Command className="org.mwolff.command.sample.FirstCommand"/>
    <Command className="org.mwolff.command.sample.SecondCommand"/>
    <Command className="org.mwolff.command.sample.ThirdCommand"/>
</CommandChain>
```

Die `XMLChainBuilder`-Klasse kann verwendet werden, um eine solche XML-Datei zu parsen und einen `CommandContainer` zu erstellen.

```java
XMLChainBuilder<DefaultParameterObject> builder = new XMLChainBuilder<>();
CommandContainer<DefaultParameterObject> commandChain = builder.build(new FileInputStream("command-chain.xml"));
commandChain.executeCommandAsChain(new DefaultParameterObject());
```

## SOLID-Prinzipien

Das SimpleCommandFramework wurde entwickelt, um die Einhaltung der SOLID-Prinzipien zu fördern:

*   **Single Responsibility Principle:** Jedes `Command` hat eine einzige, klar definierte Aufgabe.
*   **Open/Closed Principle:** Das Framework ist offen für Erweiterungen (neue `Commands` können einfach hinzugefügt werden), aber geschlossen für Modifikationen (bestehende `Commands` müssen nicht geändert werden).
*   **Liskov Substitution Principle:** `CommandContainer` können anstelle von einzelnen `Commands` verwendet werden, ohne die Korrektheit des Programms zu beeinträchtigen.
*   **Interface Segregation Principle:** Die verschiedenen `Command`-Interfaces (`Command`, `ChainCommand`, `ProcessCommand`) bieten spezifische Funktionalitäten für unterschiedliche Anwendungsfälle.
*   **Dependency Inversion Principle:** Das Framework hängt von Abstraktionen (`Interfaces`) ab, nicht von konkreten Implementierungen.

## Lizenz

Das SimpleCommandFramework ist unter der GNU Lesser General Public License v2.1 lizenziert.