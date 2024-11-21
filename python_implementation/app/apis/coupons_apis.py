import json
from fastapi import APIRouter, status, Request
from fastapi.responses import JSONResponse
from services.coupons_services import CouponService

router = APIRouter()


@router.get("/")
def get_coupons():
    try:
        cs = CouponService()
        data = cs.get_coupons_data()
        return JSONResponse(content={"coupons": (data)}, status_code=status.HTTP_200_OK)
    except Exception as e:
        return JSONResponse(
            content={"error": f"{e}"}, status_code=status.HTTP_500_INTERNAL_SERVER_ERROR
        )


@router.post("/")
async def create_coupons(request: Request):
    try:
        data = await request.json()
        cs = CouponService()
        coupon, coupon_details = cs.create_coupons(data)
        return JSONResponse(
            {
                "create_coupons": {
                    "coupon": f"{coupon}",
                    "coupon_details": f"{coupon_details}",
                }
            },
            status_code=status.HTTP_200_OK,
        )
    except Exception as e:
        return JSONResponse(
            content={"error": f"{e}"}, status_code=status.HTTP_500_INTERNAL_SERVER_ERROR
        )


@router.get("/{id}")
def get_coupon_id(id: int):
    try:
        cs = CouponService()
        data = cs.get_coupons_id(id)
        return JSONResponse(content={"coupons": data}, status_code=status.HTTP_200_OK)
    except Exception as e:
        return JSONResponse(
            content={"error": f"{e}"}, status_code=status.HTTP_500_INTERNAL_SERVER_ERROR
        )


@router.put("/{id}")
async def update_coupon(request: Request, id: int):
    try:
        data = await request.json()
        cs = CouponService()
        coupon, coupon_details = cs.update_coupon(data, id)
        return JSONResponse(
            {
                "update_coupons": {
                    "coupon": f"{coupon}",
                    "coupon_details": f"{coupon_details}",
                }
            },
            status_code=status.HTTP_200_OK,
        )
    except Exception as e:
        return JSONResponse(
            content={"error": f"{e}"}, status_code=status.HTTP_500_INTERNAL_SERVER_ERROR
        )


@router.delete("/{id}")
def delete_coupon(id: int):
    try:
        cs = CouponService()
        data = cs.delete_coupon(id)
        if data == True:
            return JSONResponse(
                {"msg": "deleted successfully"},
                status_code=status.HTTP_200_OK,
            )
        else:
            raise Exception(e)
    except Exception as e:
        return JSONResponse(
            content={"error": f"{e}"}, status_code=status.HTTP_500_INTERNAL_SERVER_ERROR
        )


@router.post("/apply-coupon/{id}")
async def apply_coupon(id: int, request: Request):
    try:
        data = await request.json()
        cs = CouponService()
        info = cs.apply_coupon(id, data)
        if info == True:
            return JSONResponse(
                {"msg": "applied coupon successfully"},
                status_code=status.HTTP_200_OK,
            )
        else:
            raise Exception(e)
    except Exception as e:
        return JSONResponse(
            content={"error": f"{e}"}, status_code=status.HTTP_500_INTERNAL_SERVER_ERROR
        )
