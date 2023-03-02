from flask import Blueprint, jsonify
import algorithm
from flasgger import swag_from
from models import CourierInformationModel
import copy
import matplotlib.pyplot as plt

api_bp = Blueprint("api", __name__, url_prefix = "/api")

@api_bp.route("/new")
@swag_from("../yml_files/new.yml")
def initialization():
    algorithm.initialization()
    algorithm.new()
    return jsonify({"code": 200, "message": "", "data": ''})

@api_bp.route("/way")
@swag_from("../yml_files/way.yml")
def api_json():
    # algorithm.initialization()
    # algorithm.new()
    list_json, shortest_distance = algorithm.search_path()
    return jsonify({
        "distance_x_y": list_json,
        "shortest_distance": shortest_distance
    })

@api_bp.route("/way_for")
@swag_from("../yml_files/way_for.yml")
def api_json_for():
    algorithm.initialization()
    algorithm.new()
    list_json, shortest_distance = algorithm.search_path_for()
    # x = []
    # y = []
    # z = []
    # print(list_json)
    # for i in list_json:
    #     x.append(i['x'])
    #     y.append(i['y'])
    #     z.append(i['sort'])
    # for i in range(len(z)-1):
    #     x1, y1 = x[z.index(i)], y[z.index(i)]
    #     x2, y2 = x[z.index(i+1)], y[z.index(i+1)]
    #     plt.plot([x1, x2], [y1, y2], 'o-', linewidth=2)
    # plt.show()
    return jsonify({
        "distance_x_y": list_json,
        "shortest_distance": shortest_distance
    })

@api_bp.route("/output")
@swag_from("../yml_files/output.yml")
def output():
    algorithm.initialization()
    algorithm.new()
    data = algorithm.outdata()
    courier_information = CourierInformationModel.query.all()
    count = 0
    for i in courier_information:
        count = count + 1
    return jsonify({"data" : data,
                    "count": count})



