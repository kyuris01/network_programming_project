package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static Server.WaitingThread.clientQueue;
import static Server.WaitingThread.partNum;

public class GameThread extends Thread {

    ArrayList<Socket> participants = new ArrayList<>();
    ArrayList<Integer> bidPrice = new ArrayList<>(partNum);
    @Override
    public void run() {
        try { //각 클라이언트의 응찰여부 수집
            for (Socket socket : clientQueue) {
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                if (br.readLine().equals("yes")) {
                    participants.add(socket);
                }

                pw.flush();
                pw.close();
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //호가 수집
        try {

            for (int i = 0; i < partNum; i++) {
                Socket socket = participants.get(i);
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                bidPrice.add(i, Integer.parseInt(br.readLine()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        

    }
}
