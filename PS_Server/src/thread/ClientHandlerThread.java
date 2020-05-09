package thread;

import controller.Controller;
import domain.DomainObject;
import domain.Radnik;
import lombok.AllArgsConstructor;
import lombok.Getter;
import transfer.RequestObject;
import transfer.ResponseObject;
import util.Operation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author nikol
 */
/**
 * Represent an operator for a single client who has connected to the server.
 */
@AllArgsConstructor
public class ClientHandlerThread extends Thread {

    /**
     * The socket by which the client has connected to the server.
     *
     * @return an instance of the Socket class.
     */
    @Getter
    private final Socket socket;

    /**
     * Definition of client language region.
     */
    private Locale locale;

    /**
     * The constructor of this class with value for the socket as parameter.
     *
     * @param socket
     */
    public ClientHandlerThread(Socket socket) {
        this.socket = socket;
        this.setName("/");
    }

    @Override
    public void run() {
        RequestObject request = null;
        ResponseObject response = null;
        while (!socket.isClosed()) {
            try {
                request = receiveRequest();

                switch (request.getOperation()) {
                    case Operation.OPERATION_CHOOSE_LANGUAGE:
                        response = operationChooseLanguage(request);
                        break;
                    case Operation.OPERATION_LOGIN_WORKER:
                        response = operationLoginWorker(request);
                        break;
                    case Operation.OPERATION_GENERATE:
                        response = operationGenerate(request);
                        break;
                    case Operation.OPERATION_INSERT:
                        response = operationInsert(request);
                        break;
                    case Operation.OPERATION_SEARCH_CUSTOMER:
                        response = operationSearchCustomers(request);
                        break;
                    case Operation.OPERATION_UPDATE:
                        response = operationUpdate(request);
                        break;
                    case Operation.OPERATION_SEARCH_EMPLOYEE:
                        response = operationSearchEmployees(request);
                        break;
                    case Operation.OPERATION_DELETE:
                        response = operationDelete(request);
                        break;
                    case Operation.OPERATION_SELECT_ALL_TAX:
                        response = operationGetAllTax(request);
                        break;
                    case Operation.OPERATION_SEARCH_CAR_PART:
                        response = operationSearchCarPart(request);
                        break;
                    case Operation.OPERATION_SEARCH_SERVICE:
                        response = operationSearchService(request);
                        break;
                    case Operation.OPERATION_SEARCH_OBJECT_OF_SALE:
                        response = operationSearchObjectOfSale(request);
                        break;
                    case Operation.OPERATION_INSERT_LIST:
                        response = operationInsertListOfDomainObject(request);
                        break;
                    case Operation.OPERATION_SEARCH_BILL:
                        response = operationSearchBill(request);
                        break;
                    case Operation.OPERATION_SEARCH_BILL_FROM_DATE:
                        response = operationSearchBillFromDate(request);
                        break;
                    case Operation.OPERATION_SEARCH_NEW_CLIENTS_FROM_DATE:
                        response = operationSearchNewClixentsFromDate(request);
                        break;
                    case Operation.OPERATION_SEARCH_CLIENTS_WITH_DEBT:
                        response = operationSearchClientsWithDebt(request);
                        break;
                    case Operation.OPERATION_SEARCH_TAX:
                        response = operationSearchTax(request);
                        break;
                    case Operation.OPERATION_DISCONNECT:
                        operationDisconnect(request);
                        break;
                    case Operation.OPERATION_SEARCH_ALL_OBJECT_OF_SALES:
                        response = operationSearchObjectOfSales(request);
                        break;
                    case Operation.OPERATION_SELECT_ALL_BILLS:
                        response = operationGetAllBills(request);
                        break;
                    case Operation.OPERATION_SELECT_ALL_EMPLOYEES:
                        response = operationGetAllEmployees(request);
                        break;
                    case Operation.OPERATION_SELECT_ALL_CAR_PARTS:
                        response = operationGetAllCarParts(request);
                        break;
                    case Operation.OPERATION_SELECT_ALL_SERVICES:
                        response = operationGetAllServices(request);
                        break;
                    case Operation.OPERATION_SELECT_ALL_CUSTOMERS:
                        response = operationGetAllCustomers(request);
                        break;
                    case Operation.OPERATION_SELECT_ALL_OBJECT_OF_SALE:
                        response = operationGetAllObjectOfSale(request);
                        break;
                    case Operation.OPERATION_SEARCH_BILL_BY_CUSTOMER:
                        response = operationSearchBillByCustomer(request);
                        break;
                }
                if (request.getOperation() != Operation.OPERATION_DISCONNECT) {
                    sendResponse(response);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Method for receiving a request from client.
     *
     * @return data as object from received request.
     * @throws IOException if problems with stream arise.
     * @throws ClassNotFoundException if received object can't cast to request
     * object.
     */
    public RequestObject receiveRequest() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        return (RequestObject) in.readObject();
    }

    /**
     * Меthod for sending response to client application.
     *
     * @param response is an object to be sent to the client application
     * @throws IOException if problems with stream arise.
     */
    public void sendResponse(ResponseObject response) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(response);
        out.flush();
    }

    /**
     * Method for processing client's request for choosing language.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationChooseLanguage(RequestObject request) {
        ResponseObject response;
        Locale l = (Locale) request.getData();

        response = new ResponseObject();
        this.locale = l;

        return response;
    }

    /**
     * Method for processing client's request for logging user.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationLoginWorker(RequestObject request) {
        ResponseObject response = null;
        Map<String, String> data = (Map) request.getData();
        String username = data.get("username");
        String password = data.get("password");

        try {
            response = new ResponseObject();
            Radnik radnik = (Radnik) Controller.getInstance().loginWorker(username, password);
            this.setName(radnik.getImeRadnika() + " " + radnik.getPrezimeRadnika());
            response.setData(radnik);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for initialization domain object.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationGenerate(RequestObject request) {
        ResponseObject response = null;
        DomainObject odo = (DomainObject) request.getData();

        try {
            response = new ResponseObject();
            odo = (DomainObject) Controller.getInstance().createNewDomainObject(odo);
            response.setData(odo);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for inserting domain object.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationInsert(RequestObject request) {
        ResponseObject response = null;
        DomainObject odo = (DomainObject) request.getData();

        try {
            response = new ResponseObject();
            odo = (DomainObject) Controller.getInstance().insertDomainObject(odo);
            response.setData(odo);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for customer search.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationSearchCustomers(RequestObject request) {
        ResponseObject response = null;
        Long customerID = (Long) request.getData();

        try {
            response = new ResponseObject();
            List<DomainObject> customers = Controller.getInstance().searchCustomers(customerID);
            response.setData(customers);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for update domain object.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationUpdate(RequestObject request) {
        ResponseObject response = null;
        DomainObject odo = (DomainObject) request.getData();

        try {
            response = new ResponseObject();
            odo = Controller.getInstance().updateDomainObject(odo);
            response.setData(odo);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for employees search.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationSearchEmployees(RequestObject request) {
        ResponseObject response = null;
        Long employeeID = (Long) request.getData();

        try {
            response = new ResponseObject();
            List<DomainObject> employees = Controller.getInstance().searchEmployees(employeeID);
            response.setData(employees);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for delete domain object.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationDelete(RequestObject request) {
        ResponseObject response = null;
        DomainObject odo = (DomainObject) request.getData();

        try {
            response = new ResponseObject();
            odo = Controller.getInstance().deleteDomainObject(odo);
            response.setData(odo);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for getting all of tax rates.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationGetAllTax(RequestObject request) {
        ResponseObject response = null;

        try {
            response = new ResponseObject();
            List<DomainObject> tax = Controller.getInstance().getAllTax();
            response.setData(tax);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for car parts search.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationSearchCarPart(RequestObject request) {
        ResponseObject response = null;
        Long criteria = (Long) request.getData();

        try {
            response = new ResponseObject();
            List<DomainObject> parts = Controller.getInstance().searchCarParts(criteria);
            response.setData(parts);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for services search.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationSearchService(RequestObject request) {
        ResponseObject response = null;
        Long criteria = (Long) request.getData();

        try {
            response = new ResponseObject();
            List<DomainObject> services = Controller.getInstance().searchService(criteria);
            response.setData(services);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for object of sale search.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationSearchObjectOfSale(RequestObject request) {
        ResponseObject response = null;
        Long criteria = (Long) request.getData();

        try {
            response = new ResponseObject();
            DomainObject objectOfSale = Controller.getInstance().searchObjectOfSale(criteria);
            response.setData(objectOfSale);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for objects of sale search.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationSearchObjectOfSales(RequestObject request) {
        ResponseObject response = null;
        Long criteria = (Long) request.getData();

        try {
            response = new ResponseObject();
            Map<Object, Object> objectsOfSale = Controller.getInstance().searchObjectOfSaleWithNames(criteria);
            response.setData(objectsOfSale);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for insert list of domain object.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationInsertListOfDomainObject(RequestObject request) {
        ResponseObject response = null;
        List<DomainObject> listOdo = (List<DomainObject>) request.getData();

        try {
            response = new ResponseObject();
            Controller.getInstance().insertListOfDomainObject(listOdo);
            response.setData(listOdo);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for bills search.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationSearchBill(RequestObject request) {
        ResponseObject response = null;
        Long criteria = (Long) request.getData();

        try {
            response = new ResponseObject();
            List<DomainObject> bills = Controller.getInstance().searchBill(criteria);
            response.setData(bills);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for bills search from specific
     * date.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationSearchBillFromDate(RequestObject request) {
        ResponseObject response = null;
        Date date = (Date) request.getData();

        try {
            response = new ResponseObject();
            List<DomainObject> bills = Controller.getInstance().searchBillFromDate(date);
            response.setData(bills);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for new clients search from
     * specific date.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationSearchNewClixentsFromDate(RequestObject request) {
        ResponseObject response = null;
        Date date = (Date) request.getData();

        try {
            response = new ResponseObject();
            List<DomainObject> clients = Controller.getInstance().searchNewClientsFromDate(date);
            response.setData(clients);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for clients with debt search.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationSearchClientsWithDebt(RequestObject request) {
        ResponseObject response = null;

        try {
            response = new ResponseObject();
            List<DomainObject> clients = Controller.getInstance().searchClientsWithDebt();
            response.setData(clients);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for tax rates search.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationSearchTax(RequestObject request) {
        ResponseObject response = null;
        Long id = (Long) request.getData();

        try {
            response = new ResponseObject();
            List<DomainObject> tax = Controller.getInstance().searchTax(id);
            response.setData(tax);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for disconnection.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     */
    private void operationDisconnect(RequestObject request) {
        try {
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method for processing client's request for getting all of customers.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationGetAllCustomers(RequestObject request) {
        ResponseObject response = null;

        try {
            response = new ResponseObject();
            List<DomainObject> customers = Controller.getInstance().getAllCustomers();
            response.setData(customers);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for getting all of bills.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationGetAllBills(RequestObject request) {
        ResponseObject response = null;

        try {
            response = new ResponseObject();
            List<DomainObject> bills = Controller.getInstance().getAllBills();
            response.setData(bills);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for getting all of employees.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationGetAllEmployees(RequestObject request) {
        ResponseObject response = null;

        try {
            response = new ResponseObject();
            List<DomainObject> employees = Controller.getInstance().getAllEmployees();
            response.setData(employees);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for getting all of car parts.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationGetAllCarParts(RequestObject request) {
        ResponseObject response = null;

        try {
            response = new ResponseObject();
            List<DomainObject> carParts = Controller.getInstance().getAllCarParts();
            response.setData(carParts);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for getting all of services.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationGetAllServices(RequestObject request) {
        ResponseObject response = null;

        try {
            response = new ResponseObject();
            List<DomainObject> services = Controller.getInstance().getAllServices();
            response.setData(services);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for getting all of objects of
     * sales.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationGetAllObjectOfSale(RequestObject request) {
        ResponseObject response = null;

        try {
            response = new ResponseObject();
            Map<Object, Object> objects = Controller.getInstance().getAllObjectOfSale();
            response.setData(objects);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }

    /**
     * Method for processing client's request for bills search.
     *
     * @param request is an object with number of operation and data that sent
     * from client.
     * @return an object of class ResponseObject.
     */
    private ResponseObject operationSearchBillByCustomer(RequestObject request) {
        ResponseObject response = null;
        Long criteria = (Long) request.getData();

        try {
            response = new ResponseObject();
            List<DomainObject> bills = Controller.getInstance().searchBillByCustomer(criteria);
            response.setData(bills);
        } catch (Exception ex) {
            response.setException(ex);
        }

        return response;
    }
}
