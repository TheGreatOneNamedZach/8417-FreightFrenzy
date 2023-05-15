package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled; // This import's use is just "@Disabled"
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 *  * The names of OpModes appear on the menu of the FTC Driver Station.
 *  * When an selection is made from the menu, the corresponding OpMode
 *  * class is instantiated on the Robot Controller and executed.
 *
 *  This particular OpMode executes a POV mode driveterrain on a robot with 4 wheels (two on each side).
 *  This OpMode also contains code for 2 servos controlled by the gamepad buttons.
 *
 *  Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 *  * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="MyFirstOpMode", group="Opmode")
@Disabled    // Disables the program by removing it from OpMode list on the Driver Station App.
public class MyFirstOpMode extends OpMode
{
    // Declare OpMode members.
    // Defines all motors so that they exist outside a (nested) loop.
    public ElapsedTime runtime = new ElapsedTime();
    public DcMotor front_Left_Drive = null;
    public DcMotor back_Left_Drive = null;
    public DcMotor front_Right_Drive = null;
    public DcMotor back_Right_Drive = null;
    public DcMotor shooterLeft = null;
    public DcMotor shooterRight = null;
    public DcMotor intakeMotor = null; // THIS IS NOT THE TRANSFER
    public Servo intakeLift = null;
    public Servo intakeArm = null;
    public Servo wobbleLeft = null;
    public Servo wobbleRight = null;
    public Servo wobbleClaw = null;


    // Code to run ONCE when the driver hits INIT
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        /* Initialize the hardware variables. Note that the strings used here as parameters
           to 'get' must correspond to the names assigned during the robot configuration
           step (using the FTC Driver Station app on the phone).
           This defines a motor in the phone to a motor in the code. */
        front_Left_Drive = hardwareMap.get(DcMotor.class, "Front Left Drive");
        back_Left_Drive = hardwareMap.get(DcMotor.class, "Back Left Drive");
        front_Right_Drive = hardwareMap.get(DcMotor.class, "Front Right Drive");
        back_Right_Drive = hardwareMap.get(DcMotor.class, "Back Right Drive");
        shooterLeft = hardwareMap.get(DcMotor.class, "ShooterLeft");
        shooterRight = hardwareMap.get(DcMotor.class, "ShooterRight");
        intakeMotor = hardwareMap.get(DcMotor.class, "IntakeMotor"); // THIS IS NOT THE TRANSFER
        intakeLift = hardwareMap.servo.get("IntakeLift");
        intakeArm = hardwareMap.servo.get("IntakeArm");
        wobbleLeft = hardwareMap.servo.get("WobbleLeft");
        wobbleRight = hardwareMap.servo.get("WobbleRight");
        wobbleClaw = hardwareMap.servo.get("WobbleClaw");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        front_Left_Drive.setDirection(DcMotor.Direction.REVERSE);
        back_Left_Drive.setDirection(DcMotor.Direction.REVERSE);
        front_Right_Drive.setDirection(DcMotor.Direction.FORWARD);
        back_Right_Drive.setDirection(DcMotor.Direction.FORWARD);
        shooterLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE); // THIS IS NOT THE TRANSFER

        // Tell the driver that initialization is complete.
        //telemetry.addData("Status", "Initialized");
    }


    // Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
    @Override
    public void init_loop() {
    }


    // Code to run ONCE when the driver hits PLAY
    @Override
    public void start() {
        runtime.reset();
    }


    /* Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
       This is usually the only part of code you see "working" IRL during TeleOp. */
    @Override
    public void loop() {
        // Setup a variable for each drive wheel to save power level for telemetry
        double leftPower;
        double rightPower;
        double shooterPower;
        double intakePower;

        double drive = -gamepad1.left_stick_y;
        double turn  =  gamepad1.right_stick_x;
        leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
        rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

        double shooter = gamepad2.left_trigger;
        shooterPower = Range.clip(shooter, 0.0, 1.0);

        double intake = gamepad1.right_trigger;
        intakePower = Range.clip(intake, 0.0, 1.0);

        // Gamepad 1
        if (gamepad1.y) // If button "y" is pressed on gamepad 1 then...
            intakeLift.setPosition(1); // ...set the servo to the maximum position. (Makes transfer go up)
        if (gamepad1.a) // If button "a" is pressed on gamepad 1 then...
            intakeLift.setPosition(0); // ...set the servo to the minimum position. (Makes transfer go down)
        if (gamepad1.x) // If button "x" is pressed on gamepad 1 then...
            intakeArm.setPosition(1); // ...set the servo to the maximum position. (Makes transfer arm go in)
        if (gamepad1.b) // If button "b" is pressed on gamepad 1 then...
            intakeArm.setPosition(0); // ...set the servo to the minimum position. (Makes transfer arm go out)

        // Gamepad 2
        if (gamepad2.y) // If button "y" is pressed on gamepad 2 then...
            wobbleLeft.setPosition(0); // ...set the servo to the minimum position. (Makes wobble goal mover go up)
            wobbleRight.setPosition(1); // ...set the servo to the maximum position.
        if (gamepad2.a) // If button "a" is pressed on gamepad 2 then...
            wobbleLeft.setPosition(1); // ...set the servo to the maximum position. (Makes wobble goal mover go down)
            wobbleRight.setPosition(0); // ...set the servo to the minimum position.
        if (gamepad2.x) // If button "x" is pressed on gamepad 2 then...
            wobbleClaw.setPosition(0); // ...set the servo to the minimum position. (Claw closed)
        if (gamepad2.b) // If button "b" is pressed on gamepad 2 then...
            wobbleClaw.setPosition(1); // ...set the servo to the maximum position. (Claw opened).

        // Send calculated power to wheels
        front_Left_Drive.setPower(leftPower);
        back_Left_Drive.setPower(leftPower);
        front_Right_Drive.setPower(rightPower);
        back_Right_Drive.setPower(rightPower);

        shooterLeft.setPower(shooterPower);
        shooterRight.setPower(shooterPower);

        intakeMotor.setPower(intakePower); // THIS IS NOT THE TRANSFER

        // Show the elapsed game time and power of different motors.
        telemetry.addData("Status", "Run Time: " + runtime.toString()); // Time since Init started
        //telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower); // Motor power
        //telemetry.addData("Shooter", "Power (%.2f)", shooterPower); // Shooter power
    }


    /* Code to run ONCE after the driver hits STOP
       Code should only go here if something needs to be disabled when you stop the program.
       Examples:
       disabling an image recognition program (some do not disable automatically)
       setting the position of a servo so it does not break */
    @Override
    public void stop() {
    }

}
