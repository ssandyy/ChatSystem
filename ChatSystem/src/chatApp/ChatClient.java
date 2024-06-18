package chatApp;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        Socket clientSocket = null;
        PrintWriter output = null;
        BufferedReader userInput = null;

        System.out.println("Enter your name : ");
        Scanner sc = new Scanner(System.in);
        String clientName = sc.nextLine();

        try {
            // Create Client area with client socket
            clientSocket = new Socket("192.168.1.7", 5000);
            System.out.println("Connected to server : " + clientSocket);

            // Create input and output streams
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // reading data from socket
            output = new PrintWriter(clientSocket.getOutputStream(), true);  // writing data to the socket

            // Create separate thread to read messages from the server
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

            // Start communicating
            userInput = new BufferedReader(new InputStreamReader(System.in));
            String message;
            while ((message = userInput.readLine()) != null) {
                output.println(clientName + ": " + message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                output.close();
            }
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
                if (userInput != null) {
                    userInput.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
