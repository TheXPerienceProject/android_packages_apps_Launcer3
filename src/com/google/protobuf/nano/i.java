//
// Decompiled by Procyon v0.5.30
//

package com.google.protobuf.nano;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import static javax.xml.transform.OutputKeys.INDENT;

public final class i
{
    /**
     }
     * Function that will print the given message/field into the StringBuffer.
     * Meant to be called recursively.
     *
     * @param identifier the identifier to use, or {@code null} if this is the root message to
     *        print.
     * @param object the value to print. May in fact be a primitive value or byte array and not a
     *        message.
     * @param indentBuf the indentation each line should begin with.
     * @param buf the output buffer.
     */
    private static void Xe(String identifier, Object object,
                           StringBuffer indentBuf, StringBuffer buf) throws IllegalAccessException,
            InvocationTargetException {
        if (object == null) {
            // This can happen if...
            //   - we're about to print a message, String, or byte[], but it not present;
            //   - we're about to print a primitive, but "reftype" optional style is enabled, and
            //     the field is unset.
            // In both cases the appropriate behavior is to output nothing.
        } else if (object instanceof MessageNano) {  // Nano proto message
            int origIndentBufLength = indentBuf.length();
            if (identifier != null) {
                buf.append(indentBuf).append(Xg(identifier)).append(" <\n");
                indentBuf.append(INDENT);
            }
            Class<?> clazz = object.getClass();

            // Proto fields follow one of two formats:
            //
            // 1) Public, non-static variables that do not begin or end with '_'
            // Find and print these using declared public fields
            for (Field field : clazz.getFields()) {
                int modifiers = field.getModifiers();
                String fieldName = field.getName();
                if ("cachedSize".equals(fieldName)) {
                    // TODO(bduff): perhaps cachedSize should have a more obscure name.
                    continue;
                }

                if ((modifiers & Modifier.PUBLIC) == Modifier.PUBLIC
                        && (modifiers & Modifier.STATIC) != Modifier.STATIC
                        && !fieldName.startsWith("_")
                        && !fieldName.endsWith("_")) {
                    Class<?> fieldType = field.getType();
                    Object value = field.get(object);

                    if (fieldType.isArray()) {
                        Class<?> arrayType = fieldType.getComponentType();

                        // bytes is special since it's not repeated, but is represented by an array
                        if (arrayType == byte.class) {
                            Xe(fieldName, value, indentBuf, buf);
                        } else {
                            int len = value == null ? 0 : Array.getLength(value);
                            for (int i = 0; i < len; i++) {
                                Object elem = Array.get(value, i);
                                Xe(fieldName, elem, indentBuf, buf);
                            }
                        }
                    } else {
                        Xe(fieldName, value, indentBuf, buf);
                    }
                }
            }

            // 2) Fields that are accessed via getter methods (when accessors
            //    mode is turned on)
            // Find and print these using getter methods.
            for (Method method : clazz.getMethods()) {
                String name = method.getName();
                // Check for the setter accessor method since getters and hazzers both have
                // non-proto-field name collisions (hashCode() and getSerializedSize())
                if (name.startsWith("set")) {
                    String subfieldName = name.substring(3);

                    Method hazzer = null;
                    try {
                        hazzer = clazz.getMethod("has" + subfieldName);
                    } catch (NoSuchMethodException e) {
                        continue;
                    }
                    // If hazzer doesn't exist or returns false, no need to continue
                    if (!(Boolean) hazzer.invoke(object)) {
                        continue;
                    }

                    Method getter = null;
                    try {
                        getter = clazz.getMethod("get" + subfieldName);
                    } catch (NoSuchMethodException e) {
                        continue;
                    }

                    Xe(subfieldName, getter.invoke(object), indentBuf, buf);
                }
            }
            if (identifier != null) {
                indentBuf.setLength(origIndentBufLength);
                buf.append(indentBuf).append(">\n");
            }
        } else if (object instanceof Map) {
            Map<?,?> map = (Map<?,?>) object;
            identifier = Xg(identifier);

            for (Map.Entry<?,?> entry : map.entrySet()) {
                buf.append(indentBuf).append(identifier).append(" <\n");
                int origIndentBufLength = indentBuf.length();
                indentBuf.append(INDENT);
                Xe("key", entry.getKey(), indentBuf, buf);
                Xe("value", entry.getValue(), indentBuf, buf);
                indentBuf.setLength(origIndentBufLength);
                buf.append(indentBuf).append(">\n");
            }
        } else {
            // Non-null primitive value
            identifier = Xg(identifier);
            buf.append(indentBuf).append(identifier).append(": ");
            if (object instanceof String) {
                String stringMessage = Xf((String) object);
                buf.append("\"").append(stringMessage).append("\"");
            } else if (object instanceof byte[]) {
                appendQuotedBytes((byte[]) object, buf);
            } else {
                buf.append(object);
            }
            buf.append("\n");
        }
    }

    private static String Xf(String string) {
        final int n = 200;
        if (!string.startsWith("http") && string.length() > n) {
            string = string.substring(0, n) + "[...]";
        }
        return Xj(string);
    }

    private static String Xg(final String s) {
        int i = 0;
        final StringBuffer sb = new StringBuffer();
        while (i < s.length()) {
            final char char1 = s.charAt(i);
            if (i == 0) {
                sb.append(Character.toLowerCase(char1));
            }
            else if (Character.isUpperCase(char1)) {
                sb.append('_').append(Character.toLowerCase(char1));
            }
            else {
                sb.append(char1);
            }
            ++i;
        }
        return sb.toString();
    }

    public static String Xh(final a a) {
        if (a == null) {
            return "";
        }
        final StringBuffer sb = new StringBuffer();
        try {
            Xe(null, a, new StringBuffer(), sb);
            return sb.toString();
        }
        catch (InvocationTargetException ex) {
            return "Error printing proto: " + ex.getMessage();
        }
        catch (IllegalAccessException ex2) {
            return "Error printing proto: " + ex2.getMessage();
        }
    }

    private static void appendQuotedBytes(final byte[] array, final StringBuffer sb) {
        final char c = '\\';
        final char c2 = '\"';
        if (array == null) {
            sb.append("\"\"");
            return;
        }
        sb.append(c2);
        for (int i = 0; i < array.length; ++i) {
            final int n = array[i] & 0xFF;
            if (n == c || n == c2) {
                sb.append(c).append((char)n);
            }
            else if (n >= ' ' && n < '\u007f') {
                sb.append((char)n);
            }
            else {
                sb.append(String.format("\\%03o", n));
            }
        }
        sb.append(c2);
    }

    private static String Xj(final String s) {
        final int length = s.length();
        final StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            final char char1 = s.charAt(i);
            if (char1 >= ' ' && char1 <= '~' && char1 != '\"' && char1 != '\'') {
                sb.append(char1);
            }
            else {
                sb.append(String.format("\\u%04x", (int)char1));
            }
        }
        return sb.toString();
    }
}
