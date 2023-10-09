package website.motus.domain.video.controller;

import website.motus.domain.video.dto.VideoDetailResponseDTO;
import website.motus.domain.video.dto.VideoRequestDTO;
import website.motus.domain.video.dto.VideoResponseDTO;
import website.motus.domain.video.dto.pagedto.VideoPageRequestDTO;
import website.motus.domain.video.dto.pagedto.VideoPageResponseDTO;
import website.motus.domain.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;


    @GetMapping("/list")
    public VideoPageResponseDTO<VideoResponseDTO> getList(VideoPageRequestDTO pageRequestDTO) {
        return videoService.getVideoList(pageRequestDTO);
    }

    @GetMapping("/{vno}")
    public VideoDetailResponseDTO getOne(@PathVariable("vno") Long vno) {
        return videoService.getVideo(vno);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createVideo(VideoRequestDTO videoRequestDTO) {
        String result = videoService.createVideo(videoRequestDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{vno}")
    public ResponseEntity<String> deleteVideo(@PathVariable Long vno) {
        String result = videoService.deleteVideo(vno);
        return ResponseEntity.ok(result);
    }
}