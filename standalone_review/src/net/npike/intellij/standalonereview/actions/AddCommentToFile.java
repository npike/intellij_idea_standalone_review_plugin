package net.npike.intellij.standalonereview.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import net.npike.intellij.standalonereview.ReviewManager;
import net.npike.intellij.standalonereview.ui.IssueDialog;

/**
 * Created by npike on 3/11/16.
 */
public class AddCommentToFile extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        if (project == null) {
            return;
        }

        VirtualFile[] selectedFiles = FileEditorManager.getInstance(project).getSelectedFiles();
        if (selectedFiles == null || selectedFiles.length == 0) {
            return;
        }

        if (!ReviewManager.getInstance().isStarted(project)) {
            ReviewManager.getInstance().startReview(project);
        }

        IssueDialog id = new IssueDialog(getRelativePath(selectedFiles[0], project.getBaseDir()), null);
        id.show();
    }


    public static String getRelativePath(VirtualFile file, VirtualFile folder) {
        String filePath = file.getPath();
        String folderPath = folder.getPath();
        if (filePath.startsWith(folderPath)) {
            return filePath.substring(folderPath.length() + 1);
        } else {
            return null;
        }
    }

    @Override
    public void update(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        if (project == null) {
            return;
        }
        e.getPresentation().setEnabledAndVisible(ReviewManager.getInstance().isStarted(project));
    }
}
