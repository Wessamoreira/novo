package controle.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;

@Controller("MudancaCarteiraControle")
@Scope("viewScope")
@Lazy
public class MudancaCarteiraControle extends SuperControle implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2121022505665374485L;
	private UnidadeEnsinoVO unidadeEnsinoVO;
    private UnidadeEnsinoVO unidadeEnsinoAcademicoVO;
    private CursoVO cursoVO;
    private TurmaVO turmaVO;
    private MatriculaVO matriculaVO;
    private ContaCorrenteVO contaCorrenteVO;
    private ContaCorrenteVO contaCorrenteOrigemVO;
    private Boolean trazerContasVencidas;
    private Boolean trazerContasRegistradas;
    private Boolean regerarContaVinculadoContaDestino;
    private List<ContaReceberVO> listaContaReceber;
    private List<SelectItem> listaSelectItemContaCorrente;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private Date dataIni;
    private Date dataFim;
    private String valorConsultaCurso;
    private String campoConsultaCurso;
    private List<CursoVO> listaConsultaCurso;
    private String valorConsultaTurma;
    private String campoConsultaTurma;
    private List<TurmaVO> listaConsultaTurma;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private List<MatriculaVO> listaConsultaAluno;
    private Boolean finalizouProcessamento;
    private Boolean iniciouProcessamento;
    private Integer nrParcelasRegerar;
    private Integer valorAtualProgressBar;
    private ProgressBarVO progressBarVO;
    
    public MudancaCarteiraControle() {
        if (!getUnidadeEnsinoLogado().getCodigo().equals(0)) {
            setUnidadeEnsinoVO(getUnidadeEnsinoLogadoClone());
        }
        inicializarListasSelectItemTodosComboBox();
        setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
        setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
        setFinalizouProcessamento(false);
        setValorAtualProgressBar(0);
        setNrParcelasRegerar(0);
        setFiltroRelatorioFinanceiroVO(new FiltroRelatorioFinanceiroVO(getLoginControle().getPermissaoAcessoMenuVO().getPermitirApenasContasDaBiblioteca()));
        setMarcarTodosTipoOrigemContaReceber(true);
        realizarSelecaoCheckboxMarcarDesmarcarTodosTipoOrigemContaReceber();
    }

    public void novo() {
        removerObjetoMemoria(this);
        if (!getUnidadeEnsinoLogado().getCodigo().equals(0)) {
            setUnidadeEnsinoVO(getUnidadeEnsinoLogadoClone());
        }
        inicializarListasSelectItemTodosComboBox();
        setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
        setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
        setFinalizouProcessamento(false);
        setIniciouProcessamento(false);
        setValorAtualProgressBar(0);
        setNrParcelasRegerar(0);
    }

    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemUnidadeEnsino();
        if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
            montarListaSelectItemContaCorrente();
        }
    }

    public void alterarCarteira() {
        try {
        	limparMensagem();
            validarDados();
            setIniciouProcessamento(false);
            setValorAtualProgressBar(0);
            setNrParcelasRegerar(0);
            if (getTrazerContasVencidas()) {
                
                setListaContaReceber(getFacadeFactory().getContaReceberFacade().consultarPorCursoTurmaMatriculaSituacaoDataVencimento(getMatriculaVO().getMatricula(), getTurmaVO().getCodigo(), getCursoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoAcademicoVO().getCodigo(), "AR", getDataIni(), getDataFim(), getContaCorrenteVO().getCodigo(), getRegerarContaVinculadoContaDestino(), getContaCorrenteOrigemVO().getCodigo(), getFiltroRelatorioFinanceiroVO(), getTrazerContasRegistradas(), false, getUsuarioLogado()));
            } else {
                if (getDataIni().compareTo(new Date()) < 0) {
                    
                    setListaContaReceber(getFacadeFactory().getContaReceberFacade().consultarPorCursoTurmaMatriculaSituacaoDataVencimento(getMatriculaVO().getMatricula(), getTurmaVO().getCodigo(), getCursoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoAcademicoVO().getCodigo(), "AR", new Date(), getDataFim(),  getContaCorrenteVO().getCodigo(), getRegerarContaVinculadoContaDestino(), getContaCorrenteOrigemVO().getCodigo(), getFiltroRelatorioFinanceiroVO(), getTrazerContasRegistradas(), false, getUsuarioLogado()));
                } else {
                    
                    setListaContaReceber(getFacadeFactory().getContaReceberFacade().consultarPorCursoTurmaMatriculaSituacaoDataVencimento(getMatriculaVO().getMatricula(), getTurmaVO().getCodigo(), getCursoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoAcademicoVO().getCodigo(), "AR", getDataIni(), getDataFim(),  getContaCorrenteVO().getCodigo(), getRegerarContaVinculadoContaDestino(), getContaCorrenteOrigemVO().getCodigo(), getFiltroRelatorioFinanceiroVO(), getTrazerContasRegistradas(), false, getUsuarioLogado()));
                }
            }
            if (!getListaContaReceber().isEmpty()) {
                setNrParcelasRegerar(getListaContaReceber().size());
                setIniciouProcessamento(true);
            }else {
            	//setMensagemDetalhada("msg_informe_dados", "Nenhuma CONTA A RECEBER encontrada com os filtros informados.", Uteis.ALERTA);
            }
        } catch (Exception e) {
            setListaContaReceber(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void regerarContasReceberPorContaCorrente() {
    	int qtdeContasAlteradas = 0;
        try {
            if (!getListaContaReceber().isEmpty()) {   
            	ContaCorrenteVO contaCorrenteVO = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getContaCorrenteVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getProgressBarVO().getUsuarioVO());
            	contaCorrenteVO.setNivelMontarDados(NivelMontarDados.TODOS);
                for (ContaReceberVO obj : getListaContaReceber()) {
                    obj.setContaCorrente(contaCorrenteVO.getCodigo());
                    obj.setContaCorrenteVO(contaCorrenteVO);
                    getFacadeFactory().getContaReceberFacade().regerarDadosBoletoMudancaCarteira(obj, getProgressBarVO().getConfiguracaoFinanceiroVO(), getProgressBarVO().getUsuarioVO());
    				getProgressBarVO().incrementar();
    				getProgressBarVO().setStatus(" ( " + getProgressBarVO().getProgresso() + " de " + getProgressBarVO().getMaxValue() + " ) ");
    				qtdeContasAlteradas++;
                }
                setFinalizouProcessamento(true);
                setIniciouProcessamento(false);
                setListaContaReceber(null);
                getProgressBarVO().getSuperControle().setMensagemDetalhada("msg_Matricula_mudancaCarteira", qtdeContasAlteradas+" CONTA(S) A RECEBER foram alteradas.", Uteis.ALERTA);
            }else{
            	getProgressBarVO().getSuperControle().setMensagemDetalhada("msg_informe_dados", "Nenhuma CONTA A RECEBER encontrada com os filtros informados.", Uteis.ALERTA);	
            }
        } catch (Exception e) {
        	getProgressBarVO().setForcarEncerramento(true);
            setListaContaReceber(null);            
            getProgressBarVO().getSuperControle().setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void validarDados() throws Exception {
        if (getUnidadeEnsinoVO().getCodigo().equals(0)) {
            throw new Exception("O campo UNIDADE ENSINO deve ser informado.");
        }
        if (getContaCorrenteVO().getCodigo().equals(0)) {
            throw new Exception("O campo CONTA CORRENTE deve ser informado.");
        }
        if (getDataIni() == null) {
            throw new Exception("O campo DATA INICIAL deve ser informado.");
        }
        if (getDataFim() == null) {
            throw new Exception("O campo DATA FINAL deve ser informado.");
        }
        if (getDataFim().compareTo(getDataIni()) < 0) {
            throw new Exception("O campo DATA INICIAL deve ser maior ou igual a DATA FINAL.");
        }
        if (!getTrazerContasVencidas()) {
            if (getDataFim().compareTo(new Date()) < 0) {
                throw new Exception("O campo DATA FINAL deve ser maior ou igual a DATA ATUAL.");
            }
        }
    }

    public void montarListaSelectItemContaCorrente() {
        try {
            montarListaSelectItemContaCorrente("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectItemContaCorrente(String prm) throws Exception {
        List<ContaCorrenteVO> resultadoConsulta = null;
        Iterator<ContaCorrenteVO> i = null;
        try {
            resultadoConsulta = consultarContaCorrentePorNome(prm);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
                if (Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())) {
              	  objs.add(new SelectItem(obj.getCodigo(),obj.getNomeApresentacaoSistema()));
  			  } else{
                  if (obj.getContaCaixa()) {
                      objs.add(new SelectItem(obj.getCodigo(), "CC:" + obj.getNumero() + "-" + obj.getDigito()));
                  } else {
                      objs.add(new SelectItem(obj.getCodigo(), "Banco:" + obj.getAgencia().getBanco().getNome() + " Ag:" + obj.getAgencia().getNumeroAgencia() + "-" + obj.getAgencia().getDigito() + " CC:"
                              + obj.getNumero() + "-" + obj.getDigito() + " - " + obj.getCarteira()));
                  }
  			  }
            }
            setListaSelectItemContaCorrente(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List<ContaCorrenteVO> consultarContaCorrentePorNome(String nomePrm) throws Exception {
        List<ContaCorrenteVO> lista = getFacadeFactory().getContaCorrenteFacade().consultarPorCodigoSomenteContasCorrente(0, getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    public void consultarCurso() {
        try {
        	setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultar(getCampoConsultaCurso(), getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCurso(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<SelectItem> getTipoConsultaComboCurso() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public void selecionarCurso() {
        try {
            CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
            setCursoVO(obj);
            getListaConsultaCurso().clear();
            this.setValorConsultaCurso("");
            this.setCampoConsultaCurso("");
            if (getUnidadeEnsinoVO().getCodigo().equals(0) && !getCursoVO().getCodigo().equals(0)) {
                montarListaSelectItemUnidadeEnsinoPorCurso();
                if (getListaSelectItemUnidadeEnsino().size() == 1) {
                    getUnidadeEnsinoVO().setCodigo((Integer) getListaSelectItemUnidadeEnsino().get(0).getValue());
                }
            }
            limparDadosTurma();
            limparDadosAluno();
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public void limparDadosCurso() {
        try {
            getListaConsultaCurso().clear();
            setValorConsultaCurso(null);
            setCursoVO(null);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarTurma() {
        try {
            super.consultar();
            List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), getCursoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
        	setListaConsultaTurma(new ArrayList<TurmaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<SelectItem> getTipoConsultaComboTurma() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        return itens;
    }

    public void selecionarTurma() {
        try {
            TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
            setTurmaVO(obj);
            if (getCursoVO().getCodigo().equals(0)) {
                setCursoVO(obj.getCurso());
            }
            if (getUnidadeEnsinoVO().getCodigo().equals(0) && !getCursoVO().getCodigo().equals(0)) {
                montarListaSelectItemUnidadeEnsinoPorCurso();
            }
            if (getUnidadeEnsinoVO().getCodigo().equals(0)) {
                setUnidadeEnsinoVO(obj.getUnidadeEnsino());
            }
            limparDadosAluno();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparDadosTurma() {
        try {
            getListaConsultaTurma().clear();
            setValorConsultaTurma(null);
            setCampoConsultaTurma(null);
            setTurmaVO(null);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarAluno() {
        try {
            super.consultar();
            List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
            if (getCampoConsultaAluno().equals("nome")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoaCursoTurmaUnidadeEnsinoFinanceira(getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), getTurmaVO().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaCursoTurmaUnidadeEnsinoFinanceira(getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), getTurmaVO().getCodigo(), false, getUsuarioLogado());
            }
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
        	setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<SelectItem> getTipoConsultaComboAluno() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        return itens;
    }

    public void selecionarAluno() {
        try {
            MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
            setMatriculaVO(obj);
            getListaConsultaAluno().clear();
            setValorConsultaAluno(null);
            setCampoConsultaAluno(null);
        } catch (Exception e) {
        }
    }

    public void limparDadosAluno() {
        try {
            getListaConsultaAluno().clear();
            setValorConsultaAluno(null);
            setCampoConsultaAluno(null);
            setMatriculaVO(null);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparDadosCursoTurmaAluno() {
        setCursoVO(null);
        setTurmaVO(null);
        setMatriculaVO(null);
        if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
            montarListaSelectItemContaCorrente();
        }
    }

    public void consultarAlunoPorMatricula() {
        try {
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimariaCursoTurma(getMatriculaVO().getMatricula(), getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getUnidadeEnsinoVO().getCodigo()), getUsuarioLogado());
            setMatriculaVO(objAluno);
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setMatriculaVO(new MatriculaVO());
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>QuestionarioUnidadeEnsino</code>.
     */
    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List<UnidadeEnsinoVO> resultadoConsulta = null;
        Iterator<UnidadeEnsinoVO> i = null;
        try {
            if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
                getListaSelectItemUnidadeEnsino().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome().toString()));
                getMatriculaVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
                return;
            }
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            Uteis.liberarListaMemoria(resultadoConsulta);
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>QuestionarioUnidadeEnsino</code>. Buscando
     * todos os objetos correspondentes a entidade <code>Questionario</code>. Esta rotina não recebe parâmetros para
     * filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
     * requisições Ajax.
     */
    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectItemUnidadeEnsinoPorCurso() {
        List<UnidadeEnsinoVO> resultadoConsulta = null;
        Iterator<UnidadeEnsinoVO> i = null;
        try {
            resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorCodigoCurso(getCursoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            Uteis.liberarListaMemoria(resultadoConsulta);
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;

        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String descricaoPrm) throws Exception {
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(descricaoPrm, this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }
    
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
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

    public String getCampoConsultaTurma() {
        if (campoConsultaTurma == null) {
            campoConsultaTurma = "";
        }
        return campoConsultaTurma;
    }

    public void setCampoConsultaTurma(String campoConsultaTurma) {
        this.campoConsultaTurma = campoConsultaTurma;
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
            listaConsultaTurma = new ArrayList<TurmaVO>(0);
        }
        return listaConsultaTurma;
    }

    public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
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

    public String getValorConsultaTurma() {
        if (valorConsultaTurma == null) {
            valorConsultaTurma = "";
        }
        return valorConsultaTurma;
    }

    public void setValorConsultaTurma(String valorConsultaTurma) {
        this.valorConsultaTurma = valorConsultaTurma;
    }

    public Date getDataIni() {
        if (dataIni == null) {
            dataIni = new Date();
        }
        return dataIni;
    }

    public void setDataIni(Date dataIni) {
        this.dataIni = dataIni;
    }

    public Date getDataFim() {
        if (dataFim == null) {
            dataFim = new Date();
        }
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
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

    public List<MatriculaVO> getListaConsultaAluno() {
        if (listaConsultaAluno == null) {
            listaConsultaAluno = new ArrayList<MatriculaVO>(0);
        }
        return listaConsultaAluno;
    }

    public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    public List<ContaReceberVO> getListaContaReceber() {
        if (listaContaReceber == null) {
            listaContaReceber = new ArrayList<ContaReceberVO>(0);
        }
        return listaContaReceber;
    }

    public void setListaContaReceber(List<ContaReceberVO> listaContaReceber) {
        this.listaContaReceber = listaContaReceber;
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

    public CursoVO getCursoVO() {
        if (cursoVO == null) {
            cursoVO = new CursoVO();
        }
        return cursoVO;
    }

    public void setCursoVO(CursoVO cursoVO) {
        this.cursoVO = cursoVO;
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

    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
        if (unidadeEnsinoVO == null) {
            unidadeEnsinoVO = new UnidadeEnsinoVO();
        }
        return unidadeEnsinoVO;
    }

    public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
        this.unidadeEnsinoVO = unidadeEnsinoVO;
    }

    public ContaCorrenteVO getContaCorrenteVO() {
        if (contaCorrenteVO == null) {
            contaCorrenteVO = new ContaCorrenteVO();
        }
        return contaCorrenteVO;
    }

    public void setContaCorrenteVO(ContaCorrenteVO contaCorrenteVO) {
        this.contaCorrenteVO = contaCorrenteVO;
    }

    public List<SelectItem> getListaSelectItemContaCorrente() {
        if (listaSelectItemContaCorrente == null) {
            listaSelectItemContaCorrente = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemContaCorrente;
    }

    public void setListaSelectItemContaCorrente(List<SelectItem> listaSelectItemContaCorrente) {
        this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
    }

    public Boolean getTrazerContasVencidas() {
        if (trazerContasVencidas == null) {
            trazerContasVencidas = false;
        }
        return trazerContasVencidas;
    }

    public void setTrazerContasVencidas(Boolean trazerContasVencidas) {
        this.trazerContasVencidas = trazerContasVencidas;
    }

    public Boolean getTrazerContasRegistradas() {
    	if (trazerContasRegistradas == null) {
    		trazerContasRegistradas = false;
    	}
    	return trazerContasRegistradas;
    }
    
    public void setTrazerContasRegistradas(Boolean trazerContasRegistradas) {
    	this.trazerContasRegistradas = trazerContasRegistradas;
    }
    
    public Boolean getFinalizouProcessamento() {
        if (finalizouProcessamento == null) {
            finalizouProcessamento = false;
        }
        return finalizouProcessamento;
    }

    public void setFinalizouProcessamento(Boolean finalizouProcessamento) {
        this.finalizouProcessamento = finalizouProcessamento;
    }

    public Integer getValorAtualProgressBar() {
        return valorAtualProgressBar;
    }

    public void setValorAtualProgressBar(Integer valorAtualProgressBar) {
        this.valorAtualProgressBar = valorAtualProgressBar;
    }

    public Integer getNrParcelasRegerar() {
        return nrParcelasRegerar;
    }

    public void setNrParcelasRegerar(Integer nrParcelasRegerar) {
        this.nrParcelasRegerar = nrParcelasRegerar;
    }    

	public Boolean getIniciouProcessamento() {
        if (iniciouProcessamento == null) {
            iniciouProcessamento = false;
        }
        return iniciouProcessamento;
    }

    public void setIniciouProcessamento(Boolean iniciouProcessamento) {
        this.iniciouProcessamento = iniciouProcessamento;
    }

    public boolean getIsExisteUnidadeEnsinoLogada() {
        return (!getUnidadeEnsinoLogado().getCodigo().equals(0));
    }

    public boolean getIsUnidadeEnsinoSelecionada() {
        return (!getUnidadeEnsinoVO().getCodigo().equals(0));
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

	public void executarInicioProgressBar(String metodo) {
		try {
			setProgressBarVO(new ProgressBarVO());
			getProgressBarVO().resetar();
			getProgressBarVO().setSuperControle(this);
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarVO().setConfiguracaoFinanceiroVO(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getUnidadeEnsinoVO().getCodigo()));
			if(getNrParcelasRegerar() <= 0) {
				setMensagemDetalhada("msg_informe_dados", "Nenhuma CONTA A RECEBER encontrada com os filtros informados.", Uteis.ALERTA);
			}
			getProgressBarVO().iniciar(0l, getNrParcelasRegerar(), "( 1 de " + getNrParcelasRegerar() + " ) ", true, this, metodo);
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public ContaCorrenteVO getContaCorrenteOrigemVO() {
		if(contaCorrenteOrigemVO == null) {
			contaCorrenteOrigemVO =  new ContaCorrenteVO();
		}
		return contaCorrenteOrigemVO;
	}

	public void setContaCorrenteOrigemVO(ContaCorrenteVO contaCorrenteOrigemVO) {
		this.contaCorrenteOrigemVO = contaCorrenteOrigemVO;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoAcademicoVO() {
		if(unidadeEnsinoAcademicoVO == null) {
			unidadeEnsinoAcademicoVO =  new UnidadeEnsinoVO();
		}
		return unidadeEnsinoAcademicoVO;
	}

	public void setUnidadeEnsinoAcademicoVO(UnidadeEnsinoVO unidadeEnsinoAcademicoVO) {
		this.unidadeEnsinoAcademicoVO = unidadeEnsinoAcademicoVO;
	}

	public Boolean getRegerarContaVinculadoContaDestino() {
		if(regerarContaVinculadoContaDestino == null) {
			regerarContaVinculadoContaDestino =  false;
		}
		return regerarContaVinculadoContaDestino;
	}

	public void setRegerarContaVinculadoContaDestino(Boolean regerarContaVinculadoContaDestino) {
		this.regerarContaVinculadoContaDestino = regerarContaVinculadoContaDestino;
	}
	
	
}
