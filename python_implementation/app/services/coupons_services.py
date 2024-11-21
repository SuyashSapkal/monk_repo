from db import db_engine
from models.db_models import db_models
from sqlalchemy.orm import sessionmaker
from datetime import datetime
import json


class CouponService:
    def __init__(self):
        self.db = db_engine.DB()
        self.Session = sessionmaker(bind=self.db.engine)

    def get_coupons_data(self):
        try:
            with self.Session() as session:
                data = session.query(
                    db_models.Coupon.coupon_id,
                    db_models.Coupon.coupon_code,
                    db_models.Coupon.coupon_name,
                    db_models.Coupon.coupon_type,
                ).all()

            data_dict_list = [
                {
                    "coupon_id": c_id,
                    "coupon_code": c_code,
                    "coupon_name": c_name,
                    "coupon_type": c_type,
                }
                for c_id, c_code, c_name, c_type in data
            ]

            return data_dict_list
        except Exception as e:
            print(e)
            return None
        finally:
            session.close()

    def create_coupons(self, data: dict):
        try:
            Coupon = db_models.Coupon
            CouponDetails = db_models.CouponDetail
            details: dict = data["details"]

            with self.Session() as session:
                # Create the new coupon
                new_coupon = Coupon(
                    coupon_code=data["coupon_code"],
                    coupon_name=data["coupon_name"],
                    coupon_type=data["coupon_type"],
                    discount_value=data["discount_value"],
                    status=data["status"],
                    start_date=datetime.strptime(
                        data["start_date"], "%Y-%m-%d %H:%M:%S.%f"
                    ),
                    end_date=datetime.strptime(
                        data["end_date"], "%Y-%m-%d %H:%M:%S.%f"
                    ),
                )
                session.add(new_coupon)
                session.commit()

                # Create coupon details
                created_coupon_details = []
                for key, value in details.items():
                    if type(value) == list or type(value) == dict:
                        value = json.dumps(value)
                    new_coupon_details = CouponDetails(
                        coupon_id=new_coupon.coupon_id,
                        detail_key=key,
                        detail_value=value,
                    )
                    session.add(new_coupon_details)
                    created_coupon_details.append(new_coupon_details)

                session.commit()

                print(new_coupon, created_coupon_details)

                return new_coupon, created_coupon_details

        except Exception as e:
            print(e)
            return None, None

        finally:
            session.close()

    def get_coupons_id(self, id: int):
        try:
            with self.Session() as session:
                data = (
                    session.query(
                        db_models.Coupon.coupon_id,
                        db_models.Coupon.coupon_code,
                        db_models.Coupon.coupon_name,
                        db_models.Coupon.coupon_type,
                    )
                    .filter(db_models.Coupon.coupon_id == id)
                    .first()
                )
                print(data)
                data_dict_list = [
                    {
                        "coupon_id": data[0],
                        "coupon_code": data[1],
                        "coupon_name": data[2],
                        "coupon_type": data[3],
                    }
                ]
                print(data_dict_list)
                return data_dict_list
        except Exception as e:
            print(e)
            return None
        finally:
            session.close()

    def update_coupon(self, data: dict, id: int):
        try:
            Coupon = db_models.Coupon
            CouponDetails = db_models.CouponDetail
            details: dict = data["details"]

            with self.Session() as session:
                req_coupon = (
                    session.query(Coupon).filter(Coupon.coupon_id == id).first()
                )

                req_coupon.coupon_code = data["coupon_code"]
                req_coupon.coupon_name = data["coupon_name"]
                req_coupon.coupon_type = data["coupon_type"]
                req_coupon.discount_value = data["discount_value"]
                req_coupon.status = data["status"]
                req_coupon.start_date = datetime.strptime(
                    data["start_date"], "%Y-%m-%d %H:%M:%S.%f"
                )
                req_coupon.end_date = datetime.strptime(
                    data["end_date"], "%Y-%m-%d %H:%M:%S.%f"
                )
                session.commit()

                session.query(CouponDetails).filter(
                    CouponDetails.coupon_id == id
                ).delete()
                session.commit()

                created_coupon_details = []
                for key, value in details.items():
                    if isinstance(value, (list, dict)):
                        value = json.dumps(value)
                    new_detail = CouponDetails(
                        coupon_id=req_coupon.coupon_id,
                        detail_key=key,
                        detail_value=value,
                    )
                    created_coupon_details.append(new_detail)

                session.add_all(created_coupon_details)
                session.commit()
                print(req_coupon, created_coupon_details)

                return req_coupon, created_coupon_details

        except Exception as e:
            print(e)
            return None, None

        finally:
            session.close()

    def delete_coupon(self, id: int):
        try:
            Coupon = db_models.Coupon
            CouponDetails = db_models.CouponDetail

            with self.Session() as session:
                session.query(Coupon).filter(Coupon.coupon_id == id).delete()
                session.query(CouponDetails).filter(
                    CouponDetails.coupon_id == id
                ).delete()
                session.commit()

                return True

        except Exception as e:
            print(e)
            return False

        finally:
            session.close()

    def apply_coupon(self, id: int, data: dict):
        try:

            with self.Session() as session:
                Coupon = db_models.Coupon
                user_id = data["user_id"]
                discount_applied = (
                    session.query(Coupon.discount_value)
                    .filter(Coupon.coupon_id == id)
                    .first()
                )
                if discount_applied:
                    discount_value = float(discount_applied[0])
                else:
                    discount_value = float(0)

                user_id = data["user_id"]
                coupon_ids = json.dumps([id])
                product_ids = []
                for item in data["cart"]["items"]:
                    product_ids.append(item["product_id"])

                product_ids = json.dumps(product_ids)
                Order = db_models.Order
                CouponUsage = db_models.CouponUsage

                order = Order(
                    user_id=user_id,
                    product_ids=product_ids,
                    coupon_ids=coupon_ids,
                )
                session.add(order)
                session.commit()

                order_id = order.order_id
                order_time = order.order_time

                coupon_usage = CouponUsage(
                    coupon_id=id,
                    user_id=user_id,
                    order_id=order_id,
                    discount_applied=discount_value,
                    used_at=order_time,
                )
                session.add(coupon_usage)
                session.commit()

                return True
        except Exception as e:
            print(e)
            return False
        finally:
            session.close()
