package com.github.bootsrc.bootpush.api.model;

import java.io.Serializable;

public class StandardMessage implements Serializable {
    private static final long serialVersionUID = -8882418738633533519L;
    private StandardHeader header;
    private StandardBody body;

    public StandardHeader getHeader() {
        return header;
    }

    public void setHeader(StandardHeader header) {
        this.header = header;
    }

    public StandardBody getBody() {
        return body;
    }

    public void setBody(StandardBody body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "StandardMessage{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }
}
