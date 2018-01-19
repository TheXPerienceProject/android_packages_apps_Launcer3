package com.google.android.apps.nexuslauncher.smartspace.a_package;

import android.content.Context;
import android.util.Log;

import com.android.launcher3.util.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class a
{
    private final Context mContext;

    public a(final Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void dw(final com.google.protobuf.nano.a p1, final String p2) {
        try {
            FileOutputStream fos = this.mContext.openFileOutput(p2, 0);
            if (fos == null) {
                byte[] ba = com.google.protobuf.nano.a.toByteArray(p1);
                fos.write(ba);
            } else {
                Log.d("ProtoStore", "deleting " + p2);
                this.mContext.deleteFile(p2);
            }
        } catch (FileNotFoundException e) {
            Log.d("ProtoStore", "file does not exist");
        } catch (Exception e) {
            Log.e("ProtoStore", "unable to write file");
        }
    }

    public boolean dv(final String s, final com.google.protobuf.nano.a a) {
        final File fileStreamPath = this.mContext.getFileStreamPath(s);
        try {
            a.mergeFrom(a, IOUtils.toByteArray(fileStreamPath));
            return true;
        }
        catch (FileNotFoundException ex2) {
            Log.d("ProtoStore", "no cached data");
            return false;
        }
        catch (Exception ex) {
            Log.e("ProtoStore", "unable to load data", ex);
            return false;
        }
    }
}
