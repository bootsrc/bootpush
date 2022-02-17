package com.github.bootsrc.bootpush.api.model;

import java.io.Serializable;

public class StandardBody implements Serializable {
    private static final long serialVersionUID = -6400481841100013448L;

    /**
     * contentType:text/json
     */
    private String contentType;
    private String content;
    private Object extras;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getExtras() {
        return extras;
    }

    public void setExtras(Object extras) {
        this.extras = extras;
    }
}
