package com.mak.feastit.domain.model

data class Instruction(
        val stepNo: String,
        val stepDesc: String
) {
    fun isSameAs(item: Instruction): Boolean {
        return this.stepNo == item.stepNo
    }
}