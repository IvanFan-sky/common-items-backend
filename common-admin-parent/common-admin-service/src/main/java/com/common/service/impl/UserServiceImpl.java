package com.common.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.core.dto.*;
import com.common.core.entity.SysUser;
import com.common.core.enums.Gender;
import com.common.core.enums.UserStatus;
import com.common.core.exception.BusinessException;
import com.common.core.mapper.UserConvertMapper;
import com.common.core.vo.PageResult;
import com.common.core.vo.UserDetailVO;
import com.common.core.vo.UserVO;
import com.common.mapper.UserMapper;
import com.common.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @Description 用户服务实现类
 * @Date 2025/1/7 15:40
 * @Author SparkFan
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements UserService {

    private final UserMapper userMapper;
    private final UserConvertMapper convertMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long register(UserCreateDTO createDTO) {
        log.info("用户注册开始，用户名：{}", createDTO.getUsername());
        
        // 参数验证
        validateUserCreate(createDTO, null);
        
        // 检查用户名、邮箱、手机号是否已存在
        checkUserUniqueness(createDTO.getUsername(), createDTO.getEmail(), createDTO.getPhone(), null);
        
        // 创建用户实体
        SysUser user = convertMapper.createDTOToEntity(createDTO);
        
        // 设置默认值
        user.setPassword(BCrypt.hashpw(createDTO.getPassword(), BCrypt.gensalt()));
        user.setStatus(UserStatus.ENABLED.getCode());
        user.setGender(Objects.isNull(createDTO.getGender()) ? Gender.UNKNOWN.getCode() : createDTO.getGender());
        user.setCreateTime(LocalDateTime.now());
        user.setDeleted(0);
        
        // 保存用户
        if (!save(user)) {
            throw new BusinessException("用户注册失败");
        }
        
        log.info("用户注册成功，用户ID：{}，用户名：{}", user.getId(), user.getUsername());
        return user.getId();
    }

    @Override
    public SysUser login(String username, String password, String loginIp) {
        log.info("用户登录验证开始，用户名：{}", username);
        
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            throw new BusinessException("用户名和密码不能为空");
        }
        
        // 支持用户名、邮箱、手机号登录
        SysUser user = getUserByLoginField(username);
        if (Objects.isNull(user)) {
            throw new BusinessException("用户不存在");
        }
        
        // 检查用户状态
        if (!UserStatus.ENABLED.getCode().equals(user.getStatus())) {
            throw new BusinessException("用户已被禁用");
        }
        
        // 验证密码
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new BusinessException("密码错误");
        }
        
        // 更新登录信息
        updateLastLogin(user.getId(), loginIp);
        
        log.info("用户登录成功，用户ID：{}，用户名：{}", user.getId(), user.getUsername());
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUser(UserCreateDTO createDTO, Long createBy) {
        log.info("管理员创建用户开始，用户名：{}，创建人：{}", createDTO.getUsername(), createBy);
        
        // 参数验证
        validateUserCreate(createDTO, createBy);
        
        // 检查用户名、邮箱、手机号是否已存在
        checkUserUniqueness(createDTO.getUsername(), createDTO.getEmail(), createDTO.getPhone(), null);
        
        // 创建用户实体
        SysUser user = convertMapper.createDTOToEntity(createDTO);
        
        // 设置默认值
        user.setPassword(BCrypt.hashpw(createDTO.getPassword(), BCrypt.gensalt()));
        user.setStatus(Objects.isNull(createDTO.getStatus()) ? UserStatus.ENABLED.getCode() : createDTO.getStatus());
        user.setGender(Objects.isNull(createDTO.getGender()) ? Gender.UNKNOWN.getCode() : createDTO.getGender());
        user.setCreateBy(createBy);
        user.setCreateTime(LocalDateTime.now());
        user.setDeleted(0);
        
        // 保存用户
        if (!save(user)) {
            throw new BusinessException("用户创建失败");
        }
        
        log.info("管理员创建用户成功，用户ID：{}，用户名：{}", user.getId(), user.getUsername());
        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUser(Long userId, UserUpdateDTO updateDTO, Long updateBy) {
        log.info("更新用户信息开始，用户ID：{}，更新人：{}", userId, updateBy);
        
        if (Objects.isNull(userId)) {
            throw new BusinessException("用户ID不能为空");
        }
        
        SysUser existUser = getById(userId);
        if (Objects.isNull(existUser) || existUser.getDeleted().equals(1)) {
            throw new BusinessException("用户不存在");
        }
        
        // 检查用户名、邮箱、手机号是否已存在（排除自己）
        if (StrUtil.isNotBlank(updateDTO.getUsername())) {
            checkUserUniqueness(updateDTO.getUsername(), null, null, userId);
        }
        if (StrUtil.isNotBlank(updateDTO.getEmail())) {
            checkUserUniqueness(null, updateDTO.getEmail(), null, userId);
        }
        if (StrUtil.isNotBlank(updateDTO.getPhone())) {
            checkUserUniqueness(null, null, updateDTO.getPhone(), userId);
        }
        
        // 使用MapStruct更新用户信息
        convertMapper.updateEntityFromDTO(updateDTO, existUser);
        existUser.setUpdateBy(updateBy);
        existUser.setUpdateTime(LocalDateTime.now());
        
        boolean success = updateById(existUser);
        log.info("更新用户信息{}，用户ID：{}", success ? "成功" : "失败", userId);
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteUser(Long userId, Long deleteBy) {
        log.info("删除用户开始，用户ID：{}，删除人：{}", userId, deleteBy);
        
        if (Objects.isNull(userId)) {
            throw new BusinessException("用户ID不能为空");
        }
        
        SysUser user = new SysUser();
        user.setId(userId);
        user.setDeleted(1);
        user.setUpdateBy(deleteBy);
        user.setUpdateTime(LocalDateTime.now());
        
        boolean success = updateById(user);
        log.info("删除用户{}，用户ID：{}", success ? "成功" : "失败", userId);
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchDeleteUsers(List<Long> userIds, Long deleteBy) {
        log.info("批量删除用户开始，用户数量：{}，删除人：{}", userIds.size(), deleteBy);
        
        if (Objects.isNull(userIds) || userIds.isEmpty()) {
            throw new BusinessException("用户ID列表不能为空");
        }
        
        for (Long userId : userIds) {
            deleteUser(userId, deleteBy);
        }
        
        log.info("批量删除用户完成，删除数量：{}", userIds.size());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUserStatus(Long userId, Integer status, Long updateBy) {
        log.info("更新用户状态开始，用户ID：{}，状态：{}，更新人：{}", userId, status, updateBy);
        
        if (Objects.isNull(userId) || Objects.isNull(status)) {
            throw new BusinessException("用户ID和状态不能为空");
        }
        
        SysUser user = new SysUser();
        user.setId(userId);
        user.setStatus(status);
        user.setUpdateBy(updateBy);
        user.setUpdateTime(LocalDateTime.now());
        
        boolean success = updateById(user);
        log.info("更新用户状态{}，用户ID：{}", success ? "成功" : "失败", userId);
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchUpdateUserStatus(List<Long> userIds, Integer status, Long updateBy) {
        log.info("批量更新用户状态开始，用户数量：{}，状态：{}，更新人：{}", userIds.size(), status, updateBy);
        
        if (Objects.isNull(userIds) || userIds.isEmpty() || Objects.isNull(status)) {
            throw new BusinessException("参数不能为空");
        }
        
        int updateCount = userMapper.batchUpdateStatus(userIds, status, updateBy);
        boolean success = updateCount > 0;
        
        log.info("批量更新用户状态{}，更新数量：{}", success ? "成功" : "失败", updateCount);
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean resetPassword(Long userId, String newPassword, Long updateBy) {
        log.info("重置用户密码开始，用户ID：{}，更新人：{}", userId, updateBy);
        
        if (Objects.isNull(userId) || StrUtil.isBlank(newPassword)) {
            throw new BusinessException("用户ID和新密码不能为空");
        }
        
        String encryptedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        int updateCount = userMapper.resetPassword(userId, encryptedPassword, updateBy);
        boolean success = updateCount > 0;
        
        log.info("重置用户密码{}，用户ID：{}", success ? "成功" : "失败", userId);
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean changePassword(Long userId, UserPasswordUpdateDTO passwordUpdateDTO) {
        log.info("修改用户密码开始，用户ID：{}", userId);
        
        if (Objects.isNull(userId)) {
            throw new BusinessException("用户ID不能为空");
        }
        
        SysUser user = getById(userId);
        if (Objects.isNull(user) || user.getDeleted().equals(1)) {
            throw new BusinessException("用户不存在");
        }
        
        // 验证旧密码
        if (!BCrypt.checkpw(passwordUpdateDTO.getOldPassword(), user.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        
        // 更新密码
        String encryptedPassword = BCrypt.hashpw(passwordUpdateDTO.getNewPassword(), BCrypt.gensalt());
        int updateCount = userMapper.resetPassword(userId, encryptedPassword, userId);
        boolean success = updateCount > 0;
        
        log.info("修改用户密码{}，用户ID：{}", success ? "成功" : "失败", userId);
        return success;
    }

    @Override
    public UserVO getUserInfo(Long userId) {
        if (Objects.isNull(userId)) {
            throw new BusinessException("用户ID不能为空");
        }
        
        SysUser user = getById(userId);
        if (Objects.isNull(user) || user.getDeleted().equals(1)) {
            return null;
        }
        
        return convertMapper.toUserVO(user);
    }

    @Override
    public UserDetailVO getUserDetail(Long userId) {
        if (Objects.isNull(userId)) {
            throw new BusinessException("用户ID不能为空");
        }
        
        SysUser user = getById(userId);
        if (Objects.isNull(user) || user.getDeleted().equals(1)) {
            return null;
        }
        
        return convertMapper.toUserDetailVO(user);
    }

    @Override
    public PageResult<UserVO> getUserPage(UserQueryDTO queryDTO) {
        Page<SysUser> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        
        IPage<SysUser> userPage = userMapper.selectUserPage(page, queryDTO);
        
        List<UserVO> userVOList = convertMapper.toUserVOList(userPage.getRecords());
        
        return new PageResult<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal(), userVOList);
    }

    @Override
    public Boolean checkUsernameAvailable(String username, Long excludeId) {
        if (StrUtil.isBlank(username)) {
            return false;
        }
        return !userMapper.existsByUsername(username, excludeId);
    }

    @Override
    public Boolean checkEmailAvailable(String email, Long excludeId) {
        if (StrUtil.isBlank(email)) {
            return false;
        }
        return !userMapper.existsByEmail(email, excludeId);
    }

    @Override
    public Boolean checkPhoneAvailable(String phone, Long excludeId) {
        if (StrUtil.isBlank(phone)) {
            return false;
        }
        return !userMapper.existsByPhone(phone, excludeId);
    }

    @Override
    public SysUser getUserByUsername(String username) {
        if (StrUtil.isBlank(username)) {
            return null;
        }
        return userMapper.selectByUsername(username);
    }

    @Override
    public SysUser getUserByEmail(String email) {
        if (StrUtil.isBlank(email)) {
            return null;
        }
        return userMapper.selectByEmail(email);
    }

    @Override
    public SysUser getUserByPhone(String phone) {
        if (StrUtil.isBlank(phone)) {
            return null;
        }
        return userMapper.selectByPhone(phone);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateLastLogin(Long userId, String loginIp) {
        if (Objects.isNull(userId)) {
            return false;
        }
        
        int updateCount = userMapper.updateLastLogin(userId, loginIp);
        return updateCount > 0;
    }

    /**
     * 验证用户创建参数
     */
    private void validateUserCreate(UserCreateDTO createDTO, Long createBy) {
        if (Objects.isNull(createDTO)) {
            throw new BusinessException("用户信息不能为空");
        }
        
        if (StrUtil.isBlank(createDTO.getUsername())) {
            throw new BusinessException("用户名不能为空");
        }
        
        if (StrUtil.isBlank(createDTO.getPassword())) {
            throw new BusinessException("密码不能为空");
        }
        
        // 用户名长度验证
        if (createDTO.getUsername().length() < 3 || createDTO.getUsername().length() > 20) {
            throw new BusinessException("用户名长度必须在3-20个字符之间");
        }
        
        // 密码长度验证
        if (createDTO.getPassword().length() < 6 || createDTO.getPassword().length() > 20) {
            throw new BusinessException("密码长度必须在6-20个字符之间");
        }
    }

    /**
     * 检查用户唯一性
     */
    private void checkUserUniqueness(String username, String email, String phone, Long excludeId) {
        if (StrUtil.isNotBlank(username) && !checkUsernameAvailable(username, excludeId)) {
            throw new BusinessException("用户名已存在");
        }
        
        if (StrUtil.isNotBlank(email) && !checkEmailAvailable(email, excludeId)) {
            throw new BusinessException("邮箱已存在");
        }
        
        if (StrUtil.isNotBlank(phone) && !checkPhoneAvailable(phone, excludeId)) {
            throw new BusinessException("手机号已存在");
        }
    }

    /**
     * 根据登录字段获取用户（支持用户名、邮箱、手机号）
     */
    private SysUser getUserByLoginField(String loginField) {
        // 优先按用户名查询
        SysUser user = getUserByUsername(loginField);
        if (Objects.nonNull(user)) {
            return user;
        }
        
        // 按邮箱查询
        if (loginField.contains("@")) {
            user = getUserByEmail(loginField);
            if (Objects.nonNull(user)) {
                return user;
            }
        }
        
        // 按手机号查询
        if (loginField.matches("^1[3-9]\\d{9}$")) {
            user = getUserByPhone(loginField);
        }
        
        return user;
    }
} 