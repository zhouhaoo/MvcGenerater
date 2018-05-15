package com.zhouhaoh.mvcmarker.utils;

import com.intellij.ide.util.PropertiesComponent;
import com.zhouhaoh.mvcmarker.action.TemplateData;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 插件工具类
 * <p>
 * Create by zhouhaoh on 2018/05/08
 */
public class PluginHelper {
    public static final String PROPERTIES_KEY = "mvc_generate_key_config";

    /**
     * 保存配置
     *
     * @param templateData
     */
    public static void saveConfig(TemplateData templateData) {
        String[] strings = {templateData.getDomain(), templateData.getController()};
        PropertiesComponent.getInstance().setValues(PROPERTIES_KEY, strings);
    }

    /**
     * 读取配置
     *
     * @return
     */
    public static TemplateData loadConfig() {
        String[] values = PropertiesComponent.getInstance().getValues(PROPERTIES_KEY);
        if (values != null && values.length > 0) {
            TemplateData data = new TemplateData();
            data.setDomain(values[0]);
            data.setController(values[1]);
//            data.setMapper(values[2]);
//            data.setServiceImpl(values[3]);
//            data.setVmManage(values[4]);
            return data;
        }
        return null;
    }

    public interface Callback<T> {
        void call(T project);
    }

    public static <T> void execute(Callback<T> callback, T data) {
        try {
            callback.call(data);
        } catch (Throwable e) {
            String errorMsg = e.getMessage();
            if (e instanceof ArrayIndexOutOfBoundsException) {
                ToastManager.error("确认在项目目录下 \"pom.mxl\"");
            } else {
                ToastManager.error(isEmpty(errorMsg) ? "Unknown error." : errorMsg);
            }
        }
    }

    public static boolean isEmpty(String... contents) {
        for (String content : contents) {
            if (content == null || content.length() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取用户
     *
     * @return
     */
    public static String getUser() {
        return System.getProperty("user.name");
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static void deleteFile(@NotNull File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteFile(f);
                }
            }
        }
    }
}
