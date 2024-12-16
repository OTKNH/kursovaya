module com.example.phonebook {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    opens com.example.phonebook;
    exports com.example.phonebook;
}