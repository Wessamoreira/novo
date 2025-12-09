package negocio.facade.jdbc.recursoshumanos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.FuncionarioDependenteVO;
import negocio.comuns.academico.enumeradores.SituacaoTipoAdvertenciaEnum;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO.EnumCampoConsultaCompetencia;
import negocio.comuns.recursoshumanos.CompetenciaPeriodoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.ControleMarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.EventoEmprestimoCargoFuncionarioVO;
import negocio.comuns.recursoshumanos.EventoFixoCargoFuncionarioVO;
import negocio.comuns.recursoshumanos.HistoricoSituacaoVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.PeriodoAquisitivoFeriasVO;
import negocio.comuns.recursoshumanos.ValorReferenciaFolhaPagamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.Conexao;
import negocio.interfaces.recursoshumanos.CompetenciaFolhaPagamentoInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>CompetenciaFolhaPagamento</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>CompetenciaFolhaPagamentoVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class CompetenciaFolhaPagamento extends SuperFacade<CompetenciaFolhaPagamentoVO> implements CompetenciaFolhaPagamentoInterfaceFacade<CompetenciaFolhaPagamentoVO> {

	private static final long serialVersionUID = 2703709295471428697L;

	protected static String idEntidade;

	public CompetenciaFolhaPagamento() {
		super();
		setIdEntidade("CompetenciaFolhaPagamento");
	}
	
	public CompetenciaFolhaPagamento(Conexao conexao, FacadeFactory facade) {
		super();
		setConexao(conexao);
		setFacadeFactory(facade);
		setIdEntidade("CompetenciaFolhaPagamento");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(CompetenciaFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == null || obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
		
		persistirPeriodosDaCompetencia(obj, usuarioVO);
		
	}
	
	private void persistirPeriodosDaCompetencia(CompetenciaFolhaPagamentoVO obj, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getCompetenciaPeriodoFolhaPagamentoInterfaceFacade().persistirTodos(obj, false, usuario);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(CompetenciaFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			CompetenciaFolhaPagamento.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			incluir(obj, "competenciafolhapagamento", new AtributoPersistencia()
					.add("datacompetencia", Uteis.getDataJDBC(obj.getDataCompetencia()))
					.add("datacaixa", Uteis.getDataJDBC(obj.getDataCaixa()))
					.add("datafechamento", Uteis.getDataJDBC(obj.getDataFechamento()))
					.add("situacao", obj.getSituacao())
					.add("usuarioUltimaAlteracao", obj.getUsuarioUltimaAlteracao().getCodigo())
					.add("dataUltimaAlteracao", obj.getDataUltimaAlteracao())
					.add("quantidadediasuteis", obj.getQuantidadeDiasUteis())
					.add("quantidadeDiasUteisMeioExpediente", obj.getQuantidadeDiasUteisMeioExpediente())
					, usuarioVO);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterar(CompetenciaFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		CompetenciaFolhaPagamento.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		alterar(obj, "competenciafolhapagamento", new AtributoPersistencia()
				.add("datacompetencia", Uteis.getDataJDBC(obj.getDataCompetencia()))
				.add("datacaixa", Uteis.getDataJDBC(obj.getDataCaixa()))
				.add("datafechamento", Uteis.getDataJDBC(obj.getDataFechamento()))
				.add("situacao", obj.getSituacao())
				.add("usuarioUltimaAlteracao", obj.getUsuarioUltimaAlteracao().getCodigo())
				.add("dataUltimaAlteracao", obj.getDataUltimaAlteracao())
				.add("quantidadediasuteis", obj.getQuantidadeDiasUteis())
				.add("quantidadeDiasUteisMeioExpediente", obj.getQuantidadeDiasUteisMeioExpediente())
				, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(CompetenciaFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		CompetenciaFolhaPagamento.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM competenciafolhapagamento WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
	}

	@Override
	public void validarDados(CompetenciaFolhaPagamentoVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getDataCompetencia())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CompetenciaFolhaPagamento_dataCompetencia"));
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	@Override
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, Date dataCompetencia) throws Exception {
		List<CompetenciaFolhaPagamentoVO> objs = new ArrayList<>(0);
		dataModelo.getListaFiltros().clear();
		dataModelo.setNivelMontarDados(Uteis.NIVELMONTARDADOS_TODOS);

		switch (EnumCampoConsultaCompetencia.valueOf(dataModelo.getCampoConsulta())) {
		case DATA_COMPETENCIA:
			objs = consultarCompetenciaFolhaPagamento(dataModelo, "datacompetencia", dataCompetencia);
			dataModelo.setTotalRegistrosEncontrados(consultarTotalCompetenciaFolhaPagamento(dataModelo, "datacompetencia", dataCompetencia));
			break;
		case CODIGO:
			dataModelo.getListaFiltros().add(Uteis.isAtributoPreenchido(dataModelo.getValorConsulta()) ? Integer.parseInt(dataModelo.getValorConsulta()) : dataModelo.getValorConsulta());
			objs = consultarCompetenciaFolhaPagamentoCodigo(dataModelo, "codigo", dataCompetencia);
			dataModelo.setTotalRegistrosEncontrados(consultarTotalCompetenciaFolhaPagamento(dataModelo, "codigo", dataCompetencia));
			break;
		default:
			break;
		}
		dataModelo.setListaConsulta(objs);
	}

	private List<CompetenciaFolhaPagamentoVO> consultarCompetenciaFolhaPagamento(DataModelo dataModelo, String campoConsulta, Date dataCompetencia) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM competenciafolhapagamento WHERE 1=1");
        if (Uteis.isAtributoPreenchido(dataCompetencia)) {
        	sql.append(" AND EXTRACT('Month' FROM dataCompetencia) = ").append(String.valueOf(UteisData.getMesData(dataCompetencia)));
        	sql.append(" AND EXTRACT('Year' FROM dataCompetencia) = ").append(String.valueOf(UteisData.getAnoData(dataCompetencia)));
        }
        if (Uteis.isAtributoPreenchido(dataModelo.getValorConsulta())) {
        	dataModelo.getListaFiltros().add(dataModelo.getValorConsulta());
        	sql.append(" AND ").append(campoConsulta).append(" = ?");
        }
        sql.append(" ORDER BY codigo DESC");

        UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());

        List<CompetenciaFolhaPagamentoVO> lista = new ArrayList<>();
        while(tabelaResultado.next()) {
        	lista.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }

        return lista;
	}
	
	private List<CompetenciaFolhaPagamentoVO> consultarCompetenciaFolhaPagamentoCodigo(DataModelo dataModelo, String campoConsulta, Date dataCompetencia) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM competenciafolhapagamento");
        sql.append(" WHERE 1=1");
        
        if (Uteis.isAtributoPreenchido(dataModelo.getListaFiltros().get(0))) {
        	sql.append(" and codigo = ? ");
        }
        
        if (Uteis.isAtributoPreenchido(dataCompetencia)) {
        	sql.append(" AND EXTRACT('Month' FROM dataCompetencia) = ").append(String.valueOf(UteisData.getMesData(dataCompetencia)));
        }
        sql.append(" ORDER BY codigo DESC");

        UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());

        List<CompetenciaFolhaPagamentoVO> lista = new ArrayList<>();
        while(tabelaResultado.next()) {
        	lista.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }

        return lista;
	}

	private Integer consultarTotalCompetenciaFolhaPagamento(DataModelo dataModelo, String campoConsulta, Date dataCompetencia) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT(codigo) as qtde FROM competenciafolhapagamento WHERE 1=1");
        if (Uteis.isAtributoPreenchido(dataCompetencia)) {
        	sql.append(" AND EXTRACT('Month' FROM dataCompetencia) = ").append(String.valueOf(UteisData.getMesData(dataCompetencia)));
        	sql.append(" AND EXTRACT('Year' FROM dataCompetencia) = ").append(String.valueOf(UteisData.getAnoData(dataCompetencia)));
        }

        if (Uteis.isAtributoPreenchido(dataModelo.getValorConsulta())) {
        	sql.append(" AND ").append(campoConsulta).append(" = ?");
        }

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());

        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	@Override
	public CompetenciaFolhaPagamentoVO consultarCompetenciaFolhaPagamentoPorMesAno(Integer mes, Integer ano, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), validarAcesso, usuarioVO);

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM competenciafolhapagamento");
		sql.append(" WHERE EXTRACT('Month' FROM dataCompetencia) = ?");
		sql.append(" AND EXTRACT('Year' FROM dataCompetencia) = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), mes, ano);
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		}
		return new CompetenciaFolhaPagamentoVO();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public CompetenciaFolhaPagamentoVO consultarPorChavePrimaria(Long id) throws Exception {
		String sql = " SELECT * FROM competenciafolhapagamento WHERE codigo = ?";
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, id);
        if (rs.next()) {
            return montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
        }
        throw new Exception("Dados não encontrados (Competência Folha Pagamento).");
	}

	@Override
	public CompetenciaFolhaPagamentoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		CompetenciaFolhaPagamentoVO obj = new CompetenciaFolhaPagamentoVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDataCompetencia(tabelaResultado.getDate("datacompetencia"));
		obj.setDataCaixa(tabelaResultado.getDate("datacaixa"));
		obj.setDataFechamento(tabelaResultado.getDate("datafechamento"));
		obj.setQuantidadeDiasUteis(tabelaResultado.getInt("quantidadediasuteis"));
		obj.setQuantidadeDiasUteisMeioExpediente(tabelaResultado.getInt("quantidadediasuteismeioexpediente"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("situacao"))) {
			obj.setSituacao(SituacaoTipoAdvertenciaEnum.valueOf(tabelaResultado.getString("situacao")));
		}
		
		if(tabelaResultado.getInt("usuarioultimaalteracao") > 0)
			obj.setUsuarioUltimaAlteracao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(tabelaResultado.getInt("usuarioultimaalteracao"), Uteis.NIVELMONTARDADOS_COMBOBOX, null));
		
		obj.setDataUltimaAlteracao(tabelaResultado.getDate("dataultimaalteracao"));
		
		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public CompetenciaFolhaPagamentoVO consultarCompetenciaAtiva(Boolean retornarExcecao) throws Exception {
		
		StringBuilder sql = new StringBuilder(" SELECT * FROM competenciafolhapagamento WHERE situacao = '")
				.append(SituacaoTipoAdvertenciaEnum.ATIVO.name()).append("'");
		
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if (rs.next()) {
            return montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
        }
        if(retornarExcecao != null && retornarExcecao) {
        	throw new Exception("Dados não encontrados (Competência Folha Pagamento).");
        }
        return null;
	}

	/**
	 * Consulta a data de competencia ativa.
	 * 
	 * @return Data competencia ativa.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Date consultarDataCompetenciaAtiva() throws Exception {
		
		StringBuilder sql = new StringBuilder(" SELECT dataCompetencia FROM competenciafolhapagamento WHERE situacao = ?");
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), SituacaoTipoAdvertenciaEnum.ATIVO.name());
		if (rs.next()) {
			return rs.getDate("dataCompetencia");
		}
		throw new Exception("Dados não encontrados (Data de Competência Folha Pagamento).");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void encerrarVigencia(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, UsuarioVO usuario) throws Exception {
		
		this.alterarDadosEncerramentoDaVigencia(competenciaFolhaPagamentoVO, usuario);
		
		StringBuilder sql = new StringBuilder(" update competenciafolhapagamento set situacao = '").append(SituacaoTipoAdvertenciaEnum.INATIVO.name()).append("'")
				.append(" WHERE situacao = '").append(SituacaoTipoAdvertenciaEnum.ATIVO.name()).append("'");
		
		getConexao().getJdbcTemplate().update(sql.toString());
		
	}

	/**
	 * Valida as regras de negocio para poder encerrar a competência.
	 * 
	 * @param competenciaFolhaPagamentoVO
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterarDadosEncerramentoDaVigencia(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, UsuarioVO usuario) throws Exception {
		montarDadosDataModelo(usuario);

		validarControleMarcacaoFeriasPorDataCompetenciaDataInicioGozoDaMarcacaoFerias(competenciaFolhaPagamentoVO);
		finalizarMarcacaoFeriasPeriodoAquisitivo(competenciaFolhaPagamentoVO, usuario);

		validarEventosFixos(competenciaFolhaPagamentoVO, usuario);
		validarFuncionariosDependentes();
		validarEmprestimos(competenciaFolhaPagamentoVO, usuario);
		validarPensao(competenciaFolhaPagamentoVO, usuario);

		//validarValeTransporte(competenciaFolhaPagamentoVO, usuario);
		validarValoresReferencia(usuario);
		validarFeriasAtivas(competenciaFolhaPagamentoVO, usuario);
		validarFinalDeFerias(competenciaFolhaPagamentoVO, usuario);
		validarPeriodoAquisitivoDeFerias(competenciaFolhaPagamentoVO);
		validarQuantidadeFaltasNoPeriodoAquisitivoAbertoEVencido(usuario);
	}

	/**
	 * Metodo Finaliza a {@link MarcacaoFeriasVO} e realiza o fechamento
	 * do {@link PeriodoAquisitivoFeriasVO}.
	 * 
	 * @param competenciaFolhaPagamentoVO
	 * @throws Exception
	 */
	private void finalizarMarcacaoFeriasPeriodoAquisitivo(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, UsuarioVO usuario) throws Exception {
		List<ControleMarcacaoFeriasVO> listaControleMarcacaoFerias = getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().consultarPorDataCompetenciaDataInicioGozoDaMarcacaoFerias(competenciaFolhaPagamentoVO.getDataCompetencia(), true);

		for (ControleMarcacaoFeriasVO controleMarcacaoFeriasVO : listaControleMarcacaoFerias) {
			MarcacaoFeriasVO marcacaoFeriasVO = getFacadeFactory().getMarcacaoFeriasInterfaceFacade().consultarPorChavePrimaria(controleMarcacaoFeriasVO.getMarcacaoFerias().getCodigo(), Uteis.NIVELMONTARDADOS_PROCESSAMENTO);
			getFacadeFactory().getMarcacaoFeriasInterfaceFacade().finalizarMarcacaoDeFerias(marcacaoFeriasVO, true, new Date(), false, usuario);
		}
	}

	/**
	 * Atualiza a quantidade de faltas no periodo aquisitivo .
	 * 
	 * @param usuario
	 * @throws Exception
	 */
	private void validarQuantidadeFaltasNoPeriodoAquisitivoAbertoEVencido(UsuarioVO usuario) throws Exception {
		List<PeriodoAquisitivoFeriasVO> periodoAquisitivoFeriasVOs = getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().consultarPeriodoAquisitivoAbertoEVencido();

		for (PeriodoAquisitivoFeriasVO periodoAquisitivoFeriasVO : periodoAquisitivoFeriasVOs) {
			Integer quantidadeFaltas = getFacadeFactory().getFaltasFuncionarioInterfaceFacade().consultarQtdFaltasDoPeriodo(periodoAquisitivoFeriasVO.getFuncionarioCargo(), periodoAquisitivoFeriasVO.getInicioPeriodo(), periodoAquisitivoFeriasVO.getFinalPeriodo(),  Boolean.TRUE);

			if (quantidadeFaltas > 0) {
				periodoAquisitivoFeriasVO.setQtdFalta(quantidadeFaltas);
				getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().alterar(periodoAquisitivoFeriasVO, false, usuario);
			}
		}
	}

	/**
	 * Validar se não existe contra cheque lançado para os Recibos de ferias calculados.
	 *  
	 * @param competenciaFolhaPagamentoVO
	 * @throws Exception
	 */
	private void validarControleMarcacaoFeriasPorDataCompetenciaDataInicioGozoDaMarcacaoFerias(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) throws Exception {
		List<ControleMarcacaoFeriasVO> lista = getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().consultarPorDataCompetenciaDataInicioGozoDaMarcacaoFerias(competenciaFolhaPagamentoVO.getDataCompetencia(), false);

		if (Uteis.isAtributoPreenchido(lista)) {
			StringBuilder mensagem = new StringBuilder();
			for (ControleMarcacaoFeriasVO controleMarcacaoFeriasVO : lista) {
				mensagem.append("<br/> - " + controleMarcacaoFeriasVO.getNomeFuncionario() + " - Matricula:" + controleMarcacaoFeriasVO.getMatriculaCargo());
			}
			throw new ConsistirException(UteisJSF.internacionalizar("msg_MarcacaoFerias_FechadaSemLancaRecibo") + mensagem.toString());
		}
	}

	@Override
	public void validarFinalDeFerias(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, UsuarioVO usuario) throws Exception {
		List<ControleMarcacaoFeriasVO> listaControleMarcacaoFeriasFinalizadas = getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().consultarFinalDasFeriasDoFuncionarios(new Date(), Uteis.NIVELMONTARDADOS_TODOS);
		for (ControleMarcacaoFeriasVO controleMarcacao : listaControleMarcacaoFeriasFinalizadas) {
			try {
				getFacadeFactory().getMarcacaoFeriasInterfaceFacade().finalizarMarcacaoDeFerias(controleMarcacao.getMarcacaoFerias(), false, null, false, usuario);
				
				//Gera Historico situacao funcionario para Ativo
				gerarHistoricoSituacaoFuncionario(usuario, controleMarcacao.getFuncionarioCargo().getCodigo(), SituacaoFuncionarioEnum.ATIVO);
			} catch (Exception e) {
				throw new ConsistirException(controleMarcacao.getMatriculaCargo() + " - " + controleMarcacao.getNomeFuncionario() + ":" + e.getMessage());
			}
		}
	}

	@Override
	public void validarFeriasAtivas(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, UsuarioVO usuario) throws Exception {
		List<Integer> listaFuncionarioEntrarFerias = getFacadeFactory().getMarcacaoFeriasInterfaceFacade().consultarFuncionariosMarcacaoFerias();
		for (Integer codigoFuncionario : listaFuncionarioEntrarFerias) {
			getFacadeFactory().getFuncionarioCargoFacade().alterarSituacaoFuncionario(codigoFuncionario, SituacaoFuncionarioEnum.FERIAS.getValor(), usuario);

			//Gera Historico situacao funcionario Inativo
			gerarHistoricoSituacaoFuncionario(usuario, codigoFuncionario, SituacaoFuncionarioEnum.FERIAS);
		}
	}

	/**
	 * Gera o historico de situação pelo funcionario cargo informado. 
	 * 
	 * @param usuario
	 * @param codigoFuncionario
	 * @param situacao
	 * @throws Exception
	 */
	private void gerarHistoricoSituacaoFuncionario(UsuarioVO usuario, Integer codigoFuncionario, SituacaoFuncionarioEnum situacao) throws Exception {
		FuncionarioCargoVO funcionarioCargo = getFacadeFactory().getFuncionarioCargoFacade().consultarPorCodigo(codigoFuncionario, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);

		HistoricoSituacaoVO historicoSituacaoVO = getFacadeFactory().getHistoricoSituacaoInterfaceFacade().montarDadosPorSituacaoFuncionario(funcionarioCargo, situacao.getValor());
		getFacadeFactory().getHistoricoSituacaoInterfaceFacade().persistir(historicoSituacaoVO, false, usuario);
	}

	@Override
	public void validarValoresReferencia(UsuarioVO usuario) {
		List<ValorReferenciaFolhaPagamentoVO> listaValoresReferencia = getFacadeFactory().getValorReferenciaFolhaPagamentoInterfaceFacade().consultarAtivosFinalVigenciaSelecionada();
		for (ValorReferenciaFolhaPagamentoVO valorReferenciaFolhaPagamentoVO : listaValoresReferencia) {
			Calendar dataFinalVigencia = Calendar.getInstance();
			dataFinalVigencia.setTime(valorReferenciaFolhaPagamentoVO.getDataFimVigencia());
			dataFinalVigencia.set(Calendar.YEAR, dataFinalVigencia.get(Calendar.YEAR) + 1);

			Calendar dataAtual  = Calendar.getInstance();
			dataAtual.setTime(new Date());

			int mesDataAtual = dataAtual.get(Calendar.MONTH);
			int mesVigencia = dataFinalVigencia.get(Calendar.MONTH);

			if (mesVigencia == mesDataAtual || (mesVigencia- 1) == mesDataAtual) {
				valorReferenciaFolhaPagamentoVO.setDataFimVigencia(dataFinalVigencia.getTime());
				getFacadeFactory().getValorReferenciaFolhaPagamentoInterfaceFacade().alterarFinalVigencia(valorReferenciaFolhaPagamentoVO, usuario);
			}
		}
	}

	private void validarPeriodoAquisitivoDeFerias(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) throws Exception {
		List<FuncionarioCargoVO> listaFuncionarioCargo = getFacadeFactory().getFuncionarioCargoFacade().consultarListaFuncionarioPorSituacaoDiferenteDeDemitidoLicencaSemVencimentoOutros(Boolean.TRUE, Uteis.NIVELMONTARDADOS_PROCESSAMENTO);

		if(listaFuncionarioCargo != null && !listaFuncionarioCargo.isEmpty()) {

			for(FuncionarioCargoVO funcionarioCargoVO : listaFuncionarioCargo) {
				//Atualiza periodo aquisitivo dos funcionarios ativos
				getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().
					realizarAtualizacaoDoPeriodoAquisitivoDoFuncionario(competenciaFolhaPagamentoVO, funcionarioCargoVO);
			}
		}
	}

	public void validarValeTransporte(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, UsuarioVO usuario) {
		getFacadeFactory().getEventoValeTransporteFuncionarioCargoInterfaceFacade().alterarDiasValeTransporte(
				competenciaFolhaPagamentoVO.getQuantidadeDiasUteis(), competenciaFolhaPagamentoVO.getQuantidadeDiasUteisMeioExpediente(), usuario);
	}

	private void validarEmprestimos(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, UsuarioVO usuario) throws Exception {
		List<EventoEmprestimoCargoFuncionarioVO> listaEventosEmprestimo = getFacadeFactory().getEventoEmprestimoCargoFuncionarioInterfaceFacade().consultarPorCompetenciaEventoEmprestimo(competenciaFolhaPagamentoVO.getCodigo().longValue());
		for (EventoEmprestimoCargoFuncionarioVO eventoEmprestimo : listaEventosEmprestimo) {
			getFacadeFactory().getEventoEmprestimoCargoFuncionarioInterfaceFacade().alterarParcelaEmprestimo(eventoEmprestimo, usuario);

		}
		getFacadeFactory().getEventoEmprestimoCargoFuncionarioInterfaceFacade().alterarSituacaoQuitado(usuario);
	}

	private void validarPensao(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, UsuarioVO usuario) throws Exception {
		SqlRowSet pesquisaPensao = getFacadeFactory().getContraChequeEventoInterfaceFacade().consultarEventosDePensaoLancadosNoContraCheque(competenciaFolhaPagamentoVO);
		
		if(pesquisaPensao == null)
			return;
		
		do {
			
			FuncionarioDependenteVO dependentePensao = getFacadeFactory().getFuncionarioDependenteInterfaceFacade().consultarPorChavePrimaria(pesquisaPensao.getInt("dependentePensao"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			BigDecimal valorPensao = pesquisaPensao.getBigDecimal("valorPensao");
			
			//Gravar Historico de pensao
			getFacadeFactory().getHistoricoPensaoInterfaceFacade().gravar(competenciaFolhaPagamentoVO, dependentePensao, valorPensao, usuario);
			
		} while (pesquisaPensao.next());

	}
	
	/**
	 * Valida os dados da Regra de Negocio do dependente (Frequencia Escolar, Carteira Vacinação, Idade maior que 21, Universitario)
	 * é altera os campos relativos as validações.
	 * 
	 * @throws Exception
	 */
	private void validarFuncionariosDependentes() throws Exception {
		List<FuncionarioDependenteVO> dependentes = getFacadeFactory().getFuncionarioDependenteInterfaceFacade().consultarFuncionariosDependentes();

		for (FuncionarioDependenteVO funcionarioDependenteVO : dependentes) {
			if (Uteis.isAtributoPreenchido(funcionarioDependenteVO.getDataNascimento())) {
				getFacadeFactory().getFuncionarioDependenteInterfaceFacade().validarDadosDependente(funcionarioDependenteVO);
				getFacadeFactory().getFuncionarioDependenteInterfaceFacade().alterarFuncionariosDependentes(funcionarioDependenteVO);
			}
		}
	}

	private void validarEventosFixos(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, UsuarioVO usuario) {
		alterarParcelasEventosFixosLancadoNoContraCheque(competenciaFolhaPagamentoVO, usuario);
		
		excluirEventosFixosParcelasRezados(competenciaFolhaPagamentoVO, usuario);
	}

	/**
	 * Exclui todos os eventos fixos com valores rezados.
	 * 
	 * @param competenciaFolhaPagamentoVO
	 * @param usuario
	 */
	private void excluirEventosFixosParcelasRezados(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO,UsuarioVO usuario) {
		List<EventoFixoCargoFuncionarioVO> listaEventoFixo = getFacadeFactory().getEventoFixoCargoFuncionarioInterfaceFacade().consultarEventoFixoZerado(usuario);
		for (EventoFixoCargoFuncionarioVO eventoFixoCargoFuncionarioVO : listaEventoFixo) {
			getFacadeFactory().getEventoFixoCargoFuncionarioInterfaceFacade().excluir(eventoFixoCargoFuncionarioVO, false, usuario);
		}
	}

	/**
	 * Altera o numero de lancamento do evento fixo caso o mesmo tenho sido lançado no {@link ContraChequeVO} 
	 * 
	 * @param competenciaFolhaPagamentoVO
	 * @param usuario
	 */
	private void alterarParcelasEventosFixosLancadoNoContraCheque(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, UsuarioVO usuario) {
		List<EventoFixoCargoFuncionarioVO> listaEventoFixo = getFacadeFactory().getEventoFixoCargoFuncionarioInterfaceFacade().consultarPorCompetenciaEventoFixo(competenciaFolhaPagamentoVO.getCodigo().longValue(), usuario);
		for (EventoFixoCargoFuncionarioVO eventoFixoCargoFuncionarioVO : listaEventoFixo) {
			getFacadeFactory().getEventoFixoCargoFuncionarioInterfaceFacade().alterarNumeroLancamentoEventoFixo(eventoFixoCargoFuncionarioVO, usuario);
		}
	}

	private void montarDadosDataModelo(UsuarioVO usuario) {
		DataModelo dataModelo = new DataModelo();
		dataModelo.setControlarAcesso(Boolean.FALSE);
		dataModelo.setNivelMontarDados(Uteis.NIVELMONTARDADOS_COMBOBOX);
		dataModelo.setUsuario(usuario);
	}

	@Override
	public void clonarVigencia(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) {
		try {
			CompetenciaFolhaPagamentoVO competenciaNova = (CompetenciaFolhaPagamentoVO) BeanUtils.cloneBean(competenciaFolhaPagamentoVO);
			
			competenciaNova.setDataCaixa(UteisData.obterDataFuturaAdicionandoMes(competenciaFolhaPagamentoVO.getDataCaixa(), 1));
			competenciaNova.setDataFechamento(UteisData.obterDataFuturaAdicionandoMes(competenciaFolhaPagamentoVO.getDataFechamento(), 1));
			competenciaNova.setDataCompetencia(UteisData.obterDataFuturaAdicionandoMes(competenciaFolhaPagamentoVO.getDataCompetencia(), 1));
			competenciaNova.setSituacao(SituacaoTipoAdvertenciaEnum.ATIVO);
			competenciaNova.setUsuarioUltimaAlteracao(competenciaFolhaPagamentoVO.getUsuarioUltimaAlteracao());
			competenciaNova.setDataUltimaAlteracao(competenciaFolhaPagamentoVO.getDataUltimaAlteracao());

			incluir(competenciaNova, false, null);
			
			for(CompetenciaPeriodoFolhaPagamentoVO periodo : competenciaNova.getPeriodos()) {
				periodo.setCompetenciaFolhaPagamento(competenciaNova);
				getFacadeFactory().getCompetenciaPeriodoFolhaPagamentoInterfaceFacade().incluir(periodo, false, null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void validarFuncionarioQueRetornouDeFerias(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, UsuarioVO usuarioVO) throws Exception{
		List<ControleMarcacaoFeriasVO> listaControleMarcacaoFeriasFinalizadas = getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().consultarFuncionariosComFinalDasFeriasMaioQueCompetencia(new Date(), Uteis.NIVELMONTARDADOS_TODOS);
		for (ControleMarcacaoFeriasVO controleMarcacao : listaControleMarcacaoFeriasFinalizadas) {

			getFacadeFactory().getFuncionarioCargoFacade().alterarSituacaoFuncionario(controleMarcacao.getMarcacaoFerias().getFuncionarioCargoVO().getCodigo(), SituacaoFuncionarioEnum.ATIVO.getValor(), usuarioVO);

			//Gera Historico situacao funcionario para Ativo
			gerarHistoricoSituacaoFuncionario(usuarioVO, controleMarcacao.getFuncionarioCargo().getCodigo(), SituacaoFuncionarioEnum.ATIVO);
		}
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		CompetenciaFolhaPagamento.idEntidade = idEntidade;
	}
}