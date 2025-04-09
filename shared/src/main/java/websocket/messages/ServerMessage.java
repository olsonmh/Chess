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

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }
    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
        this.game = null;
        this.message = null;

    }

    public ServerMessage(ServerMessageType type, String json) {
        this.serverMessageType = type;
        switch(type){
            case LOAD_GAME:
                this.game = json;
                this.message = null;
                break;
            case ERROR:
            case NOTIFICATION:
                this.game = null;
                this.message = json;
                break;
            default:
                this.game = json;
                this.message = json;
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
