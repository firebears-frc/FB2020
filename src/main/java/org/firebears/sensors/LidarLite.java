package org.firebears.sensors;

import edu.wpi.first.wpilibj.I2C;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/** Class for LIDAR Lite v3 */
public class LidarLite {

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
        /**
         * Delay between measurements (5 ms units) when ACQ_CFG_MEASURE_DELAY is set
         */
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

    /** Offset of distance readings (962 for broken lidar) */
    static private final int DISTANCE_OFFSET = 0;

    /** Maximum distance of 1.5 m */
    static private final int DISTANCE_MAX = 1500;

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
                || write(Register.ACQ_CONFIG, ACQ_CFG_QUICK_TERM_DISABLE | ACQ_CFG_MEASURE_DELAY)
                || write(Register.BURST_COUNT, BURST_COUNT_CONTINUOUS) || write(Register.ACQ_COMMAND, ACQ_CMD_MEASURE);
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
        if (d >= DISTANCE_OFFSET)
            d -= DISTANCE_OFFSET;
        if (d < 0)
            return d;
        else if (d >= DISTANCE_MAX) {
            System.err.println("Invalid distance: " + d);
            return -2;
        } else
            return d;
    }
}