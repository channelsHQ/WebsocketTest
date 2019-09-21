/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.channelshq.websocketclient;

import com.channelshq.messenger.Message;
import com.channelshq.messenger.MyStompSessionHandler;
import com.channelshq.messenger.SubscriptionHandler_Messages;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
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

    private String URL = "http://localhost:8080/channels";
    StompSession session = null;

    @FXML
    private TextArea txtMessage;
    @FXML
    private JFXListView<String> lstChat;
    @FXML
    private JFXButton btnClear;

    ObservableList<String> observableList = FXCollections.observableArrayList();
    
    Subscription subscription;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lstChat.setItems(observableList);

    }

    @FXML
    private void connect(ActionEvent event) {
        WebSocketClient standardWebSocketClient = new StandardWebSocketClient();

        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(standardWebSocketClient));
        SockJsClient sockJsClient = new SockJsClient(transports);

        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        MyStompSessionHandler sessionHandler = new MyStompSessionHandler();
        sessionHandler.setController(this);

        try {
            session = stompClient.connect(URL, sessionHandler).get();
        } catch (InterruptedException | ExecutionException ex) {
            System.err.println(ex.getMessage());
        }

        
    }

    @FXML
    private void sendMessage(ActionEvent event) {

        System.out.println("is connected:+ " + session.isConnected());
//        Platform.runLater(new Runnable() {
//
//            @Override
//            public void run() {
        observableList.add("Clicked on send button");
        String txt = (txtMessage.getText() == null) ? "null" : txtMessage.getText();
        session.send("/app/channels", new Message(txt, txt, txt));
//            }
//        });

    }

    @FXML
    private void disconnect(ActionEvent event) {

        session.disconnect();
        observableList.add("Disconnected");
    }
    


    @FXML
    private void clear(ActionEvent event) {
        observableList.clear();
    }

    @FXML
    private void subscribe(ActionEvent event) {
        //subscribe
        SubscriptionHandler_Messages subscriptionHandler = new SubscriptionHandler_Messages();
        subscriptionHandler.setController(this);
        
        subscription = session.subscribe("/topic/messages", subscriptionHandler);
        observableList.add("From Subscribe method: Subscribed to /topic/messages");

        System.out.println("From Subscribe method: Subscribed to /topic/messages");
    }

    @FXML
    private void unsubscibe(ActionEvent event) {
        subscription.unsubscribe();
        observableList.add("Unsubscribed!");
    }

    public TextArea getTxtMessage() {
        return txtMessage;
    }

    public JFXListView<String> getListView() {
        return lstChat;
    }
}
