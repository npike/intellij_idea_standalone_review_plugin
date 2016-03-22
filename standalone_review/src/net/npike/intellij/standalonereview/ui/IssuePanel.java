package net.npike.intellij.standalonereview.ui;

import javax.swing.*;

public class IssuePanel {
    private JPanel myPane;
    private JTextArea mTextAreaComment;
    private JTextArea jTextAreaCodePreview;

    public IssuePanel() {
        // NOOP
    }

    public void putCodePreview(String code) {
        jTextAreaCodePreview.setText(code);
    }

    public String getComment() {
        return mTextAreaComment.getText();
    }

    public JPanel getPanel() {
        mTextAreaComment.requestFocusInWindow();
        return myPane;
    }


}