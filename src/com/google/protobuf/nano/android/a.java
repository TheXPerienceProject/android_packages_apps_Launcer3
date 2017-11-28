// 
// Decompiled by Procyon v0.5.30
// 

package com.google.protobuf.nano.android;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;

public final class a implements Parcelable.Creator
{
    private Class Pt;
    
    static void UZ(final Class clazz, final com.google.protobuf.nano.a a, final Parcel parcel) {
        parcel.writeString(clazz.getName());
        parcel.writeByteArray(a.toByteArray(a));
    }
    
    public com.google.protobuf.nano.a createFromParcel(final Parcel p0) {
        try {
            return com.google.protobuf.nano.a.mergeFrom(
                    Class.forName(p0.readString(), false, getClass().getClassLoader())
                        .asSubclass(com.google.protobuf.nano.a.class)
                        .getConstructor()
                        .newInstance(), p0.createByteArray());
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public com.google.protobuf.nano.a[] newArray(final int n) {
        return (com.google.protobuf.nano.a[])Array.newInstance(this.Pt, n);
    }
}
