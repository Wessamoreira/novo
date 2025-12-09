package controle.arquitetura;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;



@SpringBootApplication
@ComponentScan(basePackages = {"controle", "integracoes", "jobs", "jobs2", "negocio", "webservice", "relatorio", "sms", "webservice"})
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class SeiApplication {
	
	@Value("${spring.datasource.url}")
	private String jdbcUrl;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	@Value("${spring.datasource.maximumPoolSize}")
	private Integer maximumPoolSize;
	@Value("${spring.datasource.minimumidle}")
	private Integer minPoolSize;
	@Value("${spring.datasource.connectionTimeout}")
	private Integer connectionTimeout;

	public static void main(String[] args) {
		SpringApplication.run(SeiApplication.class, args);
	}


	@Bean
	public DataSource dataSouce() {

		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(jdbcUrl);
		config.setUsername(username);
		config.setPassword(password);
		config.setDriverClassName("org.postgresql.Driver");
		config.setPoolName("dataSource_sei");
		config.setAutoCommit(false);

		// Equivalente ao minPoolSize
		config.setMinimumIdle(minPoolSize);

		// Equivalente ao maxPoolSize
		config.setMaximumPoolSize(maximumPoolSize);

		// Configurações adicionais
		config.setConnectionTimeout(connectionTimeout);

		// Equivalente ao acquireIncrement
		// HikariCP não tem um equivalente direto, mas você pode ajustar o maximumPoolSize

		// Equivalente ao maxIdleTime (em milissegundos)
		config.setIdleTimeout(500000);

		// Equivalente ao maxStatements
		// HikariCP não tem cache de PreparedStatement, mas você pode habilitar o cache JDBC
		config.addDataSourceProperty("dataSource.cachePrepStmts", "true");
		config.addDataSourceProperty("dataSource.prepStmtCacheSize", "250");
		config.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048");

		// Equivalente ao initialPoolSize
		// HikariCP não tem este conceito, ele cria conexões conforme necessário
		// Equivalente ao idleConnectionTestPeriod (em milissegundos)
		config.setKeepaliveTime(120000);

		// Equivalente ao debugUnreturnedConnectionStackTraces
		// HikariCP não tem um equivalente direto, mas você pode habilitar o leak detection
		config.setLeakDetectionThreshold(60000); // 60 segundos
		return new HikariDataSource(config);
	}

	
	@Bean
	public JdbcTemplate jdbcTemplate() {
		JdbcTemplate template = new JdbcTemplate(dataSouce());		
		return template;
	}
	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSouce());		
		return template;
	}
}
