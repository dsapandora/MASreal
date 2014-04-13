package com.dsalab.masreal.conexion;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * Clase que maneja la conexión de SSL y devuelve un tipo de cliente que administra las conexiones al servidor.
 *
 */
public class  MySSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");
    /**
     * Método constructor que crea la conexión segura.
     *
     * **/
    public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sslContext.init(null, new TrustManager[] { tm }, null);
    }

    /**
     * Crea el socket para la conexión.
     * @param socket Objeto socket que se customizara.
     * @param host url a donde se realizará la conexión
     * @param port puerto al cual se ejecutará la excepción.
     * @param autoClose indica si la conexión se cierra automaticamente.
     * @throws java.io.IOException
     *          Cuando hay errores de lectura/escritura en la conexión con el servidor.
     * */
    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws UnknownHostException,IOException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }
    /**
     * Crea un nuevo socket de forma segura.
     * @return Socket con la conexión segura.
     *  @throws  java.io.IOException
     *    Cuando hay errores en la escritura del socket.
     * */
    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }

    /**
     * Crea un HttpClient ya sea normal o con la conexión segura.
     * @return httpclient. Con la conexión segura o la conexión normal.
     * */
    public HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();

            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }
}