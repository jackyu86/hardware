package com.hw.hardware.domain.base;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 领域模型基类(常规公共字段)<br/>
 * 一律使用引用类型
 * @author cfish
 * @since 2013-09-09
 */
public class BaseDomain extends BaseQuery {
	private static final long serialVersionUID = 1L;
	private Long id;// 编号
	@XStreamOmitField
	private String uuid;// 唯一编号
	@XStreamOmitField
	private String code;// 编码
	@XStreamOmitField
	private String remark;// 备注
	@XStreamOmitField
	private Date createDate;// 创建日期
	@XStreamOmitField
	private String createUser;// 创建者
	@XStreamOmitField
	private Date modifyDate;// 最后修改日期
	@XStreamOmitField
	private String modifyUser;// 最后修改者
	@XStreamOmitField
	private Integer isDel;// 是否删除
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@JSONField(serialize=false)
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@JSONField(serialize=false)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@JSONField(serialize=false)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@JSONField(serialize=false)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@JSONField(serialize=false)
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	@JSONField(serialize=false)
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	@JSONField(serialize=false)
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	
	@JSONField(serialize=false)
	public Integer getIsDel() {
		return isDel;
	}
	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}
}
