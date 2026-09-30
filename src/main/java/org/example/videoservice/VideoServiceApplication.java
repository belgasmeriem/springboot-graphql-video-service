package org.example.videoservice;

import org.example.videoservice.dao.entities.Creator;
import org.example.videoservice.dao.entities.Video;
import org.example.videoservice.dao.repositories.CreatorRepository;
import org.example.videoservice.dao.repositories.VideoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class VideoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner start(CreatorRepository creatorRepository, VideoRepository videoRepository) {
        return args -> {
            List<Creator> creators = creatorRepository.saveAll(List.of(
                    Creator.builder().name("Meriem").email("meriem@gmail.com").build(),
                    Creator.builder().name("Manal").email("manal@gmail.com").build()
            ));

            videoRepository.saveAll(List.of(
                    Video.builder().name("GraphQL tuto").url("url1").description("desc 1").datePublication("28/11/2023").creator(creators.get(0)).build(),
                    Video.builder().name("Spring Boot tuto").url("url2").description("desc 2").datePublication("29/11/2023").creator(creators.get(1)).build()
            ));
        };
    }

}
