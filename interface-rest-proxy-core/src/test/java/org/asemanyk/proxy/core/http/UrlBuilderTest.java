package org.asemanyk.proxy.core.http;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UrlBuilderTest {

  public static final String BASE_URL = "https://example.com/";
  public static final String BASE_PATH = "/examples";
  public static final String PATH_WITH_VAR = "/{id}";

  @Test
  void verifyBaseUrlOnly() {
    UrlBuilder urlBuilder = new UrlBuilder(BASE_URL);

    assertThat(urlBuilder.toString()).isEqualTo("https://example.com/");
  }

  @Test
  void verifyUrlWithPath() {
    UrlBuilder urlBuilder = new UrlBuilder(BASE_URL)
        .addPathSegment(BASE_PATH);

    assertThat(urlBuilder.toString()).isEqualTo("https://example.com/examples");
  }

  @Test
  void verifyUrlWithPathVar() {
    UrlBuilder urlBuilder = new UrlBuilder(BASE_URL)
        .addPathSegment(BASE_PATH)
        .addPathSegment(PATH_WITH_VAR)
        .addPathParameter("id", "foo");

    assertThat(urlBuilder.toString()).isEqualTo("https://example.com/examples/foo");
  }

  @Test
  void verifyUrlWithQuery() {
    UrlBuilder urlBuilder = new UrlBuilder(BASE_URL)
        .addPathSegment(BASE_PATH)
        .addQueryParameter("id", "foo")
        .addQueryParameter("bar", "bar");

    assertThat(urlBuilder.toString()).isEqualTo("https://example.com/examples?id=foo&bar=bar");
  }

}
