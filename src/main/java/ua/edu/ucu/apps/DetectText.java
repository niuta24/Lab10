package ua.edu.ucu.apps;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetectText {
  public static void detectText() throws IOException {
    // TODO(developer): Replace these variables before running the sample.
    String filePath = "path/to/your/image/file.jpg";
    detectText(filePath);
  }

  public static void detectText(String filePath) throws IOException {
    List<AnnotateImageRequest> requests = new ArrayList<>();

    ByteString imgBytes = ByteString.
    readFrom(new FileInputStream(filePath));

    Image img = Image.newBuilder().setContent(imgBytes).build();
    Feature feat = Feature.newBuilder().
    setType(Feature.Type.TEXT_DETECTION).build();
    AnnotateImageRequest request =
        AnnotateImageRequest.newBuilder()
        .addFeatures(feat).setImage(img).build();
    requests.add(request);

    try (ImageAnnotatorClient CLIENT = ImageAnnotatorClient.create()) {
      BatchAnnotateImagesResponse response = CLIENT.
      batchAnnotateImages(requests);
      List<AnnotateImageResponse> responses = response.
      getResponsesList();

      for (AnnotateImageResponse res : responses) {
        if (res.hasError()) {
          System.out.
          format("Error: %s%n", res.getError().getMessage());
          return;
        }

        for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
          System.out.format("Text: %s%n", annotation.getDescription());
          System.out.
          format("Position : %s%n", annotation.getBoundingPoly());
        }
      }
    }
  }

}
