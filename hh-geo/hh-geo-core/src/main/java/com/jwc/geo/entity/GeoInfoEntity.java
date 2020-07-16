package com.jwc.geo.entity;

public class GeoInfoEntity {
    private Integer id;

    private String name;

    private Integer parentId;

    private Integer level;

    private Integer sort;

    private Integer canDeliver;

    private Integer selfSupport;

    private String pinyin;

    private String initial;

    private Integer hot;

    private Integer version;

    private String creator;

    private String modifier;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getCanDeliver() {
        return canDeliver;
    }

    public void setCanDeliver(Integer canDeliver) {
        this.canDeliver = canDeliver;
    }

    public Integer getSelfSupport() {
        return selfSupport;
    }

    public void setSelfSupport(Integer selfSupport) {
        this.selfSupport = selfSupport;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}