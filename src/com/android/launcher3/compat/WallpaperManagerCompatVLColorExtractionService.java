package com.android.launcher3.compat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WallpaperManagerCompatVLColorExtractionService extends Service {
    public WallpaperManagerCompatVLColorExtractionService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
