package frc.team871.subsystems.peripheral;

import com.team871.io.peripheral.EndPoint;
import com.team871.io.peripheral.ICommunicationsInterface;
import com.team871.io.peripheral.StringPacket;
import java.util.ArrayDeque;
import java.util.Deque;

public class Teensy {
    public static final String DELIMITER = ";";
    public static final String ALL_PREFIX = "A";
    public static final int MAX_MSG_LENGTH = 200;

    private ICommunicationsInterface comms;
    private Deque<String> queue = new ArrayDeque<>();
    private EndPoint endpoint;

    public Teensy(ICommunicationsInterface comms, EndPoint endpoint) {
        this.comms = comms;
        this.endpoint = endpoint;
    }

    public void writePacketAll(TeensyPacket packet){
        write(ALL_PREFIX + DELIMITER + packet)
    }

    public void writePacket(int strip, TeensyPacket packet){
        write(strip + DELIMITER + packet)
    }

    public void write(String str) {
        if(str.length() > MAX_MSG_LENGTH) {
            System.err.println("[Teensy] Message exceeds max length! (" + str.length() + "/" + MAX_MSG_LENGTH + "): " + str);
        }else {
            queue.add(str);
        }
    }

    private long lastFlush = 0;

    public void update() {

        StringPacket packet = new StringPacket();
        if(comms.numAvailableSupported()){
            int bytes = comms.numAvailable(endpoint);
            if(bytes > 0){
                comms.read(endpoint, bytes, packet);
            }
        }else{
            comms.read(endpoint, 1024, packet);
        }

        if(packet.getSize() > 0){
            System.out.println("[Teensy]" + packet.getPayload());
        }

        long now = System.currentTimeMillis();
        if(now - lastFlush >= 100) {
            if(!queue.isEmpty()) {
                packet.setPayload(queue.pop());

//              System.out.println("Writing: " + str);

                comms.send(endpoint, packet);

                lastFlush = now;
            }
        }

//        byte[] read = comms.read( )
//        if(port.getBytesReceived() > 0) {
//            System.out.println("Recieved: " + port.readString());
//        }
//
//        long now = System.currentTimeMillis();
//        if(now - lastFlush >= 100) {
//            String str = null;
//
//            if(!queue.isEmpty()) {
//                str = queue.get(0);
//                queue.remove(0);
//
////              System.out.println("Writing: " + str);
//
//                comms.send(, str.getBytes());
//
//                port.writeString(str + "\n");
//                port.flush();
//
//                lastFlush = now;
//            }
//        }
    }

    public static int color(int r, int g, int b){
        int rgb = red;
        rgb = (rgb << 8) + green;
        rgb = (rgb << 8) + blue;
        return rgb;
    }

    public static String hex(int color){
        return String.format("0x%06X", 0xFFFFFF & color);
    }

}
