package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class MainThread {


    public static void main(String[] args) {
        System.out.println("경매 서버 시작...");

        WaitingThread t = new WaitingThread();
        t.start();
        /*
        ItemThread t = new ItemThread();
        t.start();
         */
    }


}

