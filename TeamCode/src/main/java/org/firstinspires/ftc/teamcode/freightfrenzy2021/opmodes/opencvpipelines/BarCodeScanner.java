package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.opencvpipelines;

import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.Arrays;

public class BarCodeScanner extends OpenCvPipeline {
    // Notice this is declared as an instance variable (and re-used), not a local variable
    Mat leftHsv = new Mat();
    Mat rightHsv = new Mat();
    Mat leftTargetMat;
    Mat rightTargetMat;
    boolean firstPass = true;
    int leftHue;
    int rightHue;
    int readingCount = 0;
    boolean readingConsumed = false;    // manage state of whether reading has been consumed
    Rect leftRect = new Rect(new Point(10, 170), new Point(80, 200));
    Rect rightRect = new Rect(new Point(165, 170), new Point(230, 200));

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Getters & Setters
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public int getLeftHue(){
        return leftHue;
    }
    public int getRightHue(){
        return rightHue;
    }

    public boolean isReadingConsumed() {
        return readingConsumed;
    }

    public int getReadingCount() {
        return readingCount;
    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Static Methods
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    // No static methods defined


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Instance Methods
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @Override
    public void init(Mat firstFrame) {
        Log.d("EBOTS", "Mat size: " + firstFrame.size().toString());
        leftTargetMat = firstFrame.submat(leftRect);
        rightTargetMat = firstFrame.submat(rightRect);
        Log.d("EBOTS", "SubMat size: " + leftTargetMat.size().toString());
        Log.d("EBOTS", "First cell values " + Arrays.toString(leftTargetMat.get(0,0)));
    }

    @Override
    public Mat processFrame(Mat mat) {
        Imgproc.cvtColor(leftTargetMat, leftHsv, Imgproc.COLOR_RGB2HSV);
        Imgproc.cvtColor(rightTargetMat, rightHsv, Imgproc.COLOR_RGB2HSV);
        leftHue = calculateAverageHue(leftHsv);
        rightHue = calculateAverageHue(rightHsv);
        readingConsumed = false;    // flat the current value as a new reading
        readingCount++;

        if (firstPass) {
            Log.d("EBOTS", "hsv size: " + leftHsv.size().toString());
            Log.d("EBOTS", "hsv cols: " + String.format("%d", leftHsv.cols()));
            Log.d("EBOTS", "val1: " + Arrays.toString(leftHsv.get(0,0)));
            Log.d("EBOTS", "val2: " + Arrays.toString(leftHsv.get(45,17)));
            Log.d("EBOTS", "val3: " + Arrays.toString(leftHsv.get(85,32)));
            Log.d("EBOTS", "Left Hue: " + String.format("%d", leftHue));
            Log.d("EBOTS", "Right Hue: " + String.format("%d", rightHue));
        }

        // draw a bounding rectangle
        Scalar rectColor = new Scalar(255,10,10);
        int thickness = 2;
        Imgproc.rectangle (mat,leftRect, rectColor, thickness);
        Imgproc.rectangle (mat,rightRect, rectColor, thickness);

        firstPass = false;

        return mat;
    }



    public void markReadingAsConsumed(){
        readingConsumed = true;
    }

    private int calculateAverageHue(Mat hsv){
        int sum = 0;
        int divisor = hsv.rows() * hsv.cols();
        for (int row=0; row < hsv.rows(); row++){
            for (int col=0; col < hsv.cols(); col++){
                sum += hsv.get(row, col)[0];
            }
        }

        double average = ((double) sum) / divisor;
        return (int) average;

    }
}
