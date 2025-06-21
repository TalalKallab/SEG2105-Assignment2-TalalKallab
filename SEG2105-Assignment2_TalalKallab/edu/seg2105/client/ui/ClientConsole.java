package edu.seg2105.client.ui;

import java.io.*;
import java.util.Scanner;
import edu.seg2105.client.backend.ChatClient;
import edu.seg2105.client.common.*;

public class ClientConsole implements ChatIF {
  final public static int DEFAULT_PORT = 5555;

  ChatClient client;
  Scanner fromConsole;

  public ClientConsole(String loginId, String host, int port) {
    try {
      // Pass loginId to ChatClient once you update ChatClient to support it
      client = new ChatClient(host, port, this , loginId );
    } catch (IOException exception) {
      System.out.println("Error: Can't setup connection! Terminating client.");
      System.exit(1);
    }
    fromConsole = new Scanner(System.in);
  }

  public void accept() {
    try {
      String message;
      while (true) {
        message = fromConsole.nextLine();
        client.handleMessageFromClientUI(message);
      }
    } catch (Exception ex) {
      System.out.println("Unexpected error while reading from console!");
    }
  }

  public void display(String message) {
    System.out.println("> " + message);
  }

  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("ERROR: Login ID must be provided.");
      System.exit(1);
    }

    String loginId = args[0];
    String host = (args.length >= 2) ? args[1] : "localhost";
    int port = (args.length >= 3) ? Integer.parseInt(args[2]) : DEFAULT_PORT;

    ClientConsole chat = new ClientConsole(loginId, host, port);
    chat.accept();
  }
}
