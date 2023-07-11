package com.devops.lb.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class LoadBalance {

    private List<Integer> serverPorts = Arrays.asList(8081, 8082);

    private Integer incomingRequestCount = 0;

    private Integer port = 8080;

    public void connect(){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            while(true){
                Socket clientSocket = serverSocket.accept();

                Integer serverPort = getServerPort();
                
            }
        }catch(IOException exp){
            exp.printStackTrace();
        }
    }

    public Integer getServerPort(){
        Integer selectedIndex = incomingRequestCount%serverPorts.size();
        return serverPorts.get(selectedIndex);
    }

    public void handleSocket(Socket socket){
        new Thread(()->{

        }).start();
    }

    public static void main(String[] args) {
        LoadBalance loadBalance = new LoadBalance();
        loadBalance.connect();
    }

}
