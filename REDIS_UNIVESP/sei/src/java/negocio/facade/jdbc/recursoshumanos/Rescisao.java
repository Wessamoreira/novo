package negocio.facade.jdbc.recursoshumanos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.HistoricoSituacaoVO;
import negocio.comuns.recursoshumanos.RescisaoIndividualVO;
import negocio.comuns.recursoshumanos.RescisaoVO;
import negocio.comuns.recursoshumanos.RescisaoVO.EnumCampoConsultaRescisao;
import negocio.comuns.recursoshumanos.enumeradores.MotivoDemissaoEnum;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoRescisaoEnum;
import negocio.comuns.recursoshumanos.enumeradores.TipoDemissaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.Conexao;
import negocio.interfaces.recursoshumanos.RescisaoInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>RescisaoVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>RescisaoVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class Rescisao  extends SuperFacade<RescisaoVO> implements RescisaoInterfaceFacade<RescisaoVO> {

	private static final long serialVersionUID = 1L;
	private static final String RESCISAO = "Rescisao";

	protected static String idEntidade;

	public Rescisao() throws Exception {
		super();
		setIdEntidade(RESCISAO);
	}

	public Rescisao(Conexao conexao, FacadeFactory facade) {
		super();
		setConexao(conexao);
		setFacadeFactory(facade);
		setIdEntidade(RESCISAO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void rescindirContrato(RescisaoVO rescisaoVO , boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {

		getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().persistir(rescisaoVO.getTemplateLancamentoFolhaPagamento(), validarAcesso, usuarioVO);
		this.persistir(rescisaoVO, validarAcesso, usuarioVO);

		//Consultar funcionarios por filtro (Template)
		List<FuncionarioCargoVO> funcionarioCargoVOs = getFacadeFactory().getFuncionarioCargoFacade().consultarCargoFuncionarioPorFiltrosTemplateFolhaPagamento(rescisaoVO.getTemplateLancamentoFolhaPagamento(), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO = getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarCompetenciaAtiva(true);

		for (FuncionarioCargoVO funcionarioCargoVO : funcionarioCargoVOs) {
			getFacadeFactory().getContraChequeInterfaceFacade().validarSeExisteContraChequeParaFuncionarioCargo(rescisaoVO.getCompetenciaFolhaPagamento(), funcionarioCargoVO);

			boolean existeRescisaoDoFuncionario = getFacadeFactory().getRescisaoIndividualInterfaceFacade().validarSeExisteRescisaoParaFuncionarioCargo(funcionarioCargoVO, competenciaFolhaPagamentoVO);

			if (!existeRescisaoDoFuncionario) {
				getFacadeFactory().getFuncionarioCargoFacade().alterarSituacaoFuncionarioDataDemissao(funcionarioCargoVO.getCodigo(), rescisaoVO.getDataDemissao(),  SituacaoFuncionarioEnum.DEMITIDO.name(), usuarioVO);
	
				HistoricoSituacaoVO historicoSituacaoVO = getFacadeFactory().getHistoricoSituacaoInterfaceFacade().montarDadosSituacaoDemitido(funcionarioCargoVO, rescisaoVO.getDataDemissao());
				getFacadeFactory().getHistoricoSituacaoInterfaceFacade().persistir(historicoSituacaoVO, false, usuarioVO);
	
				RescisaoIndividualVO rescisaoIndividualVO = getFacadeFactory().getRescisaoIndividualInterfaceFacade().montarDados(rescisaoVO, funcionarioCargoVO, historicoSituacaoVO);
				getFacadeFactory().getRescisaoIndividualInterfaceFacade().persistir(rescisaoIndividualVO, false, usuarioVO);
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void cancelarRescisao(RescisaoVO rescisaoVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (Uteis.isAtributoPreenchido(rescisaoVO.getCodigo())) {
			List<RescisaoIndividualVO> rescisaoIndividualVOs = getFacadeFactory().getRescisaoIndividualInterfaceFacade().consultarPorFormaContratacaoFuncionario(rescisaoVO);

			for (RescisaoIndividualVO rescisaoIndividual : rescisaoIndividualVOs) {

				getFacadeFactory().getContraChequeInterfaceFacade().validarSeExisteContraChequeParaFuncionarioCargo(rescisaoVO.getCompetenciaFolhaPagamento(), rescisaoIndividual.getFuncionarioCargo());

				getFacadeFactory().getFuncionarioCargoFacade().alterarSituacaoFuncionario(rescisaoIndividual.getFuncionarioCargo().getCodigo(), SituacaoFuncionarioEnum.ATIVO.name(), usuarioVO);

				getFacadeFactory().getRescisaoIndividualInterfaceFacade().excluir(rescisaoIndividual, false, usuarioVO);

				getFacadeFactory().getHistoricoSituacaoInterfaceFacade().excluir(rescisaoIndividual.getHistoricoSituacao(), false, usuarioVO);
				
				getFacadeFactory().getFuncionarioCargoFacade().alterarSituacaoFuncionarioDataDemissao(rescisaoIndividual.getFuncionarioCargo().getCodigo(), null,  SituacaoFuncionarioEnum.ATIVO.name(), usuarioVO);
			}

			getFacadeFactory().getRescisaoInterfaceFacade().alterar(rescisaoVO, false, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(RescisaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void incluir(RescisaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		Rescisao.incluir(getIdEntidade(), validarAcesso, usuarioVO);

		incluir(obj, RESCISAO,
				new AtributoPersistencia()
						.add("tipodemissao", obj.getTipoDemissao())
						.add("motivoDemissao", obj.getMotivoDemissao())
						.add("dataDemissao", Uteis.getDataJDBCTimestamp(obj.getDataDemissao()))
						.add("competenciaFolhaPagamento", obj.getCompetenciaFolhaPagamento().getCodigo() == 0 ? null : obj.getCompetenciaFolhaPagamento().getCodigo())
						.add("templateLancamentoFolhaPagamento", obj.getTemplateLancamentoFolhaPagamento().getCodigo() == 0 ? null : obj.getTemplateLancamentoFolhaPagamento().getCodigo())
						.add("situacao", obj.getSituacao().name()), usuarioVO);
		obj.setNovoObj(Boolean.TRUE);
	}

	@Override
	public void alterar(RescisaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		Rescisao.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		alterar(obj, RESCISAO, 
				new AtributoPersistencia()
					.add("tipodemissao", obj.getTipoDemissao())
					.add("motivoDemissao", obj.getMotivoDemissao())
					.add("dataDemissao", Uteis.getDataJDBCTimestamp(obj.getDataDemissao()))
					.add("competenciaFolhaPagamento", obj.getCompetenciaFolhaPagamento().getCodigo() == 0 ? null : obj.getCompetenciaFolhaPagamento().getCodigo())
					.add("templateLancamentoFolhaPagamento", obj.getTemplateLancamentoFolhaPagamento().getCodigo() == 0 ? null : obj.getTemplateLancamentoFolhaPagamento().getCodigo())
					.add("situacao", obj.getSituacao().name()),
					new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
		obj.setNovoObj(Boolean.FALSE);
	}

	/**
	 * Exclui o {@link RescisaoVO} pelo codigo informado.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(RescisaoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		Rescisao.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM Rescisao WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
	}
	
	/**
	 * Consulta o {@link RescisaoVO} pelo  codigo informado.
	 */
	@Override
	public RescisaoVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE rescisao.codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	/**
	 * Valida os campos obrigatorios do {@link RescisaoVO}
	 */
	@Override
	public void validarDados(RescisaoVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getTemplateLancamentoFolhaPagamento())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_Rescisao_funcionarioCargo"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getTipoDemissao())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_Rescisao_tipoAfastamento"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getMotivoDemissao())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_Rescisao_motivoAfastamento"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getDataDemissao())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_Rescisao_dataInicio"));
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception {
		dataModelo.getListaFiltros().clear();

		dataModelo.setListaConsulta(consultarRescisao(dataModelo));
		dataModelo.setTotalRegistrosEncontrados(consultarTotalRescisao(dataModelo));
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultarPorFiltros(DataModelo dataModelo, Date dataInicial, Date dataFinal) throws Exception {
		dataModelo.getListaFiltros().clear();

		dataModelo.setDataIni(dataInicial);
		dataModelo.setDataFim(dataFinal);
		dataModelo.setListaConsulta(consultarRescisao(dataModelo));
		dataModelo.setTotalRegistrosEncontrados(consultarTotalRescisao(dataModelo));
	}


	/**
	 * Consulta Paginada das recisões retornando 10 registros.
	 * 
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private List<RescisaoVO> consultarRescisao(DataModelo dataModelo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico());
		sql.append(" WHERE 1 = 1");
        sql.append(" AND templatefolha.tipotemplatefolhapagamento = 'RESCISAO'");

        switch (EnumCampoConsultaRescisao.valueOf(dataModelo.getCampoConsulta())) {
		case DATA_COMPETENCIA:
			sql.append(" AND ").append(realizarGeracaoWherePeriodoConsiderandoMesAno(dataModelo.getDataIni(), dataModelo.getDataFim(), "competencia.dataCompetencia"));
			break;
		default:
			break;
		}

        sql.append(" ORDER BY rescisao.codigo DESC");
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());

		return montarDadosLista(tabelaResultado);
	}

	/**
	 * Consulta o total de {@link RescisaoVO} de acordo com o filtro informado.
	 *  
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private Integer consultarTotalRescisao(DataModelo dataModelo) throws Exception {
        StringBuilder sql = new StringBuilder(getSqlBasicoCount());
        sql.append(" WHERE 1 = 1");
        sql.append(" AND templatefolha.tipotemplatefolhapagamento = 'RESCISAO'");

        switch (EnumCampoConsultaRescisao.valueOf(dataModelo.getCampoConsulta())) {
		case DATA_COMPETENCIA:
			sql.append(" AND ").append(realizarGeracaoWherePeriodo(dataModelo.getDataIni(), dataModelo.getDataFim(), "competencia.dataCompetencia", true));
			break;
		default:
			break;
		}

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
    }

	/**
	 * Monta a lista de {@link RescisaoVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<RescisaoVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<RescisaoVO> rescisoes = new ArrayList<>();

        while(tabelaResultado.next()) {
        	rescisoes.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return rescisoes;
	}

	@Override
	public RescisaoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		RescisaoVO obj = new RescisaoVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("tipoDemissao"))) {
			obj.setTipoDemissao(TipoDemissaoEnum.valueOf(tabelaResultado.getString("tipoDemissao")));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("motivoDemissao"))) {
			obj.setMotivoDemissao(MotivoDemissaoEnum.valueOf(tabelaResultado.getString("motivoDemissao")));
		}

		obj.setDataDemissao(tabelaResultado.getDate("dataDemissao"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("competenciaFolhaPagamento"))) {			
			obj.setCompetenciaFolhaPagamento(getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getLong("competenciaFolhaPagamento")));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("templateLancamentoFolhaPagamento"))) {
			obj.setTemplateLancamentoFolhaPagamento(getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getInt("templateLancamentoFolhaPagamento"), Uteis.NIVELMONTARDADOS_DADOSBASICOS));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("situacao"))) {			
			obj.setSituacao(SituacaoRescisaoEnum.valueOf(tabelaResultado.getString("situacao")));
		}

		return obj;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT rescisao.codigo, rescisao.templatelancamentofolhapagamento, rescisao.competenciafolhapagamento, ")
			.append(" rescisao.tipodemissao, rescisao.motivodemissao, rescisao.datademissao, rescisao.situacao")
			.append(" FROM rescisao")
			.append(" INNER JOIN templatelancamentofolhapagamento templatefolha ON templatefolha.codigo = rescisao.templatelancamentofolhapagamento")
			.append(" INNER JOIN competenciafolhapagamento competencia ON competencia.codigo = rescisao.competenciafolhapagamento");

		return sql.toString();
	}

	private String getSqlBasicoCount() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(rescisao.codigo) as qtde FROM Rescisao")
		.append(" INNER JOIN templatelancamentofolhapagamento templatefolha ON templatefolha.codigo = rescisao.templatelancamentofolhapagamento")
		.append(" INNER JOIN competenciafolhapagamento competencia ON competencia.codigo = rescisao.competenciafolhapagamento");

		return sql.toString();
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		Rescisao.idEntidade = idEntidade;
	}
}
