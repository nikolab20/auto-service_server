/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thread;

import controller.Controller;
import domain.DomainObject;
import domain.Klijent;
import domain.Radnik;
import lombok.AllArgsConstructor;
import lombok.Getter;
import transfer.RequestObject;
import transfer.ResponseObject;
import util.Operation;

import java.awt.*;
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
                    case Operation.OPERATION_GENERATE_CUSTOMER:
                        response = operationGenerate(request);
                        break;
                    case Operation.OPERATION_ADD_CUSTOMER:
                        response = operationAddCustomer(request);
                        break;
                    case Operation.OPERATION_SEARCH_CUSTOMER:
                        response = operationSearchCustomers(request);
                        break;
                    case Operation.OPERATION_UPDATE_CUSTOMER:
                        response = operationUpdateCustomers(request);
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
     * @throws IOException            if problems with stream arise.
     * @throws ClassNotFoundException if received object can't cast to request object.
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
     * @param request is an object with number of operation and data that sent from client.
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
            odo = (DomainObject) Controller.getInstance().createNewCustomer(odo);
            response.setData(odo);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        }

        return response;
    }

    private ResponseObject operationAddCustomer(RequestObject request) {
        ResponseObject response = null;
        Klijent klijent = (Klijent) request.getData();

        try {
            response = new ResponseObject();
            klijent = (Klijent) Controller.getInstance().addCustomer(klijent);
            response.setData(klijent);
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


    private ResponseObject operationUpdateCustomers(RequestObject request) {
        ResponseObject response = null;
        Klijent klijent = (Klijent) request.getData();

        try {
            response = new ResponseObject();
            Controller.getInstance().updateCustomer(klijent);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        }

        return response;
    }
}
