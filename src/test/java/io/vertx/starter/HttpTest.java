package io.vertx.starter;

import io.reactivex.Flowable;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.web.client.WebClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.net.ServerSocket;

@ExtendWith(VertxExtension.class)
public class HttpTest {

  @Test
  public void httpRequestFlow(VertxTestContext ctx) throws IOException {
    Vertx vertx = Vertx.vertx();
    final int port = rndPort();
    final Checkpoint responseReceived = ctx.checkpoint(1);
    final String str = "123";
    vertx.createHttpServer().requestHandler(request -> {
      Flowable.fromArray(
        Buffer.buffer(str.getBytes())
      ).subscribe(request.response().toSubscriber());
    }).rxListen(port)
      .blockingGet();
    WebClient.create(vertx).get(port, "127.0.0.1", "/hello")
      .send(ctx.succeeding(event -> {
        String response = event.bodyAsString();
        Assertions.assertEquals(str, response);
        responseReceived.flag();
      }));
  }

  /**
   * Find a random port.
   *
   * @return The free port.
   * @throws IOException If fails.
   */
  private int rndPort() throws IOException {
    try (ServerSocket socket = new ServerSocket(0)) {
      return socket.getLocalPort();
    }
  }
}
