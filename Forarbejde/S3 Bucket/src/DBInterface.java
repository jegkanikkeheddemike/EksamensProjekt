import java.io.File;
import java.io.IOException;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import org.apache.commons.io.FileUtils;

public class DBInterface {
    private AWSCredentials credentials;
    private AmazonS3 s3client;
    public List<Bucket> buckets;

    public void connect() {
        credentials = new BasicAWSCredentials(Rootkey.accessKey, Rootkey.secretKey);
        s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_NORTH_1).build();
        buckets = s3client.listBuckets();
    }

    public void connect(String accessKey, String secretKey) {
        credentials = new BasicAWSCredentials(accessKey, secretKey);
        s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_NORTH_1).build();
        buckets = s3client.listBuckets();
    }

    public void uploadToBucket(String bucketName, String fileName) {
        File uploadFile = new File(fileName);
        s3client.putObject(bucketName, fileName, uploadFile);
    }

    public void downloadFromBucket(String bucketName, String fileName) {
        S3Object s3object = s3client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        try {
            FileUtils.copyInputStreamToFile(inputStream, new File("Jens.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printBuckets() {
        for (Bucket bucket : buckets) {
            System.out.println(bucket.getName());
        }
    }

    public void printObjects(String bucketName) {
        ObjectListing objectListing = s3client.listObjects(bucketName);
        for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
            System.out.println(os.getKey());
        }
    }

    public void printObjects(Bucket bucket) {
        ObjectListing objectListing = s3client.listObjects(bucket.getName());
        for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
            System.out.println(os.getKey());
        }
    }
}
