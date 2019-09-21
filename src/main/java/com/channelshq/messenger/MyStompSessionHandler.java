package com.channelshq.messenger;

import com.channelshq.websocketclient.MainController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import javafx.application.Platform;

/**
 * This class is an implementation for <code>StompSessionHandlerAdapter</code>.
 * Once a connection is established, We subscribe to /topic/messages and 
 * send a sample message to server.
 * 
 * @author Kalyan
 *
 */
public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    private Logger logger = LogManager.getLogger(MyStompSessionHandler.class);
    
    MainController controller;
    
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        Platform.runLater( new Runnable() {
            @Override
            public void run() {
                controller.getListView().getItems().add("From SessionHandler: COnnection successfull : ");
            }
        });
        //logger.info("New session established : " + session.getSessionId());
        
        //session.subscribe("/topic/messages", this);
        //logger.info("From Handler: Subscribed to /topic/messages");
//        controller.getListView().getItems().add("From Handler: Subscribed to /topic/messages");

//        session.send("/app/channels", getSampleMessage());
//        logger.info("Message sent to websocket server");
//                controller.getListView().getItems().add("From Handler: Message sent to webserver");

    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        logger.error("Got an exception", exception);
    }

    public MainController getController() {
        return controller;
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }

    

    /**
     * A sample message instance.
     * @return instance of <code>Message</code>
     */
    private Message getSampleMessage() {
        Message msg = new Message();
        msg.setFrom("CLient App");
        msg.setMessage("I originally sent from the client app!!");
        return msg;
    }
}