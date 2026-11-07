package com.stampede.flattire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import tools.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SpringBootApplication
public class FlattireApplication {
    private static final String RESET = "\u001B[0m";
    private static final String HEADING_COLOR = "\u001B[1;36m";
    private static final String[] TOPIC_COLORS = {
            "\u001B[1;94m", "\u001B[1;92m", "\u001B[1;93m", "\u001B[1;95m", "\u001B[1;96m"
    };
    private static boolean colorEnabled = true;
    private static int topicColorIndex;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(FlattireApplication.class, args);

        // IntelliJ Program arguments: --lesson=fundamentals, --lesson=modern,
        // --lesson=compare, or --lesson=spring. Add --no-color if ANSI colors are not shown.
        // With no selection, run every teaching section as before.
        String selectedLesson = "all";
        for (String arg : args) {
            if (arg.startsWith("--lesson=")) {
                selectedLesson = arg.substring("--lesson=".length());
            } else if (arg.equals("--no-color")) {
                colorEnabled = false;
            }
        }

        switch (selectedLesson) {
            case "fundamentals" -> runFundamentals();
            case "modern" -> runModernJava();
            case "compare" -> showTraditionalVsModern();
            case "spring" -> runSpringConcepts(context);
            case "all" -> {
                runFundamentals();
                runModernJava();
                runSpringConcepts(context);
            }
            default -> System.out.println("Unknown --lesson value: " + selectedLesson
                    + ". Choose fundamentals, modern, compare, spring, or all.");
        }
    }

    private static void runFundamentals() {
        heading("PART I: JAVA FUNDAMENTALS");
        showToolchainAndTypes();
        showMethods();
        showControlFlow();
        showObjectsAndInterfaces();
        showModifiersAndStaticMemory();
        showCollections();
        showEnums();
        showExceptions();
        showThreads();
        showTernary();
    }

    private static void runModernJava() {
        heading("PART II: JAVA 8 TO 17");
        showLambdas();
        showOptionals();
        showStreams();
        showRecords();
        showTraditionalVsModern();
    }

    private static void runSpringConcepts(ConfigurableApplicationContext context) {
        heading("PART III: SPRING CONCEPTS");
        topic("Spring startup and beans");
        System.out.println("Spring's container creates and manages objects called beans.");
        System.out.println("@SpringBootApplication -> configuration + component scanning + auto-configuration");
        System.out.println("SpringApplication.run(...) -> ConfigurableApplicationContext");
        System.out.println("Example: context.getBean(FlattireApplication.class) -> bean exists: "
                + (context.getBean(FlattireApplication.class) != null));
        System.out.println("Maven reads pom.xml at build time; Spring creates beans at application startup.");
        showDtoJson(context.getBean(ObjectMapper.class));
        showLogging();
    }

    private static void printColored(String color, String line) {
        System.out.println(colorEnabled ? color + line + RESET : line);
    }

    private static void heading(String title) {
        System.out.println();
        printColored(HEADING_COLOR, "========== " + title + " ==========");
    }

    private static void section(String title) {
        System.out.println();
        printColored(HEADING_COLOR, "+---------------- " + title + " ----------------+");
    }

    private static void topic(String title) {
        System.out.println();
        printColored(TOPIC_COLORS[topicColorIndex++ % TOPIC_COLORS.length], "  [ " + title + " ]");
    }

    private static void showToolchainAndTypes() {
        section("JVM, PACKAGES, TYPES");
        topic("From source code to a running program");
        System.out.println("The JDK compiles .java source; the JVM runs the resulting .class bytecode.");
        System.out.println("FlattireApplication.java --javac--> .class bytecode --JVM--> running program");
        System.out.println("JDK = development kit (compiler + runtime); JRE = runtime (JVM + libraries).");
        System.out.println("JVM = the engine that runs bytecode and manages memory.");

        topic("Truststore and HTTPS");
        System.out.println("A truststore lists certificate authorities Java trusts when checking a server's certificate.");
        System.out.println("HTTPS example: server certificate -> Java truststore (often cacerts) -> TLS trust check.");
        System.out.println("cacerts is a common Java truststore; it does not hold your app password.");

        topic("Package and import");
        System.out.println("A package groups related classes under a name: " + FlattireApplication.class.getPackageName());
        System.out.println("package com.stampede.flattire; -> this file belongs under com/stampede/flattire/");
        System.out.println("import java.util.List; -> write List instead of java.util.List.");

        topic("Primitive values and object references");
        System.out.println("A primitive stores a value; a reference points to an object.");
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
        section("METHODS");
        topic("Return a value");
        System.out.println("A method names reusable work. Its return type says what the caller receives.");
        System.out.println("static int add(int left, int right) { return left + right; }");
        System.out.println("add(2, 3) -> int result: " + add(2, 3));
        topic("Perform an action");
        System.out.println("void means the method returns no value. announce(String) prints its input:");
        announce("Methods can return a value or perform an action.");
    }

    private static int add(int left, int right) {
        return left + right;
    }

    private static void announce(String message) {
        System.out.println("announce(\"" + message + "\") -> " + message);
    }

    private static void showControlFlow() {
        section("IF / ELSE AND LOOPS");
        topic("Choose a path with if / else");
        System.out.println("if runs its block when the condition is true; else runs otherwise.");
        int score = 82;
        System.out.println("Example: if (score >= 60) with score = " + score);
        if (score >= 60) {
            System.out.println("if (score >= 60) -> " + score + " passes");
        } else {
            System.out.println("else -> " + score + " needs practice");
        }
        int practiceScore = 45;
        if (practiceScore >= 60) {
            System.out.println("if -> " + practiceScore + " passes");
        } else {
            System.out.println("else -> " + practiceScore + " needs practice");
        }

        topic("Repeat with indexed, enhanced, and callback loops");
        System.out.println("Indexed for gives a position; enhanced for gives each item; forEach calls a lambda.");
        String[] topics = {"Java", "Spring"};
        System.out.println("String[] topics -> " + List.of(topics));
        for (int index = 0; index < topics.length; index++) {
            System.out.println("for index " + index + " -> topics[index] = " + topics[index]);
        }
        for (String topic : topics) {
            System.out.println("enhanced for -> String topic = " + topic);
        }
        List.of(topics).forEach(topic -> System.out.println("List.forEach(lambda) -> " + topic));

        topic("Repeat while a condition stays true");
        System.out.println("while checks its condition before every pass.");
        int remaining = 2;
        while (remaining > 0) {
            System.out.println("while (remaining > 0) -> " + remaining);
            remaining--;
        }
        System.out.println("Traditional for gives an index; enhanced for gives each value; forEach takes a lambda.");
    }

    private static void showObjectsAndInterfaces() {
        section("CLASSES, OBJECTS, INTERFACES");
        topic("Class blueprint and object instance");
        System.out.println("A class defines fields and methods; new creates one object from that class.");
        System.out.println("The Student class later in this file has a name and mutable points.");
        Student student = new Student("Ada", 10);
        System.out.println("Student student = new Student(\"Ada\", 10); -> points() = " + student.points());

        topic("References: same object or different object?");
        Student sameStudent = student;
        Student anotherStudent = new Student("Ada", 10);
        System.out.println("sameStudent = student; anotherStudent = new Student(\"Ada\", 10);");
        System.out.println("sameStudent == student -> boolean -> " + (sameStudent == student));
        System.out.println("anotherStudent == student -> boolean -> " + (anotherStudent == student));
        System.out.println("== checks whether both references point to the same object.");

        topic("Encapsulation: change private state through a method");
        System.out.println("points is private, so callers use addPoints(5) rather than changing the field directly.");
        student.addPoints(5);
        System.out.println("student.addPoints(5) -> void -> points now " + student.points());

        topic("Inheritance and polymorphism");
        System.out.println("Student extends Learner; a Learner variable can refer to a Student object.");
        Learner learner = student;
        System.out.println("Learner learner = student; learner.role() -> String -> " + learner.role());
        System.out.println("The Student version of role() runs because the object is a Student.");

        topic("Record as a data transfer object");
        System.out.println("A record carries named values and generates accessors, equality, and text output.");
        StudentDto dto = new StudentDto(student.name(), student.points());
        System.out.println("record StudentDto(String name, int points) -> " + dto);
        System.out.println("dto.name() -> String -> " + dto.name() + " | dto.points() -> int -> " + dto.points());
        System.out.println("dto.equals(new StudentDto(\"Ada\", 15)) -> boolean -> "
                + dto.equals(new StudentDto("Ada", 15)));
        System.out.println("Student is a mutable object; StudentDto is a shallowly immutable data carrier.");

        topic("Interface: the method contract");
        System.out.println("An interface declares a method callers can use; each class supplies its own behavior.");
        System.out.println("interface GreetingFormatter { String format(StudentDto student); }");
        System.out.println("class FormalGreeting implements GreetingFormatter {");
        System.out.println("    @Override // compiler checks that format matches the interface method");
        System.out.println("    public String format(StudentDto student) { return \"Good day, \" + student.name() + \".\"; }");
        System.out.println("}");
        topic("Interface: call the same method on different objects");
        GreetingFormatter formatter = new FormalGreeting();
        System.out.println("GreetingFormatter formatter = new FormalGreeting(); -> interface reference, concrete object");
        System.out.println("formatter.format(dto) -> String -> " + formatter.format(dto));
        formatter = new CasualGreeting();
        System.out.println("formatter = new CasualGreeting();");
        System.out.println("formatter.format(dto) -> String -> " + formatter.format(dto));
        System.out.println("Same call, different greeting: the object's class decides what runs.");
    }

    private static void showModifiersAndStaticMemory() {
        section("ACCESS, FINAL, STATIC, MEMORY");
        topic("Access modifiers");
        System.out.println("Access modifiers control where a class member can be used.");
        VisibilityExample example = new VisibilityExample("Ada");
        VisibilityChild child = new VisibilityChild("Bea");
        System.out.println("private String name -> only VisibilityExample accesses the field directly");
        System.out.println("public publicName() -> String -> " + example.publicName());
        System.out.println("protected role() -> String -> subclass override returns " + child.role());
        System.out.println("no modifier: packageHint() -> String -> " + example.packageHint());
        System.out.println("public = wherever the type is visible | protected = package/subclass | no modifier = package | private = class");

        topic("Final reference versus mutable contents");
        System.out.println("final prevents reassigning the variable, but it does not freeze an ArrayList.");
        final List<String> topics = new ArrayList<>(List.of("Java"));
        topics.add("Spring");
        System.out.println("final List<String> topics -> same reference, mutable contents -> " + topics);
        System.out.println("final class cannot be extended; final method cannot be overridden.");
        topic("Static members belong to the class");
        System.out.println("static means one member belongs to VisibilityExample, not to each new object.");
        System.out.println("VisibilityExample.course() -> static String shared at class level -> "
                + VisibilityExample.course());
        System.out.println("static final String constant = good for a small, immutable shared value.");

        topic("Memory risk: unbounded static data");
        System.out.println("A static field can keep a collection reachable for the class loader's lifetime.");
        System.out.println("DON'T execute: static final List<byte[]> CACHE = new ArrayList<>(); add forever");
        System.out.println("If CACHE grows forever, its objects stay in memory. Give caches a size limit.");
        System.out.println("A static method itself does not cause out-of-memory errors.");
    }

    private static void showDtoJson(ObjectMapper objectMapper) {
        section("REQUEST / RESPONSE DTO AND JSON");
        topic("DTOs carry selected fields across a boundary");
        System.out.println("A DTO (Data Transfer Object) is a small object with fields chosen for an API message.");
        System.out.println("Client JSON -> deserialize -> TeachingStudentRequest -> map -> TeachingStudentResponse");
        System.out.println("TeachingStudentResponse -> serialize -> JSON response body");
        String requestJson = "{\"name\":\"Ada\",\"points\":10}";
        topic("Deserialize JSON into a request object");
        System.out.println("Deserialize means turn JSON text into a Java object.");
        System.out.println("Input JSON String -> " + requestJson);
        try {
            TeachingStudentRequest request = objectMapper.readValue(requestJson, TeachingStudentRequest.class);
            System.out.println("readValue(json, TeachingStudentRequest.class) -> TeachingStudentRequest -> " + request);
            topic("Map the request and serialize a response");
            System.out.println("Serialize means turn a Java object into JSON text.");
            TeachingStudentResponse response = new TeachingStudentResponse(42L, request.name(), request.points());
            System.out.println("map request -> response DTO (example ID, no database write) -> " + response);
            String responseJson = objectMapper.writeValueAsString(response);
            System.out.println("writeValueAsString(response) -> String JSON -> " + responseJson);
            System.out.println("@RequestBody commonly deserializes request JSON; @RestController serializes returned DTO.");
            System.out.println("Request DTO = client-allowed input; response DTO = fields intentionally exposed.");
            topic("Malformed JSON and validation");
            System.out.println("Bad JSON can fail before application logic reads the DTO.");
            try {
                objectMapper.readValue("{broken", TeachingStudentRequest.class);
            } catch (Exception badJson) {
                System.out.println("Malformed JSON -> deserialization error before normal controller logic: "
                        + badJson.getClass().getSimpleName());
            }
            System.out.println("Valid JSON syntax is not validation or authorization; check fields and permissions separately.");
        } catch (Exception exception) {
            System.out.println("JSON teaching example failed: " + exception.getClass().getSimpleName());
        }
    }

    private static void showThreads() {
        section("THREADS");
        topic("Start a worker, then join it");
        System.out.println("A thread is another path of execution in the same process.");
        System.out.println("new Thread(Runnable, \"learning-worker\") -> start() -> join()");
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
        section("OPTIONALS");
        topic("Mental model: value or empty");
        System.out.println("Optional<String> holds one String or no String. It is not a boolean.");
        System.out.println("| Optional<T> = [ value ] OR [ empty ]     |");
        System.out.println("| empty != false; it means 'no T inside'  |");
        System.out.println("+------------------------------------------+");

        String possibleNull = null;
        Optional<String> safeValue = Optional.ofNullable(possibleNull);
        Optional<String> name = Optional.of("Ada");
        Optional<String> missingName = Optional.empty();
        topic("Create an Optional");
        System.out.println("ofNullable accepts a possibly null String and returns an Optional<String>.");
        System.out.println("Optional.ofNullable(null) -> Optional<String>: " + safeValue);
        System.out.println("Optional.of(\"Ada\") -> Optional<String>: " + name);
        System.out.println("Optional.empty() -> Optional<String>: " + missingName);

        topic("Transform or keep only matching values");
        System.out.println("map changes the inside value; filter keeps it only when a condition is true.");
        System.out.println("Optional<String> -> map(String::toUpperCase) -> Optional<String> -> " + name.map(String::toUpperCase));
        System.out.println("Optional<String> -> filter(Predicate<String>) -> Optional<String> -> " + name.filter(value -> value.length() > 3));

        topic("Fallback or act when present");
        System.out.println("orElse returns a plain String when empty; ifPresent runs an action only when filled.");
        System.out.println("Optional<String> -> orElse(String) -> String -> " + missingName.orElse("Guest"));
        System.out.println("Optional<String> -> orElseGet(Supplier<String>) -> String -> "
                + missingName.orElseGet(() -> "Generated guest"));
        System.out.print("Optional<String> -> ifPresent(Consumer<String>) -> void -> ");
        name.ifPresent(value -> System.out.println("printed " + value));

        topic("Check whether a value exists");
        System.out.println("Optional.empty means no value inside; isEmpty() returns a boolean about that state.");
        System.out.println("isEmpty() -> boolean: " + safeValue.isEmpty() + " | isPresent() -> boolean: " + safeValue.isPresent());
    }

    private static void showStreams() {
        section("STREAM PIPELINE");
        topic("Source and pipeline shape");
        System.out.println("A stream processes items from a source. Intermediate steps wait for a terminal step.");
        System.out.println("List<String> -> stream() -> filter/map -> toList() -> List<String>");
        List<String> languages = List.of("Java", "JavaScript", "Java", "Kotlin", "Python", "Go", "Rust");
        System.out.println("source:   " + languages);
        topic("One operation at a time");
        System.out.println("filter(length >= 5).toList() -> keep matching names: "
                + languages.stream().filter(language -> language.length() >= 5).toList());
        System.out.println("map(String::toUpperCase).toList() -> change every name: "
                + languages.stream().map(String::toUpperCase).toList());
        System.out.println("sorted().toList() -> alphabetical order: " + languages.stream().sorted().toList());
        System.out.println("distinct().toList() -> remove duplicate Java: " + languages.stream().distinct().toList());
        Map<Integer, List<String>> languagesByLength = languages.stream()
                .collect(Collectors.groupingBy(String::length, TreeMap::new, Collectors.toList()));
        System.out.println("collect(groupingBy length) -> Map<Integer,List<String>>: " + languagesByLength);

        topic("Combine filter, map, and toList");
        List<Integer> longNameLengths = languages.stream()
                .filter(language -> language.length() >= 5)
                .map(String::length)
                .toList();
        System.out.println("pipeline: filter(length >= 5) -> map(String::length) -> toList()");
        System.out.println("          Stream<String> -> Stream<Integer> -> List<Integer>: " + longNameLengths);
        System.out.println("The List is the source; filter and map are intermediate; toList is terminal.");
    }

    private static void showExceptions() {
        section("EXCEPTIONS");
        topic("When each kind of failure occurs");
        System.out.println("An exception is a failure object thrown while code runs; a compile error stops code from running.");
        System.out.println("                 when?                 example");
        System.out.println("compile-time  [before run]      String x = 7;          compiler rejects it");
        System.out.println("checked       [while running]   LessonFullException  caller must catch or declare");
        System.out.println("unchecked     [while running]   InvalidScoreException caller may catch; compiler does not require it");
        System.out.println("Error         [JVM/system]      OutOfMemoryError      usually not recoverable");
        System.out.println("Checked describes a compiler rule; the exception itself is thrown only while running.");

        topic("Throwable hierarchy and exception syntax");
        System.out.println("Throwable -> Exception (checked or RuntimeException) | Error");
        System.out.println("new LessonFullException(message) -> construct an object; nothing is thrown yet.");
        System.out.println("throw new LessonFullException(message) -> stop this method and send the failure to its caller.");
        System.out.println("enroll(boolean) throws LessonFullException -> declare that a checked failure can escape.");
        System.out.println("try = attempt | catch = handle a matching type | finally = run afterward");

        topic("Checked exception: caller catches a declared failure");
        System.out.println("Example: enroll(true) returns a String; enroll(false) throws LessonFullException.");
        try {
            System.out.println("enroll(true) -> String -> " + enroll(true));
            enroll(false);
        } catch (LessonFullException exception) {
            System.out.println("enroll(false) -> LessonFullException -> getMessage() = " + exception.getMessage());
        } finally {
            System.out.println("finally -> this enrollment attempt finished");
        }
        System.out.println("First call returned; second call threw, so it never returned a String.");

        topic("Unchecked exception: caller may still catch it");
        System.out.println("Example: a negative score violates requireValidScore(int)'s rule.");
        try {
            requireValidScore(-1);
        } catch (InvalidScoreException exception) {
            System.out.println("requireValidScore(-1) -> unchecked InvalidScoreException -> " + exception.getMessage());
        }
        System.out.println("Catch known failures by their specific type; catch(Throwable) also catches serious Errors.");

        topic("Built-in unchecked exception");
        System.out.println("Integer.parseInt expects numeric text; invalid text throws NumberFormatException.");
        try {
            Integer.parseInt("not-a-number");
        } catch (NumberFormatException exception) {
            System.out.println("parseInt(\"not-a-number\") -> NumberFormatException -> caught safely");
        } finally {
            System.out.println("finally -> runs after the try/catch in this example");
        }
    }

    private static String enroll(boolean open) throws LessonFullException {
        if (!open) {
            throw new LessonFullException("No seats left in the lesson");
        }
        return "enrolled";
    }

    private static int requireValidScore(int score) {
        if (score < 0) {
            throw new InvalidScoreException(score);
        }
        return score;
    }

    private static void showLogging() {
        section("LOGGING / SLF4J");
        topic("The logging API and backend");
        System.out.println("SLF4J is the API code calls; a backend such as Logback writes the message.");
        System.out.println("Application -> SLF4J API -> Logback backend -> console/file");
        topic("Choose a level for the event");
        System.out.println("Choose a level based on what happened, not on how important the code looks.");
        System.out.println("INFO   normal progress:  log.info(\"Found {} results\", count)");
        System.out.println("WARN   recoverable risk: log.warn(\"Cache is empty\")");
        System.out.println("ERROR  failed operation:  log.error(\"Search failed\", exception)");
        System.out.println("DEBUG  developer detail:   log.debug(\"Query = {}\", query)");
        System.out.println("SLF4J lets code use one logging API while the backend handles formatting/output.");
        System.out.println("These are printed call examples; this lesson does not emit WARN or ERROR log events.");
    }

    private static void showCollections() {
        section("COLLECTIONS");
        topic("Choose a structure by its behavior");
        System.out.println("Collections hold groups of values; generics such as <String> say what type goes inside.");
        System.out.println("             order?  duplicates?  lookup");
        System.out.println("List<T>       yes       yes        index");
        System.out.println("Set<T>        varies*   no         contains(value)");
        System.out.println("Map<K,V>      varies*   keys no    get(key) -> value or null");
        System.out.println("Queue<T>      varies    yes        peek / poll");
        System.out.println("List, Set, Queue are interfaces; ArrayList, LinkedHashSet, ArrayDeque are implementations.");
        System.out.println("Map is in the collections framework but is not a Collection subtype.");

        topic("List: order, duplicates, and an unmodifiable snapshot");
        System.out.println("Use a List when the position and every repeated item matter.");
        List<String> list = List.of("Java", "Java", "Python");
        System.out.println("List.of(...) -> unmodifiable List<String>: " + list + " | get(0) -> String: " + list.get(0));
        List<String> mutableList = new ArrayList<>(list);
        List<String> snapshot = List.copyOf(mutableList);
        boolean itemAdded = mutableList.add("Kotlin");
        System.out.println("new ArrayList<>(list) -> mutable; add(Kotlin) -> boolean: " + itemAdded
                + " | list now: " + mutableList);
        System.out.println("List.copyOf before add -> unmodifiable snapshot: " + snapshot);

        topic("Set: one copy of each value");
        System.out.println("Use a Set when each distinct value should appear once.");
        Set<String> set = new LinkedHashSet<>(list);
        boolean duplicateAdded = set.add("Java");
        System.out.println("Set<String>: " + set + " | add(duplicate Java) -> boolean: " + duplicateAdded);

        topic("Map: lookup and counts by key");
        System.out.println("A Map pairs each unique key with one value, such as a language and its count.");
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("Java", 4);
        map.put("Python", 6);
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (String language : list) {
            counts.put(language, counts.getOrDefault(language, 0) + 1);
        }
        System.out.println("Map<String,Integer> lengths: " + map + " | get(Java) -> Integer: " + map.get("Java"));
        System.out.println("counts.put(language, counts.getOrDefault(language, 0) + 1) -> " + counts);
        System.out.println("getOrDefault uses 0 for a name that has no count yet.");
        System.out.println("Map.get(missing) -> Integer or null: " + map.get("missing"));

        topic("Queue: inspect or remove the next item");
        System.out.println("Use this ArrayDeque queue when the first task added should be handled first.");
        Queue<String> queue = new ArrayDeque<>();
        queue.add("first task");
        queue.add("second task");
        System.out.println("Queue before: " + queue + " | peek() -> String: " + queue.peek() + " | still: " + queue);
        System.out.println("poll() -> String: " + queue.poll() + " | remaining: " + queue);
        System.out.println("poll() -> String: " + queue.poll() + " | poll() when empty -> null: " + queue.poll());
        System.out.println("This ArrayDeque queue is FIFO; peek reads the head, while poll removes it.");

        topic("Order and mutability depend on the implementation");
        System.out.println("* LinkedHashSet/LinkedHashMap preserve insertion order; HashSet/HashMap do not promise one.");
        System.out.println("final collection reference can still have mutable contents; List.copyOf makes an unmodifiable snapshot.");
    }

    private static void showEnums() {
        section("ENUMS");
        topic("Define a fixed set of choices");
        System.out.println("An enum defines named values. Use one for a status with only allowed choices:");
        System.out.println("enum Status {");
        System.out.println("    PENDING(\"Waiting for approval\"), ACTIVE(\"Allowed to operate\"),");
        System.out.println("    SUSPENDED(\"Temporarily blocked\");");
        System.out.println("    private final String description;");
        System.out.println("    Status(String description) { this.description = description; }");
        System.out.println("    private String description() { return description; }");
        System.out.println("}");
        System.out.println("Enum constants are Status objects, not arbitrary strings or booleans.");

        topic("Create and inspect enum values");
        System.out.println("Example: an account can be PENDING, ACTIVE, or SUSPENDED.");
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

        topic("Choose behavior with switch");
        System.out.println("A switch expression can give each status its own message.");
        // Enum example 5: switch expressions can return a value for an enum.
        String message = switch (currentStatus) {
            case PENDING -> "Waiting for approval.";
            case ACTIVE -> "Account can be used.";
            case SUSPENDED -> "Account is temporarily blocked.";
        };
        System.out.println("5. switch(Status) -> String -> " + message);
        System.out.println("   use case -> choose behavior for each allowed status");

        topic("Group enum values with EnumSet");
        System.out.println("EnumSet holds a group of enum constants for one shared rule.");
        EnumSet<Status> activeStatuses = EnumSet.of(Status.PENDING, Status.ACTIVE);
        System.out.println("6. EnumSet<Status> -> unique enum values -> " + activeStatuses);
        System.out.println("   use case -> define a group of statuses that need the same treatment");
    }

    private static void showRecords() {
        section("RECORDS");
        topic("Record declaration and generated methods");
        System.out.println("A record is a concise, shallowly immutable data carrier, useful for an API response.");
        System.out.println("record Person(String name, int age) { }");
        System.out.println("// Java generates the constructor, name(), age(), toString(), equals(), hashCode()");
        System.out.println("// The actual nested Person record also defines greeting().");

        topic("Construct and read a record");
        System.out.println("Pass name and age to the constructor, then read them with name() and age().");
        // Record example 1: create a record using its generated constructor.
        Person firstPerson = new Person("Ada", 36);
        Person secondPerson = new Person("Ada", 36);
        System.out.println("1. Person person = new Person(\"Ada\", 36);");
        System.out.println("   object -> " + firstPerson + " | use case -> carry user data without boilerplate");

        // Record example 2: access record components with name() and age(), not getName() or getAge().
        System.out.println("2. name() -> String, age() -> int -> " + firstPerson.name() + ", " + firstPerson.age());
        System.out.println("   usage -> person.name(), person.age()  // records do not use getName()");

        topic("Generated equality and text output");
        System.out.println("Two Person records with the same component values compare equal.");
        // Record example 3: records compare their component values automatically.
        System.out.println("3. equals(Person) -> boolean -> " + firstPerson.equals(secondPerson));

        // Record example 4: records automatically provide a useful toString().
        System.out.println("4. toString() -> String -> " + firstPerson);
        System.out.println("   useful for console output, debugging, and simple data transfer");

        topic("Add behavior with a method");
        System.out.println("A record may also define a method that uses its data.");
        // Record example 5: a record can still have custom methods.
        System.out.println("5. greeting() -> String -> " + firstPerson.greeting());
        System.out.println("   records are concise, but they can still contain behavior related to their data");
    }

    private static void showLambdas() {
        section("LAMBDAS");
        topic("A functional interface gives a lambda its method signature");
        System.out.println("A lambda is a small function passed as a value. Its target interface has one abstract method.");
        System.out.println("Block form: () -> { statement1; statement2; } | expression form: () -> value");

        topic("Runnable: no input, no return value");
        System.out.println("Runnable.run() performs an action and returns void.");
        // Lambda example 1: Runnable has run() -> void and accepts no input.
        Runnable showMessage = () -> {
            System.out.println("   block lambda ran two statements");
            System.out.println("   Runnable.run() returns void");
        };
        System.out.println("1. Runnable = () -> { ... }:");
        showMessage.run();

        topic("Supplier: no input, returns a value");
        System.out.println("Supplier<String>.get() returns a String when called.");
        // Lambda example 2: Supplier<T> has get() -> T and accepts no input.
        Supplier<String> createMessage = () -> {
            String prefix = "Supplier result: ";
            return prefix + "a String";
        };
        System.out.println("Supplier<String> createMessage = () -> {");
        System.out.println("    String prefix = \"Supplier result: \";");
        System.out.println("    return prefix + \"a String\";");
        System.out.println("};");
        System.out.println("2. Supplier<String>.get() -> String -> " + createMessage.get());

        topic("Predicate: test one input");
        System.out.println("Predicate<Integer>.test(...) returns true or false.");
        // Lambda example 3: Predicate<T> accepts a value and returns true or false.
        Predicate<Integer> isEven = number -> number % 2 == 0;
        System.out.println("Predicate<Integer> isEven = number -> number % 2 == 0;");
        System.out.println("3. Predicate<Integer>.test(Integer) -> boolean -> " + isEven.test(8));
        System.out.println("   isEven.test(7) -> boolean -> " + isEven.test(7));

        topic("Function: turn one input into an output");
        System.out.println("Function<String, Integer> turns text into a number.");
        // Lambda example 4: Function<T, R> accepts one type and returns another type.
        Function<String, Integer> stringLength = text -> text.length();
        System.out.println("Function<String, Integer> stringLength = text -> text.length();");
        System.out.println("4. Function<String,Integer>.apply(String) -> Integer -> " + stringLength.apply("Java"));

        topic("Consumer: use one input without returning a value");
        System.out.println("Consumer<String>.accept(...) prints a greeting and returns void.");
        // Lambda example 5: Consumer<T> accepts a value and performs an action without returning one.
        Consumer<String> printGreeting = text -> System.out.println("   Hello, " + text + "!");
        System.out.println("Consumer<String> printGreeting = text -> System.out.println(\"   Hello, \" + text + \"!\");");
        System.out.print("5. Consumer<String>.accept(String) -> void -> ");
        printGreeting.accept("friends");

        topic("Another Function: square a number");
        // Lambda example 6: transform an Integer using a one-parameter lambda.
        Function<Integer, Integer> square = number -> number * number;
        System.out.println("Function<Integer, Integer> square = number -> number * number;");
        System.out.println("6. Function<Integer,Integer> square -> Integer -> " + square.apply(5));

        topic("Two inputs: Comparator");
        System.out.println("A Comparator compares two items and returns an int used for ordering.");
        // Lambda example 7: this comparator has two parameters and is passed to List.sort().
        List<String> names = new ArrayList<>(List.of("Zoe", "Ada", "Mia"));
        names.sort((left, right) -> left.compareTo(right));
        System.out.println("names.sort((left, right) -> left.compareTo(right));");
        System.out.println("7. Comparator<String> (left, right) -> int; sort mutates list order -> " + names);

        topic("A lambda whose interface declares a checked exception");
        System.out.println("ThrowingAction.run() declares throws Exception, so its lambda may throw one.");
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
        section("TERNARY");
        topic("Choose one value from a condition");
        System.out.println("A ternary is a short expression that chooses one of two values.");
        System.out.println("Syntax: condition ? valueIfTrue : valueIfFalse");
        System.out.println("Use it for a short value choice; the if / else lesson shows the block form.");

        int score = 82;
        String result = score >= 60 ? "Pass" : "Fail";
        System.out.println("String result = score >= 60 ? \"Pass\" : \"Fail\"; -> " + result);

        boolean loggedIn = false;
        String greeting = loggedIn ? "Welcome back" : "Please log in";
        System.out.println("String greeting = loggedIn ? \"Welcome back\" : \"Please log in\"; -> " + greeting);

        int age = 16;
        String access = age >= 18 ? "Adult access" : "Minor access";
        System.out.println("String access = age >= 18 ? \"Adult access\" : \"Minor access\"; -> " + access);
    }

    private static void showTraditionalVsModern() {
        heading("BEFORE / AFTER: CHOOSE THE CLEARER JAVA TOOL");
        compareLoopAndStream();
        compareAnonymousClassAndLambda();
        compareClassAndRecord();
        compareNullCheckAndOptional();
        compareIterationStyles();
    }

    private static void compareLoopAndStream() {
        topic("1. LOOP vs STREAM: filter long names, then uppercase");
        System.out.println("Goal: keep names longer than three letters and uppercase them.");
        List<String> names = List.of("Ada", "Clara", "Christopher");
        List<String> loopResult = new ArrayList<>();
        for (String name : names) {
            if (name.length() > 3) {
                loopResult.add(name.toUpperCase());
            }
        }
        List<String> streamResult = names.stream()
                .filter(name -> name.length() > 3)
                .map(String::toUpperCase)
                .toList();
        System.out.println("input List<String> -> " + names);
        System.out.println("loop: if (name.length() > 3) result.add(name.toUpperCase());");
        System.out.println("      List<String> -> " + loopResult);
        System.out.println("stream: names.stream().filter(...).map(String::toUpperCase).toList();");
        System.out.println("        List<String> -> " + streamResult);
        System.out.println("same values? boolean -> " + loopResult.equals(streamResult));
        System.out.println("Loop: easy break/mutation | Stream: clear pipeline; toList() result is unmodifiable.");
    }

    private static void compareAnonymousClassAndLambda() {
        topic("2. ANONYMOUS CLASS vs LAMBDA: implement Runnable.run() -> void");
        System.out.println("Both define work now and run it later when run() is called.");
        Runnable oldAction = new Runnable() {
            @Override
            public void run() {
                System.out.println("   anonymous run() printed");
            }
        };
        Runnable lambdaAction = () -> {
            System.out.println("   lambda run() printed");
        };
        System.out.println("before: new Runnable() { public void run() { ... } }");
        System.out.println("after:  () -> { ... }  // target type is Runnable");
        System.out.println("Assignment stores behavior; neither body ran until run() is called:");
        oldAction.run();
        lambdaAction.run();
        System.out.println("Lambda: concise one-method behavior | Named/anonymous class: more room for structure.");
    }

    private static void compareClassAndRecord() {
        topic("3. CLASS vs RECORD: two separate objects with the same data");
        System.out.println("LegacyPerson inherits identity-based equals; Person gets value equality from record.");
        LegacyPerson oldFirst = new LegacyPerson("Ada", 36);
        LegacyPerson oldSecond = new LegacyPerson("Ada", 36);
        Person recordFirst = new Person("Ada", 36);
        Person recordSecond = new Person("Ada", 36);
        System.out.println("before: new LegacyPerson(\"Ada\", 36) -> name() String "
                + oldFirst.name() + ", age() int " + oldFirst.age());
        System.out.println("after:  new Person(\"Ada\", 36) -> name() String "
                + recordFirst.name() + ", age() int " + recordFirst.age());
        System.out.println("LegacyPerson.equals(second) -> boolean " + oldFirst.equals(oldSecond)
                + " (Object identity: no override)");
        System.out.println("Person.equals(second) -> boolean " + recordFirst.equals(recordSecond)
                + " (record compares components)");
        System.out.println("Record: value data with generated methods | Class: choose custom identity or mutation.");
    }

    private static void compareNullCheckAndOptional() {
        topic("4. NULL CHECK vs OPTIONAL: missing String with a fallback");
        System.out.println("Both examples choose Guest when the String reference is null.");
        String possibleName = null;
        String oldResult = possibleName == null ? "Guest" : possibleName.toUpperCase();
        Optional<String> maybeName = Optional.ofNullable(possibleName);
        String optionalResult = maybeName.map(String::toUpperCase).orElse("Guest");
        System.out.println("input String reference -> null");
        System.out.println("before: null check + ternary -> String -> " + oldResult);
        System.out.println("after:  ofNullable(null) -> Optional<String> -> " + maybeName);
        System.out.println("        map(...).orElse(\"Guest\") -> String -> " + optionalResult);
        System.out.println("Optional.empty is not false; isEmpty() -> boolean -> " + maybeName.isEmpty());
        System.out.println("Null check: fine locally | Optional return type: makes absence explicit to callers.");
    }

    private static void compareIterationStyles() {
        topic("5. INDEXED for vs ENHANCED for vs forEach");
        System.out.println("All three visit the same items; choose based on whether you need an index or callback.");
        List<String> steps = List.of("read", "practice");
        System.out.println("input List<String> -> " + steps);
        for (int index = 0; index < steps.size(); index++) {
            System.out.println("indexed for:  int index " + index + " -> " + steps.get(index));
        }
        for (String step : steps) {
            System.out.println("enhanced for: String step -> " + step);
        }
        steps.forEach(step -> System.out.println("forEach:      Consumer<String> -> " + step));
        System.out.println("Indexed: position | Enhanced: item and break/continue | forEach: callback, no loop break.");
        System.out.println("Enhanced for is Java 5; lambda/forEach is Java 8-era syntax.");
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

    // Teaching-only wire shapes. These are not FlatTire endpoints or persistence entities.
    public record TeachingStudentRequest(String name, int points) {
    }

    public record TeachingStudentResponse(long id, String name, int points) {
    }

    private static class VisibilityExample {
        private static final String COURSE = "Java";
        private final String name;

        VisibilityExample(String name) {
            this.name = name;
        }

        public String publicName() {
            return name;
        }

        protected String role() {
            return "Learner";
        }

        String packageHint() {
            return "same-package access";
        }

        public static String course() {
            return COURSE;
        }
    }

    private static final class VisibilityChild extends VisibilityExample {
        VisibilityChild(String name) {
            super(name);
        }

        @Override
        protected String role() {
            return "Student (using " + publicName() + ")";
        }
    }

    private interface GreetingFormatter {
        String format(StudentDto student);
    }

    // Teaching-only failure types; no FlatTire service or controller uses them.
    private static final class LessonFullException extends Exception {
        LessonFullException(String message) {
            super(message);
        }
    }

    private static final class InvalidScoreException extends RuntimeException {
        InvalidScoreException(int score) {
            super("Score cannot be negative: " + score);
        }
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

    private static final class LegacyPerson {
        private final String name;
        private final int age;

        LegacyPerson(String name, int age) {
            this.name = name;
            this.age = age;
        }

        String name() {
            return name;
        }

        int age() {
            return age;
        }
    }

    @FunctionalInterface
    private interface ThrowingAction {

        void run() throws Exception;
    }

}
