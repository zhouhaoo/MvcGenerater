package com.zhouhaoh.mvcmarker.ui;

import com.zhouhaoh.mvcmarker.action.TemplateData;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class MvcGeneraterDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField mDomain;
    private JTextField mController;
    //    private JTextField mMapper;
//    private JTextField mVmManage;
//    private JTextField mServiceImpl;
    private OkActionClickListener okActionClickListener;

    /**
     * @param templateData          --历史录入数据
     * @param okActionClickListener -- 确认点击后的数据
     */
    public MvcGeneraterDialog(TemplateData templateData, OkActionClickListener okActionClickListener) {
        setTitle("模板配置");
        this.okActionClickListener = okActionClickListener;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        loadConfig(templateData);
        initdialog();
    }

    /**
     * 显示配置到输入框
     *
     * @param templateData
     */
    private void loadConfig(TemplateData templateData) {
        if (templateData != null) {
            mDomain.setText(templateData.getDomain());
//            mMapper.setText(templateData.getMapper());
//            mServiceImpl.setText(templateData.getServiceImpl());
            mController.setText(templateData.getController());
//            mVmManage.setText(templateData.getVmManage());
        }
    }

    private void initdialog() {
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        TemplateData data = new TemplateData();
        data.setDomain(mDomain.getText());
        data.setController(mController.getText());
//        data.setMapper(mMapper.getText());
//        data.setServiceImpl(mServiceImpl.getText());
//        data.setVmManage(mVmManage.getText());
        if (okActionClickListener != null) {
            okActionClickListener.onOkActionClicked(data);
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    /**
     * 确认点击监听
     */
    public interface OkActionClickListener {
        /**
         * 输入框的数据
         *
         * @param templateData
         */
        void onOkActionClicked(TemplateData templateData);
    }
}
