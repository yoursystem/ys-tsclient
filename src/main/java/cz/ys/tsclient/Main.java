package cz.ys.tsclient;

import cz.ys.tsclient.crypto.DigestAlgEnum;
import cz.ys.tsclient.crypto.TSClient;
import cz.ys.tsclient.util.TrustStoreUtils;
import org.bouncycastle.tsp.TSPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Main class for testing.
 *
 * @author Jan Simunek
 */
public class Main {

    public static void main(String[] args) throws IOException, TSPException, GeneralSecurityException {
        Logger logger = LoggerFactory.getLogger(Main.class);
        logger.info("Start to generate TS");

        // Load truststore with certificates needed for SSL connection to Postsignum
        TrustStoreUtils.loadTrustStore();

        // Init client, auth using username and password, postsignum authority
        TSClient tsClient = new TSClient("demoTSA", "demoTSA2010", TSClient.POSTSIGNUM_TSA);

        // Generate time stamp for sample zip file
        String sample_file = "./sample/export.zip";
        tsClient.stamp(sample_file, DigestAlgEnum.HA_1);

        logger.info("TS has been generated");
    }


}
