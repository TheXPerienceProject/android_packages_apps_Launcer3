package com.android.launcher3.util;

import android.os.Looper;
import android.os.MessageQueue;

import com.android.launcher3.Utilities;

public class LooperIdleLock implements MessageQueue.IdleHandler, Runnable
{
    private boolean mIsLocked;
    private Object mLock;

    public LooperIdleLock(Object lock, Looper looper) {
        mLock = lock;
        mIsLocked = true;
        if (Utilities.ATLEAST_MARSHMALLOW) {
            looper.getQueue().addIdleHandler(this);
        }
        else {
            new LooperExecutor(looper).execute(this);
        }
    }

    public boolean awaitLocked(long n) {
        if (mIsLocked) {
            try {
                mLock.wait(n);
            }
            catch (InterruptedException ex) {
            }
        }
        return mIsLocked;
    }

    public boolean queueIdle() {
        synchronized (mLock) {
            mIsLocked = false;
            mLock.notify();
            return false;
        }
    }

    public void run() {
        Looper.myQueue().addIdleHandler(this);
    }
}