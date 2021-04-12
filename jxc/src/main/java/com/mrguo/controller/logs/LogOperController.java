package com.mrguo.controller.logs;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mrguo.common.entity.PageParams;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrguo.service.inter.LogService;

/**
 * 后台系统操作日志Controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/log/oper")
public class LogOperController {

	@Resource
	private LogService logService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(true);
		//true:允许输入空值，false:不能为空值
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	/**
	 * 根据条件分页查询日志信息
	 * @param pageParams
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/list")
	@RequiresPermissions(value = { "系统日志" })
	public IPage list(PageParams pageParams)throws Exception{
		return logService.list(pageParams);
	}
}
