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

    /**
     * List of data which represented in table.
     */
    private List<ClientHandlerThread> clients;

    /**
     * Array of strings that represents names of columns.
     */
    private final String[] columnNames;

    /**
     * Reference of resource bundle as dictionary.
     */
    private final ResourceBundle resourceBundle;

    /**
     * Parameterized constructor of that table model.
     *
     * @param clients is list of data which represented in table.
     */
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

    /**
     * Method for updating data into a table.
     *
     * @param clients is updated list of data for table.
     */
    public void updateTable(List<ClientHandlerThread> clients) {
        this.clients = clients;
        fireTableDataChanged();
    }
}
