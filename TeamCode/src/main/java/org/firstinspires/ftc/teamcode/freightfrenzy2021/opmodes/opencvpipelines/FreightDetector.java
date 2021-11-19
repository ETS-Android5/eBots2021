package org.firstinspires.ftc.teamcode.freightfrenzy2021.opmodes.opencvpipelines;

import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.Arrays;

public class FreightDetector extends OpenCvPipeline {
    Mat frameHsv = new Mat();
    Mat frameTargetMat;
    boolean firstPass = true;
    int readingCount = 0;
    boolean readingConsumed = false;
    String logTag = "EBOTS";
    boolean isBox = false;

    //Check the co ordinates
    Rect frameRect = new Rect(new Point(130, 20), new Point(190, 60));
    float confidenceNoRed = 0.0f;
    int averageHue = 0;

    public boolean isReadingConsumed() {
        return readingConsumed;
    }

    public int getReadingCount() {
        return readingCount;
    }

    public float getFrameConfidenceNoRed() {
        return confidenceNoRed;
    }

    public float getAverageHue() {
        return averageHue;
    }

    public boolean getIsBox() {
        return isBox;
    }

    @Override
    public void init(Mat firstFrame) {
        Log.d("EBOTS", "Mat size: " + firstFrame.size().toString());
        frameTargetMat = firstFrame.submat(frameRect);
        Log.d("EBOTS", "SubMat size: " + frameTargetMat.size().toString());

    }

    @Override
    public Mat processFrame(Mat input) {
        double confidenceThreshold = 0.7;

        Imgproc.cvtColor(frameTargetMat, frameHsv, Imgproc.COLOR_RGB2HSV);
        confidenceNoRed = calculateConfidenceNoRed(frameHsv);
        averageHue = calculateAverageHue(frameHsv);
        isBox = calculateConfidenceBox(frameHsv) >= confidenceThreshold;

        readingConsumed = false;    // flat the current value as a new reading
        readingCount++;

        if (firstPass) {
            Log.d("EBOTS", "hsv size: " + frameHsv.size().toString());
            Log.d("EBOTS", "hsv cols: " + String.format("%d", frameHsv.cols()));
            Log.d("EBOTS", "val1: " + Arrays.toString(frameHsv.get(0,0)));
            Log.d("EBOTS", "val2: " + Arrays.toString(frameHsv.get(input.rows()/2,input.cols()/2)));
            Log.d("EBOTS", "val3: " + Arrays.toString(frameHsv.get(input.rows()-1,input.cols()-1)));
            Log.d("EBOTS", "Confidence Not Red: " + String.format("%.1f", confidenceNoRed));
            Log.d("EBOTS", "Average Hue: " + String.format("%d", averageHue));
        }
        // draw a bounding rectangle
        Scalar rectColor = new Scalar(255,10,10);
        int thickness = 2;
        Imgproc.rectangle (input,frameRect, rectColor, thickness);
        firstPass = false;
        return input;
    }

    private float calculateConfidenceNoRed(Mat hsv) {
        int noRed = 0;
        int totalPixels = hsv.rows() * hsv.cols();
        double valueThreshold = 50.0;
        int validPixels = 0;
        double pixelValue = 0;
        for (int row = 0; row < hsv.rows(); row++) {
            for (int col = 0; col < hsv.cols(); col++) {

                pixelValue = hsv.get(row, col)[2];
                if (pixelValue > valueThreshold) {
                    validPixels++;
                    // if value is high enough (ignores black)
                    double currentHue = hsv.get(row, col)[0];
                    if (currentHue > 45 && currentHue < 150){
                        noRed++;
                    }
                }

            }
        }
        Log.d(logTag, "validPixels: " + String.format("%d", validPixels) + ", noRed: " + String.format("%d", noRed));
        float percentValidPixels = ((float) validPixels) / totalPixels;
        float noRedPercentage = (percentValidPixels > 0.40) ? ((float)noRed / validPixels) : 0.0f;
        return  noRedPercentage;
    }

    private double calculateConfidenceBox(Mat hsv){
        int pixelCount = 0;
        double pixelHue;
        double pixelSaturation;
        double pixelValue;

        double valueThreshold = 50.0;
        int cubeCount=0;
        for (int row = 0; row < hsv.rows(); row++) {
            for (int col = 0; col < hsv.cols(); col++) {
                pixelHue = hsv.get(row, col)[0];
                pixelSaturation = hsv.get(row, col)[1];
                pixelValue = hsv.get(row, col)[2];
                pixelCount++;
                // if value is high enough (ignores black)
                if ((pixelHue > 10 && pixelHue < 20) &&
                        pixelSaturation > 200 && pixelValue > 150) {
                    cubeCount++;
                }
            }
        }
        double cubeConfidence = ((double) cubeCount) / pixelCount;
        Log.d(logTag, "cubeCount / pixelCount: " + String.format("%.2f", cubeConfidence));
        return cubeConfidence;
    }

    private int calculateAverageHue(Mat hsv){
        // Average Caused issues with red readings, which can be 150-180 and 0-40 in low light
        int sum = 0;

        int divisor = hsv.rows() * hsv.cols();
        for (int row=0; row < hsv.rows(); row++){
            for (int col=0; col < hsv.cols(); col++){
                sum += hsv.get(row, col)[0];
            }
        }

        double average = ((double) sum) / divisor;
        averageHue = (int) average;

        logColorValues(hsv);

        return (int) average;


    }

    public void markReadingAsConsumed(){
        readingConsumed = true;
    }

    public void logColorValues(Mat hsv){
        Log.d("EBOTS", "val1: " + Arrays.toString(hsv.get(0,0)));
        Log.d("EBOTS", "val2: " + Arrays.toString(hsv.get(hsv.rows()/2,hsv.cols()/2)));
        Log.d("EBOTS", "val3: " + Arrays.toString(hsv.get(hsv.rows()-1,hsv.cols()-1)));
        Log.d("EBOTS", "Avg Hue: " + String.format("%d", averageHue));

    }
}
