package cz.ys.tsclient.util;

/**
 * @author Jan Simunek
 */
public class Base64Tools {

    private static final char[] base64CodeTable = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/', '='};

    public Base64Tools() {
    }

    public static String toBase64(byte[] src) {
        StringBuffer ret = new StringBuffer(src.length * 2);

        int i;
        int x;
        for (i = 0; i < src.length - 2; i += 3) {
            ret.append(base64CodeTable[src[i] >>> 2 & 63]);
            x = (src[i] & 3) << 4 & 63;
            x |= src[i + 1] >>> 4 & 15;
            ret.append(base64CodeTable[x]);
            x = (src[i + 1] & 15) << 2;
            x |= src[i + 2] >>> 6 & 3;
            ret.append(base64CodeTable[x]);
            ret.append(base64CodeTable[src[i + 2] & 63]);
        }

        if (i > 2) {
            switch (src.length % 3) {
                case 1:
                    ret.append(base64CodeTable[src[i] >>> 2 & 63]);
                    ret.append(base64CodeTable[(src[i] & 3) << 4 & 63]);
                    ret.append('=');
                    ret.append('=');
                    break;
                case 2:
                    ret.append(base64CodeTable[src[i] >>> 2 & 63]);
                    x = (src[i] & 3) << 4 & 63;
                    x |= src[i + 1] >>> 4 & 15;
                    ret.append(base64CodeTable[x]);
                    x = (src[i + 1] & 15) << 2;
                    ret.append(base64CodeTable[x]);
                    ret.append('=');
            }
        }

        return ret.toString();
    }
}
