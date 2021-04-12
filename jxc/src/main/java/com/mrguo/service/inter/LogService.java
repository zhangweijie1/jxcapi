package com.mrguo.service.inter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mrguo.common.entity.PageParams;
import com.mrguo.entity.log.Log;

/**
 * 系统日志Service接口
 * @author mrguo
 *
 */
public interface LogService {


	
	/**
	 * 修改或者修改日志信息
	 * @param log
	 */
	public void save(Log log);
	
	/**
	 * 根据条件分页查询日志信息
	 * @param pageParams
	 * @return
	 */
	public IPage<Log> list(PageParams<Log> pageParams);
	
	/**
	 * 获取总记录数
	 * @param log
	 * @return
	 */
	public Long getCount(Log log);

}
