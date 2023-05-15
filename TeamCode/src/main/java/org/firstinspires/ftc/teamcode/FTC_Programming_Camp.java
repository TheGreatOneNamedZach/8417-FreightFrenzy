package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.Arrays;

@Disabled
@TeleOp
public class FTC_Programming_Camp extends OpMode {
    DcMotor fL = null;
    DcMotor fR = null;
    DcMotor bL = null;
    DcMotor bR = null;

    @Override
    public void init() {
        fL = hardwareMap.get(DcMotor.class, "Front Left");
        fR = hardwareMap.get(DcMotor.class, "Front Right");
        bL = hardwareMap.get(DcMotor.class, "Back Left");
        bR = hardwareMap.get(DcMotor.class, "Back Right");

        fL.setDirection(DcMotorSimple.Direction.REVERSE);
        fR.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        setPowerMecanum(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);


    }

    public void setPowerMecanum(double x, double y, double rot) { // rot = rotation
        // Calculating the motor powers
        double frontLeftMotorPower = y - x - rot;
        double frontRightMotorPower = y + x + rot;
        double backLeftMotorPower = y + x - rot;
        double backRightMotorPower = y - x + rot;

        double motorPowers[] = {
                Math.abs(frontLeftMotorPower),
                Math.abs(frontRightMotorPower),
                Math.abs(backLeftMotorPower),
                Math.abs(backRightMotorPower)
        };

        Arrays.sort(motorPowers);

        if(motorPowers[3] != 0) {
            frontLeftMotorPower = frontLeftMotorPower / motorPowers[3];
            frontRightMotorPower = frontRightMotorPower / motorPowers[3];
            backLeftMotorPower = backLeftMotorPower / motorPowers[3];
            backRightMotorPower = backRightMotorPower / motorPowers[3];
        }

        fL.setPower(frontLeftMotorPower);
        fR.setPower(frontRightMotorPower);
        bL.setPower(backLeftMotorPower);
        bR.setPower(backRightMotorPower);
    }

}
