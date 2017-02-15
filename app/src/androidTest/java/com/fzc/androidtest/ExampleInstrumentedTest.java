package com.fzc.androidtest;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.concurrent.CountDownLatch;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {


    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.fzc.androidtest", appContext.getPackageName());
    }

    @Test
    public void testReadAssets() throws Exception {
        InputStream inputStream = provideCertificate();
        System.out.println("fuck");
    }

    @Test
    public void testRailway() throws Exception {
        System.out.println("test rail way");
        final CountDownLatch latch = new CountDownLatch(1);
        final Context context = InstrumentationRegistry.getContext();

        SocketFactory socketFactory = provideSslContext().getSocketFactory();

        OkHttpClient client = new OkHttpClient.Builder()
                .socketFactory(socketFactory)
                .build();

        Request request = new Request.Builder()
                .url("https://www.12306.cn/mormhweb/")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                latch.countDown();
                e.printStackTrace();
                System.out.println("fuck fail");
                assertEquals("exception", e, null);


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                latch.countDown();
                System.out.println(response.body().string());
            }
        });

        latch.await();
    }

    private static InputStream provideCertificate() throws Exception {
        final Context context = InstrumentationRegistry.getContext();
        return context.getAssets().open("srca.cer");
    }

    private static SSLContext provideSslContext() throws Exception {

        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null);
        keyStore.setCertificateEntry("0", certificateFactory.generateCertificate(provideCertificate()));

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
        return sslContext;
    }
}
