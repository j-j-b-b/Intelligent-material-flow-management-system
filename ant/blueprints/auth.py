from flask import Blueprint, render_template, jsonify, redirect, url_for, session, g
from exts import mail, db
from flask_mail import Message
from flask import request
import string
import random
from models import EmailCaptchaModel
from .forms import RegisterForm, LoginForm
from models import AdminModel
from werkzeug.security import generate_password_hash, check_password_hash
from flasgger import swag_from
import redis_captcha
from verify_token import auth, generate_auth_token
from flask_cors import CORS
import json

auth_bp = Blueprint("auth", __name__, url_prefix = "/auth")
CORS(auth_bp, resources={r"/*": {"origins": "*"}})

@auth_bp.route("/login", methods=['GET', 'POST', 'OPTIONS'])
@swag_from("../yml_files/login.yml")
def login():
    if request.method == 'GET':
        res = {
        'isLogin': '0',
        'msg': 'success'
        }
        return jsonify(res)
        # return render_template("login.html")
    else:
        print(request.form.get("username"))
        print(request.form.get("password"))
        form = LoginForm(request.form)
        if form.validate():
            username = request.form.get("username")
            password = request.form.get("password")
            admin = AdminModel.query.filter_by(email=username).first()
            if not admin:
                print("邮箱不存在")
                res = {
                'isLogin': '-1',
                'msg': 'email erro'
                }
                return jsonify(res)
                # return redirect(url_for("auth.login"))
                
            if check_password_hash(admin.password, password):
                # flask中的session是加密后存储在cookie中的
                session['admin_id'] = admin.id
                res = {
                'isLogin': '0',
                'msg': 'success'
                }
                print('success')
                return jsonify(res)
                # return redirect("/")
            else:
                print("密码错误")
                res = {
                'isLogin': '-1',
                'msg': 'password erro'
                }
                return jsonify(res)
                # return redirect(url_for("auth.login"))
        else:
            print(form.errors)
            res = {
            'isLogin': '-1',
            'msg': 'geshicuow'
            }
            return jsonify(res), 400
            # return redirect(url_for("auth.login"))
  
@auth_bp.route("/register", methods=['GET', 'POST'])
@swag_from("../yml_files/register.yml")
def register():
    if request.method == 'GET':
        res = {
        'isRegister': '0',
        'msg': 'success'
        }
        return jsonify(res)
    else:
        # 表单验证
        form = RegisterForm(request.form)
        if form.validate():
            email = form.email.data
            adminname = form.adminname.data
            password = form.password.data
            admin = AdminModel(email=email, adminname=adminname, password=generate_password_hash(password))
            db.session.add(admin)
            db.session.commit()
            redis_captcha.redis_delete(email)
            # return redirect("/auth/login")
            res = {
            'isRegister': '0',
            'msg': 'success'
            }
            return jsonify(res)
        else:
            print(form.errors)
            res = {
            'isRegister': '-1',
            'msg': 'form erro'
            }
            return jsonify(res), 400
 
@auth_bp.route("/logout", methods=['POST']) 
def logout():
    session.clear()
    res = {
    'isLogout': '0',
    'msg': 'success'
    }
    return jsonify(res)
            
@auth_bp.route("/captcha/email", methods=['GET', 'POST'])
@swag_from("../yml_files/captcha.yml")
def get_email_captcha():
    email = request.args.get("email")
    source = string.digits*6
    captcha = random.sample(source, 6)
    captcha = "".join(captcha)
    print(captcha)
    message = Message(subject="飞路之焰", recipients=[email], body=f"您的邮箱验证码是：{captcha}，如果您没有尝试注册，请忽略这封电子邮件")
    mail.send(message)
    redis_captcha.redis_set(key=email, value=captcha)
    
    # RESTful API
    return jsonify({"code": 200, "message": "captcha success", "data": None})