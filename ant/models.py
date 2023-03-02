from exts import db
from datetime import datetime

class AdminModel(db.Model):
    __tablrname__ = "admins"
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    adminname = db.Column(db.String(1000), nullable=False)
    password = db.Column(db.String(200), nullable=False)
    email = db.Column(db.String(100), nullable=False, unique=True)
    join_time = db.Column(db.DateTime, default=datetime.now)

class EmailCaptchaModel(db.Model):
    __tablrname__ = "email_captcha"
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    email = db.Column(db.String(100), nullable=False)
    captcha = db.Column(db.String(100), nullable=False)
    # used = db.Column(db.Boolean, default=False)

class CourierInformationModel(db.Model):
    __tablrname__ = "courier_information"
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    address_x = db.Column(db.Float, nullable=False)
    address_y = db.Column(db.Float, nullable=False)
    number =db.Column(db.String(100), nullable=False)
    name = db.Column(db.String(100), nullable=False)

class UserModel(db.Model):
    __tablrname__ = "user"
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    username = db.Column(db.String(100), nullable=False)
    password = db.Column(db.String(200), nullable=False)
    email = db.Column(db.String(100), nullable=False, unique=True)
    join_time = db.Column(db.DateTime, default=datetime.now)
# flask db migrate
# flask db upgrade