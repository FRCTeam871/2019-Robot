package frc.team871.auto;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

//TODO: "implements ITarget"
public abstract class LineSensor {

    PIDSource centerSource;
    PIDSource angleSource;

    public LineSensor(){
        centerSource = new PIDSource() {
            @Override
            public void setPIDSourceType(PIDSourceType pidSource) {
                //no
            }

            @Override
            public PIDSourceType getPIDSourceType() {
                return PIDSourceType.kDisplacement;
            }

            @Override
            public double pidGet() {
                return getCenter();
            }
        };

        angleSource = new PIDSource() {
            @Override
            public void setPIDSourceType(PIDSourceType pidSource) {
                //no
            }

            @Override
            public PIDSourceType getPIDSourceType() {
                return PIDSourceType.kDisplacement;
            }

            @Override
            public double pidGet() {
                return getAngle();
            }
        };
    }

    public abstract double getAngle();
    public PIDSource getAngleSource(){
        return angleSource;
    }

    public abstract double getCenter();
    public PIDSource getCenterSource(){
        return centerSource;
    }

    public abstract boolean hasLine();

}
