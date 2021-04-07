package com.jy.baselibrary.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.jy.baselibrary.utils.YLogUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Administrator
 * created at 2018/6/7 10:27
 * TODO:logcat写文件
 */
public class LogCatHelper {

    private static final String TAG = LogCatHelper.class.getSimpleName();
    private static LogCatHelper instance = null;

    private String dirPath;//保存路径
    //日志缓冲队列
    private ArrayBlockingQueue<String> mCacheLog = new ArrayBlockingQueue<>(500);
    //缓存区大小100k
    private final int BUFF_SIZE = 100;
    private LogThread logThread;

    public static LogCatHelper getInstance() {
        if (instance == null) {
            instance = new LogCatHelper();
        }
        return instance;
    }

    public void init(Context mContext, String path) {
        if (TextUtils.isEmpty(path)) {
            dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "seeker" + File.separator + mContext.getPackageName();
        } else {
            dirPath = path;
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 重置
     *
     * @param mContext
     * @param path
     */
    public void reset(Context mContext, String path) {
        stop();
        init(mContext, path);
        start();
    }

    /**
     * 启动log日志保存
     */
    public void start() {
        if (TextUtils.isEmpty(dirPath)) {
            YLogUtils.INSTANCE.eTag(TAG, "请先在全局Application中调用init(mContext,path)初始化！");
            return;
        }
        logThread = new LogThread(dirPath);
        logThread.start();
    }

    public void stop() {
        if (logThread != null) {
            logThread.stopWrite();
        }
    }

    /**
     * 将日志存入缓存区
     *
     * @param msg
     */
    public void writeLogFile(String msg) {
        try {
            mCacheLog.offer(msg, 100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class LogThread extends Thread {

        private FileOutputStream fos;
        String dirPath;
        String fileName;
        private boolean openWrite = true;

        public LogThread(String dirPath) {
            this.dirPath = dirPath;
            try {
                fileName = FormatDate.getFormatDate();
                File file = new File(dirPath, fileName + ".log");
                if (!file.exists()) {
                    file.createNewFile();
                }
                fos = new FileOutputStream(file, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                filter(dirPath, fileName);
                BufferedOutputStream mOutputStream = new BufferedOutputStream(fos, BUFF_SIZE);

                while (openWrite && !isInterrupted()) {
                    if (mCacheLog.size() > 0) {
                        String msg = FormatDate.getFormatTime() + mCacheLog.poll() + "\r\n";
                        byte[] bytes = msg.getBytes();
                        mOutputStream.write(bytes, 0, bytes.length);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                        fos = null;
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }

        public void stopWrite() {
            openWrite = false;
            this.interrupt();
        }
    }

    private static void filter(String dirPath, String nowFileName) {
        try {
            int nowTime = 0;
            if (!TextUtils.isEmpty(nowFileName)) {
                nowTime = Integer.parseInt(nowFileName);
                YLogUtils.INSTANCE.iTag(TAG, "创建log文件", nowFileName, "当前时间", nowTime);
            }
            File root = new File(dirPath);
            File[] fileList = root.listFiles();
            if (fileList == null || fileList.length < 1) {
                return;
            }
            YLogUtils.INSTANCE.iTag(TAG, "过滤旧log文件，只保留3天log");
            for (File file : fileList) {
                String name = file.getName();
                if (!TextUtils.isEmpty(name) && name.endsWith(".log")) {
                    int time = Integer.parseInt(name.substring(0, name.length() - 4));
                    if (nowTime - time > 2) {
                        boolean success = file.delete();
                        YLogUtils.INSTANCE.iTag(TAG, "删除文件", name, "删除成功", success);
                    } else {
                        YLogUtils.INSTANCE.iTag(TAG, "保留文件", name);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            YLogUtils.INSTANCE.eTag(TAG, "过滤log失败", e.getMessage());
        }
    }

    /**
     * 获取logfile
     *
     * @return
     */
    public Map<String, File> getLogFile() {
        File root = new File(dirPath);
        File[] fileList = root.listFiles();
        Map<String, File> fileMap = new HashMap<String, File>();
        if (fileList == null) {
            return fileMap;
        }
        for (int i = 0; i < fileList.length; i++) {
            fileMap.put(fileList[i].getName(), fileList[i]);
            YLogUtils.INSTANCE.iTag(TAG, "getLogFile", fileList[i].getName());
        }
        return fileMap;
    }

    public String getDirPath() {
        return dirPath;
    }

    @SuppressLint("SimpleDateFormat")
    private static class FormatDate {

        public static String getFormatDate() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            return sdf.format(System.currentTimeMillis());
        }

        public static String getFormatTime() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS  ");
            return sdf.format(System.currentTimeMillis());
        }
    }
}
