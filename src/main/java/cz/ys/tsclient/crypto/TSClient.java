package cz.ys.tsclient.crypto;

import cz.ys.tsclient.util.Base64Tools;
import cz.ys.tsclient.util.Utils;
import org.bouncycastle.tsp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Generate timestamp class
 *
 * @author Jan Simunek
 */
public class TSClient {

    public static final String POSTSIGNUM_TSA = "https://www3.postsignum.cz/DEMOTSA/TSS_user/";

    private String username;
    private String password;
    private URL tsaUrl;

    private TimeStampToken token;

    private static Logger logger = LoggerFactory.getLogger(TSClient.class);

    /* Constructors */
    public TSClient() {
    }

    public TSClient(String username, String password, String tsaUrl) throws MalformedURLException {
        this(username, password, new URL(tsaUrl));
    }

    public TSClient(String username, String password, URL tsaUrl) {
        this();
        this.username = username;
        this.password = password;
        this.tsaUrl = tsaUrl;
    }

    /**
     * Generate time stamp for given filename and save to the same directory as original file.
     *
     * @param file
     * @param digestAlg
     * @return
     * @throws IOException
     * @throws TSPException
     * @throws GeneralSecurityException
     */
    public int stamp(String file, DigestAlgEnum digestAlg) throws IOException, TSPException, GeneralSecurityException {
        // Generate file digest
        byte[] data = this.getFileDigestData(file, digestAlg);

        // Init
        Random random = new Random();
        BigInteger nonse = new BigInteger(16, random);
        String shaId = digestAlg.getOid();
        TimeStampRequestGenerator tsrng = new TimeStampRequestGenerator();
        tsrng.setCertReq(true);
        TimeStampRequest tsreq = tsrng.generate(shaId, data, nonse);
        BufferedOutputStream buf = new BufferedOutputStream(new FileOutputStream(Utils.getTSQFilename(file)));
        buf.write(tsreq.getEncoded());
        buf.close();
        int result = 999;
        TimeStampResponse tsresp;

        // Call postsignum service
        if (tsaUrl != null) {

            // HTTP connection init
            logger.debug("Open connection to " + tsaUrl);
            HttpURLConnection conn = (HttpURLConnection) tsaUrl.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setDoOutput(true);
            conn.setUseCaches(true);
            conn.setRequestMethod("POST");

            String dos = getUsername() + ":" + getPassword();
            String reqData = Base64Tools.toBase64(dos.getBytes());
            conn.setRequestProperty("Authorization", "Basic " + reqData);

            conn.setRequestProperty("Content-Type", "application/timestamp-query");
            DataOutputStream dos1 = new DataOutputStream(conn.getOutputStream());
            byte[] reqData1 = tsreq.getEncoded();
            dos1.write(reqData1);
            InputStream is = conn.getInputStream();
            tsresp = new TimeStampResponse(is);
            result = tsresp.getStatus();
            tsresp.validate(tsreq);
            if (result >= 2) {
                throw new GeneralSecurityException("Server returned pkiStatus=" + result);
            }

            this.token = tsresp.getTimeStampToken();

            // Write generated time stamp to file
            String tsrFilename = Utils.getTSRFilename(file);
            logger.info("Saving time stamp to " + tsrFilename);

            FileOutputStream fileOutputStream = new FileOutputStream(tsrFilename);
            buf = new BufferedOutputStream(fileOutputStream);
            buf.write(this.token.getEncoded());
            buf.close();
        }

        return result;
    }

    /**
     * Generate digest for given file.
     *
     * @param file
     * @param digestAlg
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private byte[] getFileDigestData(String file, DigestAlgEnum digestAlg) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance(digestAlg.getName());
        DigestInputStream dstream = new DigestInputStream(new FileInputStream(file), md);
        byte[] buff = new byte[dstream.available()];
        dstream.read(buff);
        md = dstream.getMessageDigest();
        byte[] data = md.digest();
        dstream.close();
        return data;
    }

    /* Getters and setters */

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public URL getTsaUrl() {
        return tsaUrl;
    }

    public void setTsaUrl(URL tsaUrl) {
        this.tsaUrl = tsaUrl;
    }
}
