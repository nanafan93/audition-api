package com.audition.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuditionPost {

    private int userId;
    private int id;
    private String title;
    private String body;
    private List<PostComment> comments;
}
