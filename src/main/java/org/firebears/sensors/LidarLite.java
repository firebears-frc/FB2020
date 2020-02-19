package frc.robot.subsystems;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.io.FileReader;
import java.io.BufferedReader;

/*
EX. how to get the rpm that you need
double desired_velocity = LidarLite.optimal_velocity(LidarLite.getDistance() / 100.0);
double rpm = LidarLite.calcRpm(desired_velocity);
*/

/** Class for LIDAR Lite v3 */
public class LidarLite extends SubsystemBase {

    /** Bit to enable bulk read from a series of registers */
    static private final byte BULK_REGISTER_BIT = (byte) (1 << 7);

    /** Control registers */
    static private enum Register {
        /** Acquisition command */
        ACQ_COMMAND(0x00),
        /** Status flags */
        STATUS(0x01),
        /** Maximum number of acquisitions */
        SIG_COUNT_VAL(0x02),
        /** Acquisition configuration */
        ACQ_CONFIG(0x04),
        /** Distance measurement MSB */
        DISTANCE_MSB(0x0F),
        /** Distance measurement LSB */
        DISTANCE_LSB(0x10),
        /** Measurement burst count (OUTER_LOOP_COUNT) */
        BURST_COUNT(0x11),
        /** Number of measurement acquisitions when ACQ_CFG_REF_COUNT is set */
        REF_COUNT_VAL(0x12),
        /** Bypass peak detection threshold */
        THRESHOLD_BYPASSS(0x1C),
        /** Delay between measurements (5 ms units)
         *  when ACQ_CFG_MEASURE_DELAY is set */
        MEASURE_DELAY(0x45);

        /** Register number */
        public final byte register;

        /** Get register for a bulk read (more than 1 byte) */
        public byte bulk() {
            return (byte) (register | BULK_REGISTER_BIT);
        }

        /** Create a register */
        private Register(int r) {
            register = (byte) r;
        }
    }

    /** Default I2C address of LIDAR Lite v3 */
    static private final byte LIDAR_ADDRESS = 0x62;

    /** I2C for LIDAR Lite */
    private final I2C lidar = new I2C(I2C.Port.kMXP, LIDAR_ADDRESS);

    /** Data buffer */
    private final ByteBuffer buf = ByteBuffer.allocate(2);

    /** Create a new Lidar Lite */
    public LidarLite() {
        buf.order(ByteOrder.BIG_ENDIAN);
        write(Register.ACQ_COMMAND, ACQ_CMD_RESET);
    }

    /** Status flags */
    static public final int STATUS_SYSTEM_ERR = 1 << 6;
    static public final int STATUS_HEALTH_OK = 1 << 5;
    static public final int STATUS_SECONDARY_RETURN = 1 << 4;
    static public final int STATUS_INVALID_SIGNAL = 1 << 3;
    static public final int STATUS_SIGNAL_OVERFLOW = 1 << 2;
    static public final int STATUS_REFERENCE_OVERFLOW = 1 << 1;
    static public final int STATUS_BUSY = 1 << 0;

    /** Acquisition commands (ACQ_COMMAND) */
    static private final byte ACQ_CMD_RESET = 0x00;
    static private final byte ACQ_CMD_MEASURE_NO_BIAS = 0x03;
    static private final byte ACQ_CMD_MEASURE = 0x04;

    /** Continuous burst count (BURST_COUNT) */
    static private final byte BURST_COUNT_CONTINUOUS = (byte) 0xFF;

    /** Disable measurement reference process (ACQ_CONFIG flag) */
    static private final int ACQ_CFG_REF_PROCESS_DISABLE = 1 << 6;

    /** Use MEASURE_DELAY instead of default 10 Hz rate (ACQ_CONFIG flag) */
    static private final int ACQ_CFG_MEASURE_DELAY = 1 << 5;

    /** Disable reference filter, avg of 8 measurements (ACQ_CONFIG flag) */
    static private final int ACQ_CFG_REF_FILTER_DISABLE = 1 << 4;

    /** Disable quick termination of measurements (ACQ_CONFIG flag) */
    static private final int ACQ_CFG_QUICK_TERM_DISABLE = 1 << 3;

    /** Use REF_COUNT_VAL instead of default 5 (ACQ_CONFIG flag) */
    static private final int ACQ_CFG_REF_COUNT = 1 << 2;

    /** Configuration mode bits */
    static private final int ACQ_CFG_MODE = 0x03;
    static private final int ACQ_CFG_MODE_PWM = 0x00;
    static private final int ACQ_CFG_MODE_STATUS_PIN = 0x01;
    static private final int ACQ_CFG_MODE_DELAY_PWN = 0x02;
    static private final int ACQ_CFG_MODE_OSCILLATOR = 0x03;

    /** Write a value to a register */
    private boolean write(Register reg, int value) {
        return lidar.write(reg.register, (byte) value);
    }

    /** Read a byte value from a register */
    private int readByte(Register reg) {
        if (lidar.read(reg.register, 1, buf))
            return -1;
        else
            return ((int) buf.get(0)) & 0xFF;
    }

    /** Read a short value from 2 successive registers */
    private int readShort(Register reg) {
        if (lidar.read(reg.bulk(), 2, buf))
            return -1;
        else
            return ((int) buf.getShort(0)) & 0xFFFF;
    }

    /** Start continuous measurement */
    public boolean startContinuous() {
        return write(Register.MEASURE_DELAY, 10)
            || write(Register.ACQ_CONFIG, ACQ_CFG_QUICK_TERM_DISABLE |
                                          ACQ_CFG_MEASURE_DELAY)
            || write(Register.BURST_COUNT, BURST_COUNT_CONTINUOUS)
            || write(Register.ACQ_COMMAND, ACQ_CMD_MEASURE);
    }

    /** Stop continuous measurement */
    public boolean stopContinuous() {
        return write(Register.BURST_COUNT, 0);
    }

    /** Get sensor status flags */
    public int getStatus() {
        return readByte(Register.STATUS);
    }

    /** Get a distance measurement */
    public int getDistance() {
        int d = readShort(Register.DISTANCE_MSB);
        if (d < 0)
            return d;
        else
            return d;
    }

    double wheel_speed = 0.0079756; // the number of m/s that one rpm is equal to
    double loss_rate = 0.45; // estamated loss rate 
    double nedded_rpm; // needed rpm for desired velocity of ball



    double array [][]={

        {1.660,8.879,8.921,8.879},
        {1.670,8.808,8.902,8.808},
        {1.680,8.741,8.883,8.741},
        {1.690,8.677,8.865,8.677},
        {1.710,8.561,8.830,8.561},
        {1.720,8.507,8.814,8.507},
        {1.740,8.440,8.783,8.407},
        {1.750,8.430,8.768,8.361},
        {1.760,8.420,8.753,8.318},
        {1.770,8.411,8.740,8.276},
        {1.780,8.402,8.726,8.236},
        {1.790,8.393,8.713,8.198},
        {1.800,8.384,8.701,8.162},
        {1.810,8.376,8.688,8.128},
        {1.820,8.368,8.677,8.095},
        {1.830,8.360,8.665,8.090},
        {1.840,8.353,8.654,8.085},
        {1.850,8.346,8.644,8.080},
        {1.860,8.339,8.634,8.076},
        {1.870,8.332,8.624,8.072},
        {1.880,8.326,8.614,8.068},
        {1.890,8.320,8.605,8.065},
        {1.900,8.314,8.596,8.061},
        {1.910,8.309,8.587,8.058},
        {1.920,8.303,8.579,8.055},
        {1.930,8.298,8.571,8.052},
        {1.940,8.293,8.563,8.049},
        {1.950,8.289,8.556,8.047},
        {1.960,8.284,8.549,8.044},
        {1.970,8.280,8.542,8.042},
        {1.980,8.276,8.535,8.040},
        {1.990,8.272,8.529,8.038},
        {2.000,8.268,8.522,8.037},
        {2.010,8.265,8.516,8.035},
        {2.020,8.261,8.511,8.034},
        {2.030,8.258,8.505,8.032},
        {2.040,8.255,8.500,8.031},
        {2.050,8.252,8.495,8.030},
        {2.060,8.250,8.490,8.030},
        {2.070,8.247,8.485,8.029},
        {2.080,8.245,8.481,8.028},
        {2.090,8.243,8.476,8.028},
        {2.100,8.241,8.472,8.027},
        {2.110,8.239,8.468,8.027},
        {2.120,8.237,8.465,8.027},
        {2.130,8.235,8.461,8.027},
        {2.140,8.234,8.458,8.027},
        {2.150,8.232,8.454,8.027},
        {2.160,8.231,8.451,8.028},
        {2.170,8.230,8.448,8.028},
        {2.180,8.229,8.446,8.029},
        {2.190,8.228,8.443,8.029},
        {2.200,8.227,8.441,8.030},
        {2.210,8.227,8.438,8.031},
        {2.220,8.226,8.436,8.032},
        {2.230,8.226,8.434,8.032},
        {2.240,8.225,8.432,8.034},
        {2.250,8.225,8.430,8.035},
        {2.260,8.225,8.429,8.036},
        {2.270,8.225,8.427,8.037},
        {2.280,8.225,8.426,8.039},
        {2.290,8.225,8.425,8.040},
        {2.300,8.226,8.423,8.042},
        {2.310,8.226,8.422,8.043},
        {2.320,8.227,8.421,8.045},
        {2.330,8.227,8.421,8.047},
        {2.340,8.228,8.420,8.049},
        {2.350,8.229,8.419,8.051},
        {2.360,8.229,8.419,8.053},
        {2.370,8.230,8.419,8.055},
        {2.380,8.231,8.418,8.057},
        {2.390,8.232,8.418,8.059},
        {2.400,8.234,8.418,8.061},
        {2.410,8.235,8.418,8.063},
        {2.420,8.236,8.418,8.066},
        {2.430,8.238,8.418,8.068},
        {2.440,8.239,8.419,8.071},
        {2.450,8.241,8.419,8.073},
        {2.460,8.242,8.419,8.076},
        {2.470,8.244,8.420,8.079},
        {2.480,8.246,8.421,8.081},
        {2.490,8.247,8.421,8.084},
        {2.500,8.249,8.422,8.087},
        {2.510,8.251,8.423,8.090},
        {2.520,8.253,8.424,8.093},
        {2.530,8.255,8.425,8.096},
        {2.540,8.257,8.426,8.099},
        {2.550,8.260,8.427,8.102},
        {2.560,8.262,8.428,8.105},
        {2.570,8.264,8.430,8.108},
        {2.580,8.267,8.431,8.111},
        {2.590,8.269,8.432,8.115},
        {2.600,8.271,8.434,8.118},
        {2.610,8.274,8.435,8.121},
        {2.620,8.277,8.437,8.125},
        {2.630,8.279,8.439,8.128},
        {2.640,8.282,8.440,8.132},
        {2.650,8.285,8.442,8.135},
        {2.660,8.287,8.444,8.139},
        {2.670,8.290,8.446,8.142},
        {2.680,8.293,8.448,8.146},
        {2.690,8.296,8.450,8.150},
        {2.700,8.299,8.452,8.153},
        {2.710,8.302,8.454,8.157},
        {2.720,8.305,8.456,8.161},
        {2.730,8.308,8.458,8.165},
        {2.740,8.311,8.461,8.169},
        {2.750,8.314,8.463,8.172},
        {2.760,8.318,8.465,8.176},
        {2.770,8.321,8.468,8.180},
        {2.780,8.324,8.470,8.184},
        {2.790,8.328,8.473,8.188},
        {2.800,8.331,8.475,8.192},
        {2.810,8.334,8.478,8.196},
        {2.820,8.338,8.481,8.200},
        {2.830,8.341,8.484,8.205},
        {2.840,8.345,8.486,8.209},
        {2.850,8.348,8.489,8.213},
        {2.860,8.352,8.492,8.217},
        {2.870,8.356,8.495,8.221},
        {2.880,8.359,8.498,8.226},
        {2.890,8.363,8.501,8.230},
        {2.900,8.367,8.504,8.234},
        {2.910,8.371,8.507,8.239},
        {2.920,8.374,8.510,8.243},
        {2.930,8.378,8.513,8.248},
        {2.940,8.382,8.516,8.252},
        {2.950,8.386,8.519,8.256},
        {2.960,8.390,8.522,8.261},
        {2.970,8.394,8.526,8.265},
        {2.980,8.398,8.529,8.270},
        {2.990,8.402,8.532,8.275},
        {3.000,8.406,8.536,8.279},
        {3.010,8.410,8.539,8.284},
        {3.020,8.414,8.543,8.288},
        {3.030,8.418,8.546,8.293},
        {3.040,8.422,8.550,8.298},
        {3.050,8.426,8.553,8.302},
        {3.060,8.431,8.557,8.307},
        {3.070,8.435,8.560,8.312},
        {3.080,8.439,8.564,8.317},
        {3.090,8.443,8.568,8.321},
        {3.100,8.448,8.571,8.326},
        {3.110,8.452,8.575,8.331},
        {3.120,8.456,8.579,8.336},
        {3.130,8.461,8.582,8.341},
        {3.140,8.465,8.586,8.345},
        {3.150,8.469,8.590,8.350},
        {3.160,8.474,8.594,8.355},
        {3.170,8.478,8.598,8.360},
        {3.180,8.483,8.602,8.365},
        {3.190,8.487,8.606,8.370},
        {3.200,8.492,8.610,8.375},
        {3.210,8.496,8.613,8.380},
        {3.220,8.501,8.618,8.385},
        {3.230,8.505,8.622,8.390},
        {3.240,8.510,8.626,8.395},
        {3.250,8.515,8.630,8.400},
        {3.260,8.519,8.634,8.405},
        {3.270,8.524,8.638,8.410},
        {3.280,8.529,8.642,8.415},
        {3.290,8.533,8.646,8.420},
        {3.300,8.538,8.650,8.426},
        {3.310,8.543,8.655,8.431},
        {3.320,8.547,8.659,8.436},
        {3.330,8.552,8.663,8.441},
        {3.340,8.557,8.667,8.446},
        {3.350,8.562,8.672,8.451},
        {3.360,8.567,8.676,8.457},
        {3.370,8.571,8.680,8.462},
        {3.380,8.576,8.685,8.467},
        {3.390,8.581,8.689,8.472},
        {3.400,8.586,8.694,8.477},
        {3.410,8.591,8.698,8.483},
        {3.420,8.596,8.702,8.488},
        {3.430,8.601,8.707,8.493},
        {3.440,8.606,8.711,8.498},
        {3.450,8.611,8.716,8.504},
        {3.460,8.615,8.720,8.509},
        {3.470,8.620,8.725,8.515},
        {3.480,8.625,8.729,8.520},
        {3.490,8.630,8.734,8.525},
        {3.500,8.635,8.738,8.531},
        {3.510,8.640,8.743,8.536},
        {3.520,8.646,8.748,8.541},
        {3.530,8.651,8.752,8.547},
        {3.540,8.656,8.757,8.552},
        {3.550,8.661,8.762,8.557},
        {3.560,8.666,8.766,8.563},
        {3.570,8.671,8.771,8.568},
        {3.580,8.676,8.776,8.574},
        {3.590,8.681,8.780,8.579},
        {3.600,8.686,8.785,8.585},
        {3.610,8.691,8.790,8.590},
        {3.620,8.697,8.795,8.595},
        {3.630,8.702,8.800,8.601},
        {3.640,8.707,8.804,8.606},
        {3.650,8.712,8.809,8.612},
        {3.660,8.717,8.814,8.618},
        {3.670,8.723,8.819,8.623},
        {3.680,8.728,8.824,8.629},
        {3.690,8.733,8.828,8.634},
        {3.700,8.738,8.833,8.640},
        {3.710,8.744,8.838,8.645},
        {3.720,8.749,8.843,8.651},
        {3.730,8.754,8.848,8.656},
        {3.740,8.759,8.853,8.662},
        {3.750,8.765,8.858,8.667},
        {3.760,8.770,8.863,8.673},
        {3.770,8.775,8.868,8.679},
        {3.780,8.781,8.873,8.684},
        {3.790,8.786,8.878,8.690},
        {3.800,8.791,8.883,8.696},
        {3.810,8.797,8.888,8.701},
        {3.820,8.802,8.893,8.707},
        {3.830,8.808,8.898,8.712},
        {3.840,8.813,8.903,8.718},
        {3.850,8.818,8.908,8.724},
        {3.860,8.824,8.913,8.729},
        {3.870,8.829,8.918,8.735},
        {3.880,8.834,8.923,8.741},
        {3.890,8.840,8.928,8.746},
        {3.900,8.845,8.934,8.752},
        {3.910,8.851,8.939,8.758},
        {3.920,8.856,8.944,8.763},
        {3.930,8.862,8.949,8.769},
        {3.940,8.867,8.954,8.775},
        {3.950,8.873,8.959,8.781},
        {3.960,8.878,8.964,8.786},
        {3.970,8.884,8.970,8.792},
        {3.980,8.889,8.975,8.798},
        {3.990,8.895,8.980,8.804},
        {4.000,8.900,8.985,8.809},
        {4.010,8.906,8.990,8.815},
        {4.020,8.911,8.996,8.821},
        {4.030,8.917,9.001,8.827},
        {4.040,8.922,9.006,8.832},
        {4.050,8.928,9.011,8.838},
        {4.060,8.933,9.017,8.844},
        {4.070,8.939,9.022,8.850},
        {4.080,8.945,9.027,8.855},
        {4.090,8.950,9.032,8.861},
        {4.100,8.956,9.035,8.867},
        {4.110,8.961,9.038,8.873},
        {4.120,8.967,9.041,8.879},
        {4.130,8.972,9.044,8.884},
        {4.140,8.978,9.047,8.890},
        {4.150,8.984,9.050,8.896},
        {4.160,8.989,9.053,8.902},
        {4.170,8.995,9.056,8.908},
        {4.180,9.001,9.059,8.913},
        {4.190,9.006,9.062,8.919},
        {4.200,9.012,9.065,8.925},
        {4.210,9.017,9.069,8.931},
        {4.220,9.023,9.072,8.937},
        {4.230,9.029,9.075,8.943},
        {4.240,9.034,9.079,8.949},
        {4.250,9.040,9.082,8.954},
        {4.260,9.046,9.085,8.960},
        {4.270,9.051,9.089,8.966},
        {4.280,9.057,9.092,8.972},
        {4.290,9.063,9.096,8.978},
        {4.300,9.068,9.099,8.984},
        {4.310,9.074,9.103,8.990},
        {4.320,9.080,9.106,8.996},
        {4.330,9.086,9.110,9.001},
        {4.340,9.091,9.113,9.007},
        {4.350,9.097,9.117,9.013},
        {4.360,9.103,9.121,9.019},
        {4.370,9.108,9.124,9.025},
        {4.380,9.114,9.128,9.031},
        {4.390,9.120,9.132,9.037},
        {4.400,9.125,9.135,9.043},
        {4.410,9.131,9.139,9.049},
        {4.420,9.137,9.143,9.055},
        {4.430,9.143,9.147,9.061},
        {4.440,9.148,9.151,9.066},
        {4.450,9.154,9.154,9.072},
        {4.470,9.162,9.162,9.084},
        {4.510,9.178,9.178,9.108},
        {4.540,9.190,9.190,9.126},
        {4.550,9.194,9.194,9.132},
        {4.570,9.202,9.202,9.144},
        {4.590,9.210,9.210,9.156},
        {4.600,9.214,9.214,9.162},
        {4.610,9.218,9.218,9.168},
        {4.620,9.223,9.223,9.173},
        {4.640,9.231,9.231,9.185},
        {4.690,9.252,9.252,9.215},
        {4.700,9.257,9.257,9.221},
        {4.710,9.261,9.261,9.227},
        {4.770,9.287,9.287,9.263},
        {4.790,9.296,9.296,9.275},
        {4.800,9.300,9.300,9.281},
        {4.810,9.305,9.305,9.287},
        {4.820,9.309,9.309,9.293},
        {4.830,9.314,9.314,9.299},
        {4.840,9.318,9.318,9.305},
        {4.860,9.327,9.327,9.317},
        {4.930,9.359,9.359,9.359}

    };

    public double optimal_velocity (double range)
    {
        for(int i = 1; i < array.length; i++)
        {
            if(array[i][0]>range)
            {
                return array[i-1][1];
            }
        }
        return 0;
    }

    

    public double calcRpm (double desired_velocity)
    {
        nedded_rpm = (desired_velocity / wheel_speed) * loss_rate;
        return nedded_rpm;

    }

    public void periodic() 
    {
        double desired_velocity = optimal_velocity(getDistance() / 100.0);
        SmartDashboard.putNumber("Velocity Needed", desired_velocity);
        SmartDashboard.putNumber("RPM Needed", calcRpm(desired_velocity));

    }
        /*
        EX. how to get the rpm that you need
        double desired_velocity = LidarLite.optimal_velocity(LidarLite.getDistance() / 100.0);
        double rpm = LidarLite.calcRpm(desired_velocity);
        */
}