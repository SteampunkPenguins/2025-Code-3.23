// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import frc.robot.subsystems.Algae;
import frc.robot.subsystems.Coral;
//import frc.robot.subsystems.Drivetrain;
import frc.robot.controls.controllers.*;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Subsystem;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.HttpCamera;
import edu.wpi.first.cscore.VideoSource;

import java.util.ArrayList;
import java.util.List;

import com.revrobotics.spark.SparkLowLevel.MotorType;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private final RobotContainer m_robotContainer;
  //private final DriverController m_driverController = new DriverController(0, true, true);
  //private final OperatorController m_operatorController = new OperatorController(1, true, true);
  //private final SparkMax Elevator = new SparkMax(3, MotorType.kBrushless);
  //Camera Server
  HttpCamera httpCamera;
    // Robot subsystems

  //private List<Subsystem> m_allSubsystems = new ArrayList<>();
  //private final Drivetrain m_drive = Drivetrain.getInstance();
  private final Coral m_coral = Coral.getInstance();
  private final Algae m_algae = Algae.getInstance();
  private final Elevator m_elevator = Elevator.getInstance();
  private final XboxController player = new XboxController(1);

  boolean scorePressed = false;
  public Robot() {
    m_robotContainer = new RobotContainer();
    httpCamera = new HttpCamera("Limelight :DDD", "http://10.32.4.208:5800");
    //CameraServer.startAutomaticCapture(0);
    Shuffleboard.getTab("Tab").add(httpCamera);
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run(); 
    //CameraServer.getInstance().addCamera(camera);
    //SmartDashboard.getTab("Tab").add(httpCamera);
    //ShuffleboardTab.putSource(httpCamera);
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {
    if(player.getBButtonPressed()) {
      m_elevator.goToElevatorStow();
      SmartDashboard.putString( "Button Pressed!", "B");
      SmartDashboard.putString( "Elevator Position", "STOW");
      System.out.println( "Button Pressed!");
    }

    if(player.getAButtonPressed()){
      m_elevator.goToElevatorL2();
      SmartDashboard.putString( "Button Pressed!", "A");
      SmartDashboard.putString( "Elevator Position", "L2");
    }
  }

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}

  @Override
  public void simulationPeriodic() {
    if(player.getBButtonPressed()) {
      m_elevator.goToElevatorStow();
      SmartDashboard.putString( "Button Pressed!", "B");
      SmartDashboard.putString( "Elevator Position", "STOW");
      System.out.println( "Button Pressed!");
    }

    if(player.getAButtonPressed()){
      m_elevator.goToElevatorL2();
      SmartDashboard.putString( "Button Pressed!", "A");
      SmartDashboard.putString( "Elevator Position", "L2");
    }
  }
}