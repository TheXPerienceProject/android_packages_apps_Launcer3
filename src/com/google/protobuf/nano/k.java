// 
// Decompiled by Procyon v0.5.30
// 

package com.google.protobuf.nano;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

class k implements Cloneable
{
    private List<j> Qa;
    private g Qb;
    private Object value;
    
    k() {
        this.Qa = new ArrayList();
    }
    
    private byte[] Xk() throws CodedOutputByteBufferNano.OutOfSpaceException {
        final byte[] array = new byte[this.computeSerializedSize()];
        this.writeTo(b.VJ(array));
        return array;
    }
    
    public final k clone() {
        k clone = new k();
        try {
            clone.Qb = Qb;
            if (Qa == null) {
                clone.Qa = null;
            } else {
                clone.Qa.addAll(Qa);
            }

            // Whether we need to deep clone value depends on its type. Primitive reference types
            // (e.g. Integer, Long etc.) are ok, since they're immutable. We need to clone arrays
            // and messages.
            if (value == null) {
                // No cloning required.
            } else if (value instanceof MessageNano) {
                clone.value = ((MessageNano) value).clone();
            } else if (value instanceof byte[]) {
                clone.value = ((byte[]) value).clone();
            } else if (value instanceof byte[][]) {
                byte[][] valueArray = (byte[][]) value;
                byte[][] cloneArray = new byte[valueArray.length][];
                clone.value = cloneArray;
                for (int i = 0; i < valueArray.length; i++) {
                    cloneArray[i] = valueArray[i].clone();
                }
            } else if (value instanceof boolean[]) {
                clone.value = ((boolean[]) value).clone();
            } else if (value instanceof int[]) {
                clone.value = ((int[]) value).clone();
            } else if (value instanceof long[]) {
                clone.value = ((long[]) value).clone();
            } else if (value instanceof float[]) {
                clone.value = ((float[]) value).clone();
            } else if (value instanceof double[]) {
                clone.value = ((double[]) value).clone();
            } else if (value instanceof MessageNano[]) {
                MessageNano[] valueArray = (MessageNano[]) value;
                MessageNano[] cloneArray = new MessageNano[valueArray.length];
                clone.value = cloneArray;
                for (int i = 0; i < valueArray.length; i++) {
                    cloneArray[i] = valueArray[i].clone();
                }
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
    
    int computeSerializedSize() {
        int size = 0;
        if (value != null) {
            size = Qb.WV(value);
        } else {
            for (j unknownField : Qa) {
                size += unknownField.computeSerializedSize();
            }
        }
        return size;
    }
    
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof k)) {
            return false;
        }
        final k k = (k)o;
        if (this.value != null && k.value != null) {
            if (this.Qb != k.Qb) {
                return false;
            }
            if (!this.Qb.PT.isArray()) {
                return this.value.equals(k.value);
            }
            if (this.value instanceof byte[]) {
                return Arrays.equals((byte[])this.value, (byte[])k.value);
            }
            if (this.value instanceof int[]) {
                return Arrays.equals((int[])this.value, (int[])k.value);
            }
            if (this.value instanceof long[]) {
                return Arrays.equals((long[])this.value, (long[])k.value);
            }
            if (this.value instanceof float[]) {
                return Arrays.equals((float[])this.value, (float[])k.value);
            }
            if (this.value instanceof double[]) {
                return Arrays.equals((double[])this.value, (double[])k.value);
            }
            if (this.value instanceof boolean[]) {
                return Arrays.equals((boolean[])this.value, (boolean[])k.value);
            }
            return Arrays.deepEquals((Object[])this.value, (Object[])k.value);
        }
        else {
            if (this.Qa != null && k.Qa != null) {
                return this.Qa.equals(k.Qa);
            }
            try {
                final byte[] xk = this.Xk();
                return Arrays.equals(xk, k.Xk());
            } catch (CodedOutputByteBufferNano.OutOfSpaceException e) {
                throw new IllegalStateException(e);
            }
        }
    }
    
    public int hashCode() {
        try {
            final byte[] xk = this.Xk();
            return Arrays.hashCode(xk) + 527;
        } catch (CodedOutputByteBufferNano.OutOfSpaceException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }
    
    void writeTo(final b b) throws CodedOutputByteBufferNano.OutOfSpaceException {
        if (this.value != null) {
            this.Qb.WS(this.value, b);
        }
        else {
            for (j unknownField : Qa) {
                unknownField.writeTo(b);
            }
        }
    }
}
