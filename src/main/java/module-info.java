module com.manely.ap.project.twitterproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.validator;
    requires java.desktop;
    requires java.sql;
    requires sqlite.jdbc;
    requires jjwt.api;
    requires com.google.gson;
    requires jdk.httpserver;


    opens com.manely.ap.project.client to javafx.fxml;
    opens com.manely.ap.project.client.controller to javafx.fxml;
    exports com.manely.ap.project.client;
    exports com.manely.ap.project.client.controller;
}