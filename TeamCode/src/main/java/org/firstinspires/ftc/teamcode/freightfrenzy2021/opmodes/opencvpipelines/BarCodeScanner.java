package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.opencvpipelines;

import android.util.Log;

import com.vuforia.Rectangle;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.Arrays;

public class BarCodeScanner extends OpenCvPipeline {
    // Notice this is declared as an instance variable (and re-used), not a local variable
    Mat hsv = new Mat();
    Mat submat;
    boolean firstPass = true;
    int avgHue;
    Rect leftRect = new Rect(new Point(15, 120), new Point(105, 170));
    Rect rightRect = new Rect(new Point(200, 120), new Point(230, 170));

    @Override
    public void init(Mat firstFrame) {
        Log.d("EBOTS", "Mat size: " + firstFrame.size().toString());
        submat = firstFrame.submat(leftRect);
        Log.d("EBOTS", "SubMat size: " + submat.size().toString());
        Log.d("EBOTS", "First cell values " + Arrays.toString(submat.get(0,0)));
    }

    @Override
    public Mat processFrame(Mat mat) {
        Imgproc.cvtColor(submat, hsv, Imgproc.COLOR_RGB2HSV);
        setAverageHue(hsv);
        if (firstPass) {
            Log.d("EBOTS", "hsv size: " + hsv.size().toString());
            Log.d("EBOTS", "hsv cols: " + String.format("%d", hsv.cols()));
            Log.d("EBOTS", "val1: " + Arrays.toString(hsv.get(0,0)));
            Log.d("EBOTS", "val2: " + Arrays.toString(hsv.get(45,17)));
            Log.d("EBOTS", "val3: " + Arrays.toString(hsv.get(85,32)));
            Log.d("EBOTS", "Average Hue: " + String.format("%d", avgHue));
        }

        // draw a bounding rectangle
        Scalar rectColor = new Scalar(255,10,10);
        Point upperLeft = new Point(15, 120);
        Point lowerRight = new Point(105, 170);
        int thickness = 2;
        Imgproc.rectangle (mat,leftRect, rectColor, thickness);
        Imgproc.rectangle (mat,rightRect, rectColor, thickness);

        firstPass = false;

        return mat;
    }


    public int getAvgHue(){
        return avgHue;
    }

    private void setAverageHue(Mat hsv){
        int sum = 0;
        int divisor = hsv.rows() * hsv.cols();
        for (int row=0; row < hsv.rows(); row++){
            for (int col=0; col < hsv.cols(); col++){
                sum += hsv.get(row, col)[0];
            }
        }

        double average = ((double) sum) / divisor;
        avgHue = (int) average;

    }
}
