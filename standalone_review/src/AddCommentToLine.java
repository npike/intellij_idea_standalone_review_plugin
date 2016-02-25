import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Created by npike on 2/25/16.
 */
public class AddCommentToLine extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        // TODO: insert action logic here
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

        if (!ReviewManager.getInstance().isStarted()) {
            ReviewManager.getInstance().startReview(project);
        }

        // Note: will probably need this to show icon in gutter at some point indicating theres a review comment
//        editor.getGutter().

        // TODO
        // - Capture line numbers of selection
        // - Capture file name
        // - Capture git revision

        int lineStart = document.getLineNumber(editor.getSelectionModel().getSelectionStart());
        int lineEnd = document.getLineNumber(editor.getSelectionModel().getSelectionEnd());

        String txt= Messages.showInputDialog(project, "Comment", "Input comment", Messages.getQuestionIcon());

        ReviewManager.getInstance().addComment(virtualFile.getPath(), new int[]{lineStart, lineEnd}, txt);
        ReviewManager.getInstance().flush();
    }
}
