
import java.io.File;
import java.io.IOException;
import java.util.List;

import com.amazonaws.auth.*;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.*;

import org.apache.commons.io.FileUtils;

class Main {
    public static void main(String[] args) {
        new Main();
    }

    Main() {
        AWSCredentials credentials = new BasicAWSCredentials(Rootkey.accessKey, Rootkey.secretKey);
        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.EU_NORTH_1).build();
        List<Bucket> buckets = s3client.listBuckets();

        System.out.println("________");
        for (Bucket bucket : buckets) {
            System.out.println(bucket.getName());
        }

        // HOW TO UPLOAD FILE TO BUCKET
        // File testFile = new File("Jens.txt");
        // s3client.putObject(buckets.get(0).getName(), testFile.getName(), testFile);

        /*
         * //HOW TO LIST FILES IN BUCKET ObjectListing objectListing =
         * s3client.listObjects(buckets.get(0).getName()); for (S3ObjectSummary os :
         * objectListing.getObjectSummaries()) { System.out.println(os.getKey()); }
         */

        // HOW TO DOWNLOAD FILE FROM BUCKET
        S3Object s3object = s3client.getObject(buckets.get(0).getName(), "Jens.txt");
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        try {
            FileUtils.copyInputStreamToFile(inputStream, new File("Jens.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}