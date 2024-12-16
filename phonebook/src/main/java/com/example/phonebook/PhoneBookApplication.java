package com.example.phonebook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Главный класс приложения. Запускает JavaFX интерфейс.
 */
public class PhoneBookApplication extends Application {
    private static final Logger logger = LogManager.getLogger(PhoneBookApplication.class);

    @Override
    public void start(Stage stage) throws IOException {
        LoggingConfig.init();
        FXMLLoader loader = new FXMLLoader(
                PhoneBookApplication.class.getResource("phonebook-main.fxml"));
        Scene scene = new Scene(loader.load(), 900, 600);
        stage.setTitle("Телефонный справочник");
        stage.setScene(scene);
        stage.show();
        logger.info("Приложение запущено.");
    }

    public static void main(String[] args) {
        launch();
    }
}
