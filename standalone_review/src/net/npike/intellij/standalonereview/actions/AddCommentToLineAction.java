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
 * Created by npike on 2/25/16.
 */
public class AddCommentToLineAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        if (project == null) {
            return;
        }
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) {
            return;
        }
        final Document document = editor.getDocument();
        if (document == null) {
            return;
        }

        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
        if (virtualFile == null) {
            return;
        }

        if (!ReviewManager.getInstance().isStarted(project)) {
            ReviewManager.getInstance().startReview(project);
        }

        // Note: will probably need this to show icon in gutter at some point indicating theres a review comment
        // editor.getGutter().

        // TODO
        // - Capture line numbers of selection
        // - Capture file name
        // - Capture git revision

        int lineStart = document.getLineNumber(editor.getSelectionModel().getSelectionStart());
        int lineEnd = document.getLineNumber(editor.getSelectionModel().getSelectionEnd());


        IssueDialog id = new IssueDialog(getRelativePath(virtualFile, project.getBaseDir()), new int[]{lineStart, lineEnd});
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
