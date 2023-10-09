package website.motus.domain.video.repository;

import website.motus.domain.video.entity.Category;
import website.motus.domain.video.entity.Position;
import website.motus.domain.video.entity.Video;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class VideoRepositoryTest {
    @Autowired
    VideoRepository videoRepository;

    @Test
    void insertDummyData() {

        Category[] categoryList = {Category.KNEE, Category.THIGH, Category.SHOULDER, Category.ARM};
        Position[] positionList = {Position.LYING, Position.SITTING, Position.SITTING};

        for (int i = 1; i <=100; i++) {
            Video video = Video.builder()
                    .title("테스트 title" + i)
                    .description("테스트 description" + i)
                    .category(categoryList[i % 4])
                    .position(positionList[i % 3])
                    .frame(300L)
                    .playTime(18.5)
                    .videoPath("videoPath..")
                    .videoURL("videoURL..")
                    .jsonPath("jsonPath..")
                    .jsonURL("jsonURL..")
                    .thumbnailPath("thumbnailPath")
                    .thumbnailURL("thumbnailURL")
                    .build();

            videoRepository.save(video);
        }
    }
}