package net.npike.intellij.standalonereview.models;

import com.intellij.openapi.project.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by npike on 2/25/16.
 */
public class Review {
    public List<File> files;
    public List<Comment> comments;
    public String project;
    public long startedTimeInMillis;


    public Review(Project project) {
        this.project = project.getName();
        startedTimeInMillis = System.currentTimeMillis();
        files = new ArrayList<>();
        comments = new ArrayList<>();
    }
}
