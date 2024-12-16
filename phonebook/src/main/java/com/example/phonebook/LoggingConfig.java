package com.example.phonebook;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс для инициализации логирования.
 */
public class LoggingConfig {
    private static final Logger logger = LogManager.getLogger(LoggingConfig.class);

    /**
     * Инициализация логирования (можно вызывать при старте приложения).
     */
    public static void init() {
        logger.info("Логирование инициализировано.");
    }
}
