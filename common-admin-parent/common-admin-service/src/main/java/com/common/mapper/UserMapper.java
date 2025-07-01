package com.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.core.entity.SysUser;
import com.common.core.dto.user.UserQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

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
    @Update("UPDATE sys_user SET last_login_ip = #{loginIp}, last_login_time = NOW(), " +
            "login_count = login_count + 1 WHERE id = #{userId} AND deleted = 0")
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

    // ==================== 高级查询方法 ====================

    /**
     * 多条件分页查询用户（支持模糊搜索和排序）
     * 
     * @param page 分页参数
     * @param queryDTO 查询条件
     * @return 用户分页结果
     */
    @Select("<script>" +
            "SELECT u.*, " +
            "GROUP_CONCAT(r.role_name) as role_names " +
            "FROM sys_user u " +
            "LEFT JOIN sys_user_role ur ON u.id = ur.user_id " +
            "LEFT JOIN sys_role r ON ur.role_id = r.id AND r.deleted = 0 " +
            "WHERE u.deleted = 0 " +
            "<if test='queryDTO.username != null and queryDTO.username != \"\"'>" +
            "AND u.username LIKE CONCAT('%', #{queryDTO.username}, '%') " +
            "</if>" +
            "<if test='queryDTO.nickname != null and queryDTO.nickname != \"\"'>" +
            "AND u.nickname LIKE CONCAT('%', #{queryDTO.nickname}, '%') " +
            "</if>" +
            "<if test='queryDTO.email != null and queryDTO.email != \"\"'>" +
            "AND u.email LIKE CONCAT('%', #{queryDTO.email}, '%') " +
            "</if>" +
            "<if test='queryDTO.phone != null and queryDTO.phone != \"\"'>" +
            "AND u.phone LIKE CONCAT('%', #{queryDTO.phone}, '%') " +
            "</if>" +
            "<if test='queryDTO.status != null'>" +
            "AND u.status = #{queryDTO.status} " +
            "</if>" +
            "<if test='queryDTO.gender != null'>" +
            "AND u.gender = #{queryDTO.gender} " +
            "</if>" +
            "<if test='queryDTO.startTime != null'>" +
            "AND u.create_time >= #{queryDTO.startTime} " +
            "</if>" +
            "<if test='queryDTO.endTime != null'>" +
            "AND u.create_time <= #{queryDTO.endTime} " +
            "</if>" +
            "<if test='queryDTO.keyword != null and queryDTO.keyword != \"\"'>" +
            "AND (u.username LIKE CONCAT('%', #{queryDTO.keyword}, '%') " +
            "OR u.nickname LIKE CONCAT('%', #{queryDTO.keyword}, '%') " +
            "OR u.email LIKE CONCAT('%', #{queryDTO.keyword}, '%') " +
            "OR u.phone LIKE CONCAT('%', #{queryDTO.keyword}, '%')) " +
            "</if>" +
            "GROUP BY u.id " +
            "<choose>" +
            "<when test='queryDTO.sortField != null and queryDTO.sortField != \"\"'>" +
            "ORDER BY ${queryDTO.sortField} ${queryDTO.sortOrder} " +
            "</when>" +
            "<otherwise>" +
            "ORDER BY u.create_time DESC " +
            "</otherwise>" +
            "</choose>" +
            "</script>")
    IPage<SysUser> selectUserPageAdvanced(IPage<SysUser> page, @Param("queryDTO") UserQueryDTO queryDTO);

    /**
     * 根据角色ID查询用户
     * 
     * @param roleId 角色ID
     * @return 用户列表
     */
    @Select("SELECT u.* FROM sys_user u " +
            "INNER JOIN sys_user_role ur ON u.id = ur.user_id " +
            "WHERE ur.role_id = #{roleId} AND u.deleted = 0 " +
            "ORDER BY u.create_time DESC")
    List<SysUser> selectByRoleId(@Param("roleId") Long roleId);

    /**
     * 查询最近登录的用户
     * 
     * @param limit 限制数量
     * @return 用户列表
     */
    @Select("SELECT * FROM sys_user " +
            "WHERE deleted = 0 AND last_login_time IS NOT NULL " +
            "ORDER BY last_login_time DESC " +
            "LIMIT #{limit}")
    List<SysUser> selectRecentLoginUsers(@Param("limit") Integer limit);

    /**
     * 查询活跃用户（最近30天有登录）
     * 
     * @return 用户列表
     */
    @Select("SELECT * FROM sys_user " +
            "WHERE deleted = 0 AND last_login_time >= DATE_SUB(NOW(), INTERVAL 30 DAY) " +
            "ORDER BY last_login_time DESC")
    List<SysUser> selectActiveUsers();

    /**
     * 查询用户统计信息
     * 
     * @return 统计信息
     */
    @Select("SELECT " +
            "COUNT(*) as total_count, " +
            "COUNT(CASE WHEN status = 1 THEN 1 END) as enabled_count, " +
            "COUNT(CASE WHEN status = 0 THEN 1 END) as disabled_count, " +
            "COUNT(CASE WHEN last_login_time >= DATE_SUB(NOW(), INTERVAL 30 DAY) THEN 1 END) as active_count, " +
            "COUNT(CASE WHEN gender = 1 THEN 1 END) as male_count, " +
            "COUNT(CASE WHEN gender = 2 THEN 1 END) as female_count " +
            "FROM sys_user WHERE deleted = 0")
    Map<String, Object> selectUserStatistics();

    /**
     * 批量查询用户详细信息
     * 
     * @param userIds 用户ID列表
     * @return 用户列表
     */
    @Select("<script>" +
            "SELECT * FROM sys_user " +
            "WHERE deleted = 0 AND id IN " +
            "<foreach collection='userIds' item='id' open='(' close=')' separator=','>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<SysUser> selectBatchByIds(@Param("userIds") List<Long> userIds);
} 