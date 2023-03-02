from flask import Flask, session, g, jsonify, request
import config
from exts import db, mail
from models import AdminModel
from blueprints.home import home_bp
from blueprints.auth import auth_bp
from blueprints.api import api_bp
from flask_migrate import Migrate
from flasgger import Swagger, LazyJSONEncoder
from itsdangerous import TimedJSONWebSignatureSerializer as Serializer
from flask_httpauth import HTTPBasicAuth
from flask_cors import CORS, cross_origin

app = Flask(__name__)
# CORS(app, supports_credentials=True)
CORS(app, resources={r"/*": {"origins": "*"}})
app.config.from_object(config)
app.json_encoder = LazyJSONEncoder
Swagger(app)
auth = HTTPBasicAuth()

db.init_app(app)
mail.init_app(app)

migrate = Migrate(app, db)

app.register_blueprint(home_bp)
app.register_blueprint(auth_bp)
app.register_blueprint(api_bp)

@app.before_request
def my_before_request():
    admin_id = session.get("admin_id")
    if admin_id:
        admin = AdminModel.query.get(admin_id)
        setattr(g, "admin", admin)
    else:
        setattr(g, "admin", None)

@app.context_processor
def my_context_processor():
    return {"admin": g.admin}



if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug = True)
    # app.run(host="0.0.0.0", port=5000, ssl_context="adhoc")
