package io.vertx.starter;

import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;

public class HttpTest {

  @Test
  public void httpRequestFlow() throws IOException {
    Vertx vertx = Vertx.vertx();
    final int port = rndPort();
    final String str = "123";
    vertx.createHttpServer().requestHandler(request -> {
      request.response().end(str);
    }).rxListen(port)
      .blockingGet();
    final String resp = WebClient.create(vertx).get(port, "127.0.0.1", "/hello")
      .rxSend()
      .blockingGet()
      .bodyAsString();
    Assertions.assertEquals(str, resp);
    vertx.close();
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
