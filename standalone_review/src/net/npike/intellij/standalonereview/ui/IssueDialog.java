package net.npike.intellij.standalonereview.ui;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;

import net.npike.intellij.standalonereview.ReviewManager;
import net.npike.intellij.standalonereview.models.Review;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class IssueDialog extends DialogWrapper {
    private static final Logger LOGGER = Logger.getInstance(ReviewManager.class.getName());

    private IssuePanel mCenterPanel;
    private String mFilePath;
    private int[] mLines;

    public IssueDialog() {
        super(false);

        setup();
    }

    public IssueDialog(String filepath, int[] lines) {
        super(false);
        mFilePath = filepath;
        mLines = lines;

        setup();
    }

    private void setup() {
        setTitle("New comment");
        this.mCenterPanel = new IssuePanel();
        this.setResizable(true);

        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        mCenterPanel.getPanel().requestFocus();
        return mCenterPanel.getPanel();
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mCenterPanel.getPanel();
    }

    protected Action[] createActions() {
        return new Action[]{getOKAction(), getCancelAction()};
    }

    @Override
    protected void doOKAction() {
        LOGGER.info("doOKAction");

        if (mFilePath != null) {
            ReviewManager.getInstance().addComment(mFilePath, mLines, mCenterPanel.getComment());
        } else {
            ReviewManager.getInstance().addComment(mCenterPanel.getComment());
        }

        super.doOKAction();
    }
}