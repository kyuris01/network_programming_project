package Server;

import java.util.Timer;
import java.util.TimerTask;

class ClockThread extends Thread {
    public static int round = 1;
    public static Boolean timeCheck = false;

    public void waitFor10() {
        Timer timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("Waiting for bidding for 10 secs");
                        timer.cancel(); // 작업이 끝난 후 타이머 종료
                    }
                }, 10000
        );
        timeCheck = true;
    }

    public void waitFor6() {
        Timer timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("Waiting for bidding for 10 secs");
                        timer.cancel(); // 작업이 끝난 후 타이머 종료
                    }
                }, 6000
        );
        timeCheck = true;
    }

    @Override
    public void run() {
        ClockThread t = new ClockThread();
        t.waitFor10();


    }
}
