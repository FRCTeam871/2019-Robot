package frc.team871.subsystems.peripheral;

public class Audio {

    /**
     * @param file The filename to play
     */
    public static TeensyPacket play(String file){
        return new TeensyPacket("P" + Teensy.DELIMITER + file);
    }

    /**
     * Stops all audio
     */
    public static TeensyPacket stop(){
        return new TeensyPacket("S");
    }

    /**
     * @param volume The volume in the range [0, 100]
     */
    public static TeensyPacket setVolume(int volume){
        return new TeensyPacket("V" + Teensy.DELIMITER + volume);
    }

}
