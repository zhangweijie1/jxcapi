package com.mrguo.dao.log;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.log.Log;
import org.springframework.stereotype.Repository;

/**
 * @ClassName LogMapper
 * @Description 系统日志接口
 * @author 郭成兴
 * @date 2019/8/5 6:29 PM
 * @updater 郭成兴
 * @updatedate 2019/8/5 6:29 PM
 */
@Repository("logMapper")
public interface LogMapper extends MyMapper<Log> {
}