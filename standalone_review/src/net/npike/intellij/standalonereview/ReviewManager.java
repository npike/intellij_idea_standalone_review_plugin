package net.npike.intellij.standalonereview;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

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
    private File mReviewFileForProject;
    private final Gson mGson;
    private Review mInMemoryReview;
    private Project mProjectUnderReview;

    public static ReviewManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ReviewManager();
        }
        return INSTANCE;
    }

    public ReviewManager() {
        mGson = new GsonBuilder().setPrettyPrinting().create();
    }

    public boolean isStarted(Project project) {
        if (project != mProjectUnderReview) {
            return false;
        }
        return mReviewFileForProject != null;
    }

    public void startReview(Project project) {
        LOGGER.info("startReview for project.");
        mProjectUnderReview = project;
        mReviewFileForProject = new File(project.getBaseDir().getPath(), "standalone_review.txt");

        if (!mReviewFileForProject.exists()) {
            LOGGER.info("Review file " + mReviewFileForProject.getAbsolutePath() + " doesn't exist.");
            mInMemoryReview = new Review(project);
        } else {
            LOGGER.info("Review file " + mReviewFileForProject.getAbsolutePath() + " already exists.");

            int dialogResponse = Messages.showYesNoCancelDialog(project, "It seems a review has already been started for " +
                    "this project.  If you would like to resume it click 'Yes'.  Pressing 'No' will " +
                    "start a new review.", "Standalone Review", Messages.getWarningIcon());

            if (Messages.YES == dialogResponse) {
                try {
                    BufferedReader br = new BufferedReader(
                            new FileReader(mReviewFileForProject));
                    mInMemoryReview = mGson.fromJson(br, Review.class);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (Messages.NO == dialogResponse) {
                mInMemoryReview = new Review(project);
            } else {
                mProjectUnderReview = null;
                mReviewFileForProject = null;
            }


        }
    }

    /**
     * Adds a comment to the top level review, not associated with a file or line.
     */
    public void addComment(String comment) {
        if (mReviewFileForProject == null) {
            LOGGER.warn("Review for project hasn't been started yet?");
            return;
        }

        Comment commentObject = new Comment();
        commentObject.comment = comment;

        mInMemoryReview.comments.add(commentObject);
    }

    public void addComment(String filepath, int[] lines, String comment) {
        if (mReviewFileForProject == null) {
            LOGGER.warn("Review for project hasn't been started yet?");
            return;
        }

        net.npike.intellij.standalonereview.models.File reviewFile = null;

        for (net.npike.intellij.standalonereview.models.File f : mInMemoryReview.files) {
            if (f.filename.equalsIgnoreCase(filepath)) {
                reviewFile = f;
                break;
            }
        }

        if (reviewFile == null) {
            reviewFile = new net.npike.intellij.standalonereview.models.File();
            reviewFile.filename = filepath;
            reviewFile.comments = new ArrayList<>();
            mInMemoryReview.files.add(reviewFile);
        }

        Comment newComment = new Comment();
        newComment.lines = lines;
        newComment.comment = comment;

        reviewFile.comments.add(newComment);

        flush();
    }


    public void flush() {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(mReviewFileForProject, false)))) {
            out.print(mGson.toJson(mInMemoryReview));
            LOGGER.info("Flushed review to file " + mReviewFileForProject.getAbsolutePath());
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
            LOGGER.error("flush exception", e);
        }
    }

    public void publish() {
        publish(mReviewFileForProject.getParentFile().getAbsolutePath() + "/" + "standalone_review_publish.txt");
    }

    private void publish(String filePath) {
        // Loop through the in memory representation of our review and flatten it into a txt file

        StringBuilder output = new StringBuilder();

        output.append("Review for " + mInMemoryReview.project + "\n");
        output.append("Started " + new Date(mInMemoryReview.startedTimeInMillis) + "\n");
        output.append("\n\n");


        if (mInMemoryReview.comments != null && mInMemoryReview.comments.size() > 0) {
            output.append("General comments:\n");
            for (Comment comment : mInMemoryReview.comments) {
                output.append("* " + comment.comment + "\n");
            }
            output.append("\n");
        }

        for (net.npike.intellij.standalonereview.models.File file : mInMemoryReview.files) {
            output.append(file.filename + ":" + "\n");
            // Sort comments by lines first
            if (file.comments != null) {
                Collections.sort(file.comments, new Comparator<Comment>() {
                    @Override
                    public int compare(Comment o1, Comment o2) {
                        if (o1.lines == null || o1.lines.length == 0) {
                            return -1;
                        }

                        if (o2.lines == null || o2.lines.length == 0) {
                            return -1;
                        }
                        return Integer.compare(o1.lines[0], o2.lines[0]);
                    }
                });

                for (Comment comment : file.comments) {
                    output.append("\t* ");
                    if (comment.lines != null && comment.lines.length > 0) {
                        output.append("[" + comment.lines[0] + ":" + comment.lines[comment.lines.length - 1] + "] ");
                    }
                    output.append(comment.comment + "\n");
                }
            }

            output.append("\n");
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath, false)))) {
            out.print(output.toString());
            LOGGER.info("Published review to file " + filePath);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
            LOGGER.error("Publish exception", e);
        }
    }
}
