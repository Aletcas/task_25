
package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestUtils {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private TestUtils() {
        // Утилитный класс
    }

    /**
     * Простое логирование сообщения.
     */
    public static void log(String message) {
        System.out.println("[" + getCurrentTime() + "] [TEST] " + message);
    }

    /**
     * Логирование шага теста.
     */
    public static void logStep(String step) {
        System.out.println("  → " + step);
    }

    /**
     * Логирование проверки (assertion).
     */
    public static void logAssert(String message, boolean condition) {
        String status = condition ? "✓" : "✗";
        System.out.println("  [" + status + "] " + message);
    }

    /**
     * Разделитель между тестами.
     */
    public static void separator() {
        System.out.println("─".repeat(60));
    }

    /**
     * Форматирование сравнения.
     */
    public static String formatComparison(String label, Object expected, Object actual) {
        boolean match = expected.equals(actual);
        String symbol = match ? "✓" : "✗";
        return String.format("%s: expected=%s, actual=%s %s",
                label, expected, actual, symbol);
    }

    /**
     * Логирование начала теста.
     */
    public static void logTestStart(String testName) {
        separator();
        log("Начало теста: " + testName);
    }

    /**
     * Логирование завершения теста.
     */
    public static void logTestEnd(String testName, boolean passed) {
        String status = passed ? "ПРОЙДЕН" : "ПРОВАЛЕН";
        log("Тест '" + testName + "' " + status);
        separator();
    }

    /**
     * Логирование исключения.
     */
    public static void logException(String context, Exception e) {
        System.err.println("[" + getCurrentTime() + "] [ERROR] " + context);
        System.err.println("  Причина: " + e.getMessage());
        e.printStackTrace();
    }

    private static String getCurrentTime() {
        return LocalDateTime.now().format(formatter);
    }
}