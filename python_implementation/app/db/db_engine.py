from sqlalchemy import create_engine
from models.db_models import db_models
from core.config import DB_URL

class DB:
    def __init__(self):
        self.engine = create_engine(DB_URL)
        self.Base = db_models.Base
        self.create_db_schema()

    def create_db_schema(self):
        try:
            self.Base.metadata.create_all(self.engine)
            return None
        except Exception as e:
            return print(e)