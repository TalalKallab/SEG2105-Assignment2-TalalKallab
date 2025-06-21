package edu.seg2105.edu.server.backend;

import ocsf.server.*;

public class EchoServer extends AbstractServer {
  final public static int DEFAULT_PORT = 5555;

  private ServerConsole serverUI;

  public EchoServer(int port, ServerConsole serverUI) {
    super(port);
    this.serverUI = serverUI;
  }

  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
    System.out.println("Message received: " + msg + " from " + client);
    sendToAllClients(msg);
  }

  protected void serverStarted() {
    System.out.println("Server is now listening on port " + getPort());
  }

  protected void serverStopped() {
    System.out.println("Server has stopped accepting new connections.");
  }

  protected void clientConnected(ConnectionToClient client) {
    System.out.println("A new client has connected.");
  }

  synchronized protected void clientDisconnected(ConnectionToClient client) {
    System.out.println("A client has disconnected.");
  }
}
