import java.net.InetAddress;
import java.net.UnknownHostException;

public class MyInetAddress {

    public static void main(String[] args) {
        InetAddress inetAddr = null;
        try {
            inetAddr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        System.out.println(inetAddr.getHostName());
        System.out.println(inetAddr.getHostAddress());
    }

}