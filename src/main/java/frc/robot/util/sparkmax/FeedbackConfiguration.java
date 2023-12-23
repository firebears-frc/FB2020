package frc.robot.util.sparkmax;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.MotorFeedbackSensor;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;

public interface FeedbackConfiguration {
    public MotorFeedbackSensor apply(CANSparkMax motor);

    public static FeedbackConfiguration absoluteEncoder(boolean inverted, double conversionFactor) {
        return absoluteEncoder(inverted, conversionFactor, null, null);
    }

    public static FeedbackConfiguration absoluteEncoder(boolean inverted, double conversionFactor, Double zeroOffset,
            Integer averageDepth) {
        return new FeedbackConfiguration() {
            @Override
            public MotorFeedbackSensor apply(CANSparkMax motor) {
                AbsoluteEncoder encoder = motor.getAbsoluteEncoder(Type.kDutyCycle);
                Util.configureCheckAndVerify(encoder::setInverted, encoder::getInverted, inverted, "inverted");
                Util.configureCheckAndVerify(encoder::setPositionConversionFactor, encoder::getPositionConversionFactor,
                        conversionFactor, "positionFactor");
                Util.configureCheckAndVerify(encoder::setVelocityConversionFactor, encoder::getVelocityConversionFactor,
                        conversionFactor / 60.0, "velocityFactor");
                if (zeroOffset != null)
                    Util.configureCheckAndVerify(encoder::setZeroOffset, encoder::getZeroOffset, zeroOffset,
                            "zeroOffset");
                if (averageDepth != null)
                    Util.configureCheckAndVerify(encoder::setAverageDepth, encoder::getAverageDepth, averageDepth,
                            "averageDepth");
                return encoder;
            }
        };
    }

    public static FeedbackConfiguration builtInEncoder(boolean inverted, double conversionFactor) {
        return builtInEncoder(inverted, conversionFactor, null, null);
    }

    public static FeedbackConfiguration builtInEncoder(boolean inverted, double conversionFactor, Integer averageDepth,
            Integer measurementPeriod) {
        return new FeedbackConfiguration() {
            @Override
            public MotorFeedbackSensor apply(CANSparkMax motor) {
                RelativeEncoder encoder = motor.getEncoder();
                Util.configureCheckAndVerify(encoder::setInverted, encoder::getInverted, inverted, "inverted");
                Util.configureCheckAndVerify(encoder::setPositionConversionFactor, encoder::getPositionConversionFactor,
                        conversionFactor, "positionFactor");
                Util.configureCheckAndVerify(encoder::setVelocityConversionFactor, encoder::getVelocityConversionFactor,
                        conversionFactor / 60.0, "velocityFactor");
                if (averageDepth != null)
                    Util.configureCheckAndVerify(encoder::setAverageDepth, encoder::getAverageDepth, averageDepth,
                            "averageDepth");
                if (measurementPeriod != null)
                    Util.configureCheckAndVerify(encoder::setMeasurementPeriod, encoder::getMeasurementPeriod,
                            measurementPeriod, "measurementPeriod");
                return encoder;
            }
        };
    }
}
