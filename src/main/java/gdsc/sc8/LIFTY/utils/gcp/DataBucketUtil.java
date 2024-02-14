package gdsc.sc8.LIFTY.utils.gcp;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class DataBucketUtil {

    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String keyFileLocation;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    private final Storage storage;

    @Autowired
    public DataBucketUtil(Storage storage) {
        this.storage = storage;
    }

    public String uploadToBucket(MultipartFile file) throws IOException {

        // 이미지 uuid와 파일 형식
        String uuid = UUID.randomUUID().toString();
        String ext = file.getContentType();
        log.info("uuid: " + uuid);
        log.info("bucketName: " + bucketName);

        // GCS에 이미지 업로드
        log.info("GCS에 이미지 업로드");
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, uuid)
            .setContentType(ext)
            .build();
        Blob blob = storage.create(blobInfo, file.getBytes());
        log.info(blob.getMediaLink());
        return bucketName + "/" + uuid;
    }

//    public String uploadToBucket(MultipartFile file) throws IOException {
//        InputStream keyFile = ResourceUtils.getURL(keyFileLocation).openStream();
//        String uuid = UUID.randomUUID().toString();
//        String ext = file.getContentType();
//
//        Storage storage = StorageOptions.newBuilder()
//            .setCredentials(GoogleCredentials.fromStream(keyFile))
//            .build()
//            .getService();
//
//        String imgUrl = "https://storage.googleapis.com/" + bucketName + "/" + uuid;
//
//        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, uuid)
//            .setContentType(ext)
//            .build();
//        Blob blob = storage.create(blobInfo, file.getBytes());
//
//        return bucketName + "/" + uuid;
//    }
}
