package frc.team871.subsystems.peripheral;

public class LEDStripMode {

    //LEDStripModeOff

    public static TeensyPacket off(){
        return new TeensyPacket("OFF");
    }

    //LEDStripModeSolid

    public static TeensyPacket solid(int color){
        return new TeensyPacket("SOLID" + Teensy.DELIMITER + Teensy.hex(color));
    }

    //LEDStripModeBounce

    public static TeensyPacket bounce(int num, int... colors){
        String build = "BOUNCE" + Teensy.DELIMITER + num;
        for(int i = 0; i < colors.length; i++){
            build += Teensy.DELIMITER + String.format("#%06X", 0xFFFFFF & colors[i]);
        }
        return new TeensyPacket(build);
    }

    //LEDStripModeChase

    /**
     * @param offset How many ms each pixel is offset.
     * @param period The period of the animation in ms.
     * @param change The time during the period before which it uses the first color and after which it uses the second color.
     */
    public static TeensyPacket chase(int offset, int period, int change, int color1, int color2){
        return new TeensyPacket("CHASE" + Teensy.DELIMITER + offset + Teensy.DELIMITER + period + Teensy.DELIMITER + change + Teensy.DELIMITER + Teensy.hex(color1) + Teensy.DELIMITER + Teensy.hex(color2));
    }

    //LEDStripModeWave

    /**
     * @param height The max height of the wave as a percentage [0-100]
     * @param period The period of the animation in ms.
     */
    public static TeensyPacket wave(int color1, int color2, int height, int period){
        return new TeensyPacket("WAVE" + Teensy.DELIMITER + Teensy.hex(color1) + Teensy.DELIMITER + Teensy.hex(color2) + Teensy.DELIMITER + height + Teensy.DELIMITER + period);
    }

    //LEDStripModeFade

    /**
     * @param period The period of the animation in ms.
     * @param offset How many ms each pixel is offset.
     */
    public static TeensyPacket fade(int color1, int color2, int period, int offset){
        return new TeensyPacket("FADE" + Teensy.DELIMITER + Teensy.hex(color1) + Teensy.DELIMITER + Teensy.hex(color2) + Teensy.DELIMITER + period + Teensy.DELIMITER + offset);
    }

}
