package com.zhouhaoh.mvcmarker.action;

import com.google.common.base.CaseFormat;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.zhouhaoh.mvcmarker.TemplateType;
import com.zhouhaoh.mvcmarker.ui.MvcGeneraterDialog;
import com.zhouhaoh.mvcmarker.utils.PluginHelper;
import com.zhouhaoh.mvcmarker.utils.ToastManager;
import org.apache.commons.lang.WordUtils;

import javax.xml.ws.handler.PortInfo;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import java.io.*;

/**
 * 生成普通mvc的代码模板。
 * <p>
 * Create by zhouhaoh on 2018/05/07
 */
public class MvcGeneraterAction extends AnAction implements MvcGeneraterDialog.OkActionClickListener {
    /**
     *
     */
    private String basePath;
    private AnActionEvent anActionEvent;

    /**
     * 点击插件时候，调用的方法
     *
     * @param anActionEvent
     */
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        basePath = anActionEvent.getProject().getBasePath();
        this.anActionEvent = anActionEvent;
        PluginHelper.execute(project -> initDialog(project), anActionEvent);
    }

    /**
     * 弹出配置确认界面,读取模板文件
     *
     * @param project
     */
    private void initDialog(AnActionEvent project) {
        ToastManager.setProject(project.getProject());
        MvcGeneraterDialog dialog = new MvcGeneraterDialog(PluginHelper.loadConfig(), this);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        dialog.requestFocus();
    }

    @Override
    public void onOkActionClicked(TemplateData templateData) {
        createTemplateFile(templateData);
        //刷新配置、项目文件。
        PluginHelper.saveConfig(templateData);
    }

    /**
     * 替换字段
     *
     * @param templateData
     */
    private void createTemplateFile(TemplateData templateData) {
        ReplaceModel replacemodel = new ReplaceModel();
        String[] domainStrs = templateData.getDomain().split("\\.");
        replacemodel.setBasePackage(domainStrs[0] + "." + domainStrs[1] + "." + domainStrs[2]);
        replacemodel.setFuncPackage(templateData.getController());
        replacemodel.setDomainDto(domainStrs[domainStrs.length - 1]);
        replacemodel.setMapperPackage(domainStrs[domainStrs.length - 2]);

        String mappingStr = "/" + templateData.getController() + "/";
        if (mappingStr.contains(".")) {
            mappingStr = mappingStr.replace(".", "/");
        }
        replacemodel.setControllerMapping(mappingStr);
        generaterFile(TemplateType.Controller, replacemodel);
        generaterFile(TemplateType.Service, replacemodel);
        generaterFile(TemplateType.ServiceImpl, replacemodel);
        generaterFile(TemplateType.VmManage, replacemodel);
//        ToastManager.info("生成代码成功~");
        refreshProject(anActionEvent);
    }

    /**
     * 刷新目录文件
     *
     * @param anActionEvent
     */
    private void refreshProject(AnActionEvent anActionEvent) {
        anActionEvent.getProject().getBaseDir().refresh(false, true);
    }

    /**
     * 生成模板文件
     *
     * @param type
     */
    private void generaterFile(TemplateType type, ReplaceModel replacemodel) {
        String templateFileName = "";
        String content = "";
        String dirPath = "";
        String fileName = "";
        String basePath = replacemodel.getBasePackage().replace(".", "/");
        switch (type) {
            case Controller:
                templateFileName = "ControllerTemplate.txt";
                content = readTemplateFile(templateFileName);
                content = replaceTempalteContent(content, replacemodel);
                dirPath = "/web/src/main/java/" + basePath + "/mvc/" + getfuncPath(replacemodel.getFuncPackage());
                fileName = replacemodel.getDomainDto() + "Controller.java";
                break;
            case Service:
                templateFileName = "ServiceTemplate.txt";
                content = readTemplateFile(templateFileName);
                content = replaceTempalteContent(content, replacemodel);
                dirPath = "/service/src/main/java/" + basePath + "/service/" + getfuncPath(replacemodel.getFuncPackage());
                fileName = replacemodel.getDomainDto() + "Service.java";
                break;
            case ServiceImpl:
                templateFileName = "ServiceImpITemplate.txt";
                content = readTemplateFile(templateFileName);
                content = replaceTempalteContent(content, replacemodel);
                String serviceImplMName =  "/service-"+basePath.substring(basePath.lastIndexOf("/")+1,basePath.length());
                dirPath = serviceImplMName + "/src/main/java/" + basePath + "/service/" + getfuncPath(replacemodel.getFuncPackage());
                fileName = replacemodel.getDomainDto() + "ServiceImpl.java";
                break;
            case VmManage:
                templateFileName = "vm_manage.txt";
                content = readTemplateFile(templateFileName);
                dirPath = "/web/src/main/webapp/WEB-INF/views/" + getfuncPath(replacemodel.getFuncPackage());
                fileName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, replacemodel.getDomainDto()) + "_manage.vm";
                break;
        }
        writeFile(content, this.basePath + dirPath, fileName);
//        String aaa = "aaaa";

    }

    private String getfuncPath(String funcPackStr) {
        if (funcPackStr.contains(".")) {
            return funcPackStr.replace(".", "/");
        }
        return funcPackStr;
    }

    /**
     * 写入文件
     *
     * @param content
     * @param dirPath
     * @param fileName
     */
    private void writeFile(String content, String dirPath, String fileName) {
        try {
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dirPath + "/" + fileName);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                ToastManager.info(fileName + "已存在，不再生成。");
                return;
            }
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsoluteFile(), false), "utf-8"));
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 替换字符
     *
     * @param content
     * @return
     */
    private String replaceTempalteContent(String content, ReplaceModel replacemodel) {

        if (content.contains("$basepackage")) {
            content = content.replace("$basepackage", replacemodel.getBasePackage());
        }
        if (content.contains("$funcPackage")) {
            content = content.replace("$funcPackage", replacemodel.getFuncPackage());
        }
        if (content.contains("$mapperpackage")) {
            content = content.replace("$mapperpackage", replacemodel.getMapperPackage());
        }
        if (content.contains("$DomainDto")) {
            content = content.replace("$DomainDto", replacemodel.getDomainDto());
        }
        if (content.contains("$domainDto")) {
            content = content.replace("$domainDto", WordUtils.uncapitalize(replacemodel.getDomainDto()));
        }
        if (content.contains("$controllerMapping")) {
            content = content.replace("$controllerMapping", replacemodel.getControllerMapping());
        }
        if (content.contains("$author")) {
            content = content.replace("$author", PluginHelper.getUser());
        }
        if (content.contains("$date")) {
            content = content.replace("$date", PluginHelper.getDate());
        }
        return content;
    }

    /**
     * 读取模板文件中的字符内容
     *
     * @param fileName 模板文件名
     */
    private String readTemplateFile(String fileName) {
        InputStream in = null;
        in = this.getClass().getResourceAsStream("/template/" + fileName);
        String content = "";
        try {
            content = new String(readStream(in), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 读取数据
     *
     * @param inputStream
     */
    private byte[] readStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            outputStream.close();
            inputStream.close();
        }
        return outputStream.toByteArray();
    }

}
