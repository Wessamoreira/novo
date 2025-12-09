package controle.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.MatriculaPeriodoVencimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;

@Controller("AtualizacaoVencimentoControle")
@Scope("viewScope")
@Lazy
public class AtualizacaoVencimentoControle extends SuperControle implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7694870179889476977L;
	private String tipoAtualizacao;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private List listaConsultaAluno;
    private String valorConsultaTurma;
    private String campoConsultaTurma;
    private List listaConsultaTurma;
    private MatriculaVO matriculaVO;
    private TurmaVO turmaVO;
    private Date novaDataVencimentoContasAluno;
    private Boolean mostrarPanelContasAluno;
    private Date novaDataVencimentoContasTurma;
    private Boolean mostrarPanelContasTurma;
    private List<ContaReceberVO> listaContasReceberAluno;
    private List<ContaReceberVO> listaContasReceberAlunosTurma;
    private List<MatriculaVO> listaAlunosTurmaComContasReceber;
    private List<MatriculaPeriodoVencimentoVO> listaMatriculaPeriodoVencimentoAluno;
    private Integer novoDiaVencimento;
    private Boolean atualizarIndividualmente;
    private Boolean mostrarCampoNovaData;
    private Boolean mostrarDataTableContasIndividuais;
    private ContaReceberVO contaReceberVO;
    private ConfiguracaoFinanceiroVO configuracaoFinanceiroVO;
    private String abrirModalCobrarReimpressao;
    private Boolean atualizarContaPorParcela;
    private List<SelectItem> listaSelectItemParcela;
    private String parcela;
    private String ano;
    private String semestre;
    private String tipoOrigem;
    private Boolean consultarVencida;
	private List<SelectItem> listaSelectItemTipoAtualizacao;
	private Date dataVencimentoInicio;
	private Date dataVencimentoFinal;
	private DiaSemana diaSemana;
	private List<ContaReceberVO> listaContasReceberVOs;
	private List<MatriculaVO> listaMatriculaVOs;
	private MatriculaVO matriculaVOTemp;
	private Date novaDataVencimentoContaReceberPeriodo;
	private ProgressBarVO progressBarVO;
	private Integer quantidadeTotalContaReceberPeriodo;
	private Boolean permitirAlterarDataCompetenciaMesmoMesAnoVencimento;
	private Boolean atualizarDataCompetenciaDeAcordoComDataVencimento;
	private FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO;
	private Boolean marcarTodosTipoOrigem;
    private Boolean permissaoSomenteMensalidade = false;
    private Boolean permissaoSomenteMatricula = false;
	private Date dataVencimentoInicioTurma;
	private Date dataVencimentoFinalTurma;
	
	
	public AtualizacaoVencimentoControle() {
		verificarUsuarioPermissaoAlterarDataCompetenciaMesmoMesAnoVencimento();
		inicializarDadosFiltrRelatorioFinanceiroVO();
		setTipoAtualizacao("");
		verificaPermissaoConsultaContaReceberVencida();
	}
	
    public void consultarAluno() {
        try {
            List objs = new ArrayList(0);

            if (getValorConsultaAluno().equals("")) {
                throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
                if (!obj.getMatricula().equals("")) {
                    objs.add(obj);
                }
            }
            if (getCampoConsultaAluno().equals("nomeAluno")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            getListaConsultaAluno().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarAlunoPorMatricula() {
        try {
        	getMatriculaVO().getUnidadeEnsino().setCodigo(this.getUnidadeEnsinoLogado().getCodigo());
        	getMatriculaVO().getUnidadeEnsino().setNome(this.getUnidadeEnsinoLogado().getNome());
            getFacadeFactory().getMatriculaFacade().carregarDados(getMatriculaVO(), getUsuarioLogado());
            if (getMatriculaVO().getMatricula().equals("")) {
                throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            setMostrarPanelContasAluno(Boolean.TRUE);
            setMostrarCampoNovaData(Boolean.TRUE);
            //consultarContasReceberAluno();
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setListaContasReceberAluno(null);
            this.setMatriculaVO(new MatriculaVO());
        }
    }

    public void limparDadosAluno() throws Exception {
        setMatriculaVO(null);
        getListaContasReceberAluno().clear();
        getListaMatriculaPeriodoVencimentoAluno().clear();
        setMostrarPanelContasAluno(Boolean.FALSE);
        setMostrarCampoNovaData(Boolean.FALSE);
        setMostrarDataTableContasIndividuais(Boolean.FALSE);
        setMensagemID("msg_entre_prmconsulta");
    }


	public void selecionarAluno() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
			MatriculaVO objCompleto = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			setMatriculaVO(objCompleto);
			obj = null;
			objCompleto = null;
			setValorConsultaAluno("");
			setCampoConsultaAluno("");
			getListaConsultaAluno().clear();
			setMostrarPanelContasAluno(Boolean.TRUE);
			setMostrarCampoNovaData(Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
    
    public List<SelectItem> getTipoConsultaComboAluno() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nomeAluno", "Nome Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public List<SelectItem> getTipoConsultaComboSemestre() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("1", "1º"));
        itens.add(new SelectItem("2", "2º"));
        return itens;
    }

    private void verificaPermissaoConsultaContaReceberTipoOrigem() throws Exception{
        try {
            ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("AtualizacaoVencimento_Mensalidade", getUsuarioLogado());
            setPermissaoSomenteMensalidade(true);
        } catch (Exception e) {
        	setPermissaoSomenteMensalidade(false);
        }
        try {
            ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("AtualizacaoVencimento_Matricula", getUsuarioLogado());
            setPermissaoSomenteMatricula(true);       
        } catch (Exception e) {
        	setPermissaoSomenteMatricula(false);  
        }
    }

    private void verificaPermissaoConsultaContaReceberVencida() {
        try {
            ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("AtualizacaoVencimento_ContaVencida", getUsuarioLogado());
            setConsultarVencida(Boolean.FALSE);
        } catch (Exception e) {
            setConsultarVencida(Boolean.TRUE);
        }
    }

    public void consultarContasReceberAluno() throws Exception {
    	if (getMatriculaVO().getMatricula().equals("")) {
    		throw new ConsistirException("Informe a matrícula do aluno.");
    	}
        validarPermissoesParaConsultarContaReceber();
        setListaContasReceberAluno(getFacadeFactory().getContaReceberFacade().consultaRapidaPorMatriculaESituacaoAReceberCompleto(getMatriculaVO().getMatricula(), getFiltroRelatorioFinanceiroVO(), getConsultarVencida(), getDataVencimentoInicio(), getDataVencimentoFinal(), getConfiguracaoFinanceiroPadraoSistema(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        //setListaMatriculaPeriodoVencimentoAluno(getFacadeFactory().getMatriculaPeriodoVencimentoFacade().consultarMatriculaPeriodoVencimentoNaoPagaPorMatricula(getMatriculaVO().getMatricula(), getDataVencimentoInicio(), getDataVencimentoFinal(), getConsultarVencida(), getFiltroRelatorioFinanceiroVO(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
    }

	private void validarPermissoesParaConsultarContaReceber() throws Exception {
		verificaPermissaoConsultaContaReceberTipoOrigem();
        verificaPermissaoConsultaContaReceberVencida();
        if(getPermissaoSomenteMensalidade() || getPermissaoSomenteMatricula()){
        	getFiltroRelatorioFinanceiroVO().realizarDesmarcarTodasOrigens();
        	if(getPermissaoSomenteMensalidade()){
        		getFiltroRelatorioFinanceiroVO().setTipoOrigemMensalidade(true);
        		getFiltroRelatorioFinanceiroVO().setDesabilitarFiltroOrigem(true);
        	}
        	if(getPermissaoSomenteMatricula()){
        		getFiltroRelatorioFinanceiroVO().setTipoOrigemMatricula(true);
        		getFiltroRelatorioFinanceiroVO().setDesabilitarFiltroOrigem(true);
        	}
        }
	}

    public void atualizarVencimentoMatricula() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "AtualizacaoVencimentoControle", "Iniciando Alterar Atualizacao Vencimento", "Alterando");            
            getFacadeFactory().getAtualizacaoVencimentoFacade().executarAlteracaoDataVencimentoPorMatricula(getListaContasReceberAluno(), getAtualizarIndividualmente(), getAtualizarDataCompetenciaDeAcordoComDataVencimento(), getPermitirAlterarDataCompetenciaMesmoMesAnoVencimento(), getNovaDataVencimentoContasAluno(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            registrarAtividadeUsuario(getUsuarioLogado(), "AtualizacaoVencimentoControle", "Finalizando Alterar Atualizacao Vencimento", "Alterando");
            setMensagemID("msg_AtualizacaoVencimento_dataAtualizada");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarTurma() {
        try {
            super.consultar();
            if (getCampoConsultaTurma().equals("identificador")) {
            	setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            } else if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
            	setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            } else if (getCampoConsultaTurma().equals("nomeTurno")) {
            	setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            } else if (getCampoConsultaTurma().equals("nomeCurso")) {
            	setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarTurma() {
        try {
            TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
            if (!getTurmaVO().getCodigo().equals(obj.getCodigo())) {
            	getListaAlunosTurmaComContasReceber().clear();
            }
            setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            montarListaParcelaAlunosTurma();
            obj = null;
            setValorConsultaTurma("");
            setCampoConsultaTurma("");
            getListaConsultaTurma().clear();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarTurmaPorIdentificador() {
        try {
            if (getTurmaVO().getIdentificadorTurma().equalsIgnoreCase("")) {
                throw new Exception("Informe o identificador da turma que deseja consultar.");
            } else {
                setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaEspecifico(turmaVO, getTurmaVO().getIdentificadorTurma(), super.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
                montarListaParcelaAlunosTurma();
                setMensagemID("msg_dados_consultados");
            }
        } catch (Exception e) {
            turmaVO = new TurmaVO();
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<SelectItem> getTipoConsultaComboTurma() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("identificador", "Identificador da Turma"));
        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade de Ensino"));
        itens.add(new SelectItem("nomeTurno", "Turno"));
        itens.add(new SelectItem("nomeCurso", "Curso"));

        return itens;

    }

    public void limparDadosTurma() throws Exception {
        setTurmaVO(null);
        getListaAlunosTurmaComContasReceber().clear();
        setMostrarPanelContasTurma(Boolean.FALSE);
        setMostrarCampoNovaData(Boolean.FALSE);
        setMostrarDataTableContasIndividuais(Boolean.FALSE);
        setAtualizarContaPorParcela(Boolean.FALSE);
        getListaSelectItemParcela().clear();
        setMensagemID("msg_entre_prmconsulta");

    }
    
    public void validarDadosAnoSemestre() throws Exception {
    	if (getTurmaVO().getCurso().getPeriodicidade().equals("SE")) {
    		if (getAno().equals("")) {
				throw new Exception("O campo (ANO) deve ser informado.");
			}
			if (getSemestre().equals("")) {
				throw new Exception("O campo (SEMESTRE) deve ser informado.");
			}
    	}
    	if (getTurmaVO().getCurso().getPeriodicidade().equals("AN")) {
    		if (getAno().equals("")) {
				throw new Exception("O campo (ANO) deve ser informado.");
			}
    	}
    }

    public void buscarAlunosTurma() {
        try {   
        	if(Uteis.isAtributoPreenchido(getDataVencimentoFinalTurma()) && Uteis.isAtributoPreenchido(getDataVencimentoInicioTurma())) {
            	if (UteisData.getCompareData(getDataVencimentoFinalTurma(), getDataVencimentoInicioTurma()) < 0) {
            		throw new ConsistirException("A data final não não poder ser menor que a data inicial.");
            	}
        	}
            registrarAtividadeUsuario(getUsuarioLogado(), "AtualizacaoVencimentoControle", "Iniciando Consultar Atualizacao Vencimento Turma", "Consultando");
            validarPermissoesParaConsultarContaReceber();
            validarDadosAnoSemestre();
            Uteis.checkState(getTurmaVO().getIdentificadorTurma().equals(""), "Favor informar a turma.");
            if (getParcela().equals("Matrícula")) {
                setParcela("MA");
            }
            setQuantidadeTotalContaReceberPeriodo(0);
            if(getTurmaVO().getTurmaAgrupada()) {
            	getFacadeFactory().getTurmaAgrupadaFacade().carregarDadosTurmaAgrupada(getTurmaVO(), getUsuarioLogado());
            }
            setListaAlunosTurmaComContasReceber(getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaTurmaAnoSemestreDataVencimentoContaReceber("turmaParcelaAnoSemestre", this.getUnidadeEnsinoLogado().getCodigo(), getMatriculaVO().getMatricula(), getTurmaVO(), getAno(), getSemestre(), getDataVencimentoInicioTurma(), getDataVencimentoFinalTurma(), getDiaSemana(), getConsultarVencida(), getParcela(), Optional.ofNullable(getFiltroRelatorioFinanceiroCloneParaConsultaTurma()), getUsuarioLogado()));            
            boolean gerarPorParcela = !getParcela().equals("TO") ? true:false;
			String parcela = "";
			if (gerarPorParcela) {
				if (parcela.equals("Matrícula")) {
					parcela = "MA";
				}
				parcela = getParcela();
			}
			
            
            for (MatriculaVO matriculaVO : getListaAlunosTurmaComContasReceber()) {
				matriculaVO.setListaContaReceberVOs(getFacadeFactory().getContaReceberFacade().consultaRapidaPorMatriculaDataVencimentoEDiaSemenaESituacaoAReceberCompleto(matriculaVO.getMatricula(), getDataVencimentoInicioTurma(), getDataVencimentoFinalTurma(), getDiaSemana(), getConsultarVencida(), getConfiguracaoFinanceiroPadraoSistema(), Optional.ofNullable(getFiltroRelatorioFinanceiroVO()), parcela, getUsuarioLogado()));
				getFacadeFactory().getContaReceberFacade().realizarSimulacaoDataVencimentoAtualizar(getNovaDataVencimentoContasTurma(), matriculaVO.getListaContaReceberVOs(), getUsuarioLogado());
				matriculaVO.getListaContaReceberVOs().stream().forEach(p->{p.setSelecionado(true);});		
				setQuantidadeTotalContaReceberPeriodo(getQuantidadeTotalContaReceberPeriodo() + matriculaVO.getListaContaReceberVOs().size());
				Ordenacao.ordenarLista(matriculaVO.getListaContaReceberVOs(), "dataVencimento");
			}
            setMostrarPanelContasTurma(Boolean.TRUE);
            setMensagemID("msg_dados_consultados");
            registrarAtividadeUsuario(getUsuarioLogado(), "AtualizacaoVencimentoControle", "Finalizando Consultar Atualizacao Vencimento Turma", "Consultando");
        } catch (Exception e) {
            setMostrarPanelContasTurma(Boolean.FALSE);
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void visualizarContas() {
        try {
            getListaContasReceberAlunosTurma().clear();
            MatriculaVO objMatricula = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoTurmaComContasReceber");
            if (objMatricula != null) {
            	boolean gerarPorParcela = !getParcela().equals("TO") ? true:false;
    			List<MatriculaPeriodoVencimentoVO> matriculaPeriodoVencimentoVOs = new ArrayList<MatriculaPeriodoVencimentoVO>(0);
    			if (gerarPorParcela) {
    				if (parcela.equals("Matrícula")) {
        				parcela = "MA";
        			}
    				matriculaPeriodoVencimentoVOs = getFacadeFactory().getMatriculaPeriodoVencimentoFacade().consultarPorMatricula(objMatricula.getMatricula(), getParcela(), getAno(), getSemestre(), getConsultarVencida(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getProgressBarVO().getConfiguracaoFinanceiroVO(), Optional.ofNullable(getFiltroRelatorioFinanceiroCloneParaConsultaTurma()), getProgressBarVO().getUsuarioVO());
    			} else {
    				matriculaPeriodoVencimentoVOs = getFacadeFactory().getMatriculaPeriodoVencimentoFacade().consultarPorMatricula(objMatricula.getMatricula(), "", getAno(), getSemestre(), getConsultarVencida(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getProgressBarVO().getConfiguracaoFinanceiroVO(), Optional.ofNullable(getFiltroRelatorioFinanceiroCloneParaConsultaTurma()), getProgressBarVO().getUsuarioVO());
    			}
    			matriculaPeriodoVencimentoVOs.stream().forEach(p->{
    				getListaContasReceberAlunosTurma().add(p.getContaReceber());
    			});
            	//setListaContasReceberAlunosTurma(getFacadeFactory().getContaReceberFacade().consultaRapidaPorMatriculaESituacaoAReceber(objMatricula.getMatricula(), getConsultarVencida(), Optional.ofNullable(getFiltroRelatorioFinanceiroCloneParaConsultaTurma())));
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void montarListaParcelaAlunosTurma() {
        List<String> resultadoConsulta = null;
        try {
        	validarPermissoesParaConsultarContaReceber();
        	getListaSelectItemParcela().clear();
        	getListaSelectItemParcela().add(new SelectItem("TO", "Todas"));
        	
            if(getTurmaVO().getTurmaAgrupada()) {
            	getFacadeFactory().getTurmaAgrupadaFacade().carregarDadosTurmaAgrupada(getTurmaVO(), getUsuarioLogado());
            }
        	
            resultadoConsulta = getFacadeFactory().getContaReceberFacade().consultarParcelaPorTurma(getTurmaVO(), Optional.ofNullable(getFiltroRelatorioFinanceiroCloneParaConsultaTurma()), getUsuarioLogado());
            getListaSelectItemParcela().addAll(resultadoConsulta.stream().map(s -> new SelectItem(s, s)).collect(Collectors.toList()));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
        }
    }

    public void atualizarVencimentoTurma() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "AtualizacaoVencimentoControle", "Iniciando Alterar Atualizacao Vencimento Turma", "Alterando");
			boolean gerarPorParcela = !getParcela().equals("TO") ? true:false;
			getFacadeFactory().getContaReceberFacade().validarDadosAtualizarVencimentoTurma(getParcela(), getTurmaVO().getCurso().getPeriodicidade(), getAno(), getSemestre(), getConsultarVencida(), getProgressBarVO().getUsuarioVO());
			List<MatriculaPeriodoVencimentoVO> matriculaPeriodoVencimentoVOs = new ArrayList<MatriculaPeriodoVencimentoVO>(0);
			for (MatriculaVO matriculaVO : getListaAlunosTurmaComContasReceber()) {
				getProgressBarVO().setStatus("Processando dados da Matrícula n°" + matriculaVO.getMatricula() + " ( " + getProgressBarVO().getProgresso() + " de " + getProgressBarVO().getMaxValue() + " ) ");
				matriculaPeriodoVencimentoVOs.clear();
				if (gerarPorParcela) {
					if (parcela.equals("Matrícula")) {
						parcela = "MA";
					}
					matriculaPeriodoVencimentoVOs = getFacadeFactory().getMatriculaPeriodoVencimentoFacade().consultarPorMatricula(matriculaVO.getMatricula(), getParcela(), getAno(), getSemestre(), getConsultarVencida(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getProgressBarVO().getConfiguracaoFinanceiroVO(), Optional.ofNullable(getFiltroRelatorioFinanceiroCloneParaConsultaTurma()), getProgressBarVO().getUsuarioVO());
				} else {
					matriculaPeriodoVencimentoVOs = getFacadeFactory().getMatriculaPeriodoVencimentoFacade().consultarPorMatricula(matriculaVO.getMatricula(), "", getAno(), getSemestre(), getConsultarVencida(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getProgressBarVO().getConfiguracaoFinanceiroVO(), Optional.ofNullable(getFiltroRelatorioFinanceiroCloneParaConsultaTurma()), getProgressBarVO().getUsuarioVO());
				}
				
				if(Uteis.isAtributoPreenchido(matriculaPeriodoVencimentoVOs)) {
					for (MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVO : matriculaPeriodoVencimentoVOs) {
						if(Uteis.isAtributoPreenchido(matriculaPeriodoVencimentoVO.getContaReceber())) {
							for (ContaReceberVO contaReceberVO : matriculaVO.getListaContaReceberVOs()) {
								if (matriculaPeriodoVencimentoVO.getContaReceber().getCodigo().equals(contaReceberVO.getCodigo())) {
									matriculaPeriodoVencimentoVO.getContaReceber().setSelecionado(contaReceberVO.getSelecionado());
									matriculaPeriodoVencimentoVO.getContaReceber().setDataVencimento(contaReceberVO.getDataVencimentoAtualizar());
									break;
								}
							}
						}
					}
				}
				getFacadeFactory().getContaReceberFacade().atualizarVencimentoTurma(matriculaPeriodoVencimentoVOs, getNovaDataVencimentoContasTurma(), gerarPorParcela, getProgressBarVO().getConfiguracaoFinanceiroVO(), getProgressBarVO().getUsuarioVO());			
				getProgressBarVO().setProgresso(getProgressBarVO().getProgresso() + 1);
			
				String parcela = "";
				if (gerarPorParcela) {
					if (parcela.equals("Matrícula")) {
						parcela = "MA";
					}
					parcela = getParcela();
				}
				
				matriculaVO.setListaContaReceberVOs(getFacadeFactory().getContaReceberFacade().consultaRapidaPorMatriculaDataVencimentoEDiaSemenaESituacaoAReceberCompleto(matriculaVO.getMatricula(), getDataVencimentoInicioTurma(), getDataVencimentoFinalTurma(), getDiaSemana(), getConsultarVencida(), getConfiguracaoFinanceiroPadraoSistema(), Optional.ofNullable(getFiltroRelatorioFinanceiroVO()), parcela, getUsuarioLogado()));
				
				for (ContaReceberVO contaReceberAtualizar : matriculaVO.getListaContaReceberVOs()) {
					for (MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVO : matriculaPeriodoVencimentoVOs) {
						if(Uteis.isAtributoPreenchido(matriculaPeriodoVencimentoVO.getContaReceber()) && contaReceberAtualizar.getCodigo().equals(matriculaPeriodoVencimentoVO.getContaReceber().getCodigo())) {
							contaReceberAtualizar.setSelecionado(matriculaPeriodoVencimentoVO.getContaReceber().getSelecionado());
							break;														
						}
					}	
				}
								
				Ordenacao.ordenarLista(matriculaVO.getListaContaReceberVOs(), "dataVencimento");
				
			}
			setMensagemID("msg_AtualizacaoVencimento_dataAtualizada");
			registrarAtividadeUsuario(getUsuarioLogado(), "AtualizacaoVencimentoControle", "Finalizando Alterar Atualizacao Vencimento Turma", "Alterando");			
			
		} catch (Exception e) {
			getProgressBarVO().setForcarEncerramento(true);			
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

    public void verificarTipoAtualizacao() {
        if (getAtualizarIndividualmente()) {
            setMostrarCampoNovaData(Boolean.FALSE);
            setMostrarDataTableContasIndividuais(Boolean.TRUE);

        } else {
            setMostrarCampoNovaData(Boolean.TRUE);
            setMostrarDataTableContasIndividuais(Boolean.FALSE);

        }
    }

    public void imprimirBoleto() throws Exception {
        ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contasAluno");

        if (obj.getSituacaoEQuitada()) {
            setMensagemDetalhada("msg_erro", "Não é possível emitir o boleto de uma parcela já paga!");

        }
        setContaReceberVO(obj);

        if (getConfiguracaoFinanceiroVO().getCodigo().equals(0)) {
            // setConfiguracaoFinanceiroVO(getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(
            // Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), null));
            setConfiguracaoFinanceiroVO(getConfiguracaoFinanceiroPadraoSistema());

        }
        getFacadeFactory().getContaReceberFacade().carregarDados(obj, NivelMontarDados.TODOS, getConfiguracaoFinanceiroVO(), getUsuarioLogado());

        try {
            // if (getConfiguracaoFinanceiroVO().getCobrarReimpressaoBoletos()
            // || getContaReceberVO().getImpressaoBoletoRealizada()) {
            if (getConfiguracaoFinanceiroVO().getCobrarReimpressaoBoletos()) {
                if (getContaReceberVO().getImpressaoBoletoRealizada()) {
                    setAbrirModalCobrarReimpressao("RichFaces.$('panelCobrarReimpressao').show();");

                } else {
                    setAbrirModalCobrarReimpressao("");
                    setAbrirModalCobrarReimpressao(getBoletoConsultaContaReceber());

                }
            } else {
                setAbrirModalCobrarReimpressao("");
                setAbrirModalCobrarReimpressao(getBoletoConsultaContaReceber());

            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        } finally {
            obj = null;

        }
    }
    

    public String getBoletoConsultaContaReceber() {
        if (getApresentarImprimirBoletoConsultaContaReceber()) {
            try {
                getContaReceberVO().setImpressaoBoletoRealizada(Boolean.TRUE);
                getFacadeFactory().getContaReceberFacade().alterarBooleanEmissaoBoletoRealizada(getContaReceberVO().getCodigo(), true, getUsuarioLogado());

                return "abrirPopup('../../BoletoBancarioSV?codigoContaReceber=" + getContaReceberVO().getCodigo() + "&amp;titulo=BoletoPagamento', 'boletoGeral', 780, 585);RichFaces.$('panelCobrarReimpressao').hide();";

            } catch (Exception e) {
                return "";

            }
        } else {
            return "";

        }
    }

    public Boolean getApresentarImprimirBoletoConsultaContaReceber() {
        if (getContaReceberVO().getSituacaoEQuitada()) {
            return false;

        } else {
            return true;

        }
    }

    public String imprimirBoletoCriandoContaReimpressao() throws Exception {
        try {
            getFacadeFactory().getContaReceberFacade().carregarDados(getContaReceberVO(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            if (!getContaReceberVO().getTipoParceiro() && !getContaReceberVO().getTipoFornecedor()) {
                getFacadeFactory().getPessoaFacade().carregarDados(getContaReceberVO().getPessoa(), getUsuarioLogado());
                getFacadeFactory().getContaReceberFacade().criarContaReceber(null, null, getContaReceberVO().getPessoa(), getContaReceberVO().getUnidadeEnsino(), getConfiguracaoFinanceiroVO().getContaCorrenteReimpressaoBoletos(), 0, getContaReceberVO().getTipoOrigem(), new Date(), new Date(), getConfiguracaoFinanceiroVO().getValorCobrarReimpressaoBoletos().doubleValue(), getConfiguracaoFinanceiroVO().getCentroReceitaReimpressaoBoletos().getCodigo(), 1, 1, "OU", getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), null, "");
            } else if (getContaReceberVO().getTipoParceiro()) {
                getFacadeFactory().getContaReceberFacade().criarContaReceber(null, getContaReceberVO().getParceiroVO(), null, getContaReceberVO().getUnidadeEnsino(), getConfiguracaoFinanceiroVO().getContaCorrenteReimpressaoBoletos(), 0, getContaReceberVO().getTipoOrigem(), new Date(), new Date(), getConfiguracaoFinanceiroVO().getValorCobrarReimpressaoBoletos().doubleValue(), getConfiguracaoFinanceiroVO().getCentroReceitaReimpressaoBoletos().getCodigo(), 1, 1, "OU", getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), null, "");
            } else if (getContaReceberVO().getTipoFornecedor()) {
                getFacadeFactory().getContaReceberFacade().criarContaReceber(null, null, null, getContaReceberVO().getUnidadeEnsino(), getConfiguracaoFinanceiroVO().getContaCorrenteReimpressaoBoletos(), 0, getContaReceberVO().getTipoOrigem(), new Date(), new Date(), getConfiguracaoFinanceiroVO().getValorCobrarReimpressaoBoletos().doubleValue(), getConfiguracaoFinanceiroVO().getCentroReceitaReimpressaoBoletos().getCodigo(), 1, 1, "OU", getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), getContaReceberVO().getFornecedor(), "");
            }
            return "";

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

            return "";

        }
    }

    public String definirBoletoParaImpressao() {
        return "";

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

    public List<SelectItem> getListaConsultaAluno() {
        if (listaConsultaAluno == null) {
            listaConsultaAluno = new ArrayList<SelectItem>(0);

        }
        return listaConsultaAluno;

    }

    public void setListaConsultaAluno(List listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;

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

    public String getCampoConsultaTurma() {
        if (campoConsultaTurma == null) {
            campoConsultaTurma = "";

        }
        return campoConsultaTurma;

    }

    public void setCampoConsultaTurma(String campoConsultaTurma) {
        this.campoConsultaTurma = campoConsultaTurma;

    }

    public List<SelectItem> getListaConsultaTurma() {
        if (listaConsultaTurma == null) {
            listaConsultaTurma = new ArrayList<SelectItem>(0);

        }
        return listaConsultaTurma;

    }

    public void setListaConsultaTurma(List listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;

    }

    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;

    }

    public MatriculaVO getMatriculaVO() {
        if (matriculaVO == null) {
            matriculaVO = new MatriculaVO();

        }
        return matriculaVO;

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

    public void setMostrarPanelContasAluno(Boolean mostrarPanelContasAluno) {
        this.mostrarPanelContasAluno = mostrarPanelContasAluno;

    }

    public Boolean getMostrarPanelContasAluno() {
        if (mostrarPanelContasAluno == null) {
            mostrarPanelContasAluno = false;

        }
        return mostrarPanelContasAluno;

    }

    public Date getNovaDataVencimentoContasAluno() {
        if (novaDataVencimentoContasAluno == null) {
            novaDataVencimentoContasAluno = new Date();

        }
        return novaDataVencimentoContasAluno;

    }

    public void setNovaDataVencimentoContasAluno(Date novaDataVencimentoContasAluno) {
        this.novaDataVencimentoContasAluno = novaDataVencimentoContasAluno;

    }

    public Date getNovaDataVencimentoContasTurma() {
        if (novaDataVencimentoContasTurma == null) {
            novaDataVencimentoContasTurma = new Date();

        }
        return novaDataVencimentoContasTurma;

    }

    public void setNovaDataVencimentoContasTurma(Date novaDataVencimentoContasTurma) {
        this.novaDataVencimentoContasTurma = novaDataVencimentoContasTurma;

    }

    public Boolean getMostrarPanelContasTurma() {
        if (mostrarPanelContasTurma == null) {
            mostrarPanelContasTurma = false;

        }
        return mostrarPanelContasTurma;

    }

    public void setMostrarPanelContasTurma(Boolean mostrarPanelContasTurma) {
        this.mostrarPanelContasTurma = mostrarPanelContasTurma;

    }

    public void setListaContasReceberAluno(List<ContaReceberVO> listaContasReceberAluno) {
        this.listaContasReceberAluno = listaContasReceberAluno;

    }

    public List<ContaReceberVO> getListaContasReceberAluno() {
        if (listaContasReceberAluno == null) {
            listaContasReceberAluno = new ArrayList<ContaReceberVO>(0);

        }
        return listaContasReceberAluno;

    }

    public List<MatriculaVO> getListaAlunosTurmaComContasReceber() {
        if (listaAlunosTurmaComContasReceber == null) {
            listaAlunosTurmaComContasReceber = new ArrayList<MatriculaVO>(0);

        }
        return listaAlunosTurmaComContasReceber;

    }

    public void setListaAlunosTurmaComContasReceber(List<MatriculaVO> listaAlunosTurmaComContasReceber) {
        this.listaAlunosTurmaComContasReceber = listaAlunosTurmaComContasReceber;

    }

    public void setListaContasReceberAlunosTurma(List<ContaReceberVO> listaContasReceberAlunosTurma) {
        this.listaContasReceberAlunosTurma = listaContasReceberAlunosTurma;

    }

    public List<ContaReceberVO> getListaContasReceberAlunosTurma() {
        if (listaContasReceberAlunosTurma == null) {
            listaContasReceberAlunosTurma = new ArrayList<ContaReceberVO>(0);

        }
        return listaContasReceberAlunosTurma;

    }

    public List<MatriculaPeriodoVencimentoVO> getListaMatriculaPeriodoVencimentoAluno() {
        if (listaMatriculaPeriodoVencimentoAluno == null) {
            listaMatriculaPeriodoVencimentoAluno = new ArrayList<MatriculaPeriodoVencimentoVO>(0);

        }
        return listaMatriculaPeriodoVencimentoAluno;

    }

    public void setListaMatriculaPeriodoVencimentoAluno(List<MatriculaPeriodoVencimentoVO> listaMatriculaPeriodoVencimentoAluno) {
        this.listaMatriculaPeriodoVencimentoAluno = listaMatriculaPeriodoVencimentoAluno;

    }

    public Integer getNovoDiaVencimento() {
        if (novoDiaVencimento == null) {
            novoDiaVencimento = 0;

        }
        return novoDiaVencimento;

    }

    public void setNovoDiaVencimento(Integer novoDiaVencimento) {
        this.novoDiaVencimento = novoDiaVencimento;

    }

    public void atualizarVencimentoPorContaReceberIndividual() {
        try {
            ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contasAluno");
            getFacadeFactory().getAtualizacaoVencimentoFacade().executarAlteracaoDataVencimentoPorContaReceber(obj,  getAtualizarIndividualmente(), getAtualizarDataCompetenciaDeAcordoComDataVencimento(), getPermitirAlterarDataCompetenciaMesmoMesAnoVencimento(), getNovaDataVencimentoContasAluno(), 0, getConfiguracaoFinanceiroVO(), true, getUsuarioLogado());
            setMensagemID("msg_AtualizacaoVencimento_dataAtualizada");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void removerContaRecebimentoAluno() {
    	try {
    		ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contasAluno");
    		getListaContasReceberAluno().remove(obj);
    		
    		for (int i = 0; i < getListaMatriculaPeriodoVencimentoAluno().size(); i++) {
    			MatriculaPeriodoVencimentoVO matriculaPeriodoVencimento = getListaMatriculaPeriodoVencimentoAluno().get(i);
    			
    			if (matriculaPeriodoVencimento.getContaReceber().getCodigo().equals(obj.getCodigo())) {
    				getListaMatriculaPeriodoVencimentoAluno().remove(i);
    				
    				break;
    				
    			}
    		}
    	} catch (Exception e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    		
    	}
    }

    public void removerMatriculaAluno() {
        try {
            MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoTurmaComContasReceber");
            getListaAlunosTurmaComContasReceber().remove(obj);

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void setAtualizarIndividualmente(Boolean atualizarIndividualmente) {
        this.atualizarIndividualmente = atualizarIndividualmente;

    }

    public Boolean getAtualizarIndividualmente() {
        if (atualizarIndividualmente == null) {
            atualizarIndividualmente = Boolean.FALSE;

        }
        return atualizarIndividualmente;

    }

    public void setMostrarCampoNovaData(Boolean mostrarCampoNovaData) {
        this.mostrarCampoNovaData = mostrarCampoNovaData;

    }

    public Boolean getMostrarCampoNovaData() {
        if (mostrarCampoNovaData == null) {
            mostrarCampoNovaData = Boolean.FALSE;

        }
        return mostrarCampoNovaData;

    }

    public void setMostrarDataTableContasIndividuais(Boolean mostrarDataTableContasIndividuais) {
        this.mostrarDataTableContasIndividuais = mostrarDataTableContasIndividuais;

    }

    public Boolean getMostrarDataTableContasIndividuais() {
        if (mostrarDataTableContasIndividuais == null) {
            mostrarDataTableContasIndividuais = Boolean.FALSE;

        }
        return mostrarDataTableContasIndividuais;

    }

    public void setContaReceberVO(ContaReceberVO contaReceberVO) {
        this.contaReceberVO = contaReceberVO;

    }

    public ContaReceberVO getContaReceberVO() {
        if (contaReceberVO == null) {
            contaReceberVO = new ContaReceberVO();

        }
        return contaReceberVO;

    }

    public void setConfiguracaoFinanceiroVO(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) {
        this.configuracaoFinanceiroVO = configuracaoFinanceiroVO;

    }

    public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroVO() {
        if (configuracaoFinanceiroVO == null) {
            configuracaoFinanceiroVO = new ConfiguracaoFinanceiroVO();

        }
        return configuracaoFinanceiroVO;

    }

    public void setAbrirModalCobrarReimpressao(String abrirModalCobrarReimpressao) {
        this.abrirModalCobrarReimpressao = abrirModalCobrarReimpressao;

    }

    public String getAbrirModalCobrarReimpressao() {
        if (abrirModalCobrarReimpressao == null) {
            abrirModalCobrarReimpressao = "";

        }
        return abrirModalCobrarReimpressao;

    }

    public List<SelectItem> getListaSelectItemParcela() {
        if (listaSelectItemParcela == null) {
            listaSelectItemParcela = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemParcela;
    }

    public void setListaSelectItemParcela(List<SelectItem> listaSelectItemParcela) {
        this.listaSelectItemParcela = listaSelectItemParcela;
    }

    public Boolean getAtualizarContaPorParcela() {
        if (atualizarContaPorParcela == null) {
            atualizarContaPorParcela = Boolean.FALSE;
        }
        return atualizarContaPorParcela;
    }

    public void setAtualizarContaPorParcela(Boolean atualizarContaPorParcela) {
        this.atualizarContaPorParcela = atualizarContaPorParcela;
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

    public Boolean getApresentarParcela() {
        return !getTurmaVO().getCodigo().equals(0);
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

    public Boolean getApresentarAno() {
        return getTurmaVO().getCurso().getPeriodicidade().equals("SE") || getTurmaVO().getCurso().getPeriodicidade().equals("AN");
    }

    public Boolean getApresentarSemestre() {
    	return getTurmaVO().getCurso().getPeriodicidade().equals("SE");
    }

    /**
     * @return the tipoOrigem
     */
    public String getTipoOrigem() {
        if (tipoOrigem == null) {
            tipoOrigem = "";
        }
        return tipoOrigem;
    }

    /**
     * @param tipoOrigem the tipoOrigem to set
     */
    public void setTipoOrigem(String tipoOrigem) {
        this.tipoOrigem = tipoOrigem;
    }

    /**
     * @return the consultarVencida
     */
    public Boolean getConsultarVencida() {
        if (consultarVencida == null) {
            consultarVencida = Boolean.TRUE;
        }
        return consultarVencida;
    }

    /**
     * @param consultarVencida the consultarVencida to set
     */
    public void setConsultarVencida(Boolean consultarVencida) {
        this.consultarVencida = consultarVencida;
    }


    public List<SelectItem> getListaSelectItemTipoAtualizacao() {
    	List<SelectItem> objs = new ArrayList<SelectItem>(0);
        objs.add(new SelectItem("", ""));        
    	objs.add(new SelectItem("MAT", "Matrícula"));
    	objs.add(new SelectItem("TUR", "Turma"));
    	objs.add(new SelectItem("PERIODO_VENCIMENTO", "Período Vencimento"));
    	return objs;
    }    	

	public String getTipoAtualizacao() {
		if (tipoAtualizacao == null) {
			tipoAtualizacao = "";
		}
		return tipoAtualizacao;
	}

	public void setTipoAtualizacao(String tipoAtualizacao) {
		this.tipoAtualizacao = tipoAtualizacao;
	}

	public Date getDataVencimentoInicio() {
		if (dataVencimentoInicio == null) {
			dataVencimentoInicio = new Date();
		}
		return dataVencimentoInicio;
	}

	public void setDataVencimentoInicio(Date dataVencimentoInicio) {
		this.dataVencimentoInicio = dataVencimentoInicio;
	}

	public Date getDataVencimentoFinal() {
		if (dataVencimentoFinal == null) {
			dataVencimentoFinal = Uteis.getDataUltimoDiaMes(new Date());
		}
		return dataVencimentoFinal;
	}

	public void setDataVencimentoFinal(Date dataVencimentoFinal) {
		this.dataVencimentoFinal = dataVencimentoFinal;
	}

	public List<SelectItem> getComboDiaSemana() {
		return DiaSemana.getComboDiaSemana();
	}

	public DiaSemana getDiaSemana() {
		if (diaSemana == null) {
			diaSemana = DiaSemana.NENHUM;
		}
		return diaSemana;
	}

	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}

	public List<ContaReceberVO> getListaContasReceberVOs() {
		if (listaContasReceberVOs == null) {
			listaContasReceberVOs = new ArrayList<ContaReceberVO>(0);
		}
		return listaContasReceberVOs;
	}

	public void setListaContasReceberVOs(List<ContaReceberVO> listaContasReceberVOs) {
		this.listaContasReceberVOs = listaContasReceberVOs;
	}

	public void consultarAlunoPorFiltrosReferentesADataVencimento() {
		try {
			validarPermissoesParaConsultarContaReceber();
			setQuantidadeTotalContaReceberPeriodo(0);
			getFacadeFactory().getContaReceberFacade().validarDadosAtualizacaoVencimentoPorPeriodo(getDataVencimentoInicio(), getDataVencimentoFinal());
			setListaMatriculaVOs(getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaTurmaAnoSemestreDataVencimentoContaReceber("AlunoPorFiltros", this.getUnidadeEnsinoLogado().getCodigo(), getMatriculaVO().getMatricula(), getTurmaVO(), getAno(), getSemestre(), getDataVencimentoInicio(), getDataVencimentoFinal(), getDiaSemana(), getConsultarVencida(), "TO", Optional.ofNullable(getFiltroRelatorioFinanceiroVO()), getUsuarioLogado()));
			for (MatriculaVO matriculaVO : getListaMatriculaVOs()) {
				matriculaVO.setListaContaReceberVOs(getFacadeFactory().getContaReceberFacade().consultaRapidaPorMatriculaDataVencimentoEDiaSemenaESituacaoAReceberCompleto(matriculaVO.getMatricula(), getDataVencimentoInicio(), getDataVencimentoFinal(), getDiaSemana(), getConsultarVencida(), getConfiguracaoFinanceiroPadraoSistema(), Optional.ofNullable(getFiltroRelatorioFinanceiroVO()), "", getUsuarioLogado()));
				getFacadeFactory().getContaReceberFacade().realizarSimulacaoDataVencimentoAtualizar(getNovaDataVencimentoContaReceberPeriodo(), matriculaVO.getListaContaReceberVOs(), getUsuarioLogado());
				matriculaVO.getListaContaReceberVOs().stream().forEach(p->{p.setSelecionado(true);});		
				setQuantidadeTotalContaReceberPeriodo(getQuantidadeTotalContaReceberPeriodo() + matriculaVO.getListaContaReceberVOs().size());
				Ordenacao.ordenarLista(matriculaVO.getListaContaReceberVOs(), "dataVencimento");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerContaReceberPanelDataVencimento() {
		try {
			ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contaRecVO");
			getListaContasReceberVOs().remove(obj);

			for (int i = 0; i < getListaMatriculaPeriodoVencimentoAluno().size(); i++) {
				MatriculaPeriodoVencimentoVO matriculaPeriodoVencimento = getListaMatriculaPeriodoVencimentoAluno().get(i);
				if (matriculaPeriodoVencimento.getContaReceber().getCodigo().equals(obj.getCodigo())) {
					getListaMatriculaPeriodoVencimentoAluno().remove(i);
					break;
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public List<MatriculaVO> getListaMatriculaVOs() {
		if (listaMatriculaVOs == null) {
			listaMatriculaVOs = new ArrayList<MatriculaVO>(0);
		}
		return listaMatriculaVOs;
	}

	public void setListaMatriculaVOs(List<MatriculaVO> listaMatriculaVOs) {
		this.listaMatriculaVOs = listaMatriculaVOs;
	}

	public void visualizarContaReceberPeriodo() {
		try {
			if(getTipoAtualizacao().equals("PERIODO_VENCIMENTO")) {
				setMatriculaVOTemp((MatriculaVO) context().getExternalContext().getRequestMap().get("alunoTurmaComContasReceberPeriodo"));
			}else if(getTipoAtualizacao().equals("TUR")) {
				setMatriculaVOTemp((MatriculaVO) context().getExternalContext().getRequestMap().get("alunoTurmaComContasReceber"));
			}
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarSimulacaoDataVencimentoAtualizar() {
		try {
			if(getTipoAtualizacao().equals("PERIODO_VENCIMENTO")) {
				for (MatriculaVO obj : getListaMatriculaVOs()) {
					getFacadeFactory().getContaReceberFacade().realizarSimulacaoDataVencimentoAtualizar(getNovaDataVencimentoContaReceberPeriodo(), obj.getListaContaReceberVOs(), getUsuarioLogado());
				}
			}else if(getTipoAtualizacao().equals("TUR")) {
				for (MatriculaVO obj : getListaAlunosTurmaComContasReceber()) {
					getFacadeFactory().getContaReceberFacade().realizarSimulacaoDataVencimentoAtualizar(getNovaDataVencimentoContasTurma(), obj.getListaContaReceberVOs(), getUsuarioLogado());
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}	

	

	public void preencherTodosListaAluno() {		
		getMatriculaVOTemp().getListaContaReceberVOs().stream().forEach(p->{p.setSelecionado(true);});
		getMatriculaVOTemp().setAlunoSelecionado(true);
	}

	public void desmarcarTodosListaAluno() {
		getMatriculaVOTemp().getListaContaReceberVOs().stream().forEach(p->{p.setSelecionado(false);});
		getMatriculaVOTemp().setAlunoSelecionado(false);
	}
	
	public void atualizarListaAluno() {
		ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contaRecberPeriodoItens");
		try {
			getMatriculaVOTemp().getListaContaReceberVOs().stream()
			.filter(p-> p.getCodigo().equals(obj.getCodigo())).forEach(p->{p.setSelecionado(obj.getSelecionado());});
			if(getMatriculaVOTemp().getListaContaReceberVOs().stream().allMatch(p->!p.getSelecionado()) && getMatriculaVOTemp().getAlunoSelecionado()){
				getMatriculaVOTemp().setAlunoSelecionado(false);
			}else if(getMatriculaVOTemp().getListaContaReceberVOs().stream().anyMatch(p->p.getSelecionado()) && !getMatriculaVOTemp().getAlunoSelecionado()){
				getMatriculaVOTemp().setAlunoSelecionado(true);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void preencherTodosListaGeralAluno() {
		for (MatriculaVO obj : getListaMatriculaVOs()) {
			obj.setAlunoSelecionado(true);
			for (ContaReceberVO contaReceberVO : obj.getListaContaReceberVOs()) {
				contaReceberVO.setSelecionado(true);
			}
		}
	}

	public void desmarcarTodosListaGeralAluno() {
		for (MatriculaVO obj : getListaMatriculaVOs()) {
			obj.setAlunoSelecionado(false);
			for (ContaReceberVO contaReceberVO : obj.getListaContaReceberVOs()) {
				contaReceberVO.setSelecionado(false);
			}
		}
	}
	
	public void atualizarListaGeralAluno() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoTurmaComContasReceberPeriodo");
		try {
			for (ContaReceberVO contaReceberVO : obj.getListaContaReceberVOs()) {
				contaReceberVO.setSelecionado(obj.getAlunoSelecionado());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerAlunoListaPeriodo() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoTurmaComContasReceberPeriodo");
			int index = 0;
			Iterator i = getListaMatriculaVOs().iterator();
			while (i.hasNext()) {
				MatriculaVO objExistente = (MatriculaVO) i.next();
				if (objExistente.getMatricula().equals(obj.getMatricula())) {
					setQuantidadeTotalContaReceberPeriodo(getQuantidadeTotalContaReceberPeriodo() - objExistente.getListaContaReceberVOs().size());
					getListaMatriculaVOs().remove(index);
					return;
				}
				index++;
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void atualizarVencimentoContaReceberPorPeriodo() {
		try {
			registrarAtividadeUsuario(getProgressBarVO().getUsuarioVO(), "AtualizacaoVencimentoControle", "Iniciando Alterar Atualizacao Vencimento Período", "Alterando");
			getFacadeFactory().getContaReceberFacade().validarDadosAtualizacaoVencimentoPorPeriodo(getDataVencimentoInicio(), getDataVencimentoFinal());
			getFacadeFactory().getContaReceberFacade().realizarAtualizacaoVencimentoPorPeriodo(getListaMatriculaVOs(), getNovaDataVencimentoContaReceberPeriodo(), getProgressBarVO(), getProgressBarVO().getConfiguracaoFinanceiroVO(), getProgressBarVO().getUsuarioVO());
			setMensagemID("msg_AtualizacaoVencimento_dataAtualizada");
			registrarAtividadeUsuario(getProgressBarVO().getUsuarioVO(), "AtualizacaoVencimentoControle", "Finalizando Alterar Atualizacao Vencimento Turma", "Alterando");
		} catch (Exception e) {
			getProgressBarVO().setForcarEncerramento(true);
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public Date getNovaDataVencimentoContaReceberPeriodo() {
		if (novaDataVencimentoContaReceberPeriodo == null) {
			novaDataVencimentoContaReceberPeriodo = new Date();
		}
		return novaDataVencimentoContaReceberPeriodo;
	}

	public void setNovaDataVencimentoContaReceberPeriodo(Date novaDataVencimentoContaReceberPeriodo) {
		this.novaDataVencimentoContaReceberPeriodo = novaDataVencimentoContaReceberPeriodo;
	}

	public ProgressBarVO getProgressBarVO() {
		if (progressBarVO == null) {
			progressBarVO = new ProgressBarVO();
		}
		return progressBarVO;
	}

	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}

	public void executarInicioProgressBarAtualizacaoVencimento(String metodo) {
		try {
			setProgressBarVO(new ProgressBarVO());
			getProgressBarVO().resetar();			
			Long maxValue = 0L;
			if (getTipoAtualizacao().equals("TUR")) {
				maxValue = getListaAlunosTurmaComContasReceber().stream().count();
			} else {
				maxValue = getListaMatriculaVOs().stream().filter(MatriculaVO::getAlunoSelecionado).count();
			}
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
			getProgressBarVO().setConfiguracaoFinanceiroVO(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getCodigo()));
			getProgressBarVO().iniciar(0l, maxValue.intValue(), "Iniciando Carregamento dos dados ( 1 de " + maxValue + " ) ", true, this, metodo);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosReferenteAoTipoAtualizacao() {
		try {
			if (getTipoAtualizacao().equals("MAT") || getTipoAtualizacao().equals("TUR")) {
				getListaMatriculaVOs().clear();
			}
			setTurmaVO(null);
			setMatriculaVO(null);
			setListaAlunosTurmaComContasReceber(null);
			validarPermissoesParaConsultarContaReceber();	
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Integer getQuantidadeAlunoListaPeriodo() {
		return getListaMatriculaVOs().size();
	}

	public Integer getQuantidadeTotalContaReceberPeriodo() {
		if (quantidadeTotalContaReceberPeriodo == null) {
			quantidadeTotalContaReceberPeriodo = 0;
		}
		return quantidadeTotalContaReceberPeriodo;
	}

	public void setQuantidadeTotalContaReceberPeriodo(Integer quantidadeTotalContaReceberPeriodo) {
		this.quantidadeTotalContaReceberPeriodo = quantidadeTotalContaReceberPeriodo;
	}
	
	
	
	public Boolean getPermitirAlterarDataCompetenciaMesmoMesAnoVencimento() {
		if (permitirAlterarDataCompetenciaMesmoMesAnoVencimento == null) {
			permitirAlterarDataCompetenciaMesmoMesAnoVencimento = false;
		}
		return permitirAlterarDataCompetenciaMesmoMesAnoVencimento;
	}

	public void setPermitirAlterarDataCompetenciaMesmoMesAnoVencimento(Boolean permitirAlterarDataCompetenciaMesmoMesAnoVencimento) {
		this.permitirAlterarDataCompetenciaMesmoMesAnoVencimento = permitirAlterarDataCompetenciaMesmoMesAnoVencimento;
	}

	public void verificarUsuarioPermissaoAlterarDataCompetenciaMesmoMesAnoVencimento() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirAlterarDataCompetenciaMesmoMesAnoVencimento", getUsuarioLogado());
			setPermitirAlterarDataCompetenciaMesmoMesAnoVencimento(Boolean.TRUE);
			setAtualizarDataCompetenciaDeAcordoComDataVencimento(true);
		} catch (Exception e) {
			setPermitirAlterarDataCompetenciaMesmoMesAnoVencimento(Boolean.FALSE);
		}
	}

	public Boolean getAtualizarDataCompetenciaDeAcordoComDataVencimento() {
		if (atualizarDataCompetenciaDeAcordoComDataVencimento == null) {
			atualizarDataCompetenciaDeAcordoComDataVencimento = false;
		}
		return atualizarDataCompetenciaDeAcordoComDataVencimento;
	}

	public void setAtualizarDataCompetenciaDeAcordoComDataVencimento(Boolean atualizarDataCompetenciaDeAcordoComDataVencimento) {
		this.atualizarDataCompetenciaDeAcordoComDataVencimento = atualizarDataCompetenciaDeAcordoComDataVencimento;
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
	
	public Boolean getMarcarTodosTipoOrigem() {
		if (marcarTodosTipoOrigem == null) {
			marcarTodosTipoOrigem = true;
		}
		return marcarTodosTipoOrigem;
	}

	public void setMarcarTodosTipoOrigem(Boolean marcarTodosTipoOrigem) {
		this.marcarTodosTipoOrigem = marcarTodosTipoOrigem;
	}
	
	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodosTipoOrigem() {
		if (getMarcarTodosTipoOrigem()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}
	
	public void realizarSelecaoCheckboxMarcarDesmarcarTodosTipoOrigem() {
		if (getMarcarTodosTipoOrigem()) {
			filtroRelatorioFinanceiroVO.realizarMarcarTodasOrigens();
		} else {
			filtroRelatorioFinanceiroVO.realizarDesmarcarTodasOrigens();
		}
	}

	
	public Boolean getPermissaoSomenteMensalidade() {
		if (permissaoSomenteMensalidade == null) {
			permissaoSomenteMensalidade = false;
		}
		return permissaoSomenteMensalidade;
	}

	public void setPermissaoSomenteMensalidade(Boolean permissaoSomenteMensalidade) {
		this.permissaoSomenteMensalidade = permissaoSomenteMensalidade;
	}

	public Boolean getPermissaoSomenteMatricula() {
		if (permissaoSomenteMatricula == null) {
			permissaoSomenteMatricula = false;
		}
		return permissaoSomenteMatricula;
	}

	public void setPermissaoSomenteMatricula(Boolean permissaoSomenteMatricula) {
		this.permissaoSomenteMatricula = permissaoSomenteMatricula;
	}
	
	public MatriculaVO getMatriculaVOTemp() {
		if (matriculaVOTemp == null) {
			matriculaVOTemp = new MatriculaVO();
		}
		return matriculaVOTemp;
	}

	public void setMatriculaVOTemp(MatriculaVO matriculaVOTemp) {
		this.matriculaVOTemp = matriculaVOTemp;
	}

	public void inicializarDadosFiltrRelatorioFinanceiroVO() {
		getFiltroRelatorioFinanceiroVO().setTipoOrigemBiblioteca(true);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemBolsaCusteadaConvenio(true);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemContratoReceita(true);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemDevolucaoCheque(true);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemInclusaoReposicao(true);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemInscricaoProcessoSeletivo(false);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemMatricula(true);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemMensalidade(true);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemNegociacao(true);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemOutros(true);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemRequerimento(false);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemMaterialDidatico(false);
	}
	
	public void realizarConsultaContasReceberAluno() {
    	try {
    		consultarContasReceberAluno();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

	public FiltroRelatorioFinanceiroVO getFiltroRelatorioFinanceiroCloneParaConsultaTurma() {
		FiltroRelatorioFinanceiroVO filtro = new FiltroRelatorioFinanceiroVO();
		filtro.setTipoOrigemBiblioteca(false);
		filtro.setTipoOrigemBolsaCusteadaConvenio(false);
		filtro.setTipoOrigemContratoReceita(false);
		filtro.setTipoOrigemDevolucaoCheque(false);
		filtro.setTipoOrigemInclusaoReposicao(false);
		filtro.setTipoOrigemInscricaoProcessoSeletivo(false);
		filtro.setTipoOrigemNegociacao(false);
		filtro.setTipoOrigemOutros(false);
		filtro.setTipoOrigemRequerimento(false);
		filtro.setTipoOrigemMensalidade(getFiltroRelatorioFinanceiroVO().getTipoOrigemMensalidade());
		filtro.setTipoOrigemMaterialDidatico(getFiltroRelatorioFinanceiroVO().getTipoOrigemMaterialDidatico());
		filtro.setTipoOrigemMatricula(getFiltroRelatorioFinanceiroVO().getTipoOrigemMatricula());
		if (!filtro.getTipoOrigemMensalidade() && !filtro.getTipoOrigemMaterialDidatico() && !filtro.getTipoOrigemMatricula()) {
			filtro.setTipoOrigemMatricula(true);
			filtro.setTipoOrigemMensalidade(true);
			filtro.setTipoOrigemMaterialDidatico(true);
		}
		return filtro;
	}

	public Date getDataVencimentoInicioTurma() {
		return dataVencimentoInicioTurma;
	}

	public void setDataVencimentoInicioTurma(Date dataVencimentoInicioTurma) {
		this.dataVencimentoInicioTurma = dataVencimentoInicioTurma;
	}

	public Date getDataVencimentoFinalTurma() {
		return dataVencimentoFinalTurma;
	}

	public void setDataVencimentoFinalTurma(Date dataVencimentoFinalTurma) {
		this.dataVencimentoFinalTurma = dataVencimentoFinalTurma;
	}
	
}
