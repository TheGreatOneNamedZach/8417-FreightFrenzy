package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

/**
 * This file is to help someone learn Autonomous and to be the template for Autonomous files.
 */

@Autonomous(name="MyFirstAutoMode", group="Autonomous")
@Disabled
public class    MyFirstAutoMode extends LinearOpMode {

    /* Declare OpMode members */
    HardwarePushbot robot = new HardwarePushbot();   // Use a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();

    /* Declare variables */
    static final double DRIVE_SPEED = 1; // Motor power

    @Override
    public void runOpMode() {
        /* When Initialized: */
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        /* Declares motors */
        DcMotor front_left = hardwareMap.get(DcMotor.class, "front_left");
        DcMotor back_left = hardwareMap.get(DcMotor.class, "back_left");
        DcMotor front_right = hardwareMap.get(DcMotor.class, "front_right");
        DcMotor back_right = hardwareMap.get(DcMotor.class, "back_right");

        /* Sets the direction of the motors */
        front_left.setDirection(DcMotor.Direction.FORWARD);
        back_left.setDirection(DcMotor.Direction.REVERSE);
        front_right.setDirection(DcMotor.Direction.FORWARD);
        back_right.setDirection(DcMotor.Direction.REVERSE);

        waitForStart(); // Waits for activation of driver mode

        /* When Driver Mode is activated: */
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.35)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();

            /* Sets the motor's direction to prepare for Driver Control */
            /* Turns the robot */
            front_left.setPower(DRIVE_SPEED);
            back_left.setPower(-DRIVE_SPEED);
            front_right.setPower(-DRIVE_SPEED);
            back_right.setPower(DRIVE_SPEED);
        }

        /* Turns off the motors by setting their power to 0 */
        front_left.setPower(0);
        back_left.setPower(0);
        front_right.setPower(0);
        back_right.setPower(0);
    }
}