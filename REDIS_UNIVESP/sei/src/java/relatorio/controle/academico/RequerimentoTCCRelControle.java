package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.protocolo.RequerimentoHistoricoVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.protocolo.SituacaoRequerimentoDepartamentoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.RequerimentoRel;

/**
 * 
 * @author Alberto
 */
@Controller("RequerimentoTCCRelControle")
@Scope("viewScope")
public class RequerimentoTCCRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = -1145125765869657140L;
	private Date DataInicio;
	private Date DataFim;
	private String situacao;
	private List<TipoRequerimentoVO> listaConsultaTipoRequerimento;
	private List<SelectItem> listaSelectItemDepartamentoResponsavel;
	private Integer departamento;
	private Boolean finalizadoDeferido;
	private Boolean finalizadoIndeferido;
	private Boolean emExecucao;
	private Boolean pendente;
	private Boolean aguardandoPagamento;
	private Boolean canceladoFinanceiramente;
	private Boolean solicitacaoIsencao;
	private Boolean solicitacaoIsencaoDeferido;
	private Boolean solicitacaoIsencaoIndeferido;
	private Boolean aguardandoAutorizacaoPagamento;
	private Boolean isento;
	private Boolean pago;
	private Boolean prontoRetirada;
	private Boolean atrasado;
	private DisciplinaVO disciplinaVO;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List<DisciplinaVO> listaConsultaDisciplina;
	private List<SelectItem> tipoConsultaComboDisciplina;
	private FuncionarioVO funcionarioVO;
	private String campoConsultaFuncionario;
	private String valorConsultaFuncionario;
	private List<FuncionarioVO> listaConsultaFuncionario;
	private List<SelectItem> tipoConsultaComboFuncionario;
	private Boolean marcarTodasSituacoesRequerimento;
	private String unidadeEnsinoApresentar;
	private String cursoApresentar;
	
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	
	private TurmaVO turma;
	
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> cursoVOs;
	
	private CursoVO curso;
	
	private String campoConsultaRequerente;
	private String valorConsultaRequerente;
	private List<PessoaVO> listaConsultaRequerente;
	private PessoaVO pessoa;
	private List<SelectItem> listaSelectItemSituacaoRequerimentoDepartamentoCons;
	private Integer situacaoRequerimentoDepartamento;
	private Integer codigoTipoRequerimento;
	
	private String campoConsultaCoordenador;
	private String valorConsultaCoordenador;
	private List<PessoaVO> listaConsultaCoordenador;
	private PessoaVO coordenador;
	private Date DataInicioConclusao;
	private Date DataFimConclusao;
	private Date DataInicioAtendimento;
	private Date DataFimAtendimento;
	private String filtrarPeriodoPor;
	private TurmaVO turmaReposicao;
	private String campoConsultaTurmaReposicao;
	private String valorConsultaTurmaReposicao;
	private List<TurmaVO> listaConsultaTurmaReposicao;
	private MatriculaVO matricula;
    private String valorConsultaMatricula;
    private String campoConsultaMatricula;
    private List<MatriculaVO> listaConsultaMatricula;
    private List<SelectItem> listaSelectItemTipoRequerimentoConsulta;

	public RequerimentoTCCRelControle() throws Exception {
		verificarTodasUnidadesSelecionadas();
		montarListaSelectItemTipoRequerimentoConsulta();
	}
	
	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome().trim()).append("; ");
				}
			}
			setUnidadeEnsinoApresentar(unidade.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					setUnidadeEnsinoApresentar(getUnidadeEnsinoVOs().get(0).getNome());
				}
			}
		}
		consultarCursoFiltroRelatorio("");
		consultarTurnoFiltroRelatorio();
	}

	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			unidade.setFiltrarUnidadeEnsino(getMarcarTodasUnidadeEnsino());
		}
		verificarTodasUnidadesSelecionadas();
	}

	public List<SelectItem> getTipoConsultaComboTipoRequerimento() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Codigo"));
		itens.add(new SelectItem("prazoExecucao", "Prazo Execução"));
		return itens;
	}

	public List<SelectItem> getTipoSituacaoRequerimento() {
		List<SelectItem> objs = new ArrayList<SelectItem>();
		objs.add(new SelectItem("", ""));
		objs.add(new SelectItem("FD", "Finalizado - Deferido"));
		objs.add(new SelectItem("FI", "Finalizado - Indeferido"));
		objs.add(new SelectItem("EX", "Em Execução"));
		objs.add(new SelectItem("PE", "Pendente"));
		objs.add(new SelectItem("AP", "Aguardando Pagamento"));
		objs.add(new SelectItem("IS", "Isento"));
		objs.add(new SelectItem("PR", "Pronto para Retirada"));
		return objs;
	}
	
    public List<SelectItem> getTipoConsultaComboMatricula() {
    	List<SelectItem> itens = new ArrayList<SelectItem>();
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        return itens;
    }
	
	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
	}
	
	
	
	public void consultarAlunoPorMatricula() throws Exception {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatricula().getMatricula(), 0, NivelMontarDados.TODOS, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatricula().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatricula(objAluno);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatricula(new MatriculaVO());
		}
	}
	
	
	 public void consultarAluno() {
        try {
            List<MatriculaVO> objs = new ArrayList<MatriculaVO>();
            if (getValorConsultaMatricula().equals("")) {
                throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
            }
            if (getCampoConsultaMatricula().equals("matricula")) {
                MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimariaSituacaoDadosCompletos(getValorConsultaMatricula(), 0, false, "", getUsuarioLogado());
                if (!obj.getMatricula().equals("")) {
                    objs.add(obj);
                }
            }
            if (getCampoConsultaMatricula().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaMatricula(), 0, false, "","", getUsuarioLogado());
            }
            setListaConsultaMatricula(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaMatricula(new ArrayList<MatriculaVO>());
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
	 
	   public void limparDadosMatricula() throws Exception {
	    	setMensagemID("msg_entre_dados");
	    	setMatricula(null);
	    }

	public void limparTurma() {
		try {
			setTurma(new TurmaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Date getDataInicio() {
		if (DataInicio == null) {
			DataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
		}
		return DataInicio;
	}

	public void setDataInicio(Date DataInicio) {
		this.DataInicio = DataInicio;
	}

	public Date getDataFim() {
		if (DataFim == null) {
			DataFim = Uteis.getDataUltimoDiaMes(new Date());
		}
		return DataFim;
	}

	public void setDataFim(Date DataFim) {
		this.DataFim = DataFim;
	}

	public List<TipoRequerimentoVO> getListaConsultaTipoRequerimento() {
		if (listaConsultaTipoRequerimento == null) {
			listaConsultaTipoRequerimento = new ArrayList<TipoRequerimentoVO>(0);
		}
		return listaConsultaTipoRequerimento;
	}

	public void setListaConsultaTipoRequerimento(List<TipoRequerimentoVO> listaConsultaTipoRequerimento) {
		this.listaConsultaTipoRequerimento = listaConsultaTipoRequerimento;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public void inicializarListaSelectItemDepartamento() {
		try {
			getListaSelectItemDepartamentoResponsavel().clear();
			List<DepartamentoVO> departamentoVOs = getFacadeFactory().getDepartamentoFacade().consultarDepartamentoRequerimento(getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getListaSelectItemDepartamentoResponsavel().add(new SelectItem(0, ""));
			for (DepartamentoVO departamentoVO : departamentoVOs) {
				getListaSelectItemDepartamentoResponsavel().add(new SelectItem(departamentoVO.getCodigo(), departamentoVO.getNome()));
			}
		} catch (Exception e) {

		}
	}

	public List<SelectItem> getListaSelectItemDepartamentoResponsavel() {
		if (listaSelectItemDepartamentoResponsavel == null) {
			listaSelectItemDepartamentoResponsavel = new ArrayList<SelectItem>(0);
			inicializarListaSelectItemDepartamento();
		}
		return listaSelectItemDepartamentoResponsavel;
	}

	public void setListaSelectItemDepartamentoResponsavel(List<SelectItem> listaSelectItemDepartamentoResponsavel) {
		this.listaSelectItemDepartamentoResponsavel = listaSelectItemDepartamentoResponsavel;
	}

	public Integer getDepartamento() {
		if (departamento == null) {
			departamento = 0;
		}
		return departamento;
	}

	public void setDepartamento(Integer departamento) {
		this.departamento = departamento;
	}

	public Boolean getFinalizadoDeferido() {
		if (finalizadoDeferido == null) {
			finalizadoDeferido = Boolean.FALSE;
		}
		return finalizadoDeferido;
	}

	public void setFinalizadoDeferido(Boolean finalizadoDeferido) {
		this.finalizadoDeferido = finalizadoDeferido;
	}

	public Boolean getFinalizadoIndeferido() {
		if (finalizadoIndeferido == null) {
			finalizadoIndeferido = Boolean.FALSE;
		}
		return finalizadoIndeferido;
	}

	public void setFinalizadoIndeferido(Boolean finalizadoIndeferido) {
		this.finalizadoIndeferido = finalizadoIndeferido;
	}

	public Boolean getEmExecucao() {
		if (emExecucao == null) {
			emExecucao = Boolean.FALSE;
		}
		return emExecucao;
	}

	public void setEmExecucao(Boolean emExecucao) {
		this.emExecucao = emExecucao;
	}

	public Boolean getPendente() {
		if (pendente == null) {
			pendente = Boolean.FALSE;
		}
		return pendente;
	}

	public void setPendente(Boolean pendente) {
		this.pendente = pendente;
	}

	public Boolean getAguardandoPagamento() {
		if (aguardandoPagamento == null) {
			aguardandoPagamento = Boolean.FALSE;
		}
		return aguardandoPagamento;
	}

	public void setAguardandoPagamento(Boolean aguardandoPagamento) {
		this.aguardandoPagamento = aguardandoPagamento;
	}

	public Boolean getIsento() {
		if (isento == null) {
			isento = Boolean.FALSE;
		}
		return isento;
	}

	public void setIsento(Boolean isento) {
		this.isento = isento;
	}

	public Boolean getProntoRetirada() {
		if (prontoRetirada == null) {
			prontoRetirada = Boolean.FALSE;
		}
		return prontoRetirada;
	}

	public void setProntoRetirada(Boolean prontoRetirada) {
		this.prontoRetirada = prontoRetirada;
	}

	public Boolean getAtrasado() {
		if (atrasado == null) {
			atrasado = Boolean.FALSE;
		}
		return atrasado;
	}

	public void setAtrasado(Boolean atrasado) {
		this.atrasado = atrasado;
	}

	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			if (Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado().getCodigo())) {
				setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuario(getUsuarioLogado()));
			}else {
				setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(0, false, getUsuarioLogado()));
			}
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				obj.setFiltrarUnidadeEnsino(true);
			}
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public void consultarDisciplina() {
		try {
			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (getValorConsultaDisciplina().equals("")) {
					setValorConsultaDisciplina("0");
				}
				if (getValorConsultaDisciplina().trim() != null || !getValorConsultaDisciplina().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getValorConsultaDisciplina().trim());
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(valorInt, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDisciplina() throws Exception {
		setDisciplinaVO((DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItem"));
		setValorConsultaDisciplina(null);
		setListaConsultaDisciplina(null);
	}

	public void selecionarAluno() {
		try {
			setMatricula((MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens"));			
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboDisciplina;
	}
	
	public List<SelectItem> getTipoConsultaComboRequerente() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
	}
	
	public void consultarRequerente() {
		try {
			super.consultar();
			List<PessoaVO> objs = new ArrayList<PessoaVO>(0);
			if (getCampoConsultaRequerente().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaRequerente(), null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaRequerente().equals("cpf")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaRequerente(), null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaRequerente(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarRequerente() {
		setPessoa((PessoaVO) context().getExternalContext().getRequestMap().get("RequerenteItens"));
	}
	
	public Boolean getApresentarCampoCpf() {
		return getCampoConsultaRequerente().equals("cpf");
	}

	public void limparDisciplina() throws Exception {
		try {
			setDisciplinaVO(null);
			setValorConsultaDisciplina(null);
			setListaConsultaDisciplina(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getCampoConsultaDisciplina() {
		if (campoConsultaDisciplina == null) {
			campoConsultaDisciplina = "";
		}
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

	public String getValorConsultaDisciplina() {
		if (valorConsultaDisciplina == null) {
			valorConsultaDisciplina = "";
		}
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}

	public List<DisciplinaVO> getListaConsultaDisciplina() {
		if (listaConsultaDisciplina == null) {
			listaConsultaDisciplina = new ArrayList<DisciplinaVO>(0);
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
	}
	
	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodasSituacoesRequerimento() {
		if (getMarcarTodasSituacoesRequerimento()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}

	public Boolean getMarcarTodasSituacoesRequerimento() {
		if (marcarTodasSituacoesRequerimento == null) {
			marcarTodasSituacoesRequerimento = false;
		}
		return marcarTodasSituacoesRequerimento;
	}

	public void setMarcarTodasSituacoesRequerimento(Boolean marcarTodasSituacoesRequerimento) {
		this.marcarTodasSituacoesRequerimento = marcarTodasSituacoesRequerimento;
	}
	
	public void realizarSelecaoCheckboxMarcarDesmarcarTodasSituacoesRequerimento() {
		realizarSelecionarTodosSituacoesRequerimento(getMarcarTodasSituacoesRequerimento());
	}
	
	public void realizarSelecionarTodosSituacoesRequerimento(boolean selecionado){
		if(getFiltrarPeriodoPor().equals("dtConclusao")) {
			setFinalizadoDeferido(selecionado);
			setFinalizadoIndeferido(selecionado);
		}
		else if(getFiltrarPeriodoPor().equals("dtPrevisaoConclusao")) {
			setEmExecucao(selecionado);
			setPendente(selecionado);
			setProntoRetirada(selecionado);
			setAtrasado(selecionado);	
		}
		else {
			setFinalizadoDeferido(selecionado);
			setFinalizadoIndeferido(selecionado);
			setEmExecucao(selecionado);
			setPendente(selecionado);
			setProntoRetirada(selecionado);
			setAtrasado(selecionado);	
		}

	}

	public FuncionarioVO getFuncionarioVO() {
		if (funcionarioVO == null) {
			funcionarioVO = new FuncionarioVO();
		}
		return funcionarioVO;
	}

	public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}

	public String getCampoConsultaFuncionario() {
		if (campoConsultaFuncionario == null) {
			campoConsultaFuncionario = "";
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null) {
			valorConsultaFuncionario = "";
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		if (tipoConsultaComboFuncionario == null) {
			tipoConsultaComboFuncionario = new ArrayList<SelectItem>(0);
			tipoConsultaComboFuncionario.add(new SelectItem("NOME", "Nome"));
			tipoConsultaComboFuncionario.add(new SelectItem("MATRICULA", "Matrícula"));
			tipoConsultaComboFuncionario.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboFuncionario.add(new SelectItem("CARGO", "Cargo"));
			tipoConsultaComboFuncionario.add(new SelectItem("UNIDADEENSINO", "Unidade de Ensino"));
		}
		return tipoConsultaComboFuncionario;
	}
	
	public void limparFuncionario() throws Exception {
		try {
			setFuncionarioVO(null);
			setValorConsultaFuncionario(null);
			setListaConsultaFuncionario(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarFuncionario() {
		try {
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaFuncionario().equals("NOME")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), getDepartamento(), "", getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("MATRICULA")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), getDepartamento(), getUnidadeEnsinoLogado().getCodigo(), null, null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null, null, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), getDepartamento(), "", getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CARGO")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), getDepartamento(), getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("UNIDADEENSINO")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "FU", getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarFuncionario() throws Exception {
		setFuncionarioVO((FuncionarioVO) context().getExternalContext().getRequestMap().get("responsavel"));
	}
	
	public void consultarTurma() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), 0, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void marcarTodosCursosAction() throws Exception {
		for (CursoVO cursoVO : getCursoVOs()) {
			cursoVO.setFiltrarCursoVO(getMarcarTodosCursos());
		}
		verificarTodosCursosSelecionados();
	}

	public void verificarTodosCursosSelecionados() {
		StringBuilder curso = new StringBuilder();
		if (getCursoVOs().size() > 1) {
			for (CursoVO obj : getCursoVOs()) {
				if (obj.getFiltrarCursoVO()) {
					curso.append(obj.getCodigo()).append(" - ");
					curso.append(obj.getNome()).append("; ");
				}
			}
			setCursoApresentar(curso.toString());
		} else {
			if (!getCursoVOs().isEmpty()) {
				if (getCursoVOs().get(0).getFiltrarCursoVO()) {
					setCursoApresentar(getCursoVOs().get(0).getNome());
				}
			}
		}
	}
	
	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurma(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparUnidadeEnsino() {
		try {
			setUnidadeEnsinoApresentar(null);
			setMarcarTodasUnidadeEnsino(false);
			marcarTodasUnidadesEnsinoAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String getUnidadeEnsinoApresentar() {
		if (unidadeEnsinoApresentar == null) {
			unidadeEnsinoApresentar = "";
		}
		return unidadeEnsinoApresentar;
	}
	
	public void consultarProfessor() {
		try {
			super.consultar();
			List<PessoaVO> objs = new ArrayList<PessoaVO>(0);
			if (getCampoConsultaRequerente().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaRequerente(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaRequerente().equals("cpf")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaRequerente(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaRequerente(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void setUnidadeEnsinoApresentar(String unidadeEnsinoApresentar) {
		this.unidadeEnsinoApresentar = unidadeEnsinoApresentar;
	}
	
	public Boolean getPago() {
		if (pago == null) {
			pago = false;
		}
		return pago;
	}

	public void setPago(Boolean pago) {
		this.pago = pago;
	}

	public Boolean getAguardandoAutorizacaoPagamento() {
		if (aguardandoAutorizacaoPagamento == null) {
			aguardandoAutorizacaoPagamento = false;
		}
		return aguardandoAutorizacaoPagamento;
	}

	public void setAguardandoAutorizacaoPagamento(Boolean aguardandoAutorizacaoPagamento) {
		this.aguardandoAutorizacaoPagamento = aguardandoAutorizacaoPagamento;
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
			listaConsultaTurma = new ArrayList<TurmaVO>();
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	public void setTurma(TurmaVO turma) {
		this.turma = turma;
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

	public CursoVO getCurso() {
		if (curso == null) {
			curso = new CursoVO();
		}
		return curso;
	}

	public void setCurso(CursoVO curso) {
		this.curso = curso;
	}

	public List<CursoVO> getCursoVOs() {
		if (cursoVOs == null) {
			cursoVOs = new ArrayList<CursoVO>(0);
		}
		return cursoVOs;
	}

	public void setCursoVOs(List<CursoVO> cursoVOs) {
		this.cursoVOs = cursoVOs;
	}
	
	public String getCursoApresentar() {
		if (cursoApresentar == null) {
			cursoApresentar = "";
		}
		return cursoApresentar;
	}

	public void setCursoApresentar(String cursoApresentar) {
		this.cursoApresentar = cursoApresentar;
	}

	public PessoaVO getPessoa() {
		if (pessoa == null) {
			pessoa = new PessoaVO();
		}
		return pessoa;
	}

	public void setPessoa(PessoaVO pessoa) {
		this.pessoa = pessoa;
	}

	public String getCampoConsultaRequerente() {
		if (campoConsultaRequerente == null) {
			campoConsultaRequerente = "";
		}
		return campoConsultaRequerente;
	}

	public void setCampoConsultaRequerente(String campoConsultaRequerente) {
		this.campoConsultaRequerente = campoConsultaRequerente;
	}

	public String getValorConsultaRequerente() {
		if (valorConsultaRequerente == null) {
			valorConsultaRequerente = "";
		}
		return valorConsultaRequerente;
	}

	public void setValorConsultaRequerente(String valorConsultaRequerente) {
		this.valorConsultaRequerente = valorConsultaRequerente;
	}

	public List<PessoaVO> getListaConsultaRequerente() {
		if (listaConsultaRequerente == null) {
			listaConsultaRequerente = new ArrayList<>();
		}
		return listaConsultaRequerente;
	}

	public void setListaConsultaRequerente(List<PessoaVO> listaConsultaRequerente) {
		this.listaConsultaRequerente = listaConsultaRequerente;
	}
	

	public List<SelectItem> getListaSelectItemSituacaoRequerimentoDepartamentoCons() {
		if (listaSelectItemSituacaoRequerimentoDepartamentoCons == null) {
			listaSelectItemSituacaoRequerimentoDepartamentoCons = new ArrayList<SelectItem>();
			try {
				List<SituacaoRequerimentoDepartamentoVO> situacaoRequerimentoDepartamentoVOs =  getFacadeFactory().getSituacaoRequerimentoDepartamentoFacade().consultarPorSituacao("", getUsuarioLogado());
				listaSelectItemSituacaoRequerimentoDepartamentoCons = UtilSelectItem.getListaSelectItem(situacaoRequerimentoDepartamentoVOs, "codigo", "situacao");
			} catch (Exception e) {				
				e.printStackTrace();
			}
			
		}
		return listaSelectItemSituacaoRequerimentoDepartamentoCons;
	}

	public void setListaSelectItemSituacaoRequerimentoDepartamentoCons(List<SelectItem> listaSelectItemSituacaoRequerimentoDepartamentoCons) {
		this.listaSelectItemSituacaoRequerimentoDepartamentoCons = listaSelectItemSituacaoRequerimentoDepartamentoCons;
	}

	public Integer getSituacaoRequerimentoDepartamento() {
		if (situacaoRequerimentoDepartamento == null) {
			situacaoRequerimentoDepartamento = 0;
		}
		return situacaoRequerimentoDepartamento;
	}

	public void setSituacaoRequerimentoDepartamento(Integer situacaoRequerimentoDepartamento) {
		this.situacaoRequerimentoDepartamento = situacaoRequerimentoDepartamento;
	}

	public Boolean getCanceladoFinanceiramente() {
		if (canceladoFinanceiramente == null) {
			canceladoFinanceiramente = false;
		}
		return canceladoFinanceiramente;
	}

	public void setCanceladoFinanceiramente(Boolean canceladoFinanceiramente) {
		this.canceladoFinanceiramente = canceladoFinanceiramente;
	}

	public Boolean getSolicitacaoIsencao() {
		if (solicitacaoIsencao == null) {
			solicitacaoIsencao = false;
		}
		return solicitacaoIsencao;
	}

	public void setSolicitacaoIsencao(Boolean solicitacaoIsencao) {
		this.solicitacaoIsencao = solicitacaoIsencao;
	}

	public Boolean getSolicitacaoIsencaoDeferido() {
		if (solicitacaoIsencaoDeferido == null) {
			solicitacaoIsencaoDeferido = false;
		}
		return solicitacaoIsencaoDeferido;
	}

	public void setSolicitacaoIsencaoDeferido(Boolean solicitacaoIsencaoDeferido) {
		this.solicitacaoIsencaoDeferido = solicitacaoIsencaoDeferido;
	}

	public Boolean getSolicitacaoIsencaoIndeferido() {
		if (solicitacaoIsencaoIndeferido == null) {
			solicitacaoIsencaoIndeferido = false;
		}
		return solicitacaoIsencaoIndeferido;
	}

	public void setSolicitacaoIsencaoIndeferido(Boolean solicitacaoIsencaoIndeferido) {
		this.solicitacaoIsencaoIndeferido = solicitacaoIsencaoIndeferido;
	}
	
	public void realizarImpressaoExcel() throws Exception {
		List<RequerimentoVO> listaRequerimentoRelVO = null;
		getLoginControle().getUnidadeEnsinoVOs();
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoRelControle", "Inicializando Geração de Relatório Requerimento", "Emitindo Relatório");
			getFacadeFactory().getRequerimentoRelFacade().validarDados(getDataInicio(), getDataFim(), getUnidadeEnsinoVOs());
			listaRequerimentoRelVO = getFacadeFactory().getRequerimentoRelFacade().criarObjetoRequerimentoTipoTCC(getUnidadeEnsinoVOs(), getFinalizadoDeferido(), getFinalizadoIndeferido(), getEmExecucao(), getPendente(), getAguardandoPagamento(), getAguardandoAutorizacaoPagamento(), getIsento(), getPago(), getCanceladoFinanceiramente(), getSolicitacaoIsencao(), getSolicitacaoIsencaoDeferido(), getSolicitacaoIsencaoIndeferido(), getProntoRetirada(), getAtrasado(), getDataInicio(), getDataFim(), getFuncionarioVO().getCodigo(), getDepartamento(), getDisciplinaVO().getCodigo(), getMatricula().getMatricula(), getCurso(), getTurma(), getCursoVOs(), getPessoa(), getSituacaoRequerimentoDepartamento(), getCoordenador(), getFiltrarPeriodoPor(), getTurmaReposicao(),getCodigoTipoRequerimento(),getUsuarioLogado());
			if (!listaRequerimentoRelVO.isEmpty()) {
				List<RequerimentoHistoricoVO> requerimentoHistoricoVOs = new ArrayList<RequerimentoHistoricoVO>(0);
				for(RequerimentoVO requerimentoVO: listaRequerimentoRelVO) {
					for(RequerimentoHistoricoVO requerimentoHistoricoVO: requerimentoVO.getRequerimentoHistoricoVOs()) {
						requerimentoHistoricoVO.setRequerimentoVO(requerimentoVO);						
						requerimentoHistoricoVOs.add(requerimentoHistoricoVO);
					}
				}
				getSuperParametroRelVO().setNomeDesignIreport(RequerimentoRel.designIReportRelatorioTCCExcel());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
				getSuperParametroRelVO().setSubReport_Dir(RequerimentoRel.caminhoBaseRelatorio());
				getSuperParametroRelVO().setTituloRelatorio("Requerimentos de Trabalho de Conclusão de Curso");
				getSuperParametroRelVO().setListaObjetos(requerimentoHistoricoVOs);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(RequerimentoRel.caminhoBaseRelatorio());
				
				realizarImpressaoRelatorio();
				removerObjetoMemoria(this);
				consultarUnidadeEnsino();
				montarListaSelectItemTipoRequerimentoConsulta();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoRelControle", "Finalizando Geração de Relatório Requerimento", "Emitindo Relatório");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaRequerimentoRelVO);
		}
	}
	
	public void montarListaSelectItemTipoRequerimentoConsulta(){
		List<TipoRequerimentoVO> resultadoConsulta = null;
		Iterator<TipoRequerimentoVO> i = null;
		try {			
			resultadoConsulta = getFacadeFactory().getTipoRequerimentoFacade().consultarTipoRequerimentoTCCComboBox(false, "AT", getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());				
			i = resultadoConsulta.iterator();
			getListaSelectItemTipoRequerimentoConsulta().add(new SelectItem(0, ""));			
			while (i.hasNext()) {
				TipoRequerimentoVO obj = (TipoRequerimentoVO) i.next();
				getListaSelectItemTipoRequerimentoConsulta().add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}										
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.SUCESSO);
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public String getCampoConsultaCoordenador() {
		if (campoConsultaCoordenador == null) {
			campoConsultaCoordenador = "";
		}
		return campoConsultaCoordenador;
	}

	public void setCampoConsultaCoordenador(String campoConsultaCoordenador) {
		this.campoConsultaCoordenador = campoConsultaCoordenador;
	}

	public String getValorConsultaCoordenador() {
		if (valorConsultaCoordenador == null) {
			valorConsultaCoordenador = "";
		}
		return valorConsultaCoordenador;
	}

	public void setValorConsultaCoordenador(String valorConsultaCoordenador) {
		this.valorConsultaCoordenador = valorConsultaCoordenador;
	}

	public List<PessoaVO> getListaConsultaCoordenador() {
		if (listaConsultaCoordenador == null) {
			listaConsultaCoordenador = new ArrayList<>();
		}
		return listaConsultaCoordenador;
	}

	public void setListaConsultaCoordenador(List<PessoaVO> listaConsultaCoordenador) {
		this.listaConsultaCoordenador = listaConsultaCoordenador;
	}
	
	public PessoaVO getCoordenador() {
		if (coordenador == null) {
			coordenador = new PessoaVO();
		}
		return coordenador;
	}

	public void setCoordenador(PessoaVO coordenador) {
		this.coordenador = coordenador;
	}

	public void consultarCoordenador() {
		try {
			super.consultar();
			List<PessoaVO> objs = new ArrayList<PessoaVO>(0);
			if (getCampoConsultaCoordenador().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultaCoordenadorPorNome(getValorConsultaCoordenador(), getUnidadeEnsinoVOs(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaCoordenador(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarCoordenador() {
		setCoordenador((PessoaVO) context().getExternalContext().getRequestMap().get("coordenadorItens"));
	}
	
	public List<SelectItem> getTipoConsultaComboCoordenador() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}
	
	
	public void limparCoordenador() throws Exception {
		try {
			setCoordenador(null);;
			setValorConsultaCoordenador(null);
			setListaConsultaCoordenador(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Date getDataInicioConclusao() {
		return DataInicioConclusao;
	}

	public void setDataInicioConclusao(Date dataInicioConclusao) {
		DataInicioConclusao = dataInicioConclusao;
	}

	public Date getDataFimConclusao() {
		return DataFimConclusao;
	}

	public void setDataFimConclusao(Date dataFimConclusao) {
		DataFimConclusao = dataFimConclusao;
	}

	public Date getDataInicioAtendimento() {
		return DataInicioAtendimento;
	}

	public void setDataInicioAtendimento(Date dataInicioAtendimento) {
		DataInicioAtendimento = dataInicioAtendimento;
	}

	public Date getDataFimAtendimento() {
		return DataFimAtendimento;
	}

	public void setDataFimAtendimento(Date dataFimAtendimento) {
		DataFimAtendimento = dataFimAtendimento;
	}
	
	public String getFiltrarPeriodoPor() {
		if (filtrarPeriodoPor == null) {
			filtrarPeriodoPor = "filtrarPeriodoPor";
		}
		return filtrarPeriodoPor;
	}

	public void setFiltrarPeriodoPor(String filtrarPeriodoPor) {
		this.filtrarPeriodoPor = filtrarPeriodoPor;
	}
	
	public List<SelectItem> getFiltrarPeriodoPorCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("dtAbertura", "Data Abertura"));
		itens.add(new SelectItem("dtConclusao", "Data Conclusão"));
		itens.add(new SelectItem("dtAtendimento", "Data Atendimento"));
		itens.add(new SelectItem("dtPrevisaoConclusao", "Data Previsão Conclusão"));
		return itens;
	}
	
	public Boolean getFiltrarPeriodoPorDataConclusao() {
		if(getFiltrarPeriodoPor().equals("dtConclusao")) {
			setEmExecucao(false);
			setPendente(false);
			setProntoRetirada(false);
			setAtrasado(false);	
			return true;
		}
		return false;
	}
	
	public Boolean getFiltrarPeriodoPorDataPrevisaoConclusao() {
		if(getFiltrarPeriodoPor().equals("dtPrevisaoConclusao")) {
			setFinalizadoDeferido(false);
			setFinalizadoIndeferido(false);
			return true;
		}
		return false;
	}
	
	public void limparCurso() {
		try {
			setCursoApresentar(null);
			setMarcarTodosCursos(false);
			marcarTodosCursosAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparRequerente() throws Exception {
		try {
			setPessoa(null);
			setValorConsultaRequerente(null);
			setListaConsultaRequerente(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public TurmaVO getTurmaReposicao() {
		if(turmaReposicao == null) {
			turmaReposicao = new TurmaVO();
		}
		return turmaReposicao;
	}

	public void setTurmaReposicao(TurmaVO turmaReposicao) {
		this.turmaReposicao = turmaReposicao;
	}

	public String getCampoConsultaTurmaReposicao() {
		if(campoConsultaTurmaReposicao == null) {
			campoConsultaTurmaReposicao = "";
		}
		return campoConsultaTurmaReposicao;
	}

	public void setCampoConsultaTurmaReposicao(String campoConsultaTurmaReposicao) {
		this.campoConsultaTurmaReposicao = campoConsultaTurmaReposicao;
	}

	public String getValorConsultaTurmaReposicao() {
		if(valorConsultaTurmaReposicao == null) {
			valorConsultaTurmaReposicao = "";
		}
		return valorConsultaTurmaReposicao;
	}

	public void setValorConsultaTurmaReposicao(String valorConsultaTurmaReposicao) {
		this.valorConsultaTurmaReposicao = valorConsultaTurmaReposicao;
	}
	
	public List<TurmaVO> getListaConsultaTurmaReposicao() {
		if(listaConsultaTurmaReposicao == null) {
			listaConsultaTurmaReposicao = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurmaReposicao;
	}

	public void setListaConsultaTurmaReposicao(List<TurmaVO> listaConsultaTurmaReposicao) {
		this.listaConsultaTurmaReposicao = listaConsultaTurmaReposicao;
	}

	public void selecionarTurmaReposicao() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaReposicaoItens");
			setTurmaReposicao(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparTurmaReposicao() {
		try {
			setTurmaReposicao(new TurmaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarTurmaReposicao() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurmaReposicao().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurmaReposicao(), 0, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaTurmaReposicao(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurmaReposicao(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public MatriculaVO getMatricula() {
		if(matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}

	public String getValorConsultaMatricula() {
		if(valorConsultaMatricula == null) {
			valorConsultaMatricula ="";
		}
		return valorConsultaMatricula;
	}

	public void setValorConsultaMatricula(String valorConsultaMatricula) {
		this.valorConsultaMatricula = valorConsultaMatricula;
	}

	public String getCampoConsultaMatricula() {
		if(campoConsultaMatricula == null) {
			campoConsultaMatricula ="";
		}
		return campoConsultaMatricula;
	}

	public void setCampoConsultaMatricula(String campoConsultaMatricula) {
		this.campoConsultaMatricula = campoConsultaMatricula;
	}

	public List<MatriculaVO> getListaConsultaMatricula() {
		if(listaConsultaMatricula == null) {
			listaConsultaMatricula = new ArrayList<MatriculaVO>();
		}
		return listaConsultaMatricula;
	}

	public void setListaConsultaMatricula(List<MatriculaVO> listaConsultaMatricula) {
		this.listaConsultaMatricula = listaConsultaMatricula;
	}

	public List<SelectItem> getListaSelectItemTipoRequerimentoConsulta() {
		if(listaSelectItemTipoRequerimentoConsulta == null) {
			listaSelectItemTipoRequerimentoConsulta = new ArrayList<SelectItem>();
		}
		return listaSelectItemTipoRequerimentoConsulta;
	}

	public void setListaSelectItemTipoRequerimentoConsulta(List<SelectItem> listaSelectItemTipoRequerimentoConsulta) {
		this.listaSelectItemTipoRequerimentoConsulta = listaSelectItemTipoRequerimentoConsulta;
	}

	public Integer getCodigoTipoRequerimento() {
		if(codigoTipoRequerimento == null) {
			codigoTipoRequerimento = 0;
		}
		return codigoTipoRequerimento;
	}

	public void setCodigoTipoRequerimento(Integer codigoTipoRequerimento) {
		this.codigoTipoRequerimento = codigoTipoRequerimento;
	}
	
	
	
	
}
