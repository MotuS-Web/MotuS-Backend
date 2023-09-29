package com.hallym.rehab.domain.video.entity;

import com.hallym.rehab.global.baseEntity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Video extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vno; // video PK

    private String title; // 동작 제목

    private String description; // 동작 설명

    @Enumerated(value = EnumType.STRING)
    private Category category; // 카테고리

    @Enumerated(value = EnumType.STRING)
    private Position position; // 자세

    private Long frame; // AI 에서 쓸 영상 프레임

    private double playTime; // Client 에서 쓸 영상 시간

    private String videoURL; // 동영상 URL (Client, AI 서버에서 사용)

    private String jsonURL; // json URL (Client, AI 서버에서 사용)

    private String thumbnailURL; // thumbnail URL (Cilent 및 AI 서버에서 사용)

    private String videoPath; // Object Storage 에서의 video 경로 (삭제시 사용)

    private String jsonPath; // Object Storage 에서의 json 경로 (삭제시 사용)

    private String thumbnailPath; // Object Storage 에서의 thumbnail 경로 (삭제시 사용)
}