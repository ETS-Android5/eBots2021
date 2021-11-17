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

    //Check the co ordinates
    Rect frameRect = new Rect(new Point(10, 170), new Point(80, 200));
    float confidenceNoRed = 0.0f;

    public boolean isReadingConsumed() {
        return readingConsumed;
    }

    public int getReadingCount() {
        return readingCount;
    }

    public float getFrameConfidenceNoRed() {
        return confidenceNoRed;
    }

    @Override
    public void init(Mat firstFrame) {
        Log.d("EBOTS", "Mat size: " + firstFrame.size().toString());
        frameTargetMat = firstFrame.submat(frameRect);
        Log.d("EBOTS", "SubMat size: " + frameTargetMat.size().toString());

    }

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(frameTargetMat, frameHsv, Imgproc.COLOR_RGB2HSV);
        confidenceNoRed = calculateConfidenceNoRed(frameHsv);
        readingConsumed = false;    // flat the current value as a new reading
        readingCount++;

        if (firstPass) {
            Log.d("EBOTS", "hsv size: " + frameHsv.size().toString());
            Log.d("EBOTS", "Lefthsv cols: " + String.format("%d", frameHsv.cols()));
            Log.d("EBOTS", "val1: " + Arrays.toString(frameHsv.get(0,0)));
            Log.d("EBOTS", "val2: " + Arrays.toString(frameHsv.get(14,34)));
            Log.d("EBOTS", "val3: " + Arrays.toString(frameHsv.get(29,69)));
            Log.d("EBOTS", "Left Confidence Red: " + String.format("%.1f", confidenceNoRed));
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
                    if (!(currentHue < 45)| !(currentHue > 150)) {
                        noRed++;
                    }
                }

            }
        }
        float percentValidPixels = ((float) validPixels) / totalPixels;
        float noRedPercentage = (percentValidPixels > 0.40) ? ((float)noRed / validPixels) : 0.0f;
        return  noRedPercentage;
    }

    public void markReadingAsConsumed(){
        readingConsumed = true;
    }
}
