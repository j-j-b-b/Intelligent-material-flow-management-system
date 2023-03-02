from itsdangerous import TimedJSONWebSignatureSerializer as Serializer
from config import SECRET_KEY
from itsdangerous import BadSignature, SignatureExpired
from flask_httpauth import HTTPBasicAuth
import re
from flask import g, redirect, url_for
from models import AdminModel
from werkzeug.security import check_password_hash
auth = HTTPBasicAuth()

def generate_auth_token(user_id, expiration=36000):
    s = Serializer(SECRET_KEY, expires_in=expiration)
    return s.dumps({'user_id': user_id})

def verify_auth_token(token):
    s = Serializer(SECRET_KEY)
    # token正确
    try:
        data = s.loads(token)
        return data
    # token过期
    except SignatureExpired:
        return None
    # token错误
    except BadSignature:
        return None

@auth.verify_password
def verify_password(username, password):
    # 先验证token
    user_id = re.sub(r'^"|"$', '', username)
    user_id = verify_auth_token(user_id)
    # 如果token不存在，验证用户id与密码是否匹配
    if not user_id:
        admin = AdminModel.query.filter_by(email = username).first()
        if not admin:
                print("邮箱不存在")
                return redirect(url_for("auth.login"))
        check_password = check_password_hash(admin.password, password)
        # 如果用户id与密码对应不上，返回False
        if not check_password:
            return False
    g.user_id = user_id.get('user_id')
    return True