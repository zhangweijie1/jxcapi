package com.mrguo.entity.log;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 系统日志实体
 * @author mrguo
 *
 */
@Data
@Table(name="log_oper")
public class Log {

	public final static String LOGIN_ACTION="登录操作";
	public final static String LOGOUT_ACTION="注销操作";
	public final static String SEARCH_ACTION="查询操作";
	public final static String UPDATE_ACTION="更新操作";
	public final static String ADD_ACTION="添加操作";
	public final static String DELETE_ACTION="删除操作";

	/**
	 * 编号
	 */
	@Id
	@JsonSerialize(using = ToStringSerializer.class)
	private Integer id;

	/**
	 * 日志类型
	 */
	@Column(length=100)
	private String type; 

	/**
	 * 操作用户
	 */
	@Column(name="user_id")
	private Long userId;

	/**
	 * 操作内容
	 */
	@Column(length=1000)
	private String content; 

	/**
	 * 操作时间
	 */
	@Temporal(TemporalType.TIMESTAMP) 
	private Date time; 

	/**
	 * 起始时间  搜索用到
	 */
	@Transient
	private Date btime; 

	/**
	 * 结束时间  搜索用到
	 */
	@Transient
	private Date etime; 
	

	public Log() {
		super();
		 //TODO Auto-generated constructor stub
	}

	public Log(String type,String content) {
		super();
		this.type = type;
		this.content = content;
	}
}
