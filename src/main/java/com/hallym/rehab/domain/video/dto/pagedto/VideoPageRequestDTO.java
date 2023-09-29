package com.hallym.rehab.domain.video.dto.pagedto;

import com.hallym.rehab.domain.video.entity.Category;
import com.hallym.rehab.domain.video.entity.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoPageRequestDTO {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    // Below are the search conditions.

    private String title;

    private String description;

    private Category category;

    private Position position;

    public Pageable getPageable(String...props) {
        return PageRequest.of(this.page -1, this.size, Sort.by(props).descending());
    }
}
