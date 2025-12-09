package negocio.facade.jdbc.recursoshumanos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.FuncionarioDependenteVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.HistoricoDependentesVO;
import negocio.comuns.recursoshumanos.HistoricoDependentesVO.EnumCampoConsultaHistoricoDependentes;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.Conexao;
import negocio.interfaces.recursoshumanos.HistoricoDependentesInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>HistoricoDependentesVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>HistoricoDependentesVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class HistoricoDependentes extends SuperFacade<HistoricoDependentesVO> implements HistoricoDependentesInterfaceFacade<HistoricoDependentesVO> {

	private static final long serialVersionUID = 1L;

	protected static String idEntidade;

	public HistoricoDependentes() throws Exception {
		super();
		setIdEntidade("HistoricoDependentes");
	}

	public HistoricoDependentes(Conexao conexao, FacadeFactory facade) throws Exception {
		super();
		setConexao(conexao);
		setFacadeFactory(facade);
		setIdEntidade("HistoricoDependentes");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(HistoricoDependentesVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void incluir(HistoricoDependentesVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			HistoricoDependentes.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder();
					sql.append("INSERT INTO public.historicodependentes(funcionario, datamudanca, numerodependentesirrf, numerodependentessalariofamilia)");
					sql.append("VALUES (?, ?, ?, ?) returning codigo");
					sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());

					int i = 0;
					Uteis.setValuePreparedStatement(obj.getFuncionarioVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataMudanca(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNumeroDependentesIRRF(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNumeroDependentesSalarioFamilia(), ++i, sqlInserir);

					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(final ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Override
	public void alterar(HistoricoDependentesVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		HistoricoDependentes.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append(" UPDATE public.historicodependentes SET funcionario=?, datamudanca=?, numerodependentesirrf=?, numerodependentessalariofamilia=?");
				sql.append(" WHERE codigo = ?");
				sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getFuncionarioVO(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getDataMudanca(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getNumeroDependentesIRRF(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getNumeroDependentesSalarioFamilia(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
	}

	/**
	 * Exclui o {@link HistoricoDependentesVO} pelo codigo informado.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(HistoricoDependentesVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		HistoricoDependentes.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM HistoricoDependentes WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });

	}

	/**
	 * Consulta o {@link HistoricoDependentesVO} pelo  codigo informado.
	 */
	@Override
	public HistoricoDependentesVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	/**
	 * Valida os campos obrigatorios do {@link HistoricoDependentesVO}
	 */
	@Override
	public void validarDados(HistoricoDependentesVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getFuncionarioVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HistoricoDependentes_funcionario"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getDataMudanca())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HistoricoDependentes_dataMudanca"));
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception {
		dataModelo.getListaFiltros().clear();

		dataModelo.setListaConsulta(consultarHistoricoDependentes(dataModelo));
		dataModelo.setTotalRegistrosEncontrados(consultarTotalHistoricoDependentes(dataModelo));		
	}

	/**
	 * Consulta Paginada dos historicos dos dependentes retornando 10 registros.
	 * 
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private List<HistoricoDependentesVO> consultarHistoricoDependentes(DataModelo dataModelo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico());
		sql.append(" WHERE 1 = 1");
		dataModelo.setLimitePorPagina(10);
		dataModelo.setCampoConsulta("MATRICULA_FUNCIONARIO");

		switch (EnumCampoConsultaHistoricoDependentes.valueOf(dataModelo.getCampoConsulta())) {
		case CARGO:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);
			sql.append(" AND cargo.nome like UPPER(sem_acentos(?))");
			break;
		case FUNCIONARIO:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);
			sql.append(" AND pessoa.nome like UPPER(sem_acentos(?))");
			break;
		case MATRICULA_FUNCIONARIO:
			dataModelo.getListaFiltros().add(dataModelo.getValorConsulta());
			sql.append(" AND funcionario.matricula = ?");
			break;
		default:
			break;
		}

		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());

		return montarDadosLista(tabelaResultado);
	}

	/**
	 * Consulta o total de {@link HistoricoDependentesVO} de acordo com o filtro informado.
	 *  
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private Integer consultarTotalHistoricoDependentes(DataModelo dataModelo) throws Exception {
        StringBuilder sql = new StringBuilder(getSqlBasicoCount());
        sql.append(" WHERE 1 = 1");
        dataModelo.setCampoConsulta("MATRICULA_FUNCIONARIO");

        switch (EnumCampoConsultaHistoricoDependentes.valueOf(dataModelo.getCampoConsulta())) {
		case CARGO:
			sql.append(" AND cargo.nome like UPPER(sem_acentos(?))");
			break;
		case FUNCIONARIO:
			sql.append(" AND pessoa.nome like UPPER(sem_acentos(?))");
			break;
		case MATRICULA_FUNCIONARIO:
			sql.append(" AND funcionario.matricula = ?");
			break;
		default:
			break;
		}

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
    }

	/**
	 * Monta a lista de {@link HistoricoDependentesVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<HistoricoDependentesVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<HistoricoDependentesVO> tiposEmprestimos = new ArrayList<>();

        while(tabelaResultado.next()) {
        	tiposEmprestimos.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return tiposEmprestimos;
	}

	@Override
	public HistoricoDependentesVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		HistoricoDependentesVO obj = new HistoricoDependentesVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("funcionario"))) {			
			obj.setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(tabelaResultado.getInt("funcionario"), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null));
		}

		obj.setDataMudanca(tabelaResultado.getDate("datamudanca"));
		obj.setNumeroDependentesIRRF(tabelaResultado.getInt("numerodependentesirrf"));
		obj.setNumeroDependentesSalarioFamilia(tabelaResultado.getInt("numerodependentessalariofamilia"));

		return obj;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT hist.codigo, hist.funcionario, pessoa.nome , hist.dataMudanca, hist.numerodependentesirrf, hist.numerodependentessalariofamilia FROM historicodependentes hist");
		sql.append(" INNER JOIN funcionario ON funcionario.codigo = hist.funcionario");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");

		return sql.toString();
	}

	private String getSqlBasicoCount() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(hist.codigo) as qtde FROM historicodependentes hist");
		sql.append(" INNER JOIN funcionario ON funcionario.codigo = hist.funcionario");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");

		return sql.toString();
	}

	@Override
	public void persistirTodos(FuncionarioVO funcionarioVO, List<FuncionarioDependenteVO> dependenteVOs, UsuarioVO usuario) throws Exception {

		HistoricoDependentesVO historicoDependentesVO = montarDadosHistoricosPorDependentes(funcionarioVO, dependenteVOs);
		this.persistir(historicoDependentesVO, false, usuario);
	
	}

	private HistoricoDependentesVO montarDadosHistoricosPorDependentes(FuncionarioVO funcionarioVO, List<FuncionarioDependenteVO> dependenteVOs) {
		HistoricoDependentesVO obj = new HistoricoDependentesVO();
		obj.setFuncionarioVO(funcionarioVO);
		obj.setDataMudanca(new Date());
		obj.setNumeroDependentesIRRF((int) dependenteVOs.stream().filter(p -> p.getInss() == Boolean.TRUE).count());
		obj.setNumeroDependentesSalarioFamilia( (int) dependenteVOs.stream().filter(p -> p.getSalarioFamilia() == Boolean.TRUE).count());

		return obj;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		HistoricoDependentes.idEntidade = idEntidade;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarFuncionarioHistoricoDependentesUnificacaoFuncionario(Integer funcionarioAntigo, Integer funcionarioNovo) throws Exception {
		String sqlStr = "UPDATE historicodependentes set funcionario=? WHERE ((funcionario = ?))";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { funcionarioNovo, funcionarioAntigo });
	}
}
