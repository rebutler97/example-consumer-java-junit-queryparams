package com.example.products;

import org.junit.jupiter.api.extension.ExtendWith;
import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "pactflow-example-provider-springboot")
public class ProductsPactTest {

  @Pact(consumer="pactflow-example-consumer-java-junit")
  public RequestResponsePact getProduct(PactDslWithProvider builder) {

    PactDslJsonBody body = new PactDslJsonBody();
    body.stringType("name", "product name");
    body.stringType("type", "product series");
    body.stringType("id", "5cc989d0-d800-434c-b4bb-b1268499e850");

      return builder
        .given("a product with ID 10 exists")
        .uponReceiving("a request to get a product")
          .path("/product/10")
          .method("GET")
          .matchQuery("param", "[A-Za-z0-9]{1,20}")
        .willRespondWith()
          .status(200)
          .body(body)
        .toPact();
    }

  @PactTestFor(pactMethod = "getProduct")
  @Test
  public void testGetProduct(MockServer mockServer) throws IOException {
    Product product = new ProductClient().setUrl(mockServer.getUrl()).getProduct("10");

    assertThat(product.getId(), is("5cc989d0-d800-434c-b4bb-b1268499e850"));
  }
}