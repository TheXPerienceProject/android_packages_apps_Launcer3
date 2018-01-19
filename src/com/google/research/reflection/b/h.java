// 
// Decompiled by Procyon v0.5.30
// 

package com.google.research.reflection.b;

import com.google.research.reflection.a.a;

import java.util.Comparator;

class h implements Comparator
{
    h(final k k) {
    }

    public int compare(Object a, Object a2) {
        return Long.compare(((a)a2).D(), ((a)a).D());
    }
}
