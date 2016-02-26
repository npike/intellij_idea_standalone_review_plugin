package net.npike.intellij.standalonereview;

import com.google.gson.Gson;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

import net.npike.intellij.standalonereview.models.Comment;
import net.npike.intellij.standalonereview.models.Review;


import java.io.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by npike on 2/25/16.
 */
public class ReviewManager {
    private static final Logger LOGGER = Logger.getInstance(ReviewManager.class.getName());
    private static ReviewManager INSTANCE;
    private File reviewFileForProject;
    private final Gson gson;
    private Review inMemoryReview;
    private Project mProjectUnderReview;

    public static ReviewManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ReviewManager();
        }
        return INSTANCE;
    }

    public ReviewManager() {
        gson = new Gson();

    }

    public boolean isStarted(Project project) {
        if (project != mProjectUnderReview) {
            return false;
        }
        return reviewFileForProject != null;
    }

    public void startReview(Project project) {
        LOGGER.info("startReview for project.");
        mProjectUnderReview = project;
        reviewFileForProject = new File(project.getBaseDir().getPath(), "standalone_review.txt");

        if (!reviewFileForProject.exists()) {
            LOGGER.info("Review file " + reviewFileForProject.getAbsolutePath() + " doesn't exist.");
            inMemoryReview = new Review(project);
        } else {
            LOGGER.info("Review file " + reviewFileForProject.getAbsolutePath() + " already exists.");
            try {
                BufferedReader br = new BufferedReader(
                        new FileReader(reviewFileForProject));
                inMemoryReview = gson.fromJson(br, Review.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void addComment(String filepath, int[] lines, String comment) {
        if (reviewFileForProject == null) {
            LOGGER.warn("Review for project hasn't been started yet?");
            return;
        }

        net.npike.intellij.standalonereview.models.File reviewFile = null;

        for (net.npike.intellij.standalonereview.models.File f : inMemoryReview.files) {
            if (f.filename.equalsIgnoreCase(filepath)) {
                reviewFile = f;
                break;
            }
        }

        if (reviewFile == null) {
            reviewFile = new net.npike.intellij.standalonereview.models.File();
            reviewFile.filename = filepath;
            reviewFile.comments = new ArrayList<>();
            inMemoryReview.files.add(reviewFile);
        }

        Comment newComment = new Comment();
        newComment.lines = lines;
        newComment.comment = comment;

        reviewFile.comments.add(newComment);

        flush();
    }


    public void flush() {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(reviewFileForProject, false)))) {
            out.print(gson.toJson(inMemoryReview));
            LOGGER.info("Flushed review to file "+reviewFileForProject.getAbsolutePath());
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
            LOGGER.error("flush exception", e);
        }
    }

    public void publish() {
        publish(reviewFileForProject.getParentFile().getAbsolutePath()+"/"+"standalone_review_publish.txt");
    }

    private void publish(String filePath) {
        // Loop through the in memory representation of our review and flatten it into a txt file

        StringBuilder output = new StringBuilder();

        output.append("Review for "+inMemoryReview.project+"\n");
        output.append("Started "+new Date(inMemoryReview.startedTimeInMillis)+"\n");
        output.append("\n\n");

        for (net.npike.intellij.standalonereview.models.File file : inMemoryReview.files) {
            output.append(file.filename+":"+"\n");
            // Sort comments by lines first
            if (file.comments != null) {
                Collections.sort(file.comments, new Comparator<Comment>() {
                    @Override
                    public int compare(Comment o1, Comment o2) {
                        return Integer.compare(o1.lines[0], o2.lines[0]);
                    }
                });

                for (Comment comment : file.comments) {
                    output.append("\t["+comment.lines[0]+":"+comment.lines[comment.lines.length-1]+"] ");
                    output.append(comment.comment+"\n");
                }
            }
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath, false)))) {
            out.print(output.toString());
            LOGGER.info("Published review to file "+filePath);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
            LOGGER.error("Publish exception", e);
        }
    }
}
