package com.common.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.core.dto.user.UserCreateDTO;
import com.common.core.dto.user.UserImportDTO;
import com.common.core.dto.user.UserPasswordUpdateDTO;
import com.common.core.dto.user.UserQueryDTO;
import com.common.core.dto.user.UserUpdateDTO;
import com.common.core.dto.user.UserProfileUpdateDTO;
import com.common.core.dto.user.UserAvatarUpdateDTO;
import com.common.core.dto.user.UserSecuritySettingsDTO;
import com.common.core.entity.SysUser;
import com.common.core.enums.Gender;
import com.common.core.enums.UserStatus;
import com.common.core.exception.BusinessException;
import com.common.core.mapper.UserConvertMapper;
import com.common.core.vo.ImportResultVO;
import com.common.core.vo.PageResult;
import com.common.core.vo.user.UserDetailVO;
import com.common.core.vo.user.UserExportVO;
import com.common.core.vo.user.UserVO;
import com.common.core.vo.user.UserProfileVO;
import com.common.mapper.UserMapper;
import com.common.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    // ==================== 导入导出功能 ====================

    @Override
    public List<com.common.core.vo.user.UserExportVO> exportUsers(com.common.core.dto.user.UserQueryDTO queryDTO) {
        log.info("开始导出用户数据，查询条件：{}", queryDTO);
        
        // 查询用户列表
        QueryWrapper<SysUser> queryWrapper = buildUserQueryWrapper(queryDTO);
        List<SysUser> userList = userMapper.selectList(queryWrapper);
        
        // 转换为导出VO
        List<com.common.core.vo.user.UserExportVO> exportList = new ArrayList<>();
        for (SysUser user : userList) {
            com.common.core.vo.user.UserExportVO exportVO = new com.common.core.vo.user.UserExportVO();
            exportVO.setId(user.getId());
            exportVO.setUsername(user.getUsername());
            exportVO.setNickname(user.getNickname());
            exportVO.setEmail(user.getEmail());
            exportVO.setPhone(user.getPhone());
            exportVO.setGenderText(getGenderText(user.getGender()));
            exportVO.setBirthday(user.getBirthday());
            exportVO.setStatusText(getStatusText(user.getStatus()));
            exportVO.setLastLoginIp(user.getLastLoginIp());
            exportVO.setLastLoginTime(user.getLastLoginTime());
            exportVO.setLoginCount(user.getLoginCount());
            exportVO.setRoleNames(getUserRoleNames(user.getId()));
            exportVO.setRemark(user.getRemark());
            exportVO.setCreateTime(user.getCreateTime());
            exportVO.setCreateByName(getUsernameById(user.getCreateBy()));
            
            exportList.add(exportVO);
        }
        
        log.info("用户数据导出完成，导出数量：{}", exportList.size());
        return exportList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public com.common.core.vo.ImportResultVO importUsers(List<com.common.core.dto.user.UserImportDTO> importDataList, Long importBy) {
        log.info("开始导入用户数据，数据量：{}，导入人ID：{}", importDataList.size(), importBy);
        
        long startTime = System.currentTimeMillis();
        com.common.core.vo.ImportResultVO result = new com.common.core.vo.ImportResultVO();
        result.setTotalCount(importDataList.size());
        result.setErrorMessages(new ArrayList<>());
        result.setFailureDetails(new ArrayList<>());
        
        int successCount = 0;
        int failureCount = 0;
        
        for (int i = 0; i < importDataList.size(); i++) {
            com.common.core.dto.user.UserImportDTO importDTO = importDataList.get(i);
            importDTO.setRowIndex(i + 2); // Excel行号从2开始（第1行是标题）
            
            try {
                // 数据验证和转换
                if (!validateAndConvertImportData(importDTO)) {
                    failureCount++;
                    addFailureDetail(result, importDTO, importDTO.getErrorMessage());
                    continue;
                }
                
                // 检查用户名是否已存在
                if (!checkUsernameAvailable(importDTO.getUsername(), null)) {
                    failureCount++;
                    addFailureDetail(result, importDTO, "用户名已存在");
                    continue;
                }
                
                // 检查邮箱是否已存在
                if (StrUtil.isNotBlank(importDTO.getEmail()) && !checkEmailAvailable(importDTO.getEmail(), null)) {
                    failureCount++;
                    addFailureDetail(result, importDTO, "邮箱已存在");
                    continue;
                }
                
                // 检查手机号是否已存在
                if (StrUtil.isNotBlank(importDTO.getPhone()) && !checkPhoneAvailable(importDTO.getPhone(), null)) {
                    failureCount++;
                    addFailureDetail(result, importDTO, "手机号已存在");
                    continue;
                }
                
                // 创建用户
                SysUser user = convertImportDTOToEntity(importDTO, importBy);
                int rows = userMapper.insert(user);
                
                if (rows > 0) {
                    successCount++;
                } else {
                    failureCount++;
                    addFailureDetail(result, importDTO, "数据库保存失败");
                }
                
            } catch (Exception e) {
                log.error("导入用户数据异常，行号：{}，数据：{}", importDTO.getRowIndex(), importDTO, e);
                failureCount++;
                addFailureDetail(result, importDTO, "系统异常：" + e.getMessage());
            }
        }
        
        result.setSuccessCount(successCount);
        result.setFailureCount(failureCount);
        result.setAllSuccess(failureCount == 0);
        result.setDuration(System.currentTimeMillis() - startTime);
        
        log.info("用户数据导入完成，总数：{}，成功：{}，失败：{}，耗时：{}ms", 
                result.getTotalCount(), result.getSuccessCount(), result.getFailureCount(), result.getDuration());
        
        return result;
    }

    @Override
    public List<com.common.core.dto.user.UserImportDTO> getUserImportTemplate() {
        log.info("生成用户导入模板");
        
        List<com.common.core.dto.user.UserImportDTO> templateList = new ArrayList<>();
        
        // 添加示例数据
        com.common.core.dto.user.UserImportDTO example = new com.common.core.dto.user.UserImportDTO();
        example.setUsername("test001");
        example.setNickname("测试用户");
        example.setEmail("test@example.com");
        example.setPhone("13800138000");
        example.setGenderText("男");
        example.setBirthday(LocalDate.of(1990, 1, 1));
        example.setStatusText("启用");
        example.setRemark("这是一个示例用户");
        
        templateList.add(example);
        
        log.info("用户导入模板生成完成");
        return templateList;
    }

    // ==================== 高级查询功能实现 ====================

    @Override
    public PageResult<UserVO> getUserPageAdvanced(com.common.core.dto.user.UserQueryDTO queryDTO) {
        log.info("高级分页查询用户开始");
        
        // 校验分页参数
        queryDTO.validatePage();
        
        // 构建查询条件
        QueryWrapper<SysUser> queryWrapper = buildAdvancedQueryWrapper(queryDTO);
        
        // 分页查询
        Page<SysUser> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        IPage<SysUser> userPage = page(page, queryWrapper);
        
        // 转换结果
        List<UserVO> userVOList = convertMapper.toUserVOList(userPage.getRecords());
        
        PageResult<UserVO> result = new PageResult<>();
        result.setRecords(userVOList);
        result.setTotal(userPage.getTotal());
        result.setCurrent(queryDTO.getCurrent());
        result.setSize(queryDTO.getSize());
        
        log.info("高级分页查询用户完成，总数：{}", result.getTotal());
        return result;
    }

    @Override
    public List<UserVO> getUsersByRole(Long roleId) {
        log.info("根据角色查询用户，角色ID：{}", roleId);
        
        if (Objects.isNull(roleId)) {
            throw new BusinessException("角色ID不能为空");
        }
        
        // TODO: 实现角色用户查询
        // 这里需要查询用户角色关联表
        List<SysUser> users = userMapper.selectByRoleId(roleId);
        List<UserVO> userVOList = convertMapper.toUserVOList(users);
        
        log.info("根据角色查询用户完成，角色ID：{}，用户数：{}", roleId, userVOList.size());
        return userVOList;
    }

    @Override
    public List<UserVO> getRecentLoginUsers(Integer limit) {
        log.info("查询最近登录用户，限制数量：{}", limit);
        
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        
        List<SysUser> users = userMapper.selectRecentLoginUsers(limit);
        List<UserVO> userVOList = convertMapper.toUserVOList(users);
        
        log.info("查询最近登录用户完成，数量：{}", userVOList.size());
        return userVOList;
    }

    @Override
    public List<UserVO> getActiveUsers() {
        log.info("查询活跃用户");
        
        List<SysUser> users = userMapper.selectActiveUsers();
        List<UserVO> userVOList = convertMapper.toUserVOList(users);
        
        log.info("查询活跃用户完成，数量：{}", userVOList.size());
        return userVOList;
    }

    @Override
    public Map<String, Object> getUserStatistics() {
        log.info("获取用户统计信息");
        
        Map<String, Object> statistics = userMapper.selectUserStatistics();
        
        log.info("获取用户统计信息完成");
        return statistics;
    }

    @Override
    public List<UserVO> getBatchUsers(List<Long> userIds) {
        log.info("批量查询用户信息，用户数量：{}", userIds.size());
        
        if (userIds == null || userIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<SysUser> users = userMapper.selectBatchByIds(userIds);
        List<UserVO> userVOList = convertMapper.toUserVOList(users);
        
        log.info("批量查询用户信息完成，查询数量：{}，返回数量：{}", userIds.size(), userVOList.size());
        return userVOList;
    }

    @Override
    public List<UserVO> searchUsers(String keyword, Integer limit) {
        log.info("搜索用户，关键词：{}，限制数量：{}", keyword, limit);
        
        if (StrUtil.isBlank(keyword)) {
            return new ArrayList<>();
        }
        
        if (limit == null || limit <= 0) {
            limit = 20;
        }
        
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0);
        queryWrapper.and(wrapper -> wrapper
                .like("username", keyword)
                .or()
                .like("nickname", keyword)
                .or()
                .like("email", keyword)
                .or()
                .like("phone", keyword)
        );
        queryWrapper.orderByDesc("create_time");
        queryWrapper.last("LIMIT " + limit);
        
        List<SysUser> users = list(queryWrapper);
        List<UserVO> userVOList = convertMapper.toUserVOList(users);
        
        log.info("搜索用户完成，关键词：{}，结果数量：{}", keyword, userVOList.size());
        return userVOList;
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 构建用户查询条件
     */
    private QueryWrapper<SysUser> buildUserQueryWrapper(com.common.core.dto.user.UserQueryDTO queryDTO) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0);
        
        if (queryDTO != null) {
            if (StrUtil.isNotBlank(queryDTO.getUsername())) {
                queryWrapper.like("username", queryDTO.getUsername());
            }
            if (StrUtil.isNotBlank(queryDTO.getNickname())) {
                queryWrapper.like("nickname", queryDTO.getNickname());
            }
            if (StrUtil.isNotBlank(queryDTO.getEmail())) {
                queryWrapper.like("email", queryDTO.getEmail());
            }
            if (StrUtil.isNotBlank(queryDTO.getPhone())) {
                queryWrapper.like("phone", queryDTO.getPhone());
            }
            if (queryDTO.getStatus() != null) {
                queryWrapper.eq("status", queryDTO.getStatus());
            }
            if (queryDTO.getGender() != null) {
                queryWrapper.eq("gender", queryDTO.getGender());
            }
        }
        
        queryWrapper.orderByDesc("create_time");
        return queryWrapper;
    }

    /**
     * 构建高级查询条件
     */
    private QueryWrapper<SysUser> buildAdvancedQueryWrapper(com.common.core.dto.user.UserQueryDTO queryDTO) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0);
        
        if (queryDTO != null) {
            // 基础条件
            if (StrUtil.isNotBlank(queryDTO.getUsername())) {
                queryWrapper.like("username", queryDTO.getUsername());
            }
            if (StrUtil.isNotBlank(queryDTO.getNickname())) {
                queryWrapper.like("nickname", queryDTO.getNickname());
            }
            if (StrUtil.isNotBlank(queryDTO.getEmail())) {
                queryWrapper.like("email", queryDTO.getEmail());
            }
            if (StrUtil.isNotBlank(queryDTO.getPhone())) {
                queryWrapper.like("phone", queryDTO.getPhone());
            }
            if (queryDTO.getStatus() != null) {
                queryWrapper.eq("status", queryDTO.getStatus());
            }
            if (queryDTO.getGender() != null) {
                queryWrapper.eq("gender", queryDTO.getGender());
            }
            
            // 关键词搜索（支持用户名、昵称、邮箱、手机号模糊匹配）
            if (StrUtil.isNotBlank(queryDTO.getKeyword())) {
                queryWrapper.and(wrapper -> wrapper
                        .like("username", queryDTO.getKeyword())
                        .or()
                        .like("nickname", queryDTO.getKeyword())
                        .or()
                        .like("email", queryDTO.getKeyword())
                        .or()
                        .like("phone", queryDTO.getKeyword())
                );
            }
            
            // 动态排序
            if (StrUtil.isNotBlank(queryDTO.getSortField())) {
                if ("desc".equalsIgnoreCase(queryDTO.getSortOrder())) {
                    queryWrapper.orderByDesc(queryDTO.getSortField());
                } else {
                    queryWrapper.orderByAsc(queryDTO.getSortField());
                }
            } else {
                queryWrapper.orderByDesc("create_time");
            }
        } else {
            queryWrapper.orderByDesc("create_time");
        }
        
        return queryWrapper;
    }

    /**
     * 验证和转换导入数据
     */
    private boolean validateAndConvertImportData(com.common.core.dto.user.UserImportDTO importDTO) {
        StringBuilder errorMsg = new StringBuilder();
        
        // 验证必填字段
        if (StrUtil.isBlank(importDTO.getUsername())) {
            errorMsg.append("用户名不能为空；");
        }
        
        // 转换性别
        if (StrUtil.isNotBlank(importDTO.getGenderText())) {
            switch (importDTO.getGenderText().trim()) {
                case "男":
                    importDTO.setGender(1);
                    break;
                case "女":
                    importDTO.setGender(2);
                    break;
                case "未知":
                default:
                    importDTO.setGender(0);
                    break;
            }
        } else {
            importDTO.setGender(0);
        }
        
        // 转换状态
        if (StrUtil.isNotBlank(importDTO.getStatusText())) {
            switch (importDTO.getStatusText().trim()) {
                case "启用":
                    importDTO.setStatus(1);
                    break;
                case "禁用":
                    importDTO.setStatus(0);
                    break;
                default:
                    importDTO.setStatus(1);
                    break;
            }
        } else {
            importDTO.setStatus(1);
        }
        
        if (errorMsg.length() > 0) {
            importDTO.setErrorMessage(errorMsg.toString());
            return false;
        }
        
        return true;
    }

    /**
     * 转换导入DTO为实体
     */
    private SysUser convertImportDTOToEntity(com.common.core.dto.user.UserImportDTO importDTO, Long importBy) {
        SysUser user = new SysUser();
        user.setUsername(importDTO.getUsername());
        user.setNickname(StrUtil.isNotBlank(importDTO.getNickname()) ? importDTO.getNickname() : importDTO.getUsername());
        user.setEmail(importDTO.getEmail());
        user.setPhone(importDTO.getPhone());
        user.setGender(importDTO.getGender());
        user.setBirthday(importDTO.getBirthday());
        user.setStatus(importDTO.getStatus());
        user.setRemark(importDTO.getRemark());
        
        // 设置默认密码
        user.setPassword(BCrypt.hashpw(importDTO.getPassword(), BCrypt.gensalt()));
        user.setPasswordUpdateTime(LocalDateTime.now());
        
        // 设置创建信息
        user.setCreateBy(importBy);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateBy(importBy);
        user.setUpdateTime(LocalDateTime.now());
        user.setDeleted(0);
        
        return user;
    }

    /**
     * 添加失败详情
     */
    private void addFailureDetail(com.common.core.vo.ImportResultVO result, com.common.core.dto.user.UserImportDTO importDTO, String reason) {
        com.common.core.vo.ImportResultVO.FailureDetail detail = new com.common.core.vo.ImportResultVO.FailureDetail();
        detail.setRowIndex(importDTO.getRowIndex());
        detail.setReason(reason);
        detail.setOriginalData(importDTO);
        
        result.getFailureDetails().add(detail);
        result.getErrorMessages().add(String.format("第%d行：%s", importDTO.getRowIndex(), reason));
    }

    /**
     * 获取性别文本
     */
    private String getGenderText(Integer gender) {
        if (gender == null) return "未知";
        switch (gender) {
            case 1: return "男";
            case 2: return "女";
            default: return "未知";
        }
    }

    /**
     * 获取状态文本
     */
    private String getStatusText(Integer status) {
        if (status == null) return "禁用";
        return status == 1 ? "启用" : "禁用";
    }

    /**
     * 获取用户角色名称
     */
    private String getUserRoleNames(Long userId) {
        // 这里需要查询用户角色关联表，暂时返回空字符串
        // TODO: 实现角色查询逻辑
        return "";
    }

    /**
     * 根据用户ID获取用户名
     */
    private String getUsernameById(Long userId) {
        if (userId == null) return "";
        SysUser user = userMapper.selectById(userId);
        return user != null ? user.getUsername() : "";
    }

    // ==================== 个人信息管理功能实现 ====================

    @Override
    public UserProfileVO getUserProfile(Long userId) {
        log.info("获取用户个人资料，用户ID：{}", userId);
        
        if (Objects.isNull(userId)) {
            throw new BusinessException("用户ID不能为空");
        }
        
        SysUser user = getById(userId);
        if (Objects.isNull(user) || user.getDeleted().equals(1)) {
            throw new BusinessException("用户不存在");
        }
        
        UserProfileVO profileVO = convertMapper.entityToProfileVO(user);
        
        // 计算年龄
        if (user.getBirthday() != null) {
            int age = LocalDate.now().getYear() - user.getBirthday().getYear();
            profileVO.setAge(age);
        }
        
        // 设置性别文本
        profileVO.setGenderText(getGenderText(user.getGender()));
        
        // 解析标签（假设存储为JSON字符串）
        if (StrUtil.isNotBlank(user.getRemark()) && user.getRemark().startsWith("[")) {
            try {
                // 这里应该使用JSON解析，暂时简化处理
                profileVO.setTags(List.of(user.getRemark().replace("[", "").replace("]", "").replace("\"", "").split(",")));
            } catch (Exception e) {
                profileVO.setTags(new ArrayList<>());
            }
        } else {
            profileVO.setTags(new ArrayList<>());
        }
        
        // TODO: 获取用户角色和权限
        profileVO.setRoles(new ArrayList<>());
        profileVO.setPermissions(new ArrayList<>());
        
        log.info("获取用户个人资料成功，用户ID：{}", userId);
        return profileVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUserProfile(Long userId, UserProfileUpdateDTO profileUpdateDTO) {
        log.info("更新用户个人资料开始，用户ID：{}", userId);
        
        if (Objects.isNull(userId)) {
            throw new BusinessException("用户ID不能为空");
        }
        
        SysUser existUser = getById(userId);
        if (Objects.isNull(existUser) || existUser.getDeleted().equals(1)) {
            throw new BusinessException("用户不存在");
        }
        
        // 检查邮箱和手机号是否已被其他用户使用
        if (StrUtil.isNotBlank(profileUpdateDTO.getEmail())) {
            checkUserUniqueness(null, profileUpdateDTO.getEmail(), null, userId);
        }
        if (StrUtil.isNotBlank(profileUpdateDTO.getPhone())) {
            checkUserUniqueness(null, null, profileUpdateDTO.getPhone(), userId);
        }
        
        // 更新用户信息
        if (StrUtil.isNotBlank(profileUpdateDTO.getNickname())) {
            existUser.setNickname(profileUpdateDTO.getNickname());
        }
        if (StrUtil.isNotBlank(profileUpdateDTO.getEmail())) {
            existUser.setEmail(profileUpdateDTO.getEmail());
        }
        if (StrUtil.isNotBlank(profileUpdateDTO.getPhone())) {
            existUser.setPhone(profileUpdateDTO.getPhone());
        }
        if (StrUtil.isNotBlank(profileUpdateDTO.getAvatar())) {
            existUser.setAvatar(profileUpdateDTO.getAvatar());
        }
        if (profileUpdateDTO.getGender() != null) {
            existUser.setGender(profileUpdateDTO.getGender());
        }
        if (profileUpdateDTO.getBirthday() != null) {
            existUser.setBirthday(profileUpdateDTO.getBirthday());
        }
        if (StrUtil.isNotBlank(profileUpdateDTO.getRemark())) {
            existUser.setRemark(profileUpdateDTO.getRemark());
        }
        
        existUser.setUpdateTime(LocalDateTime.now());
        
        boolean success = updateById(existUser);
        log.info("更新用户个人资料{}，用户ID：{}", success ? "成功" : "失败", userId);
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUserAvatar(Long userId, UserAvatarUpdateDTO avatarUpdateDTO) {
        log.info("更新用户头像开始，用户ID：{}", userId);
        
        if (Objects.isNull(userId)) {
            throw new BusinessException("用户ID不能为空");
        }
        
        SysUser existUser = getById(userId);
        if (Objects.isNull(existUser) || existUser.getDeleted().equals(1)) {
            throw new BusinessException("用户不存在");
        }
        
        existUser.setAvatar(avatarUpdateDTO.getAvatar());
        existUser.setUpdateTime(LocalDateTime.now());
        
        boolean success = updateById(existUser);
        log.info("更新用户头像{}，用户ID：{}", success ? "成功" : "失败", userId);
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUserSecuritySettings(Long userId, UserSecuritySettingsDTO securitySettingsDTO) {
        log.info("更新用户安全设置开始，用户ID：{}", userId);
        
        if (Objects.isNull(userId)) {
            throw new BusinessException("用户ID不能为空");
        }
        
        SysUser existUser = getById(userId);
        if (Objects.isNull(existUser) || existUser.getDeleted().equals(1)) {
            throw new BusinessException("用户不存在");
        }
        
        // TODO: 实现安全设置更新逻辑
        // 这里需要根据具体的安全设置字段进行更新
        // 例如：双因子认证、IP白名单等
        
        existUser.setUpdateTime(LocalDateTime.now());
        
        boolean success = updateById(existUser);
        log.info("更新用户安全设置{}，用户ID：{}", success ? "成功" : "失败", userId);
        return success;
    }

    @Override
    public Boolean verifyCurrentPassword(Long userId, String currentPassword) {
        log.info("验证用户当前密码，用户ID：{}", userId);
        
        if (Objects.isNull(userId) || StrUtil.isBlank(currentPassword)) {
            throw new BusinessException("用户ID和密码不能为空");
        }
        
        SysUser user = getById(userId);
        if (Objects.isNull(user) || user.getDeleted().equals(1)) {
            throw new BusinessException("用户不存在");
        }
        
        boolean isValid = BCrypt.checkpw(currentPassword, user.getPassword());
        log.info("验证用户当前密码{}，用户ID：{}", isValid ? "成功" : "失败", userId);
        return isValid;
    }

    @Override
    public Map<String, String> generateTwoFactorSecret(Long userId) {
        log.info("生成双因子认证密钥，用户ID：{}", userId);
        
        if (Objects.isNull(userId)) {
            throw new BusinessException("用户ID不能为空");
        }
        
        // TODO: 实现TOTP密钥生成和二维码生成
        // 这里需要集成Google Authenticator或类似的TOTP库
        
        Map<String, String> result = new HashMap<>();
        result.put("secret", "JBSWY3DPEHPK3PXP"); // 示例密钥
        result.put("qrCode", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg=="); // 示例二维码
        
        log.info("生成双因子认证密钥成功，用户ID：{}", userId);
        return result;
    }

    @Override
    public Boolean verifyTwoFactorCode(Long userId, String code) {
        log.info("验证双因子认证码，用户ID：{}", userId);
        
        if (Objects.isNull(userId) || StrUtil.isBlank(code)) {
            throw new BusinessException("用户ID和验证码不能为空");
        }
        
        // TODO: 实现TOTP验证码验证
        // 这里需要根据用户的密钥验证TOTP码
        
        boolean isValid = "123456".equals(code); // 示例验证
        log.info("验证双因子认证码{}，用户ID：{}", isValid ? "成功" : "失败", userId);
        return isValid;
    }

    @Override
    public List<Map<String, Object>> getUserLoginHistory(Long userId, Integer limit) {
        log.info("获取用户登录历史，用户ID：{}，限制数量：{}", userId, limit);
        
        if (Objects.isNull(userId)) {
            throw new BusinessException("用户ID不能为空");
        }
        
        // TODO: 实现登录历史查询
        // 这里需要创建登录历史表并实现查询逻辑
        
        List<Map<String, Object>> loginHistory = new ArrayList<>();
        Map<String, Object> record = new HashMap<>();
        record.put("loginTime", LocalDateTime.now());
        record.put("loginIp", "192.168.1.1");
        record.put("userAgent", "Mozilla/5.0");
        record.put("location", "北京市");
        loginHistory.add(record);
        
        log.info("获取用户登录历史成功，用户ID：{}，记录数：{}", userId, loginHistory.size());
        return loginHistory;
    }

    @Override
    public List<Map<String, Object>> getUserDevices(Long userId) {
        log.info("获取用户设备信息，用户ID：{}", userId);
        
        if (Objects.isNull(userId)) {
            throw new BusinessException("用户ID不能为空");
        }
        
        // TODO: 实现用户设备查询
        // 这里需要创建用户设备表并实现查询逻辑
        
        List<Map<String, Object>> devices = new ArrayList<>();
        Map<String, Object> device = new HashMap<>();
        device.put("deviceId", "device_001");
        device.put("deviceName", "Chrome浏览器");
        device.put("deviceType", "Web");
        device.put("lastActiveTime", LocalDateTime.now());
        device.put("location", "北京市");
        device.put("isCurrent", true);
        devices.add(device);
        
        log.info("获取用户设备信息成功，用户ID：{}，设备数：{}", userId, devices.size());
        return devices;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeUserDevice(Long userId, String deviceId) {
        log.info("移除用户设备，用户ID：{}，设备ID：{}", userId, deviceId);
        
        if (Objects.isNull(userId) || StrUtil.isBlank(deviceId)) {
            throw new BusinessException("用户ID和设备ID不能为空");
        }
        
        // TODO: 实现设备移除逻辑
        // 这里需要从设备表中删除指定设备并强制下线
        
        boolean success = true; // 示例返回
        log.info("移除用户设备{}，用户ID：{}，设备ID：{}", success ? "成功" : "失败", userId, deviceId);
        return success;
    }
} 