package com.manely.ap.project.client.controller;

import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.Main;
import com.manely.ap.project.client.callback.ResponseCallback;
import com.manely.ap.project.client.model.Data;
import com.manely.ap.project.client.util.ButtonUtility;
import com.manely.ap.project.common.API;
import com.manely.ap.project.common.model.User;
import com.manely.ap.project.common.model.UserInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.UrlValidator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class EditProfile extends VBox {

    private final FileChooser fileChooser = new FileChooser();
    private com.manely.ap.project.common.model.Image avatar;
    private com.manely.ap.project.common.model.Image header;

    @FXML
    private TextField bioTextField;

    @FXML
    private TextField locationTextField;

    @FXML
    private TextField websiteTextField;

    @FXML
    private ImageView avatarImageView;

    @FXML
    private ImageView headerImageView;

    @FXML
    private Label websiteErrorLabel;

    public EditProfile() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("edit-profile.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        User user = Data.getUser();
        Image avatar = null, header = null;

        if (user.getAvatar() == null) {
            try {
                URL url = new URL("file:src/main/resources/avatar.png");
                avatar = new Image(url.toString());
            }
            catch (MalformedURLException ignore) {
            }
        }
        else {
            avatar = new Image(new ByteArrayInputStream(user.getAvatar().getBytes()));
        }

        if (avatar != null) {
            ButtonUtility.setRoundedImageView(avatarImageView, avatar);
        }

        if (user.getHeader() == null) {
            try {
                URL url = new URL("file:src/main/resources/grey.jpg");
                header = new Image(url.toString());
            }
            catch (MalformedURLException ignore) {
            }
        }
        else {
            header = new Image(new ByteArrayInputStream(user.getHeader().getBytes()));
        }

        if (header != null) {
            headerImageView.setImage(header);
        }

        bioTextField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            int length = newValue.length();
            if (newValue.length() > 160) {
                bioTextField.deleteText(160, length);
            }
        });
    }

    @FXML
    void addAvatarButtonPressed(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        File avatarFile = fileChooser.showOpenDialog(stage);

        if (avatarFile != null) {
            String[] pathParts = avatarFile.getPath().split("\\.");
            String format = pathParts[pathParts.length - 1];
            if (!format.equals("jpg") && !format.equals("png")) {
                return;
            }
            try {
                FileInputStream fis = new FileInputStream(avatarFile);
                avatar = new com.manely.ap.project.common.model.Image(format, fis.readAllBytes(), com.manely.ap.project.common.model.Image.KIND.AVATAR);
                Image image = new Image(new ByteArrayInputStream(avatar.getBytes()));

                ButtonUtility.setRoundedImageView(avatarImageView, image);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void addHeaderButtonPressed(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        File headerFile = fileChooser.showOpenDialog(stage);

        if (headerFile != null) {
            String[] pathParts = headerFile.getPath().split("\\.");
            String format = pathParts[pathParts.length - 1];
            if (!format.equals("jpg") && !format.equals("png")) {
                return;
            }
            try {
                FileInputStream fis = new FileInputStream(headerFile);
                header = new com.manely.ap.project.common.model.Image(format, fis.readAllBytes(), com.manely.ap.project.common.model.Image.KIND.HEADER);
                Image image = new Image(new ByteArrayInputStream(header.getBytes()));

                headerImageView.setImage(image);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void cancelButtonPressed() {
        Scene.gotoHomePage();
    }

    @FXML
    void saveButtonPressed(ActionEvent event) {
        String website = websiteTextField.getText();
        UrlValidator urlValidator = new UrlValidator();
        if (!website.isBlank() && !urlValidator.isValid(website)) {
            websiteErrorLabel.setText("Invalid URL");
            return;
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setBio(bioTextField.getText());
        userInfo.setLocation(locationTextField.getText());
        userInfo.setWebsite(website);

        HttpCall.post(API.SET_USER_INFO, userInfo, Object.class,
                new ResponseCallback<>() {
                    @Override
                    public void run() {
                        if (getResponse().isSuccess()) {
                            Data.getUser().setInfo(userInfo);
                        }
                        else {
                            System.out.println(getResponse().getMessage());
                        }
                    }
                });

        if (avatar != null) {
            HttpCall.post(API.SET_USER_IMAGE, avatar, Object.class,
                    new ResponseCallback<>() {
                        @Override
                        public void run() {
                            if (getResponse().isSuccess()) {
                                Data.getUser().setAvatar(avatar);
                            }
                            else {
                                System.out.println(getResponse().getMessage());
                            }
                        }
                    });
        }

        if (header != null) {
            HttpCall.post(API.SET_USER_IMAGE, header, Object.class,
                    new ResponseCallback<>() {
                        @Override
                        public void run() {
                            if (getResponse().isSuccess()) {
                                Data.getUser().setHeader(header);
                            }
                            else {
                                System.out.println(getResponse().getMessage());
                            }
                        }
                    });
        }

        Scene.gotoHomePage();
    }

}
