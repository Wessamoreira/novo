package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas historicoForm.jsp historicoCons.jsp) com as funcionalidades da classe <code>Historico</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Historico
 * @see HistoricoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import jobs.JobGeracaoManualParcela;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.GeracaoManualParcelaVO;
import negocio.comuns.financeiro.enumerador.SituacaoProcessamentoGeracaoManualParcelaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.financeiro.GeracaoManualParcela;

@Controller("GeracaoManualParcelasControle")
@Scope("viewScope")
@Lazy
public class GeracaoManualParcelasControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5345176058017051776L;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemSituacao;
	private GeracaoManualParcelaVO geracaoManualParcelaVO;
	private List<GeracaoManualParcelaVO> geracaoManualParcelaEmProcessamentoVOs;
	private DataModelo controleConsultaMatriculaPeriodoVencimentoSucesso;
	private DataModelo controleConsultaMatriculaPeriodoVencimentoErro;
	private String matricula;
	private String aluno;
	private String curso;
	private Date dataVencimento;
	private String parcela;
	
	private String matriculaErro;
	private String alunoErro;
	private String cursoErro;
	private Date dataVencimentoErro;
	private String parcelaErro;

	public GeracaoManualParcelasControle() {

	}

	public String consultar() {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getGeracaoManualParcelaFacade().consultar(getGeracaoManualParcelaVO().getUnidadeEnsino().getCodigo(), getGeracaoManualParcelaVO().getCurso().getNome(), getGeracaoManualParcelaVO().getTurma().getIdentificadorTurma(), getControleConsultaOtimizado().getDataIni(), getControleConsultaOtimizado().getDataFim(), getGeracaoManualParcelaVO().getSituacaoProcessamentoGeracaoManualParcela(), true, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getGeracaoManualParcelaFacade().consultarTotalRegistro(getGeracaoManualParcelaVO().getUnidadeEnsino().getCodigo(), getGeracaoManualParcelaVO().getCurso().getNome(), getGeracaoManualParcelaVO().getTurma().getIdentificadorTurma(), getControleConsultaOtimizado().getDataIni(), getControleConsultaOtimizado().getDataFim(), getGeracaoManualParcelaVO().getSituacaoProcessamentoGeracaoManualParcela()));
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
//		return "consultar";
	}

	public String irPaginaConsulta() {
		setGeracaoManualParcelaVO(new GeracaoManualParcelaVO());
		getGeracaoManualParcelaVO().setSituacaoProcessamentoGeracaoManualParcela(SituacaoProcessamentoGeracaoManualParcelaEnum.TODAS);
		setControleConsultaOtimizado(null);
		getControleConsultaOtimizado().setLimitePorPagina(10);
		getControleConsultaOtimizado().setPaginaAtual(1);
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setDataIni(Uteis.obterDataAntiga(new Date(), 90));
		getControleConsultaOtimizado().setDataFim(new Date());
		limparMensagem();
		return Uteis.getCaminhoRedirecionamentoNavegacao("geracaoManualParcelaCons");
	}

	public String editar() {
		setGeracaoManualParcelaVO((GeracaoManualParcelaVO) getRequestMap().get("geracaoManualParcelaItem"));
		try {
			setGeracaoManualParcelaVO(getFacadeFactory().getGeracaoManualParcelaFacade().consultarPorChavePrimaria(getGeracaoManualParcelaVO().getCodigo(), getUsuarioLogado()));
			inicializarConsultaMatriculaPeriodoVencimentoPorGeracaoManualParcela();			
			limparMensagem();
			return Uteis.getCaminhoRedirecionamentoNavegacao("geracaoManualParcelaForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("geracaoManualParcelaCons");
		}
	}
	
	public void inicializarConsultaMatriculaPeriodoVencimentoPorGeracaoManualParcela(){
		setControleConsultaMatriculaPeriodoVencimentoErro(null);
		setControleConsultaMatriculaPeriodoVencimentoSucesso(null);
		getControleConsultaMatriculaPeriodoVencimentoErro().setLimitePorPagina(10);
		getControleConsultaMatriculaPeriodoVencimentoErro().setPage(0);
		getControleConsultaMatriculaPeriodoVencimentoErro().setPaginaAtual(1);
		getControleConsultaMatriculaPeriodoVencimentoSucesso().setLimitePorPagina(10);
		getControleConsultaMatriculaPeriodoVencimentoSucesso().setPage(0);
		getControleConsultaMatriculaPeriodoVencimentoSucesso().setPaginaAtual(1);
		setMatriculaErro("");
		setAlunoErro("");
		setCursoErro(""); 
		setDataVencimentoErro(null);
		setParcelaErro("");
		consultarMatriculaPeriodoVencimento(getControleConsultaMatriculaPeriodoVencimentoErro(), true, getMatricula(), getAluno(), getCurso(), getDataVencimento(), getParcela());
		consultarMatriculaPeriodoVencimento(getControleConsultaMatriculaPeriodoVencimentoSucesso(), false, getMatriculaErro(), getAlunoErro(), getCursoErro(), getDataVencimentoErro(), getParcelaErro());
	}

	public void paginarConsulta(DataScrollEvent DataScrollEvent) {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}

	public void paginarMatriculaPeriodoVencimentoSucesso(DataScrollEvent DataScrollEvent) {
		getControleConsultaMatriculaPeriodoVencimentoSucesso().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaMatriculaPeriodoVencimentoSucesso().setPage(DataScrollEvent.getPage());
		consultarMatriculaPeriodoVencimento(getControleConsultaMatriculaPeriodoVencimentoSucesso(), false, getMatricula(), getAluno(), getCurso(), getDataVencimento(), getParcela());
	}

	public void paginarMatriculaPeriodoVencimentoErro(DataScrollEvent DataScrollEvent) {
		getControleConsultaMatriculaPeriodoVencimentoErro().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaMatriculaPeriodoVencimentoErro().setPage(DataScrollEvent.getPage());
		consultarMatriculaPeriodoVencimento(getControleConsultaMatriculaPeriodoVencimentoErro(), true, getMatriculaErro(), getAlunoErro(), getCursoErro(), getDataVencimentoErro(), getParcelaErro());
	}

	public void consultarMatriculaPeriodoVencimentoSucesso() {
		getControleConsultaMatriculaPeriodoVencimentoSucesso().setPaginaAtual(1);
		getControleConsultaMatriculaPeriodoVencimentoSucesso().setPage(0);
		consultarMatriculaPeriodoVencimento(getControleConsultaMatriculaPeriodoVencimentoSucesso(), false, getMatricula(), getAluno(), getCurso(), getDataVencimento(), getParcela());
	}
		
	
	public void consultarMatriculaPeriodoVencimentoErro() {
		getControleConsultaMatriculaPeriodoVencimentoErro().setPaginaAtual(1);
		getControleConsultaMatriculaPeriodoVencimentoErro().setPage(0);
		consultarMatriculaPeriodoVencimento(getControleConsultaMatriculaPeriodoVencimentoErro(), true, getMatriculaErro(), getAlunoErro(), getCursoErro(), getDataVencimentoErro(), getParcelaErro());
	}
	
	public void consultarMatriculaPeriodoVencimento(DataModelo dataModelo, Boolean erro, String matricula, String aluno, String curso, Date dataVencimento, String parcela) {
		try {
			dataModelo.setListaConsulta(getFacadeFactory().getMatriculaPeriodoVencimentoFacade().consultarMatriculaPeriodoVencimentoPorGeracaoManualParcela(getGeracaoManualParcelaVO().getCodigo(), matricula, aluno, curso, dataVencimento, parcela, erro, dataModelo.getLimitePorPagina(), dataModelo.getOffset()));
			dataModelo.setTotalRegistrosEncontrados(getFacadeFactory().getMatriculaPeriodoVencimentoFacade().consultarMatriculaPeriodoVencimentoPorGeracaoManualParcelaTotalRegistro(getGeracaoManualParcelaVO().getCodigo(), matricula, aluno, curso, dataVencimento, parcela, erro));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getLocalizedMessage(), Uteis.ERRO);
		}
	}

	public String novo() {
		setGeracaoManualParcelaVO(new GeracaoManualParcelaVO());
		getGeracaoManualParcelaVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
		getGeracaoManualParcelaVO().getUsuario().setCodigo(getUsuarioLogado().getCodigo());
		getGeracaoManualParcelaVO().getUsuario().setNome(getUsuarioLogado().getNome());
		try {
			getGeracaoManualParcelaVO().setPermitirGerarParcelaAlunoPreMatricula(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getGeracaoManualParcelaVO().getUnidadeEnsino().getCodigo()).getPermitirGerarParcelaPreMatricula());
		} catch (Exception e) {
			getGeracaoManualParcelaVO().setPermitirGerarParcelaAlunoPreMatricula(false);
		}
		getGeracaoManualParcelaVO().setMesReferencia(String.valueOf(Uteis.getMesDataAtual()));
		if (getGeracaoManualParcelaVO().getMesReferencia().length() == 1) {
			getGeracaoManualParcelaVO().setMesReferencia("0" + getGeracaoManualParcelaVO().getMesReferencia());
		}
		getGeracaoManualParcelaVO().setAnoReferencia(String.valueOf(Uteis.getAnoDataAtual()));
		if (getGeracaoManualParcelaVO().getAnoReferencia().length() == 2) {
			getGeracaoManualParcelaVO().setAnoReferencia("20" + getGeracaoManualParcelaVO().getAnoReferencia());
		}
		consultarGeracaoManualParcelasEmProcessamento();
		return Uteis.getCaminhoRedirecionamentoNavegacao("geracaoManualParcelaForm");
	}
	
	public void atualizarPermissaoGerarParcelaAlutnoPreMatricula(){
		try {
			if(Uteis.isAtributoPreenchido(getGeracaoManualParcelaVO().getUnidadeEnsino())){
					getGeracaoManualParcelaVO().setPermitirGerarParcelaAlunoPreMatricula(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getGeracaoManualParcelaVO().getUnidadeEnsino().getCodigo()).getPermitirGerarParcelaPreMatricula());	
			}
		} catch (Exception e) {
			getGeracaoManualParcelaVO().setPermitirGerarParcelaAlunoPreMatricula(false);
			setMensagemDetalhada("msg_erro", e.getLocalizedMessage(), Uteis.ERRO);
		}
	}

	public void limparDadosCurso() throws Exception {
		getGeracaoManualParcelaVO().setCurso(new CursoVO());
		setMensagemID("msg_entre_dados");
	}

	public void limparIdentificador() throws Exception {
		getGeracaoManualParcelaVO().setTurma(new TurmaVO());
		setMensagemID("msg_entre_dados");
	}

	/**
	 * @return the listaSelectItemUnidadeEnsino
	 */
	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
			montarListaSelectItemUnidadeEnsino();
		}
		return listaSelectItemUnidadeEnsino;
	}

	/**
	 * @param listaSelectItemUnidadeEnsino
	 *            the listaSelectItemUnidadeEnsino to set
	 */
	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(super.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			for (UnidadeEnsinoVO obj : resultadoConsulta) {
				getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome()));
				if (getGeracaoManualParcelaVO().getUnidadeEnsino().getCodigo() == 0) {
					getGeracaoManualParcelaVO().getUnidadeEnsino().setCodigo(obj.getCodigo());
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
		}
	}

	public void localizarParcelasParaGeracaoPorUnidade() {
		try {
			getFacadeFactory().getGeracaoManualParcelaFacade().realizarConsultarContaReceberGerar(getGeracaoManualParcelaVO(), getUsuarioLogado());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparAnoMes() {
		if (getGeracaoManualParcelaVO().getGerarTodasParcelas()) {
			getGeracaoManualParcelaVO().setMesReferencia("");
			getGeracaoManualParcelaVO().setAnoReferencia("");
			getGeracaoManualParcelaVO().setUtilizarDataCompetencia(false);
		}
	}

	public Boolean getApresentarBotaoGerarParcelas() {
		return getGeracaoManualParcelaVO().getQtdeParcelaGerar() > 0 && getGeracaoManualParcelaEmProcessamentoVOs().isEmpty() && (getGeracaoManualParcelaVO().getSituacaoProcessamentoGeracaoManualParcela().equals(SituacaoProcessamentoGeracaoManualParcelaEnum.AGUARDANDO_PROCESSAMENTO) || getGeracaoManualParcelaVO().getSituacaoProcessamentoGeracaoManualParcela().equals(SituacaoProcessamentoGeracaoManualParcelaEnum.ERRO_PROCESSAMENTO));
	}

	@PostConstruct
	public void consultarGeracaoManualParcelasEmProcessamento() {
		try {
			setOncompleteModal("");
			setGeracaoManualParcelaEmProcessamentoVOs(getFacadeFactory().getGeracaoManualParcelaFacade().consultar(getUnidadeEnsinoLogado().getCodigo(), "", "", null, null, SituacaoProcessamentoGeracaoManualParcelaEnum.EM_PROCESSAMENTO, false, getUsuarioLogado(), 0, 0));
			setApresentarProgressBar(!getGeracaoManualParcelaEmProcessamentoVOs().isEmpty());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarFinalizacaoLista(){
		if(getGeracaoManualParcelaEmProcessamentoVOs().isEmpty()){
			setApresentarProgressBar(false);
			setOncompleteModal("RichFaces.$('panelGeracaoManualEmProcessamento').hide()");
		}
	}

	public void consultarGeracaoManualParcelasEmProcessamentoCons() {
		try {
			setOncompleteModal("");
			GeracaoManualParcelaVO obj = (GeracaoManualParcelaVO) getRequestMap().get("geracaoManualParcelaItem");
			GeracaoManualParcelaVO obj2 = getFacadeFactory().getGeracaoManualParcelaFacade().consultarPorChavePrimaria(obj.getCodigo(), getUsuarioLogado());
			if (obj2.getSituacaoProcessamentoGeracaoManualParcela().equals(SituacaoProcessamentoGeracaoManualParcelaEnum.PROCESSAMENTO_CONCLUIDO) || obj2.getSituacaoProcessamentoGeracaoManualParcela().equals(SituacaoProcessamentoGeracaoManualParcelaEnum.ERRO_PROCESSAMENTO)) {				
				for (Iterator<GeracaoManualParcelaVO> iterator = getGeracaoManualParcelaEmProcessamentoVOs().iterator(); iterator.hasNext();) {
					GeracaoManualParcelaVO item = iterator.next();
					if (item.getCodigo().equals(obj2.getCodigo())) {
						if (obj2.getCodigo().intValue() == getGeracaoManualParcelaVO().getCodigo().intValue()) {
							setGeracaoManualParcelaVO(obj2);
							inicializarConsultaMatriculaPeriodoVencimentoPorGeracaoManualParcela();							
						}
						item.setProgresso(obj2.getParcelaAtual().longValue());
						item.setMaxValue(obj2.getQtdeParcelaGerar());
						item.setStatus(obj2.getLabelProcessamento());
						item.setSituacaoProcessamentoGeracaoManualParcela(obj2.getSituacaoProcessamentoGeracaoManualParcela());
						item.setForcarEncerramento(true);
						iterator.remove();
						setOncompleteModal("RichFaces.$('panelGeracaoManualEmProcessamento').hide()");
						return;
					}					
				}
			}			
			//int index = 0;
			for (GeracaoManualParcelaVO item : getGeracaoManualParcelaEmProcessamentoVOs()) {
				if (item.getCodigo().equals(obj2.getCodigo())) {
					item.setProgresso(obj2.getParcelaAtual().longValue());
					item.setMaxValue(obj2.getQtdeParcelaGerar());
					item.setStatus(obj2.getLabelProcessamento());
					item.setSituacaoProcessamentoGeracaoManualParcela(obj2.getSituacaoProcessamentoGeracaoManualParcela());					
					//getGeracaoManualParcelaEmProcessamentoVOs().set(index, obj);
					break;
				}
				//index++;
			}			
			//getApresentarProgressBar();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			//return e.getMessage();
		}
	}

	public void gerarParcelas() {
		try {
			ControleAcesso.incluir(GeracaoManualParcela.getIdEntidade(), getUsuarioLogado());
			getFacadeFactory().getGeracaoManualParcelaFacade().validarDados(getGeracaoManualParcelaVO());
			AplicacaoControle aplicacaoControle = getControladorAplicacaoControle("AplicacaoControle");
			aplicacaoControle.realizarGeracaoManualParcela(getGeracaoManualParcelaVO().getUnidadeEnsino().getCodigo(), true, false);
			getGeracaoManualParcelaVO().setDataInicioProcessamento(new Date());
			getGeracaoManualParcelaVO().setSituacaoProcessamentoGeracaoManualParcela(SituacaoProcessamentoGeracaoManualParcelaEnum.EM_PROCESSAMENTO);
			getFacadeFactory().getGeracaoManualParcelaFacade().persistir(getGeracaoManualParcelaVO(), getUsuarioLogado());
			Thread jobGeracaoManualParcela = new Thread(new JobGeracaoManualParcela(geracaoManualParcelaVO, aplicacaoControle), "GeracaoManualParcela_Unidade_" + geracaoManualParcelaVO.getUnidadeEnsino().getCodigo());
			jobGeracaoManualParcela.start();
			consultarGeracaoManualParcelasEmProcessamento();
		} catch (Exception e) {
			consultarGeracaoManualParcelasEmProcessamento();			
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<SelectItem> tipoConsultaComboCurso;

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboCurso;
	}

	public void consultarCurso() {
		try {
			getFacadeFactory().getEnvelopeRelFacade().validarDadosConsultaAlunoUnidadeEnsino(getGeracaoManualParcelaVO().getUnidadeEnsino().getCodigo());
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getGeracaoManualParcelaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
			getGeracaoManualParcelaVO().setCurso(obj);
			listaConsultaCurso.clear();
			this.setValorConsultaCurso("");
			this.setCampoConsultaCurso("");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		return itens;
	}

	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getValorConsultaTurma().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaTurma().equals("codigo")) {
				if (getValorConsultaTurma().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaTurma());
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorCodigoTurmaCursoUnidadeEnsino(new Integer(valorInt), getGeracaoManualParcelaVO().getCurso().getCodigo(), getGeracaoManualParcelaVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCursoTurno(getValorConsultaTurma(), getGeracaoManualParcelaVO().getUnidadeEnsino().getCodigo(), getGeracaoManualParcelaVO().getCurso().getCodigo(), 0, false, getUsuarioLogado());

			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
			getGeracaoManualParcelaVO().setTurma(obj);
			getGeracaoManualParcelaVO().setCurso(obj.getCurso());
			obj = null;
			setValorConsultaTurma("");
			setCampoConsultaTurma("");
			getListaConsultaTurma().clear();
		} catch (Exception e) {
			getGeracaoManualParcelaVO().setTurma(new TurmaVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public boolean getApresentarGerarTodasParcelas() {
		if (getGeracaoManualParcelaVO().getTurma().getCodigo().intValue() != 0 || getGeracaoManualParcelaVO().getCurso().getCodigo().intValue() != 0) {
			return true;
		} else {
			return false;
		}
	}

	public GeracaoManualParcelaVO getGeracaoManualParcelaVO() {
		if (geracaoManualParcelaVO == null) {
			geracaoManualParcelaVO = new GeracaoManualParcelaVO();
		}
		return geracaoManualParcelaVO;
	}

	public void setGeracaoManualParcelaVO(GeracaoManualParcelaVO geracaoManualParcelaVO) {
		this.geracaoManualParcelaVO = geracaoManualParcelaVO;
	}

	public List<GeracaoManualParcelaVO> getGeracaoManualParcelaEmProcessamentoVOs() {
		if (geracaoManualParcelaEmProcessamentoVOs == null) {
			geracaoManualParcelaEmProcessamentoVOs = new ArrayList<GeracaoManualParcelaVO>();
		}
		return geracaoManualParcelaEmProcessamentoVOs;
	}

	public void setGeracaoManualParcelaEmProcessamentoVOs(List<GeracaoManualParcelaVO> geracaoManualParcelaEmProcessamentoVOs) {
		this.geracaoManualParcelaEmProcessamentoVOs = geracaoManualParcelaEmProcessamentoVOs;
	}

	public Boolean apresentarProgressBar;
	public Boolean getApresentarProgressBar() {
		if(apresentarProgressBar == null){
			apresentarProgressBar = !getGeracaoManualParcelaEmProcessamentoVOs().isEmpty();
		}
		return apresentarProgressBar;
	}
	
	public void setApresentarProgressBar(Boolean apresentarProgressBar) {
		this.apresentarProgressBar = apresentarProgressBar;
	}

	public DataModelo getControleConsultaMatriculaPeriodoVencimentoSucesso() {
		if (controleConsultaMatriculaPeriodoVencimentoSucesso == null) {
			controleConsultaMatriculaPeriodoVencimentoSucesso = new DataModelo();
		}
		return controleConsultaMatriculaPeriodoVencimentoSucesso;
	}

	public void setControleConsultaMatriculaPeriodoVencimentoSucesso(DataModelo controleConsultaMatriculaPeriodoVencimentoSucesso) {
		this.controleConsultaMatriculaPeriodoVencimentoSucesso = controleConsultaMatriculaPeriodoVencimentoSucesso;
	}

	public DataModelo getControleConsultaMatriculaPeriodoVencimentoErro() {
		if (controleConsultaMatriculaPeriodoVencimentoErro == null) {
			controleConsultaMatriculaPeriodoVencimentoErro = new DataModelo();
		}
		return controleConsultaMatriculaPeriodoVencimentoErro;
	}

	public void setControleConsultaMatriculaPeriodoVencimentoErro(DataModelo controleConsultaMatriculaPeriodoVencimentoErro) {
		this.controleConsultaMatriculaPeriodoVencimentoErro = controleConsultaMatriculaPeriodoVencimentoErro;
	}

	public void setTipoConsultaComboCurso(List<SelectItem> tipoConsultaComboCurso) {
		this.tipoConsultaComboCurso = tipoConsultaComboCurso;
	}

	public List<SelectItem> getListaSelectItemSituacao() {
		if (listaSelectItemSituacao == null) {
			listaSelectItemSituacao = new ArrayList<SelectItem>(0);
			listaSelectItemSituacao.add(new SelectItem(SituacaoProcessamentoGeracaoManualParcelaEnum.TODAS, SituacaoProcessamentoGeracaoManualParcelaEnum.TODAS.getValorApresentar()));
			listaSelectItemSituacao.add(new SelectItem(SituacaoProcessamentoGeracaoManualParcelaEnum.EM_PROCESSAMENTO, SituacaoProcessamentoGeracaoManualParcelaEnum.EM_PROCESSAMENTO.getValorApresentar()));
			listaSelectItemSituacao.add(new SelectItem(SituacaoProcessamentoGeracaoManualParcelaEnum.ERRO_PROCESSAMENTO, SituacaoProcessamentoGeracaoManualParcelaEnum.ERRO_PROCESSAMENTO.getValorApresentar()));
			listaSelectItemSituacao.add(new SelectItem(SituacaoProcessamentoGeracaoManualParcelaEnum.PROCESSAMENTO_CONCLUIDO, SituacaoProcessamentoGeracaoManualParcelaEnum.PROCESSAMENTO_CONCLUIDO.getValorApresentar()));
		}
		return listaSelectItemSituacao;
	}

	public void setListaSelectItemSituacao(List<SelectItem> listaSelectItemSituacao) {
		this.listaSelectItemSituacao = listaSelectItemSituacao;
	}

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getAluno() {
		if (aluno == null) {
			aluno = "";
		}
		return aluno;
	}

	public void setAluno(String aluno) {
		this.aluno = aluno;
	}

	public String getCurso() {
		if (curso == null) {
			curso = "";
		}
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public Date getDataVencimento() {
		
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public String getParcela() {
		if (parcela == null) {
			parcela = "";
		}
		return parcela;
	}

	public void setParcela(String parcela) {
		this.parcela = parcela;
	}

	public String getMatriculaErro() {
		if (matriculaErro == null) {
			matriculaErro = "";
		}
		return matriculaErro;
	}

	public void setMatriculaErro(String matriculaErro) {
		this.matriculaErro = matriculaErro;
	}

	public String getAlunoErro() {
		if (alunoErro == null) {
			alunoErro = "";
		}
		return alunoErro;
	}

	public void setAlunoErro(String alunoErro) {
		this.alunoErro = alunoErro;
	}

	public String getCursoErro() {
		if (cursoErro == null) {
			cursoErro = "";
		}
		return cursoErro;
	}

	public void setCursoErro(String cursoErro) {
		this.cursoErro = cursoErro;
	}

	public Date getDataVencimentoErro() {		
		return dataVencimentoErro;
	}

	public void setDataVencimentoErro(Date dataVencimentoErro) {
		this.dataVencimentoErro = dataVencimentoErro;
	}

	public String getParcelaErro() {
		if (parcelaErro == null) {
			parcelaErro = "";
		}
		return parcelaErro;
	}

	public void setParcelaErro(String parcelaErro) {
		this.parcelaErro = parcelaErro;
	}

}
