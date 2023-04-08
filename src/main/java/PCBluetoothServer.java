import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.*;

public class PCBluetoothServer {
    private String UUID = "0000110100001000800000805F9B34FB";

    public static void main(String[] args) {
        new PCBluetoothServer();
    }

    public PCBluetoothServer() {
        try {
            /**
             * 开启一个连接，等待访问
             */
            String url = "btspp://localhost:" + UUID;
            StreamConnectionNotifier notifier = (StreamConnectionNotifier) Connector
                    .open(url);
            serverLoop(notifier);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void serverLoop(StreamConnectionNotifier notifier) {
        try {
            // infinite loop to accept connections.
            while (true) {
                System.out.println("等待连接");
                /**
                 * 阻塞方法acceptAndOpen(),一直等待
                 */
                handleConnection(notifier.acceptAndOpen());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 处理连接请求,使用线程异步处理，避免一直等待处理完成
     * 先要接收到客户端信息，才可以继续执行
     * @param conn
     * @throws IOException
     */
    private void handleConnection(StreamConnection conn) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    OutputStream out = conn.openOutputStream();
                    InputStream in = conn.openInputStream();
//                    DataOutputStream dos = new DataOutputStream(out);
                    DataInputStream dis = new DataInputStream(in);
                    // 阻塞方法
                    String read = dis.readUTF();
                    System.out.println("接收到Client:" + read);
//                    //给客户端回复信息
//                    dos.writeUTF("Server receive you Msg,Thank you!");
//                    dos.close();
//                    dis.close();
//                    conn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
