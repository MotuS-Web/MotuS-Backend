package com.hallym.rehab.domain.video.dto;

import com.hallym.rehab.domain.video.entity.Category;
import com.hallym.rehab.domain.video.entity.Position;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class VideoResponseDTO {
    private Long vno;
    private String title;
    private String description;
    private Category category;
    private Position position;
    private double playTime;

    // Path to directly access objects stored in object storage
    private String videoURL;
    private String thumbnailURL;

    @QueryProjection
    public VideoResponseDTO(Long vno, String title, String description,
                            Category category, Position position,
                            double playTime, String videoURL, String thumbnailURL) {
        this.vno = vno;
        this.title = title;
        this.description = description;
        this.category = category;
        this.position = position;
        this.playTime = playTime;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }
}
