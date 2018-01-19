// 
// Decompiled by Procyon v0.5.30
// 

package com.google.research.reflection.layers;

import java.util.ArrayList;

public class s extends v
{
    i[] ON;
    int OO;
    
    public b UB(final boolean b, final a a, final ArrayList[] array, final b b2) throws InvalidValueException {
        final int pd = 1;
        b b3 = null;
        if (array == null) {
            this.Pd = false;
            this.Pb.add(b2);
        }
        else {
            this.Pd = (pd != 0);
            this.Pc.add(array);
        }
        if (this.Pg.SX() > pd) {
            b3 = (b)this.Pg.SU();
        }
        b b4 = (b)this.Pg.SS();
        if (b4 == null) {
            b4 = new b(this.Pa, this.Pf);
        }
        final b b5 = (b)this.Pg.add(b4);
        e.getInstance().Uc(this.ON.length, new f(this, array, b2, b3, b5));
        return b5;
    }
    
    void UC() {
        super.UC();
        final i[] on = this.ON;
        for (int length = on.length, i = 0; i < length; ++i) {
            on[i].Ui();
        }
    }
    
    public void UD(final a a, final int n, final b b, final b b2, final b b3) throws InvalidValueException {
        int i = 0;
        b.TT(b, b2, this.Ph, false);
        e.getInstance().Uc(this.ON.length, new w(this, n, (ArrayList[])this.Pc.ST(n), (b)this.Pb.ST(n), (b)this.Pg.ST(n - 1)));
        this.OY.clear();
        for (int j = 0; j < this.ON.length; ++j) {
            this.OY.TR(this.ON[j].Uh());
        }
        this.OZ.clear();
        while (i < this.ON.length) {
            this.OZ.TR(this.ON[i].Um());
            ++i;
        }
    }
    
    public void UO() {
        super.UO();
        final i[] on = this.ON;
        for (int length = on.length, i = 0; i < length; ++i) {
            on[i].Un();
        }
    }
    
    public String Uu() {
        return "LSTMLayer";
    }
    
    public v clone() {
        return null;
    }
    
    public void update() throws InvalidValueException {
        final i[] on = this.ON;
        for (int length = on.length, i = 0; i < length; ++i) {
            on[i].update();
        }
    }

    class f implements c
    {
        final /* synthetic */ b NA;
        final /* synthetic */ b NB;
        final /* synthetic */ ArrayList[] NC;
        final /* synthetic */ s Ny;
        final /* synthetic */ b Nz;

        f(final s ny, final ArrayList[] nc, final b nb, final b nz, final b na) {
            this.Ny = ny;
            this.NC = nc;
            this.NB = nb;
            this.Nz = nz;
            this.NA = na;
        }

        public Boolean Ub(final int n) throws InvalidValueException {
            final b b = new b(this.Ny.Pa, this.Ny.OO);
            this.Ny.ON[n].Uk(this.NC, this.NB, this.Nz, b);
            final int n2 = n * this.Ny.OO;
            for (int i = 0; i < this.Ny.Pa; ++i) {
                for (int j = 0; j < this.Ny.OO; ++j) {
                    this.NA.TQ(false, i, n2 + j, b.TP(false, i, j));
                }
            }
            return true;
        }
    }

    class w implements c
    {
        final /* synthetic */ s Pi;
        final /* synthetic */ b Pj;
        final /* synthetic */ b Pk;
        final /* synthetic */ int Pl;
        final /* synthetic */ ArrayList[] Pm;

        w(final s pi, final int pl, final ArrayList[] pm, final b pk, final b pj) {
            this.Pi = pi;
            this.Pl = pl;
            this.Pm = pm;
            this.Pk = pk;
            this.Pj = pj;
        }

        public Boolean Ub(final int n) throws InvalidValueException {
            final b b = new b(this.Pi.Pa, this.Pi.OO);
            final int n2 = n * this.Pi.OO;
            for (int i = 0; i < this.Pi.Pa; ++i) {
                for (int j = 0; j < this.Pi.OO; ++j) {
                    b.TQ(false, i, j, this.Pi.Ph.TP(false, i, n2 + j));
                }
            }
            this.Pi.ON[n].Uf(this.Pl, this.Pm, this.Pk, this.Pj, b);
            return true;
        }
    }
}
