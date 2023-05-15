package org.firstinspires.ftc.teamcode.Vision;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class OpenCV extends OpenCvPipeline {
    Mat HSVMat = new Mat(); // Defining "HSVMat" as an empty mat
    Mat contoursOnFrameMat = new Mat(); // Defining "contoursOnFrameMat" as an empty mat

    List<MatOfPoint> contoursList = new ArrayList<>(); // This will hold a list of all the contours

    int numContoursFound = 0; // Defines variable

    public Scalar lowerHSV = new Scalar(59, 89, 172); // Lower color range that returns positive
    public Scalar upperHSV = new Scalar(149, 250, 250); // Higher color range that returns positive

    public double threshold = 100; // Stops searching the image above this point
    public double blurConstant = 1; // How blurry the image is
    public double dilationConstant = 2; // Makes it pixelated

    int duckPosition = -1;

    Telemetry telemetryOpenCV;
    public OpenCV(Telemetry OpModeTelemetry){
        telemetryOpenCV = OpModeTelemetry;
    }


    public int getDuckPosition() {
        return duckPosition;
    }


    @Override
    public Mat processFrame(Mat input) {
        contoursList.clear(); // Clears the Array
        Imgproc.cvtColor(input, HSVMat, Imgproc.COLOR_RGB2HSV_FULL); // Converts from RGB to HSV

        Core.inRange(HSVMat, lowerHSV, upperHSV, HSVMat); // Filters the colors so only what is in the range (lowerHSV and higherHSV)

        Size kernelSize = new Size(blurConstant, blurConstant);

        Imgproc.GaussianBlur(HSVMat, HSVMat, kernelSize, 0); // Blurs the image

        Size kernelSize2 = new Size(2 * dilationConstant + 1, 2 * dilationConstant + 1); // DilationConstant MUST be an ODD number

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, kernelSize2);

        Imgproc.dilate(HSVMat, HSVMat, kernel); // Dilates the image

        Imgproc.findContours(HSVMat, contoursList, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE); // Finds the contours

        numContoursFound = contoursList.size(); // The number of contours found are stored in the variable "numContoursFound"
        input.copyTo(contoursOnFrameMat); // Copies the image to contoursOnFrameMat

        for (MatOfPoint contour : contoursList) { // Stores everything in contoursList into contours
            Rect rect = Imgproc.boundingRect(contour); // Makes a rectangle around all contours

            if (rect.y >= threshold) { // Only if the rectangle is below "threshold"
                Imgproc.rectangle(contoursOnFrameMat, rect.tl(), rect.br(), new Scalar(255, 0, 0), 2); // Outputs the rectangle on screen
                Imgproc.putText(contoursOnFrameMat, String.valueOf(rect.x), rect.tl(), 0, 0.5, new Scalar(255, 255, 255)); // Outputs the X value on screen

                if (rect.x >= 130) { // If the duck is on the right half of the screen
                    duckPosition = 2; // Right
                } else if (rect.x <= 80) {
                    duckPosition = 0; // Left
                } else {
                    duckPosition = 1; // Middle
                }
            }
        }
        return input;
    }
}