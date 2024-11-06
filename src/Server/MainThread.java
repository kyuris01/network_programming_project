package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class MainThread {
    private static final int PORT = 6003;
    private static final int MIN_CLIENTS = 1;
    public static Queue<Socket> clientQueue = new LinkedList<>();

    public static void main(String[] args) {
        System.out.println("경매 서버 시작...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientQueue.add(clientSocket);
                System.out.println("클라이언트가 접속하였습니다. 현재 접속자 수 : " + clientQueue.size());

                if (clientQueue.size() >= MIN_CLIENTS) {

                    for (Socket socket : clientQueue) {
                        InputStream in = socket.getInputStream();
                        OutputStream out = socket.getOutputStream();
                        PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));

                        pw.println("게임이 곧 시작됩니다...");
                        pw.flush();
                        pw.close();
                        br.close();
                    }
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("서버 오류: " + e.getMessage());
        }
        /*
        ItemThread t = new ItemThread();
        t.start();
         */
    }


}

