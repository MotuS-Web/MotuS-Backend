package com.hallym.rehab.domain.video.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.hallym.rehab.domain.video.dto.UploadFileDTO;
import com.hallym.rehab.domain.video.dto.VideoDetailResponseDTO;
import com.hallym.rehab.domain.video.dto.VideoRequestDTO;
import com.hallym.rehab.domain.video.dto.VideoResponseDTO;
import com.hallym.rehab.domain.video.dto.pagedto.VideoPageRequestDTO;
import com.hallym.rehab.domain.video.dto.pagedto.VideoPageResponseDTO;
import com.hallym.rehab.domain.video.entity.Video;
import com.hallym.rehab.domain.video.repository.VideoRepository;
import com.hallym.rehab.global.config.S3Client;
import com.hallym.rehab.global.util.AWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.FileChannelWrapper;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService{

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName; // your bucketName
    private final S3Client s3Client; // connect to S3
    private final VideoRepository videoRepository;

    /**
     * you cad add SearchParam (Title, Tag)
     * @VideoPageRequestDTO if you want to know more specific parameter refer to this DTO
     * @param requestDTO
     * @return VideoPageResponseDTO
     */
    @Override
    public VideoPageResponseDTO<VideoResponseDTO> getVideoList(VideoPageRequestDTO requestDTO) {
        Page<VideoResponseDTO> result = videoRepository.search(requestDTO);

        return VideoPageResponseDTO.<VideoResponseDTO>withAll()
                .pageRequestDTO(requestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

    /**
     * Single video view
     * @param vno
     * @return VideoDetailResponseDTO
     */
    @Override
    public VideoDetailResponseDTO getVideo(Long vno) {
        Video video = videoRepository.findById(vno)
                .orElseThrow(() -> new NotFoundException("video not found for id -> " + vno));

        return video.toDetailDTO();
    }

    /**
     * create Video using RequestDTO
     * @param videoRequestDTO
     */
    @Override
    public String createVideo(VideoRequestDTO videoRequestDTO) {
        MultipartFile[] files = videoRequestDTO.getFiles();

        MultipartFile videoFile =  files[0];
        MultipartFile jsonFile =  files[1];
        UploadFileDTO uploadFileDTO = uploadFileToS3(videoFile, jsonFile);

        Video video = videoRequestDTO.toVideo(uploadFileDTO);
        videoRepository.save(video);

        return "success";
    }

    /**
     * delete Video using Video PK
     * @param vno Video PK
     * @return if you success to delete, return "success"
     * if vno was not saved database, return "Video not found for id : " + vno;
     */
    @Override
    public String deleteVideo(Long vno) {
        Video video = videoRepository.findById(vno)
                .orElseThrow(() -> new NotFoundException("video not found for id -> " + vno));

        String videoPath = video.getVideoPath();
        String jsonPath = video.getJsonPath();
        String thumbnailPath = video.getThumbnailPath();

        deleteFileFromS3(videoPath, jsonPath, thumbnailPath);
        videoRepository.delete(video);

        return "success";
    }

    /**
     * convert MultipartFile to File
     * @param multipartFile
     * @param fileName
     * @return File Object
     */
    @Override
    public File convertMultipartFileToFile(MultipartFile multipartFile, String fileName) {
        File convertedFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return convertedFile;
    }

    @Override
    public UploadFileDTO uploadFileToS3(MultipartFile videoFile, MultipartFile jsonFile) {
        AmazonS3 s3 = s3Client.getAmazonS3();

        UUID uuid_1 = UUID.randomUUID();
        UUID uuid_2 = UUID.randomUUID();
        UUID uuid_3 = UUID.randomUUID();

        String videoFileName = uuid_1 + "_" + videoFile.getOriginalFilename();
        String jsonFileName = uuid_2 + "_" + jsonFile.getOriginalFilename();
        String thumbnailFileName = uuid_3 + "_" + videoFile.getOriginalFilename().split("\\.")[0] + ".png";

        // For temporary storage to upload
        File uploadVideoFile = null;
        File uploadJsonFile = null;
        File uploadThumbnailFile = null;

        try {
            uploadVideoFile = convertMultipartFileToFile(videoFile, videoFileName);
            uploadJsonFile = convertMultipartFileToFile(jsonFile, jsonFileName);

            FileChannelWrapper channel = NIOUtils.readableChannel(uploadVideoFile);
            FrameGrab grab = FrameGrab.createFrameGrab(channel);

            // Get the first frame
            Picture picture = grab.getNativeFrame();
            // Convert the Picture object to a BufferedImage
            BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
            // Now you can use the BufferedImage as needed. For example, write it to a file:
            uploadThumbnailFile = new File(thumbnailFileName);
            ImageIO.write(bufferedImage, "png", uploadThumbnailFile);

            String VideoObjectPath = "video/" + videoFileName;
            String jsonObjectPath = "json/" + jsonFileName;
            String thumbnailObjectPath = "thumbnail/" + thumbnailFileName;

            s3.putObject(bucketName, VideoObjectPath, uploadVideoFile);
            s3.putObject(bucketName, jsonObjectPath, uploadJsonFile);
            s3.putObject(bucketName, thumbnailObjectPath, uploadThumbnailFile);

            String baseUploadURL = "https://kr.object.ncloudstorage.com/" + bucketName + "/";
            String videoURL = baseUploadURL + VideoObjectPath;
            String jsonURL = baseUploadURL + jsonObjectPath;
            String thumbnailURL = baseUploadURL + thumbnailObjectPath;

            // direct access URL
            log.info(videoURL);
            log.info(jsonURL);
            log.info(thumbnailURL);

            // All User can access Object
            setAcl(s3, VideoObjectPath);
            setAcl(s3, jsonObjectPath);
            setAcl(s3, thumbnailObjectPath);

            // close readable channel to delete temp file
            channel.close();

            return UploadFileDTO.builder()
                    .videoURL(videoURL)
                    .jsonURL(jsonURL)
                    .thumbnailURL(thumbnailURL)
                    .videoPath(VideoObjectPath)
                    .jsonPath(jsonObjectPath)
                    .thumbnailPath(thumbnailObjectPath)
                    .build();

        } catch (AmazonS3Exception e) { // ACL Exception
            log.info(e.getErrorMessage());
            System.exit(1);
            return null; // if error during upload, return null
        } catch (JCodecException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            // Delete temporary files used when uploading
            assert uploadJsonFile != null;
            assert uploadVideoFile != null;
            assert uploadThumbnailFile != null;

            uploadVideoFile.delete();
            uploadJsonFile.delete();
            uploadThumbnailFile.delete();
        }

    }

    /**
     * delete File From S3 using above path
     * @param videoPath
     * @param jsonPath
     * @param thumbnailPath
     */
    @Override
    public void deleteFileFromS3(String videoPath, String jsonPath, String thumbnailPath) {
        AmazonS3 s3 = s3Client.getAmazonS3();

        try {
            s3.deleteObject(bucketName, videoPath);
            s3.deleteObject(bucketName, jsonPath);
            log.info("Delete Object successfully");
        } catch(SdkClientException e) {
            e.printStackTrace();
            log.info("Error deleteFileFromS3");
        }
    }

    /**
     * When first created, it cannot be read, so you need to set up the ACL.
     * @param s3 AmazonS3 Object
     * @param objectPath objectPath to apply setACL
     */
    @Override
    public void setAcl(AmazonS3 s3, String objectPath) {
        AccessControlList objectAcl = s3.getObjectAcl(bucketName, objectPath);
        objectAcl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
        s3.setObjectAcl(bucketName, objectPath, objectAcl);
    }

    /**
     * Convenience method to clear All File on ObjectStorage and Video Entity
     * you can customize folder Name
     */
    @Override
    public void clearAllVideoAndJson() {
        AmazonS3 s3 = s3Client.getAmazonS3();

        ObjectListing videoObjectList = s3.listObjects(bucketName, "video/");
        while (true) {
            for (S3ObjectSummary summary : videoObjectList.getObjectSummaries()) {
                if (!summary.getKey().equals("video/")) { // Exclude the folder itself
                    s3.deleteObject(bucketName, summary.getKey());
                }
            }
            if (!videoObjectList.isTruncated()) break;
            videoObjectList = s3.listNextBatchOfObjects(videoObjectList);
        }

        ObjectListing jsonObjectList = s3.listObjects(bucketName, "json/");
        while (true) {
            for (S3ObjectSummary summary : jsonObjectList.getObjectSummaries()) {
                if (!summary.getKey().equals("json/")) { // Exclude the folder itself
                    s3.deleteObject(bucketName, summary.getKey());
                }
            }
            if (!jsonObjectList.isTruncated()) break;
            jsonObjectList = s3.listNextBatchOfObjects(jsonObjectList);
        }

        ObjectListing thumbnailObjectList = s3.listObjects(bucketName, "thumbnail/");
        while (true) {
            for (S3ObjectSummary summary : thumbnailObjectList.getObjectSummaries()) {
                if (!summary.getKey().equals("thumbnail/")) { // Exclude the folder itself
                    s3.deleteObject(bucketName, summary.getKey());
                }
            }
            if (!thumbnailObjectList.isTruncated()) break;
            thumbnailObjectList = s3.listNextBatchOfObjects(thumbnailObjectList);
        }

        videoRepository.deleteAll();
    }
}
