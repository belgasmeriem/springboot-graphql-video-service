package org.example.videoservice.web;

import org.example.videoservice.dao.entities.Creator;
import org.example.videoservice.dao.entities.Video;
import org.example.videoservice.dao.repositories.CreatorRepository;
import org.example.videoservice.dao.repositories.VideoRepository;
import org.example.videoservice.dtos.CreatorRequest;
import org.example.videoservice.dtos.VideoRequest;
// + imports de Creator, Video, CreatorRepository, VideoRepository
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

@Controller
public class VideoGraphQlController {

    private final CreatorRepository creatorRepository;
    private final VideoRepository videoRepository;

    public VideoGraphQlController(CreatorRepository creatorRepository, VideoRepository videoRepository) {
        this.creatorRepository = creatorRepository;
        this.videoRepository = videoRepository;
    }

    @QueryMapping
    public List<Video> videoList() {
        return videoRepository.findAll();
    }

    @QueryMapping
    public Creator creatorById(@Argument Long id) {
        return creatorRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Creator %s not found", id)));
    }

    @MutationMapping
    public Creator saveCreator(@Argument CreatorRequest creator) {
        Creator entity = Creator.builder()
                .name(creator.getName())
                .email(creator.getEmail())
                .build();
        return creatorRepository.save(entity);
    }

    @MutationMapping
    public Video saveVideo(@Argument VideoRequest video) {
        Creator creator = creatorRepository.save(Creator.builder()
                .name(video.getCreator().getName())
                .email(video.getCreator().getEmail())
                .build());

        Video entity = Video.builder()
                .name(video.getName())
                .url(video.getUrl())
                .description(video.getDescription())
                .datePublication(video.getDatePublication())
                .creator(creator)
                .build();
        return videoRepository.save(entity);
    }
    @SubscriptionMapping
    public Flux<Video> notifyVideoChange() {
        return Flux.fromStream(
                Stream.generate(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    Creator creator = creatorRepository.save(Creator.builder()
                            .name("x" + new Random().nextInt())
                            .email("x@gmail.com")
                            .build());

                    Video video = videoRepository.findById(1L)
                            .orElseThrow(() -> new RuntimeException("Video 1 not found"));
                    video.setCreator(creator);

                    return videoRepository.save(video);
                }));
    }
}