package springallinone.practice.websocket.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import springallinone.practice.websocket.dto.ChatMessage;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("Websocket connected: sessionId= {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage msg) throws Exception {
        ChatMessage chatMsg = objectMapper.readValue(msg.getPayload(), ChatMessage.class);
        String payload = objectMapper.writeValueAsString(chatMsg);
        broadcast(payload);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("Websocket disconnected: sessionId= {}, status= {}", session.getId(), status);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable t) {
        sessions.remove(session);
        log.error("Websocket transport error: sessionId= {}", session.getId(), t);
    }



    private void broadcast(String payload) {
        sessions.forEach(session -> {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(payload));
                } catch (IOException e) {
                    log.error("Unable to send message to sesion: {}", session.getId(), e);
                }
            }
        });
    }
}
