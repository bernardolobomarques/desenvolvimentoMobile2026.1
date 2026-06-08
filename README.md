# MetaQuest 🎯

App Android gamificado de metas pessoais — AP2 Desenvolvimento Mobile IBMEC RJ

## Proposta

O MetaQuest transforma o acompanhamento de metas pessoais em uma experiência gamificada. O usuário cria metas, acompanha o progresso, conclui desafios e acumula XP para subir de nível — tudo integrado a uma API REST com persistência em PostgreSQL.

## Tecnologias

| Camada | Tecnologia |
|---|---|
| App Android | Kotlin + XML + Material Design 3 |
| HTTP Client | Retrofit 2 + Coroutines |
| Backend | FastAPI (Python 3.11) |
| ORM | SQLAlchemy 2 + Alembic |
| Banco de dados | PostgreSQL 15 |
| Deploy | GCP Cloud Run + Cloud SQL |
| Docs API | Swagger UI em `/docs` |

## Estrutura do Repositório

```
/
├── ap2-back/       ← API FastAPI + PostgreSQL
├── ap2-front/      ← App Android Kotlin
└── .github/workflows/
```

## Como Executar

### Backend (local com Docker)

```bash
cd ap2-back
cp .env.example .env
docker-compose up
```

API disponível em `http://localhost:8000` | Swagger em `http://localhost:8000/docs`

### Backend (sem Docker)

```bash
cd ap2-back
pip install -r requirements.txt
# Criar banco PostgreSQL e configurar .env
uvicorn main:app --reload
```

### Android

1. Abrir a pasta `ap2-front/` no Android Studio
2. Criar `ap2-front/local.properties` com:
   ```
   API_BASE_URL=http://10.0.2.2:8000/
   ```
   Para produção (Cloud Run), substituir pelo URL do serviço.
3. Run no emulador ou dispositivo físico

## API

### Endpoints principais

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/metas` | Criar meta |
| `GET` | `/metas` | Listar metas |
| `GET` | `/metas/{id}` | Buscar meta |
| `PUT` | `/metas/{id}` | Editar meta |
| `PATCH` | `/metas/{id}/progresso` | Atualizar progresso |
| `PATCH` | `/metas/{id}/concluir` | Concluir meta (+20 XP) |
| `DELETE` | `/metas/{id}` | Excluir meta |
| `GET` | `/gamificacao/resumo` | XP, nível, streak, badges |

### Swagger

- **Local:** `http://localhost:8000/docs`
- **Cloud Run:** `https://<service>.run.app/docs`

## Deploy GCP Cloud Run

```bash
# 1. Criar instância Cloud SQL PostgreSQL 15: metaquest-db
# 2. Armazenar DATABASE_URL no Secret Manager
# 3. Build e deploy:
gcloud run deploy metaquest-api \
  --source ./ap2-back \
  --add-cloudsql-instances PROJECT_ID:REGION:metaquest-db \
  --set-secrets DATABASE_URL=metaquest-db-url:latest \
  --allow-unauthenticated \
  --region us-central1
```

## Telas do App

| Tela | Descrição |
|---|---|
| MainActivity | Dashboard com XP, nível, streak e navegação |
| GoalsListActivity | Lista de metas com RecyclerView + CardView |
| CreateGoalActivity | Formulário de criação/edição de meta |
| WizardActivity + GoalWizardFragment | Assistente de metas em 4 etapas |
| ResultActivity | Resultado do wizard + intent implícita de compartilhamento |

## Screenshots

> _Adicionar prints após execução no emulador_

## Critérios Atendidos (AP2)

| Critério | Status |
|---|---|
| 5+ telas (Activities/Fragments) | ✅ 5 telas + 1 Fragment |
| Fragment funcional | ✅ GoalWizardFragment |
| Intents explícitas | ✅ Todas as navegações |
| Intent implícita | ✅ ACTION_SEND em ResultActivity |
| 6+ componentes gráficos | ✅ TextView, EditText, Button, Spinner, Switch, RecyclerView, ProgressBar, CardView, ImageView, RadioButton |
| API REST + Swagger | ✅ FastAPI `/docs` |
| Banco relacional | ✅ PostgreSQL via SQLAlchemy |
| CRUD completo | ✅ 7 endpoints em /metas |
| **Bônus** deploy cloud | ✅ GCP Cloud Run | 
