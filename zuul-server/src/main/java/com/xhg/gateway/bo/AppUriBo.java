package com.xhg.gateway.bo;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author K神带你飞
 * @since 2018-05-28
 */
public class AppUriBo implements Serializable {

    private Integer id;

    /**
     * 应用id(app表的)
     */
    @NotNull(message = "应用id不能为空")
    private Integer appId;

    /**
     * 非受权uri
     */
    @NotBlank(message = "url不能为空")
    @Pattern(regexp="/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]", message = "uri格式不对")
    private String url;

    /**
     * uri作用描述
     */
    private String description;

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
}
