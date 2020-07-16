package com.jwc.geo.model;

public class GeoUpdateModel extends GeoAddModel {
    private Integer id;

    private String pinyin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
}
