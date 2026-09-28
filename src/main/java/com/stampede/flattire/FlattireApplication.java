package com.stampede.flattire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.EnumSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SpringBootApplication
public class FlattireApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlattireApplication.class, args);

        System.out.println("\n+---------------- OPTIONALS ----------------+");
        System.out.println("| Optional<T> = [ value ] OR [ empty ]     |");
        System.out.println("| empty != false; it means 'no T inside'  |");
        System.out.println("+------------------------------------------+");

        String possibleNull = null;
        Optional<String> safeValue = Optional.ofNullable(possibleNull);
        Optional<String> name = Optional.of("Ada");
        Optional<String> missingName = Optional.empty();
        System.out.println("Input -> operation -> return type -> result");
        System.out.println("null:String -> ofNullable -> Optional<String> -> " + safeValue);
        System.out.println("Optional<String> -> map(String::toUpperCase) -> Optional<String> -> " + name.map(String::toUpperCase));
        System.out.println("Optional<String> -> filter(Predicate<String>) -> Optional<String> -> " + name.filter(value -> value.length() > 3));
        System.out.println("Optional<String> -> orElse(String) -> String -> " + missingName.orElse("Guest"));
        System.out.print("Optional<String> -> ifPresent(Consumer<String>) -> void -> ");
        name.ifPresent(value -> System.out.println("printed " + value));
        System.out.println("ofNullable(null) wraps null instead of immediately causing NPE.");
        System.out.println("isEmpty() -> boolean: " + safeValue.isEmpty() + " | isPresent() -> boolean: " + safeValue.isPresent());

        System.out.println("\n+---------------- STREAM PIPELINE ----------------+");
        System.out.println("List<String> -> stream() -> filter/map/sorted -> terminal operation");
        System.out.println("                     intermediate       result");
        List<String> languages = List.of("Java", "JavaScript", "Java", "Kotlin", "Python", "Go", "Rust");
        System.out.println("source:   " + languages);
        System.out.println("filter:   Stream<String> -> List<String>: " + languages.stream().filter(language -> language.length() >= 5).toList());
        System.out.println("map:      Stream<String> -> List<String>: " + languages.stream().map(String::toUpperCase).toList());
        System.out.println("sorted:   Stream<String> -> List<String>: " + languages.stream().sorted().toList());
        System.out.println("distinct: Stream<String> -> List<String>: " + languages.stream().distinct().toList());
        Map<Integer, List<String>> languagesByLength = languages.stream().collect(Collectors.groupingBy(String::length));
        System.out.println("collect:  Stream<String> -> Map<Integer,List<String>>: " + languagesByLength);
        System.out.println("filter = choose | map = transform | sorted = order | distinct = unique | collect = gather");

        System.out.println("\n+---------------- EXCEPTIONS ----------------+");
        System.out.println("                 when?                 example");
        System.out.println("compile-time  [before run]      String x = 7;          compiler rejects it");
        System.out.println("checked       [compile + run]   IOException           catch/throws required");
        System.out.println("runtime       [while running]   NullPointerException  catch or fix input");
        System.out.println("error         [JVM/system]      OutOfMemoryError       usually not recoverable");
        System.out.println("try = risky code | catch = recovery | finally = cleanup");
        System.out.println("Exception path: normal code --throw--> catch handler --continue--> next statement");

        System.out.println("\n+---------------- LOGGING / SLF4J ----------------+");
        System.out.println("Application -> SLF4J API -> Logback backend -> console/file");
        System.out.println("INFO   normal progress:  log.info(\"Found {} results\", count)");
        System.out.println("WARN   recoverable risk: log.warn(\"Cache is empty\")");
        System.out.println("ERROR  failed operation:  log.error(\"Search failed\", exception)");
        System.out.println("DEBUG  developer detail:   log.debug(\"Query = {}\", query)");
        System.out.println("SLF4J lets code use one logging API while the backend handles formatting/output.");

        System.out.println("\n+---------------- COLLECTIONS ----------------+");
        System.out.println("             order?  duplicates?  lookup");
        System.out.println("List<T>       yes       yes        index");
        System.out.println("Set<T>        no*       no         contains");
        System.out.println("Map<K,V>      keys      keys no     get(key)");
        System.out.println("Queue<T>      waiting   yes         FIFO");
        List<String> list = List.of("Java", "Java", "Python");
        Set<String> set = Set.of("Java", "Python");
        Map<String, Integer> map = Map.of("Java", 4, "Python", 6);
        System.out.println("List<String>: " + list + " | get(0) -> " + list.get(0));
        System.out.println("Set<String>:  " + set + " | duplicate Java not stored");
        System.out.println("Map<String,Integer>: " + map + " | get(Java) -> " + map.get("Java"));
        System.out.println("* LinkedHashSet/TreeSet can preserve or sort order.");

        System.out.println("\n+---------------- ENUMS ----------------+");
        System.out.println("Use an enum when a value must be one of a fixed set of choices:");
        System.out.println("enum Status {");
        System.out.println("    PENDING, ACTIVE, SUSPENDED");
        System.out.println("}");

        // Enum example 1: use a named constant instead of a magic string.
        Status currentStatus = Status.ACTIVE;
        System.out.println("1. Status currentStatus = Status.ACTIVE;");
        System.out.println("   value -> " + currentStatus + " | use case -> account is allowed to operate");

        // Enum example 2: values() returns every constant declared in the enum.
        System.out.println("2. Status.values() -> Status[] -> " + List.of(Status.values()));

        // Enum example 3: valueOf() converts matching text into an enum constant.
        // The text must match exactly, including capitalization.
        Status statusFromText = Status.valueOf("SUSPENDED");
        System.out.println("3. Status.valueOf(String) -> Status -> " + statusFromText);
        System.out.println("   use case -> convert trusted config text into a type-safe status");

        // Enum example 4: enums can have their own fields and methods.
        System.out.println("4. currentStatus.description() -> String -> " + currentStatus.description());

        // Enum example 5: switch expressions can return a value for an enum.
        String message = switch (currentStatus) {
            case PENDING -> "Waiting for approval.";
            case ACTIVE -> "Account can be used.";
            case SUSPENDED -> "Account is temporarily blocked.";
        };
        System.out.println("5. switch(Status) -> String -> " + message);
        System.out.println("   use case -> choose behavior for each allowed status");

        EnumSet<Status> activeStatuses = EnumSet.of(Status.PENDING, Status.ACTIVE);
        System.out.println("6. EnumSet<Status> -> unique enum values -> " + activeStatuses);
        System.out.println("   use case -> define a group of statuses that need the same treatment");

        System.out.println("\n+---------------- RECORDS ----------------+");
        System.out.println("Use a record for a small immutable data object, such as an API response:");
        System.out.println("record Person(String name, int age) { }");
        System.out.println("// Java generates the constructor, name(), age(), toString(), equals(), hashCode()");

        // Record example 1: create a record using its generated constructor.
        Person firstPerson = new Person("Ada", 36);
        Person secondPerson = new Person("Ada", 36);
        System.out.println("1. Person person = new Person(\"Ada\", 36);");
        System.out.println("   object -> " + firstPerson + " | use case -> carry user data without boilerplate");

        // Record example 2: access record components with name() and age(), not getName() or getAge().
        System.out.println("2. name() -> String, age() -> int -> " + firstPerson.name() + ", " + firstPerson.age());
        System.out.println("   usage -> person.name(), person.age()  // records do not use getName()");

        // Record example 3: records compare their component values automatically.
        System.out.println("3. equals(Person) -> boolean -> " + firstPerson.equals(secondPerson));
        System.out.println("   two records with the same component values compare equal");

        // Record example 4: records automatically provide a useful toString().
        System.out.println("4. toString() -> String -> " + firstPerson);
        System.out.println("   useful for console output, debugging, and simple data transfer");

        // Record example 5: a record can still have custom methods.
        System.out.println("5. greeting() -> String -> " + firstPerson.greeting());
        System.out.println("   records are concise, but they can still contain behavior related to their data");

        System.out.println("\n+---------------- LAMBDAS ----------------+");
        System.out.println("lambda: (input) -> output/action | functional interface = one abstract method");
        System.out.println("Block form: () -> { statement1; statement2; } | expression form: () -> value");

        // Lambda example 1: Runnable has run() -> void and accepts no input.
        Runnable showMessage = () -> {
            System.out.println("   block lambda ran two statements");
            System.out.println("   Runnable.run() returns void");
        };
        System.out.println("1. Runnable = () -> { ... }:");
        showMessage.run();

        // Lambda example 2: Supplier<T> has get() -> T and accepts no input.
        Supplier<String> createMessage = () -> {
            String prefix = "Supplier result: ";
            return prefix + "a String";
        };
        System.out.println("2. Supplier<String>.get() -> String -> " + createMessage.get());

        // Lambda example 3: Predicate<T> accepts a value and returns true or false.
        Predicate<Integer> isEven = number -> number % 2 == 0;
        System.out.println("3. Predicate<Integer>.test(int) -> boolean -> " + isEven.test(8));

        // Lambda example 4: Function<T, R> accepts one type and returns another type.
        Function<String, Integer> stringLength = text -> text.length();
        System.out.println("4. Function<String,Integer>.apply(String) -> Integer -> " + stringLength.apply("Java"));

        // Lambda example 5: Consumer<T> accepts a value and performs an action without returning one.
        Consumer<String> printGreeting = text -> System.out.println("   Hello, " + text + "!");
        System.out.print("5. Consumer<String>.accept(String) -> void -> ");
        printGreeting.accept("friends");

        // Lambda example 6: lambdas can have more than one parameter.
        Function<Integer, Integer> square = number -> number * number;
        System.out.println("6. Function<Integer,Integer> square -> Integer -> " + square.apply(5));

        // Lambda example 7: a lambda can be passed directly to another method, such as List.sort().
        List<String> names = new java.util.ArrayList<>(List.of("Zoe", "Ada", "Mia"));
        names.sort((left, right) -> left.compareTo(right));
        System.out.println("7. Comparator<String> in sort -> mutates list order -> " + names);

        // Lambda example 8: a lambda can throw an exception declared by its functional interface.
        ThrowingAction riskyAction = () -> {
            System.out.println("8. Throwing lambda started...");
            throw new Exception("Something went wrong inside the lambda");
        };
        try {
            riskyAction.run();
        } catch (Exception exception) {
            System.out.println("   catch(Exception) -> " + exception.getMessage());
            System.out.println("   The exception was handled, so the application can continue.");
        }

        System.out.println("\n+---------------- TERNARY ----------------+");
        System.out.println("condition ? valueIfTrue : valueIfFalse  -> returns one value");

        int score = 82;
        String result = score >= 60 ? "Pass" : "Fail";
        System.out.println("1. score >= 60 ? Pass : Fail -> String -> " + result);

        boolean loggedIn = true;
        String greeting = loggedIn ? "Welcome back" : "Please log in";
        System.out.println("2. loggedIn ? Welcome : Login -> String -> " + greeting);

        int age = 20;
        String access = age >= 18 ? "Adult access" : "Minor access";
        System.out.println("3. age >= 18 ? Adult : Minor -> String -> " + access);
    }

    private enum Status {
        PENDING("Waiting for approval"),
        ACTIVE("Allowed to operate"),
        SUSPENDED("Temporarily blocked");

        private final String description;

        Status(String description) {
            this.description = description;
        }

        private String description() {
            return description;
        }
    }

    private record Person(String name, int age) {

        private String greeting() {
            return "Hello, my name is " + name + ".";
        }
    }

    @FunctionalInterface
    private interface ThrowingAction {

        void run() throws Exception;
    }

}
