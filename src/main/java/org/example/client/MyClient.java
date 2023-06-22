package org.example.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.example.Bunny;
import org.example.dto.MarccoMessage;
import org.example.dto.enums.MessageType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.example.Utils.makeAction;

class MyClient implements Runnable {

    private final ObjectMapper objectMapper = new ObjectMapper();
    public static String botId;
    char[][]board;
    private final Socket connection;
    private boolean connected = true;
    private final BufferedReader buffReader;
    private final OutputStream writer;
    private final Bunny bunny;

    public MyClient(String address, int port) {
        this.connection = initConnection(address, port);
        this.buffReader = initReader();
        this.writer = initWriter();
        this.bunny = new Bunny();
    }

    @Override
    public void run() {
        try {
            read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register(String teamName) {
        String registerMsg = "register: " + teamName;
        registerMsg = "{ \"get_team_id_for\" :\"" + teamName + "\"}";

        try {
            this.sendMessage(registerMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) throws IOException {
        int msgLen = message.length();
        String hex = String.format("%04X", msgLen);
        String fullMsg = hex + message + "\0";

        byte[] byteArray = fullMsg.getBytes(StandardCharsets.UTF_8);
        writer.write(byteArray);
    }

    private void read() throws IOException {
        char c;
        while (true) {
            StringBuilder message = new StringBuilder();
            do {
                int r = this.buffReader.read();
                if (r < 0 || r > 65535) {
                    throw new IllegalArgumentException("Invalid Char code: " + r);
                }
                c = (char) r;
                //println("char: " + c)
                message.append(c);

            } while (c != (char) 0);
            String stringMessage = message.toString();

            String json = stringMessage.substring(stringMessage.indexOf("{"), stringMessage.lastIndexOf("}") + 1);
            if (botId == null) {
                Map<String, String> messageMap = objectMapper.readValue(json, new TypeReference<Map<String, String>>() {
                });
                botId = messageMap.get("bot_id");
            } else {
                if (json.contains("\"err\"")) {
                    System.out.println("not good :(");
                } else {
                    MarccoMessage marccoMessage = objectMapper.readValue(json, MarccoMessage.class);
                    if (marccoMessage.gameBoard != null) {
                        marccoMessage.messageType = MessageType.GAME_BOARD;
                        this.board=marccoMessage.gameBoard;
                    } else {
                        marccoMessage.messageType = MessageType.OBJECTS;
                    }
                    bunny.updateLocation(marccoMessage.row,marccoMessage.col);
                    bunny.setStamina(marccoMessage.stamina);
                    bunny.setCurrentRound(marccoMessage.round);
                    bunny.setBackpack(marccoMessage.backpack);
                    String resp=makeAction(bunny, this.board, botId, marccoMessage.objects);

                    this.sendMessage(resp);
                }
            }
        }
    }



    public void close() throws IOException {
        this.connected = false;
        this.connection.close();
    }

    private Socket initConnection(String address, int port) {
        try {
            return new Socket(address, port);
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private OutputStream initWriter() {
        try {
            return this.connection.getOutputStream();
        } catch (IOException ex) {
            return null;
        }
    }

    private BufferedReader initReader() {
        try {
            return new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
        } catch (IOException ex) {
            return null;
        }
    }
}
