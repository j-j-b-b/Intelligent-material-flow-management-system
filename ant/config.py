from flasgger import Swagger, LazyString, LazyJSONEncoder
SECRET_KEY = "200306__zcxlkylsq"

# mysql
HOSTNAME = "127.0.0.1"
PORT = 3306
USERNAME = "root"
PASSWORD = "123456"
DATABASE = "flame_of_the_flyway"
DB_URI = 'mysql+pymysql://{}:{}@{}:{}/{}?charset=utf8mb4'.format(USERNAME, PASSWORD, HOSTNAME, PORT, DATABASE)
SQLALCHEMY_DATABASE_URI = DB_URI

# 邮箱配置
MAIL_SERVER = "smtp.qq.com"
MAIL_USE_SSL = True
MAIL_PORT = 465
MAIL_USERNAME = "without_milk@qq.com"
MAIL_PASSWORD = "ltbxciqtyrsdeiih"
MAIL_DEFAULT_SENDER = "without_milk@qq.com"

# redis
CACHE_TYPE= 'redis'
CACHE_REDIS_HOST= '127.0.0.1'
CACHE_REDIS_PORT= 6379
# docker exec -it redis_test /bin/bash

# token
SECRET_KEY = 'LIUBINGZHEISBEST'
NOT_CHECK_URL = ['/auth/login']

# swagger
template = {
    "openapi": "3.0",
    "info": {
        "title": "飞路之焰 API 3.0.1",
        "description": "API for my data",
        "contact": {
            'name': "刘思圻",
            "email": "20030612@czuxt.edu.cn",
            "url":"https://github.com/j-j-b-b",
        },
        "version": "3.0.1"
    },
    "schemes": [
        "http"
    ],
}