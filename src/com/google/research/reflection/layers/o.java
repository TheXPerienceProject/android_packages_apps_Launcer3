package com.google.research.reflection.layers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class o extends v {
    private b OA;
    private float OB;
    private int OC;
    private b OD;
    private int OE;
    private b Ou;
    private b Ov;
    int Ow;
    private boolean Ox;
    private b Oy;
    private b Oz;

    public o() {
    }

    public o(final boolean b, final int n, final int ow, final int n2, final int n3, final int n4, final int oe, final int oc, final boolean ox, final boolean b2, final float ob) {
        super(b, n, n2, n3, n4);
        this.OE = oe;
        this.OB = ob;
        this.Oz = new b(1, n4);
        this.Ow = ow;
        this.Ov = new b(n3, n4);
        this.OD = new b(n4, n4);
        this.OA = new b(1, n4);
        this.Ox = ox;
        if (oe < 0) {
            k.Uo(this.Ou = new b(n3, n4), b2);
            k.Up(this.Oz.Nl);
        }
        this.OC = oc;
        k.Uo(this.Oy = new b(n4, n4), b2);
    }

    private static void UG(final int n, final b b, final b b2) throws InvalidValueException {
        if (n != 1 && n != 0) {
            if (n != 2) {
                throw new RuntimeException("Unsupported activation function: " + n);
            }
            e.getInstance().Uc(b.TW(false), new r(b, b2));
        } else {
            e.getInstance().Uc(b.Nl.length, new g(n, b2, b));
        }
    }

    public int UA() {
        return this.OE;
    }

    public b UB(final boolean b, final a a, final ArrayList<com.google.research.reflection.common.c>[] array, final b b2) throws InvalidValueException {
        b b3 = null;
        final int pd = 1;
        int i = 0;
        if (array == null) {
            this.Pd = false;
            this.Pb.add(b2);
            if (b2.TU(false) != this.UT() || b2.TW(false) != this.UR()) {
                throw new RuntimeException("Inconsistent input matrix");
            }
        } else {
            this.Pd = true;
            this.Pc.add(array);
        }
        if (this.Pg.SX() > pd) {
            b3 = (b) this.Pg.SU();
        }
        final b b4 = new b(this.Pa, this.Pf);
        e.getInstance().Uc(this.Pa * this.Pf, new x(this, a, array, b2, b3, b4));
        final b b5 = (b) this.Pg.add(new b(this.Pa, this.Pf));
        UG(this.Ow, b4, b5);
        if (this.OB > 0.0f) {
            if (!b) {
                while (i < b5.Nl.length) {
                    final double[] nl = b5.Nl;
                    nl[i] *= 1.0f - this.OB;
                    ++i;
                }
            } else {
                for (int j = 0; j < b5.Nl.length; ++j) {
                    if (Math.random() < this.OB) {
                        b5.Nl[j] = 0.0;
                    }
                }
            }
        }
        return b5;
    }

    void UC() {
        super.UC();
        k.Up(this.Ov.Nl);
        k.Up(this.OD.Nl);
        k.Up(this.OA.Nl);
    }

    public void UD(final a a, final int n, final b b, final b b2, final b b3) throws InvalidValueException {
        final boolean b4 = true;
        b.TT(b, b2, this.Ph, false);
        this.Ux(this.Ow, this.Ph, (b) this.Pg.ST(n), b3);
        b.TO(this.Ph, this.UE(a), !this.Ox && b4, this.OY, false);
        if (this.OW) {
            b.TO(this.Ph, this.Oy, b4, this.OZ, false);
        }
        e.getInstance().Uc(this.Pa * this.Pf, new u(this, (ArrayList<com.google.research.reflection.common.c>[]) this.Pc.ST(n), (b) this.Pb.ST(n), (b) this.Pg.ST(n - 1)));
    }

    public b UE(final a a) {
        if (this.OE < 0) {
            return this.Ou;
        }
        return ((o) a.TF().get(this.OE)).Ou;
    }

    public b UN() {
        return this.Oz;
    }

    public String Uu() {
        return "LinearLayer";
    }

    public void Uv(final DataOutputStream dataOutputStream) throws IOException {
        super.Uv(dataOutputStream);
        dataOutputStream.writeInt(this.Ow);
        dataOutputStream.writeBoolean(this.Ox);
        this.Oy.TY(dataOutputStream);
        this.Oz.TY(dataOutputStream);
        dataOutputStream.writeInt(this.OE);
        if (this.OE < 0) {
            this.Ou.TY(dataOutputStream);
        }
        dataOutputStream.writeInt(this.OC);
        this.UQ(dataOutputStream);
    }

    public void Uw(final DataInputStream dataInputStream) throws IOException {
        super.Uw(dataInputStream);
        this.Ow = dataInputStream.readInt();
        this.Ox = dataInputStream.readBoolean();
        (this.Oy = new b()).TZ(dataInputStream);
        (this.Oz = new b()).TZ(dataInputStream);
        this.OE = dataInputStream.readInt();
        if (this.OE < 0) {
            (this.Ou = new b()).TZ(dataInputStream);
        }
        this.Ov = new b(this.Pe, this.Pf);
        this.OD = new b(this.Pf, this.Pf);
        this.OA = new b(1, this.Pf);
        this.OC = dataInputStream.readInt();
        this.UP(dataInputStream);
    }

    void Ux(final int n, final b b, final b b2, final b b3) throws InvalidValueException {
        e.getInstance().Uc(b.Nl.length, new t(this, n, b2, b));
    }

    public void Uz(final o o) {
        super.UW(o);
        o.Ox = this.Ox;
        o.Ow = this.Ow;
        o.Ou = this.Ou.clone();
        o.Ov = this.Ov.clone();
        o.Oy = this.Oy.clone();
        o.OD = this.OD.clone();
        o.Oz = this.Oz.clone();
        o.OA = this.OA.clone();
        o.OE = this.OE;
        o.OC = this.OC;
    }

    public o clone() {
        final o o = new o();
        super.UW(o);
        o.Ox = this.Ox;
        o.Ow = this.Ow;
        o.Ou = this.Ou.clone();
        o.Ov = this.Ov.clone();
        o.Oy = this.Oy.clone();
        o.OD = this.OD.clone();
        o.Oz = this.Oz.clone();
        o.OA = this.OA.clone();
        o.OE = this.OE;
        o.OC = this.OC;
        return o;
    }

    public void update() throws InvalidValueException {
        e.getInstance().Uc(this.Pf, new l(this));
    }

    static class g implements c {
        final /* synthetic */ int ND;
        final /* synthetic */ b NE;
        final /* synthetic */ b NF;

        g(final int nd, final b nf, final b ne) {
            this.ND = nd;
            this.NF = nf;
            this.NE = ne;
        }

        public Boolean Ub(final int n) {
            if (this.ND != 1) {
                this.NF.Nl[n] = k.Ut(this.NE.Nl[n]);
            } else {
                this.NF.Nl[n] = k.Uq(this.NE.Nl[n]);
            }
            return true;
        }
    }

    class l implements c {
        final /* synthetic */ o Oo;

        l(final o oo) {
            this.Oo = oo;
        }

        public Boolean Ub(final int n) {
            int i = 0;
            for (int j = 0; j < this.Oo.Pe; ++j) {
                this.Oo.Ou.TX(this.Oo.Ox, j, n, -v.UV() * this.Oo.Ov.Nl[j * this.Oo.Pf + n]);
            }
            if (this.Oo.OW) {
                while (i < this.Oo.Pf) {
                    final int n2 = this.Oo.Pf * i;
                    final double[] nl = this.Oo.Oy.Nl;
                    final int n3 = n2 + n;
                    nl[n3] -= v.UV() * this.Oo.OD.Nl[n2 + n];
                    ++i;
                }
            }
            if (!Double.isNaN(this.Oo.OA.Nl[n])) {
                final double[] nl2 = this.Oo.Oz.Nl;
                nl2[n] -= v.UV() * this.Oo.OA.Nl[n];
                return true;
            }
            throw new RuntimeException("NaN in bias gradients...");
        }
    }

    static class r implements c {
        final /* synthetic */ b OL;
        final /* synthetic */ b OM;

        r(final b ol, final b om) {
            this.OL = ol;
            this.OM = om;
        }

        public Boolean Ub(final int n) {
            k.Ur(this.OL, n, this.OM);
            return true;
        }
    }

    class t implements c {
        final /* synthetic */ int OP;
        final /* synthetic */ b OQ;
        final /* synthetic */ b OR;

        t(final o o, final int op, final b oq, final b or) {
            this.OP = op;
            this.OQ = oq;
            this.OR = or;
        }

        public Boolean Ub(final int n) {
            final double n2 = 0.0;
            if (this.OP != 1) {
                if (this.OP != 0) {
                    throw new RuntimeException("Unsupported activation function: " + this.OP);
                }
                this.OR.Nl[n] *= this.OQ.Nl[n] * (1.0 - this.OQ.Nl[n]);
            } else if (this.OQ.Nl[n] == n2) {
                this.OR.Nl[n] = n2;
            }
            return true;
        }
    }

    class u implements c {
        final /* synthetic */ b OS;
        final /* synthetic */ b OT;
        final /* synthetic */ o OU;
        final /* synthetic */ ArrayList<com.google.research.reflection.common.c>[] OV;

        u(final o ou, final ArrayList<com.google.research.reflection.common.c>[] ov, final b ot, final b os) {
            this.OU = ou;
            this.OV = ov;
            this.OT = ot;
            this.OS = os;
        }

        public Boolean Ub(final int n) {
            int i = 0;
            final int n2 = n / this.OU.Pf;
            final int n3 = n % this.OU.Pf;
            final double n4 = this.OU.Ph.Nl[this.OU.Pf * n2 + n3];
            if (!this.OU.Pd) {
                for (int j = 0; j < this.OU.Pe; ++j) {
                    final double[] nl = this.OU.Ov.Nl;
                    final int n5 = this.OU.Pf * j + n3;
                    nl[n5] += this.OT.Nl[this.OU.Pe * n2 + j] * n4;
                }
            } else {
                for (final com.google.research.reflection.common.c c : this.OV[n2]) {
                    final double[] nl2 = this.OU.Ov.Nl;
                    final int n6 = c.MV * this.OU.Pf + n3;
                    nl2[n6] += c.MW * n4;
                }
            }
            if (this.OU.OW && this.OS != null) {
                while (i < this.OU.Pf) {
                    final double[] nl3 = this.OU.OD.Nl;
                    final int n7 = this.OU.Pf * i + n3;
                    nl3[n7] += this.OS.Nl[this.OU.Pf * n2 + i] * n4;
                    ++i;
                }
            }
            final double[] nl4 = this.OU.OA.Nl;
            nl4[n3] += n4;
            return true;
        }
    }

    class x implements c {
        final /* synthetic */ b Pn;
        final /* synthetic */ b Po;
        final /* synthetic */ a Pp;
        final /* synthetic */ b Pq;
        final /* synthetic */ o Pr;
        final /* synthetic */ ArrayList<com.google.research.reflection.common.c>[] Ps;

        x(final o pr, final a pp, final ArrayList<com.google.research.reflection.common.c>[] ps, final b po, final b pn, final b pq) {
            this.Pr = pr;
            this.Pp = pp;
            this.Ps = ps;
            this.Po = po;
            this.Pn = pn;
            this.Pq = pq;
        }

        public Boolean Ub(final int n) {
            final int n2 = n / this.Pr.Pf;
            final int n3 = n % this.Pr.Pf;
            final b ue = this.Pr.UE(this.Pp);
            final int n4 = n2 * this.Pr.Pf;
            final double n5 = this.Pr.Oz.Nl[n3];
            double n6;
            if (!this.Pr.Pd) {
                n6 = n5;
                for (int i = 0; i < this.Pr.Pe; ++i) {
                    n6 += this.Po.TP(false, n2, i) * ue.TP(this.Pr.Ox, i, n3);
                }
            } else {
                final Iterator<com.google.research.reflection.common.c> iterator = this.Ps[n2].iterator();
                n6 = n5;
                while (iterator.hasNext()) {
                    final com.google.research.reflection.common.c c = iterator.next();
                    n6 += ue.TP(this.Pr.Ox, c.MV, n3) * c.MW;
                }
            }
            if (this.Pr.OW && this.Pn != null) {
                for (int j = 0; j < this.Pr.Pf; ++j) {
                    n6 += this.Pn.TP(false, n2, j) * this.Pr.Oy.TP(false, j, n3);
                }
            }
            this.Pq.Nl[n4 + n3] = n6;
            return true;
        }
    }
}
