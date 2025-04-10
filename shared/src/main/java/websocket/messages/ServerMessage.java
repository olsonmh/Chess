package websocket.messages;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    public final ServerMessageType serverMessageType;
    public final String game;
    public final String message;
    public final String errorMessage;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }
    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
        this.game = null;
        this.message = null;
        this.errorMessage = null;

    }

    public ServerMessage(ServerMessageType type, String json) {
        this.serverMessageType = type;
        switch(type){
            case LOAD_GAME:
                this.game = json;
                this.message = null;
                this.errorMessage = null;
                break;
            case ERROR:
                this.game = null;
                this.message = null;
                this.errorMessage = json;
                break;
            case NOTIFICATION:
                this.game = null;
                this.message = json;
                this.errorMessage = null;
                break;
            default:
                this.game = json;
                this.message = json;
                this.errorMessage = json;
                break;
        }
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage)) {
            return false;
        }
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
