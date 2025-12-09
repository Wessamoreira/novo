package controle.financeiro;

import java.io.File;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas contaCorrenteForm.jsp
 * contaCorrenteCons.jsp) com as funcionalidades da classe <code>ContaCorrente</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see ContaCorrente
 * @see ContaCorrenteVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.academico.ExpedicaoDiplomaControle;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.AgenciaVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.LogContaCorrenteVO;
import negocio.comuns.financeiro.UnidadeEnsinoContaCorrenteVO;
import negocio.comuns.financeiro.enumerador.TipoContaCorrenteEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.Bancos;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.Assinador;
import controle.arquitetura.SuperControle.MSG_TELA;

import java.io.File;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas contaCorrenteForm.jsp
 * contaCorrenteCons.jsp) com as funcionalidades da classe <code>ContaCorrente</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see ContaCorrente
 * @see ContaCorrenteVO
 */
import java.io.Serializable;
import negocio.comuns.financeiro.LogContaCorrenteVO;
import negocio.comuns.utilitarias.UtilSelectItem;


@Controller("ContaCorrenteControle")
@Scope("viewScope")
@Lazy
public class ContaCorrenteControle extends SuperControle implements Serializable {

    private ContaCorrenteVO contaCorrenteVO;
    private List listaSelectItemAgencia;
    private List listaSelectItemUnidadeEnsino;
    private String campoConsultaFuncionario;
    private String valorConsultaFuncionario;
    private List listaConsultaFuncionario;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private Boolean apresentarMensagemAlteracao;
    private StringBuilder mensagemAlteracaoContaCorrente = new StringBuilder();
    private String mensagemApresentarAlteracao;
	protected String campoConsultaCidade;
	protected String valorConsultaCidade;
	protected List listaConsultaCidade;
	private List listaSelectItemFormaPagamento;
	private List listaSelectItemCategoriaDespesa;
	private Boolean marcarTodosTipoOrigem;
	private String situacaoContaCorrenteFiltro;
	private TipoContaCorrenteEnum tipoContaCorrenteFiltro;
	private UnidadeEnsinoVO unidadeEnsinoCorrenteFiltro;
	private Double saldoContaCorrente;
	private List<SelectItem> listaSelectItemFormaPagamentoPix;

    
    public ContaCorrenteControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        getControleConsulta().setCampoConsulta("numero");
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>ContaCorrente</code> para edição pelo
     * usuário da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setContaCorrenteVO(new ContaCorrenteVO());
        inicializarListasSelectItemTodosComboBox();
        realizarSelecaoCheckboxMarcarDesmarcarTodosTipoOrigem();
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("contaCorrenteForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>ContaCorrente</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
     * disponibilizá-lo para edição.
     */
    public String editar() {
        ContaCorrenteVO obj = (ContaCorrenteVO) context().getExternalContext().getRequestMap().get("contaCorrenteItens");
        try {
            obj.setNovoObj(Boolean.FALSE);
            setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimariaUnica(obj.getCodigo(), false,Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            inicializarListasSelectItemTodosComboBox();
            verificarTipoOrigemContaCorrente();
            atualizarDadosSaldoContaCorrente();
            setMensagemID("msg_dados_editar");
            realizarObtencaoDataVencimentoCertificado();
            return Uteis.getCaminhoRedirecionamentoNavegacao("contaCorrenteForm.xhtml");
        } catch (Exception e) {        	
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        } finally {
            obj = null;
        }
    }
    

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>ContaCorrente</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            LogContaCorrenteVO obj = new LogContaCorrenteVO();
            getFacadeFactory().getContaCorrenteFacade().inicializarDadosBancoContaCorrente(contaCorrenteVO, getUsuarioLogado());
            if (contaCorrenteVO.isNovoObj().booleanValue()) {
                List contas = new ArrayList(0);
                contas = getFacadeFactory().getContaCorrenteFacade().consultarPorCodigo(0, 0, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
                        getUsuarioLogado());
                Iterator i = contas.iterator();
                while (i.hasNext()) {
                    ContaCorrenteVO conta = (ContaCorrenteVO) i.next();
                    if (contaCorrenteVO.getContaCaixa().booleanValue()) {
                        if (contaCorrenteVO.getNumero().equals(conta.getNumero()) && contaCorrenteVO.getDigito().equals(conta.getDigito())) {
                            throw new ConsistirException("Conta Caixa já existe");
                        }
                    } else {
                        if (contaCorrenteVO.getAgencia().getCodigo().intValue() == conta.getAgencia().getCodigo().intValue()
                                && contaCorrenteVO.getNumero().equals(conta.getNumero()) && contaCorrenteVO.getDigito().equals(conta.getDigito()) && contaCorrenteVO.getCarteira().equals(conta.getCarteira())) {
                            throw new ConsistirException("Conta Corrente já existe");
                        }
                    }
                }
                getFacadeFactory().getContaCorrenteFacade().incluir(contaCorrenteVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
                obj = preencherLogContaCorrente("inclusao");
                getFacadeFactory().getLogContaCorrenteFacade().incluir(obj, getUsuarioLogado());
            } else {
                getFacadeFactory().getContaCorrenteFacade().alterar(contaCorrenteVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
                obj = preencherLogContaCorrente("alteracao");
                getAplicacaoControle().atualizarContaCorrenteConfiguracaoFinanceiraEmNivelAplicacao(contaCorrenteVO);
                getAplicacaoControle().removerContaCorrente(contaCorrenteVO.getCodigo());
                getFacadeFactory().getLogContaCorrenteFacade().incluir(obj, getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("contaCorrenteForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("contaCorrenteForm.xhtml");
        }
    }

    public void persistir() {
        try {
            if (contaCorrenteVO.isNovoObj().booleanValue()) {
                gravar();
            } else if (!getContaCorrenteVO().isNovoObj().booleanValue() && !getContaCorrenteVO().getContaCaixa()) {
                mensagemAlteracaoContaCorrente = new StringBuilder();
                setApresentarMensagemAlteracao(getFacadeFactory().getContaCorrenteFacade().apreasentarMensagemAlteracaoContaCorrenteMudancaCarteira(getContaCorrenteVO(), mensagemAlteracaoContaCorrente, getUsuarioLogado()));
                setMensagemApresentarAlteracao(mensagemAlteracaoContaCorrente.toString());
                if (!getApresentarMensagemAlteracao()) {
                    gravar();
                }
            } else if (!getContaCorrenteVO().isNovoObj().booleanValue() && getContaCorrenteVO().getContaCaixa()) {
                gravar();
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String getApresentarMensagemAlteracaoContaCorrente() {
        if (getApresentarMensagemAlteracao()) {
            return "RichFaces.$('panelMensagemContaCorrente').show();";
        }
        return "";
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ContaCorrenteCons.jsp. Define o tipo de consulta
     * a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getContaCorrenteFacade().consultarPorCodigo(new Integer(valorInt), 0, false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("numero")) {
                objs = getFacadeFactory().getContaCorrenteFacade().consultarPorNumero(getControleConsulta().getValorConsulta(), getSituacaoContaCorrenteFiltro(), getTipoContaCorrenteFiltro(), getUnidadeEnsinoCorrenteFiltro().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("dataAbertura")) {
                objs = getFacadeFactory().getContaCorrenteFacade().consultarPorDataAbertura(
                        Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0),
                        Uteis.getDateTime(getControleConsulta().getDataIni(), 23, 59, 59), getSituacaoContaCorrenteFiltro(), getTipoContaCorrenteFiltro(),  getUnidadeEnsinoCorrenteFiltro().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
                        getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("numeroAgenciaAgencia")) {
                objs = getFacadeFactory().getContaCorrenteFacade().consultarPorNumeroAgenciaAgencia(getControleConsulta().getValorConsulta(), getSituacaoContaCorrenteFiltro(), getTipoContaCorrenteFiltro(), getUnidadeEnsinoCorrenteFiltro().getCodigo(),
                        false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            /*if (getControleConsulta().getCampoConsulta().equals("nomeUnidadeEnsino")) {
                objs = getFacadeFactory().getContaCorrenteFacade().consultarPorNomeUnidadeEnsino(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }*/
            if (getControleConsulta().getCampoConsulta().equals("banco")) {
            	objs = getFacadeFactory().getContaCorrenteFacade().consultarPorNomeBanco(getControleConsulta().getValorConsulta(), getSituacaoContaCorrenteFiltro(), getTipoContaCorrenteFiltro(),  getUnidadeEnsinoCorrenteFiltro().getCodigo(),
            			false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeApresentacao")) {
            	objs = getFacadeFactory().getContaCorrenteFacade().consultarPorNomeApresentacao(getControleConsulta().getValorConsulta(), getSituacaoContaCorrenteFiltro(), getTipoContaCorrenteFiltro(),  getUnidadeEnsinoCorrenteFiltro().getCodigo(),
            			false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            
            /*if (getControleConsulta().getCampoConsulta().equals("tipoContaCorrente")) {
            	objs = getFacadeFactory().getContaCorrenteFacade().consultarPorTipoContaCorrente(getContaCorrenteVO().getTipoContaCorrenteEnum(), getUnidadeEnsinoLogado().getCodigo(),
            			false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }*/
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("contaCorrenteCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("contaCorrenteCons.xhtml");
        }
    }

    public LogContaCorrenteVO preencherLogContaCorrente(String acao) throws Exception {
        LogContaCorrenteVO obj = new LogContaCorrenteVO();
        obj.setContaCorrente(getContaCorrenteVO().getCodigo());
        obj.setDataAbertura(getContaCorrenteVO().getDataAbertura());
        if (getContaCorrenteVO().getAgencia().getCodigo() != 0) {
            obj.setAgencia(getFacadeFactory().getAgenciaFacade().consultarPorChavePrimaria(getContaCorrenteVO().getAgencia().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()).getNumeroAgencia());
        }
        obj.setCarteira(getContaCorrenteVO().getCarteira());
        obj.setSaldo(getContaCorrenteVO().getSaldo());
        obj.setNumero(getContaCorrenteVO().getNumero());
        obj.setDigito(getContaCorrenteVO().getDigito());
        obj.setConvenio(getContaCorrenteVO().getConvenio());
        obj.setCodigoCedente(getContaCorrenteVO().getCodigoCedente());
        obj.setContaCaixa(getContaCorrenteVO().getContaCaixa());
        obj.setAcao(acao);
        return obj;
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>ContaCorrenteVO</code> Após a exclusão
     * ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            LogContaCorrenteVO obj = new LogContaCorrenteVO();
            obj = preencherLogContaCorrente("exclusao");
            getFacadeFactory().getContaCorrenteFacade().excluir(contaCorrenteVO, getUsuarioLogado());
            getFacadeFactory().getLogContaCorrenteFacade().incluir(obj, getUsuarioLogado());
            setContaCorrenteVO(new ContaCorrenteVO());
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("contaCorrenteForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("contaCorrenteForm.xhtml");
        }
    }

    public void irPaginaInicial() throws Exception {
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
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>UnidadeEnsino</code>.
     */
    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>. Buscando todos os
     * objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false,
                Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Agencia</code>.
     */
    public void montarListaSelectItemAgencia(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarAgenciaPorNumeroAgencia(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                AgenciaVO obj = (AgenciaVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getBanco().getNome() + " - " + obj.getNumeroAgencia().toString() + "-"
                        + obj.getDigito().toString()));
            }
            setListaSelectItemAgencia(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Agencia</code>. Buscando todos os objetos
     * correspondentes a entidade <code>Agencia</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto
     * é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemAgencia() {
        try {
            montarListaSelectItemAgencia("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>numeroAgencia</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarAgenciaPorNumeroAgencia(String numeroAgenciaPrm) throws Exception {
        List lista = getFacadeFactory().getAgenciaFacade().consultarPorNumeroAgencia(numeroAgenciaPrm, false, Uteis.NIVELMONTARDADOS_TODOS,
                getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemAgencia();
        montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemFormaPagamento();      
        montarListaSelectItemFormaPagamentoFix();
        montarListaSelectItemCategoriaDespesa();
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("numero", "Número"));
        itens.add(new SelectItem("dataAbertura", "Data Abertura"));
        itens.add(new SelectItem("numeroAgenciaAgencia", "Agência"));
        itens.add(new SelectItem("banco", "Banco"));
        itens.add(new SelectItem("nomeApresentacao", "Nome Apresentação"));
        return itens;
    }
    
    public List getTipoComboSituacao() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("AT", "Ativa"));
        itens.add(new SelectItem("IN", "Inativa"));
        return itens;
    }
    
  

    public boolean isCampoData() {
        return getControleConsulta().getCampoConsulta().equals("dataAbertura");
    }
    
    public boolean isCampoTipoContaCorrente() {
    	return getControleConsulta().getCampoConsulta().equals("tipoContaCorrente");
    }

    public void consultarFuncionario() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaFuncionario().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");
            if (getCampoConsultaFuncionario().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(),
                        TipoPessoa.FUNCIONARIO.getValor(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
                        getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(),
                        this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaFuncionario(objs);
            executarMetodoControle(ExpedicaoDiplomaControle.class.getSimpleName(), "setMensagemID", "msg_dados_consultados");
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaFuncionario(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarFuncionario() {
        try {
            FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
            getContaCorrenteVO().setFuncionarioResponsavel(
                    getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getCodigo(), obj.getUnidadeEnsino().getCodigo(), false,
                    Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            getListaConsultaFuncionario().clear();
            setCampoConsultaFuncionario("");
            setValorConsultaFuncionario("");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getContaCorrenteVO().getArquivoCertificadoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.CERTIFICADO, getUsuarioLogado());
			realizarObtencaoDataVencimentoCertificado();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public void upLoadArquivoUnidCert(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getContaCorrenteVO().getArquivoUnidadeCertificadoraVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.CERTIFICADO, getUsuarioLogado());			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

    public void limparDadosFuncionario() throws Exception {
        getContaCorrenteVO().setFuncionarioResponsavel(new FuncionarioVO());
        getContaCorrenteVO().setRequerConfirmacaoMovimentacaoFinanceira(false);
        getListaConsultaFuncionario().clear();
    }

    public List getTipoConsultaComboFuncionario() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        return itens;
    }

	public void consultarCidade() {
		try {
			setMensagemID("");
			List objs = new ArrayList(0);
			if (getCampoConsultaCidade().equals("codigo")) {
				if (getValorConsultaCidade().equals("")) {
					setMensagemID("msg_entre_prmconsulta");
					setValorConsultaCidade("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCidade());
				objs = getFacadeFactory().getCidadeFacade().consultaRapidaPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
			}
			if (getCampoConsultaCidade().equals("nome")) {
				objs = getFacadeFactory().getCidadeFacade().consultaRapidaPorNome(getValorConsultaCidade(), false, getUsuarioLogado());
			}

			setListaConsultaCidade(objs);
			if (getMensagemID().equals("")) {
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			setListaConsultaCidade(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	
	public void selecionarCidade() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItens");
		getContaCorrenteVO().setCidade(obj);
		listaConsultaCidade.clear();
		this.setValorConsultaCidade("");
		this.setCampoConsultaCidade("");
	}

	public List getTipoConsultaCidade() {
		List itens = new ArrayList(0);		
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void carregarEnderecoPessoa() {
		try {
			//getFacadeFactory().getEnderecoFacade().carregarEndereco(unidadeEnsinoVO, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setControleConsulta(new ControleConsulta());
        getControleConsulta().setCampoConsulta("numero");
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("contaCorrenteCons.xhtml");
    }

    public List getListaSelectItemUnidadeEnsino() {
        return (listaSelectItemUnidadeEnsino);
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public List getListaSelectItemAgencia() {
        return (listaSelectItemAgencia);
    }

    public void setListaSelectItemAgencia(List listaSelectItemAgencia) {
        this.listaSelectItemAgencia = listaSelectItemAgencia;
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

    public List getListaConsultaFuncionario() {
        if (listaConsultaFuncionario == null) {
            listaConsultaFuncionario = new ArrayList(0);
        }
        return listaConsultaFuncionario;
    }

    public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
        this.listaConsultaFuncionario = listaConsultaFuncionario;
    }

    public String getStyleClassCarteiraConvenio() {
        if (getContaCorrenteVO().getContaCaixa()) {
            return "form-control  campos";
        } else {
            return "form-control camposObrigatorios";
        }
    }

    public void adicionarTodasUnidadesEnsinoContaCorrente() {
        try {
        	getFacadeFactory().getUnidadeEnsinoContaCorrenteFacade().adicionarTodasUnidadesEnsinoContaCorrente(getContaCorrenteVO(), super.getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            setUnidadeEnsinoVO(null);
        }
    }

    public void adicionarUnidadeEnsinoContaCorrente() {
        UnidadeEnsinoContaCorrenteVO unidadeEnsinoContaCorrenteVO = new UnidadeEnsinoContaCorrenteVO();
        try {
            setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false,
                    Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
            unidadeEnsinoContaCorrenteVO.setUnidadeEnsino(getUnidadeEnsinoVO());

            for (UnidadeEnsinoContaCorrenteVO unidEnsContaCorrenteVO : getContaCorrenteVO().getUnidadeEnsinoContaCorrenteVOs()) {
                if (unidadeEnsinoContaCorrenteVO.getUnidadeEnsino().getCodigo().equals(unidEnsContaCorrenteVO.getUnidadeEnsino().getCodigo())) {
                    throw new Exception("Essa unidade de ensino já se encontra na lista.");
                }
            }

            getContaCorrenteVO().getUnidadeEnsinoContaCorrenteVOs().add(unidadeEnsinoContaCorrenteVO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            unidadeEnsinoContaCorrenteVO = null;
            setUnidadeEnsinoVO(null);
        }
    }

    public void removerUnidadeEnsinoContaCorrente() {
        try {
            UnidadeEnsinoContaCorrenteVO unidadeEnsinoContaCorrenteVO = (UnidadeEnsinoContaCorrenteVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoContaCorrenteItens");
            for (UnidadeEnsinoContaCorrenteVO unidEnsContaCorrenteVO : getContaCorrenteVO().getUnidadeEnsinoContaCorrenteVOs()) {
                if (unidadeEnsinoContaCorrenteVO.getUnidadeEnsino().getCodigo().equals(unidEnsContaCorrenteVO.getUnidadeEnsino().getCodigo())) {
                	if (getFacadeFactory().getContaCorrenteFacade().consultarExistenciaContaGeradaPorContaCaixaExistente(getContaCorrenteVO().getCodigo(), unidEnsContaCorrenteVO.getUnidadeEnsino().getCodigo())) {
                		throw new Exception("Não Foi Possível Remover a Unidade de Ensino Pois Ela Está Vinculada a Um Recebimento.");
                	}
                    getContaCorrenteVO().getUnidadeEnsinoContaCorrenteVOs().remove(unidadeEnsinoContaCorrenteVO);
                    break;
                }
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
        }
    }

    public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
        this.unidadeEnsinoVO = unidadeEnsinoVO;
    }

    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
        if (unidadeEnsinoVO == null) {
            unidadeEnsinoVO = new UnidadeEnsinoVO();
        }
        return unidadeEnsinoVO;
    }

    public boolean getIsExisteFuncionarioResponsavel() {
        return (!getContaCorrenteVO().getFuncionarioResponsavel().getPessoa().getCodigo().equals(0));
    }

    public Boolean getApresentarCarteiraRegistrada() {
        return !getContaCorrenteVO().getContaCaixa();
    }

    public Boolean getApresentarMensagemCarteiraRegistrada() {
        return getContaCorrenteVO().getCarteiraRegistrada() && !getContaCorrenteVO().getPermiteEmissaoBoletoVencido();
    }

    public Boolean getApresentarMensagemAlteracao() {
        if (apresentarMensagemAlteracao == null) {
            apresentarMensagemAlteracao = Boolean.FALSE;
        }
        return apresentarMensagemAlteracao;
    }

    public void setApresentarMensagemAlteracao(Boolean apresentarMensagemAlteracao) {
        this.apresentarMensagemAlteracao = apresentarMensagemAlteracao;
    }

    public String getMensagemApresentarAlteracao() {
        if (mensagemApresentarAlteracao == null) {
            mensagemApresentarAlteracao = "";
        }
        return mensagemApresentarAlteracao;
    }

    public void setMensagemApresentarAlteracao(String mensagemApresentarAlteracao) {
        this.mensagemApresentarAlteracao = mensagemApresentarAlteracao;
    }
    
	/**
	 * @return the campoConsultaCidade
	 */
	public String getCampoConsultaCidade() {
		return campoConsultaCidade;
	}

	/**
	 * @param campoConsultaCidade
	 *            the campoConsultaCidade to set
	 */
	public void setCampoConsultaCidade(String campoConsultaCidade) {
		this.campoConsultaCidade = campoConsultaCidade;
	}

	/**
	 * @return the valorConsultaCidade
	 */
	public String getValorConsultaCidade() {
		return valorConsultaCidade;
	}

	/**
	 * @param valorConsultaCidade
	 *            the valorConsultaCidade to set
	 */
	public void setValorConsultaCidade(String valorConsultaCidade) {
		this.valorConsultaCidade = valorConsultaCidade;
	}

	public List getListaConsultaCidade() {
		if (listaConsultaCidade == null) {
			listaConsultaCidade = new ArrayList();
		}
		return (listaConsultaCidade);
	}

	public void setListaConsultaCidade(List listaConsultaCidade) {
		this.listaConsultaCidade = listaConsultaCidade;
	}

	public void validarUtilizarRemessa() {
		try {
			UnidadeEnsinoContaCorrenteVO obj = (UnidadeEnsinoContaCorrenteVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoContaCorrenteItens");			
			Iterator i = this.getContaCorrenteVO().getUnidadeEnsinoContaCorrenteVOs().iterator();
			while (i.hasNext()) {
				UnidadeEnsinoContaCorrenteVO uni = (UnidadeEnsinoContaCorrenteVO) i.next();
				if (uni.getUtilizarRemessa().booleanValue() && (uni.getUnidadeEnsino().getCodigo().intValue() != obj.getUnidadeEnsino().getCodigo().intValue())) {
					uni.setUtilizarRemessa(Boolean.FALSE);
				} else if (uni.getUnidadeEnsino().getCodigo().intValue() == obj.getUnidadeEnsino().getCodigo().intValue()) {
					uni.setUtilizarRemessa(obj.getUtilizarRemessa());
				}
			}
			//return "editar";
		} catch (Exception e) {
			//return "editar";
		}
	}
	
	public void desabilitarCamposRemessaOnline() {
		try {
			getContaCorrenteVO().setRemessaBoletoEmitido(false);
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void desabilitarCamposPix() {
		try {
			if(!getContaCorrenteVO().getHabilitarRegistroPix()) {
				getContaCorrenteVO().setChavePix("");
				getContaCorrenteVO().setFormaRecebimentoPadraoPix(new FormaPagamentoVO());
				getContaCorrenteVO().setTokenIdRegistroPix("");
				getContaCorrenteVO().setTokenKeyRegistroPix("");
				getContaCorrenteVO().setChaveAplicacaoDesenvolvedorPix("");	
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarValidacaoTokenPix() {
		try {
			getFacadeFactory().getPixContaCorrenteFacade().realizarValidacaoTokenPix(getContaCorrenteVO());
			setMensagemID("msg_dados_tokenAutenticado", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarRegistroWebhookPix() {
		try {
			getFacadeFactory().getPixContaCorrenteFacade().realizarConfiguracaoWebhookPix(getContaCorrenteVO());
			setMensagemID("msg_dados_tokenAutenticado", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarConsultaWebhookPix() {
		try {
			String webhookUrl = getFacadeFactory().getPixContaCorrenteFacade().realizarConsultaWebhookPix(getContaCorrenteVO());
			setMensagemID("webhookUrl = " + webhookUrl, Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarCancelamentoWebhookPix() {
		try {
			getFacadeFactory().getPixContaCorrenteFacade().realizarCancelamentoWebhookPix(getContaCorrenteVO());
			setMensagemID("Webhook canelado com sucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}	
	
	

  public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
  }

	public List getTipoConsultaCNAB() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("CNAB240", "CNAB240"));
		itens.add(new SelectItem("CNAB400", "CNAB400"));
		return itens;
	}

	public void montarListaSelectItemFormaPagamentoPix() throws Exception {
		List lista = getFacadeFactory().getFormaPagamentoFacade().consultarPorTipo("DE", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		setListaSelectItemFormaPagamentoPix(UtilSelectItem.getListaSelectItem(lista, "codigo", "nome"));
	}
	
	public void montarListaSelectItemFormaPagamento(String prm) throws Exception {
		List resultadoConsulta = consultarFormaPagamentoPorNome(prm);
		setListaSelectItemFormaPagamento(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
	}

	public List consultarFormaPagamentoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getFormaPagamentoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemFormaPagamento() {
		try {
			montarListaSelectItemFormaPagamento("");
		} catch (Exception e) {
		}
	}
	
	public void montarListaSelectItemFormaPagamentoFix() {
		try {
			montarListaSelectItemFormaPagamentoPix();
		} catch (Exception e) {
		}
	}

	public List<SelectItem> getListaSelectItemFormaPagamentoPix() {
		return listaSelectItemFormaPagamentoPix;
	}

	public void setListaSelectItemFormaPagamentoPix(List<SelectItem> listaSelectItemFormaPagamentoPix) {
		this.listaSelectItemFormaPagamentoPix = listaSelectItemFormaPagamentoPix;
	}

	public List getListaSelectItemFormaPagamento() {
		return listaSelectItemFormaPagamento;
	}

	public void setListaSelectItemFormaPagamento(List listaSelectItemFormaPagamento) {
		this.listaSelectItemFormaPagamento = listaSelectItemFormaPagamento;
	}

	public void montarListaSelectItemCategoriaDespesa(String prm) throws Exception {
		List resultadoConsulta = consultarCategoriaDespesaPorDescricao(prm);
		setListaSelectItemCategoriaDespesa(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricao"));
	}

	public List consultarCategoriaDespesaPorDescricao(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(nomePrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemCategoriaDespesa() {
		try {
			montarListaSelectItemCategoriaDespesa("");
		} catch (Exception e) {
		}
	}

	public List getListaSelectItemCategoriaDespesa() {
		return listaSelectItemCategoriaDespesa;
	}

	public void setListaSelectItemCategoriaDespesa(List listaSelectItemCategoriaDespesa) {
		this.listaSelectItemCategoriaDespesa = listaSelectItemCategoriaDespesa;
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
	
	public TipoContaCorrenteEnum getTipoContaCorrenteFiltro() {
		return tipoContaCorrenteFiltro;
	}

	public void setTipoContaCorrenteFiltro(TipoContaCorrenteEnum tipoContaCorrenteFiltro) {
		this.tipoContaCorrenteFiltro = tipoContaCorrenteFiltro;
	}

	public String getSituacaoContaCorrenteFiltro() {
		if (situacaoContaCorrenteFiltro == null) {
			situacaoContaCorrenteFiltro = "";
		}
		return situacaoContaCorrenteFiltro;
	}

	public void setSituacaoContaCorrenteFiltro(String situacaoContaCorrenteFiltro) {
		this.situacaoContaCorrenteFiltro = situacaoContaCorrenteFiltro;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoCorrenteFiltro() {
		if (unidadeEnsinoCorrenteFiltro == null) {
			unidadeEnsinoCorrenteFiltro = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoCorrenteFiltro;
	}

	public void setUnidadeEnsinoCorrenteFiltro(UnidadeEnsinoVO unidadeEnsinoCorrenteFiltro) {
		this.unidadeEnsinoCorrenteFiltro = unidadeEnsinoCorrenteFiltro;
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
		contaCorrenteVO.setTipoOrigemBiblioteca(selecionado);
		contaCorrenteVO.setTipoOrigemBolsaCusteadaConvenio(selecionado);
		contaCorrenteVO.setTipoOrigemContratoReceita(selecionado);
		contaCorrenteVO.setTipoOrigemDevolucaoCheque(selecionado);
		contaCorrenteVO.setTipoOrigemInclusaoReposicao(selecionado);
		contaCorrenteVO.setTipoOrigemInscricaoProcessoSeletivo(contaCorrenteVO.getContaCaixa() ? selecionado : false);
		contaCorrenteVO.setTipoOrigemMaterialDidatico(selecionado);
		contaCorrenteVO.setTipoOrigemMatricula(selecionado);
		contaCorrenteVO.setTipoOrigemMensalidade(selecionado);
		contaCorrenteVO.setTipoOrigemNegociacao(selecionado);
		contaCorrenteVO.setTipoOrigemOutros(selecionado);
		contaCorrenteVO.setTipoOrigemRequerimento(contaCorrenteVO.getContaCaixa() ? selecionado : false);
		
	}
	
	public void verificarTipoOrigemContaCorrente() {
		if(contaCorrenteVO.getTipoOrigemBiblioteca() || contaCorrenteVO.getTipoOrigemBolsaCusteadaConvenio() || contaCorrenteVO.getTipoOrigemContratoReceita() || contaCorrenteVO.getTipoOrigemDevolucaoCheque() 
			|| contaCorrenteVO.getTipoOrigemInclusaoReposicao() || contaCorrenteVO.getTipoOrigemInscricaoProcessoSeletivo() || contaCorrenteVO.getTipoOrigemMaterialDidatico() || contaCorrenteVO.getTipoOrigemMatricula() || contaCorrenteVO.getTipoOrigemMensalidade()
			|| contaCorrenteVO.getTipoOrigemNegociacao() || contaCorrenteVO.getTipoOrigemOutros() || contaCorrenteVO.getTipoOrigemRequerimento()) {
			setMarcarTodosTipoOrigem(true);
		}
		else {
			setMarcarTodosTipoOrigem(false);
		}
	}
	
	public void validarUsarPorDefaultMovimentacaoFinanceira() {
		try {
			UnidadeEnsinoContaCorrenteVO obj = (UnidadeEnsinoContaCorrenteVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoContaCorrenteItens");			
			Iterator i = this.getContaCorrenteVO().getUnidadeEnsinoContaCorrenteVOs().iterator();
			while (i.hasNext()) {
				UnidadeEnsinoContaCorrenteVO uni = (UnidadeEnsinoContaCorrenteVO) i.next();
				if (uni.getUsarPorDefaultMovimentacaoFinanceira().booleanValue() && (uni.getUnidadeEnsino().getCodigo().intValue() != obj.getUnidadeEnsino().getCodigo().intValue())) {
					uni.setUsarPorDefaultMovimentacaoFinanceira(Boolean.FALSE);
				} else if (uni.getUnidadeEnsino().getCodigo().intValue() == obj.getUnidadeEnsino().getCodigo().intValue()) {
					uni.setUsarPorDefaultMovimentacaoFinanceira(obj.getUsarPorDefaultMovimentacaoFinanceira());
				}
			}
		} catch (Exception e) {
			 setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarObtencaoDataVencimentoCertificado() {
		try {
			if(getContaCorrenteVO().getHabilitarRegistroRemessaOnline() && Uteis.isAtributoPreenchido(getContaCorrenteVO().getArquivoCertificadoVO().getNome()) && Uteis.isAtributoPreenchido(getContaCorrenteVO().getSenhaCertificado())) {			
				getContaCorrenteVO().setDataVencimentoCertificado(Assinador.getDataValidadeCertificado(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo()+File.separator+getContaCorrenteVO().getArquivoCertificadoVO().getPastaBaseArquivo()+File.separator+getContaCorrenteVO().getArquivoCertificadoVO().getNome(), getContaCorrenteVO().getSenhaCertificado()));
				if(getContaCorrenteVO().getDataVencimentoCertificado().compareTo(new Date()) < 0) {
					setMensagemDetalhada("msg_erro", "Certificado de remessa on-line está vencido, adicione um novo certificado para continuar utilizando o serviço.");
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<SelectItem> getListaSelectItemInicialNossoNumero() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", "Nenhum"));
		itens.add(new SelectItem("10", "10"));
		itens.add(new SelectItem("20", "20"));
		itens.add(new SelectItem("30", "30"));
		itens.add(new SelectItem("40", "40"));
		itens.add(new SelectItem("50", "50"));
		itens.add(new SelectItem("60", "60"));
		itens.add(new SelectItem("70", "70"));
		itens.add(new SelectItem("80", "80"));
		itens.add(new SelectItem("90", "90"));
		return itens;
	}
	
	public void carregarDadosAgenciaSelecionada() {
		if (Uteis.isAtributoPreenchido(getContaCorrenteVO().getAgencia().getCodigo())) {
			try {
				getContaCorrenteVO().setAgencia(getFacadeFactory().getAgenciaFacade().consultarPorChavePrimaria(getContaCorrenteVO().getAgencia().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
	}
	
	public String getCamposObrigatorios(){
		return this.getContaCorrenteVO().getAgencia().getBanco().getNrBanco().equals(Bancos.CAIXA_ECONOMICA_FEDERAL.getNumeroBanco()) ? "campos" : "camposObrigatorios";
	}
	
	
	
	public Double getSaldoContaCorrente() {
		if (saldoContaCorrente == null) {
			saldoContaCorrente = 0.0;
		}
		return saldoContaCorrente;
	}

	public void setSaldoContaCorrente(Double saldoContaCorrente) {
		this.saldoContaCorrente = saldoContaCorrente;
	}

	public void atualizarDadosSaldoContaCorrente() {
		try {
			if (getContaCorrenteVO().getContaCaixa()) {
				setSaldoContaCorrente(getFacadeFactory().getContaCorrenteFacade().atualizarDadosSaldoContaCaixa(getContaCorrenteVO().getCodigo(), getUsuarioLogado()));
			}else {
				setSaldoContaCorrente(getFacadeFactory().getExtratoContaCorrenteRelFacade().consultarSaldoAnterior(getContaCorrenteVO().getCodigo(), new Date()));
			}
			
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
  
}
