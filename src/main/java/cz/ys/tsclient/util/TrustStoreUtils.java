package cz.ys.tsclient.util;

import javax.net.ssl.SSLException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Trust stores utils, helpers
 *
 * @author Jan Simunek
 */
public class TrustStoreUtils {

    private static final String KEYSTORE_PROPERTY_NAME = "javax.net.ssl.trustStore";

    private static final String TRUSTSTORE = "postsignum.truststore";
    private static final String TEMP_TRUSTSTORE_NAME = "ys-digistamp";
    private static final String TEMP_TRUSTSTORE_EXT = ".truststore";

    /**
     * Read keystore from input stream and set system property to use it.
     *
     * @param truststore
     * @throws SSLException
     */
    public static void loadTrustStore(InputStream truststore) throws SSLException {
        try {
            File tf = File.createTempFile(TEMP_TRUSTSTORE_NAME, TEMP_TRUSTSTORE_EXT);
            tf.deleteOnExit();
            byte buffer[] = new byte[0x1000];

            FileOutputStream out = new FileOutputStream(tf);
            int cnt;
            while ((cnt = truststore.read(buffer)) != -1) {
                out.write(buffer, 0, cnt);
            }
            truststore.close();
            out.close();

            System.setProperty(KEYSTORE_PROPERTY_NAME, tf.getAbsolutePath());
        } catch (Exception ex) {
            throw new SSLException("Cannot load truststore", ex);
        }
    }

    public static void loadTrustStore() throws SSLException {
        loadTrustStore(TrustStoreUtils.class.getResourceAsStream(TRUSTSTORE));
    }
}
