package gg.norisk.core.common;

import gg.norisk.core.payloads.*;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Main API class for creating and managing NoRisk Client payloads
 * This class provides convenient methods to create various types of payloads
 * that can be sent to NoRisk Client users.
 */
public interface NoRiskServerApi {

    /**
     * Creates a toast notification payload to display messages in the NoRisk Client
     *
     * @param progressBar Whether to show a progress bar in the toast
     * @param header      The header text of the toast notification
     * @param content     The main content text of the toast notification
     * @param playerHead  Whether to show the player's avatar in the toast
     * @param playerUUID  The UUID of the player whose avatar to show (required if playerHead is true)
     * @param toastType   The type of toast (INFO, SUCCESS, ERROR) which determines the color scheme
     * @return AbstractToastPayload The created toast payload ready to be sent
     */
    AbstractToastPayload createToastPayload(
            boolean progressBar,
            String header,
            String content,
            boolean playerHead,
            UUID playerUUID,
            ToastType toastType
    );

    /**
     * Creates a module deactivate payload to disable specific NoRisk Client modules
     *
     * @param modules List of modules to deactivate on the client
     * @return AbstractModuleDeactivatePayload The created module deactivate payload ready to be sent
     */
    AbstractModuleDeactivatePayload createModuleDeactivatePayload(
            List<Modules> modules
    );

    /**
     * Creates a beacon beam payload to display colored light beams at specific coordinates
     *
     * @param xyz       The X, Y, Z coordinates where the beacon beam should appear
     * @param dimension The dimension where the beacon beam should appear (OVERWORLD, NETHER, END)
     * @param color     The RGB color of the beacon beam (values 0-255 for each component)
     * @return AbstractBeaconBeamPayload The created beacon beam payload ready to be sent
     */
    AbstractBeaconBeamPayload createBeaconBeamPayload(
            XYZ xyz,
            Dimension dimension,
            RGBColor color
    );

    /**
     * Creates a gamemode payload to override the gamemode display in the NoRisk Client
     *
     * @param gamemode The custom gamemode name to display in the client
     * @return AbstractGamemodePayload The created gamemode payload ready to be sent
     */
    AbstractGamemodePayload createGamemodePayload(
            String gamemode
    );

    /**
     * Creates a wheel payload to add entries to the NoRisk Client action wheel
     *
     * @param name    The display name of the action wheel entry
     * @param command The command to execute when the entry is selected
     * @return AbstractWheelPayload The created wheel payload ready to be sent
     */
    AbstractWheelPayload createWheelPayload(
            String name,
            String command
    );

    /**
     * Creates an inputbar payload to show input prompts in the NoRisk Client
     *
     * @param input       The header text for the input prompt
     * @param placeholder The placeholder text shown in the input field (can be empty)
     * @param maxLength   The maximum number of characters allowed in the input
     * @return AbstractInputbarPayload The created inputbar payload ready to be sent
     */
    AbstractInputbarPayload createInputbarPayload(
            String input,
            String placeholder,
            int maxLength
    );

    /**
     * Requests input from a player and handles the response
     *
     * @param playerUuid      The UUID of the player to request input from
     * @param inputbarPayload The inputbar payload containing the prompt details
     * @param sendToClient    Function to send the payload data to the client
     * @param onResponse      Callback function called when the player provides input
     */
    void requestInput(
            UUID playerUuid,
            AbstractInputbarPayload inputbarPayload,
            BiConsumer<UUID, byte[]> sendToClient,
            Consumer<String> onResponse
    );

    /**
     * Requests input from a player and handles both response and cancellation
     *
     * @param playerUuid      The UUID of the player to request input from
     * @param inputbarPayload The inputbar payload containing the prompt details
     * @param sendToClient    Function to send the payload data to the client
     * @param onResponse      Callback function called when the player provides input
     * @param onCancel        Callback function called when the player cancels the input
     */
    void requestInput(
            UUID playerUuid,
            AbstractInputbarPayload inputbarPayload,
            BiConsumer<UUID, byte[]> sendToClient,
            Consumer<String> onResponse,
            Runnable onCancel
    );
} 