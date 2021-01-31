package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

@Entity(nameInDb = "printers")
public class Printer {

    @SerializedName("id")
    @Id(autoincrement = true)
    private Long id;

    @SerializedName("uuid")
    @Property(nameInDb = "serverId")
    private String uuId;

    @SerializedName("name")
    @Property(nameInDb = "name")
    private String name;

    @SerializedName("printer_ip")
    @Property(nameInDb = "ipAddress")
    private String ipAddress;

    @SerializedName("printer_port")
    @Property(nameInDb = "port")
    private Long port;

    @SerializedName("pos_listening_port")
    @Property(nameInDb = "listeningPort")
    private Long posPort;

    @Property(nameInDb = "type")
    private Long type;

    @Property(nameInDb = "format")
    private String format;

    @SerializedName("printer_protocol_uuid")
    @Property(nameInDb = "protocolId")
    private String protocolId;

    public PrinterProtocol getPrinterProtocol() {
        return printerProtocol;
    }

    public void setPrinterProtocol(PrinterProtocol printerProtocol) {
        this.printerProtocol = printerProtocol;
    }

    @Property(nameInDb = "protocol")
    private String protocol;

    @SerializedName("created_at")
    @Property(nameInDb = "createdAt")
    private String createdAt;

    @SerializedName("updated_at")
    @Property(nameInDb = "updatedAt")
    private String updatedAt;

    @Transient
    @SerializedName("printer_protocol")
    private PrinterProtocol printerProtocol;


    @Generated(hash = 1729996659)
    public Printer(Long id, String uuId, String name, String ipAddress, Long port,
            Long posPort, Long type, String format, String protocolId,
            String protocol, String createdAt, String updatedAt) {
        this.id = id;
        this.uuId = uuId;
        this.name = name;
        this.ipAddress = ipAddress;
        this.port = port;
        this.posPort = posPort;
        this.type = type;
        this.format = format;
        this.protocolId = protocolId;
        this.protocol = protocol;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Generated(hash = 206512902)
    public Printer() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuId() {
        return this.uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Long getPort() {
        return this.port;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    public Long getPosPort() {
        return this.posPort;
    }

    public void setPosPort(Long posPort) {
        this.posPort = posPort;
    }

    public String getProtocolId() {
        return this.protocolId;
    }

    public void setProtocolId(String protocolId) {
        this.protocolId = protocolId;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Long getType() {
        return this.type;
    }

    public void setType(Long type) {
        this.type = type;
    }


}
