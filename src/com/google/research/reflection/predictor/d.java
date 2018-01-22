package com.google.research.reflection.predictor;

import com.google.research.reflection.a.b;
import com.google.research.reflection.common.UncertaintyException;
import com.google.research.reflection.common.a;
import com.google.research.reflection.common.e;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class d {
    protected boolean MB;
    private int MC;
    protected a MD;
    private long ME_msInHour;

    public d() {
        this.ME_msInHour = 3600000L;
        this.MC = 100;
        this.MD = new a(this.MC, false);
    }

    public static d SA(final String s, final b b) {
        d sc = new d();
        if (s != null) {
            sc = SC(s.getBytes(StandardCharsets.ISO_8859_1), b);
        }
        return sc;
    }

    public static d SC(final byte[] array, final b b) {
        final d d = new d();
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
        final DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        try {
            d.SB(dataInputStream, b);
            dataInputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return d;
    }

    public static String SF(final d d) {
        if (d == null) {
            return null;
        }
        return new String(SH(d), StandardCharsets.ISO_8859_1);
    }

    public static byte[] SH(final d d) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            d.SD(dataOutputStream);
            dataOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void SB(final DataInputStream dataInputStream, final b b) throws IOException {
        byte[] array = null;
        this.clear();
        for (int int1 = dataInputStream.readInt(), i = 0; i < int1; ++i) {
            final int int2 = dataInputStream.readInt();
            if (array == null || array.length < int2) {
                array = new byte[int2];
            }
            dataInputStream.read(array, 0, int2);
            final b j = b.I(array, 0, int2);
            if (this.SG(j)) {
                this.Sz(j);
            }
        }
    }

    public void SD(final DataOutputStream dataOutputStream) throws IOException {
        final int size = this.size();
        dataOutputStream.writeInt(size);
        for (int i = 0; i < size; ++i) {
            final byte[] r = ((b) this.MD.ST(i)).R();
            dataOutputStream.writeInt(r.length);
            dataOutputStream.write(r, 0, r.length);
        }
        this.MB = false;
    }

    public a SE() {
        return this.MD;
    }

    protected boolean SG(final b b) {
        boolean b2 = false;
        if (b.Q() == null || b.Q().equals("") || b.Q().equals("app_launch")) {
            b2 = true;
        }
        return b2;
    }

    public void Sz(final b b) {
        if (!this.SG(b)) {
            return;
        }

        while (this.MD.SV() > 0) {
            long tn;
            try {
                b ST = (b) this.MD.ST(0);
                tn = e.Tn(ST, b);
            } catch (UncertaintyException ex) {
                tn = Long.MAX_VALUE;
            }

            if (tn <= this.ME_msInHour) {
                break;
            }
            this.MD.SW();
        }
        this.MD.add(b);
        this.MB = true;
    }

    public void clear() {
        this.MD.clear();
    }

    public int size() {
        return this.MD.SV();
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("(size ");
        sb.append(this.MD.SV());
        sb.append("): ");
        for (int i = 0; i < this.MD.SV(); ++i) {
            sb.append(((b) this.MD.ST(i)).getId());
            sb.append(" ");
        }
        sb.append("\n");
        return sb.toString();
    }
}
