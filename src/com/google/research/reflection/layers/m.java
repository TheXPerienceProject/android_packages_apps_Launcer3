package com.google.research.reflection.layers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class m extends o {
    private int Op;

    public m() {
        this.Op = 0;
    }

    public m(final int op, final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final boolean b) {
        super(false, n, n2, n3, n4, n5, n6, n7, b, false, 0.0f);
        this.Op = 0;
        this.Op = op;
    }

    public String Uu() {
        return "OutputLayer";
    }

    public void Uv(final DataOutputStream dataOutputStream) throws IOException {
        super.Uv(dataOutputStream);
        dataOutputStream.writeInt(this.Op);
        this.UQ(dataOutputStream);
    }

    public void Uw(final DataInputStream dataInputStream) throws IOException {
        super.Uw(dataInputStream);
        this.Op = dataInputStream.readInt();
        this.UP(dataInputStream);
    }

    void Ux(final int n, final b b, final b b2, final b b3) throws InvalidValueException {
        e.getInstance().Uc(b.Nl.length, new d(this, n, b, b2, b3));
    }

    public m clone() {
        final m m = new m();
        super.Uz(m);
        m.Op = this.Op;
        return m;
    }

    class d implements c {
        final /* synthetic */ b Nn;
        final /* synthetic */ m No;
        final /* synthetic */ int Np;
        final /* synthetic */ b Nq;
        final /* synthetic */ b Nr;

        d(final m no, final int np, final b nr, final b nn, final b nq) {
            this.No = no;
            this.Np = np;
            this.Nr = nr;
            this.Nn = nn;
            this.Nq = nq;
        }

        public Boolean Ub(final int n) {
            final boolean b = true;
            if (this.Np != 0) {
                if (this.Np != 2) {
                    throw new RuntimeException("unsupported activation function for the output layer");
                }
                this.Nr.Nl[n] = this.Nn.Nl[n] - this.Nr.Nl[n];
            } else if (this.No.Op != 0) {
                if (this.No.Op == (b ? 1 : 0)) {
                    this.Nr.Nl[n] = this.Nn.Nl[n] - this.Nr.Nl[n];
                }
            } else {
                this.Nr.Nl[n] = this.Nn.Nl[n] * (1.0 - this.Nn.Nl[n]) * (this.Nn.Nl[n] - this.Nr.Nl[n]);
            }
            if (this.Nq != null) {
                final double[] nl = this.Nr.Nl;
                nl[n] *= this.Nq.Nl[n];
            }
            return b;
        }
    }
}
