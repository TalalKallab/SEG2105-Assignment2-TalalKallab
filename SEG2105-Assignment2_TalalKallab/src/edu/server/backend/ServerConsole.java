package edu.seg2105.edu.server.backend;

import java.io.*;
import java.util.Scanner;

public class ServerConsole {
    final public static int DEFAULT_PORT = 5555;

    private EchoServer server;
    private Scanner fromConsole;

    public ServerConsole(int port) {
        server = new EchoServer(port, this);
        fromConsole = new Scanner(System.in);

        try {
            server.listen();
        } catch (IOException e) {
            System.out.println("Could not start server.");
        }
    }

    public void accept() {
        try {
            String input;

            while (true) {
                input = fromConsole.nextLine();

                if (input.startsWith("#")) {
                    if (input.equalsIgnoreCase("#quit")) {
                        System.out.println("Server shut down");
                        server.close();
                        System.exit(0);
                    } else if (input.equalsIgnoreCase("#logoff")) {
                        server.stopListening();
                        System.out.println("Server stopped accepting clients.");
                    } else if (input.equalsIgnoreCase("#close")) {
                        try {
                            server.close();
                        } catch (IOException e) {
                            System.out.println("Error while closing server.");
                        }
                    } else if (input.startsWith("#setport")) {
                        String[] parts = input.split(" ");
                        int port = Integer.parseInt(parts[1]);
                        server.setPort(port);
                        System.out.println("Port changed.");
                    } else if (input.equalsIgnoreCase("#start")) {
                        try {
                            server.listen();
                        } catch (IOException e) {
                            System.out.println("Error starting server.");
                        }
                    } else if (input.equalsIgnoreCase("#getport")) {
                        System.out.println("Port: " + server.getPort());
                    } else {
                        System.out.println("Unknown command.");
                    }
                } else {
                    server.sendToAllClients("SERVER MSG> " + input);
                    System.out.println("SERVER MSG> " + input);
                }
            }
        } catch (Exception e) {
            System.out.println("Unexpected error.");
        }
    }

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        ServerConsole console = new ServerConsole(port);
        console.accept();
    }
}
