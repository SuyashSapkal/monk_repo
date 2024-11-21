from pydantic import BaseModel
from datetime import datetime
from enum import Enum


class Status(Enum):
    active = "active"
    inactive = "inactive"
    expired = "expired"


class Coupon(BaseModel):
    coupon_code: str
    coupon_code: str
    coupon_name: str
    coupon_type: str
    discount_value: float
    status: Status
    start_date: datetime
    end_date: datetime
    coupon_details: dict


class Product(BaseModel):
    name: str
    description: str
    price: float
    stock_quantity: int


class User(BaseModel):
    username: str
    email: str
    password_hash: str
    first_name: str
    last_name: str


class Order(BaseModel):
    user_id: int
    product_ids: int
    coupon_ids: int
    order_time: datetime
