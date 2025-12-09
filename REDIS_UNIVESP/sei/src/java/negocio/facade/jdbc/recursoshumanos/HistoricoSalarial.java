package negocio.facade.jdbc.recursoshumanos;

import java.math.BigDecimal;
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
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.HistoricoSalarialVO;
import negocio.comuns.recursoshumanos.HistoricoSalarialVO.EnumCampoConsultaHistoricoSalarial;
import negocio.comuns.recursoshumanos.enumeradores.MotivoMudancaCargoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.Conexao;
import negocio.interfaces.recursoshumanos.HistoricoSalarialInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>HistoricoSalarialVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>HistoricoSalarialVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class HistoricoSalarial extends SuperFacade<HistoricoSalarialVO> implements HistoricoSalarialInterfaceFacade<HistoricoSalarialVO> {

	private static final long serialVersionUID = 1L;

	protected static String idEntidade;

	public HistoricoSalarial() throws Exception {
		super();
		setIdEntidade("HistoricoSalarial");
	}

	public HistoricoSalarial(Conexao conexao, FacadeFactory facade) throws Exception {
		super();
		setConexao(conexao);
		setFacadeFactory(facade);
		setIdEntidade("HistoricoSalarial");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(HistoricoSalarialVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void incluir(HistoricoSalarialVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			HistoricoSalarial.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder();
					sql.append(" INSERT INTO public.historicosalarial(funcionariocargo, datamudanca, jornada, salario, motivomudanca, percentualvariacaosalarial, salariohora)");
					sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?) returning codigo");
					sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());

					int i = 0;
					Uteis.setValuePreparedStatement(obj.getFuncionarioCargo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataMudanca(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getJornada(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSalario(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getMotivoMudanca(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPercentualVariacaoSalarial(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSalarioHora(), ++i, sqlInserir);

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
	public void alterar(HistoricoSalarialVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		HistoricoSalarial.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append(" UPDATE public.historicosalarial SET funcionariocargo=?, datamudanca=?, jornada=?, salario=?, motivomudanca=?, percentualvariacaosalarial=?, salariohora=?");
				sql.append(" WHERE codigo = ?");
				sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getFuncionarioCargo(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getDataMudanca(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getJornada(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getSalario(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getMotivoMudanca(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getPercentualVariacaoSalarial(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getSalarioHora(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
	}

	/**
	 * Exclui o {@link HistoricoSalarialVO} pelo codigo informado.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(HistoricoSalarialVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		HistoricoSalarial.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM HistoricoSalarial WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });

	}

	/**
	 * Consulta o {@link HistoricoSalarialVO} pelo  codigo informado.
	 */
	@Override
	public HistoricoSalarialVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	/**
	 * Valida os campos obrigatorios do {@link HistoricoSalarialVO}
	 */
	@Override
	public void validarDados(HistoricoSalarialVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getFuncionarioCargo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HistoricoSalarial_funcionarioCargo"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getDataMudanca())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HistoricoSalarial_dataMudanca"));
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception {
		dataModelo.getListaFiltros().clear();

		dataModelo.setListaConsulta(consultarHistoricoSalarial(dataModelo));
		dataModelo.setTotalRegistrosEncontrados(consultarTotalHistoricoSalarial(dataModelo));		
	}

	/**
	 * Consulta Paginada dos historicos dos dependentes retornando 10 registros.
	 * 
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private List<HistoricoSalarialVO> consultarHistoricoSalarial(DataModelo dataModelo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico());
		sql.append(" WHERE 1 = 1");
		dataModelo.setLimitePorPagina(10);

		switch (EnumCampoConsultaHistoricoSalarial.valueOf(dataModelo.getCampoConsulta())) {
		case CARGO:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);
			sql.append(" AND cargo.nome like UPPER(sem_acentos(?))");
			break;
		case FUNCIONARIO:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);
			sql.append(" AND pessoa.nome like UPPER(sem_acentos(?))");
			break;
		case MATRICULA_CARGO:
			dataModelo.getListaFiltros().add(dataModelo.getValorConsulta());
			sql.append(" AND fc.matriculacargo = ?");
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
	 * Consulta o total de {@link HistoricoSalarialVO} de acordo com o filtro informado.
	 *  
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private Integer consultarTotalHistoricoSalarial(DataModelo dataModelo) throws Exception {
        StringBuilder sql = new StringBuilder(getSqlBasicoCount());
        sql.append(" WHERE 1 = 1");

        switch (EnumCampoConsultaHistoricoSalarial.valueOf(dataModelo.getCampoConsulta())) {
		case CARGO:
			sql.append(" AND cargo.nome like UPPER(sem_acentos(?))");
			break;
		case FUNCIONARIO:
			sql.append(" AND pessoa.nome like UPPER(sem_acentos(?))");
			break;
		case MATRICULA_CARGO:
			sql.append(" AND fc.matriculacargo = ?");
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
	 * Monta a lista de {@link HistoricoSalarialVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<HistoricoSalarialVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<HistoricoSalarialVO> tiposEmprestimos = new ArrayList<>();

        while(tabelaResultado.next()) {
        	tiposEmprestimos.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return tiposEmprestimos;
	}

	@Override
	public HistoricoSalarialVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		HistoricoSalarialVO obj = new HistoricoSalarialVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("funcionariocargo"))) {			
			obj.setFuncionarioCargo(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("funcionariocargo"), Uteis.NIVELMONTARDADOS_COMBOBOX, null));
		}

		obj.setDataMudanca(tabelaResultado.getDate("datamudanca"));
		obj.setJornada(tabelaResultado.getInt("jornada"));
		obj.setSalario(tabelaResultado.getBigDecimal("salario"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("motivoMudanca"))) {
			obj.setMotivoMudanca(MotivoMudancaCargoEnum.valueOf(tabelaResultado.getString("motivoMudanca")));
		}
		obj.setPercentualVariacaoSalarial(tabelaResultado.getBigDecimal("percentualvariacaosalarial"));
		obj.setSalarioHora(tabelaResultado.getBigDecimal("salariohora"));

		return obj;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT hist.codigo, hist.funcionariocargo, pessoa.nome, fc.matriculacargo , hist.dataMudanca, hist.jornada,");
		sql.append(" hist.salario, hist.motivoMudanca, hist.percentualvariacaosalarial, hist.salariohora FROM HistoricoSalarial hist");
		sql.append(" INNER JOIN funcionariocargo fc ON fc.codigo = hist.funcionariocargo");
		sql.append(" INNER JOIN cargo ON cargo.codigo = fc.cargo");
		sql.append(" INNER JOIN funcionario ON funcionario.codigo = fc.funcionario");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");

		return sql.toString();
	}

	private String getSqlBasicoCount() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(hist.codigo) as qtde FROM HistoricoSalarial hist");
		sql.append(" INNER JOIN funcionariocargo fc ON fc.codigo = hist.funcionariocargo");
		sql.append(" INNER JOIN cargo ON cargo.codigo = fc.cargo");
		sql.append(" INNER JOIN funcionario ON funcionario.codigo = fc.funcionario");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");

		return sql.toString();
	}

	@Override
	public void persistirPorFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO, boolean validarAcesso) throws Exception {
		if (Uteis.isAtributoPreenchido(funcionarioCargoVO.getSalario()) && funcionarioCargoVO.getSalario() != BigDecimal.ZERO 
				&& funcionarioCargoVO.getHistoricoSalarialVO().getGerarHistorico()) {
			HistoricoSalarialVO obj = this.montarDadosPorFuncionarioCargo(funcionarioCargoVO);
			persistir(obj, validarAcesso, null);
		}
	}

	private HistoricoSalarialVO montarDadosPorFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO) {
		HistoricoSalarialVO obj = new HistoricoSalarialVO(); 
		obj.setFuncionarioCargo(funcionarioCargoVO);
		BigDecimal percentual = BigDecimal.ZERO;
		
		if (funcionarioCargoVO.getHistoricoSalarialVO().getSalario() != BigDecimal.ZERO) {
			percentual = funcionarioCargoVO.getSalario()
				.subtract(funcionarioCargoVO.getHistoricoSalarialVO().getSalario())
				.divide(funcionarioCargoVO.getSalario(), BigDecimal.ROUND_DOWN).setScale(2, BigDecimal.ROUND_DOWN)
				.multiply(new BigDecimal(100));
		}

		obj.setPercentualVariacaoSalarial(percentual);
        obj.setDataMudanca(new Date());
        obj.setJornada(funcionarioCargoVO.getJornada());
        obj.setSalario(funcionarioCargoVO.getSalario());
        obj.setSalarioHora(funcionarioCargoVO.getSalario().divide(new BigDecimal(funcionarioCargoVO.getJornada()),  BigDecimal.ROUND_DOWN).setScale(2, BigDecimal.ROUND_DOWN));

        if (Uteis.isAtributoPreenchido(funcionarioCargoVO.getHistoricoSalarialVO().getMotivoMudanca())) {
        	obj.setMotivoMudanca(funcionarioCargoVO.getHistoricoSalarialVO().getMotivoMudanca());
        } else {
        	obj.setMotivoMudanca(MotivoMudancaCargoEnum.ADMISSAO);
        }

		return obj;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		HistoricoSalarial.idEntidade = idEntidade;
	}
}