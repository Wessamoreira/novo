package controle.crm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.TipoMidiaCaptacaoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.OperacaoFuncionalidadeVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAcademicoEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoCRMEnum;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.CursoInteresseVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.TipoProspectVO;
import negocio.comuns.crm.enumerador.TipoProspectEnum;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.segmentacao.SegmentacaoProspectVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisEmail;
import negocio.comuns.utilitarias.UtilNavegacao;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.crm.Prospects;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas prospectsForm.jsp prospectsCons.jsp) com as funcionalidades da classe
 * <code>Prospects</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Prospects
 * @see ProspectsVO
 */
@Controller("ProspectsControle")
@Scope("viewScope")
@Lazy
public class ProspectsControle extends SuperControle {

    private ProspectsVO prospectsVO;
    private ProspectsVO prospectsUnificar;
    protected List listaSelectItemUnidadeEnsino;
    private CursoInteresseVO cursoInteresseVO;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List listaConsultaCurso;
    private String campoConsultaCidade;
    private String valorConsultaCidade;
    private List listaConsultaCidade;
    private String campoConsultaUnificarProspects;
    private String valorConsultaUnificarProspects;
    private List listaConsultaUnificarProspects;
    private Boolean consultarDadoCadastrado;
    private Boolean habilitarConsultaProspect;
    private String ajaxRedirecionarFocoCampo;
    private String valorConsultaTipoProspect;
    private List listaConsultaTipoProspect;
    private List listaConsultaFuncionario;
    private String valorConsultaFuncionario;
    private String campoConsultaFuncionario;
    private String campoConsultaNaturalidade;
    private String valorConsultaNaturalidade;
    private List listaConsultaNaturalidade;
    private List listaSelectItemNacionalidade;
    private FormacaoAcademicaVO formacaoAcademicaVO;
    private List listaSelectItemAreaConhecimento;
    private ProspectsVO prospectVindoBuscaProspect;
    private Boolean permitirUnificarProspectsDuplicados;
    private Boolean permiteAlterarConsultor;
    private List<SegmentacaoProspectVO> listaSegmentacoes;
    private String tipoFiltroConsulta;
    private List listaSelectItemTipoMidia;
    private boolean liberarEdicaoProspectVinculadoPessoaSomenteComSenha=false;
	private String userNameLiberarFuncionalidade;
	private String senhaLiberarFuncionalidade;
	private List<OperacaoFuncionalidadeVO> listaOperacaoFuncionalidadeVO;
    private List<SelectItem> listaSelectItemAcaoAlteracaoConsultor;
    private List<FuncionarioVO> listaConsultores;
    private Boolean definirConsultor;
    private String mensagemAvisoExclusao;	

    public ProspectsControle() throws Exception {
        super();        
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);

    }

    public void realizarGeracaoVinculoProspectComConsultorInteracaoComoResponsavel() {
        try {
            if (!getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getAssociarNovoProspectComConsultorResponsavelCadastro()) {
                return;
            }
            FuncionarioVO funcionario = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(getUsuarioLogado().getPessoa().getCodigo(), Boolean.FALSE, getUsuarioLogado());
            getProspectsVO().setConsultorPadrao(funcionario);
        } catch (Exception e) {
        }
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe
     * <code>Prospects</code> para edição pelo usuário da aplicação.
     */
    @PostConstruct
    public String novo() {
        setProspectsVO(new ProspectsVO());
        getProspectsVO().setResponsavelCadastro(getUsuarioLogadoClone());
        realizarGeracaoVinculoProspectComConsultorInteracaoComoResponsavel();
        getListaConsultaTipoProspect().clear();
        setConsultarDadoCadastrado(Boolean.TRUE);
        inicializarListasSelectItemTodosComboBox();
        setCursoInteresseVO(new CursoInteresseVO());
        consultarSegmentacaoProspect();
        setLiberarEdicaoProspectVinculadoPessoaSomenteComSenha(false);
        setMensagemID("msg_entre_dados", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("prospectsForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe
     * <code>Prospects</code> para alteração. O objeto desta classe é
     * disponibilizado na session da página (request) para que o JSP
     * correspondente possa disponibilizá-lo para edição.
     */
    public String editarFollowUpDadosIncompletos(ProspectsVO obj) {
        obj = montarDadosProspectsVOCompleto(obj);
        setConsultarDadoCadastrado(Boolean.FALSE);
        inicializarAtributosRelacionados(obj);
        obj.setNovoObj(new Boolean(false));
        setProspectsVO(obj);
        try {
            setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getProspectsVO().getArquivoFoto(), PastaBaseArquivoEnum.IMAGEM.toString(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
        } catch (Exception ex) {
            setMensagemID("msg_caminhoFotoInexistente", Uteis.ALERTA);
        }
        inicializarListasSelectItemTodosComboBox();
        verificarPermissaoLiberarEdicaoProspectVinculadoPessoaSomenteComSenha();
        setCursoInteresseVO(new CursoInteresseVO());
        setMensagemID("msg_dados_editar", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("prospectsForm.xhtml");
    }

    public void prepararProspectVOParaEdicao(ProspectsVO obj) {
        obj = montarDadosProspectsVOCompleto(obj);
        setConsultarDadoCadastrado(Boolean.FALSE);
        inicializarAtributosRelacionados(obj);
        obj.setNovoObj(new Boolean(false));
        setProspectsVO(getFacadeFactory().getProspectsFacade().executarValidarDadosProspectsConformePessoa(obj));
        setProspectsUnificar(null);
        try {
            setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getProspectsVO().getArquivoFoto(), PastaBaseArquivoEnum.IMAGEM.toString(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
        } catch (Exception ex) {
            setMensagemID("msg_caminhoFotoInexistente", Uteis.ALERTA);
        }
        inicializarListasSelectItemTodosComboBox();
        verificarPermissaoLiberarEdicaoProspectVinculadoPessoaSomenteComSenha();
        setCursoInteresseVO(new CursoInteresseVO());
        setMensagemID("msg_dados_editar", Uteis.ALERTA);
    }

    public String editar()  {
    	try {
    		ProspectsVO obj = (ProspectsVO) context().getExternalContext().getRequestMap().get("prospectsItens");
            prepararProspectVOParaEdicao(obj);
            consultarSegmentacaoProspect();
            getFacadeFactory().getProspectsFacade().realizarRecuperacaoProspectSegmentacaoOpcao(getProspectsVO(), getListaSegmentacoes(), getUsuarioLogado());
            setProspectsUnificar(null);
            setDefinirConsultor(Boolean.FALSE);            
            setMensagemID("msg_dados_editar", Uteis.ALERTA);	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
        
        return Uteis.getCaminhoRedirecionamentoNavegacao("prospectsForm.xhtml");
    }
    
    public void editar(ProspectsVO obj) throws Exception{
         prepararProspectVOParaEdicao(obj);
         consultarSegmentacaoProspect();
         getFacadeFactory().getProspectsFacade().realizarRecuperacaoProspectSegmentacaoOpcao(getProspectsVO(), getListaSegmentacoes(), getUsuarioLogado());
    }

    /**
     * Método responsável inicializar objetos relacionados a classe
     * <code>ProspectsVO</code>. Esta inicialização é necessária por exigência
     * da tecnologia JSF, que não trabalha com valores nulos para estes
     * atributos.
     */
    public void inicializarAtributosRelacionados(ProspectsVO obj) {
        if (obj.getUnidadeEnsino() == null) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
        }
        if (obj.getPessoa() == null) {
            obj.setPessoa(new PessoaVO());
        }
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto
     * da classe <code>Prospects</code>. Caso o objeto seja novo (ainda não
     * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
     * contrário é acionado o <code>alterar()</code>. Se houver alguma
     * inconsistência o objeto não é gravado, sendo re-apresentado para o
     * usuário juntamente com uma mensagem de erro.
     */
    public String persistir() {
        
    	try {

        	if (getProspectsVO().getFornecedor().getCodigo() != 0) {
                getFacadeFactory().getProspectsFacade().realizarAtualizacaoTelefoneFornecedor(getProspectsVO(), getUsuarioLogado());
            }
            if (getProspectsVO().getParceiro().getCodigo() != 0) {
                getFacadeFactory().getProspectsFacade().realizarAtualizacaoTelefoneParceiro(getProspectsVO(), getUsuarioLogado());
            }
            if (getProspectsVO().getUnidadeEnsinoProspect().getCodigo() != 0) {
                getFacadeFactory().getProspectsFacade().realizarAtualizacaoTelefoneUnidadeEnsino(getProspectsVO(), getUsuarioLogado());
            }

            getFacadeFactory().getProspectsFacade().realizarCriacaoProspectSegmentacaoOpcao(getProspectsVO(), getListaSegmentacoes(), getUsuarioLogado());
            if(getProspectsVO().getPessoa().getCodigo()!= null && getProspectsVO().getPessoa().getCodigo()!= 0){
            	if(getProspectsVO().getCpf() == null || getProspectsVO().getCpf().trim().equals("")){
            		throw new Exception("Já existe uma pessoa cadastrada para este PROSPECT, não é possível ALTERAR este PROSPECT removendo o CPF");
            	}
            }
            
            getFacadeFactory().getProspectsFacade().persistir(getProspectsVO(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            getProspectsVO().setConsultorAlterado(false);
            //getProspectsVO().setSincronizadoRDStation(false);
            setConsultarDadoCadastrado(Boolean.FALSE);
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);

        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
        	return Uteis.getCaminhoRedirecionamentoNavegacao("prospectsForm.xhtml");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP
     * ProspectsCons.jsp. Define o tipo de consulta a ser executada, por meio de
     * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
     * resultado, disponibiliza um List com os objetos selecionados na sessao da
     * pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            setListaConsulta(getFacadeFactory().getProspectsFacade().consultar(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), getControleConsulta().getCampoConsulta(), false, getUsuarioLogado(), getTipoFiltroConsulta()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
        	return Uteis.getCaminhoRedirecionamentoNavegacao("prospectsCons.xhtml");
        }
    }

    public void consultarNaturalidade() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaNaturalidade().equals("codigo")) {
                if (getValorConsultaNaturalidade().equals("")) {
                    setValorConsultaNaturalidade("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaNaturalidade());
                objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
            }
            if (getCampoConsultaNaturalidade().equals("nome")) {
                objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaNaturalidade(), false, getUsuarioLogado());
            }

            setListaConsultaNaturalidade(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaNaturalidade(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe
     * <code>ProspectsVO</code> Após a exclusão ela automaticamente aciona a
     * rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getProspectsFacade().excluir(getProspectsVO(), true, getUsuarioLogado());
            setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
            setConsultarDadoCadastrado(Boolean.TRUE);
            setProspectsVO(new ProspectsVO());
            setCursoInteresseVO(new CursoInteresseVO());
            consultarSegmentacaoProspect();
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
            return "";
        }
    }

    public String realizarNavegacaoFollowUp() {
        UtilNavegacao.executarMetodoControle("FollowUpControle", "realizarValidacaoProspectExiste", getProspectsVO());
        return Uteis.getCaminhoRedirecionamentoNavegacao("followUpForm.xhtml");
    }

    public ProspectsVO montarDadosProspectsVOCompleto(ProspectsVO obj) {
        try {
            getFacadeFactory().getProspectsFacade().carregarDados(obj, getUsuarioLogado());
            return obj;
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return new ProspectsVO();
    }

    /*
     * Método responsável por adicionar um novo objeto da classe
     * <code>CursoInteresse</code> para o objeto <code>prospectsVO</code> da
     * classe <code>Prospects</code>
     */
    public void adicionarCursoInteresse() throws Exception {
        try {
            if (!getProspectsVO().getCodigo().equals(0)) {
                getCursoInteresseVO().setProspects(getProspectsVO());
            }
            if (getCursoInteresseVO().getCurso().getCodigo().intValue() != 0) {
                Integer campoConsulta = getCursoInteresseVO().getCurso().getCodigo();
                CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
                getCursoInteresseVO().setCurso(curso);
                getFacadeFactory().getProspectsFacade().adicionarObjCursoInteresseVOs(getProspectsVO(), getCursoInteresseVO());
            } else {
                setMensagemDetalhada("msg_erro", "Nenhum curso selecionado para adição", Uteis.ERRO);
            }
            this.setCursoInteresseVO(new CursoInteresseVO());
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void adicionarFormacaoAcademica() throws Exception {
        try {
            if (!getProspectsVO().getCodigo().equals(0)) {
                formacaoAcademicaVO.setProspectsVO(getProspectsVO());
            }
            getFacadeFactory().getProspectsFacade().adicionarObjFormacaoAcademicaVOs(getFormacaoAcademicaVO(), getProspectsVO());
            this.setFormacaoAcademicaVO(new FormacaoAcademicaVO());
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void editarFormacaoAcademica() throws Exception {
        FormacaoAcademicaVO obj = (FormacaoAcademicaVO) context().getExternalContext().getRequestMap().get("formacaoAcademicaItem");
        setFormacaoAcademicaVO(obj);
    }

    public void removerFormacaoAcademica() throws Exception {
        FormacaoAcademicaVO obj = (FormacaoAcademicaVO) context().getExternalContext().getRequestMap().get("formacaoAcademicaItem");
        getFacadeFactory().getProspectsFacade().excluirObjFormacaoAcademicaVOs(obj.getCurso(), getProspectsVO());
        setMensagemID("msg_dados_excluidos");
    }

    public void consultarTipoProspect() throws Exception {
        setListaConsultaTipoProspect(getFacadeFactory().getProspectsFacade().consultarTipoProspect(getProspectsVO().getCpf(), getProspectsVO().getCnpj(), getProspectsVO().getCodigo()));
        setHabilitarConsultaProspect(true);
    }

    public void consultarUnificarProspect() {
        try {
            setListaConsultaUnificarProspects(getFacadeFactory().getProspectsFacade().consultar(getValorConsultaUnificarProspects(), getUnidadeEnsinoLogado().getCodigo(), getCampoConsultaUnificarProspects(), false, getUsuarioLogado(), null));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {
            getListaConsultaUnificarProspects().clear();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void verificarDefinirConsultor() throws Exception {
    	this.setListaConsultores(new ArrayList<FuncionarioVO>());
    	this.getProspectsUnificar().getCodigo();
    	this.getProspectsVO().getCodigo();
    	listaConsultores = getFacadeFactory().getFuncionarioFacade().consultarConsultoresPropsectsUnificacao(this.getProspectsUnificar().getCodigo(), this.getProspectsVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
    	if (!listaConsultores.isEmpty()) {
    		if (listaConsultores.size() == 1) {
    			getProspectsVO().setConsultorPadrao(listaConsultores.get(0));
    			this.setListaConsultores(new ArrayList<FuncionarioVO>());
    			setDefinirConsultor(Boolean.FALSE);
    		} else {
    			setDefinirConsultor(Boolean.TRUE);
    		}
    	} else {
    		this.setListaConsultores(new ArrayList<FuncionarioVO>());
    		setDefinirConsultor(Boolean.FALSE);
    	}
    }
    
    public void selecionarConsultorUnificarProspect() throws Exception {
    	FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("consultorItens");
    	getProspectsVO().setConsultorPadrao(obj);
    	setDefinirConsultor(Boolean.FALSE);
    }
    
	public void selecionarUnificarProspect() throws Exception {
        ProspectsVO obj = (ProspectsVO) context().getExternalContext().getRequestMap().get("unificarProspectItens");
        this.setProspectsUnificar(obj);
        verificarDefinirConsultor();        
        Uteis.liberarListaMemoria(this.getListaConsultaUnificarProspects());
        this.setValorConsultaUnificarProspects(null);
        this.setValorConsultaUnificarProspects(null);
    }

	public void unificarProspectsBaseTotal() throws Exception {
        try {
        	getFacadeFactory().getProspectsFacade().obterProspectsDuplicadosUnificando(getUsuarioLogadoClone());
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (Exception e) {
            setProspectsUnificar(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
	
    public void unificarProspect() throws Exception {
        try {
            if (getProspectsVO().getCodigo().intValue() == getProspectsUnificar().getCodigo().intValue()) {
                throw new Exception("O prospect manter não pode ser o mesmo do prospect remover.");
            }
            getFacadeFactory().getProspectsFacade().unificarProspects(getProspectsVO().getCodigo(), getProspectsUnificar().getCodigo(), getUsuarioLogado());
            getFacadeFactory().getProspectsFacade().alterarConsultorProspect(getProspectsVO(), getUsuarioLogado());
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (Exception e) {
            setProspectsUnificar(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String getAbrirModalProspect() {
        if (getHabilitarConsultaProspect()) {
        	setMensagemID("msg_dados_consultados", Uteis.SUCESSO);		
            setHabilitarConsultaProspect(false);
            if (getListaConsultaTipoProspect().isEmpty()) {
                limparDadosReferenteTipoProspect();
                return "RichFaces.$('panelTipoProspect').hide();";
//            } else if (getListaConsultaTipoProspect().size() == 1) {
//                selecionarProspectPesquisaCpfCnpjSemModal();
//                return "Richfaces.hideModalPanel('panelTipoProspect');";
            } else {
                return "RichFaces.$('panelTipoProspect').show();";
            }
        }
        return "RichFaces.$('panelTipoProspect').hide();";
    }

    public void selecionarProspectPesquisaCpfCnpjComModal() {
        TipoProspectVO obj = (TipoProspectVO) context().getExternalContext().getRequestMap().get("tipoProspectItens");
        try {
            selecionarProspectPesquisaCpfCnpj(obj);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void selecionarProspectPesquisaCpfCnpjSemModal() {
        TipoProspectVO obj = (TipoProspectVO) getListaConsultaTipoProspect().get(0);
        try {
            selecionarProspectPesquisaCpfCnpj(obj);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void selecionarProspectPesquisaCpfCnpj(TipoProspectVO obj) throws Exception {
        try {
            if (obj.getPerfisProspect().equals("PESSOA")) {
            	if(Uteis.isAtributoPreenchido(obj.getCpf())){
            		setProspectsVO(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorPessoa(obj.getCpf(), getProspectsVO(), getUsuarioLogado()));
            	}else if (Uteis.isAtributoPreenchido(obj.getEmail())) {
        			ProspectsVO p = getFacadeFactory().getProspectsFacade().consultarPorEmailUnico(obj.getEmail(), false, getUsuarioLogado());
                	getProspectsVO().setCodigo(p.getCodigo());
                    if (!getProspectsVO().getArquivoFoto().isNovoObj()) {
                        setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getProspectsVO().getArquivoFoto(), PastaBaseArquivoEnum.IMAGEM.toString().toLowerCase(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
                    }
        		} else {
        			ProspectsVO p = getFacadeFactory().getProspectsFacade().consultarPorCPFCNPJUnico(getProspectsVO(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        			getProspectsVO().setCodigo(p.getCodigo());
                    if (!getProspectsVO().getArquivoFoto().isNovoObj()) {
                        setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getProspectsVO().getArquivoFoto(), PastaBaseArquivoEnum.IMAGEM.toString().toLowerCase(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
                    }
        		}
            } else if (obj.getPerfisProspect().equals("PARCEIRO") && getProspectsVO().getTipoProspect().equals(TipoProspectEnum.JURIDICO)) {
                setProspectsVO(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorParceiroCnpj(obj.getCnpj(), getProspectsVO(), getUsuarioLogado()));
            } else if (obj.getPerfisProspect().equals("PARCEIRO") && getProspectsVO().getTipoProspect().equals(TipoProspectEnum.FISICO)) {
                setProspectsVO(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorParceiroCpf(obj.getCpf(), getProspectsVO(), getUsuarioLogado()));
            } else if (obj.getPerfisProspect().equals("FORNECEDOR") && getProspectsVO().getTipoProspect().equals(TipoProspectEnum.JURIDICO)) {
                setProspectsVO(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorFornecedorCnpj(obj.getCnpj(), getProspectsVO(), getUsuarioLogado()));
            } else if (obj.getPerfisProspect().equals("FORNECEDOR") && getProspectsVO().getTipoProspect().equals(TipoProspectEnum.FISICO)) {
                setProspectsVO(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorFornecedorCpf(obj.getCpf(), getProspectsVO(), getUsuarioLogado()));
            } else if (obj.getPerfisProspect().equals("PROSPECT")) {
            	if(Uteis.isAtributoPreenchido(obj.getCpf())){
            		getProspectsVO().setCpf(obj.getCpf());
            		setProspectsVO(getFacadeFactory().getProspectsFacade().consultarPorCPFCNPJUnico(getProspectsVO(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            	}else if(Uteis.isAtributoPreenchido(obj.getEmail())){
            		setProspectsVO(getFacadeFactory().getProspectsFacade().consultarPorEmailUnico(obj.getEmail(), false, getUsuarioLogado()));
            	}
                setProspectsVO(montarDadosProspectsVOCompleto(getProspectsVO()));
			} else {
                setProspectsVO(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorUnidade(obj.getCnpj(), obj.getNome(), getProspectsVO(), getUsuarioLogado()));
            }
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void limparConsultaFuncionario() {
        getListaConsultaFuncionario().clear();
    }

    public void limparDadosFuncionario() {
        getProspectsVO().setConsultorPadrao(null);
    }

    public void inicializarDadosMensagem() {
        setMensagemID("msg_entre_dados", Uteis.ALERTA);
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe
     * <code>CursoInteresse</code> para edição pelo usuário.
     */
    public void editarCursoInteresse() throws Exception {
        CursoInteresseVO obj = (CursoInteresseVO) context().getExternalContext().getRequestMap().get("cursoInteresseItem");
        setCursoInteresseVO(obj);
    }

    /*
     * Método responsável por remover um novo objeto da classe
     * <code>CursoInteresse</code> do objeto <code>prospectsVO</code> da classe
     * <code>Prospects</code>
     */
    public void removerCursoInteresse() throws Exception {
        CursoInteresseVO obj = (CursoInteresseVO) context().getExternalContext().getRequestMap().get("cursoInteresseItem");
        getFacadeFactory().getProspectsFacade().excluirObjCursoInteresseVOs(getProspectsVO(), obj.getCurso().getCodigo());
        setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
    }

    public void carregarEndereco() {
        try {
            getFacadeFactory().getEnderecoFacade().carregarEndereco(getProspectsVO(), getUsuarioLogado());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarCidade() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCidade().equals("nome")) {
                if (getValorConsultaCidade().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidade(), false, getUsuarioLogado());
            }
            setListaConsultaCidade(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCidade(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void selecionarCidadeFormacao() {
        CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeFormacaoItens");
        getFormacaoAcademicaVO().setCidade(obj);
        listaConsultaCidade.clear();
        this.setValorConsultaCidade("");
        this.setCampoConsultaCidade("");
    }

    /**
     * Método responsável por selecionar o objeto CidadeVO <code>Cidade/code>.
     */
    public void selecionarCidade() {
        CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItem");
        getProspectsVO().setCidade(obj);
        listaConsultaCidade.clear();
        this.setValorConsultaCidade("");
        this.setCampoConsultaCidade("");
    }

    /**
     * Método responsável por carregar umaCombobox com os tipos de pesquisa de
     * Cidade <code>Cidade/code>.
     */
    public List getTipoConsultaCidade() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }

    public List getTipoConsultaUnificarProspects() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("NOME", "Nome"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("EMAIL", "E-mail"));
        return itens;
    }

    public void consultarFuncionario() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaFuncionario().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaFuncionario().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), 0, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("CPF")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("cargo")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("departamento")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionario(), "FU", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "FU", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaFuncionario(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarFuncionario() {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
        if(!getProspectsVO().isNovoObj() && getProspectsVO().getConsultorPadrao().getCodigo().intValue() > 0
        		&& getProspectsVO().getConsultorPadrao().getCodigo().intValue() != obj.getCodigo()){
        	getProspectsVO().setConsultorAlterado(true);
        }else{
        	getProspectsVO().setConsultorAlterado(false);
        }
        getProspectsVO().setConsultorPadrao(obj);        
    }

    public List getTipoConsultaComboFuncionario() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("cargo", "Cargo"));
        itens.add(new SelectItem("departamento", "Departamento"));
        itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
        return itens;
    }

    /**
     * Método responsável por processar a consulta na entidade
     * <code>Curso</code> por meio dos parametros informados no richmodal. Esta
     * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
     * busca pelos parâmentros informados no richModal montando automaticamente
     * o resultado da consulta para apresentação.
     */
    public void consultarCurso() {
        try {
            setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultar(getCampoConsultaCurso(), getValorConsultaCurso(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            getListaConsultaCurso().clear();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public List getTipoConsultaCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }

    public void selecionarCurso() throws Exception {
        CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
        if (getMensagemDetalhada().equals("")) {
            this.getCursoInteresseVO().setCurso(obj);
        }
        Uteis.liberarListaMemoria(this.getListaConsultaCurso());
        this.setValorConsultaCurso(null);
        this.setCampoConsultaCurso(null);
    }

    public void selecionarNaturalidade() {
        CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("naturalidadeItens");
        getProspectsVO().setNaturalidade(obj);
        getListaConsultaNaturalidade().clear();
        this.setValorConsultaNaturalidade("");
        this.setCampoConsultaNaturalidade("");
    }

    public void limparCampoCurso() {
        this.getCursoInteresseVO().setCurso(new CursoVO());
    }

    public void limparDadosCidade() {
        getProspectsVO().setCidade(new CidadeVO());
    }

    public void montarListaSelectItemTipoMidia() {
        try {
            montarListaSelectItemTipoMidia("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());
        }
    }

    public void montarListaSelectItemTipoMidia(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarTipoMidiaPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                TipoMidiaCaptacaoVO obj = (TipoMidiaCaptacaoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNomeMidia()));
            }
            setListaSelectItemTipoMidia(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List consultarTipoMidiaPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getTipoMidiaCaptacaoFacade().consultarPorNomeMidia(nomePrm, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo
     * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
     * <code>UnidadeEnsino</code>.
     */
    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
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

    public List getListaSelectItemSituacaoFormacaoAcademica() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable situacaoFormacaoAcademicas = (Hashtable) Dominios.getSituacaoFormacaoAcademica();
        Enumeration keys = situacaoFormacaoAcademicas.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) situacaoFormacaoAcademicas.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    public List getListaSelectItemTipoInstFormacaoAcademica() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoInstFormacaoAcademicas = (Hashtable) Dominios.getTipoInstFormacaoAcademica();
        Enumeration keys = tipoInstFormacaoAcademicas.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoInstFormacaoAcademicas.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    public void montarListaSelectItemNacionalidade(String prm) throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarPaizPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                PaizVO obj = (PaizVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNacionalidade()));
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemNacionalidade(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            ordenador = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }

    }

    public void montarListaSelectItemAreaConhecimento(String prm) throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarAreaConhecimentoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                AreaConhecimentoVO obj = (AreaConhecimentoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemAreaConhecimento(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            ordenador = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List consultarAreaConhecimentoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getAreaConhecimentoFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
        return lista;
    }

    public List consultarPaizPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getPaizFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
        return lista;
    }

    public List getListaSelectItemEstadoCivilProspect() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable estadoCivils = (Hashtable) Dominios.getEstadoCivil();
        Enumeration keys = estadoCivils.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) estadoCivils.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    public List getTipoConsultaNaturalidade() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo
     * <code>UnidadeEnsino</code>. Buscando todos os objetos correspondentes a
     * entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros
     * para filtragem de dados, isto é importante para a inicialização dos dados
     * da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());
            ;
        }
    }

    public void montarListaSelectItemNacionalidade() {
        try {
            montarListaSelectItemNacionalidade("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());
            ;
        }
    }

    public void montarListaSelectItemAreaConhecimento() {
        try {
            montarListaSelectItemAreaConhecimento("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());
            ;
        }
    }

    public List getListaSelectItemEscolaridadeFormacaoAcademica() throws Exception {
        List objs = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(NivelFormacaoAcademica.class, false);
        return objs;
    }

    /**
     * Método responsável por inicializar a lista de valores (
     * <code>SelectItem</code>) para todos os ComboBox's.
     */    
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemNacionalidade();
        montarListaSelectItemAreaConhecimento();
        montarListaSelectItemTipoMidia();
    }

    /**
     * Rotina responsável por atribui um javascript com o método de mascara para
     * campos do tipo Data, CPF, CNPJ, etc.
     */
    public String getMascaraConsulta() {
        return "";
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes
     * de uma consulta.
     */
    public String inicializarConsultar() {
        limparCampoCurso();
        getListaConsulta().clear();
        getControleConsulta().setValorConsulta("");
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("prospectsCons.xhtml");
    }

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do
     * backing bean. Garantindo uma melhor atuação do Garbage Coletor do Java. A
     * mesma é automaticamente quando realiza o logout.
     */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        prospectsVO = null;
        Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
        cursoInteresseVO = null;
    }

    public void renderizarUpload() {
        setExibirUpload(false);
    }

    public void upLoadImagem(FileUploadEvent uploadEvent) {
        try {
            getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getProspectsVO().getArquivoFoto(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.IMAGEM_TMP, getUsuarioLogado());
            setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getProspectsVO().getArquivoFoto(), PastaBaseArquivoEnum.IMAGEM_TMP.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", true));
            setExibirBotao(Boolean.TRUE);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            uploadEvent = null;
        }
    }

    public void recortarFoto() {
        try {
            getFacadeFactory().getArquivoHelper().recortarFoto(getProspectsVO().getArquivoFoto(), getConfiguracaoGeralPadraoSistema(), getLargura(), getAltura(), getX(), getY());
            setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getProspectsVO().getArquivoFoto(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg", false));
            getProspectsVO().getArquivoFoto().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.IMAGEM);
            setRemoverFoto((Boolean) false);
            cancelar();
            setOncompleteModal("RichFaces.$('panelImagem').hide();");
        } catch (Exception ex) {
            setOncompleteModal("RichFaces.$('panelImagem').show();");
            setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
        }
    }

    public void cancelar() {
        setExibirUpload(true);
        setExibirBotao(false);
    }

    public String getShowFotoCrop() {
        try {
            if (getProspectsVO().getArquivoFoto().getNome() == null) {
                return "resources/imagens/usuarioPadrao.jpg";
            }
            return getCaminhoFotoUsuario()+"?UID="+new Date().getTime();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getTagImageComFotoPadrao();
        }
    }

    public void limparDadosReferenteTipoProspect() {
        setHabilitarConsultaProspect(true);
        if (getProspectsVO().getFisico()) {
            getProspectsVO().setRazaoSocial("");
            getProspectsVO().setInscricaoEstadual("");
            getProspectsVO().setCnpj("");
        } else if (getProspectsVO().getJuridico()) {
            getProspectsVO().setCpf("");
            getProspectsVO().setSexo("");
            getProspectsVO().setRg("");
            getProspectsVO().setDataNascimento(new Date());
        }
    }

    public String fechar() {
        setMensagemDetalhada("", "");
        return Uteis.getCaminhoRedirecionamentoNavegacao("prospectsCons.xhtml");
    }

    public String getCampoConsultaCurso() {
        if (campoConsultaCurso == null) {
            campoConsultaCurso = "";
        }
        return campoConsultaCurso;
    }

    public void setCampoConsultaCurso(String campoConsultarCurso) {
        this.campoConsultaCurso = campoConsultarCurso;
    }

    public String getValorConsultaCurso() {
        if (valorConsultaCurso == null) {
            valorConsultaCurso = "";
        }

        return valorConsultaCurso;
    }

    public void setValorConsultaCurso(String valorConsultarCurso) {
        this.valorConsultaCurso = valorConsultarCurso;
    }

    public List getListaConsultaCurso() {
        if (listaConsultaCurso == null) {
            listaConsultaCurso = new ArrayList(0);
        }
        return listaConsultaCurso;
    }

    public void setListaConsultaCurso(List listaConsultarCurso) {
        this.listaConsultaCurso = listaConsultarCurso;
    }

    public CursoInteresseVO getCursoInteresseVO() {
        if (cursoInteresseVO == null) {
            cursoInteresseVO = new CursoInteresseVO();
        }
        return cursoInteresseVO;
    }

    public void setCursoInteresseVO(CursoInteresseVO cursoInteresseVO) {
        this.cursoInteresseVO = cursoInteresseVO;
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

    public ProspectsVO getProspectsVO() {
        if (prospectsVO == null) {
            prospectsVO = new ProspectsVO();
        }
        return prospectsVO;
    }

    public void setProspectsVO(ProspectsVO prospectsVO) {
        this.prospectsVO = prospectsVO;
    }

    public String getCampoConsultaCidade() {
        if (campoConsultaCidade == null) {
            campoConsultaCidade = "";
        }
        return campoConsultaCidade;
    }

    public void setCampoConsultaCidade(String campoConsultaCidade) {
        this.campoConsultaCidade = campoConsultaCidade;
    }

    public String getValorConsultaCidade() {
        if (valorConsultaCidade == null) {
            valorConsultaCidade = "";
        }
        return valorConsultaCidade;
    }

    public void setValorConsultaCidade(String valorConsultaCidade) {
        this.valorConsultaCidade = valorConsultaCidade;
    }

    public List getListaConsultaCidade() {
        if (listaConsultaCidade == null) {
            listaConsultaCidade = new ArrayList(0);
        }
        return listaConsultaCidade;
    }

    public void setListaConsultaCidade(List listaConsultaCidade) {
        this.listaConsultaCidade = listaConsultaCidade;
    }

    public Boolean getConsultarDadoCadastrado() {
        if (consultarDadoCadastrado == null) {
            consultarDadoCadastrado = false;
        }
        return consultarDadoCadastrado;
    }

    public void setConsultarDadoCadastrado(Boolean consultarDadoCadastrado) {
        this.consultarDadoCadastrado = consultarDadoCadastrado;
    }

    public String getAjaxRedirecionarFocoCampo() {
        if (ajaxRedirecionarFocoCampo == null) {
            ajaxRedirecionarFocoCampo = "";
        }
        return ajaxRedirecionarFocoCampo;
    }

    public void setAjaxRedirecionarFocoCampo(String ajaxRedirecionarFocoCampo) {
        this.ajaxRedirecionarFocoCampo = ajaxRedirecionarFocoCampo;
    }

    public String getValorConsultaTipoProspect() {
        if (valorConsultaTipoProspect == null) {
            valorConsultaTipoProspect = "";
        }
        return valorConsultaTipoProspect;
    }

    public void setValorConsultaTipoProspect(String valorConsultaTipoProspect) {
        this.valorConsultaTipoProspect = valorConsultaTipoProspect;
    }

    public List getListaConsultaTipoProspect() {
        if (listaConsultaTipoProspect == null) {
            listaConsultaTipoProspect = new ArrayList(0);
        }
        return listaConsultaTipoProspect;
    }

    public void setListaConsultaTipoProspect(List listaConsultaTipoProspect) {
        this.listaConsultaTipoProspect = listaConsultaTipoProspect;
    }

    public Boolean getHabilitarConsultaProspect() {
        if (habilitarConsultaProspect == null) {
            habilitarConsultaProspect = false;
        }
        return habilitarConsultaProspect;
    }

    public void setHabilitarConsultaProspect(Boolean habilitarConsultaProspect) {
        this.habilitarConsultaProspect = habilitarConsultaProspect;
    }

    public String getBotaoAluno() {
    	return "fas fa-user-graduate";
    }
    
    public String getBotaoAluno2() {
        if (getProspectsVO().getPessoa().getAluno()) {
        	return "btn btn-success-icon";
        }
        return "btn btn-default-icon";
    }

    public String getBotaoProfessor() {
    	return "fas fa-chalkboard-teacher";
    }
    
    public String getBotaoProfessor2() {
        if (getProspectsVO().getPessoa().getProfessor()) {
            return "btn btn-success-icon";
        }
        return "btn btn-default-icon";
    }

    public String getBotaoFuncionario() {
    	return "fas fa-user-tie";
    }
    
    public String getBotaoFuncionario2() {
        if (getProspectsVO().getPessoa().getFuncionario()) {
        	return "btn btn-success-icon";
        }
        return "btn btn-default-icon";
    }
    
    public String getBotaoUnidade() {
        if (getProspectsVO().getUnidadeEnsinoProspect().getCodigo() != 0) {
            return "fas fa-book-open";
        }
        return "fas fa-book-open";
    }
    
    public String getBotaoUnidade2() {
        if (getProspectsVO().getUnidadeEnsinoProspect().getCodigo() != 0) {
        	return "btn btn-success-icon";
        }
        return "btn btn-default-icon";
    }
    
    public String getBotaoFornecedor() {
        if (getProspectsVO().getFornecedor().getCodigo() != 0) {
            return "fas fa-people-carry";
        }
        return "fas fa-people-carry";
    }
    
    public String getBotaoFornecedor2() {
        if (getProspectsVO().getFornecedor().getCodigo() != 0) {
        	return "btn btn-success-icon";
        }
        return "btn btn-default-icon";
    }
    
    public String getBotaoParceiro() {
        if (getProspectsVO().getParceiro().getCodigo() != 0) {
            return "fas fa-user-friends";
        }
        return "fas fa-user-friends";
    }
    
    public String getBotaoParceiro2() {
        if (getProspectsVO().getParceiro().getCodigo() != 0) {
        	return "btn btn-success-icon";
        }
        return "btn btn-default-icon";
    }
    
    public Boolean getApresentarCpfCnpj() {
        if (getProspectsVO().getCpf().equals("")) {
            return true;
        }
        return false;
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

    public String getValorConsultaFuncionario() {
        if (valorConsultaFuncionario == null) {
            valorConsultaFuncionario = "";
        }
        return valorConsultaFuncionario;
    }

    public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
        this.valorConsultaFuncionario = valorConsultaFuncionario;
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

    public String getCampoConsultaNaturalidade() {
        if (campoConsultaNaturalidade == null) {
            campoConsultaNaturalidade = "";
        }
        return campoConsultaNaturalidade;
    }

    public void setCampoConsultaNaturalidade(String campoConsultaNaturalidade) {
        this.campoConsultaNaturalidade = campoConsultaNaturalidade;
    }

    public String getValorConsultaNaturalidade() {
        if (valorConsultaNaturalidade == null) {
            valorConsultaNaturalidade = "";
        }
        return valorConsultaNaturalidade;
    }

    public void setValorConsultaNaturalidade(String valorConsultaNaturalidade) {
        this.valorConsultaNaturalidade = valorConsultaNaturalidade;
    }

    public List getListaConsultaNaturalidade() {
        if (listaConsultaNaturalidade == null) {
            listaConsultaNaturalidade = new ArrayList(0);
        }
        return listaConsultaNaturalidade;
    }

    public void setListaConsultaNaturalidade(List listaConsultaNaturalidade) {
        this.listaConsultaNaturalidade = listaConsultaNaturalidade;
    }

    public List getListaSelectItemNacionalidade() {
        if (listaSelectItemNacionalidade == null) {
            listaSelectItemNacionalidade = new ArrayList(0);
        }
        return listaSelectItemNacionalidade;
    }

    public void setListaSelectItemNacionalidade(List listaSelectItemNacionalidade) {
        this.listaSelectItemNacionalidade = listaSelectItemNacionalidade;
    }

    public FormacaoAcademicaVO getFormacaoAcademicaVO() {
        if (formacaoAcademicaVO == null) {
            formacaoAcademicaVO = new FormacaoAcademicaVO();
        }
        return formacaoAcademicaVO;
    }

    public void setFormacaoAcademicaVO(FormacaoAcademicaVO formacaoAcademicaVO) {
        this.formacaoAcademicaVO = formacaoAcademicaVO;
    }

    public List getListaSelectItemAreaConhecimento() {
        if (listaSelectItemAreaConhecimento == null) {
            listaSelectItemAreaConhecimento = new ArrayList(0);
        }
        return listaSelectItemAreaConhecimento;
    }

    public void setListaSelectItemAreaConhecimento(List listaSelectItemAreaConhecimento) {
        this.listaSelectItemAreaConhecimento = listaSelectItemAreaConhecimento;
    }

    /**
     * @return the prospectVindoBuscaProspect
     */
    public ProspectsVO getProspectVindoBuscaProspect() {
        return prospectVindoBuscaProspect;
    }

    /**
     * @param prospectVindoBuscaProspect
     *            the prospectVindoBuscaProspect to set
     */
    public void setProspectVindoBuscaProspect(ProspectsVO prospectVindoBuscaProspect) {
        this.prospectVindoBuscaProspect = prospectVindoBuscaProspect;
    }

    /**
     * @return the permitirUnificarProspectsDuplicados
     */
    public Boolean getPermitirUnificarProspectsDuplicados() {
        if (permitirUnificarProspectsDuplicados == null) {
            try {
                ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Prospects_unificarProspectsDuplicados", getUsuarioLogado());
                permitirUnificarProspectsDuplicados = Boolean.TRUE;
            } catch (Exception e) {
                permitirUnificarProspectsDuplicados = Boolean.FALSE;
            }
        }
        return permitirUnificarProspectsDuplicados;
    }

    /**
     * @param permitirUnificarProspectsDuplicados
     *            the permitirUnificarProspectsDuplicados to set
     */
    public void setPermitirUnificarProspectsDuplicados(Boolean permitirUnificarProspectsDuplicados) {
        this.permitirUnificarProspectsDuplicados = permitirUnificarProspectsDuplicados;
    }

    /**
     * @return the campoConsultaUnificarProspects
     */
    public String getCampoConsultaUnificarProspects() {
        if (campoConsultaUnificarProspects == null) {
            campoConsultaUnificarProspects = "";
        }
        return campoConsultaUnificarProspects;
    }

    /**
     * @param campoConsultaUnificarProspects
     *            the campoConsultaUnificarProspects to set
     */
    public void setCampoConsultaUnificarProspects(String campoConsultaUnificarProspects) {
        this.campoConsultaUnificarProspects = campoConsultaUnificarProspects;
    }

    /**
     * @return the valorConsultaUnificarProspects
     */
    public String getValorConsultaUnificarProspects() {
        if (valorConsultaUnificarProspects == null) {
            valorConsultaUnificarProspects = "";
        }
        return valorConsultaUnificarProspects;
    }

    /**
     * @param valorConsultaUnificarProspects
     *            the valorConsultaUnificarProspects to set
     */
    public void setValorConsultaUnificarProspects(String valorConsultaUnificarProspects) {
        this.valorConsultaUnificarProspects = valorConsultaUnificarProspects;
    }

    /**
     * @return the listaConsultaUnificarProspects
     */
    public List getListaConsultaUnificarProspects() {
        if (listaConsultaUnificarProspects == null) {
            listaConsultaUnificarProspects = new ArrayList(0);
        }
        return listaConsultaUnificarProspects;
    }

    /**
     * @param listaConsultaUnificarProspects
     *            the listaConsultaUnificarProspects to set
     */
    public void setListaConsultaUnificarProspects(List listaConsultaUnificarProspects) {
        this.listaConsultaUnificarProspects = listaConsultaUnificarProspects;
    }

    /**
     * @return the prospectsUnificar
     */
    public ProspectsVO getProspectsUnificar() {
        if (prospectsUnificar == null) {
            prospectsUnificar = new ProspectsVO();
        }
        return prospectsUnificar;
    }

    /**
     * @param prospectsUnificar
     *            the prospectsUnificar to set
     */
    public void setProspectsUnificar(ProspectsVO prospectsUnificar) {
        this.prospectsUnificar = prospectsUnificar;
    }

    public String getApresentarMensagemInativacaoProspect() {
        if (getProspectsVO().getInativo()) {
            return "alert('Ao inativar um prospect o mesmo não será mais listado na busca de Prospect, não será relacionado para novas campanhas de marketing e terá todas suas agendas com status não inicializada removidas.');";
        } else {
            return "";
        }
    }

    public void inativarAtivarProspect() {
        if (getProspectsVO().getInativo()) {
            getProspectsVO().setResponsavelInativacao(getUsuarioLogadoClone());
        } else {
            getProspectsVO().setResponsavelInativacao(null);
            getProspectsVO().setMotivoInativacao("");
        }
    }

    public void consultarSegmentacaoProspect() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "SegmentacaoProspectControle", "Inicializando Consultar Segmento", "Consultando");
            List objs = new ArrayList(0);
            getListaSegmentacoes().clear();
            objs = getFacadeFactory().getSegmentacaoProspectFacade().consultarSegmentosAtivos("", true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            setListaSegmentacoes(objs);
            registrarAtividadeUsuario(getUsuarioLogado(), "SegmentacaoProspectControle", "Finalizando Consultar Segmento", "Consultando");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * @return the permiteAlterarConsultor
     */
    public Boolean getPermiteAlterarConsultor() {
        if (permiteAlterarConsultor == null) {
            try {
                ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Prospects_alterarConsultorResponsavel", getUsuarioLogado());
                permiteAlterarConsultor = Boolean.TRUE;
            } catch (Exception e) {
                permiteAlterarConsultor = Boolean.FALSE;
            }
        }
        return permiteAlterarConsultor;
    }

    /**
     * @param permiteAlterarConsultor
     *            the permiteAlterarConsultor to set
     */
    public void setPermiteAlterarConsultor(Boolean permiteAlterarConsultor) {
        this.permiteAlterarConsultor = permiteAlterarConsultor;
    }

    public List<SegmentacaoProspectVO> getListaSegmentacoes() {

        if (listaSegmentacoes == null) {
            listaSegmentacoes = new ArrayList<SegmentacaoProspectVO>();
        }

        return listaSegmentacoes;
    }

    public void setListaSegmentacoes(List<SegmentacaoProspectVO> listaSegmentacoes) {
        this.listaSegmentacoes = listaSegmentacoes;
    }

    public List getListaSelectItemFiltro() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem("AT", "Ativo"));
        lista.add(new SelectItem("IN", "Inativo"));
        lista.add(new SelectItem("TO", "Todos"));
        return lista;
    }

    public String getTipoFiltroConsulta() {
        if (tipoFiltroConsulta == null) {
            tipoFiltroConsulta = "TO";
        }
        return tipoFiltroConsulta;
    }

    public void setTipoFiltroConsulta(String tipoFiltroConsulta) {
        this.tipoFiltroConsulta = tipoFiltroConsulta;
    }

    public List getListaSelectItemTipoMidia() {
        if (listaSelectItemTipoMidia == null) {
            listaSelectItemTipoMidia = new ArrayList(0);
        }
        return listaSelectItemTipoMidia;
    }

    public void setListaSelectItemTipoMidia(List listaSelectItemTipoMidia) {
        this.listaSelectItemTipoMidia = listaSelectItemTipoMidia;
    }
    
    public List<SelectItem> getListaSelectItemAcaoAlteracaoConsultor() {
		if(listaSelectItemAcaoAlteracaoConsultor == null){
			listaSelectItemAcaoAlteracaoConsultor = new ArrayList<SelectItem>(0);
			listaSelectItemAcaoAlteracaoConsultor.add(new SelectItem("EXCLUIR", "Excluir Compromisso"));
			listaSelectItemAcaoAlteracaoConsultor.add(new SelectItem("ALTERAR", "Alterar Para o Novo Consultor"));
			listaSelectItemAcaoAlteracaoConsultor.add(new SelectItem("MANTER", "Manter Compromisso"));
		}
		return listaSelectItemAcaoAlteracaoConsultor;
	}

	public void setListaSelectItemAcaoAlteracaoConsultor(List<SelectItem> listaSelectItemAcaoAlteracaoConsultor) {
		this.listaSelectItemAcaoAlteracaoConsultor = listaSelectItemAcaoAlteracaoConsultor;
	}

	public Boolean getDefinirConsultor() {
		if (definirConsultor == null) {
			definirConsultor = Boolean.FALSE;
		}
		return definirConsultor;
	}

	public void setDefinirConsultor(Boolean definirConsultor) {
		this.definirConsultor = definirConsultor;
	}

	public List<FuncionarioVO> getListaConsultores() {
		if (listaConsultores == null) {
			listaConsultores = new ArrayList<FuncionarioVO>();
		}
		return listaConsultores;
	}

	public void setListaConsultores(List<FuncionarioVO> listaConsultores) {
		this.listaConsultores = listaConsultores;
	}
	
	public String getUserNameLiberarFuncionalidade() {
		if (userNameLiberarFuncionalidade == null) {
			userNameLiberarFuncionalidade = "";
		}
		return userNameLiberarFuncionalidade;
	}

	public void setUserNameLiberarFuncionalidade(String userNameLiberarValorAcimaPrevisto) {
		this.userNameLiberarFuncionalidade = userNameLiberarValorAcimaPrevisto;
	}

	public String getSenhaLiberarFuncionalidade() {
		if (senhaLiberarFuncionalidade == null) {
			senhaLiberarFuncionalidade = "";
		}
		return senhaLiberarFuncionalidade;
	}

	public void setSenhaLiberarFuncionalidade(String senhaLiberarValorAcimaPrevisto) {
		this.senhaLiberarFuncionalidade = senhaLiberarValorAcimaPrevisto;
	}
	
	public List<OperacaoFuncionalidadeVO> getListaOperacaoFuncionalidadeVO() {
		if (listaOperacaoFuncionalidadeVO == null) {
			listaOperacaoFuncionalidadeVO = new ArrayList<>();
		}
		return listaOperacaoFuncionalidadeVO;
	}

	public void setListaOperacaoFuncionalidadeVO(List<OperacaoFuncionalidadeVO> listaOperacaoFuncionalidadeVO) {
		this.listaOperacaoFuncionalidadeVO = listaOperacaoFuncionalidadeVO;
	}

	public boolean isLiberarEdicaoProspectVinculadoPessoaSomenteComSenha() {
		return liberarEdicaoProspectVinculadoPessoaSomenteComSenha;
	}
	
	public void setLiberarEdicaoProspectVinculadoPessoaSomenteComSenha(boolean liberarEdicaoProspectVinculadoPessoaSomenteComSenha) {
		this.liberarEdicaoProspectVinculadoPessoaSomenteComSenha = liberarEdicaoProspectVinculadoPessoaSomenteComSenha;
	}
	
	public void verificarPermissaoLiberarEdicaoProspectVinculadoPessoaSomenteComSenha() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoCRMEnum.LIBERAR_EDICAO_PROSPECT_VINCULADO_PESSOA_SOMENTE_COM_SENHA, getUsuarioLogadoClone());
			if(Uteis.isAtributoPreenchido(getProspectsVO().getPessoa())) {
				setLiberarEdicaoProspectVinculadoPessoaSomenteComSenha(true);
				setListaOperacaoFuncionalidadeVO(getFacadeFactory().getOperacaoFuncionalidadeFacade().consultarOperacaoFuncionalidadeVOPorCodigoOrigemPorOrigemOperacaoFuncionalidadePorOperacaoFuncionalidade(getProspectsVO().getCodigo(), OrigemOperacaoFuncionalidadeEnum.PROSPECT, OperacaoFuncionalidadeEnum.LIBERAR_EDICAO_COM_SENHA, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogadoClone()));
			}
		} catch (Exception e) {
			setLiberarEdicaoProspectVinculadoPessoaSomenteComSenha(false);
		}
	}
	
	public void persistirLiberarEdicaoProspectVinculadoPessoaSomenteComSenha() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUserNameLiberarFuncionalidade(), this.getSenhaLiberarFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoCRMEnum.LIBERAR_EDICAO_PROSPECT_VINCULADO_PESSOA_SOMENTE_COM_SENHA, usuarioVerif);
			getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.PROSPECT, getProspectsVO().getCodigo().toString(), OperacaoFuncionalidadeEnum.LIBERAR_EDICAO_COM_SENHA, usuarioVerif, ""));
			setLiberarEdicaoProspectVinculadoPessoaSomenteComSenha(false);
		} catch (Exception e) {
			setLiberarEdicaoProspectVinculadoPessoaSomenteComSenha(true);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void gravarProspectMatriculaOnlineExterna() {
        try {
			getFacadeFactory().getProspectsFacade().persistir(getProspectsVO(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void consultarTipoInteracaoProspectEmail() throws Exception {
    	if (!getProspectsVO().getEmailPrincipal().trim().equals("")) {
	        setListaConsultaTipoProspect(getFacadeFactory().getProspectsFacade().consultarTipoProspectEmail(getProspectsVO().getEmailPrincipal(), getUnidadeEnsinoLogado().getCodigo(), getProspectsVO().getCodigo()));
	        setHabilitarConsultaProspect(true);
    	}
    }
	
	public void consultarTipoInteracaoProspectEmailSecundario() throws Exception {
		if (!getProspectsVO().getEmailSecundario().trim().equals("")) {
			setListaConsultaTipoProspect(getFacadeFactory().getProspectsFacade().consultarTipoProspectEmail(getProspectsVO().getEmailSecundario(), getUnidadeEnsinoLogado().getCodigo(), getProspectsVO().getCodigo()));
			setHabilitarConsultaProspect(true);
		}
	}

	public void verificaExcluirProspect() {
		try {
			setMensagemAvisoExclusao(getFacadeFactory().getProspectsFacade().consultarAgendaProspect(getProspectsVO().getCodigo()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
	}

	public String excluirProspect() {
		try {
			getFacadeFactory().getProspectsFacade().excluirProspectERegistrosReferenciados(getProspectsVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
			novo();
			return Uteis.getCaminhoRedirecionamentoNavegacao("prospectsForm.xhtml");
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			return Uteis.getCaminhoRedirecionamentoNavegacao("prospectsForm.xhtml");
		}
	}

	public String getMensagemAvisoExclusao() {
		if (mensagemAvisoExclusao == null) {
			mensagemAvisoExclusao = "";
		}		
		return mensagemAvisoExclusao;
	}

	public void setMensagemAvisoExclusao(String mensagemAvisoExclusao) {
		this.mensagemAvisoExclusao = mensagemAvisoExclusao;
	}
	
	@PostConstruct
	public void editarProspectVindoDaTelaDeBuscaProspect() {
		if (context().getExternalContext().getSessionMap().get("prospectsVOItens") != null) {
			setProspectsVO((ProspectsVO) context().getExternalContext().getSessionMap().get("prospectsVOItens"));
			context().getExternalContext().getSessionMap().remove("prospectsVOItens");
			prepararProspectVOParaEdicao(prospectsVO);
		}
	}
	
	public String enviarProspectParaRdStation() {

		Integer status = 404;		
		try {
			
			if(getProspectsVO().getEmailPrincipal() == null || !UteisEmail.getValidEmail(getProspectsVO().getEmailPrincipal())) {
				setMensagemDetalhada("msg_EmailProspectoNaoPreenchidos", Uteis.ERRO);
			}
			
			if(!getConfiguracaoGeralPadraoSistema().getAtivarIntegracaoRdStation()) {
				setMensagemDetalhada("msg_IntegracaoRdStationDesativada", Uteis.ERRO);
			} else {
				
				status = getFacadeFactory().getLeadInterfaceFacade().incluirLeadNoRdStation(getProspectsVO(), getConfiguracaoGeralPadraoSistema());
				
				switch (status) {
				case 200:
					setMensagemID("msg_ProspectoCadastradoNoRdStation", Uteis.SUCESSO);
					getProspectsVO().setSincronizadoRDStation(true);
					getFacadeFactory().getProspectsFacade().alterarFlagProspectSincronizadoComRDStation(getProspectsVO(), getUsuarioLogado());
					break;
				case 302:
					setMensagemID("msg_ProspectoCadastradoComAlertaNoRdStation", Uteis.ALERTA);
					getProspectsVO().setSincronizadoRDStation(true);
					getFacadeFactory().getProspectsFacade().alterarFlagProspectSincronizadoComRDStation(getProspectsVO(), getUsuarioLogado());
					break;
				case 400:
					setMensagemDetalhada("msg_DadosobrigatoriosNaoPreenchidos", Uteis.ERRO);
					break;
					
				case 401:
					setMensagemDetalhada("msg_TokenInvalido", Uteis.ERRO);
					break;
					
				case 500:
					setMensagemDetalhada("msg_ErroNaIntegracaoRdStation", Uteis.ERRO);
					break;
					
				default:
					break;
				}
			}
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			return Uteis.getCaminhoRedirecionamentoNavegacao("prospectsForm.xhtml");
		}
		
	}
	
	public void setarNomeBatismo() {
		if(!Uteis.isAtributoPreenchido(getProspectsVO().getNomeBatismo())) {
			getProspectsVO().setNomeBatismo(getProspectsVO().getNome());
		}
	}
	
	public void setarNomeSocial() {
		if(!Uteis.isAtributoPreenchido(getProspectsVO().getNome())) {
			getProspectsVO().setNome(getProspectsVO().getNomeBatismo());
		}
	}
	
	public void validarDadosProspects(ProspectsVO prospectVO) {
	
		
	
	}
	
}