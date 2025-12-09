package controle.processosel;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas pessoaForm.jsp pessoaCons.jsp) com as funcionalidades da classe <code>Pessoa</code>.
 * Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Pessoa
 * @see PessoaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.TipoMidiaCaptacaoVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.CursoInteresseVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.enumerador.TipoProspectEnum;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.PreInscricaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.CorRaca;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;

@Controller("CandidatoControle")
@Scope("viewScope")
@Lazy
public class CandidatoControle extends SuperControle implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ProspectsVO prospectVO;
    private MatriculaVO matriculaVO;
    private MatriculaPeriodoVO matriculaPeriodoVO;
    private boolean liberarSelecionarCurso;
    private boolean apresentarMensagem;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private String apresentarRichMensagem;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List<SelectItem> listaSelectItemCurso;
    private PessoaVO pessoaVO;
    private PessoaVO pessoaExistente;
    protected List<SelectItem> listaSelectItemNacionalidade;
    protected List<SelectItem> listaSelectItemAreaConhecimento;
    public Boolean verificarCpf;
    public Boolean consultarPessoa;
    public Boolean consultarCPF;
    public Boolean editarDados;
    private String campoConsultaCidade;
    private String valorConsultaCidade;
    private String campoConsultaNaturalidade;
    private String valorConsultaNaturalidade;
    private List listaConsultaNaturalidade;
    private List listaConsultaCidade;
    private FormacaoAcademicaVO formacaoAcademicaVO;
    private String disciplina_Erro;
    private FiliacaoVO filiacaoVO;
    private String turno_Erro;
    private String tipoPessoa;
    private Boolean inscricaoPermitida;
    private boolean cpfInvalido;
    private Boolean importarDadosPessoa;
    private boolean naoCandidato;
    private List listaSelectItemTipoMidia;

    private String campoConsultaFiliacaoCidade;
    private String valorConsultaFiliacaoCidade;
    private List listaConsultaFiliacaoCidade;
    private ConfiguracaoGeralSistemaVO configuracaoGeralSistema;
    // Esta variavel controla se o prospect foi encontrato por email ou por cpf
    // Caso o mesmo seja encontrado por cpf, a busca por e-mail nem é realizada,
    // pois o prospect já foi detectado e o cpf é mais forte que o e-mail
    private Boolean encontrouProspectAluno;

    public CandidatoControle() throws Exception {
        // obterUsuarioLogado();
        getFacadeFactory().getPessoaFacade().setIdEntidade("Candidato");
        setControleConsulta(new ControleConsulta());
        setLiberarSelecionarCurso(Boolean.FALSE);
        setApresentarMensagem(Boolean.FALSE);
        setMensagemID("msg_entre_prmconsulta");
    }

    @PostConstruct
    public void inicializarDadosServlet() {
        try {
            montarListaSelectItemUnidadeEnsino("");
            montarListaSelectItemTipoMidia();
            if (((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("ue") != null
                    && ((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("cu") != null) {
                boolean existe = false;
                for(SelectItem itemUnidadeEnsino : (List<SelectItem>)getListaSelectItemUnidadeEnsino()){
                    if(itemUnidadeEnsino.getValue().equals(Integer.parseInt(((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("ue")))){
                        existe = true;
                        break;
                    }
                }
                if(existe){
                    getMatriculaVO().getUnidadeEnsino().setCodigo(Integer.parseInt(((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("ue")));
                    getMatriculaVO().getCurso().setCodigo(Integer.parseInt(((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("cu")));
                }else{
                    if(!getListaSelectItemUnidadeEnsino().isEmpty()){
                        getMatriculaVO().getUnidadeEnsino().setCodigo((Integer)((SelectItem)getListaSelectItemUnidadeEnsino().get(0)).getValue());
                    }   
                }                
                setMensagemID("msg_entre_dados");
            }else{
                if(!getListaSelectItemUnidadeEnsino().isEmpty()){
                    getMatriculaVO().getUnidadeEnsino().setCodigo((Integer)((SelectItem)getListaSelectItemUnidadeEnsino().get(0)).getValue());
                }
            }
            realizarLiberarSelecionarCurso();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Pessoa</code> para edição pelo usuário da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setPessoaVO(new PessoaVO());
        setProspectVO(new ProspectsVO());
        inicializarListasSelectItemTodosComboBox();
        montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemCurso();            
        setFiliacaoVO(null);
        setFormacaoAcademicaVO(null);
        setVerificarCpf(this.validarCadastroPorCpf());
        setConsultarPessoa(Boolean.TRUE);
        setEditarDados(null);
        this.setEncontrouProspectAluno(Boolean.FALSE);
        setMensagemID("msg_entre_dados");
        setInscricaoPermitida(null);
        setConsultarCPF(null);
        return Uteis.getCaminhoRedirecionamentoNavegacao("candidatoForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Pessoa</code> para alteração. O objeto desta classe é disponibilizado na session da página
     * (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() throws Exception {
        PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItens");
        obj = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        obj.setNovoObj(Boolean.FALSE);
        setPessoaVO(obj);
        inicializarListasSelectItemTodosComboBox();
        // setPessoaVO(new PessoaVO());
        setFiliacaoVO(new FiliacaoVO());
        setFormacaoAcademicaVO(new FormacaoAcademicaVO());
        setInscricaoPermitida(true);
        setConsultarPessoa(Boolean.FALSE);
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("candidatoForm.xhtml");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Pessoa</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a
     * operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o
     * usuário juntamente com uma mensagem de erro.
     */
    public void gravar() {
        try {
            getFacadeFactory().getPessoaFacade().setIdEntidade("Candidato");
            pessoaVO.setCandidato(Boolean.TRUE);
            //pessoaVO.validarDadosCandidato(getPessoaVO(), getConfiguracaoGeralSistema().getConfiguracaoCandidatoProcessoSeletivoVO());
//	public void incluir(final PessoaVO obj, boolean verificarAcesso, final UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno, boolean deveValidarCPF, boolean validarCandidato) throws Exception {
//public void alterar(final PessoaVO obj, boolean verificarAcesso, final UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno, boolean deveValidarCPF, boolean validarCandidato) throws Exception {
            if(Uteis.isAtributoPreenchido(pessoaVO.getNecessidadesEspeciais())) {
            	pessoaVO.setPortadorNecessidadeEspecial(Boolean.TRUE);
            }
            if (pessoaVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getPessoaFacade().incluir(pessoaVO, true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), false, true, true, false, false);
            } else {
                getFacadeFactory().getPessoaFacade().alterar(pessoaVO, false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), false, false, true, false, false);
            }
            setMensagemID("msg_dados_gravados");
            setInscricaoPermitida(true);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void gravarPreInscricao() {
        PreInscricaoVO pre = new PreInscricaoVO();
        try {
            pre.setUnidadeEnsino(getMatriculaVO().getUnidadeEnsino());
            pre.setCurso(getMatriculaVO().getCurso());
            pre.setData(new Date());
            pre.setProspect(getProspectVO());
            pre.getProspect().getPessoa().setCodigo(getPessoaVO().getCodigo());
            pre.getProspect().setTipoProspect(TipoProspectEnum.FISICO);
            pre.getProspect().setCEP(getPessoaVO().getCEP());
            pre.getProspect().setUnidadeEnsino(getMatriculaVO().getUnidadeEnsino());
            pre.getProspect().setCpf(getPessoaVO().getCPF());
            pre.getProspect().setCelular(getPessoaVO().getCelular());
            pre.getProspect().setTelefoneResidencial(getPessoaVO().getTelefoneRes());
            pre.getProspect().setTelefoneComercial(getPessoaVO().getTelefoneComer());
            pre.getProspect().setNaturalidade(getPessoaVO().getNaturalidade());
            pre.getProspect().setEstadoCivil(getPessoaVO().getEstadoCivil());
            pre.getProspect().setNome(getPessoaVO().getNome());
            pre.getProspect().setNomeBatismo(getPessoaVO().getNomeBatismo());
            pre.getProspect().setEmailPrincipal(getPessoaVO().getEmail());
            pre.getProspect().setSexo(getPessoaVO().getSexo());
            pre.getProspect().setRg(getPessoaVO().getRG());
            pre.getProspect().setDataExpedicao(getPessoaVO().getDataEmissaoRG());
            pre.getProspect().setOrgaoEmissor(getPessoaVO().getOrgaoEmissor());
            pre.getProspect().setEstadoEmissor(getPessoaVO().getEstadoEmissaoRG());
            pre.getProspect().setSetor(getPessoaVO().getSetor());
            pre.getProspect().setEndereco(getPessoaVO().getEndereco());
            pre.getProspect().setDataNascimento(getPessoaVO().getDataNasc());
            pre.getProspect().setCidade(getPessoaVO().getCidade());
            
            CursoInteresseVO cursoInt = new CursoInteresseVO();
            cursoInt.setCurso(getMatriculaVO().getCurso());
            cursoInt.setDataCadastro(new Date());
            Boolean existeCurso = false;
            for(CursoInteresseVO cursoInteresseVO:pre.getProspect().getCursoInteresseVOs()){
            	if(cursoInteresseVO.getCurso().getCodigo().intValue() == cursoInt.getCurso().getCodigo().intValue()){
            		existeCurso = true;
            		break;
            	}
            }
            if(!existeCurso){
            	pre.getProspect().getCursoInteresseVOs().add(cursoInt);
            }
            
            if ((!this.getFormacaoAcademicaVO().getCurso().equals("")) && 
                (!this.getFormacaoAcademicaVO().getInstituicao().equals(""))) {
            	
                pre.getProspect().getFormacaoAcademicaVOs().add(this.getFormacaoAcademicaVO());
            }
            
            
            getFacadeFactory().getPreInscricaoFacade().incluirPreInscricaoAPartirSiteOuHomePreInscricao(pre, getConfiguracaoGeralSistema());
            setApresentarMensagem(Boolean.TRUE);
            this.setEncontrouProspectAluno(Boolean.FALSE);
            if(getConfiguracaoGeralSistema().getUrlConfirmacaoPreInscricao().trim().isEmpty()){
            	setApresentarRichMensagem("RichFaces.$('panelMensagem').show()");
            }else{
            	setApresentarRichMensagem("window.open('"+getConfiguracaoGeralSistema().getUrlConfirmacaoPreInscricao().trim()+"')");
            }            
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            pre.getCompromissoAgendaPessoaHorarioVO().setCodigo(0);
            setMensagemDetalhada("msg_erro", e.getMessage());
            setApresentarRichMensagem("");
            setApresentarMensagem(Boolean.FALSE);
        }
    }

    public String iniciarInscricao() {
        try {
            InscricaoVO inscricaoVO = new InscricaoVO();
            inscricaoVO.setCandidato(getPessoaVO());
            context().getExternalContext().getSessionMap().put("inscricaoVO", inscricaoVO);
//            InscricaoControle inscricaoControle = null;
//            inscricaoControle = (InscricaoControle) context().getExternalContext().getSessionMap().get(InscricaoControle.class.getSimpleName());
//            if (inscricaoControle == null) {
//                inscricaoControle = new InscricaoControle();
//                context().getExternalContext().getSessionMap().put(InscricaoControle.class.getSimpleName(), inscricaoControle);
//            }
//            inscricaoControle.novo();
//            inscricaoControle.setInscricaoVO(inscricaoVO);
            return Uteis.getCaminhoRedirecionamentoNavegacao("inscricaoForm");
        } catch (Exception e) {
            return "";
        }

    }
    
    
    //
    // /**
    // * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Pessoa</code>.
    // * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
    // * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
    // */
    // public void gravarVisaoCandidato() {
    // try {
    // getFacadeFactory().getPessoaFacade().setIdEntidade("Candidato");
    // pessoaVO.setCandidato(Boolean.TRUE);
    // if (pessoaVO.isNovoObj().booleanValue()) {
    // getFacadeFactory().getPessoaFacade().incluir(pessoaVO);
    // } else {
    // getFacadeFactory().getPessoaFacade().alterar(pessoaVO);
    // }
    // /*Atributos Para controlar tela da visao do candidato*/
    // // setApresentarPanelInscricaoCandidado(true);
    // // setApresentarPanelCadastroCandidado(false);
    // // InscricaoControle inscricaoControle = (InscricaoControle) context().getExternalContext().getSessionMap().get("InscricaoControle");
    // // if (inscricaoControle != null) {
    // // inscricaoControle.inicializarInscricaoControle();
    // // }
    //
    // } catch (Exception e) {
    // setMensagemDetalhada("msg_erro", e.getMessage());
    //
    // }
    // }
    //
    // public void visaoCadastroCandidato() throws Exception {
    // try {
    // String campoConsulta = pessoaVO.getCPF();
    // consultarCandidatoPorCPF(campoConsulta);
    // VisaoCandidatoControle visaoCandidatoControle = (VisaoCandidatoControle) context().getExternalContext().getSessionMap().get("VisaoCandidatoControle");
    // if (visaoCandidatoControle != null) {
    // visaoCandidatoControle.setApresentarInscricao(false);
    // visaoCandidatoControle.setApresentarProcessoSeletivo(false);
    // visaoCandidatoControle.setMenuInscricao(false);
    // visaoCandidatoControle.setApresentarCurso(false);
    // visaoCandidatoControle.setApresentarUnidadeEnsino(false);
    // }
    // } catch (Exception e) {
    // setMensagemDetalhada("msg_erro", e.getMessage());
    //
    // }
    //
    //
    // }
    // public void voltar() throws Exception {
    // VisaoCandidatoControle visaoCandidatoControle = (VisaoCandidatoControle) context().getExternalContext().getSessionMap().get("VisaoCandidatoControle");
    // if (visaoCandidatoControle != null) {
    // visaoCandidatoControle.setApresentarInscricao(false);
    // visaoCandidatoControle.setMenuInscricao(false);
    // visaoCandidatoControle.setApresentarCurso(false);
    // visaoCandidatoControle.setApresentarUnidadeEnsino(false);
    // setApresentarPanelInscricaoCandidado(false);
    // setApresentarPanelCadastroCandidado(true);
    // }
    //
    //
    // }
    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP PessoaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta,
     * disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            getFacadeFactory().getPessoaFacade().setIdEntidade("Candidato");
            List objs = new ArrayList(0);
            // if (getControleConsulta().getCampoConsulta().equals("codigo")) {
            // if (getControleConsulta().getValorConsulta().equals("")) {
            // getControleConsulta().setValorConsulta("0");
            // }
            // int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
            // objs = getFacadeFactory().getPessoaFacade().consultarPorCodigo(new Integer(valorInt), tipoPessoa, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
            // }
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getControleConsulta().getValorConsulta(), getTipoPessoa(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }

            if (getControleConsulta().getCampoConsulta().equals("nomeCidade")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorNomeCidade(getControleConsulta().getValorConsulta(), getTipoPessoa(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("CPF")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getControleConsulta().getValorConsulta(), getTipoPessoa(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("RG")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorRG(getControleConsulta().getValorConsulta(), getTipoPessoa(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("necessidadesEspeciais")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorNecessidadesEspeciais(getControleConsulta().getValorConsulta(), getTipoPessoa(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            // objs = ControleConsulta.obterSubListPaginaApresentar(objs, controleConsulta);
            // definirVisibilidadeLinksNavegacao(controleConsulta.getPaginaAtual(), controleConsulta.getNrTotalPaginas());
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("candidatoCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("candidatoCons.xhtml");
        }
    }

    public void consultarCandidatoPorCPF(String campoConsulta) {
        try {
            PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(campoConsulta, 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            if (!pessoa.getCodigo().equals(0)) {
                PessoaVO obj = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(pessoa.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
                obj.setNovoObj(Boolean.FALSE);
                setPessoaVO(obj);
                inicializarListasSelectItemTodosComboBox();
                setFiliacaoVO(new FiliacaoVO());
                setFormacaoAcademicaVO(new FormacaoAcademicaVO());
                setConsultarPessoa(Boolean.FALSE);
            } else {
                setPessoaVO(new PessoaVO());
                getPessoaVO().setCPF(campoConsulta);
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setPessoaVO(new PessoaVO());

        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>PessoaVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getPessoaFacade().setIdEntidade("Candidato");
            getFacadeFactory().getPessoaFacade().excluir(pessoaVO, getUsuarioLogado());
            setPessoaVO(new PessoaVO());
            setFiliacaoVO(new FiliacaoVO());
            setFormacaoAcademicaVO(new FormacaoAcademicaVO());
            setVerificarCpf(this.validarCadastroPorCpf());
            setConsultarPessoa(Boolean.TRUE);
            setEditarDados(Boolean.FALSE);
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("candidatoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("candidatoForm.xhtml");
        }
    }

    public String editarDadosPessoa() throws Exception {
        setPessoaVO(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(getPessoaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        getPessoaVO().setNovoObj(Boolean.FALSE);
        inicializarListasSelectItemTodosComboBox();
        // setPessoaVO(new PessoaVO());
        setFiliacaoVO(new FiliacaoVO());
        setConsultarPessoa(Boolean.FALSE);
        setImportarDadosPessoa(Boolean.FALSE);
        setFormacaoAcademicaVO(new FormacaoAcademicaVO());
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("candidatoForm.xhtml");

    }

    public String fechar() {
        setMensagemDetalhada("", "");
        return Uteis.getCaminhoRedirecionamentoNavegacao("candidatoCons.xhtml");
    }
    
    public void consultaProspectPessoaEmail() {
        if ((getPessoaVO().getCodigo() == 0) && 
            (!getPessoaVO().getEmail().trim().equals("")) &&
            (!getEncontrouProspectAluno())){
            getProspectVO().setEmailPrincipal(getPessoaVO().getEmail());
            getProspectVO().setTipoProspect(TipoProspectEnum.FISICO);
            try {
                ProspectsVO pro = getFacadeFactory().getProspectsFacade().consultarPorEmailUnico(getProspectVO().getEmailPrincipal(), false, getUsuarioLogado());
                if (pro.getCodigo().intValue() != 0) {
                    this.setEncontrouProspectAluno(Boolean.TRUE);
                    getFacadeFactory().getProspectsFacade().carregarDados(pro, null);
                    setProspectVO(pro);
                    
                    getPessoaVO().setCodProspect(pro.getCodigo());
                    getPessoaVO().setNovoObj(Boolean.FALSE);
                    getPessoaVO().setCPF(pro.getCpf());
                    getPessoaVO().setNome(pro.getNome());
                    getPessoaVO().setSexo(pro.getSexo());
                    getPessoaVO().setEmail(pro.getEmailPrincipal());
                    getPessoaVO().setTelefoneRes(pro.getTelefoneResidencial());
                    getPessoaVO().setCelular(pro.getCelular());
                    getPessoaVO().setDataNasc(pro.getDataNascimento());
                    getPessoaVO().setCEP(pro.getCEP());
                    getPessoaVO().setEndereco(pro.getEndereco());
                    getPessoaVO().setSetor(pro.getSetor());
                    getPessoaVO().setCidade(pro.getCidade());
                } else {
                    PessoaVO p = getFacadeFactory().getPessoaFacade().consultarPorEmailUnico(getPessoaVO().getEmail(), false, Uteis.NIVELMONTARDADOS_TODOS, null);
                    if (p.getCodigo().intValue() != 0) {
                        this.setEncontrouProspectAluno(Boolean.TRUE);
                        setPessoaVO(p);
                    } else {
                        this.setEncontrouProspectAluno(Boolean.FALSE);
                        limparDadosReferenteTipoProspect();
                    }
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    public void consultaProspectPessoaCPF() {
        if (!getPessoaVO().getCPF().equalsIgnoreCase("")) {
            getProspectVO().setCpf(getPessoaVO().getCPF());
            getProspectVO().setTipoProspect(TipoProspectEnum.FISICO);
            try {
                ProspectsVO pro = getFacadeFactory().getProspectsFacade().consultarPorCPFCNPJUnico(getProspectVO(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
                if (pro.getCodigo().intValue() != 0) {
                    this.setEncontrouProspectAluno(Boolean.TRUE);
                    getFacadeFactory().getProspectsFacade().carregarDados(pro, null);
                    setProspectVO(pro);
                    getPessoaVO().setCodProspect(pro.getCodigo());
                    getPessoaVO().setNovoObj(Boolean.FALSE);
                    getPessoaVO().setCPF(pro.getCpf());
                    getPessoaVO().setNome(pro.getNome());
                    getPessoaVO().setNomeBatismo(pro.getNomeBatismo());
                    getPessoaVO().setSexo(pro.getSexo());
                    getPessoaVO().setEmail(pro.getEmailPrincipal());
                    getPessoaVO().setTelefoneRes(pro.getTelefoneResidencial());
                    getPessoaVO().setCelular(pro.getCelular());
                    getPessoaVO().setTelefoneComer(pro.getTelefoneComercial());
                    getPessoaVO().setDataNasc(pro.getDataNascimento());
                    getPessoaVO().setCEP(pro.getCEP());
                    getPessoaVO().setEndereco(pro.getEndereco());
                    getPessoaVO().setSetor(pro.getSetor());
                    getPessoaVO().setCidade(pro.getCidade());
                    getPessoaVO().setRG(pro.getRg());
                    getPessoaVO().setDataEmissaoRG(pro.getDataExpedicao());
                    getPessoaVO().setOrgaoEmissor(pro.getOrgaoEmissor());
                    getPessoaVO().setEstadoEmissaoRG(pro.getEstadoEmissor());
                    getPessoaVO().setEstadoCivil(pro.getEstadoCivil());
                    getPessoaVO().setNaturalidade(pro.getNaturalidade());
                } else {
                    PessoaVO p = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(getPessoaVO().getCPF(), 0, "", false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
                    if (p.getCodigo().intValue() != 0) {
                        this.setEncontrouProspectAluno(Boolean.TRUE);
                        setPessoaVO(p);
                    } else {
                        this.setEncontrouProspectAluno(Boolean.FALSE);
                        limparDadosReferenteTipoProspect();
                    }
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    public void limparDadosReferenteTipoProspect() {
        getPessoaVO().setCodProspect(0);
        getPessoaVO().setNome("");
        getPessoaVO().setNomeBatismo("");
        getPessoaVO().setCEP("");
        getPessoaVO().setEndereco("");
        getPessoaVO().setSetor("");
        getPessoaVO().setCidade(new CidadeVO());
        if(!getConfiguracaoGeralSistema().getOcultarCPFPreInscricao()){
            getPessoaVO().setEmail("");
        }else{
            getPessoaVO().setCPF("");
        }
        getProspectVO().setEmailSecundario("");
        getPessoaVO().setCelular("");
        getPessoaVO().setTelefoneComer("");
        getPessoaVO().setTelefoneRecado("");
        getPessoaVO().setTelefoneRes("");
        getPessoaVO().setSexo("");
        getPessoaVO().setRG("");
        getPessoaVO().setDataEmissaoRG(new Date());
        getPessoaVO().setEstadoEmissaoRG("");
        getPessoaVO().setOrgaoEmissor("");
        getPessoaVO().setDataNasc(new Date());
    }

    public void validarPessoaCadastrada() {
        try {
        	ProspectsVO obj = null;
            String msg = "";
            String cpf;

            if (getPessoaVO().getCPF().length() == 14) {
                cpf = getPessoaVO().getCPF();
                Boolean validacpf = getFacadeFactory().getConfiguracaoGeralSistemaFacade().realizarVerificacaoValidarCpf(false, getUsuarioLogado());
                if (Uteis.isAtributoPreenchido(validacpf) && validacpf) {
                    boolean cpfValido = Uteis.verificaCPF(cpf);
                    if (!cpfValido) {
                        setCpfInvalido(true);
                        setEditarDados(false);
                        setNaoCandidato(false);
                        throw new ConsistirException("O CPF não é VÁLIDO.");
                    }
                }
                pessoaExistente = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(cpf, 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                if (pessoaExistente.getCodigo().intValue() == 0) {
                    obj = new ProspectsVO();
                    obj.setCpf(cpf);
                    obj = getFacadeFactory().getProspectsFacade().consultarPorCPFCNPJUnico(obj, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                    setProspectVO(obj);
                }
            } else {
                setCpfInvalido(true);
                setEditarDados(false);
                setNaoCandidato(false);
                throw new Exception("CPF inválido.");
            }
            if (obj != null) {
                if (obj.getCodigo().intValue() != 0) {
                    mensagem = "Já existe um ";
                    mensagem += "Prospect cadastrado";
                    mensagem += " com este CPF. \nDeseja cadastrá-lo como candidato?";
                    setEditarDados(false);
                    setCpfInvalido(false);
                    setNaoCandidato(true);
                    setImportarDadosPessoa(true);
                    //getFacadeFactory().getProspectsFacade().realizarPreenchimentoPessoaPorProspect(obj, getUsuarioLogado());
                    throw new Exception(mensagem);
                }
            }
            if (pessoaExistente.getCodigo().intValue() != 0) {
                msg = "Já existe um ";
                if (pessoaExistente.getCandidato().equals(Boolean.TRUE)) {
                    msg += "Candidato cadastrado com este CPF. Deseja editá-lo?";
                    setPessoaVO(pessoaExistente);
                    setNaoCandidato(false);
                    setCpfInvalido(false);
                    setEditarDados(true);
                } else {                                    	
                    if (pessoaExistente.getAluno().equals(Boolean.TRUE)) {
                        msg += "Aluno cadastrado";
                    } else if (pessoaExistente.getFuncionario().equals(Boolean.TRUE)) {
                        msg += "Funcionário cadastrado";
                    } else if (pessoaExistente.getMembroComunidade().equals(Boolean.TRUE)) {
                        msg += "Membro da Comunidade cadastrado";
                    } else if (pessoaExistente.getProfessor().equals(Boolean.TRUE)) {
                        msg += "Professor cadastrado";
                    }
                    msg += " com este CPF. \nDeseja cadastrá-lo como candidato?";
                    setEditarDados(false);
                    setCpfInvalido(false);
                    setNaoCandidato(true);
                }
                throw new Exception(msg);
            }
            setPessoaVO(new PessoaVO());
            getPessoaVO().setCPF(cpf);
            setConsultarPessoa(Boolean.FALSE);
            setImportarDadosPessoa(Boolean.FALSE);

        } catch (Exception e) {
            pessoaVO.setCPF("");
            setImportarDadosPessoa(Boolean.TRUE);
            setConsultarPessoa(Boolean.FALSE);
            setMensagemDetalhada(e.getMessage());
        }
    }

    public void naoImportarPessoaCadastrada() {
        setPessoaVO(new PessoaVO());
        setImportarDadosPessoa(Boolean.FALSE);
        setConsultarPessoa(Boolean.TRUE);
        setMensagemDetalhada("");
        // return "editar";
    }

    public void importarPessoaCadastrada() throws Exception {
    	setPessoaVO(new PessoaVO());
    	if (getPessoaExistente().getCodigo().intValue() == 0) {
            setPessoaVO(getFacadeFactory().getProspectsFacade().realizarPreenchimentoPessoaPorProspect(getProspectVO(), getUsuarioLogado()));
        } else {
            setPessoaVO(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(getPessoaExistente().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
        }
        setImportarDadosPessoa(Boolean.FALSE);
        setConsultarPessoa(Boolean.FALSE);
        setMensagemDetalhada("");
    }

    /*
     * Método responsável por adicionar um novo objeto da classe <code>Filiacao</code> para o objeto <code>pessoaVO</code> da classe <code>Pessoa</code>
     */
    public void adicionarFiliacao() throws Exception {
        try {
            if (!getPessoaVO().getCodigo().equals(0)) {
                filiacaoVO.setAluno(getPessoaVO().getCodigo());
            }
            if (getFiliacaoVO().getCep().equals("")) {
                getFiliacaoVO().setCep(getPessoaVO().getCEP());
            }
            getPessoaVO().adicionarObjFiliacaoVOs(getFiliacaoVO());
            this.setFiliacaoVO(new FiliacaoVO());
            setMensagemID("msg_dados_adicionados");

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>Filiacao</code> para edição pelo usuário.
     */
    public void editarFiliacao()  {
    	try {
	        FiliacaoVO obj = (FiliacaoVO) context().getExternalContext().getRequestMap().get("filiacaoItens");
	        setFiliacaoVO(obj.getClone());
	    	setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

    /*
     * Método responsável por remover um novo objeto da classe <code>Filiacao</code> do objeto <code>pessoaVO</code> da classe <code>Pessoa</code>
     */
    public void removerFiliacao() throws Exception {
        FiliacaoVO obj = (FiliacaoVO) context().getExternalContext().getRequestMap().get("filiacaoItens");
        getPessoaVO().excluirObjFiliacaoVOs(obj.getNome());
        setMensagemID("msg_dados_excluidos");

    }

    /*
     * Método responsável por adicionar um novo objeto da classe <code>FormacaoAcademica</code> para o objeto <code>pessoaVO</code> da classe <code>Pessoa</code>
     */
    public void adicionarFormacaoAcademica() throws Exception {
        try {
            if (!getPessoaVO().getCodigo().equals(0)) {
                getFormacaoAcademicaVO().setPessoa(getPessoaVO().getCodigo());
            }
            getPessoaVO().adicionarObjFormacaoAcademicaVOs(getFormacaoAcademicaVO());
            this.setFormacaoAcademicaVO(new FormacaoAcademicaVO());
            setMensagemID("msg_dados_adicionados");

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>FormacaoAcademica</code> para edição pelo usuário.
     */
    public void editarFormacaoAcademica() throws Exception {
        FormacaoAcademicaVO obj = (FormacaoAcademicaVO) context().getExternalContext().getRequestMap().get("formacaoAcademicaItens");
        setFormacaoAcademicaVO(obj);

    }

    /*
     * Método responsável por remover um novo objeto da classe <code>FormacaoAcademica</code> do objeto <code>pessoaVO</code> da classe <code>Pessoa</code>
     */
    public void removerFormacaoAcademica() throws Exception {
        FormacaoAcademicaVO obj = (FormacaoAcademicaVO) context().getExternalContext().getRequestMap().get("formacaoAcademicaItens");
        getPessoaVO().excluirObjFormacaoAcademicaVOs(obj.getCurso());
        setMensagemID("msg_dados_excluidos");
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
     * Método responsável por consultar Cidade <code>Cidade/code>.
     * Buscando todos os objetos correspondentes a entidade
     * <code>Cidade</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
     * requisições Ajax.
     */
    public void consultarCidade() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCidade().equals("codigo")) {
                if (getValorConsultaCidade().equals("")) {
                    setValorConsultaCidade("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaCidade());
                objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
            }
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

    /**
     * Método responsável por selecionar o objeto CidadeVO <code>Cidade/code>.
     */
    public void selecionarCidade() {
        CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItem");
        getPessoaVO().setCidade(obj);
        getListaConsultaCidade().clear();
        this.setValorConsultaCidade("");
        this.setCampoConsultaCidade("");
    }

    /**
     * Método responsável por carregar umaCombobox com os tipos de pesquisa de Cidade <code>Cidade/code>.
     */
    public List<SelectItem> getTipoConsultaCidade() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    /**
     * Método responsável por consultar Naturalidade <code>Cidade/code>.
     * Buscando todos os objetos correspondentes a entidade
     * <code>Cidade</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
     * requisições Ajax.
     */
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
                if (getValorConsultaNaturalidade().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
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
     * Método responsável por selecionar o objeto CidadeVO em Naturalidade <code>Cidade/code>.
     */
    public void selecionarNaturalidade() {
        CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("naturalidadeItens");
        getPessoaVO().setNaturalidade(obj);
        getListaConsultaNaturalidade().clear();
        this.setValorConsultaNaturalidade("");
        this.setCampoConsultaNaturalidade("");
    }

    /**
     * Método responsável por carregar umaCombobox com os tipos de pesquisa de Cidade para Naturalidade <code>Cidade/code>.
     */
    public List<SelectItem> getTipoConsultaNaturalidade() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo <code>diaSemana</code>
     */
    public List getListaSelectItemDiaSemanaDisponibilidadeHorario() throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        objs.add(new SelectItem("", ""));
        Hashtable diaSemanaDisponibilidadeHorarios = (Hashtable) Dominios.getDiaSemanaDisponibilidadeHorario();
        Enumeration keys = diaSemanaDisponibilidadeHorarios.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) diaSemanaDisponibilidadeHorarios.get(value);
            objs.add(new SelectItem(value, label));
        }
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo <code>tipo</code>
     */
    public List<SelectItem> getListaSelectItemTipoFiliacao() throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoFiliacaos = (Hashtable) Dominios.getTipoFiliacao();
        Enumeration keys = tipoFiliacaos.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoFiliacaos.get(value);
            objs.add(new SelectItem(value, label));
        }
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo <code>situacao</code>
     */
    public List<SelectItem> getListaSelectItemSituacaoFormacaoAcademica() throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        objs.add(new SelectItem("", ""));
        Hashtable situacaoFormacaoAcademicas = (Hashtable) Dominios.getSituacaoFormacaoAcademica();
        Enumeration keys = situacaoFormacaoAcademicas.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) situacaoFormacaoAcademicas.get(value);
            objs.add(new SelectItem(value, label));
        }
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo <code>escolaridade</code>
     */
    public List<SelectItem> getListaSelectItemEscolaridadeFormacaoAcademica() throws Exception {
        // List objs = new ArrayList(0);
        // objs.add(new SelectItem("", ""));
        // Hashtable escolaridadeFormacaoAcademicas = (Hashtable) Dominios.getEscolaridadeFormacaoAcademica();
        // Enumeration keys = escolaridadeFormacaoAcademicas.keys();
        // while (keys.hasMoreElements()) {
        // String value = (String) keys.nextElement();
        // String label = (String) escolaridadeFormacaoAcademicas.get(value);
        // objs.add(new SelectItem(value, label));
        // }
        List<SelectItem> objs = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(NivelFormacaoAcademica.class, true);
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo <code>tipoPessoa</code>
     */
    public List<SelectItem> getListaSelectItemTipoPessoaPessoa() throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoPessoaBasicoPessoas = (Hashtable) Dominios.getTipoPessoaBasicoPessoa();
        Enumeration keys = tipoPessoaBasicoPessoas.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoPessoaBasicoPessoas.get(value);
            objs.add(new SelectItem(value, label));
        }
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo <code>estadoEmissaoRG</code>
     */
    public List<SelectItem> getListaSelectItemEstadoEmissaoRGPessoa() throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        objs.add(new SelectItem("", ""));
        Hashtable estados = (Hashtable) Dominios.getEstado();
        Enumeration keys = estados.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) estados.get(value);
            objs.add(new SelectItem(value, label));
        }
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo <code>estadoCivil</code>
     */
    public List<SelectItem> getListaSelectItemEstadoCivilPessoa() throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        objs.add(new SelectItem("", ""));
        Hashtable estadoCivils = (Hashtable) Dominios.getEstadoCivil();
        Enumeration keys = estadoCivils.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) estadoCivils.get(value);
            objs.add(new SelectItem(value, label));
        }
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo <code>sexo</code>
     */
    public List<SelectItem> getListaSelectItemSexoPessoa() throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        objs.add(new SelectItem("", ""));
        Hashtable sexos = (Hashtable) Dominios.getSexo();
        Enumeration keys = sexos.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) sexos.get(value);
            objs.add(new SelectItem(value, label));
        }
        return objs;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>Nacionalidade</code>.
     */
    public void montarListaSelectItemNacionalidade(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarPaizPorNome(prm);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                PaizVO obj = (PaizVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNacionalidade()));
            }
            setListaSelectItemNacionalidade(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Nacionalidade</code>. Buscando todos os objetos correspondentes a entidade <code>Paiz</code>. Esta
     * rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemNacionalidade() {
        try {
            montarListaSelectItemNacionalidade("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo
     * <code>nome</code> Este atributo é uma lista ( <code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarPaizPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getPaizFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
        return lista;
    }

    public void carregarEnderecoPessoa() {
        try {
            getFacadeFactory().getEnderecoFacade().carregarEndereco(getPessoaVO(), getUsuarioLogado());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Método responsável por inicializar a lista de valores ( <code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemNacionalidade();
        montarListaSelectItemAreaConhecimento();
        montarListaSelectItemTipoMidia();
        
    }

    public List getListaSelectItemUnidadeEnsino() throws Exception {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList();
        }
        return listaSelectItemUnidadeEnsino;
        // SelectItemOrdemValor ordenador = null;
        // List resultadoConsulta = null;
        // Iterator i = null;
        // try {
        // resultadoConsulta = consultarUnidadeEnsinoPorNome("");
        // i = resultadoConsulta.iterator();
        // List objs = new ArrayList(0);
        // objs.add(new SelectItem(0, ""));
        // while (i.hasNext()) {
        // UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
        // objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
        // }
        //
        // ordenador = new SelectItemOrdemValor();
        // Collections.sort((List) objs, ordenador);
        // return objs;
        // } catch (Exception e) {
        // return new ArrayList();
        // //throw e;
        // } finally {
        // ordenador = null;
        // Uteis.liberarListaMemoria(resultadoConsulta);
        // i = null;
        // }
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectItemCurso() {
        try {
            montarListaSelectItemCurso("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
//            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                if(getMatriculaVO().getUnidadeEnsino().getCodigo() == 0){
                    getMatriculaVO().getUnidadeEnsino().setCodigo(obj.getCodigo());
                }
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

    public void montarListaSelectItemCurso(String prm) throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = null;
        Iterator i = null;
        Map<Integer, String> cursos = new HashMap<Integer, String>(0);
        try {
            resultadoConsulta = consultarCursoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
//            objs.add(new SelectItem(0, "Selecione o Curso"));
            
            while (i.hasNext()) {
                UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) i.next();
                if(!cursos.containsKey(obj.getCurso().getCodigo())){
                    objs.add(new SelectItem(obj.getCurso().getCodigo(), obj.getCurso().getNome()));
                    cursos.put(obj.getCurso().getCodigo(), obj.getCurso().getNome());
                }
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemCurso(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            cursos.clear();
            cursos = null;
            ordenador = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List getTipoConsultaComboCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public void consultarCurso() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCurso().equals("codigo")) {
                if (getValorConsultaCurso().equals("")) {
                    setValorConsultaCurso("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaCurso());
                objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCursoUnidadeEnsino(valorInt, getMatriculaVO().getUnidadeEnsino().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaCurso().equals("nome")) {
                objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getMatriculaVO().getUnidadeEnsino().getCodigo(), false, "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaSelectItemCurso(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaSelectItemCurso(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void realizarLiberarSelecionarCurso() throws Exception {
        if (getMatriculaVO().getUnidadeEnsino().getCodigo().intValue() != 0) {
            setLiberarSelecionarCurso(Boolean.TRUE);
            montarListaSelectItemCurso();
            
        } else {
            setLiberarSelecionarCurso(Boolean.TRUE);
            getListaSelectItemCurso().clear();
//            getListaSelectItemCurso().add(new SelectItem(0, "Selecione a Unidade de Ensino"));
        }
    }

    public void selecionarCurso() throws Exception {
        try {
            UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocurso");
            CursoVO cursoVO = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(unidadeEnsinoCurso.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
            getMatriculaVO().setCurso(cursoVO);
            getMatriculaVO().setTurno(unidadeEnsinoCurso.getTurno());
        } catch (Exception e) {
            getMatriculaVO().setCurso(new CursoVO());
            getMatriculaVO().setTurno(new TurnoVO());
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparListaConsultaCurso() {
        setListaSelectItemCurso(new ArrayList(0));
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo
     * <code>nome</code> Este atributo é uma lista ( <code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNomePorApresentarHomePreInscricao(nomePrm, 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    public List<UnidadeEnsinoCursoVO> consultarCursoPorNome(String nomePrm) throws Exception {
//        List lista = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getMatriculaVO().getUnidadeEnsino().getCodigo(), true, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        List<UnidadeEnsinoCursoVO> lista = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorUnidadeEnsinoENivelEducacional(getMatriculaVO().getUnidadeEnsino().getCodigo(), null, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
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

    public void montarListaSelectItemAreaConhecimento() {
        try {
            montarListaSelectItemAreaConhecimento("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo
     * <code>nome</code> Este atributo é uma lista ( <code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarAreaConhecimentoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getAreaConhecimentoFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
        return lista;
    }

    public void carregarFiliacaoPessoa() {
        try {
            setFiliacaoVO(getFacadeFactory().getFiliacaoFacade().carregarApenasUmPorCPF(getFiliacaoVO(), getPessoaVO(), true, getUsuarioLogado()));
            if (getFiliacaoVO().getPais().getCodigo().intValue() == 0 || getFiliacaoVO().getPais().getCEP().isEmpty()) {
                getFiliacaoVO().getPais().setCEP(getPessoaVO().getCEP());
                getFiliacaoVO().getPais().setEndereco(getPessoaVO().getEndereco());
                getFiliacaoVO().getPais().setSetor(getPessoaVO().getSetor());
                getFiliacaoVO().getPais().setNumero(getPessoaVO().getNumero());
                getFiliacaoVO().getPais().setComplemento(getPessoaVO().getComplemento());
                getFiliacaoVO().getPais().setCidade(getPessoaVO().getCidade());
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void carregarEnderecoPessoaFiliacao() {
        try {
            getFacadeFactory().getEnderecoFacade().carregarEndereco(getFiliacaoVO().getPais(), getUsuarioLogado());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarFiliacaoCidade() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaFiliacaoCidade().equals("codigo")) {
                if (getValorConsultaFiliacaoCidade().equals("")) {
                    setValorConsultaFiliacaoCidade("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaFiliacaoCidade());
                objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
            }
            if (getCampoConsultaFiliacaoCidade().equals("nome")) {
                if (getValorConsultaFiliacaoCidade().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaFiliacaoCidade(), false, getUsuarioLogado());
            }
            if (getCampoConsultaFiliacaoCidade().equals("estado")) {
                objs = getFacadeFactory().getCidadeFacade().consultarPorSiglaEstado(getValorConsultaFiliacaoCidade(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }

            setListaConsultaFiliacaoCidade(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaCidade(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void selecionarFiliacaoCidade() {
        try {
            CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("filiacaoCidadeItens");
            getFiliacaoVO().getPais().setCidade(obj);
            listaConsultaFiliacaoCidade.clear();
            this.setValorConsultaFiliacaoCidade("");
            this.setCampoConsultaFiliacaoCidade("");
            setMensagemID("msg_dados_selecionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }

    }

    public void verificarExistenciaResponsavelFinanceiroExistente() throws Exception {
        try {
            if (getFiliacaoVO().getResponsavelFinanceiro()) {
                PessoaVO.validarDadosFiliacaoResponsavelFinanceiro(getPessoaVO(), getFiliacaoVO());
            }
            setMensagemDetalhada("");
            setMensagemID("msg_entre_dados");
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        // itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("nomeCidade", "Cidade"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("RG", "RG"));
        itens.add(new SelectItem("necessidadesEspeciais", "Necessidades Especiais"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        // setPaginaAtualDeTodas("0/0");
        // setListaConsulta(new ArrayList(0));
        // definirVisibilidadeLinksNavegacao(0, 0);
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("candidatoCons.xhtml");
    }
    
    public List<SelectItem> getListaSelectItemCorRaca() throws Exception {
    	List<SelectItem> lista = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(CorRaca.class);
		return lista;
	}
    
    //SelectItem item = getListaSelectItemCorRaca().get(0);

    public String getTurno_Erro() {
        return turno_Erro;
    }

    public void setTurno_Erro(String turno_Erro) {
        this.turno_Erro = turno_Erro;
    }

    public FiliacaoVO getFiliacaoVO() {
        if (filiacaoVO == null) {
            filiacaoVO = new FiliacaoVO();
        }
        return filiacaoVO;
    }

    public void setFiliacaoVO(FiliacaoVO filiacaoVO) {
        this.filiacaoVO = filiacaoVO;
    }

    public String getDisciplina_Erro() {
        return disciplina_Erro;
    }

    public void setDisciplina_Erro(String disciplina_Erro) {
        this.disciplina_Erro = disciplina_Erro;
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

    public List getListaSelectItemNacionalidade() {
        if (listaSelectItemNacionalidade == null) {
            listaSelectItemNacionalidade = new ArrayList(0);
        }
        return (listaSelectItemNacionalidade);
    }

    public void setListaSelectItemNacionalidade(List listaSelectItemNacionalidade) {
        this.listaSelectItemNacionalidade = listaSelectItemNacionalidade;
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

    public PessoaVO getPessoaVO() {
        if (pessoaVO == null) {
            pessoaVO = new PessoaVO();
        }
        return pessoaVO;
    }

    public void setPessoaVO(PessoaVO pessoaVO) {
        this.pessoaVO = pessoaVO;
    }

    public String formacaoAcademica() {
        return "formacaoAcademica";
    }

    public String dadosPessoais() {
        return "dadosPessoais";
    }

    public String documentosDadosFuncionais() {
        return "documentosDadosFuncionais";
    }

    public String filiacao() {
        return "filiacao";
    }

    public Boolean getConsultarPessoa() {
        if (consultarPessoa == null) {
            consultarPessoa = Boolean.TRUE;
        }
        return consultarPessoa;
    }

    public void setConsultarPessoa(Boolean consultarPessoa) {
        this.consultarPessoa = consultarPessoa;
    }

    public Boolean getEditarDados() {
        if (editarDados == null) {
            editarDados = Boolean.FALSE;
        }
        return editarDados;
    }

    public void setEditarDados(Boolean editarDados) {
        this.editarDados = editarDados;
    }

    public Boolean getVerificarCpf() {
        return verificarCpf;
    }

    public void setVerificarCpf(Boolean verificarCpf) {
        this.verificarCpf = verificarCpf;
    }

    public Boolean getConsultarCPF() {
        if (consultarCPF == null) {
            consultarCPF = true;
        }
        return consultarCPF;
    }

    public void setConsultarCPF(Boolean consultarCPF) {
        this.consultarCPF = consultarCPF;
    }

    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        pessoaVO = null;
        Uteis.liberarListaMemoria(listaConsultaCidade);
        Uteis.liberarListaMemoria(listaConsultaNaturalidade);
        Uteis.liberarListaMemoria(listaSelectItemNacionalidade);
        Uteis.liberarListaMemoria(listaSelectItemAreaConhecimento);
        campoConsultaCidade = null;
        valorConsultaCidade = null;
        campoConsultaNaturalidade = null;
        valorConsultaNaturalidade = null;
        setFormacaoAcademicaVO(null);
        disciplina_Erro = null;
        filiacaoVO = null;
        turno_Erro = null;

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
     * @param campoConsultaCidade the campoConsultaCidade to set
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
     * @param valorConsultaCidade the valorConsultaCidade to set
     */
    public void setValorConsultaCidade(String valorConsultaCidade) {
        this.valorConsultaCidade = valorConsultaCidade;
    }

    /**
     * @return the listaConsultaCidade
     */
    public List getListaConsultaCidade() {
        if (listaConsultaCidade == null) {
            listaConsultaCidade = new ArrayList();
        }
        return listaConsultaCidade;
    }

    /**
     * @param listaConsultaCidade the listaConsultaCidade to set
     */
    public void setListaConsultaCidade(List listaConsultaCidade) {
        this.listaConsultaCidade = listaConsultaCidade;
    }

    /**
     * @return the campoConsultaNaturalidade
     */
    public String getCampoConsultaNaturalidade() {
        if (campoConsultaNaturalidade == null) {
            campoConsultaNaturalidade = "";
        }
        return campoConsultaNaturalidade;
    }

    /**
     * @param campoConsultaNaturalidade the campoConsultaNaturalidade to set
     */
    public void setCampoConsultaNaturalidade(String campoConsultaNaturalidade) {
        this.campoConsultaNaturalidade = campoConsultaNaturalidade;
    }

    /**
     * @return the valorConsultaNaturalidade
     */
    public String getValorConsultaNaturalidade() {
        if (valorConsultaNaturalidade == null) {
            valorConsultaNaturalidade = "";
        }
        return valorConsultaNaturalidade;
    }

    /**
     * @param valorConsultaNaturalidade the valorConsultaNaturalidade to set
     */
    public void setValorConsultaNaturalidade(String valorConsultaNaturalidade) {
        this.valorConsultaNaturalidade = valorConsultaNaturalidade;
    }

    /**
     * @return the listaConsultaNaturalidade
     */
    public List getListaConsultaNaturalidade() {
        if (listaConsultaNaturalidade == null) {
            listaConsultaNaturalidade = new ArrayList(0);
        }
        return listaConsultaNaturalidade;
    }

    /**
     * @param listaConsultaNaturalidade the listaConsultaNaturalidade to set
     */
    public void setListaConsultaNaturalidade(List listaConsultaNaturalidade) {
        this.listaConsultaNaturalidade = listaConsultaNaturalidade;
    }

    public void setInscricaoPermitida(Boolean inscricaoPermitida) {
        this.inscricaoPermitida = inscricaoPermitida;
    }

    public Boolean getIsInscricaoPermitida() {
        if (inscricaoPermitida == null) {
            inscricaoPermitida = false;
        }
        return inscricaoPermitida;
    }

    /**
     * @return the tipoPessoa
     */
    public String getTipoPessoa() {
        if (tipoPessoa == null) {
            return "CA";
        }
        return tipoPessoa;
    }

    /**
     * @param tipoPessoa the tipoPessoa to set
     */
    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public boolean isCpfInvalido() {
        return cpfInvalido;
    }

    public void setCpfInvalido(boolean cpfInvalido) {
        this.cpfInvalido = cpfInvalido;
    }

    public PessoaVO getPessoaExistente() {
        if (pessoaExistente == null) {
            pessoaExistente = new PessoaVO();
        }
        return pessoaExistente;
    }

    public void setPessoaExistente(PessoaVO pessoaExistente) {
        this.pessoaExistente = pessoaExistente;
    }

    public Boolean getImportarDadosPessoa() {
        return importarDadosPessoa;
    }

    public void setImportarDadosPessoa(Boolean importarDadosPessoa) {
        this.importarDadosPessoa = importarDadosPessoa;
    }

    public boolean isNaoCandidato() {
        return naoCandidato;
    }

    public void setNaoCandidato(boolean naoCandidato) {
        this.naoCandidato = naoCandidato;
    }

    public boolean getIsInformarCpfNovamente() {
        if ((getEditarDados() && getEditarDados() != null) || (isNaoCandidato())) {
            return true;
        }
        return false;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    /**
     * @return the matriculaVO
     */
    public MatriculaVO getMatriculaVO() {
        if (matriculaVO == null) {
            matriculaVO = new MatriculaVO();
        }
        return matriculaVO;
    }

    /**
     * @param matriculaVO the matriculaVO to set
     */
    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
    }

    /**
     * @return the matriculaPeriodoVO
     */
    public MatriculaPeriodoVO getMatriculaPeriodoVO() {
        if (matriculaPeriodoVO == null) {
            matriculaPeriodoVO = new MatriculaPeriodoVO();
        }
        return matriculaPeriodoVO;
    }

    /**
     * @param matriculaPeriodoVO the matriculaPeriodoVO to set
     */
    public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
        this.matriculaPeriodoVO = matriculaPeriodoVO;
    }

    /**
     * @return the campoConsultaCurso
     */
    public String getCampoConsultaCurso() {
        if (campoConsultaCurso == null) {
            campoConsultaCurso = "";
        }
        return campoConsultaCurso;
    }

    /**
     * @param campoConsultaCurso the campoConsultaCurso to set
     */
    public void setCampoConsultaCurso(String campoConsultaCurso) {
        this.campoConsultaCurso = campoConsultaCurso;
    }

    /**
     * @return the valorConsultaCurso
     */
    public String getValorConsultaCurso() {
        if (valorConsultaCurso == null) {
            valorConsultaCurso = "";
        }
        return valorConsultaCurso;
    }

    /**
     * @param valorConsultaCurso the valorConsultaCurso to set
     */
    public void setValorConsultaCurso(String valorConsultaCurso) {
        this.valorConsultaCurso = valorConsultaCurso;
    }

    /**
     * @return the listaSelectItemCurso
     */
    public List getListaSelectItemCurso() {
        if (listaSelectItemCurso == null) {
            listaSelectItemCurso = new ArrayList();
        }
        return listaSelectItemCurso;
    }

    /**
     * @param listaSelectItemCurso the listaSelectItemCurso to set
     */
    public void setListaSelectItemCurso(List listaSelectItemCurso) {
        this.listaSelectItemCurso = listaSelectItemCurso;
    }

    /**
     * @return the liberarSelecionarCurso
     */
    public boolean getLiberarSelecionarCurso() {
        return liberarSelecionarCurso;
    }

    /**
     * @param liberarSelecionarCurso the liberarSelecionarCurso to set
     */
    public void setLiberarSelecionarCurso(boolean liberarSelecionarCurso) {
        this.liberarSelecionarCurso = liberarSelecionarCurso;
    }

    /**
     * @return the prospectVO
     */
    public ProspectsVO getProspectVO() {
        if (prospectVO == null) {
            prospectVO = new ProspectsVO();
        }
        return prospectVO;
    }

    /**
     * @param prospectVO the prospectVO to set
     */
    public void setProspectVO(ProspectsVO prospectVO) {
        this.prospectVO = prospectVO;
    }

    /**
     * @return the apresentarMensagem
     */
    public boolean getApresentarMensagem() {
        return apresentarMensagem;
    }

    /**
     * @param apresentarMensagem the apresentarMensagem to set
     */
    public void setApresentarMensagem(boolean apresentarMensagem) {
        this.apresentarMensagem = apresentarMensagem;
    }

    /**
     * @return the apresentarRichMensagem
     */
    public String getApresentarRichMensagem() {
        if (apresentarRichMensagem == null) {
            apresentarRichMensagem = "";
        }
        return apresentarRichMensagem;
    }

    /**
     * @param apresentarRichMensagem the apresentarRichMensagem to set
     */
    public void setApresentarRichMensagem(String apresentarRichMensagem) {
        this.apresentarRichMensagem = apresentarRichMensagem;
    }

    public String getCampoConsultaFiliacaoCidade() {
        if (campoConsultaFiliacaoCidade == null) {
            campoConsultaFiliacaoCidade = "";
        }
        return campoConsultaFiliacaoCidade;
    }

    public void setCampoConsultaFiliacaoCidade(String campoConsultaFiliacaoCidade) {
        this.campoConsultaFiliacaoCidade = campoConsultaFiliacaoCidade;
    }

    public String getValorConsultaFiliacaoCidade() {
        if (valorConsultaFiliacaoCidade == null) {
            valorConsultaFiliacaoCidade = "";
        }
        return valorConsultaFiliacaoCidade;
    }

    public void setValorConsultaFiliacaoCidade(String valorConsultaFiliacaoCidade) {
        this.valorConsultaFiliacaoCidade = valorConsultaFiliacaoCidade;
    }

    public List getListaConsultaFiliacaoCidade() {
        if (listaConsultaFiliacaoCidade == null) {
            listaConsultaFiliacaoCidade = new ArrayList(0);
        }
        return listaConsultaFiliacaoCidade;
    }

    public void setListaConsultaFiliacaoCidade(List listaConsultaFiliacaoCidade) {
        this.listaConsultaFiliacaoCidade = listaConsultaFiliacaoCidade;
    }

    public boolean getApresentarCamposEditarFiliacao() {
        return (getFiliacaoVO() != null && getFiliacaoVO().getCodigo() != 0);
    }

    public String getApresentarCss() {
        if (getFiliacaoVO() != null && getFiliacaoVO().getCodigo() != 0) {
            return "camposSomenteLeitura";
        } else {
            return "camposObrigatorios";
        }

    }

    
    public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistema() {
        if(configuracaoGeralSistema == null){
            try {
                configuracaoGeralSistema = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getUnidadeEnsinoLogado().getCodigo());
            } catch (Exception e) {
                setConfiguracaoGeralSistema(new ConfiguracaoGeralSistemaVO());
            }
        }
        return configuracaoGeralSistema;
    }

    
    public void setConfiguracaoGeralSistema(ConfiguracaoGeralSistemaVO configuracaoGeralSistema) {
        this.configuracaoGeralSistema = configuracaoGeralSistema;
    }

    /**
     * @return the encontrouProspectAluno
     */
    public Boolean getEncontrouProspectAluno() {
        if (encontrouProspectAluno == null) {
            encontrouProspectAluno = Boolean.FALSE;
        }
        return encontrouProspectAluno;
    }

    /**
     * @param encontrouProspectAluno the encontrouProspectAluno to set
     */
    public void setEncontrouProspectAluno(Boolean encontrouProspectAluno) {
        this.encontrouProspectAluno = encontrouProspectAluno;
    }
    
    public String getStyleClassCamposObrigatoriosEncontrouProspect() {
        if (this.getEncontrouProspectAluno()) {
            return "camposSomenteLeitura";
        }
        return "camposObrigatorios";
    }
    
    public String getStyleClassCamposEncontrouProspect() {
        if (this.getEncontrouProspectAluno()) {
            return "camposSomenteLeitura";
        }
        return "campos";        
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
    
    public List getListaSelectItemTipoMidia() {
        if (listaSelectItemTipoMidia == null) {
            listaSelectItemTipoMidia = new ArrayList(0);
        }
        return listaSelectItemTipoMidia;
    }

    public void setListaSelectItemTipoMidia(List listaSelectItemTipoMidia) {
        this.listaSelectItemTipoMidia = listaSelectItemTipoMidia;
    }
    
	public void setarNomeBatismo() {
		if(!Uteis.isAtributoPreenchido(getPessoaVO().getNomeBatismo())) {
			getPessoaVO().setNomeBatismo(getPessoaVO().getNome());
		}
	}
	
	public void setarNomeSocial() {
		if(!Uteis.isAtributoPreenchido(getPessoaVO().getNome())) {
			getPessoaVO().setNome(getPessoaVO().getNomeBatismo());
		}
	}
	
	public void selecionarCidadeFormacao() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItem");
		getFormacaoAcademicaVO().setCidade(obj);
		listaConsultaCidade.clear();
		this.setValorConsultaCidade("");
		this.setCampoConsultaCidade("");
	}
	
	public void limparCidadeFormacao() {
		getFormacaoAcademicaVO().setCidade(new CidadeVO());
	}


	public List listaSelectItemTipoInstFormacaoAcademica;
	public List getListaSelectItemTipoInstFormacaoAcademica() throws Exception {
		if(listaSelectItemTipoInstFormacaoAcademica == null) {
			listaSelectItemTipoInstFormacaoAcademica = new ArrayList<SelectItem>(0);
			listaSelectItemTipoInstFormacaoAcademica.add(new SelectItem("", ""));
			Hashtable tipoInstFormacaoAcademicas = (Hashtable) Dominios.getTipoInstFormacaoAcademica();
			Enumeration keys = tipoInstFormacaoAcademicas.keys();
			while (keys.hasMoreElements()) {
				String value = (String) keys.nextElement();
				String label = (String) tipoInstFormacaoAcademicas.get(value);
				listaSelectItemTipoInstFormacaoAcademica.add(new SelectItem(value, label));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) listaSelectItemTipoInstFormacaoAcademica, ordenador);
		}
		return listaSelectItemTipoInstFormacaoAcademica;
	}
}
