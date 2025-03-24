// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import frc.robot.subsystems.Algae;
import frc.robot.subsystems.Coral;
//import frc.robot.subsystems.Drivetrain;
import frc.robot.controls.controllers.*;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Subsystem;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import com.revrobotics.spark.SparkMax;

import java.util.ArrayList;
import java.util.List;

import com.revrobotics.spark.SparkLowLevel.MotorType;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private final RobotContainer m_robotContainer;
  //private final DriverController m_driverController = new DriverController(0, true, true);
  private final OperatorController m_operatorController = new OperatorController(1, true, true);
  //private final SparkMax Elevator = new SparkMax(3, MotorType.kBrushless);
    // Robot subsystems
  //private List<Subsystem> m_allSubsystems = new ArrayList<>();
  //private final Drivetrain m_drive = Drivetrain.getInstance();
  //private final Coral m_coral = Coral.getInstance();
  //private final Algae m_algae = Algae.getInstance();
  private final Elevator m_elevator = Elevator.getInstanc
  e();

  boolean scorePressed = false;
  public Robot() {
    m_robotContainer = new RobotContainer();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run(); 
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
    m_elevator.goToElevatorL2();
    //double maxSpeed = m_driverController.getWantsSpeedMode() ? Drivetrain.kMaxBoostSpeed : Drivetrain.kMaxSpeed;

    // double xSpeed = m_speedLimiter.calculate(m_driverController.getForwardAxis()
    // * maxSpeed);
    //double xSpeed = m_driverController.getForwardAxis() * maxSpeed;

    // m_drive.slowMode(m_driverController.getWantsSlowMode());
    // m_drive.speedMode(m_driverController.getWantsSpeedMode());
    //double rot = m_rotLimiter.calculate(m_driverController.getTurnAxis() * Drivetrain.kMaxAngularSpeed);

    //m_drive.drive(xSpeed, rot);
    /* 
    // FINAL DRIVER CONTROLS
    if (m_driverController.getWantsScoreCoral()) {
      scorePressed = true;

      if (m_elevator.getState() == Elevator.ElevatorState.STOW) {
        //m_coral.scoreL1();
      } else {
        //m_coral.scoreL24();
      }
    } else if (scorePressed) {
      scorePressed = false;

      m_elevator.goToElevatorStow();
      //m_coral.intake();
    } else if (m_driverController.getWantsScoreAlgae()) {
      //m_algae.score();
    } else if (m_driverController.getWantsGroundAlgae()) {
      //m_algae.groundIntake();
    }
    */

    // FINAL OPERATOR CONTROLS
    if (m_operatorController.getWantsElevatorStow()) {
      m_elevator.goToElevatorStow();
      //m_algae.stow();
    } else if (m_operatorController.getWantsElevatorL2()) {
      m_elevator.goToElevatorL2();
      //m_algae.stow();
    } else if (m_operatorController.getWantsElevatorL3()) {
      m_elevator.goToElevatorL3();
      //m_algae.stow();
    } else if (m_operatorController.getWantsElevatorL4()) {
      m_elevator.goToElevatorL4();
      //m_algae.stow();
    } else if (m_operatorController.getWantsA1()) {
      m_elevator.goToAlgaeLow();
      //m_algae.grabAlgae();
    } else if (m_operatorController.getWantsA2()) {
      m_elevator.goToAlgaeHigh();
      //m_algae.grabAlgae();
    } else if (m_operatorController.getWantsStopAlgae()) {
      //m_algae.stopAlgae();
      //m_algae.stow();
    } else if (m_operatorController.getWantsGroundAlgae()) {
      //m_algae.groundIntake();
    } else if (m_operatorController.getWantsCoralIntake()) {
      //m_coral.intake();
    }

    // if (m_driverController.getWantsScoreCoral()) {
     if (m_elevator.getState() == Elevator.ElevatorState.STOW) {
     //m_coral.scoreL1();
    
     } 
     
     //else if (m_driverController.getWantsIntakeCoral()) {
    // m_coral.intake();
     //m_elevator.goToElevatorStow();
     //}

    if (m_operatorController.getWantsElevatorReset()) //||
    // m_driverController.getWantsElevatorReset()) 
    {
    // RobotTelemetry.print("Resetting elevator");
     m_elevator.reset();
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
  public void simulationPeriodic() {}
}
