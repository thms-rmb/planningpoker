# Planning Poker

Use the planning poker app to organize voting between multiple people.

## Running

```shell
python3 -m venv venv
source venv/bin/activate

pip install .

gunicorn \
  --workers 1 \
  --threads 100 \
  --worker-class eventlet \
  --access-logfile - \
  --bind localhost:9999 \
  --reload \
  planningpoker:create_app()
```
