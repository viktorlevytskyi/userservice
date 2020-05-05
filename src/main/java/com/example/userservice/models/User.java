package com.example.userservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {
    private Integer id;
    private String name;
    private String aboutMe;
    List<Integer> subscribedPodcasts;
}

