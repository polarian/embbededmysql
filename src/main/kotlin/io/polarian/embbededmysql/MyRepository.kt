package io.polarian.embbededmysql

interface MyRepository {
    fun insert(myRecord: MyRecord)
    fun getMyRecordById(id: Int): MyRecord
}