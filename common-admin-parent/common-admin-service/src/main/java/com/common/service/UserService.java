package com.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.core.entity.SysUser;
import com.common.core.dto.user.UserCreateDTO;
import com.common.core.dto.user.UserUpdateDTO;
import com.common.core.dto.user.UserQueryDTO;
import com.common.core.dto.user.UserPasswordUpdateDTO;
import com.common.core.dto.user.UserProfileUpdateDTO;
import com.common.core.dto.user.UserAvatarUpdateDTO;
import com.common.core.dto.user.UserSecuritySettingsDTO;
import com.common.core.vo.user.UserVO;
import com.common.core.vo.user.UserDetailVO;
import com.common.core.vo.user.UserProfileVO;
import com.common.core.vo.PageResult;

import java.util.List;
import java.util.Map;

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

    // ==================== 导入导出功能 ====================

    /**
     * 导出用户数据到Excel
     * 
     * @param queryDTO 查询条件
     * @return 导出数据列表
     */
    List<com.common.core.vo.user.UserExportVO> exportUsers(com.common.core.dto.user.UserQueryDTO queryDTO);

    /**
     * 导入用户数据
     * 
     * @param importDataList 导入数据列表
     * @param importBy 导入人ID
     * @return 导入结果
     */
    com.common.core.vo.ImportResultVO importUsers(List<com.common.core.dto.user.UserImportDTO> importDataList, Long importBy);

    /**
     * 下载用户导入模板
     * 
     * @return 模板数据
     */
    List<com.common.core.dto.user.UserImportDTO> getUserImportTemplate();

    // ==================== 高级查询功能 ====================

    /**
     * 高级分页查询用户（支持多条件筛选和排序）
     * 
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageResult<UserVO> getUserPageAdvanced(com.common.core.dto.user.UserQueryDTO queryDTO);

    /**
     * 根据角色查询用户
     * 
     * @param roleId 角色ID
     * @return 用户列表
     */
    List<UserVO> getUsersByRole(Long roleId);

    /**
     * 查询最近登录的用户
     * 
     * @param limit 限制数量
     * @return 用户列表
     */
    List<UserVO> getRecentLoginUsers(Integer limit);

    /**
     * 查询活跃用户（最近30天有登录）
     * 
     * @return 用户列表
     */
    List<UserVO> getActiveUsers();

    /**
     * 获取用户统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getUserStatistics();

    /**
     * 批量查询用户信息
     * 
     * @param userIds 用户ID列表
     * @return 用户列表
     */
    List<UserVO> getBatchUsers(List<Long> userIds);

    /**
     * 搜索用户（关键词模糊匹配）
     * 
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 用户列表
     */
    List<UserVO> searchUsers(String keyword, Integer limit);

    // ==================== 个人信息管理功能 ====================

    /**
     * 获取用户个人资料
     * 
     * @param userId 用户ID
     * @return 个人资料信息
     */
    UserProfileVO getUserProfile(Long userId);

    /**
     * 更新用户个人资料
     * 
     * @param userId 用户ID
     * @param profileUpdateDTO 个人资料更新信息
     * @return 是否成功
     */
    Boolean updateUserProfile(Long userId, UserProfileUpdateDTO profileUpdateDTO);

    /**
     * 更新用户头像
     * 
     * @param userId 用户ID
     * @param avatarUpdateDTO 头像更新信息
     * @return 是否成功
     */
    Boolean updateUserAvatar(Long userId, UserAvatarUpdateDTO avatarUpdateDTO);

    /**
     * 更新用户安全设置
     * 
     * @param userId 用户ID
     * @param securitySettingsDTO 安全设置信息
     * @return 是否成功
     */
    Boolean updateUserSecuritySettings(Long userId, UserSecuritySettingsDTO securitySettingsDTO);

    /**
     * 验证用户当前密码
     * 
     * @param userId 用户ID
     * @param currentPassword 当前密码
     * @return 是否正确
     */
    Boolean verifyCurrentPassword(Long userId, String currentPassword);

    /**
     * 生成双因子认证密钥
     * 
     * @param userId 用户ID
     * @return 密钥和二维码
     */
    Map<String, String> generateTwoFactorSecret(Long userId);

    /**
     * 验证双因子认证码
     * 
     * @param userId 用户ID
     * @param code 验证码
     * @return 是否正确
     */
    Boolean verifyTwoFactorCode(Long userId, String code);

    /**
     * 获取用户登录历史
     * 
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 登录历史列表
     */
    List<Map<String, Object>> getUserLoginHistory(Long userId, Integer limit);

    /**
     * 获取用户设备信息
     * 
     * @param userId 用户ID
     * @return 设备信息列表
     */
    List<Map<String, Object>> getUserDevices(Long userId);

    /**
     * 移除用户设备
     * 
     * @param userId 用户ID
     * @param deviceId 设备ID
     * @return 是否成功
     */
    Boolean removeUserDevice(Long userId, String deviceId);
} 