from pydantic import BaseModel, Field


class DeviceBase(BaseModel):
    name: str = Field(min_length=1, max_length=100)
    type: str = Field(min_length=1, max_length=50)
    is_active: bool


class DeviceCreate(DeviceBase):
    pass


class DeviceUpdate(DeviceBase):
    pass


class DeviceResponse(DeviceBase):
    id: int

    class Config:
        from_attributes = True
