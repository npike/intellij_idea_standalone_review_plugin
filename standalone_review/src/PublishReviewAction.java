import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * Created by npike on 2/25/16.
 */
public class PublishReviewAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        if (!ReviewManager.getInstance().isStarted()) {
            return;
        }

        ReviewManager.getInstance().publish();
    }
}
