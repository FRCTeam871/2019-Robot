package frc.team871.subsystems.peripheral;

public class TeensyPacket {

    String payload;

    public TeensyPacket(String payload){
        this.payload = payload;
    }

    public String getPayload(){
        return payload;
    }

}
