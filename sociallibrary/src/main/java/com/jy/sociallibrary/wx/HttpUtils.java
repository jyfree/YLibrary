package com.jy.sociallibrary.wx;

import android.graphics.Bitmap;

import com.jy.sociallibrary.utils.SDKLogUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpUtils {

    private static final String TAG = "SDK_HttpUtil";

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle, final int maxSize) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int quality = 100;
        bmp.compress(Bitmap.CompressFormat.JPEG, quality, output);

        int outputLength = output.toByteArray().length;
        while (outputLength / 1024 > maxSize) {
            output.reset();
            if (quality >= 10) {
                quality = quality - 10;
            }
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, output);
            outputLength = output.toByteArray().length;
            if (quality == 0) break;
        }

        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        SDKLogUtils.i("compress bitmap", result.length / 1024, "KB");
        return result;
    }

    public static byte[] httpPost(String url, String entity) {
        InputStream inStream = null;
        HttpURLConnection httpConnection = null;
        try {
            URL htmlUrl = new URL(url);
            if (htmlUrl.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) htmlUrl.openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                httpConnection = https;
            } else {
                httpConnection = (HttpURLConnection) htmlUrl.openConnection();
            }
            httpConnection.setRequestMethod("POST");
            DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
            wr.writeBytes(entity);
            wr.flush();
            wr.close();
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != httpConnection) {
                httpConnection.disconnect();
            }
        }
        return inputStreamToByte(inStream);
    }

    private static byte[] inputStreamToByte(InputStream inStream) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            int ch;
            while ((ch = inStream.read()) != -1) {
                byteStream.write(ch);
            }
            byte imgData[] = byteStream.toByteArray();
            byteStream.close();
            return imgData;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != inStream) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 信任所有服务器，无需验证证书
     */
    public static void trustAllHosts() {
        final String TAG = "trustAllHosts";
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
}
