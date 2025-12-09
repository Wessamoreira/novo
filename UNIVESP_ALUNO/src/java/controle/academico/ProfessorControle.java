package controle.academico;

import java.io.OutputStream;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas pessoaForm.jsp pessoaCons.jsp)
 * com as funcionalidades da classe <code>Pessoa</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Pessoa
 * @see PessoaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.primefaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import jakarta.faces. model.SelectItem;
import negocio.comuns.academico.DisciplinaVO;
//import negocio.comuns.academico.DisciplinasInteresseVO;
//import negocio.comuns.academico.DisponibilidadeHorarioVO;
import negocio.comuns.academico.FiliacaoVO;
//import negocio.comuns.academico.HorarioProfessorVO;
//import negocio.comuns.academico.QuadroHorarioVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;

@Controller("ProfessorControle")
@Scope("request")
@Lazy
public class ProfessorControle extends SuperControle implements Serializable {

    private FuncionarioVO funcionarioVO;
    private PessoaVO pessoaVO;
//    private QuadroHorarioVO quadroHorario;
    protected List listaSelectItemCidade;
    protected List listaSelectItemNaturalidade;
    protected List listaSelectItemNacionalidade;
    protected List listaSelectItemUnidadeEnsino;
    protected List listaSelectItemDepartamento;
    protected List listaSelectItemCargo;
    protected List listaSelectItemTurno;
    protected List listaSelectItemAreaConhecimento;
    protected List listaSelectItemPerfilEconomico;
    protected List listaConsultaDisciplina;
    private String campoConsultaDisciplina;
    private String valorConsultaDisciplina;
    private Integer codigoTurno;
    public Boolean verificarCpf;
    public Boolean consultarPessoa;
    public Boolean editarDados;
    // Referente a pessoa
    private FormacaoAcademicaVO formacaoAcademicaVO;
    private String disciplina_Erro;
    private FiliacaoVO filiacaoVO;
    private String turno_Erro;
    public String tipoPessoa = "PR";
//    private DisciplinasInteresseVO disciplinasInteresseVO;
//    private DisponibilidadeHorarioVO disponibilidadeHorarioVO;
    private String diaSemana = "02";
    private Boolean horarioSimples;
    private UnidadeEnsinoVO unidadeEnsinoVO;

    public ProfessorControle() throws Exception {
       
        setControleConsulta(new ControleConsulta());
        setCodigoTurno(0);
        setMensagemID("msg_entre_prmconsulta");
    }

    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
        if (unidadeEnsinoVO == null) {
            unidadeEnsinoVO = new UnidadeEnsinoVO();
            unidadeEnsinoVO.setCodigo(getUnidadeEnsinoLogado().getCodigo());
            unidadeEnsinoVO.setNome(getUnidadeEnsinoLogado().getNome());
        }
        return unidadeEnsinoVO;
    }

    public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
        this.unidadeEnsinoVO = unidadeEnsinoVO;
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Pessoa</code> para edição pelo usuário da
     * aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setFuncionarioVO(new FuncionarioVO());
        setPessoaVO(new PessoaVO());
//        setDisponibilidadeHorarioVO(new DisponibilidadeHorarioVO());
        setFiliacaoVO(new FiliacaoVO());
//        setDisciplinasInteresseVO(new DisciplinasInteresseVO());
        setFormacaoAcademicaVO(new FormacaoAcademicaVO());
        inicializarListasSelectItemTodosComboBox();
//        setQuadroHorario(new QuadroHorarioVO());
        setCodigoTurno(0);
        setConsultarPessoa(Boolean.TRUE);
        setEditarDados(Boolean.FALSE);
        setHorarioSimples(Boolean.TRUE);
        setMensagemID("msg_entre_dados");
        return "editar";
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Pessoa</code> para alteração. O
     * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
     * disponibilizá-lo para edição.
     */
    public String editar() {
        try {
            FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionario");
            obj = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(obj.getPessoa().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            setFuncionarioVO(new FuncionarioVO());
            setPessoaVO(new PessoaVO());
            obj.setNovoObj(Boolean.FALSE);
            setFuncionarioVO(obj);
            inicializarListasSelectItemTodosComboBox();
            setPessoaVO(obj.getPessoa());
//            setDisponibilidadeHorarioVO(new DisponibilidadeHorarioVO());
            setFiliacaoVO(new FiliacaoVO());
//            setDisciplinasInteresseVO(new DisciplinasInteresseVO());
            setFormacaoAcademicaVO(new FormacaoAcademicaVO());
            obj.setNovoObj(Boolean.FALSE);
            // setFuncionarioVO(obj);
//            setQuadroHorario(new QuadroHorarioVO());
            setCodigoTurno(0);
            setConsultarPessoa(Boolean.FALSE);
            setHorarioSimples(Boolean.TRUE);
            setMensagemID("msg_dados_editar");
            return "editar";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }

    public void editarDadosVisaoProfessor() {
//        try {
////            VisaoProfessorControle visaoProfessorControle = (VisaoProfessorControle) context().getExternalContext().getSessionMap().get("VisaoProfessorControle");
//            if (visaoProfessorControle != null) {
//                visaoProfessorControle.inicializarMenuMeusHorarios();
//            } else {
//                return;
//            }
////			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(getUsuarioLogado().getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
//            PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(getUsuarioLogado().getPessoa().getCodigo(), false, true, false, getUsuarioLogado());
//            setPessoaVO(new PessoaVO());
//            setPessoaVO(pessoa);
//            setDisponibilidadeHorarioVO(new DisponibilidadeHorarioVO());
//            setQuadroHorario(new QuadroHorarioVO());
//            setCodigoTurno(0);
//            setListaSelectItemTurno(consultarTurnoPorNome(""));
//            setMensagemID("msg_dados_editar");
//        } catch (Exception e) {
//            setMensagemDetalhada("msg_erro", e.getMessage());
//        }
    }

    public String editarDadosPessoa() throws Exception {

        FuncionarioVO obj = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(getPessoaVO().getCodigo(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        setFuncionarioVO(new FuncionarioVO());
        setPessoaVO(new PessoaVO());
        funcionarioVO.setNovoObj(Boolean.FALSE);
        setFuncionarioVO(funcionarioVO);
        inicializarListasSelectItemTodosComboBox();
        setPessoaVO(obj.getPessoa());
//        setDisponibilidadeHorarioVO(new DisponibilidadeHorarioVO());
        setFiliacaoVO(new FiliacaoVO());
//        setDisciplinasInteresseVO(new DisciplinasInteresseVO());
        setFormacaoAcademicaVO(new FormacaoAcademicaVO());
        obj.setNovoObj(Boolean.FALSE);
        setFuncionarioVO(obj);
//        setQuadroHorario(new QuadroHorarioVO());
        setCodigoTurno(0);
        setConsultarPessoa(Boolean.FALSE);
        setMensagemID("msg_dados_editar");
        return "editar";

    }

    public String fechar() {
        setMensagemDetalhada("", "");
        return "consultar";
    }

    public void validarPessoaCadastrada() {
        try {
            PessoaVO pessoaExistente = new PessoaVO();
            String mensagem = "";

            if (getPessoaVO().getCPF().length() == 14) {
                String cpf = getPessoaVO().getCPF();
                // getPessoaVO().verificaCPF(cpf);
                pessoaExistente = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(cpf, 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            } else {
                throw new Exception("CPF inválido.");
            }

            if (pessoaExistente.getCodigo().intValue() != 0) {
                mensagem = "Já existe um ";

                if (pessoaExistente.getProfessor().equals(Boolean.TRUE)) {
                    mensagem += "Professor cadastrado";
                    setPessoaVO(pessoaExistente);
                    setEditarDados(Boolean.TRUE);
                } else if (pessoaExistente.getFuncionario().equals(Boolean.TRUE)) {
                    mensagem += "Funcionário cadastrado";
                } else if (pessoaExistente.getAluno().equals(Boolean.TRUE)) {
                    mensagem += "Aluno cadastrado";
                } else if (pessoaExistente.getCandidato().equals(Boolean.TRUE)) {
                    mensagem += "Candidato cadastrado";
                } else if (pessoaExistente.getMembroComunidade().equals(Boolean.TRUE)) {
                    mensagem += "Membro da Comunidade cadastrado";
                }
                mensagem += " com este CPF.";
                throw new Exception(mensagem);
            }
            setConsultarPessoa(Boolean.FALSE);

        } catch (Exception e) {
            pessoaVO.setCPF("");
            setConsultarPessoa(Boolean.TRUE);
            setMensagemDetalhada(e.getMessage());
        }
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Pessoa</code>. Caso o
     * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            pessoaVO.setProfessor(Boolean.TRUE);
            pessoaVO.setFuncionario(Boolean.TRUE);
            getFacadeFactory().getPessoaFacade().setIdEntidade("Professor");
//            pessoaVO.montarListaHorarioProfessor();
            funcionarioVO.setPessoa(pessoaVO);
            if (funcionarioVO.isNovoObj().booleanValue()) {
                ConfiguracaoGeralSistemaVO conf = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                if (conf.getCodigo().intValue() == 0 && getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
                    conf = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoUnidadeEnsino(0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                }
                getPessoaVO().setValorCssTopoLogo(conf.getVisaoPadraoProfessor().getValorCssTopoLogo());
                getPessoaVO().setValorCssBackground(conf.getVisaoPadraoProfessor().getValorCssBackground());
                getPessoaVO().setValorCssMenu(conf.getVisaoPadraoProfessor().getValorCssMenu());
                getFacadeFactory().getFuncionarioFacade().incluir(funcionarioVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            } else {
                getFacadeFactory().getFuncionarioFacade().alterar(funcionarioVO, getUsuario(), getConfiguracaoGeralPadraoSistema());
            }
            setMensagemID("msg_dados_gravados");
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public void gravarVisaoProfessor() {
        try {
            pessoaVO.setProfessor(Boolean.TRUE);
            pessoaVO.setFuncionario(Boolean.TRUE);
            getFacadeFactory().getPessoaFacade().setIdEntidade("Professor");
//            pessoaVO.montarListaHorarioProfessor();
            if (pessoaVO.isNovoObj().booleanValue()) {
                ConfiguracaoGeralSistemaVO conf = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoUnidadeEnsino(getUnidadeEnsinoVO().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                if (conf.getCodigo().intValue() == 0 && getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
                    conf = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoUnidadeEnsino(0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                }
                //getPessoaVO().setCssPadrao(conf.getVisaoPadraoProfessor().getCsspadrao());
                getPessoaVO().setValorCssTopoLogo(conf.getVisaoPadraoProfessor().getValorCssTopoLogo());
                getPessoaVO().setValorCssBackground(conf.getVisaoPadraoProfessor().getValorCssBackground());
                getPessoaVO().setValorCssMenu(conf.getVisaoPadraoProfessor().getValorCssMenu());
                getFacadeFactory().getPessoaFacade().incluir(pessoaVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), false);
            } else {
                getFacadeFactory().getPessoaFacade().alterar(pessoaVO, false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), false);
            }
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void upload(FileUploadEvent uploadEvent) {

        try {
            getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.IMAGEM_TMP, getUsuarioLogado());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            uploadEvent = null;
        }
    }

    public void paint(OutputStream out, Object data) throws Exception {
        ArquivoHelper arquivoHelper = new ArquivoHelper();
        try {
            arquivoHelper.renderizarImagemNaTela(out, getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            arquivoHelper = null;
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP PessoaCons.jsp. Define o tipo de consulta a ser
     * executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            getFacadeFactory().getPessoaFacade().setIdEntidade("Professor");
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getControleConsulta().getValorConsulta(), tipoPessoa,null, this.getUnidadeEnsinoLogado().getCodigo(), true,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getControleConsulta().getValorConsulta(), tipoPessoa, this.getUnidadeEnsinoLogado().getCodigo(), true,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeCidade")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCidade(getControleConsulta().getValorConsulta(), tipoPessoa, this.getUnidadeEnsinoLogado().getCodigo(), true,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("CPF")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getControleConsulta().getValorConsulta(), tipoPessoa, null, this.getUnidadeEnsinoLogado().getCodigo(), true,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("RG")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorRG(getControleConsulta().getValorConsulta(), tipoPessoa, this.getUnidadeEnsinoLogado().getCodigo(), true,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("unidadeEnsino")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeUnidadeEnsino(getControleConsulta().getValorConsulta(), tipoPessoa, this.getUnidadeEnsinoLogado().getCodigo(), true,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("departamento")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeDepartamento(getControleConsulta().getValorConsulta(), tipoPessoa, this.getUnidadeEnsinoLogado().getCodigo(), true,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            objs = ControleConsulta.obterSubListPaginaApresentar(objs, controleConsulta);
            definirVisibilidadeLinksNavegacao(controleConsulta.getPaginaAtual(), controleConsulta.getNrTotalPaginas());
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return "consultar";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>PessoaVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getPessoaFacade().setIdEntidade("Professor");
            getFacadeFactory().getFuncionarioFacade().excluir(funcionarioVO, getUsuarioLogado());
            setFuncionarioVO(new FuncionarioVO());
            setPessoaVO(new PessoaVO());
            setFiliacaoVO(new FiliacaoVO());
            setFormacaoAcademicaVO(new FormacaoAcademicaVO());
            setCodigoTurno(0);
//            setDisponibilidadeHorarioVO(new DisponibilidadeHorarioVO());
//            setDisciplinasInteresseVO(new DisciplinasInteresseVO());
            setVerificarCpf(this.validarCadastroPorCpf());
            setConsultarPessoa(Boolean.TRUE);
            setEditarDados(Boolean.FALSE);
            setHorarioSimples(Boolean.TRUE);
            setMensagemID("msg_dados_excluidos");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    public void consultarDisciplina() {
        try {

            List objs = new ArrayList(0);
            if (getValorConsultaDisciplina().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaDisciplina().equals("codigo")) {
                int valorInt = Integer.parseInt(getValorConsultaDisciplina());
                DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(new Integer(valorInt), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                if (!disciplina.equals(new DisciplinaVO()) || disciplina != null) {
                    objs.add(disciplina);
                }
            }
            if (getCampoConsultaDisciplina().equals("nome")) {
                objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaDisciplina().equals("areaConhecimento")) {
                objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeAreaConhecimento(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsultaDisciplina(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaDisciplina(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void adicionarDisciplina() throws Exception {
        try {
            setMensagemDetalhada("");
            DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplina");
//            getDisciplinasInteresseVO().setDisciplina(disciplina);
            adicionarDisciplinasInteresse();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaComboDisciplina() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("areaConhecimento", "Área de Conhecimento"));
        return itens;
    }

    public void selecionarTurno() throws Exception {
        TurnoVO obj = (TurnoVO) context().getExternalContext().getRequestMap().get("turno");
        setCodigoTurno(obj.getCodigo());
        montarListaDisponibilidadeTurno(true);
    }

    public Integer getTamanhoListaTurno() {
        if (getListaSelectItemTurno() == null) {
            return 0;
        }
        return getListaSelectItemTurno().size();
    }

    public void montarDadosHorarioProfessorTurno() {
        try {
            montarListaDisponibilidadeTurno(Boolean.FALSE);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void montarListaDisponibilidadeTurno(Boolean detalhado) throws Exception {

//        if (getQuadroHorario().getTurno().getCodigo().intValue() != 0) {
//            adicionarQuadroHorario();
//            setQuadroHorario(new QuadroHorarioVO());
//        }
//        setQuadroHorario(getPessoaVO().consultarObjQuadroHorarioVO(getCodigoTurno()));
//        if (getQuadroHorario().getTurno().getCodigo().equals(0) && getCodigoTurno().intValue() != 0) {
//            getQuadroHorario().setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(codigoTurno, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
//            atualizarQuadroHorario(detalhado);
//        } else if (getCodigoTurno().intValue() == 0) {
//            setQuadroHorario(new QuadroHorarioVO());
//        }

    }

    public void atualizarQuadroHorario(Boolean detalhado) throws Exception {
//        List lista = getPessoaVO().getHorarioProfessorVOs();
//        Iterator i = lista.iterator();
//        while (i.hasNext()) {
//            HorarioProfessorVO obj = (HorarioProfessorVO) i.next();
//            if (obj.getTurno().getCodigo().equals(getQuadroHorario().getTurno().getCodigo())) {
//                setQuadroHorario(getFacadeFactory().getHorarioProfessorFacade().atualizarDadosQuadroHorario(obj, getQuadroHorario(), detalhado, null, null, getUsuarioLogado()));
//                adicionarQuadroHorario();
//                return;
//            }
//        }
//        getFacadeFactory().getHorarioProfessorFacade().montarDadosListaQuadroHorarioVO(getQuadroHorario(), getUsuarioLogado());
//        adicionarQuadroHorario();
//        pessoaVO.montarListaHorarioProfessor();
    }

    public void montarQuadroHorarioTurno() throws Exception {
        selecionarTurno();
        montarListaDisponibilidadeTurnoDetalhado();
    }

    public void montarListaDisponibilidadeTurnoDetalhado() throws Exception {
        try {
            if (getCodigoTurno().intValue() != 0) {
                atualizarQuadroHorario(Boolean.TRUE);
                setDiaSemana("02");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void adicionarQuadroHorario() throws Exception {
//        try {
//            getPessoaVO().adicionarObjQuadroHorarioVOs(getQuadroHorario());
//            setMensagemID("msg_dados_adicionados");
//        } catch (Exception e) {
//            setMensagemDetalhada("msg_erro", e.getMessage());
//        }
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>DisponibilidadeHorario</code> para
     * edição pelo usuário.
     */
    public void editarDisponibilidadeHorario() throws Exception {
//        DisponibilidadeHorarioVO obj = (DisponibilidadeHorarioVO) context().getExternalContext().getRequestMap().get("disponibilidadeHorario");
//        setDisponibilidadeHorarioVO(obj);
//        // return "editar";
    }

    /*
     * Método responsável por remover um novo objeto da classe <code>DisponibilidadeHorario</code> do objeto
     * <code>pessoaVO</code> da classe <code>Pessoa</code>
     */
    public void removerDisponibilidadeHorario() throws Exception {
//        getPessoaVO().excluirObjDisponibilidadeHorarioVOs(getDisponibilidadeHorarioVO());
//        setMensagemID("msg_dados_excluidos");
//        // return "editar";
    }

    /*
     * Método responsável por adicionar um novo objeto da classe <code>Filiacao</code> para o objeto
     * <code>pessoaVO</code> da classe <code>Pessoa</code>
     */
    public void adicionarFiliacao() throws Exception {
        try {
            if (!getPessoaVO().getCodigo().equals(0)) {
                filiacaoVO.setAluno(getPessoaVO().getCodigo());
            }
            getPessoaVO().adicionarObjFiliacaoVOs(getFiliacaoVO());
            this.setFiliacaoVO(new FiliacaoVO());
            setMensagemID("msg_dados_adicionados");
            // return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            // return "editar";
        }
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>Filiacao</code> para edição pelo
     * usuário.
     */
    public void editarFiliacao()  {
    	try {
	        FiliacaoVO obj = (FiliacaoVO) context().getExternalContext().getRequestMap().get("filiacao");
	        setFiliacaoVO(obj.getClone());
	    	setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

    /*
     * Método responsável por remover um novo objeto da classe <code>Filiacao</code> do objeto <code>pessoaVO</code> da
     * classe <code>Pessoa</code>
     */
    public void removerFiliacao() throws Exception {
        FiliacaoVO obj = (FiliacaoVO) context().getExternalContext().getRequestMap().get("filiacao");
        getPessoaVO().excluirObjFiliacaoVOs(obj.getNome());
        setMensagemID("msg_dados_excluidos");
        // return "editar";
    }

    /*
     * Método responsável por adicionar um novo objeto da classe <code>DisciplinasInteresse</code> para o objeto
     * <code>pessoaVO</code> da classe <code>Pessoa</code>
     */
    public void adicionarDisciplinasInteresse() throws Exception {
//        try {
//            if (!getPessoaVO().getCodigo().equals(0)) {
//                disciplinasInteresseVO.setProfessor(getPessoaVO().getCodigo());
//            }
//            if (getDisciplinasInteresseVO().getDisciplina().getCodigo().intValue() != 0) {
//                Integer campoConsulta = getDisciplinasInteresseVO().getDisciplina().getCodigo();
//                DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
//                getDisciplinasInteresseVO().setDisciplina(disciplina);
//            }
//            getPessoaVO().adicionarObjDisciplinasInteresseVOs(getDisciplinasInteresseVO());
//            removerDisciplinaListaConsulta();
//            this.setDisciplinasInteresseVO(new DisciplinasInteresseVO());
//            setMensagemID("msg_dados_adicionados");
//            // return "editar";
//        } catch (Exception e) {
//            setMensagemDetalhada("msg_erro", e.getMessage());
//            // return "editar";
//        }
    }

    public void removerDisciplinaListaConsulta() {
        Iterator i = getListaConsultaDisciplina().iterator();
        int index = 0;
        while (i.hasNext()) {
            DisciplinaVO objExistente = (DisciplinaVO) i.next();
//            if (objExistente.getCodigo().equals(getDisciplinasInteresseVO().getDisciplina().getCodigo())) {
//                getListaConsultaDisciplina().remove(index);
//                return;
//            }
            index++;
        }
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>DisciplinasInteresse</code> para edição
     * pelo usuário.
     */
    public void editarDisciplinasInteresse() throws Exception {
//        DisciplinasInteresseVO obj = (DisciplinasInteresseVO) context().getExternalContext().getRequestMap().get("disciplinasInteresse");
//        setDisciplinasInteresseVO(obj);
//        // return "editar";
    }

    /*
     * Método responsável por remover um novo objeto da classe <code>DisciplinasInteresse</code> do objeto
     * <code>pessoaVO</code> da classe <code>Pessoa</code>
     */
    public void removerDisciplinasInteresse() throws Exception {
//        DisciplinasInteresseVO obj = (DisciplinasInteresseVO) context().getExternalContext().getRequestMap().get("disciplinasInteresse");
//        getPessoaVO().excluirObjDisciplinasInteresseVOs(obj.getDisciplina().getCodigo());
//        setMensagemID("msg_dados_excluidos");
//        // return "editar";
    }

    /*
     * Método responsável por adicionar um novo objeto da classe <code>FormacaoAcademica</code> para o objeto
     * <code>pessoaVO</code> da classe <code>Pessoa</code>
     */
    public void adicionarFormacaoAcademica() throws Exception {
        try {
            if (!getPessoaVO().getCodigo().equals(0)) {
                formacaoAcademicaVO.setPessoa(getPessoaVO().getCodigo());
            }
            getPessoaVO().adicionarObjFormacaoAcademicaVOs(getFormacaoAcademicaVO());
            this.setFormacaoAcademicaVO(new FormacaoAcademicaVO());
            setMensagemID("msg_dados_adicionados");
            // return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            // return "editar";
        }
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>FormacaoAcademica</code> para edição
     * pelo usuário.
     */
    public void editarFormacaoAcademica() throws Exception {
        FormacaoAcademicaVO obj = (FormacaoAcademicaVO) context().getExternalContext().getRequestMap().get("formacaoAcademica");
        setFormacaoAcademicaVO(obj);
        // return "editar";
    }

    /*
     * Método responsável por remover um novo objeto da classe <code>FormacaoAcademica</code> do objeto
     * <code>pessoaVO</code> da classe <code>Pessoa</code>
     */
    public void removerFormacaoAcademica() throws Exception {
        FormacaoAcademicaVO obj = (FormacaoAcademicaVO) context().getExternalContext().getRequestMap().get("formacaoAcademica");
        getPessoaVO().excluirObjFormacaoAcademicaVOs(obj.getCurso());
        setMensagemID("msg_dados_excluidos");
        // return "editar";
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
     * Método responsável por processar a consulta na entidade <code>Turno</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarTurnoPorChavePrimaria() {
//        try {
//            Integer campoConsulta = disponibilidadeHorarioVO.getTurno().getCodigo();
//            TurnoVO turno = getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
//            disponibilidadeHorarioVO.getTurno().setNome(turno.getNome());
//            this.setTurno_Erro("");
//            setMensagemID("msg_dados_consultados");
//        } catch (Exception e) {
//            setMensagemID("msg_erro_dadosnaoencontrados");
//            disponibilidadeHorarioVO.getTurno().setNome("");
//            disponibilidadeHorarioVO.getTurno().setCodigo(0);
//            this.setTurno_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
//        }
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>diaSemana</code>
     */
    public List getListaSelectItemDiaSemanaDisponibilidadeHorario() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable diaSemanaDisponibilidadeHorarios = (Hashtable) Dominios.getDiaSemanaDisponibilidadeHorario();
        Enumeration keys = diaSemanaDisponibilidadeHorarios.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) diaSemanaDisponibilidadeHorarios.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>tipo</code>
     */
    public List getListaSelectItemTipoFiliacao() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoFiliacaos = (Hashtable) Dominios.getTipoFiliacao();
        Enumeration keys = tipoFiliacaos.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoFiliacaos.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Disciplina</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarDisciplinaPorChavePrimaria() {
//        try {
//            Integer campoConsulta = disciplinasInteresseVO.getDisciplina().getCodigo();
//            DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
//            disciplinasInteresseVO.getDisciplina().setNome(disciplina.getNome());
//            this.setDisciplina_Erro("");
//            setMensagemID("msg_dados_consultados");
//        } catch (Exception e) {
//            setMensagemID("msg_erro_dadosnaoencontrados");
//            disciplinasInteresseVO.getDisciplina().setNome("");
//            disciplinasInteresseVO.getDisciplina().setCodigo(0);
//            this.setDisciplina_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
//        }
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>situacao</code>
     */
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

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>escolaridade</code>
     */
    public List getListaSelectItemEscolaridadeFormacaoAcademica() throws Exception {
//		List objs = new ArrayList(0);
//		objs.add(new SelectItem("", ""));
//		Hashtable escolaridadeFormacaoAcademicas = (Hashtable) Dominios.getEscolaridadeFormacaoAcademica();
//		Enumeration keys = escolaridadeFormacaoAcademicas.keys();
//		while (keys.hasMoreElements()) {
//			String value = (String) keys.nextElement();
//			String label = (String) escolaridadeFormacaoAcademicas.get(value);
//			objs.add(new SelectItem(value, label));
//		}
//		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
//		Collections.sort((List) objs, ordenador);
        List objs = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(NivelFormacaoAcademica.class, true);
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>tipoPessoa</code>
     */
    public List getListaSelectItemTipoPessoaPessoa() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoPessoaBasicoPessoas = (Hashtable) Dominios.getTipoPessoaBasicoPessoa();
        Enumeration keys = tipoPessoaBasicoPessoas.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoPessoaBasicoPessoas.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>estadoEmissaoRG</code>
     */
    public List getListaSelectItemEstadoEmissaoRGPessoa() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable estados = (Hashtable) Dominios.getEstado();
        Enumeration keys = estados.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) estados.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>estadoCivil</code>
     */
    public List getListaSelectItemEstadoCivilPessoa() throws Exception {
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

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>sexo</code>
     */
    public List getListaSelectItemSexoPessoa() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable sexos = (Hashtable) Dominios.getSexo();
        Enumeration keys = sexos.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) sexos.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /**
     * Operação responsável por processar a consulta pelo parâmetro informado pelo usuário.
     * <code>DisciplinasInteresseVO</code> Após a consulta ela automaticamente adciona o código e o nome da disciplina
     * na tela.
     */
//    public List consultarDisciplinaSuggestionbox(Object event) {
//        try {
//            String valor = event.toString();
//            List lista = getFacadeFactory().getDisciplinaFacade().consultarPorNome(valor, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
//            this.setDisciplina_Erro("");
//            disciplinasInteresseVO.getDisciplina().setNome("");
//            return lista;
//        } catch (Exception e) {
//            this.setDisciplina_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
//            return new ArrayList(0);
//        }
//    }

    /**
     * Operação responsável por processar a consulta pelo parâmetro informado pelo usuário.
     * <code>DisponibilidadeHorarioVO</code> Após a consulta ela automaticamente adciona o código e o nome do turno na
     * tela.
     */
    public List consultarTurnoSuggestionbox(Object event) {
        try {
            String valor = event.toString();
            List lista = getFacadeFactory().getTurnoFacade().consultarPorNome(valor, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            this.setTurno_Erro("");
//            disponibilidadeHorarioVO.getTurno().setNome("");
            return lista;
        } catch (Exception e) {
            this.setTurno_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
            return new ArrayList(0);
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Nacionalidade</code>.
     */
    public void montarListaSelectItemNacionalidade(String prm) throws Exception {
        List resultadoConsulta = consultarPaizPorNome(prm);
        Iterator i = resultadoConsulta.iterator();
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        while (i.hasNext()) {
            PaizVO obj = (PaizVO) i.next();
            objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        setListaSelectItemNacionalidade(objs);
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Nacionalidade</code>. Buscando todos os
     * objetos correspondentes a entidade <code>Paiz</code>. Esta rotina não recebe parâmetros para filtragem de dados,
     * isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemNacionalidade() {
        try {
            montarListaSelectItemNacionalidade("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarPaizPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getPaizFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Naturalidade</code>.
     */
    public void montarListaSelectItemNaturalidade(String prm) throws Exception {
        List resultadoConsulta = consultarCidadePorNome(prm);
        Iterator i = resultadoConsulta.iterator();
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        while (i.hasNext()) {
            CidadeVO obj = (CidadeVO) i.next();
            objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        setListaSelectItemNaturalidade(objs);
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Naturalidade</code>. Buscando todos os
     * objetos correspondentes a entidade <code>Cidade</code>. Esta rotina não recebe parâmetros para filtragem de
     * dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemNaturalidade() {
        try {
            montarListaSelectItemNaturalidade("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Cidade</code>.
     */
    public void montarListaSelectItemCidade(String prm) throws Exception {
        List resultadoConsulta = consultarCidadePorNome(prm);
        Iterator i = resultadoConsulta.iterator();
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        while (i.hasNext()) {
            CidadeVO obj = (CidadeVO) i.next();
            objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        setListaSelectItemCidade(objs);
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Cidade</code>. Buscando todos os objetos
     * correspondentes a entidade <code>Cidade</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemCidade() {
        try {
            montarListaSelectItemCidade("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
        Iterator i = resultadoConsulta.iterator();
        List objs = new ArrayList(0);
        if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
            objs.add(new SelectItem(0, ""));
        }
        while (i.hasNext()) {
            UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
            objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
        }
        setListaSelectItemUnidadeEnsino(objs);
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

    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarCidadePorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getCidadeFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemDepartamento(String prm) throws Exception {
        List resultadoConsulta = consultarDepartamentoPorNome(prm);
        Iterator i = resultadoConsulta.iterator();
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        while (i.hasNext()) {
            DepartamentoVO obj = (DepartamentoVO) i.next();
            objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
        }
        setListaSelectItemDepartamento(objs);
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Departamento</code>. Buscando todos os
     * objetos correspondentes a entidade <code>Departamento</code>. Esta rotina não recebe parâmetros para filtragem de
     * dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemDepartamento() {
        try {
            montarListaSelectItemDepartamento("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarDepartamentoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getDepartamentoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemCargo(String prm) throws Exception {
        List resultadoConsulta = consultarCargoPorNome(prm);
        Iterator i = resultadoConsulta.iterator();
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        while (i.hasNext()) {
            CargoVO obj = (CargoVO) i.next();
            objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
        }
        setListaSelectItemCargo(objs);
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Cargo</code>. Buscando todos os objetos
     * correspondentes a entidade <code>Cargo</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemCargo() {
        try {
            montarListaSelectItemCargo("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarCargoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getCargoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    public void verHorarioSimples() {
        setHorarioSimples(Boolean.TRUE);
    }

    public void verHorarioDetalhado() {
        setHorarioSimples(Boolean.FALSE);
    }

    public void montarListaSelectItemTurno(String prm) throws Exception {
        List resultadoConsulta = consultarTurnoPorNome(prm);
        setListaSelectItemTurno(resultadoConsulta);
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Cargo</code>. Buscando todos os objetos
     * correspondentes a entidade <code>Cargo</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemTurno() {
        try {
            montarListaSelectItemTurno("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarTurnoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getTurnoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

//    public void montarListaSelectItemAreaConhecimento(String prm) throws Exception {
//        List resultadoConsulta = consultarAreaConhecimentoPorNome(prm);
//        Iterator i = resultadoConsulta.iterator();
//        List objs = new ArrayList(0);
//        objs.add(new SelectItem(0, ""));
//        while (i.hasNext()) {
//            AreaConhecimentoVO obj = (AreaConhecimentoVO) i.next();
//            objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
//        }
//        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
//        Collections.sort((List) objs, ordenador);
//        setListaSelectItemAreaConhecimento(objs);
//    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Nacionalidade</code>. Buscando todos os
     * objetos correspondentes a entidade <code>Paiz</code>. Esta rotina não recebe parâmetros para filtragem de dados,
     * isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
//    public void montarListaSelectItemAreaConhecimento() {
//        try {
//            montarListaSelectItemAreaConhecimento("");
//        } catch (Exception e) {
//            //System.out.println("MENSAGEM => " + e.getMessage());;
//        }
//    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
   

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Cidade</code>.
     */
   

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Cidade</code>. Buscando todos os objetos
     * correspondentes a entidade <code>Cidade</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
   

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
  

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemCidade();
        montarListaSelectItemNaturalidade();
        montarListaSelectItemNacionalidade();
        montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemDepartamento();
        montarListaSelectItemTurno();
        montarListaSelectItemCargo();
//        montarListaSelectItemAreaConhecimento();
//        montarListaSelectItemPerfilEconomico();
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("nomeCidade", "Cidade"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("RG", "RG"));
        itens.add(new SelectItem("departamento", "Departamento"));
        itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setPaginaAtualDeTodas("0/0");
        setListaConsulta(new ArrayList(0));
        definirVisibilidadeLinksNavegacao(0, 0);
        setMensagemID("msg_entre_prmconsulta");
        return "consultar";
    }

    public Integer getCodigoTurno() {
        return codigoTurno;
    }

    public void setCodigoTurno(Integer codigoTurno) {
        this.codigoTurno = codigoTurno;
    }

//    public QuadroHorarioVO getQuadroHorario() {
//        return quadroHorario;
//    }
//
//    public void setQuadroHorario(QuadroHorarioVO quadroHorario) {
//        this.quadroHorario = quadroHorario;
//    }

    public String getCampoConsultaDisciplina() {
        return campoConsultaDisciplina;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
        this.campoConsultaDisciplina = campoConsultaDisciplina;
    }

    public List getListaConsultaDisciplina() {
        return listaConsultaDisciplina;
    }

    public void setListaConsultaDisciplina(List listaConsultaDisciplina) {
        this.listaConsultaDisciplina = listaConsultaDisciplina;
    }

    public String getValorConsultaDisciplina() {
        return valorConsultaDisciplina;
    }

    public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
        this.valorConsultaDisciplina = valorConsultaDisciplina;
    }

    public FuncionarioVO getFuncionarioVO() {
        return funcionarioVO;
    }

    public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
        this.funcionarioVO = funcionarioVO;
    }

    public String getTurno_Erro() {
        return turno_Erro;
    }

    public void setTurno_Erro(String turno_Erro) {
        this.turno_Erro = turno_Erro;
    }

//    public DisponibilidadeHorarioVO getDisponibilidadeHorarioVO() {
//        return disponibilidadeHorarioVO;
//    }
//
//    public void setDisponibilidadeHorarioVO(DisponibilidadeHorarioVO disponibilidadeHorarioVO) {
//        this.disponibilidadeHorarioVO = disponibilidadeHorarioVO;
//    }

    public FiliacaoVO getFiliacaoVO() {
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
//
//    public DisciplinasInteresseVO getDisciplinasInteresseVO() {
//        return disciplinasInteresseVO;
//    }
//
//    public void setDisciplinasInteresseVO(DisciplinasInteresseVO disciplinasInteresseVO) {
//        this.disciplinasInteresseVO = disciplinasInteresseVO;
//    }

    public List getListaSelectItemCargo() {
        return listaSelectItemCargo;
    }

    public void setListaSelectItemCargo(List listaSelectItemCargo) {
        this.listaSelectItemCargo = listaSelectItemCargo;
    }

    public List getListaSelectItemDepartamento() {
        return listaSelectItemDepartamento;
    }

    public void setListaSelectItemDepartamento(List listaSelectItemDepartamento) {
        this.listaSelectItemDepartamento = listaSelectItemDepartamento;
    }

    public List getListaSelectItemUnidadeEnsino() {
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public FormacaoAcademicaVO getFormacaoAcademicaVO() {
        return formacaoAcademicaVO;
    }

    public void setFormacaoAcademicaVO(FormacaoAcademicaVO formacaoAcademicaVO) {
        this.formacaoAcademicaVO = formacaoAcademicaVO;
    }

    public List getListaSelectItemNacionalidade() {
        return (listaSelectItemNacionalidade);
    }

    public void setListaSelectItemNacionalidade(List listaSelectItemNacionalidade) {
        this.listaSelectItemNacionalidade = listaSelectItemNacionalidade;
    }

    public List getListaSelectItemNaturalidade() {
        return (listaSelectItemNaturalidade);
    }

    public void setListaSelectItemNaturalidade(List listaSelectItemNaturalidade) {
        this.listaSelectItemNaturalidade = listaSelectItemNaturalidade;
    }

    public List getListaSelectItemCidade() {
        return (listaSelectItemCidade);
    }

    public void setListaSelectItemCidade(List listaSelectItemCidade) {
        this.listaSelectItemCidade = listaSelectItemCidade;
    }

    public List getListaSelectItemTurno() {
        return listaSelectItemTurno;
    }

    public void setListaSelectItemTurno(List listaSelectItemTurno) {
        this.listaSelectItemTurno = listaSelectItemTurno;
    }

    public PessoaVO getPessoaVO() {
        return pessoaVO;
    }

    public void setPessoaVO(PessoaVO pessoaVO) {
        this.pessoaVO = pessoaVO;
    }

    public List getListaSelectItemAreaConhecimento() {
        return listaSelectItemAreaConhecimento;
    }

    public void setListaSelectItemAreaConhecimento(List listaSelectItemAreaConhecimento) {
        this.listaSelectItemAreaConhecimento = listaSelectItemAreaConhecimento;
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

    public String disciplina() {
        return "disciplina";
    }

    public String disponibilidadeHorario() {
        return "disponibilidadeHorario";
    }

    public Boolean getConsultarPessoa() {
        return consultarPessoa;
    }

    public void setConsultarPessoa(Boolean consultarPessoa) {
        this.consultarPessoa = consultarPessoa;
    }

    public Boolean getEditarDados() {
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

    public Boolean getHorarioSimples() {
        return horarioSimples;
    }

    public void setHorarioSimples(Boolean horarioSimples) {
        this.horarioSimples = horarioSimples;
    }

    public List getListaSelectItemPerfilEconomico() {
        return listaSelectItemPerfilEconomico;
    }

    public void setListaSelectItemPerfilEconomico(List listaSelectItemPerfilEconomico) {
        this.listaSelectItemPerfilEconomico = listaSelectItemPerfilEconomico;
    }

    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        funcionarioVO = null;
        pessoaVO = null;
        Uteis.liberarListaMemoria(listaSelectItemCidade);
        Uteis.liberarListaMemoria(listaSelectItemNaturalidade);
        Uteis.liberarListaMemoria(listaSelectItemNacionalidade);
        Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
        Uteis.liberarListaMemoria(listaSelectItemDepartamento);
        Uteis.liberarListaMemoria(listaSelectItemCargo);
        Uteis.liberarListaMemoria(listaSelectItemPerfilEconomico);
        formacaoAcademicaVO = null;
        disciplina_Erro = null;
        filiacaoVO = null;
        turno_Erro = null;
        tipoPessoa = null;
//        disciplinasInteresseVO = null;
//        disponibilidadeHorarioVO = null;
        horarioSimples = null;
    }
}
