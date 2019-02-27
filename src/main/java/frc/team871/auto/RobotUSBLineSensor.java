package frc.team871.auto;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.vision.VisionThread;
import java.text.DecimalFormat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.RotatedRect;
import org.opencv.imgproc.Imgproc;

public class RobotUSBLineSensor implements ILineSensor{

    VisionThread vt;

    public double angle;
    public double centerX;
    public double centerY;
    public double lengthX;
    public double lengthY;
    public boolean hasLine;
    UsbCamera cam;
    int width;
    int height;

    RobotUSBLineSensor(UsbCamera cam, int width, int height){
        vt = new VisionThread(cam, new GripPipeline(), this::update);
        vt.start();
        this.cam = cam;
        this.width = width;
        this.height = height;
    }

    private <P extends GripPipeline> void update(P pipeline) {
        if(!pipeline.findContoursOutput().isEmpty()){
            hasLine = true;
            RotatedRect r = Imgproc.minAreaRect(new MatOfPoint2f(pipeline.findContoursOutput().stream().sorted((m1, m2) -> {
                return Imgproc.contourArea(m1) < Imgproc.contourArea(m2) ? -1 : 1;
            }).findFirst().get().toArray()));
//            angle = r.angle;
//            if(r.size.width > r.size.height) angle = angle + 90;
            angle = r.angle + ((r.size.width > r.size.height)? 90: 0);
            DecimalFormat d = new DecimalFormat("0.0");
            centerX = r.center.x - (width / 2);
            centerY = r.center.y;
            if(r.size.width > r.size.height) {
                lengthX = r.size.height;
                lengthY = r.size.width;
            }else {
                lengthX = r.size.width;
                lengthY = r.size.height;
            }

            //System.out.println(pipeline.filterContoursOutput().size() + "\t" + d.format(r.center.x) + "\t" + d.format(angle) + "\t" + d.format(r.size.width) + "\t" + d.format(r.size.height));
        } else{
            hasLine = false;
        }
    }

    @Override
    public double getLineAngle() {
        return angle;
    }

    @Override
    public double getCenterX() {
        return centerX;
    }

    @Override
    public double getCenterY() {
        return centerY;
    }

    @Override
    public double getDistance() {
        //Returning 0 is intentional
        return 0;
    }

    @Override
    public double getLengthX() {
        return lengthX;
    }

    @Override
    public double getLengthY() {
        return lengthY;
    }

    @Override
    public boolean doesTargetExist() {
        return hasLine;
    }

}
