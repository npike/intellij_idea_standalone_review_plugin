package net.npike.intellij.standalonereview.ui;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.DialogWrapper;

import net.npike.intellij.standalonereview.ReviewManager;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class IssueDialog extends DialogWrapper {
    private static final Logger LOGGER = Logger.getInstance(ReviewManager.class.getName());

    private IssuePanel mIssuePanel;
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

    public void setCodePreview(String code) {
        mIssuePanel.putCodePreview(code);
    }

    private void setup() {
        setTitle("New comment");
        this.mIssuePanel = new IssuePanel();
        this.setResizable(true);

        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        mIssuePanel.getPanel().requestFocus();
        return mIssuePanel.getPanel();
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mIssuePanel.getPanel();
    }

    protected Action[] createActions() {
        return new Action[]{getOKAction(), getCancelAction()};
    }

    @Override
    protected void doOKAction() {
        LOGGER.info("doOKAction");

        if (mFilePath != null) {
            ReviewManager.getInstance().addComment(mFilePath, mLines, mIssuePanel.getComment());
        } else {
            ReviewManager.getInstance().addComment(mIssuePanel.getComment());
        }

        super.doOKAction();
    }
}