package net.npike.intellij.standalonereview.models;

import java.util.List;

/**
 * Created by npike on 2/25/16.
 */
public class File {
    public String filename;
    public List<Comment> comments;


    public boolean equals(Object o){
        if (o instanceof File){
            File temp = (File)o;
            if (this.filename.equals(temp.filename)) {
                return true;
            }
        }
        return false;
    }
}
