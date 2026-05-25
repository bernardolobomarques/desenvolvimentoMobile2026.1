from sqlalchemy.orm import Session

from .. import models, schemas


def create_device(db: Session, data: schemas.DeviceCreate) -> models.Device:
    device = models.Device(**data.model_dump())
    db.add(device)
    db.commit()
    db.refresh(device)
    return device


def list_devices(db: Session) -> list[models.Device]:
    return db.query(models.Device).all()


def get_device(db: Session, device_id: int) -> models.Device | None:
    return db.query(models.Device).filter(models.Device.id == device_id).first()


def update_device(
    db: Session, device: models.Device, data: schemas.DeviceUpdate
) -> models.Device:
    for key, value in data.model_dump().items():
        setattr(device, key, value)
    db.commit()
    db.refresh(device)
    return device


def delete_device(db: Session, device: models.Device) -> None:
    db.delete(device)
    db.commit()
