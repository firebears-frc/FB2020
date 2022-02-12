package frc.robot.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Preferences;

/**
 * Utility functions for loading {@link Properties} files into WPILib's
 * {@link Preferences} objects.
 */
public final class Config {

    protected static PrintStream out = System.err;

    protected final static String BOOLEAN = "^(true)|(false)";
    protected final static String INTEGER = "^-?\\d+$";
    protected final static String LONG = "^-?\\d+L$";
    protected final static String FLOAT = "^-?\\d+\\.\\d*F$";
    protected final static String DOUBLE = "^-?\\d+\\.\\d*D?$";

    /**
     * Print out all key/value pairs in {@link Preferences}, with the keys in
     * alphabetical order.
     * 
     * @param outStream Outputstream, such as {@code System.out}.
     */
    public static void printPreferences(PrintStream outStream) {
        final NetworkTable networkTable = NetworkTableInstance.getDefault().getTable("Preferences");
        SortedSet<String> sortedKeys = new TreeSet<String>(Preferences.getKeys());
        for (String key : sortedKeys) {
            try {
                switch (networkTable.getEntry(key).getType()) {
                    case kBoolean:
                        outStream.printf("%s=%b%n", key, Preferences.getBoolean(key, false));
                        break;
                    case kDouble:
                        outStream.printf("%s=%f%n", key, Preferences.getDouble(key, 0.0));
                        break;
                    case kString:
                        outStream.printf("%s=%f%n", key, Preferences.getString(key, null));
                        break;
                    default:
                        outStream.printf("%s=%s%n", key, Preferences.getString(key, "UNKNOWN"));
                }
            } catch (Exception e) {
                System.out.println("ERROR: Config problem for property: " + key);
                e.printStackTrace();
            }
        }
    }

    /**
     * Remove all preferences from the robot and the network tables. Network table
     * values can be really tenacious, and sometimes you just want to start with a
     * clean slate.
     */
    public static void cleanAllPreferences() {
        SortedSet<String> sortedKeys = new TreeSet<String>(Preferences.getKeys());
        for (String key : sortedKeys) {
            Preferences.remove(key);
        }
    }

    /**
     * Read a sequence of property files into {@link Preferences} . If the files or
     * resources don't exist, print an error message and gracefully move to the next
     * file.
     * 
     * @param fileNames File names or resource names.
     */
    public static void loadConfiguration(String... fileNames) {
        for (String fileName : fileNames) {
            try {
                InputStream inStream = openStream(fileName);
                Properties properties = loadProperties(inStream);
                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    String key = entry.getKey().toString().trim();
                    String value = entry.getValue().toString();
                    if (value.length() == 0) {
                        Preferences.remove(key);
                    } else if (value.matches(BOOLEAN)) {
                        Preferences.setBoolean(key, Boolean.parseBoolean(value));
                    } else if (value.matches(INTEGER)) {
                        Preferences.setInt(key, Integer.parseInt(value));
                    } else if (value.matches(LONG)) {
                        Preferences.setLong(key, Long.parseLong(value));
                    } else if (value.matches(FLOAT)) {
                        Preferences.setFloat(key, Float.parseFloat(value));
                    } else if (value.matches(DOUBLE)) {
                        Preferences.setDouble(key, Double.parseDouble(value));
                    } else {
                        Preferences.setString(key, value);
                    }
                }
            } catch (IOException iox) {
                iox.printStackTrace(out);
            }
        }
    }

    protected static InputStream openStream(String fileName) throws IOException {
        if (fileName.startsWith("/")) {
            File file = new File(fileName);
            if (file.exists() && file.canRead()) {
                return new FileInputStream(file);
            }
        } else {
            URL url = ClassLoader.getSystemResource(fileName);
            if (url != null) {
                return url.openStream();
            }
        }
        out.printf("Config: failed to open %s %n", fileName);
        return null;
    }

    protected static Properties loadProperties(InputStream inStream) throws IOException {
        Properties properties = new Properties();
        if (inStream != null) {
            properties.load(inStream);
            inStream.close();
        }
        return properties;
    }
}