from flask import Blueprint, render_template

room = Blueprint("room", __name__, template_folder="templates")


@room.route("/")
def index():
    return render_template("index.html")
