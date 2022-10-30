from flask import current_app, request, render_template
from flask_socketio import SocketIO, emit

from planningpoker.db import select_room, upsert_room, insert_estimation, select_estimations_by_room_name, \
    delete_estimation, select_room_by_session_id, update_estimation, reset_estimations_by_room_name


def handle_message(data):
    print(f"Received a message: {data}")


def handle_room_selection(data):
    if not isinstance(data, dict) or "name" not in data or "room" not in data:
        current_app.logger.warning("Bad message received: {}".format(data))
        return

    current_app.logger.info("Current request session id: {}".format(request.sid))

    room = select_room(data["room"])
    if room is None:
        room = upsert_room(data["room"], False)

    current_app.logger.info(room)

    insert_estimation(request.sid, room["room_name"], data["name"], "", False)

    update_room(room["room_name"])


def handle_room_estimate(data):
    if any([
        not isinstance(data, dict),
        "sessionId" not in data,
        "estimation" not in data,
        "name" not in data,
        "room" not in data,
        "ready" not in data,
    ]):
        current_app.logger.warning("Bad message received: {}".format(data))
        return

    current_app.logger.info(data)
    update_estimation(request.sid, data["estimation"], data["ready"])

    update_room(data["room"])


def handle_room_reveal(data):
    if not isinstance(data, dict) or "revealed" not in data or "room" not in data:
        current_app.logger.warning("Bad message received: {}".format(data))
        return

    current_app.logger.info(data)

    upsert_room(data["room"], data["revealed"])

    update_room(data["room"])


def handle_room_reset(data):
    if not isinstance(data, dict) or "room" not in data:
        current_app.logger.warning("Bad message received: {}".format(data))
        return

    current_app.logger.info(data)

    reset_estimations_by_room_name(data["room"])

    update_room(data["room"])


def update_room(room_name):
    room = select_room(room_name)
    estimations = select_estimations_by_room_name(room_name)

    for estimation in estimations:
        session_id = estimation["session_id"]
        revealed = room["revealed"]

        results = render_template("estimations.html", estimations=estimations, session_id=session_id, revealed=revealed)
        emit("room-estimations", results, to=session_id)


def handle_disconnect():
    if request.sid:
        delete_estimation(request.sid)

        room = select_room_by_session_id(request.sid)
        if room is not None:
            update_room(room["room_name"])


class SocketIOExtension:
    def __init__(self, app=None):
        self.socketio = None
        if app is not None:
            self.init_app(app)

    def init_app(self, app):
        self.socketio = SocketIO(app)

        self.on("message", handle_message)
        self.on("my event", handle_message)
        self.on("room-selection", handle_room_selection)
        self.on("room-estimate", handle_room_estimate)
        self.on("room-reveal", handle_room_reveal)
        self.on("room-reset", handle_room_reset)
        self.on("disconnect", handle_disconnect)

    def on(self, message, handler, namespace=None):
        self.socketio.on(message, namespace)(handler)
