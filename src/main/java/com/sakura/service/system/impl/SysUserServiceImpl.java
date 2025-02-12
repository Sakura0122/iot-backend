package com.sakura.service.system.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sakura.common.PageVo;
import com.sakura.common.ResultCodeEnum;
import com.sakura.exception.SakuraException;
import com.sakura.model.dto.system.user.SysUserAddDto;
import com.sakura.model.dto.system.user.SysUserListDto;
import com.sakura.model.dto.system.user.SysUserUpdateDto;
import com.sakura.model.po.system.SysUser;
import com.sakura.model.vo.system.SysUserVo;
import com.sakura.service.system.SysUserService;
import com.sakura.mapper.system.SysUserMapper;
import org.springframework.stereotype.Service;

/**
 * @author sakura
 * @description 针对表【sys_user(用户表)】的数据库操作Service实现
 * @createDate 2025-02-11 17:32:51
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public PageVo<SysUserVo> getUserList(SysUserListDto userDto) {
        // 1. 获取分页查询条件
        Page<SysUser> p = userDto.toMpPageDefaultSortByUpdateTimeDesc();

        // 2.查询
        Page<SysUser> page = lambdaQuery()
                .between(StrUtil.isNotBlank(userDto.getBeginTime()) && StrUtil.isNotBlank(userDto.getEndTime()),
                        SysUser::getCreateTime, userDto.getBeginTime(), userDto.getEndTime())
                .and(StrUtil.isNotBlank(userDto.getKeyword()), wrapper -> wrapper
                        .like(SysUser::getUsername, userDto.getKeyword())
                        .or()
                        .like(SysUser::getName, userDto.getKeyword())
                        .or()
                        .like(SysUser::getPhone, userDto.getKeyword())
                )
                .page(p);

        // 3.返回
        return PageVo.of(page, SysUserVo.class);
    }

    @Override
    public void addUser(SysUserAddDto userDto) {
        // 1.密码加密
        String hashpw = BCrypt.hashpw(userDto.getPassword());
        userDto.setPassword(hashpw);

        // 2.保存
        SysUser sysUser = BeanUtil.copyProperties(userDto, SysUser.class);
        boolean result = this.save(sysUser);

        // 3.判断是否成功
        if (!result) {
            throw new SakuraException(ResultCodeEnum.FAIL);
        }
    }

    @Override
    public void updateUser(SysUserUpdateDto userDto) {
        // 1.拷贝
        SysUser sysUser = BeanUtil.copyProperties(userDto, SysUser.class);

        // 2.更新
        boolean result = this.updateById(sysUser);

        // 3.判断是否成功
        if (!result) {
            throw new SakuraException(ResultCodeEnum.FAIL);
        }
    }
}
