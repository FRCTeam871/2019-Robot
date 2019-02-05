package frc.team871;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class SettablePIDSource implements PIDSource {

    double val;
    PIDSourceType type = PIDSourceType.kDisplacement;

    public SettablePIDSource(double v){
        this.val = v;
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
        this.type = pidSource;
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return type;
    }

    @Override
    public double pidGet() {
        return this.val;
    }

    public void setValue(double val){
        this.val = val;
    }

}
