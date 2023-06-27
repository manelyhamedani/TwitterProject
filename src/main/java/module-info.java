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

    opens com.manely.ap.project.client to javafx.fxml, javafx.controls, com.google.gson, jdk.httpserver;
    opens com.manely.ap.project.common.model to com.google.gson, commons.validator;
    opens com.manely.ap.project.database to sqlite.jdbc, java.sql;
    opens com.manely.ap.project.server.jwt to jdk.httpserver, jjwt.api, com.google.gson;

    exports com.manely.ap.project.client;
    exports com.manely.ap.project.client.controller;
    exports com.manely.ap.project.common;
    exports com.manely.ap.project.common.model;
    exports com.manely.ap.project.database;
    exports com.manely.ap.project.database.tables;
    exports com.manely.ap.project.filemanager;
    exports com.manely.ap.project.server;
    exports com.manely.ap.project.server.jwt;
    opens com.manely.ap.project.client.controller to com.google.gson, commons.validator, javafx.controls, javafx.fxml, jdk.httpserver;
}