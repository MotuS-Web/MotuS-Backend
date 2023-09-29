package com.hallym.rehab.domain.video.service.videoSearch;

import com.hallym.rehab.domain.video.dto.VideoResponseDTO;
import com.hallym.rehab.domain.video.dto.pagedto.VideoPageRequestDTO;
import org.springframework.data.domain.Page;

public interface VideoSearch {
    Page<VideoResponseDTO> search(VideoPageRequestDTO videoPageRequestDTO);
}
