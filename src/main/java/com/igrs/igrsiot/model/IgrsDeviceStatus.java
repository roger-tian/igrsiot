package com.igrs.igrsiot.model;

public class IgrsDeviceStatus {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDevice() {
        return device;
    }

    public void setDevice(Long device) {
        this.device = device;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getClientChannel() {
        return clientChannel;
    }

    public void setClientChannel(String clientChannel) {
        this.clientChannel = clientChannel;
    }

    private Long id;
    public Long device;
    private String attribute;
    private String value;
    private String clientChannel;
}
