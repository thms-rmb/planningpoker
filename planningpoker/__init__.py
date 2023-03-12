import logging
import os

from flask import Flask

from planningpoker.cli import cli
from planningpoker.room import room
from planningpoker.db import db, migrate
from planningpoker.realtime import SocketIOExtension


def create_app():
    app = Flask(__name__, instance_relative_config=True)
    app.config.from_mapping(
        SECRET_KEY="dev",
        DATABASE_URI=os.path.join(app.instance_path, "planningpoker.sqlite"),
        SQLALCHEMY_DATABASE_URI="sqlite:///" + os.path.join(app.instance_path, "planningpoker.sqlite"),
    )

    app.config.from_pyfile("config.py", silent=True)

    logging.getLogger().setLevel(app.config.get("LOGGING_LEVEL", logging.WARNING))

    try:
        os.makedirs(app.instance_path)
    except OSError:
        pass

    cli.init_app(app)

    db.init_app(app)
    migrate.init_app(app, db)

    SocketIOExtension(app)

    app.register_blueprint(room)

    return app
