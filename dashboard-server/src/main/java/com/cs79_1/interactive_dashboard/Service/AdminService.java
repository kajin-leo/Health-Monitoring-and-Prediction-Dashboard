package com.cs79_1.interactive_dashboard.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private RedisTemplate redisTemplate;

    public String getSuperadminUsername() {
        return (String) redisTemplate.opsForValue().get("superadmin:username");
    }

    public String getSuperadminPassword() {
        return (String) redisTemplate.opsForValue().get("superadmin:password");
    }
}
