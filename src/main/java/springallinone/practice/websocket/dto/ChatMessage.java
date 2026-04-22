package springallinone.practice.websocket.dto;

public record ChatMessage(String sender, String content, MessageType type) {

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }
}
