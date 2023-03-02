import redis

r = redis.StrictRedis(host='localhost', port=6379, db=0, decode_responses=True)
# 存储验证码
def redis_set(key, value, timeout=300):  # timeout=300过期时间5分钟
    return r.set(key, value, timeout)

# 提取验证码
def redis_get(key):
    return r.get(key)

# 删除验证码
def redis_delete(key):
    return r.delete(key)
