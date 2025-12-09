package negocio.facade.jdbc.recursoshumanos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
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
import negocio.comuns.recursoshumanos.ControleMarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasColetivasVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.ReciboFeriasVO;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoMarcacaoFeriasEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.interfaces.recursoshumanos.MarcacaoFeriasColetivasInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>MarcacaoFeriasColetivasVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>MarcacaoFeriasColetivasVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
@Scope
@Lazy
public class MarcacaoFeriasColetivas extends SuperFacade<MarcacaoFeriasColetivasVO> implements MarcacaoFeriasColetivasInterfaceFacade<MarcacaoFeriasColetivasVO> {

	private static final long serialVersionUID = -5690031795725435328L;

	protected static String idEntidade;

	public MarcacaoFeriasColetivas() throws Exception {
		super();
		setIdEntidade("MarcacaoFeriasColetivas");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(MarcacaoFeriasColetivasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		realizarConfigurarTemplateParaSalvarNovoEManterInformacoesDoAntigo(obj);
		getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().persistir(obj.getTemplateLancamentoFolhaPagamentoVO(), false, usuarioVO);
		
		if (!Uteis.isAtributoPreenchido(obj.getCodigo())) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}
	
	//Zerar codido para que seja gerado um novo template e assim guardar as informacoes antigas e salvar as novas
	private void realizarConfigurarTemplateParaSalvarNovoEManterInformacoesDoAntigo(MarcacaoFeriasColetivasVO obj) {
		obj.getTemplateLancamentoFolhaPagamentoVO().setCodigo(null);		
	}
	
	@Override
	public void incluir(MarcacaoFeriasColetivasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {

			MarcacaoFeriasColetivas.incluir(getIdEntidade(), validarAcesso, usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder();
					sql.append(" INSERT INTO public.marcacaoferiascoletivas(")
					.append(" datafechamento, datainiciogozo, datafinalgozo, datapagamento, datainicioaviso,")
					.append(" quantidadediasabono, quantidadedias, abono, pagarprimeiraparcela13, encerrarperiodoaquisitivo,")
					.append(" templatelancamentofolha, informacoesadicionais, situacao, descricao)")
					.append(" VALUES (?, ?, ?, ?, ?,")
					.append(" ?, ?, ?, ?, ?, ")
					.append(" ?, ?, ?, ?)")
					.append(" returning codigo ")
					.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());

					int i = 0;
					Uteis.setValuePreparedStatement(obj.getDataFechamento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataInicioGozo() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataFinalGozo() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataPagamento() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataInicioAviso() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getQuantidadeDiasAbono() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getQuantidadeDias() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getAbono() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPagarPrimeiraParcela13() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getEncerrarPeriodoAquisitivo() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTemplateLancamentoFolhaPagamentoVO() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getInformacoesAdicionais() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSituacao() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDescricao() , ++i, sqlInserir);

					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(final ResultSet arg0) throws SQLException {
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
	public void alterar(MarcacaoFeriasColetivasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		MarcacaoFeriasColetivas.alterar(getIdEntidade(), validarAcesso, usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append(" UPDATE public.marcacaoferiascoletivas")
				.append(" SET datafechamento=?, datainiciogozo=?, datafinalgozo=?, datapagamento=?, datainicioaviso=?,")
				.append(" quantidadediasabono=?, quantidadedias=?, abono=?, pagarprimeiraparcela13=?, encerrarperiodoaquisitivo=?,")
				.append(" templatelancamentofolha=?, informacoesadicionais=?, situacao=?, descricao=?")
				.append(" WHERE codigo=?")
				.append( adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;

				Uteis.setValuePreparedStatement(obj.getDataFechamento(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getDataInicioGozo() , ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getDataFinalGozo() , ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getDataPagamento() , ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getDataInicioAviso() , ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getQuantidadeDiasAbono() , ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getQuantidadeDias() , ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getAbono() , ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getPagarPrimeiraParcela13() , ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getEncerrarPeriodoAquisitivo() , ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getTemplateLancamentoFolhaPagamentoVO() , ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getInformacoesAdicionais() , ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getSituacao() , ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getDescricao() , ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
	}

	/**
	 * Realiza o cancelamento do recibo de ferias, excluir os controles {@link ControleMarcacaoFeriasVO} 
	 * e exclui a marcacao de ferias coletivas.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(MarcacaoFeriasColetivasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		this.cancelarRecibo(obj, validarAcesso, usuarioVO);
		this.excluirRegistrosAnteriores(obj, usuarioVO);

		this.excluirMarcacaoFeriasColetivas(obj, validarAcesso, usuarioVO);
	}

	/**
	 * Exclui a marcação de ferias coletivas informada.
	 * 
	 * @param obj
	 * @param validarAcesso
	 * @param usuarioVO
	 */
	private void excluirMarcacaoFeriasColetivas(MarcacaoFeriasColetivasVO obj, boolean validarAcesso, UsuarioVO usuarioVO) {
		StringBuilder sql = new StringBuilder("DELETE FROM marcacaoferiascoletivas WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
	}

	@Override
	public MarcacaoFeriasColetivasVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" where codigo = ? ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id.intValue());
        
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
	}

	/**
	 * Recupera o valor basico da consulta sql.
	 * 
	 * @return
	 */
	private String getSQLBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT codigo, datafechamento, datainiciogozo, datafinalgozo, datapagamento, datainicioaviso, quantidadediasabono,");
		sql.append(" quantidadedias, abono, pagarprimeiraparcela13, encerrarperiodoaquisitivo, templatelancamentofolha, informacoesadicionais,");
		sql.append(" situacao, descricao" );
		sql.append(" FROM public.marcacaoferiascoletivas");
		return sql.toString();
	}

	private String getSQLBasicoTotal() {
		StringBuilder sql = new StringBuilder();
		return sql.append(" SELECT COUNT(codigo) as qtde FROM public.marcacaoferiascoletivas").toString();
	}

	/**
	 * Valida os dados dos campos obrigatorios. 
	 */
	@Override
	public void validarDados(MarcacaoFeriasColetivasVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getDescricao())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_MarcacaoFeriasColetivas_descricao"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getDataInicioGozo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_MarcacaoFeriasColetivas_dataInicioGozo"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getDataFinalGozo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_MarcacaoFeriasColetivas_dataFinalGozo"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getDataPagamento())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_MarcacaoFeriasColetivas_dataPagamento"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getDataInicioAviso())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_MarcacaoFeriasColetivas_dataInicioAviso"));
		}

		if (obj.getQuantidadeDias() == null) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_MarcacaoFeriasColetivas_quantidadeDias"));
		}

		if (obj.getQuantidadeDiasAbono() == null) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_MarcacaoFeriasColetivas_quantidadeDiasAbono"));
		}

		if (obj.getEncerrarPeriodoAquisitivo() && 
				!Uteis.isAtributoPreenchido(obj.getDataFechamento())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_MarcacaoFeriasColetivas_dataFechamento"));
		}
	}

	/**
	 * Monta os dados da marcacao de ferias.
	 */
	@Override
	public MarcacaoFeriasColetivasVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		MarcacaoFeriasColetivasVO obj = new MarcacaoFeriasColetivasVO();
        obj.setCodigo(tabelaResultado.getInt("codigo"));
        obj.setDataFechamento(tabelaResultado.getDate("datafechamento"));
        obj.setDataInicioGozo(tabelaResultado.getDate("dataInicioGozo"));
        obj.setDataFinalGozo(tabelaResultado.getDate("dataFinalGozo"));
        obj.setDataPagamento(tabelaResultado.getDate("datapagamento"));
        obj.setDataInicioAviso(tabelaResultado.getDate("datainicioaviso"));

        obj.setQuantidadeDiasAbono(tabelaResultado.getInt("quantidadediasabono"));
        obj.setQuantidadeDias(tabelaResultado.getInt("quantidadedias"));
        obj.setAbono(tabelaResultado.getBoolean("abono"));
        obj.setPagarPrimeiraParcela13(tabelaResultado.getBoolean("pagarprimeiraparcela13"));
        obj.setEncerrarPeriodoAquisitivo(tabelaResultado.getBoolean("encerrarperiodoaquisitivo"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("templatelancamentofolha"))) {
			obj.setTemplateLancamentoFolhaPagamentoVO(getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getInt("templatelancamentofolha"), Uteis.NIVELMONTARDADOS_DADOSBASICOS));
		}
		obj.setInformacoesAdicionais(tabelaResultado.getString("informacoesadicionais"));
		
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("situacao"))) {
			obj.setSituacao(SituacaoMarcacaoFeriasEnum.valueOf(tabelaResultado.getString("situacao")));
		}
		obj.setDescricao(tabelaResultado.getString("descricao"));
		return obj;
	}

	/**
	 * Metodo que consulta as marcações de ferias e o total das marcações para o paginador da 
	 * pagina de consulta.
	 */
	@Override
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, String valorConsultaSituacao) throws Exception {
		dataModelo.setListaConsulta(consultarMarcacaoFeriasColetivas(dataModelo, valorConsultaSituacao));
		dataModelo.getListaFiltros().clear();
		dataModelo.setTotalRegistrosEncontrados(consultarTotalMarcacaoFeriasColetivas(dataModelo, valorConsultaSituacao));
	}

	/**
	 * Consulta as marcações de ferias coletivas com os filtros informados.
	 * 
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private List<MarcacaoFeriasColetivasVO> consultarMarcacaoFeriasColetivas(DataModelo dataModelo, String valorConsultaSituacao) throws Exception {
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" WHERE 1 = 1");
		sql.append(" AND descricao ILIKE ? ");
		dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);
		if (!valorConsultaSituacao.equals("TODOS")) {
			dataModelo.getListaFiltros().add(valorConsultaSituacao);
			sql.append(" AND situacao = upper(?) ");
		}
		sql.append(" AND ").append(realizarGeracaoWherePeriodo(dataModelo.getDataIni(), dataModelo.getDataFim(), "datainiciogozo", false));
		sql.append(" AND ").append(realizarGeracaoWherePeriodo(dataModelo.getDataIni(), dataModelo.getDataFim(), "datafinalgozo", false));
		sql.append(" ORDER BY codigo DESC");
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
        return (montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados()));
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
	private List<MarcacaoFeriasColetivasVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List<MarcacaoFeriasColetivasVO> lista = new ArrayList<>();
		while(tabelaResultado.next()) {
			lista.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		return lista;
	}

	/**
	 * Consulta o total de marcacao de ferias coletivas para o paginador.
	 * 
	 * @param dataModelo
	 * @return
	 */
	private Integer consultarTotalMarcacaoFeriasColetivas(DataModelo dataModelo, String valorConsultaSituacao) {
		StringBuilder sql = new StringBuilder(getSQLBasicoTotal());
		sql.append(" WHERE 1 = 1");
		sql.append(" AND descricao ILIKE ? ");
		dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);
		if (!valorConsultaSituacao.equals("TODOS")) {
			dataModelo.getListaFiltros().add(valorConsultaSituacao);
			sql.append(" AND situacao = upper(?) ");
		}
		sql.append(" AND ").append(realizarGeracaoWherePeriodo(dataModelo.getDataIni(), dataModelo.getDataFim(), "datainiciogozo", false));
		sql.append(" AND ").append(realizarGeracaoWherePeriodo(dataModelo.getDataIni(), dataModelo.getDataFim(), "datafinalgozo", false));
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}

	/**
	 * Realiza a marcação de ferias pelos filtros informados.
	 * 
	 * @throws Exception 
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void marcarFerias(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {

		if(Uteis.isAtributoPreenchido(marcacaoFeriasColetivasVO.getCodigo())) {
			excluirRegistrosAnteriores(marcacaoFeriasColetivasVO, usuarioVO);
		}
		
		List<FuncionarioCargoVO> funcionarioCargoVOs = consultarFuncionariosPeloFiltroInformado(marcacaoFeriasColetivasVO);

		MarcacaoFeriasVO marcacaoFerias;
		marcacaoFeriasColetivasVO.setSituacao(SituacaoMarcacaoFeriasEnum.MARCADA);
		getFacadeFactory().getMarcacaoFeriasColetivasInterfaceFacade().persistir(marcacaoFeriasColetivasVO, validarAcesso, usuarioVO);

		for (FuncionarioCargoVO funcionarioCargoVO : funcionarioCargoVOs) {
			marcacaoFerias = getFacadeFactory().getMarcacaoFeriasInterfaceFacade().consultarMarcacaoDiferenteDeFechadaPorFuncionario(funcionarioCargoVO.getMatriculaCargo());

			if(Uteis.isAtributoPreenchido(marcacaoFerias)) {
				if(marcacaoFerias.getSituacaoMarcacao().equals(SituacaoMarcacaoFeriasEnum.CALCULADA)) {
					continue;
				}
			} else {
			
				marcacaoFerias = getFacadeFactory().getMarcacaoFeriasInterfaceFacade().inicializarMarcacaoDeFeriasPrimeiroPeriodoAquisitivoVencidoDoFuncionario(funcionarioCargoVO.getMatriculaCargo());
				
				//caso nao haja periodo aquisitivo vencido pegar o primeiro aberto.
				if (!Uteis.isAtributoPreenchido(marcacaoFerias.getPeriodoAquisitivoFeriasVO().getCodigo())) {
					marcacaoFerias = getFacadeFactory().getMarcacaoFeriasInterfaceFacade().inicializarMarcacaoDeFeriasPrimeiroPeriodoAquisitivoAbertoDoFuncionario(funcionarioCargoVO.getMatriculaCargo());
				}
			}

			if (Uteis.isAtributoPreenchido(marcacaoFerias.getPeriodoAquisitivoFeriasVO().getCodigo())) {

				getFacadeFactory().getMarcacaoFeriasInterfaceFacade().montarDadosPorMarcacaoFeriasColetiva(marcacaoFeriasColetivasVO, funcionarioCargoVO, marcacaoFerias);
				getFacadeFactory().getMarcacaoFeriasInterfaceFacade().persistir(marcacaoFerias, false, usuarioVO);
				
				ControleMarcacaoFeriasVO controleMF = getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().montarDadosControleMarcacaoFerias(marcacaoFeriasColetivasVO, marcacaoFerias, funcionarioCargoVO);
				getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().persistir(controleMF, validarAcesso, usuarioVO);
			}
		}
	}

	/**
	 * Exclui os registros anteriores:
	 * - Marcacao de Ferias 
	 * - Historico Marcacao Ferias
	 * @param usuarioVO
	 */
	private void excluirRegistrosAnteriores(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, UsuarioVO usuarioVO) throws Exception {
		excluirMarcacaoFerias(marcacaoFeriasColetivasVO, usuarioVO);
		excluirHistoricoMarcacaoFerias(marcacaoFeriasColetivasVO, usuarioVO);
	}

	private void excluirHistoricoMarcacaoFerias(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().excluirHistoricoPorMarcacaoFeriasColetivas(marcacaoFeriasColetivasVO, usuarioVO);
	}

	private void excluirMarcacaoFerias(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, UsuarioVO usuarioVO) throws Exception {
		List<ControleMarcacaoFeriasVO> listaDeHistoricos = getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().consultarDadosPorMarcacaoFeriasColetivas(marcacaoFeriasColetivasVO, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		
		for(ControleMarcacaoFeriasVO historico : listaDeHistoricos) {
			getFacadeFactory().getMarcacaoFeriasInterfaceFacade().excluirMarcacaoFerias(historico.getMarcacaoFerias(), SituacaoMarcacaoFeriasEnum.MARCADA, usuarioVO);	
		}
		
	}

	/**
	 * Realiza o calculo do recibo de ferias dos funcionarios do historico selecionados pelos filtros na fase de marcacao
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void calcularRecibo(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {

		marcacaoFeriasColetivasVO.setSituacao(SituacaoMarcacaoFeriasEnum.CALCULADA);
		persistir(marcacaoFeriasColetivasVO, validarAcesso, usuarioVO);
		
		List<ControleMarcacaoFeriasVO> listaControleMarcacaoFerias = getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().consultarDadosPorMarcacaoFeriasColetivas(marcacaoFeriasColetivasVO, SituacaoMarcacaoFeriasEnum.MARCADA, Uteis.NIVELMONTARDADOS_TODOS);
		
		for(ControleMarcacaoFeriasVO controleMF : listaControleMarcacaoFerias) {
			//Calcula apenas os recibos dos funcionarios que ainda nao foram calculados
			if(controleMF.getMarcacaoFerias().getSituacaoMarcacao().equals(SituacaoMarcacaoFeriasEnum.MARCADA)) {
				FuncionarioCargoVO funcionarioCargoVO = getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(controleMF.getMarcacaoFerias().getFuncionarioCargoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
				controleMF.getMarcacaoFerias().setFuncionarioCargoVO(funcionarioCargoVO);
				getFacadeFactory().getMarcacaoFeriasInterfaceFacade().calcularRecibo(marcacaoFeriasColetivasVO, controleMF.getMarcacaoFerias(), new ReciboFeriasVO(), validarAcesso, usuarioVO);
			}
		}
	}

	/**
	 * Metodo que cancela os recibos gerados pela marcação de ferias e volta a situação da ferias coletiva para 'MARCADA'.
	 * 
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void cancelarRecibo(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		//marcacaoFeriasColetivasVO = getFacadeFactory().getMarcacaoFeriasColetivasInterfaceFacade().consultarPorChavePrimaria(marcacaoFeriasColetivasVO.getCodigo().longValue());

		List<ControleMarcacaoFeriasVO> listaHistoricoMarcacaoFerias = getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().consultarDadosPorMarcacaoFeriasColetivas(marcacaoFeriasColetivasVO, SituacaoMarcacaoFeriasEnum.CALCULADA, Uteis.NIVELMONTARDADOS_TODOS);
		for (ControleMarcacaoFeriasVO historicoMarcacaoFerias : listaHistoricoMarcacaoFerias) {

			if(historicoMarcacaoFerias.getLancadoAdiantamento() || historicoMarcacaoFerias.getLancadoReciboNoContraCheque()) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_MarcacaoFerias_funcionarioComReciboLancado"));
			}

			//Cancela apenas os recibos dos funcionarios que foram calculados e nao foram lancados adiantamento
			if(historicoMarcacaoFerias.getMarcacaoFerias().getSituacaoMarcacao().equals(SituacaoMarcacaoFeriasEnum.CALCULADA)) {

				getFacadeFactory().getMarcacaoFeriasInterfaceFacade().cancelarRecibo(historicoMarcacaoFerias.getMarcacaoFerias(), validarAcesso, usuarioVO);
				//persistirHistorico(marcacaoFeriasColetivasVO, validarAcesso, usuarioVO, historicoMarcacaoFerias.getMarcacaoFerias(), historicoMarcacaoFerias.getFuncionarioCargo());	

			} else {
				//Caso algum funcionario sai do processo coletivo e tenha sido feito seu processo de forma individual
				//Exclui do historico e nao tem mas seu controle pela marcacao de ferias coletiva
				getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().excluirHistoricoPorMarcacaoDeFeriasEFuncionarioCargo(historicoMarcacaoFerias.getMarcacaoFerias(), historicoMarcacaoFerias.getFuncionarioCargo(), usuarioVO);
			}
		}

		//Volta a  marcacao para a situacao inicial
		marcarFerias(marcacaoFeriasColetivasVO, validarAcesso, usuarioVO);
	}

	/**
	 * finalizar as ferias dos funcionarios pelos filtros informados
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void finalizarFerias(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {

		List<ControleMarcacaoFeriasVO> listaControleMarcacaoFerias = getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().consultarDadosPorFiltrosMarcacaoFeriasColetivas(marcacaoFeriasColetivasVO, SituacaoMarcacaoFeriasEnum.CALCULADA, Uteis.NIVELMONTARDADOS_TODOS);

		marcacaoFeriasColetivasVO.setSituacao(SituacaoMarcacaoFeriasEnum.FECHADA);
		
		for (ControleMarcacaoFeriasVO controleMF : listaControleMarcacaoFerias) {
			//Finaliza apenas os recibos dos funcionarios que foram calculados
			if(controleMF.getMarcacaoFerias().getSituacaoMarcacao().equals(SituacaoMarcacaoFeriasEnum.CALCULADA) && controleMF.getLancadoReciboNoContraCheque()) {
				getFacadeFactory().getMarcacaoFeriasInterfaceFacade().finalizarMarcacaoDeFerias(marcacaoFeriasColetivasVO, controleMF.getMarcacaoFerias(), marcacaoFeriasColetivasVO.getEncerrarPeriodoAquisitivo(), marcacaoFeriasColetivasVO.getDataFechamento(), validarAcesso, usuarioVO);
			} else if(!controleMF.getLancadoReciboNoContraCheque()) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_MarcacaoFerias_FechadaSemLancaRecibo"));
			}
		}
		
		persistir(marcacaoFeriasColetivasVO, validarAcesso, usuarioVO);
	}

	
	/**
	 * Consulta lista de FuncionarioCargo pelos filtros do template contido na marcacao de ferias
	 * @param marcacaoFeriasColetivasVO
	 * @return
	 * @throws Exception
	 * @throws ConsistirException
	 */
	private List<FuncionarioCargoVO> consultarFuncionariosPeloFiltroInformado(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO) throws Exception {
		List<FuncionarioCargoVO> funcionarioCargoVOs =  getFacadeFactory().getFuncionarioCargoFacade().consultarCargoFuncionarioPorFiltrosTemplateFolhaPagamento(marcacaoFeriasColetivasVO.getTemplateLancamentoFolhaPagamentoVO(), Uteis.NIVELMONTARDADOS_TODOS);

		if (!Uteis.isAtributoPreenchido(funcionarioCargoVOs)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_padrao_FuncionarioNaoEncontradoPeloFiltro"));
		}
		return funcionarioCargoVOs;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		MarcacaoFeriasColetivas.idEntidade = idEntidade;
	}
	
}