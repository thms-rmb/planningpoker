from planningpoker.db import db


def init_db():
    """Initializes the db."""
    db.create_all()


class CLI:
    def __init__(self, app=None):
        self._app = None
        if app is not None:
            self.init_app(app)

    def init_app(self, app):
        self._app = app
        self.command(init_db, "init-db")

    def command(self, handler, *args, **kwargs):
        self._app.cli.command(*args, **kwargs)(handler)


cli = CLI()
