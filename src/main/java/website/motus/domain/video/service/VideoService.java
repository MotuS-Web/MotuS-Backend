package website.motus.domain.video.service;

import com.amazonaws.services.s3.AmazonS3;
import website.motus.domain.video.dto.UploadFileDTO;
import website.motus.domain.video.dto.VideoDetailResponseDTO;
import website.motus.domain.video.dto.VideoRequestDTO;
import website.motus.domain.video.dto.VideoResponseDTO;
import website.motus.domain.video.dto.pagedto.VideoPageRequestDTO;
import website.motus.domain.video.dto.pagedto.VideoPageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface VideoService {
    VideoPageResponseDTO<VideoResponseDTO> getVideoList(VideoPageRequestDTO requestDTO);
    VideoDetailResponseDTO getVideo(Long vno);
    String createVideo(VideoRequestDTO videoRequestDTO);
    String deleteVideo(Long vno);
    File convertMultipartFileToFile(MultipartFile multipartFile, String fileName);
    UploadFileDTO uploadFileToS3(MultipartFile videoFile, MultipartFile jsonFile);
    void deleteFileFromS3(String guideVideoObjectPath, String jsonObjectPath, String thumbnailObjectPath);
    void setAcl(AmazonS3 s3, String ObjectPath);
    void clearAllVideoAndJson();
}
