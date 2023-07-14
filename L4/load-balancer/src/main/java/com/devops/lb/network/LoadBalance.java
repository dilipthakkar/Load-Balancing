package com.devops.lb.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.IOUtils;

/**
 * This class represents a load balancer that distributes incoming requests
 * across multiple servers to achieve optimal resource utilization and
 * improve system performance and reliability.
 * 
 * <p>The load balancer uses a round robin algorithm to distribute the requests evenly among the
 * available servers in the server pool.
 * 
 * <p>Usage:
 * <pre>
 * {@Code 
 * LoadBalancer loadBalancer = new LoadBalancer(portNumber);
 * loadBalancer.connect();
 * }
 * </pre>
 *
 */
public class LoadBalance {

    private static final Integer DEFAULT_PORT = 8080;

    private List<Integer> serverPorts = Arrays.asList(8081, 8082);

    private Integer incomingRequestCount = 0;

    private Integer port = DEFAULT_PORT;

    /**
     * This method established a connection with configured port and handles incoming requests with clients.
     * 
     * <p>
     * This method listens for incoming socket connections on specific port.
     * When a connection is accepted it delegates the handling of the socket to
     * {@link #handleSocket(Socket)} method.
     * 
     * <p>
     * Note: This method run indefinetly until an exception occurs or the program 
     * is terminated externally.
     */
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

    /**
     * This method finds a suitable server port using round robin algorithm.
     * @return The selected server port of handling the next incoming request.
     */
    public Integer getServerPort() {
        Integer selectedIndex = incomingRequestCount % serverPorts.size();
        incomingRequestCount++;
        return serverPorts.get(selectedIndex);
    }

    /**
     * Transfers data from client socket to server socket over a TCP connection.
     * 
     * <p>
     * This method estalishes a TCP socket connection with the selected server, 
     * It transfers the incoming request from the client socket to the
     * selected server and transfers the server's response back to the client.
     * 
     * <p>Note: This method runs on a separate thread to allow concurrent handling of multiple
     * client connections.
     * 
     * @param clientSocket The client socket representing the incoming request connection.
     */
    public void handleSocket(Socket clientSocket) {
        new Thread(() -> {
            Integer serverPort = getServerPort();

            try (Socket lbToServerSocket = new Socket("localhost", serverPort);
                    Socket lbToClientSocket = clientSocket;) {

                InputStream lbToServerInputStream = lbToServerSocket.getInputStream();
                OutputStream lbToServerOutputStream = lbToServerSocket.getOutputStream();

                InputStream lbToClientInputStream = lbToClientSocket.getInputStream();
                OutputStream lbToClientOutputStream = lbToClientSocket.getOutputStream();

                // Thread to transfer request from client to server using load-balancer.
                Thread thread1 = new Thread(() -> {
                    try {
                        IOUtils.copy(lbToClientInputStream, lbToServerOutputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });

                // Thread to transfer response from server to client using load-balancer.
                Thread thread2 = new Thread(() -> {
                    try {
                        IOUtils.copy(lbToServerInputStream, lbToClientOutputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });

                thread1.start();
                thread2.start();

                // Wait for both the threads to complete.
                thread1.join();
                thread2.join();

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public LoadBalance(Integer port){
        this.port = port;
    }

    public static void main(String[] args) {
        LoadBalance lb = new LoadBalance(8080);
        lb.connect();
    }

}
