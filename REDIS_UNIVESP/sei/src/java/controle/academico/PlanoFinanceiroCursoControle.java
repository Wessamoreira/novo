package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CondicaoPagamentoPlanoDescontoVO;
import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.PlanoDescontoDisponivelMatriculaVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.PlanoFinanceiroAlunoVO;
import negocio.comuns.academico.PlanoFinanceiroCursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoPlanoFinanceiroCursoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoTextoPadrao;
import negocio.facade.jdbc.academico.PlanoFinanceiroCurso;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas planoFinanceiroCursoForm.jsp planoFinanceiroCursoCons.jsp) com as funcionalidades da classe
 * <code>PlanoFinanceiroCurso</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see PlanoFinanceiroCurso
 * @see PlanoFinanceiroCursoVO
 */
@Controller("PlanoFinanceiroCursoControle")
@Scope("viewScope")
@Lazy
public class PlanoFinanceiroCursoControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = -1618992598160243357L;

	private PlanoFinanceiroCursoVO planoFinanceiroCursoVO;
    protected CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVO;
    private PlanoDescontoDisponivelMatriculaVO planoDescontoDisponivelMatriculaVO;
    protected List listaSelectItemDescontoProgressivoPadrao;
    private List listaSelectItemDescontoProgressivoPadraoMatricula;
    protected List listaSelectItemPlanoDescontoPadrao;
    protected List listaSelectItemPlanoDesconto;
    private List listaSelectItemDescontoProgresivo;
    private List listaSelectItemTextoPadraoContratoMatricula;
    private List listaSelectItemTextoPadraoContratoMatriculaCondicao;
    private List listaSelectItemTextoPadraoContratoExtensao;
    private List listaSelectItemTextoPadraoContratoFiador;
	private List listaSelectItemTextoPadraoContratoInclusaoReposicao;    
    private List listaSelectItemTextoPadraoContratoAditivo;
    protected List<SelectItem> listaSelectItemTurma;
    private List<SelectItem> listaSelectTipoCobrancaCreditoValor;
    private List<SelectItem> listaSelectTipoCobrancaCreditoValorHora;
    private List<SelectItem> listaSelectTipoDescontoDisciplinaEAD;
    private List<SelectItem> listaSelectTipoControleAlunoIntegralizandoCurso;
    private String curso_Erro;
    protected List listaSelectItemUnidadeEnsino;
    protected List listaSelectItemUnidadeEnsinoFinanceira;
    protected List listaSelectItemTurno;
    private String responsavelAutorizacao_Erro;
    private List listaConsultaCurso;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    protected String mostrarAlertaMudancaTipoParcela;
    protected String tipoFormaCalculoParcelaAux;
    protected String textoAlertaMudancaTipoParcela;
    private Boolean criaNovaCondicaoPagamento;
    private Boolean fecharModalCondicaoPagamento;

    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List listaConsultaTurma;
    
    protected List<CentroReceitaVO> listaConsultaCentroReceita;
    protected String valorConsultaCentroReceita;
    protected String campoConsultaCentroReceita;
    private List<SelectItem> listaSelectTipoUsoCondicacaoPagamento;
    
    private CondicaoPagamentoPlanoDescontoVO condicaoPagamentoPlanoDescontoVO;
    private SituacaoPlanoFinanceiroCursoEnum situacaoPlanoFinanceiroCurso;

    public PlanoFinanceiroCursoControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        getControleConsulta().setCampoConsulta("descricao");
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsavel por disponibilizar um novo objeto da classe <code>PlanoFinanceiroCurso</code> para edicao pelo usuario da aplicacao.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setPlanoFinanceiroCursoVO(new PlanoFinanceiroCursoVO());
        getCondicaoPagamentoPlanoFinanceiroCursoVO().setSituacao("EL");
        inicializarListasSelectItemTodosComboBox();
        inicializarResponsavelPlanoFinanceiroCursoComUsuarioLogado();
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("planoFinanceiroCursoForm.xhtml");
    }

    public void inicializarResponsavelPlanoFinanceiroCursoComUsuarioLogado() {
        try {
            planoFinanceiroCursoVO.setResponsavelAutorizacao(getUsuarioLogadoClone());
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Rotina responsavel por disponibilizar os dados de um objeto da classe <code>PlanoFinanceiroCurso</code> para alteracao. O objeto desta classe e disponibilizado na
     * session da pagina (request) para que o JSP correspondente possa disponibiliza-lo para edicao.
     */
    public String editar() throws Exception {
        PlanoFinanceiroCursoVO obj = (PlanoFinanceiroCursoVO) context().getExternalContext().getRequestMap().get("planoFinanceiroCursoItens");
        setPlanoFinanceiroCursoVO(obj);
        getFacadeFactory().getPlanoFinanceiroCursoFacade().carregarDados(getPlanoFinanceiroCursoVO(), getUsuarioLogado());
        getPlanoFinanceiroCursoVO().setNovoObj(Boolean.FALSE);
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("planoFinanceiroCursoForm.xhtml");
    }

    /**
     * Rotina responsavel por gravar no BD os dados editados de um novo objeto da classe <code>PlanoFinanceiroCurso</code>. Caso o objeto seja novo (ainda nao gravado no BD)
     * e acionado a operacao <code>incluir()</code>. Caso contrario e acionado o <code>alterar()</code>. Se houver alguma inconsistencia o objeto nao e gravado,
     * sendo re-apresentado para o usuario juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (planoFinanceiroCursoVO.getCodigo().intValue() == 0) {
                getFacadeFactory().getPlanoFinanceiroCursoFacade().incluir(planoFinanceiroCursoVO, getUsuarioLogado());
            } else {
                // Como o usuario atual esta alterando o plano, ele passa
                // a ser responsavel pelo mesmo.
                inicializarResponsavelPlanoFinanceiroCursoComUsuarioLogado();
                getFacadeFactory().getPlanoFinanceiroCursoFacade().alterar(planoFinanceiroCursoVO, getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("planoFinanceiroCursoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("planoFinanceiroCursoForm.xhtml");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP PlanoFinanceiroCursoCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado
     * campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    @Override
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                Integer valorConsInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getPlanoFinanceiroCursoFacade().consultaRapidaPorCodigo(valorConsInt, getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("descricao")) {
            	objs = getFacadeFactory().getPlanoFinanceiroCursoFacade().consultaRapidaPorDescricao(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, getSituacaoPlanoFinanceiroCurso(), getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("planoFinanceiroCursoCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("planoFinanceiroCursoCons.xhtml");
        }
    }

    /**
     * Operacao responsavel por processar a exclusao um objeto da classe <code>PlanoFinanceiroCursoVO</code> Apos a exclusao ela automaticamente aciona a rotina para
     * uma nova inclusao.
     */
    public String excluir() {
        try {
            getFacadeFactory().getPlanoFinanceiroCursoFacade().excluir(planoFinanceiroCursoVO, getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("planoFinanceiroCursoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("planoFinanceiroCursoForm.xhtml");
        }
    }

    public String getRealizarVerificacaoFechamentoModal() {
        if (getFecharModalCondicaoPagamento()) {
            return "RichFaces.$('panelValorFixo').hide(); RichFaces.$('panelValorDisciplina').hide(); RichFaces.$('panelValorCredito').hide(); RichFaces.$('panelValorFormaCalculo').hide()";
        }
        return getSelecionarRichModalPanel();
    }

    public void adicionarCondicaoPagamento() {
        try {
            getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().validarDadosAdicionarCondicaoPagamentoPlanoFinanceiroCurso(getCondicaoPagamentoPlanoFinanceiroCursoVO(), getUsuarioLogado());
			/**
			 * Por default, toda CondicaoPagamentoPlanoFinanceiroCurso cujo
			 * tipoCalculoParcela seja FC (Valor por Forma Cálculo), o
			 * tipoUsoCondicaoPagamento deve ser 'MATRICULA_REGULAR'.
			 */
			if (getCondicaoPagamentoPlanoFinanceiroCursoVO().getTipoCalculoParcela().equals("FC")) {
				getCondicaoPagamentoPlanoFinanceiroCursoVO().setTipoUsoCondicaoPagamento("MATRICULA_REGULAR");
			}
            getPlanoFinanceiroCursoVO().adicionarCondicaoPagamentoPlanoFinanceiroCursoVOs(getCondicaoPagamentoPlanoFinanceiroCursoVO());
            setCondicaoPagamentoPlanoFinanceiroCursoVO(new CondicaoPagamentoPlanoFinanceiroCursoVO());
            setMensagemID("msg_dados_adicionados");
            setFecharModalCondicaoPagamento(true);
        } catch (Exception e) {
            setFecharModalCondicaoPagamento(false);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void adicionarPlanoDescontoDisponivelMatricula() {
        try {
            getPlanoDescontoDisponivelMatriculaVO().validarDados(getPlanoDescontoDisponivelMatriculaVO());
            getPlanoDescontoDisponivelMatriculaVO().setPlanoDesconto(getFacadeFactory().getPlanoDescontoFacade().consultarPorChavePrimaria(getPlanoDescontoDisponivelMatriculaVO().getPlanoDesconto().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            getCondicaoPagamentoPlanoFinanceiroCursoVO().adicionarPlanoDescontoDisponivelMatriculaVOs(getPlanoDescontoDisponivelMatriculaVO());
            setPlanoDescontoDisponivelMatriculaVO(new PlanoDescontoDisponivelMatriculaVO());
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void adicionarTodosPlanoDescontoDisponivelMatricula() {
        try {
            Iterator i = getListaSelectItemPlanoDesconto().iterator();
            while (i.hasNext()) {
                SelectItem si = (SelectItem) i.next();
                PlanoDescontoVO pd = new PlanoDescontoVO();
                if ((Integer)si.getValue() != 0) {
                    pd.setCodigo((Integer)si.getValue());
                    getPlanoDescontoDisponivelMatriculaVO().setPlanoDesconto(pd);
                    getPlanoDescontoDisponivelMatriculaVO().validarDados(getPlanoDescontoDisponivelMatriculaVO());
                    getPlanoDescontoDisponivelMatriculaVO().setPlanoDesconto(getFacadeFactory().getPlanoDescontoFacade().consultarPorChavePrimaria(getPlanoDescontoDisponivelMatriculaVO().getPlanoDesconto().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
                    getCondicaoPagamentoPlanoFinanceiroCursoVO().adicionarPlanoDescontoDisponivelMatriculaVOs(getPlanoDescontoDisponivelMatriculaVO());
                    setPlanoDescontoDisponivelMatriculaVO(new PlanoDescontoDisponivelMatriculaVO());
                }
            }
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void montarListaSelectItemDescontoProgressivo(String prm) throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarDescontoProgressivoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                DescontoProgressivoVO obj = (DescontoProgressivoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemDescontoProgresivo(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            ordenador = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }
    
    public void montarListaSelectItemDescontoProgressivo() {
        try {
            montarListaSelectItemDescontoProgressivo("");
        } catch (Exception e) {
            //System.out.println(e.getMessage());
        }
    }
    
    public List consultarDescontoProgressivoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getDescontoProgressivoFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
        return lista;
    }

    public void removerPlanoDescontoDisponivelMatricula() {
        try {
            PlanoDescontoDisponivelMatriculaVO planoDesconto = (PlanoDescontoDisponivelMatriculaVO) context().getExternalContext().getRequestMap().get("planoDescontoDisponivelMatriculaVOItens");
            getCondicaoPagamentoPlanoFinanceiroCursoVO().excluirObjPlanoDescontoDisponivelMatriculaVOs(planoDesconto.getPlanoDesconto().getCodigo());
            setMensagemID("msg_dados_excluidos");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String getCampoConsultaCurso() {
        return campoConsultaCurso;
    }

    public void setCampoConsultaCurso(String campoConsultaCurso) {
        this.campoConsultaCurso = campoConsultaCurso;
    }

    public List getListaConsultaCurso() {
        return listaConsultaCurso;
    }

    public void setListaConsultaCurso(List listaConsultaCurso) {
        this.listaConsultaCurso = listaConsultaCurso;
    }

    public String getValorConsultaCurso() {
        return valorConsultaCurso;
    }

    public void setValorConsultaCurso(String valorConsultaCurso) {
        this.valorConsultaCurso = valorConsultaCurso;
    }

    public List getTipoConsultaComboCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }

    public void irPaginaInicial() throws Exception {
        controleConsulta.setPaginaAtual(1);
        this.consultar();
    }

    public void irPaginaAnterior() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
        this.consultar();
    }

    public void irPaginaPosterior() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
        this.consultar();
    }

    public void irPaginaFinal() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
        this.consultar();
    }

    /**
     * Mï¿½todo responsï¿½vel por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave primï¿½ria. Esta rotina ï¿½ utilizada fundamentalmente por
     * requisiï¿½ï¿½es Ajax, que realizam busca pela chave primï¿½ria da entidade montando automaticamente o resultado da consulta para apresentaï¿½ï¿½o.
     */
    public void consultarPessoaPorChavePrimaria() {
        try {
            Integer campoConsulta = planoFinanceiroCursoVO.getResponsavelAutorizacao().getCodigo();
            PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            planoFinanceiroCursoVO.getResponsavelAutorizacao().setNome(pessoa.getNome());
            this.setResponsavelAutorizacao_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            planoFinanceiroCursoVO.getResponsavelAutorizacao().setNome("");
            planoFinanceiroCursoVO.getResponsavelAutorizacao().setCodigo(0);
            this.setResponsavelAutorizacao_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Mï¿½todo responsï¿½vel por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este atributo ï¿½ uma lista (<code>List</code>) utilizada para
     * definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarTurnoPorNome(String nomePrm, Integer codigoCurso) throws Exception {
        List lista = getFacadeFactory().getTurnoFacade().consultarPorNome(nomePrm, codigoCurso, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Mï¿½todo responsï¿½vel por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>UnidadeEnsino</code>.
     */
    public void criarNovaCondicacaoPagamento() throws ConsistirException {
        this.setCondicaoPagamentoPlanoFinanceiroCursoVO(new CondicaoPagamentoPlanoFinanceiroCursoVO());        
        setCriaNovaCondicaoPagamento(Boolean.TRUE);
        inicializarListasSelectItemComboBoxCondicaoPagamento();
        if (getPlanoFinanceiroCursoVO().getIsValorPorDisciplina()){
            this.getCondicaoPagamentoPlanoFinanceiroCursoVO().setTipoCalculoParcela("VD");
            try {
                getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().adicionarCondicaoPlanoFinanceiroCursoTurma(getCondicaoPagamentoPlanoFinanceiroCursoVO(), getPlanoFinanceiroCursoVO().getTurma().getCodigo());
            } catch (Exception e) {
            }
        }

    }

    /**
     * Por padrão o SEI sempre direcionará para o painel de registro de condição de pagamento
     * por valor Fixo. Contudo, o usuário poderá alterar isto dentro do próprio painel.
     * @return 
     */
    public String getTipoPanelCalculoParcelas() {
        if (getCondicaoPagamentoPlanoFinanceiroCursoVO().getTipoCalculoParcela().equals("VC")) {
            return "RichFaces.$('panelValorCredito').show();";
        } else if (getCondicaoPagamentoPlanoFinanceiroCursoVO().getTipoCalculoParcela().equals("VF")) {
            return "RichFaces.$('panelValorFixo').show()";
        } else {
        	return "RichFaces.$('panelValorDisciplina').show()";
        }
    }

    /**
     * Por padrão o SEI sempre direcionará para o painel de registro de condição de pagamento
     * por valor Fixo. Contudo, o usuário poderá alterar isto dentro do próprio painel.
     * @return 
     */
    public String getSelecionarRichModalPanel() {
        if (getCondicaoPagamentoPlanoFinanceiroCursoVO().getTipoCalculoParcela().equals("VF")) {
            return "RichFaces.$('panelValorFixo').show()";
        } else if (getCondicaoPagamentoPlanoFinanceiroCursoVO().getTipoCalculoParcela().equals("VC")) {
            return "RichFaces.$('panelValorCredito').show()";
        } else if (getCondicaoPagamentoPlanoFinanceiroCursoVO().getTipoCalculoParcela().equals("VD")) {
            return "RichFaces.$('panelValorDisciplina').show()";
        } else {
            return "RichFaces.$('panelValorFormaCalculo').show()";
        }
    }

    public void alertaMudancaTipoParcela() throws ConsistirException {
        try {
            int i = 0;
        } catch (Exception e) {
            setMensagemDetalhada(e.getMessage());
        }
    }
    
    public String getPainelCondicaoPagamentoRedirecionar() {
        if (this.getCondicaoPagamentoPlanoFinanceiroCursoVO().getTipoCalculoParcela().equals("VF")) {
            return "RichFaces.$('panelValorFormaCalculo').hide(); RichFaces.$('panelValorDisciplina').hide(); RichFaces.$('panelValorCredito').hide(); RichFaces.$('panelValorFixo').show();";
        }
        if (this.getCondicaoPagamentoPlanoFinanceiroCursoVO().getTipoCalculoParcela().equals("VC")) {
            return "RichFaces.$('panelValorFixo').hide(); RichFaces.$('panelValorFormaCalculo').hide(); RichFaces.$('panelValorDisciplina').hide(); RichFaces.$('panelValorCredito').show();";
        }
        if (this.getCondicaoPagamentoPlanoFinanceiroCursoVO().getTipoCalculoParcela().equals("FC")) {
            return "RichFaces.$('panelValorDisciplina').hide(); RichFaces.$('panelValorCredito').hide(); RichFaces.$('panelValorFixo').hide(); RichFaces.$('panelValorFormaCalculo').show(); ";
        }
        return "";
    }

    public String getVisualisarPanelAlertaTipoMudanca() {
        return getMostrarAlertaMudancaTipoParcela();
    }

    public void limparListaCondicaoPagamento() throws Exception {
//        List<PlanoFinanceiroAlunoVO> planos = new ArrayList<PlanoFinanceiroAlunoVO>(0);
//        planos = getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorCondicaoPagamentoPlanoFinanceiroCurso(getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo().intValue(), false,
//                Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
//        if (planos.size() != 0) {
//            throw new ConsistirException("Você não pode alterar o campo Forma Cálculo Parcelas pois uma das condições de pagamento está sendo utilizada, para criar uma nova Forma Cálculo Parcelas crie um novo Plano Financeiro para este curso.");
//        }
//        getPlanoFinanceiroCursoVO().setCondicaoPagamentoPlanoFinanceiroCursoVOs(new ArrayList<CondicaoPagamentoPlanoFinanceiroCursoVO>(0));
//        setTipoFormaCalculoParcelaAux(getPlanoFinanceiroCursoVO().getTipoCalculoParcela());
//        setMostrarAlertaMudancaTipoParcela("Richfaces.hideModalPanel('panelAlertaMudancaTipoParcela')");
    }

    public void manterTipoParcela() {
//        getPlanoFinanceiroCursoVO().setTipoCalculoParcela(getTipoFormaCalculoParcelaAux());
//        setMostrarAlertaMudancaTipoParcela("Richfaces.hideModalPanel('panelAlertaMudancaTipoParcela')");
    }

    public void editarCondicaoPagamento() {
        try {
            CondicaoPagamentoPlanoFinanceiroCursoVO condicao = (CondicaoPagamentoPlanoFinanceiroCursoVO) context().getExternalContext().getRequestMap().get("condicaoItens");
            this.setCondicaoPagamentoPlanoFinanceiroCursoVO(condicao);
            setCriaNovaCondicaoPagamento(Boolean.FALSE);
            if(getPlanoFinanceiroCursoVO().getIsValorPorDisciplina() && getCondicaoPagamentoPlanoFinanceiroCursoVO().getCondicaoPlanoFinanceiroCursoTurmaVOs().isEmpty()){
                try {
                    getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().adicionarCondicaoPlanoFinanceiroCursoTurma(getCondicaoPagamentoPlanoFinanceiroCursoVO(), getPlanoFinanceiroCursoVO().getTurma().getCodigo());
                } catch (Exception e) {
                   
                }
            }
            inicializarListasSelectItemComboBoxCondicaoPagamento();
            setMensagemID("msg_dados_editar");
        } catch (Exception e) {
            setMensagemDetalhada(e.getMessage());
        }
    }

    public void clonarCondicaoPagamento() {
        try {
            CondicaoPagamentoPlanoFinanceiroCursoVO condicao = (CondicaoPagamentoPlanoFinanceiroCursoVO) context().getExternalContext().getRequestMap().get("condicaoItens");
            condicao = (getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(condicao.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            setCondicaoPagamentoPlanoFinanceiroCursoVO(condicao.getClone());
            setCriaNovaCondicaoPagamento(Boolean.TRUE);
            inicializarListasSelectItemComboBoxCondicaoPagamento();
            if(getPlanoFinanceiroCursoVO().getIsValorPorDisciplina() && getCondicaoPagamentoPlanoFinanceiroCursoVO().getCondicaoPlanoFinanceiroCursoTurmaVOs().isEmpty()){
            	getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().adicionarCondicaoPlanoFinanceiroCursoTurma(getCondicaoPagamentoPlanoFinanceiroCursoVO(), getPlanoFinanceiroCursoVO().getTurma().getCodigo());
            }
            setMensagemID("msg_dados_clonados");
        } catch (Exception e) {
            setMensagemDetalhada(e.getMessage());
        }
    }

    public void excluirCondicaoPagamento() {
        try {
            CondicaoPagamentoPlanoFinanceiroCursoVO condicao = (CondicaoPagamentoPlanoFinanceiroCursoVO) context().getExternalContext().getRequestMap().get("condicaoItens");
            List<PlanoFinanceiroAlunoVO> planos = new ArrayList<PlanoFinanceiroAlunoVO>(0);
            planos = getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorCondicaoPagamentoPlanoFinanceiroCurso(condicao.getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            if (planos.size() != 0) {
                throw new ConsistirException("Você não pode excluir essa condição de pagamento porque ela está sendo utilizada.");
            }
            getPlanoFinanceiroCursoVO().getCondicaoPagamentoPlanoFinanceiroCursoVOs().remove(condicao);
            setMensagemID("msg_dados_excluidos");
        } catch (Exception e) {
            setMensagemDetalhada(e.getMessage());
        }
    }

    /**
     * Metodo responsavel por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este atributo e uma lista (<code>List</code>) utilizada para
     * definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuarioUnidadeEnsinoVinculadaAoUsuario(getUsuarioLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            if (resultadoConsulta.isEmpty()) {
                resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            }
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            if (getUnidadeEnsinoLogado().getCodigo() == 0) {
                objs.add(new SelectItem(0, ""));
            }
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }
    
    public void montarListaSelectItemUnidadeEnsinoFinanceira() {
        try {
            montarListaSelectItemUnidadeEnsinoFinanceira("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectItemUnidadeEnsinoFinanceira(String prm) throws Exception {
        try {
        	getListaSelectItemUnidadeEnsinoFinanceira().clear();
        	if (getUnidadeEnsinoLogado().getCodigo() == 0) {
            	getListaSelectItemUnidadeEnsinoFinanceira().add(new SelectItem(0, ""));
            }
        	List<UnidadeEnsinoVO> listaUnidadeEnsinoFinanceira = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuarioUnidadeEnsinoVinculadaAoUsuario(getUsuarioLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	        if (listaUnidadeEnsinoFinanceira.isEmpty()) {
	           	listaUnidadeEnsinoFinanceira = consultarUnidadeEnsinoPorNome("");
	        }
	        listaUnidadeEnsinoFinanceira.stream().forEach(p->getListaSelectItemUnidadeEnsinoFinanceira().add(new SelectItem(p.getCodigo(), p.getNome())));
        	
        } catch (Exception e) {
            throw e;
        } 
    }

    /**
     * Mï¿½todo responsï¿½vel por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>PlanoDescontoPadrao</code>.
     */
    public void montarListaSelectItemPlanoDescontoPadrao(Boolean novaCondicaoPagamento) throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = new ArrayList(0);
        Iterator i = null;
        try {
            List<PlanoDescontoVO> listaPlanoDesconto = new ArrayList<PlanoDescontoVO>(0);
            listaPlanoDesconto = consultarPlanoDesconto();

            for (PlanoDescontoVO desconto : listaPlanoDesconto) {
                if (novaCondicaoPagamento) {
                    if (desconto.getAtivo()) {
                        resultadoConsulta.add(desconto);
                    }
                } else {
                    if (desconto.getCodigo().equals(getCondicaoPagamentoPlanoDescontoVO().getPlanoDescontoVO().getCodigo())) {
                        resultadoConsulta.add(desconto);
                    } else {
                        if (desconto.getAtivo()) {
                            resultadoConsulta.add(desconto);
                        }
                    }
                }
            }

            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                PlanoDescontoVO obj = (PlanoDescontoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemPlanoDescontoPadrao(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            ordenador = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Mï¿½todo responsï¿½vel por atualizar o ComboBox relativo ao atributo <code>PlanoDescontoPadrao</code>. Buscando todos os objetos correspondentes a entidade
     * <code>PlanoDesconto</code>. Esta rotina nï¿½o recebe parï¿½metros para filtragem de dados, isto ï¿½ importante para a inicializaï¿½ï¿½o dos dados da tela para o acionamento
     * por meio requisiï¿½ï¿½es Ajax.
     */
    public void montarListaSelectItemPlanoDescontoPadrao() {
        try {
            montarListaSelectItemPlanoDescontoPadrao(getCriaNovaCondicaoPagamento());
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Mï¿½todo responsï¿½vel por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este atributo ï¿½ uma lista (<code>List</code>) utilizada para
     * definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarPlanoDesconto() throws Exception {
        List lista = getFacadeFactory().getPlanoDescontoFacade().consultarPlanoDescontoNivelComboBox(getUsuarioLogadoClone());
        return lista;
    }
    
    public void removerItemPlanoDesconto() throws Exception {
    	CondicaoPagamentoPlanoDescontoVO obj = (CondicaoPagamentoPlanoDescontoVO) context().getExternalContext().getRequestMap().get("condicaoPagamentoPlanoDescontoItens");
    	getCondicaoPagamentoPlanoFinanceiroCursoVO().excluirObjItemPlanoDesconto(obj);
    	setMensagemID("msg_dados_excluidos");
    }

    /**
     * Mï¿½todo responsï¿½vel por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>DescontoProgressivoPadrao</code>.
     */
    public void montarListaSelectItemDescontoProgressivoPadrao(Boolean novaCondicaoPagameto) throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = new ArrayList(0);
        Iterator i = null;
        try {
            List<DescontoProgressivoVO> listaDescontoProgressivo = new ArrayList<DescontoProgressivoVO>(0);
            listaDescontoProgressivo = consultarDescontoProgressivo();
            for (DescontoProgressivoVO desconto : listaDescontoProgressivo) {
                if (novaCondicaoPagameto) {
                    if (desconto.getAtivado()) {
                        resultadoConsulta.add(desconto);
                    }
                } else {
                    if (desconto.getCodigo().equals(getCondicaoPagamentoPlanoFinanceiroCursoVO().getDescontoProgressivoPadrao().getCodigo())) {
                        resultadoConsulta.add(desconto);
                    } else {
                        if (desconto.getAtivado()) {
                            resultadoConsulta.add(desconto);
                        }
                    }
                }

            }

            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                DescontoProgressivoVO obj = (DescontoProgressivoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemDescontoProgressivoPadrao(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            ordenador = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Mï¿½todo responsï¿½vel por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>DescontoProgressivoPadrao</code>.
     */
    public void montarListaSelectItemDescontoProgressivoPadraoMatricula(Boolean novaCondicaoPagamento) throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = new ArrayList(0);
        Iterator i = null;
        try {
            List<DescontoProgressivoVO> listaDescontoProgressivo = new ArrayList<DescontoProgressivoVO>(0);
            listaDescontoProgressivo = consultarDescontoProgressivo();
            for (DescontoProgressivoVO desconto : listaDescontoProgressivo) {
                if (novaCondicaoPagamento) {
                    if (desconto.getAtivado()) {
                        resultadoConsulta.add(desconto);
                    }
                } else {
                    if (desconto.getCodigo().equals(getCondicaoPagamentoPlanoFinanceiroCursoVO().getDescontoProgressivoPadraoMatricula().getCodigo())) {
                        resultadoConsulta.add(desconto);
                    } else {
                        if (desconto.getAtivado()) {
                            resultadoConsulta.add(desconto);
                        }
                    }
                }
            }
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                DescontoProgressivoVO obj = (DescontoProgressivoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemDescontoProgressivoPadraoMatricula(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            ordenador = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Mï¿½todo responsï¿½vel por atualizar o ComboBox relativo ao atributo <code>DescontoProgressivoPadrao</code>. Buscando todos os objetos correspondentes a entidade
     * <code>DescontoProgressivo</code>. Esta rotina nï¿½o recebe parï¿½metros para filtragem de dados, isto ï¿½ importante para a inicializaï¿½ï¿½o dos dados da tela para o
     * acionamento por meio requisiï¿½ï¿½es Ajax.
     */
    public void montarListaSelectItemDescontoProgressivoPadrao() {
        try {
            montarListaSelectItemDescontoProgressivoPadrao(getCriaNovaCondicaoPagamento());
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectItemDescontoProgressivoPadraoMatricula() {
        try {
            montarListaSelectItemDescontoProgressivoPadraoMatricula(getCriaNovaCondicaoPagamento());
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Mï¿½todo responsï¿½vel por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este atributo ï¿½ uma lista (<code>List</code>) utilizada para
     * definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarDescontoProgressivo() throws Exception {
        List lista = getFacadeFactory().getDescontoProgressivoFacade().consultarDescontoProgressivoComboBox(getUsuarioLogadoClone());
        return lista;
    }

    public void montarListaSelectItemTextoPadraoContratoFiador() throws Exception {
        List<TextoPadraoVO> textoPadraoVOFiador = consultarTextoPadraoPorTipo(TipoTextoPadrao.FIADOR.getValor());
        setListaSelectItemTextoPadraoContratoFiador(UtilSelectItem.getListaSelectItem(textoPadraoVOFiador, "codigo", "descricao"));
    }

    public void montarListaSelectItemTextoPadraoContratoMatricula() throws Exception {
        List<TextoPadraoVO> textoPadraoVOMatricula = consultarTextoPadraoPorTipo(TipoTextoPadrao.CONTRATO.getValor());
        setListaSelectItemTextoPadraoContratoMatricula(UtilSelectItem.getListaSelectItem(textoPadraoVOMatricula, "codigo", "descricao"));
    }

    public void montarListaSelectItemTextoPadraoContratoExtensao() throws Exception {
        List<TextoPadraoVO> textoPadraoVOExtensao = consultarTextoPadraoPorTipo(TipoTextoPadrao.EXTENSAO.getValor());
        setListaSelectItemTextoPadraoContratoExtensao(UtilSelectItem.getListaSelectItem(textoPadraoVOExtensao, "codigo", "descricao"));
    }

//    public void montarListaSelectItemTextoPadraoContratoModular() throws Exception {
//        List<TextoPadraoVO> textoPadraoVOModular = consultarTextoPadraoPorTipo(TipoTextoPadrao.MODULAR.getValor());
//        setListaSelectItemTextoPadraoContratoModular(UtilSelectItem.getListaSelectItem(textoPadraoVOModular, "codigo", "descricao"));
//    }

    public void montarListaSelectItemTextoPadraoContratoAditivo() throws Exception {
        List<TextoPadraoVO> textoPadraoVOAditivo = consultarTextoPadraoPorTipo(TipoTextoPadrao.ADITIVO.getValor());
        setListaSelectItemTextoPadraoContratoAditivo(UtilSelectItem.getListaSelectItem(textoPadraoVOAditivo, "codigo", "descricao"));
    }

    public void montarListaSelectItemTextoPadraoContratoMatriculaCondicao() throws Exception {
        List<TextoPadraoVO> textoPadraoVOMatricula = consultarTextoPadraoPorTipo(TipoTextoPadrao.CONTRATO.getValor());
        setListaSelectItemTextoPadraoContratoMatriculaCondicao(UtilSelectItem.getListaSelectItem(textoPadraoVOMatricula, "codigo", "descricao"));
    }

    @SuppressWarnings("unchecked")
    public List<TextoPadraoVO> consultarTextoPadraoPorTipo(String nomePrm) throws Exception {
        List<TextoPadraoVO> lista = getFacadeFactory().getTextoPadraoFacade().consultarPorTipoNivelComboBox(nomePrm, getPlanoFinanceiroCursoVO().getUnidadeEnsino(), "", false, getUsuarioLogado());
        return lista;
    }

    /**
     * Mï¿½todo responsï¿½vel por inicializar a lista de valores ( <code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        try {
            // montarListaSelectItemDescontoProgressivoPadrao();
            // montarListaSelectItemPlanoDescontoPadrao();
            montarListaSelectItemTextoPadraoContratoMatricula();
            montarListaSelectItemTextoPadraoContratoMatriculaCondicao();
            montarListaSelectItemTextoPadraoContratoFiador();
            montarListaSelectItemTextoPadraoContratoExtensao();
            //montarListaSelectItemTextoPadraoContratoModular();
            montarListaSelectItemTextoPadraoContratoInclusaoReposicao();            			
            montarListaSelectItemTextoPadraoContratoAditivo();
            montarListaSelectItemUnidadeEnsino();
            montarListaSelectItemUnidadeEnsinoFinanceira();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void inicializarListasSelectItemComboBoxCondicaoPagamento() {
        try {
            montarListaSelectItemDescontoProgressivoPadrao();
            montarListaSelectItemDescontoProgressivo();
            montarListaSelectItemDescontoProgressivoPadraoMatricula();
            montarListaSelectItemPlanoDescontoPadrao();
            montarListaSelectItemUnidadeEnsinoFinanceira();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Rotina responsï¿½vel por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    /**
     * Rotina responsï¿½vel por organizar a paginaï¿½ï¿½o entre as pï¿½ginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setPaginaAtualDeTodas("0/0");
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("planoFinanceiroCursoCons.xhtml");
    }

    public void inicializarDadosAtivacaoEInativacao() {
        CondicaoPagamentoPlanoFinanceiroCursoVO obj = (CondicaoPagamentoPlanoFinanceiroCursoVO) context().getExternalContext().getRequestMap().get("condicaoItens");
        setCondicaoPagamentoPlanoFinanceiroCursoVO(obj);
    }

    public void realizarAtivacaoCondicaoPagamentoPlanoFinanceiroCurso() {
        try {
            if (getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo() == 0) {
                throw new Exception("Dados ainda não gravados.");
            }
            getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().realizarAtivacaoCondicaoPagamento(getCondicaoPagamentoPlanoFinanceiroCursoVO(), Boolean.TRUE, getUsuarioLogado());
            getCondicaoPagamentoPlanoFinanceiroCursoVO().setSituacao("AT");
            setMensagemID("msg_dados_ativados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void adicionarItemPlanoDesconto() throws Exception {
        try {
        	getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().adicionarItemPlanoDesconto(getCondicaoPagamentoPlanoDescontoVO(), getCondicaoPagamentoPlanoFinanceiroCursoVO().getCondicaoPagamentoPlanoDescontoVOs(), getUsuarioLogado());
        	setCondicaoPagamentoPlanoDescontoVO(new CondicaoPagamentoPlanoDescontoVO());
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void montarListaSelectItemPlanoDesconto() {
        try {
            List resultadoConsulta = getFacadeFactory().getPlanoDescontoFacade().consultarPorNomeSomenteAtiva("", false, getUsuarioLogado());
            setListaSelectItemPlanoDesconto(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
        } catch (Exception e) {
            //System.out.println(e.getMessage());
        }
    }

    public void abrirRichModalPlanoFinanceiroCurso() {
        try {
            montarListaSelectItemPlanoDesconto();
            // setMensagemID("msg_dados_ativados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void realizarInativacaoCondicaoPagamentoPlanoFinanceiroCurso() {
        try {
            getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().realizarInativacaoCondicaoPagamento(getCondicaoPagamentoPlanoFinanceiroCursoVO(), Boolean.FALSE, getUsuarioLogado());
            setMensagemID("msg_dados_inativados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }

    }

    public String getResponsavelAutorizacao_Erro() {
        if (responsavelAutorizacao_Erro == null) {
            responsavelAutorizacao_Erro = "";
        }
        return responsavelAutorizacao_Erro;
    }

    public void setResponsavelAutorizacao_Erro(String responsavelAutorizacao_Erro) {
        this.responsavelAutorizacao_Erro = responsavelAutorizacao_Erro;
    }

    public List getListaSelectItemTurno() {
        if (listaSelectItemTurno == null) {
            listaSelectItemTurno = new ArrayList(0);
        }
        return (listaSelectItemTurno);
    }

    public void setListaSelectItemTurno(List listaSelectItemTurno) {
        this.listaSelectItemTurno = listaSelectItemTurno;
    }

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return (listaSelectItemUnidadeEnsino);
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }
    
    

    public List getListaSelectItemUnidadeEnsinoFinanceira() {
		if (listaSelectItemUnidadeEnsinoFinanceira == null) {
			listaSelectItemUnidadeEnsinoFinanceira = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsinoFinanceira;
	}

	public void setListaSelectItemUnidadeEnsinoFinanceira(List listaSelectItemUnidadeEnsinoFinanceira) {
		this.listaSelectItemUnidadeEnsinoFinanceira = listaSelectItemUnidadeEnsinoFinanceira;
	}

	public String getCurso_Erro() {
        if (curso_Erro == null) {
            curso_Erro = "";
        }
        return curso_Erro;
    }

    public void setCurso_Erro(String curso_Erro) {
        this.curso_Erro = curso_Erro;
    }

    public List getListaSelectItemPlanoDescontoPadrao() {
        if (listaSelectItemPlanoDescontoPadrao == null) {
            listaSelectItemPlanoDescontoPadrao = new ArrayList(0);
        }
        return (listaSelectItemPlanoDescontoPadrao);
    }

    public void setListaSelectItemPlanoDescontoPadrao(List listaSelectItemPlanoDescontoPadrao) {
        this.listaSelectItemPlanoDescontoPadrao = listaSelectItemPlanoDescontoPadrao;
    }

    public List getListaSelectItemDescontoProgressivoPadrao() {
        if (listaSelectItemDescontoProgressivoPadrao == null) {
            listaSelectItemDescontoProgressivoPadrao = new ArrayList(0);
        }
        return (listaSelectItemDescontoProgressivoPadrao);
    }

    public void setListaSelectItemDescontoProgressivoPadrao(List listaSelectItemDescontoProgressivoPadrao) {
        this.listaSelectItemDescontoProgressivoPadrao = listaSelectItemDescontoProgressivoPadrao;
    }

    public PlanoFinanceiroCursoVO getPlanoFinanceiroCursoVO() {
        if (planoFinanceiroCursoVO == null) {
            planoFinanceiroCursoVO = new PlanoFinanceiroCursoVO();
        }
        return planoFinanceiroCursoVO;
    }

    public void setPlanoFinanceiroCursoVO(PlanoFinanceiroCursoVO planoFinanceiroCursoVO) {
        this.planoFinanceiroCursoVO = planoFinanceiroCursoVO;
    }

    /*
     * Mï¿½todo responsï¿½vel por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo <code>tipoCalculoParcela</code>
     */
    public List getListaSelectItemTipoCalculoParcelaPlanoFinanceiroCurso() throws Exception {
        List objs = new ArrayList(0);
        Hashtable planoFinanceiroCursoTipoCalculoParcelas = (Hashtable) Dominios.getPlanoFinanceiroCursoTipoCalculoParcela();
        Enumeration keys = planoFinanceiroCursoTipoCalculoParcelas.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) planoFinanceiroCursoTipoCalculoParcelas.get(value);
            if (!value.equals("VD")) { // Para valor por disciplina não será possível alterar manualmente via combobox o tipoCalculoDaCondicaoPagto
                objs.add(new SelectItem(value, label));
            }
        }
        return objs;
    }

    public List getListaSelectItemModeloGeracaoParcelas() throws Exception {
        List objs = new ArrayList(0);
        Hashtable hash = (Hashtable) Dominios.getPlanoFinanceiroCursoModeloGeracaoParcelas();
        Enumeration keys = hash.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) hash.get(value);
            objs.add(new SelectItem(value, label));
        }
        return objs;
    }

    public void montarListaTextosPadroes() throws Exception {
        montarListaSelectItemTextoPadraoContratoMatricula();
        montarListaSelectItemTextoPadraoContratoFiador();
        montarListaSelectItemTextoPadraoContratoExtensao();
        //montarListaSelectItemTextoPadraoContratoModular();
		montarListaSelectItemTextoPadraoContratoInclusaoReposicao();        
        montarListaSelectItemTextoPadraoContratoAditivo();
        montarListaSelectItemTextoPadraoContratoMatriculaCondicao();
        getPlanoFinanceiroCursoVO().setTextoPadraoContratoMatricula(null);
        getPlanoFinanceiroCursoVO().setTextoPadraoContratoFiador(null);
        getPlanoFinanceiroCursoVO().setTextoPadraoContratoExtensao(null);
        //getPlanoFinanceiroCursoVO().setTextoPadraoContratoModular(null);
        getPlanoFinanceiroCursoVO().setTextoPadraoContratoAditivo(null);
        getPlanoFinanceiroCursoVO().setTextoPadraoContratoInclusaoReposicao(null);        		
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
            setListaConsultaCentroReceita(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCentroReceita(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void selecionarCentroReceita() {
        CentroReceitaVO obj = (CentroReceitaVO) context().getExternalContext().getRequestMap().get("centroReceitaItens");
        this.getCondicaoPagamentoPlanoFinanceiroCursoVO().setCentroReceita(obj);
    }
    public void limparCentroReceita() {    	
    	this.getCondicaoPagamentoPlanoFinanceiroCursoVO().setCentroReceita(null);
    }
    
    public List getTipoConsultaComboCentroReceita() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("identificadorCentroReceita", "Identificador Centro Receita"));
        itens.add(new SelectItem("nomeDepartamento", "Departamento"));
        return itens;
    }

    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        planoFinanceiroCursoVO = null;
        Uteis.liberarListaMemoria(listaSelectItemDescontoProgressivoPadrao);
        Uteis.liberarListaMemoria(listaSelectItemPlanoDescontoPadrao);
        curso_Erro = null;
        Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
        Uteis.liberarListaMemoria(listaSelectItemTurno);
        responsavelAutorizacao_Erro = null;
    }

    /**
     * @return the listaSelectItemTextoPadraoContrato
     */
    public List getListaSelectItemTextoPadraoContratoMatricula() {
        if (listaSelectItemTextoPadraoContratoMatricula == null) {
            listaSelectItemTextoPadraoContratoMatricula = new ArrayList(0);
        }
        return listaSelectItemTextoPadraoContratoMatricula;
    }

    /**
     * @param listaSelectItemTextoPadraoContrato the listaSelectItemTextoPadraoContrato to set
     */
    public void setListaSelectItemTextoPadraoContratoMatricula(List listaSelectItemTextoPadraoContratoMatricula) {
        this.listaSelectItemTextoPadraoContratoMatricula = listaSelectItemTextoPadraoContratoMatricula;
    }

    /**
     * @return the condicaoPagamentoPlanoFinanceiroCursoVO
     */
    public CondicaoPagamentoPlanoFinanceiroCursoVO getCondicaoPagamentoPlanoFinanceiroCursoVO() {
        if (condicaoPagamentoPlanoFinanceiroCursoVO == null) {
            condicaoPagamentoPlanoFinanceiroCursoVO = new CondicaoPagamentoPlanoFinanceiroCursoVO();
        }
        return condicaoPagamentoPlanoFinanceiroCursoVO;
    }

    /**
     * @param condicaoPagamentoPlanoFinanceiroCursoVO the condicaoPagamentoPlanoFinanceiroCursoVO to set
     */
    public void setCondicaoPagamentoPlanoFinanceiroCursoVO(CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVO) {
        this.condicaoPagamentoPlanoFinanceiroCursoVO = condicaoPagamentoPlanoFinanceiroCursoVO;
    }

    /**
     * @return the listaSelectItemTurma
     */
    public List<SelectItem> getListaSelectItemTurma() {
        if (listaSelectItemTurma == null) {
            listaSelectItemTurma = new ArrayList<SelectItem>();
        }
        return listaSelectItemTurma;
    }

    /**
     * @param listaSelectItemTurma the listaSelectItemTurma to set
     */
    public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
        this.listaSelectItemTurma = listaSelectItemTurma;
    }

    /**
     * @return the mostrarAlertaMudancaTipoParcela
     */
    public String getMostrarAlertaMudancaTipoParcela() {
        if (mostrarAlertaMudancaTipoParcela == null) {
            mostrarAlertaMudancaTipoParcela = "";
        }
        return mostrarAlertaMudancaTipoParcela;
    }

    /**
     * @param mostrarAlertaMudancaTipoParcela the mostrarAlertaMudancaTipoParcela to set
     */
    public void setMostrarAlertaMudancaTipoParcela(String mostrarAlertaMudancaTipoParcela) {
        this.mostrarAlertaMudancaTipoParcela = mostrarAlertaMudancaTipoParcela;
    }

    public String getTipoFormaCalculoParcelaAux() {
        if (tipoFormaCalculoParcelaAux == null) {
            tipoFormaCalculoParcelaAux = "VF";
        }
        return tipoFormaCalculoParcelaAux;
    }

    public void setTipoFormaCalculoParcelaAux(String tipoFormaCalculoParcelaAux) {
        this.tipoFormaCalculoParcelaAux = tipoFormaCalculoParcelaAux;
    }

    public String getTextoAlertaMudancaTipoParcela() {
        if (textoAlertaMudancaTipoParcela == null) {
            textoAlertaMudancaTipoParcela = "";
        }
        return textoAlertaMudancaTipoParcela;
    }

    public void setTextoAlertaMudancaTipoParcela(String textoAlertaMudancaTipoParcela) {
        this.textoAlertaMudancaTipoParcela = textoAlertaMudancaTipoParcela;
    }

    /**
     * @return the listaSelectItemTextoPadraoFiador
     */
    public List getListaSelectItemTextoPadraoContratoFiador() {
        if (listaSelectItemTextoPadraoContratoFiador == null) {
            listaSelectItemTextoPadraoContratoFiador = new ArrayList(0);
        }
        return listaSelectItemTextoPadraoContratoFiador;
    }

    /**
     * @param listaSelectItemTextoPadraoFiador the listaSelectItemTextoPadraoFiador to set
     */
    public void setListaSelectItemTextoPadraoContratoFiador(List listaSelectItemTextoPadraoContratoFiador) {
        this.listaSelectItemTextoPadraoContratoFiador = listaSelectItemTextoPadraoContratoFiador;
    }

    /**
     * @return the listaSelectItemTextoPadraoContratoMatriculaCondicao
     */
    public List getListaSelectItemTextoPadraoContratoMatriculaCondicao() {
        if (listaSelectItemTextoPadraoContratoMatriculaCondicao == null) {
            listaSelectItemTextoPadraoContratoMatriculaCondicao = new ArrayList(1);
        }
        return listaSelectItemTextoPadraoContratoMatriculaCondicao;
    }

    /**
     * @param listaSelectItemTextoPadraoContratoMatriculaCondicao the listaSelectItemTextoPadraoContratoMatriculaCondicao to set
     */
    public void setListaSelectItemTextoPadraoContratoMatriculaCondicao(List listaSelectItemTextoPadraoContratoMatriculaCondicao) {
        this.listaSelectItemTextoPadraoContratoMatriculaCondicao = listaSelectItemTextoPadraoContratoMatriculaCondicao;
    }

    public Boolean getApresentarBotaoAtivar() {
        if (getCondicaoPagamentoPlanoFinanceiroCursoVO().getSituacao().equals("EL") || getCondicaoPagamentoPlanoFinanceiroCursoVO().getSituacao().equals("IN")) {
            return true;
        }
        return false;
    }

    public Boolean getApresentarBotaoInativar() {
        if (getCondicaoPagamentoPlanoFinanceiroCursoVO().getSituacao().equals("AT") && !getCondicaoPagamentoPlanoFinanceiroCursoVO().getSituacao().equals("EL") && !getCondicaoPagamentoPlanoFinanceiroCursoVO().getSituacao().equals("IN")) {
            return true;
        }
        return false;
    }
    
    public Boolean getIsPermitirAlterarCondicaoPagamento() {        
            return getCondicaoPagamentoPlanoFinanceiroCursoVO().getSituacao().equals("EL");        
    }

    public Boolean getIsPermitirAlteracaoFormaCalculo() {
        if (getPlanoFinanceiroCursoVO().getNovoObj()) {
            return false;
        }
        return true;
    }

    /**
     * @return the criaNovaCondicaoPagamento
     */
    public Boolean getCriaNovaCondicaoPagamento() {
        if (criaNovaCondicaoPagamento == null) {
            criaNovaCondicaoPagamento = Boolean.FALSE;
        }
        return criaNovaCondicaoPagamento;
    }

    /**
     * @param criaNovaCondicaoPagamento the criaNovaCondicaoPagamento to set
     */
    public void setCriaNovaCondicaoPagamento(Boolean criaNovaCondicaoPagamento) {
        this.criaNovaCondicaoPagamento = criaNovaCondicaoPagamento;
    }

    /**
     * @return the listaSelectItemDescontoProgressivoPadraoMatricula
     */
    public List getListaSelectItemDescontoProgressivoPadraoMatricula() {
        if (listaSelectItemDescontoProgressivoPadraoMatricula == null) {
            listaSelectItemDescontoProgressivoPadraoMatricula = new ArrayList(0);
        }
        return listaSelectItemDescontoProgressivoPadraoMatricula;
    }

    /**
     * @param listaSelectItemDescontoProgressivoPadraoMatricula the listaSelectItemDescontoProgressivoPadraoMatricula to set
     */
    public void setListaSelectItemDescontoProgressivoPadraoMatricula(List listaSelectItemDescontoProgressivoPadraoMatricula) {
        this.listaSelectItemDescontoProgressivoPadraoMatricula = listaSelectItemDescontoProgressivoPadraoMatricula;
    }

    public Boolean getFecharModalCondicaoPagamento() {
        if (fecharModalCondicaoPagamento == null) {
            fecharModalCondicaoPagamento = false;
        }
        return fecharModalCondicaoPagamento;
    }

    public void setFecharModalCondicaoPagamento(Boolean fecharModalCondicaoPagamento) {
        this.fecharModalCondicaoPagamento = fecharModalCondicaoPagamento;
    }

    public void naoControlarMatriculaCondicaoPagamentoPlanoFinanceiroCurso() {
        if (this.condicaoPagamentoPlanoFinanceiroCursoVO.getNaoControlarMatricula()) {
            this.condicaoPagamentoPlanoFinanceiroCursoVO.setUtilizarValorMatriculaFixo(false);
            this.condicaoPagamentoPlanoFinanceiroCursoVO.setValorMatricula(0.0);
            this.condicaoPagamentoPlanoFinanceiroCursoVO.setValorMatriculaSistemaPorCredito(0.0);
        }
    }
    
    public void naoControlarValorParcela() {
        if (this.condicaoPagamentoPlanoFinanceiroCursoVO.getNaoControlarValorParcela()) {
            this.condicaoPagamentoPlanoFinanceiroCursoVO.setValorParcela(0.0);
            this.condicaoPagamentoPlanoFinanceiroCursoVO.setValorPrimeiraParcela(0.0);
            this.condicaoPagamentoPlanoFinanceiroCursoVO.setNrParcelasPeriodo(0);
        }
    }

    public void setListaSelectItemTextoPadraoContratoExtensao(List listaSelectItemTextoPadraoContratoExtensao) {
        this.listaSelectItemTextoPadraoContratoExtensao = listaSelectItemTextoPadraoContratoExtensao;
    }

    public List getListaSelectItemTextoPadraoContratoExtensao() {
        if (listaSelectItemTextoPadraoContratoExtensao == null) {
            listaSelectItemTextoPadraoContratoExtensao = new ArrayList(0);
        }
        return listaSelectItemTextoPadraoContratoExtensao;
    }

    public List getListaSelectItemTextoPadraoContratoAditivo() {
        if (listaSelectItemTextoPadraoContratoAditivo == null) {
            listaSelectItemTextoPadraoContratoAditivo = new ArrayList(0);
        }
        return listaSelectItemTextoPadraoContratoAditivo;
    }

    public void setListaSelectItemTextoPadraoContratoAditivo(List listaSelectItemTextoPadraoContratoAditivo) {
        this.listaSelectItemTextoPadraoContratoAditivo = listaSelectItemTextoPadraoContratoAditivo;
    }

    /**
     * @return the planoDescontoDisponivelMatriculaVO
     */
    public PlanoDescontoDisponivelMatriculaVO getPlanoDescontoDisponivelMatriculaVO() {
        if (planoDescontoDisponivelMatriculaVO == null) {
            planoDescontoDisponivelMatriculaVO = new PlanoDescontoDisponivelMatriculaVO();
        }
        return planoDescontoDisponivelMatriculaVO;
    }

    /**
     * @param planoDescontoDisponivelMatriculaVO the planoDescontoDisponivelMatriculaVO to set
     */
    public void setPlanoDescontoDisponivelMatriculaVO(PlanoDescontoDisponivelMatriculaVO planoDescontoDisponivelMatriculaVO) {
        this.planoDescontoDisponivelMatriculaVO = planoDescontoDisponivelMatriculaVO;
    }

    public List getListaSelectItemPlanoDesconto() {
        if (listaSelectItemPlanoDesconto == null) {
            listaSelectItemPlanoDesconto = new ArrayList();
        }
        return listaSelectItemPlanoDesconto;
    }

    public void setListaSelectItemPlanoDesconto(List listaSelectItemPlanoDesconto) {
        this.listaSelectItemPlanoDesconto = listaSelectItemPlanoDesconto;
    }

    private List<SelectItem> tipoConsultaComboTurma;

    public List<SelectItem> getTipoConsultaComboTurma() {
        if (tipoConsultaComboTurma == null) {
            tipoConsultaComboTurma = new ArrayList(0);
            tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
            tipoConsultaComboTurma.add(new SelectItem("nomeUnidadeEnsino", "Unidade de Ensino"));
            tipoConsultaComboTurma.add(new SelectItem("nomeTurno", "Turno"));
            tipoConsultaComboTurma.add(new SelectItem("nomeCurso", "Curso"));
        }
        return tipoConsultaComboTurma;
    }
    
    public void consultarTurmaPorIdentificadorUnico() {
        try {
            getPlanoFinanceiroCursoVO().setTurma(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getPlanoFinanceiroCursoVO().getTurma().getIdentificadorTurma(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            getPlanoFinanceiroCursoVO().setTurma(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void consultarTurma() {
        try {
            setListaConsultaTurma(getFacadeFactory().getAberturaTurmaFacade().consultarTurma(getCampoConsultaTurma(), getPlanoFinanceiroCursoVO().getUnidadeEnsino(), getValorConsultaTurma(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaTurma(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void selecionarTurma() throws Exception {
        limparTurma();
        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
        getPlanoFinanceiroCursoVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
        obj = null;
        valorConsultaTurma = "";
        campoConsultaTurma = "";
        listaConsultaTurma.clear();
    }

    public void limparTurma() {
        getPlanoFinanceiroCursoVO().setTurma(null);
        getPlanoFinanceiroCursoVO().getCondicaoPagamentoPlanoFinanceiroCursoVOs().clear();
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

    public List getListaConsultaTurma() {
        if (listaConsultaTurma == null) {
            listaConsultaTurma = new ArrayList(0);
        }
        return listaConsultaTurma;
    }

    public void setListaConsultaTurma(List listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
    }

    public Boolean getApresentarBotaoAdicionarCondicaoPagamento(){
        return (getPlanoFinanceiroCursoVO().getIsValorPorDisciplina() && getPlanoFinanceiroCursoVO().getTurma().getCodigo()>0) || !getPlanoFinanceiroCursoVO().getIsValorPorDisciplina();
    }
    
    public List<CentroReceitaVO> getListaConsultaCentroReceita() {
        return listaConsultaCentroReceita;
    }

    public void setListaConsultaCentroReceita(List<CentroReceitaVO> listaConsultaCentroReceita) {
        this.listaConsultaCentroReceita = listaConsultaCentroReceita;
    }
    
    public String getValorConsultaCentroReceita() {
        return valorConsultaCentroReceita;
    }

    public void setValorConsultaCentroReceita(String valorConsultaCentroReceita) {
        this.valorConsultaCentroReceita = valorConsultaCentroReceita;
    }
    
    public String getCampoConsultaCentroReceita() {
        return campoConsultaCentroReceita;
    }

    public void setCampoConsultaCentroReceita(String campoConsultaCentroReceita) {
        this.campoConsultaCentroReceita = campoConsultaCentroReceita;
    }

	public List getListaSelectItemDescontoProgresivo() {
		if (listaSelectItemDescontoProgresivo == null) {
			listaSelectItemDescontoProgresivo = new ArrayList(0);
		}
		return listaSelectItemDescontoProgresivo;
	}

	public void setListaSelectItemDescontoProgresivo(List listaSelectItemDescontoProgresivo) {
		this.listaSelectItemDescontoProgresivo = listaSelectItemDescontoProgresivo;
	}

	public CondicaoPagamentoPlanoDescontoVO getCondicaoPagamentoPlanoDescontoVO() {
		if (condicaoPagamentoPlanoDescontoVO == null) {
			condicaoPagamentoPlanoDescontoVO = new CondicaoPagamentoPlanoDescontoVO();
		}
		return condicaoPagamentoPlanoDescontoVO;
	}

	public void setCondicaoPagamentoPlanoDescontoVO(CondicaoPagamentoPlanoDescontoVO condicaoPagamentoPlanoDescontoVO) {
		this.condicaoPagamentoPlanoDescontoVO = condicaoPagamentoPlanoDescontoVO;
	}

    /**
     * @return the listaSelectTipoUsoCondicacaoPagamento
     */
    public List<SelectItem> getListaSelectTipoUsoCondicacaoPagamento() {
        if (listaSelectTipoUsoCondicacaoPagamento == null) {
            listaSelectTipoUsoCondicacaoPagamento = new ArrayList();
            listaSelectTipoUsoCondicacaoPagamento.add(new SelectItem("MATRICULA_REGULAR", "Matrícula Regular"));
            listaSelectTipoUsoCondicacaoPagamento.add(new SelectItem("MATRICULA_ESPECIAL", "Matrícula Especial"));
        }
        return listaSelectTipoUsoCondicacaoPagamento;
    }

    /**
     * @param listaSelectTipoUsoCondicacaoPagamento the listaSelectTipoUsoCondicacaoPagamento to set
     */
    public void setListaSelectTipoUsoCondicacaoPagamento(List<SelectItem> listaSelectTipoUsoCondicacaoPagamento) {
        this.listaSelectTipoUsoCondicacaoPagamento = listaSelectTipoUsoCondicacaoPagamento;
    }
    
    public Boolean getIsCondicaoValorFixo() {
        if (this.getCondicaoPagamentoPlanoFinanceiroCursoVO().getTipoCalculoParcela().equals("VF")) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
    
    public Boolean getIsApresentarTipoUsoCondicaoPagamentoMatriculaRegular() {
        if (this.getCondicaoPagamentoPlanoFinanceiroCursoVO().getTipoUsoCondicaoPagamento().equals("MATRICULA_REGULAR")) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * @return the listaSelectTipoCobrancaCreditoValor
     */
    public List<SelectItem> getListaSelectTipoCobrancaCreditoValor() {
        if (listaSelectTipoCobrancaCreditoValor == null) {
            listaSelectTipoCobrancaCreditoValor = new ArrayList();
            listaSelectTipoCobrancaCreditoValor.add(new SelectItem("VF", "Valor Fixo"));
            listaSelectTipoCobrancaCreditoValor.add(new SelectItem("VC", "Nr. Créditos"));
        }
        return listaSelectTipoCobrancaCreditoValor;
    }

    /**
     * @param listaSelectTipoCobrancaCreditoValor the listaSelectTipoCobrancaCreditoValor to set
     */
    public void setListaSelectTipoCobrancaCreditoValor(List<SelectItem> listaSelectTipoCobrancaCreditoValor) {
        this.listaSelectTipoCobrancaCreditoValor = listaSelectTipoCobrancaCreditoValor;
    }
    
    public void atualizarValorCobrarMatriculaEspecial() {
        this.getCondicaoPagamentoPlanoFinanceiroCursoVO().atualizarValorCobrarPorAtividadeComplementar();
        this.getCondicaoPagamentoPlanoFinanceiroCursoVO().atualizarValorCobrarPorENADE();
        this.getCondicaoPagamentoPlanoFinanceiroCursoVO().atualizarValorCobrarPorEstagio();
    }
    
    public Boolean getUtilizarTipoCobrancaPorCreditoParaMatriculaEspecial() {
        return this.getCondicaoPagamentoPlanoFinanceiroCursoVO().getUtilizaTipoCobrancaPorCreditoParaMatriculaEspecial();
    }

    /**
     * @return the listaSelectTipoCobrancaCreditoValorHora
     */
    public List<SelectItem> getListaSelectTipoCobrancaCreditoValorHora() {
        if (listaSelectTipoCobrancaCreditoValorHora == null) {
            listaSelectTipoCobrancaCreditoValorHora = new ArrayList();
            listaSelectTipoCobrancaCreditoValorHora.add(new SelectItem("VF", "Valor Fixo"));
            listaSelectTipoCobrancaCreditoValorHora.add(new SelectItem("VC", "Valor Por Crédito"));
            listaSelectTipoCobrancaCreditoValorHora.add(new SelectItem("VH", "Valor Por Hora Disciplina"));
        }
        return listaSelectTipoCobrancaCreditoValorHora;
    }

    /**
     * @param listaSelectTipoCobrancaCreditoValorHora the listaSelectTipoCobrancaCreditoValorHora to set
     */
    public void setListaSelectTipoCobrancaCreditoValorHora(List<SelectItem> listaSelectTipoCobrancaCreditoValorHora) {
        this.listaSelectTipoCobrancaCreditoValorHora = listaSelectTipoCobrancaCreditoValorHora;
    }
    
    public void atualizarTipoCobrancaInclusaoDisciplinaRegular() {
        if (this.getCondicaoPagamentoPlanoFinanceiroCursoVO().getTipoCobrancaInclusaoDisciplinaRegular().equals("CR")) {
            this.getCondicaoPagamentoPlanoFinanceiroCursoVO().setValorFixoDisciplinaIncluida(this.getCondicaoPagamentoPlanoFinanceiroCursoVO().getValorUnitarioCredito());
            this.getCondicaoPagamentoPlanoFinanceiroCursoVO().setValorFixoDisciplinaIncluidaEAD(this.getCondicaoPagamentoPlanoFinanceiroCursoVO().getValorUnitarioCredito());
        } else {
            this.getCondicaoPagamentoPlanoFinanceiroCursoVO().setValorFixoDisciplinaIncluida(0.0);
            this.getCondicaoPagamentoPlanoFinanceiroCursoVO().setValorFixoDisciplinaIncluidaEAD(0.0);
        }
    }
    
    public void atualizarTipoCobrancaExclusaoDisciplinaRegular() {
    	if (this.getCondicaoPagamentoPlanoFinanceiroCursoVO().getTipoCobrancaExclusaoDisciplinaRegular().equals("CR")) {
    		this.getCondicaoPagamentoPlanoFinanceiroCursoVO().setValorDescontoDisciplinaExcluida(this.getCondicaoPagamentoPlanoFinanceiroCursoVO().getValorUnitarioCredito());
    		this.getCondicaoPagamentoPlanoFinanceiroCursoVO().setValorFixoDisciplinaExcluidaEAD(this.getCondicaoPagamentoPlanoFinanceiroCursoVO().getValorUnitarioCredito());
    	} else {
    		this.getCondicaoPagamentoPlanoFinanceiroCursoVO().setValorDescontoDisciplinaExcluida(0.0);
    		this.getCondicaoPagamentoPlanoFinanceiroCursoVO().setValorFixoDisciplinaExcluidaEAD(0.0);
    	}
    }
    
    public void atualizarTipoCobrancaInclusaoDisciplinaDependencia() {
        if (this.getCondicaoPagamentoPlanoFinanceiroCursoVO().getTipoCobrancaInclusaoDisciplinaDependencia().equals("CR")) {
            this.getCondicaoPagamentoPlanoFinanceiroCursoVO().setValorDisciplinaIncluidaDependencia(this.getCondicaoPagamentoPlanoFinanceiroCursoVO().getValorUnitarioCredito());
            this.getCondicaoPagamentoPlanoFinanceiroCursoVO().setValorDisciplinaIncluidaDependenciaEAD(this.getCondicaoPagamentoPlanoFinanceiroCursoVO().getValorUnitarioCredito());
        } else {
            this.getCondicaoPagamentoPlanoFinanceiroCursoVO().setValorDisciplinaIncluidaDependencia(0.0);
            this.getCondicaoPagamentoPlanoFinanceiroCursoVO().setValorDisciplinaIncluidaDependenciaEAD(0.0);
        }
    }
    
    public void atualizarTipoDescontoDisciplinaRegularEAD() {
        if (this.getCondicaoPagamentoPlanoFinanceiroCursoVO().getTipoDescontoDisciplinaRegularEAD().equals("CR")) {
            this.getCondicaoPagamentoPlanoFinanceiroCursoVO().setValorDescontoDisciplinaRegularEAD(this.getCondicaoPagamentoPlanoFinanceiroCursoVO().getValorUnitarioCredito());
        } else {
            this.getCondicaoPagamentoPlanoFinanceiroCursoVO().setValorDescontoDisciplinaRegularEAD(0.0);
        }
    }
    
    public void atualizarTipoCobrancaInclusaoDisciplinaOptativa() {
        if (this.getCondicaoPagamentoPlanoFinanceiroCursoVO().getTipoCobrancaDisciplinaOptativa().equals("CR")) {
            this.getCondicaoPagamentoPlanoFinanceiroCursoVO().setValorDisciplinaOptativa(this.getCondicaoPagamentoPlanoFinanceiroCursoVO().getValorUnitarioCredito());
            this.getCondicaoPagamentoPlanoFinanceiroCursoVO().setValorDisciplinaOptativaEAD(this.getCondicaoPagamentoPlanoFinanceiroCursoVO().getValorUnitarioCredito());
        } else {
            this.getCondicaoPagamentoPlanoFinanceiroCursoVO().setValorDisciplinaOptativa(0.0);
            this.getCondicaoPagamentoPlanoFinanceiroCursoVO().setValorDisciplinaOptativaEAD(0.0);
        }
    }

    /**
     * @return the listaSelectTipoDescontoDisciplinaEAD
     */
    public List<SelectItem> getListaSelectTipoDescontoDisciplinaEAD() {
        if (listaSelectTipoDescontoDisciplinaEAD == null) {
            listaSelectTipoDescontoDisciplinaEAD = new ArrayList();
            listaSelectTipoDescontoDisciplinaEAD.add(new SelectItem("PE", "% sobre Valor Disciplina"));
            listaSelectTipoDescontoDisciplinaEAD.add(new SelectItem("VF", "Valor Fixo"));
            listaSelectTipoDescontoDisciplinaEAD.add(new SelectItem("VC", "Valor Por Créditos"));
            listaSelectTipoDescontoDisciplinaEAD.add(new SelectItem("VH", "Valor Por Hora Disciplina"));
        }
        return listaSelectTipoDescontoDisciplinaEAD;
    }

    /**
     * @param listaSelectTipoDescontoDisciplinaEAD the listaSelectTipoDescontoDisciplinaEAD to set
     */
    public void setListaSelectTipoDescontoDisciplinaEAD(List<SelectItem> listaSelectTipoDescontoDisciplinaEAD) {
        this.listaSelectTipoDescontoDisciplinaEAD = listaSelectTipoDescontoDisciplinaEAD;
    }
    
	public List<SelectItem> getListaSelectItemSemestre() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("", ""));
		objs.add(new SelectItem("1", "1º"));
		objs.add(new SelectItem("2", "2º"));
		return objs;
	}
        
    public List<SelectItem> getListaSelectTipoControleAlunoIntegralizandoCurso() {
        if (listaSelectTipoControleAlunoIntegralizandoCurso == null) {
            listaSelectTipoControleAlunoIntegralizandoCurso = new ArrayList();
            listaSelectTipoControleAlunoIntegralizandoCurso.add(new SelectItem("PC", "% Crédito Pendente Último Período"));
            listaSelectTipoControleAlunoIntegralizandoCurso.add(new SelectItem("PH", "% CH Pendente Último Período"));
            listaSelectTipoControleAlunoIntegralizandoCurso.add(new SelectItem("CH", "CH Pendente"));
            listaSelectTipoControleAlunoIntegralizandoCurso.add(new SelectItem("CR", "Nr. Créditos Pendentes"));
        }
        return listaSelectTipoControleAlunoIntegralizandoCurso;
    }        


    public void montarListaSelectItemTextoPadraoContratoInclusaoReposicao() throws Exception {
    	List<TextoPadraoVO> textoPadraoVOInclusaoReposicao = consultarTextoPadraoPorTipo(TipoTextoPadrao.INCLUSAO_REPOSICAO.getValor());
    	setListaSelectItemTextoPadraoContratoInclusaoReposicao(UtilSelectItem.getListaSelectItem(textoPadraoVOInclusaoReposicao, "codigo", "descricao"));
    }
    
    public List getListaSelectItemTextoPadraoContratoInclusaoReposicao() {
    	if (listaSelectItemTextoPadraoContratoInclusaoReposicao == null) {
    		listaSelectItemTextoPadraoContratoInclusaoReposicao = new ArrayList(0);
    	}
    	return listaSelectItemTextoPadraoContratoInclusaoReposicao;
    }
    
    public void setListaSelectItemTextoPadraoContratoInclusaoReposicao(List listaSelectItemTextoPadraoContratoInclusaoReposicao) {
    	this.listaSelectItemTextoPadraoContratoInclusaoReposicao = listaSelectItemTextoPadraoContratoInclusaoReposicao;
    }
            
    private String filtroCondicaoAtiva;
	private String filtroCondicaoInativa;
	private String filtroCondicaoConstrucao;

	public boolean filtrarNomeCondicaoAtiva(Object obj) {
		if (!getFiltroCondicaoAtiva().trim().isEmpty() && obj instanceof CondicaoPagamentoPlanoFinanceiroCursoVO) {
			return ((CondicaoPagamentoPlanoFinanceiroCursoVO) obj).getSituacao().equals("AT") && Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(((CondicaoPagamentoPlanoFinanceiroCursoVO) obj).getDescricao())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltroCondicaoAtiva().toUpperCase().trim()))); 			
		} 
		return ((CondicaoPagamentoPlanoFinanceiroCursoVO) obj).getSituacao().equals("AT");				
	}
	public boolean filtrarNomeCondicaoInativa(Object obj) {
		if (!getFiltroCondicaoAtiva().trim().isEmpty() && obj instanceof CondicaoPagamentoPlanoFinanceiroCursoVO) {
			return ((CondicaoPagamentoPlanoFinanceiroCursoVO) obj).getSituacao().equals("IN") && Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(((CondicaoPagamentoPlanoFinanceiroCursoVO) obj).getDescricao())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltroCondicaoInativa().toUpperCase().trim()))); 			
		} 
		return ((CondicaoPagamentoPlanoFinanceiroCursoVO) obj).getSituacao().equals("IN");				
	}
	public boolean filtrarNomeCondicaoConstrucao(Object obj) {
		if (!getFiltroCondicaoAtiva().trim().isEmpty() && obj instanceof CondicaoPagamentoPlanoFinanceiroCursoVO) {
			return ((CondicaoPagamentoPlanoFinanceiroCursoVO) obj).getSituacao().equals("EL") && Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(((CondicaoPagamentoPlanoFinanceiroCursoVO) obj).getDescricao())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltroCondicaoConstrucao().toUpperCase().trim()))); 			
		} 
		return ((CondicaoPagamentoPlanoFinanceiroCursoVO) obj).getSituacao().equals("EL");				
	}

	/**
	 * @return the filtroCondicaoAtiva
	 */
	public String getFiltroCondicaoAtiva() {
		if (filtroCondicaoAtiva == null) {
			filtroCondicaoAtiva = "";
		}
		return filtroCondicaoAtiva;
	}

	/**
	 * @param filtroCondicaoAtiva
	 *            the filtroCondicaoAtiva to set
	 */
	public void setFiltroCondicaoAtiva(String filtroCondicaoAtiva) {
		this.filtroCondicaoAtiva = filtroCondicaoAtiva;
	}

	/**
	 * @return the filtroCondicaoInativa
	 */
	public String getFiltroCondicaoInativa() {
		if (filtroCondicaoInativa == null) {
			filtroCondicaoInativa = "";
		}
		return filtroCondicaoInativa;
	}

	/**
	 * @param filtroCondicaoInativa
	 *            the filtroCondicaoInativa to set
	 */
	public void setFiltroCondicaoInativa(String filtroCondicaoInativa) {
		this.filtroCondicaoInativa = filtroCondicaoInativa;
	}

	/**
	 * @return the filtroCondicaoConstrucao
	 */
	public String getFiltroCondicaoConstrucao() {
		if (filtroCondicaoConstrucao == null) {
			filtroCondicaoConstrucao = "";
		}
		return filtroCondicaoConstrucao;
	}

	/**
	 * @param filtroCondicaoConstrucao
	 *            the filtroCondicaoConstrucao to set
	 */
	public void setFiltroCondicaoConstrucao(String filtroCondicaoConstrucao) {
		this.filtroCondicaoConstrucao = filtroCondicaoConstrucao;
	}
	
	public void realizarAtivacaoPlanoFinanceiroCurso() {
		try {
			getFacadeFactory().getPlanoFinanceiroCursoFacade().realizarAtivacaoPlanoFinanceiroCurso(getPlanoFinanceiroCursoVO(), getUsuarioLogado());
			setMensagemID("msg_dados_ativados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarInativacaoPlanoFinanceiroCurso() {
		try {
			getFacadeFactory().getPlanoFinanceiroCursoFacade().realizarInativacaoPlanoFinanceiroCurso(getPlanoFinanceiroCursoVO(), getUsuarioLogado());
			setMensagemID("msg_dados_inativado");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List getListaSelectItemSituacaoPlanoFinanceiroVOs() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem(SituacaoPlanoFinanceiroCursoEnum.TODAS, "Todas"));
        itens.add(new SelectItem(SituacaoPlanoFinanceiroCursoEnum.ATIVO, "Ativo"));
        itens.add(new SelectItem(SituacaoPlanoFinanceiroCursoEnum.INATIVO, "Inativo"));
        itens.add(new SelectItem(SituacaoPlanoFinanceiroCursoEnum.EM_CONSTRUCAO, "Em Construção"));
        return itens;
    }

	public SituacaoPlanoFinanceiroCursoEnum getSituacaoPlanoFinanceiroCurso() {
		if (situacaoPlanoFinanceiroCurso == null) {
			situacaoPlanoFinanceiroCurso = SituacaoPlanoFinanceiroCursoEnum.TODAS;
		}
		return situacaoPlanoFinanceiroCurso;
	}

	public void setSituacaoPlanoFinanceiroCurso(SituacaoPlanoFinanceiroCursoEnum situacaoPlanoFinanceiroCurso) {
		this.situacaoPlanoFinanceiroCurso = situacaoPlanoFinanceiroCurso;
	}
}