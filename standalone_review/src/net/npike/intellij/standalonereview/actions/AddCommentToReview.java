package net.npike.intellij.standalonereview.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;

import net.npike.intellij.standalonereview.ReviewManager;
import net.npike.intellij.standalonereview.ui.IssueDialog;

/**
 * Created by npike on 3/11/16.
 */
public class AddCommentToReview extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        if (project == null) {
            return;
        }

        IssueDialog id = new IssueDialog();
        id.show();
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
