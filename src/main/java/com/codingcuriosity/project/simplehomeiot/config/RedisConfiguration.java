package com.codingcuriosity.project.simplehomeiot.config;

import com.codingcuriosity.project.simplehomeiot.controls.model.DeviceControlInfo;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceInfo;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupRawInfo;
import com.codingcuriosity.project.simplehomeiot.logs.model.ControllerLogInfo;
import com.codingcuriosity.project.simplehomeiot.logs.model.DeviceLogInfo;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerInfo;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfiguration {

  @Bean
  public JedisConnectionFactory jedisConnectionFactory() {
    return new JedisConnectionFactory();
  }

  @Bean
  public RedisTemplate<String, DeviceInfo> redisTemplateForDevice() {
    RedisTemplate<String, DeviceInfo> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(jedisConnectionFactory());
    return redisTemplate;
  }

  @Bean
  public RedisTemplate<String, GroupRawInfo> redisTemplateForGroup() {
    RedisTemplate<String, GroupRawInfo> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(jedisConnectionFactory());
    return redisTemplate;
  }

  @Bean
  public RedisTemplate<String, DeviceControlInfo> redisTemplateForControl() {
    RedisTemplate<String, DeviceControlInfo> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(jedisConnectionFactory());
    return redisTemplate;
  }

  @Bean
  public RedisTemplate<String, ControllerLogInfo> redisTemplateForControllerLog() {
    RedisTemplate<String, ControllerLogInfo> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(jedisConnectionFactory());
    return redisTemplate;
  }

  @Bean
  public RedisTemplate<String, DeviceLogInfo> redisTemplateForDeviceLog() {
    RedisTemplate<String, DeviceLogInfo> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(jedisConnectionFactory());
    return redisTemplate;
  }

  @Bean
  public RedisTemplate<String, TimerInfo> redisTemplateForTimer() {
    RedisTemplate<String, TimerInfo> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(jedisConnectionFactory());
    return redisTemplate;
  }

  @Bean
  public RedisTemplate<String, ZoneInfo> redisTemplateForZone() {
    RedisTemplate<String, ZoneInfo> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(jedisConnectionFactory());
    return redisTemplate;
  }
}
