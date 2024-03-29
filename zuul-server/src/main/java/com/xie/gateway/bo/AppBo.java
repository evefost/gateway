package com.xie.gateway.bo;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author K神带你飞
 * @since 2018-05-28
 */
public class AppBo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 应用唯一标识
     */
    @NotBlank(message = "服务serviceId不能为空")
    private String serviceId;

    /**
     * 应用名称
     */
    @NotBlank(message = "应用名称不能为空")
    private String name;

    /**
     * 应用描述
     */
    private String description;

    /**
     * 版本号
     */
    @NotBlank(message = "应用版本不能为空")
    private String version;

    /**
     * 应用根路径
     */
    private String contextPath;

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "App{" +
        ", id=" + id +
        ", serverId=" + serviceId +
        ", name=" + name +
        ", description=" + description +
        ", version=" + version +
        "}";
    }
}
