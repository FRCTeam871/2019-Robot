package frc.team871.auto;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.vision.VisionThread;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;
import org.opencv.core.CvType;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.RotatedRect;
import org.opencv.imgproc.Imgproc;

public class RobotUSBTargetProvider implements ITargetProvider{

    private RobotUSBLineSensor ls;

    VisionThread vt;

    public double angle;
    public double centerX;
    public double centerY;
    public double lengthX;
    public double lengthY;
    public boolean hasTarget;

    int width;
    int height;

    public RobotUSBTargetProvider(UsbCamera lineCam, UsbCamera targetCam, int width, int height, int width2, int height2){
        ls =  new RobotUSBLineSensor(lineCam, width, height);

        vt = new VisionThread(targetCam, new GripPipelineTwo(), this::update);
        vt.start();
        this.width = width2;
        this.height = height2;
    }

    private <P extends GripPipelineTwo> void update(P pipeline) {
//        System.out.println(pipeline.filterContoursOutput().size() + " " + pipeline.findContoursOutput().size());
        if(!pipeline.convexHullsOutput().isEmpty()){
            List<RotatedRect> rects = pipeline.convexHullsOutput().stream().sorted((m1, m2) -> {
                return Imgproc.contourArea(m1) < Imgproc.contourArea(m2) ? -1 : 1;
            }).map(m -> {
                MatOfPoint2f m2f = new MatOfPoint2f();
                m.convertTo(m2f, CvType.CV_32FC2);
                return Imgproc.minAreaRect(m2f);
            }).collect(Collectors.toList());
//            angle = r.angle;
//            if(r.size.width > r.size.height) angle = angle + 90;
//            System.out.println("num = " + rects.size());
            for(int i = 0; i < rects.size(); i++) {
                RotatedRect r = rects.get(i);
                double angle = r.angle + ((r.size.width > r.size.height) ? 90 : 0);
                DecimalFormat d = new DecimalFormat("0.0");
                double centerX = r.center.x - (width / 2);
                double centerY = r.center.y;
                double lengthX, lengthY;
                if (r.size.width > r.size.height) {
                    lengthX = r.size.height;
                    lengthY = r.size.width;
                } else {
                    lengthX = r.size.width;
                    lengthY = r.size.height;
                }

//                System.out.println(i + ")\t" + d.format(r.center.x) + "\t" + d.format(angle) + "\t" + d.format(r.size.width) + "\t" + d.format(r.size.height));
            }

            if(rects.size() >= 2){
                hasTarget = true;
                select(rects.get(0), rects.get(1));
            }else{
                hasTarget = false;
            }

//            System.out.println(pipeline.convexHullsOutput().size() + "\t" + d.format(r.center.x) + "\t" + d.format(angle) + "\t" + d.format(r.size.width) + "\t" + d.format(r.size.height));
        } else {
            hasTarget = false;
//            System.out.println("no");
        }
    }

    @Override
    public ILineSensor getLineSensor() {
        return ls;
    }

    @Override
    public ITarget getTarget() {
        return new ITarget() {
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
                return 6000;
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
                return hasTarget;
            }
        };
    }

    void select(RotatedRect rect1, RotatedRect rect2){

        int width = (rect2.boundingRect().x + rect2.boundingRect().width) - rect1.boundingRect().x;
        int height = (rect2.boundingRect().y + rect2.boundingRect().height) - rect1.boundingRect().y;

        angle = (rect1.angle + rect2.angle) / 2;
        DecimalFormat d = new DecimalFormat("0.0");
        centerX = (rect1.center.x + rect2.center.x)/2 - (this.width / 2);
        centerY = (rect1.center.y + rect2.center.y)/2;
        lengthX = width;
        lengthY = height;

    }

}
