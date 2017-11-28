// 
// Decompiled by Procyon v0.5.30
// 

package com.google.protobuf.nano;

public abstract class a
{
    protected volatile int cachedSize;
    
    public a() {
        this.cachedSize = -1;
    }
    
    public static final a mergeFrom(final a a, final byte[] array) {
        return mergeFrom(a, array, 0, array.length);
    }
    
    public static final a mergeFrom(final a a, final byte[] array, final int n, final int n2) {
        final c wc = c.WC(array, n, n2);
        a.mergeFrom(wc);
        try {
            wc.WK(0);
        } catch (InvalidProtocolBufferNanoException e) {
            throw new IllegalStateException(e);
        }
        return a;
    }
    
    public static final void toByteArray(final a a, final byte[] array, final int n, final int n2) {
        final b vz = b.VZ(array, n, n2);
        a.writeTo(vz);
        vz.VI();
    }
    
    public static final byte[] toByteArray(final a a) {
        final byte[] array = new byte[a.getSerializedSize()];
        toByteArray(a, array, 0, array.length);
        return array;
    }
    
    public a clone() {
        try {
            return (a) super.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    protected int computeSerializedSize() {
        return 0;
    }
    
    public int getCachedSize() {
        if (this.cachedSize < 0) {
            this.getSerializedSize();
        }
        return this.cachedSize;
    }
    
    public int getSerializedSize() {
        return this.cachedSize = this.computeSerializedSize();
    }
    
    public abstract a mergeFrom(final c p0);
    
    public String toString() {
        return i.Xh(this);
    }
    
    public void writeTo(final b b) {
    }
}
