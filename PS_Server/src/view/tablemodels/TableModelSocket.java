/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.tablemodels;

import controller.Controller;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;
import thread.ClientHandlerThread;

/**
 *
 * @author nikol
 */
public class TableModelSocket extends AbstractTableModel {

    private List<ClientHandlerThread> clients;
    private final String[] columnNames;
    private final ResourceBundle resourceBundle;

    public TableModelSocket(List<ClientHandlerThread> clients) {
        this.clients = clients;
        resourceBundle = ResourceBundle.getBundle("props/LanguageBundle", Controller.getInstance().getLocale());
        this.columnNames = new String[]{
            resourceBundle.getString("column_ip_address"),
            resourceBundle.getString("column_port"),
            resourceBundle.getString("column_user")};
    }

    @Override
    public int getRowCount() {
        return clients.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ClientHandlerThread client = clients.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return client.getSocket().getLocalAddress();
            case 1:
                return client.getSocket().getPort();
            case 2:
                return client.getName();
            default:
                return "n/a";
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void updateTable(List<ClientHandlerThread> clients) {
        this.clients = clients;
        fireTableDataChanged();
    }
}
