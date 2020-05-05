package com.example.userservice.controllers;

import com.example.userservice.models.Episode;
import com.example.userservice.models.Podcast;
import com.example.userservice.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "users")
public class UserController {
    @Autowired
    private WebClient.Builder webClientBuilder;

    private List<User> users = Arrays.asList(
            new User(1, "Viktor", "Hosting this webinar", Arrays.asList(1, 2)),
            new User(2, "Olaf", "Good guy", Collections.emptyList())
    );

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getUsers() {
        return users;
    }

    @RequestMapping("/schedule/{userId}")
    public List<Episode> getUserSchedule(@PathVariable Integer userId) {
        List<Podcast> podcasts = users.stream().filter(user -> user.getId() == userId).findFirst().orElse(null).getSubscribedPodcasts()
                .stream().map(podcastId -> webClientBuilder.baseUrl("http://podcast-service").build().get()
                        .uri("/podcasts/" + podcastId).retrieve().bodyToFlux(Podcast.class).blockFirst())
                .collect(Collectors.toList());
        List<Episode> episodes = podcasts.stream().flatMap(podcast -> podcast.getEdisodes().stream())
                .sorted(Comparator.comparing(Episode::getStartTime)).collect(Collectors.toList());
        return episodes;
    }
}

