package ru.bozaro.gitlfs.common.client.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * GET object request.
 *
 * @author Artem V. Navrotskiy <bozaro@users.noreply.github.com>
 */
public class ObjectGet implements Request<InputStream> {
  @NotNull
  @Override
  public HttpMethod createRequest(@NotNull ObjectMapper mapper, @NotNull String url) {
    return new GetMethod(url);
  }

  @Override
  public InputStream processResponse(@NotNull ObjectMapper mapper, @NotNull HttpMethod request) throws IOException {
    return request.getResponseBodyAsStream();
  }
}
