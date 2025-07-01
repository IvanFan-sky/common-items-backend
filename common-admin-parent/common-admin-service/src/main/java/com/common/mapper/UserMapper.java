package com.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.core.entity.SysUser;
import com.common.core.dto.UserQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 用户数据访问层接口
 * @Date 2025/1/7 15:30
 * @Author SparkFan
 */
@Mapper
public interface UserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    SysUser selectByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户信息
     */
    SysUser selectByEmail(@Param("email") String email);

    /**
     * 根据手机号查询用户
     * 
     * @param phone 手机号
     * @return 用户信息
     */
    SysUser selectByPhone(@Param("phone") String phone);

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @param excludeId 排除的用户ID（用于更新时排除自己）
     * @return 是否存在
     */
    boolean existsByUsername(@Param("username") String username, @Param("excludeId") Long excludeId);

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @param excludeId 排除的用户ID（用于更新时排除自己）
     * @return 是否存在
     */
    boolean existsByEmail(@Param("email") String email, @Param("excludeId") Long excludeId);

    /**
     * 检查手机号是否存在
     * 
     * @param phone 手机号
     * @param excludeId 排除的用户ID（用于更新时排除自己）
     * @return 是否存在
     */
    boolean existsByPhone(@Param("phone") String phone, @Param("excludeId") Long excludeId);

    /**
     * 分页查询用户列表
     * 
     * @param page 分页参数
     * @param query 查询条件
     * @return 用户列表
     */
    IPage<SysUser> selectUserPage(Page<SysUser> page, @Param("query") UserQueryDTO query);

    /**
     * 查询用户详细信息（包含角色信息）
     * 
     * @param userId 用户ID
     * @return 用户详细信息
     */
    SysUser selectUserDetailById(@Param("userId") Long userId);

    /**
     * 更新用户最后登录时间和IP
     * 
     * @param userId 用户ID
     * @param loginIp 登录IP
     * @return 更新行数
     */
    int updateLastLogin(@Param("userId") Long userId, @Param("loginIp") String loginIp);

    /**
     * 批量更新用户状态
     * 
     * @param userIds 用户ID列表
     * @param status 状态
     * @param updateBy 更新人
     * @return 更新行数
     */
    int batchUpdateStatus(@Param("userIds") List<Long> userIds, 
                          @Param("status") Integer status, 
                          @Param("updateBy") Long updateBy);

    /**
     * 重置密码
     * 
     * @param userId 用户ID
     * @param password 新密码（已加密）
     * @param updateBy 更新人
     * @return 更新行数
     */
    int resetPassword(@Param("userId") Long userId, 
                      @Param("password") String password, 
                      @Param("updateBy") Long updateBy);
} 