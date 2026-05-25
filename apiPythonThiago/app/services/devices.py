from fastapi import HTTPException, status
from sqlalchemy.orm import Session

from .. import schemas
from ..repositories import devices as repo


def create_device(db: Session, data: schemas.DeviceCreate):
    return repo.create_device(db, data)


def list_devices(db: Session):
    return repo.list_devices(db)


def get_device(db: Session, device_id: int):
    device = repo.get_device(db, device_id)
    if device is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Device not found")
    return device


def update_device(db: Session, device_id: int, data: schemas.DeviceUpdate):
    device = repo.get_device(db, device_id)
    if device is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Device not found")
    return repo.update_device(db, device, data)


def delete_device(db: Session, device_id: int):
    device = repo.get_device(db, device_id)
    if device is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Device not found")
    repo.delete_device(db, device)
