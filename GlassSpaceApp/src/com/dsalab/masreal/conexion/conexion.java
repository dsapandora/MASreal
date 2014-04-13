package com.dsalab.masreal.conexion;

/**
 * Created by arielvernaza on 02/01/14.
 */
import android.content.Context;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;


/**
 * Clase que realiza la conexión del servidor.
 */
public class conexion {
   static String URL = "http://marsweather.ingenology.com/v1/latest/";

    /**
     * método que realiza el post de la información en el servidor y recibe una respuesta de este.
     * @param actual contexto de la aplicación o servicio que realiza el envió.
     * @param nameValuePairs valores que serán enviado al servidor.
     * @return HttpResponse respuesta del servidor.
     * */
    public static HttpResponse postCedula(Context actual, List<NameValuePair> nameValuePairs)
    {

        HttpClient httpclient = new DefaultHttpClient();

        KeyStore ks;
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException e) {
            e.printStackTrace();
            ks = null;
        }
           MySSLSocketFactory nuevo;
        try {
            nuevo = new MySSLSocketFactory(ks);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            nuevo= null;
        } catch (KeyManagementException e) {
            e.printStackTrace();
            nuevo= null;
        } catch (KeyStoreException e) {
            e.printStackTrace();
            nuevo= null;
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
            nuevo= null;
        }
        httpclient = nuevo.getNewHttpClient();
        HttpPost httppost = new HttpPost(URL);
        httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);


        Log.e("URL", URL);

        try {
            //Coloca los parametros para hacer la Consulta
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httppost.setHeader("User-Agent", "DSALAB_ANDROID");
            Log.e("ENTIDAD", EntityUtils.toString(httppost.getEntity()));
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();





            return response;
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            Log.e("Protocolo", e.toString());
           // Toast.makeText(actual, "La conexión se ha perdido, consulte con su proveedor de Internet", Toast.LENGTH_SHORT).show();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("Protocolo", e.toString());
           // Toast.makeText(actual, "La conexión se ha perdido, consulte con su proveedor de Internet. ", Toast.LENGTH_SHORT).show();
            return null;

        } catch(IllegalArgumentException e) {
            // TODO Auto-generated catch block
            Log.e("Protocolo", e.toString());
            //Toast.makeText(actual, "La conexión se ha perdido, consulte con su proveedor de Internet: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }


    }

}
