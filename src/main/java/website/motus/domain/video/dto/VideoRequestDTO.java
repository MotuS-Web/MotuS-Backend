package website.motus.domain.video.dto;

import website.motus.domain.video.entity.Category;
import website.motus.domain.video.entity.Position;
import website.motus.domain.video.entity.Video;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoRequestDTO {
    private String title;
    private String description;
    private Category category;
    private Position position;
    private Long frame; // Video Frame
    private double playTime;
    @Builder.Default
    private MultipartFile[] files = new MultipartFile[2]; // Video File, Json File

    public Video toVideo(UploadFileDTO uploadFileDTO) {
        return Video.builder()
                    .title(title)
                    .description(description)
                    .category(category)
                    .position(position)
                    .frame(frame)
                    .playTime(playTime)
                    .videoURL(uploadFileDTO.getVideoURL())
                    .jsonURL(uploadFileDTO.getJsonURL())
                    .videoPath(uploadFileDTO.getVideoPath())
                    .jsonPath(uploadFileDTO.getJsonPath())
                    .thumbnailURL(uploadFileDTO.getThumbnailURL())
                    .thumbnailPath(uploadFileDTO.getThumbnailPath())
                    .build();
    }
}
