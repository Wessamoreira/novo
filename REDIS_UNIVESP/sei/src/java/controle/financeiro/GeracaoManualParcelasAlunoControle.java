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
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoFinanceiroCursoVO;
import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.MatriculaPeriodoVencimentoVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.academico.MatriculaPeriodo;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("GeracaoManualParcelasAlunoControle")
@Scope("viewScope")
@Lazy
public class GeracaoManualParcelasAlunoControle extends SuperControle implements Serializable {

    private MatriculaVO matriculaVO;
    private String valorConsultaAluno;
    private List<MatriculaVO> listaConsultaAluno;
    private String campoConsultaAluno;
    private List<MatriculaVO> matriculaVOs;
    private Integer valorAtualProgressBar;
    private Integer valorAtualProgressBarAluno;
    private boolean progressBarAtivo;
    private boolean progressBarAtivoAluno;
    private UnidadeEnsinoVO unidadeEnsino;
    private UnidadeEnsinoVO unidadeEnsinoAluno;
    private List<UnidadeEnsinoVO> listaSelectItemUnidadeEnsino;
    private String mesReferencia;
    private String semestreReferencia;
    private String anoReferencia;
    private String mesReferenciaAluno;
    private String anoReferenciaAluno;
    private Integer nrParcelasGerar;
    private Integer nrParcelasGerarAluno;
    private Double valorTotalParcelasGerar;
    private List<MatriculaPeriodoVencimentoVO> listaMatriculaPeriodoVencimentoGerar;
    private Boolean finalizouProcessamento;
    private Boolean finalizouProcessamentoAluno;
    private Integer nrItensComErroProcessamento;
    private List<MatriculaPeriodoVO> listaProcessarVcto;
    // ATRIBUTOS UTILIZADOS PARA GERACAO DO OBJETO MATRICULAPERIODOVENCIMENTO
    // ESTA GERACAO VIA PROCESSAMENTO É NECESSARIO QUANDO SE IMPORTA DADOS
    // DE SISTEMAS LEGADOS, EVITANDO ASSIM QUE O USUARIO TENHA QUE EDITAR
    // UM A UM TODAS AS MATRICULAS PARA GERAR A LISTA MATRICULAPERIODOVENCIMENTO
    // CORRESPONDENTGE AO CURSO.
    private String parcelaGerar;
    private Date dataVencimentoGerarParcela;
    private Boolean telaEmUso;
    private boolean trazerMatriculasComCanceladoFinanceiro = false;

    public GeracaoManualParcelasAlunoControle() {
        inicializarControlador();
    }

    @PostConstruct
    private void inicializarControlador() {
        setMatriculaVO(new MatriculaVO());
        setUnidadeEnsino(new UnidadeEnsinoVO());
        setUnidadeEnsinoAluno(new UnidadeEnsinoVO());
        montarListaSelectItemUnidadeEnsino();
        finalizouProcessamento = true;
        setFinalizouProcessamentoAluno((Boolean) false);
        setNrItensComErroProcessamento((Integer) 0);
        setSemestreReferencia(Uteis.getSemestreAtual());
//        mesReferencia = String.valueOf(Uteis.getMesDataAtual());
//        if (mesReferencia.length() == 1) {
//            mesReferencia = "0" + mesReferencia;
//        }
//        anoReferencia = String.valueOf(Uteis.getAnoDataAtual());
//        if (anoReferencia.length() == 2) {
//            anoReferencia = "20" + anoReferencia;
//        }
//        mesReferenciaAluno = String.valueOf(Uteis.getMesDataAtual());
//        if (mesReferenciaAluno.length() == 1) {
//            mesReferenciaAluno = "0" + mesReferenciaAluno;
//        }
//        anoReferenciaAluno = String.valueOf(Uteis.getAnoDataAtual());
//        if (anoReferenciaAluno.length() == 2) {
//            anoReferenciaAluno = "20" + anoReferenciaAluno;
//        }

        nrParcelasGerar = 0;
        nrParcelasGerarAluno = 0;
        valorTotalParcelasGerar = 0.0;
        valorAtualProgressBar = 0;
        valorAtualProgressBarAluno = 0;
        listaMatriculaPeriodoVencimentoGerar = new ArrayList<MatriculaPeriodoVencimentoVO>(0);
        listaProcessarVcto = new ArrayList<MatriculaPeriodoVO>(0);
        progressBarAtivo = false;
        setProgressBarAtivoAluno(false);

        parcelaGerar = "3/5";
        try {
            dataVencimentoGerarParcela = Uteis.getDate("30/10/2010");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void novo() {
    }

    public void consultarAluno() {
        try {
            List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);

            if (getValorConsultaAluno().equals("")) {
                throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getValorConsultaAluno(), this.getUnidadeEnsinoAluno().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
                if (!obj.getMatricula().equals("")) {
                    objs.add(obj);
                }
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoAluno().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoAluno().getCodigo(), false, getUsuarioLogado());
            }

            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparDadosAluno() throws Exception {
        setMatriculaVO(new MatriculaVO());
        setMensagemID("msg_entre_dados");
    }

    public void consultarAlunoPorMatricula() {
        try {
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatriculaVO().getMatricula(), getUnidadeEnsinoAluno().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
			if(objAluno.getBloqueioPorSolicitacaoLiberacaoMatricula()) {
				throw new Exception(getMensagemInternalizacao("msg_RenovarMatricula_avisoMatriculaSuspensa"));
			}
            verificaSituacaoAluno(objAluno);
            setUnidadeEnsino(objAluno.getUnidadeEnsino());
            getUnidadeEnsinoAluno().setCodigo(objAluno.getUnidadeEnsino().getCodigo());
            setMatriculaVO(objAluno);
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setMatriculaVO(new MatriculaVO());
        }
    }    

    public void selecionarAluno() throws Exception {
        try {
            MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
            MatriculaVO objCompleto = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), getUnidadeEnsinoAluno().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
			if(objCompleto.getBloqueioPorSolicitacaoLiberacaoMatricula()) {
				throw new Exception(getMensagemInternalizacao("msg_RenovarMatricula_avisoMatriculaSuspensa"));
			}
            verificaSituacaoAluno(objCompleto);
            setMatriculaVO(objCompleto);
            setUnidadeEnsino(objCompleto.getUnidadeEnsino());
            getUnidadeEnsinoAluno().setCodigo(objCompleto.getUnidadeEnsino().getCodigo());
            obj = null;
            objCompleto = null;
            setValorConsultaAluno("");
            setCampoConsultaAluno("");
            getListaConsultaAluno().clear();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setMatriculaVO(new MatriculaVO());
        }
    }
    
    public void verificaSituacaoAluno(MatriculaVO obj) throws Exception {
    	if ((!isTrazerMatriculasComCanceladoFinanceiro() && obj.getCanceladoFinanceiro()) || obj.getSituacao().equals("CA") || obj.getSituacao().equals("TR")) {
            throw new Exception("Esse aluno está trancado, cancelado ou cancelado financeiramente. Não é possível gerar as parcelas!");
        }
    	if (obj.getSituacao().equalsIgnoreCase("CA") || obj.getSituacao().equalsIgnoreCase("TR") || obj.getSituacao().equalsIgnoreCase("JU")
                || obj.getSituacao().equalsIgnoreCase("CF") || obj.getSituacao().equalsIgnoreCase("TS") || obj.getSituacao().equalsIgnoreCase("ER")
                || obj.getSituacao().equalsIgnoreCase("IN") || obj.getSituacao().equalsIgnoreCase("DE") //|| obj.getSituacao().equalsIgnoreCase("PR")
                || obj.getSituacao().equalsIgnoreCase("AC")) {
            throw new Exception("O Aluno selecionado não encontra-se com situação ATIVA, sendo assim não é possível gerar parcela para o mesmo!");
        }
    }

    public List<SelectItem> getTipoConsultaComboAluno() {
        List<SelectItem> itens = new ArrayList<SelectItem>();
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public void realizarLimpezaMatricula() {
        setMatriculaVO(new MatriculaVO());
    }

    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
    }

    public MatriculaVO getMatriculaVO() {
        return matriculaVO;
    }

    public void setValorConsultaAluno(String valorConsultaAluno) {
        this.valorConsultaAluno = valorConsultaAluno;
    }

    public String getValorConsultaAluno() {
        return valorConsultaAluno;
    }

    public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    public List<MatriculaVO> getListaConsultaAluno() {
        return listaConsultaAluno;
    }

    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
    }

    public String getCampoConsultaAluno() {
        return campoConsultaAluno;
    }

    public void setMatriculaVOs(List<MatriculaVO> matriculaVOs) {
        this.matriculaVOs = matriculaVOs;
    }

    public List<MatriculaVO> getMatriculaVOs() {
        return matriculaVOs;
    }

    public void setValorAtualProgressBar(int valorAtualProgressBar) {
        this.valorAtualProgressBar = valorAtualProgressBar;
    }

    public Integer getValorAtualProgressBar() {
        return valorAtualProgressBar;
    }

    public void setProgressBarAtivo(boolean progressBarAtivo) {
        this.progressBarAtivo = progressBarAtivo;
    }

    public boolean isProgressBarAtivo() {
        return progressBarAtivo;
    }

    /**
     * @return the unidadeEnsino
     */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        return unidadeEnsino;
    }

    /**
     * @param unidadeEnsino
     *            the unidadeEnsino to set
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    /**
     * @return the listaSelectItemUnidadeEnsino
     */
    public List<UnidadeEnsinoVO> getListaSelectItemUnidadeEnsino() {
        return listaSelectItemUnidadeEnsino;
    }

    /**
     * @param listaSelectItemUnidadeEnsino
     *            the listaSelectItemUnidadeEnsino to set
     */
    public void setListaSelectItemUnidadeEnsino(List<UnidadeEnsinoVO> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            ordenador = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * @return the mesReferencia
     */
    public String getMesReferencia() {
        return mesReferencia;
    }

    /**
     * @param mesReferencia
     *            the mesReferencia to set
     */
    public void setMesReferencia(String mesReferencia) {
        this.mesReferencia = mesReferencia;
    }

    /**
     * @return the mesReferencia
     */
    public String getMesReferenciaAluno() {
        return mesReferenciaAluno;
    }

    /**
     * @param mesReferencia
     *            the mesReferencia to set
     */
    public void setMesReferenciaAluno(String mesReferenciaAluno) {
        this.mesReferenciaAluno = mesReferenciaAluno;
    }

    /**
     * @return the anoReferencia
     */
    public String getAnoReferencia() {
        return anoReferencia;
    }

    /**
     * @param anoReferencia
     *            the anoReferencia to set
     */
    public void setAnoReferencia(String anoReferencia) {
        this.anoReferencia = anoReferencia;
    }

    /**
     * @return the anoReferencia
     */
    public String getAnoReferenciaAluno() {
        return anoReferenciaAluno;
    }

    /**
     * @param anoReferencia
     *            the anoReferencia to set
     */
    public void setAnoReferenciaAluno(String anoReferenciaAluno) {
        this.anoReferenciaAluno = anoReferenciaAluno;
    }

    /**
     * @return the nrParcelasGerar
     */
    public Integer getNrParcelasGerar() {
        return nrParcelasGerar;
    }

    /**
     * @param nrParcelasGerar
     *            the nrParcelasGerar to set
     */
    public void setNrParcelasGerar(Integer nrParcelasGerar) {
        this.nrParcelasGerar = nrParcelasGerar;
    }

    /**
     * @return the valorTotalParcelasGerar
     */
    public Double getValorTotalParcelasGerar() {
        return valorTotalParcelasGerar;
    }

    /**
     * @param valorTotalParcelasGerar
     *            the valorTotalParcelasGerar to set
     */
    public void setValorTotalParcelasGerar(Double valorTotalParcelasGerar) {
        this.valorTotalParcelasGerar = valorTotalParcelasGerar;
    }

    /**
     * @return the listaMatriculaPeriodoVencimentoGerar
     */
    public List<MatriculaPeriodoVencimentoVO> getListaMatriculaPeriodoVencimentoGerar() {
        return listaMatriculaPeriodoVencimentoGerar;
    }

    /**
     * @param listaMatriculaPeriodoVencimentoGerar
     *            the listaMatriculaPeriodoVencimentoGerar to set
     */
    public void setListaMatriculaPeriodoVencimentoGerar(List<MatriculaPeriodoVencimentoVO> listaMatriculaPeriodoVencimentoGerar) {
        this.listaMatriculaPeriodoVencimentoGerar = listaMatriculaPeriodoVencimentoGerar;
    }

    public void localizarParcelasParaGeracaoPorUnidade() {
        try {
            if (mesReferencia.equals("")) {
                throw new Exception("O mês de referência deve ser informado!");
            }
            if (anoReferencia.equals("")) {
                throw new Exception("O ano de referência deve ser informado!");
            }
            if (unidadeEnsino.getCodigo().equals(0)) {
                throw new Exception("A Unidade de Ensino deve ser informada!");
            }
            List<MatriculaPeriodoVencimentoVO> listaProcessar = getFacadeFactory().getMatriculaPeriodoVencimentoFacade().consultarPorMesReferenciaSituacaoUnidade(mesReferencia, anoReferencia, "NG",
                    unidadeEnsino.getCodigo(), null, null, getConfiguracaoFinanceiroPadraoSistema().getPermitirGerarParcelaPreMatricula(), isTrazerMatriculasComCanceladoFinanceiro(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), 0, 0, false);
            atualizarListaMatriculaPeriodoVencimentoVOsGerar(listaProcessar);
            setMensagemDetalhada("msg_Matricula_consultarParcelaGerarFinanceiro", "Foram encontradas " + listaProcessar.size() + " parcelas pendentes para geração no MÊS " + mesReferencia + "/"
                    + anoReferencia);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void localizarParcelasParaGeracaoPorAluno() throws Exception {
        if (unidadeEnsinoAluno.getCodigo().equals(0)) {
            throw new Exception("A Unidade de Ensino deve ser informada!");
        }
        if (getMatriculaVO().getMatricula().equals("")) {
            throw new Exception("A Matrícula do aluno deve ser informada!");
        }
        List<MatriculaPeriodoVencimentoVO> listaProcessar = getFacadeFactory().getMatriculaPeriodoVencimentoFacade().consultarPorMesReferenciaSituacaoUnidadeAluno(mesReferenciaAluno,
                anoReferenciaAluno, "NG", getMatriculaVO().getMatricula(), unidadeEnsinoAluno.getCodigo(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getUnidadeEnsinoAluno().getCodigo()).getPermitirGerarParcelaPreMatricula(), isTrazerMatriculasComCanceladoFinanceiro(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getUnidadeEnsinoAluno().getCodigo()), getUsuarioLogado());
        atualizarListaMatriculaPeriodoVencimentoVOsGerarAluno(listaProcessar);
        if (Uteis.isAtributoPreenchido(listaProcessar) && Uteis.isAtributoPreenchido(mesReferenciaAluno) && Uteis.isAtributoPreenchido(anoReferenciaAluno)) {
        	 setMensagemDetalhada("msg_Matricula_consultarParcelaGerarFinanceiro", "Foram encontradas " + listaProcessar.size() + " parcelas pendentes para geração no MÊS " + mesReferenciaAluno + "/" + anoReferenciaAluno);
        	return;
        }
        setMensagemDetalhada("msg_Matricula_consultarParcelaGerarFinanceiro", "Foram encontradas " + listaProcessar.size() + " parcelas pendentes para geração");
    }

    public void gerarParcelaMatriculaPeriodoVencimentoVOEspecifica(MatriculaPeriodoVencimentoVO vcto) throws Exception {
        try {
            // Como esta rotina trabalha com base em uma consulta a um matriculaPeriodoVencimentoVO
            // específico (ou seja de uma determinado mes), então temos que adicionar este objeto.
            // para dentro da matriculaperiodo (de onde o mesmo é referenciado pela rotina de geração).
            vcto.getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoVOs().add(vcto);
//            getFacadeFactory().getMatriculaPeriodoFacade().processarGeracaoContasReceberMesReferenciaEspecifica(vcto.getMatriculaPeriodoVO().getMatriculaVO(), vcto.getMatriculaPeriodoVO(),
//                    getMesReferencia() + "/" + getAnoReferencia(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            MatriculaPeriodoVencimentoVO mpvRegerar = new MatriculaPeriodoVencimentoVO();
            mpvRegerar.setParcela(vcto.getParcela());
            mpvRegerar.setTipoOrigemMatriculaPeriodoVencimento(vcto.getTipoOrigemMatriculaPeriodoVencimento());
            mpvRegerar.setUsaValorPrimeiraParcela(vcto.isUsaValorPrimeiraParcela());
        	if (mpvRegerar.getParcela().contains("/")) {
        		mpvRegerar.setParcela(mpvRegerar.getParcela().substring(0,mpvRegerar.getParcela().indexOf("/")));
        	}            
            getFacadeFactory().getMatriculaPeriodoFacade().processarGeracaoContasReceberMesReferenciaEspecifica(vcto.getMatriculaPeriodoVO().getMatriculaVO(), vcto.getMatriculaPeriodoVO(), mpvRegerar, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), false);
            vcto.setControleGeracaoParcelaMesAMes("OK");
            vcto.setErroGeracaoParcelaMesAMes("");
        } catch (Exception e) {
            vcto.setControleGeracaoParcelaMesAMes("ERRO");
            vcto.setErroGeracaoParcelaMesAMes(e.getMessage());
        }
    }

    public void gerarParcelaMatriculaPeriodoVencimentoVOEspecificaAluno(MatriculaPeriodoVencimentoVO vcto) throws Exception {
        // Como esta rotina trabalha com base em uma consulta a um matriculaPeriodoVencimentoVO
        // específico (ou seja de uma determinado mes), então temos que adicionar este objeto.
        // para dentro da matriculaperiodo (de onde o mesmo é referenciado pela rotina de geração).
        vcto.getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoVOs().add(vcto);
//        getFacadeFactory().getMatriculaPeriodoFacade().processarGeracaoContasReceberMesReferenciaEspecifica(vcto.getMatriculaPeriodoVO().getMatriculaVO(), vcto.getMatriculaPeriodoVO(),
//                Uteis.getMesReferenciaData(vcto.getDataVencimento()), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
    	
        MatriculaPeriodoVencimentoVO mpvRegerar = new MatriculaPeriodoVencimentoVO();
        mpvRegerar.setParcela(vcto.getParcela());
        mpvRegerar.setTipoOrigemMatriculaPeriodoVencimento(vcto.getTipoOrigemMatriculaPeriodoVencimento());
        mpvRegerar.setUsaValorPrimeiraParcela(vcto.isUsaValorPrimeiraParcela());
    	if (mpvRegerar.getParcela().contains("/")) {
    		mpvRegerar.setParcela(mpvRegerar.getParcela().substring(0,mpvRegerar.getParcela().indexOf("/")));
    	}
        getFacadeFactory().getMatriculaPeriodoFacade().processarGeracaoContasReceberMesReferenciaEspecifica(vcto.getMatriculaPeriodoVO().getMatriculaVO(), vcto.getMatriculaPeriodoVO(), mpvRegerar, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), false);
        vcto.setControleGeracaoParcelaMesAMes("OK");
        vcto.setErroGeracaoParcelaMesAMes("");
    }

    public Boolean getApresentarBotaoGerarParcelas() {
        if ((listaMatriculaPeriodoVencimentoGerar != null) && (listaMatriculaPeriodoVencimentoGerar.size() > 0)) {
            return true;
        }
        return false;
    }

    public void atualizarListaMatriculaPeriodoVencimentoVOsGerar(List<MatriculaPeriodoVencimentoVO> listaMatriculaPeriodoVencimentoGerarPrm) {
        progressBarAtivo = false;
        finalizouProcessamento = false;
        valorAtualProgressBar = -1;
        setNrItensComErroProcessamento((Integer) 0);
        listaMatriculaPeriodoVencimentoGerar = listaMatriculaPeriodoVencimentoGerarPrm;
        nrParcelasGerar = listaMatriculaPeriodoVencimentoGerar.size();
        valorTotalParcelasGerar = 0.0;
        for (MatriculaPeriodoVencimentoVO vcto : listaMatriculaPeriodoVencimentoGerar) {
            valorTotalParcelasGerar += vcto.getValor();
        }
        valorTotalParcelasGerar = Uteis.arrendondarForcando2CadasDecimais(valorTotalParcelasGerar);
    }

    public void atualizarListaMatriculaPeriodoVencimentoVOsGerarAluno(List<MatriculaPeriodoVencimentoVO> listaMatriculaPeriodoVencimentoGerarPrm) {
        progressBarAtivoAluno = false;
        finalizouProcessamentoAluno = false;
        valorAtualProgressBarAluno = -1;
        setNrItensComErroProcessamento((Integer) 0);
        listaMatriculaPeriodoVencimentoGerar = listaMatriculaPeriodoVencimentoGerarPrm;
        nrParcelasGerarAluno = listaMatriculaPeriodoVencimentoGerar.size();
    }

    /**
     * @return the finalizouProcessamento
     */
    public Boolean getFinalizouProcessamento() {
        return finalizouProcessamento;
    }

    /**
     * @param finalizouProcessamento
     *            the finalizouProcessamento to set
     */
    public void setFinalizouProcessamento(Boolean finalizouProcessamento) {
        this.finalizouProcessamento = finalizouProcessamento;
    }

    private void prepararListaMatriculaPeriodoVencimentoApresentacaoResultadoFinal(List<MatriculaPeriodoVencimentoVO> listaMatriculaPeriodoVencimentoGerar) {
        setNrItensComErroProcessamento((Integer) 0);
        for (MatriculaPeriodoVencimentoVO vcto : listaMatriculaPeriodoVencimentoGerar) {
            if (vcto.getErroGeracaoParcelaMesAMes().equals("ERRO")) {
                setNrItensComErroProcessamento((Integer) (getNrItensComErroProcessamento() + 1));
            }
        }
        Ordenacao.ordenarLista(listaMatriculaPeriodoVencimentoGerar, "baseOrdenacao");
    }

    /**
     * @return the nrItensComErroProcessamento
     */
    public Integer getNrItensComErroProcessamento() {
        return nrItensComErroProcessamento;
    }

    /**
     * @param nrItensComErroProcessamento
     *            the nrItensComErroProcessamento to set
     */
    public void setNrItensComErroProcessamento(Integer nrItensComErroProcessamento) {
        this.nrItensComErroProcessamento = nrItensComErroProcessamento;
    }

    /**
     * @return the parcelaGerar
     */
    public String getParcelaGerar() {
        return parcelaGerar;
    }

    /**
     * @param parcelaGerar
     *            the parcelaGerar to set
     */
    public void setParcelaGerar(String parcelaGerar) {
        this.parcelaGerar = parcelaGerar;
    }

    /**
     * @return the dataVencimentoGerarParcela
     */
    public Date getDataVencimentoGerarParcela() {
        return dataVencimentoGerarParcela;
    }

    /**
     * @param dataVencimentoGerarParcela
     *            the dataVencimentoGerarParcela to set
     */
    public void setDataVencimentoGerarParcela(Date dataVencimentoGerarParcela) {
        this.dataVencimentoGerarParcela = dataVencimentoGerarParcela;
    }

    public synchronized void gerarParcelas() {
        try {

            if (getTelaEmUso().equals(Boolean.FALSE)) {
                setTelaEmUso(Boolean.TRUE);

                progressBarAtivo = true;
//                ConfiguracaoFinanceiroVO configuracaoFinanceiro = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), null);
                //ConfiguracaoFinanceiroVO configuracaoFinanceiro = getConfiguracaoFinanceiroPadraoSistema();
                ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVOCarregado = null;
                PlanoFinanceiroCursoVO planoFinanceiroCursoVOCarregado = null;
                CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVOCarregado = null;
                Integer unidadeEnsinoCursoCodigoAtual = null;

                for (MatriculaPeriodoVencimentoVO vcto : listaMatriculaPeriodoVencimentoGerar) {
                    // INICIALIZANDO A CONFIGURACAO FINANCEIRO, PARA EVITAR QUE ESTA CONSULTA SEJA REALIZADA
                    // NOVAMENTE, DE FORMA REPETIDA PARA CADA ALUNO.
                    vcto.getMatriculaPeriodoVO().setConfiguracaoFinanceiro(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(vcto.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()));
                    vcto.getMatriculaPeriodoVO().getConfiguracaoFinanceiro().setNivelMontarDados(NivelMontarDados.TODOS);
                    // INICIALIZANDO O PROCESSO MATRICULA, PARA GERACAO DA CONTA A RECEBER
                    // VALE RESSALTAR QUE A ROTINA TRABALHA COM O CONCEITO DE ORDENAR OS VCTOS POR UNIDADEENSINOCURSO /
                    // PROCESSOMATRICULA, DE FORMA QUE TODOS OS VCTO QUE COMPARTILHAM ESTAS INFORMACOES SEJAM PROCESSADOS
                    // CONJUNTAMENTE. EVITANDO ASSIM QUE ESTE DADO TENHA QUE SER REPROCESSADO DIVERSAS VEZES
                    if ((processoMatriculaCalendarioVOCarregado == null) || (!unidadeEnsinoCursoCodigoAtual.equals(vcto.getMatriculaPeriodoVO().getUnidadeEnsinoCurso()))) {
                        getFacadeFactory().getMatriculaPeriodoFacade().montarDadosProcessoMatriculaCalendarioVO(vcto.getMatriculaPeriodoVO().getMatriculaVO(), vcto.getMatriculaPeriodoVO(),
                                NivelMontarDados.TODOS, getUsuarioLogado());
                        processoMatriculaCalendarioVOCarregado = vcto.getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO();
                        unidadeEnsinoCursoCodigoAtual = vcto.getMatriculaPeriodoVO().getUnidadeEnsinoCursoVO().getCodigo();
                    } else {
                        vcto.getMatriculaPeriodoVO().setProcessoMatriculaCalendarioVO(processoMatriculaCalendarioVOCarregado);
                        vcto.getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO().setNivelMontarDados(NivelMontarDados.TODOS);
                    }
                    // INICIALIZANDO O PLANO FINANCEIRO DO CURSO;
                    if ((planoFinanceiroCursoVOCarregado == null) || (!planoFinanceiroCursoVOCarregado.getCodigo().equals(vcto.getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getCodigo()))) {
                        MatriculaPeriodo.montarDadosPlanoFinanceiroCurso(vcto.getMatriculaPeriodoVO(), NivelMontarDados.TODOS, getUsuarioLogado());
                        planoFinanceiroCursoVOCarregado = vcto.getMatriculaPeriodoVO().getPlanoFinanceiroCurso();
                    } else {
                        vcto.getMatriculaPeriodoVO().setPlanoFinanceiroCurso(planoFinanceiroCursoVOCarregado);
                        vcto.getMatriculaPeriodoVO().getPlanoFinanceiroCurso().setNivelMontarDados(NivelMontarDados.TODOS);
                    }
                    // INICIALIZANDO CONDICAO PAGAMENTO PLANO FINANCEIRO CURSO
                    if ((condicaoPagamentoPlanoFinanceiroCursoVOCarregado == null)
                            || (!condicaoPagamentoPlanoFinanceiroCursoVOCarregado.getCodigo().equals(vcto.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo()))) {
                        MatriculaPeriodo.montarDadosCondicaoPagamentoPlanoFinanceiroCurso(vcto.getMatriculaPeriodoVO(), NivelMontarDados.TODOS, getUsuarioLogado());
                        condicaoPagamentoPlanoFinanceiroCursoVOCarregado = vcto.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso();
                    } else {
                        vcto.getMatriculaPeriodoVO().setCondicaoPagamentoPlanoFinanceiroCurso(condicaoPagamentoPlanoFinanceiroCursoVOCarregado);
                        vcto.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().setNivelMontarDados(NivelMontarDados.TODOS);
                    }
                    gerarParcelaMatriculaPeriodoVencimentoVOEspecifica(vcto);
                    valorAtualProgressBar++;
                }
                progressBarAtivo = false;
                finalizouProcessamento = true;
                prepararListaMatriculaPeriodoVencimentoApresentacaoResultadoFinal(listaMatriculaPeriodoVencimentoGerar);
                setMensagemID("msg_Matricula_gerarParcelaFinanceira");

                setTelaEmUso(Boolean.FALSE);

            } else {
                throw new Exception("A tela está sendo usada por outro usuário. Aguarde!");
            }

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setTelaEmUso(Boolean.FALSE);
        }
    }

    public synchronized void gerarParcelasAluno() {
        try {
            localizarParcelasParaGeracaoPorAluno();
            setProgressBarAtivoAluno(true);
            ConfiguracaoFinanceiroVO configuracaoFinanceiro = getFacadeFactory().getConfiguracaoFinanceiroFacade().verificarConfiguracaoUtilizarGeracaoParcela(getUnidadeEnsinoAluno(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getUnidadeEnsinoAluno().getCodigo()), getUsuarioLogado());
            ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVOCarregado = null;
            PlanoFinanceiroCursoVO planoFinanceiroCursoVOCarregado = null;
            CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVOCarregado = null;
            Integer unidadeEnsinoCursoCodigoAtual = null;

            for (MatriculaPeriodoVencimentoVO vcto : listaMatriculaPeriodoVencimentoGerar) {
                // INICIALIZANDO A CONFIGURACAO FINANCEIRO, PARA EVITAR QUE ESTA CONSULTA SEJA REALIZADA
                // NOVAMENTE, DE FORMA REPETIDA PARA CADA ALUNO.
                vcto.getMatriculaPeriodoVO().setConfiguracaoFinanceiro(configuracaoFinanceiro);
                vcto.getMatriculaPeriodoVO().getConfiguracaoFinanceiro().setNivelMontarDados(NivelMontarDados.TODOS);
                // INICIALIZANDO O PROCESSO MATRICULA, PARA GERACAO DA CONTA A RECEBER
                // VALE RESSALTAR QUE A ROTINA TRABALHA COM O CONCEITO DE ORDENAR OS VCTOS POR UNIDADEENSINOCURSO /
                // PROCESSOMATRICULA, DE FORMA QUE TODOS OS VCTO QUE COMPARTILHAM ESTAS INFORMACOES SEJAM PROCESSADOS
                // CONJUNTAMENTE. EVITANDO ASSIM QUE ESTE DADO TENHA QUE SER REPROCESSADO DIVERSAS VEZES
                if ((processoMatriculaCalendarioVOCarregado == null) || (!unidadeEnsinoCursoCodigoAtual.equals(vcto.getMatriculaPeriodoVO().getUnidadeEnsinoCurso()))) {
                    getFacadeFactory().getMatriculaPeriodoFacade().montarDadosProcessoMatriculaCalendarioVO(vcto.getMatriculaPeriodoVO().getMatriculaVO(), vcto.getMatriculaPeriodoVO(),
                            NivelMontarDados.TODOS, getUsuarioLogado());
                    processoMatriculaCalendarioVOCarregado = vcto.getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO();
                    unidadeEnsinoCursoCodigoAtual = vcto.getMatriculaPeriodoVO().getUnidadeEnsinoCursoVO().getCodigo();
                } else {
                    vcto.getMatriculaPeriodoVO().setProcessoMatriculaCalendarioVO(processoMatriculaCalendarioVOCarregado);
                    vcto.getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO().setNivelMontarDados(NivelMontarDados.TODOS);
                }
                // INICIALIZANDO O PLANO FINANCEIRO DO CURSO;
                if ((planoFinanceiroCursoVOCarregado == null) || (!planoFinanceiroCursoVOCarregado.getCodigo().equals(vcto.getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getCodigo()))) {
                    MatriculaPeriodo.montarDadosPlanoFinanceiroCurso(vcto.getMatriculaPeriodoVO(), NivelMontarDados.TODOS, getUsuarioLogado());
                    planoFinanceiroCursoVOCarregado = vcto.getMatriculaPeriodoVO().getPlanoFinanceiroCurso();
                } else {
                    vcto.getMatriculaPeriodoVO().setPlanoFinanceiroCurso(planoFinanceiroCursoVOCarregado);
                    vcto.getMatriculaPeriodoVO().getPlanoFinanceiroCurso().setNivelMontarDados(NivelMontarDados.TODOS);
                }
                vcto.getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setPlanoFinanceiroCursoVO(planoFinanceiroCursoVOCarregado);
                if (vcto.getMatriculaPeriodoVO().getTurma().getCodigo().equals(0)) {
                    vcto.getMatriculaPeriodoVO().setTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorMatriculaPeriodoDadosContaCorrente(vcto.getMatriculaPeriodoVO().getCodigo(), getUsuarioLogado()));
                }
                // INICIALIZANDO CONDICAO PAGAMENTO PLANO FINANCEIRO CURSO
                if ((condicaoPagamentoPlanoFinanceiroCursoVOCarregado == null)
                        || (!condicaoPagamentoPlanoFinanceiroCursoVOCarregado.getCodigo().equals(vcto.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo()))) {
                    MatriculaPeriodo.montarDadosCondicaoPagamentoPlanoFinanceiroCurso(vcto.getMatriculaPeriodoVO(), NivelMontarDados.TODOS, getUsuarioLogado());
                    condicaoPagamentoPlanoFinanceiroCursoVOCarregado = vcto.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso();
                } else {
                    vcto.getMatriculaPeriodoVO().setCondicaoPagamentoPlanoFinanceiroCurso(condicaoPagamentoPlanoFinanceiroCursoVOCarregado);
                    vcto.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().setNivelMontarDados(NivelMontarDados.TODOS);
                }
                vcto.getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setCondicaoPagamentoPlanoFinanceiroCursoVO(condicaoPagamentoPlanoFinanceiroCursoVOCarregado);
                gerarParcelaMatriculaPeriodoVencimentoVOEspecificaAluno(vcto);
                valorAtualProgressBarAluno++;
            }
            setProgressBarAtivoAluno(false);
            finalizouProcessamentoAluno = false;
            prepararListaMatriculaPeriodoVencimentoApresentacaoResultadoFinal(listaMatriculaPeriodoVencimentoGerar);
            setMensagemID("msg_Matricula_gerarParcelaFinanceira");

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void atualizarListaMatriculaPeriodoVencimentoGerar(List<MatriculaPeriodoVO> listaGerar) {
        progressBarAtivo = false;
        finalizouProcessamento = false;
        valorAtualProgressBar = -1;

        setNrItensComErroProcessamento((Integer) 0);
        nrParcelasGerar = listaGerar.size();
        valorTotalParcelasGerar = 0.0;
    }

    public Boolean getApresentarBotaoGerarVctos() {
        if ((listaProcessarVcto != null) && (listaProcessarVcto.size() > 0)) {
            return true;
        }
        return false;
    }

    public void consultarMatriculaPeriodoVOGerarVencimentoPendente() {
        try {
            listaProcessarVcto = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaMatriculaPeriodoAtivaSemMatriculaPeriodoVencimento(parcelaGerar, semestreReferencia, anoReferencia, getUsuarioLogado());
            atualizarListaMatriculaPeriodoVencimentoGerar(listaProcessarVcto);
            setMensagemDetalhada("msg_Matricula_consultarParcelaGerarFinanceiro", "Foram encontradas " + listaProcessarVcto.size()
                    + " matrículas para as quais é necessário gerar Vcto referente a parcela " + parcelaGerar);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }

    }

    public void gerarMatriculaPeriodoVencimentoAlunos() {
        try {
            progressBarAtivo = true;
//            ConfiguracaoFinanceiroVO configuracaoFinanceiro = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), null);
            //ConfiguracaoFinanceiroVO configuracaoFinanceiro = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino);
            ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVOCarregado = null;
            PlanoFinanceiroCursoVO planoFinanceiroCursoVOCarregado = null;
            CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVOCarregado = null;
            Integer unidadeEnsinoCursoCodigoAtual = null;
            // List<MatriculaPeriodoVO> listaMatriculaPeriodoAtivaSemMatriculaPeriodoVencimentoCriada = listaProcessarVcto;

            for (MatriculaPeriodoVO matriculaPeriodoVO : listaProcessarVcto) {
                matriculaPeriodoVO.setConfiguracaoFinanceiro(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCodigo()));
                matriculaPeriodoVO.getConfiguracaoFinanceiro().setNivelMontarDados(NivelMontarDados.TODOS);

                // INICIALIZANDO O PROCESSO MATRICULA, PARA GERACAO DA CONTA A RECEBER
                // VALE RESSALTAR QUE A ROTINA TRABALHA COM O CONCEITO DE ORDENAR OS VCTOS POR UNIDADEENSINOCURSO /
                // PROCESSOMATRICULA, DE FORMA QUE TODOS OS VCTO QUE COMPARTILHAM ESTAS INFORMACOES SEJAM PROCESSADOS
                // CONJUNTAMENTE. EVITANDO ASSIM QUE ESTE DADO TENHA QUE SER REPROCESSADO DIVERSAS VEZES
                if ((processoMatriculaCalendarioVOCarregado == null) || (!unidadeEnsinoCursoCodigoAtual.equals(matriculaPeriodoVO.getUnidadeEnsinoCurso()))) {
                    getFacadeFactory().getMatriculaPeriodoFacade().montarDadosProcessoMatriculaCalendarioVO(matriculaPeriodoVO.getMatriculaVO(), matriculaPeriodoVO, NivelMontarDados.TODOS, getUsuarioLogado());
                    processoMatriculaCalendarioVOCarregado = matriculaPeriodoVO.getProcessoMatriculaCalendarioVO();
                    unidadeEnsinoCursoCodigoAtual = matriculaPeriodoVO.getUnidadeEnsinoCursoVO().getCodigo();
                } else {
                    matriculaPeriodoVO.setProcessoMatriculaCalendarioVO(processoMatriculaCalendarioVOCarregado);
                    matriculaPeriodoVO.getProcessoMatriculaCalendarioVO().setNivelMontarDados(NivelMontarDados.TODOS);
                }
                // INICIALIZANDO O PLANO FINANCEIRO DO CURSO;
                if ((planoFinanceiroCursoVOCarregado == null) || (!planoFinanceiroCursoVOCarregado.getCodigo().equals(matriculaPeriodoVO.getPlanoFinanceiroCurso().getCodigo()))) {
                    MatriculaPeriodo.montarDadosPlanoFinanceiroCurso(matriculaPeriodoVO, NivelMontarDados.TODOS, getUsuarioLogado());
                    planoFinanceiroCursoVOCarregado = matriculaPeriodoVO.getPlanoFinanceiroCurso();
                } else {
                    matriculaPeriodoVO.setPlanoFinanceiroCurso(planoFinanceiroCursoVOCarregado);
                    matriculaPeriodoVO.getPlanoFinanceiroCurso().setNivelMontarDados(NivelMontarDados.TODOS);
                }
                // INICIALIZANDO CONDICAO PAGAMENTO PLANO FINANCEIRO CURSO
                if ((condicaoPagamentoPlanoFinanceiroCursoVOCarregado == null)
                        || (!condicaoPagamentoPlanoFinanceiroCursoVOCarregado.getCodigo().equals(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo()))) {
                    MatriculaPeriodo.montarDadosCondicaoPagamentoPlanoFinanceiroCurso(matriculaPeriodoVO, NivelMontarDados.TODOS, getUsuarioLogado());
                    condicaoPagamentoPlanoFinanceiroCursoVOCarregado = matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso();
                } else {
                    matriculaPeriodoVO.setCondicaoPagamentoPlanoFinanceiroCurso(condicaoPagamentoPlanoFinanceiroCursoVOCarregado);
                    matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().setNivelMontarDados(NivelMontarDados.TODOS);
                }
                // gerarMatriculaPeriodoVencimentoEspecifica(matriculaPeriodoVO, parcelaGerar, dataVencimentoGerarParcela);
                valorAtualProgressBar++;
            }
            progressBarAtivo = false;
            finalizouProcessamento = true;
            setMensagemID("msg_Matricula_gerarParcelaFinanceira");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    private void gerarMatriculaPeriodoVencimentoEspecifica(MatriculaPeriodoVO matriculaPeriodoVO, String parcelaGerar, Date dataVencimentoGerarParcela) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }


    /**
     * @return the semestreReferencia
     */
    public String getSemestreReferencia() {
        return semestreReferencia;
    }

    /**
     * @param semestreReferencia
     *            the semestreReferencia to set
     */
    public void setSemestreReferencia(String semestreReferencia) {
        this.semestreReferencia = semestreReferencia;
    }

    /**
     * @return the progressBarAtivoAluno
     */
    public boolean isProgressBarAtivoAluno() {
        return progressBarAtivoAluno;
    }

    /**
     * @param progressBarAtivoAluno
     *            the progressBarAtivoAluno to set
     */
    public void setProgressBarAtivoAluno(boolean progressBarAtivoAluno) {
        this.progressBarAtivoAluno = progressBarAtivoAluno;
    }

    /**
     * @return the finalizouProcessamentoAluno
     */
    public Boolean getFinalizouProcessamentoAluno() {
        return finalizouProcessamentoAluno;
    }

    /**
     * @param finalizouProcessamentoAluno
     *            the finalizouProcessamentoAluno to set
     */
    public void setFinalizouProcessamentoAluno(Boolean finalizouProcessamentoAluno) {
        this.finalizouProcessamentoAluno = finalizouProcessamentoAluno;
    }

    /**
     * @return the valorAtualProgressBarAluno
     */
    public Integer getValorAtualProgressBarAluno() {
        return valorAtualProgressBarAluno;
    }

    /**
     * @param valorAtualProgressBarAluno
     *            the valorAtualProgressBarAluno to set
     */
    public void setValorAtualProgressBarAluno(Integer valorAtualProgressBarAluno) {
        this.valorAtualProgressBarAluno = valorAtualProgressBarAluno;
    }

    /**
     * @return the nrParcelasGerarAluno
     */
    public Integer getNrParcelasGerarAluno() {
        return nrParcelasGerarAluno;
    }

    /**
     * @param nrParcelasGerarAluno
     *            the nrParcelasGerarAluno to set
     */
    public void setNrParcelasGerarAluno(Integer nrParcelasGerarAluno) {
        this.nrParcelasGerarAluno = nrParcelasGerarAluno;
    }

    /**
     * @return the unidadeEnsinoAluno
     */
    public UnidadeEnsinoVO getUnidadeEnsinoAluno() {
        return unidadeEnsinoAluno;
    }

    /**
     * @param unidadeEnsinoAluno
     *            the unidadeEnsinoAluno to set
     */
    public void setUnidadeEnsinoAluno(UnidadeEnsinoVO unidadeEnsinoAluno) {
        this.unidadeEnsinoAluno = unidadeEnsinoAluno;
    }

    public void setTelaEmUso(Boolean telaEmUso) {
        this.telaEmUso = telaEmUso;
    }

    public Boolean getTelaEmUso() {
        if (telaEmUso == null) {
            telaEmUso = Boolean.FALSE;
        }
        return telaEmUso;
    }
    
    public boolean isTrazerMatriculasComCanceladoFinanceiro() {
		return trazerMatriculasComCanceladoFinanceiro;
	}

	public void setTrazerMatriculasComCanceladoFinanceiro(boolean trazerMatriculasComCanceladoFinanceiro) {
		this.trazerMatriculasComCanceladoFinanceiro = trazerMatriculasComCanceladoFinanceiro;
	}
}
