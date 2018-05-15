package com.zhouhaoh.mvcmarker.action;

/**
 * 录入框数据model
 * <p>
 * Create by zhouhaoh on 2018/05/08
 */
public class TemplateData {
    private String domain;
    private String mapper;
    private String serviceImpl;
    private String controller;
    private String vmManage;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getMapper() {
        return mapper;
    }

    public void setMapper(String mapper) {
        this.mapper = mapper;
    }

    public String getServiceImpl() {
        return serviceImpl;
    }

    public void setServiceImpl(String serviceImpl) {
        this.serviceImpl = serviceImpl;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getVmManage() {
        return vmManage;
    }

    public void setVmManage(String vmManage) {
        this.vmManage = vmManage;
    }
}
