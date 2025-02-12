package com.sakura.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sakura.model.po.system.SysOperLog;
import com.sakura.service.system.SysOperLogService;
import com.sakura.mapper.system.SysOperLogMapper;
import org.springframework.stereotype.Service;

/**
* @author sakura
* @description 针对表【sys_oper_log(操作日志记录)】的数据库操作Service实现
* @createDate 2025-02-11 17:32:51
*/
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog>
    implements SysOperLogService{

}




