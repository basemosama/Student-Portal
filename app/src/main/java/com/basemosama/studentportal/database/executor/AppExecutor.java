package com.basemosama.studentportal.database.executor;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutor {
    private static final Object LOCK = new Object();
    private static AppExecutor sInstance;
    private final Executor diskIo;
    private final Executor mainThread;

    private AppExecutor(Executor diskIo, Executor mainThread) {
        this.diskIo = diskIo;
        this.mainThread = mainThread;
    }

    public static AppExecutor getExecutor() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppExecutor(
                        Executors.newSingleThreadExecutor(), new MainThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor getDiskIo() {
        return diskIo;
    }

    public Executor getMainThread() {
        return mainThread;
    }


    private static class MainThreadExecutor implements Executor {
        private Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    }
}
