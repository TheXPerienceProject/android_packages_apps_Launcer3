package com.google.research.reflection.b;

import com.google.research.reflection.common.a;
import com.google.research.reflection.common.d;

import java.util.List;

public class i extends b {
    public int Ts() {
        return 3;
    }

    public com.google.research.reflection.layers.b Tt(final a a, final com.google.research.reflection.a.b b) {
        final int n = 2;
        final int n2 = 1;
        final com.google.research.reflection.layers.b b2 = new com.google.research.reflection.layers.b(n2, this.Ts());
        final List<com.google.research.reflection.a.a> tc = com.google.research.reflection.common.b.Tc(b, "lat_long");
        if (tc.size() > 0) {
            final float[] th = d.Th((double) tc.get(0).B().get(0), (double) tc.get(0).B().get(n2));
            b2.Nl[0] = th[0];
            b2.Nl[n2] = th[n2];
            b2.Nl[n] = th[n];
        }
        return b2;
    }

    public b clone() {
        return new i();
    }
}
