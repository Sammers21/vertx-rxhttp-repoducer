package io.vertx.starter;

import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@ExtendWith(VertxExtension.class)
public class HttpTest {

  @Test
  public void flowSaveWorks(VertxTestContext testContext) throws InterruptedException {
    Vertx vertx = Vertx.vertx();
    final CountDownLatch async = new CountDownLatch(2);
    final int port = vertx.createHttpServer().requestHandler(request -> {
      async.countDown();
      request.toFlowable().doOnTerminate(() -> {
        // this countDown is not called
        async.countDown();
      });
      vertx.setTimer(1000, event -> {
        request.response().end("OK");
      });
    }).listen().actualPort();
    WebClient.create(vertx).get(port, "localhost", "/hello").rxSend().blockingGet();
    async.await(10_000, TimeUnit.MILLISECONDS);
    if (async.getCount() == 1) {
      testContext.failNow(new IllegalStateException("Request flow has no end"));
    }
    vertx.close();
  }
}
