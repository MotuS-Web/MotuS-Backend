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

    private String title; // video title

    private String description; // video description

    @Enumerated(value = EnumType.STRING)
    private Category category; // search condition

    @Enumerated(value = EnumType.STRING)
    private Position position; // search condition

    private Long frame; // Video frame

    private double playTime; // Video playTime

    private String videoURL; // video URL (Used by Cilent and AI Server)

    private String jsonURL; // json URL (Used by Cilent and AI Server)

    private String thumbnailURL; // thumbnail URL (Used by Cilent and AI Server)

    private String videoPath; // (Used when deleting)

    private String jsonPath; // (Used when deleting)

    private String thumbnailPath; // (Used when deleting)
}