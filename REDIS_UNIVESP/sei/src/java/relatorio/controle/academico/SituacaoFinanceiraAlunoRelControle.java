package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.SituacaoFinanceiraAlunoRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.jdbc.academico.SituacaoFinanceiraAlunoRel;

@SuppressWarnings("unchecked")
@Controller("SituacaoFinanceiraAlunoRelControle")
@Scope("viewScope")
@Lazy
public class SituacaoFinanceiraAlunoRelControle extends SuperControleRelatorio {

	private MatriculaVO matriculaVO;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List listaConsultaAluno;
	private SituacaoFinanceiraAlunoRelVO situacaoFinanceiraAlunoRelVO;
	private ParceiroVO parceiroVO;
	private PessoaVO responsavelFinanceiro;
	private String campoConsultaParceiro;
	private String valorConsultaParceiro;
	private List listaConsultaParceiro;
	private List listaAlunosParceiro;
	private String tipoPessoa;
	private Boolean imprimirObservacao;
	private String layout;
	private String ano;
	private String semestre;
	private Boolean apresentarTipoAlunoParceiro;
	private String tipoAlunoParceiro;
	private String tipoLayout;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private Boolean filtrarPorDataCompetencia;
	private Date dataInicio;
	private Date dataFim;
	private FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO;
	private Boolean marcarTodasSituacoesFinanceiraAluno;
	private Boolean marcarTodosTipoOrigem;
	
	private CentroReceitaVO centroReceitaVO;
	protected List<CentroReceitaVO> listaConsultaCentroReceitaVOs;
    protected String valorConsultaCentroReceita;
    protected String campoConsultaCentroReceita;

	public SituacaoFinanceiraAlunoRelControle() throws Exception {
		setTipoPessoa("aluno");
		setMensagemID("msg_entre_prmrelatorio");
		realizarSelecaoTodasOrigens(true);
	}
	
	
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("SituacaoFinanceiraAlunoRel");
			verificarTodasUnidadesSelecionadas();
			realizarConsultaPreferenciasUsuario();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@PostConstruct
	public void inicializarMatriculaFollowUp(){
		consultarUnidadeEnsino();
		try {
			String matricula = (String) context().getExternalContext().getSessionMap().get("matriculaItens");
			if (matricula != null && !matricula.trim().equals("")) {
				getMatriculaVO().setMatricula(matricula);
				consultarAlunoPorMatricula();
				context().getExternalContext().getSessionMap().remove("matriculaItens");
			}
			PessoaVO responsavelFinanceiroVO = (PessoaVO) context().getExternalContext().getSessionMap().get("responsavelFinanceiroInteracaoWorkFlow");
			if (responsavelFinanceiroVO != null && !responsavelFinanceiroVO.getCodigo().equals(0)) {
				setTipoPessoa("responsavelFinanceiro");
				setResponsavelFinanceiro(responsavelFinanceiroVO);
				context().getExternalContext().getSessionMap().remove("responsavelFinanceiroInteracaoWorkFlow");
			}
		} catch (Exception e) {
			
		}
		
	}
	
	public void realizarConsultaPreferenciasUsuario() {
		try {
			getFiltroRelatorioFinanceiroVO().setSituacaoReceber(getFacadeFactory().getParametroRelatorioFacade().consultarPorEntidadeCampo("situacaoFinanceiraAlunoRel", "situacaoReceber", false, getUsuarioLogado()).getApresentarCampo());
			getFiltroRelatorioFinanceiroVO().setSituacaoRecebido(getFacadeFactory().getParametroRelatorioFacade().consultarPorEntidadeCampo("situacaoFinanceiraAlunoRel", "situacaoRecebido", false, getUsuarioLogado()).getApresentarCampo());
			getFiltroRelatorioFinanceiroVO().setSituacaoCancelado(getFacadeFactory().getParametroRelatorioFacade().consultarPorEntidadeCampo("situacaoFinanceiraAlunoRel", "situacaoCancelado", false, getUsuarioLogado()).getApresentarCampo());
			getFiltroRelatorioFinanceiroVO().setSituacaoRenegociado(getFacadeFactory().getParametroRelatorioFacade().consultarPorEntidadeCampo("situacaoFinanceiraAlunoRel", "situacaoRenegociado", false, getUsuarioLogado()).getApresentarCampo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void imprimirPDF() {
		String titulo = null;
		UnidadeEnsinoVO uni = null;
		List<SituacaoFinanceiraAlunoRelVO> lista = null;
		try {
			if (getTipoAlunoParceiro().equals("parceiro")) {
				if (getParceiroVO().getCodigo().intValue() > 0) {
					setParceiroVO(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(getParceiroVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				} else {
					setParceiroVO(getFacadeFactory().getParceiroFacade().consultarPorMatriculaAluno(getMatriculaVO().getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				}
				setTipoPessoa("parceiro");
			}
			SituacaoFinanceiraAlunoRel.validarDados(getMatriculaVO(), getTipoPessoa(), getResponsavelFinanceiro());

			if (getIsTipoLayotSituacaFinanceiraAluno()) {
				titulo = UteisJSF.internacionalizar("prt_SituacaoFinanceiraAluno_tituloRelatorio");
			} else {
				titulo = "Nada Consta";
			}
			if (getMatriculaVO().getUnidadeEnsino() != null && !getMatriculaVO().getUnidadeEnsino().getCodigo().equals(0)) {
				getMatriculaVO().getUnidadeEnsino().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
				getFacadeFactory().getUnidadeEnsinoFacade().carregarDados(getMatriculaVO().getUnidadeEnsino(), NivelMontarDados.BASICO, getUsuarioLogado());
				uni = getMatriculaVO().getUnidadeEnsino();
			} else {
				uni = getFacadeFactory().getUnidadeEnsinoFacade().obterUnidadeMatriz(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getSituacaoFinanceiraAlunoRelVO().getObservacaoComplementar() != null) {
				if (!getImprimirObservacao() && !getSituacaoFinanceiraAlunoRelVO().getObservacaoComplementar().equals("")) {
					getSituacaoFinanceiraAlunoRelVO().setObservacaoComplementar(null);
				}
			}
			lista = getFacadeFactory().getContaReceberAlunosRelFacade().criarObjeto(getMatriculaVO(), getSituacaoFinanceiraAlunoRelVO(), getTipoPessoa(), getParceiroVO(), getUsuarioLogado(), getUnidadeEnsinoVOs(), getConfiguracaoFinanceiroPadraoSistema(), getResponsavelFinanceiro(), getAno(), getSemestre(), getDataInicio(), getDataFim(), getFiltrarPorDataCompetencia(), getFiltroRelatorioFinanceiroVO(), getCentroReceitaVO(), getTipoLayout());
			if (!lista.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(SituacaoFinanceiraAlunoRel.getDesignIReportRelatorio(getLayout(), getTipoLayout()));
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(SituacaoFinanceiraAlunoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setListaObjetos(lista);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(SituacaoFinanceiraAlunoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setQuantidade(lista.size());
				getSuperParametroRelVO().setParceiro(getParceiroVO().getNome());
				getSuperParametroRelVO().adicionarParametro("dataPorExtenso", Uteis.getDataCidadeEstadoDiaMesPorExtensoEAno(uni.getCidade().getNome(), uni.getCidade().getEstado().getSigla(), new Date(), true));
				realizarImpressaoRelatorio();
				realizarPersistenciaPreferenciasUsuario();
				removerObjetoMemoria(this);
				setTipoPessoa("aluno");
				setMensagemID("msg_relatorio_ok");
				realizarSelecaoTodasOrigens(true);
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(lista);
			titulo = null;
			uni = null;

		}
	}
	
	public void realizarPersistenciaPreferenciasUsuario() {
		try {
			getFacadeFactory().getParametroRelatorioFacade().persistirParametroRelatorio("situacaoFinanceiraAlunoRel", "situacaoReceber", getFiltroRelatorioFinanceiroVO().getSituacaoReceber(), getUsuarioLogado());
			getFacadeFactory().getParametroRelatorioFacade().persistirParametroRelatorio("situacaoFinanceiraAlunoRel", "situacaoRecebido", getFiltroRelatorioFinanceiroVO().getSituacaoRecebido(), getUsuarioLogado());
			getFacadeFactory().getParametroRelatorioFacade().persistirParametroRelatorio("situacaoFinanceiraAlunoRel", "situacaoCancelado", getFiltroRelatorioFinanceiroVO().getSituacaoCancelado(), getUsuarioLogado());
			getFacadeFactory().getParametroRelatorioFacade().persistirParametroRelatorio("situacaoFinanceiraAlunoRel", "situacaoRenegociado", getFiltroRelatorioFinanceiroVO().getSituacaoRenegociado(), getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);

			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatriculaListaUnidadeEnsinoVOs(getValorConsultaAluno(), this.getUnidadeEnsinoVOs(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				if (!obj.getMatricula().equals("")) {
					objs.add(obj);
				}
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoaListaUnidadeEnsino(getValorConsultaAluno(), this.getUnidadeEnsinoVOs(), false, "", getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCursoListaUnidadeEnsinoVOs(getValorConsultaAluno(), this.getUnidadeEnsinoVOs(), false, "", getUsuarioLogado());
			}

			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			setMatriculaVO(obj);
			obj = null;
			valorConsultaAluno = "";
			campoConsultaAluno = "";
			getListaConsultaAluno().clear();
			setParceiroVO(getFacadeFactory().getParceiroFacade().consultarPorMatriculaAluno(getMatriculaVO().getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (getParceiroVO().getCodigo().intValue() != 0) {
				setApresentarTipoAlunoParceiro(Boolean.TRUE);
				selecionarTipoAlunoParceiro();
			}
		} catch (Exception e) {

		}
	}

	public void consultarAlunoPorMatricula() throws Exception {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatriculaListaUnidadeEnsinoVOs(getMatriculaVO().getMatricula(), this.getUnidadeEnsinoVOs(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de Matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de Matrícula está correto.");
			}
			setMatriculaVO(objAluno);
			setParceiroVO(getFacadeFactory().getParceiroFacade().consultarPorMatriculaAluno(getMatriculaVO().getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (getParceiroVO().getCodigo().intValue() != 0) {
				setApresentarTipoAlunoParceiro(Boolean.TRUE);
				selecionarTipoAlunoParceiro();
			}
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatriculaVO(new MatriculaVO());
		}
	}

	public void selecionarTipoAlunoParceiro() {
		try {
			if (getTipoAlunoParceiro().equals("parceiro")) {
				setTipoPessoa("parceiro");
				getListaAlunosParceiro().add(getMatriculaVO());
			} else {
				getListaAlunosParceiro().clear();
				setTipoPessoa("aluno");
				setParceiroVO(null);
			}
		} catch (Exception e) {

		}
	}

	public void limparDadosAluno() throws Exception {
		setMatriculaVO(new MatriculaVO());
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", UteisJSF.internacionalizar("prt_SituacaoFinanceiraAluno_matricula")));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public void imprimirPDFVindoOutraTela(MatriculaVO obj) {
		try {
			SituacaoFinanceiraAlunoRel.validarDados(obj, getTipoPessoa(), getResponsavelFinanceiro());

			String titulo = UteisJSF.internacionalizar("prt_SituacaoFinanceiraAluno_tituloRelatorio");
			String nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
			String design = SituacaoFinanceiraAlunoRel.getDesignIReportRelatorio(getLayout(), "SituacaoFinanceiraAlunoRel");
			apresentarRelatorioObjetos(SituacaoFinanceiraAlunoRel.getIdEntidade(), titulo, nomeEntidade, "", "PDF", "", design, getUsuarioLogado().getNome(), "", getFacadeFactory().getContaReceberAlunosRelFacade().criarObjeto(obj, getSituacaoFinanceiraAlunoRelVO(), getTipoPessoa(), getParceiroVO(), getUsuarioLogado(), this.getUnidadeEnsinoVOs(), getConfiguracaoFinanceiroPadraoSistema(), getResponsavelFinanceiro(), getAno(), getSemestre(), getDataInicio(), getDataFim(), getFiltrarPorDataCompetencia(), new FiltroRelatorioFinanceiroVO(getLoginControle().getPermissaoAcessoMenuVO().getPermitirApenasContasDaBiblioteca()), null, getTipoLayout()), SituacaoFinanceiraAlunoRel.getCaminhoBaseRelatorio());
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemTipoPessoa() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("aluno", "Aluno"));
		itens.add(new SelectItem("responsavelFinanceiro", UteisJSF.internacionalizar("prt_SituacaoFinanceiraAluno_responsavelFinanceiro")));
		itens.add(new SelectItem("parceiro", "Parceiro"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemTipoAlunoParceiro() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("aluno", "Aluno"));
		itens.add(new SelectItem("parceiro", "Parceiro"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemSemestre() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("1", UteisJSF.internacionalizar("msg_SituacaoFinanceiraAluno_semestre1")));
		itens.add(new SelectItem("2", UteisJSF.internacionalizar("msg_SituacaoFinanceiraAluno_semestre2")));
		return itens;
	}

	public void consultarParceiro() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaParceiro().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getParceiroFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("nome")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorNome(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("razaoSocial")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocial(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("RG")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRG(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("CPF")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorCPF(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("tipoParceiro")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorTipoParceiro(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaParceiro(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarParceiro() {
		try {
			ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
			setParceiroVO(obj);
			consultarAlunosQueTemContaVinculadaAoParceiroSelecionado(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunosQueTemContaVinculadaAoParceiroSelecionado(ParceiroVO obj) throws Exception {
		setListaAlunosParceiro(getFacadeFactory().getMatriculaFacade().consultarAlunosPossuemParceiroDadosComboBox(getUnidadeEnsinoVOs(), obj.getCodigo(), getUsuarioLogado()));
	}

	public void selecionarAlunoParceiro() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoParceiroItens");
		setMatriculaVO(obj);
		try {
			getFacadeFactory().getMatriculaFacade().carregarDados(getMatriculaVO(), NivelMontarDados.BASICO, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosAlunoParceiro() {
		setMatriculaVO(new MatriculaVO());
		setParceiroVO(new ParceiroVO());
		limparDadosResponsavelFinanceiro();
	}

	public void limparDadosResponsavelFinanceiro() {
		getResponsavelFinanceiro().setCodigo(0);
		getResponsavelFinanceiro().setNome("");
	}

	public List getTipoConsultaComboParceiro() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", UteisJSF.internacionalizar("prt_SituacaoFinanceiraAluno_razaoSocial")));
		itens.add(new SelectItem("RG", "RG"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("tipoParceiro", "Tipo Parceiro"));
		return itens;
	}

	public List getTipoConsultaComboLayout() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("retrato", "Retrato"));
		itens.add(new SelectItem("paisagem", "Paisagem"));
		return itens;
	}

	public boolean getIsApresentarCampoAluno() {
		return getTipoPessoa().equals("aluno");
	}

	public boolean getIsApresentarCampoResponsavelFinanceiro() {
		return getTipoPessoa().equals("responsavelFinanceiro");
	}
	
	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			if (getMarcarTodasUnidadeEnsino()) {
				unidade.setFiltrarUnidadeEnsino(Boolean.TRUE);
			} else {
				unidade.setFiltrarUnidadeEnsino(Boolean.FALSE);
			}
		}
		verificarTodasUnidadesSelecionadas();
	}

	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome()).append("; ");
				} 
			}
			getUnidadeEnsinoVO().setNome(unidade.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					getUnidadeEnsinoVO().setNome(getUnidadeEnsinoVOs().get(0).getNome());
				} 
			} else {
				getUnidadeEnsinoVO().setNome(unidade.toString());
			}			
		}
	}

	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
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

	public List getListaConsultaAluno() {
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.academico.SituacaoFinanceiraAlunoRelInterfaceFacade
	 * #getSituacaoFinanceiraAlunoRelVO()
	 */
	public SituacaoFinanceiraAlunoRelVO getSituacaoFinanceiraAlunoRelVO() {
		if (situacaoFinanceiraAlunoRelVO == null) {
			situacaoFinanceiraAlunoRelVO = new SituacaoFinanceiraAlunoRelVO();
		}
		return situacaoFinanceiraAlunoRelVO;
	}

	public void setSituacaoFinanceiraAlunoRelVO(SituacaoFinanceiraAlunoRelVO situacaoFinanceiraAlunoRelVO) {
		this.situacaoFinanceiraAlunoRelVO = situacaoFinanceiraAlunoRelVO;
	}

	public String getTipoPessoa() {
		if (tipoPessoa == null) {
			tipoPessoa = "";
		}
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public ParceiroVO getParceiroVO() {
		if (parceiroVO == null) {
			parceiroVO = new ParceiroVO();
		}
		return parceiroVO;
	}

	public void setParceiroVO(ParceiroVO parceiroVO) {
		this.parceiroVO = parceiroVO;
	}

	public String getCampoConsultaParceiro() {
		if (campoConsultaParceiro == null) {
			campoConsultaParceiro = "";
		}
		return campoConsultaParceiro;
	}

	public void setCampoConsultaParceiro(String campoConsultaParceiro) {
		this.campoConsultaParceiro = campoConsultaParceiro;
	}

	public String getValorConsultaParceiro() {
		if (valorConsultaParceiro == null) {
			valorConsultaParceiro = "";
		}
		return valorConsultaParceiro;
	}

	public void setValorConsultaParceiro(String valorConsultaParceiro) {
		this.valorConsultaParceiro = valorConsultaParceiro;
	}

	public List getListaConsultaParceiro() {
		if (listaConsultaParceiro == null) {
			listaConsultaParceiro = new ArrayList(0);
		}
		return listaConsultaParceiro;
	}

	public void setListaConsultaParceiro(List listaConsultaParceiro) {
		this.listaConsultaParceiro = listaConsultaParceiro;
	}

	public List getListaAlunosParceiro() {
		if (listaAlunosParceiro == null) {
			listaAlunosParceiro = new ArrayList(0);
		}
		return listaAlunosParceiro;
	}

	public void setListaAlunosParceiro(List listaAlunosParceiro) {
		this.listaAlunosParceiro = listaAlunosParceiro;
	}

	public Boolean getIsApresentarNomeAlunoParceiro() {
		if (!getParceiroVO().getNome().equals("")) {
			return true;
		}
		return false;
	}

	public Boolean getImprimirObservacao() {
		if (imprimirObservacao == null) {
			imprimirObservacao = Boolean.FALSE;
		}
		return imprimirObservacao;
	}

	public void setImprimirObservacao(Boolean imprimirObservacao) {
		this.imprimirObservacao = imprimirObservacao;
	}

	public String getLayout() {
		if (layout == null) {
			layout = "retrato";
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	protected List<PessoaVO> listaConsultaResponsavelFinanceiro;
	protected String valorConsultaResponsavelFinanceiro;
	protected String campoConsultaResponsavelFinanceiro;

	public void consultarResponsavelFinanceiro() {
		try {

			if (getValorConsultaResponsavelFinanceiro().trim().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("ResponsavelFinanceiro");
			getListaConsultaResponsavelFinanceiro().clear();
			if (getCampoConsultaResponsavelFinanceiro().equals("nome")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorNomeResponsavelFinanceiroListaUnidadeEnsinoVOs(getValorConsultaResponsavelFinanceiro(), this.getUnidadeEnsinoVOs(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaResponsavelFinanceiro().equals("nomeAluno")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorNomeAlunoResponsavelFinanceiroListaUnidadeEnsinoVOs(getValorConsultaResponsavelFinanceiro(), this.getUnidadeEnsinoVOs(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaResponsavelFinanceiro().equals("CPF")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorCpfResponsavelFinanceiroListaUnidadeEnsinoVOs(getValorConsultaResponsavelFinanceiro(), this.getUnidadeEnsinoVOs(), false, getUsuarioLogado()));
			}

			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaResponsavelFinanceiro(new ArrayList<PessoaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<SelectItem> tipoConsultaComboResponsavelFinanceiro;

	public List<SelectItem> getTipoConsultaComboResponsavelFinanceiro() {
		if (tipoConsultaComboResponsavelFinanceiro == null) {
			tipoConsultaComboResponsavelFinanceiro = new ArrayList<SelectItem>(0);
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nomeAluno", "Aluno"));
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("CPF", "CPF"));
		}
		return tipoConsultaComboResponsavelFinanceiro;
	}

	public void selecionarResponsavelFinanceiro() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("responsavelFinanceiroItens");
			getListaConsultaResponsavelFinanceiro().clear();
			this.setResponsavelFinanceiro(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<PessoaVO> getListaConsultaResponsavelFinanceiro() {
		if (listaConsultaResponsavelFinanceiro == null) {
			listaConsultaResponsavelFinanceiro = new ArrayList<PessoaVO>(0);
		}
		return listaConsultaResponsavelFinanceiro;
	}

	public void setListaConsultaResponsavelFinanceiro(List<PessoaVO> listaConsultaResponsavelFinanceiro) {
		this.listaConsultaResponsavelFinanceiro = listaConsultaResponsavelFinanceiro;
	}

	public String getValorConsultaResponsavelFinanceiro() {
		if (valorConsultaResponsavelFinanceiro == null) {
			valorConsultaResponsavelFinanceiro = "";
		}
		return valorConsultaResponsavelFinanceiro;
	}

	public void setValorConsultaResponsavelFinanceiro(String valorConsultaResponsavelFinanceiro) {
		this.valorConsultaResponsavelFinanceiro = valorConsultaResponsavelFinanceiro;
	}

	public String getCampoConsultaResponsavelFinanceiro() {
		if (campoConsultaResponsavelFinanceiro == null) {
			campoConsultaResponsavelFinanceiro = "";
		}
		return campoConsultaResponsavelFinanceiro;
	}

	public void setCampoConsultaResponsavelFinanceiro(String campoConsultaResponsavelFinanceiro) {
		this.campoConsultaResponsavelFinanceiro = campoConsultaResponsavelFinanceiro;
	}

	public PessoaVO getResponsavelFinanceiro() {
		if (responsavelFinanceiro == null) {
			responsavelFinanceiro = new PessoaVO();
		}
		return responsavelFinanceiro;
	}

	public void setResponsavelFinanceiro(PessoaVO responsavelFinanceiro) {
		this.responsavelFinanceiro = responsavelFinanceiro;
	}

	public Boolean getApresentarAnoSemestre() {
		return !getMatriculaVO().getCurso().getNivelEducacional().equals("PO") && !getMatriculaVO().getCurso().getNivelEducacional().equals("EX") && getTipoPessoa().equals("aluno") && !getMatriculaVO().getMatricula().equals("");
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	/**
	 * @return the apresentarTipoAlunoParceiro
	 */
	public Boolean getApresentarTipoAlunoParceiro() {
		if (apresentarTipoAlunoParceiro == null) {
			apresentarTipoAlunoParceiro = Boolean.FALSE;
		}
		return apresentarTipoAlunoParceiro;
	}

	/**
	 * @param apresentarTipoAlunoParceiro
	 *            the apresentarTipoAlunoParceiro to set
	 */
	public void setApresentarTipoAlunoParceiro(Boolean apresentarTipoAlunoParceiro) {
		this.apresentarTipoAlunoParceiro = apresentarTipoAlunoParceiro;
	}

	/**
	 * @return the tipoAlunoParceiro
	 */
	public String getTipoAlunoParceiro() {
		if (tipoAlunoParceiro == null) {
			tipoAlunoParceiro = "";
		}
		return tipoAlunoParceiro;
	}

	/**
	 * @param tipoAlunoParceiro
	 *            the tipoAlunoParceiro to set
	 */
	public void setTipoAlunoParceiro(String tipoAlunoParceiro) {
		this.tipoAlunoParceiro = tipoAlunoParceiro;
	}

	//
	// public boolean getApresentarTipoAlunoParceiro() {
	// if (getParceiroVO().getCodigo().intValue() != 0 &&
	// getTipoPessoa().equals("aluno")) {
	// return true;
	// } else {
	// return false;
	// }
	// }

	public List<SelectItem> getListaSelectItemTipoLayou() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("SituacaoFinanceiraAlunoRel", UteisJSF.internacionalizar("prt_SituacaoFinanceiraAluno_tituloRelatorio")));
		objs.add(new SelectItem("SituacaoFinanceiraAlunoRelLayout1", "Situação Financeira Aluno Com Centro Receita"));
		objs.add(new SelectItem("SituacaoFinanceiraAlunoNadaConstaRel", "Nada Consta Aluno"));
		return objs;
	}

	public boolean getIsTipoLayotSituacaFinanceiraAluno() {
		return getTipoLayout().equals("SituacaoFinanceiraAlunoRel") || getTipoLayout().equals("SituacaoFinanceiraAlunoRelLayout1");
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "SituacaoFinanceiraAlunoRel";
		}
		return tipoLayout;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}
	
	public Boolean getFiltrarPorDataCompetencia() {
		if (filtrarPorDataCompetencia == null) {
			filtrarPorDataCompetencia = false;
		}
		return filtrarPorDataCompetencia;
	}

	public void setFiltrarPorDataCompetencia(Boolean filtrarPorDataCompetencia) {
		this.filtrarPorDataCompetencia = filtrarPorDataCompetencia;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public void preencherDadosPeriodo() throws Exception {
		Integer ano = getAno().equals("") ? 0 : Integer.valueOf(getAno());
		Integer semestre = getSemestre().equals("") ? 0 : Integer.valueOf(getSemestre());
		setDataInicio(Uteis.getDataInicioSemestreAno(ano, semestre));
		setDataFim(Uteis.getDataFimSemestreAno(ano, semestre));
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

	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodosSituacaoFinanceira() {
		if (getMarcarTodasSituacoesFinanceiraAluno()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}
	
	public void realizarSelecaoCheckboxMarcarDesmarcarTodosSituacaoFinanceiraAluno() {
		if (getMarcarTodasSituacoesFinanceiraAluno()) {
			realizarSelecionarTodosSituacoesFinanceira(true);
		} else {
			realizarSelecionarTodosSituacoesFinanceira(false);
		}
	}
	
	public void realizarSelecionarTodosSituacoesFinanceira(boolean selecionado){
		getFiltroRelatorioFinanceiroVO().setSituacaoReceber(selecionado);
		getFiltroRelatorioFinanceiroVO().setSituacaoRecebido(selecionado);
		getFiltroRelatorioFinanceiroVO().setSituacaoCancelado(selecionado);
		getFiltroRelatorioFinanceiroVO().setSituacaoRenegociado(selecionado);
	}

	public Boolean getMarcarTodasSituacoesFinanceiraAluno() {
		if (marcarTodasSituacoesFinanceiraAluno == null) {
			marcarTodasSituacoesFinanceiraAluno = true;
		}
		return marcarTodasSituacoesFinanceiraAluno;
	}

	public void setMarcarTodasSituacoesFinanceiraAluno(Boolean marcarTodasSituacoesFinanceiraAluno) {
		this.marcarTodasSituacoesFinanceiraAluno = marcarTodasSituacoesFinanceiraAluno;
	}

	public CentroReceitaVO getCentroReceitaVO() {
		if (centroReceitaVO == null) {
			centroReceitaVO = new CentroReceitaVO();
		}
		return centroReceitaVO;
	}

	public void setCentroReceitaVO(CentroReceitaVO centroReceitaVO) {
		this.centroReceitaVO = centroReceitaVO;
	}
	
	public void consultarCentroReceita() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCentroReceita().equals("descricao")) {
                objs = getFacadeFactory().getCentroReceitaFacade().consultarPorDescricao(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaCentroReceita().equals("identificadorCentroReceita")) {
                objs = getFacadeFactory().getCentroReceitaFacade().consultarPorIdentificadorCentroReceita(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaCentroReceita().equals("nomeDepartamento")) {
                objs = getFacadeFactory().getCentroReceitaFacade().consultarPorNomeDepartamento(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsultaCentroReceitaVOs(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCentroReceitaVOs(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
	
    public void selecionarCentroReceita() {
        CentroReceitaVO obj = (CentroReceitaVO) context().getExternalContext().getRequestMap().get("centroReceitaItens");
        setCentroReceitaVO(obj);
    }

    public List getTipoConsultaComboCentroReceita() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("identificadorCentroReceita", "Identificador Centro Receita"));
        itens.add(new SelectItem("nomeDepartamento", "Departamento"));
        return itens;
    }

	
	public void limparDadosCentroReceita() {
		setCentroReceitaVO(null);
	}

	public List<CentroReceitaVO> getListaConsultaCentroReceitaVOs() {
		if (listaConsultaCentroReceitaVOs == null) {
			listaConsultaCentroReceitaVOs = new ArrayList<CentroReceitaVO>(0);
		}
		return listaConsultaCentroReceitaVOs;
	}

	public void setListaConsultaCentroReceitaVOs(List<CentroReceitaVO> listaConsultaCentroReceitaVOs) {
		this.listaConsultaCentroReceitaVOs = listaConsultaCentroReceitaVOs;
	}

	public String getValorConsultaCentroReceita() {
		if (valorConsultaCentroReceita == null) {
			valorConsultaCentroReceita = "";
		}
		return valorConsultaCentroReceita;
	}

	public void setValorConsultaCentroReceita(String valorConsultaCentroReceita) {
		this.valorConsultaCentroReceita = valorConsultaCentroReceita;
	}

	public String getCampoConsultaCentroReceita() {
		if (campoConsultaCentroReceita == null) {
			campoConsultaCentroReceita = "";
		}
		return campoConsultaCentroReceita;
	}

	public void setCampoConsultaCentroReceita(String campoConsultaCentroReceita) {
		this.campoConsultaCentroReceita = campoConsultaCentroReceita;
	}
	
	public Boolean getMarcarTodosTipoOrigem() {
		if (marcarTodosTipoOrigem == null) {
			marcarTodosTipoOrigem = Boolean.TRUE;
		}
		return marcarTodosTipoOrigem;
	}

	public void setMarcarTodosTipoOrigem(Boolean marcarTodosTipoOrigem) {
		this.marcarTodosTipoOrigem = marcarTodosTipoOrigem;
	}
	
	public void realizarSelecaoCheckboxMarcarDesmarcarTodosTipoOrigem() {
		if (getMarcarTodosTipoOrigem()) {
			realizarSelecaoTodasOrigens(true);
		} else {
			realizarSelecaoTodasOrigens(false);
		}
	}
	
	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodosTipoOrigem() {
		if (getMarcarTodosTipoOrigem()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}
	
	public void realizarSelecaoTodasOrigens(boolean selecionado){
		getFiltroRelatorioFinanceiroVO().setTipoOrigemBiblioteca(selecionado);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemBolsaCusteadaConvenio(selecionado);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemContratoReceita(selecionado);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemDevolucaoCheque(selecionado);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemInclusaoReposicao(selecionado);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemInscricaoProcessoSeletivo(selecionado);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemMaterialDidatico(selecionado);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemMatricula(selecionado);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemMensalidade(selecionado);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemNegociacao(selecionado);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemOutros(selecionado);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemRequerimento(selecionado);
		
	}
	
	@PostConstruct
	public void realizarCarregamentoSituacaoFinanceiraAlunoVindoTelaMapaNegativacaoCobrancaContraReceber() {
		String matricula = (String)context().getExternalContext().getSessionMap().get("matriculaAlunoNegativacaoCobrancaContaReceber");
		if (matricula != null && !matricula.equals("") ) {
			try {				
				getMatriculaVO().setMatricula(matricula);
				consultarAlunoPorMatricula();
				setUnidadeEnsinoVO(getMatriculaVO().getUnidadeEnsino());	
				
				for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
					if (unidade.getCodigo().equals(getUnidadeEnsinoVO().getCodigo())) {
						unidade.setFiltrarUnidadeEnsino(Boolean.TRUE);
					} else {
						unidade.setFiltrarUnidadeEnsino(Boolean.FALSE);
					}
				}
				
				setDataInicio((Date)context().getExternalContext().getSessionMap().get("dataInicio"));
				setDataFim((Date)context().getExternalContext().getSessionMap().get("dataFim"));
			}  catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("matriculaAlunoNegativacaoCobrancaContaReceber");
			}
			
		}
	}
}
