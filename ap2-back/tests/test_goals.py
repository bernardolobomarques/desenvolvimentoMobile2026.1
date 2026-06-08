def test_create_and_list_goal(client):
    response = client.post("/metas", json={
        "titulo": "Estudar Calculo",
        "prioridade": "alta",
        "xp": 20,
        "origem": "manual",
    })
    assert response.status_code == 201
    data = response.json()
    assert data["titulo"] == "Estudar Calculo"
    assert data["concluida"] is False

    list_resp = client.get("/metas")
    assert list_resp.status_code == 200
    assert len(list_resp.json()) == 1


def test_conclude_goal_awards_xp(client):
    goal_id = client.post("/metas", json={"titulo": "Meta X", "prioridade": "alta"}).json()["id"]
    client.patch(f"/metas/{goal_id}/concluir")

    summary = client.get("/gamificacao/resumo").json()
    assert summary["xp_total"] > 0
    assert "primeira_conclusao" in summary["badges"]
    assert "meta_alta_prioridade" in summary["badges"]


def test_update_progress(client):
    goal_id = client.post("/metas", json={"titulo": "Meta Y"}).json()["id"]
    resp = client.patch(f"/metas/{goal_id}/progresso", json={"progresso": 50})
    assert resp.status_code == 200
    assert resp.json()["progresso"] == 50


def test_delete_goal(client):
    goal_id = client.post("/metas", json={"titulo": "Meta Z"}).json()["id"]
    del_resp = client.delete(f"/metas/{goal_id}")
    assert del_resp.status_code == 204
    assert client.get(f"/metas/{goal_id}").status_code == 404


def test_gamification_summary(client):
    resp = client.get("/gamificacao/resumo")
    assert resp.status_code == 200
    body = resp.json()
    assert "xp_total" in body
    assert "nivel" in body
    assert "streak" in body
    assert "badges" in body
