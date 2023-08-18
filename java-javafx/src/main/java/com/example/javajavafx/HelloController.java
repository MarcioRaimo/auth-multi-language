package com.example.javajavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.Objects;

public class HelloController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    protected void authClick() {
        boolean control = false;
        var alert = new Alert(Alert.AlertType.NONE);
        for (User user : HelloApplication.users) {
            if (user.email.equals(this.emailField.getText())) {
                control = true;
                if (user.password.equals(this.passwordField.getText())) {
                    alert.setAlertType(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Usuário autenticado");
                    alert.showAndWait();
                    this.emailField.setText("");
                    this.passwordField.setText("");
                    return;
                }
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("Senha incorreta");
                alert.showAndWait();
                return;
            }
        }
        if (!control) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Usuário não encontrado");
            alert.showAndWait();
        }
    }

    @FXML
    protected void registerClick() {
        var alert = new Alert(Alert.AlertType.NONE);
        if (this.emailField.getText().isEmpty() || this.passwordField.getText().isEmpty()) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Preencha todos os campos");
            alert.showAndWait();
            return;
        }
        if (!this.emailField.getText().contains("@")) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("E-mail inválido");
            alert.showAndWait();
            return;
        }
        for (User user : HelloApplication.users) {
            if (user.email.equals(this.emailField.getText())) {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("Usuário já cadastrado");
                alert.showAndWait();
                return;
            }
        }
        HelloApplication.users.add(new User(this.emailField.getText(), this.passwordField.getText()));
        HelloApplication.writeFile();
        this.emailField.setText("");
        this.passwordField.setText("");
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Usuário registrado!");
        alert.showAndWait();
    }
}