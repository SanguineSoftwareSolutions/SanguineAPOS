package com.example.apos.config;

import android.util.Log;

public class Logger {

	public enum Level {
		ERROR, WARN, INFO, DEBUG, VERBOSE
	}

	private static String PATH = "ApplicationLog.txt";

	public static String TAG = "APOS";

	private static boolean isLogEnabled = true;
	private static Level logLevel = Level.VERBOSE;

	public static boolean isLogEnabled() {
		return isLogEnabled;
	}

	public static void setLogEnabled(boolean isLogEnabled) {
		Logger.isLogEnabled = isLogEnabled;
	}

	public static Level getLogLevel() {
		return logLevel;
	}

	public static void setLogLevel(Level logLevel) {
		Logger.logLevel = logLevel;
	}

	public static void setLogTag(String logTag) {
		TAG = logTag;
	}

	public static void debug(String msg) {
		if (isLogEnabled && logLevel.ordinal() >= 3 && msg != null) {
			Log.d(TAG, msg);
			//logToFile(TAG, msg + "\r\n");
		}
	}

	public static void debug(Throwable t) {
		if (isLogEnabled && logLevel.ordinal() >= 3) {
			Log.d(TAG, "Exception: ", t);
		}
	}

	public static void debug(String msg, Throwable t) {
		if (isLogEnabled && logLevel.ordinal() >= 3 && msg != null) {
			Log.d(TAG, msg, t);
		}
	}

	public static void info(String msg) {
		if (isLogEnabled && logLevel.ordinal() >= 2 && msg != null) {
			Log.i(TAG, msg);
		}
	}

	public static void warn(String msg) {
		if (isLogEnabled && logLevel.ordinal() >= 1 && msg != null) {
			Log.w(TAG, msg);
		}
	}

	public static void error(String msg) {
		if (isLogEnabled && logLevel.ordinal() >= 0 && msg != null) {
			Log.e(TAG, msg);
		}
	}

	/*private static void logToFile(String tag, String message) {
		try {
			File logFile = new File(CommonUtils.getDataDir(), PATH);
			if (!logFile.exists()) {
				logFile.getParentFile().mkdirs();
				logFile.createNewFile();
			}
			if (logFile.length() > 2097152) { // 2 MB
				logFile.delete();
				//TODO Report bugs
				logFile.createNewFile();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
			writer.write(String.format("%1s [%2s]:%3s\r\n==========\n", getDateTimeStamp(), tag, message));
			writer.close();
		} catch (IOException e) {
			android.util.Log.e(TAG, "Unable to log exception to file.", e);
		}
	}

	private static String getDateTimeStamp() {
		Date dateNow = Calendar.getInstance().getTime();
		return (DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US).format(dateNow));
	}*/
}
