package com.example.javajavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.json.*;

public class HelloApplication extends Application {

    static ArrayList<User> users = new ArrayList<>();
    static String dirUriString = System.getProperty("java.io.tmpdir") + "javajavafx";
    static Path dirPath = Path.of(dirUriString);
    static String fileUriString = dirUriString + "\\database.json";
    static Path filePath = Path.of(fileUriString);

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        HelloApplication.readFile();
        launch();
    }

    public static void readFile() {
        if (Files.exists(filePath)) {
            try {
                var file = Files.readString(dirPath);
                var json = new JSONArray(file.toString());
                json.forEach(user ->
                        users.add(User.fromJson((JSONObject) user))
                );
            } catch (IOException e) {
                System.out.println("Deu errado IO");
            }
        } else {
            new File(dirUriString).mkdir();
            try {
                new File(fileUriString).createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void writeFile() {
        try {
            var writeString = "[";
            for (int index = 0; index < users.size(); index++) {
                writeString += User.toJson(users.get(index));
                if (index != users.size() - 1) {
                    writeString += ",";
                }
            }
            writeString += "]";
            Files.writeString(filePath, writeString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}