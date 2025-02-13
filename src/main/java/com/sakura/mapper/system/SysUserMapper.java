package com.sakura.mapper.system;

import com.sakura.model.po.system.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author sakura
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2025-02-11 17:32:51
* @Entity com.sakura.model.pojo.system.SysUser
*/
public interface SysUserMapper extends BaseMapper<SysUser> {

    List<String> getUserRoleCode(@Param("userId") Long userId);

    List<String> getUserComponentCode(@Param("userId") Long userId);

    List<String> getUserPermsCode(@Param("userId") Long userId);

}




