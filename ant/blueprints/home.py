from flask import Blueprint, render_template, request, redirect, url_for, g, jsonify
from models import CourierInformationModel
from .forms import CourierForm
from exts import db
from flasgger import swag_from
from decorators import login_required
from verify_token import auth
from flask_cors import CORS
import copy


home_bp = Blueprint("home", __name__, url_prefix = "/")
CORS(home_bp, resources={r"/*": {"origins": "*"}})

@home_bp.route("/")
# @auth.login_required
def index():
    return "hello"

@home_bp.route("/input", methods=['POST', 'GET'])  # /<name>/<number>/<address_x>/<address_y>
# @login_required
@swag_from("../yml_files/input.yml")
def input():  # name,number,address_x,address_y
    # 表单验证
    form = CourierForm(request.form)
    if form.validate():
        address_x = request.form.get("address_x")
        address_y = request.form.get("address_y")
        number = request.form.get("number")
        name = request.form.get("name")
        courier_information = CourierInformationModel(address_x=address_x, address_y=address_y, number=number, name=name)
        db.session.add(courier_information)
        db.session.commit()
        # return jsonify({"code": 200, "message": "", "data": None})
        res = {
            'isInput': '0',
            'msg': 'success'
            }
        return jsonify(res)
    else:
        print(form.errors)
        res = {
            'isInput': '-1',
            'msg': 'error'
            }
        return jsonify(res)


@home_bp.route("/delete/<id>", methods=['DELETE'])
@swag_from("../yml_files/delete.yml")
def delete(id):
    courier = CourierInformationModel.query.get(id)
    if not courier:
        res = {
        'isDelete': '-1',
        'msg': 'no courier'
        }
        return jsonify(res)    
    else:
        # courier = CourierInformationModel.query.get(courier)
        db.session.delete(courier)
        db.session.commit()
        res = {
        'isDelete': '0',
        'msg': 'success'
        }
        return jsonify(res)

@home_bp.route("/purchase/<id>", methods=['PUT'])
@swag_from("../yml_files/purchase.yml")
def purchase(id):
    # 表单验证
    courier_information = CourierInformationModel.query.get(id)
    form = CourierForm(request.form)
    if form.validate():
        courier_information.address_x = request.form.get("address_x")
        courier_information.address_y = request.form.get("address_y")
        courier_information.number = request.form.get("number")
        courier_information.name = request.form.get("name")
        db.session.commit()

        # return jsonify({"code": 200, "message": "", "data": None})
        res = {
       'isPurchase': '0',
        'msg': 'success'
        }
        return jsonify(res)
    else:
        print(form.errors)
        res = {
        'isPurchase': '-1',
        'msg': 'error'
        }
        return jsonify(res)


@home_bp.route("/output/<int:page>/<int:limits>")
@swag_from("../yml_files/output_path.yml")
def output(page, limits):
    distance_x = []
    distance_y = []
    courier_information = CourierInformationModel.query.offset((page-1) * limits).limit(limits)
    for i in courier_information:
        distance_x.append(i.address_x)
        distance_y.append(i.address_y)
    list_json = []
    api_json = {"id": 1, "address_x": 1, "address_y": 2, "number":0, "name": "2"}
    for i, j in zip(distance_x, distance_y):
        api_json["address_x"] = i
        api_json["address_y"] = j
        for ci in  courier_information:
            if ci.address_x == i and ci.address_y == j:
                api_json["id"] = ci.id
                api_json["name"] = ci.name
                api_json["number"] = ci.number
        list_json.append(copy.deepcopy(api_json))
        courier_information = CourierInformationModel.query.all()
        count = 0
        for i in courier_information:
            count = count + 1
    return jsonify({"data" : list_json,
                    "count": count})