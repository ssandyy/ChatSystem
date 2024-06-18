package chatApp;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatServer {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        PrintWriter output = null;
        BufferedReader userInput = null;

        System.out.println("Enter your name : ");
        Scanner sc = new Scanner(System.in);
        String serverName = sc.nextLine();

        try {
            serverSocket = new ServerSocket(5000); // Create Server Socket
            System.out.println("Server Started Successfully, Waiting for client..!");

            // Accept client connection over the server port
            clientSocket = serverSocket.accept();
            System.out.println("Client Connected Successfully : " + clientSocket);

            // Create input output streams
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);

            // Create separate thread to read and write messages from the client
            Thread readThread = new Thread(() -> {
                String message;
                try {
                    while ((message = input.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        input.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            readThread.start();

            // Start the communication with client
            userInput = new BufferedReader(new InputStreamReader(System.in));
            String message;
            while ((message = userInput.readLine()) != null) {
                output.println(serverName + ": " + message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                output.close();
            }
            try {
                if (userInput != null) {
                    userInput.close();
                }
                if (clientSocket != null) {
                    clientSocket.close();
                }
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
