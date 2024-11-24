import pandas as pd
from db_models import Base, User, Product, Order, Coupon, CouponDetail, CouponUsage
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
import os

engine = create_engine("sqlite:///temp.db", echo=True)
Base.metadata.create_all(engine)

Session = sessionmaker(bind=engine)
session = Session()


data_folder_path = r".\csv_dummy_data"

files = os.listdir(data_folder_path)

for file in files:
    file_path = data_folder_path + os.sep + file
    print(file_path)
    df = pd.read_csv(file_path)

    # print(df)

    if file == "Coupon.csv":
        labels = [
            'coupon_id',
        ]
        df.drop(labels=labels, axis=1, inplace=True)
        df['start_date'] = pd.to_datetime(df['start_date'])
        df['end_date'] = pd.to_datetime(df['end_date'])
        # print(df)
        dict_data = df.to_dict(orient='records')
        # print(dict_data)
        data = session.bulk_insert_mappings(Coupon, dict_data)
    elif file == "CouponDetail.csv":
        labels = [
            'coupon_detail_id'
        ]
        df.drop(labels=labels, axis=1, inplace=True)
        dict_data = df.to_dict(orient='records')
        data = session.bulk_insert_mappings(CouponDetail, dict_data)
    elif file == "CouponUsage.csv":
        labels = [
            'usage_id',
        ]
        df.drop(labels=labels, axis=1, inplace=True)
        df['used_at'] = pd.to_datetime(df['used_at'])
        dict_data = df.to_dict(orient='records')
        data = session.bulk_insert_mappings(CouponUsage, dict_data)
    elif file == "Order.csv":
        labels = [
            'order_id',
        ]
        df.drop(labels=labels, axis=1, inplace=True)
        df['order_time'] = pd.to_datetime(df['order_time'])
        dict_data = df.to_dict(orient='records')
        data = session.bulk_insert_mappings(Order, dict_data)
    elif file == "Product.csv":
        labels = [
            'product_id'
        ]
        df.drop(labels=labels, axis=1, inplace=True)
        df['created_at'] = pd.to_datetime(df['created_at'])
        df['updated_at'] = pd.to_datetime(df['updated_at'])
        dict_data = df.to_dict(orient='records')
        data = session.bulk_insert_mappings(Product, dict_data)
    elif file == "User.csv":
        labels = [
            'user_id'
        ]
        df.drop(labels=labels, axis=1, inplace=True)
        df['created_at'] = pd.to_datetime(df['created_at'])
        dict_data = df.to_dict(orient='records')
        data = session.bulk_insert_mappings(User, dict_data)

session.commit()

data = session.query(Coupon).all()
for d in data:
    print(d)

data = session.query(CouponDetail).all()
for d in data:
    print(d)

data = session.query(CouponUsage).all()
for d in data:
    print(d)

data = session.query(Order).all()
for d in data:
    print(d)

data = session.query(Product).all()
for d in data:
    print(d)

data = session.query(User).all()
for d in data:
    print(d)

session.close()
