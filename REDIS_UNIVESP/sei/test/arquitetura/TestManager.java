package arquitetura;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.Conexao;

public class TestManager extends ControleAcesso {

	private static final long serialVersionUID = 2396060218809233212L;

	private static final String DRIVER_CLASS_NAME = "org.postgresql.Driver";
	private static final String URL = "jdbc:postgresql://localhost:5432/SEIBD-unirg-02-07-2020";
	private static final String USER_NAME = "postgres";
	private static final String PASSWORD = "admin";

	public TestManager() {
		super();
		setFacadeFactory(new FacadeFactory());
		super.setConexao(getConexao());
	}

	private static Conexao conexao;
	private static FacadeFactoryTest facadeFactoryTest;

	private static Conexao criarClasseDeTeste() {
		conexao = new Conexao();
		JdbcTemplate template = new JdbcTemplate();
		template.setDataSource(getDataSource());
		
		conexao.setJdbcTemplate(template);

		return conexao;
	}

	public void setConexao(Conexao conexao) {
		TestManager.conexao = conexao;
	}

	public static Conexao getConexao() {
		if (conexao == null)
			conexao = criarClasseDeTeste();
		return conexao;
	}

	public static DriverManagerDataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(DRIVER_CLASS_NAME);
		dataSource.setUrl(URL);
		dataSource.setUsername(USER_NAME);
		dataSource.setPassword(PASSWORD);
		return dataSource;
	}

	public static FacadeFactory getFacadeFactoryTest() {
		if (facadeFactoryTest == null) {
			facadeFactoryTest = new FacadeFactoryTest(getConexao());
		}
		return facadeFactoryTest.getFacadeFactory();
	}
	
	public static void  setFacadeFactoryTest(FacadeFactoryTest facadeFactoryNovo) {
		facadeFactoryTest = facadeFactoryNovo;
	}

}