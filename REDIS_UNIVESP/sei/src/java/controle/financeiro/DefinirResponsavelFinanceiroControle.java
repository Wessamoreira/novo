package controle.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;

@Controller("DefinirResponsavelFinanceiroControle")
@Scope("viewScope")
@Lazy
public class DefinirResponsavelFinanceiroControle extends SuperControle implements Serializable {

	private List<SelectItem> listaConsultaAluno;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private MatriculaVO matriculaVO;
	private List<ContaReceberVO> contaReceberVOs;
	private Boolean consDataCompetencia;
	private String situacaoContaReceber;
	private FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO;
	private List<PessoaVO> pessoaVOs;
	private PessoaVO pessoaVO;
	private Boolean apresentarModalResponsavelFinanceiro;
	private Boolean esconderModalPanelResponsavelFinanceiro;
	private Boolean consAnoSemestre;
	private String ano;
	private String semestre;
	private PessoaVO responsavelFinanceiroVindoTelaAlunoParaAlteracaoDeContas;
	private Date dataInicial;
	private Date dataFinal;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	
	@PostConstruct
	public void realizarCarregamentoDadosResponsavelFinanceiroVindoTelaAluno() {
		try { 
			MatriculaVO matricula =  (MatriculaVO) context().getExternalContext().getSessionMap().get("matriculaVOTelaDefinirResponsavelFinanceiro");
			if(matricula != null && Uteis.isAtributoPreenchido(matricula.getMatricula())){
				setMatriculaVO(matricula);
				setSituacaoContaReceber("TO");
				setContaReceberVOs(getFacadeFactory().getContaReceberFacade().consultarContaReceberAlterarResponsavelFinanceiro(getMatriculaVO().getMatricula(), getConsDataCompetencia(), Uteis.getDate("01/01/1980"), Uteis.getDate("01/01/2100"), getAno(), getSemestre(), getSituacaoContaReceber(), 0, 0, getFiltroRelatorioFinanceiroVO(), true, null, null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				for (ContaReceberVO object : getContaReceberVOs()) {
					if(!object.getNotaFiscalEmitida()){
						object.setSelecionado(true);
					}
				}
				
				setResponsavelFinanceiroVindoTelaAlunoParaAlteracaoDeContas((PessoaVO) context().getExternalContext().getSessionMap().get("responsavelFinanceiroInformadoTelaDefinirResponsavelFinanceiro"));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}finally{
			context().getExternalContext().getSessionMap().remove("matriculaVOTelaDefinirResponsavelFinanceiro");
			context().getExternalContext().getSessionMap().remove("responsavelFinanceiroInformadoTelaDefinirResponsavelFinanceiro");
		}
	}

	public void consultarContaReceber() {
		try {
			if (getMatriculaVO().getMatricula().trim().equals("")) {
				throw new ConsistirException("É obrigatório informar a matrícula");
			}
			if (getConsDataCompetencia()) {
				setDataInicial(getDataInicial() != null ? Uteis.getDataPrimeiroDiaMes(getDataInicial()) : null);
				setDataFinal(getDataFinal() != null ? Uteis.getDataUltimoDiaMes(getDataFinal()) : null);
			} else if (getConsAnoSemestre() && getAno().equals("") && getSemestre().equals("")) {
				setDataInicial(null);
				setDataFinal(null);
			}
			setContaReceberVOs(getFacadeFactory().getContaReceberFacade().consultarContaReceberAlterarResponsavelFinanceiro(getMatriculaVO().getMatricula(), getConsDataCompetencia(), getDataInicial(), getDataFinal(), getAno(), getSemestre(), getSituacaoContaReceber(), 0, 0, getFiltroRelatorioFinanceiroVO(), true, null, null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarDadosResponsavelFinanceiro() {
		try {
			for (ContaReceberVO conVo : getContaReceberVOs()) {
				if (conVo.getSelecionado()) {
					setApresentarModalResponsavelFinanceiro(Boolean.TRUE);
				}
			}
			if (!getApresentarModalResponsavelFinanceiro()) {
				throw new ConsistirException("É necessário selecionar no mínimo uma Conta para definir/alterar o Responsável Financeiro.");
			}
			limparMensagem();
			setPessoaVO(null);
			setPessoaVOs(getFacadeFactory().getPessoaFacade().consultarFiliacaoPorPessoa(getMatriculaVO().getAluno().getCodigo(), getUsuarioLogado()));
			if(Uteis.isAtributoPreenchido(getResponsavelFinanceiroVindoTelaAlunoParaAlteracaoDeContas().getCodigo())){
				for (PessoaVO pessoa : getPessoaVOs()) {
					if(pessoa.getCodigo().intValue() == responsavelFinanceiroVindoTelaAlunoParaAlteracaoDeContas.getCodigo().intValue()){
						pessoa.setSelecionado(true);
					}else{
						setPessoaVO(null);
						pessoa.setSelecionado(false);
						setPessoaVO(pessoa);
					}
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarResponsavelFinanceiro() {
		try {
			getFacadeFactory().getDefinirResponsavelFinanceiroFacade().executarDefinirAlterarResponsavelFinanceiro(getContaReceberVOs(), getPessoaVO(), getUsuarioLogado());
			setEsconderModalPanelResponsavelFinanceiro(Boolean.TRUE);
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setEsconderModalPanelResponsavelFinanceiro(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String getMinimizarModalPanelResponsavelFinanceiro() {
		if (getEsconderModalPanelResponsavelFinanceiro()) {
			return "RichFaces.$('panelResponsavelFinanceiro').hide()";
		}
		return "";
	}

	public void selecionarDesselecionarResponsavelFinanceiro() {
		try {
			PessoaVO pessoaVO = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItens");
			if (!pessoaVO.getSelecionado()) {
				setPessoaVO(null);
				getPessoaVO().setSelecionado(false);
			} else {
				setPessoaVO(pessoaVO);
				getPessoaVO().setSelecionado(true);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), 0, false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), 0, false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), 0, false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
		setMatriculaVO(obj);
		getUnidadeEnsinoVO().setCodigo(obj.getUnidadeEnsino().getCodigo());
		valorConsultaAluno = "";
		campoConsultaAluno = "";
		getListaConsultaAluno().clear();
	}

	public void limparDadosAluno() throws Exception {
		setMatriculaVO(null);
		setContaReceberVOs(null);
		setPessoaVO(null);
		setPessoaVOs(null);
	}
	
	public void limparDadosFiltroTodoPeriodo() {
		setAno("");
		setSemestre("");
		setConsDataCompetencia(Boolean.FALSE);
		setConsAnoSemestre(Boolean.FALSE);
	}
	
	public void limparDadosFiltroDataCompetencia() {
		setAno("");
		setSemestre("");
		setConsAnoSemestre(Boolean.FALSE);
		getControleConsulta().setBuscarPeriodoCompleto(Boolean.FALSE);
	}
	
	public void limparDadosFiltroAnoSemestre() {
		setAno("");
		setSemestre("");
		setConsDataCompetencia(Boolean.FALSE);
		getControleConsulta().setBuscarPeriodoCompleto(Boolean.FALSE);
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatriculaVO().getMatricula(), 0, NivelMontarDados.BASICO, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatriculaVO(objAluno);
			Integer matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultaCodigoUltimaMatriculaPeriodoPorMatricula(objAluno.getMatricula(), false, getUsuario());
			if (!Uteis.isAtributoPreenchido(matriculaPeriodo)) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			getUnidadeEnsinoVO().setCodigo(objAluno.getUnidadeEnsino().getCodigo());
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatriculaVO(new MatriculaVO());
		}
	}

	public void preencherTodosListaContaReceber() {
		try {
			for (ContaReceberVO contaReceberVO : getContaReceberVOs()) {
//				if (!contaReceberVO.getNotaFiscalEmitida()) {
					contaReceberVO.setSelecionado(true);
//				}
			}
			setApresentarModalResponsavelFinanceiro(Boolean.FALSE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void desmarcarTodosListaContaReceber() {
		try {
			for (ContaReceberVO contaReceberVO : getContaReceberVOs()) {
				contaReceberVO.setSelecionado(false);
			}
			setApresentarModalResponsavelFinanceiro(Boolean.FALSE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getExibirModalPanelResponsavelFinanceiro() {
		if (getApresentarModalResponsavelFinanceiro()) {
			return "RichFaces.$('panelResponsavelFinanceiro').show()";
		}
		return "";
	}

	public List<SelectItem> getComboSemestre() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("1", "1°"));
		itens.add(new SelectItem("2", "2°"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemSituacaoContaReceber() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		Hashtable listaSituacaoRecebimento = (Hashtable) Dominios.getReceberRecebidoNegociado();
		Enumeration keys = listaSituacaoRecebimento.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) listaSituacaoRecebimento.get(value);
			objs.add(new SelectItem(value, label));
		}
		objs.add(new SelectItem("TO", "Todos"));
		return objs;
	}

	public List<SelectItem> getListaSelectItemLayout() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("Layout1", "Layout 1"));
		objs.add(new SelectItem("Layout2", "Layout 2"));
		return objs;
	}
	
	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List<SelectItem> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<SelectItem>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<SelectItem> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
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

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
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

	public List<ContaReceberVO> getContaReceberVOs() {
		if (contaReceberVOs == null) {
			contaReceberVOs = new ArrayList<ContaReceberVO>(0);
		}
		return contaReceberVOs;
	}

	public void setContaReceberVOs(List<ContaReceberVO> contaReceberVOs) {
		this.contaReceberVOs = contaReceberVOs;
	}

	public Boolean getConsDataCompetencia() {
		if (consDataCompetencia == null) {
			consDataCompetencia = Boolean.FALSE;
		}
		return consDataCompetencia;
	}

	public void setConsDataCompetencia(Boolean consDataCompetencia) {
		this.consDataCompetencia = consDataCompetencia;
	}

	public String getSituacaoContaReceber() {
		if (situacaoContaReceber == null) {
			situacaoContaReceber = "";
		}
		return situacaoContaReceber;
	}

	public void setSituacaoContaReceber(String situacaoContaReceber) {
		this.situacaoContaReceber = situacaoContaReceber;
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

	public List<PessoaVO> getPessoaVOs() {
		if (pessoaVOs == null) {
			pessoaVOs = new ArrayList<PessoaVO>(0);
		}
		return pessoaVOs;
	}

	public void setPessoaVOs(List<PessoaVO> pessoaVOs) {
		this.pessoaVOs = pessoaVOs;
	}

	public PessoaVO getPessoaVO() {
		if (pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public Boolean getApresentarModalResponsavelFinanceiro() {
		if (apresentarModalResponsavelFinanceiro == null) {
			apresentarModalResponsavelFinanceiro = Boolean.FALSE;
		}
		return apresentarModalResponsavelFinanceiro;
	}

	public void setApresentarModalResponsavelFinanceiro(Boolean apresentarModalResponsavelFinanceiro) {
		this.apresentarModalResponsavelFinanceiro = apresentarModalResponsavelFinanceiro;
	}

	public Boolean getEsconderModalPanelResponsavelFinanceiro() {
		if (esconderModalPanelResponsavelFinanceiro == null) {
			esconderModalPanelResponsavelFinanceiro = Boolean.FALSE;
		}
		return esconderModalPanelResponsavelFinanceiro;
	}

	public void setEsconderModalPanelResponsavelFinanceiro(Boolean esconderModalPanelResponsavelFinanceiro) {
		this.esconderModalPanelResponsavelFinanceiro = esconderModalPanelResponsavelFinanceiro;
	}

	public Boolean getConsAnoSemestre() {
		if (consAnoSemestre == null) {
			consAnoSemestre = Boolean.FALSE;
		}
		return consAnoSemestre;
	}

	public void setConsAnoSemestre(Boolean consAnoSemestre) {
		this.consAnoSemestre = consAnoSemestre;
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

	public PessoaVO getResponsavelFinanceiroVindoTelaAlunoParaAlteracaoDeContas() {
		if(responsavelFinanceiroVindoTelaAlunoParaAlteracaoDeContas == null ){
			responsavelFinanceiroVindoTelaAlunoParaAlteracaoDeContas = new PessoaVO();
		}
		return responsavelFinanceiroVindoTelaAlunoParaAlteracaoDeContas;
	}

	public void setResponsavelFinanceiroVindoTelaAlunoParaAlteracaoDeContas(PessoaVO responsavelFinanceiroVindoTelaAlunoParaAlteracaoDeContas) {
		this.responsavelFinanceiroVindoTelaAlunoParaAlteracaoDeContas = responsavelFinanceiroVindoTelaAlunoParaAlteracaoDeContas;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
}
