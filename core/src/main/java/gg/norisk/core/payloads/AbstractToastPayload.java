package gg.norisk.core.payloads;

import java.util.UUID;

/**
 * Abstract payload for displaying toast notifications in the NoRisk Client
 */
public abstract class AbstractToastPayload extends AbstractPayload {
    private final boolean progressBar;
    private final String header;
    private final String content;
    private final boolean playerHead;
    private final UUID playerUUID;
    private final ToastType toastType;
    
    /**
     * Creates a new toast payload
     * @param progressBar Whether to show a progress bar in the toast
     * @param header The header text of the toast notification
     * @param content The main content text of the toast notification
     * @param playerHead Whether to show the player's avatar in the toast
     * @param playerUUID The UUID of the player whose avatar to show (required if playerHead is true)
     * @param toastType The type of toast (INFO, SUCCESS, ERROR) which determines the color scheme
     */
    public AbstractToastPayload(boolean progressBar, String header, String content, 
                               boolean playerHead, UUID playerUUID, ToastType toastType) {
        super("toast");
        this.progressBar = progressBar;
        this.header = header;
        this.content = content;
        this.playerHead = playerHead;
        this.playerUUID = playerUUID;
        this.toastType = toastType;
    }
    
    public boolean isProgressBar() {
        return progressBar;
    }
    
    public String getHeader() {
        return header;
    }
    
    public String getContent() {
        return content;
    }
    
    public boolean isPlayerHead() {
        return playerHead;
    }
    
    public UUID getPlayerUUID() {
        return playerUUID;
    }
    
    public ToastType getToastType() {
        return toastType;
    }
} 