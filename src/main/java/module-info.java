open module dk.sdu.se_f22 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires com.dlsc.formsfx;
    requires commons.dbcp2;
    requires java.management;
    requires java.sql;
    requires json.simple;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;
    requires org.controlsfx.controls;
    requires org.postgresql.jdbc;
    requires validatorfx;
	requires java.desktop;
    requires com.google.gson;
    requires org.apache.commons.pool2;
    requires org.jetbrains.annotations;
    requires org.jsoup;
    exports dk.sdu.se_f22;
	exports dk.sdu.se_f22.searchmodule.onewaysynonyms;
    exports dk.sdu.se_f22.searchmodule.onewaysynonyms.data;
    exports dk.sdu.se_f22.searchmodule.onewaysynonyms.domain;
    exports dk.sdu.se_f22.searchmodule.onewaysynonyms.presentation;
    exports dk.sdu.se_f22.searchmodule.infrastructure.GUI;
}


