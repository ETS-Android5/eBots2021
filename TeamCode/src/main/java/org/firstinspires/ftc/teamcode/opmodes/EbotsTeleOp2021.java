package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ebotsenums.Alliance;
import org.firstinspires.ftc.teamcode.DriveWheel;
import org.firstinspires.ftc.teamcode.EbotsRobot;
import org.firstinspires.ftc.teamcode.StopWatch;
import org.firstinspires.ftc.teamcode.manips.EbotsManip;

import java.util.ArrayList;

import static java.lang.String.format;

@TeleOp(name="2021 Teleop", group="Dev")
public class EbotsTeleOp2021 extends LinearOpMode {

    // Declare OpMode members.
    private StopWatch stopWatch;
    private EbotsRobot robot;
    private int loopCount = 0;


    @Override
    public void runOpMode() throws InterruptedException {
        //Configure FtcDashboard telemetry
//        dashboard = FtcDashboard.getInstance();
//        dashboardTelemetry = dashboard.getTelemetry();
//        telemetry = new MultipleTelemetry(telemetry, dashboardTelemetry);

        //robot = new EbotsRobot();
        //robot.initializeStandardDriveWheels(hardwareMap);
        //robot.initializeManipMotors(this.hardwareMap);

        // This constructor initializes driveWheels and manip devices
        robot = new EbotsRobot(hardwareMap);
        robot.setAlliance(Alliance.RED);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        waitForStart();

        // Once the start button is pressed, reset some variables
        stopWatch = new StopWatch();
        telemetry.clearAll();

        // enter the control loop
        while(opModeIsActive()){
            loopCount++;
            robot.setDriveCommand(gamepad1);        //Sets command and runs calculateDrivePowers
            //robot.calculateDrivePowers();
            robot.drive();
            // robot.handleManipInput(gamepad2);
            handleManipInput(gamepad2);
            updateTelemetry();
        }
    }

    private void handleManipInput(Gamepad gamepad){
        // Get an array of all the manip devices
        ArrayList<EbotsManip> ebotsManips = robot.getEbotsManips();

        // Send each manip device the gamepad inputs to process
        for(EbotsManip m: ebotsManips){
            m.handleGamepadInput(gamepad2);
        }
    }

    public void updateTelemetry(){
        // Show the elapsed game time and wheel power.
        String f = "%.2f";
        telemetry.addData("Status", "Run Time: " + stopWatch.getElapsedTimeSeconds());
        telemetry.addData("Crane Power", String.format(f, robot.getCrane().getPower()));
        telemetry.addData("Shooter Power/Velocity:  ",
                format(f, robot.getLauncher().getPower()) + " / "
                        + format(f, robot.getLauncher().getVelocity()));
        telemetry.addData("Crane Position", robot.getCrane().getCurrentPosition());

        telemetry.addData("ringFeeder Position", robot.getRingFeeder().getPosition());
        telemetry.addData("ringFeeder cycle timer:", robot.getRingFeederCycleTimer().getElapsedTimeMillis());


        for (DriveWheel dw: robot.getDriveWheels()){
            int encoderClicks = dw.getEncoderClicks();
            telemetry.addData(dw.getWheelPosition() + " encoder Clicks: ", encoderClicks);
        }

        telemetry.addLine(stopWatch.toString(loopCount));
        telemetry.update();
    }
}
