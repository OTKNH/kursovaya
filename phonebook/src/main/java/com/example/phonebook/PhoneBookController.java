package com.example.phonebook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер главного окна.
 * Отображает контакты в виде строк (одна строка - один номер).
 * Позволяет добавлять/удалять контакты, сохранять/загружать базу,
 * фильтровать по ФИО и номеру, сортировать, менять масштаб таблицы.
 */
public class PhoneBookController {

    private static final Logger logger = LogManager.getLogger(PhoneBookController.class);

    @FXML
    private TableView<PhoneEntryRow> phoneTable;
    @FXML
    private TableColumn<PhoneEntryRow, String> nameColumn;
    @FXML
    private TableColumn<PhoneEntryRow, String> numberColumn;
    @FXML
    private TableColumn<PhoneEntryRow, String> typeColumn;

    @FXML
    private TextField searchNameField;
    @FXML
    private TextField searchPhoneField;

    @FXML
    private Slider zoomSlider;

    private final ObservableList<Contact> contactList = FXCollections.observableArrayList();
    private final ObservableList<PhoneEntryRow> displayedRows = FXCollections.observableArrayList();

    /**
     * Инициализация контроллера. Настраивает столбцы таблицы, масштабирование и логгирование.
     */
    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().fio()));
        numberColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().number()));
        typeColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().type().getDisplayName()));

        phoneTable.setItems(displayedRows);

        phoneTable.setOnScroll(event -> {
            if (event.isControlDown()) {
                double delta = event.getDeltaY();
                double scaleFactor = (delta > 0) ? 1.1 : 0.9;
                double oldScale = phoneTable.getScaleX();
                double newScale = oldScale * scaleFactor;
                if (newScale < 0.5) newScale = 0.5;
                if (newScale > 2.0) newScale = 2.0;
                phoneTable.setScaleX(newScale);
                phoneTable.setScaleY(newScale);
                zoomSlider.setValue(newScale);
                event.consume();
            }
        });
        zoomSlider.setValue(1.0);
        zoomSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            phoneTable.setScaleX(newVal.doubleValue());
            phoneTable.setScaleY(newVal.doubleValue());
        });

        logger.info("Главное окно инициализировано.");
    }

    /**
     * Запускает фильтрацию контактов на основе введённых пользователем критериев.
     */
    private void doFilter() {
        refreshRows();
    }

    /**
     * Проверяет, соответствует ли заданный контакт критериям фильтрации по ФИО и номеру.
     *
     * @param contact контакт, который нужно проверить
     * @return true, если контакт удовлетворяет условиям поиска, иначе false
     */
    private boolean matchesFilters(Contact contact) {
        String nameQuery = searchNameField.getText().toLowerCase().trim();
        String phoneQuery = searchPhoneField.getText().toLowerCase().trim();

        boolean matchName = contact.getFullName().toLowerCase().contains(nameQuery);
        if (!matchName) return false;

        if (!phoneQuery.isEmpty()) {
            return contact.getPhoneNumbers().stream()
                    .anyMatch(pn -> pn.getNumber().toLowerCase().contains(phoneQuery));
        } else {
            return true;
        }
    }

    /**
     * Обновляет список отображаемых строк таблицы на основе отфильтрованных контактов.
     */
    private void refreshRows() {
        List<Contact> filteredContacts = contactList.stream()
                .filter(this::matchesFilters)
                .toList();

        displayedRows.clear();

        for (Contact c : filteredContacts) {
            for (PhoneNumber pn : c.getPhoneNumbers()) {
                PhoneEntryRow row = new PhoneEntryRow(c.getFullName(), pn.getNumber(), pn.getType());
                displayedRows.add(row);
            }
        }
        phoneTable.refresh();
    }

    /**
     * Обработчик нажатия на кнопку "Добавить". Открывает диалог добавления контакта.
     */
    @FXML
    private void onAddButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-entry-dialog.fxml"));
            Parent root = loader.load();
            AddEntryDialogController controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Добавить контакт");
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            Contact newContact = controller.getNewContact();
            if (newContact != null) {
                contactList.add(newContact);
                logger.info("Контакт '{}' добавлен ({} номеров).", newContact.getFullName(), newContact.getPhoneNumbers().size());
                refreshRows();
            }
        } catch (IOException e) {
            logger.error("Ошибка при добавлении контакта: {}", e.getMessage(), e);
            showError("Не удалось открыть окно добавления контакта");
        }
    }

    /**
     * Обработчик нажатия на кнопку "Удалить". Удаляет выбранный номер или контакт.
     */
    @FXML
    private void onDeleteButtonClick() {
        PhoneEntryRow selectedRow = phoneTable.getSelectionModel().getSelectedItem();
        if (selectedRow == null) return;

        Contact contact = contactList.stream()
                .filter(c -> c.getFullName().equals(selectedRow.fio()))
                .findFirst().orElse(null);

        if (contact != null) {
            contact.getPhoneNumbers().removeIf(pn ->
                    pn.getNumber().equals(selectedRow.number()) && pn.getType() == selectedRow.type());
            if (contact.getPhoneNumbers().isEmpty()) {
                contactList.remove(contact);
                logger.info("Контакт '{}' удалён полностью (не осталось номеров).", contact.getFullName());
            } else {
                logger.info("Из контакта '{}' удалён номер '{} ({})'", contact.getFullName(), selectedRow.number(), selectedRow.type().getDisplayName());
            }
            refreshRows();
        }
    }

    /**
     * Обработчик нажатия на кнопку "Сортировать по ФИО". Сортирует контакты по их именам.
     */
    @FXML
    private void sortByName() {
        contactList.sort((c1, c2) -> c1.getFullName().compareToIgnoreCase(c2.getFullName()));
        refreshRows();
        logger.info("Список отсортирован по ФИО.");
    }

    /**
     * Обработчик нажатия на кнопку "Сохранить". Открывает диалог выбора файла для сохранения данных.
     */
    @FXML
    private void onSaveAsFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить как...");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Data file (*.dat)", "*.dat"));
        File file = fileChooser.showSaveDialog(phoneTable.getScene().getWindow());
        if (file != null) {
            saveToFile(file);
        }
    }

    /**
     * Обработчик нажатия на кнопку "Загрузить". Открывает диалог выбора файла для загрузки данных.
     */
    @FXML
    private void onLoadFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузить файл...");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Data file (*.dat)", "*.dat"));
        File file = fileChooser.showOpenDialog(phoneTable.getScene().getWindow());
        if (file != null) {
            loadFromFile(file);
        }
    }

    /**
     * Сохраняет текущий список контактов в указанный файл.
     *
     * @param file файл, в который нужно сохранить данные
     */
    private void saveToFile(File file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(new ArrayList<>(contactList));
            logger.info("Данные сохранены в файл {}", file.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Ошибка сохранения: {}", e.getMessage(), e);
            showError("Не удалось сохранить данные: " + e.getMessage());
        }
    }

    /**
     * Загружает список контактов из указанного файла.
     *
     * @param file файл, из которого нужно загрузить данные
     */
    private void loadFromFile(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Contact> loaded = (List<Contact>) ois.readObject();
            contactList.clear();
            contactList.addAll(loaded);
            refreshRows();
            logger.info("Данные загружены из файла {}", file.getAbsolutePath());
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Ошибка загрузки: {}", e.getMessage(), e);
            showError("Не удалось загрузить данные: " + e.getMessage());
        }
    }

    /**
     * Показывает диалоговое окно с сообщением об ошибке.
     *
     * @param message текст сообщения об ошибке
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

    /**
         * Внутренний класс для строк таблицы. Каждая строка представляет один номер одного контакта.
         */
        private record PhoneEntryRow(String fio, String number, ContactType type) {
        /**
         * Создаёт строку таблицы на основе данных о контакте и конкретном номере.
         *
         * @param fio    ФИО контакта
         * @param number номер телефона
         * @param type   тип номера
         */
        private PhoneEntryRow {
        }

            /**
             * Возвращает ФИО, отображаемое в строке.
             */
            @Override
            public String fio() {
                return fio;
            }

            /**
             * Возвращает номер телефона, отображаемый в строке.
             */
            @Override
            public String number() {
                return number;
            }

            /**
             * Возвращает тип номера телефона, отображаемый в строке.
             */
            @Override
            public ContactType type() {
                return type;
            }
        }
}
