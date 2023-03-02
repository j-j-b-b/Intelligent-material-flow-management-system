from functools import wraps
from flask import g, redirect, url_for, jsonify

def login_required(func):
    @wraps(func)
    def inner(*args, **kwargs):
        if g.admin:
            return func(*args, **kwargs)
        else:
            res = {
            'isLogin': '-1',
            'msg': 'no login'
            }
            return jsonify(res)
    return inner