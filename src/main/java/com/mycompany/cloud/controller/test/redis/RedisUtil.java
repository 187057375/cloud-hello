package com.mycompany.cloud.controller.test.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
	@SuppressWarnings("rawtypes")
	@Autowired
	private RedisTemplate redisTemplate;
	private static final Long SLEEP_TIME = 100l;

	/**
	 * 批量删除对应的value
	 *
	 * @param keys
	 */
	public void remove(final String... keys) {
		for (String key : keys) {
			remove(key);
		}
	}

	/**
	 * 批量删除key
	 *
	 * @param pattern
	 */
	@SuppressWarnings("unchecked")
	public void removePattern(final String pattern) {
		Set<Serializable> keys = redisTemplate.keys(pattern);
		if (keys.size() > 0)
			redisTemplate.delete(keys);
	}

	/**
	 * 删除对应的value
	 *
	 * @param key
	 */
	@SuppressWarnings("unchecked")
	public void remove(final String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	/**
	 * 判断缓存中是否有对应的value
	 *
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 读取缓存
	 *
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object get(final String key) {
		Object result = null;
		ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
		result = operations.get(key);
		return result;
	}

	/**
	 * 写入缓存
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean set(final String key, Object value) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 写入缓存
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean set(final String key, Object value, Long expireTime) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 释放分布式锁
	 * 
	 * @param key
	 */
	@SuppressWarnings("unchecked")
	public void distributeUnLock(String key) {
		redisTemplate.delete(key);
	}

	/**
	 * 分布式锁
	 * 
	 * @param key
	 * @param expireTime
	 *            超时时间，毫秒为单位
	 */
	public Boolean distributeLock(final String key, Long expireTime) {
		Long lockTimeOut = 0l;
		while (true) { // 循环获取锁
			if (lockTimeOut >= expireTime) {
				return false;
			}

			try {
				if (lock(key, expireTime)) {
					return true;
				}
				TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lockTimeOut += SLEEP_TIME;
		}

	}

	@SuppressWarnings({ "unchecked" })
	private Boolean lock(final String key, final Long expireTime) {
		Long timeOutTime = System.currentTimeMillis() + expireTime;
		if (tryLock(key, timeOutTime)) {
			redisTemplate.expire(key, timeOutTime, TimeUnit.MILLISECONDS);
			return true;
		} else {
			// 如果没有获取到锁，判断锁是否失效
			Long currentLockTime = (Long) redisTemplate.opsForValue().get(key);
			if (currentLockTime != null && currentLockTime < System.currentTimeMillis()) {
				redisTemplate.expire(key, timeOutTime, TimeUnit.MILLISECONDS);
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private Boolean tryLock(final String key, final Long timeOutTime) {
		Object obj = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				StringRedisSerializer serializer = new StringRedisSerializer();
				Boolean success = connection.setNX(serializer.serialize(key),
						serializer.serialize(String.valueOf(timeOutTime)));
				connection.close();
				return success;
			}
		});
		return obj != null ? (Boolean) obj : false;
	}

}
