import java.net.Socket;
import java.io.*;

public class Client {

    private static final String SERVER_IP = "";

    public static void main(String[] args) {
        try {
            System.out.println("start");
            Socket socket = new Socket(SERVER_IP, 6003);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            PrintWriter pw  = new PrintWriter(new OutputStreamWriter(out));
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String msg = null;
            msg = br.readLine();
            System.out.println(msg);
            System.out.println("end");
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
