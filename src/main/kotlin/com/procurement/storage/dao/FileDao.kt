package com.procurement.storage.dao

import com.datastax.driver.core.Session
import com.datastax.driver.core.querybuilder.QueryBuilder.*
import com.procurement.storage.model.entity.FileEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class FileDao(private val session: Session) {

    fun getOneById(fileId: String): FileEntity? {
        val query = select()
                .from(FILES_TABLE)
                .where(eq(ID, fileId))
                .limit(1)
        val row = session.execute(query).one()
        return if (row != null) FileEntity(
                id = row.getString(ID),
                isOpen = row.getBool(IS_OPEN),
                dateModified = row.getTimestamp(MODIFIED),
                datePublished = row.getTimestamp(PUBLISHED),
                hash = row.getString(HASH),
                weight = row.getLong(WEIGHT),
                fileName = row.getString(NAME),
                fileOnServer = row.getString(ON_SERVER),
                owner = row.getString(OWNER)) else null
    }

    fun getAllByIds(fileIds: Set<String>): List<FileEntity> {
        val query = select()
                .all()
                .from(FILES_TABLE)
                .where(`in`(ID, *fileIds.toTypedArray()))
        val resultSet = session.execute(query)
        val entities = ArrayList<FileEntity>()
        if (resultSet.isFullyFetched)
            resultSet.forEach { row ->
                entities.add(FileEntity(
                        id = row.getString(ID),
                        isOpen = row.getBool(IS_OPEN),
                        dateModified = row.getTimestamp(MODIFIED),
                        datePublished = row.getTimestamp(PUBLISHED),
                        hash = row.getString(HASH),
                        weight = row.getLong(WEIGHT),
                        fileName = row.getString(NAME),
                        fileOnServer = row.getString(ON_SERVER),
                        owner = row.getString(OWNER)))
            }
        return entities
    }

    fun save(entity: FileEntity): FileEntity {
        val insert = insertInto(FILES_TABLE)
                .value(ID, entity.id)
                .value(IS_OPEN, entity.isOpen)
                .value(MODIFIED, entity.dateModified)
                .value(PUBLISHED, entity.datePublished)
                .value(HASH, entity.hash)
                .value(WEIGHT, entity.weight)
                .value(NAME, entity.fileName)
                .value(ON_SERVER, entity.fileOnServer)
                .value(OWNER, entity.owner)
        session.execute(insert)
        return entity
    }

    companion object {
        private const val FILES_TABLE = "storage_files"
        private const val ID = "file_id"
        private const val IS_OPEN = "file_is_open"
        private const val MODIFIED = "date_modified"
        private const val PUBLISHED = "date_published"
        private const val HASH = "file_hash"
        private const val WEIGHT = "file_weight"
        private const val NAME = "file_name"
        private const val ON_SERVER = "file_on_server"
        private const val OWNER = "file_owner"
    }
}
