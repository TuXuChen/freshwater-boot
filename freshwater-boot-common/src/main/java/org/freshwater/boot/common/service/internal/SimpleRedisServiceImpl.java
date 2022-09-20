package org.freshwater.boot.common.service.internal;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.freshwater.boot.common.service.RedisService;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 基础的redis服务实现
 * @author tuxuchen
 * @date 2022/7/22 17:42
 */
@Slf4j
@Service
public class SimpleRedisServiceImpl implements RedisService {

  @Autowired
  private RedissonClient redissonClient;
  @Autowired
  private RedisTemplate redisTemplate;

  @Override
  public void lock(String key) {
    Validate.notBlank(key, "锁key不能为空");
    RLock rlock = redissonClient.getLock(key);
    rlock.lock();
  }

  @Override
  public boolean tryLock(String key, TimeUnit unit, int timeout) {
    Validate.notBlank(key, "锁key不能为空");
    Validate.notNull(unit, "时间单位不能为为空");
    Validate.isTrue(timeout > 0, "等待时间必须大于0");
    RLock rlock = redissonClient.getLock(key);
    try {
      return rlock.tryLock(timeout, unit);
    } catch (InterruptedException e) {
      log.error(e.getMessage(), e);
      Thread.currentThread().interrupt();
    }
    return false;
  }

  @Override
  public boolean tryLock(String key, TimeUnit unit, int timeout, int tryTime) {
    Validate.isTrue(timeout >= 1, "重试次数必须大于1");
    boolean result = false;
    for (int times = 0; times < tryTime; times++) {
      result = tryLock(key, unit, timeout);
      if (result) {
        return result;
      }
    }
    return result;
  }

  @Override
  public void unlock(String key) {
    Validate.notBlank(key, "锁key不能为空");
    if(this.isLocked(key)) {
      RLock rlock = redissonClient.getLock(key);
      rlock.unlock();
    }
  }

  @Override
  public boolean isLocked(String key) {
    Validate.notBlank(key, "锁key不能为空");
    RLock rlock = redissonClient.getLock(key);
    return rlock.isLocked();
  }

  @Override
  public long getAndIncrement(String code, long min) {
    Validate.notNull(code, "编码必须传入");
    long currentMin = 0;
    if (min > 0) {
      currentMin = min;
    }
    String lockCode = StringUtils.join("get_increment_async_", code);
    /**
     * -处理过程为：
     * 1、由于min的大小可能随时变化，所以一个code的请求，一次只能有一个进程操作
     * ——谁获得了lockCode，谁就进行code增量的操作
     * 2、如果当前获得的值小于currentMin，那么重新设定为currentMin后再进行操作
     * -否则就是正常进行数量新增即可
     * */
    long currentValue = 0L;
    try {
      // 1、====
      this.lock(lockCode);
      // 2、====
      currentValue = this.redissonClient.getAtomicLong(code).getAndIncrement();
      if(currentValue < currentMin) {
        this.redissonClient.getAtomicLong(code).set(currentMin + 1);
        currentValue = currentMin;
      }
    } finally {
      this.unlock(lockCode);
    }
    return currentValue;
  }

  @Override
  public long getAndIncrement(String code, long min, long expire, TimeUnit unit) {
    Validate.notNull(code , "code必须传入");
    Validate.notNull(unit , "时间单位必须传入");
    long currentMin;
    if(min <= 0) {
      currentMin = 0;
    } else {
      currentMin = min;
    }
    String lockCode = StringUtils.join("_async_", code);
    /**
     * -处理过程为：
     * 1、由于min的大小可能随时变化，所以一个code的请求，一次只能有一个进程操作
     * ——谁获得了lockCode，谁就进行code增量的操作
     * 2、如果当前获得的值小于currentMin，那么重新设定为currentMin后再进行操作
     * -否则就是正常进行数量新增即可
     * */
    long currentValue = 0L;
    try {
      // 1、====
      this.lock(lockCode);
      // 2、====
      RAtomicLong atomicLong = this.redissonClient.getAtomicLong(code);
      currentValue = atomicLong.getAndIncrement();
      if(currentValue < currentMin) {
        atomicLong.set(currentMin + 1);
        currentValue = currentMin;
      }
      atomicLong.expire(expire, unit);
    } finally {
      this.unlock(lockCode);
    }
    return currentValue;
  }

  @Override
  public Long getIncrement(String code) {
    Validate.notNull(code , "code必须传入");
    RAtomicLong atomicLong = this.redissonClient.getAtomicLong(code);
    if(!atomicLong.isExists()) {
      return null;
    }
    return atomicLong.get();
  }

  @Override
  public String getAndIncrement(String code, long min, Integer mixStrLen, long expire, TimeUnit unit) {
    Validate.isTrue(mixStrLen > 0 , "进行数字格式化的时候，设定的格式化最小长度必须大于0");
    long current;
    if(expire <= 0) {
      current = this.getAndIncrement(code, min);
    } else {
      current = this.getAndIncrement(code, min, expire, unit);
    }
    String currentValue = String.valueOf(current);
    int currentStrLen = currentValue.length();
    // 如果条件成立，说明当前返回的数字长度已经满足最小长度，不用再补位了
    if(currentStrLen >= mixStrLen) {
      return currentValue;
    }
    char[] fillChars = new char[mixStrLen];
    Arrays.fill(fillChars, 0, mixStrLen, '0');
    DecimalFormat format = new DecimalFormat(new String(fillChars));
    return format.format(current);
  }

  @Override
  public String getAndIncrement(String code, long min, Integer mixStrLen) {
    return this.getAndIncrement(code, min, mixStrLen, 0, TimeUnit.SECONDS);
  }

  @Override
  public void setValue(String key, Object value) {
    Validate.notNull(key, "key不能为空");
    ValueOperations<String, Object> ops = redisTemplate.opsForValue();
    ops.set(key, value);
  }

  @Override
  public void setValue(String key, Object value, long timeout, TimeUnit unit) {
    Validate.notNull(key, "key不能为空");
    ValueOperations<String, Object> ops = redisTemplate.opsForValue();
    ops.set(key, value, timeout, unit);
  }

  @Override
  public Object getValue(String key) {
    if(key == null) {
      return null;
    }
    ValueOperations<String, Object> ops = redisTemplate.opsForValue();
    return ops.get(key);
  }

  @Override
  public boolean delete(String key) {
    Validate.notBlank(key, "key is blank");
    return redisTemplate.delete(key);
  }
}
