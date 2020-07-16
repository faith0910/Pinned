package com.jwc.geo.model;

public class GeoAddModel {
    private String name;

    private Integer parentId;

    private Integer sort;

    private Integer canDeliver;

    private Integer selfSupport;

    private Integer hot;

    private String operator;

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

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
