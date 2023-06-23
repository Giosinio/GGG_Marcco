package org.example.client;

import java.io.IOException;
import org.example.client.MyClient;
import org.example.dto.enums.ConstructionType;
import org.example.dto.enums.ObjectType;

public final class SocketClient {

    public static void main(String[] args) throws IOException {
        String address = "localhost";
//        String address = "10.66.173.86";
        int port = 31415;
        String teamName = "Probleme la Mansard4";
        MyClient client = new MyClient(address, port);
        new Thread(client).start();
        client.register(teamName);
    }
}

