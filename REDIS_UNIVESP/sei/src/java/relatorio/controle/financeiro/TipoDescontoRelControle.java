/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.PlanoFinanceiroAlunoVO;
import negocio.comuns.academico.TurmaAgrupadaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.FinanciamentoEstudantil;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.TipoDescontoRelVO;
import relatorio.negocio.jdbc.financeiro.TipoDescontoRel;

/**
 *
 * @author Philippe
 */
@Controller("TipoDescontoRelControle")
@Scope("viewScope")
@Lazy
public class TipoDescontoRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private TurmaVO turmaVO;
	private PessoaVO aluno;
	private PlanoDescontoVO planoDescontoVO;
	private DescontoProgressivoVO descontoProgressivoVO;
	private PlanoFinanceiroAlunoVO planoFinanceiroAlunoVO;
	private ConvenioVO convenioVO;
	private String financiamentoEstudantil;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemConvenio;
	private List<SelectItem> listaSelectItemDescontoProgresivo;
	private List<SelectItem> listaSelectItemPlanoDesconto;
	private List<SelectItem> listaSelectItemFinanciamentoEstudantil;
	private List<TurmaVO> listaConsultaTurma;
	private List<UnidadeEnsinoCursoVO> listaConsultaCurso;
	private List<MatriculaVO> listaConsultaAluno;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private String tipoDesconto;
	private String ano;
	private String semestre;
	private Boolean apresentarCampoAno;
	private Boolean apresentarCampoSemestre;

	public TipoDescontoRelControle() throws Exception {
		setMensagemID("msg_entre_prmconsulta");
	}

	@PostConstruct
	public void inicializarListaSelectItems(){
		try {
			montarListaSelectItemFinanciamentoEstudantil();
			montarListaSelectItemConvenio();
			montarListaSelectItemDescontoProgressivo();
			montarListaSelectItemPlanoDesconto();
		} catch (Exception e) {
		}
	}

	public void imprimirRelatorioExcel() throws Exception {
		List<TipoDescontoRelVO> listaObjetos = null;
		try {
			listaObjetos = getFacadeFactory().getTipoDescontoRelFacade().criarObjeto(getUnidadeEnsinoVOs(), getTipoDesconto(), getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getAluno().getCodigo(), getTipoDesconto().equals("descontoAluno"), getAno(), getSemestre(), getConvenioVO().getCodigo(), getPlanoDescontoVO().getCodigo(), getDescontoProgressivoVO().getCodigo(), getFinanciamentoEstudantil());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getTipoDescontoRelFacade().designIReportRelatorioExcel());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getTipoDescontoRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Tipos de Descontos");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getTipoDescontoRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().adicionarParametro("tipoDesconto", TipoDescontoRel.getDescricaoTipoDesconto(getTipoDesconto()));
				realizarImpressaoRelatorio();
				removerObjetoMemoria(this);
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
			setMarcarTodasUnidadeEnsino(true);
			marcarTodasUnidadesEnsinoAction();
			inicializarListaSelectItems();
		}
	}

	public void imprimirPDF() throws Exception {
		List<TipoDescontoRelVO> listaObjetos = null;
		try {
			listaObjetos = getFacadeFactory().getTipoDescontoRelFacade().criarObjeto(getUnidadeEnsinoVOs(), getTipoDesconto(), getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getAluno().getCodigo(), getTipoDesconto().equals("descontoAluno"), getAno(), getSemestre(), getConvenioVO().getCodigo(), getPlanoDescontoVO().getCodigo(), getDescontoProgressivoVO().getCodigo(), getFinanciamentoEstudantil());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getTipoDescontoRelFacade().designIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getTipoDescontoRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Tipos de Descontos");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getTipoDescontoRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().adicionarParametro("tipoDesconto", TipoDescontoRel.getDescricaoTipoDesconto(getTipoDesconto()));
				realizarImpressaoRelatorio();
				removerObjetoMemoria(this);
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
			setMarcarTodasUnidadeEnsino(true);
			marcarTodasUnidadesEnsinoAction();
			inicializarListaSelectItems();
		}
	}

	public void montarListaSelectItemConvenio() {
		try {
			montarListaSelectItemConvenio("");
		} catch (Exception e) {
		}
	}

	public void montarListaSelectItemConvenio(String prm) throws Exception {
		List<ConvenioVO> resultadoConsulta = consultarConvenioPorDescricao(prm);
		setListaSelectItemConvenio(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricao", true));
	}

	@SuppressWarnings("unchecked")
	public List<ConvenioVO> consultarConvenioPorDescricao(String descricaoPrm) throws Exception {
		return getFacadeFactory().getConvenioFacade().consultarPorDescricao(descricaoPrm, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	public void montarListaSelectItemDescontoProgressivo() {
		try {
			montarListaSelectItemDescontoProgressivo("");
		} catch (Exception e) {
		}
	}

	public void montarListaSelectItemDescontoProgressivo(String prm) throws Exception {
		List<DescontoProgressivoVO> resultadoConsulta = consultarDescontoProgressivoPorNome(prm);
		setListaSelectItemDescontoProgresivo(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", true));
	}

	@SuppressWarnings("unchecked")
	public List<DescontoProgressivoVO> consultarDescontoProgressivoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getDescontoProgressivoFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
	}

	@SuppressWarnings("rawtypes")
	public void consultarTurma() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsinoCurso(getValorConsultaTurma(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), 0, 0);
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurnoCurso(getValorConsultaTurma(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			setTurmaVO((TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens"));
			if (getUnidadeEnsinoCursoVO().getCodigo() == 0) {
				getUnidadeEnsinoCursoVO().setCurso(getTurmaVO().getCurso());
			}
			if (getUnidadeEnsinoVO().getCodigo() == 0) {
				setUnidadeEnsinoVO(getTurmaVO().getUnidadeEnsino());
			}
			getListaConsultaTurma().clear();
			if (getTurmaVO().getTurmaAgrupada()) {
				for (TurmaAgrupadaVO turmaAgrupada : getTurmaVO().getTurmaAgrupadaVOs()) {
					verificarApresentarAnoSemestre(turmaAgrupada.getTurma().getCurso().getPeriodicidade());
				}
			} else {
				verificarApresentarAnoSemestre(getTurmaVO().getCurso().getPeriodicidade());
			}
			this.setValorConsultaTurma("");
			this.setCampoConsultaTurma("");
			setMensagemID("", "");
		} catch (Exception e) {
		}
	}

	public void limparTurma() throws Exception {
		try {
			setTurmaVO(null);
		} catch (Exception e) {
		}
	}

	public void limparAluno() throws Exception {
		try {
			setAluno(null);
		} catch (Exception e) {
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List<SelectItem> getTipoComboTipoDesconto() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("todos", "Todos"));
		itens.add(new SelectItem("descontoInstituicao", "Desconto Instituição"));
		itens.add(new SelectItem("descontoProgressivo", "Desconto Progressivo"));
		itens.add(new SelectItem("descontoConvenio", "Desconto Convênio"));
		itens.add(new SelectItem("descontoAluno", "Desconto Aluno"));
		itens.add(new SelectItem("financiamentoEstudantil", "Financiamento Estudantil"));
		return itens;
	}

	public void montarListaSelectItemPlanoDesconto() {
		try {
			montarListaSelectItemPlanoDesconto("");
		} catch (Exception e) {
		}
	}

	public void montarListaSelectItemPlanoDesconto(String prm) throws Exception {
		List<PlanoDescontoVO> resultadoConsulta = consultarPlanoDescontoPorNome(prm);
		setListaSelectItemPlanoDesconto(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", true));
	}

	@SuppressWarnings("unchecked")
	public List<PlanoDescontoVO> consultarPlanoDescontoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getPlanoDescontoFacade().consultarPlanoDescontoNivelComboBox(getUsuarioLogadoClone());
	}

	public void consultarCurso() {
		try {
			List<UnidadeEnsinoCursoVO> objs = new ArrayList<UnidadeEnsinoCursoVO>(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<UnidadeEnsinoCursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() {
		try {
			setUnidadeEnsinoCursoVO((UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCursoItens"));
			if (getUnidadeEnsinoVO().getCodigo() == 0) {
				getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoCursoVO().getUnidadeEnsino());
			}
			setTurmaVO(null);
			valorConsultaCurso = "";
			campoConsultaCurso = "";
			getListaConsultaCurso().clear();
			verificarApresentarAnoSemestre(getUnidadeEnsinoCursoVO().getCurso().getPeriodicidade());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCurso() throws Exception {
		setUnidadeEnsinoCursoVO(null);
		limparTurma();
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getCampoConsultaAluno().equals("nome")) {
				if (getTurmaVO().getCodigo() != 0) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoaCursoTurma(getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), false, getUsuarioLogado());
				} else if (getUnidadeEnsinoCursoVO().getCurso().getCodigo() != 0) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), false, getUsuarioLogado());
				} else if (getUnidadeEnsinoVO().getCodigo() != 0) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
				} else {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), 0, false, getUsuarioLogado());
				}
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		setAluno(obj.getAluno());
		getUnidadeEnsinoCursoVO().setCurso(obj.getCurso());
		setUnidadeEnsinoVO(obj.getUnidadeEnsino());
		valorConsultaAluno = "";
		campoConsultaAluno = "";
		getListaConsultaAluno().clear();
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public void montarListaSelectItemFinanciamentoEstudantil() throws Exception {
		setListaSelectItemFinanciamentoEstudantil(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(FinanciamentoEstudantil.class, true));
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

	public List<UnidadeEnsinoCursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<UnidadeEnsinoCursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<UnidadeEnsinoCursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
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

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public Boolean getIsDescontoConvenio() {
		return getTipoDesconto().equals("descontoConvenio");
	}

	public Boolean getIsDescontoInstituicao() {
		return getTipoDesconto().equals("descontoInstituicao");
	}

	public Boolean getIsFinanciamentoEstudantil() {
		return getTipoDesconto().equals("financiamentoEstudantil");
	}

	public String getTipoDesconto() {
		if (tipoDesconto == null) {
			tipoDesconto = "Todos";
		}
		return tipoDesconto;
	}

	public void setTipoDesconto(String tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCursoVO == null) {
			unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoVO;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
	}

	public PessoaVO getAluno() {
		if (aluno == null) {
			aluno = new PessoaVO();
		}
		return aluno;
	}

	public void setAluno(PessoaVO aluno) {
		this.aluno = aluno;
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

	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
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

	public List<SelectItem> getListaSelectItemConvenio() {
		if (listaSelectItemConvenio == null) {
			listaSelectItemConvenio = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemConvenio;
	}

	public void setListaSelectItemConvenio(List<SelectItem> listaSelectItemConvenio) {
		this.listaSelectItemConvenio = listaSelectItemConvenio;
	}

	public List<SelectItem> getListaSelectItemDescontoProgresivo() {
		if (listaSelectItemDescontoProgresivo == null) {
			listaSelectItemDescontoProgresivo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDescontoProgresivo;
	}

	public void setListaSelectItemDescontoProgresivo(List<SelectItem> listaSelectItemDescontoProgresivo) {
		this.listaSelectItemDescontoProgresivo = listaSelectItemDescontoProgresivo;
	}

	public List<SelectItem> getListaSelectItemPlanoDesconto() {
		if (listaSelectItemPlanoDesconto == null) {
			listaSelectItemPlanoDesconto = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemPlanoDesconto;
	}

	public void setListaSelectItemPlanoDesconto(List<SelectItem> listaSelectItemPlanoDesconto) {
		this.listaSelectItemPlanoDesconto = listaSelectItemPlanoDesconto;
	}

	public PlanoDescontoVO getPlanoDescontoVO() {
		if (planoDescontoVO == null) {
			planoDescontoVO = new PlanoDescontoVO();
		}
		return planoDescontoVO;
	}

	public void setPlanoDescontoVO(PlanoDescontoVO planoDescontoVO) {
		this.planoDescontoVO = planoDescontoVO;
	}

	public DescontoProgressivoVO getDescontoProgressivoVO() {
		if (descontoProgressivoVO == null) {
			descontoProgressivoVO = new DescontoProgressivoVO();
		}
		return descontoProgressivoVO;
	}

	public void setDescontoProgressivoVO(DescontoProgressivoVO descontoProgressivoVO) {
		this.descontoProgressivoVO = descontoProgressivoVO;
	}

	public PlanoFinanceiroAlunoVO getPlanoFinanceiroAlunoVO() {
		if (planoFinanceiroAlunoVO == null) {
			planoFinanceiroAlunoVO = new PlanoFinanceiroAlunoVO();
		}
		return planoFinanceiroAlunoVO;
	}

	public void setPlanoFinanceiroAlunoVO(PlanoFinanceiroAlunoVO planoFinanceiroAlunoVO) {
		this.planoFinanceiroAlunoVO = planoFinanceiroAlunoVO;
	}

	public ConvenioVO getConvenioVO() {
		if (convenioVO == null) {
			convenioVO = new ConvenioVO();
		}
		return convenioVO;
	}

	public void setConvenioVO(ConvenioVO convenioVO) {
		this.convenioVO = convenioVO;
	}

	public Boolean getIsDescontoProgressivo() {
		return getTipoDesconto().equals("descontoProgressivo");
	}

	public List<SelectItem> getListaSelectItemFinanciamentoEstudantil() {
		if (listaSelectItemFinanciamentoEstudantil == null) {
			listaSelectItemFinanciamentoEstudantil = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemFinanciamentoEstudantil;
	}

	public void setListaSelectItemFinanciamentoEstudantil(List<SelectItem> listaSelectItemFinanciamentoEstudantil) {
		this.listaSelectItemFinanciamentoEstudantil = listaSelectItemFinanciamentoEstudantil;
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

	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("ContaReceberRel");
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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

	public Boolean getApresentarCampoAno() {
		if (apresentarCampoAno == null) {
			apresentarCampoAno = true;
		}
		return apresentarCampoAno;
	}

	public void setApresentarCampoAno(Boolean apresentarCampoAno) {
		this.apresentarCampoAno = apresentarCampoAno;
	}

	public Boolean getApresentarCampoSemestre() {
		if (apresentarCampoSemestre == null) {
			apresentarCampoSemestre = true;
		}
		return apresentarCampoSemestre;
	}

	public void setApresentarCampoSemestre(Boolean apresentarCampoSemestre) {
		this.apresentarCampoSemestre = apresentarCampoSemestre;
	}

	private void verificarApresentarAnoSemestre(String periodicidade) {
		if (periodicidade.equals("AN") || periodicidade.equals("SE")) {
			setApresentarCampoAno(true);
		} else {
			setApresentarCampoAno(false);
		}
		if (periodicidade.equals("SE")) {
			setApresentarCampoSemestre(true);
		} else {
			setApresentarCampoSemestre(false);
		}
	}

	public List<SelectItem> getTipoConsultaComboSemestre() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("1", "1º"));
		itens.add(new SelectItem("2", "2º"));
		return itens;
	}

	public String getFinanciamentoEstudantil() {
		if (financiamentoEstudantil == null) {
			financiamentoEstudantil = "";
		}
		return financiamentoEstudantil;
	}

	public void setFinanciamentoEstudantil(String financiamentoEstudantil) {
		this.financiamentoEstudantil = financiamentoEstudantil;
	}

}
