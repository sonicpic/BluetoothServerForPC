import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class BluetoothServer {

    // 蓝牙服务名称
    private static final String SERVICE_NAME = "BluetoothServer";

    public static void main(String[] args) throws IOException {
        // 获取本地蓝牙适配器
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        // 开启可被发现模式
        localDevice.setDiscoverable(DiscoveryAgent.GIAC);

        // 创建连接监听器
        String UUID = "0000110100001000800000805F9B34FB";
        String url = "btspp://localhost:" + UUID;
        StreamConnectionNotifier notifier = (StreamConnectionNotifier) Connector.open(url);
        System.out.println("等待连接...");

        while (true) {
            // 等待连接
            StreamConnection conn = notifier.acceptAndOpen();
            System.out.println("连接已建立");

            // 获取远程设备信息
            RemoteDevice remoteDevice = RemoteDevice.getRemoteDevice(conn);
            System.out.println("远程设备名称: " + remoteDevice.getFriendlyName(true));
            System.out.println("远程设备地址: " + remoteDevice.getBluetoothAddress());

            // 获取输入输出流
            InputStream inStream = conn.openInputStream();
            OutputStream outStream = conn.openOutputStream();

            // 循环响应
            while (true) {
                try{
                    // 读取数据
                    byte[] buffer = new byte[1024];
                    int bytesRead = inStream.read(buffer);
                    String receivedData = new String(buffer, 0, bytesRead);
                    System.out.println("接收到数据:" + receivedData);
                    // 发送数据
                    String sendData = "Hello from server";
                    outStream.write(sendData.getBytes());
                }catch (Exception e){
                    System.out.println(e.toString());
                    break;
                }
            }
        }
    }
}
