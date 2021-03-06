package frc.subsystems.drivetrain;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import firelib.looper.ILooper;
import firelib.looper.Loop;
import firelib.subsystem.ISubsystem;
import frc.robot.RobotMap;

public class Drivetrain implements ISubsystem {
    private static Drivetrain instance;
    private PeriodicIO mPeriodicIO = new PeriodicIO();
    private MotorBase mMotorBase;
    private Kinematics kinematics = new Kinematics(0.508, 3.6);

    /**
     * singleton method for use throughout the robot
     * 
     * @return
     */
    public static Drivetrain getInstance() {
        if (instance == null) {
            instance = new Drivetrain(new TalonSRX(RobotMap.DRIVETRAIN_LEFT_MASTER),
                    new TalonSRX(RobotMap.DRIVETRAIN_RIGHT_MASTER), new VictorSPX(RobotMap.DRIVETRAIN_LEFT_SLAVE),
                    new VictorSPX(RobotMap.DRIVETRAIN_RIGHT_SLAVE));
        }
        return instance;
    }

    /**
     * ctor -- DO NOT USE -- except for unit testing
     */
    public Drivetrain(TalonSRX masterLeft, TalonSRX masterRight, VictorSPX slaveLeft, VictorSPX slaveRight) {
        mMotorBase = new MotorBase(masterLeft, masterRight, slaveLeft, slaveRight);

    }

    public synchronized void setPeriodicIO(double demandedThrottle, double demandedRot) {
        mPeriodicIO.mDemandedThrottle = demandedThrottle;
        mPeriodicIO.mDemandedRot = demandedRot;

    }

    /**
     * basic drive code for early testing
     */
    public void cartersianDrive() {
        DriveSignal signal = kinematics.toWheelSpeeds(mPeriodicIO.mDemandedThrottle, mPeriodicIO.mDemandedRot);
        mMotorBase.setVelocity(signal.getLeftSpeed(), signal.getRightSpeed());
    }

    /**
     * interface for curvature driving untested
     */
    public void curvatureDrive() {
        DriveSignal signal = kinematics.toCurveWheelSpeeds(mPeriodicIO.mDemandedThrottle, mPeriodicIO.mDemandedRot);
        mMotorBase.setVelocity(signal.getLeftSpeed(), signal.getRightSpeed());
    }

    @Override
    public void updateSmartDashboard() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pollTelemetry() {
        // TODO Auto-generated method stub
        mPeriodicIO.mLeftVel = 0;
        mPeriodicIO.mRightVel = 0;

    }

    @Override
    public void registerEnabledLoops(ILooper enabledLooper) {
        // TODO Auto-generated method stub
        enabledLooper.register(new Loop() {

            @Override
            public void onStop(double timestamp) {
                mMotorBase.setVelocity(0, 0);

            }

            @Override
            public void onStart(double timestamp) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoop(double timestamp) {
                // TODO Auto-generated method stub
                synchronized (this) {
                    cartersianDrive();

                }

            }
        });

    }

    private class PeriodicIO {
        public double mDemandedThrottle = 0;
        public double mDemandedRot = 0;
        public int mLeftVel = 0;
        public int mRightVel = 0;
    }

}