package com.fzc.androidtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.StringTokenizer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    BufferedReader reader;
    StringTokenizer tokenizer;

    private TrustManagerFactory trustManagerFactory;
    private X509Certificate[] x509Certificates = new X509Certificate[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OkHttpClient client = null;
        trustManagerFactory = provideTrustManagerFactory();
        try {
            client = new OkHttpClient.Builder()
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return "www.12306.cn".equals(hostname);
                        }
                    })
                    .sslSocketFactory(provideSslContext().getSocketFactory(), new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return x509Certificates;
                        }
                    })
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .url("https://www.12306.cn/mormhweb/")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                System.out.println("fuck fail");


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });

    }


    private InputStream provideCertificateInputStream() throws Exception {
        return getAssets().open("srca.cer");
    }

    private SSLContext provideSslContext() throws Exception {


        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers()
                , new SecureRandom());


        return sslContext;
    }

    private TrustManagerFactory provideTrustManagerFactory() {
        TrustManagerFactory trustManagerFactory = null;
        try {
            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(provideKeyStore());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trustManagerFactory;
    }

    private CertificateFactory provideX509CertificateFactory() {
        CertificateFactory certificateFactory = null;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
        } catch (Exception e) {

        }
        return certificateFactory;
    }

    private KeyStore provideKeyStore() {
        KeyStore keyStore = null;
        try {

            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            Certificate certificate = provideX509CertificateFactory()
                    .generateCertificate(provideCertificateInputStream());
            keyStore.setCertificateEntry("0", certificate);
            x509Certificates[0] = (X509Certificate) certificate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyStore;
    }
}
