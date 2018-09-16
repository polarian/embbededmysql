package io.polarian.embbededmysql


import com.wix.mysql.EmbeddedMysql.anEmbeddedMysql
import com.wix.mysql.ScriptResolver.classPathScript
import com.wix.mysql.config.Charset.UTF8
import com.wix.mysql.config.MysqldConfig.aMysqldConfig
import com.wix.mysql.distribution.Version
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource


@RunWith(MockitoJUnitRunner::class)
class DefaultMyRepositoryTest {
    private val mysqlConfig = aMysqldConfig(Version.v5_7_latest)
            .withCharset(UTF8)
            .withPort(3306)
            .withUser("myuser", "mypass")
            .withTimeZone("UTC")
            .withServerVariable("max_connect_errors", 500)
            .build()

    private val mysqld = anEmbeddedMysql(mysqlConfig)
            .addSchema("mydb", classPathScript("db/001_init.sql"))
            .start()

    private val dataSource = DriverManagerDataSource().apply {
        setDriverClassName("com.mysql.jdbc.Driver")
        url = "jdbc:mysql://localhost:3306/mydb"
        username = "myuser"
        password = "mypass"
    }

    private lateinit var jdbcTemplate: JdbcTemplate

    private lateinit var repository: DefaultMyRepository

    @Before
    fun setUp() {
        jdbcTemplate = JdbcTemplate(dataSource)
        repository = DefaultMyRepository(jdbcTemplate)
    }

    @Test
    fun test_insertAndGet() {
        val myRecord = MyRecord(1, "this is my first record")


        repository.insert(myRecord)


        val myFirstRecord = repository.getMyRecordById(1)
        assertThat(myFirstRecord).isEqualTo(myRecord)
    }
}
