from fastapi import FastAPI, status
from fastapi.responses import JSONResponse
from apis.coupons_apis import router as coupon_router
from db import db_engine
from models.db_models.db_models import User
from sqlalchemy.orm import sessionmaker
import pandas as pd


app = FastAPI()

app.include_router(router=coupon_router, prefix="/coupons")

# create db if not created already
db = db_engine.DB()
db.create_db_schema()


@app.get("/")
def index():
    try:
        return JSONResponse(
            {"msg": "created db successfully, and this is the index page"},
            status_code=status.HTTP_200_OK,
        )
    except Exception as e:
        return JSONResponse(
            {"error": f"{e}"}, status_code=status.HTTP_500_INTERNAL_SERVER_ERROR
        )
