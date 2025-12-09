package relatorio.controle.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoFinanceiroEnum;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.RegistroNegativacaoCobrancaContaReceberItemVO;
import negocio.comuns.financeiro.RegistroNegativacaoCobrancaContaReceberRelVO;
import negocio.comuns.financeiro.RegistroNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.enumerador.TipoAgenteNegativacaoCobrancaContaReceberEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

@Controller("RegistroNegativacaoCobrancaContaReceberControle")
@Scope("viewScope")
@Lazy
public class RegistroNegativacaoCobrancaContaReceberControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private AgenteNegativacaoCobrancaContaReceberVO agente;
	private TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente;
	private UnidadeEnsinoVO unidadeEnsino;
	private MatriculaVO matricula;
	private UnidadeEnsinoCursoVO unidadeEnsinoCurso;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<MatriculaVO> listaConsultaAluno;
	private String campoConsultaAluno;
	private String filtroConsultaAluno;
	private Date periodoInicial;
	private Date periodoFinal;
	private CursoVO cursoVO;
	private String campoConsultaCurso;
	private String valorConsultaCursos;
	List<SelectItem> tipoConsultaComboCurso;
	private List<CursoVO> listaConsultaCurso;
	private TurmaVO turmaVO;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	List<SelectItem> tipoConsultaComboTurma;
	private List<TurmaVO> listaConsultaTurma;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;
	private FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO;
	private Boolean marcarTodasSituacoesFinanceiras;
	private List<CentroReceitaVO> centroReceitaVOs;
	private Boolean marcarTodosCentroReceitas;
	private RegistroNegativacaoCobrancaContaReceberItemVO registroExclusao;
	private String retornoExclusao;
	private Boolean permitirExclusaoRegistroNegativacaoCobranca;
	private String campoConsultaAgente;
	private String valorConsultaAgente;
	List<SelectItem> tipoConsultaComboAgente;
	private List<AgenteNegativacaoCobrancaContaReceberVO> listaConsultaAgente;
	
	private int valorConsultaUnidadeEnsino;
	private Boolean campoConsultaNossoNumero;
	private boolean apresentarRegistroViaIntegracao = false;
	

	RegistroNegativacaoCobrancaContaReceberControle() {
		carregarListaUnidadeEnsino();
		getControleConsultaOtimizado().setLimitePorPagina(10);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getControleConsultaOtimizado().setPage(0);
	}
	
	
	public Boolean getPermitirExclusaoRegistroNegativacaoCobranca() {
		if (permitirExclusaoRegistroNegativacaoCobranca == null) {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("ExclusaoRegistroNegativacaoCobranca", getUsuarioLogado());
				permitirExclusaoRegistroNegativacaoCobranca = true;
			} catch (Exception e) {
				permitirExclusaoRegistroNegativacaoCobranca = false;
			}
		}
		return permitirExclusaoRegistroNegativacaoCobranca;
	}
	
	public void setPermitirExclusaoRegistroNegativacaoCobranca(Boolean permitirExclusaoRegistroNegativacaoCobranca) {
		this.permitirExclusaoRegistroNegativacaoCobranca = permitirExclusaoRegistroNegativacaoCobranca;
	}
	
	public void realizarInicioExclusaoTotalRegistroNegativacaoCobranca() {
		try {
			boolean possuiContaParaExcluir = false;
			for (RegistroNegativacaoCobrancaContaReceberItemVO registro : getRegistroNegativacaoCobrancaContaReceberVO().getListaContasReceberCobranca()) {
				if (registro.getMotivo().trim().isEmpty()) {
					possuiContaParaExcluir = true;
					break;
				}
			}
			if (!possuiContaParaExcluir) {
				getRegistroExclusao().setNovoObj(false);
				throw new Exception("Todas as Contas Receber já foram excluidas!");
			}
			setRegistroExclusao(new RegistroNegativacaoCobrancaContaReceberItemVO());
			getRegistroExclusao().setCodigoUsuario(getUsuarioLogado().getCodigo());
			getRegistroExclusao().setNomeUsuario(getUsuarioLogado().getNome());
			getRegistroExclusao().setNovoObj(true);
			setMensagemID("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void gravarExclusaoTotalRegistroNegativacaoCobranca() {
		try {
			getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().executarExclusaoTotalRegistroNegativacaoCobranca(getRegistroNegativacaoCobrancaContaReceberVO(), getRegistroExclusao().getMotivo(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
			setRetornoExclusao("RichFaces.$('panelExclusaoTotal').hide()");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setRetornoExclusao("");
		}
	}
	
	public void realizarInicioExclusaoRegistroNegativacaoCobranca() {
		try {
			setRegistroExclusao((RegistroNegativacaoCobrancaContaReceberItemVO) context().getExternalContext().getRequestMap().get("registroItens"));
			getRegistroExclusao().setRegistroNegativacaoCobrancaContaReceber(getRegistroNegativacaoCobrancaContaReceberVO().getCodigo());
			getRegistroExclusao().setCodigoUsuario(getUsuarioLogado().getCodigo());
			getRegistroExclusao().setNomeUsuario(getUsuarioLogado().getNome());
			getRegistroExclusao().setDataExclusao(new Date());
			getRegistroExclusao().setNovoObj(true);
			setMensagemID("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarExibicaoExclusaoRegistroNegativacaoCobranca() {
		try {
			setRegistroExclusao((RegistroNegativacaoCobrancaContaReceberItemVO) context().getExternalContext().getRequestMap().get("registroItens"));
			getRegistroExclusao().setNovoObj(false);
			setMensagemID("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void gravarExclusaoRegistroNegativacaoCobranca() {
		try {
			getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().executarExclusaoRegistroNegativacaoCobranca(getRegistroExclusao(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
			setRetornoExclusao("RichFaces.$('panelExclusao').hide()");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setRetornoExclusao("");
		}
	}
	
	public void consultarSituacaoContaReceber() {
		try {
			setRegistroExclusao((RegistroNegativacaoCobrancaContaReceberItemVO) context().getExternalContext().getRequestMap().get("registroItens"));
			realizarConsultaContaReceber(getRegistroExclusao());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			getRegistroExclusao().setSituacao("");
		}
	}
	
	public void realizarConsultaSituacaoContaReceberTotal() {
		try {
			for (RegistroNegativacaoCobrancaContaReceberItemVO item : getRegistroNegativacaoCobrancaContaReceberVO().getListaContasReceberCobranca()) {
				realizarConsultaContaReceber(item);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private void realizarConsultaContaReceber(RegistroNegativacaoCobrancaContaReceberItemVO item) throws Exception {
		item.setSituacao(getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().consultarSituacaoContaReceber(getRegistroNegativacaoCobrancaContaReceberVO().getAgente().getIntegracao(), item.getMatricula(), item.getCodigoCurso()));		
	}

	public void carregarListaUnidadeEnsino() {
		try {
			setListaSelectItemUnidadeEnsino(getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().montarListaSelectItemUnidadeEnsino(getUnidadeEnsinoLogado(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getCampoConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getFiltroConsultaAluno().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaCursoTurma(getCampoConsultaAluno(), 0, getCursoVO().getCodigo(), getTurmaVO().getCodigo(), false, getUsuarioLogado());
			}
			if (getFiltroConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoaCursoTurma(getCampoConsultaAluno(), 0, getCursoVO().getCodigo(), getTurmaVO().getCodigo(), false, getUsuarioLogado());
			}
//			if (getFiltroConsultaAluno().equals("nomeCurso")) {
//				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getCampoConsultaAluno(), getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
//			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
		setMatricula(obj);		
		setFiltroConsultaAluno("");
		setCampoConsultaAluno("");
		//getListaConsultaAluno().clear();
		if(!getFacadeFactory().getContaReceberFacade().realizarVerificacaoAlunoPossuiContaAReceberVinculadoUnidadeEnsinoFinanceira(obj.getMatricula(), getUnidadeEnsino().getCodigo())) {
			getUnidadeEnsino().setCodigo(obj.getUnidadeEnsino().getCodigo());
		}
	}

	public void adicionarContasMatriculaAluno() throws Exception {
		try {		
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			setMatricula(obj);			
			executarConsutaContasReceber();			
			setFiltroConsultaAluno("");
			setCampoConsultaAluno("");
			if(!getFacadeFactory().getContaReceberFacade().realizarVerificacaoAlunoPossuiContaAReceberVinculadoUnidadeEnsinoFinanceira(obj.getMatricula(), getUnidadeEnsino().getCodigo())) {
				getUnidadeEnsino().setCodigo(obj.getUnidadeEnsino().getCodigo());
			}
			//getListaConsultaAluno().clear();
		} catch (Exception e) {
			setMatricula(null);
			setCursoVO(null);
	//		getRegistroNegativacaoCobrancaContaReceberVO().getListaContasReceberCobranca().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void imprimirPDF() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "Registro Cobrança Conta Receber Rel ", "Iniciando Impressao PDF", "Emitindo Relatorio");
			List<RegistroNegativacaoCobrancaContaReceberRelVO> listaRelatorio = new ArrayList<RegistroNegativacaoCobrancaContaReceberRelVO>();
			RegistroNegativacaoCobrancaContaReceberRelVO relatorio = new RegistroNegativacaoCobrancaContaReceberRelVO();
			relatorio.setRegistroNegativacaoCobrancaContaReceber(getRegistroNegativacaoCobrancaContaReceberVO());
			listaRelatorio.add(relatorio);
			
			getSuperParametroRelVO().setNomeDesignIreport(getCaminhoBaseRelatorio() + "RegistroNegativacaoCobrancaContaReceberRel.jrxml");
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setTituloRelatorio("REGISTRO NEGATIVAÇÃO/COBRANÇA");
			getSuperParametroRelVO().setListaObjetos(listaRelatorio);
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
//			logoPadraoRelatorio
//			nomeEmpresa
			realizarImpressaoRelatorio();
			
			setMensagemID("msg_relatorio_ok");
			registrarAtividadeUsuario(getUsuarioLogado(), "Registro Cobrança Conta Receber Rel", "Finalizando Impressao PDF", "Emitindo Relatorio");
		} catch (Exception e) {
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private String getCaminhoBaseRelatorio() {
		return "relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator;
	}
	
	public void executarConsutaContasReceber() {
		try {
			getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().validarDados(getUnidadeEnsino(), getAgente(), getPeriodoFinal());
			if (!getRegistroNegativacaoCobrancaContaReceberVO().getListaContasReceberCobranca().isEmpty()) {
				List<RegistroNegativacaoCobrancaContaReceberItemVO> lista = getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().execultarConsultaContaReceberPendente(getTipoAgente(), getAgente(), getUnidadeEnsino(), getCursoVO(), getTurmaVO(), getMatricula(), 
						getRegistroNegativacaoCobrancaContaReceberVO().getUsuarioVO(), getPeriodoInicial(), getPeriodoFinal(), getFiltroRelatorioAcademicoVO(), getFiltroRelatorioFinanceiroVO(), getCentroReceitaVOs() , false);
				if (!lista.isEmpty()) {
					getRegistroNegativacaoCobrancaContaReceberVO().adicionarListaContaReceber(lista);
					setMensagem("Foram adicionados " + lista.size() + " contas na listagem!");
					setMensagemDetalhada("Foram adicionados " + lista.size() + " contas na listagem!");
				} else {
					throw new Exception("Não foi encontrado nenhuma conta a receber em aberto para ser selecionada!");
				}
			} else {
				RegistroNegativacaoCobrancaContaReceberVO obj = getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade()
				.criarRegistro(getAgente(), getTipoAgente(), getUnidadeEnsino(), getCursoVO(), getTurmaVO(), getMatricula(), getRegistroNegativacaoCobrancaContaReceberVO().getUsuarioVO(),
							   getPeriodoInicial(), getPeriodoFinal(), getMatricula().getAluno().getNome(), getFiltroRelatorioAcademicoVO(),
							   getFiltroRelatorioFinanceiroVO(), getCentroReceitaVOs(), getRegistroNegativacaoCobrancaContaReceberVO().getCentroReceitaApresentar());
				obj.setRegistrarNegativacaoContaReceberViaIntegracao(getRegistroNegativacaoCobrancaContaReceberVO().isRegistrarNegativacaoContaReceberViaIntegracao());
				setRegistroNegativacaoCobrancaContaReceberVO(obj);
				setMensagemID("msg_dados_consultados");
			}			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void gravar() {
		try {
			getRegistroNegativacaoCobrancaContaReceberVO().setAgente(getAgente());
			getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().validarDados(getUnidadeEnsino(), getAgente(), getPeriodoFinal());
			getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().persistir(getRegistroNegativacaoCobrancaContaReceberVO(), false, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getUnidadeEnsino().getCodigo()), getUsuarioLogado());
//			getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().incluir(getRegistroNegativacaoCobrancaContaReceberVO(), getUsuarioLogado());
			//setRegistroNegativacaoCobrancaContaReceberVO(getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().gravarRegistro(getRegistroNegativacaoCobrancaContaReceberVO(), getUsuarioLogado()));
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}
	
	public List<SelectItem> getListaSelectItensTipoAgente() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem(TipoAgenteNegativacaoCobrancaContaReceberEnum.NEGATIVACAO, "Negativação"));
		itens.add(new SelectItem(TipoAgenteNegativacaoCobrancaContaReceberEnum.COBRANCA, "Cobrança"));
		return itens;
	}

	public List<SelectItem> getListaSelectIntensAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
//		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List<MatriculaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCurso == null) {
			unidadeEnsinoCurso = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCurso;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCurso) {
		this.unidadeEnsinoCurso = unidadeEnsinoCurso;
	}

	public String getFiltroConsultaAluno() {
		if (filtroConsultaAluno == null) {
			filtroConsultaAluno = "";
		}
		return filtroConsultaAluno;
	}

	public void setFiltroConsultaAluno(String filtroConsultaAluno) {
		this.filtroConsultaAluno = filtroConsultaAluno;
	}

	public Date getPeriodoFinal() {
		if (periodoFinal == null) {
			periodoFinal = new Date();
		}
		return periodoFinal;
	}

	public void setPeriodoFinal(Date periodoFinal) {
		this.periodoFinal = periodoFinal;
	}

	public Date getPeriodoInicial() {
		if (periodoInicial == null) {
			periodoInicial = new Date();
		}
		return periodoInicial;
	}

	public void setPeriodoInicial(Date periodoInicial) {
		this.periodoInicial = periodoInicial;
	}

	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("codigo")) {
				if (getCampoConsultaTurma().equals("")) {
					setValorConsultaTurma("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaTurma());
				objs = getFacadeFactory().getTurmaFacade().consultarPorCodigoTurmaCursoEUnidadeEnsino(valorInt, getCursoVO().getCodigo(), getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nome")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeTurmaCursoEUnidadeEnsino(getValorConsultaTurma(), getCursoVO().getCodigo(), getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void selecionarCurso() throws Exception {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
			getUnidadeEnsinoCursoVO().setCurso(getCursoVO());
			setCampoConsultaCurso("");
			setValorConsultaCursos("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarAgente() throws Exception {
		try {
			AgenteNegativacaoCobrancaContaReceberVO obj = (AgenteNegativacaoCobrancaContaReceberVO) context().getExternalContext().getRequestMap().get("agenteItens");
			setAgente(getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().consultarPorChavePrimaria(obj.getCodigo(), false,Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (!getAgente().getTipoAmbos()) {
				setTipoAgente(getAgente().getTipo());
			} else {
				setTipoAgente(TipoAgenteNegativacaoCobrancaContaReceberEnum.NEGATIVACAO);
			}
			if(getAgente().getIntegracaoSerasaApiGeo() && getAgente().getRegistrarAutomaticamenteContasNegativacao()) {
				getFiltroRelatorioFinanceiroVO().setTipoOrigemBiblioteca(getAgente().getTipoOrigemBiblioteca());
				getFiltroRelatorioFinanceiroVO().setTipoOrigemBolsaCusteadaConvenio(getAgente().getTipoOrigemBolsaCusteadaConvenio());
				getFiltroRelatorioFinanceiroVO().setTipoOrigemContratoReceita(getAgente().getTipoOrigemContratoReceita());
				getFiltroRelatorioFinanceiroVO().setTipoOrigemDevolucaoCheque(getAgente().getTipoOrigemDevolucaoCheque());
				getFiltroRelatorioFinanceiroVO().setTipoOrigemInclusaoReposicao(getAgente().getTipoOrigemInclusaoReposicao());
				getFiltroRelatorioFinanceiroVO().setTipoOrigemMatricula(getAgente().getTipoOrigemMatricula());
				getFiltroRelatorioFinanceiroVO().setTipoOrigemMensalidade(getAgente().getTipoOrigemMensalidade());
				getFiltroRelatorioFinanceiroVO().setTipoOrigemNegociacao(getAgente().getTipoOrigemNegociacao());
				getFiltroRelatorioFinanceiroVO().setTipoOrigemOutros(getAgente().getTipoOrigemOutros());
				getFiltroRelatorioFinanceiroVO().setTipoOrigemInscricaoProcessoSeletivo(getAgente().getTipoOrigemInscricaoProcessoSeletivo());
				getFiltroRelatorioFinanceiroVO().setTipoOrigemRequerimento(getAgente().getTipoOrigemRequerimento());
				getFiltroRelatorioFinanceiroVO().setTipoOrigemMaterialDidatico(getAgente().getTipoOrigemMaterialDidatico());
			}
			setCampoConsultaAgente("");
			setValorConsultaAgente("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		try {
			TurmaVO turma = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(turma.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			setCursoVO(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(turma.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			setCampoConsultaTurma("");
			setCampoConsultaTurma("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCurso() throws Exception {
		try {
			setCursoVO(new CursoVO());
			setListaConsultaCurso(null);
			limparTurma();
			limparDados();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparAgente() throws Exception {
		try {
			setAgente(new AgenteNegativacaoCobrancaContaReceberVO());
			setTipoAgente(TipoAgenteNegativacaoCobrancaContaReceberEnum.NEGATIVACAO);
			setListaConsultaAgente(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() throws Exception {
		try {
			setTurmaVO(new TurmaVO());
			setListaConsultaTurma(null);
			setCursoVO(new CursoVO());
		} catch (Exception e) {
		}
	}

	public void limparDadosAluno() {
		setMatricula(null);
		//setUnidadeEnsino(null);
		setListaConsultaTurma(null);
		setCursoVO(null);
		setTurmaVO(null);
		setListaConsultaCurso(null);
	}

	public void consultarCurso() {
		try {
			if (!Uteis.isAtributoPreenchido(getUnidadeEnsino())) {
				throw new ConsistirException("O campo Unidade De Ensino (Relatório Registro Cobrança Conta Receber) deve ser informado.");
			}
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCursos().equals("")) {
					setValorConsultaCursos("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCursos());
				objs = getFacadeFactory().getCursoFacade().consultarCursoPorCodigoUnidadeEnsino(valorInt, getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultarPorNomeCursoUnidadeEnsinoBasica(getValorConsultaCursos(), getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarAgente() {
		try {
			List<AgenteNegativacaoCobrancaContaReceberVO> objs = new ArrayList<AgenteNegativacaoCobrancaContaReceberVO>(0);
			objs = getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().consultarPorNome(getValorConsultaAgente(), false, getUsuarioLogado());			
			setListaConsultaAgente(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAgente(new ArrayList<AgenteNegativacaoCobrancaContaReceberVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDados() throws Exception {
		setCursoVO(null);
		setListaConsultaCurso(null);
		setTurmaVO(null);
		setListaConsultaTurma(null);
		setMatricula(null);
		setListaConsultaAluno(null);
		if (getUnidadeEnsino().getCodigo().intValue() > 0) {
			setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
		}
		setMensagemID("msg_entre_dados");
	}

	public String inicializarConsultar() {
		getListaConsulta().clear();
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("registroNegativacaoCobrancaContaReceberCons.xhtml");
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
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

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
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

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboCurso;
	}
	
	public List<SelectItem> getTipoConsultaComboAgente() {
		if (tipoConsultaComboAgente == null) {
			tipoConsultaComboAgente = new ArrayList<SelectItem>(0);
			tipoConsultaComboAgente.add(new SelectItem("nome", "Nome"));
//			tipoConsultaComboAgente.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboAgente;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboTurma.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboTurma;
	}

	public String getValorConsultaCursos() {
		if (valorConsultaCursos == null) {
			valorConsultaCursos = "";
		}
		return valorConsultaCursos;
	}

	public void setValorConsultaCursos(String valorConsultaCursos) {
		this.valorConsultaCursos = valorConsultaCursos;
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

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>();
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public void incializarDados() {
		limparDadosAluno();
		setTurmaVO(new TurmaVO());
		carregarListaUnidadeEnsino();
	}

	// Outra Tela

	private RegistroNegativacaoCobrancaContaReceberVO registroNegativacaoCobrancaContaReceberVO;
	private Date dataGeracaoInicio;
	private Date dataGeracaoFim;
	private String campoConsulta;
	private String valorConsulta;

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("turma", "Turma"));
		itens.add(new SelectItem("aluno", "Aluno"));
		itens.add(new SelectItem("nossonumero", "Nosso Número"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("usuario", "Usuário"));
		return itens;
	}

	
	public void consultaOtimizado() {
		consultar();
	}
	
	public void paginarConsulta(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}
	
	public String consultar() {
		try {
			super.consultar();
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().consultar(getControleConsultaOtimizado(), getControleConsulta().getCampoConsulta(), getDataGeracaoInicio(), getDataGeracaoFim(), getValorConsultaUnidadeEnsino(), getControleConsulta().getValorConsulta(), true, getUsuarioLogado());
//			List<RegistroNegativacaoCobrancaContaReceberVO> objs = null;
//			if (getControleConsulta().getCampoConsulta().equals("curso")) {
//				objs = getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().consultarPorCurso(getDataGeracaoInicio(), getDataGeracaoFim(), getValorConsultaUnidadeEnsino(), getControleConsulta().getValorConsulta(), false, getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("turma")) {
//				objs = getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().consultarPorTurma(getDataGeracaoInicio(), getDataGeracaoFim(), getValorConsultaUnidadeEnsino(), getControleConsulta().getValorConsulta(), false, getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("aluno")) {
//				objs = getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().consultarPorAluno(getDataGeracaoInicio(), getDataGeracaoFim(), getValorConsultaUnidadeEnsino(), getControleConsulta().getValorConsulta(), false, getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("matricula")) {
//				objs = getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().consultarPorMatricula(getDataGeracaoInicio(), getDataGeracaoFim(), getValorConsultaUnidadeEnsino(), getControleConsulta().getValorConsulta(), false, getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("nossonumero")) {
//				objs = getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().consultarPorNossoNumero(getValorConsultaUnidadeEnsino(), getControleConsulta().getValorConsulta(), false, getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("usuario")) {
//				objs = getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().consultarPorUsuario(getDataGeracaoInicio(), getDataGeracaoFim(), getValorConsultaUnidadeEnsino(), getControleConsulta().getValorConsulta(), false, getUsuarioLogado());
//			}
//			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroNegativacaoCobrancaContaReceberCons.xhtml");
		} catch (Exception e) {
//			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroNegativacaoCobrancaContaReceberCons.xhtml");
		}
	}

	public void removerItemRegistro() {
		try {
			RegistroNegativacaoCobrancaContaReceberItemVO obj = (RegistroNegativacaoCobrancaContaReceberItemVO) context().getExternalContext().getRequestMap().get("registroItens");
			getRegistroNegativacaoCobrancaContaReceberVO().getListaContasReceberCobranca().remove(obj);
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", "Não foi possível remover o item!");
		}
	}
	
	public String editar() {
		try {
			RegistroNegativacaoCobrancaContaReceberVO obj = (RegistroNegativacaoCobrancaContaReceberVO) context().getExternalContext().getRequestMap().get("registro");
			getRegistroNegativacaoCobrancaContaReceberVO().setNovoObj(false);
			getRegistroNegativacaoCobrancaContaReceberVO().setCodigo(obj.getCodigo());
			getRegistroNegativacaoCobrancaContaReceberVO().setCentroReceitaApresentar(obj.getCentroReceitaApresentar());
			setCursoVO(obj.getCursoVO());
			setTurmaVO(obj.getTurmaVO());
			getRegistroNegativacaoCobrancaContaReceberVO().setUsuarioVO(obj.getUsuarioVO());
			getRegistroNegativacaoCobrancaContaReceberVO().setDataGeracao(obj.getDataGeracao());
			setAgente(obj.getAgente());
			setTipoAgente(obj.getTipoAgente());
			getRegistroNegativacaoCobrancaContaReceberVO().setAgente(getAgente());
			getRegistroNegativacaoCobrancaContaReceberVO().setTipoAgente(getTipoAgente());
			setUnidadeEnsino(obj.getUnidadeEnsinoVO());
			setPeriodoInicial(obj.getDataInicioFiltro());
			setPeriodoFinal(obj.getDataFimFiltro());
			getMatricula().setMatricula(obj.getMatricula());
			getMatricula().getAluno().setNome(obj.getAluno());
			setFiltroRelatorioAcademicoVO(obj.getFiltroRelatorioAcademicoVO());
			setFiltroRelatorioFinanceiroVO(obj.getFiltroRelatorioFinanceiroVO());
			carregarListaUnidadeEnsino();
			isPermitirRegistrarNegativacaoContaReceberViaIntegracao();
			getRegistroNegativacaoCobrancaContaReceberVO().setListaContasReceberCobranca(getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().consultarContasReceberPorRegistroNegativacaoCobranca(getRegistroNegativacaoCobrancaContaReceberVO().getCodigo(), getUsuarioLogado()));
			setMensagemID("msg_entre_prmconsulta");
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroNegativacaoCobrancaContaReceberForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void irPaginaInicial() throws Exception {
		removerObjetoMemoria(getRegistroNegativacaoCobrancaContaReceberVO());
		this.consultar();
	}
	
	 public void isPermitirRegistrarNegativacaoContaReceberViaIntegracao() {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.MAPA_NEGATIVACAO_COBRANCA_CONTA_RECEBER_VIA_INTEGRACAO, getUsuarioLogadoClone());
				setApresentarRegistroViaIntegracao(true);				
			} catch (Exception e) {
				setApresentarRegistroViaIntegracao(false);
			}
		}

	public RegistroNegativacaoCobrancaContaReceberVO getRegistroNegativacaoCobrancaContaReceberVO() {
		if (registroNegativacaoCobrancaContaReceberVO == null) {
			registroNegativacaoCobrancaContaReceberVO = new RegistroNegativacaoCobrancaContaReceberVO();
		}
		return registroNegativacaoCobrancaContaReceberVO;
	}

	public void setRegistroNegativacaoCobrancaContaReceberVO(RegistroNegativacaoCobrancaContaReceberVO registroNegativacaoCobrancaContaReceberVO) {
		this.registroNegativacaoCobrancaContaReceberVO = registroNegativacaoCobrancaContaReceberVO;
	}

	public String getCampoConsulta() {
		if (campoConsulta == null) {
			campoConsulta = "";
		}
		return campoConsulta;
	}

	public void setCampoConsulta(String campoConsulta) {
		this.campoConsulta = campoConsulta;
	}

	public String getValorConsulta() {
		if (valorConsulta == null) {
			valorConsulta = "";
		}
		return valorConsulta;
	}

	public void setValorConsulta(String valorConsulta) {
		this.valorConsulta = valorConsulta;
	}

	public Date getDataGeracaoInicio() {
		return dataGeracaoInicio;
	}

	public void setDataGeracaoInicio(Date dataGeracaoInicio) {
		this.dataGeracaoInicio = dataGeracaoInicio;
	}

	public Date getDataGeracaoFim() {
		return dataGeracaoFim;
	}

	public void setDataGeracaoFim(Date dataGeracaoFim) {
		this.dataGeracaoFim = dataGeracaoFim;
	}

	public String novo() throws Exception {
		setRegistroNegativacaoCobrancaContaReceberVO(new RegistroNegativacaoCobrancaContaReceberVO());
		getRegistroNegativacaoCobrancaContaReceberVO().setUsuarioVO(getUsuarioLogadoClone());
		getRegistroNegativacaoCobrancaContaReceberVO().setDataGeracao(new Date());
		setAgente(new AgenteNegativacaoCobrancaContaReceberVO());
		setTipoAgente(TipoAgenteNegativacaoCobrancaContaReceberEnum.NEGATIVACAO);
		setFiltroRelatorioAcademicoVO(new FiltroRelatorioAcademicoVO());
		setFiltroRelatorioFinanceiroVO(new FiltroRelatorioFinanceiroVO(getLoginControle().getPermissaoAcessoMenuVO().getPermitirApenasContasDaBiblioteca()));
		//consultarCentroReceitaFiltroRelatorio();
		setMarcarTodasSituacoesAcademicas(false);
		setMarcarTodasSituacoesFinanceiras(false);
		setCursoVO(new CursoVO());
		setListaConsultaCurso(null);
		setListaConsultaAgente(null);
		setTurmaVO(new TurmaVO());
		setListaConsultaTurma(null);
		setMatricula(new MatriculaVO());
		setListaConsultaAluno(null);
		setUnidadeEnsino(new UnidadeEnsinoVO());
		setPeriodoInicial(new Date());
		setPeriodoFinal(new Date());
		carregarListaUnidadeEnsino();
		isPermitirRegistrarNegativacaoContaReceberViaIntegracao();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("registroNegativacaoCobrancaContaReceberForm.xhtml");
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatricula().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), 0, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			if (obj.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + obj.getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}			
			setMatricula(obj);
			setFiltroConsultaAluno("");
			setCampoConsultaAluno("");
			getListaConsultaAluno().clear();
			if(!getFacadeFactory().getContaReceberFacade().realizarVerificacaoAlunoPossuiContaAReceberVinculadoUnidadeEnsinoFinanceira(obj.getMatricula(), getUnidadeEnsino().getCodigo())) {
				getUnidadeEnsino().setCodigo(obj.getUnidadeEnsino().getCodigo());
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public FiltroRelatorioAcademicoVO getFiltroRelatorioAcademicoVO() {
		if (filtroRelatorioAcademicoVO == null) {
			filtroRelatorioAcademicoVO = new FiltroRelatorioAcademicoVO();
		}
		return filtroRelatorioAcademicoVO;
	}

	public void setFiltroRelatorioAcademicoVO(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) {
		this.filtroRelatorioAcademicoVO = filtroRelatorioAcademicoVO;
	}

	public FiltroRelatorioFinanceiroVO getFiltroRelatorioFinanceiroVO() {
		if (filtroRelatorioFinanceiroVO == null) {
			filtroRelatorioFinanceiroVO = new FiltroRelatorioFinanceiroVO(getLoginControle().getPermissaoAcessoMenuVO().getPermitirApenasContasDaBiblioteca());
		}
		return filtroRelatorioFinanceiroVO;
	}

	public void setFiltroRelatorioFinanceiroVO(FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) {
		this.filtroRelatorioFinanceiroVO = filtroRelatorioFinanceiroVO;
	}
	
	public void realizarSelecaoCheckboxMarcarDesmarcarTodosSituacaoFinanceira() {
		if (getMarcarTodasSituacoesFinanceiras()) {
			getFiltroRelatorioFinanceiroVO().realizarMarcarTodosTipoOrigem();
		} else {
			getFiltroRelatorioFinanceiroVO().realizarDesmarcarTodosTipoOrigem();
		}
	}

	public Boolean getMarcarTodasSituacoesFinanceiras() {
		if (marcarTodasSituacoesFinanceiras == null) {
			marcarTodasSituacoesFinanceiras = false;
		}
		return marcarTodasSituacoesFinanceiras;
	}

	public void setMarcarTodasSituacoesFinanceiras(Boolean marcarTodasSituacoesFinanceiras) {
		this.marcarTodasSituacoesFinanceiras = marcarTodasSituacoesFinanceiras;
	}
	
	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodosFinanceiro() {
		if (getMarcarTodasSituacoesFinanceiras()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}
	
	public void marcarTodosCentroReceitasAction() {
		for (CentroReceitaVO centroReceitaVO : centroReceitaVOs) {
			centroReceitaVO.setFiltrarCentroReceitaVO(getMarcarTodosCentroReceitas());
		}
		verificarTodosCentroReceitaSelecionados();
	}
	
	public void verificarTodosCentroReceitaSelecionados() {
		StringBuilder centroReceita = new StringBuilder();
		if (centroReceitaVOs.size() > 1) {
			for (CentroReceitaVO obj : centroReceitaVOs) {
				if (obj.getFiltrarCentroReceitaVO()) {
					centroReceita.append(obj.getDescricao().trim()).append("; ");
				}
			}
			getRegistroNegativacaoCobrancaContaReceberVO().setCentroReceitaApresentar(centroReceita.toString());
		} else {
			if (!centroReceitaVOs.isEmpty()) {
				if (centroReceitaVOs.get(0).getFiltrarCentroReceitaVO()) {
					getRegistroNegativacaoCobrancaContaReceberVO().setCentroReceitaApresentar(centroReceitaVOs.get(0).getDescricao().trim());
				}
			} else {
				getRegistroNegativacaoCobrancaContaReceberVO().setCentroReceitaApresentar(centroReceita.toString());
			}
		}
	}
	
	public Boolean getMarcarTodosCentroReceitas() {
		if (marcarTodosCentroReceitas == null) {
			marcarTodosCentroReceitas = false;
		}
		return marcarTodosCentroReceitas;
	}
	
	public void setMarcarTodosCentroReceitas(Boolean marcarTodosCentroReceitas) {
		this.marcarTodosCentroReceitas = marcarTodosCentroReceitas;
	}
	
	public List<CentroReceitaVO> getCentroReceitaVOs() {
		if (centroReceitaVOs == null) {
			centroReceitaVOs = new ArrayList<CentroReceitaVO>(0);
		}
		return centroReceitaVOs;
	}
	
	public void setCentroReceitaVOs(List<CentroReceitaVO> centroReceitaVOs) {
		this.centroReceitaVOs = centroReceitaVOs;
	}
	
	public void consultarCentroReceitaFiltroRelatorio() {
		try {
			setCentroReceitaVOs(getFacadeFactory().getCentroReceitaFacade().consultarCentroReceitaVinculadoContaReceber(null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparCentroReceita() {
		getRegistroNegativacaoCobrancaContaReceberVO().setCentroReceitaApresentar("");
		setMarcarTodosCentroReceitas(false);
		marcarTodosCentroReceitasAction();
	}

	public AgenteNegativacaoCobrancaContaReceberVO getAgente() {
		if (agente == null) {
			agente = new AgenteNegativacaoCobrancaContaReceberVO();
		}
		return agente;
	}

	public void setAgente(AgenteNegativacaoCobrancaContaReceberVO agente) {
		this.agente = agente;
	}

	public RegistroNegativacaoCobrancaContaReceberItemVO getRegistroExclusao() {
		if (registroExclusao == null) {
			registroExclusao = new RegistroNegativacaoCobrancaContaReceberItemVO();
		}
		return registroExclusao;
	}

	public void setRegistroExclusao(RegistroNegativacaoCobrancaContaReceberItemVO registroExclusao) {
		this.registroExclusao = registroExclusao;
	}
	
	public String getRetornoExclusao() {
		if (retornoExclusao == null) {
			retornoExclusao = "";
		}
		return retornoExclusao;
	}

	public void setRetornoExclusao(String retornoExclusao) {
		this.retornoExclusao = retornoExclusao;
	}

	public String getCampoConsultaAgente() {
		if (campoConsultaAgente == null) {
			campoConsultaAgente = "";
		}
		return campoConsultaAgente;
	}

	public void setCampoConsultaAgente(String campoConsultaAgente) {
		this.campoConsultaAgente = campoConsultaAgente;
	}

	public String getValorConsultaAgente() {
		if (valorConsultaAgente == null) {
			valorConsultaAgente = "";
		}
		return valorConsultaAgente;
	}

	public void setValorConsultaAgente(String valorConsultaAgente) {
		this.valorConsultaAgente = valorConsultaAgente;
	}

	public List<AgenteNegativacaoCobrancaContaReceberVO> getListaConsultaAgente() {
		if (listaConsultaAgente == null) {
			listaConsultaAgente = new ArrayList<AgenteNegativacaoCobrancaContaReceberVO>();
		}
		return listaConsultaAgente;
	}

	public void setListaConsultaAgente(List<AgenteNegativacaoCobrancaContaReceberVO> listaConsultaAgente) {
		this.listaConsultaAgente = listaConsultaAgente;
	}

	public TipoAgenteNegativacaoCobrancaContaReceberEnum getTipoAgente() {
		return tipoAgente;
	}

	public void setTipoAgente(TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente) {
		this.tipoAgente = tipoAgente;
	}
	
	/**
	 * @return the valorConsultaUnidadeEnsino
	 */
	public int getValorConsultaUnidadeEnsino() {		
		return valorConsultaUnidadeEnsino;
	}

	/**
	 * @param valorConsultaUnidadeEnsino
	 *            the valorConsultaUnidadeEnsino to set
	 */
	public void setValorConsultaUnidadeEnsino(int valorConsultaUnidadeEnsino) {
		this.valorConsultaUnidadeEnsino = valorConsultaUnidadeEnsino;
	}
	
	public void changeTipoConsultaCombo() {
		getControleConsulta().setValorConsulta("");
		setValorConsultaUnidadeEnsino(0);
		if(getControleConsulta().getCampoConsulta().equals("nossonumero")) {
			setCampoConsultaNossoNumero(true);
			if(this.getUnidadeEnsinoLogado().getCodigo() > 0) {
				setValorConsultaUnidadeEnsino(this.getUnidadeEnsinoLogado().getCodigo());
			}
		}else {
			setCampoConsultaNossoNumero(false);
		}
	}

	public Boolean getCampoConsultaNossoNumero() {
		return campoConsultaNossoNumero;
	}

	public void setCampoConsultaNossoNumero(Boolean campoConsultaNossoNumero) {
		this.campoConsultaNossoNumero = campoConsultaNossoNumero;
	}


	public boolean isApresentarRegistroViaIntegracao() {
		return apresentarRegistroViaIntegracao;
	}


	public void setApresentarRegistroViaIntegracao(boolean apresentarRegistroViaIntegracao) {
		this.apresentarRegistroViaIntegracao = apresentarRegistroViaIntegracao;
	}
	
	
	
	

}