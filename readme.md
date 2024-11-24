# Monk Repo

This repo consists of tasks for Monk Commerce as mentioned in the [doc](https://drive.google.com/file/d/125I_3GmENCluttfHgynrHCvV7pe1JRan/view) here.

I mainly wanted to highlight that I am efficient and adaptable working with different tech stacks. That's the reason I have 2 implementations here in this repo. I have Java and Python implementations. Given the time I can do the same with Express too in either TS/JS. I have also worked with C/C++ for performance based tooling requirement and I am also learning Go recently. 

I believe this gives a good idea of my adaptabilty as per the requirement.

---
## Repo structure

Below is the list of major components in the repo.

1. `java_implementation`: Consists of Java implementation of APIs.
2. `python_implementation`: Consists of Python implemenation of APIs.
3. `utils`: Consists of code to generate dummy data for testing.
4. `Coupons.postman_collection.json`: Consists of Json data for postman requests.

---

## Tech Stack being used

#### SQLite DB:
I wanted to keep the db simple to use and test. So I am using SQLite. I believe this fits well for the current use case and I can anytime upgrade to other DBs like Postgres, MySQL etc.

#### Java Spring Boot:

I am using Java 17 and Spring Boot for writng the APIs and using Hibernate to do the DB transactions with SQLite.

#### Python FastAPI:

I am using python 3.11.5 with FastAPI for writing the APIs and using SQLAlchemy to do the DB transactions with SQLite.

#### Postman Collection for Test:

You can setup the Postman collection using the `Coupons.postman_collection.json` file to test the API calls after running the HTTP servers.

---
## Setting up the Projects

### Setting up the Java Project

Assuming you have already cloned the repo.

You can just go to `java_implementation` directory in your terminal and run the following command.

```shell
./mvnw spring-boot:run
```

This should run the server on `http://127.0.0.1:3000`. This also creates the `java_implementation/db/monk.db` DB file if not already present.

You can change the settings in `java_implementation/src/main/resources/application.properties` file.

### Setting up the Python Project

Assuming you have already cloned the repo.

You can just go to `python_implementation` directory in your terminal and install the dependencies.

You can create a virtual environment (recommended) or you can just run the following command.

```shell
pip install -r requirements.txt
```

You can move into `python_implemenation/app` and run the following command.
```shell
uvicorn main:app --port 3000
```

This should run the server on `http://127.0.0.1:3000`. This also creates the `python_implementation/app/monk.db` DB file if not already present.

### Running Utils

Assuming you have cloned the repo and already installed all the dependencies for python.

You can move to the `utils` directory and run the following command.
```shell
python create_dummy_data.py
```

This will take the contents from `utils/csv_dummy_data` csv files and create a SQLite DB file `utils/temp.db` which you can place in respective DB paths in the projects as mentioned below.

- java project: `java_implementation/db/monk.db`
- python project: `python_implementation/app/monk.db`

---

## DB Schema

Below is the list of tables created and used in the SQLite DB.
- coupons
    ```sql
    -- coupons definition

    CREATE TABLE coupons (
            coupon_id integer,
            coupon_code varchar(50) not null unique,
            coupon_name varchar(255),
            coupon_type varchar(50),
            discount_value float,
            end_date timestamp,
            start_date timestamp,
            status varchar(20) check (status in ('active','inactive','expired')),
            primary key (coupon_id)
    );
    ```
    Table example with dummy data:

    |coupon_id|coupon_code|coupon_name|coupon_type|discount_value|end_date|start_date|status|
    |---------|-----------|-----------|-----------|--------------|--------|----------|------|
    |1|DISCOUNT10|10% Off|percentage|10.0|2024-12-31 23:59:59.000000|2024-01-01 00:00:00.000000|active|
    |2|BLACKFRIDAY|Black Friday Sale|percentage|20.0|2024-11-25 23:59:59.000000|2024-11-25 00:00:00.000000|active|
    |3|SPRINGSALE|Summer Spring Sale|fixed|15.0|2024-05-31 23:59:59.000000|2024-03-01 00:00:00.000000|active|
    |4|XMAS20|Christmas Special|percentage|20.0|2023-12-31 23:59:59.000000|2023-12-01 00:00:00.000000|expired|
    |5|FREESHIP|Free Shipping|fixed|5.0|2024-12-31 23:59:59.000000|2024-06-01 00:00:00.000000|active|

- coupon_details
    ```sql
    -- coupon_details definition

    CREATE TABLE coupon_details (
        coupon_detail_id integer,
        detail_key varchar(100),
        detail_value varchar(255),
        coupon_id bigint not null,
        primary key (coupon_detail_id)
    );
    ```
    Table example with dummy data:
    
    |coupon_detail_id|detail_key|detail_value|coupon_id|
    |----------------|----------|------------|---------|
    |1|min_purchase|50.00|1|
    |2|category|clothing|1|
    |3|min_purchase|100.00|2|
    |4|category|electronics|2|
    |5|min_purchase|75.00|3|


- coupon_usage

    ```sql
    -- coupon_usage definition

    CREATE TABLE coupon_usage (
        usage_id integer,
        discount_applied float,
        used_at timestamp,
        coupon_id bigint not null,
        order_id bigint,
        user_id bigint,
        primary key (usage_id)
    );
    ```
    Table example with dummy data:

    |usage_id|discount_applied|used_at|coupon_id|order_id|user_id|
    |--------|----------------|-------|---------|--------|-------|
    |1|10.0|2024-01-05 12:30:00.000000|1|101|1|
    |2|20.0|2024-11-25 08:45:00.000000|2|102|2|
    |3|15.0|2024-03-10 14:00:00.000000|3|103|3|
    |4|5.0|2024-06-02 11:00:00.000000|5|104|4|
    |5|25.0|2025-01-01 09:00:00.000000|6|105|5|



- orders
    ```sql
    -- orders definition

    CREATE TABLE orders (
            order_id integer,
            coupon_ids varchar(255),
            order_time timestamp not null,
            product_ids varchar(255) not null,
            user_id bigint,
            primary key (order_id)
    );

    ```
    Table example with dummy data:

    |order_id|coupon_ids|order_time|product_ids|user_id|
    |--------|----------|----------|-----------|-------|
    |1|[3, 7]|2024-01-05 12:30:00.000000|[1, 3, 5]|1|
    |2|[8, 2, 6]|2024-11-25 08:45:00.000000|[2, 6, 8]|2|
    |3|[5, 10]|2024-03-10 14:00:00.000000|[3, 7]|3|
    |4|[1, 9]|2024-06-02 11:00:00.000000|[4, 9, 6]|4|
    |5|[9, 4]|2025-01-01 09:00:00.000000|[5, 10]|5|

- products

    ```sql
    -- products definition

    CREATE TABLE products (
        product_id integer,
        created_at timestamp not null,
        description varchar(500),
        name varchar(255) not null,
        price float not null,
        stock_quantity integer not null,
        updated_at timestamp,
        primary key (product_id)
    );
    ```
    Table example with dummy data:

    |product_id|created_at|description|name|price|stock_quantity|updated_at|
    |----------|----------|-----------|----|-----|--------------|----------|
    |1|2024-01-01 00:00:00.000000|Latest model with advanced features|Apple iPhone 15|999.99|50|2024-01-01 00:00:00.000000|
    |2|2024-02-15 00:00:00.000000|Flagship Android smartphone|Samsung Galaxy S24|899.99|40|2024-02-15 00:00:00.000000|
    |3|2024-03-01 00:00:00.000000|High performance laptop|Dell XPS 13|1299.99|30|2024-03-01 00:00:00.000000|
    |4|2024-04-10 00:00:00.000000|Professional business laptop|Lenovo ThinkPad|1099.99|20|2024-04-10 00:00:00.000000|
    |5|2024-05-05 00:00:00.000000|Apple laptop with M2 chip|MacBook Pro|2399.99|15|2024-05-05 00:00:00.000000|


- users
    ```sql
    -- users definition

    CREATE TABLE users (
        user_id integer,
        created_at timestamp not null,
        email varchar(255) not null unique,
        first_name varchar(100),
        last_name varchar(100),
        password_hash varchar(255) not null,
        username varchar(100) not null unique,
        primary key (user_id)
    );
    ```
    Table example with dummy data:

    |user_id|created_at|email|first_name|last_name|password_hash|username|
    |-------|----------|-----|----------|---------|-------------|--------|
    |1|2024-01-01 00:00:00.000000|john@example.com|John|Doe|$2b$12$uVuA4o03US7uF2WVpA2Xzmm9ZDhZl3a5oFzJkY5XxX93t9iL1.Gte|john_doe|
    |2|2024-02-15 00:00:00.000000|jane@example.com|Jane|Smith|$2b$12$EaP5nWzA4dL9WGvNUKHz9/q3zClXz1XmfQF9D2zB.6ugTn5t0FZna|jane_smith|
    |3|2024-03-01 00:00:00.000000|bob@example.com|Bob|Jones|$2b$12$1Xc/JQnpZB57/8Vm6efhFgTc3IhgaBYvsl8wK8pD5aZ1p2wD1htM6|bob_jones|
    |4|2024-04-10 00:00:00.000000|alice@example.com|Alice|Brown|$2b$12$Apv7dFQU04Q1YIT5AaljcMcTmHhv3gWztd9jDzxhOMXxWOC7zVE0x|alice_brown|
    |5|2024-05-05 00:00:00.000000|charlie@example.com|Charlie|Clark|$2b$12$Pi89Zmh6vwhhbZ5r9H8OBg0wozEv5YxQpKT1OHXJ1j9LfgPwwFjLa|charlie_clark|


---

## APIs

This mainly consists of the following APIs.

1. Index API:

    url: `http://127.0.0.1:3000/`
    
    Get request to the main page. Just gives you a hello message.

2. Get Coupons API:

    url: `http://127.0.0.1:3000/coupons/`

    Get request to get all the coupons.
    This gets data from `coupons` table.

3. Get Coupon By Id API:

    url: `http://127.0.0.1:3000/coupons/{id}`

    Get request to get coupon passing the id as a path variable.
    This gets data from `coupons` table based on `coupon_id`.

4. Save new Coupon API:

    url: `http://127.0.0.1:3000/coupons/`

    Post request to save the coupon in the DB by passing the JSON body along with the request.

    ```json
    // Example JSON body
    // cart-wise
    {
        "coupon_code" : "cart10",
        "coupon_name" : "10% cart discount",
        "coupon_type" : "cart-wise",
        "discount_value" : 10,
        "status" : "active",
        "start_date" : "2024-01-01 00:00:00.000000",
        "end_date" : "2024-12-31 23:59:59.000000",
        "details" :{
            "threshold":100
        }
    }
    ```

    `details` can be any form of valid Json object.
    This saves data to `coupons` table and `coupon_details` table.

5. Update Coupon By Id API:

    url: `http://127.0.0.1:3000/coupons/{id}`

    Put request to update the Coupon data in the DB by passing the JSON body along with the request.

    ```json
    // Example JSON body
    // cart-wise
    {
        "coupon_code" : "cart30",
        "coupon_name" : "30% cart discount",
        "coupon_type" : "cart-wise",
        "discount_value" : 30,
        "status" : "active",
        "start_date" : "2024-01-01 00:00:00.000000",
        "end_date" : "2024-12-31 23:59:59.000000",
        "details" :{
            "threshold":1500
        }
    }
    ```

    This updates data in `coupons` table and `coupon_details` table.

6. Delete Coupon By Id:

    url: `http://127.0.0.1:3000/coupons/{id}`

    Delete request to delete the Coupon data in DB based on the Coupon Id.

    This deletes data from `coupons` table and `coupon_details` table.

7. Apply Coupon By Id:

    url: `http://127.0.0.1:3000/coupons/apply-coupon/{id}`

    Post request to apply coupon usage based on coupon id and sends the required details by JSON body along with the requeset.

    ```json
    // example JSON body
    {
        "user_id": 1,
        "cart": {
            "items": [
                {
                    "product_id": 1,
                    "quantity": 6,
                    "price": 50
                },
                {
                    "product_id": 2,
                    "quantity": 3,
                    "price": 30
                },
                {
                    "product_id": 3,
                    "quantity": 2,
                    "price": 25
                }
            ]
        }
    }
    ```

    This updates records in `orders` table and `coupon_usage` table.

---

## Observations based on the current implementation

In this implementation for both Java and Python, I am assuming that user and product data will already be present. If required CRUD APIs can be addded too.

I believe with more time I could have added some more data validations and unit tests too. For production, I would have switched the DB to Postgres or some other DB based on requirement. I also would have written some test to stress test the DB calls and APIs before production roll outs. Along with this I would have also liked to create docker images for both individual projects for easy deployments.

I believe this is a good base for further enhnacements in terms of API additions or data model modifications for the given time.