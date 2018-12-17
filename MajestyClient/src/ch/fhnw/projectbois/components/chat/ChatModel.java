package ch.fhnw.projectbois.components.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import ch.fhnw.projectbois._mvc.Model;
import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.LobbyDTO;
import ch.fhnw.projectbois.dto.MessageDTO;
import ch.fhnw.projectbois.dto.ReportDTO;
import ch.fhnw.projectbois.enumerations.ChatMember;
import ch.fhnw.projectbois.gameobjects.GameState;
import ch.fhnw.projectbois.gameobjects.Player;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Network;
import ch.fhnw.projectbois.session.Session;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * The Class ChatModel.
 *
 * @author Leeroy Koller
 */

public class ChatModel extends Model {

	private SimpleObjectProperty<MessageDTO> chatProperty = null;
	
	private HashMap<String, ChatMember> usernameMap = null;

	/**
	 * Instantiates a new chat model.
	 */
	public ChatModel() {
		this.chatProperty = new SimpleObjectProperty<>();
		this.usernameMap = new HashMap<>();
		this.initResponseListener();
	}

	/**
	 * Send message.
	 * Create and sends request. 
	 * UserToken: identify user, RequestId: knows which logic to use, json: has all the info.
	 *
	 * @param message the message
	 */
	public void sendMessage(MessageDTO message) {
		String json = JsonUtils.Serialize(message);

		Request request = new Request(Session.getCurrentUserToken(), RequestId.CHAT_SEND_MSG, json);

		Network.getInstance().sendRequest(request);
	}

	/**
	 * Gets the lobby info.
	 *
	 * @return the lobby info
	 */
	public void getLobbyInfo() {
		Request request = new Request(Session.getCurrentUserToken(), RequestId.GET_LOBBY_OF_CLIENT, null);
		Network.getInstance().sendRequest(request);
	}

	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois._mvc.Model#getChangeListener()
	 * 
	 * Receives response from server.
	 */
	@Override
	protected ChangeListener<Response> getChangeListener() {
		return new ChangeListener<Response>() {
			@Override
			public void changed(ObservableValue<? extends Response> observable, Response oldValue, Response newValue) {

				if (newValue.getResponseId() == ResponseId.RECEIVE_MSG) {
					String json = newValue.getJsonDataObject();
					MessageDTO message = JsonUtils.Deserialize(json, MessageDTO.class);

					chatProperty.setValue(message);

				} else if (newValue.getResponseId() == ResponseId.LOBBY_INFO
						|| newValue.getResponseId() == ResponseId.LOBBY_JOINED_MULTICAST
						|| newValue.getResponseId() == ResponseId.LOBBY_LEFT_MULTICAST) {

					String json = newValue.getJsonDataObject();
					LobbyDTO lobbyInfo = JsonUtils.Deserialize(json, LobbyDTO.class);

					mapUsernames(lobbyInfo);

				} else if (newValue.getRequestId() == ResponseId.GAME_PLAYER_LEFT) {
					String json = newValue.getJsonDataObject();
					GameState gameState = JsonUtils.Deserialize(json, GameState.class);

					for (Player p : gameState.getBoard().getPlayers()) {
						if (p.isPlayerLeft()) {
							usernameMap.remove(p.getUsername());
						}
					}

				} else if (newValue.getResponseId() == ResponseId.CHAT_ERROR) {
					String json = newValue.getJsonDataObject();
					ReportDTO report = JsonUtils.Deserialize(json, ReportDTO.class);

					getReportProperty().setValue(report);
				}

			}
		};
	}

	/**
	 * Gets the chat property.
	 *
	 * @return the chat property
	 */
	public SimpleObjectProperty<MessageDTO> getChatProperty() {
		return this.chatProperty;
	}

	/**
	 * Gets the username map.
	 *
	 * @return the username map
	 */
	public HashMap<String, ChatMember> getUsernameMap() {
		return this.usernameMap;
	}

	/**
	 * Gets the current user chat member.
	 *
	 * @return the current user chat member
	 */
	public ChatMember getCurrentUserChatMember() {
		String username = Session.getCurrentUsername();
		ChatMember ownChatMember = usernameMap.get(username);

		return ownChatMember;
	}

	/**
	 * Gets the username by chatmember.
	 *
	 * @param chatMember the chat member
	 * @return the username by chatmember
	 */
	public String getUsernameByChatmember(ChatMember chatMember) {
		String username = "System";
		username = translator.getTranslation("txt_ChatMember_System");
		
		Set<String> usernames = usernameMap.keySet();

		for (String name : usernames) {
			ChatMember member = usernameMap.get(name);

			if (member == chatMember) {
				username = name;
				break;
			}
		}

		return username;
	}

	/**
	 * Map usernames.
	 *
	 * @param lobbyInfo the lobby info
	 */
	private void mapUsernames(LobbyDTO lobbyInfo) {
		usernameMap.clear();
		ArrayList<String> players = lobbyInfo.getPlayers();

		usernameMap.put(players.get(0), ChatMember.Player1);
		if (players.size() >= 2) {
			usernameMap.put(players.get(1), ChatMember.Player2);
		}
		if (players.size() >= 3) {
			usernameMap.put(players.get(2), ChatMember.Player3);
		}
		if (players.size() >= 4) {
			usernameMap.put(players.get(3), ChatMember.Player4);
		}
	}

}
