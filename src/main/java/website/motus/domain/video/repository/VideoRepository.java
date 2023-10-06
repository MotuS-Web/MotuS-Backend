package website.motus.domain.video.repository;

import website.motus.domain.video.entity.Video;
import website.motus.domain.video.service.videoSearch.VideoSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long>, VideoSearch {
}
