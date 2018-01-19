// 
// Decompiled by Procyon v0.5.30
// 

package com.google.research.reflection.b;

import com.google.research.reflection.common.a;

import java.util.List;

public class j extends b
{
    public int Ts() {
        return 2;
    }
    
    public com.google.research.reflection.layers.b Tt(final a a, final com.google.research.reflection.a.b b) {
        final int n = 1;
        final double n2 = 1.0;
        final com.google.research.reflection.layers.b b2 = new com.google.research.reflection.layers.b(n, 2);
        final List<com.google.research.reflection.a.a> tc = com.google.research.reflection.common.b.Tc(b, "semantic_place");
        if (tc.size() > 0 && tc.get(0).C() != null && tc.get(0).C().size() > 0) {
            if (!(tc.get(0).C().get(0)).equals("Work")) {
                if (tc.get(0).C().get(0).equals("Home")) {
                    b2.Nl[n] = n2;
                }
            }
            else {
                b2.Nl[0] = n2;
            }
        }
        return b2;
    }
    
    public b clone() {
        return new j();
    }
}
