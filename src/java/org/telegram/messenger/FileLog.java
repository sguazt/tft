/*
 * This is the source code of Telegram for Android v. 3.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2016.
 */

package org.telegram.messenger;

//[sguazt]
//import android.util.Log;
//[/sguazt]

import org.telegram.messenger.time.FastDateFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Locale;

public class FileLog {
//[sguazt]
    //private OutputStreamWriter streamWriter = null;
    private OutputStreamWriter streamWriter = new OutputStreamWriter(System.err);
//[/sguazt]
    private FastDateFormat dateFormat = null;
//[sguazt]
    //private DispatchQueue logQueue = null;
//[/sguazt]
    private File currentFile = null;
    private File networkFile = null;

    private static volatile FileLog Instance = null;
    public static FileLog getInstance() {
        FileLog localInstance = Instance;
        if (localInstance == null) {
            synchronized (FileLog.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new FileLog();
                }
            }
        }
        return localInstance;
    }

    public FileLog() {
        if (!BuildVars.DEBUG_VERSION) {
            return;
        }
        dateFormat = FastDateFormat.getInstance("dd_MM_yyyy_HH_mm_ss", Locale.US);
//[sguazt]
//        try {
//            File sdCard = ApplicationLoader.applicationContext.getExternalFilesDir(null);
//            if (sdCard == null) {
//                return;
//            }
//            File dir = new File(sdCard.getAbsolutePath() + "/logs");
//            dir.mkdirs();
//            currentFile = new File(dir, dateFormat.format(System.currentTimeMillis()) + ".txt");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            logQueue = new DispatchQueue("logQueue");
//            currentFile.createNewFile();
//            FileOutputStream stream = new FileOutputStream(currentFile);
//            streamWriter = new OutputStreamWriter(stream);
//            streamWriter.write("-----start log " + dateFormat.format(System.currentTimeMillis()) + "-----\n");
//            streamWriter.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
try {
	streamWriter.write("-----start log " + dateFormat.format(System.currentTimeMillis()) + "-----\n");
	streamWriter.flush();
} catch (Exception e) {
	e.printStackTrace();
}
//[/sguazt]
    }

    public static String getNetworkLogPath() {
        if (!BuildVars.DEBUG_VERSION) {
            return "";
        }
//[sguazt]
//        try {
//            File sdCard = ApplicationLoader.applicationContext.getExternalFilesDir(null);
//            if (sdCard == null) {
//                return "";
//            }
//            File dir = new File(sdCard.getAbsolutePath() + "/logs");
//            dir.mkdirs();
//            getInstance().networkFile = new File(dir, getInstance().dateFormat.format(System.currentTimeMillis()) + "_net.txt");
//            return getInstance().networkFile.getAbsolutePath();
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//[/sguazt]
        return "";
    }

    public static void e(final String tag, final String message, final Throwable exception) {
        if (!BuildVars.DEBUG_VERSION) {
            return;
        }
//[sguazt]
//        Log.e(tag, message, exception);
System.err.println("[ERROR] tag: " + tag + ", message: " + message + ", exception: " + exception);
//[/sguazt]
        if (getInstance().streamWriter != null) {
//[sguazt]
//            getInstance().logQueue.postRunnable(new Runnable() {
//                @Override
//                public void run() {
//[/sguazt]
                    try {
                        getInstance().streamWriter.write(getInstance().dateFormat.format(System.currentTimeMillis()) + " E/" + tag + "﹕ " + message + "\n");
                        getInstance().streamWriter.write(exception.toString());
                        getInstance().streamWriter.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//[sguazt]
//                }
//            });
//[/sguazt]
        }
    }

    public static void e(final String tag, final String message) {
        if (!BuildVars.DEBUG_VERSION) {
            return;
        }
//[sguazt]
//        Log.e(tag, message);
System.err.println("[ERROR] tag: " + tag + ", message: " + message);
//[/sguazt]
        if (getInstance().streamWriter != null) {
//[sguazt]
//            getInstance().logQueue.postRunnable(new Runnable() {
//                @Override
//                public void run() {
//[/sguazt]
                    try {
                        getInstance().streamWriter.write(getInstance().dateFormat.format(System.currentTimeMillis()) + " E/" + tag + "﹕ " + message + "\n");
                        getInstance().streamWriter.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//[sguazt]
//                }
//            });
//[/sguazt]
        }
    }

    public static void e(final String tag, final Throwable e) {
        if (!BuildVars.DEBUG_VERSION) {
            return;
        }
        e.printStackTrace();
        if (getInstance().streamWriter != null) {
//[sguazt]
//            getInstance().logQueue.postRunnable(new Runnable() {
//                @Override
//                public void run() {
//[/sguazt]
                    try {
                        getInstance().streamWriter.write(getInstance().dateFormat.format(System.currentTimeMillis()) + " E/" + tag + "﹕ " + e + "\n");
                        StackTraceElement[] stack = e.getStackTrace();
                        for (int a = 0; a < stack.length; a++) {
                            getInstance().streamWriter.write(getInstance().dateFormat.format(System.currentTimeMillis()) + " E/" + tag + "﹕ " + stack[a] + "\n");
                        }
                        getInstance().streamWriter.flush();
//[sguazt]
//                    } catch (Exception e) {
//                        e.printStackTrace();
                    } catch (Exception e2) {
                        e2.printStackTrace();
//[/sguazt]
                    }
//[sguazt]
//                }
//            });
//[/sguazt]
        } else {
            e.printStackTrace();
        }
    }

    public static void d(final String tag, final String message) {
        if (!BuildVars.DEBUG_VERSION) {
            return;
        }
//[sguazt]
//        Log.d(tag, message);
System.err.println("[DEBUG] tag: " + tag + ", message: " + message);
//[/sguazt]
        if (getInstance().streamWriter != null) {
//[sguazt]
//            getInstance().logQueue.postRunnable(new Runnable() {
//                @Override
//                public void run() {
//[/sguazt]
                    try {
                        getInstance().streamWriter.write(getInstance().dateFormat.format(System.currentTimeMillis()) + " D/" + tag + "﹕ " + message + "\n");
                        getInstance().streamWriter.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//[sguazt]
//                }
//            });
//[/sguazt]
        }
    }

    public static void w(final String tag, final String message) {
        if (!BuildVars.DEBUG_VERSION) {
            return;
        }
//[sguazt]
//        Log.w(tag, message);
System.err.println("[WARNING] tag: " + tag + ", message: " + message);
//[/sguazt]
        if (getInstance().streamWriter != null) {
//[sguazt]
//            getInstance().logQueue.postRunnable(new Runnable() {
//                @Override
//                public void run() {
//[/sguazt]
                    try {
                        getInstance().streamWriter.write(getInstance().dateFormat.format(System.currentTimeMillis()) + " W/" + tag + ": " + message + "\n");
                        getInstance().streamWriter.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//[sguazt]
//                }
//            });
//[/sguazt]
        }
    }

    public static void cleanupLogs() {
//[sguazt]
//        File sdCard = ApplicationLoader.applicationContext.getExternalFilesDir(null);
//        if (sdCard == null) {
//            return;
//        }
//        File dir = new File (sdCard.getAbsolutePath() + "/logs");
//        File[] files = dir.listFiles();
//        if (files != null) {
//            for (int a = 0; a < files.length; a++) {
//                File file = files[a];
//                if (getInstance().currentFile != null && file.getAbsolutePath().equals(getInstance().currentFile.getAbsolutePath())) {
//                    continue;
//                }
//                if (getInstance().networkFile != null && file.getAbsolutePath().equals(getInstance().networkFile.getAbsolutePath())) {
//                    continue;
//                }
//                file.delete();
//            }
//        }
//[/sguazt]
    }
}
