package thread;

import controller.Controller;
import lombok.Getter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import listeners.ClientsListener;

public class ServerThread extends Thread {

    /**
     * The socket on which server waiting for the client's request.
     *
     * @return The current value for server socket.
     */
    @Getter
    private final ServerSocket serverSocket;

    /**
     * The list of the currently active clients.
     */
    @Getter
    private final List<ClientHandlerThread> clients;

    private final List<ClientsListener> clientsListeners;

    /**
     * The constructor of this class without any parameters.
     *
     * @throws IOException if properties file doesn't exist.
     */
    public ServerThread() throws IOException {
        this.clientsListeners = new ArrayList<>();
        Properties props = Controller.getInstance().readPropertiesFile();
        int port = Integer.parseInt(props.getProperty("default_serverPort"));

        if (!Controller.getInstance().isDefaultConfig()) {
            port = Integer.parseInt(props.getProperty("serverPort"));
        }

        serverSocket = new ServerSocket(port);
        clients = new ArrayList<>();
    }

    public void addListener(ClientsListener clientsListener) {
        clientsListeners.add(clientsListener);
    }

    @Override
    public void run() {
        while (!serverSocket.isClosed()) {
            System.out.println("Waiting for clients...");
            try {
                Socket socket = serverSocket.accept();
                ClientHandlerThread client = new ClientHandlerThread(socket);
                client.start();
                clients.add(client);
                for (ClientsListener clientsListener : clientsListeners) {
                    clientsListener.onClientConnected();
                }
                System.out.println("Client connected!");
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        stopClientHandlers();

    }

    /**
     * Server shutdown method.
     *
     * @throws IOException if server socket already closed.
     */
    public void stopServerThread() throws IOException {
        stopClientHandlers();
        serverSocket.close();
    }

    /**
     * Method for closing all clients sockets.
     */
    private void stopClientHandlers() {
        for (ClientHandlerThread client : clients) {
            try {
                client.getSocket().close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
