package ru.bozaro.gitlfs.client;

import com.google.common.collect.ImmutableMap;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.bozaro.gitlfs.common.JsonHelper;
import ru.bozaro.gitlfs.common.data.Error;
import ru.bozaro.gitlfs.common.data.*;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

/**
 * Replay tests for https://github.com/github/git-lfs/blob/master/docs/api/http-v1-batch.md
 *
 * @author Artem V. Navrotskiy
 */
public class ClientBatchTest {
  /**
   * Simple upload.
   */
  @Test
  public void batchUpload01() throws IOException {
    batchUpload("/ru/bozaro/gitlfs/client/batch-upload-01.yml", false);
  }

  /**
   * Simple upload (JFrog Artifactory).
   */
  @Test
  public void batchUpload02() throws IOException {
    batchUpload("/ru/bozaro/gitlfs/client/batch-upload-02.yml", false);
  }

  @Test
  public void batchUploadChunked() throws IOException {
    batchUpload("/ru/bozaro/gitlfs/client/batch-upload-chunked.yml", true);
  }

  private void batchUpload(@Nonnull String path, boolean chunked) throws IOException {
    final HttpReplay replay = YamlHelper.createReplay(path);
    final Client client = new Client(new FakeAuthProvider(chunked), replay);
    final BatchRes result = client.postBatch(new BatchReq(
        Operation.Upload,
        Arrays.asList(
            new Meta("b810bbe954d51e380f395de0c301a0a42d16f115453f2feb4188ca9f7189074e", 28),
            new Meta("1cbec737f863e4922cee63cc2ebbfaafcd1cff8b790d8cfd2e6a5d550b648afa", 3)
        )
    ));
    Assert.assertEquals(JsonHelper.mapper.writeValueAsString(result), JsonHelper.mapper.writeValueAsString(new BatchRes(
        Arrays.asList(
            new BatchItem(
                new Meta("b810bbe954d51e380f395de0c301a0a42d16f115453f2feb4188ca9f7189074e", 28),
                ImmutableMap.<LinkType, Link>builder()
                    .build()
            ),
            new BatchItem(
                new Meta("1cbec737f863e4922cee63cc2ebbfaafcd1cff8b790d8cfd2e6a5d550b648afa", 3),
                ImmutableMap.<LinkType, Link>builder()
                    .put(LinkType.Upload, new Link(
                        URI.create("https://github-cloud.s3.amazonaws.com/alambic/media/111975537/1c/be/1cbec737f863e4922cee63cc2ebbfaafcd1cff8b790d8cfd2e6a5d550b648afa?actor_id=2458138"),
                        ImmutableMap.<String, String>builder()
                            .put("Authorization", "AWS4-HMAC-SHA256 Credential=Token-2")
                            .put("x-amz-content-sha256", "1cbec737f863e4922cee63cc2ebbfaafcd1cff8b790d8cfd2e6a5d550b648afa")
                            .put("x-amz-date", "20151007T190730Z")
                            .build(),
                        null
                    ))
                    .put(LinkType.Verify, new Link(
                        URI.create("https://api.github.com/lfs/bozaro/test/objects/1cbec737f863e4922cee63cc2ebbfaafcd1cff8b790d8cfd2e6a5d550b648afa/verify"),
                        ImmutableMap.<String, String>builder()
                            .put("Accept", "application/vnd.git-lfs+json")
                            .put("Authorization", "RemoteAuth Token-3")
                            .build(),
                        null
                    ))
                    .build()
            )
        )
    )));
    replay.close();
  }

  /**
   * Simple download.
   */
  @Test
  public void batchDownload01() throws IOException {
    batchDownload("/ru/bozaro/gitlfs/client/batch-download-01.yml");
  }

  private void batchDownload(@Nonnull String path) throws IOException {
    final HttpReplay replay = YamlHelper.createReplay(path);
    final Client client = new Client(new FakeAuthProvider(false), replay);
    final BatchRes result = client.postBatch(new BatchReq(
        Operation.Download,
        Arrays.asList(
            new Meta("b810bbe954d51e380f395de0c301a0a42d16f115453f2feb4188ca9f7189074e", 28),
            new Meta("1cbec737f863e4922cee63cc2ebbfaafcd1cff8b790d8cfd2e6a5d550b648afa", 3)
        )
    ));
    Assert.assertEquals(JsonHelper.mapper.writeValueAsString(result), JsonHelper.mapper.writeValueAsString(new BatchRes(
        Arrays.asList(
            new BatchItem(
                new Meta("b810bbe954d51e380f395de0c301a0a42d16f115453f2feb4188ca9f7189074e", 28),
                ImmutableMap.<LinkType, Link>builder()
                    .put(LinkType.Download, new Link(
                        URI.create("https://github-cloud.s3.amazonaws.com/alambic/media/111975537/b8/10/b810bbe954d51e380f395de0c301a0a42d16f115453f2feb4188ca9f7189074e?actor_id=2458138"),
                        ImmutableMap.<String, String>builder()
                            .put("Authorization", "Token-2")
                            .put("x-amz-content-sha256", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855")
                            .put("x-amz-date", "20151007T190640Z")
                            .build(),
                        null
                    ))
                    .build()
            ),
            new BatchItem(
                new Meta("1cbec737f863e4922cee63cc2ebbfaafcd1cff8b790d8cfd2e6a5d550b648afa", 3),
                new Error(404, "Object does not exist on the server")
            )
        )
    )));
    replay.close();
  }

  /**
   * Simple download (JFrog Artifactory).
   */
  @Test
  public void batchDownload02() throws IOException {
    batchDownload("/ru/bozaro/gitlfs/client/batch-download-02.yml");
  }
}
