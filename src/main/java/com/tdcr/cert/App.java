package com.tdcr.cert;

import javax.net.ssl.*;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // configure the SSLContext with a TrustManager
        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
            SSLContext.setDefault(ctx);

            URL url = new URL("https://github.com");//https://mms.nw.ru
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
            System.out.println(conn.getResponseCode());
            Certificate[] certs = conn.getServerCertificates();
            for (Certificate cert :certs){
                System.out.println(cert.getType());
                if(cert instanceof X509Certificate){
                    X509Certificate certificate = (X509Certificate) cert;
                    System.out.println(certificate.getSubjectDN());
                    System.out.println(certificate.getNotAfter());
                }
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
