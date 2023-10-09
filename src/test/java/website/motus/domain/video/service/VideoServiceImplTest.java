package website.motus.domain.video.service;

import website.motus.domain.video.dto.VideoRequestDTO;
import website.motus.domain.video.entity.Category;
import website.motus.domain.video.entity.Position;
import website.motus.domain.video.entity.Video;
import website.motus.domain.video.repository.VideoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VideoServiceTest {


    @Autowired
    VideoRepository videoRepository;
    @Autowired
    VideoService videoService;


    @Test
    @Order(1)
//    @Transactional
    void createVideo() throws IOException {
        String mp4FilePath = "src/main/resources/sample.mp4";
        String jsonFilePath = "src/main/resources/sample.json";
        byte[] mp4Bytes = Files.readAllBytes(Paths.get(mp4FilePath));
        byte[] jsonBytes = Files.readAllBytes(Paths.get(jsonFilePath));

        // set MockMultipartFile
        MultipartFile mp4File = new MockMultipartFile(
                "file",
                "sample.mp4",
                "video/mp4",
                mp4Bytes           // MP4 file data read as byte array
        );


        MultipartFile jsonFile = new MockMultipartFile(
                "file",
                "sample.json",
                "file",
                jsonBytes
        );

        MultipartFile[] files = new MultipartFile[2];
        files[0] = mp4File;
        files[1] = jsonFile;

        VideoRequestDTO videoRequestDTO = VideoRequestDTO.builder()
                .title("테스트 title")
                .description("테스트 description")
                .category(Category.ARM)
                .position(Position.STANDING)
                .frame(300L)
                .playTime(18.5)
                .files(files)
                .build();

        String result = videoService.createVideo(videoRequestDTO);
        assertThat(result).isEqualTo("success");
    }

    @Test
    @Order(2)
//    @Transactional
    void deleteVideo() {
        Video video = videoRepository.findAll().get(0);
        String result = videoService.deleteVideo(video.getVno());
        assertThat(result).isEqualTo("success");
    }

    @Test
    @Order(3)
    void clearAllVideoAndJson() {
        videoService.clearAllVideoAndJson();
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList.size()).isEqualTo(0);
    }

}