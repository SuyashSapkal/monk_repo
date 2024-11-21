from fastapi import APIRouter, status
from fastapi.responses import JSONResponse


router = APIRouter()


@router.get("/")
def get_coupons():
    coupons = ["cart-wise", "product-wise", "bxgy"]
    return JSONResponse(content={"coupons": coupons}, status_code=status.HTTP_200_OK)


@router.post("/")
def create_coupons():
    return JSONResponse({"create_coupons": "successful"}, status_code=status.HTTP_200_OK)
