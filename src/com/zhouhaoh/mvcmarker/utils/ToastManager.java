package com.zhouhaoh.mvcmarker.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

/**
 * 提示工具类
 * <p>
 * Create by zhouhaoh on 2018/05/08
 */
public class ToastManager {

    private static WeakReference<Project> projectWeakReference;

    public static void setProject(Project project) {
        projectWeakReference = new WeakReference<Project>(project);
    }

    public static void error(final String message) {
        ifProjectExist(new PluginHelper.Callback<Project>() {
            @Override
            public void call(Project project) {
                toast(project, MessageType.ERROR, message);
            }
        });
        projectWeakReference = null;
    }

    public static void info(final String message) {
        ifProjectExist(new PluginHelper.Callback<Project>() {
            @Override
            public void call(Project project) {
                toast(project, MessageType.INFO, message);
            }
        });
    }

    public static void warn(final String message) {
        ifProjectExist(new PluginHelper.Callback<Project>() {
            @Override
            public void call(Project project) {
                toast(project, MessageType.WARNING, message);
            }
        });
    }

    private static void ifProjectExist(@NotNull PluginHelper.Callback<Project> callback) {
        if (projectWeakReference != null) {
            Project project = projectWeakReference.get();
            if (project != null) {
                callback.call(project);
            }
        }
    }

    public static void toast(Project project, MessageType messageType, String message) {
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(message, messageType, null)
                .setFadeoutTime(5000)
                .createBalloon()
                .show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
    }
}
