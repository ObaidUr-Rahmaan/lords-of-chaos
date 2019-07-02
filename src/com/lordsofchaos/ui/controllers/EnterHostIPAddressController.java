package com.lordsofchaos.ui.controllers;

        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.control.Button;
        import javafx.scene.control.TextField;
        import javafx.scene.layout.AnchorPane;

public class EnterHostIPAddressController {

    Util util = new Util();

    public static String HOST_IP;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button returnButton;

    @FXML
    private Button joinLobby;

    @FXML
    private TextField ipField;

    @FXML
    void joinLobbyButtonPressed(ActionEvent event) {

        //we need to add a check to make sure the IP is valid

        HOST_IP = ipField.getText();
        util.openFrame(rootPane, "Player.fxml");
    }

    @FXML
    void returnButtonPressed(ActionEvent event) {
        util.openFrame(rootPane, "MainMenu.fxml");
    }

}
