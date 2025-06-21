package edu.seg2105.client.backend;

import ocsf.client.*;
import java.io.*;
import edu.seg2105.client.common.*;

public class ChatClient extends AbstractClient {
  ChatIF clientUI;
  private boolean isConnected = true;
  private String loginId;

  public ChatClient(String host, int port, ChatIF clientUI, String loginId) throws IOException {
    super(host, port);
    this.clientUI = clientUI;
    this.loginId = loginId;
    openConnection();
    sendToServer("#login " + loginId);
  }

  public void handleMessageFromServer(Object msg) {
    clientUI.display(msg.toString());
  }

  public void handleMessageFromClientUI(String message) {
    if (message.startsWith("#")) {
      if (message.equalsIgnoreCase("#quit")) {
        quit();
      } else if (message.equalsIgnoreCase("#logoff")) {
        try {
          closeConnection();
          isConnected = false;
        } catch (IOException e) {
          clientUI.display("Error while logging off.");
        }
      } else if (message.startsWith("#sethost")) {
        String[] parts = message.split(" ");
        if (parts.length == 2) {
          if (isConnected) {
            clientUI.display("Cannot set host while connected.");
          } else {
            setHost(parts[1]);
            clientUI.display("Host set to: " + parts[1]);
          }
        } else {
          clientUI.display("Usage: #sethost <host>");
        }

      }
      else if (message.startsWith("#setport")) {
        String[] parts = message.split(" ");
        if (parts.length == 2) {
          try {
            int newPort = Integer.parseInt(parts[1]);
            if (isConnected) {
              clientUI.display("Cannot set port while connected.");
            } else {
              setPort(newPort);
              clientUI.display("Port set to: " + newPort);
            }
          } catch (NumberFormatException e) {
            clientUI.display("Invalid port number.");
          }
        } else {
          clientUI.display("Usage: #setport <port>");
        }
      }

      else if (message.equalsIgnoreCase("#login")) {
        if (!isConnected) {
          try {
            openConnection();
            isConnected = true;
          } catch (IOException e) {
            clientUI.display("Login failed.");
          }
        } else {
          clientUI.display("Already connected.");
        }
      } else if (message.equalsIgnoreCase("#gethost")) {
        clientUI.display("Current host: " + getHost());
      } else if (message.equalsIgnoreCase("#getport")) {
        clientUI.display("Current port: " + getPort());
      } else {
        clientUI.display("Unknown command.");
      }
    } else {
      try {
        sendToServer(message);
      } catch (IOException e) {
        clientUI.display("Could not send message to server. Terminating client.");
        quit();
      }
    }
  }

  public void quit() {
    try {
      closeConnection();
    } catch (IOException e) {}
    System.exit(0);
  }
}
