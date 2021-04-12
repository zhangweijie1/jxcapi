package com.mrguo.service.impl.log;

import java.util.Date;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.log.LogMapper;
import com.mrguo.dao.sys.SysUserMapper;
import com.mrguo.entity.sys.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrguo.entity.log.Log;
import com.mrguo.service.inter.LogService;

/**
 * 系统日志Service实现类
 * @author mrguo
 */
@Service("logService")
public class LogServiceImpl extends BaseServiceImpl<Log> implements LogService{

	@Autowired
	private LogMapper logMapper;

	@Autowired
	private SysUserMapper userMapper;

	@Override
	public MyMapper<Log> getMapper() {
		return logMapper;
	}

	@Override
	public void save(Log log) {
		// 设置操作日期
		log.setTime(new Date());
		// 设置用户名
        SysUser sysUser = new SysUser();
		logMapper.insertSelective(log);
	}

	public IPage<Log> list(PageParams pageParams) {
		return null;
	}

	@Override
	public Long getCount(Log log) {
		return null;
	}

}
