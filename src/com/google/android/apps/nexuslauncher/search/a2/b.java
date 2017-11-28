// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.apps.nexuslauncher.search.a2;

import com.google.protobuf.nano.f;
import com.google.protobuf.nano.c;
import com.google.protobuf.nano.d;
import com.google.protobuf.nano.a;

public final class b extends a
{
    private static volatile b[] ei;
    public String ej;
    public String ek;
    public String el;
    public String label;
    
    public b() {
        this.clear();
    }
    
    public static b[] emptyArray() {
        synchronized (d.PH) {
            if (b.ei == null) {
                b.ei = new b[0];
            }
        }
        return b.ei;
    }
    
    public b clear() {
        this.label = "";
        this.ek = "";
        this.ej = "";
        this.el = "";
        this.cachedSize = -1;
        return this;
    }
    
    protected int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        if (!this.label.equals("")) {
            computeSerializedSize += com.google.protobuf.nano.b.VW(1, this.label);
        }
        if (!this.ek.equals("")) {
            computeSerializedSize += com.google.protobuf.nano.b.VW(2, this.ek);
        }
        if (!this.ej.equals("")) {
            computeSerializedSize += com.google.protobuf.nano.b.VW(3, this.ej);
        }
        if (!this.el.equals("")) {
            computeSerializedSize += com.google.protobuf.nano.b.VW(4, this.el);
        }
        return computeSerializedSize;
    }
    
    public b mergeFrom(final c c) {
        while (true) {
            try {
                final int ws = c.Ws();
                switch (ws) {
                    default: {
                        if (!f.WR(c, ws)) {
                            return this;
                        }
                        continue;
                    }
                    case 0: {
                        return this;
                    }
                    case 10: {
                        this.label = c.WB();
                        continue;
                    }
                    case 18: {
                        this.ek = c.WB();
                        continue;
                    }
                    case 26: {
                        this.ej = c.WB();
                        continue;
                    }
                    case 34: {
                        this.el = c.WB();
                        continue;
                    }
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }
    
    public void writeTo(final com.google.protobuf.nano.b b) {
        try {
            if (!this.label.equals("")) {
                b.VT(1, this.label);
            }
            if (!this.ek.equals("")) {
                b.VT(2, this.ek);
            }
            if (!this.ej.equals("")) {
                b.VT(3, this.ej);
            }
            if (!this.el.equals("")) {
                b.VT(4, this.el);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        super.writeTo(b);
    }
}
