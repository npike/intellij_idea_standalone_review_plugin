package net.npike.intellij.standalonereview.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;

/**
 * Created by npike on 2/26/16.
 */
public class Toast {

    public static final int TOAST_LONG = 7500;

    public static void show(AnActionEvent event, String message, int timeout) {
        StatusBar statusBar = WindowManager.getInstance()
                .getStatusBar(DataKeys.PROJECT.getData(event.getDataContext()));
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(message, MessageType.INFO, null)
                .setFadeoutTime(TOAST_LONG)
                .createBalloon()
                .show(RelativePoint.getCenterOf(statusBar.getComponent()),
                        Balloon.Position.atRight);
    }
}
