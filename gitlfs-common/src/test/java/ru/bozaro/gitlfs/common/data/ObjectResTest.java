package ru.bozaro.gitlfs.common.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;

/**
 * Test Meta deserialization.
 *
 * @author Artem V. Navrotskiy <bozaro@users.noreply.github.com>
 */
public class ObjectResTest {
  @NotNull
  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  public void parse01() throws IOException, ParseException, URISyntaxException {
    try (InputStream stream = getClass().getResourceAsStream("object-res-01.json")) {
      Assert.assertNotNull(stream);

      final ObjectRes meta = mapper.readValue(stream, ObjectRes.class);
      Assert.assertNotNull(meta);
      Assert.assertEquals(meta.getOid(), "01ba4719c80b6fe911b091a7c05124b64eeece964e09c058ef8f9805daca546b");
      Assert.assertEquals(meta.getSize(), Long.valueOf(130L));
      Assert.assertEquals(1, meta.getLinks().size());

      final Link link = meta.getLinks().get("upload");
      Assert.assertNotNull(link);
      Assert.assertEquals(link.getHref(), new URI("https://storage-server.com/OID"));
      Assert.assertEquals(link.getHeader(),
          ImmutableMap.builder()
              .put("Authorization", "Basic ...")
              .build()
      );
    }
  }
}
