module com.example.javajavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;

    opens com.example.javajavafx to javafx.fxml;
    exports com.example.javajavafx;
}