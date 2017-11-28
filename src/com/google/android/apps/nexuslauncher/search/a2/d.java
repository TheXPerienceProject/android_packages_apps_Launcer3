// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.apps.nexuslauncher.search.a2;

import com.google.protobuf.nano.f;
import com.google.protobuf.nano.b;
import com.google.protobuf.nano.a;

public final class d extends a
{
    public c eB;
    
    public d() {
        this.clear();
    }
    
    public d clear() {
        this.eB = null;
        this.cachedSize = -1;
        return this;
    }
    
    protected int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        if (this.eB != null) {
            computeSerializedSize += b.Vo(1, this.eB);
        }
        return computeSerializedSize;
    }
    
    public d mergeFrom(final com.google.protobuf.nano.c c) {
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
                        if (this.eB == null) {
                            this.eB = new c();
                        }
                        c.Ww(this.eB);
                        continue;
                    }
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }
    
    public void writeTo(final b b) {
        if (this.eB != null) {
            try {
                b.VK(1, this.eB);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        super.writeTo(b);
    }
}
