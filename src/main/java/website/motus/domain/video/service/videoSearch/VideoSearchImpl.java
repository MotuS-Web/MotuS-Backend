package website.motus.domain.video.service.videoSearch;

import website.motus.domain.video.dto.QVideoResponseDTO;
import website.motus.domain.video.dto.VideoResponseDTO;
import website.motus.domain.video.dto.pagedto.VideoPageRequestDTO;
import website.motus.domain.video.entity.Position;
import website.motus.domain.video.entity.Category;
import website.motus.domain.video.entity.QVideo;
import website.motus.domain.video.entity.Video;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
public class VideoSearchImpl extends QuerydslRepositorySupport implements VideoSearch {

    private final JPAQueryFactory queryFactory;

    public VideoSearchImpl(EntityManager entityManager) {
        super(Video.class);
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * search VideoList using customized BooleanBuilder
     * @param requestDTO
     * @return
     */
    @Override
    public Page<VideoResponseDTO> search(VideoPageRequestDTO requestDTO) {
        QVideo video = QVideo.video;

        String title = requestDTO.getTitle();
        String description = requestDTO.getDescription();
        Category category = requestDTO.getCategory();
        Position position = requestDTO.getPosition();

        Pageable pageable = requestDTO.getPageable();

        // set the condition you consider
        BooleanBuilder builder = new BooleanBuilder();
        if (title != null) builder.and(video.title.containsIgnoreCase(title));
        if (description != null) builder.and(video.description.containsIgnoreCase(description));
        if (category != null) builder.and(video.category.eq(category));
        if (position != null) builder.and(video.position.eq(position));

        QueryResults<VideoResponseDTO> fetchResults = queryFactory
                .select(new QVideoResponseDTO(video.vno, video.title, video.description,
                        video.category, video.position, video.playTime, video.videoURL, video.thumbnailURL))
                .from(video)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        long total = fetchResults.getTotal();
        List<VideoResponseDTO> content = fetchResults.getResults();

        return new PageImpl<>(content, pageable, total);
    }
}
