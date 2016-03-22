package net.npike.intellij.standalonereview.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
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
        CaretModel caretModel = editor.getCaretModel();

        String selectedCode;
        int lineStart;
        int lineEnd;
        if (editor.getSelectionModel().hasSelection()) {
            lineStart = document.getLineNumber(editor.getSelectionModel().getSelectionStart()) + 1;
            lineEnd = document.getLineNumber(editor.getSelectionModel().getSelectionEnd())+ 1;

            selectedCode = document.getCharsSequence().subSequence(editor.getSelectionModel().getSelectionStart(), editor.getSelectionModel().getSelectionEnd()).toString();
        } else {
            Pair<LogicalPosition, LogicalPosition> lines = EditorUtil.calcSurroundingRange(editor, caretModel.getVisualPosition(), caretModel.getVisualPosition());
            int offset = editor.getCaretModel().getOffset();

            LogicalPosition lineStartLP = lines.first;
            LogicalPosition nextLineStart = lines.second;
            int start = editor.logicalPositionToOffset(lineStartLP);
            int end = editor.logicalPositionToOffset(nextLineStart);

            selectedCode = document.getCharsSequence().subSequence(start, end).toString();

            lineStart  = lineEnd = document.getLineNumber(caretModel.getVisualLineStart()) + 1;
        }

        IssueDialog id = new IssueDialog(getRelativePath(virtualFile, project.getBaseDir()),
                new int[]{lineStart,
                        lineEnd});
        id.setCodePreview(selectedCode);
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
