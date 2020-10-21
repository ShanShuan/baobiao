package com.pc.huangshan.model;



import java.util.Collection;
import java.util.Date;

public class UserInfo  {
    private Integer id;

    private String accName;

    private String accPass;

    private String accNo;

    private String realName;

    private String accType;

    private String accStatus;

    private String ifAdmin;

    private String orderTips;

    private Date createTime;

    private String createBy;

    private Date modifyTime;

    private String modifyBy;

    private String deleted;

    private String corpCode;

    private Date loginDate;

    private Integer wrongTimes;

    private Date loginLockTime;

    private Date resetPassLockTime;

    private Integer distributorId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName == null ? null : accName.trim();
    }

    public String getAccPass() {
        return accPass;
    }

    public void setAccPass(String accPass) {
        this.accPass = accPass == null ? null : accPass.trim();
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo == null ? null : accNo.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType == null ? null : accType.trim();
    }

    public String getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus == null ? null : accStatus.trim();
    }

    public String getIfAdmin() {
        return ifAdmin;
    }

    public void setIfAdmin(String ifAdmin) {
        this.ifAdmin = ifAdmin == null ? null : ifAdmin.trim();
    }

    public String getOrderTips() {
        return orderTips;
    }

    public void setOrderTips(String orderTips) {
        this.orderTips = orderTips == null ? null : orderTips.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy == null ? null : modifyBy.trim();
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted == null ? null : deleted.trim();
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode == null ? null : corpCode.trim();
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public Integer getWrongTimes() {
        return wrongTimes;
    }

    public void setWrongTimes(Integer wrongTimes) {
        this.wrongTimes = wrongTimes;
    }

    public Date getLoginLockTime() {
        return loginLockTime;
    }

    public void setLoginLockTime(Date loginLockTime) {
        this.loginLockTime = loginLockTime;
    }

    public Date getResetPassLockTime() {
        return resetPassLockTime;
    }

    public void setResetPassLockTime(Date resetPassLockTime) {
        this.resetPassLockTime = resetPassLockTime;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

}