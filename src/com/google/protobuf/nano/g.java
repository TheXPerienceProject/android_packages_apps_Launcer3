// 
// Decompiled by Procyon v0.5.30
// 

package com.google.protobuf.nano;

import java.lang.reflect.Array;

public class g
{
    protected boolean PR;
    public int PS;
    protected Class PT;
    protected int type;
    
    void WS(final Object o, final b b) throws CodedOutputByteBufferNano.OutOfSpaceException {
        if (this.PR) {
            this.WW(o, b);
        }
        else {
            this.WT(o, b);
        }
    }
    
    protected void WT(final Object o, final b b) throws CodedOutputByteBufferNano.OutOfSpaceException {
        b.VU(this.PS);
        switch (this.type) {
            case 10: {
                final int wo = f.WO(this.PS);
                b.VD((a)o);
                b.VY(wo, 4);
                break;
            }
            case 11: {
                b.VB((a)o);
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown type " + this.type);
        }

    }
    
    protected int WU(final Object o) {
        final int wo = f.WO(this.PS);
        switch (this.type) {
            default: {
                throw new IllegalArgumentException("Unknown type " + this.type);
            }
            case 10: {
                return b.VM(wo, (a)o);
            }
            case 11: {
                return b.Vo(wo, (a)o);
            }
        }
    }
    
    int WV(final Object o) {
        if (this.PR) {
            return this.WX(o);
        }
        return this.WU(o);
    }
    
    protected void WW(final Object o, final b b) throws CodedOutputByteBufferNano.OutOfSpaceException {
        for (int length = Array.getLength(o), i = 0; i < length; ++i) {
            final Object value = Array.get(o, i);
            if (value != null) {
                this.WT(value, b);
            }
        }
    }
    
    protected int WX(final Object o) {
        int n = 0;
        for (int length = Array.getLength(o), i = 0; i < length; ++i) {
            if (Array.get(o, i) != null) {
                n += this.WU(Array.get(o, i));
            }
        }
        return n;
    }
}
