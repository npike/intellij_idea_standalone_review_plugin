import javax.swing.*;

public class IssuePanel {
    private JPanel myPane;
    private JTextArea mTextAreaComment;

    public IssuePanel() {
        // NOOP
    }

    public String getComment() {
        return mTextAreaComment.getText();
    }

    public JPanel getPanel() {
        return myPane;
    }
}