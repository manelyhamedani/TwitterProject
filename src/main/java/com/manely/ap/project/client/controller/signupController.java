package com.manely.ap.project.client.controller;

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

public class signupController {
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String repeatedPassword;
    private String email;
    private String phoneNumber;
    private String country;
    private Date birthdate;

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
    private Button signupButton;

    @FXML
    private Label signupErrorLabel;

    @FXML
    private Label usernameErrorLabel;

    @FXML
    private TextField usernameTextField;

    @FXML
    private HBox repeatPassHBox;

    @FXML
    private HBox phoneNumberHBox;

    @FXML
    private HBox countryHBox;

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
        StringBuffer errMsg = new StringBuffer();

        if (firstname.isBlank()) {
            firstnameErrorLabel.setText("*");
            invalid = true;
        }
        if (lastname.isBlank()) {
            lastnameErrorLabel.setText("*");
            invalid = true;
        }
        if (username.isBlank()) {
            usernameErrorLabel.setText("*");
            invalid = true;
        }
        if (password.isBlank()) {
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

        if (email.isBlank() && phoneNumber.isBlank()) {
            emailErrorLabel1.setText("*");
            phoneNumberErrorLabel1.setText("*");
            errMsg.append("Enter email or phone number!");
            invalid = true;
        }

        if (!password.isBlank() && (password.length() < 8 || !password.matches("[a-zA-Z]+"))) {
            VBox.setMargin(repeatPassHBox, new Insets(8, 0, 0, 0));
            passwordErrorLabel2.setText("Your password must be at least 8 characters!");
            invalid = true;
        }

        if (!repeatedPassword.isBlank() && !repeatedPassword.equals(password)) {
            repeatPasswordErrorLabel2.setText("Repeated password isn't correct!");
            invalid = true;
        }

        if (!email.isBlank() && !EmailValidator.getInstance().isValid(email)) {
            VBox.setMargin(phoneNumberHBox, new Insets(8, 0, 0, 0));
            emailErrorLabel2.setText("Email is invalid!");
            invalid = true;
        }

        if (!phoneNumber.isBlank() && !phoneNumber.matches("[0-9]+")) {
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
        usernameErrorLabel.setText(" ");
        passwordErrorLabel1.setText(" ");
        passwordErrorLabel2.setText("");
        repeatPasswordErrorLabel1.setText(" ");
        repeatPasswordErrorLabel2.setText("");
        emailErrorLabel1.setText(" ");
        emailErrorLabel2.setText("");
        phoneNumberErrorLabel1.setText(" ");
        phoneNumberErrorLabel2.setText("");
        VBox.setMargin(repeatPassHBox, new Insets(0, 0, 0, 0));
    }

    @FXML
    void signupButtonPressed() {
        resetErrorLabels();

        firstname = firstnameTextField.getText();
        lastname = lastnameTextField.getText();
        username = usernameTextField.getText();
        password = passwordTextField.getText();
        repeatedPassword = repeatPasswordTextField.getText();
        email = emailTextField.getText();
        phoneNumber = phoneNumberTextField.getText();
        country = countryComboBox.getSelectionModel().getSelectedItem();
        LocalDate bd = birthdatePicker.getValue();
        if (bd == null) {
            birthdate = null;
        }
        else {
            birthdate = Date.from(birthdatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        if (validateFields()) {

        }
    }

}
