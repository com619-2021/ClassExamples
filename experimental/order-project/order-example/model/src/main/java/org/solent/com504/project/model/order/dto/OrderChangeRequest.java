package org.solent.com504.project.model.order.dto;

import java.util.Date;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.solent.com504.project.model.party.dto.Party;
import org.solent.com504.project.model.party.dto.PartyHref;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderChangeRequest {

    private Order changeRequest;

    private Date requestDate;

    private Date approvedDate;

    private ChangeStatus status;

    private String changeReason;

    private PartyHref changeRequestor;

    private String responseDescription;

    private Long id;

    private String uuid;

    private String href;

    private String orderUuid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getChangeRequest() {
        return changeRequest;
    }

    public void setChangeRequest(Order changeRequest) {
        this.changeRequest = changeRequest;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public ChangeStatus getStatus() {
        return status;
    }

    public void setStatus(ChangeStatus status) {
        this.status = status;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public PartyHref getChangeRequestor() {
        return changeRequestor;
    }

    public void setChangeRequestor(PartyHref changeRequestor) {
        this.changeRequestor = changeRequestor;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getOrderUuid() {
        return orderUuid;
    }

    public void setOrderUuid(String orderUuid) {
        this.orderUuid = orderUuid;
    }

    @Override
    public String toString() {
        return "OrderChangeRequest{" + "changeRequest=" + changeRequest + ", requestDate=" + requestDate + ", approvedDate=" + approvedDate + ", status=" + status + ", changeReason=" + changeReason + ", changeRequestor=" + changeRequestor + ", responseDescription=" + responseDescription + ", id=" + id + ", uuid=" + uuid + ", href=" + href + ", orderUuid=" + orderUuid + '}';
    }

}
