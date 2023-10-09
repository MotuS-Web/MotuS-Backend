package website.motus.global.config;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.List;

@SpringBootTest
class S3ClientTest {

    @Autowired
    S3Client s3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName; // your bucketName
    @Test
    public void bucketList() {
        AmazonS3 s3 = s3Client.getAmazonS3();

        try {
            List<Bucket> buckets = s3.listBuckets();
            System.out.println("Bucket List: ");
            for (Bucket bucket : buckets) {
                System.out.println("    name=" + bucket.getName() + ", creation_date=" + bucket.getCreationDate() + ", owner=" + bucket.getOwner().getId());
            }
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void uploadFile() {
        AmazonS3 s3 = s3Client.getAmazonS3();

        // upload local file
        String objectPath = "video/video1";
        String filePath = "src/main/resources/sample.mp4";

        try {
            s3.putObject(bucketName, objectPath, new File(filePath));
            System.out.format("Object %s has been created.\n", objectPath);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteFile() {
        AmazonS3 s3 = s3Client.getAmazonS3();

        String objectPath = "video/video1";

        // delete object
        try {
            s3.deleteObject(bucketName, objectPath);
            System.out.format("Object %s has been deleted.\n", objectPath);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }
    }
}