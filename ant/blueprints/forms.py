import wtforms
from wtforms.validators import Email, Length, EqualTo
from models import AdminModel, EmailCaptchaModel
from exts import db
from redis_captcha import redis_get
class RegisterForm(wtforms.Form):
    email = wtforms.StringField(validators=[Email(message="邮箱格式错误")])
    captcha = wtforms.StringField(validators=[Length(min=6, max=6, message="验证码格式错误")])
    adminname = wtforms.StringField(validators=[Length(min=3, max=2000, message="用户名格式错误")])
    password = wtforms.StringField(validators=[Length(min=6, max=20, message="密码格式错误")])
    password_confirm = wtforms.StringField(validators=[EqualTo("password", message="两次密码不一样")])
    
    def validate_email(self, field):
        email = field.data
        admin = AdminModel.query.filter_by(email=email).first()
        if admin:
            raise wtforms.ValidationError(message="该邮箱已被注册")
    
    def validate_captcha(self, field):
        captcha = field.data
        email = self.email.data
        # captcha_model = EmailCaptchaModel.query.filter_by(email=email, captcha=captcha).first()
        redis_captcha = redis_get(email)
        if not redis_captcha or captcha.lower() != redis_captcha.lower():
            raise wtforms.ValidationError(message="邮箱或者验证码错误")
        # if not captcha_model:
        #     raise wtforms.ValidationError(message="邮箱或者验证码错误")
        # else:
        #     db.session.delete(captcha_model)
        #     db.session.commit()
        
class LoginForm(wtforms.Form):
    username = wtforms.StringField(validators=[Email(message="邮箱格式错误")])
    password = wtforms.StringField(validators=[Length(min=6, max=20, message="密码格式错误")])
    
class CourierForm(wtforms.Form):
    address_x = wtforms.StringField(validators=[Length(min=1, max=6, message="x格式错误")])
    address_y = wtforms.StringField(validators=[Length(min=1, max=6, message="y格式错误")])
    number = wtforms.StringField(validators=[Length(min=11, max=11, message="手机号格式错误")])
    name = wtforms.StringField(validators=[Length(min=3, max=20, message="用户名格式错误")])
    
    

            
        
        
        
        