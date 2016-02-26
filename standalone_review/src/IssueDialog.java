

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.DialogWrapper;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class IssueDialog extends DialogWrapper {
    private static final Logger LOGGER = Logger.getInstance(ReviewManager.class.getName());

    private final IssuePanel mCenterPanel;
    private final String mFilePath;
    private final int[] mLines;

    public IssueDialog(String filepath, int[] lines) {
        super(false);
        setTitle("New comment");
        this.mCenterPanel = new IssuePanel();
        this.setResizable(true);

        mFilePath = filepath;
        mLines = lines;

        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return mCenterPanel.getPanel();
    }

    protected Action[] createActions() {
        return new Action[]{getOKAction(), getCancelAction()};
    }

    @Override
    protected void doOKAction() {
        LOGGER.info("doOKAction");

        ReviewManager.getInstance().addComment(mFilePath, mLines, mCenterPanel.getComment());

        super.doOKAction();
    }
}