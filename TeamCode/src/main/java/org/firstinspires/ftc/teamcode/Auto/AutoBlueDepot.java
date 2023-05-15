package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Err;
import org.firstinspires.ftc.teamcode.Vision.OpenCV;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

//@Disabled
@Autonomous
public class AutoBlueDepot extends OpMode {
    DcMotor fL = null;
    DcMotor fR = null;
    DcMotor bL = null;
    DcMotor bR = null;
    public CRServo intakeLeft = null;
    public CRServo intakeRight = null;
    DcMotor CSpinner = null;
    public ElapsedTime runtime = new ElapsedTime();
    public int ifLoopCount;
    public boolean runOnce = false;
    public boolean runLoopOnce = false;
    int leftFrontTarget;
    int rightFrontTarget;
    int leftBackTarget;
    int rightBackTarget;
    boolean ififrun = false;

    static final double TICKS_PER_REV = 1120; // 1120
    static final double wheelDiameter = 4.72441; // 4.72441
    static final double TICKS_PER_INCH = TICKS_PER_REV / (wheelDiameter * Math.PI);

    OpenCvCamera webcam;
    OpenCV detector = new OpenCV(telemetry); // Connects "detector" to the OpenCV code

    org.firstinspires.ftc.teamcode.Err Err = new Err(telemetry);

    @Override
    public void init() {
        Err.init(this);
        Err.telemetryUpdate();


        fL = Err.hardwareMapDc("Front Left Drive");
        fR = Err.hardwareMapDc("Front Right Drive");
        bL = Err.hardwareMapDc("Back Left Drive");
        bR = Err.hardwareMapDc("Back Right Drive");
        CSpinner = Err.hardwareMapDc("Carousel Spinner");
        intakeLeft = Err.hardwareMapCRServo("Intake Left");
        intakeRight = Err.hardwareMapCRServo("Intake Right");


        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId); // Hardware maps the webcam
        webcam.setPipeline(detector); // Hardware maps the webcam

        //Err.setDirection(fL, "REVERSE");
        //Err.setDirection(bL, "REVERSE");

        fL.setDirection(DcMotorSimple.Direction.REVERSE);
        bL.setDirection(DcMotorSimple.Direction.REVERSE);

        fR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        ifLoopCount = 0;

        telemetry.addData("Duck", "Duck: Unknown");

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }
            @Override
            public void onError(int errorCode)
            {
                // This will be called if the camera could not be opened
                telemetry.addData("Error", errorCode);
            }
        });


    }

    public void init_loop() {
        telemetry.addData("Duck Position", detector.getDuckPosition());
    }

    @Override
    public void loop() {
        if(!runLoopOnce) {
            try {
                webcam.stopStreaming();
            }
            catch (Exception ignored) {}
            runtime.reset();
            runLoopOnce = true;
        }
        if (detector.getDuckPosition() == 0) {
            telemetry.addData("Duck", "Duck: Middle");
            // Run code if duck is in the middle

            if (ifLoopCount == 0) {
                //encoderDrive( 0.15, -12, 12); // Test line of code.
                encoderDrive(0.15, 3, 3); // Moves the robot 1.5-ish inches forward at 15% speed
            }
            if (runtime.time() >= 0.500 && ifLoopCount == 1) { // Elapsed time 500 milliseconds
                encoderDrive(0.15, -5, 5); // Turns the robot left at 15% speed
                //ifLoopCount++;
            }
            if (runtime.time() >= 0.250 && ifLoopCount == 2) { // Elapsed time 250
                encoderDrive(0.2, -8.5, -8.5); // Moves the robot 7-ish inches backwards at 40% speed
                //ifLoopCount++;
            }
            if (runtime.time() >= 0.300 && ifLoopCount == 3) { // Elapsed time 300
                // Run the carousel spinner
                CSpinner.setPower(-0.5);
                encoderDrive(0.035, -4, -4);
                //runtime.reset();
                //ifLoopCount++;
            }
            if (runtime.time() >= 2.500 && ifLoopCount == 4) {
                CSpinner.setPower(0);
                encoderDrive(0.2, 1, 1);
                //ifLoopCount++;
            }
            if (runtime.time() >= 0.15 && ifLoopCount == 5) { // Turns towards depot
                encoderDrive(0.15, 10, -10);
            }
            if (runtime.time() >= 0.25 && ifLoopCount == 6) {
                encoderDrive(0.2, 7.5, 7.5);
            }
            /*
            if (runtime.time() >= 0.250 && ifLoopCount == 7) {
                Err.setPowerCRServo(intakeLeft, 1.0);
                Err.setPowerCRServo(intakeRight, -1.0);
                ifLoopCount++;
            }
            if (runtime.time() >= 1.5 && ifLoopCount == 8) {
                Err.setPowerCRServo(intakeLeft, 0);
                Err.setPowerCRServo(intakeRight, 0);
                ifLoopCount++;
            }
            */




            /*
            if (runtime.time() >= 0.300 && ifLoopCount == 5) { // Elapsed time 300 (guessed)
                encoderDrive(0.2, 2, -2);
                //ifLoopCount++;
            }
            if (runtime.time() >= 0.250 && ifLoopCount == 6) { // Elapsed time 250
                encoderDrive(0.1, 2, 2); // Needs to go farther?
                //ifLoopCount++;
            }
            if (ifLoopCount == 7) {
                // Run code to drop cube into level
                Err.setPowerCRServo(intakeLeft, -1.0);
                Err.setPowerCRServo(intakeRight, 1.0);
                runtime.reset();
                ifLoopCount++;
            }
            if (runtime.time() >= 1.250 && ifLoopCount == 8) { // Elapsed time 350 (guessed)
                encoderDrive(0.3, -3, -3);
                //ifLoopCount++;
            }
            if (runtime.time() >= 0.250 && ifLoopCount == 9) { // Elapsed time 250
                encoderDrive(0.3, -5, 5);
                //ifLoopCount++;
            }
            if (runtime.time() >= 0.250 && ifLoopCount == 10) { // Elapsed time 250
                encoderDrive(0.7, 15, 15);
                //ifLoopCount++;
            }*/
            telemetry.addData("L", Math.round(Math.abs(fL.getCurrentPosition() + bL.getCurrentPosition()))/2 + " R: " + Math.round(Math.abs(fR.getCurrentPosition() + bR.getCurrentPosition()))/2 + " Target: " + leftFrontTarget);
            telemetry.addData("Runtime", "Runtime: " + runtime.toString());
            telemetry.addData("Loop", ifLoopCount);
            telemetry.addData("Pos", "fL: " + fL.getCurrentPosition() + " fR: " + fR.getCurrentPosition() + " bL: " + bL.getCurrentPosition() + " bR: " + bR.getCurrentPosition() + " Target: " + leftFrontTarget + ", " + rightFrontTarget);

            if(((Math.round(Math.abs(fL.getCurrentPosition() + bL.getCurrentPosition()))/2) >= Math.abs(leftFrontTarget))) {
                telemetry.addData("LeftTargetReached: ", "True" + " " + ((Math.round(Math.abs(fL.getCurrentPosition() + bL.getCurrentPosition()))/2)));
            } else {
                telemetry.addData("LeftTargetReached: ", "False" + " " + ((Math.round(Math.abs(fL.getCurrentPosition() + bL.getCurrentPosition()))/2)));
            }
            if(Math.round(Math.abs(bR.getCurrentPosition()) + 200) >= Math.abs(rightBackTarget)) {
                telemetry.addData("RightTargetReached: ", "True" + " " + Math.round(Math.abs(bR.getCurrentPosition())));
            } else {
                telemetry.addData("RightTargetReached: ", "False" + " " + Math.round(Math.abs(bR.getCurrentPosition())));
            }
            telemetry.addData("If", ififrun);
        } else if (detector.getDuckPosition() == 1) {
            telemetry.addData("Duck", "Duck: Left");
            // Run code if duck is on the left

            if (ifLoopCount == 0) {
                //encoderDrive( 0.15, -12, 12); // Test line of code.
                encoderDrive(0.15, 3, 3); // Moves the robot 1.5-ish inches forward at 15% speed
            }
            if (runtime.time() >= 0.500 && ifLoopCount == 1) { // Elapsed time 500 milliseconds
                encoderDrive(0.15, -5, 5); // Turns the robot left at 15% speed
                //ifLoopCount++;
            }
            if (runtime.time() >= 0.250 && ifLoopCount == 2) { // Elapsed time 250
                encoderDrive(0.2, -8.5, -8.5); // Moves the robot 7-ish inches backwards at 40% speed
                //ifLoopCount++;
            }
            if (runtime.time() >= 0.300 && ifLoopCount == 3) { // Elapsed time 300
                // Run the carousel spinner
                CSpinner.setPower(-0.5);
                encoderDrive(0.035, -4, -4);
                //runtime.reset();
                //ifLoopCount++;
            }
            if (runtime.time() >= 2.500 && ifLoopCount == 4) {
                CSpinner.setPower(0);
                encoderDrive(0.2, 1, 1);
                //ifLoopCount++;
            }
            if (runtime.time() >= 0.15 && ifLoopCount == 5) { // Turns towards depot
                encoderDrive(0.15, 10, -10);
            }
            if (runtime.time() >= 0.25 && ifLoopCount == 6) {
                encoderDrive(0.2, 7.5, 7.5);
            }
            /*
            if (runtime.time() >= 0.250 && ifLoopCount == 7) {
                Err.setPowerCRServo(intakeLeft, 1.0);
                Err.setPowerCRServo(intakeRight, -1.0);
                ifLoopCount++;
            }
            if (runtime.time() >= 1.5 && ifLoopCount == 8) {
                Err.setPowerCRServo(intakeLeft, 0);
                Err.setPowerCRServo(intakeRight, 0);
                ifLoopCount++;
            }
            */




            /*
            if (runtime.time() >= 0.300 && ifLoopCount == 5) { // Elapsed time 300 (guessed)
                encoderDrive(0.2, 2, -2);
                //ifLoopCount++;
            }
            if (runtime.time() >= 0.250 && ifLoopCount == 6) { // Elapsed time 250
                encoderDrive(0.1, 2, 2); // Needs to go farther?
                //ifLoopCount++;
            }
            if (ifLoopCount == 7) {
                // Run code to drop cube into level
                Err.setPowerCRServo(intakeLeft, -1.0);
                Err.setPowerCRServo(intakeRight, 1.0);
                runtime.reset();
                ifLoopCount++;
            }
            if (runtime.time() >= 1.250 && ifLoopCount == 8) { // Elapsed time 350 (guessed)
                encoderDrive(0.3, -3, -3);
                //ifLoopCount++;
            }
            if (runtime.time() >= 0.250 && ifLoopCount == 9) { // Elapsed time 250
                encoderDrive(0.3, -5, 5);
                //ifLoopCount++;
            }
            if (runtime.time() >= 0.250 && ifLoopCount == 10) { // Elapsed time 250
                encoderDrive(0.7, 15, 15);
                //ifLoopCount++;
            }*/
            telemetry.addData("L", Math.round(Math.abs(fL.getCurrentPosition() + bL.getCurrentPosition()))/2 + " R: " + Math.round(Math.abs(fR.getCurrentPosition() + bR.getCurrentPosition()))/2 + " Target: " + leftFrontTarget);
            telemetry.addData("Runtime", "Runtime: " + runtime.toString());
            telemetry.addData("Loop", ifLoopCount);
            telemetry.addData("Pos", "fL: " + fL.getCurrentPosition() + " fR: " + fR.getCurrentPosition() + " bL: " + bL.getCurrentPosition() + " bR: " + bR.getCurrentPosition() + " Target: " + leftFrontTarget + ", " + rightFrontTarget);

            if(((Math.round(Math.abs(fL.getCurrentPosition() + bL.getCurrentPosition()))/2) >= Math.abs(leftFrontTarget))) {
                telemetry.addData("LeftTargetReached: ", "True" + " " + ((Math.round(Math.abs(fL.getCurrentPosition() + bL.getCurrentPosition()))/2)));
            } else {
                telemetry.addData("LeftTargetReached: ", "False" + " " + ((Math.round(Math.abs(fL.getCurrentPosition() + bL.getCurrentPosition()))/2)));
            }
            if(Math.round(Math.abs(bR.getCurrentPosition()) + 200) >= Math.abs(rightBackTarget)) {
                telemetry.addData("RightTargetReached: ", "True" + " " + Math.round(Math.abs(bR.getCurrentPosition())));
            } else {
                telemetry.addData("RightTargetReached: ", "False" + " " + Math.round(Math.abs(bR.getCurrentPosition())));
            }
            telemetry.addData("If", ififrun);
        } else {
            telemetry.addData("Duck", "Duck: Right");
            // Run code if duck is on the right
            // Don't say to use a switch-case. I can customize this code better.
            // The inches are not inches due to the robot rolling forward or drifting. (unless you go really really slow)


            if (ifLoopCount == 0) {
                //encoderDrive( 0.15, -12, 12); // Test line of code.
                encoderDrive(0.15, 3, 3); // Moves the robot 1.5-ish inches forward at 15% speed
            }
            if (runtime.time() >= 0.500 && ifLoopCount == 1) { // Elapsed time 500 milliseconds
                encoderDrive(0.15, -5, 5); // Turns the robot left at 15% speed
                //ifLoopCount++;
            }
            if (runtime.time() >= 0.250 && ifLoopCount == 2) { // Elapsed time 250
                encoderDrive(0.2, -8.5, -8.5); // Moves the robot 7-ish inches backwards at 40% speed
                //ifLoopCount++;
            }
            if (runtime.time() >= 0.300 && ifLoopCount == 3) { // Elapsed time 300
                // Run the carousel spinner
                CSpinner.setPower(-0.5);
                encoderDrive(0.035, -4, -4);
                //runtime.reset();
                //ifLoopCount++;
            }
            if (runtime.time() >= 2.500 && ifLoopCount == 4) {
                CSpinner.setPower(0);
                encoderDrive(0.2, 1, 1);
                //ifLoopCount++;
            }
            if (runtime.time() >= 0.15 && ifLoopCount == 5) { // Turns towards depot
                encoderDrive(0.15, 10, -10);
            }
            if (runtime.time() >= 0.25 && ifLoopCount == 6) {
                encoderDrive(0.2, 7.5, 7.5);
            }
            /*
            if (runtime.time() >= 0.250 && ifLoopCount == 7) {
                Err.setPowerCRServo(intakeLeft, 1.0);
                Err.setPowerCRServo(intakeRight, -1.0);
                ifLoopCount++;
            }
            if (runtime.time() >= 1.5 && ifLoopCount == 8) {
                Err.setPowerCRServo(intakeLeft, 0);
                Err.setPowerCRServo(intakeRight, 0);
                ifLoopCount++;
            }
            */




            /*
            if (runtime.time() >= 0.300 && ifLoopCount == 5) { // Elapsed time 300 (guessed)
                encoderDrive(0.2, 2, -2);
                //ifLoopCount++;
            }
            if (runtime.time() >= 0.250 && ifLoopCount == 6) { // Elapsed time 250
                encoderDrive(0.1, 2, 2); // Needs to go farther?
                //ifLoopCount++;
            }
            if (ifLoopCount == 7) {
                // Run code to drop cube into level
                Err.setPowerCRServo(intakeLeft, -1.0);
                Err.setPowerCRServo(intakeRight, 1.0);
                runtime.reset();
                ifLoopCount++;
            }
            if (runtime.time() >= 1.250 && ifLoopCount == 8) { // Elapsed time 350 (guessed)
                encoderDrive(0.3, -3, -3);
                //ifLoopCount++;
            }
            if (runtime.time() >= 0.250 && ifLoopCount == 9) { // Elapsed time 250
                encoderDrive(0.3, -5, 5);
                //ifLoopCount++;
            }
            if (runtime.time() >= 0.250 && ifLoopCount == 10) { // Elapsed time 250
                encoderDrive(0.7, 15, 15);
                //ifLoopCount++;
            }*/
            telemetry.addData("L", Math.round(Math.abs(fL.getCurrentPosition() + bL.getCurrentPosition()))/2 + " R: " + Math.round(Math.abs(fR.getCurrentPosition() + bR.getCurrentPosition()))/2 + " Target: " + leftFrontTarget);
            telemetry.addData("Runtime", "Runtime: " + runtime.toString());
            telemetry.addData("Loop", ifLoopCount);
            telemetry.addData("Pos", "fL: " + fL.getCurrentPosition() + " fR: " + fR.getCurrentPosition() + " bL: " + bL.getCurrentPosition() + " bR: " + bR.getCurrentPosition() + " Target: " + leftFrontTarget + ", " + rightFrontTarget);

            if(((Math.round(Math.abs(fL.getCurrentPosition() + bL.getCurrentPosition()))/2) >= Math.abs(leftFrontTarget))) {
                telemetry.addData("LeftTargetReached: ", "True" + " " + ((Math.round(Math.abs(fL.getCurrentPosition() + bL.getCurrentPosition()))/2)));
            } else {
                telemetry.addData("LeftTargetReached: ", "False" + " " + ((Math.round(Math.abs(fL.getCurrentPosition() + bL.getCurrentPosition()))/2)));
            }
            if(Math.round(Math.abs(bR.getCurrentPosition()) + 200) >= Math.abs(rightBackTarget)) {
                telemetry.addData("RightTargetReached: ", "True" + " " + Math.round(Math.abs(bR.getCurrentPosition())));
            } else {
                telemetry.addData("RightTargetReached: ", "False" + " " + Math.round(Math.abs(bR.getCurrentPosition())));
            }
            telemetry.addData("If", ififrun);
        }
    }

    public void encoderDrive(double speed, double leftInches, double rightInches) {
        if(!runOnce) {
            leftFrontTarget = 0;
            rightFrontTarget = 0;
            leftBackTarget = 0;
            rightBackTarget = 0;

            leftFrontTarget = /*fL.getCurrentPosition() +*/ (int) (leftInches * TICKS_PER_INCH);
            rightFrontTarget = /*fR.getCurrentPosition() +*/ (int) (rightInches * TICKS_PER_INCH);
            leftBackTarget = /*bL.getCurrentPosition() +*/ (int) (leftInches * TICKS_PER_INCH);
            rightBackTarget = /*bR.getCurrentPosition() +*/ (int) (rightInches * TICKS_PER_INCH);

            // Telling the motors how many ticks I want them to go and then to stop
            if(leftBackTarget < 0) {
                bL.setTargetPosition(leftBackTarget - (int) TICKS_PER_REV);
                fL.setTargetPosition(leftFrontTarget - (int) TICKS_PER_REV);
            } else {
                bL.setTargetPosition(leftBackTarget + (int) TICKS_PER_REV);
                fL.setTargetPosition(leftFrontTarget + (int) TICKS_PER_REV);
            }

            if(rightBackTarget < 0) {
                fR.setTargetPosition(rightFrontTarget - (int) TICKS_PER_REV);
                bR.setTargetPosition(rightBackTarget - (int) TICKS_PER_REV);
            } else {
                fR.setTargetPosition(rightFrontTarget + (int) TICKS_PER_REV);
                bR.setTargetPosition(rightBackTarget + (int) TICKS_PER_REV);
            }

            // Changes motor mode to "run to position"
            //fR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fL.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // Sending power to motors
            if(rightInches < 0) {
                fR.setPower(-speed);
            } else {
                fR.setPower(speed);
            }
            bR.setPower(speed);
            bL.setPower(speed);
            fL.setPower(speed);
            runOnce = true;
        }

        if(((Math.round(Math.abs(fL.getCurrentPosition() + bL.getCurrentPosition()))/2) >= Math.abs(leftFrontTarget))/*((Math.round(Math.abs(fL.getCurrentPosition() + bL.getCurrentPosition()))/2) >= Math.abs(leftFrontTarget)) && Math.round(Math.abs(bR.getCurrentPosition())) >= Math.abs(rightBackTarget)*/) {
            if (Math.round(Math.abs(bR.getCurrentPosition())) >= Math.abs(rightBackTarget)) {
                // Takes the average, absolute value of both sides and makes sure one of them is above their target position
                // Stops the motors after they reach the position
                fR.setPower(0);
                bR.setPower(0);
                fL.setPower(0);
                bL.setPower(0);

                ififrun = true;
                // Changed the motor type to "run using encoders"
                //fR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                bR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                bL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                fL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                // Resets encoder ticks
                fR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                bR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                bL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                fL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                runtime.reset();
                runOnce = false;
                ifLoopCount++;
            }
        }
    }
}