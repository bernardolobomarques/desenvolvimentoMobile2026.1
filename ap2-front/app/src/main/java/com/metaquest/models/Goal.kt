package com.metaquest.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Goal(
    val id: Long? = null,
    val titulo: String = "",
    val descricao: String? = null,
    val categoria: String? = null,
    val prioridade: String? = null,
    val prazo: String? = null,
    val concluida: Boolean = false,
    val progresso: Int = 0,
    val xp: Int = 10,
    val origem: String = "manual",
    @SerializedName("criada_em") val criadaEm: String? = null
) : Parcelable
