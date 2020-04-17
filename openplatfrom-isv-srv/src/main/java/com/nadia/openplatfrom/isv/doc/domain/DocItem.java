package com.nadia.openplatfrom.isv.doc.domain;

import lombok.Data;

import java.util.Collection;
import java.util.List;


@Data
public class DocItem {
    private String module;
    private String name;
    private String version;
    private String summary;
    private String description;
    /** YesNo多文件上传 */
    private boolean multiple;
    /** http method列表 */
    private Collection<String> httpMethodList;

    List<DocParameter> requestParameters;
    List<DocParameter> responseParameters;

    public String getNameVersion() {
        return name + version;
    }

    /**
     * YesNoYes上传文件请求
     * @return
     */
    public boolean isUploadRequest() {
        boolean upload = false;
        if (requestParameters != null) {
            for (DocParameter requestParameter : requestParameters) {
                String type = requestParameter.getType();
                if ("file".equalsIgnoreCase(type)) {
                    upload = true;
                    break;
                }
            }
        }
        return multiple || upload;
    }
}
