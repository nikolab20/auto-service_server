package thread;

import java.util.Iterator;
import view.tablemodels.TableModelSocket;

/**
 *
 * @author nikol
 */
public class CheckNewClientsThread extends Thread {

    /**
     * A reference of server thread.
     */
    private final ServerThread serverThread;

    /**
     * A reference of socket table model.
     */
    private final TableModelSocket tms;

    /**
     * Parameterized constructor of this thread.
     *
     * @param serverThread is a reference of server thread.
     * @param tms is a reference of socket table model.
     */
    public CheckNewClientsThread(ServerThread serverThread, TableModelSocket tms) {
        this.serverThread = serverThread;
        this.tms = tms;
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            try {
                for (Iterator<ClientHandlerThread> iterator = serverThread.getClients().iterator(); iterator.hasNext();) {
                    ClientHandlerThread client = iterator.next();
                    if (client.getSocket().isClosed()) {
                        iterator.remove();
                    }
                }
                tms.updateTable(serverThread.getClients());
                sleep(1000);
            } catch (InterruptedException ex) {
                interrupt();
            }
        }
    }

}
