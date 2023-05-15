package org.firstinspires.ftc.teamcode;

import android.content.Context;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name="FreightFrenzy", group="Opmode") // Puts the program under the OpMode arrow on the app
//@Disabled    Disables the program by removing from OpMode list on the DS
public class FreightFrenzy extends OpMode {

    // Declare OpMode members.
    //public ElapsedTime runtime = new ElapsedTime();
    public DcMotor front_Left_Drive = null;
    public DcMotor back_Left_Drive = null;
    public DcMotor front_Right_Drive = null;
    public DcMotor back_Right_Drive = null;
    public DcMotor LinearSlideLeft = null;
    public DcMotor LinearSlideRight = null;
    public DcMotor CSpinner = null;
    public boolean lSED = false;
    public boolean sloMode = false;
    public int sloModePower = 0;
    public int linearSlideLeftLastPos = 0;
    public int linearSlideRightLastPos = 0;
    public CRServo intakeLeft = null;
    public CRServo intakeRight = null;
    public boolean intakeToggle = false;
    public boolean isReversed = false; // Reverses Linear Slides
    public String songsfx = "ss_roger_roger";
    public boolean soundPlaying = false;
    public int soundID = -1;
    public boolean hasRun = false;
    public ElapsedTime intakeRuntime = new ElapsedTime(); // For intake cooldown
    public ElapsedTime isReversedRuntime = new ElapsedTime(); // For reversing the slides

    // Linear Slide Vars (remove 2 zeros)
    public int maxPos = -2300000; // Decrease this (or increase ignoring the negative sign) to make the slide go higher
    public int minPos = 300000;// Increase this to make the slide lower farther (go to far and it goes back up)
    public int reverseMaxPos = maxPos * -1; // Swaps to the other sign. (Almost like absolute value)
    public int reverseMinPos = minPos * -1;
    public int maxPosOffset = 0; // These will keep the boundaries the same when the motors are recalibrated
    public int minPosOffset = 0;
    public int motorAverage = 0;
    // End of Linear Slide Vars

    Err Err = new Err(telemetry); // Runs Err.java


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {

        Err.init(this); // Takes THIS opmode and passes it to the method init
        Err.telemetryUpdate(); // Makes telemetry

        //Context myApp = hardwareMap.appContext;

        /*
        SoundPlayer.PlaySoundParams params = new SoundPlayer.PlaySoundParams();
        params.loopControl = 0;
        params.waitForNonLoopingSoundsToFinish = true;
         */


        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        front_Left_Drive = Err.hardwareMapDc("Front Left Drive");
        back_Left_Drive = Err.hardwareMapDc("Back Left Drive");
        front_Right_Drive = Err.hardwareMapDc("Front Right Drive");
        back_Right_Drive = Err.hardwareMapDc("Back Right Drive");
        intakeLeft = Err.hardwareMapCRServo("Intake Left");
        intakeRight = Err.hardwareMapCRServo("Intake Right");
        LinearSlideLeft = Err.hardwareMapDc("Linear Slide Left");
        LinearSlideRight = Err.hardwareMapDc("Linear Slide Right");
        CSpinner = Err.hardwareMapDc("Carousel Spinner");

        // Most robots need the motor on one side to be reversed to drive forward
        Err.setDirection(front_Left_Drive, "REVERSE");
        Err.setDirection(back_Left_Drive, "REVERSE");
        Err.setDirection(front_Right_Drive, "FORWARD");
        Err.setDirection(back_Right_Drive, "FORWARD");
        Err.setDirection(LinearSlideLeft, "FORWARD");
        Err.setDirection(LinearSlideRight, "REVERSE");
        Err.setDirection(CSpinner, "FORWARD");

        front_Left_Drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //front_Right_Drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_Left_Drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_Right_Drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LinearSlideLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LinearSlideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        front_Left_Drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //front_Right_Drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        back_Left_Drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        back_Right_Drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LinearSlideLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LinearSlideRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        sloModePower = 4; // SloMode power divide number
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        // Setup a variable for each drive wheel to save power level for telemetry

        //Err.deadMotorCheck(front_Left_Drive, back_Left_Drive);
        //Err.deadMotorCheck(front_Right_Drive, back_Right_Drive);

        double leftPower;
        double rightPower;
        //double linerSlide;

        double drive = -gamepad1.left_stick_y;
        double turn  =  gamepad1.right_stick_x;
        leftPower    = Range.clip(drive + turn, -1.0, 1.0);
        rightPower   = Range.clip(drive - turn, -1.0, 1.0);

        //linerSlide = Range.clip(gamepad2.left_stick_y, -1.0, 1.0);

        /*
        if(gamepad1.left_bumper) { // Intake forwards
            Err.setPowerCRServo(intakeLeft, 1.0);
            Err.setPowerCRServo(intakeRight, -1.0);
            //intakeLeft.setPower(1.0);
            //intakeRight.setPower(-1.0);
        } /*else {
            //Err.setPowerCRServo(intakeLeft, 0);
            //Err.setPowerCRServo(intakeRight, 0);
            intakeLeft.setPower(0);
            intakeRight.setPower(0);
        }*/
        
        if(gamepad1.right_bumper) { // Intake forwards
            Err.setPowerCRServo(intakeLeft, 1.0);
            Err.setPowerCRServo(intakeRight, -1.0);
        } else if (gamepad1.right_trigger != 0) {
            Err.setPowerCRServo(intakeLeft, -1.0);
            Err.setPowerCRServo(intakeRight, 1.0);
        } else {
            Err.setPowerCRServo(intakeLeft, 0);
            Err.setPowerCRServo(intakeRight, 0);
        }


        /* Toggle intake code
        if(gamepad1.right_bumper && intakeRuntime.time() >= 1.00) { // Intake toggle
            if (intakeToggle) {
                Err.setPowerCRServo(intakeLeft, -1.0);
                Err.setPowerCRServo(intakeRight, 1.0);
                intakeToggle = false;
            } else {
                Err.setPowerCRServo(intakeLeft, 1.0);
                Err.setPowerCRServo(intakeRight, -1.0);
                intakeToggle = true;
            }
            intakeRuntime.reset();
        }
        */

        telemetry.addData("intakeRuntime", Math.round(intakeRuntime.time()) + " isReversedRuntime: " + Math.round(isReversedRuntime.time()));

        Err.setPowerDc(CSpinner, gamepad2.right_stick_x);
        telemetry.addData("CSpinner", gamepad2.right_stick_x);
        /*
        if(gamepad1.right_bumper) { // Intake backwards
            Err.setPowerCRServo(intakeLeft, -1.0);
            Err.setPowerCRServo(intakeRight, 1.0);
            //intakeLeft.setPower(-1.0);
            //intakeRight.setPower(1.0);
        } /*else {
            //Err.setPowerCRServo(intakeLeft, 0);
            //Err.setPowerCRServo(intakeRight, 0);
            intakeLeft.setPower(0);
            intakeRight.setPower(0);
        }*/

        sloMode = !gamepad1.left_bumper; // SloMode is true when bumper is false vice versa

        if(gamepad1.dpad_up && !soundPlaying) {
            Context myApp = hardwareMap.appContext;

            SoundPlayer.PlaySoundParams params = new SoundPlayer.PlaySoundParams();
            params.loopControl = 0;
            params.waitForNonLoopingSoundsToFinish = true;

            telemetry.addData("", "Has played");

            if ((soundID = myApp.getResources().getIdentifier(songsfx, "raw", myApp.getPackageName())) != 0){

                // Signal that the sound is now playing.
                soundPlaying = true;

                // Start playing, and also Create a callback that will clear the playing flag when the sound is complete.
                SoundPlayer.getInstance().startPlaying(myApp, soundID, params, null,
                        new Runnable() {
                            public void run() {
                                soundPlaying = false;
                            }} );
            }
        }

        // Send calculated power to wheels
        if(sloMode) {
            Err.setPowerDc(front_Left_Drive, leftPower / sloModePower);
            Err.setPowerDc(back_Left_Drive, leftPower / sloModePower);
            Err.setPowerDc(front_Right_Drive, rightPower / sloModePower);
            Err.setPowerDc(back_Right_Drive, rightPower / sloModePower);
        } else {
            Err.setPowerDc(front_Left_Drive, leftPower);
            Err.setPowerDc(back_Left_Drive, leftPower);
            Err.setPowerDc(front_Right_Drive, rightPower);
            Err.setPowerDc(back_Right_Drive, rightPower);
        }
        //Err.setPowerDc(LinearSlideLeft, linerSlide);
        //Err.setPowerDc(LinearSlideRight, linerSlide);
        if(gamepad2.x && isReversedRuntime.time() >= 2.00) {
            isReversed = !isReversed; /*Toggles to opposite value*/
            isReversedRuntime.reset();
        }

        linearSlideEncoderDrive(LinearSlideLeft, LinearSlideRight, gamepad2.dpad_down, gamepad2.dpad_up);

        //telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
        telemetry.addData("SlidePos", "Left: " + LinearSlideLeft.getCurrentPosition() + " Right: " + LinearSlideRight.getCurrentPosition() + " Reversed: " + isReversed);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

    // This method will make the linear slides stop at the limits
    private void linearSlideEncoderDrive(DcMotor leftMotor, DcMotor rightMotor, boolean slideUp, boolean slideDown) {
        double speed = 1.0;
        reverseMaxPos = maxPos * -1; // Swaps to the other sign. (Almost like absolute value)
        reverseMinPos = minPos * -1;

        telemetry.addData("maxPos", maxPos + " minPos: " + minPos + " reverseMaxPos: " + reverseMaxPos + " reverseMinPos: " + reverseMinPos);
        telemetry.addData("RightLastPos", linearSlideRightLastPos + " LeftLastPos: " + linearSlideLeftLastPos);
        telemetry.addData("maxOffset", maxPosOffset + " minOffset: " + minPosOffset);
        if(isReversed) {
            if (slideUp) {
                Err.setTargetPosition(leftMotor, linearSlideLeftLastPos + reverseMaxPos);
                Err.setTargetPosition(rightMotor, linearSlideRightLastPos + reverseMaxPos);
                Err.setPowerDc(leftMotor, -speed);
                Err.setPowerDc(rightMotor, -speed);
            } else if (slideDown) {
                Err.setTargetPosition(leftMotor, linearSlideLeftLastPos + reverseMinPos);
                Err.setTargetPosition(rightMotor, linearSlideRightLastPos + reverseMinPos);
                Err.setPowerDc(leftMotor, -speed);
                Err.setPowerDc(rightMotor, -speed);
            } else { // If joystick is at rest
                Err.setTargetPosition(leftMotor, linearSlideLeftLastPos + reverseMinPos);
                Err.setTargetPosition(rightMotor, linearSlideRightLastPos + reverseMinPos);
                Err.setPowerDc(leftMotor, 0);
                Err.setPowerDc(rightMotor, 0);
            }
        }else {
            if (slideUp) {
                Err.setTargetPosition(leftMotor, linearSlideLeftLastPos + maxPos);
                Err.setTargetPosition(rightMotor, linearSlideRightLastPos + maxPos);
                Err.setPowerDc(leftMotor, speed);
                Err.setPowerDc(rightMotor, speed);
            } else if (slideDown) {
                Err.setTargetPosition(leftMotor, linearSlideLeftLastPos + minPos);
                Err.setTargetPosition(rightMotor, linearSlideRightLastPos + minPos);
                Err.setPowerDc(leftMotor, speed);
                Err.setPowerDc(rightMotor, speed);
            } else { // If joystick is at rest
                Err.setTargetPosition(leftMotor, linearSlideLeftLastPos + minPos);
                Err.setTargetPosition(rightMotor, linearSlideRightLastPos + minPos);
                Err.setPowerDc(leftMotor, 0);
                Err.setPowerDc(rightMotor, 0);
            }
        }

        // If I made this one if statement then it would be hard to read
        /*
        if(leftMotor.getCurrentPosition() != 0 && rightMotor.getCurrentPosition() != 0) {
            if (!(leftMotor.isBusy()) && !(rightMotor.isBusy()) && !slideUp && !slideDown) { // Makes sure the motors are not moving
                if ((leftMotor.getCurrentPosition() >= 10 && rightMotor.getCurrentPosition() >= 10) || (leftMotor.getCurrentPosition() <= -10 && rightMotor.getCurrentPosition() <= -10))
                    motorAverage = (Math.round(leftMotor.getCurrentPosition() + rightMotor.getCurrentPosition()) / 2);
                if (motorAverage > 0) {
                    maxPosOffset = motorAverage - maxPos;
                    minPosOffset = motorAverage - minPos;
                } else {
                    maxPosOffset = motorAverage + maxPos;
                    minPosOffset = motorAverage + minPos;
                }
                maxPos = maxPos + maxPosOffset;
                minPos = minPos + minPosOffset;
            /*
            What this code does:
            Because of encoder drifting, whenever the motors are not running and are not at 0,
            it resets the encoders and moves the upper and lower boundary so the boundaries are the same
            even though the encoders are now 0 and the slides are slightly raised.


            Upper Boundary, 2000  ----

            Current pos, 500       --
            Home,           0     ----

            Lower Boundary, -500  ----


            If the slides are raised a little, then it removes 500 from the boundaries and resets the encoder


            Upper Boundary, 1500  ----

            Current Pos, 0         --
            (Previous Home) -500  ----

            Lower Boundary, -1000 ----
             *//*
                leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }
        }
        */


        /* if(leftMotor.getCurrentPosition() >= linearSlideLeftLastPos + -5 || rightMotor.getCurrentPosition() >= linearSlideRightLastPos + -5) {
            leftMotor.setPower(0);
            rightMotor.setPower(0);
            leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        } */
        if(!lSED) { // Only sets the motor run mode to run to position once.
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            linearSlideLeftLastPos = leftMotor.getCurrentPosition();
            linearSlideRightLastPos = rightMotor.getCurrentPosition();
            lSED = true; // "Has run once?" boolean
        }
    }
}
