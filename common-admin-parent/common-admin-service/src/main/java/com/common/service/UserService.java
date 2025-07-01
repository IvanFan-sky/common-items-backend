package com.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.core.entity.SysUser;
import com.common.core.dto.user.UserCreateDTO;
import com.common.core.dto.user.UserUpdateDTO;
import com.common.core.dto.user.UserQueryDTO;
import com.common.core.dto.user.UserPasswordUpdateDTO;
import com.common.core.vo.user.UserVO;
import com.common.core.vo.user.UserDetailVO;
import com.common.core.vo.PageResult;

import java.util.List;

/**
 * @Description 用户服务接口
 * @Date 2025/1/7 15:35
 * @Author SparkFan
 */
public interface UserService extends IService<SysUser> {

    /**
     * 用户注册
     * 
     * @param createDTO 注册信息
     * @return 用户ID
     */
    Long register(UserCreateDTO createDTO);

    /**
     * 用户登录验证
     * 
     * @param username 用户名（支持用户名/邮箱/手机号）
     * @param password 密码
     * @param loginIp 登录IP
     * @return 用户信息
     */
    SysUser login(String username, String password, String loginIp);

    /**
     * 创建用户（管理员功能）
     * 
     * @param createDTO 用户信息
     * @param createBy 创建人ID
     * @return 用户ID
     */
    Long createUser(UserCreateDTO createDTO, Long createBy);

    /**
     * 更新用户信息
     * 
     * @param userId 用户ID
     * @param updateDTO 更新信息
     * @param updateBy 更新人ID
     * @return 是否成功
     */
    Boolean updateUser(Long userId, UserUpdateDTO updateDTO, Long updateBy);

    /**
     * 删除用户（逻辑删除）
     * 
     * @param userId 用户ID
     * @param deleteBy 删除人ID
     * @return 是否成功
     */
    Boolean deleteUser(Long userId, Long deleteBy);

    /**
     * 批量删除用户
     * 
     * @param userIds 用户ID列表
     * @param deleteBy 删除人ID
     * @return 是否成功
     */
    Boolean batchDeleteUsers(List<Long> userIds, Long deleteBy);

    /**
     * 启用/禁用用户
     * 
     * @param userId 用户ID
     * @param status 状态（1-启用，0-禁用）
     * @param updateBy 更新人ID
     * @return 是否成功
     */
    Boolean updateUserStatus(Long userId, Integer status, Long updateBy);

    /**
     * 批量更新用户状态
     * 
     * @param userIds 用户ID列表
     * @param status 状态
     * @param updateBy 更新人ID
     * @return 是否成功
     */
    Boolean batchUpdateUserStatus(List<Long> userIds, Integer status, Long updateBy);

    /**
     * 重置用户密码
     * 
     * @param userId 用户ID
     * @param newPassword 新密码
     * @param updateBy 更新人ID
     * @return 是否成功
     */
    Boolean resetPassword(Long userId, String newPassword, Long updateBy);

    /**
     * 修改密码
     * 
     * @param userId 用户ID
     * @param passwordUpdateDTO 密码修改信息
     * @return 是否成功
     */
    Boolean changePassword(Long userId, UserPasswordUpdateDTO passwordUpdateDTO);

    /**
     * 获取用户信息
     * 
     * @param userId 用户ID
     * @return 用户信息
     */
    UserVO getUserInfo(Long userId);

    /**
     * 获取用户详细信息
     * 
     * @param userId 用户ID
     * @return 用户详细信息
     */
    UserDetailVO getUserDetail(Long userId);

    /**
     * 分页查询用户列表
     * 
     * @param queryDTO 查询条件
     * @return 用户列表
     */
    PageResult<UserVO> getUserPage(UserQueryDTO queryDTO);

    /**
     * 检查用户名是否可用
     * 
     * @param username 用户名
     * @param excludeId 排除的用户ID
     * @return 是否可用
     */
    Boolean checkUsernameAvailable(String username, Long excludeId);

    /**
     * 检查邮箱是否可用
     * 
     * @param email 邮箱
     * @param excludeId 排除的用户ID
     * @return 是否可用
     */
    Boolean checkEmailAvailable(String email, Long excludeId);

    /**
     * 检查手机号是否可用
     * 
     * @param phone 手机号
     * @param excludeId 排除的用户ID
     * @return 是否可用
     */
    Boolean checkPhoneAvailable(String phone, Long excludeId);

    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    SysUser getUserByUsername(String username);

    /**
     * 根据邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户信息
     */
    SysUser getUserByEmail(String email);

    /**
     * 根据手机号查询用户
     * 
     * @param phone 手机号
     * @return 用户信息
     */
    SysUser getUserByPhone(String phone);

    /**
     * 更新用户最后登录信息
     * 
     * @param userId 用户ID
     * @param loginIp 登录IP
     * @return 是否成功
     */
    Boolean updateLastLogin(Long userId, String loginIp);
} 