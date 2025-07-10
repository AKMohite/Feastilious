package com.mak.feastit.data.mapper

internal abstract class BaseMapper<Json, Entity, Model> {

    open fun jsonToEntity(json: Json): Entity {
        throw Error("jsonToEntity is not implemented within ${this::class.simpleName}")
    }

    open fun entityToModel(entity: Entity): Model {
        throw Error("entityToModel is not implemented within ${this::class.simpleName}")
    }

    open fun modelToEntity(model: Model): Entity {
        throw Error("modelToEntity is not implemented within ${this::class.simpleName}")
    }

    open fun modelToJson(model: Model): Json {
        throw Error("modelToJson is not implemented within ${this::class.simpleName}")
    }

    open fun jsonToModel(json: Json): Model {
        throw Error("jsonToModel is not implemented within ${this::class.simpleName}")
    }

    fun jsonToEntities(json: List<Json>) = json.map(this::jsonToEntity)

    fun jsonToModels(json: List<Json>) = json.map(this::jsonToModel)

    fun entitiesToModels(entities: List<Entity>) = entities.map(this::entityToModel)

    fun modelsToEntities(models: List<Model>) = models.map(this::modelToEntity)
}
