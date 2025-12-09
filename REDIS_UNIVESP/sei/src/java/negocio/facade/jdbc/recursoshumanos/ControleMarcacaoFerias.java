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
import negocio.comuns.administrativo.enumeradores.FormaContratacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.TipoRecebimentoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ControleMarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasColetivasVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.TemplateLancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoMarcacaoFeriasEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.Conexao;
import negocio.interfaces.recursoshumanos.ControleMarcacaoFeriasInterfaceFacade;

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
public class ControleMarcacaoFerias  extends SuperFacade<ControleMarcacaoFeriasVO> implements ControleMarcacaoFeriasInterfaceFacade<ControleMarcacaoFeriasVO>  {

	private static final long serialVersionUID = -4825480922004318002L;
	
	public ControleMarcacaoFerias() {
		
	}

	/**
	 * Contrutor que recebe as instancia para realizacao dos testes unitarios.
	 * 
	 * @param conexao
	 * @param facade
	 * @throws Exception
	 */
	public ControleMarcacaoFerias(Conexao conexao, FacadeFactory facade) throws Exception {
		super();
		setConexao(conexao);
		setFacadeFactory(facade);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ControleMarcacaoFeriasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (!Uteis.isAtributoPreenchido(obj.getCodigo())) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void incluir(ControleMarcacaoFeriasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {

			ControleMarcacaoFerias.incluir(getIdEntidade(), validarAcesso, usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder();
					sql.append(" INSERT INTO public.controlemarcacaoferias( funcionariocargo, marcacaoferias, marcacaoferiascoletivas, cargo, situacao, ")
					.append(" formacontratacao, matriculacargo, nome, dataHistorico, situacaoMarcacaoFerias, ")
					.append(" lancadoAdiantamento, lancadoReciboNoContraCheque, adiantamentoFerias, reciboFerias) ")
					.append(" VALUES (?, ?, ?, ?, ?, ")
					.append(" ?, ?, ?, ?, ?, ")
					.append(" ?, ?, ?, ? ) ")
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
					Uteis.setValuePreparedStatement(obj.getAdiantamentoFerias() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getReciboFerias() , ++i, sqlInserir);

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
	public void alterar(ControleMarcacaoFeriasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleMarcacaoFerias.alterar(getIdEntidade(), validarAcesso, usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append(" UPDATE public.controlemarcacaoferias ")
				.append(" SET funcionariocargo=?, marcacaoferias=?, marcacaoferiascoletivas=?, cargo=?, situacao=?, ")
				.append(" formacontratacao=?, matriculacargo=?, nome=?, dataHistorico=?, situacaoMarcacaoFerias=?, ") 
				.append(" lancadoAdiantamento=?, lancadoReciboNoContraCheque=?, adiantamentoFerias=?, reciboFerias=? ")
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
				Uteis.setValuePreparedStatement(obj.getAdiantamentoFerias() , ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getReciboFerias() , ++i, sqlAlterar);
				
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
	}

	@Override
	public void excluir(ControleMarcacaoFeriasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleMarcacaoFerias.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM controlemarcacaoferias WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });
	}

	@Override
	public ControleMarcacaoFeriasVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" where codigo = ? ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{id.intValue()});

        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
	}

	/**
	 *  Recupera o Controle de marcação de ferias pela data competencia a situação da marcação
	 *  'Calculada' é lancado recibo contracheque igual a false.
	 */
	@Override
	public List<ControleMarcacaoFeriasVO> consultarPorDataCompetenciaDataInicioGozoDaMarcacaoFerias(Date dataCompetencia, boolean lancadorecibonocontracheque) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT controle.* FROM controlemarcacaoferias controle");
		sql.append(" INNER JOIN marcacaoferias marcacao ON controle.marcacaoferias = marcacao.codigo");
		sql.append(" WHERE controle.lancadorecibonocontracheque = ").append(lancadorecibonocontracheque);
		sql.append(" AND marcacao.situacaomarcacao = 'CALCULADA' ");

		if (Uteis.isAtributoPreenchido(dataCompetencia)) {
        	sql.append(" AND EXTRACT('Month' FROM  marcacao.datainiciogozo) = ").append(String.valueOf(UteisData.getMesData(dataCompetencia)));
        	sql.append(" AND EXTRACT('Year' FROM  marcacao.datainiciogozo) = ").append(String.valueOf(UteisData.getAnoData(dataCompetencia)));
        }

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<ControleMarcacaoFeriasVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			lista.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
		}
		return lista;
	}

	/**
	 * Metodo que consulta as marcações de ferias e o total das marcações para o paginador da 
	 * pagina de consulta.
	 */
	@Override
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception {
		dataModelo.setListaConsulta(consultarHistoricoMarcacaoFeriasColetivas(dataModelo));
		dataModelo.setTotalRegistrosEncontrados(consultarTotalHistoricoMarcacaoFeriasColetivas(dataModelo));
	}

	/**
	 * Consulta as marcações de ferias coletivas com os filtros informados.
	 * 
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private List<ControleMarcacaoFeriasVO> consultarHistoricoMarcacaoFeriasColetivas(DataModelo dataModelo) throws Exception {
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
	private List<ControleMarcacaoFeriasVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List<ControleMarcacaoFeriasVO> lista = new ArrayList<>();
		while(tabelaResultado.next()) {
			lista.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		return lista;
	}

	@Override
	public void validarDados(ControleMarcacaoFeriasVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getFuncionarioCargo().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar(""));
		}

		if (!Uteis.isAtributoPreenchido(obj.getMarcacaoFerias().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar(""));
		}

	}

	@Override
	public ControleMarcacaoFeriasVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		
		ControleMarcacaoFeriasVO obj = new ControleMarcacaoFeriasVO();
		
        obj.setCodigo(tabelaResultado.getInt("codigo"));
        obj.getFuncionarioCargo().setCodigo(tabelaResultado.getInt("funcionariocargo"));
        obj.getMarcacaoFerias().setCodigo(tabelaResultado.getInt("marcacaoferias"));
        obj.getMarcacaoFeriasColetivas().setCodigo(tabelaResultado.getInt("marcacaoferiascoletivas"));
		obj.getAdiantamentoFerias().setCodigo(tabelaResultado.getInt("adiantamentoferias"));
       	obj.getReciboFerias().setCodigo(tabelaResultado.getInt("reciboferias"));
        
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
        
        if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("adiantamentoferias"))) {
			obj.setAdiantamentoFerias(getFacadeFactory().getContraChequeInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getLong("adiantamentoferias")));
		}

        if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("reciboferias"))) {
        	obj.setReciboFerias(getFacadeFactory().getContraChequeInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getLong("reciboferias")));
        }

        return obj;
	}

	private String getSQLBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM public.controlemarcacaoferias ");
		return sql.toString();
	}
	
	private String getSQLBasicoTotal() {
		StringBuilder sql = new StringBuilder();
		return sql.append(" SELECT COUNT(codigo) as qtde FROM public.controlemarcacaoferias").toString();
	}

	@Override
	public List<ControleMarcacaoFeriasVO> consultarDadosPorMarcacaoFeriasColetivas(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, int nivelMontarDados) throws Exception {
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" WHERE marcacaoferiascoletivas = ?");
		sql.append(" ORDER BY codigo DESC");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  marcacaoFeriasColetivasVO.getCodigo());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}
	
	@Override
	public List<ControleMarcacaoFeriasVO> consultarDadosPorMarcacaoFeriasColetivas(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, SituacaoMarcacaoFeriasEnum situacao, int nivelMontarDados) throws Exception {
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" WHERE marcacaoferiascoletivas = ? ");
		sql.append(" and  situacaoMarcacaoFerias = ? ");
		sql.append(" ORDER BY codigo DESC");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  marcacaoFeriasColetivasVO.getCodigo(), situacao.getValor());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	@Override
	public List<ControleMarcacaoFeriasVO> consultarDadosPorFiltrosMarcacaoFeriasColetivas(MarcacaoFeriasColetivasVO obj, SituacaoMarcacaoFeriasEnum situacao, int nivelMontarDados) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT controle.codigo, controle.funcionariocargo, controle.marcacaoferias, controle.nome, controle.cargo, controle.situacao, controle.formacontratacao,");
		sql.append(" controle.matriculacargo, controle.datahistorico, controle.situacaomarcacaoferias, controle.lancadoadiantamento, controle.marcacaoferiascoletivas, ");
		sql.append(" controle.adiantamentoferias, controle.reciboferias, controle.lancadoReciboNoContraCheque FROM controlemarcacaoferias controle");
		sql.append(" INNER JOIN marcacaoferiascoletivas mfc ON controle.marcacaoferiascoletivas = mfc.codigo");
		sql.append(" INNER JOIN templatelancamentofolhapagamento te ON mfc.templatelancamentofolha = te.codigo");
		sql.append(" WHERE controle.marcacaoferiascoletivas = ? ");
		sql.append(" and  controle.situacaoMarcacaoFerias = ? ");

		sql.append(UteisTexto.montarStringDeFiltroPorEnumerado(FormaContratacaoFuncionarioEnum.values(), obj.getTemplateLancamentoFolhaPagamentoVO().getFormaContratacaoFuncionario().split(";"), "te.formacontratacaofuncionario"));
		sql.append(UteisTexto.montarStringDeFiltroPorEnumerado(TipoRecebimentoEnum.values(), obj.getTemplateLancamentoFolhaPagamentoVO().getTipoRecebimento().split(";"), "te.tiporecebimento"));
		sql.append(UteisTexto.montarStringDeFiltroPorEnumerado(SituacaoFuncionarioEnum.values(), obj.getTemplateLancamentoFolhaPagamentoVO().getSituacaoFuncionario().split(";"), "te.situacaoFuncionario"));
		
		sql.append(" ORDER BY controle.codigo DESC");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  obj.getCodigo(), situacao.getValor());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	@Override
	public void excluirHistoricoPorMarcacaoFeriasColetivas(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, UsuarioVO usuarioVO) throws Exception{
		ControleMarcacaoFerias.excluir(getIdEntidade(), false, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM controlemarcacaoferias WHERE marcacaoferiascoletivas = ? ")
				.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), marcacaoFeriasColetivasVO.getCodigo());
	}

	@Override
	public void excluirHistoricoPorMarcacaoDeFeriasEFuncionarioCargo(MarcacaoFeriasVO marcacaoFerias, FuncionarioCargoVO funcionarioCargo, UsuarioVO usuarioVO) {
		StringBuilder sql = new StringBuilder("DELETE FROM controlemarcacaoferias ");
		sql.append(" where marcacaoferias = ? ");
		sql.append(" and funcionariocargo = ? ");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		
		getConexao().getJdbcTemplate().update(sql.toString(), marcacaoFerias.getCodigo(), funcionarioCargo.getCodigo());
	}
	

	/**
	 * Monta os dados do objeto HistoricoMarcacaoFeriasVO.
	 * 
	 * @param marcacaoFeriasColetivasVO
	 * @param funcionarioCargoVO
	 * @param marcacaoFerias
	 * @param historico
	 */
	@Override
	public ControleMarcacaoFeriasVO montarDadosControleMarcacaoFerias(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, MarcacaoFeriasVO marcacaoFerias, FuncionarioCargoVO funcionarioCargoVO) {
		ControleMarcacaoFeriasVO controleMF = new ControleMarcacaoFeriasVO();
		
		controleMF.setFuncionarioCargo(funcionarioCargoVO);
		controleMF.setMarcacaoFerias(marcacaoFerias);
		controleMF.setCargo(funcionarioCargoVO.getCargo().getNome());
		controleMF.setSituacao(funcionarioCargoVO.getSituacaoFuncionarioApresentar());
		controleMF.setMatriculaCargo(funcionarioCargoVO.getMatriculaCargo());
		controleMF.setNomeFuncionario(funcionarioCargoVO.getFuncionarioVO().getPessoa().getNome());
		controleMF.setFormaContratacao(funcionarioCargoVO.getFormaContratacao().getDescricao());
		
		if(Uteis.isAtributoPreenchido(marcacaoFeriasColetivasVO)) {
			controleMF.setMarcacaoFeriasColetivas(marcacaoFeriasColetivasVO);
			controleMF.setSituacaoMarcacaoFerias(marcacaoFeriasColetivasVO.getSituacao());	
		} else {
			controleMF.setSituacaoMarcacaoFerias(marcacaoFerias.getSituacaoMarcacao());
		}
		
		
		return controleMF;
	}	

	/**
	 * Consulta as o {@link ControleMarcacaoFeriasVO} pela {@link MarcacaoFeriasVO} informado.
	 * 
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	@Override
	public ControleMarcacaoFeriasVO consultarDadosPorMarcacaoFerias(MarcacaoFeriasVO marcacaoFeriasVO) throws Exception {
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" WHERE marcacaoferias = ?");
		sql.append(" ORDER BY nome DESC");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), marcacaoFeriasVO.getCodigo());

       	if(tabelaResultado.next()) {
           	return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));
       	}

        return new ControleMarcacaoFeriasVO();
	}
	
	/**
	 * Consulta o controle de marcação de ferias pelo funcionarioca cargo, pela situação da {@link MarcacaoFeriasVO} 'CALCULADA' 
	 * ou FECHADA para a data de final gozo informado 
	 * 
	 * @param funcionarioCargoVO
	 * @param dataInicioGozo
	 * @param dataFinalGozo
	 * @return
	 */
	@Override
	public ControleMarcacaoFeriasVO consultarControlePorFuncionarioCargoEPeriodoGozo(FuncionarioCargoVO funcionarioCargoVO, Date dataFinalGozo) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT controle.* FROM controlemarcacaoferias AS controle ");
		sql.append(" INNER JOIN marcacaoferias ON marcacaoferias.codigo = controle.marcacaoferias ");
		sql.append(" WHERE controle.funcionariocargo = ?");
		sql.append(" AND ((controle.situacaoMarcacaoFerias = 'CALCULADA') or (controle.situacaoMarcacaoFerias = 'FECHADA' ");

		if (Uteis.isAtributoPreenchido(dataFinalGozo)) {
        	sql.append(" AND EXTRACT('Month' FROM marcacaoferias.datafinalgozo) = ").append(String.valueOf(UteisData.getMesData(dataFinalGozo)));
        	sql.append(" AND EXTRACT('Year' FROM marcacaoferias.datafinalgozo) = ").append(String.valueOf(UteisData.getAnoData(dataFinalGozo)));
        }

		sql.append(")) limit 1");
		
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  funcionarioCargoVO.getCodigo());
        
        try {
        	if(tabelaResultado.next())
               	return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));	
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return new ControleMarcacaoFeriasVO();
	}
	
	
	/**
	 * Salva controle marcacacao de ferias pelo Marcacao de Ferias
	 * 
	 * @param marcacaoFeriasVO
	 * @param situacao
	 * @param validarAcesso
	 * @param usuarioVO
	 * @throws Exception
	 */
	@Override
	public void salvarControleMarcacaoFerias(MarcacaoFeriasVO marcacaoFeriasVO, SituacaoMarcacaoFeriasEnum situacao, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {

		salvarControleMarcacaoFerias(null, marcacaoFeriasVO, situacao, validarAcesso, usuarioVO);
	}
	
	
	/**
	 * Salva controle marcacacao de ferias pelo Marcacao de Ferias Coletivas
	 * 
	 * @param marcacaoFeriasColetivasVO
	 * @param marcacaoFeriasVO
	 * @param situacao
	 * @param validarAcesso
	 * @param usuarioVO
	 * @throws Exception
	 */
	@Override
	public void salvarControleMarcacaoFerias(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, MarcacaoFeriasVO marcacaoFeriasVO, SituacaoMarcacaoFeriasEnum situacao, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		ControleMarcacaoFeriasVO controle = consultarDadosPorMarcacaoFerias(marcacaoFeriasVO);
		
		salvarControleMarcacaoFerias(controle, marcacaoFeriasColetivasVO, marcacaoFeriasVO, situacao, validarAcesso, usuarioVO);
	}
	
	/**
	 * Salva controle marcacacao de ferias pelo Marcacao de Ferias Coletivas
	 * 
	 * @param marcacaoFeriasColetivasVO
	 * @param marcacaoFeriasVO
	 * @param situacao
	 * @param validarAcesso
	 * @param usuarioVO
	 * @throws Exception
	 */
	@Override
	public void salvarControleMarcacaoFerias(ControleMarcacaoFeriasVO controle, MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, MarcacaoFeriasVO marcacaoFeriasVO, SituacaoMarcacaoFeriasEnum situacao, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		if(Uteis.isAtributoPreenchido(controle)) {
			
			controle.setSituacaoMarcacaoFerias(situacao);
			
			if(Uteis.isAtributoPreenchido(marcacaoFeriasColetivasVO))
				controle.setMarcacaoFeriasColetivas(marcacaoFeriasColetivasVO);
			
			getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().persistir(controle, validarAcesso, usuarioVO);	
			
		} else {
			ControleMarcacaoFeriasVO controleMF = this.montarDadosControleMarcacaoFerias(marcacaoFeriasColetivasVO, marcacaoFeriasVO, marcacaoFeriasVO.getFuncionarioCargoVO());
			getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().persistir(controleMF, validarAcesso, usuarioVO);
		}
	}

	@Override
	public List<ControleMarcacaoFeriasVO> consultarFinalDasFeriasDoFuncionarios(Date dataCompetencia, int nivelMontarDados) {

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT controlemarcacaoferias.* FROM controlemarcacaoferias ");
		sql.append(" inner join marcacaoferias marcacao on controlemarcacaoferias.marcacaoferias = marcacao.codigo ");
		sql.append(" INNER JOIN funcionariocargo fc ON fc.codigo = marcacao.funcionariocargo");
		sql.append(" WHERE marcacao.datafinalgozo < ? AND fc.situacaofuncionario = 'FERIAS'");
		sql.append(" AND marcacao.situacaomarcacao = 'CALCULADA'");
	
		List<ControleMarcacaoFeriasVO> listaControleMarcacaoFerias = new ArrayList<>();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Date());
	
		while (tabelaResultado.next()) {
			try {
				listaControleMarcacaoFerias.add(montarDados(tabelaResultado, nivelMontarDados));	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listaControleMarcacaoFerias;	
	}
	
	@Override
	public List<ControleMarcacaoFeriasVO> consultarFuncionariosComFinalDasFeriasMaioQueCompetencia(Date dataCompetencia, int nivelMontarDados) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT  distinct on (fc.codigo) controlemarcacaoferias.* FROM controlemarcacaoferias ");
		sql.append(" inner join marcacaoferias marcacao on controlemarcacaoferias.marcacaoferias = marcacao.codigo ");
		sql.append(" INNER JOIN funcionariocargo fc ON fc.codigo = marcacao.funcionariocargo");
		sql.append(" WHERE marcacao.datafinalgozo < ? AND fc.situacaofuncionario like 'FERIAS'");

		List<ControleMarcacaoFeriasVO> listaControleMarcacaoFerias = new ArrayList<>();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Date());
		
        while (tabelaResultado.next()) {
        	try {
        		listaControleMarcacaoFerias.add(montarDados(tabelaResultado, nivelMontarDados));				
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        return listaControleMarcacaoFerias;	
	}

	@Override
	public void cancelarLancamentoDoAdiantamentoDoReciboNoContraCheque(CompetenciaFolhaPagamentoVO competencia, TemplateLancamentoFolhaPagamentoVO templateFP, UsuarioVO usuarioVO) {
		StringBuilder sql = new StringBuilder("update controlemarcacaoferias set lancadoAdiantamento = false, adiantamentoFerias = null ");
		sql.append(" where codigo in (");
		sql.append(" select controlemarcacaoferias.codigo from controlemarcacaoferias ");
		sql.append(" inner join funcionariocargo fc on fc.codigo = controlemarcacaoferias.funcionariocargo ");
		sql.append(" inner join contracheque contracheque on controlemarcacaoferias.adiantamentoFerias = contracheque.codigo ");
		sql.append(" where contracheque.competenciafolhapagamento = ? ");
		sql.append(getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().getFiltrosDoTemplate(templateFP));
		sql.append(" )");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		
		getConexao().getJdbcTemplate().update(sql.toString(), competencia.getCodigo());
	}

	@Override
	public void cancelarLancamentoDoReciboDeFeriasNoContraCheque(CompetenciaFolhaPagamentoVO competencia, TemplateLancamentoFolhaPagamentoVO templateFP, UsuarioVO usuarioVO) {
		StringBuilder sql = new StringBuilder("update controlemarcacaoferias set lancadoReciboNoContraCheque = false, reciboFerias = null ");
		sql.append(" where codigo in (");
		sql.append(" select controlemarcacaoferias.codigo from controlemarcacaoferias ");
		sql.append(" inner join funcionariocargo fc on fc.codigo = controlemarcacaoferias.funcionariocargo ");
		sql.append(" inner join contracheque contracheque on controlemarcacaoferias.reciboFerias = contracheque.codigo ");
		sql.append(" where contracheque.competenciafolhapagamento = ? ");
		sql.append(getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().getFiltrosDoTemplate(templateFP));	
		sql.append(" )");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		
		getConexao().getJdbcTemplate().update(sql.toString(), competencia.getCodigo());
	}
	
}