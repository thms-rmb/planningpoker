from flask_migrate import Migrate
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.dialects.sqlite import Insert
from sqlalchemy import MetaData, Table, Column, String, Boolean, ForeignKey, BinaryExpression

metadata = MetaData()
room_table = Table(
    "room",
    metadata,
    Column("room_name", String(50), primary_key=True),
    Column("revealed", Boolean()),
)
estimation_table = Table(
    "estimation",
    metadata,
    Column("session_id", String(50), primary_key=True),
    Column("room_name", String(50), ForeignKey(room_table.c.room_name)),
    Column("user_name", String(50)),
    Column("estimation_value", String(10)),
    Column("ready", Boolean()),
)

db = SQLAlchemy(metadata=metadata)
migrate = Migrate()


def fetch_all_as_dict(statement):
    with db.engine.connect() as conn:
        return [r._asdict() for r in conn.execute(statement)]


def fetch_single_as_dict(statement):
    with db.engine.connect() as conn:
        for r in conn.execute(statement):
            return r._asdict()


def update(statement):
    with db.engine.connect() as conn:
        results = conn.execute(statement)
        conn.commit()

        return results.rowcount


def select_room(room_name):
    where_clause = room_table.c.room_name == room_name
    statement = room_table.select().where(where_clause)

    return fetch_single_as_dict(statement)


def select_room_by_session_id(session_id):
    join_clause = (
        estimation_table.c.room_name == room_table.c.room_name
        and estimation_table.c.session_id == session_id
    )
    statement = (
        room_table
        .select()
        .join(estimation_table, join_clause)
    )

    return fetch_single_as_dict(statement)


def upsert_room(room_name, revealed):
    statement = (
        Insert(room_table)
        .values(dict(room_name=room_name, revealed=revealed))
        .returning(room_table.c.room_name, room_table.c.revealed)
        .on_conflict_do_update(
            index_elements=[room_table.c.room_name],
            index_where=room_table.c.room_name == room_name,
            set_=dict(revealed=revealed)
        )
    )

    with db.engine.connect() as conn:
        results = conn.execute(statement).fetchall()
        conn.commit()

    for r in results:
        return r._asdict()


def select_estimations_by_room_name(room_name):
    where_clause = estimation_table.c.room_name == room_name
    statement = estimation_table.select().where(where_clause)

    return fetch_all_as_dict(statement)


def select_estimation_by_session_id(session_id):
    where_clause = estimation_table.c.session_id == session_id
    statement = estimation_table.select().where(where_clause)

    return fetch_single_as_dict(statement)


def insert_estimation(session_id, room_name, user_name, estimation_value, ready):
    values = dict(
        session_id=session_id,
        room_name=room_name,
        user_name=user_name,
        estimation_value=estimation_value,
        ready=ready,
    )

    statement = estimation_table.insert().values(values)

    return update(statement)


def delete_estimation(session_id):
    where_clause = estimation_table.c.session_id == session_id
    statement = estimation_table.delete().where(where_clause)

    return update(statement)


def update_estimation(session_id, estimation_value, ready):
    where_clause = estimation_table.c.session_id == session_id
    statement = estimation_table.update().where(where_clause).values(
        estimation_value=estimation_value,
        ready=ready
    )

    return update(statement)


def reset_estimations_by_room_name(room_name):
    where_clause = estimation_table.c.room_name == room_name
    statement = estimation_table.update().where(where_clause).values(estimation_value="", ready=False)

    return update(statement)
