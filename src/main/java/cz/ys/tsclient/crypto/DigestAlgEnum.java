package cz.ys.tsclient.crypto;

/**
 * Create digest enum
 *
 * @author Jan Simunek
 */
public enum DigestAlgEnum {

    HA_1("SHA-1", "1.3.14.3.2.26"),
    SHA256("SHA256", "2.16.840.1.101.3.4.2.1");

    private final String name;
    private final String oid;

    DigestAlgEnum(String name, String oid) {
        this.name = name;
        this.oid = oid;
    }

    public static DigestAlgEnum getByName(String name) {
        DigestAlgEnum[] arr$ = values();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            DigestAlgEnum m = arr$[i$];
            if (m.name.equals(name)) {
                return m;
            }
        }

        return null;
    }

    public static DigestAlgEnum getByOID(String oid) {
        DigestAlgEnum[] arr$ = values();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            DigestAlgEnum m = arr$[i$];
            if (m.oid.equals(oid)) {
                return m;
            }
        }

        return null;
    }

    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }

    public String getOid() {
        return this.oid;
    }
}
