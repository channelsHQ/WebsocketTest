/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.channelshq.messenger;

import com.channelshq.websocketclient.MainController;
import java.lang.reflect.Type;
import javafx.application.Platform;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

/**
 *
 * @author heyphord
 */
public class SubscriptionHandler_Messages implements StompFrameHandler {

        private MainController controller ;
        

    @Override
    public Type getPayloadType(StompHeaders sh) {
        return Message.class;
    }

    @Override
    public void handleFrame(StompHeaders sh, Object payload) {
       //  this.payload= (Message) payload;

        Platform.runLater(  new Runnable() {
            @Override
            public void run() {
                
                   controller.getListView().getItems().add("From SubscriptionHandler.handleFrame: Messaged received from server: msg");
            }
        });
    }

     public MainController getController() {
        return controller;
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }

}
