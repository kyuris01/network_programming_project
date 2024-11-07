package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static Server.WaitingThread.clientQueue;
import static Server.WaitingThread.playerNum;

public class ClientHandlerThread extends Thread{
    //플레이어가 응찰을 하면 참여자


    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//    ArrayList<Socket> participants = new ArrayList<>(playerNum);
//    ArrayList<Integer> bidPrice = new ArrayList<>(playerNum);
//    ArrayList<Boolean> isBid = new ArrayList<>(playerNum);
    ArrayList<PlayerInfo> participantsList = new ArrayList<>(playerNum);

    public class PlayerInfo {
        private Boolean isBid;
        private Integer bidPrice;
        private Socket socket;

        public PlayerInfo(Boolean isBid, Integer bidPrice, Socket socket) {
            this.isBid = isBid;
            this.bidPrice = bidPrice;
            this.socket = socket;
        }

        public Boolean getBid() {
            return isBid;
        }

        public void setBid(Boolean bid) {
            isBid = bid;
        }

        public Integer getBidPrice() {
            return bidPrice;
        }

        public void setBidPrice(Integer bidPrice) {
            this.bidPrice = bidPrice;
        }

        public Socket getSocket() {
            return socket;
        }

        public void setSocket(Socket socket) {
            this.socket = socket;
        }
    }

    public void BidCheck() {

        try { //각 클라이언트의 응찰여부 수집
            for (Socket socket : clientQueue) {
                PlayerInfo playerInfo = new PlayerInfo(false, 0, socket);
                playerInfo.socket = socket;
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(out), true);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                pw.println("응찰에 참여하시겠습니까?");
                String clientMsg = br.readLine();
                if (clientMsg.equals("yes")) {
                    playerInfo.setBid(true);
                    pw.println("당신은 응찰에 참여합니다");
                } else if (br.readLine().equals("no")) {
                    playerInfo.setBid(false);
                    pw.println("당신은 응찰에 참여하지 않습니다");
                }
                participantsList.add(playerInfo);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

//    public void BidPriceCheck() {
//        scheduler.scheduleAtFixedRate(() -> {
//            ArrayList<Boolean> sequenceBidPrice = new ArrayList<>(playerNum); //해당 호가시퀀스에서의 비드 여부를 담는 어레이리스트
//            //호가시작
//            boolean isPrice = false; //호가여부
//            for (PlayerInfo playerInfo : participantsList) {
//                if (playerInfo.getBidPrice() != 0) {
//                    isPrice = true;
//                }
//            }
//            if (isPrice) {
//                System.out.println("임의의 참여자가 호가함. 10초 대기 시작...");
//                resetTimer(); // 타이머 리셋
//            } else {
//                System.out.println("어떠한 참여자도 호가하지않음. 다음 단계로 이동...");
//                nextPhase();
//            }
//
//        }, 0, 10, TimeUnit.SECONDS);
//    }

    public void resetTimer() {
        scheduler.schedule(this::CollectBidPrice, 10, TimeUnit.SECONDS);
    }

    public void nextPhase() {
        System.out.println("next phase");
        for (PlayerInfo playerInfo : participantsList) {
            System.out.println("playerinfo.getBid(): " + playerInfo.getBid());
            System.out.println("playerinfo.getBidPrice(): " + playerInfo.getBidPrice());
        }
    }
    
    public void CollectBidPrice() {
        scheduler.scheduleAtFixedRate(() -> {
            ArrayList<Integer> oldBidPriceList = new ArrayList<>(playerNum);

            for (int i = 0; i < playerNum; i++) {
                if (participantsList.get(i).getBid()) {
                    try {
                        Socket socket = participantsList.get(i).getSocket();
                        InputStream in = socket.getInputStream();
                        OutputStream out = socket.getOutputStream();
                        PrintWriter pw = new PrintWriter(new OutputStreamWriter(out), true);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        pw.println("호가하십시오!");
                        oldBidPriceList.add(i, participantsList.get(i).getBidPrice());
                        participantsList.get(i).setBidPrice(Integer.parseInt(br.readLine()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    continue;
                }

            }



            //호가시작
            boolean newBid = false; //호가여부
            for (int i = 0; i < oldBidPriceList.size(); i++) {
                if (oldBidPriceList.get(i) != participantsList.get(i).getBidPrice()) {
                    newBid = true;
                }
            }
            if (newBid) {
                System.out.println("임의의 참여자가 호가함. 10초 대기 시작...");
                resetTimer(); // 타이머 리셋
            } else {
                System.out.println("어떠한 참여자도 호가하지않음. 다음 단계로 이동...");
                nextPhase();
            }

        }, 0, 10, TimeUnit.SECONDS);





    }


    @Override
    public void run() {
        BidCheck();
        CollectBidPrice();

    }
}
