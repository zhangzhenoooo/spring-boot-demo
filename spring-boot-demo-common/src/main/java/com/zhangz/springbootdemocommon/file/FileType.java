package com.zhangz.springbootdemocommon.file;

public enum FileType {
    XLSX("xlsx"), JSON("json"), TXT("txt"), PDF("pdf"), PNG("png"), GIF("gif"), JPG("jpg"),

    SQL("sql");

    private String type;

    private FileType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getTypeWithPoint() {
        return "." + type;
    }

}
