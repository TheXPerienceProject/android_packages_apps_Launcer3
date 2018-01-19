package com.google.android.apps.nexuslauncher.utils;

import android.content.Context;
import android.util.Log;

import com.android.launcher3.util.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ProtoStore {
    private final Context mContext;

    public ProtoStore(Context context) {
        mContext = context.getApplicationContext();
    }

    public void dw(com.google.protobuf.nano.a p1, String name) {
        try {
            FileOutputStream fos = mContext.openFileOutput(name, 0);
            if (fos != null) {
                fos.write(com.google.protobuf.nano.a.toByteArray(p1));
            } else {
                Log.d("ProtoStore", "deleting " + name);
                mContext.deleteFile(name);
            }
        } catch (FileNotFoundException e) {
            Log.d("ProtoStore", "file does not exist");
        } catch (Exception e) {
            Log.e("ProtoStore", "unable to write file");
        }
    }

    public boolean dv(String name, com.google.protobuf.nano.a a) {
        File fileStreamPath = mContext.getFileStreamPath(name);
        try {
            a.mergeFrom(a, IOUtils.toByteArray(fileStreamPath));
            return true;
        }
        catch (FileNotFoundException ex2) {
            Log.d("ProtoStore", "no cached data");
        }
        catch (Exception ex) {
            Log.e("ProtoStore", "unable to load data", ex);
        }
        return false;
    }
}
