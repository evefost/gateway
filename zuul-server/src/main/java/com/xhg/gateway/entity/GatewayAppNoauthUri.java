package com.xhg.gateway.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author K神带你飞
 * @since 2018-05-29
 */
public class GatewayAppNoauthUri implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 应用id(app表的)
     */
    private Integer appId;

    /**
     * 非受限url
     */
    private String url;
    /**
     * uri作用描述
     */
    private String description;
    /**
     * 1,启用(非受权uri生效)，0禁用
     */
    private Integer enable;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "GatewayAppNoauthUri{" +
        ", id=" + id +
        ", appId=" + appId +
        ", url=" + url +
        ", description=" + description +
        ", enable=" + enable +
        "}";
    }
}
