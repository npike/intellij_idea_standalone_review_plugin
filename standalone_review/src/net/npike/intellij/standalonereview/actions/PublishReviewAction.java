package net.npike.intellij.standalonereview.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;

import net.npike.intellij.standalonereview.ReviewManager;

/**
 * Created by npike on 2/25/16.
 */
public class PublishReviewAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        if (project == null) {
            return;
        }
        if (!ReviewManager.getInstance().isStarted(project)) {
            return;
        }

        ReviewManager.getInstance().publish();
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
