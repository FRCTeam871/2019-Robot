package frc.team871.config;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import com.team871.hid.IAxis;
import com.team871.io.actuator.CombinedSpeedController;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SpeedController;
import java.util.Arrays;

public enum RowBoatConfigHack implements IRowBoatConfig{
    DEFAULT;

    SpeedController frontLeftMotor;
    SpeedController rearLeftMotor;
    SpeedController frontRightMotor;
    SpeedController rearRightMotor;
    SpeedController lowerArmMotor;
    SpeedController upperArmMotor;
    SpeedController wristMotor;
    SpeedController vacuumMotor;
    AHRS gyro;
    AnalogPotentiometer w;
    IAxis wp;
    IAxis lp;
    IAxis up;

    RowBoatConfigHack(){
          this.frontLeftMotor = new WPI_VictorSPX(3);
          //this.frontLeftMotor.setInverted(true);
           this.rearLeftMotor = new WPI_VictorSPX(2);
         this.frontRightMotor = new WPI_VictorSPX(0);
          this.rearRightMotor = new WPI_VictorSPX(1);
          //this.rearRightMotor.setInverted(true);


            this.wristMotor = new WPI_TalonSRX(4);
         this.upperArmMotor = new CombinedSpeedController(Arrays.asList(new WPI_TalonSRX(5), new WPI_TalonSRX(6)));
         this.lowerArmMotor = new WPI_TalonSRX(7);
           this.vacuumMotor = new WPI_TalonSRX(8);

         this.gyro = new AHRS(SerialPort.Port.kMXP);

         TalonSRX t = (TalonSRX) wristMotor;

        wp = new IAxis() {
            @Override
            public double getRaw() {
                return t.getSelectedSensorPosition();
            }

            @Override
            public double getValue() {
                return getRaw();
            }

            @Override
            public void setMapping(double v, double v1) {

            }
        };

        TalonSRX tt = (TalonSRX) lowerArmMotor;

        lp = new IAxis() {
            @Override
            public double getRaw() {
                return tt.getSelectedSensorPosition();
            }

            @Override
            public double getValue() {
                return getRaw();
            }

            @Override
            public void setMapping(double v, double v1) {

            }
        };

        TalonSRX ttt = (TalonSRX) upperArmMotor;

        up = new IAxis() {
            @Override
            public double getRaw() {
                return ttt.getSelectedSensorPosition();
            }

            @Override
            public double getValue() {
                return getRaw();
            }

            @Override
            public void setMapping(double v, double v1) {

            }
        };
         //TODO find sensor channels
    }

    @Override
    public SpeedController getFrontLeftMotor() {
        return frontLeftMotor;
    }

    @Override
    public SpeedController getRearLeftMotor() {
        return rearLeftMotor;
    }

    @Override
    public SpeedController getFrontRightMotor() {
        return frontRightMotor;
    }

    @Override
    public SpeedController getRearRightMotor() {
        return rearRightMotor;
    }

    @Override
    public SpeedController getLowerArmMotor() {
        return lowerArmMotor;
    }

    @Override
    public SpeedController getUpperArmMotor() {
        return upperArmMotor;
    }

    @Override
    public SpeedController getWristMotor() {
        return wristMotor;
    }

    @Override
    public SpeedController getVacuumMotor() {
        return vacuumMotor;
    }

    @Override
    public AHRS getGyro() {
        return gyro;
    }

    @Override
    public AnalogInput getLowerArmAxisSensor() {
        return null;
    }

    @Override
    public AnalogInput getUpperArmAxisSensor() {
        return null;
    }

    public IAxis getWristPot(){return wp;}
    public IAxis getLowerPot(){return lp;}
    public IAxis getUpperPot(){return up;}

    @Override
    public AnalogInput getWristAxisSensor() {
        return null;
    }

    @Override
    public AnalogPotentiometer getLowerArmPot() {
        return null;
    }

    @Override
    public AnalogPotentiometer getUpperArmPot() {
        return null;
    }

    @Override
    public AnalogPotentiometer getWristPotAxis() {
        return null;
    }

    @Override
    public DigitalInput getGrabSensor() {
        return null;
    }
}
