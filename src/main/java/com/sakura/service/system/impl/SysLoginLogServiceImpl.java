package com.sakura.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sakura.model.po.system.SysLoginLog;
import com.sakura.service.system.SysLoginLogService;
import com.sakura.mapper.system.SysLoginLogMapper;
import org.springframework.stereotype.Service;

/**
* @author sakura
* @description 针对表【sys_login_log(系统用户登录记录)】的数据库操作Service实现
* @createDate 2025-02-11 17:32:51
*/
@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog>
    implements SysLoginLogService{

}




