package com.common.core.util;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Description 登录安全管理工具类，处理登录失败次数限制、账户锁定等安全功能
 * @Date 2025/1/7 16:35
 * @Author SparkFan
 */
@Slf4j
@Component
public class LoginSecurityUtil {

    private static final String LOGIN_FAIL_KEY_PREFIX = "login:fail:";
    private static final String ACCOUNT_LOCK_KEY_PREFIX = "account:lock:";
    private static final int MAX_LOGIN_ATTEMPTS = 5; // 最大登录失败次数
    private static final int FAIL_COUNT_EXPIRE_TIME = 3600; // 失败次数记录过期时间（1小时）
    private static final int ACCOUNT_LOCK_TIME = 1800; // 账户锁定时间（30分钟）

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 记录登录失败
     * 
     * @param identifier 登录标识（用户名/邮箱/手机号）
     * @return 当前失败次数
     */
    public int recordLoginFailure(String identifier) {
        if (StrUtil.isBlank(identifier)) {
            return 0;
        }
        
        String failKey = LOGIN_FAIL_KEY_PREFIX + identifier.toLowerCase();
        
        // 获取当前失败次数
        Integer failCount = (Integer) redisTemplate.opsForValue().get(failKey);
        failCount = failCount == null ? 0 : failCount;
        failCount++;
        
        // 更新失败次数
        redisTemplate.opsForValue().set(failKey, failCount, FAIL_COUNT_EXPIRE_TIME, TimeUnit.SECONDS);
        
        log.warn("记录登录失败，标识：{}，当前失败次数：{}", identifier, failCount);
        
        // 如果达到最大失败次数，锁定账户
        if (failCount >= MAX_LOGIN_ATTEMPTS) {
            lockAccount(identifier);
            log.warn("账户因登录失败次数过多被锁定，标识：{}", identifier);
        }
        
        return failCount;
    }

    /**
     * 清除登录失败记录
     * 
     * @param identifier 登录标识
     */
    public void clearLoginFailure(String identifier) {
        if (StrUtil.isBlank(identifier)) {
            return;
        }
        
        String failKey = LOGIN_FAIL_KEY_PREFIX + identifier.toLowerCase();
        redisTemplate.delete(failKey);
        
        log.debug("清除登录失败记录，标识：{}", identifier);
    }

    /**
     * 获取登录失败次数
     * 
     * @param identifier 登录标识
     * @return 失败次数
     */
    public int getLoginFailureCount(String identifier) {
        if (StrUtil.isBlank(identifier)) {
            return 0;
        }
        
        String failKey = LOGIN_FAIL_KEY_PREFIX + identifier.toLowerCase();
        Integer failCount = (Integer) redisTemplate.opsForValue().get(failKey);
        return failCount == null ? 0 : failCount;
    }

    /**
     * 锁定账户
     * 
     * @param identifier 登录标识
     */
    public void lockAccount(String identifier) {
        if (StrUtil.isBlank(identifier)) {
            return;
        }
        
        String lockKey = ACCOUNT_LOCK_KEY_PREFIX + identifier.toLowerCase();
        redisTemplate.opsForValue().set(lockKey, System.currentTimeMillis(), ACCOUNT_LOCK_TIME, TimeUnit.SECONDS);
        
        log.warn("锁定账户，标识：{}，锁定时间：{}秒", identifier, ACCOUNT_LOCK_TIME);
    }

    /**
     * 解锁账户
     * 
     * @param identifier 登录标识
     */
    public void unlockAccount(String identifier) {
        if (StrUtil.isBlank(identifier)) {
            return;
        }
        
        String lockKey = ACCOUNT_LOCK_KEY_PREFIX + identifier.toLowerCase();
        redisTemplate.delete(lockKey);
        
        // 同时清除失败次数记录
        clearLoginFailure(identifier);
        
        log.info("解锁账户，标识：{}", identifier);
    }

    /**
     * 检查账户是否被锁定
     * 
     * @param identifier 登录标识
     * @return 是否被锁定
     */
    public boolean isAccountLocked(String identifier) {
        if (StrUtil.isBlank(identifier)) {
            return false;
        }
        
        String lockKey = ACCOUNT_LOCK_KEY_PREFIX + identifier.toLowerCase();
        return Boolean.TRUE.equals(redisTemplate.hasKey(lockKey));
    }

    /**
     * 获取账户锁定剩余时间（秒）
     * 
     * @param identifier 登录标识
     * @return 剩余锁定时间，-1表示未锁定
     */
    public long getAccountLockRemainingTime(String identifier) {
        if (StrUtil.isBlank(identifier)) {
            return -1;
        }
        
        String lockKey = ACCOUNT_LOCK_KEY_PREFIX + identifier.toLowerCase();
        Long expireTime = redisTemplate.getExpire(lockKey, TimeUnit.SECONDS);
        return expireTime == null ? -1 : expireTime;
    }

    /**
     * 检查是否可以登录（未锁定且失败次数未达上限）
     * 
     * @param identifier 登录标识
     * @return 是否可以登录
     */
    public boolean canLogin(String identifier) {
        if (StrUtil.isBlank(identifier)) {
            return false;
        }
        
        // 检查账户是否被锁定
        if (isAccountLocked(identifier)) {
            log.warn("账户被锁定，拒绝登录，标识：{}", identifier);
            return false;
        }
        
        return true;
    }

    /**
     * 获取登录失败提示信息
     * 
     * @param identifier 登录标识
     * @return 提示信息
     */
    public String getLoginFailureMessage(String identifier) {
        if (StrUtil.isBlank(identifier)) {
            return "登录失败";
        }
        
        if (isAccountLocked(identifier)) {
            long remainingTime = getAccountLockRemainingTime(identifier);
            if (remainingTime > 0) {
                long minutes = remainingTime / 60;
                long seconds = remainingTime % 60;
                return String.format("账户已被锁定，请在 %d分%d秒 后重试", minutes, seconds);
            } else {
                return "账户已被锁定，请稍后重试";
            }
        }
        
        int failCount = getLoginFailureCount(identifier);
        int remainingAttempts = MAX_LOGIN_ATTEMPTS - failCount;
        
        if (failCount > 0 && remainingAttempts > 0) {
            return String.format("登录失败，还有 %d 次尝试机会", remainingAttempts);
        }
        
        return "用户名或密码错误";
    }

    /**
     * 获取最大登录失败次数
     * 
     * @return 最大失败次数
     */
    public int getMaxLoginAttempts() {
        return MAX_LOGIN_ATTEMPTS;
    }

    /**
     * 获取账户锁定时间（秒）
     * 
     * @return 锁定时间
     */
    public int getAccountLockTime() {
        return ACCOUNT_LOCK_TIME;
    }
} 