package com.stampede.flattire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SpringBootApplication
public class FlattireApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(FlattireApplication.class, args);

        // IntelliJ Program arguments: --lesson=fundamentals, --lesson=modern, or --lesson=spring.
        // With no selection, run every teaching section as before.
        String selectedLesson = "all";
        for (String arg : args) {
            if (arg.startsWith("--lesson=")) {
                selectedLesson = arg.substring("--lesson=".length());
            }
        }

        switch (selectedLesson) {
            case "fundamentals" -> runFundamentals();
            case "modern" -> runModernJava();
            case "spring" -> runSpringConcepts(context);
            case "all" -> {
                runFundamentals();
                runModernJava();
                runSpringConcepts(context);
            }
            default -> System.out.println("Unknown --lesson value: " + selectedLesson
                    + ". Choose fundamentals, modern, spring, or all.");
        }
    }

    private static void runFundamentals() {
        System.out.println("\n========== PART I: JAVA FUNDAMENTALS ==========");
        showToolchainAndTypes();
        showMethods();
        showControlFlow();
        showObjectsAndInterfaces();
        showCollections();
        showEnums();
        showExceptions();
        showThreads();
        showTernary();
    }

    private static void runModernJava() {
        System.out.println("\n========== PART II: JAVA 8 TO 17 ==========");
        showLambdas();
        showOptionals();
        showStreams();
        showRecords();
    }

    private static void runSpringConcepts(ConfigurableApplicationContext context) {
        System.out.println("\n========== PART III: SPRING CONCEPTS ==========");
        System.out.println("@SpringBootApplication -> configuration + component scanning + auto-configuration");
        System.out.println("SpringApplication.run(...) -> ConfigurableApplicationContext");
        System.out.println("context.getBean(FlattireApplication.class) -> managed bean exists: "
                + (context.getBean(FlattireApplication.class) != null));
        System.out.println("Maven reads pom.xml at build time; Spring creates beans at application startup.");
        showLogging();
    }

    private static void showToolchainAndTypes() {
        System.out.println("\n+---------------- JVM, PACKAGES, TYPES ----------------+");
        System.out.println("FlattireApplication.java --javac--> .class bytecode --JVM--> running program");
        System.out.println("JDK = compiler + tools + runtime | JVM = executes bytecode and manages memory");
        System.out.println("JRE = runtime concept (JVM + libraries); a JDK includes the runtime needed to run code.");
        System.out.println("HTTPS example: server certificate -> Java truststore (often cacerts) -> TLS trust check.");
        System.out.println("cacerts holds trusted certificates, not your app password; never disable TLS checks to fix trust.");
        System.out.println("package -> namespace for related classes: " + FlattireApplication.class.getPackageName());

        int count = 3;
        double price = 2.5;
        boolean ready = true;
        char grade = 'A';
        String language = "Java";
        System.out.println("int count = 3 -> primitive int -> " + count);
        System.out.println("double price = 2.5 -> primitive double -> " + price);
        System.out.println("boolean ready = true -> primitive boolean -> " + ready);
        System.out.println("char grade = 'A' -> primitive char -> " + grade);
        System.out.println("String language = \"Java\" -> object reference -> " + language);
        System.out.println("A reference can be null; a primitive int cannot. Objects are managed by the JVM.");
    }

    private static void showMethods() {
        System.out.println("\n+---------------- METHODS ----------------+");
        System.out.println("static int add(int left, int right) { return left + right; }");
        System.out.println("add(2, 3): int + int -> int -> " + add(2, 3));
        System.out.println("static void announce(String message) -> void: prints but returns no value");
        announce("Methods can return a value or perform an action.");
    }

    private static int add(int left, int right) {
        return left + right;
    }

    private static void announce(String message) {
        System.out.println("announce(\"" + message + "\") -> " + message);
    }

    private static void showControlFlow() {
        System.out.println("\n+---------------- IF / ELSE AND LOOPS ----------------+");
        int score = 82;
        if (score >= 60) {
            System.out.println("if (score >= 60) -> " + score + " passes");
        } else {
            System.out.println("else -> " + score + " needs practice");
        }

        String[] topics = {"Java", "Spring"};
        System.out.println("String[] topics -> " + List.of(topics));
        for (int index = 0; index < topics.length; index++) {
            System.out.println("for index " + index + " -> topics[index] = " + topics[index]);
        }
        for (String topic : topics) {
            System.out.println("enhanced for -> String topic = " + topic);
        }
        List.of(topics).forEach(topic -> System.out.println("List.forEach(lambda) -> " + topic));

        int remaining = 2;
        while (remaining > 0) {
            System.out.println("while (remaining > 0) -> " + remaining);
            remaining--;
        }
        System.out.println("Traditional for gives an index; enhanced for gives each value; forEach takes a lambda.");
    }

    private static void showObjectsAndInterfaces() {
        System.out.println("\n+---------------- CLASSES, OBJECTS, INTERFACES ----------------+");
        System.out.println("class Student extends Learner { private int points; ... }");
        Student student = new Student("Ada", 10);
        System.out.println("new Student(\"Ada\", 10) -> Student object -> points = " + student.points());
        Student sameStudent = student;
        Student anotherStudent = new Student("Ada", 10);
        System.out.println("sameStudent == student -> boolean -> " + (sameStudent == student));
        System.out.println("anotherStudent == student -> boolean -> " + (anotherStudent == student));
        System.out.println("References can point to one object; a second new Student is a different object.");
        student.addPoints(5);
        System.out.println("student.addPoints(5) -> void -> points now " + student.points());
        System.out.println("private points + addPoints() -> encapsulation protects changes to state");

        Learner learner = student;
        System.out.println("Learner learner = student; learner.role() -> String -> " + learner.role());
        System.out.println("Student inherits Learner; the Student override supplies the role at runtime.");

        StudentDto dto = new StudentDto(student.name(), student.points());
        System.out.println("record StudentDto(String name, int points) -> " + dto);
        System.out.println("dto.name() -> String -> " + dto.name() + " | dto.points() -> int -> " + dto.points());
        System.out.println("dto.equals(new StudentDto(\"Ada\", 15)) -> boolean -> "
                + dto.equals(new StudentDto("Ada", 15)));
        System.out.println("Student is a mutable object; StudentDto is a shallowly immutable data carrier.");

        System.out.println("interface GreetingFormatter { String format(StudentDto student); }");
        System.out.println("class FormalGreeting implements GreetingFormatter {");
        System.out.println("    public String format(StudentDto student) { return \"Good day, \" + student.name() + \".\"; }");
        System.out.println("}");
        GreetingFormatter formatter = new FormalGreeting();
        System.out.println("FormalGreeting.format(dto) -> String -> " + formatter.format(dto));
        formatter = new CasualGreeting();
        System.out.println("CasualGreeting.format(dto) -> String -> " + formatter.format(dto));
        System.out.println("The interface variable stays the same; the implementation changes the result.");
    }

    private static void showThreads() {
        System.out.println("\n+---------------- THREADS ----------------+");
        Thread worker = new Thread(() -> System.out.println("worker Runnable.run() -> running on "
                + Thread.currentThread().getName()), "learning-worker");
        worker.start();
        try {
            worker.join();
            System.out.println("worker.join() -> void -> main waited for the worker to finish");
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            System.out.println("join interrupted -> preserve the interrupt flag for the caller");
        }
        System.out.println("start() launches a thread; calling run() directly stays on the current thread.");
    }

    private static void showOptionals() {

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
        System.out.println("\"Ada\":String -> of -> Optional<String> -> " + name);
        System.out.println("no input -> empty() -> Optional<String> -> " + missingName);
        System.out.println("Optional<String> -> map(String::toUpperCase) -> Optional<String> -> " + name.map(String::toUpperCase));
        System.out.println("Optional<String> -> filter(Predicate<String>) -> Optional<String> -> " + name.filter(value -> value.length() > 3));
        System.out.println("Optional<String> -> orElse(String) -> String -> " + missingName.orElse("Guest"));
        System.out.println("Optional<String> -> orElseGet(Supplier<String>) -> String -> "
                + missingName.orElseGet(() -> "Generated guest"));
        System.out.print("Optional<String> -> ifPresent(Consumer<String>) -> void -> ");
        name.ifPresent(value -> System.out.println("printed " + value));
        System.out.println("ofNullable(null) wraps null instead of immediately causing NPE.");
        System.out.println("isEmpty() -> boolean: " + safeValue.isEmpty() + " | isPresent() -> boolean: " + safeValue.isPresent());
    }

    private static void showStreams() {
        System.out.println("\n+---------------- STREAM PIPELINE ----------------+");
        System.out.println("List<String> -> stream() -> filter/map/sorted -> terminal operation");
        System.out.println("                     intermediate       result");
        List<String> languages = List.of("Java", "JavaScript", "Java", "Kotlin", "Python", "Go", "Rust");
        System.out.println("source:   " + languages);
        System.out.println("filter:   Stream<String> -> List<String>: " + languages.stream().filter(language -> language.length() >= 5).toList());
        System.out.println("map:      Stream<String> -> List<String>: " + languages.stream().map(String::toUpperCase).toList());
        System.out.println("sorted:   Stream<String> -> List<String>: " + languages.stream().sorted().toList());
        System.out.println("distinct: Stream<String> -> List<String>: " + languages.stream().distinct().toList());
        Map<Integer, List<String>> languagesByLength = languages.stream()
                .collect(Collectors.groupingBy(String::length, TreeMap::new, Collectors.toList()));
        System.out.println("collect:  Stream<String> -> Map<Integer,List<String>>: " + languagesByLength);
        List<Integer> longNameLengths = languages.stream()
                .filter(language -> language.length() >= 5)
                .map(String::length)
                .toList();
        System.out.println("pipeline: filter(length >= 5) -> map(String::length) -> toList()");
        System.out.println("          Stream<String> -> Stream<Integer> -> List<Integer>: " + longNameLengths);
        System.out.println("filter = choose | map = transform | sorted = order | distinct = unique | collect = gather");
    }

    private static void showExceptions() {
        System.out.println("\n+---------------- EXCEPTIONS ----------------+");
        System.out.println("                 when?                 example");
        System.out.println("compile-time  [before run]      String x = 7;          compiler rejects it");
        System.out.println("checked       [compile + run]   IOException           catch/throws required");
        System.out.println("runtime       [while running]   NullPointerException  catch or fix input");
        System.out.println("error         [JVM/system]      OutOfMemoryError       usually not recoverable");
        System.out.println("try = risky code | catch = recovery | finally = cleanup");
        System.out.println("Exception path: normal code --throw--> catch handler --continue--> next statement");
        try {
            Integer.parseInt("not-a-number");
        } catch (NumberFormatException exception) {
            System.out.println("parseInt(\"not-a-number\") -> NumberFormatException -> caught safely");
        } finally {
            System.out.println("finally -> runs after the try/catch in this example");
        }
    }

    private static void showLogging() {
        System.out.println("\n+---------------- LOGGING / SLF4J ----------------+");
        System.out.println("Application -> SLF4J API -> Logback backend -> console/file");
        System.out.println("INFO   normal progress:  log.info(\"Found {} results\", count)");
        System.out.println("WARN   recoverable risk: log.warn(\"Cache is empty\")");
        System.out.println("ERROR  failed operation:  log.error(\"Search failed\", exception)");
        System.out.println("DEBUG  developer detail:   log.debug(\"Query = {}\", query)");
        System.out.println("SLF4J lets code use one logging API while the backend handles formatting/output.");
        System.out.println("These are printed call examples; this lesson does not emit WARN or ERROR log events.");
    }

    private static void showCollections() {
        System.out.println("\n+---------------- COLLECTIONS ----------------+");
        System.out.println("             order?  duplicates?  lookup");
        System.out.println("List<T>       yes       yes        index");
        System.out.println("Set<T>        no*       no         contains");
        System.out.println("Map<K,V>      keys      keys no     get(key)");
        System.out.println("Queue<T>      waiting   yes         FIFO");
        List<String> list = List.of("Java", "Java", "Python");
        Set<String> set = new LinkedHashSet<>(list);
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("Java", 4);
        map.put("Python", 6);
        Queue<String> queue = new ArrayDeque<>();
        queue.add("first task");
        queue.add("second task");
        System.out.println("List<String>: " + list + " | get(0) -> " + list.get(0));
        System.out.println("Set<String>:  " + set + " | duplicate Java not stored");
        System.out.println("Map<String,Integer>: " + map + " | get(Java) -> " + map.get("Java"));
        System.out.println("Queue<String>: " + queue + " | poll() -> " + queue.poll() + " | remaining -> " + queue);
        System.out.println("* This LinkedHashSet preserves insertion order; HashSet does not promise one.");
    }

    private static void showEnums() {
        System.out.println("\n+---------------- ENUMS ----------------+");
        System.out.println("Use an enum when a value must be one of a fixed set of choices:");
        System.out.println("enum Status {");
        System.out.println("    PENDING(\"Waiting for approval\"), ACTIVE(\"Allowed to operate\"),");
        System.out.println("    SUSPENDED(\"Temporarily blocked\");");
        System.out.println("    // constructor stores a description; description() returns String");
        System.out.println("}");
        System.out.println("Enum constants are Status objects, not arbitrary strings or booleans.");

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
    }

    private static void showRecords() {
        System.out.println("\n+---------------- RECORDS ----------------+");
        System.out.println("Use a record for a small immutable data object, such as an API response:");
        System.out.println("record Person(String name, int age) { }");
        System.out.println("// Java generates the constructor, name(), age(), toString(), equals(), hashCode()");
        System.out.println("// The actual nested Person record also defines greeting().");

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
    }

    private static void showLambdas() {
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

        // Lambda example 6: transform an Integer using a one-parameter lambda.
        Function<Integer, Integer> square = number -> number * number;
        System.out.println("6. Function<Integer,Integer> square -> Integer -> " + square.apply(5));

        // Lambda example 7: this comparator has two parameters and is passed to List.sort().
        List<String> names = new ArrayList<>(List.of("Zoe", "Ada", "Mia"));
        names.sort((left, right) -> left.compareTo(right));
        System.out.println("7. Comparator<String> (left, right) -> int; sort mutates list order -> " + names);

        // Lambda example 8: a lambda can throw an exception declared by its functional interface.
        ThrowingAction riskyAction = () -> {
            System.out.println("8. Throwing lambda started...");
            throw new Exception("Something went wrong inside the lambda");
        };
        System.out.println("@FunctionalInterface interface ThrowingAction { void run() throws Exception; }");
        try {
            riskyAction.run();
        } catch (Exception exception) {
            System.out.println("   catch(Exception) -> " + exception.getMessage());
            System.out.println("   The exception was handled, so the application can continue.");
        }
    }

    private static void showTernary() {
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

    // Teaching-only types: they are not Spring components or FlatTire domain objects.
    private abstract static class Learner {
        private final String name;

        Learner(String name) {
            this.name = name;
        }

        final String name() {
            return name;
        }

        abstract String role();
    }

    private static final class Student extends Learner {
        private int points;

        Student(String name, int points) {
            super(name);
            this.points = points;
        }

        void addPoints(int morePoints) {
            if (morePoints <= 0) {
                throw new IllegalArgumentException("Points must be positive");
            }
            points += morePoints;
        }

        int points() {
            return points;
        }

        @Override
        String role() {
            return "Student";
        }
    }

    private record StudentDto(String name, int points) {
    }

    private interface GreetingFormatter {
        String format(StudentDto student);
    }

    private static final class FormalGreeting implements GreetingFormatter {
        @Override
        public String format(StudentDto student) {
            return "Good day, " + student.name() + ".";
        }
    }

    private static final class CasualGreeting implements GreetingFormatter {
        @Override
        public String format(StudentDto student) {
            return "Hey, " + student.name() + "!";
        }
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
