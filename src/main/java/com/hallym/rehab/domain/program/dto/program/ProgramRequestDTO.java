package com.hallym.rehab.domain.program.dto.program;

import com.hallym.rehab.domain.program.entity.Category;
import com.hallym.rehab.domain.program.entity.Position;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProgramRequestDTO {
    private String programTitle;
    private String description;
    private Category category;
    private Position position;
}
