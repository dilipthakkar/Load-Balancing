package com.devops.lb.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.IOUtils;

public class LoadBalance {

    private List<Integer> serverPorts = Arrays.asList(8081, 8082);

    private Integer incomingRequestCount = 0;

    private Integer port = 8080;

    public void connect() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleSocket(clientSocket);
            }
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    public Integer getServerPort() {
        Integer selectedIndex = incomingRequestCount % serverPorts.size();
        incomingRequestCount++;
        return serverPorts.get(selectedIndex);
    }

    public void handleSocket(Socket clientSocket) {
        new Thread(() -> {
            Integer serverPort = getServerPort();

            try (Socket lbToServerSocket = new Socket("localhost", serverPort);
                    Socket lbToClientSocket = clientSocket;) {

                InputStream lbToServerInputStream = lbToServerSocket.getInputStream();
                OutputStream lbToServerOutputStream = lbToServerSocket.getOutputStream();

                InputStream lbToClientInputStream = lbToClientSocket.getInputStream();
                OutputStream lbToClientOutputStream = lbToClientSocket.getOutputStream();

                Thread thread1 = new Thread(() -> {
                    try {
                        IOUtils.copy(lbToClientInputStream, lbToServerOutputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });

                Thread thread2 = new Thread(() -> {
                    try {
                        IOUtils.copy(lbToServerInputStream, lbToClientOutputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });

                thread1.start();
                thread2.start();

                thread1.join();
                thread2.join();

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        LoadBalance loadBalance = new LoadBalance();
        loadBalance.connect();
    }

}
