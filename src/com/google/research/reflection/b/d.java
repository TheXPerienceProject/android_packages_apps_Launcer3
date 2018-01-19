// 
// Decompiled by Procyon v0.5.30
// 

package com.google.research.reflection.b;

import com.google.research.reflection.common.UncertaintyException;
import com.google.research.reflection.common.a;
import com.google.research.reflection.common.c;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class d extends b
{
    protected long Na;
    protected boolean[] Nb;
    protected int Nc;
    protected HashMap<Object, Object> Nd;
    protected int Ne;
    protected HashMap<Object, Object> Nf;
    protected long Ng;
    
    public d() {
        this.Nf = new HashMap<>();
        this.Nd = new HashMap<>();
        this.Ne = 200;
        this.Na = 600000L;
        this.Ng = 0L;
        this.Nc = 2;
        this.Nb = new boolean[this.Ne];
    }
    
    public d(final int ne) {
        this.Nf = new HashMap<>();
        this.Nd = new HashMap<>();
        this.Ne = 200;
        this.Na = 600000L;
        this.Ng = 0L;
        this.Nc = 2;
        this.Ne = ne;
        this.Nb = new boolean[this.Ne];
    }
    
    public d(final int ne, final long na, final long ng, final int nc) {
        this.Nf = new HashMap();
        this.Nd = new HashMap();
        this.Ne = 200;
        this.Na = 600000L;
        this.Ng = 0L;
        this.Nc = 2;
        this.Nc = nc;
        this.Na = na;
        this.Ng = ng;
        this.Ne = ne;
        this.Nb = new boolean[this.Ne];
    }
    
    private String TC() {
        final long n = Long.MAX_VALUE;
        final Iterator<Map.Entry<Object, Object>> iterator = this.Nf.entrySet().iterator();
        long n2 = n;
        String s = null;
        while (iterator.hasNext()) {
            final Map.Entry<Object, Object> entry = iterator.next();
            final long longValue = (Long) this.Nd.get(entry.getValue());
            int n3;
            if (longValue >= n2) {
                n3 = 1;
            }
            else {
                n3 = 0;
            }
            String s2;
            long n4;
            if (n3 == 0) {
                s2 = (String) entry.getKey();
                n4 = longValue;
            }
            else {
                s2 = s;
                n4 = n2;
            }
            n2 = n4;
            s = s2;
        }
        return s;
    }

    protected ArrayList<c> TA(final a a, final com.google.research.reflection.a.b b, final long n, final long n2, final int n3) {
        long v4;
        final ArrayList<c> list = new ArrayList<>();
        final HashMap<Integer, c> hashMap = new HashMap<>();
        for (int i = a.SV() - 1; i >= 0; --i) {
            com.google.research.reflection.a.b b1 = (com.google.research.reflection.a.b) a.ST(i);
            if (b1.M() == null
                    || b1.M().size() == 0
                    || !b1.M().get(0).equals("GEL"))
                continue;
            try {
                v4 = com.google.research.reflection.common.e.Tn(b1, b);
            } catch (UncertaintyException ex) {
                v4 = Long.MAX_VALUE;
            }
            v4 -= b1.K();
            if (v4 < n) {
                break;
            }
            if (v4 >= n3) {
                continue;
            }
            int TB_key = this.TB(b1.getId(), b.F());
            com.google.research.reflection.common.c v2 = hashMap.get(TB_key);
            if (v2 == null) {
                if (hashMap.size() >= n3) {
                    break;
                }
                c c = new c(TB_key);
                hashMap.put(TB_key, c);
            }
            v2.MW += 1.0f;
        }

        list.addAll(hashMap.values());
        return list;
    }
    
    protected int TB(final String s, final long n) {
        final int n2 = 1;
        int i = 0;
        Integer value = (Integer) this.Nf.get(s);
        if (value == null) {
            if (this.Nf.size() != this.Ne) {
                while (i < this.Nb.length) {
                    if (!this.Nb[i]) {
                        value = i;
                        this.Nb[i] = (n2 != 0);
                        break;
                    }
                    ++i;
                }
            }
            else {
                final String tc = this.TC();
                value = (Integer)this.Nf.get(tc);
                final String[] array = new String[n2];
                array[0] = tc;
                this.Tu(Arrays.asList(array));
                this.Nb[value] = (n2 != 0);
            }
            this.Nf.put(s, value);
        }
        this.Nd.put(value, n);
        return value;
    }
    
    public int Ts() {
        return this.Ne;
    }
    
    public com.google.research.reflection.layers.b Tt(final a a, final com.google.research.reflection.a.b b) {
        final ArrayList<c> ta = this.TA(a, b, this.Na, this.Ng, this.Nc);
        final com.google.research.reflection.layers.b b2 = new com.google.research.reflection.layers.b(1, this.Ne);
        for (final com.google.research.reflection.common.c c : ta) {
            if (c.MW > 0.0f) {
                if (c.MV >= this.Ne) {
                    throw new RuntimeException(new StringBuilder(26).append("invalid index: ").append(c.MV).toString());
                }
                b2.Nl[c.MV] = 1.0;
            }
        }
        return b2;
    }
    
    public void Tu(final List list) {
        if (!list.isEmpty()) {
            final Integer n = (Integer) this.Nf.remove(list.get(0));
            if (n != null) {
                this.Nd.remove(n);
                this.Nb[n] = false;
                this.Tx(n);
            }
        }
    }
    
    public void Tv(final DataInputStream dataInputStream) throws IOException {
        this.Ne = dataInputStream.readInt();
        this.Nc = dataInputStream.readInt();
        this.Na = dataInputStream.readLong();
        this.Ng = dataInputStream.readLong();
        this.Nf = com.google.research.reflection.common.d.Tj(dataInputStream, String.class, Integer.class);
        this.Nd = com.google.research.reflection.common.d.Tj(dataInputStream, Integer.class, Long.class);
        this.Nb = new boolean[this.Ne];
        final Iterator<Object> iterator = this.Nf.values().iterator();
        while (iterator.hasNext()) {
            this.Nb[(Integer) iterator.next()] = true;
        }
    }
    
    public void Tw(final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(this.Ne);
        dataOutputStream.writeInt(this.Nc);
        dataOutputStream.writeLong(this.Na);
        dataOutputStream.writeLong(this.Ng);
        com.google.research.reflection.common.d.Ti(dataOutputStream, this.Nf);
        com.google.research.reflection.common.d.Ti(dataOutputStream, this.Nd);
    }
    
    public d clone() {
        final d d = new d(this.Ne, this.Na, this.Ng, this.Nc);
        for (final Map.Entry<Object, Object> entry : this.Nf.entrySet()) {
            d.Nf.put(entry.getKey(), entry.getValue());
        }
        for (final Map.Entry<Object, Object> entry2 : this.Nd.entrySet()) {
            d.Nd.put(entry2.getKey(), entry2.getValue());
        }
        d.Nb = Arrays.copyOf(this.Nb, this.Nb.length);
        d.Tq(this.MX);
        return d;
    }
}
