/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    private Socket socket;

    /**
     * Definition of client language region.
     */
    private Locale locale;

    /**
     * The constructor of this class with value for the socket as parameter.
     */
    public ClientHandlerThread(Socket socket) {
        this.socket = socket;
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
                    case Operation.OPERATION_GET_ALL_OBJECT_OF_SALES:
                        response = operationGetAllObjectOfSales(request);
                        break;
                    case Operation.OPERATION_INSERT_LIST:
                        response = operationInsertListOfDomainObject(request);
                        break;
                }
                sendResponse(response);
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
     * @return An object of class {@link ResponseObject}.
     */
    private ResponseObject operationChooseLanguage(RequestObject request) {
        ResponseObject response = null;
        Locale l = (Locale) request.getData();

        try {
            response = new ResponseObject();
            this.locale = l;
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        }

        return response;
    }

    private ResponseObject operationLoginWorker(RequestObject request) {
        ResponseObject response = null;
        Map<String, String> data = (Map) request.getData();
        String username = data.get("username");
        String password = data.get("password");

        try {
            response = new ResponseObject();
            Radnik radnik = (Radnik) Controller.getInstance().loginWorker(username, password);
            response.setData(radnik);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        }

        return response;
    }

    private ResponseObject operationGenerate(RequestObject request) {
        ResponseObject response = null;
        DomainObject odo = (DomainObject) request.getData();

        try {
            response = new ResponseObject();
            odo = (DomainObject) Controller.getInstance().createNewDomainObject(odo);
            response.setData(odo);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        }

        return response;
    }

    private ResponseObject operationInsert(RequestObject request) {
        ResponseObject response = null;
        DomainObject odo = (DomainObject) request.getData();

        try {
            response = new ResponseObject();
            odo = (DomainObject) Controller.getInstance().insertDomainObject(odo);
            response.setData(odo);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        }

        return response;
    }

    private ResponseObject operationSearchCustomers(RequestObject request) {
        ResponseObject response = null;
        String criteria = (String) request.getData();

        try {
            response = new ResponseObject();
            List<DomainObject> customers = Controller.getInstance().searchCustomers(criteria);
            response.setData(customers);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        }

        return response;
    }

    private ResponseObject operationUpdate(RequestObject request) {
        ResponseObject response = null;
        DomainObject odo = (DomainObject) request.getData();

        try {
            response = new ResponseObject();
            odo = Controller.getInstance().updateDomainObject(odo);
            response.setData(odo);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        }

        return response;
    }

    private ResponseObject operationSearchEmployees(RequestObject request) {
        ResponseObject response = null;
        String criteria = (String) request.getData();

        try {
            response = new ResponseObject();
            List<DomainObject> employees = Controller.getInstance().searchEmployees(criteria);
            response.setData(employees);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        }

        return response;
    }

    private ResponseObject operationDelete(RequestObject request) {
        ResponseObject response = null;
        DomainObject odo = (DomainObject) request.getData();

        try {
            response = new ResponseObject();
            odo = Controller.getInstance().deleteDomainObject(odo);
            response.setData(odo);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        }

        return response;
    }

    private ResponseObject operationGetAllTax(RequestObject request) {
        ResponseObject response = null;

        try {
            response = new ResponseObject();
            List<DomainObject> tax = Controller.getInstance().getAllTax();
            response.setData(tax);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        }

        return response;
    }

    private ResponseObject operationSearchCarPart(RequestObject request) {
        ResponseObject response = null;
        String criteria = (String) request.getData();

        try {
            response = new ResponseObject();
            List<DomainObject> parts = Controller.getInstance().searchCarParts(criteria);
            response.setData(parts);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        }

        return response;
    }

    private ResponseObject operationSearchService(RequestObject request) {
        ResponseObject response = null;
        String criteria = (String) request.getData();

        try {
            response = new ResponseObject();
            List<DomainObject> services = Controller.getInstance().searchService(criteria);
            response.setData(services);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        }

        return response;
    }

    private ResponseObject operationSearchObjectOfSale(RequestObject request) {
        ResponseObject response = null;
        Long criteria = (Long) request.getData();

        try {
            response = new ResponseObject();
            DomainObject objectOfSale = Controller.getInstance().searchObjectOfSale(criteria);
            response.setData(objectOfSale);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        }

        return response;
    }

    private ResponseObject operationGetAllObjectOfSales(RequestObject request) {
        ResponseObject response = null;
        String criteria = (String) request.getData();

        try {
            response = new ResponseObject();
            Map<Object, Object> objectsOfSale = Controller.getInstance().getAllObjectOfSale(criteria);
            response.setData(objectsOfSale);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        }

        return response;
    }

    private ResponseObject operationInsertListOfDomainObject(RequestObject request) {
        ResponseObject response = null;
        List<DomainObject> listOdo = (List<DomainObject>) request.getData();

        try {
            response = new ResponseObject();
            Controller.getInstance().insertListOfDomainObject(listOdo);
            response.setData(listOdo);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        }

        return response;
    }
}
