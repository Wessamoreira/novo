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
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.HistoricoFuncaoVO;
import negocio.comuns.recursoshumanos.HistoricoFuncaoVO.EnumCampoConsultaHistoricoFuncao;
import negocio.comuns.recursoshumanos.enumeradores.TipoContratacaoComissionadoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.Conexao;
import negocio.interfaces.recursoshumanos.HistoricoFuncaoInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>HistoricoFuncaoVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>HistoricoFuncaoVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class HistoricoFuncao extends SuperFacade<HistoricoFuncaoVO> implements HistoricoFuncaoInterfaceFacade<HistoricoFuncaoVO> {

	private static final long serialVersionUID = 1L;

	protected static String idEntidade;

	public HistoricoFuncao() throws Exception {
		super();
		setIdEntidade("HistoricoFuncao");
	}

	public HistoricoFuncao(Conexao conexao, FacadeFactory facade) throws Exception {
		super();
		setConexao(conexao);
		setFacadeFactory(facade);
		setIdEntidade("HistoricoFuncao");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(HistoricoFuncaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void incluir(HistoricoFuncaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			HistoricoFuncao.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder();
					sql.append("  INSERT INTO public.historicofuncao(funcionariocargo, datamudanca, motivomudanca, cargo, nivelsalarial, ")
						.append(" faixasalarial )")
					    .append(" VALUES (?, ?, ?, ?, ?, ")
					    .append(" ? ) ")
					    .append("returning codigo")
						.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());

					int i = 0;
					Uteis.setValuePreparedStatement(obj.getFuncionarioCargo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataMudanca(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getMotivoMudanca(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCargo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNivelSalarial(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFaixaSalarial(), ++i, sqlInserir);

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
	public void alterar(HistoricoFuncaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		HistoricoFuncao.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append(" UPDATE public.historicoFuncao SET funcionariocargo=?, datamudanca=?, motivomudanca=?, cargo=?, nivelsalarial=?, faixasalarial=?");
				sql.append(" WHERE codigo = ?");
				sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getFuncionarioCargo(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getDataMudanca(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getMotivoMudanca(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCargo(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getNivelSalarial(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getFaixaSalarial(), ++i, sqlAlterar);
				
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
	}

	/**
	 * Exclui o {@link HistoricoFuncaoVO} pelo codigo informado.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(HistoricoFuncaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		HistoricoFuncao.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM HistoricoFuncao WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());

	}

	/**
	 * Consulta o {@link HistoricoFuncaoVO} pelo  codigo informado.
	 */
	@Override
	public HistoricoFuncaoVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	/**
	 * Valida os campos obrigatorios do {@link HistoricoFuncaoVO}
	 */
	@Override
	public void validarDados(HistoricoFuncaoVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getFuncionarioCargo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HistoricoFuncao_funcionarioCargo"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getDataMudanca())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HistoricoFuncao_dataMudanca"));
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception {
		dataModelo.getListaFiltros().clear();

		dataModelo.setListaConsulta(consultarHistoricoFuncao(dataModelo));
		dataModelo.setTotalRegistrosEncontrados(consultarTotalHistoricoFuncao(dataModelo));		
	}

	/**
	 * Consulta Paginada dos historicos dos Funcao retornando 10 registros.
	 * 
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private List<HistoricoFuncaoVO> consultarHistoricoFuncao(DataModelo dataModelo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico());
		sql.append(" WHERE 1 = 1");
		dataModelo.setLimitePorPagina(10);

		switch (EnumCampoConsultaHistoricoFuncao.valueOf(dataModelo.getCampoConsulta())) {
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
	 * Consulta o total de {@link HistoricoFuncaoVO} de acordo com o filtro informado.
	 *  
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private Integer consultarTotalHistoricoFuncao(DataModelo dataModelo) throws Exception {
        StringBuilder sql = new StringBuilder(getSqlBasicoCount());
        sql.append(" WHERE 1 = 1");

        switch (EnumCampoConsultaHistoricoFuncao.valueOf(dataModelo.getCampoConsulta())) {
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
	 * Monta a lista de {@link HistoricoFuncaoVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<HistoricoFuncaoVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<HistoricoFuncaoVO> tiposEmprestimos = new ArrayList<>();

        while(tabelaResultado.next()) {
        	tiposEmprestimos.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return tiposEmprestimos;
	}

	@Override
	public HistoricoFuncaoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		HistoricoFuncaoVO obj = new HistoricoFuncaoVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDataMudanca(tabelaResultado.getDate("datamudanca"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("funcionariocargo"))) {			
			obj.setFuncionarioCargo(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("funcionariocargo"), Uteis.NIVELMONTARDADOS_COMBOBOX, null));
		}
		
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("cargo"))) {			
			obj.setCargo(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("cargo"), false, nivelMontarDados, null));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("motivomudanca"))) {
			obj.setMotivoMudanca(TipoContratacaoComissionadoEnum.valueOf(tabelaResultado.getString("motivomudanca")));
		}
		
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("nivelsalarial"))) {
			obj.setNivelSalarial(getFacadeFactory().getNivelSalarialInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getLong("nivelsalarial")));
		}
		
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("faixasalarial"))) {
			obj.setFaixaSalarial(getFacadeFactory().getFaixaSalarialInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getLong("faixasalarial")));
		}
		return obj;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT hist.codigo, hist.funcionariocargo, hist.cargo, pessoa.nome, fc.matriculacargo , hist.dataMudanca, hist.motivomudanca, hist.nivelsalarial, hist.faixasalarial FROM historicofuncao hist");
		sql.append(" INNER JOIN funcionariocargo fc ON fc.codigo = hist.funcionariocargo");
		sql.append(" INNER JOIN cargo ON cargo.codigo = fc.cargo");
		sql.append(" INNER JOIN funcionario ON funcionario.codigo = fc.funcionario");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");

		return sql.toString();
	}
	
	private String getSqlBasicoCount() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(hist.codigo) as qtde FROM historicofuncao hist");
		sql.append(" INNER JOIN funcionariocargo fc ON fc.codigo = hist.funcionariocargo");
		sql.append(" INNER JOIN cargo ON cargo.codigo = fc.cargo");
		sql.append(" INNER JOIN funcionario ON funcionario.codigo = fc.funcionario");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");

		return sql.toString();
	}

	@Override
	public void persistirPorFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO, boolean validarAcesso) throws Exception {
		if (Uteis.isAtributoPreenchido(funcionarioCargoVO.getCargo()) && funcionarioCargoVO.getCargo().getCodigo() != 0 
				&& funcionarioCargoVO.getHistoricoFuncaoVO().getGerarHistorico()) {
			HistoricoFuncaoVO obj = this.montarDadosPorFuncionarioCargo(funcionarioCargoVO);
			persistir(obj, validarAcesso, null);
		}
	}

	private HistoricoFuncaoVO montarDadosPorFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO) {
		HistoricoFuncaoVO obj = new HistoricoFuncaoVO(); 
		obj.setFuncionarioCargo(funcionarioCargoVO);
        obj.setDataMudanca(new Date());
        obj.setCargo(funcionarioCargoVO.getCargo());
        obj.setNivelSalarial(funcionarioCargoVO.getNivelSalarial());
        obj.setFaixaSalarial(funcionarioCargoVO.getFaixaSalarial());
        
        if (Uteis.isAtributoPreenchido(funcionarioCargoVO.getHistoricoFuncaoVO().getMotivoMudanca())) {
        	obj.setMotivoMudanca(funcionarioCargoVO.getHistoricoFuncaoVO().getMotivoMudanca());
        } else {
        	obj.setMotivoMudanca(TipoContratacaoComissionadoEnum.ADMISSAO);
        }

		return obj;
	}
	
	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		HistoricoFuncao.idEntidade = idEntidade;
	}
}
