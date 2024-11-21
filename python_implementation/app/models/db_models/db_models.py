from sqlalchemy import (
    Column,
    Integer,
    String,
    DateTime,
    DECIMAL,
    Enum,
    ForeignKey,
    Numeric,
)
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship
from datetime import datetime

Base = declarative_base()


class Coupon(Base):
    __tablename__ = "coupons"

    coupon_id = Column(Integer, primary_key=True, autoincrement=True)
    coupon_code = Column(String(50), unique=True, nullable=False)
    coupon_name = Column(String(255))
    coupon_type = Column(String(50))
    discount_value = Column(DECIMAL(10, 2))
    status = Column(
        Enum("active", "inactive", "expired", name="coupon_status"), default="active"
    )
    start_date = Column(DateTime)
    end_date = Column(DateTime)

    coupon_details = relationship("CouponDetail", back_populates="coupon")

    def __repr__(self):
        return (
            f"<Coupon(coupon_code={self.coupon_code}, coupon_type={self.coupon_type})>"
        )


class CouponDetail(Base):
    __tablename__ = "coupon_details"

    coupon_detail_id = Column(Integer, primary_key=True, autoincrement=True)
    coupon_id = Column(
        Integer, ForeignKey("coupons.coupon_id", ondelete="CASCADE"), nullable=False
    )
    detail_key = Column(String(100))
    detail_value = Column(String(255))

    coupon = relationship("Coupon", back_populates="coupon_details")

    def __repr__(self):
        return (
            f"<CouponDetail(coupon_id={self.coupon_id}, detail_key={self.detail_key})>"
        )


class CouponUsage(Base):
    __tablename__ = "coupon_usage"

    usage_id = Column(Integer, primary_key=True, autoincrement=True)
    coupon_id = Column(Integer, ForeignKey("coupons.coupon_id"), nullable=False)
    user_id = Column(Integer, ForeignKey("users.user_id"))
    order_id = Column(Integer, ForeignKey("orders.order_id"))
    used_at = Column(DateTime)
    discount_applied = Column(DECIMAL(10, 2))

    coupon = relationship("Coupon", backref="coupon_usages")

    def __repr__(self):
        return f"<CouponUsage(coupon_id={self.coupon_id}, user_id={self.user_id})>"


class Product(Base):
    __tablename__ = "products"

    product_id = Column(Integer, primary_key=True, autoincrement=True)
    name = Column(String(255), nullable=False)
    description = Column(String(500))
    price = Column(Numeric(10, 2), nullable=False)
    stock_quantity = Column(Integer, default=0)
    created_at = Column(DateTime, default=datetime.now())
    updated_at = Column(DateTime, default=datetime.now(), onupdate=datetime.now())

    def __repr__(self):
        return f"<Product(name={self.name}, price={self.price}, stock_quantity={self.stock_quantity})>"


class User(Base):
    __tablename__ = "users"

    user_id = Column(Integer, primary_key=True, autoincrement=True)
    username = Column(String(100), unique=True, nullable=False)
    email = Column(String(255), unique=True, nullable=False)
    password_hash = Column(String(255), nullable=False)
    first_name = Column(String(100))
    last_name = Column(String(100))
    created_at = Column(DateTime, default=datetime.now())

    coupons_used = relationship("CouponUsage", backref="users")

    def __repr__(self):
        return f"<User(username={self.username}, email={self.email})>"


class Order(Base):
    __tablename__ = "orders"

    order_id = Column(Integer, primary_key=True, autoincrement=True)
    user_id = Column(Integer, ForeignKey("users.user_id"))
    product_ids = Column(String(255), nullable=False)
    coupon_ids = Column(String(255), default=None)
    order_time = Column(DateTime, default=datetime.now())

    def __repr__(self):
        return f"<Order(order_id={self.order_id}, order_time={self.order_time})>"
