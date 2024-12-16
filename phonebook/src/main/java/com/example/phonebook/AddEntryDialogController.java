package com.example.phonebook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Контроллер диалога добавления контакта.
 * Позволяет ввести ФИО и добавить несколько номеров с типом.
 */
public class AddEntryDialogController {

    private static final Logger logger = LogManager.getLogger(AddEntryDialogController.class);

    @FXML
    private TextField nameField;
    @FXML
    private TableView<PhoneNumber> numbersTable;
    @FXML
    private TableColumn<PhoneNumber, String> numberCol;
    @FXML
    private TableColumn<PhoneNumber, String> typeCol;

    @FXML
    private TextField newNumberField;
    @FXML
    private ComboBox<ContactType> typeComboBox;

    private final ObservableList<PhoneNumber> tempNumbers = FXCollections.observableArrayList();
    private Contact newContact;

    @FXML
    private void initialize() {
        numbersTable.setItems(tempNumbers);

        numberCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNumber())
        );
        typeCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType().getDisplayName())
        );
        typeComboBox.setValue(ContactType.MOBILE);
    }

    /**
     * Добавляет новый номер в таблицу.
     */
    @FXML
    private void onAddNumber() {
        String raw = newNumberField.getText().trim();
        if (!raw.isEmpty()) {
            String formatted = PhoneUtils.formatPhoneNumber(raw);
            ContactType type = typeComboBox.getValue();
            PhoneNumber pn = new PhoneNumber(formatted, type);
            tempNumbers.add(pn);
            newNumberField.clear();
            logger.info("Добавлен номер: {} ({})", formatted, type.getDisplayName());
        }
    }

    /**
     * Сохраняет контакт и закрывает диалог.
     */
    @FXML
    private void onSave() {
        String name = nameField.getText().trim();
        if (name.isEmpty() || tempNumbers.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "ФИО или номера не заполнены!");
            alert.showAndWait();
            return;
        }
        newContact = new Contact(name);
        newContact.getPhoneNumbers().addAll(tempNumbers);
        logger.info("Создан новый контакт: {} с {} номерами", name, tempNumbers.size());
        closeDialog();
    }

    /**
     * Отменяет добавление и закрывает диалог.
     */
    @FXML
    private void onCancel() {
        logger.info("Добавление контакта отменено пользователем.");
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    /**
     * Возвращает созданный контакт или null, если не сохранён.
     */
    public Contact getNewContact() {
        return newContact;
    }
}
