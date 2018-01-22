package com.google.research.reflection.layers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class b {
    int Nk;
    public double[] Nl;
    int Nm;

    public b() {
    }

    public b(final int nk, final int nm) {
        this.Nk = nk;
        this.Nm = nm;
        this.Nl = new double[nk * nm];
    }

    public static b TO(final b b, final b b2, final boolean b3, final b b4, final boolean b5) throws InvalidValueException {
        if (b.TU(false) == b2.TW(b3) && b4.TW(false) == b.TW(false) && b4.TU(false) == b2.TU(b3)) {
            e.getInstance().Uc(b4.Nl.length, new h(b5, b4, b, b2, b3));
            return b4;
        }
        throw new RuntimeException(String.valueOf(b.TW(false)) + "x" + b.TU(false) + " " + b2.TW(b3) + "x" + b2.TU(b3) + " " + b4.TW(false) + "x" + b4.TU(false));
    }

    public static b TT(final b b, final b b2, final b b3, final boolean b4) throws InvalidValueException {
        if (b.TU(false) == b2.TU(false) && b.TW(false) == b2.TW(false) && b3.TU(false) == b2.TU(false) && b3.TW(false) == b2.TW(false)) {
            e.getInstance().Uc(b3.Nl.length, new p(b4, b3, b, b2));
            return b3;
        }
        throw new RuntimeException(String.valueOf(b.TW(false)) + "x" + b.TU(false) + " " + b2.TW(false) + "x" + b2.TU(false) + " " + b3.TW(false) + "x" + b3.TU(false));
    }

    public static b TV(final b b, final b b2) {
        if (b.TW(false) == b2.TW(false)) {
            final com.google.research.reflection.layers.b b3 = new b(b.TW(false), b.TU(false) + b2.TU(false));
            for (int i = 0; i < b3.TW(false); ++i) {
                for (int j = 0; j < b3.TU(false); ++j) {
                    if (j >= b.TU(false)) {
                        b3.TQ(false, i, j, b2.TP(false, i, j - b.TU(false)));
                    } else {
                        b3.TQ(false, i, j, b.TP(false, i, j));
                    }
                }
            }
            return b3;
        }
        throw new RuntimeException();
    }

    public double TP(final boolean b, final int n, final int n2) {
        final int n3 = 41;
        if (n >= this.TW(b)) {
            throw new RuntimeException("requested row: " + n + " >= " + this.TW(b));
        }
        if (n2 < this.TU(b)) {
            return this.Nl[this.TS(b, n, n2)];
        }
        throw new RuntimeException("requested col: " + n2 + " >= " + this.TU(b));
    }

    public void TQ(final boolean b, final int n, final int n2, final double n3) {
        this.Nl[this.TS(b, n, n2)] = n3;
    }

    public b TR(final b b) throws InvalidValueException {
        if (this.TU(false) == b.TU(false) && this.TW(false) == b.TW(false)) {
            e.getInstance().Uc(this.Nl.length, new j(this, b));
            return this;
        }
        throw new RuntimeException(String.valueOf(this.TW(false)) + "x" + this.TU(false) + " " + b.TW(false) + "x" + b.TU(false));
    }

    public int TS(final boolean b, final int n, final int n2) {
        if (!b) {
            return this.Nm * n + n2;
        }
        return this.Nm * n2 + n;
    }

    public int TU(final boolean b) {
        if (!b) {
            return this.Nm;
        }
        return this.Nk;
    }

    public int TW(final boolean b) {
        if (!b) {
            return this.Nk;
        }
        return this.Nm;
    }

    public void TX(final boolean b, final int n, final int n2, final double n3) {
        final double[] nl = this.Nl;
        final int ts = this.TS(b, n, n2);
        nl[ts] += n3;
    }

    public void TY(final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(this.Nk);
        dataOutputStream.writeInt(this.Nm);
        for (int i = 0; i < this.Nl.length; ++i) {
            dataOutputStream.writeDouble(this.Nl[i]);
        }
    }

    public void TZ(final DataInputStream dataInputStream) throws IOException {
        this.Nk = dataInputStream.readInt();
        this.Nm = dataInputStream.readInt();
        this.Nl = new double[this.Nk * this.Nm];
        for (int i = 0; i < this.Nl.length; ++i) {
            this.Nl[i] = dataInputStream.readDouble();
        }
    }

    public b Ua(final double n) throws InvalidValueException {
        e.getInstance().Uc(this.Nl.length, new q(this, n));
        return this;
    }

    public void clear() {
        k.Up(this.Nl);
    }

    public b clone() {
        final b b = new b(this.Nk, this.Nm);
        for (int i = 0; i < this.Nl.length; ++i) {
            b.Nl[i] = this.Nl[i];
        }
        return b;
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.TW(false); ++i) {
            for (int j = 0; j < this.TU(false); ++j) {
                sb.append(String.valueOf(this.TP(false, i, j)) + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    static class h implements c {
        final /* synthetic */ b NG;
        final /* synthetic */ b NH;
        final /* synthetic */ boolean NI;
        final /* synthetic */ boolean NJ;
        final /* synthetic */ b NK;

        h(final boolean ni, final b nk, final b nh, final b ng, final boolean nj) {
            this.NI = ni;
            this.NK = nk;
            this.NH = nh;
            this.NG = ng;
            this.NJ = nj;
        }

        private double Ue(final int n, final int n2) {
            final int tu = this.NH.TU(false);
            double n3 = 0.0;
            for (int i = 0; i < tu; ++i) {
                n3 += this.NH.TP(false, n, i) * this.NG.TP(this.NJ, i, n2);
            }
            return n3;
        }

        public Boolean Ub(final int n) {
            if (!this.NI) {
                this.NK.Nl[n] = this.Ue(n / this.NK.TU(false), n % this.NK.TU(false));
            } else {
                final double[] nl = this.NK.Nl;
                nl[n] += this.Ue(n / this.NK.TU(false), n % this.NK.TU(false));
            }
            return true;
        }
    }

    class j implements c {
        final /* synthetic */ b Om;
        final /* synthetic */ b On;

        j(final b on, final b om) {
            this.On = on;
            this.Om = om;
        }

        public Boolean Ub(final int n) {
            if (this.Om.Nl[n] != 0.0) {
                final double[] nl = this.On.Nl;
                nl[n] += this.Om.Nl[n];
            }
            return true;
        }
    }

    class q implements c {
        final /* synthetic */ b OJ;
        final /* synthetic */ double OK;

        q(final b oj, final double ok) {
            this.OJ = oj;
            this.OK = ok;
        }

        public Boolean Ub(final int n) {
            if (this.OJ.Nl[n] != 0.0) {
                final double[] nl = this.OJ.Nl;
                nl[n] *= this.OK;
            }
            return true;
        }
    }

    static class p implements c {
        final /* synthetic */ b OF;
        final /* synthetic */ b OG;
        final /* synthetic */ boolean OH;
        final /* synthetic */ b OI;

        p(final boolean oh, final b oi, final b og, final b of) {
            this.OH = oh;
            this.OI = oi;
            this.OG = og;
            this.OF = of;
        }

        public Boolean Ub(final int n) {
            if (!this.OH) {
                this.OI.Nl[n] = this.OG.Nl[n] + this.OF.Nl[n];
            } else {
                final double[] nl = this.OI.Nl;
                nl[n] += this.OG.Nl[n] + this.OF.Nl[n];
            }
            return true;
        }
    }
}
