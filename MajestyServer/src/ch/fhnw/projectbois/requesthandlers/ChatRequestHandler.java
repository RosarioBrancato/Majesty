package ch.fhnw.projectbois.requesthandlers;

import java.util.ArrayList;

import ch.fhnw.projectbois.communication.Request;
import ch.fhnw.projectbois.communication.RequestId;
import ch.fhnw.projectbois.communication.Response;
import ch.fhnw.projectbois.communication.ResponseId;
import ch.fhnw.projectbois.dto.MessageDTO;
import ch.fhnw.projectbois.enumerations.ChatMember;
import ch.fhnw.projectbois.general.IdFactory;
import ch.fhnw.projectbois.json.JsonUtils;
import ch.fhnw.projectbois.network.Lobby;
import ch.fhnw.projectbois.network.Server;
import ch.fhnw.projectbois.network.ServerClient;

/**
 * The Class ChatRequestHandler.
 *
 * @author Leeroy Koller
 */

public class ChatRequestHandler extends RequestHandler {

	/**
	 * Instantiates a new chat request handler.
	 *
	 * @param request the request
	 * @param server the server
	 * @param client the client
	 */
	public ChatRequestHandler(Request request, Server server, ServerClient client) {
		super(request, server, client);
	}

	/* (non-Javadoc)
	 * @see ch.fhnw.projectbois.requesthandlers.RequestHandler#handleRequest()
	 */
	@Override
	protected void handleRequest() {
		if (request.getRequestId() == RequestId.CHAT_SEND_MSG) {
			this.sendMessage();
		}
	}

	/**
	 * Send message.
	 */
	private void sendMessage() {
		Lobby lobby = client.getLobby();

		if (lobby != null) {
			String json = request.getJsonDataObject();
			MessageDTO messageDTO = JsonUtils.Deserialize(json, MessageDTO.class);

			ChatMember author = messageDTO.getAuthor();
			ChatMember receiver = messageDTO.getReceiver();

			// send response to client
			int id = IdFactory.getInstance().getNewId(MessageDTO.class.getName());
			messageDTO.setId(id);

			json = JsonUtils.Serialize(messageDTO);
			Response response = new Response(ResponseId.RECEIVE_MSG, request.getRequestId(), json);

			ArrayList<ServerClient> clients = lobby.getClients();
			
			// normal message
			if (receiver == ChatMember.All) {
				for (ServerClient c : clients) {
					c.sendResponse(response);
				}

			} else {
				// see own post
				client.sendResponse(response);
				// whisper message
				if (receiver == ChatMember.Player1) {
					clients.get(0).sendResponse(response);

				} else if (receiver == ChatMember.Player2) {
					if (clients.size() >= 2) {
						clients.get(1).sendResponse(response);
					}

				} else if (receiver == ChatMember.Player3) {
					if (clients.size() >= 3) {
						clients.get(2).sendResponse(response);
					}

				} else if (receiver == ChatMember.Player4) {
					if (clients.size() >= 4) {
						clients.get(3).sendResponse(response);
					}
				}

				// System notification to all other players
				MessageDTO whisperMessage = new MessageDTO();
				id = IdFactory.getInstance().getNewId(MessageDTO.class.getName());
				whisperMessage.setId(id);
				whisperMessage.setAuthor(ChatMember.System);
				whisperMessage.setTranslationKey("msg_System_Whisper");
				whisperMessage.getFormatVariables().add(getUsernameByChatmember(author));
				whisperMessage.getFormatVariables().add(getUsernameByChatmember(receiver));

				json = JsonUtils.Serialize(whisperMessage);
				Response whisperNotification = new Response(ResponseId.RECEIVE_MSG, request.getRequestId(), json);

				if (messageDTO.getAuthor() != ChatMember.Player1 && messageDTO.getReceiver() != ChatMember.Player1) {
					clients.get(0).sendResponse(whisperNotification);

				}
				if (messageDTO.getAuthor() != ChatMember.Player2 && messageDTO.getReceiver() != ChatMember.Player2) {
					if (clients.size() >= 2) {
						clients.get(1).sendResponse(whisperNotification);
					}
				}
				if (messageDTO.getAuthor() != ChatMember.Player3 && messageDTO.getReceiver() != ChatMember.Player3) {
					if (clients.size() >= 3) {
						clients.get(2).sendResponse(whisperNotification);
					}
				}
				if (messageDTO.getAuthor() != ChatMember.Player4 && messageDTO.getReceiver() != ChatMember.Player4) {
					if (clients.size() >= 4) {
						clients.get(3).sendResponse(whisperNotification);
					}
				}
			}
		}
	}

	/**
	 * Gets the username by chatmember.
	 *
	 * @param member the member
	 * @return the username by chatmember
	 */
	private String getUsernameByChatmember(ChatMember member) {
		String username = "";
		ArrayList<ServerClient> clients = client.getLobby().getClients();

		switch (member) {
		case Player1:
			username = clients.get(0).getUser().getUsername();
			break;
		case Player2:
			if (clients.size() >= 2) {
				username = clients.get(1).getUser().getUsername();
			}
			break;
		case Player3:
			if (clients.size() >= 3) {
				username = clients.get(2).getUser().getUsername();
			}
			break;
		case Player4:
			if (clients.size() >= 4) {
				username = clients.get(3).getUser().getUsername();
			}
			break;
		default:
			username = "Unknown";
			break;
		}

		return username;
	}

}
