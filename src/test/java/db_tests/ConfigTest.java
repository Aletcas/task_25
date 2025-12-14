package db_tests;

import config.TestConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Test;
import utils.TestUtils;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigTest {
    @Test
    void testConfigLoading() {
        TestUtils.log("Проверка загрузки конфигурации...");

        // Загружаем конфиг
        TestConfig config = ConfigFactory.create(TestConfig.class);

        // Выводим все значения
        TestUtils.log("db.url: " + config.dbUrl());
        TestUtils.log("db.user: " + config.dbUser());
        TestUtils.log("db.password: " + (config.dbPassword() != null ? "***" : "NULL"));
        TestUtils.log("ui.base.url: " + config.uiBaseUrl());

        // Проверяем
        assertNotNull(config.dbUrl(), "db.url не должен быть null");
        assertNotNull(config.dbUser(), "db.user не должен быть null");
        assertNotNull(config.dbPassword(), "db.password не должен быть null");

        TestUtils.log("✓ Конфигурация загружена успешно");
    }

    @Test
    void debugConfig() {
        // Проверим, где Java ищет файл
        URL resource = getClass().getClassLoader().getResource("test.properties");
        System.out.println("Путь к test.properties: " + (resource != null ? resource.getPath() : "НЕ НАЙДЕН"));

        // Попробуем загрузить напрямую
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("test.properties")) {
            if (is == null) {
                System.out.println("Файл не найден в classpath!");
            } else {
                System.out.println("Файл найден, читаем...");
                String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                System.out.println("Содержимое:\n" + content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    void debugOwnerIssue() {
        // Способ 1: Простая загрузка
        System.out.println("=== Способ 1: ConfigFactory.create ===");
        TestConfig config1 = ConfigFactory.create(TestConfig.class);
        System.out.println("dbUrl: " + config1.dbUrl());

        // Способ 2: Через загрузчик класса
        System.out.println("\n=== Способ 2: Проверка classpath ===");
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        java.net.URL url = cl.getResource("test.properties");
        System.out.println("test.properties URL: " + url);

        if (url != null) {
            try (java.io.InputStream is = url.openStream()) {
                String content = new String(is.readAllBytes());
                System.out.println("Содержимое файла:\n" + content);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
