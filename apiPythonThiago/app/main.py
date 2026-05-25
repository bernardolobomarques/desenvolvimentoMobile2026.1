from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from .database import init_database
from .routers import devices

app = FastAPI(title="EnergyMonitor API")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=False,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.on_event("startup")
def on_startup():
    init_database()


app.include_router(devices.router)
