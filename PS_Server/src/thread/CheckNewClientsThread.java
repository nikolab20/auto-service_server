/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thread;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.tablemodels.TableModelSocket;

/**
 *
 * @author nikol
 */
public class CheckNewClientsThread extends Thread {

    private final ServerThread serverThread;
    private final TableModelSocket tms;

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
