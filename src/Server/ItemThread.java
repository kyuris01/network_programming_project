package Server;

import java.io.*;
import java.net.Socket;
import java.util.Random;

class ItemThread extends Thread {
    public void run() {
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        int goodsOrItems = rand.nextInt(2);
        String [] goods = new String[] {"쿠", "건구스", "건덕이", "건붕이"};
        String [] items = new String[] {"현금지급기", "경매무효화", "강제낙찰권", "저격"};
        String auctionItem;
        if (ClockThread.round == 2) {
            auctionItem = items[rand.nextInt(items.length)];
        } else {
            if (goodsOrItems == 0) { //굿즈
                auctionItem = items[rand.nextInt(items.length)];
            } else { //아이템
                auctionItem = goods[rand.nextInt(goods.length)];
            }
        }

        try {
            for (Socket socket : MainThread.clientQueue) {
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                pw.println(auctionItem);

                pw.close();
                br.close();
            }
        } catch (IOException e) {
            System.out.println("ItemThread Error");
        }



    }

}

