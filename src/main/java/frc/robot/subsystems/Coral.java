package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import au.grapplerobotics.ConfigurationFailedException;
import au.grapplerobotics.LaserCan;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
//import edu.wpi.first.wpilibj.util.Color;
import frc.robot.Constants;
import frc.robot.simulation.SimulatableCANSparkMax;
import frc.robot.subsystems.leds.LEDs;

public class Coral extends SubsystemBase {

  /*-------------------------------- Private instance variables ---------------------------------*/
  private static Coral mInstance;
  // private PeriodicIO mPeriodicIO;
  public final LEDs m_leds = LEDs.getInstance();

  public static Coral getInstance() {
    if (mInstance == null) {
      mInstance = new Coral();
    }
    return mInstance;
  }

  public enum IntakeState {
    NONE,
    INTAKE,
    REVERSE,
    INDEX,
    READY,
    SCORE
  }

  // private ThriftyNova mLeftMotor;
  // private ThriftyNova mRightMotor;
  // private SimulatableCANSparkMax mLeftMotor;
  // private SimulatableCANSparkMax mRightMotor;
  private SparkMax mLeftMotor;
  private SparkMax mRightMotor; 

  private LaserCan mLaserCAN;
  private int mm_measurement;
  private int someNumber;
  private Boolean coralInRange;

  private SparkMaxConfig LeftMotorConfig;
  private SparkMaxConfig RightMotorConfig;

  public Coral() {
    super("Coral");

    mLeftMotor = new SparkMax(Constants.Coral.kLeftMotorId, MotorType.kBrushless);
    mRightMotor = new SparkMax(Constants.Coral.kRightMotorId, MotorType.kBrushless);

    //Consider Insantiating these outside of the signature and make them private
    SparkMaxConfig coralConfig = new SparkMaxConfig();
    SparkMaxConfig LeftMotorConfig = new SparkMaxConfig();
    SparkMaxConfig RightMotorConfig = new SparkMaxConfig();

    coralConfig.idleMode(IdleMode.kCoast);

    LeftMotorConfig.apply(coralConfig);
    // RightMotorConfig.apply(coralConfig).follow(mLeftMotor, true);

    mLeftMotor.configure(
        LeftMotorConfig,
        ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
    // mRightMotor.configure(
    //     RightMotorConfig,
    //     ResetMode.kResetSafeParameters,
    //     PersistMode.kPersistParameters);

    // mLaserCAN = new LaserCan(Constants.Coral.kLaserId);
    // try {
    //   mLaserCAN.setRangingMode(LaserCan.RangingMode.SHORT);
    //   mLaserCAN.setRegionOfInterest(new LaserCan.RegionOfInterest(8, 8, 16, 16));
    //   mLaserCAN.setTimingBudget(LaserCan.TimingBudget.TIMING_BUDGET_33MS);
    // } catch (ConfigurationFailedException e) {
    //   System.out.println("Configuration failed! " + e);
    // }

    mLaserCAN = new LaserCan(Constants.Coral.kLaserId);
    try {
      mLaserCAN.setRangingMode(LaserCan.RangingMode.SHORT);
      mLaserCAN.setRegionOfInterest(new LaserCan.RegionOfInterest(8, 8, 16, 16)); // to be tuned
      mLaserCAN.setTimingBudget(LaserCan.TimingBudget.TIMING_BUDGET_33MS);
    } catch(ConfigurationFailedException e) {
      System.out.println("Configuration failed!" + e);
    }
  }

  /*-------------------------------- Generic Subsystem Functions --------------------------------*/

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Coral/ Speed", mLeftMotor.get());
    readLaserCanMeasurement();
  }

  public void readLaserCanMeasurement() {
    LaserCan.Measurement measurement = mLaserCAN.getMeasurement();
    if (measurement != null && measurement.status == LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT) {
      mm_measurement = measurement.distance_mm;
    }
    else {
      mm_measurement = -1;
    }

  }

  public Boolean hasCoral() {
    //asign a value to someNumber
    if ( mm_measurement != -1 && mm_measurement < someNumber) {
      return true;
    }
    else { return false; }
  }

  public Command autoIntake(){
    return intake().until( ()-> hasCoral());
  }

  public Command autoOutake(){
    return outake().until(  ()-> !hasCoral());
  }
  public Command intake() {
    DriverStation.reportWarning("I AM INTAKINGGG", Thread.currentThread().getStackTrace());
    return this.runOnce( () -> mLeftMotor.set(0.2));
  }

  public Command outake() {
    DriverStation.reportWarning("I AM OUTAKINGGG", Thread.currentThread().getStackTrace());
    return this.runOnce( ()-> mLeftMotor.set(-0.7));
    
  }

  public Command stop() {
    return this.runOnce( ()-> mLeftMotor.set(0));
  }


  // @Override
  // public void outputTelemetry() {

  //   LaserCan.Measurement measurement = mPeriodicIO.measurement;
  //   if (measurement != null) {
  //     putNumber("Laser/distance", measurement.distance_mm);
  //     putNumber("Laser/ambient", measurement.ambient);
  //     putNumber("Laser/budget_ms", measurement.budget_ms);
  //     putNumber("Laser/status", measurement.status);

  //     putBoolean("Laser/hasCoral", isHoldingCoralViaLaserCAN());
  //   }
  // }

  // @Override
  // public void reset() {
  //   stopCoral();
  // }

  /*---------------------------------- Custom Public Functions ----------------------------------*/

//   public boolean isHoldingCoralViaLaserCAN() {
//     return mPeriodicIO.measurement.distance_mm < 75.0;
//   }

//   public void setSpeed(double rpm) {
//     mPeriodicIO.speed_diff = 0.0;
//     mPeriodicIO.rpm = rpm;
//   }

//   public void intake() {
//     mPeriodicIO.speed_diff = 0.0;
//     mPeriodicIO.rpm = Constants.Coral.kIntakeSpeed;
//     mPeriodicIO.state = IntakeState.INTAKE;

//     //m_leds.setColor(Color.kYellow);
//   }

//   public void reverse() {
//     mPeriodicIO.speed_diff = 0.0;
//     mPeriodicIO.rpm = Constants.Coral.kReverseSpeed;
//     mPeriodicIO.state = IntakeState.REVERSE;
//   }

//   public void index() {
//     mPeriodicIO.speed_diff = 0.0;
//     mPeriodicIO.rpm = Constants.Coral.kIndexSpeed;
//     mPeriodicIO.state = IntakeState.INDEX;

//     //m_leds.setColor(Color.kBlue);
//   }

//   public void scoreL1() {
//     mPeriodicIO.speed_diff = Constants.Coral.kSpeedDifference;
//     mPeriodicIO.rpm = Constants.Coral.kL1Speed;
//     mPeriodicIO.state = IntakeState.SCORE;
//   }

//   public void scoreL24() {
//     mPeriodicIO.speed_diff = 0.0;
//     mPeriodicIO.rpm = Constants.Coral.kL24Speed;
//     mPeriodicIO.state = IntakeState.SCORE;
//   }

//   public void stopCoral() {
//     mPeriodicIO.rpm = 0.0;
//     mPeriodicIO.speed_diff = 0.0;
//     mPeriodicIO.state = IntakeState.NONE;
//   }

//   /*---------------------------------- Custom Private Functions ---------------------------------*/

// //   private void checkAutoTasks() {
// //     switch (mPeriodicIO.state) {
// //       case INTAKE:
// //         if (isHoldingCoralViaLaserCAN()) {
// //           mPeriodicIO.index_debounce++;

// //           if (mPeriodicIO.index_debounce > 10) {
// //             mPeriodicIO.index_debounce = 0;
// //             index();
// //           }
// //         }
// //         break;
// //       case INDEX:
// //         if (!isHoldingCoralViaLaserCAN()) {
// //           stopCoral();

// //           mPeriodicIO.state = IntakeState.READY;
// //           //m_leds.setColor(Color.kBlue);
// //         }
// //         break;
// //       default:
// //         break;
// //     }
//   // }
}
