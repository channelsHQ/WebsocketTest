/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.channelshq.websocketclient;

import com.channelshq.messenger.Message;
import com.channelshq.messenger.MyStompSessionHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.apache.logging.log4j.LogManager;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

/**
 * FXML Controller class
 *
 * @author heyphord
 */
public class MainController implements Initializable {

    @FXML
    private JFXTextField txtSubscribeTo;
    @FXML
    private JFXTextField txtSendTo;
    @FXML
    private JFXTextArea txtMessage;
    @FXML
    private JFXButton btnSend;
    @FXML
    private JFXListView<String> lstChat;

    private static String URL = "http://localhost:8080/channels";
    StompSession session = null;

        private org.apache.logging.log4j.Logger logger = LogManager.getLogger(MainController.class);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        WebSocketClient standardWebSocketClient = new StandardWebSocketClient();

        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(standardWebSocketClient));
        SockJsClient sockJsClient = new SockJsClient(transports);

        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        try {
            session = stompClient.connect(URL, sessionHandler).get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void sendMessage(ActionEvent event) {

                    logger.info("sending a new message on button clicked");

            session.send("app/channels", new Message("Main Class", "IDK", "some message bi oo"));
        
    }

}
