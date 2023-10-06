package website.motus.domain.video.service.videoSearch;

import website.motus.domain.video.dto.VideoResponseDTO;
import website.motus.domain.video.dto.pagedto.VideoPageRequestDTO;
import org.springframework.data.domain.Page;

public interface VideoSearch {
    Page<VideoResponseDTO> search(VideoPageRequestDTO videoPageRequestDTO);
}
