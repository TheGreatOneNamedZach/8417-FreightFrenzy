package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/** Welcome to Err! (If you want to learn how to use the code read this!)
   "If you never see this code in action, then the programmer(s)
   that use this code did their job well." - Programmer on 8417

   This code will prevent the following:
   - Hardware mapping errors with DcMotors
   - Hardware mapping errors with Servos
   - Hardware mapping errors with CRServos
   - Hardware mapping errors with webcam
   It will notify you of the errors above when they occur.

   This code will also notify you when:
   - One of two motors have not turned as much as the other. (Using encoders)

   How to use:
   1. First you need to add the following code to your robot.
       In the main class (it extends OpMode):
       ////////////////////////////////////////
       Err Err = new Err(telemetry); // This is a "constructor" and required to use any of Err.
       ////////////////////////////////////////

       In the public void init() (This is the normal init() not the init loop):
       ////////////////////////////////////////
       Err.init(this); // This line of code is required for hardware mapping for Err.
       Err.telemetryUpdate(); // This line of code is required for telemetry output for Err.
       ////////////////////////////////////////

   2. Change the following lines of code. (Part 1)
       In the following example, variable names and strings are substituted
       by a line of letters. The "original" code is what the code normally
       looks like without using Err. If there is an "OR" separating parts
       parts of the code, either version can be replaced.
       The "changed" code is what the code should look like when using Err.
       Example:
       ////////////////////////////////////////
       Original:
       XXXXXXXX = hardwareMap.get(DcMotor.class, "YYYYYYYYY");
       OR
       XXXXXXXX = hardwareMap.get(DcMotorSimple.class, "YYYYYYYYY"); (This line of code does not work and is purely an example)
       Changed:
       XXXXXXXX = Err.hardwareMapDc("YYYYYYYYY");
       ////////////////////////////////////////

       Hardware Mapping A Dc Motor:
       ////////////////////////////////////////
       Original:
       XXXXXXXX = hardwareMap.get(DcMotor.class, "YYYYYYYYY");
       Changed:
       XXXXXXXX = Err.hardwareMapDc("YYYYYYYYY");
       ////////////////////////////////////////

       Hardware Mapping A Servo:
       ////////////////////////////////////////
       Original:
       XXXXXXXX = hardwareMap.servo.get("YYYYYYYYY");
       Changed:
       XXXXXXXX = Err.hardwareMapServo("YYYYYYYYY");
       ////////////////////////////////////////

       Hardware Mapping A Continuous Rotation Servo (CRServo):
       ////////////////////////////////////////
       Original:
       XXXXXXXX = hardwareMap.get(CRServo.class, "YYYYYYYYY");
       OR
       If your line of code looks different, it still may work.
       Changed:
       XXXXXXXX = Err.hardwareMapCRServo("YYYYYYYYY");
       ////////////////////////////////////////

   3. Change the following lines of code. (Part 2)
        These lines of code need to be changed for
        EVERY time one of your motors hardware mapped
        above is used. The reason for this is because
        if the code throws an error trying to hardware
        map, it will crash the moment it tries to move,
        set direction, set mode, etc. unless you change
        the code to the Err version.

        TL;DR
        If it does not throw an error trying to hardware
        map, the original code will work just fine
        If it does throw an error, the code will crash.

        Dc Motor "set Direction":
        ////////////////////////////////////////
        Original:
        XXXXXXXX.setDirection(DcMotor.Direction.YYYYYYYYY);
        Changed:
        Err.setDirection(XXXXXXXX, "YYYYYYYYY");
        ////////////////////////////////////////





   Made with love by FTC team 8417
*/

public class Err {

    public int errorCount = 0; // "O" "1" or "2"
    public int errorType = 0; // The UUID of an error
    public String lastErrorMsg = "INFO: Initialize";

    OpMode opMode; // Runs OpMode?
    HardwareMap hardwareMap; // Runs HardwareMap?

    Telemetry telemetry; // Runs Telemetry?
    public Err(Telemetry extendedTelemetry) {
        telemetry = extendedTelemetry;
    }

    public void init(OpMode opMode){ // Takes the opmode
        this.opMode = opMode; // Makes the ran telemetry var equal the opmode passed to this method
        init2(opMode.hardwareMap);
    }

    // Initialize standard Hardware interfaces
    public void init2(HardwareMap hardwareMap) {
        // Save reference to Hardware map
        this.hardwareMap = hardwareMap;
    }

    public void telemetryUpdate() { // Shows the error
        if(errorType < 10) {
            telemetry.clearAll();
            telemetry.addData("Error1", "(" + errorCount + "0" + errorType + ") " + lastErrorMsg); // Shows the error
        } else {
            telemetry.clearAll();
            telemetry.addData("Error1", "(" + errorCount + errorType + ") " + lastErrorMsg); // Shows the error
        }
    } // 00

    public DcMotor hardwareMapDc(String deviceName) {
        DcMotor tempDcMotor = null; // Makes a temporary DcMotor variable
        int errorNum = 1;
        String errorMsg = "ERR: Hardware Map";
        try {
            tempDcMotor = hardwareMap.get(DcMotor.class, deviceName); // Tries to hardware map
        } catch (Exception e) {
            if (errorCount == 0) {
                errorCount = 1;
                errorType = errorNum;
                lastErrorMsg = errorMsg;
            }
            if (errorType != errorNum) { // This will keep it from counting itself as 2+ errors if run multiple times in a loop
                if (errorCount == 1) { // This tells you if there is zero, one, or multiple errors
                    errorCount = 2;
                }
            }
            if (errorType > errorNum) { // This will show the most critical error if there is multiple. "01" is the most critical, "99" is the least
                errorType = errorNum; // This will change the error message and number only if this is the most critical
                lastErrorMsg = errorMsg; // Only changes the error message when a more critical error is discovered
            }
            if(errorType < 10) { // Yes this is unnecessary but I copy and paste this code so it eventually will be needed.
                telemetry.clearAll();
                telemetry.addData("Error1", "(" + errorCount + "0" + errorType + ") " + lastErrorMsg); // Shows the error
            } else {
                telemetry.clearAll();
                telemetry.addData("Error1", "(" + errorCount + errorType + ") " + lastErrorMsg); // Shows the error
            }
        }
        return (tempDcMotor);
    } // 01

    public Servo hardwareMapServo(String deviceName) {
        Servo tempServoMotor = null; // Makes a temporary DcMotor variable
        int errorNum = 2;
        String errorMsg = "ERR: Hardware Map Servo";
        try {
            tempServoMotor = hardwareMap.get(Servo.class, deviceName); // Tries to hardware map
        } catch (Exception e) {
            if (errorCount == 0) {
                errorCount = 1;
                errorType = errorNum;
                lastErrorMsg = errorMsg;
            }
            if (errorType != errorNum) { // This will keep it from counting itself as 2+ errors if run multiple times in a loop
                if (errorCount == 1) { // This tells you if there is zero, one, or multiple errors
                    errorCount = 2;
                }
            }
            if (errorType > errorNum) { // This will show the most critical error if there is multiple. "01" is the most critical, "99" is the least
                errorType = errorNum; // This will change the error message and number only if this is the most critical
                lastErrorMsg = errorMsg; // Only changes the error message when a more critical error is discovered
            }
            if(errorType < 10) { // Yes this is unnecessary but I copy and paste this code so it eventually will be needed.
                telemetry.clearAll();
                telemetry.addData("Error1", "(" + errorCount + "0" + errorType + ") " + lastErrorMsg); // Shows the error
            } else {
                telemetry.clearAll();
                telemetry.addData("Error1", "(" + errorCount + errorType + ") " + lastErrorMsg); // Shows the error
            }
        }
        return (tempServoMotor);
    } // 02

    public CRServo hardwareMapCRServo(String deviceName) {
        CRServo tempCRServoMotor = null; // Makes a temporary DcMotor variable
        int errorNum = 3;
        String errorMsg = "ERR: Hardware Map CR Servo";
        try {
            tempCRServoMotor = hardwareMap.get(CRServo.class, deviceName); // Tries to hardware map
        } catch (Exception e) {
            if (errorCount == 0) {
                errorCount = 1;
                errorType = errorNum;
                lastErrorMsg = errorMsg;
            }
            if (errorType != errorNum) { // This will keep it from counting itself as 2+ errors if run multiple times in a loop
                if (errorCount == 1) { // This tells you if there is zero, one, or multiple errors
                    errorCount = 2;
                }
            }
            if (errorType > errorNum) { // This will show the most critical error if there is multiple. "01" is the most critical, "99" is the least
                errorType = errorNum; // This will change the error message and number only if this is the most critical
                lastErrorMsg = errorMsg; // Only changes the error message when a more critical error is discovered
            }
            if(errorType < 10) { // Yes this is unnecessary but I copy and paste this code so it eventually will be needed.
                telemetry.clearAll();
                telemetry.addData("Error1", "(" + errorCount + "0" + errorType + ") " + lastErrorMsg); // Shows the error
            } else {
                telemetry.clearAll();
                telemetry.addData("Error1", "(" + errorCount + errorType + ") " + lastErrorMsg); // Shows the error
            }
        }
        return (tempCRServoMotor);
    } // 03

    public void setDirection(DcMotor deviceName, String direction) {
        int errorNum = 4;
        String errorMsg = "ERR: Set Direction";
        try {
            if(direction.equalsIgnoreCase("FORWARD")) {
                deviceName.setDirection(DcMotor.Direction.FORWARD); // Tries to hardware map
            } else {
                deviceName.setDirection(DcMotor.Direction.REVERSE); // Tries to hardware map
            }
        } catch (Exception e) {
            if (errorCount == 0) {
                errorCount = 1;
                errorType = errorNum;
                lastErrorMsg = errorMsg;
            }
            if (errorType != errorNum) { // This will keep it from counting itself as 2+ errors if run multiple times in a loop
                if (errorCount == 1) { // This tells you if there is zero, one, or multiple errors
                    errorCount = 2;
                }
            }
            if (errorType > errorNum) { // This will show the most critical error if there is multiple. "01" is the most critical, "99" is the least
                errorType = errorNum; // This will change the error message and number only if this is the most critical
                lastErrorMsg = errorMsg; // Only changes the error message when a more critical error is discovered
            }
            if(errorType < 10) { // Yes this is unnecessary but I copy and paste this code so it eventually will be needed.
                telemetry.clearAll();
                telemetry.addData("Error1", "(" + errorCount + "0" + errorType + ") " + lastErrorMsg); // Shows the error
            } else {
                telemetry.clearAll();
                telemetry.addData("Error1", "(" + errorCount + errorType + ") " + lastErrorMsg); // Shows the error
            }
        }
    } // 04

    public void setPowerDc(DcMotor deviceName, double power) {
        int errorNum = 5;
        String errorMsg = "ERR: Set Power (DC)";
        try {
            deviceName.setPower(power);
        } catch (Exception e) {
            if (errorType != errorNum) { // This will keep it from counting itself as 2+ errors if run multiple times in a loop
                if (errorCount == 0) { // This tells you if there is zero, one, or multiple errors
                    errorCount = 1;
                } else {
                    errorCount = 2;
                }
            }
            if (errorType > errorNum) { // This will show the most critical error if there is multiple. "01" is the most critical, "99" is the least
                errorType = errorNum; // This will change the error message and number only if this is the most critical
                lastErrorMsg = errorMsg; // Only changes the error message when a more critical error is discovered
            }
            if(errorType < 10) { // Yes this is unnecessary but I copy and paste this code so it eventually will be needed.
                telemetry.clearAll();
                telemetry.addData("Error1", "(" + errorCount + "0" + errorType + ") " + lastErrorMsg); // Shows the error
            } else {
                telemetry.clearAll();
                telemetry.addData("Error1", "(" + errorCount + errorType + ") " + lastErrorMsg); // Shows the error
            }
        }
    } // 05

    public void setTargetPosition(DcMotor deviceName, int position) {
        int errorNum = 6;
        String errorMsg = "ERR: Set Target Position";
        try {
            deviceName.setTargetPosition(position);
        } catch (Exception e) {
            if (errorType != errorNum) { // This will keep it from counting itself as 2+ errors if run multiple times in a loop
                if (errorCount == 0) { // This tells you if there is zero, one, or multiple errors
                    errorCount = 1;
                } else {
                    errorCount = 2;
                }
            }
            if (errorType > errorNum) { // This will show the most critical error if there is multiple. "01" is the most critical, "99" is the least
                errorType = errorNum; // This will change the error message and number only if this is the most critical
                lastErrorMsg = errorMsg; // Only changes the error message when a more critical error is discovered
            }
            if(errorType < 10) { // Yes this is unnecessary but I copy and paste this code so it eventually will be needed.
                telemetry.clearAll();
                telemetry.addData("Error1", "(" + errorCount + "0" + errorType + ") " + lastErrorMsg); // Shows the error
            } else {
                telemetry.clearAll();
                telemetry.addData("Error1", "(" + errorCount + errorType + ") " + lastErrorMsg); // Shows the error
            }
        }
    } // 06

    public void setPowerCRServo(CRServo deviceName, double power) {
        int errorNum = 7;
        String errorMsg = "ERR: Set Power CR Servo";
        try {
            deviceName.setPower(power);
        } catch (Exception e) {
            if (errorCount == 0) {
                errorCount = 1;
                errorType = errorNum;
                lastErrorMsg = errorMsg;
            }
            if (errorType != errorNum) { // This will keep it from counting itself as 2+ errors if run multiple times in a loop
                if (errorCount == 1) { // This tells you if there is zero, one, or multiple errors
                    errorCount = 2;
                }
            }
            if (errorType > errorNum) { // This will show the most critical error if there is multiple. "01" is the most critical, "99" is the least
                errorType = errorNum; // This will change the error message and number only if this is the most critical
                lastErrorMsg = errorMsg; // Only changes the error message when a more critical error is discovered
            }
            if(errorType < 10) { // Yes this is unnecessary but I copy and paste this code so it eventually will be needed.
                telemetry.clearAll();
                telemetry.addData("Error1", "(" + errorCount + "0" + errorType + ") " + lastErrorMsg); // Shows the error
            } else {
                telemetry.clearAll();
                telemetry.addData("Error1", "(" + errorCount + errorType + ") " + lastErrorMsg); // Shows the error
            }
        }
    } // 07

    public void deadMotorCheck(DcMotor firstMotor, DcMotor secondMotor) {
        int errorNum = 8;
        String errorMsg = "WARN: Drifting Motor";
        int motor1 = Math.abs(Math.round(firstMotor.getCurrentPosition()));
        int motor2 = Math.abs(Math.round(secondMotor.getCurrentPosition()));

        if(Math.abs(motor1 - motor2) >= 2000) {
            if (errorCount == 0) {
                errorCount = 1;
                errorType = errorNum;
                lastErrorMsg = errorMsg;
            }
            if (errorType != errorNum) { // This will keep it from counting itself as 2+ errors if run multiple times in a loop
                if (errorCount == 1) { // This tells you if there is zero, one, or multiple errors
                    errorCount = 2;
                }
            }
            if (errorType > errorNum) { // This will show the most critical error if there is multiple. "01" is the most critical, "99" is the least
                errorType = errorNum; // This will change the error message and number only if this is the most critical
                lastErrorMsg = errorMsg; // Only changes the error message when a more critical error is discovered
            }
            if(errorType < 10) { // Yes this is unnecessary but I copy and paste this code so it eventually will be needed.
                telemetry.clearAll();
                telemetry.addData("Error1", "(" + errorCount + "0" + errorType + ") " + lastErrorMsg); // Shows the error
            } else {
                telemetry.clearAll();
                telemetry.addData("Error1", "(" + errorCount + errorType + ") " + lastErrorMsg); // Shows the error
            }
        }
    } // 08
}
