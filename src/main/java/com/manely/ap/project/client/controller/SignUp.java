package com.manely.ap.project.client.controller;

import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.ResponseCallback;
import com.manely.ap.project.common.API;
import com.manely.ap.project.common.model.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.commons.validator.routines.EmailValidator;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class SignUp {
    private final User user = new User();
    private String repeatedPassword;

    @FXML
    private DatePicker birthdatePicker;

    @FXML
    private ComboBox<String> countryComboBox;

    @FXML
    private Label emailErrorLabel1;

    @FXML
    private Label emailErrorLabel2;

    @FXML
    private TextField emailTextField;

    @FXML
    private Label firstnameErrorLabel;

    @FXML
    private TextField firstnameTextField;

    @FXML
    private Label lastnameErrorLabel;

    @FXML
    private TextField lastnameTextField;

    @FXML
    private Label passwordErrorLabel1;

    @FXML
    private Label passwordErrorLabel2;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private Label phoneNumberErrorLabel1;

    @FXML
    private Label phoneNumberErrorLabel2;

    @FXML
    private Label repeatPasswordErrorLabel1;

    @FXML
    private Label repeatPasswordErrorLabel2;

    @FXML
    private PasswordField repeatPasswordTextField;

    @FXML
    private Label signupErrorLabel;

    @FXML
    private Label usernameErrorLabel1;

    @FXML
    private Label usernameErrorLabel2;

    @FXML
    private TextField usernameTextField;

    @FXML
    private HBox passwordHBox;

    @FXML
    private HBox repeatPassHBox;

    @FXML
    private HBox phoneNumberHBox;

    @FXML
    private HBox countryHBox;

    @FXML
    private Label signupLabel;

    public void initialize() {
        passwordErrorLabel2.setWrapText(true);
        countryComboBox.setStyle("-fx-font-family: 'Apple Braille'");
        birthdatePicker.setStyle("-fx-font-family: 'Apple Braille'");

        ObservableList<String> countries = FXCollections.observableArrayList();
        String[] countryCodes = Locale.getISOCountries();

        for (String cc : countryCodes) {
            Locale country = new Locale("", cc);
            countries.add(country.getDisplayCountry());
        }

        countryComboBox.setItems(countries);
    }

    private boolean validateFields() {
        boolean invalid = false;
        StringBuilder errMsg = new StringBuilder();

        if (user.getFirstName().isBlank()) {
            firstnameErrorLabel.setText("*");
            invalid = true;
        }
        if (user.getLastName().isBlank()) {
            lastnameErrorLabel.setText("*");
            invalid = true;
        }
        if (user.getUsername().isBlank()) {
            usernameErrorLabel1.setText("*");
            invalid = true;
        }
        if (user.getPassword().isBlank()) {
            passwordErrorLabel1.setText("*");
            invalid = true;
        }
        if (repeatedPassword.isBlank()) {
            repeatPasswordErrorLabel1.setText("*");
            invalid = true;
        }

        if (invalid) {
            errMsg.append("Fill required fields!\n");
        }

        if (user.getEmail().isBlank() && user.getPhoneNumber().isBlank()) {
            emailErrorLabel1.setText("*");
            phoneNumberErrorLabel1.setText("*");
            errMsg.append("Enter email or phone number!");
            invalid = true;
        }

        if (!user.getPassword().isBlank() && (user.getPassword().length() < 8 || !user.getPassword().matches("[a-zA-Z]+"))) {
            VBox.setMargin(repeatPassHBox, new Insets(8, 0, 0, 0));
            passwordErrorLabel2.setText("Your password must be at least 8 characters!");
            invalid = true;
        }

        if (!repeatedPassword.isBlank() && !repeatedPassword.equals(user.getPassword())) {
            repeatPasswordErrorLabel2.setText("Repeated password isn't correct!");
            invalid = true;
        }

        if (!user.getEmail().isBlank() && !EmailValidator.getInstance().isValid(user.getEmail())) {
            VBox.setMargin(phoneNumberHBox, new Insets(8, 0, 0, 0));
            emailErrorLabel2.setText("Email is invalid!");
            invalid = true;
        }

        if (!user.getPhoneNumber().isBlank() && !user.getPhoneNumber().matches("[0-9]+")) {
            VBox.setMargin(countryHBox, new Insets(8, 0, 0, 0));
            phoneNumberErrorLabel2.setText("Phone number is invalid!");
            invalid = true;
        }

        if (invalid) {
            signupErrorLabel.setText(errMsg.toString());
            return false;
        }

        return true;
    }

    private void resetErrorLabels() {
        signupErrorLabel.setText("");
        firstnameErrorLabel.setText(" ");
        lastnameErrorLabel.setText(" ");
        usernameErrorLabel1.setText(" ");
        usernameErrorLabel2.setText("");
        passwordErrorLabel1.setText(" ");
        passwordErrorLabel2.setText("");
        repeatPasswordErrorLabel1.setText(" ");
        repeatPasswordErrorLabel2.setText("");
        emailErrorLabel1.setText(" ");
        emailErrorLabel2.setText("");
        phoneNumberErrorLabel1.setText(" ");
        phoneNumberErrorLabel2.setText("");
        VBox.setMargin(repeatPassHBox, new Insets(0, 0, 0, 0));
        VBox.setMargin(phoneNumberHBox, new Insets(0, 0, 0, 0));
        VBox.setMargin(countryHBox, new Insets(0, 0, 0, 0));
        VBox.setMargin(passwordHBox, new Insets(0, 0, 0, 0));
    }

    @FXML
    void signupButtonPressed() {
        resetErrorLabels();

        user.setFirstName(firstnameTextField.getText());
        user.setLastName(lastnameTextField.getText());
        user.setUsername(usernameTextField.getText());
        user.setPassword(passwordTextField.getText());
        repeatedPassword = repeatPasswordTextField.getText();
        user.setEmail(emailTextField.getText());
        user.setPhoneNumber(phoneNumberTextField.getText());
        user.setCountry(countryComboBox.getSelectionModel().getSelectedItem());
        LocalDate bd = birthdatePicker.getValue();
        if (bd != null) {
            user.setBirthDate(Date.from(birthdatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }

        if (validateFields()) {
            if (user.getEmail().isBlank()) {
                user.setEmail(null);
            }
            if (user.getPhoneNumber().isBlank()) {
                user.setPhoneNumber(null);
            }
            HttpCall.post(API.SIGNUP, user, Object.class,
                    new ResponseCallback<>() {
                        @Override
                        public void run() {
                            if (getResponse().isSuccess()) {
                                Platform.runLater(() -> signupLabel.setText("Your account has been successfully created"));
                                try{
                                    sleep(2000);
                                }
                                catch (InterruptedException ignore) {

                                }
                                Scene.changeScene("entry.fxml");
                            }
                            else {
                                String errMsg = getResponse().getMessage();
                                Platform.runLater(() ->
                                {
                                    if (errMsg.contains("SQLITE_CONSTRAINT_UNIQUE")) {
                                        if (errMsg.contains("Username")) {
                                            VBox.setMargin(passwordHBox, new Insets(8, 0, 0, 0));
                                            usernameErrorLabel2.setText("Username is already in use!");
                                        } else if (errMsg.contains("Email")) {
                                            VBox.setMargin(phoneNumberHBox, new Insets(8, 0, 0, 0));
                                            emailErrorLabel2.setText("Email is already in use!");
                                        } else if (errMsg.contains("PhoneNumber")) {
                                            VBox.setMargin(countryHBox, new Insets(8, 0, 0, 0));
                                            phoneNumberErrorLabel2.setText("Phone number is already in use!");
                                        }
                                    } else {
                                        signupErrorLabel.setText("Internal Error!");
                                        System.out.println(getResponse().getMessage());
                                    }
                                });
                            }
                        }
                    });
        }
    }

}
