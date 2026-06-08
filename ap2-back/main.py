from contextlib import asynccontextmanager
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.database import Base, engine
from app.routers import goals, gamification


@asynccontextmanager
async def lifespan(app: FastAPI):
    Base.metadata.create_all(bind=engine)
    yield


app = FastAPI(
    title="MetaQuest API",
    description="API REST para o app MetaQuest — metas pessoais gamificadas",
    version="1.0.0",
    lifespan=lifespan,
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=False,
    allow_methods=["*"],
    allow_headers=["*"],
)


app.include_router(goals.router)
app.include_router(gamification.router)


@app.get("/", tags=["Health"])
def health():
    return {"status": "ok", "docs": "/docs"}
