package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class MotorTest extends OpMode {
    DcMotor onlyMotor = null;
    DcMotor onlyOtherMotor = null;
    CRServo intakeLeft = null;
    CRServo intakeRight = null;
    public void init() {
        onlyMotor = hardwareMap.dcMotor.get("Front Right Drive");
        onlyOtherMotor = hardwareMap.dcMotor.get("Back Right Drive");
        intakeLeft = hardwareMap.crservo.get("Intake Left");
        intakeRight = hardwareMap.crservo.get("Intake Right");

        //onlyMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //onlyOtherMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        onlyMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        onlyOtherMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void loop() {
        onlyMotor.setPower(gamepad1.right_stick_y);
        onlyOtherMotor.setPower(gamepad1.right_stick_y);

        if(gamepad1.right_bumper) { // Intake forwards
            intakeLeft.setPower(1.0);
            intakeRight.setPower(-1.0);
        } else if (gamepad1.right_trigger != 0) {
            intakeLeft.setPower(-1.0);
            intakeRight.setPower(1.0);
        } else {
            intakeLeft.setPower(0);
            intakeRight.setPower(0);
        }
    }
}