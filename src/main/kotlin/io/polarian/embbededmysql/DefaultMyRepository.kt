package io.polarian.embbededmysql

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject
import org.springframework.stereotype.Repository

@Repository
class DefaultMyRepository(val jdbcTemplate: JdbcTemplate) : MyRepository {
    override fun insert(myRecord: MyRecord) {
        //language=sql
        jdbcTemplate.update("INSERT INTO MY_RECORD VALUES(?, ?)", myRecord.id, myRecord.data)
    }

    override fun getMyRecordById(id: Int): MyRecord {
        //language=sql
        return jdbcTemplate.queryForObject("SELECT id, data FROM MY_RECORD WHERE id=?", id) { rs, _ ->
            MyRecord(rs.getInt(1), rs.getString(2))
        }!!
    }
}