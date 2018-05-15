package com.zhouhaoh.mvcmarker.action;

/**
 *
 * 替换模板文件中的model
 * Created by zhouhaoh on 2018/5/8.
 */
public class ReplaceModel {
    private String basePackage;
    private String funcPackage;
    private String mapperPackage;
    private String domainDto;
    private String controllerMapping;

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getFuncPackage() {
        return funcPackage;
    }

    public void setFuncPackage(String funcPackage) {
        this.funcPackage = funcPackage;
    }

    public String getMapperPackage() {
        return mapperPackage;
    }

    public void setMapperPackage(String mapperPackage) {
        this.mapperPackage = mapperPackage;
    }

    public String getDomainDto() {
        return domainDto;
    }

    public void setDomainDto(String domainDto) {
        this.domainDto = domainDto;
    }

    public String getControllerMapping() {
        return controllerMapping;
    }

    public void setControllerMapping(String controllerMapping) {
        this.controllerMapping = controllerMapping;
    }
}
