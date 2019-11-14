/*
 * 天虹商场股份有限公司版权所有.
 */
package com.xhg.server.a;


import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;


/**
 * 上传图片 请求数据对象
 * <p>
 *
 * @author 周怡成
 * @version ： 1.0.0
 * @date 2019-04-15
 */
public class UploadImageRequest {

    //增加默认值
    public UploadImageRequest() {
        this.thumbnail = 0;
    }

    /**
     * 图片文件
     */
    @NotNull(message = "图片文件 不能为空")
    private MultipartFile file;

    /**
     * 缩略图，值0=否，值1=80x80，值2=145x145，值4=220x220，值8=220x280，值16=390x390，值32=600x600，值64=800x800，多缩略图值相加
     */
    private Integer thumbnail;


    /**
     * 图片来源，默认0，0=商品，1=退货
     */
    private int source;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Integer getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Integer thumbnail) {
        this.thumbnail = thumbnail;
    }


    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "UploadImageRequest{" +
                "file=" + file +
                ", thumbnail=" + thumbnail +
                ", source=" + source +
                '}';
    }
}
