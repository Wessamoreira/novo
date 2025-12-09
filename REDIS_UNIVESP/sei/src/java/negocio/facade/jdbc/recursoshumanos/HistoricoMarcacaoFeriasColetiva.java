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
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.HistoricoMarcacaoFeriasColetivaVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasColetivasVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoMarcacaoFeriasEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.interfaces.recursoshumanos.HistoricoMarcacaoFeriasColetivaInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>HistoricoMarcacaoFeriasColetivaVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>HistoricoMarcacaoFeriasColetivaVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
@Scope
@Lazy
public class HistoricoMarcacaoFeriasColetiva  extends SuperFacade<HistoricoMarcacaoFeriasColetivaVO> implements HistoricoMarcacaoFeriasColetivaInterfaceFacade<HistoricoMarcacaoFeriasColetivaVO>  {

	private static final long serialVersionUID = -4825480922004318002L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(HistoricoMarcacaoFeriasColetivaVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (!Uteis.isAtributoPreenchido(obj.getCodigo())) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void incluir(HistoricoMarcacaoFeriasColetivaVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {

			HistoricoMarcacaoFeriasColetiva.incluir(getIdEntidade(), validarAcesso, usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder();
					sql.append(" INSERT INTO public.historicomarcacaoferiascoletivas( funcionariocargo, marcacaoferias, marcacaoferiascoletivas, cargo, situacao, ")
					.append(" formacontratacao, matriculacargo, nome, dataHistorico, situacaoMarcacaoFerias, ")
					.append(" lancadoAdiantamento, lancadoReciboNoContraCheque) ")
					.append(" VALUES (?, ?, ?, ?, ?, ")
					.append(" ?, ?, ?, ?, ?, ")
					.append(" ?, ? ) ")
					.append(" returning codigo ")
					.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());

					int i = 0;
					Uteis.setValuePreparedStatement(obj.getFuncionarioCargo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getMarcacaoFerias() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getMarcacaoFeriasColetivas() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCargo() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSituacao() , ++i, sqlInserir);
					
					Uteis.setValuePreparedStatement(obj.getFormaContratacao() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getMatriculaCargo() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNomeFuncionario() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(new Date(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSituacaoMarcacaoFerias() , ++i, sqlInserir);
					
					Uteis.setValuePreparedStatement(obj.getLancadoAdiantamento() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getLancadoReciboNoContraCheque() , ++i, sqlInserir);

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
	public void alterar(HistoricoMarcacaoFeriasColetivaVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		HistoricoMarcacaoFeriasColetiva.alterar(getIdEntidade(), validarAcesso, usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append(" UPDATE public.historicomarcacaoferiascoletivas ")
				.append(" SET funcionariocargo=?, marcacaoferias=?, marcacaoferiascoletivas=?, cargo=?, situacao=?, ")
				.append(" formacontratacao=?, matriculacargo=?, nome=?, dataHistorico=?, situacaoMarcacaoFerias=?, ") 
				.append(" lancadoAdiantamento=?, lancadoReciboNoContraCheque=? ")
				.append(" WHERE codigo=?")
				.append( adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;

				Uteis.setValuePreparedStatement(obj.getFuncionarioCargo(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getMarcacaoFerias() , ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getMarcacaoFeriasColetivas() , ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCargo() , ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getSituacao() , ++i, sqlAlterar);
				
				Uteis.setValuePreparedStatement(obj.getFormaContratacao() , ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getMatriculaCargo() , ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getNomeFuncionario() , ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(new Date(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getSituacaoMarcacaoFerias() , ++i, sqlAlterar);
				
				Uteis.setValuePreparedStatement(obj.getLancadoAdiantamento() , ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getLancadoReciboNoContraCheque() , ++i, sqlAlterar);
				
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
	}

	@Override
	public void excluir(HistoricoMarcacaoFeriasColetivaVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		HistoricoMarcacaoFeriasColetiva.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM historicomarcacaoferiascoletivas WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });
	}

	@Override
	public HistoricoMarcacaoFeriasColetivaVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" where codigo = ? ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{id.intValue()});

        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
	}

	/**
	 * Metodo que consulta as marcações de ferias e o total das marcações para o paginador da 
	 * pagina de consulta.
	 */
	@Override
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception {
		List<HistoricoMarcacaoFeriasColetivaVO> objs = new ArrayList<>();

		objs = consultarHistoricoMarcacaoFeriasColetivas(dataModelo);
		dataModelo.setTotalRegistrosEncontrados(consultarTotalHistoricoMarcacaoFeriasColetivas(dataModelo));

		dataModelo.setListaConsulta(objs);
	}

	/**
	 * Consulta as marcações de ferias coletivas com os filtros informados.
	 * 
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private List<HistoricoMarcacaoFeriasColetivaVO> consultarHistoricoMarcacaoFeriasColetivas(DataModelo dataModelo) throws Exception {
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" WHERE 1 = 1");
		sql.append(" AND marcacaoferiascoletivas = ?");
		sql.append(" ORDER BY nome DESC");
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
        return (montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados()));
	}

	/**
	 * Consulta o total de marcacao de ferias coletivas para o paginador.
	 * 
	 * @param dataModelo
	 * @return
	 */
	private Integer consultarTotalHistoricoMarcacaoFeriasColetivas(DataModelo dataModelo) {
		StringBuilder sql = new StringBuilder(getSQLBasicoTotal());
		sql.append(" WHERE 1 = 1");
		sql.append(" AND marcacaoferiascoletivas = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}

	/**
	 * Monta a lista de dados da consulta.
	 * 
	 * @param tabelaResultado
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	private List<HistoricoMarcacaoFeriasColetivaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List<HistoricoMarcacaoFeriasColetivaVO> lista = new ArrayList<>();
		while(tabelaResultado.next()) {
			lista.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		return lista;
	}

	@Override
	public void validarDados(HistoricoMarcacaoFeriasColetivaVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getFuncionarioCargo().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar(""));
		}

		if (!Uteis.isAtributoPreenchido(obj.getMarcacaoFerias().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar(""));
		}

		if (!Uteis.isAtributoPreenchido(obj.getMarcacaoFeriasColetivas().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar(""));
		}
	}

	@Override
	public HistoricoMarcacaoFeriasColetivaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		
		HistoricoMarcacaoFeriasColetivaVO obj = new HistoricoMarcacaoFeriasColetivaVO();
		
        obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
        obj.getFuncionarioCargo().setCodigo(tabelaResultado.getInt("funcionariocargo"));
        obj.getMarcacaoFerias().setCodigo(tabelaResultado.getInt("marcacaoferias"));
        obj.getMarcacaoFeriasColetivas().setCodigo(tabelaResultado.getInt("marcacaoferiascoletivas"));
        obj.setCargo(tabelaResultado.getString("cargo"));
        
        obj.setSituacao(tabelaResultado.getString("situacao"));
        obj.setFormaContratacao(tabelaResultado.getString("formacontratacao"));
        obj.setMatriculaCargo(tabelaResultado.getString("matriculacargo"));
        obj.setNomeFuncionario(tabelaResultado.getString("nome"));
        obj.setDataHistorico(tabelaResultado.getDate("dataHistorico"));
        
        obj.setSituacaoMarcacaoFerias(SituacaoMarcacaoFeriasEnum.valueOf(tabelaResultado.getString("situacaoMarcacaoFerias")));
        obj.setLancadoAdiantamento(tabelaResultado.getBoolean("lancadoAdiantamento"));
        obj.setLancadoReciboNoContraCheque(tabelaResultado.getBoolean("lancadoReciboNoContraCheque"));
        
        if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS)
        	return obj;
        
        if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("funcionariocargo"))) {
			obj.setFuncionarioCargo(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("funcionariocargo"), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null));
		}

        if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("marcacaoferias"))) {
			obj.setMarcacaoFerias(getFacadeFactory().getMarcacaoFeriasInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getInt("marcacaoferias"), Uteis.NIVELMONTARDADOS_PROCESSAMENTO));
		}

        if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("marcacaoferiascoletivas"))) {
			obj.setMarcacaoFeriasColetivas(getFacadeFactory().getMarcacaoFeriasColetivasInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getLong("marcacaoferiascoletivas")));
		}

        return obj;
	}

	private String getSQLBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM public.historicomarcacaoferiascoletivas ");
		return sql.toString();
	}
	
	private String getSQLBasicoTotal() {
		StringBuilder sql = new StringBuilder();
		return sql.append(" SELECT COUNT(codigo) as qtde FROM public.historicomarcacaoferiascoletivas").toString();
	}

	@Override
	public List<HistoricoMarcacaoFeriasColetivaVO> consultarDadosPorMarcacaoFeriasColetivas(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, int nivelMontarDados) throws Exception {
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" WHERE marcacaoferiascoletivas = ?");
		sql.append(" ORDER BY codigo DESC");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  marcacaoFeriasColetivasVO.getCodigo());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}
	
	@Override
	public List<HistoricoMarcacaoFeriasColetivaVO> consultarDadosPorMarcacaoFeriasColetivas(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, SituacaoMarcacaoFeriasEnum situacao, int nivelMontarDados) throws Exception {
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" WHERE marcacaoferiascoletivas = ? ");
		sql.append(" and  situacaoMarcacaoFerias = ? ");
		sql.append(" ORDER BY codigo DESC");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  marcacaoFeriasColetivasVO.getCodigo(), situacao.getValor());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	@Override
	public void excluirHistoricoPorMarcacaoFeriasColetivas(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, UsuarioVO usuarioVO) throws Exception{
		HistoricoMarcacaoFeriasColetiva.excluir(getIdEntidade(), false, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM historicomarcacaoferiascoletivas WHERE marcacaoferiascoletivas = ? ")
				.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), marcacaoFeriasColetivasVO.getCodigo());
	}

	@Override
	public void excluirHistoricoPorMarcacaoDeFeriasEFuncionarioCargo(MarcacaoFeriasVO marcacaoFerias, FuncionarioCargoVO funcionarioCargo, UsuarioVO usuarioVO) {
		StringBuilder sql = new StringBuilder("DELETE FROM historicomarcacaoferiascoletivas ");
		sql.append(" where marcacaoferias = ? ");
		sql.append(" and funcionariocargo = ? ");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		
		getConexao().getJdbcTemplate().update(sql.toString(), marcacaoFerias.getCodigo(), funcionarioCargo.getCodigo());
	}
}