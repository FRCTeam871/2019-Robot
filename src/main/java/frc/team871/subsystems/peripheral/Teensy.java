package frc.team871.subsystems.peripheral;

import com.team871.io.peripheral.EndPoint;
import com.team871.io.peripheral.ICommunicationsInterface;
import com.team871.io.peripheral.StringPacket;
import java.util.ArrayDeque;
import java.util.Deque;

public class Teensy {
    private ICommunicationsInterface comms;
    private Deque<String> queue = new ArrayDeque<>();
    private EndPoint endpoint;

    public Teensy(ICommunicationsInterface comms, EndPoint endpoint) {
        this.comms = comms;
        this.endpoint = endpoint;
    }

    public void playSound(Sound sound) {
        write("audio play " + sound.getPath());
    }

    public void stopSound() {
        write("audio mute");
    }

    public void setVolume(double volume) {
        write("audio volume " + volume);
    }

    public void setPixelStripMode(int strip, PixelStripMode mode) {
        write("led " + strip + " " + mode.getIndex());
    }

    public void write(String str) {
        queue.add(str);
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
            System.out.println(packet.getPayload());
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

}
