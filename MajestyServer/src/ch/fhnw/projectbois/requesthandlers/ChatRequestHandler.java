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
 * 
 * @author Leeroy Koller
 *
 */

public class ChatRequestHandler extends RequestHandler {

	public ChatRequestHandler(Request request, Server server, ServerClient client) {
		super(request, server, client);
	}

	@Override
	protected void handleRequest() {
		if (request.getRequestId() == RequestId.CHAT_SEND_MSG) {
			this.sendMessage();

		} else {

		}
	}

	private void sendMessage() {
		String json = request.getJsonDataObject();
		MessageDTO messageDTO = JsonUtils.Deserialize(json, MessageDTO.class);

		ChatMember author = messageDTO.getAuthor();
		ChatMember receiver = messageDTO.getReceiver();

		// send response to client
		int id = IdFactory.getInstance().getNewId(MessageDTO.class.getName());
		messageDTO.setId(id);

		json = JsonUtils.Serialize(messageDTO);
		Response response = new Response(ResponseId.RECEIVE_MSG, request.getRequestId(), json);

		Lobby lobby = client.getLobby();
		ArrayList<ServerClient> clients = lobby.getClients();

		if (receiver == ChatMember.All) {
			for (ServerClient c : clients) {
				c.sendResponse(response);
			}

		} else {
			// see own post
			client.sendResponse(response);
			if (receiver == ChatMember.Player1) {
				clients.get(0).sendResponse(response);

			} else if (receiver == ChatMember.Player2) {
				if (clients.size() <= 2) {
					clients.get(1).sendResponse(response);
				}

			} else if (receiver == ChatMember.Player3) {
				if (clients.size() <= 3) {
					clients.get(2).sendResponse(response);
				}

			} else if (receiver == ChatMember.Player4) {
				if (clients.size() <= 4) {
					clients.get(3).sendResponse(response);
				}
			}

			// System notification
			MessageDTO whisperMessage = new MessageDTO();
			id = IdFactory.getInstance().getNewId(MessageDTO.class.getName());
			whisperMessage.setId(id);
			whisperMessage.setAuthor(ChatMember.System);

			String notificationMessage = "Psst, " + getUsernameByChatmember(author) + " hat zu "
					+ getUsernameByChatmember(receiver) + " geflüstert...";
			whisperMessage.setMessage(notificationMessage);

			json = JsonUtils.Serialize(whisperMessage);
			Response whisperNotification = new Response(ResponseId.RECEIVE_MSG, request.getRequestId(), json);

			if (messageDTO.getAuthor() != ChatMember.Player1 && messageDTO.getReceiver() != ChatMember.Player1) {
				clients.get(0).sendResponse(whisperNotification);

			} else if (messageDTO.getAuthor() != ChatMember.Player2 && messageDTO.getReceiver() != ChatMember.Player2) {
				if (clients.size() >= 2) {
					clients.get(1).sendResponse(whisperNotification);
				}

			} else if (messageDTO.getAuthor() != ChatMember.Player3 && messageDTO.getReceiver() != ChatMember.Player3) {
				if (clients.size() >= 3) {
					clients.get(2).sendResponse(whisperNotification);
				}

			} else if (messageDTO.getAuthor() != ChatMember.Player4 && messageDTO.getReceiver() != ChatMember.Player4) {
				if (clients.size() >= 4) {
					clients.get(3).sendResponse(whisperNotification);
				}
			}
		}
	}

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
			username = "-- System";
			break;
		}

		return username;
	}

}
