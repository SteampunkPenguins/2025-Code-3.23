package frc.robot.subsystems;

import com.ctre.phoenix6.signals.ControlModeValue;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkClosedLoopController.ArbFFUnits;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.MAXMotionConfig.MAXMotionPositionMode;

import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;


public class Elevator extends SubsystemBase {
  
  private RelativeEncoder mRightEncoder;
  private SparkClosedLoopController rightElevatorController;

  private SparkMax mLeftMotor;
  private SparkMax mRightMotor;

  public Elevator() {
    super("Elevator"); //why does removing this line break it

    // KEEP MASTER MOTOR TO RIGHT ELEVATOR MOTOR

    mRightMotor = new SparkMax(Constants.Elevator.kElevatorRightMotorId, MotorType.kBrushless);
    mLeftMotor = new SparkMax(Constants.Elevator.kElevatorLeftMotorId, MotorType.kBrushless);

    SparkMaxConfig elevatorConfig = new SparkMaxConfig();
    SparkMaxConfig rightElevatorMotorConfig = new SparkMaxConfig();
    SparkMaxConfig leftElevatorMotorConfig = new SparkMaxConfig();

    rightElevatorMotorConfig.closedLoop
        .pid(Constants.Elevator.kP, Constants.Elevator.kI, Constants.Elevator.kD)
        .iZone(Constants.Elevator.kIZone);

    rightElevatorController = mRightMotor.getClosedLoopController(); // ????

    elevatorConfig.smartCurrentLimit(Constants.Elevator.kMaxCurrent);
    elevatorConfig.idleMode(IdleMode.kBrake);

    rightElevatorMotorConfig.apply(elevatorConfig).inverted(true);
    leftElevatorMotorConfig.apply(elevatorConfig).follow(mRightMotor, true);

    // mRightEncoder.setPosition(0);
    mRightEncoder = mRightMotor.getEncoder();
    
   

    //MOTORS GET CONFIGURED HERE
    mLeftMotor.configure(
        leftElevatorMotorConfig,
        ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);

    // RIGHT ELEVATOR MOTOR
    
    mRightMotor.configure(
        rightElevatorMotorConfig,
        ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
  }

  public static enum ElevatorState {
    //put positions within these parameters, maybe pull from your constants?
    TEST(0),
    STOW(0),
    L2(10),
    L3(26),
    L4(0),
    A1(0),
    A2(0);

    private final double e_position;

    private ElevatorState(double e) {
      this.e_position = e;
    }

    public double getElevatorPosition() {
      return this.e_position;
    }
  }


  /*-------------------------------- Generic Subsystem Functions --------------------------------*/

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Elevator/Position", mRightMotor.getEncoder().getPosition());
    SmartDashboard.putNumber("Velocity/Current", mRightEncoder.getVelocity());

    SmartDashboard.putNumber("Elevator/Left Motor Current", mLeftMotor.getOutputCurrent());
    SmartDashboard.putNumber("Current/Right", mRightMotor.getOutputCurrent());

    SmartDashboard.putNumber("Output/Left", mLeftMotor.getAppliedOutput());
    SmartDashboard.putNumber("Output/Right", mRightMotor.getAppliedOutput());
  }

  public void setReferenceToPoint(double position) {
    rightElevatorController.setReference(position, ControlType.kPosition);
  }

  public Command setSpeeds(double speed) {
    DriverStation.reportWarning("I am running!!!!!!!!!!!!!!!!!!!!", Thread.currentThread().getStackTrace());
    return this.runOnce( () -> mRightMotor.set(speed));
  }


  public Command stopElevator() {
    DriverStation.reportWarning("I am stopping!!!!!!!!!!!!!!", Thread.currentThread().getStackTrace());
    return this.runOnce( ()-> mRightMotor.set(0));
  }

  public Command goToReefLevel( ElevatorState position) {
    return this.runOnce( () -> rightElevatorController.setReference(position.e_position, ControlType.kPosition));
    
  }


  /*---------------------------------- Custom Public Functions ----------------------------------*/

  // public ElevatorState getState() {
  //   return mPeriodicIO.state;
  // }

  // public void setElevatorPower(double power) {
  //   putNumber("setElevatorPower", power);
  //   mPeriodicIO.is_elevator_pos_control = false;
  //   mPeriodicIO.elevator_power = power;
  // }

  // public void goToElevatorStow() {
  //   mPeriodicIO.is_elevator_pos_control = true;
  //   mPeriodicIO.elevator_target = Constants.Elevator.kStowHeight;
  //   mPeriodicIO.state = ElevatorState.STOW;
  // }

  // public void goToElevatorL2() {
  //   mPeriodicIO.is_elevator_pos_control = true;
  //   mPeriodicIO.elevator_target = Constants.Elevator.kL2Height;
  //   mPeriodicIO.state = ElevatorState.L2;
  // }

  // public void goToElevatorL3() {
  //   mPeriodicIO.is_elevator_pos_control = true;
  //   mPeriodicIO.elevator_target = Constants.Elevator.kL3Height;
  //   mPeriodicIO.state = ElevatorState.L3;
  // }

  // public void goToElevatorL4() {
  //   mPeriodicIO.is_elevator_pos_control = true;
  //   mPeriodicIO.elevator_target = Constants.Elevator.kL4Height;
  //   mPeriodicIO.state = ElevatorState.L4;
  // }

  // public void goToAlgaeLow() {
  //   mPeriodicIO.is_elevator_pos_control = true;
  //   mPeriodicIO.elevator_target = Constants.Elevator.kLowAlgaeHeight;
  //   mPeriodicIO.state = ElevatorState.A1;
  // }

  // public void goToAlgaeHigh() {
  //   mPeriodicIO.is_elevator_pos_control = true;
  //   mPeriodicIO.elevator_target = Constants.Elevator.kHighAlgaeHeight;
  //   mPeriodicIO.state = ElevatorState.A2;
  // }
  /*---------------------------------- Custom Private Functions ---------------------------------*/
}
