package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas parceiroForm.jsp
 * parceiroCons.jsp) com as funcionalidades da classe <code>Parceiro</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see Parceiro
 * @see ParceiroVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.EstagioVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.AreaProfissionalParceiroVO;
import negocio.comuns.bancocurriculum.AreaProfissionalVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContatoParceiroVO;
import negocio.comuns.financeiro.ParceiroUnidadeEnsinoContaCorrenteVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("ParceiroControle")
@Scope("viewScope")
@Lazy
public class ParceiroControle extends SuperControleRelatorio implements Serializable {

    private ParceiroVO parceiroVO;
    private ParceiroVO parceiroUnificarVO;
    private ContatoParceiroVO contatoParceiroVO;
    protected String campoConsultaCidade;
    private Boolean mostrarModalSucesso;
    private Boolean permiteAlterarDadosVisaoParceiro;
    protected String valorConsultaCidade;
    protected List<CidadeVO> listaConsultaCidade;
    private ComunicacaoInternaVO comunicacaoInternaVO;
    private List listaConsultaParceiroUnificar;
    private String valorConsultaParceiroUnificar;
    private String campoConsultaParceiroUnificar;
    private Boolean permitirUnificarParceirosDuplicados;
    
    private ParceiroUnidadeEnsinoContaCorrenteVO parceiroUnidadeEnsinoContaCorrenteVO;
    private List listaSelectItemUnidadeEnsino;
    protected List listaSelectItemFormaPagamento;    
    protected List listaSelectItemContaCorrente;    
    protected List listaConsultaCentroDespesa;
	protected String valorConsultaCentroDespesa;
	protected String campoConsultaCentroDespesa;

    private List<TextoPadraoDeclaracaoVO> listaContratos;
    private EstagioVO estagioImprimirVO;
    private Boolean imprimirContrato;
    private Boolean isDownloadContrato;
    private ArquivoVO arquivoVO;
    private boolean permitirRealizarImpressaoContrato = false;
    private String campoConsultaAreaProfissional;
    private String valorConsultaAreaProfissional;
    private List<AreaProfissionalVO> listaConsultaAreaProfissionalVOs;
    private AreaProfissionalVO areaProfissionalVO;

    public ParceiroControle() throws Exception {
        //obterUsuarioLogado();
        inicializarObjetoParceiroVO();
        setControleConsulta(new ControleConsulta());
        Boolean parceiroCadastrado = (Boolean) context().getExternalContext().getSessionMap().get("parceiroCadastrado");
        if (parceiroCadastrado != null && parceiroCadastrado) {
            consultarParceiroBancoCurriculo();
            context().getExternalContext().getSessionMap().remove("parceiroCadastrado");
        }
        Boolean parceiroInativos = (Boolean) context().getExternalContext().getSessionMap().get("parceiroInativos");
        if (parceiroInativos != null && parceiroInativos) {
            consultarParceiroInativos();
            context().getExternalContext().getSessionMap().remove("parceiroInativos");
        }
        Boolean parceiroComVaga = (Boolean) context().getExternalContext().getSessionMap().get("parceiroComVaga");
        if (parceiroComVaga != null && parceiroComVaga) {
            consultarParceiroComVagasAberta();
            context().getExternalContext().getSessionMap().remove("parceiroComVaga");
        }
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Parceiro</code> para edição pelo usuário da
     * aplicação.
     */
    public String novo() throws Exception {
        registrarAtividadeUsuario(getUsuarioLogado(), "ParceiroControle", "Novo Parceiro", "Novo");
        //removerObjetoMemoria(this);
        setParceiroVO(new ParceiroVO());
        setContatoParceiroVO(new ContatoParceiroVO());
        setAreaProfissionalVO(new AreaProfissionalVO());
        getListaConsultaAreaProfissionalVOs().clear();
        montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemContaCorrente();
        montarListaSelectItemFormaPagamento();
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("parceiroForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Parceiro</code> para alteração. O
     * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
     * disponibilizá-lo para edição.
     */
    public String editar() throws Exception {
        registrarAtividadeUsuario(getUsuarioLogado(), "ParceiroControle", "Inicializando Editar Parceiro", "Editando");
        ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
        ParceiroVO parceiroVO = getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        obj.setNovoObj(Boolean.FALSE);
        setParceiroVO(parceiroVO);
        setParceiroUnificarVO(null);
        setContatoParceiroVO(new ContatoParceiroVO());
        registrarAtividadeUsuario(getUsuarioLogado(), "ParceiroControle", "Finalizando Editar Parceiro", "Editando");
        executarVerificacaoPermissaoImprimirTextoPadraoDeclaracao();
        montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemContaCorrente();
        montarListaSelectItemFormaPagamento();
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("parceiroForm.xhtml");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Parceiro</code>. Caso o
     * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (parceiroVO.isNovoObj().booleanValue()) {
                registrarAtividadeUsuario(getUsuarioLogado(), "ParceiroControle", "Inicializando Incluir Parceiro", "Incluir");
                
                getFacadeFactory().getParceiroFacade().persistir(parceiroVO, true, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "ParceiroControle", "Finalizando Incluir Parceiro", "Incluir");
            } else {
                registrarAtividadeUsuario(getUsuarioLogado(), "ParceiroControle", "Inicializando Alterar Parceiro", "Alterar");
                getFacadeFactory().getParceiroFacade().persistir(parceiroVO, true, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
                getUsuarioLogado().setNome(getParceiroVO().getNome());
                registrarAtividadeUsuario(getUsuarioLogado(), "ParceiroControle", "Finalizando Alterar Parceiro", "Alterar");
            }
            Boolean erro = enviarEmail();
            if (erro) {
                setMensagemID("msg_textoPadraoBancoCurriculum_inexistente");
            } else {
                setMensagemID("msg_dados_gravados");
            }
            executarVerificacaoPermissaoImprimirTextoPadraoDeclaracao();
            setMensagemID("msg_dados_gravados");
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public String gravarVisaoParceiro() {
        try {
            if (parceiroVO.isNovoObj().booleanValue()) {
                registrarAtividadeUsuario(getUsuarioLogado(), "ParceiroControle", "Inicializando Incluir Parceiro Visão Parceiro", "Incluindo");
                getFacadeFactory().getParceiroFacade().persistir(parceiroVO, true, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "ParceiroControle", "Finalizando Incluir Parceiro Visão Parceiro", "Incluindo");
            } else {
                registrarAtividadeUsuario(getUsuarioLogado(), "ParceiroControle", "Inicializando Alterar Parceiro Visão Parceiro", "Alterando");
                getFacadeFactory().getParceiroFacade().persistir(parceiroVO, true, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "ParceiroControle", "Finalizando Alterar Parceiro Visão Parceiro", "Alterando");
            }
            try {
                Boolean erro = enviarEmail();
                getParceiroVO().setMostrarModalSucesso(Boolean.TRUE);
                context().getExternalContext().getSessionMap().put("ParceiroVO", getParceiroVO());
                context().getExternalContext().getSessionMap().put("LoginControle", null);
                if (erro) {
                    setMensagemID("msg_textoPadraoBancoCurriculum_inexistente");
                } else {
                    setMensagemID("msg_dados_gravados");
                }
                return "parceiro";
            } catch (Exception e) {
                getParceiroVO().setMostrarModalSucesso(Boolean.TRUE);
                context().getExternalContext().getSessionMap().put("ParceiroVO", getParceiroVO());
                context().getExternalContext().getSessionMap().put("LoginControle", null);
                return "parceiro";
            }
        } catch (Exception e) {
            getParceiroVO().setMostrarModalSucesso(Boolean.FALSE);
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    private Boolean enviarEmail() throws Exception {
        ComunicacaoInternaVO comunicacaoInternaVO = null;
        Boolean erro = false;
        try {
            String corpoMensagem = "";
            corpoMensagem = getFacadeFactory().getTextoPadraoBancoCurriculumFacade().consultarPorTipoUnica("boasVindasParceiro", false, "AT", Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()).getTexto();
            comunicacaoInternaVO = new ComunicacaoInternaVO();
            comunicacaoInternaVO.setAssunto("Boas Vindas");
            List<ComunicadoInternoDestinatarioVO> listaComunicadoInternoDestinatarioVO = new ArrayList<ComunicadoInternoDestinatarioVO>(0);
            for (ContatoParceiroVO CPVO : getParceiroVO().getContatoParceiroVOs()) {
                ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
                comunicadoInternoDestinatarioVO.setEmail(CPVO.getEmail());
                comunicadoInternoDestinatarioVO.setNome(CPVO.getNome());
                comunicadoInternoDestinatarioVO.getDestinatario().setNome(CPVO.getNome());
                comunicadoInternoDestinatarioVO.getDestinatario().setEmail(CPVO.getEmail());
                listaComunicadoInternoDestinatarioVO.add(comunicadoInternoDestinatarioVO);
            }
            comunicacaoInternaVO.setComunicadoInternoDestinatarioVOs(listaComunicadoInternoDestinatarioVO);
            comunicacaoInternaVO.setTipoDestinatario("PA");
            UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().obterUnidadeMatriz(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), unidadeEnsinoVO.getCodigo());
            PessoaVO responsavel = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarResponsavelPadraoComunicadoInternoPorCodigoConfiguracoes(config.getResponsavelPadraoComunicadoInterno().getCodigo());
            comunicacaoInternaVO.setResponsavel(responsavel);
            if (!corpoMensagem.equals("")) {
                if (corpoMensagem.contains("#Titulo_BancoCurriculum")) {
                    corpoMensagem = corpoMensagem.replaceAll("#Titulo_BancoCurriculum", config.getTituloTelaBancoCurriculum());
                }
                if (corpoMensagem.contains("#Nome_Empresa")) {
                    corpoMensagem = corpoMensagem.replaceAll("#Nome_Empresa", getParceiroVO().getNome());
                }
                if (corpoMensagem.contains("<!DOCTYPE")) {
                    corpoMensagem = corpoMensagem.replace(corpoMensagem.substring(corpoMensagem.indexOf("<!DOCTYPE"), corpoMensagem.indexOf("<html>")), "");
                }
                comunicacaoInternaVO.setMensagem(corpoMensagem);
                getFacadeFactory().getEmailFacade().realizarGravacaoEmail(comunicacaoInternaVO, new ArrayList(0), true, getUsuarioLogado(), null);
            } else {
                erro = true;
            }
            return erro;
        } catch (Exception e) {
            throw e;
        }
    }

    public void enviarComunicado() {
        try {
            ConfiguracaoGeralSistemaVO config = getConfiguracaoGeralPadraoSistema();
            getComunicacaoInternaVO().setTipoOrigemComunicacaoInternaEnum(null);
            getComunicacaoInternaVO().setResponsavel(config.getResponsavelPadraoComunicadoInterno());
            getComunicacaoInternaVO().setData(new Date());
            getComunicacaoInternaVO().setEnviarEmail(true);
            getComunicacaoInternaVO().setCodigo(0);
            getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().clear();
            getComunicacaoInternaVO().setAssunto("Solicitação de Alteração Cadastrais (Parceiro)");
            //getComunicacaoInternaVO().setMensagem(obterMensagemFormatadaMensagemProfessorPostagemMaterial(notificacaoRegistroAulaNaoLancadaVO, mensagemTemplate1.getMensagem()));
            getComunicacaoInternaVO().setTipoDestinatario("FU");
            getFacadeFactory().getFuncionarioFacade().carregarDados(config.getFuncionarioRespAlteracaoDados(), getUsuarioLogado());
            getComunicacaoInternaVO().setFuncionario(config.getFuncionarioRespAlteracaoDados());
            getComunicacaoInternaVO().setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(config.getFuncionarioRespAlteracaoDados().getPessoa()));
            getFacadeFactory().getComunicacaoInternaFacade().incluir(getComunicacaoInternaVO(), false, getUsuarioLogado(), config,null);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String redirecionarParaVisaoParceiro() {
        return "";
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ParceiroCons.jsp. Define o tipo de consulta a ser
     * executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "ParceiroControle", "Inicializando Consultar Parceiro", "Consultando");
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getParceiroFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("razaoSocial")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocial(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("RG")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorRG(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("CPF")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorCPF(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("CNPJ")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorCNPJ(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("tipoParceiro")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorTipoParceiro(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            registrarAtividadeUsuario(getUsuarioLogado(), "ParceiroControle", "Finalizando Consultar Parceiro", "Consultando");
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("parceiroCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("parceiroCons.xhtml");
        }
    }

    public String consultarParceiroBancoCurriculo() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocialBancoCurriculumTrue("", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("parceiroCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("parceiroCons.xhtml");
        }
    }

    public String consultarParceiroInativos() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            objs = getFacadeFactory().getParceiroFacade().consultarParceiroInativoBancoCurriculumFalse("", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("parceiroCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("parceiroCons.xhtml");
        }
    }

    public String consultarParceiroComVagasAberta() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            objs = getFacadeFactory().getParceiroFacade().consultarParceiroBancoCurriculumTrueComVaga("", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("parceiroCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("parceiroCons.xhtml");
        }
    }

    public void consultarParceiroPorCPF() {
        try {
        	String cpf = Uteis.removeCaractersEspeciais2(getParceiroVO().getCPF());
        	cpf = cpf.replaceAll("/", "");
        	ParceiroVO parceiro = getFacadeFactory().getParceiroFacade().consultarPorCPFUnico(cpf, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        	if (Uteis.isAtributoPreenchido(parceiro)) {
        		setParceiroVO(parceiro);
        	}
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarParceiroPorCNPJ() {
        try {
        	String cnpj = Uteis.removeCaractersEspeciais2(getParceiroVO().getCNPJ());
        	cnpj = cnpj.replaceAll("/", "");
        	ParceiroVO parceiro = getFacadeFactory().getParceiroFacade().consultarPorCNPJUnico(cnpj, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        	if (Uteis.isAtributoPreenchido(parceiro)) {
        		setParceiroVO(parceiro);
        	}
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>ParceiroVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "ParceiroControle", "Inicializando Excluir Parceiro", "Excluindo");
            getFacadeFactory().getParceiroFacade().excluir(parceiroVO, getUsuarioLogado());
            setParceiroVO(new ParceiroVO());
            setContatoParceiroVO(new ContatoParceiroVO());
            registrarAtividadeUsuario(getUsuarioLogado(), "ParceiroControle", "Finalizando Excluir Parceiro", "Excluindo");
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("parceiroForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("parceiroForm.xhtml");
        }
    }

    /*
     * Método responsável por adicionar um novo objeto da classe <code>ContatoParceiro</code> para o objeto
     * <code>parceiroVO</code> da classe <code>Parceiro</code>
     */
    public void adicionarContatoParceiro() throws Exception {
        try {
            if (!getParceiroVO().getCodigo().equals(0)) {
                contatoParceiroVO.setParceiro(getParceiroVO());
            }
            getParceiroVO().adicionarObjContatoParceiroVOs(getContatoParceiroVO());
            this.setContatoParceiroVO(new ContatoParceiroVO());
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>ContatoParceiro</code> para edição pelo
     * usuário.
     */
    public void editarContatoParceiro() throws Exception {
        ContatoParceiroVO obj = (ContatoParceiroVO) context().getExternalContext().getRequestMap().get("contatoParceiroItens");
        setContatoParceiroVO(obj);

    }

    /*
     * Método responsável por remover um novo objeto da classe <code>ContatoParceiro</code> do objeto
     * <code>parceiroVO</code> da classe <code>Parceiro</code>
     */
    public void removerContatoParceiro() throws Exception {
        ContatoParceiroVO obj = (ContatoParceiroVO) context().getExternalContext().getRequestMap().get("contatoParceiroItens");
        getParceiroVO().excluirObjContatoParceiroVOs(obj.getNome());
        setMensagemID("msg_dados_excluidos");
    }

    public void inicializarObjetoParceiroVO() {
        ParceiroVO parceiro;
        parceiro = (ParceiroVO) context().getExternalContext().getSessionMap().get(ParceiroVO.class.getSimpleName());
        if (parceiro != null) {
            setParceiroVO(parceiro);
        }
    }

    public void irPaginaInicial() throws Exception {
        // controleConsulta.setPaginaAtual(1);
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

    public void carregarEnderecoPessoa() {
        try {
            getFacadeFactory().getEnderecoFacade().carregarEndereco(parceiroVO, getUsuarioLogado());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>tipoParceiro</code>
     */
    public void consultarCidade() {
        try {
            if (getCampoConsultaCidade().equals("codigo")) {
                if (getValorConsultaCidade().equals("")) {
                    setValorConsultaCidade("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaCidade());
                setListaConsultaCidade(getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado()));
            } else if (getCampoConsultaCidade().equals("nome")) {
                if (getValorConsultaCidade().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                setListaConsultaCidade(getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidade(), false, getUsuarioLogado()));
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCidade(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Método responsável por selecionar o objeto CidadeVO <code>Cidade/code>.
     */
    public void selecionarCidade() {
        CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItens");
        getParceiroVO().setCidade(obj);
        getListaConsultaCidade().clear();
        setValorConsultaCidade("");
        setCampoConsultaCidade("");
    }

    public void consultarUnificarParceiro() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "ParceiroControle", "Inicializando Consultar Parceiro", "Consultando");
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaParceiroUnificar().equals("codigo")) {
                if (getValorConsultaParceiroUnificar().equals("")) {
                    setValorConsultaParceiroUnificar("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaParceiroUnificar());
                objs = getFacadeFactory().getParceiroFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaParceiroUnificar().equals("nome")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorNome(getValorConsultaParceiroUnificar(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaParceiroUnificar().equals("razaoSocial")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocial(getValorConsultaParceiroUnificar(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaParceiroUnificar().equals("RG")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorRG(getValorConsultaParceiroUnificar(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaParceiroUnificar().equals("CPF")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorCPF(getValorConsultaParceiroUnificar(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaParceiroUnificar().equals("CNPJ")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorCNPJ(getValorConsultaParceiroUnificar(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaParceiroUnificar().equals("tipoParceiro")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorTipoParceiro(getValorConsultaParceiroUnificar(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaParceiroUnificar(objs);
            registrarAtividadeUsuario(getUsuarioLogado(), "ParceiroControle", "Finalizando Consultar Parceiro", "Consultando");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            getListaConsultaParceiroUnificar().clear();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void selecionarParceiroUnificar() throws Exception {
        ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("ParceiroUnificarItens");
        this.setParceiroUnificarVO(obj);
        Uteis.liberarListaMemoria(this.getListaConsultaParceiroUnificar());
        this.setValorConsultaParceiroUnificar(null);
        this.setValorConsultaParceiroUnificar(null);
    }

    public void unificarParceiro() throws Exception {
        try {
            if (getParceiroVO().getCodigo().intValue() == getParceiroUnificarVO().getCodigo().intValue()) {
                throw new Exception("O Parceiro manter não pode ser o mesmo do Parceiro remover.");
            }
            getFacadeFactory().getParceiroFacade().unificarParceiro(getParceiroVO().getCodigo(), getParceiroUnificarVO().getCodigo(), getUsuarioLogado());
            setParceiroUnificarVO(null);
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (Exception e) {
            setParceiroUnificarVO(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    /**
     * Método responsável por carregar umaCombobox com os tipos de pesquisa de Cidade <code>Cidade/code>.
     */
    public List getTipoConsultaCidade() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public List getListaSelectItemTipoParceiroParceiro() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoParceiroParceiros = (Hashtable) Dominios.getTipoParceiroParceiro();
        Enumeration keys = tipoParceiroParceiros.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoParceiroParceiros.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>tipoEmpresa</code>
     */
    public List getListaSelectItemTipoEmpresaParceiro() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoEmpresas = (Hashtable) Dominios.getTipoEmpresa();
        Enumeration keys = tipoEmpresas.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoEmpresas.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List<SelectItem> getTipoConsultaCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("razaoSocial", "Razão Social"));
        itens.add(new SelectItem("RG", "RG"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("CNPJ", "CNPJ"));
        itens.add(new SelectItem("tipoParceiro", "Tipo Parceiro"));
        return itens;
    }

    public String getMascaraConsulta() {
        if (getControleConsulta().getCampoConsulta().equals("CPF")) {
            return "return mascara(this.form,'form:valorConsulta','999.999.999-99',event)";
        }
        return "";
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        // setPaginaAtualDeTodas("0/0");
        setListaConsulta(new ArrayList(0));
        // definirVisibilidadeLinksNavegacao(0, 0);
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("parceiroCons.xhtml");
    }

    public static void verificarPermissaoUsuarioEditarDadosEmpresa(UsuarioVO usuario, String nomeEntidade) throws Exception {
        ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(nomeEntidade, usuario);
    }

    public void verificarUsuarioPodeAlterarDados() {
        //getPermiteAlterarDadosVisaoParceiro();
        Boolean liberar = true;
        try {
            verificarPermissaoUsuarioEditarDadosEmpresa(getUsuarioLogado(), "Parceiro_permiteAlterarDados");
            liberar = false;
        } catch (Exception e) {
            liberar = true;
        }
        this.setPermiteAlterarDadosVisaoParceiro(liberar);
    }

    public String editarDadosVisaoParceiro() throws Exception {
        try {
            verificarUsuarioPodeAlterarDados();
            registrarAtividadeUsuario(getUsuarioLogado(), "ParceiroControle", "Inicializando Editar Parceiro Visão Parceiro", "Editando");
            setParceiroVO(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(getUsuarioLogado().getParceiro().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            if (getParceiroVO().getNovoObj()) {
                setPermiteAlterarDadosVisaoParceiro(Boolean.TRUE);
            }
            context().getExternalContext().getSessionMap().put(ParceiroVO.class.getSimpleName(), getParceiroVO());
            registrarAtividadeUsuario(getUsuarioLogado(), "ParceiroControle", "Finalizando Editar Parceiro Visão Parceiro", "Editando");
            setMensagemID("msg_dados_editar");
            return Uteis.getCaminhoRedirecionamentoNavegacao("cadastroVisaoParceiro.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public ContatoParceiroVO getContatoParceiroVO() {
        if (contatoParceiroVO == null) {
            contatoParceiroVO = new ContatoParceiroVO();
        }
        return contatoParceiroVO;
    }

    public void setContatoParceiroVO(ContatoParceiroVO contatoParceiroVO) {
        this.contatoParceiroVO = contatoParceiroVO;
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

    /**
     * @return the campoConsultaCidade
     */
    public String getCampoConsultaCidade() {
    	if (campoConsultaCidade == null) {
    		campoConsultaCidade = "";
    	}
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
    	if (valorConsultaCidade == null) {
    		valorConsultaCidade = "";
    	}
        return valorConsultaCidade;
    }

    /**
     * @param valorConsultaCidade
     *            the valorConsultaCidade to set
     */
    public void setValorConsultaCidade(String valorConsultaCidade) {
        this.valorConsultaCidade = valorConsultaCidade;
    }

    /**
     * @return the listaConsultaCidade
     */
    public List<CidadeVO> getListaConsultaCidade() {
    	if (listaConsultaCidade == null) {
    		listaConsultaCidade = new ArrayList<CidadeVO>(0);
    	}
        return listaConsultaCidade;
    }

    /**
     * @param listaConsultaCidade
     *            the listaConsultaCidade to set
     */
    public void setListaConsultaCidade(List<CidadeVO> listaConsultaCidade) {
        this.listaConsultaCidade = listaConsultaCidade;
    }

    public Boolean getApresentarBotaoGravar() {
        if (getUsuarioLogado() == null || getUsuarioLogado().getNome().equals("")) {
            return true;
        }
        return false;
    }

    public Boolean getApresentarCamposPessoaJuridica() {
        if (getParceiroVO().getParceiroJuridico()) {
            return true;
        }
        return false;
    }

    /**
     * @return the permiteAlterarDadosVisaoParceiro
     */
    public Boolean getPermiteAlterarDadosVisaoParceiro() {
        if (permiteAlterarDadosVisaoParceiro == null) {            
            if (!getParceiroVO().getNovoObj()) {
                verificarUsuarioPodeAlterarDados();
            }
        }
        return permiteAlterarDadosVisaoParceiro;
    }

    /**
     * @param permiteAlterarDadosVisaoParceiro the permiteAlterarDadosVisaoParceiro to set
     */
    public void setPermiteAlterarDadosVisaoParceiro(Boolean permiteAlterarDadosVisaoParceiro) {
        this.permiteAlterarDadosVisaoParceiro = permiteAlterarDadosVisaoParceiro;
    }

    /**
     * @return the comunicacaoInternaVO
     */
    public ComunicacaoInternaVO getComunicacaoInternaVO() {
        if (comunicacaoInternaVO == null) {
            comunicacaoInternaVO = inicializarDadosPadrao(new ComunicacaoInternaVO());
        }
        return comunicacaoInternaVO;
    }

    /**
     * @param comunicacaoInternaVO the comunicacaoInternaVO to set
     */
    public void setComunicacaoInternaVO(ComunicacaoInternaVO comunicacaoInternaVO) {
        this.comunicacaoInternaVO = comunicacaoInternaVO;
    }

    /**
     * @return the parceiroUnificarVO
     */
    public ParceiroVO getParceiroUnificarVO() {
        if (parceiroUnificarVO == null) {
            parceiroUnificarVO = new ParceiroVO();
        }
        return parceiroUnificarVO;
    }

    /**
     * @param parceiroUnificarVO the parceiroUnificarVO to set
     */
    public void setParceiroUnificarVO(ParceiroVO parceiroUnificarVO) {
        this.parceiroUnificarVO = parceiroUnificarVO;
    }

    /**
     * @return the listaConsultaParceiroUnificar
     */
    public List getListaConsultaParceiroUnificar() {
        return listaConsultaParceiroUnificar;
    }

    /**
     * @param listaConsultaParceiroUnificar the listaConsultaParceiroUnificar to set
     */
    public void setListaConsultaParceiroUnificar(List listaConsultaParceiroUnificar) {
        this.listaConsultaParceiroUnificar = listaConsultaParceiroUnificar;
    }

    /**
     * @return the valorConsultaParceiroUnificar
     */
    public String getValorConsultaParceiroUnificar() {
        return valorConsultaParceiroUnificar;
    }

    /**
     * @param valorConsultaParceiroUnificar the valorConsultaParceiroUnificar to set
     */
    public void setValorConsultaParceiroUnificar(String valorConsultaParceiroUnificar) {
        this.valorConsultaParceiroUnificar = valorConsultaParceiroUnificar;
    }

    /**
     * @return the campoConsultaParceiroUnificar
     */
    public String getCampoConsultaParceiroUnificar() {
        return campoConsultaParceiroUnificar;
    }

    /**
     * @param campoConsultaParceiroUnificar the campoConsultaParceiroUnificar to set
     */
    public void setCampoConsultaParceiroUnificar(String campoConsultaParceiroUnificar) {
        this.campoConsultaParceiroUnificar = campoConsultaParceiroUnificar;
    }

    /**
     * @return the permitirUnificarParceirosDuplicados
     */
    public Boolean getPermitirUnificarParceirosDuplicados() {
        if (permitirUnificarParceirosDuplicados == null) {
            try {
                ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Parceiro_unificarParceirosDuplicados", getUsuarioLogado());
                permitirUnificarParceirosDuplicados = Boolean.TRUE;
            } catch (Exception e) {
                permitirUnificarParceirosDuplicados = Boolean.FALSE;
            }
        }
        return permitirUnificarParceirosDuplicados;
    }

    /**
     * @param permitirUnificarParceirosDuplicados the permitirUnificarParceirosDuplicados to set
     */
    public void setPermitirUnificarParceirosDuplicados(Boolean permitirUnificarParceirosDuplicados) {
        this.permitirUnificarParceirosDuplicados = permitirUnificarParceirosDuplicados;
    }
    
    public void carregarEnderecoContatoParceiro() {
        try {
            getFacadeFactory().getEnderecoFacade().carregarEndereco(getContatoParceiroVO(), getUsuarioLogado());
	} catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
	}
    }
    
    public void validarCPFContato() {
        try {
            if (!Uteis.verificaCPF(getContatoParceiroVO().getCpf())) {
                getContatoParceiroVO().setMsgCpfInvalido("O CPF não é VÁLIDO.");
                getContatoParceiroVO().setCpf("");
            } else {
                getContatoParceiroVO().setMsgCpfInvalido("");
            }
	} catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
	}
    }
    
    /**
     * @return the listaContratosEstagio
     */
    public List<TextoPadraoDeclaracaoVO> getListaContratos() {
        if (listaContratos == null) {
            listaContratos = new ArrayList<TextoPadraoDeclaracaoVO>(0);
        }
        return listaContratos;
    }

    /**
     * @param listaContratosEstagio the listaContratosEstagio to set
     */
    public void setListaContratos(List<TextoPadraoDeclaracaoVO> listaContratos) {
        this.listaContratos = listaContratos;
    }

    /**
     * @return the estagioImprimirVO
     */
    public EstagioVO getEstagioImprimirVO() {
        return estagioImprimirVO;
    }

    /**
     * @param estagioImprimirVO the estagioImprimirVO to set
     */
    public void setEstagioImprimirVO(EstagioVO estagioImprimirVO) {
        this.estagioImprimirVO = estagioImprimirVO;
    }

    /**
     * @return the imprimirContrato
     */
    public Boolean getImprimirContrato() {
        if (imprimirContrato == null) {
            imprimirContrato = Boolean.FALSE;
        }
        return imprimirContrato;
    }

    /**
     * @param imprimirContrato the imprimirContrato to set
     */
    public void setImprimirContrato(Boolean imprimirContrato) {
        this.imprimirContrato = imprimirContrato;
    }
    
    public Boolean getIsDownloadContrato() {
        if (isDownloadContrato == null) {
            isDownloadContrato = Boolean.FALSE;
        }
        return isDownloadContrato;
    }

    public void setIsDownloadContrato(Boolean isDownloadContrato) {
        this.isDownloadContrato = isDownloadContrato;
    }  
    
    public ArquivoVO getArquivoVO() {
        if (arquivoVO == null) {
            arquivoVO = new ArquivoVO();
        }
        return arquivoVO;
    }

    public void setArquivoVO(ArquivoVO arquivoVO) {
        this.arquivoVO = arquivoVO;
    }
    
    public ImpressaoContratoVO prepararImpressaoContratoEstagio() throws Exception {
        ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
        //if (!getEstagioImprimirVO().getAno().equals("") || !getEstagioImprimirVO().getSemestre().equals("")) {
        //    impressaoContratoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaMatriculaPeriodoPorMatriculaAnoSemestre(getEstagioImprimirVO().getMatricula(), getEstagioImprimirVO().getAno(), getEstagioImprimirVO().getSemestre(), 0, "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), ""));
        //}
        //if (impressaoContratoVO.getMatriculaPeriodoVO().getCodigo().equals(0)) {
        //    impressaoContratoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatriculaOrdenandoPorAnoSemestrePeriodoLetivo(getEstagioImprimirVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        //}
        //impressaoContratoVO.getMatriculaVO().setMatricula(getEstagioImprimirVO().getMatricula());
        //getEstagioImprimirVO().setEmpresaEstagio(this.getParceiroVO());
        impressaoContratoVO.setEstagioVO(getEstagioImprimirVO());
        
//        ParceiroVO parceiroVOCarregado = getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(getEstagioImprimirVO().getEmpresaEstagio().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
//        getEstagioImprimirVO().setEmpresaEstagio(parceiroVOCarregado);
        return impressaoContratoVO;
    }
    
    public void imprimirTextoPadraoEstagioHtml() {
        try {
        	TextoPadraoDeclaracaoVO textoPadraoImprimirVO = (TextoPadraoDeclaracaoVO) context().getExternalContext().getRequestMap().get("textoPadraoItem");
        	if (Uteis.isAtributoPreenchido(textoPadraoImprimirVO)) {
        		limparMensagem();
        		setFazerDownload(false);
        		setCaminhoRelatorio("");
        		ImpressaoContratoVO impressaoContratoVO = prepararImpressaoContratoEstagio();
        		impressaoContratoVO.setGerarNovoArquivoAssinado(false);
        		impressaoContratoVO.setImpressaoPdf(false);
        		setCaminhoRelatorio(getFacadeFactory().getImpressaoDeclaracaoFacade().imprimirDeclaracao(
        				textoPadraoImprimirVO.getCodigo(), impressaoContratoVO, impressaoContratoVO, textoPadraoImprimirVO.getTipo(), 
        				impressaoContratoVO.getMatriculaPeriodoVO().getTurma(), impressaoContratoVO.getDisciplinaVO(), getConfiguracaoGeralPadraoSistema(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), true));
        		setImprimirContrato(Uteis.isAtributoPreenchido(getCaminhoRelatorio()));	
        		setMensagemID("msg_impressaoContrato_contratoDeclaracao");
            }
        } catch (Exception e) {
            setImprimirContrato(Boolean.FALSE);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void imprimirTextoPadraoEstagio() {
        try {
        	TextoPadraoDeclaracaoVO textoPadraoImprimirVO = (TextoPadraoDeclaracaoVO) context().getExternalContext().getRequestMap().get("textoPadraoItem");
        	if (Uteis.isAtributoPreenchido(textoPadraoImprimirVO)) {
        		limparMensagem();
        		setFazerDownload(false);
        		setCaminhoRelatorio("");	
        		ImpressaoContratoVO impressaoContratoVO = prepararImpressaoContratoEstagio();
        		impressaoContratoVO.setGerarNovoArquivoAssinado(false);
        		impressaoContratoVO.setImpressaoPdf(true);
        		setCaminhoRelatorio(getFacadeFactory().getImpressaoDeclaracaoFacade().imprimirDeclaracao(
        				textoPadraoImprimirVO.getCodigo(), impressaoContratoVO, impressaoContratoVO, textoPadraoImprimirVO.getTipo(), 
        				impressaoContratoVO.getMatriculaPeriodoVO().getTurma(), impressaoContratoVO.getDisciplinaVO(), getConfiguracaoGeralPadraoSistema(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), true));
        		setFazerDownload(true);
        		setMensagemID("msg_impressaoContrato_contratoDeclaracao");
        	}
        } catch (Exception e) {
            setImprimirContrato(Boolean.FALSE);
            setFazerDownload(false);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void imprimirTextoPadraoEstagioDOC() {
    	 try {
             TextoPadraoDeclaracaoVO textoPadraoImprimirVO = (TextoPadraoDeclaracaoVO) context().getExternalContext().getRequestMap().get("textoPadraoItem");
             if (Uteis.isAtributoPreenchido(textoPadraoImprimirVO)) {
            	 ImpressaoContratoVO impressaoContratoVO = prepararImpressaoContratoEstagio();         
            	 String arquivo = (getFacadeFactory().getImpressaoDeclaracaoFacade().imprimirDeclaracao(
            			 textoPadraoImprimirVO.getCodigo(), impressaoContratoVO, impressaoContratoVO, textoPadraoImprimirVO.getTipo(), 
            			 impressaoContratoVO.getMatriculaPeriodoVO().getTurma(), impressaoContratoVO.getDisciplinaVO(), getConfiguracaoGeralPadraoSistema(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), true));
            	 setIsDownloadContrato(Uteis.isAtributoPreenchido(arquivo));
            	 HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
            	 String textoHTML = (String) request.getSession().getAttribute("textoRelatorio");
            	 ArquivoHelper.criarArquivoDOC(textoHTML, getArquivoVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
            	 setMensagemID("msg_impressao_sucesso");
             }
         } catch (Exception e) {
             setImprimirContrato(Boolean.FALSE);
             setMensagemDetalhada("msg_erro", e.getMessage());
         }
    }
    
    public void consultarTextoPadrao() {
        try {
            super.consultar();
            setEstagioImprimirVO(new EstagioVO());                                                  
            setListaContratos(getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("EC", getUnidadeEnsinoLogado().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            if (getListaContratos().isEmpty()) {
                throw new Exception("Não existem Textos Padrões registrados para Convêncio de Estágio. Textos padrões para estágio (contratos, termos, declarações) podem ser cadastrados no módulo acadêmico: Texto Padrão Declaração.");
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public boolean getPodeImprimirParceiro() {
        if ((!getParceiroVO().getCodigo().equals(0)) && (getParceiroVO().getConveniadaParaVagasEstagio())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    public String getDownloadContrato() {
        try {
            if (getIsDownloadContrato()) {
                context().getExternalContext().getSessionMap().put("nomeArquivo", getArquivoVO().getNome());
                context().getExternalContext().getSessionMap().put("pastaBaseArquivo", getArquivoVO().getPastaBaseArquivo());
                context().getExternalContext().getSessionMap().put("deletarArquivo", Boolean.TRUE);
                return "location.href='../../DownloadSV'";
            }
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }    

    public String getURLImprimirContrato() {
        if (getImprimirContrato()) {
        	return "abrirPopup('../../VisualizarContrato', 'RelatorioContrato', 730, 545);";
        }
        return "";
    }
    
    private void executarVerificacaoPermissaoImprimirTextoPadraoDeclaracao() {
    	try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidade("PermitirRealizarImpressaoContrato", getUsuarioLogado());
			setPermitirRealizarImpressaoContrato(true);
		} catch (Exception e) {
			setPermitirRealizarImpressaoContrato(false);
		}
    }

	public boolean isPermitirRealizarImpressaoContrato() {
		return permitirRealizarImpressaoContrato;
	}

	public void setPermitirRealizarImpressaoContrato(boolean permitirRealizarImpressaoContrato) {
		this.permitirRealizarImpressaoContrato = permitirRealizarImpressaoContrato;
	}
	
	public void consultarAreaProfissional() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaAreaProfissional().equals("descricao")) {
				objs = getFacadeFactory().getAreaProfissionalFacade().consultarPorDescricaoAreaProfissionalAtivo(getValorConsultaAreaProfissional(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaAreaProfissionalVOs(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAreaProfissionalVOs(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAreaProfissional() {
		try {
			AreaProfissionalVO obj = (AreaProfissionalVO) context().getExternalContext().getRequestMap().get("areaProfissionalItens");
			this.setAreaProfissionalVO(obj);
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getCampoConsultaAreaProfissional() {
		if (campoConsultaAreaProfissional == null) {
			campoConsultaAreaProfissional = "";
		}
		return campoConsultaAreaProfissional;
	}

	public void setCampoConsultaAreaProfissional(String campoConsultaAreaProfissional) {
		this.campoConsultaAreaProfissional = campoConsultaAreaProfissional;
	}

	public String getValorConsultaAreaProfissional() {
		if (valorConsultaAreaProfissional == null) {
			valorConsultaAreaProfissional = "";
		}
		return valorConsultaAreaProfissional;
	}

	public void setValorConsultaAreaProfissional(String valorConsultaAreaProfissional) {
		this.valorConsultaAreaProfissional = valorConsultaAreaProfissional;
	}

	public List<AreaProfissionalVO> getListaConsultaAreaProfissionalVOs() {
		if (listaConsultaAreaProfissionalVOs == null) {
			listaConsultaAreaProfissionalVOs = new ArrayList<AreaProfissionalVO>(0);
		}
		return listaConsultaAreaProfissionalVOs;
	}

	public void setListaConsultaAreaProfissionalVOs(List<AreaProfissionalVO> listaConsultaAreaProfissionalVOs) {
		this.listaConsultaAreaProfissionalVOs = listaConsultaAreaProfissionalVOs;
	}

	public AreaProfissionalVO getAreaProfissionalVO() {
		if (areaProfissionalVO == null) {
			areaProfissionalVO = new AreaProfissionalVO();
		}
		return areaProfissionalVO;
	}

	public void setAreaProfissionalVO(AreaProfissionalVO areaProfissionalVO) {
		this.areaProfissionalVO = areaProfissionalVO;
	}

	public List<SelectItem> getTipoConsultaComboAreaProfissional() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		return itens;
	}	
	
	public String getCampoConsultaCentroDespesa() {
		if (campoConsultaCentroDespesa == null) {
			campoConsultaCentroDespesa = "";
		}
		return campoConsultaCentroDespesa;
	}

	public void setCampoConsultaCentroDespesa(String campoConsultaCentroDespesa) {
		this.campoConsultaCentroDespesa = campoConsultaCentroDespesa;
	}

	public List getListaConsultaCentroDespesa() {
		if (listaConsultaCentroDespesa == null) {
			listaConsultaCentroDespesa = new ArrayList(0);
		}
		return listaConsultaCentroDespesa;
	}

	public void setListaConsultaCentroDespesa(List listaConsultaCentroDespesa) {
		this.listaConsultaCentroDespesa = listaConsultaCentroDespesa;
	}

	public String getValorConsultaCentroDespesa() {
		if (valorConsultaCentroDespesa == null) {
			valorConsultaCentroDespesa = "";
		}
		return valorConsultaCentroDespesa;
	}

	public void setValorConsultaCentroDespesa(String valorConsultaCentroDespesa) {
		this.valorConsultaCentroDespesa = valorConsultaCentroDespesa;
	}
	
	public List getListaSelectItemContaCorrente() {
        if (listaSelectItemContaCorrente == null) {
            listaSelectItemContaCorrente = new ArrayList(0);
        }
        return listaSelectItemContaCorrente;
    }

    public void setListaSelectItemContaCorrente(List listaSelectItemContaCorrente) {
        this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
    }
    
    public List getListaSelectItemFormaPagamento() {
        if (listaSelectItemFormaPagamento == null) {
            listaSelectItemFormaPagamento = new ArrayList(0);
        }
        return (listaSelectItemFormaPagamento);
    }

    public void setListaSelectItemFormaPagamento(List listaSelectItemFormaPagamento) {
        this.listaSelectItemFormaPagamento = listaSelectItemFormaPagamento;
    }
    
    public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}
    
    public ParceiroUnidadeEnsinoContaCorrenteVO getParceiroUnidadeEnsinoContaCorrenteVO() {
    	if (parceiroUnidadeEnsinoContaCorrenteVO == null) {
    		parceiroUnidadeEnsinoContaCorrenteVO = new ParceiroUnidadeEnsinoContaCorrenteVO();
        }
		return parceiroUnidadeEnsinoContaCorrenteVO;
	}

	public void setParceiroUnidadeEnsinoContaCorrenteVO(ParceiroUnidadeEnsinoContaCorrenteVO parceiroUnidadeEnsinoContaCorrenteVO) {
		this.parceiroUnidadeEnsinoContaCorrenteVO = parceiroUnidadeEnsinoContaCorrenteVO;
	}

	public List<SelectItem> getTipoConsultaComboCentroDespesa() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificadorCentroDespesa", "Identificador Centro Despesa"));
		return itens;
	}
	
	public void limparAreaProfissional() {
		this.setAreaProfissionalVO(new AreaProfissionalVO());
		limparMensagem();
	}
	
	public void selecionarCentroDespesa() {
		CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("centroDespesa");
		getParceiroVO().setCategoriaDespesa(obj);;
	}
	
	public void consultarCentroDespesa() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCentroDespesa().equals("descricao")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCentroDespesa().equals("identificadorCentroDespesa")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCentroDespesa(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCentroDespesa(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void montarListaSelectItemContaCorrente(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarContaCorrentePorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
                if (!obj.getContaCaixa().booleanValue() && obj.getSituacao().equals("AT")) {
                	if(Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())){
                		objs.add(new SelectItem(obj.getCodigo(), obj.getNomeApresentacaoSistema()));
                	}else{
                		objs.add(new SelectItem(obj.getCodigo(), "Banco:" + obj.getAgencia().getBanco().getNome() + " Ag:" + obj.getAgencia().getNumeroAgencia() + "-" + obj.getAgencia().getDigito() + " CC:" + obj.getNumero() + "-" + obj.getDigito() + " Carteira: " + obj.getCarteira()));
                	}
                }
            }
            SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemContaCorrente(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

	private void montarListaSelectItemUnidadeEnsino() throws Exception {
		List resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));

	}
	
	
	public void montarListaSelectItemContaCorrente() throws Exception {
        try {
            montarListaSelectItemContaCorrente("");
        } catch (Exception e) {
          System.out.println("Erro montarListaSelectItemContaCorrente: " + e.getMessage());
            throw e;
        }
    }

    public List consultarContaCorrentePorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getContaCorrenteFacade().consultarPorCodigo(0, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }
    
    
    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>FormaPagamento</code>.
     */
    public void montarListaSelectItemFormaPagamento(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarFormaPagamentoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                FormaPagamentoVO obj = (FormaPagamentoVO) i.next();
                if (obj.isDebitoEmConta() || obj.isDinheiro() || obj.isCheque() || obj.isCartaoCredito() || obj.isCartaoDebito() || obj.isBoletoBancario() || obj.isPermuta()) {
                    objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
                }
            }
            setListaSelectItemFormaPagamento(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>FormaPagamento</code>. Buscando todos os
     * objetos correspondentes a entidade <code>FormaPagamento</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemFormaPagamento() {
        try {
            montarListaSelectItemFormaPagamento("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarFormaPagamentoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getFormaPagamentoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

	public void adicionarAreaProfissionalParceiro() {
		if (!getAreaProfissionalVO().getCodigo().equals(0)) {
			AreaProfissionalParceiroVO obj = new AreaProfissionalParceiroVO();
			obj.setAreaProfissionalVO(getAreaProfissionalVO());
			getFacadeFactory().getAreaProfissionalParceiroFacade().adicionarAreaProfissional(getParceiroVO().getAreaProfissionalParceiroVOs(), obj);
			setAreaProfissionalVO(new AreaProfissionalVO());
		}
		
	}
	
	public void removerAreaProfissionalParceiro() {
		AreaProfissionalParceiroVO obj = (AreaProfissionalParceiroVO) context().getExternalContext().getRequestMap().get("areaProfissionalParceiroItem");
		getFacadeFactory().getAreaProfissionalParceiroFacade().removerAreaProfissionalParceiro(getParceiroVO().getAreaProfissionalParceiroVOs(), obj);
	}
	
	
	public void addParceiroUnidadeEnsinoContaCorrenteVO() {
		try {
			getFacadeFactory().getParceiroFacade().adicionarParceiroUnidadeEnsinoContaCorrenteVO(getParceiroVO(), getParceiroUnidadeEnsinoContaCorrenteVO(), getUsuarioLogado());
			setParceiroUnidadeEnsinoContaCorrenteVO(new ParceiroUnidadeEnsinoContaCorrenteVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarParceiroUnidadeEnsinoContaCorrenteVO() {
		try {
			setParceiroUnidadeEnsinoContaCorrenteVO((ParceiroUnidadeEnsinoContaCorrenteVO) context().getExternalContext().getRequestMap().get("parceiroUnidadeEnsinoContaCorrenteItens"));
			montarListaSelectItemFormaPagamento();
			montarListaSelectItemUnidadeEnsino();
			montarListaSelectItemContaCorrente();
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removeParceiroUnidadeEnsinoContaCorrenteVO() {
		try {
			ParceiroUnidadeEnsinoContaCorrenteVO parametro = (ParceiroUnidadeEnsinoContaCorrenteVO) context().getExternalContext().getRequestMap().get("parceiroUnidadeEnsinoContaCorrenteItens");
			getFacadeFactory().getParceiroFacade().removerParceiroUnidadeEnsinoContaCorrenteVO(getParceiroVO(), parametro);
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
    
	public void selecionarCidadeContato() {
		try {
			CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItens");
			getContatoParceiroVO().setCidade(obj);
			getListaConsultaCidade().clear();
			setValorConsultaCidade("");
			setCampoConsultaCidade("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparCamposConsultaCidade() {
		try {
			getListaConsultaCidade().clear();
			setValorConsultaCidade("");
			setCampoConsultaCidade("");
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	public String getValorSomenteNumero() {
		if(getCampoConsultaCidade().equals("codigo") ) {
		 return "return somenteNumero1(event);";
		}
		return "";
	}
	
	
	public String getSizeChaveEnderecamentoPix() {
		return getSizeChavePix(getParceiroVO().getTipoIdentificacaoChavePixEnum());
	}
	
	public boolean isNumeroBancoItau() {
		return Uteis.isAtributoPreenchido(getParceiroVO().getNumeroBancoRecebimento()) &&  getParceiroVO().getNumeroBancoRecebimento().equals("341");
	}
}
