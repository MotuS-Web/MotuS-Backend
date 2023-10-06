package website.motus.domain.video.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileDTO {
    private String videoURL;
    private String jsonURL;
    private String thumbnailURL;
    private String videoPath;
    private String jsonPath;
    private String thumbnailPath;
}
