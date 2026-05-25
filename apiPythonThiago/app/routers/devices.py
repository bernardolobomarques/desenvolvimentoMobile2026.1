from fastapi import APIRouter, Depends, Response, status
from sqlalchemy.orm import Session

from ..database import get_db
from .. import schemas
from ..services import devices as service

router = APIRouter(prefix="/devices", tags=["devices"])


@router.post("/", response_model=schemas.DeviceResponse, status_code=status.HTTP_201_CREATED)
def create_device(payload: schemas.DeviceCreate, db: Session = Depends(get_db)):
    return service.create_device(db, payload)


@router.get("/", response_model=list[schemas.DeviceResponse])
def list_devices(db: Session = Depends(get_db)):
    return service.list_devices(db)


@router.get("/{device_id}", response_model=schemas.DeviceResponse)
def get_device(device_id: int, db: Session = Depends(get_db)):
    return service.get_device(db, device_id)


@router.put("/{device_id}", response_model=schemas.DeviceResponse)
def update_device(
    device_id: int, payload: schemas.DeviceUpdate, db: Session = Depends(get_db)
):
    return service.update_device(db, device_id, payload)


@router.delete("/{device_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_device(device_id: int, db: Session = Depends(get_db)):
    service.delete_device(db, device_id)
    return Response(status_code=status.HTTP_204_NO_CONTENT)
