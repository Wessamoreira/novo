/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.bancocurriculum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DropEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.AreaProfissionalVO;
import negocio.comuns.bancocurriculum.CandidatosVagasVO;
import negocio.comuns.bancocurriculum.CurriculumPessoaVO;
import negocio.comuns.bancocurriculum.OpcaoRespostaVagaQuestaoVO;
import negocio.comuns.bancocurriculum.VagaAreaVO;
import negocio.comuns.bancocurriculum.VagaContatoVO;
import negocio.comuns.bancocurriculum.VagaEstadoVO;
import negocio.comuns.bancocurriculum.VagaQuestaoVO;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.bancocurriculum.enumeradores.SituacaoReferenteVagaEnum;
import negocio.comuns.bancocurriculum.enumeradores.TipoVagaQuestaoEnum;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.BuscaCandidatoVagaVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.academico.CurriculumRelControle;

@Controller("VagasControle")
@Scope("viewScope")
@Lazy
public class VagasControle extends CurriculumRelControle implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -906870747242192959L;
	private VagasVO vagasVO;
    private String campoConsultaParceiro;
    private String valorConsultaParceiro;
    private List listaConsultaParceiro;
    private String campoConsultaAreaProfissional;
    private String valorConsultaAreaProfissional;
    private List listaConsultaAreaProfissional;
    private List listaSelectItemAreaProfissional;
    private List listaSelectItemEstado;
    private List listaSelectItemCidade;
    private List listaSelectItemCidadeSugerir;
    protected String campoConsultaCidade;
    protected String valorConsultaCidade;
    protected List listaConsultaCidade;
    private Boolean apresentarComboCidade;
    private List listaCandidatosVagasVOs;
    private String controleAbas;
    private String controleAbasPrincipal;
    private Boolean apresentarCampoOutrosSoftwares;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private List listaConsultaAluno;
    private Boolean isVisaoParceiro;
    private Boolean mostrarBotaoBuscaCandidatoVaga;
    private Date dataExpiracao;
    private List<BuscaCandidatoVagaVO> listaBuscaCandidatoVagaVO;
    private List<VagasVO> listaVagasRecentementeAtivadas;
    private String ordenacao;
    private VagaQuestaoVO vagaQuestaoVO;
    private List<SelectItem> listaSelectItemTipoVagaQuestao;
    private List<PessoaVO> pessoaVOs;
    private Boolean enviarEmailSomenteParaAlunoDaCidade;
    private VagaContatoVO vagaContato;
    private CandidatosVagasVO candidatosVagasVO;
    private PersonalizacaoMensagemAutomaticaVO mensagemTemplate;
    private CursoVO curso;
    private EstadoVO estado;
    private CidadeVO cidade;
    private AreaProfissionalVO areaProfissional;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List listaConsultaCurso;
    private List listaConsultaEstado;
    private List listaEstadoSugerir;
    private List listaCidadeSugerir;
    private List listaAreaProfissionalSugerir;
    private List listaConsultaArea;

    public VagasControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        getControleConsulta().setCampoConsulta("situacao");
        consultarVagasVisaoParceiroAoEntrar();
        montarListaVagasRecentementeAtivadas();
        Boolean vagaEncerradas = (Boolean) context().getExternalContext().getSessionMap().get("vagaEncerradas");
        if (vagaEncerradas != null && vagaEncerradas) {
            consultarVagasVisaoParceiroPorSituacao("EN");
            context().getExternalContext().getSessionMap().remove("vagaEncerradas");
        }
        Boolean vagaExpiradas = (Boolean) context().getExternalContext().getSessionMap().get("vagaExpiradas");
        if (vagaExpiradas != null && vagaExpiradas) {
            consultarVagasVisaoParceiroPorSituacao("EX");
            context().getExternalContext().getSessionMap().remove("vagaExpiradas");
        }
        Boolean vagaSobAnalise = (Boolean) context().getExternalContext().getSessionMap().get("vagaSobAnalise");
        if (vagaSobAnalise != null && vagaSobAnalise) {
            consultarVagasVisaoParceiroPorSituacao("EC");
            context().getExternalContext().getSessionMap().remove("vagaSobAnalise");
        }
        setMensagemID("msg_entre_prmconsulta");
    }

    private void montarListaVagasRecentementeAtivadas() {
        try {
            // VisaoAlunoControle visaoAlunoControle = (VisaoAlunoControle)
            // getControlador("VisaoAlunoControle");
            // visaoAlunoControle.setPessoaVO(getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(visaoAlunoControle.getPessoaVO().getCodigo(),
            // false, true, false, getUsuarioLogado()));
            setListaVagasRecentementeAtivadas(getFacadeFactory().getVagasFacade().consultarVagasRecentementeAtivadas());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void inicializarObjetoParceiroVO() {
        try {
            if (getUsuarioLogado().getParceiro() != null && !getUsuarioLogado().getParceiro().getCodigo().equals(0)) {
                ParceiroVO parceiro = getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(getUsuarioLogado().getParceiro().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
                getVagasVO().setParceiro(parceiro);
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }

    }

    public String novo() throws Exception {
        registrarAtividadeUsuario(getUsuarioLogado(), "VagasControle", "Novo Vagas", "Novo");
        removerObjetoMemoria(this);
        montarListaSelectItemAreaProfissional();
        montarListaSelectItemEstado();
        montarListaSelectItemCidade();
        montarListaSelectItemCidadeSugerir();
        setVagasVO(new VagasVO());
        ConfiguracaoGeralSistemaVO conf = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO(), null);
        getVagasVO().setQtdDiasExpiracaoVaga(conf.getQtdDiasExpiracaoVagaBancoCurriculum());
        // iniciializa o obj de parceiro quando o usuario loga na
        // visao parceiro e solicita o cadastro de nova vaga
        setControleAbas("etapa1");

        setVagaQuestaoVO(getFacadeFactory().getVagaQuestaoFacade().novo());
        inicializarObjetoParceiroVO();
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("vagasForm.xhtml");

    }

    public void novoVisaoParceiro() throws Exception {
        registrarAtividadeUsuario(getUsuarioLogado(), "VagasControle", "Novo Vagas", "Novo");
        removerObjetoMemoria(this);
        montarListaSelectItemAreaProfissional();
        montarListaSelectItemEstado();
        setVagasVO(new VagasVO());
        setVagaQuestaoVO(getFacadeFactory().getVagaQuestaoFacade().novo());
        ConfiguracaoGeralSistemaVO conf = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO(), null);
        getVagasVO().setQtdDiasExpiracaoVaga(conf.getQtdDiasExpiracaoVagaBancoCurriculum());		
        // iniciializa o obj de parceiro quando o usuario loga na
        // visao parceiro e solicita o cadastro de nova vaga
        //setControleAbas("dbAba2");
        setControleAbasPrincipal("dbAba2");
        inicializarObjetoParceiroVO();
        setMensagemID("msg_entre_dados");
    }

    public String editar() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "VagasControle", "Inicializando Editar Vagas", "Editando");
            VagasVO obj = (VagasVO) context().getExternalContext().getRequestMap().get("vagasItens");
            setVagasVO(obj);
            montarListaEstado();
            montarListaArea();
            setVagasVO(getFacadeFactory().getVagasFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            carregarListaEstadoVaga();
            carregarListaAreaVaga();
            if (!getVagasVO().getOutrosSoftwares().equals("")) {
                setApresentarCampoOutrosSoftwares(true);
            }
            if (getVagasVO().getDataAtivacao() != null) {
                setDataExpiracao(Uteis.obterDataFutura(getVagasVO().getDataAtivacao(), getConfiguracaoGeralPadraoSistema().getQtdDiasNotificacaoExpiracaoVagaBancoCurriculum()));
                setListaBuscaCandidatoVagaVO(getFacadeFactory().getPessoaFacade().consultaRapidaBuscaCandidatoVagaPorVaga(getVagasVO()));
                if (!getListaBuscaCandidatoVagaVO().isEmpty()) {
                    setMostrarBotaoBuscaCandidatoVaga(Boolean.TRUE);
                }
            }
            setControleAbas("etapa1");
            getVagasVO().setVagaQuestaoVOs(getFacadeFactory().getVagaQuestaoFacade().consultarPorVagas(getVagasVO().getCodigo()));
            montarListaSelectItemAreaProfissional();
            montarListaSelectItemEstado();
            montarListaSelectItemCidade();
            montarListaSelectItemCidadeSugerir();
            setApresentarComboCidade(Boolean.TRUE);
            // setListaConsulta(new ArrayList(0));
            registrarAtividadeUsuario(getUsuarioLogado(), "VagasControle", "Finalizando Editar Vagas", "Editando");
            //setMensagemID("msg_dados_editar");
            setMensagemID("msg_dados_editar", Uteis.SUCESSO);
            return Uteis.getCaminhoRedirecionamentoNavegacao("vagasForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public String editarVisaoParceiro() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "VagasControle", "Inicializando Editar Vagas", "Editando");
            VagasVO obj = (VagasVO) context().getExternalContext().getRequestMap().get("vagasItens");
            setVagasVO(getFacadeFactory().getVagasFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            setControleAbas("etapa1");
            if (!getVagasVO().getOutrosSoftwares().equals("")) {
                setApresentarCampoOutrosSoftwares(true);
            }
            if (getVagasVO().getDataAtivacao() != null) {
                setDataExpiracao(Uteis.obterDataFutura(getVagasVO().getDataAtivacao(), getConfiguracaoGeralPadraoSistema().getQtdDiasNotificacaoExpiracaoVagaBancoCurriculum()));
                setListaBuscaCandidatoVagaVO(getFacadeFactory().getPessoaFacade().consultaRapidaBuscaCandidatoVagaPorVaga(getVagasVO()));
                if (!getListaBuscaCandidatoVagaVO().isEmpty()) {
                    setMostrarBotaoBuscaCandidatoVaga(Boolean.TRUE);
                }
            }
            getVagasVO().setVagaQuestaoVOs(getFacadeFactory().getVagaQuestaoFacade().consultarPorVagas(getVagasVO().getCodigo()));
            montarListaSelectItemAreaProfissional();
            montarListaSelectItemEstado();
            montarListaSelectItemCidade();
            setApresentarComboCidade(Boolean.TRUE);
            // setListaConsulta(new ArrayList(0));
            registrarAtividadeUsuario(getUsuarioLogado(), "VagasControle", "Finalizando Editar Vagas", "Editando");
            setMensagemID("msg_dados_editar");
            return "vagasVisaoParceiroForm";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto
     * da classe <code>Parceiro</code>. Caso o objeto seja novo (ainda não
     * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
     * contrário é acionado o <code>alterar()</code>. Se houver alguma
     * inconsistência o objeto não é gravado, sendo re-apresentado para o
     * usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (!getApresentarCampoOutrosSoftwares()) {
                getVagasVO().setOutrosSoftwares("");
            }
            montarListaEstado();
            if (getVagasVO().isNovoObj().booleanValue()) {
                registrarAtividadeUsuario(getUsuarioLogado(), "VagasControle", "Inicializando Incluir Vagas", "Incluindo");
                getVagasVO().setVagaEstadoVOs(getListaConsultaEstado());
                getVagasVO().setVagaAreaVOs(getListaConsultaArea());
                getFacadeFactory().getVagasFacade().incluir(getVagasVO(), getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "VagasControle", "Finalizando Incluir Vagas", "Incluindo");
            } else {
                registrarAtividadeUsuario(getUsuarioLogado(), "VagasControle", "Inicializando Alterar Vagas", "Alterando");
                getVagasVO().setVagaEstadoVOs(getListaConsultaEstado());
                getVagasVO().setVagaAreaVOs(getListaConsultaArea());
                getFacadeFactory().getVagasFacade().alterar(getVagasVO(), getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "VagasControle", "Finalizando Alterar Vagas", "Alterando");
            }
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
            getListaConsulta().clear();
            return Uteis.getCaminhoRedirecionamentoNavegacao("vagasForm.xhtml");
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public String gravarVisaoParceiro() {
        try {
            if (!getApresentarCampoOutrosSoftwares()) {
                getVagasVO().setOutrosSoftwares("");
            }
            getVagasVO().setSituacao("AT");
            if (getVagasVO().isNovoObj().booleanValue()) {
                registrarAtividadeUsuario(getUsuarioLogado(), "VagasControle", "Inicializando Incluir Vagas", "Incluindo");
                getVagasVO().setVagaEstadoVOs(getListaConsultaEstado());
                getVagasVO().setVagaAreaVOs(getListaConsultaArea());
                getFacadeFactory().getVagasFacade().incluir(getVagasVO(), getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "VagasControle", "Finalizando Incluir Vagas", "Incluindo");
            } else {
                registrarAtividadeUsuario(getUsuarioLogado(), "VagasControle", "Inicializando Alterar Vagas", "Alterando");
                getVagasVO().setVagaEstadoVOs(getListaConsultaEstado());
                getVagasVO().setVagaAreaVOs(getListaConsultaArea());
                getFacadeFactory().getVagasFacade().alterar(getVagasVO(), getUsuarioLogado());
                getFacadeFactory().getVagasFacade().gravarSituacao(getVagasVO(), getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "VagasControle", "Finalizando Alterar Vagas", "Alterando");
            }
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
            getListaConsulta().clear();
            consultarVagasVisaoParceiroAoEntrar();
            return "vagasVisaoParceiroCons";
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public String ativar() {
        try {
            getVagasVO().setSituacao("AT");
            getFacadeFactory().getVagasFacade().gravarSituacao(getVagasVO(), getUsuarioLogado());
            setMensagemID("msg_ativar_dados");
            return "";
        } catch (ConsistirException e) {
            getVagasVO().setSituacao("EC");
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
            return "";
        } catch (Exception e) {
            getVagasVO().setSituacao("EC");
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public void realizarVerificacaoEnvioEmailVagaDisponivel() {
        try {
            //if (getVagasVO().getCidade() == null || getVagasVO().getCidade().getCodigo() == null || getVagasVO().getCidade().getCodigo() == 0) {
            PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOVA_VAGA_BANCO_CURRICULUM, false, null);
            if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
                setMensagemTemplate(mensagemTemplate);
                setMensagemID("msg_ativar_dados");
            } else {
                throw new Exception("Não há uma mensagem personalizada padrão definida para envio! Cadastre e habilite a mesma para que seja possível enviar o comunicado!");
            }
            setMensagemID("msg_email_enviado", Uteis.SUCESSO);
            //}
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void realizarEnvioEmailVagaDisponivel() {
        try {
            getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNovaVagaBancoCurriculum(getVagasVO(), getEnviarEmailSomenteParaAlunoDaCidade(), getUsuarioLogado(), getMensagemTemplate(), getPessoaVOs());
            setMensagemID("msg_email_enviado", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public String getAbrirModalDefinicaoEmailVagaDisponivel() {
        //if (getVagasVO().getCidade() == null || getVagasVO().getCidade().getCodigo() == null || getVagasVO().getCidade().getCodigo() == 0) {
        //    return "";
        //}
        setListaEstadoSugerir(null);
        setListaCidadeSugerir(null);
        return "RichFaces.$('modalEmailVagaDisponivel').show()";
    }

    public void limparConsultarAlunosNotificar() {
        setPessoaVOs(null);
    }

    public void consultarAlunosNotificar() {
        try {
            List<PessoaVO> pessoaVOs = getFacadeFactory().getPessoaFacade().consultarPessoaInteresseAreaProfissinal(getListaAreaProfissionalSugerir(), getListaCidadeSugerir(), getListaEstadoSugerir(), getCurso());
            setPessoaVOs(pessoaVOs);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public String cancelar() {
        try {
            getVagasVO().setSituacao("CA");
            getFacadeFactory().getVagasFacade().gravarSituacao(getVagasVO(), getUsuarioLogado());
            setMensagemID("msg_cancelar_dados");
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public String encerrarGrupo() {
        try {
            Boolean erro = false;
            if (getListaConsulta().isEmpty() || validarLista(getListaConsulta())) {
                throw new Exception("É necessário selecionar ao menos 1 (um) item para realizar o encerramento!");
            }
            getFacadeFactory().getVagasFacade().alterarSituacaoEDataDeVagasEncerrada(getListaConsulta(), getUsuarioLogado());
            erro = enviarEmailVagaEncerrada(getListaConsulta());
            consultarVagasVisaoParceiroAoEntrar();
            if (erro) {
                setMensagemID("msg_textoPadraoBancoCurriculum_inexistente");
            } else {
                setMensagemID("msg_cancelar_encerrado");
            }
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public List getTipoOrdenacao() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("data", "Data de Ativação da Vaga"));
        itens.add(new SelectItem("empresa", "Nome da Empresa"));
        itens.add(new SelectItem("codigoVaga", "Código da Vaga"));
        return itens;
    }

    private Boolean enviarEmailVagaEncerrada(List listaVagas) throws Exception {
        ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getUnidadeEnsinoLogado().getCodigo());
        Boolean erroParceiro = false;
        Boolean erroCandidato = false;
        String corpoMensagem = getFacadeFactory().getTextoPadraoBancoCurriculumFacade().consultarPorTipoUnica("vagaEncerradaParceiro", false, "AT", Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()).getTexto();
        String corpoMensagem2 = getFacadeFactory().getTextoPadraoBancoCurriculumFacade().consultarPorTipoUnica("vagaEncerradaCandidato", false, "AT", Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()).getTexto();
        if (corpoMensagem.equals("")) {
            erroParceiro = true;
        }
        if (corpoMensagem2.equals("")) {
            erroCandidato = true;
        }
        for (Object object : listaVagas) {
            VagasVO vaga = (VagasVO) object;
            if (vaga.getSituacao().equals("EN")) {
                corpoMensagem = substituirTags(corpoMensagem, null, vaga, config);
                enviarEmailParceiro(vaga.getParceiro(), corpoMensagem);
                List<CandidatosVagasVO> listaCandidatos = new ArrayList<CandidatosVagasVO>(0);
                getFacadeFactory().getVagasFacade().realizarNavegacaoParaVisualizarCandidatos(listaCandidatos, vaga);
                for (CandidatosVagasVO candidatosVagasVO : listaCandidatos) {
                    corpoMensagem2 = substituirTags(corpoMensagem2, candidatosVagasVO, vaga, config);
                    enviarEmailCandidato(candidatosVagasVO, corpoMensagem2, "Vaga Encerrada");
                    corpoMensagem2 = getFacadeFactory().getTextoPadraoBancoCurriculumFacade().consultarPorTipoUnica("vagaEncerradaCandidato", false, "AT", Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()).getTexto();
                }
            }
            corpoMensagem = getFacadeFactory().getTextoPadraoBancoCurriculumFacade().consultarPorTipoUnica("vagaEncerradaParceiro", false, "AT", Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()).getTexto();
        }
        return erroCandidato || erroParceiro;
    }

    public boolean validarLista(List listaConsulta) {
        Iterator i = listaConsulta.iterator();
        while (i.hasNext()) {
            VagasVO v = (VagasVO) i.next();
            if (v.getEncerrar()) {
                return false;
            }
        }
        return true;
    }

    public String realizarEncerramentoVaga() {
        try {
            Boolean erro = false;
            if (getVagasVO().getMotivoEncerramento().equals("")) {
                throw new Exception("O campo MOTIVO DE ENCERRAMENTO deve ser informado.");
            }
            if (getVagasVO().getMotivoEncerramento().equals("alunoInstituicaoContratado") && getVagasVO().getAlunoContratado().equals("")) {
                throw new Exception("O campo ALUNO deve ser informado.");
            }
            getVagasVO().setSituacao("EN");
            getFacadeFactory().getVagasFacade().gravarSituacao(getVagasVO(), getUsuarioLogado());
            getFacadeFactory().getVagasFacade().alterar(getVagasVO(), getUsuarioLogado());
            List listaVagas = new ArrayList(0);
            listaVagas.add(getVagasVO());
            erro = enviarEmailVagaEncerrada(listaVagas);
            if (erro) {
                setMensagemID("msg_textoPadraoBancoCurriculum_inexistente");
            } else {
                setMensagemID("msg_cancelar_encerrado");
            }
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public String realizarExpiracaoVaga() {
        try {
            getVagasVO().setSituacao("EX");
            getFacadeFactory().getVagasFacade().gravarSituacao(getVagasVO(), getUsuarioLogado());
            setMensagemID("msg_cancelar_expirado");
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public String realizarNavegacaoParaVisualizarCandidatos() {
        try {
            VagasVO obj = (VagasVO) context().getExternalContext().getRequestMap().get("vagasItens");
            if (obj != null) {
                setVagasVO(obj);
            }
            getFacadeFactory().getVagasFacade().realizarNavegacaoParaVisualizarCandidatos(getListaCandidatosVagasVOs(), getVagasVO());
            return "visualizarCandidato";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public void visualizarCurriculumPortifolio() {
        try {
            setCandidatosVagasVO(new CandidatosVagasVO());
            CandidatosVagasVO obj = (CandidatosVagasVO) context().getExternalContext().getRequestMap().get("candidatosVagasVO");
            if (obj == null) {
                BuscaCandidatoVagaVO buscaCandidatoVagaVO = (BuscaCandidatoVagaVO) context().getExternalContext().getRequestMap().get("buscaCandidatoVagaItens");
                obj = buscaCandidatoVagaVO.getCandidatosVagas();
                obj.setPessoa(buscaCandidatoVagaVO.getPessoa());
            }
            obj.getPessoa().setCurriculumPessoaVOs(getFacadeFactory().getCurriculumPessoaFacade().consultarPorPessoa(obj.getPessoa().getCodigo()));
            setCandidatosVagasVO(obj);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }

    }

    public String getCaminhoServidorDownloadCurriculum() {
        try {
            CurriculumPessoaVO obj = (CurriculumPessoaVO) getRequestMap().get("curriculumPessoaItens");
            if (obj.isNovoObj()) {
                return "abrirPopup('" + getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.CURRICULUM_TMP.getValue() + "/" + obj.getNomeRealArquivo() + "', '" + obj.getNomeRealArquivo() + "', 800, 600)";
            }
            return "abrirPopup('" + getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.CURRICULUM.getValue() + "/" + obj.getNomeRealArquivo() + "', '" + obj.getNomeRealArquivo() + "', 800, 600)";
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
        return "";
    }

    public String realizarNavegacaoParaVagaVisaoParceiro() {
        try {
            getListaCandidatosVagasVOs().clear();
            if (getListaConsulta().isEmpty()) {
                // volta pra tela de vagasVisaParceiroForm
                return "vagasVisaoParceiro";
            }
            // volta pra tela de vagasVisaParceiroCons
            return Uteis.getCaminhoRedirecionamentoNavegacao("vagasCons.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public void realizarGravacaoSituacaoCandidatoVaga() {
        try {
            CandidatosVagasVO obj = (CandidatosVagasVO) context().getExternalContext().getRequestMap().get("candidatosVagasVO");
            getFacadeFactory().getVagasFacade().realizarAlteracaoCandidatarVaga(obj, getUsuarioLogado());
            String corpoMensagem = "";
            if (obj.getSituacaoReferenteVaga().equals(SituacaoReferenteVagaEnum.SELECIONADO)) {
                corpoMensagem = getFacadeFactory().getTextoPadraoBancoCurriculumFacade().consultarPorTipoUnica("candidatoSelecionado", false, "AT", Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()).getTexto();
                corpoMensagem = substituirTags(corpoMensagem, obj, new VagasVO(), getConfiguracaoGeralPadraoSistema());
                enviarEmailCandidato(obj, corpoMensagem, "Currículo selecionado");
            } else if (obj.getSituacaoReferenteVaga().equals(SituacaoReferenteVagaEnum.DESQUALIFICADO)) {
                corpoMensagem = getFacadeFactory().getTextoPadraoBancoCurriculumFacade().consultarPorTipoUnica("candidatoDesclassificado", false, "AT", Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()).getTexto();
                corpoMensagem = substituirTags(corpoMensagem, obj, new VagasVO(), getConfiguracaoGeralPadraoSistema());
                enviarEmailCandidato(obj, corpoMensagem, "Currículo desclassificado");
            }
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    private String substituirTags(String corpoMensagem, CandidatosVagasVO candidatosVagasVO, VagasVO vaga, ConfiguracaoGeralSistemaVO config) throws Exception {
        if (corpoMensagem.contains("#Titulo_BancoCurriculum")) {
            corpoMensagem = corpoMensagem.replaceAll("#Titulo_BancoCurriculum", config.getTituloTelaBancoCurriculum());
        }
        if (corpoMensagem.contains("#Nome_Empresa")) {
            if (vaga.getCodigo() != 0) {
                corpoMensagem = corpoMensagem.replaceAll("#Nome_Empresa", vaga.getParceiro().getNome());
            } else {
                corpoMensagem = corpoMensagem.replaceAll("#Nome_Empresa", candidatosVagasVO.getVaga().getParceiro().getNome());
            }
        }
        if (corpoMensagem.contains("#Telefone_Empresa")) {
            if (vaga.getCodigo() != 0) {
                corpoMensagem = corpoMensagem.replaceAll("#Telefone_Empresa", vaga.getParceiro().getTelComercial1());
            } else {
                corpoMensagem = corpoMensagem.replaceAll("#Telefone_Empresa", candidatosVagasVO.getVaga().getParceiro().getTelComercial1());
            }
        }
        if (corpoMensagem.contains("#Cargo_Vaga")) {
            if (vaga.getCodigo() != 0) {
                corpoMensagem = corpoMensagem.replaceAll("#Cargo_Vaga", vaga.getCargo());
            } else {
                corpoMensagem = corpoMensagem.replaceAll("#Cargo_Vaga", candidatosVagasVO.getVaga().getCargo());
            }
        }
        if (corpoMensagem.contains("#Nome_Aluno")) {
            corpoMensagem = corpoMensagem.replaceAll("#Nome_Aluno", candidatosVagasVO.getPessoa().getNome());
        }
        if (corpoMensagem.contains("<!DOCTYPE")) {
            corpoMensagem = corpoMensagem.replace(corpoMensagem.substring(corpoMensagem.indexOf("<!DOCTYPE"), corpoMensagem.indexOf("<html>")), "");
        }
        return corpoMensagem;
    }

    private void enviarEmailCandidato(CandidatosVagasVO obj, String corpoMensagem, String assunto) throws Exception {
        ComunicacaoInternaVO comunicacaoInternaVO = null;
        try {
            comunicacaoInternaVO = new ComunicacaoInternaVO();
            comunicacaoInternaVO.setAssunto(assunto);
            List<ComunicadoInternoDestinatarioVO> listaComunicadoInternoDestinatarioVO = new ArrayList<ComunicadoInternoDestinatarioVO>(0);
            ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
            comunicadoInternoDestinatarioVO.setEmail(obj.getPessoa().getEmail());
            comunicadoInternoDestinatarioVO.setNome(obj.getPessoa().getNome());
            comunicadoInternoDestinatarioVO.getDestinatario().setNome(obj.getPessoa().getNome());
            comunicadoInternoDestinatarioVO.getDestinatario().setEmail(obj.getPessoa().getEmail());
            listaComunicadoInternoDestinatarioVO.add(comunicadoInternoDestinatarioVO);
            comunicacaoInternaVO.setComunicadoInternoDestinatarioVOs(listaComunicadoInternoDestinatarioVO);
            comunicacaoInternaVO.setTipoDestinatario("PA");
            ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getUnidadeEnsinoLogado().getCodigo());
            PessoaVO responsavel = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarResponsavelPadraoComunicadoInternoPorCodigoConfiguracoes(config.getResponsavelPadraoComunicadoInterno().getCodigo());
            comunicacaoInternaVO.setResponsavel(responsavel);
            comunicacaoInternaVO.setMensagem(corpoMensagem);
            getFacadeFactory().getEmailFacade().realizarGravacaoEmail(comunicacaoInternaVO, new ArrayList(0), true, getUsuarioLogado(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    private void enviarEmailParceiro(ParceiroVO obj, String corpoMensagem) throws Exception {
        ComunicacaoInternaVO comunicacaoInternaVO = null;
        try {
            comunicacaoInternaVO = new ComunicacaoInternaVO();
            comunicacaoInternaVO.setAssunto("Vaga Encerrada");
            List<ComunicadoInternoDestinatarioVO> listaComunicadoInternoDestinatarioVO = new ArrayList<ComunicadoInternoDestinatarioVO>(0);
            ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
            comunicadoInternoDestinatarioVO.setEmail(obj.getEmail());
            comunicadoInternoDestinatarioVO.getDestinatario().setNome(obj.getNome());
            comunicadoInternoDestinatarioVO.getDestinatario().setEmail(obj.getEmail());
            listaComunicadoInternoDestinatarioVO.add(comunicadoInternoDestinatarioVO);
            comunicacaoInternaVO.setComunicadoInternoDestinatarioVOs(listaComunicadoInternoDestinatarioVO);
            comunicacaoInternaVO.setTipoDestinatario("PA");
            ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getUnidadeEnsinoLogado().getCodigo());
            PessoaVO responsavel = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarResponsavelPadraoComunicadoInternoPorCodigoConfiguracoes(config.getResponsavelPadraoComunicadoInterno().getCodigo());
            comunicacaoInternaVO.setResponsavel(responsavel);
            comunicacaoInternaVO.setMensagem(corpoMensagem);
            getFacadeFactory().getEmailFacade().realizarGravacaoEmail(comunicacaoInternaVO, new ArrayList(0), true, getUsuarioLogado(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    public String realizarAbrirRichCurriculumAluno() {
        try {
            CandidatosVagasVO obj = (CandidatosVagasVO) context().getExternalContext().getRequestMap().get("candidatosVagasVO");
            obj.setPessoa(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            context().getExternalContext().getSessionMap().put("candidatosVagasVO", obj);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
        return "panelCurriculum";
    }

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
            if (getCampoConsultaCidade().equals("estado")) {
                objs = getFacadeFactory().getCidadeFacade().consultarPorSiglaEstado(getValorConsultaCidade(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }

            setListaConsultaCidade(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaCidade(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void selecionarCidade() {
        CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItens");
        getVagasVO().setCidade(obj);
        listaConsultaCidade.clear();
        this.setValorConsultaCidade("");
        this.setCampoConsultaCidade("");
    }

    public List getTipoConsultaCidade() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("estado", "Estado"));
        return itens;
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP
     * VagasCons.jsp. Define o tipo de consulta a ser executada, por meio de
     * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
     * resultado, disponibiliza um List com os objetos selecionados na sessao da
     * pagina.
     */
    public String consultar() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "VagasControle", "Inicializando Consultar Vagas", "Consultando");
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("numeroVagas")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getVagasFacade().consultarPorNumeroVagas(new Integer(valorInt), getOrdenacao(), "", null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("cargo")) {

                objs = getFacadeFactory().getVagasFacade().consultarPorCargo(getControleConsulta().getValorConsulta(), getOrdenacao(), "", null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());

            }
            if (getControleConsulta().getCampoConsulta().equals("parceiro")) {

                objs = getFacadeFactory().getVagasFacade().consultarPorParceiro(getControleConsulta().getValorConsulta(), getOrdenacao(), "", true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());

            }
            if (getControleConsulta().getCampoConsulta().equals("areaProfissional")) {

                objs = getFacadeFactory().getVagasFacade().consultarPorAreaProfissional(getControleConsulta().getValorConsulta(), getOrdenacao(), "", null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());

            }
            if (getControleConsulta().getCampoConsulta().equals("salario")) {
                objs = getFacadeFactory().getVagasFacade().consultarPorSalario(getControleConsulta().getValorConsulta(), getOrdenacao(), "", null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());

            }
            if (getControleConsulta().getCampoConsulta().equals("situacao")) {
                objs = getFacadeFactory().getVagasFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), getOrdenacao(), null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            registrarAtividadeUsuario(getUsuarioLogado(), "VagasControle", "Finalizando Consultar Vagas", "Consultando");
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("vagasCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("vagasCons.xhtml");
        }
    }

    public String consultarVagasVisaoParceiroAoEntrar() {
        getControleConsulta().setCampoConsulta("situacao");
        getControleConsulta().setValorConsulta("AT");
        setOrdenacao("data");
        setControleAbasPrincipal("dbAba1");
        return consultarVagasVisaoParceiro();
    }

    public String consultarVagasVisaoParceiro() {
        try {
            if (getUsuarioLogado() == null) {
                return "";
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "VagasControle", "Inicializando Consultar Vagas Visão Parceiro", "Consultando");

            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("numeroVagas")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getVagasFacade().consultarPorNumeroVagas(new Integer(valorInt), getOrdenacao(), "", getUsuarioLogado().getParceiro().getCodigo(), !getUsuarioLogado().getIsApresentarVisaoAlunoOuPais(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("cargo")) {
                objs = getFacadeFactory().getVagasFacade().consultarPorCargo(getControleConsulta().getValorConsulta(), getOrdenacao(), "", getUsuarioLogado().getParceiro().getCodigo(), !getUsuarioLogado().getIsApresentarVisaoAlunoOuPais(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("areaProfissional")) {
                objs = getFacadeFactory().getVagasFacade().consultarPorAreaProfissional(getControleConsulta().getValorConsulta(), getOrdenacao(), "", getUsuarioLogado().getParceiro().getCodigo(), !getUsuarioLogado().getIsApresentarVisaoAlunoOuPais(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("salario")) {
                objs = getFacadeFactory().getVagasFacade().consultarPorSalario(getControleConsulta().getValorConsulta(), getOrdenacao(), "", getUsuarioLogado().getParceiro().getCodigo(), !getUsuarioLogado().getIsApresentarVisaoAlunoOuPais(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("situacao")) {
                objs = getFacadeFactory().getVagasFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), getOrdenacao(), getUsuarioLogado().getParceiro().getCodigo(), !getUsuarioLogado().getIsApresentarVisaoAlunoOuPais(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            registrarAtividadeUsuario(getUsuarioLogado(), "VagasControle", "Finalizando Consultar Vagas Visão Parceiro", "Consultando");
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("vagasCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("vagasCons.xhtml");
        }
    }

    public String consultarVagasVisaoParceiroPorSituacao(String situacao) {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            objs = getFacadeFactory().getVagasFacade().consultarPorCargo("", getOrdenacao(), situacao, getUsuarioLogado().getParceiro().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("vagasCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("vagasCons.xhtml");
        }
    }

    public String consultarVagasAtivas() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("numeroVagas")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getVagasFacade().consultarPorNumeroVagas(new Integer(valorInt), getOrdenacao(), "AT", null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("cargo")) {
                objs = getFacadeFactory().getVagasFacade().consultarPorCargo(getControleConsulta().getValorConsulta(), getOrdenacao(), "AT", null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("parceiro")) {
                objs = getFacadeFactory().getVagasFacade().consultarPorParceiro(getControleConsulta().getValorConsulta(), getOrdenacao(), "AT", true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("areaProfissional")) {
                objs = getFacadeFactory().getVagasFacade().consultarPorAreaProfissional(getControleConsulta().getValorConsulta(), getOrdenacao(), "AT", null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("salario")) {
                objs = getFacadeFactory().getVagasFacade().consultarPorSalario(getControleConsulta().getValorConsulta(), getOrdenacao(), "AT", null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("situacao")) {
                objs = getFacadeFactory().getVagasFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), getOrdenacao(), null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("vagasCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("vagasCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe
     * <code>ParceiroVO</code> Após a exclusão ela automaticamente aciona a
     * rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "VagasControle", "Inicializando Excluir Vagas", "Excluindo");
            getFacadeFactory().getVagasFacade().excluir(getVagasVO(), getUsuarioLogado());
            setVagasVO(new VagasVO());
            registrarAtividadeUsuario(getUsuarioLogado(), "VagasControle", "Finalizando Consultar Vagas", "Excluindo");
            setMensagemID("msg_dados_excluidos");
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public List getListaSelectItemTipoVagas() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoVagas = new Hashtable();
        Enumeration keys = tipoVagas.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoVagas.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoMotivoEncerramento() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("alunoInstituicaoContratado", "Aluno da Instituição de Ensino Contratado"));
        itens.add(new SelectItem("outroContratado", "Outro Contratado"));
        itens.add(new SelectItem("vagaAdiada", "Vaga Adiada"));
        itens.add(new SelectItem("vagaCancelada", "Vaga Cancelada"));
        return itens;
    }

    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("parceiro", "Empresa"));
        itens.add(new SelectItem("numeroVagas", "Número de Vagas"));
        itens.add(new SelectItem("salario", "Salário"));
        itens.add(new SelectItem("cargo", "Cargo"));
        itens.add(new SelectItem("situacao", "Situação"));
        return itens;
    }

    public List getTipoConsultaComboVisaoParceiro() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("numeroVagas", "Número de Vagas"));
        itens.add(new SelectItem("salario", "Salário"));
        itens.add(new SelectItem("cargo", "Cargo"));
        itens.add(new SelectItem("situacao", "Situação"));
        return itens;
    }

    public List getTipoConsultaComboSalario() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("À Combinar", "À combinar"));
        itens.add(new SelectItem("Até R$999", "Até R$999,00"));
        itens.add(new SelectItem("R$1000 à R$1999", "De R$ 1.000,00 até R$ 1.999,00"));
        itens.add(new SelectItem("R$2000 à R$2999", "De R$ 2.000,00 até R$ 2.999,00"));
        itens.add(new SelectItem("R$3000 à R$3999", "De R$ 3.000,00 até R$ 3.999,00"));
        itens.add(new SelectItem("R$4000 à R$4999", "De R$ 4.000,00 até R$ 4.999,00"));
        itens.add(new SelectItem("R$5000 à R$5999", "De R$ 5.000,00 até R$ 5.999,00"));
        itens.add(new SelectItem("acima de R$6000", "Acima de R$ 6.000,00"));
        return itens;
    }

    public List getTipoConsultaComboSituacao() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("AT", "Aberta"));
        // itens.add(new SelectItem("CA", "Cancelada"));
        itens.add(new SelectItem("EX", "Expirada"));
        itens.add(new SelectItem("EN", "Encerrada"));
        // if (getUsuarioLogado().getParceiro().getCodigo() == 0) {
        itens.add(new SelectItem("EC", "Em análise"));
        // } else if (getUsuarioLogado().getPerfilAdministrador()) {
        // itens.add(new SelectItem("EC", "Em análise"));
        // }
        return itens;

    }

    public void adicionarAreaProfissional() {
        try {
            AreaProfissionalVO obj = (AreaProfissionalVO) context().getExternalContext().getRequestMap().get("areaProfissionalItens");
            getVagasVO().setAreaProfissional(obj);
            if (!getVagasVO().getListaAreaProfissional().contains(obj)) {
                getVagasVO().getListaAreaProfissional().add(getVagasVO().getAreaProfissional());
            }
            getListaConsultaAreaProfissional().remove(obj);
            getVagasVO().setAreaProfissional(new AreaProfissionalVO());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void removerAreaProfissional() {
        AreaProfissionalVO areaProfissional = (AreaProfissionalVO) context().getExternalContext().getRequestMap().get("areaProfissionalItens");
        for (AreaProfissionalVO obj : getVagasVO().getListaAreaProfissional()) {
            if (obj.getDescricaoAreaProfissional().equals(areaProfissional.getDescricaoAreaProfissional())) {
                getVagasVO().getListaAreaProfissional().remove(areaProfissional);
                return;
            }
        }
    }

    public void adicionarEstadoSugerir() {
        try {
            if (getEstado().getCodigo().intValue() == 0) {
                throw new Exception("Deve ser informado o estado, para adicionar!");
            } else {
                EstadoVO est = new EstadoVO();
                est = getFacadeFactory().getEstadoFacade().consultarPorChavePrimaria(getEstado().getCodigo().intValue(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                getListaEstadoSugerir().add(est);
            }
            setEstado(null);
            montarListaSelectItemCidadeSugerir();
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void adicionarAreaProfissionalSugerir() {
    	try {
    		if (getAreaProfissional().getCodigo().intValue() == 0) {
    			throw new Exception("Deve ser informado a área profissional, para adicionar!");
    		} else {
    			AreaProfissionalVO area = new AreaProfissionalVO();
    			area = getFacadeFactory().getAreaProfissionalFacade().consultarPorChavePrimaria(getAreaProfissional().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
    			getListaAreaProfissionalSugerir().add(area);
    		}
    		setAreaProfissional(null);
    		//montarListaSelectItemCidadeSugerir();
    		setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
    	} catch (Exception e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }
    
    public String getApresentarListaEstadoSugerir() {
        if (!getListaEstadoSugerir().isEmpty()) {
            String estados = "";
            Iterator i = getListaEstadoSugerir().iterator();
            while (i.hasNext()) {
                EstadoVO est = (EstadoVO)i.next();
                estados += est.getSigla() + "; ";
            }
            return estados;
        } else {
            return "";
        }
    }

    public String getApresentarListaAreaProfissionalSugerir() {
    	if (!getListaAreaProfissionalSugerir().isEmpty()) {
    		String areaProfissionals = "";
    		Iterator i = getListaAreaProfissionalSugerir().iterator();
    		while (i.hasNext()) {
    			AreaProfissionalVO est = (AreaProfissionalVO)i.next();
    			areaProfissionals += est.getDescricaoAreaProfissional() + "; ";
    		}
    		return areaProfissionals;
    	} else {
    		return "";
    	}
    }
    
    public String getApresentarListaCidadeSugerir() {
        if (!getListaCidadeSugerir().isEmpty()) {
            String cidades = "";
            Iterator i = getListaCidadeSugerir().iterator();
            while (i.hasNext()) {
                CidadeVO cid = (CidadeVO)i.next();
                cidades += cid.getNome() + "; ";
            }
            return cidades;
        } else {
            return "";
        }
    }

    public void removerEstadoSugerir() {
        setListaEstadoSugerir(null);
        montarListaSelectItemCidadeSugerir();
    }
    
    public void removerAreaProfissionalSugerir() {
    	getListaAreaProfissionalSugerir().clear();
    }

    public void adicionarCidadeSugerir() {
        try {
            if (getCidade().getCodigo().intValue() == 0) {
                throw new Exception("Deve ser informado a cidade, para adicionar!");
            } else {
                CidadeVO cid = new CidadeVO();
                cid = getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(getCidade().getCodigo().intValue(), false, getUsuarioLogado());
                getListaCidadeSugerir().add(cid);
            }
            setEstado(null);
            setCidade(null);
            montarListaSelectItemCidadeSugerir();
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void removerCidadeSugerir() {
        setListaCidadeSugerir(null);
//        AreaProfissionalVO areaProfissional = (AreaProfissionalVO) context().getExternalContext().getRequestMap().get("areaProfissional");
//        for (AreaProfissionalVO obj : getVagasVO().getListaAreaProfissional()) {
//            if (obj.getDescricaoAreaProfissional().equals(areaProfissional.getDescricaoAreaProfissional())) {
//                getVagasVO().getListaAreaProfissional().remove(areaProfissional);
//                return;
//            }
//        }
    }

    public void limparDadosAreaProfissional() {
        getVagasVO().getListaAreaProfissional().clear();
    }

    public Integer getColumnAreaProfissional() {
        if (getVagasVO().getListaAreaProfissional().size() > 4) {
            return 4;
        }
        return getVagasVO().getListaAreaProfissional().size();

    }

    public Integer getElementAreaProfissional() {
        return getVagasVO().getListaAreaProfissional().size();
    }

    public List getTipoConsultaComboTipoVeiculo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("CA", "Carro"));
        itens.add(new SelectItem("MO", "Moto"));

        return itens;

    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes
     * de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");

        return Uteis.getCaminhoRedirecionamentoNavegacao("vagasCons.xhtml");

    }

    public void consultarParceiro() {
        try {
            List objs = new ArrayList(0);

            if (getCampoConsultaParceiro().equals("nome")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorNomeBancoCurriculumTrue(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());

            }
            if (getCampoConsultaParceiro().equals("razaoSocial")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocialBancoCurriculumTrue(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());

            }
            if (getCampoConsultaParceiro().equals("RG")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorRGBancoCurriculumTrue(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());

            }
            if (getCampoConsultaParceiro().equals("CPF")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorCPFBancoCurriculumTrue(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());

            }
            setListaConsultaParceiro(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public List getTipoConsultaParceiro() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("razaoSocial", "Razão social"));
        itens.add(new SelectItem("RG", "RG"));
        itens.add(new SelectItem("CPF", "CPF"));
        return itens;

    }

    public void selecionarParceiro() {
        ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
        getVagasVO().setParceiro(obj);
        getVagasVO().setEmpresaSigilosaParaVaga(obj.getEmpresaSigilosaParaVaga());
        listaConsultaParceiro.clear();

        this.setValorConsultaParceiro("");

        this.setCampoConsultaParceiro("");

    }

    public void consultarAreaProfissional() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaAreaProfissional().equals("descricaoAreaProfissional")) {
                objs = getFacadeFactory().getAreaProfissionalFacade().consultarPorDescricaoAreaProfissionalAtivo(getValorConsultaAreaProfissional(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaAreaProfissional(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaAreaProfissional() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("descricaoAreaProfissional", "Descrição Profissional"));

        return itens;

    }

    public void pesquisarCadidatosVaga() {
        try {
            setListaBuscaCandidatoVagaVO(getFacadeFactory().getPessoaFacade().consultaRapidaBuscaCandidatoVagaPorVaga(getVagasVO()));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void pesquisarCadidatosVagaConsultar() {
        try {
            VagasVO obj = (VagasVO) context().getExternalContext().getRequestMap().get("vagasItens");
            setListaBuscaCandidatoVagaVO(getFacadeFactory().getPessoaFacade().consultaRapidaBuscaCandidatoVagaPorVaga(obj));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void realizarConsultaPessoa() {
        try {
            BuscaCandidatoVagaVO obj = (BuscaCandidatoVagaVO) context().getExternalContext().getRequestMap().get("buscaCandidatoVagaItens");
            obj.getCandidatosVagas().setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(obj.getPessoa().getCodigo(), false, true, false, getUsuarioLogado()));
            obj.getCandidatosVagas().getVaga().setCodigo(getVagasVO().getCodigo());
            context().getExternalContext().getSessionMap().put("candidatosVagasVO", obj.getCandidatosVagas());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarAluno() {
        try {
            List objs = new ArrayList(0);

            if (getValorConsultaAluno().equals("")) {
                throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                // MatriculaVO obj =
                // getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(),
                // this.getUnidadeEnsinoLogado().getCodigo(), true,
                // Uteis.NIVELMONTARDADOS_DADOSBASICOS);
                MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
                if (!obj.getMatricula().equals("")) {
                    objs.add(obj);
                }
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                // objs =
                // getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getValorConsultaAluno(),
                // this.getUnidadeEnsinoLogado().getCodigo(), false,
                // Uteis.NIVELMONTARDADOS_DADOSBASICOS);
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                // objs =
                // getFacadeFactory().getMatriculaFacade().consultarPorNomeCurso(getValorConsultaAluno(),
                // this.getUnidadeEnsinoLogado().getCodigo(), false,
                // Uteis.NIVELMONTARDADOS_DADOSBASICOS);
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }

            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarAluno() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
        // MatriculaVO objCompleto =
        // getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(),
        // obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO,
        // getUsuarioLogado());
        getVagasVO().setAlunoContratado(obj.getAluno().getNome());
        obj = null;
        // objCompleto = null;
        setValorConsultaAluno("");
        setCampoConsultaAluno("");
        getListaConsultaAluno().clear();
    }

    public void abrirBuscaAluno() throws Exception {
        VagasVO obj = (VagasVO) context().getExternalContext().getRequestMap().get("vagasItens");
        setVagasVO(obj);
    }

    public void selecionarAlunoEncerrar() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
        // MatriculaVO objCompleto =
        // getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(),
        // obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO,
        // getUsuarioLogado());
        getVagasVO().setAlunoContratado(obj.getAluno().getNome());
        obj = null;
        // objCompleto = null;
        setValorConsultaAluno("");
        setCampoConsultaAluno("");
        getListaConsultaAluno().clear();
    }

    public void limparDadosAluno() throws Exception {
        getVagasVO().setAlunoContratado("");
        setMensagemID("msg_entre_prmconsulta");
    }

    public List getTipoConsultaComboAluno() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public List getTipoComboNivelIngles() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("inicial", "Inicial"));
        itens.add(new SelectItem("intermediario", "Intermediário"));
        itens.add(new SelectItem("avancado", "Avançado"));
        return itens;
    }

    public List getTipoComboNivelFrances() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("inicial", "Inicial"));
        itens.add(new SelectItem("intermediario", "Intermediário"));
        itens.add(new SelectItem("avancado", "Avançado"));
        return itens;
    }

    public List getTipoComboNivelEspanhol() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("inicial", "Inicial"));
        itens.add(new SelectItem("intermediario", "Intermediário"));
        itens.add(new SelectItem("avancado", "Avançado"));
        return itens;
    }

    public void montarListaSelectItemAreaProfissional() {
        try {
            montarListaSelectItemAreaProfissional("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());
            ;
        }
    }

    public void montarListaSelectItemAreaProfissional(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarAreaProfissionalPorDescricao(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                AreaProfissionalVO obj = (AreaProfissionalVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricaoAreaProfissional()));
            }
            setListaSelectItemAreaProfissional(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List consultarAreaProfissionalPorDescricao(String prm) throws Exception {
        List lista = getFacadeFactory().getAreaProfissionalFacade().consultarPorDescricaoAreaProfissionalAtivo(prm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemCidade() {
        try {
//            if (getVagasVO().getEstado().getCodigo() != 0) {
//                setApresentarComboCidade(Boolean.TRUE);
//            } else {
//                setApresentarComboCidade(Boolean.FALSE);
//            }
            montarListaSelectItemCidade("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());
        }
    }

    public void montarListaSelectItemCidade(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarCidadePorEstado(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                CidadeVO obj = (CidadeVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemCidade(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List consultarCidadePorEstado(String prm) throws Exception {
	List lista = new ArrayList();
        lista.add(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(getVagasVO().getCidade().getCodigo(), false, getUsuarioLogado()));
        return lista;
    }

    public void montarListaSelectItemCidadeSugerir() {
        try {
            montarListaSelectItemCidadeSugerir("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());
        }
    }

    public void montarListaSelectItemCidadeSugerir(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarCidadePorEstadoSugerir(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                CidadeVO obj = (CidadeVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemCidadeSugerir(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List consultarCidadePorEstadoSugerir(String prm) throws Exception {
	List lista = getFacadeFactory().getCidadeFacade().consultarPorCodigoEstado(getEstado().getCodigo(), false, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemEstado() {
        try {
            montarListaSelectItemEstado("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());

        }
    }

    public void montarListaSelectItemEstado(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarEstadoPorSigla(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                EstadoVO obj = (EstadoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemEstado(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List consultarEstadoPorSigla(String prm) throws Exception {
        List lista = getFacadeFactory().getEstadoFacade().consultarPorCodigoPaiz(getConfiguracaoGeralPadraoSistema().getPaisPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
        return lista;
    }

    public void alterarOrdemVagaQuestao(DropEvent dropEvent) {
        try {
            if (dropEvent.getDragValue() instanceof VagaQuestaoVO && dropEvent.getDropValue() instanceof VagaQuestaoVO) {
                getFacadeFactory().getVagasFacade().alterarOrdemVagaQuestao(getVagasVO(), (VagaQuestaoVO) dropEvent.getDragValue(), (VagaQuestaoVO) dropEvent.getDropValue());
            }
            limparMensagem();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }

    }

    public void adicionarVagaQuestao() {
        try {
            getFacadeFactory().getVagasFacade().adicionarVagaQuestao(getVagasVO(), getVagaQuestaoVO());
            setVagaQuestaoVO(getFacadeFactory().getVagaQuestaoFacade().novo());
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void removerVagaQuestao() {
        try {
            getFacadeFactory().getVagasFacade().removerVagaQuestao(getVagasVO(), (VagaQuestaoVO) context().getExternalContext().getRequestMap().get("vagaQuestaoItens"));
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void editarVagaQuestao() {
        try {
            setVagaQuestaoVO(new VagaQuestaoVO());
            setVagaQuestaoVO((VagaQuestaoVO) context().getExternalContext().getRequestMap().get("vagaQuestaoItens"));
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void alterarOrdemOpcaoRespostaVagaQuestao(DropEvent dropEvent) {
        try {
            if (dropEvent.getDragValue() instanceof OpcaoRespostaVagaQuestaoVO && dropEvent.getDropValue() instanceof OpcaoRespostaVagaQuestaoVO) {
                getFacadeFactory().getVagaQuestaoFacade().alterarOrdemOpcaoRespostaVagaQuestao(getVagaQuestaoVO(), (OpcaoRespostaVagaQuestaoVO) dropEvent.getDragValue(), (OpcaoRespostaVagaQuestaoVO) dropEvent.getDropValue());
            }
            limparMensagem();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }

    }

    public void adicionarOpcaoRespostaVagaQuestao() {
        try {
            getFacadeFactory().getVagaQuestaoFacade().adicionarOrdemOpcaoRespostaVagaQuestao(getVagaQuestaoVO(), new OpcaoRespostaVagaQuestaoVO(), false);
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void removerOpcaoRespostaVagaQuestao() {
        try {
            getFacadeFactory().getVagaQuestaoFacade().removerOrdemOpcaoRespostaVagaQuestao(getVagaQuestaoVO(), (OpcaoRespostaVagaQuestaoVO) context().getExternalContext().getRequestMap().get("opcaoRespostaVagaQuestaoItens"));
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void editarOpcaoRespostaVagaQuestao() {
        try {
            ((OpcaoRespostaVagaQuestaoVO) context().getExternalContext().getRequestMap().get("opcaoRespostavagaQuestaoItens")).setEditar(true);
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void avancarEtapa2() {
        setControleAbas("etapa2");
    }

    public void avancarEtapa3() {
        setControleAbas("etapa3");
    }

    public void avancarEtapa4() {
        setControleAbas("etapa4");
    }

    public void avancarEtapa5() {
        setControleAbas("etapa5");
    }

    public void voltarEtapa3() {
        setControleAbas("etapa3");
    }

    public void voltarEtapa1() {
        setControleAbas("etapa1");
    }

    public void voltarEtapa2() {
        setControleAbas("etapa2");
    }

    public void voltarEtapa4() {
        setControleAbas("etapa4");
    }

    public void adicionarVagaContato() {
        try {
            getFacadeFactory().getVagasFacade().adicionarVagaContato(getVagasVO(), getVagaContato());
            setVagaContato(new VagaContatoVO());
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void removerVagaContato() {
        try {
            getFacadeFactory().getVagasFacade().removerVagaContato(getVagasVO(), (VagaContatoVO) context().getExternalContext().getRequestMap().get("vagaContatoItens"));
            setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void editarVagaContato() {
        try {
            getFacadeFactory().getVagasFacade().removerVagaContato(getVagasVO(), (VagaContatoVO) context().getExternalContext().getRequestMap().get("vagaContatoItens"));
            setVagaContato((VagaContatoVO) context().getExternalContext().getRequestMap().get("vagaContatoItens"));
            limparMensagem();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public String abrirModalAtualizacaoCurriculo() {
        if (getUsuarioLogado().getPessoa().getCurriculoAtualizado()) {
            VagasVO obj = (VagasVO) context().getExternalContext().getRequestMap().get("vaga");
            BuscaVagasControle busca = (BuscaVagasControle) getControlador("BuscaVagasControle");
            busca.getListaConsulta().clear();
            try {
                obj = getFacadeFactory().getVagasFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            } catch (Exception e) {
            }
            busca.getListaConsulta().add(obj);
            //context().getExternalContext().getSessionMap().put("vaga", obj);
        }
        return "buscaVagas";
    }

    public void carregarListaEstadoVaga() {
        try {
            if (!getVagasVO().getVagaEstadoVOs().isEmpty()) {
                List listaEstado = getListaConsultaEstado();
                Iterator i = listaEstado.iterator();
                while (i.hasNext()) {
                    VagaEstadoVO vagaEst = (VagaEstadoVO) i.next();
                    Iterator j = getVagasVO().getVagaEstadoVOs().iterator();
                    while (j.hasNext()) {
                        VagaEstadoVO vagEstAtual = (VagaEstadoVO) j.next();
                        if (vagEstAtual.getEstado().getCodigo().intValue() == vagaEst.getEstado().getCodigo().intValue()) {
                            vagaEst.setSelecionado(Boolean.TRUE);
                        }
//                        else {
//                            vagaEst.setSelecionado(Boolean.FALSE);
//                        }
                    }
                }
            }
        } catch (Exception e) {
            setListaConsultaEstado(null);
        }
    }

    public void carregarListaAreaVaga() {
        try {
            if (!getVagasVO().getVagaAreaVOs().isEmpty()) {
                List listaEstado = getListaConsultaArea();
                Iterator i = listaEstado.iterator();
                while (i.hasNext()) {
                    VagaAreaVO vagaArea = (VagaAreaVO) i.next();
                    Iterator j = getVagasVO().getVagaAreaVOs().iterator();
                    while (j.hasNext()) {
                        VagaAreaVO vagAreaAtual = (VagaAreaVO) j.next();
                        if (vagAreaAtual.getAreaProfissional().getCodigo().intValue() == vagaArea.getAreaProfissional().getCodigo().intValue()) {
                            vagaArea.setSelecionado(Boolean.TRUE);
                        }
//                        else {
//                            vagaArea.setSelecionado(Boolean.FALSE);
//                        }
                    }
                }
            }
        } catch (Exception e) {
            setListaConsultaEstado(null);
        }
    }

    public void selecionarItemListaEstado() {
        VagaEstadoVO obj = (VagaEstadoVO) context().getExternalContext().getRequestMap().get("vagaEstadoItens");
    }

    public void selecionarItemListaArea() {
        VagaAreaVO obj = (VagaAreaVO) context().getExternalContext().getRequestMap().get("vagaAreaItens");
    }

//    public void carregarListaEstadoArea() {
//        try {
//            if (!getVagasVO().getVagaAreaVOs().isEmpty()) {
//                List listaEstado = getListaConsultaArea();
//                Iterator i = listaEstado.iterator();
//                while (i.hasNext()) {
//                    VagaAreaVO vagaArea = (VagaAreaVO)i.next();
//                    Iterator j = getVagasVO().getVagaAreaVOs().iterator();
//                    while (j.hasNext()) {
//                        VagaAreaVO vagAreaAtual = (VagaAreaVO)j.next();
//                        if (vagAreaAtual.getAreaProfissional().getCodigo().intValue() == vagaArea.getAreaProfissional().getCodigo().intValue()) {
//                            vagaArea.setSelecionado(Boolean.TRUE);
//                        } else {
//                            vagaArea.setSelecionado(Boolean.FALSE);
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            setListaConsultaArea(null);
//        }
//    }
    public void montarListaEstado() {
        try {
            if (getListaConsultaEstado().isEmpty()) {
                List listaEstado = getFacadeFactory().getEstadoFacade().consultarPorCodigoPaiz(getConfiguracaoGeralPadraoSistema().getPaisPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                Iterator i = listaEstado.iterator();
                while (i.hasNext()) {
                    VagaEstadoVO vagaEst = new VagaEstadoVO();
                    EstadoVO est = (EstadoVO) i.next();

                    vagaEst.setEstado(est);
                    if (getVagasVO().getCodigo().intValue() == 0) {
                        vagaEst.setSelecionado(Boolean.TRUE);
                    }
                    getListaConsultaEstado().add(vagaEst);
                }
            }
            //SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
            //Collections.sort((List) getListaConsultaEstado(), ordenador);
        } catch (Exception e) {
            setListaConsultaEstado(null);
        }
    }

    public void montarListaArea() {
        try {
            if (getListaConsultaArea().isEmpty()) {
                List listaEstado = getFacadeFactory().getAreaProfissionalFacade().consultarPorDescricaoAreaProfissionalAtivo("", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                Iterator i = listaEstado.iterator();
                while (i.hasNext()) {
                    VagaAreaVO vagaArea = new VagaAreaVO();
                    AreaProfissionalVO area = (AreaProfissionalVO) i.next();
                    vagaArea.setAreaProfissional(area);
                    getListaConsultaArea().add(vagaArea);
                }
            }
        } catch (Exception e) {
            setListaConsultaArea(null);
        }
    }

    public void selecionarTodosArea() {
        try {
            if (!getListaConsultaArea().isEmpty()) {
                Iterator i = getListaConsultaArea().iterator();
                while (i.hasNext()) {
                    VagaAreaVO area = (VagaAreaVO) i.next();
                    area.setSelecionado(Boolean.TRUE);
                }
            }
        } catch (Exception e) {
            setListaConsultaArea(null);
        }
    }

    public void desmarcarTodosArea() {
        try {
            if (!getListaConsultaArea().isEmpty()) {
                Iterator i = getListaConsultaArea().iterator();
                while (i.hasNext()) {
                    VagaAreaVO area = (VagaAreaVO) i.next();
                    area.setSelecionado(Boolean.FALSE);
                }
            }
        } catch (Exception e) {
            setListaConsultaArea(null);
        }
    }

    public void selecionarTodos() {
        try {
            if (!getListaConsultaEstado().isEmpty()) {
                Iterator i = getListaConsultaEstado().iterator();
                while (i.hasNext()) {
                    VagaEstadoVO est = (VagaEstadoVO) i.next();
                    est.setSelecionado(Boolean.TRUE);
                }
            }
        } catch (Exception e) {
            setListaConsultaEstado(null);
        }
    }

    public void desmarcarTodos() {
        try {
            if (!getListaConsultaEstado().isEmpty()) {
                Iterator i = getListaConsultaEstado().iterator();
                while (i.hasNext()) {
                    VagaEstadoVO est = (VagaEstadoVO) i.next();
                    est.setSelecionado(Boolean.FALSE);
                }
            }
        } catch (Exception e) {
            setListaConsultaEstado(null);
        }
    }

    public Integer getColumn() {
        if (getListaConsultaEstado().size() > 6) {
            return 6;
        }
        return getListaConsultaEstado().size();
    }

    public Integer getElement() {
        return getListaConsultaEstado().size();
    }

    public Integer getColumnArea() {
        if (getListaConsultaArea().size() > 6) {
            return 6;
        }
        return getListaConsultaArea().size();
    }

    public Integer getElementArea() {
        return getListaConsultaArea().size();
    }

    public void selecionarAreaProfissional() {
        AreaProfissionalVO obj = (AreaProfissionalVO) context().getExternalContext().getRequestMap().get("areaProfissionalItens");
        getVagasVO().setAreaProfissional(obj);
        listaConsultaAreaProfissional.clear();

        this.setValorConsultaAreaProfissional("");

        this.setCampoConsultaAreaProfissional("");
    }

    public VagasVO getVagasVO() {
        if (vagasVO == null) {
            vagasVO = new VagasVO();

        }
        return vagasVO;

    }

    public void setVagasVO(VagasVO VagasVO) {
        this.vagasVO = VagasVO;

    }

    public void irPaginaInicial() throws Exception {
        this.consultar();

    }

    public String getCampoConsultaParceiro() {
        if (campoConsultaParceiro == null) {
            campoConsultaParceiro = "";

        }
        return campoConsultaParceiro;

    }

    public void setCampoConsultaParceiro(String campoConsultaParceiro) {
        this.campoConsultaParceiro = campoConsultaParceiro;

    }

    public String getValorConsultaParceiro() {
        if (valorConsultaParceiro == null) {
            valorConsultaParceiro = "";

        }
        return valorConsultaParceiro;

    }

    public void setValorConsultaParceiro(String valorConsultaParceiro) {
        this.valorConsultaParceiro = valorConsultaParceiro;

    }

    public List getListaConsultaParceiro() {
        if (listaConsultaParceiro == null) {
            listaConsultaParceiro = new ArrayList(0);

        }
        return listaConsultaParceiro;

    }

    public void setListaConsultaParceiro(List listaConsultaParceiro) {
        this.listaConsultaParceiro = listaConsultaParceiro;

    }

    public List getListaCandidatosVagasVOs() {
        if (listaCandidatosVagasVOs == null) {
            listaCandidatosVagasVOs = new ArrayList(0);

        }
        return listaCandidatosVagasVOs;
    }

    public void setListaCandidatosVagasVOs(List listaCandidatosVagasVOs) {
        this.listaCandidatosVagasVOs = listaCandidatosVagasVOs;
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

    public List getListaConsultaAreaProfissional() {
        if (listaConsultaAreaProfissional == null) {
            listaConsultaAreaProfissional = new ArrayList(0);

        }
        return listaConsultaAreaProfissional;

    }

    public void setListaConsultaAreaProfissional(List listaConsultaAreaProfissional) {
        this.listaConsultaAreaProfissional = listaConsultaAreaProfissional;

    }

    public boolean getApresentarTipoVeiculo() {
        return getVagasVO().getNecessitaVeiculo();

    }

    public void consultarCurso() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCurso().equals("nome")) {
                objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNome(getValorConsultaCurso(), false,
                        Uteis.NIVELMONTARDADOS_TODOS, Boolean.FALSE, getUsuarioLogado());
            }
            setListaConsultaCurso(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCurso(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarCurso() throws Exception {
        try {
            CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
            setCurso(obj);
        } catch (Exception e) {
        }
    }

    public void limparCurso() throws Exception {
        try {
            setCurso(null);
        } catch (Exception e) {
        }
    }

    public List getTipoConsultaComboCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public boolean getAtivar() {
        if (getVagasVO().isNovoObj().booleanValue()) {
            return false;
        } else if (getVagasVO().getSituacao().equalsIgnoreCase("EC") || getVagasVO().getSituacao().equalsIgnoreCase("CA")
                // || getVagasVO().getSituacao().equalsIgnoreCase("EN")
                || getVagasVO().getSituacao().equalsIgnoreCase("EX")) {
            return true;
        }
        return false;
    }

    public boolean getCancelar() {
        if (getVagasVO().getSituacao().equalsIgnoreCase("AT")) {
            return true;
        }
        return false;
    }

    public boolean getPossibilidadeGravar() {
        if (getVagasVO().getSituacao().equalsIgnoreCase("EC")) {
            return true;

        } else if (getVagasVO().isNovoObj().booleanValue()) {
            return true;

        }
        return false;

    }

    public boolean getPesquisaPorSalario() {
        if (getControleConsulta().getCampoConsulta().equals("salario")) {
            return true;

        }
        return false;

    }

    public boolean getPesquisaPorSituacao() {
        if (getControleConsulta().getCampoConsulta().equals("situacao")) {
            return true;

        }
        return false;

    }

    public boolean getPesquisarNaoPorSalarioSituacao() {
        if (getPesquisaPorSalario()) {
            return false;
        } else if (getPesquisaPorSituacao()) {
            return false;
        }
        return true;

    }

    public boolean getAprensetarModalAluno() {
        if (getVagasVO().getMotivoEncerramento().equals("alunoInstituicaoContratado")) {
            return true;
        }
        return false;
    }

    public String getControleAbas() {
        if (controleAbas == null) {
            controleAbas = "";
        }
        return controleAbas;
    }

    public void setControleAbas(String controleAbas) {
        this.controleAbas = controleAbas;
    }

    public String getControleAbasPrincipal() {
        if (controleAbasPrincipal == null) {
            controleAbasPrincipal = "";
        }
        return controleAbasPrincipal;
    }

    public void setControleAbasPrincipal(String controleAbasPrincipal) {
        this.controleAbasPrincipal = controleAbasPrincipal;
    }

    public Boolean getApresentarCampoOutrosSoftwares() {
        return apresentarCampoOutrosSoftwares;
    }

    public void setApresentarCampoOutrosSoftwares(Boolean apresentarCampoOutrosSoftwares) {
        this.apresentarCampoOutrosSoftwares = apresentarCampoOutrosSoftwares;
    }

    public String getCampoConsultaAluno() {
        return campoConsultaAluno;
    }

    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
    }

    public List getListaConsultaAluno() {
        return listaConsultaAluno;
    }

    public void setListaConsultaAluno(List listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    public String getValorConsultaAluno() {
        return valorConsultaAluno;
    }

    public void setValorConsultaAluno(String valorConsultaAluno) {
        this.valorConsultaAluno = valorConsultaAluno;
    }

    public List getListaSelectItemAreaProfissional() {
        if (listaSelectItemAreaProfissional == null) {
            listaSelectItemAreaProfissional = new ArrayList(0);
        }
        return listaSelectItemAreaProfissional;
    }

    public void setListaSelectItemAreaProfissional(List listaSelectItemAreaProfissional) {
        this.listaSelectItemAreaProfissional = listaSelectItemAreaProfissional;
    }

    public List getListaSelectItemEstado() {
        if (listaSelectItemEstado == null) {
            listaSelectItemEstado = new ArrayList(0);
        }
        return listaSelectItemEstado;
    }

    public void setListaSelectItemEstado(List listaSelectItemEstado) {
        this.listaSelectItemEstado = listaSelectItemEstado;
    }

    public List getListaSelectItemCidade() {
        if (listaSelectItemCidade == null) {
            listaSelectItemCidade = new ArrayList(0);
        }
        return listaSelectItemCidade;
    }

    public void setListaSelectItemCidade(List listaSelectItemCidade) {
        this.listaSelectItemCidade = listaSelectItemCidade;
    }

    public Boolean getApresentarComboCidade() {
        if (apresentarComboCidade == null) {
            apresentarComboCidade = Boolean.FALSE;
        }
        return apresentarComboCidade;
    }

    public void setApresentarComboCidade(Boolean apresentarComboCidade) {
        this.apresentarComboCidade = apresentarComboCidade;
    }

    public String getDataExpiracao_Apresentar() throws Exception {
        return Uteis.getData(getDataExpiracao(), "dd/MM/yyyy");
    }

    public Date getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(Date dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public Boolean getVagaIsAtiva() {
        if (getVagasVO().getSituacao().equals("AT")) {
            return true;
        }
        return false;
    }

    public List<BuscaCandidatoVagaVO> getListaBuscaCandidatoVagaVO() {
        if (listaBuscaCandidatoVagaVO == null) {
            listaBuscaCandidatoVagaVO = new ArrayList(0);
        }
        return listaBuscaCandidatoVagaVO;
    }

    public void setListaBuscaCandidatoVagaVO(List<BuscaCandidatoVagaVO> listaBuscaCandidatoVagaVO) {
        this.listaBuscaCandidatoVagaVO = listaBuscaCandidatoVagaVO;
    }

    public Boolean getMostrarBotaoBuscaCandidatoVaga() {
        if (mostrarBotaoBuscaCandidatoVaga == null) {
            mostrarBotaoBuscaCandidatoVaga = Boolean.FALSE;
        }
        return mostrarBotaoBuscaCandidatoVaga;
    }

    public void setMostrarBotaoBuscaCandidatoVaga(Boolean mostrarBotaoBuscaCandidatoVaga) {
        this.mostrarBotaoBuscaCandidatoVaga = mostrarBotaoBuscaCandidatoVaga;
    }

    public List<VagasVO> getListaVagasRecentementeAtivadas() {
        if (listaVagasRecentementeAtivadas == null) {
            listaVagasRecentementeAtivadas = new ArrayList<VagasVO>(0);
        }
        return listaVagasRecentementeAtivadas;
    }

    public void setListaVagasRecentementeAtivadas(List<VagasVO> listaVagasRecentementeAtivadas) {
        this.listaVagasRecentementeAtivadas = listaVagasRecentementeAtivadas;
    }

    public String getOrdenacao() {
        if (ordenacao == null) {
            ordenacao = "";
        }
        return ordenacao;
    }

    public void setOrdenacao(String ordenacao) {
        this.ordenacao = ordenacao;
    }

    public VagaQuestaoVO getVagaQuestaoVO() {
        if (vagaQuestaoVO == null) {
            vagaQuestaoVO = new VagaQuestaoVO();
        }
        return vagaQuestaoVO;
    }

    public void setVagaQuestaoVO(VagaQuestaoVO vagaQuestaoVO) {
        this.vagaQuestaoVO = vagaQuestaoVO;
    }

    public List<SelectItem> getListaSelectItemTipoVagaQuestao() {
        if (listaSelectItemTipoVagaQuestao == null) {
            listaSelectItemTipoVagaQuestao = new ArrayList<SelectItem>(0);
            for (TipoVagaQuestaoEnum tipoVagaQuestaoEnum : TipoVagaQuestaoEnum.values()) {
                listaSelectItemTipoVagaQuestao.add(new SelectItem(tipoVagaQuestaoEnum, tipoVagaQuestaoEnum.getValorApresentar()));
            }
        }
        return listaSelectItemTipoVagaQuestao;
    }

    public Boolean getEnviarEmailSomenteParaAlunoDaCidade() {
        if (enviarEmailSomenteParaAlunoDaCidade == null) {
            enviarEmailSomenteParaAlunoDaCidade = false;
        }
        return enviarEmailSomenteParaAlunoDaCidade;
    }

    public void setEnviarEmailSomenteParaAlunoDaCidade(Boolean enviarEmailSomenteParaAlunoDaCidade) {
        this.enviarEmailSomenteParaAlunoDaCidade = enviarEmailSomenteParaAlunoDaCidade;
    }

    public VagaContatoVO getVagaContato() {
        if (vagaContato == null) {
            vagaContato = new VagaContatoVO();
        }
        return vagaContato;
    }

    public void setVagaContato(VagaContatoVO vagaContato) {
        this.vagaContato = vagaContato;
    }

    public CandidatosVagasVO getCandidatosVagasVO() {
        if (candidatosVagasVO == null) {
            candidatosVagasVO = new CandidatosVagasVO();
        }
        return candidatosVagasVO;
    }

    public void setCandidatosVagasVO(CandidatosVagasVO candidatosVagasVO) {
        this.candidatosVagasVO = candidatosVagasVO;
    }

    /**
     * @return the mensagemTemplate
     */
    public PersonalizacaoMensagemAutomaticaVO getMensagemTemplate() {
        if (mensagemTemplate == null) {
            mensagemTemplate = new PersonalizacaoMensagemAutomaticaVO();
        }
        return mensagemTemplate;
    }

    /**
     * @param mensagemTemplate the mensagemTemplate to set
     */
    public void setMensagemTemplate(PersonalizacaoMensagemAutomaticaVO mensagemTemplate) {
        this.mensagemTemplate = mensagemTemplate;
    }

    /**
     * @return the pessoaVOs
     */
    public int getTamanhoPessoaVOs() {
        return getPessoaVOs().size();
    }

    public List<PessoaVO> getPessoaVOs() {
        if (pessoaVOs == null) {
            pessoaVOs = new ArrayList<PessoaVO>();
        }
        return pessoaVOs;
    }

    /**
     * @param pessoaVOs the pessoaVOs to set
     */
    public void setPessoaVOs(List<PessoaVO> pessoaVOs) {
        this.pessoaVOs = pessoaVOs;
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

    public List getListaConsultaCurso() {
        if (listaConsultaCurso == null) {
            listaConsultaCurso = new ArrayList(0);
        }
        return listaConsultaCurso;
    }

    public void setListaConsultaCurso(List listaConsultaCurso) {
        this.listaConsultaCurso = listaConsultaCurso;
    }

    /**
     * @return the curso
     */
    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return curso;
    }

    /**
     * @param curso the curso to set
     */
    public void setCurso(CursoVO curso) {
        this.curso = curso;
    }

    /**
     * @return the estado
     */
    public EstadoVO getEstado() {
        if (estado == null) {
            estado = new EstadoVO();
        }
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(EstadoVO estado) {
        this.estado = estado;
    }

    /**
     * @return the cidade
     */
    public CidadeVO getCidade() {
        if (cidade == null) {
            cidade = new CidadeVO();
        }
        return cidade;
    }

    /**
     * @param cidade the cidade to set
     */
    public void setCidade(CidadeVO cidade) {
        this.cidade = cidade;
    }

    /**
     * @return the areaConhecimento
     */
    public AreaProfissionalVO getAreaProfissional() {
        if (areaProfissional == null) {
            areaProfissional = new AreaProfissionalVO();
        }
        return areaProfissional;
    }

    /**
     * @param areaConhecimento the areaConhecimento to set
     */
    public void setAreaProfissional(AreaProfissionalVO areaProfissional) {
        this.areaProfissional = areaProfissional;
    }

    /**
     * @return the listaConsultaEstado
     */
    public List getListaConsultaEstado() {
        if (listaConsultaEstado == null) {
            listaConsultaEstado = new ArrayList();
        }
        return listaConsultaEstado;
    }

    /**
     * @param listaConsultaEstado the listaConsultaEstado to set
     */
    public void setListaConsultaEstado(List listaConsultaEstado) {
        this.listaConsultaEstado = listaConsultaEstado;
    }

    public List getListaConsultaArea() {
        if (listaConsultaArea == null) {
            listaConsultaArea = new ArrayList();
        }
        return listaConsultaArea;
    }

    public void setListaConsultaArea(List listaConsultaArea) {
        this.listaConsultaArea = listaConsultaArea;
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

    /**
     * @return the listaConsultaCidade
     */
    public List getListaConsultaCidade() {
        return listaConsultaCidade;
    }

    /**
     * @param listaConsultaCidade
     *            the listaConsultaCidade to set
     */
    public void setListaConsultaCidade(List listaConsultaCidade) {
        this.listaConsultaCidade = listaConsultaCidade;
    }

    /**
     * @return the listaEstadoSugerir
     */
    public List getListaEstadoSugerir() {
        if (listaEstadoSugerir == null) {
            listaEstadoSugerir = new ArrayList();
        }
        return listaEstadoSugerir;
    }

    /**
     * @param listaEstadoSugerir the listaEstadoSugerir to set
     */
    public void setListaEstadoSugerir(List listaEstadoSugerir) {
        this.listaEstadoSugerir = listaEstadoSugerir;
    }

    /**
     * @return the listaCidadeSugerir
     */
    public List getListaCidadeSugerir() {
        if (listaCidadeSugerir == null) {
            listaCidadeSugerir = new ArrayList();
        }
        return listaCidadeSugerir;
    }

    /**
     * @param listaCidadeSugerir the listaCidadeSugerir to set
     */
    public void setListaCidadeSugerir(List listaCidadeSugerir) {
        this.listaCidadeSugerir = listaCidadeSugerir;
    }

    /**
     * @return the listaSelectItemCidadeSugerir
     */
    public List getListaSelectItemCidadeSugerir() {
        if (listaSelectItemCidadeSugerir == null) {
            listaSelectItemCidadeSugerir = new ArrayList();
        }
        return listaSelectItemCidadeSugerir;
    }

    /**
     * @param listaSelectItemCidadeSugerir the listaSelectItemCidadeSugerir to set
     */
    public void setListaSelectItemCidadeSugerir(List listaSelectItemCidadeSugerir) {
        this.listaSelectItemCidadeSugerir = listaSelectItemCidadeSugerir;
    }

	public List getListaAreaProfissionalSugerir() {
		if (listaAreaProfissionalSugerir == null) {
			listaAreaProfissionalSugerir = new ArrayList();
		}
		return listaAreaProfissionalSugerir;
	}

	public void setListaAreaProfissionalSugerir(List listaAreaProfissionalSugerir) {
		listaAreaProfissionalSugerir = listaAreaProfissionalSugerir;
	}
}
