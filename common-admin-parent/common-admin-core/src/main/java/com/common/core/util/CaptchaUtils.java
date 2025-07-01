package com.common.core.util;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description 验证码工具类，提供验证码生成、验证、清理等功能
 * @Date 2025/1/7 16:30
 * @Author SparkFan
 */
@Slf4j
@Component
public class CaptchaUtils {

    private static final String CAPTCHA_KEY_PREFIX = "captcha:";
    private static final int CAPTCHA_EXPIRE_TIME = 300; // 5分钟过期
    private static final int CAPTCHA_WIDTH = 130;
    private static final int CAPTCHA_HEIGHT = 48;
    private static final int CAPTCHA_CODE_COUNT = 4;
    private static final int CAPTCHA_INTERFERE_COUNT = 8;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 生成验证码
     * 
     * @return 验证码信息（包含key和base64图片）
     */
    public Map<String, String> generateCaptcha() {
        try {
            // 生成验证码
            LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(
                    CAPTCHA_WIDTH, CAPTCHA_HEIGHT, CAPTCHA_CODE_COUNT, CAPTCHA_INTERFERE_COUNT);
            
            // 生成唯一key
            String captchaKey = IdUtil.simpleUUID();
            String code = lineCaptcha.getCode();
            
            // 存储到Redis（不区分大小写）
            String redisKey = CAPTCHA_KEY_PREFIX + captchaKey;
            redisTemplate.opsForValue().set(redisKey, code.toLowerCase(), CAPTCHA_EXPIRE_TIME, TimeUnit.SECONDS);
            
            // 获取Base64图片
            String imageBase64 = lineCaptcha.getImageBase64Data();
            
            Map<String, String> result = new HashMap<>();
            result.put("key", captchaKey);
            result.put("image", imageBase64);
            
            log.debug("生成验证码成功，key：{}，code：{}", captchaKey, code);
            return result;
            
        } catch (Exception e) {
            log.error("生成验证码失败", e);
            throw new RuntimeException("验证码生成失败");
        }
    }

    /**
     * 验证验证码
     * 
     * @param captchaKey 验证码key
     * @param userCode 用户输入的验证码
     * @return 是否验证通过
     */
    public boolean verifyCaptcha(String captchaKey, String userCode) {
        if (StrUtil.isBlank(captchaKey) || StrUtil.isBlank(userCode)) {
            log.warn("验证码参数为空，key：{}，code：{}", captchaKey, userCode);
            return false;
        }
        
        try {
            String redisKey = CAPTCHA_KEY_PREFIX + captchaKey;
            String storedCode = (String) redisTemplate.opsForValue().get(redisKey);
            
            if (StrUtil.isBlank(storedCode)) {
                log.warn("验证码已过期或不存在，key：{}", captchaKey);
                return false;
            }
            
            // 不区分大小写比较
            boolean isValid = storedCode.equalsIgnoreCase(userCode.trim());
            
            if (isValid) {
                // 验证成功后删除验证码
                redisTemplate.delete(redisKey);
                log.debug("验证码验证成功，key：{}", captchaKey);
            } else {
                log.warn("验证码验证失败，key：{}，expected：{}，actual：{}", captchaKey, storedCode, userCode);
            }
            
            return isValid;
            
        } catch (Exception e) {
            log.error("验证码验证异常，key：{}", captchaKey, e);
            return false;
        }
    }

    /**
     * 清理过期验证码（可选，Redis自动过期）
     */
    public void clearExpiredCaptcha() {
        // Redis会自动清理过期数据，这里可以添加额外的清理逻辑
        log.debug("清理过期验证码");
    }

    /**
     * 删除指定验证码
     * 
     * @param captchaKey 验证码key
     */
    public void deleteCaptcha(String captchaKey) {
        if (StrUtil.isNotBlank(captchaKey)) {
            String redisKey = CAPTCHA_KEY_PREFIX + captchaKey;
            redisTemplate.delete(redisKey);
            log.debug("删除验证码，key：{}", captchaKey);
        }
    }

    /**
     * 检查验证码是否存在
     * 
     * @param captchaKey 验证码key
     * @return 是否存在
     */
    public boolean existsCaptcha(String captchaKey) {
        if (StrUtil.isBlank(captchaKey)) {
            return false;
        }
        
        String redisKey = CAPTCHA_KEY_PREFIX + captchaKey;
        return Boolean.TRUE.equals(redisTemplate.hasKey(redisKey));
    }
} 