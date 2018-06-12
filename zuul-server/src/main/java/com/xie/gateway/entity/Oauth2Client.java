package com.xie.gateway.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import java.io.Serializable;

/**
 * <p>
 * 客户端认证信息(只有注册的应用才能被认证)
 * </p>
 *
 * @author K神带你飞
 * @since 2018-05-30
 */
public class Oauth2Client implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户端id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 客户端名称
     */
    private String clientName;
    /**
     * 客户端ID
     */
    private String clientId;
    /**
     * 客户端安全码
     */
    private String clientSecret;
    /**
     * 重定向uri
     */
    private String redirectUri;
    /**
     * 客户端uri
     */
    private String clientUri;
    /**
     * 客户端图标
     */
    private String clientIconUri;
    /**
     * 资源id
     */
    private String resourceIds;
    /**
     * 授权范围
     */
    private String scope;
    /**
     * 授权类型
     */
    private String grantTypes;
    /**
     * 允许的IP
     */
    private String allowedIps;
    /**
     * 客户端状态
     */
    private Integer status;
    /**
     * 可信
     */
    private Integer trusted;
    /**
     * 有效期
     */
    private Integer expire;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getClientUri() {
        return clientUri;
    }

    public void setClientUri(String clientUri) {
        this.clientUri = clientUri;
    }

    public String getClientIconUri() {
        return clientIconUri;
    }

    public void setClientIconUri(String clientIconUri) {
        this.clientIconUri = clientIconUri;
    }

    public String getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(String grantTypes) {
        this.grantTypes = grantTypes;
    }

    public String getAllowedIps() {
        return allowedIps;
    }

    public void setAllowedIps(String allowedIps) {
        this.allowedIps = allowedIps;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTrusted() {
        return trusted;
    }

    public void setTrusted(Integer trusted) {
        this.trusted = trusted;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }

    @Override
    public String toString() {
        return "Oauth2Client{" +
        ", id=" + id +
        ", clientName=" + clientName +
        ", clientId=" + clientId +
        ", clientSecret=" + clientSecret +
        ", redirectUri=" + redirectUri +
        ", clientUri=" + clientUri +
        ", clientIconUri=" + clientIconUri +
        ", resourceIds=" + resourceIds +
        ", scope=" + scope +
        ", grantTypes=" + grantTypes +
        ", allowedIps=" + allowedIps +
        ", status=" + status +
        ", trusted=" + trusted +
        ", expire=" + expire +
        "}";
    }
}
