package com.fzc.androidtest;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subscribers.TestSubscriber;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

import static junit.framework.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {


    OkHttpClient client;
    Retrofit retrofit;

//    interface apiService{
//        public Observable<>
//    }


    @Before
    public void prepare() {
        client = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .build();


//        retrofit = new Retrofit.Builder()
//                .client(client)
//                .baseUrl("http://www.12306.cn/")
//                .build();

    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testAsyncTask() throws Exception {
        final TestScheduler testScheduler = new TestScheduler();
        final TestSubscriber<String> subscriber = new TestSubscriber<String>();

//        testScheduler.advanceTimeBy(2000,TimeUnit.MILLISECONDS);
        Flowable.just("1", "2")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println(s);
                    }
                });

    }

    @Test
    public void testRail() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        SSLContext sslContext = SSLContext.getInstance("TLS");

        SocketFactory socketFactory = sslContext.getSocketFactory();




        client = client.newBuilder()
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
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                latch.countDown();
                System.out.println(response.body().string());
            }
        });

        latch.await();
    }

//    private InputStream trustedCertificatesInputStream() {
////        ... // Full source omitted. See sample.
//    }
//
//    public SSLContext sslContextForTrustedCertificates(InputStream in) {
//
//
//    }
}