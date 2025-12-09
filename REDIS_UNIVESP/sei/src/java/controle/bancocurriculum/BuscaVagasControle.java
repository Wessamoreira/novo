/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.bancocurriculum;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.academico.VisaoAlunoControle;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.bancocurriculum.AreaProfissionalVO;
import negocio.comuns.bancocurriculum.CandidatoVagaQuestaoRespostaVO;
import negocio.comuns.bancocurriculum.CandidatosVagasVO;
import negocio.comuns.bancocurriculum.LinkVO;
import negocio.comuns.bancocurriculum.VagaContatoVO;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.basico.AreaProfissionalInteresseContratacaoVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import relatorio.controle.academico.CurriculumRelControle;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

/**
 * 
 * @author Rogerio
 */
@Controller("BuscaVagasControle")
@Scope("viewScope")
@Lazy
public class BuscaVagasControle extends SuperControle implements Serializable {

    protected VagasVO vagasVO;
    private TurmaVO turmaVO;
    private String campoConsultaParceiro;
    private String valorConsultaParceiro;
    private List listaConsultaParceiro;
    private List listaSelectItemAreaProfissional;
    private List listaSelectItemEstado;
    private List listaSelectItemCidade;
    private Boolean apresentarComboCidade;
    private VisaoAlunoControle visaoAlunoControle;
    private List<AreaProfissionalVO> listaConsultaAreaProfissional;
    private List<VagasVO> listaMinhasVagas;
    private List<LinkVO> listaLink;
    private Boolean apresentarResultadoPesquisaVagas;
    private CandidatosVagasVO candidatosVagasVO;
    private String abaSelecionada;
    private String msgPanelVagas;

    public BuscaVagasControle() {
    	 HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	if (request.getAttribute("aba") != null) {
            setAbaAtiva((String) request.getAttribute("aba"));
        }
        montarListaSelectItemEstado();
        montarListaSelectItemAreaProfissional();
        consultarVagasPorAreaProfissionalInteressePessoa();
        consultarLinksDisponiveis();
        setControleConsulta(new ControleConsulta());
        // consultarVagas();
        setMensagemID("");
    }

    private void consultarLinksDisponiveis() {
        try {
            String visao = "PA";
            if (getUsuarioLogado().getVisaoLogar().equals("aluno")) {
                visao = "AL";
            }
            setListaLink(getFacadeFactory().getLinkFacade().consultarLinkApresentarVisao(visao, StatusAtivoInativoEnum.ATIVO, getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade()));
        } catch (Exception e) {
            setListaLink(null);
        }
    }

    private void consultarVagasPorAreaProfissionalInteressePessoa() {
        try {
            VagasVO obj = (VagasVO) context().getExternalContext().getSessionMap().get("vaga");
            if (getVisaoAlunoControle() != null && getVisaoAlunoControle().getPessoaVO().getCurriculoAtualizado()) {
                if (obj == null || obj.getCodigo() == 0) {
                    getVisaoAlunoControle().getPessoaVO().setAreaProfissionalInteresseContratacaoVOs(getFacadeFactory().getAreaProfissionalInteresseContratacaoFacade().consultarAreaProfissionalInteresseContratacaoPorCodigoPessoa(getVisaoAlunoControle().getPessoaVO().getCodigo(), false, getUsuarioLogado()));
                    getVagasVO().setEstado(getVisaoAlunoControle().getPessoaVO().getCidade().getEstado());
                    if (!getVisaoAlunoControle().getPessoaVO().getAreaProfissionalInteresseContratacaoVOs().isEmpty()) {
                        if (getVisaoAlunoControle().getPessoaVO().getAreaProfissionalInteresseContratacaoVOs().size() == 1) {
                            getVagasVO().setAreaProfissional(getVisaoAlunoControle().getPessoaVO().getAreaProfissionalInteresseContratacaoVOs().get(0).getAreaProfissional());
                            consultar();
                        } else {
                            setListaConsulta(getFacadeFactory().getVagasFacade().consultaRapidaBuscaVagaVisaoAluno(getVagasVO(), getVisaoAlunoControle().getPessoaVO().getAreaProfissionalInteresseContratacaoVOs(), getUsuarioLogado()));
                            // List objs = new ArrayList(0);
                            // for (AreaProfissionalInteresseContratacaoVO obj :
                            // getVisaoAlunoControle().getPessoaVO().getAreaProfissionalInteresseContratacaoVOs())
                            // {
                            // getVagasVO().setAreaProfissional(obj.getAreaProfissional());
                            // objs.addAll(getFacadeFactory().getVagasFacade().consultaRapidaBuscaVagaVisaoAluno(getVagasVO(),
                            // getUsuarioLogado()));
                            // }
                            // setListaConsulta(objs);
                        }
                    } else {
                        consultar();
                    }
                    setApresentarResultadoPesquisaVagas(Boolean.FALSE);
                } else {
                    getListaConsulta().clear();
                    obj = getFacadeFactory().getVagasFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
                    getListaConsulta().add(obj);
                    setApresentarResultadoPesquisaVagas(Boolean.TRUE);
                }
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String voltar() {
        setApresentarResultadoPesquisaVagas(Boolean.FALSE);
        setApresentarResultadoPesquisaVagas(Boolean.FALSE);
        setVagasVO(new VagasVO());
        consultarVagasPorAreaProfissionalInteressePessoa();
        return "consultar";
    }

    @Override
    public String consultar() {
        try {
            super.consultar();
            setListaConsulta(getFacadeFactory().getVagasFacade().consultaRapidaBuscaVagaVisaoAluno(getVagasVO(), getUsuarioLogado()));
            if (!getListaConsulta().isEmpty()) {
                getListaSelectItemCidade().clear();
                setApresentarResultadoPesquisaVagas(Boolean.TRUE);
            }
            setMensagemID("msg_dados_consultados");
            return "consultar";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }

    public String consultarVagas() {
        try {
            super.consultar();
            setListaConsulta(getFacadeFactory().getVagasFacade().consultarPorSituacao("AT", "", getUsuarioLogado().getParceiro().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
            return "consultar";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }

    public String editar() {
        return "";
    }

    public List getListaSelectItemSexoPessoa() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", "Todos"));
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

    public List consultarCargoSuggestionbox(Object event) {
        try {
            String valor = event.toString();
            List lista = getFacadeFactory().getCargoFacade().consultarPorNomeDuploPercent(valor, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            return lista;
        } catch (Exception e) {
            return new ArrayList(0);
        }
    }

    public List consultarEmpresaSuggestionbox(Object event) {
        try {
            String valor = event.toString();
            List lista = getFacadeFactory().getParceiroFacade().consultarPorNomeDuploPercent(valor, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            return lista;
        } catch (Exception e) {
            return new ArrayList(0);
        }
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

    public String visualizar() {
        try {
            VagasVO obj = (VagasVO) context().getExternalContext().getRequestMap().get("vagasItens");
            setVagasVO(getFacadeFactory().getVagasFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            setApresentarComboCidade(Boolean.TRUE);
            montarListaSelectItemCidade();
            setMensagemID("msg_dados_visualizar");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }
    public Boolean abrirModalConfirmacaoInscricao;

    public void realizarValidadacaoDadosCandidatarVaga() {
        try {
            getFacadeFactory().getCandidatoVagaQuestaoFacade().validarDados(getCandidatosVagasVO());
            setAbrirModalConfirmacaoInscricao(true);
        } catch (Exception e) {
            setAbrirModalConfirmacaoInscricao(false);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public Boolean getAbrirModalConfirmacaoInscricao() {
        if (abrirModalConfirmacaoInscricao == null) {
            abrirModalConfirmacaoInscricao = false;
        }
        return abrirModalConfirmacaoInscricao;
    }

    public void setAbrirModalConfirmacaoInscricao(Boolean abrirModalConfirmacaoInscricao) {
        this.abrirModalConfirmacaoInscricao = abrirModalConfirmacaoInscricao;
    }

    public void realizarPersistenciaCandidatarVaga() {
        try {
        	executarValidacaoSimulacaoVisaoAluno();
            getFacadeFactory().getVagasFacade().realizarCandidatarVaga(getCandidatosVagasVO(), getUsuarioLogado());
            if (enviarEmail(getVagasVO())) {
            	setMsgPanelVagas("A inscrição para concorrer a vaga selecionada foi realizada com sucesso.");
            } else {
            	setMsgPanelVagas("Dados Gravados com sucesso, porém não foi possível enviar email. Motivo: TEXTO PADRÃO BANCO CURRICULUM não cadastrado.");
            }
            setAbrirModalConfirmacaoInscricao(false);
            setCandidatosVagasVO(new CandidatosVagasVO());
        } catch (Exception e) {
            setAbrirModalConfirmacaoInscricao(true);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void realizarCancelamentoCandidatarVaga() {
        setCandidatosVagasVO(new CandidatosVagasVO());
        setAbrirModalConfirmacaoInscricao(false);
    }

    public void realizarCandidatarVaga() {
        try {
            setAbrirModalConfirmacaoInscricao(false);
            setCandidatosVagasVO(new CandidatosVagasVO());
            getCandidatosVagasVO().setVaga((VagasVO) context().getExternalContext().getRequestMap().get("vagasItens"));
            getCandidatosVagasVO().getPessoa().setCodigo(getUsuarioLogado().getPessoa().getCodigo());
            getCandidatosVagasVO().getPessoa().setNome(getUsuarioLogado().getPessoa().getNome());
            getCandidatosVagasVO().setCandidatoVagaQuestaoVOs(getFacadeFactory().getCandidatoVagaQuestaoFacade().consultarPorCandidatoVaga(0, getCandidatosVagasVO().getVaga().getCodigo()));
            setAbrirModalConfirmacaoInscricao(getCandidatosVagasVO().getCandidatoVagaQuestaoVOs().isEmpty());
            if (getCandidatosVagasVO().getCandidatoVagaQuestaoVOs().isEmpty()) {
            	setMsgPanelVagas("Tem certeza que deseja se candidar a vaga ?");
            } else {
            	setMsgPanelVagas("Responda o questionário acima");
            }
            limparDadosParceiro();
            limparMensagem();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String getControlarModalVaga() {
        if (getCandidatosVagasVO().getVaga().getCodigo() > 0) {
            if (!getAbrirModalConfirmacaoInscricao() && !getCandidatosVagasVO().getCandidatoVagaQuestaoVOs().isEmpty()) {
                return "RichFaces.$('panelVagaQuestionario').show()";
            }
            if (getAbrirModalConfirmacaoInscricao()) {
                return "RichFaces.$('panelConfirmarInscricaoVaga').show(); RichFaces.$('panelVagaQuestionario').hide()";
            }
        }
        return "RichFaces.$('panelVagaQuestionario').hide(); RichFaces.$('panelConfirmarInscricaoVaga').hide();";
    }

    public void carregarVaga() {
        try {
            VagasVO obj = (VagasVO) context().getExternalContext().getRequestMap().get("vagasItens");
            setVagasVO(obj);
            // setCandidatosVagasVO(new CandidatosVagasVO());
            // getCandidatosVagasVO().setVaga(obj);
            // getCandidatosVagasVO().getPessoa().setCodigo(getUsuarioLogado().getPessoa().getCodigo());
            // getCandidatosVagasVO().getPessoa().setNome(getUsuarioLogado().getPessoa().getNome());
            // getCandidatosVagasVO().setCandidatoVagaQuestaoVOs(getFacadeFactory().getCandidatoVagaQuestaoFacade().consultarPorCandidatoVaga(0,
            // obj.getCodigo()));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void realizarVerificacaoQuestaoUnicaEscolha() {
        CandidatoVagaQuestaoRespostaVO orq = (CandidatoVagaQuestaoRespostaVO) context().getExternalContext().getRequestMap().get("opcaoRespostaQuestao");
        if (orq.getSelecionada()) {
            orq.setSelecionada(false);
        } else {
            orq.setSelecionada(true);
            getFacadeFactory().getCandidatoVagaQuestaoFacade().realizarVerificacaoQuestaoUnicaEscolha(getCandidatosVagasVO(), orq);
        }
    }

    private Boolean enviarEmail(VagasVO vaga) throws Exception {
        vaga.setVagaContatoVOs(getFacadeFactory().getVagaContatoFacade().consultarPorVaga(vaga.getCodigo()));

        if (!vaga.getVagaContatoVOs().isEmpty()) {
            ComunicacaoInternaVO comunicacaoInternaVO = null;
            Boolean enviado = true;
            PessoaVO destinatario = null;
            List listaObjetos = new ArrayList(0);
            try {
                comunicacaoInternaVO = new ComunicacaoInternaVO();
                String corpoMensagem = getFacadeFactory().getTextoPadraoBancoCurriculumFacade().consultarPorTipoUnica("alunoCandidatadoVaga", false, "AT", Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()).getTexto();
                corpoMensagem = gerarMensagemCadidatacaoVaga(vaga, corpoMensagem);
                comunicacaoInternaVO.getResponsavel().setNome("SEI - SISTEMA EDUCACIONAL INTEGRADO");
                comunicacaoInternaVO.setAssunto("Notificação de Currículo");
                comunicacaoInternaVO.setMensagem(corpoMensagem);
                for (VagaContatoVO vagaContatoVO : getVagasVO().getVagaContatoVOs()) {
                    destinatario = new PessoaVO();
                    destinatario.setCodigo(vagaContatoVO.getCodigo());
                    destinatario.setNome(vagaContatoVO.getNome());
                    destinatario.setEmail(vagaContatoVO.getEmail());
                    destinatario.setNovoObj(false);
                    ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
                    comunicadoInternoDestinatarioVO.setDestinatario(destinatario);
                    comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().add(comunicadoInternoDestinatarioVO);
                }
                comunicacaoInternaVO.setResponsavel(getUsuarioLogado().getPessoa());
                getFacadeFactory().getPessoaFacade().carregarDados(getCandidatosVagasVO().getPessoa(), getUsuarioLogado());
                getCandidatosVagasVO().getPessoa().setFormacaoExtraCurricularVOs(getFacadeFactory().getFormacaoExtraCurricularFacade().consultarPorCodigoPessoaOrdemNovaAntiga(getCandidatosVagasVO().getPessoa().getCodigo(), false, getUsuarioLogado()));
                getCandidatosVagasVO().getPessoa().setFormacaoAcademicaVOs(getFacadeFactory().getFormacaoAcademicaFacade().consultarPorCodigoPessoaOrdemNovaAntiga(getCandidatosVagasVO().getPessoa().getCodigo(), false, getUsuarioLogado()));
                getCandidatosVagasVO().getPessoa().setDadosComerciaisVOs(getFacadeFactory().getDadosComerciaisFacade().consultarPorCodigoPessoaOrdemNovaAntiga(getCandidatosVagasVO().getPessoa().getCodigo(), false, getUsuarioLogado()));
                listaObjetos.add(getCandidatosVagasVO());
                List<File> listaAnexos = new ArrayList<File>();
                SuperParametroRelVO superParametroRelVO = new SuperParametroRelVO();
                superParametroRelVO.setNomeDesignIreport(CurriculumRelControle.getDesignIReportRelatorio());
                superParametroRelVO.setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                superParametroRelVO.setSubReport_Dir(CurriculumRelControle.getCaminhoBaseRelatorio());
                superParametroRelVO.setNomeUsuario(getUsuarioLogado().getNome());
                superParametroRelVO.setTituloRelatorio("");
                superParametroRelVO.setListaObjetos(listaObjetos);
                superParametroRelVO.setCaminhoBaseRelatorio(CurriculumRelControle.getCaminhoBaseRelatorio());
                superParametroRelVO.setNomeEmpresa("");
                superParametroRelVO.setVersaoSoftware(getVersaoSistema());
                superParametroRelVO.setFiltros("");
                superParametroRelVO.adicionarParametro("checkboxDesmarcado2", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "checkboxDesmarcado2.png");
                superParametroRelVO.adicionarParametro("checkboxMarcado2", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "checkboxMarcado2.png");
                superParametroRelVO.adicionarParametro("radioButtomDesmarcado2", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "radioButtomDesmarcado2.png");
                superParametroRelVO.adicionarParametro("radioButtomMarcado2", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "radioButtomMarcado2.png");
                File file = UteisJSF.realizarGeracaoArquivoPDF(superParametroRelVO);
                listaAnexos.add(file);
                if (!corpoMensagem.equals("")) {
                    getFacadeFactory().getEmailFacade().realizarGravacaoEmail(comunicacaoInternaVO, listaAnexos, true, getUsuarioLogado(), null);
                } else {
                    enviado = false;
                }
                return enviado;
            } catch (Exception e) {
                throw e;
            }
        }
        return true;
    }

    public String gerarMensagemCadidatacaoVaga(VagasVO vaga, String corpoMensagem) {
        if (corpoMensagem.contains("#Nome_Empresa")) {
            corpoMensagem = corpoMensagem.replace("#Nome_Empresa", vaga.getParceiro().getNome());
        }
        if (corpoMensagem.contains("#Cargo_Vaga")) {
            corpoMensagem = corpoMensagem.replace("#Cargo_Vaga", vaga.getCargo());
        }
        if (corpoMensagem.contains("#Nome_Aluno")) {
            corpoMensagem = corpoMensagem.replace("#Nome_Aluno", getUsuarioLogado().getPessoa().getNome());
        }
        if (corpoMensagem.contains("<!DOCTYPE")) {
            corpoMensagem = corpoMensagem.replace(corpoMensagem.substring(corpoMensagem.indexOf("<!DOCTYPE"), corpoMensagem.indexOf("<html>")), "");
        }
        return corpoMensagem;
    }

    public String realizarSairVaga() {
        try {
        	executarValidacaoSimulacaoVisaoAluno();
            getFacadeFactory().getVagasFacade().realizarSairVaga(getVagasVO(), getUsuarioLogado().getPessoa(), getUsuarioLogado());
            consultarMinhasVagas();
            setMensagemID("msg_Vagas_sairVaga");
            return "consultar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public Boolean getConsultarExistenciaCandidatosVagas() {
        try {
            VagasVO obj = (VagasVO) context().getExternalContext().getRequestMap().get("vagasItens");
            Boolean existeRegistro = getFacadeFactory().getVagasFacade().consultarExistenciaCandidatosVagas(obj.getCodigo(), getUsuarioLogado().getPessoa().getCodigo());
            return existeRegistro;
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return false;
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
        listaConsultaParceiro.clear();
        this.setValorConsultaParceiro("");
        this.setCampoConsultaParceiro("");
    }

    public List getTipoConsultaComboSalario() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("À Combinar", "À combinar"));
        itens.add(new SelectItem("Até R$999", "Até R$999,00"));
        itens.add(new SelectItem("R$1000 à R$1999", "De R$ 1000,00 até R$ 1999,00"));
        itens.add(new SelectItem("R$2000 à R$2999", "De R$ 2000,00 até R$ 2999,00"));
        itens.add(new SelectItem("R$3000 à R$3999", "De R$ 3000,00 até R$ 3999,00"));
        itens.add(new SelectItem("R$4000 à R$4999", "De R$ 4000,00 até R$ 4999,00"));
        itens.add(new SelectItem("R$5000 à R$5999", "De R$ 5000,00 até R$ 5999,00"));
        itens.add(new SelectItem("acima de R$6000", "Acima de R$ 6000,00"));
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

    public List getTipoComboNivelOutrosIdiomas() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("inicial", "Inicial"));
        itens.add(new SelectItem("intermediario", "Intermediário"));
        itens.add(new SelectItem("avancado", "Avançado"));
        return itens;

    }

    public List getTipoConsultaComboTipoVeiculo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("CA", "Carro"));
        itens.add(new SelectItem("MO", "Moto"));
        return itens;
    }

    public List getTipoConsultaComboAreaProfissional() {
        List itens = new ArrayList(0);
        // itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("descricao", "Descrição"));
        return itens;
    }

    public void montarListaSelectItemAreaProfissional() {
        try {
            montarListaSelectItemAreaProfissional("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());
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
                AreaProfissionalInteresseContratacaoVO obj = (AreaProfissionalInteresseContratacaoVO) i.next();
                objs.add(new SelectItem(obj.getAreaProfissional().getCodigo(), obj.getAreaProfissional().getDescricaoAreaProfissional()));
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
        List lista = getFacadeFactory().getAreaProfissionalInteresseContratacaoFacade().consultarAreaProfissionalInteresseContratacaoPorCodigoPessoa(getUsuarioLogado().getPessoa().getCodigo(), false, getUsuarioLogado());
        //List lista = getFacadeFactory().getAreaProfissionalInteresseContratacaoFacade().consultarPorChavePrimaria(Integer.MIN_VALUE, Integer.MIN_VALUE, null)orDescricaoAreaProfissionalAtivo(prm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemCidade() {
        try {
//			if (getVagasVO().getEstado().getCodigo() != 0) {
//				setApresentarComboCidade(Boolean.TRUE);
//			} else {
//				setApresentarComboCidade(Boolean.FALSE);
//			}
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
//			objs.add(new SelectItem(0, ""));
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

    public void montarListaSelectItemEstado() {
        try {
            montarListaSelectItemEstado("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());
            ;
        }
    }

    public void montarListaSelectItemEstado(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarEstadoPorSigla(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            //objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                EstadoVO obj = (EstadoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
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

    public void consultarAreaProfissional() {
        try {
            getListaConsultaAreaProfissional().clear();
            setListaConsultaAreaProfissional(getFacadeFactory().getAreaProfissionalFacade().consultar("AT", "situacao", false, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void limparDadosAreaProfissional() {
        getVagasVO().getListaAreaProfissional().clear();
    }

    public void consultarMinhasVagas() {
        try {
            setListaMinhasVagas(getFacadeFactory().getVagasFacade().consultarListaVagasPorAluno(getUsuarioLogado().getPessoa().getCodigo(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparDadosParceiro() {
        getVagasVO().setParceiro(new ParceiroVO());
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

    /*
     * Método responsável por inicializar List<SelectItem> de valores do
     * ComboBox correspondente ao atributo <code>escolaridade</code>
     */
    public List getListaSelectItemEscolaridadeFormacaoAcademica() throws Exception {
        List objs = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(NivelFormacaoAcademica.class, false);

        return objs;
    }

    public VagasVO getVagasVO() {
        if (vagasVO == null) {
            vagasVO = new VagasVO();
        }
        return vagasVO;
    }

    public void setVagasVO(VagasVO vagasVO) {
        this.vagasVO = vagasVO;
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

    public List getListaConsultaParceiro() {
        if (listaConsultaParceiro == null) {
            listaConsultaParceiro = new ArrayList(0);
        }
        return listaConsultaParceiro;
    }

    public void setListaConsultaParceiro(List listaConsultaParceiro) {
        this.listaConsultaParceiro = listaConsultaParceiro;
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

    public boolean getApresentarTipoVeiculo() {
        return getVagasVO().getNecessitaVeiculo();
    }

    public List<AreaProfissionalVO> getListaConsultaAreaProfissional() {
        if (listaConsultaAreaProfissional == null) {
            listaConsultaAreaProfissional = new ArrayList<AreaProfissionalVO>(0);
        }
        return listaConsultaAreaProfissional;
    }

    public void setListaConsultaAreaProfissional(List<AreaProfissionalVO> listaConsultaAreaProfissional) {
        this.listaConsultaAreaProfissional = listaConsultaAreaProfissional;
    }

    public List<VagasVO> getListaMinhasVagas() {
        if (listaMinhasVagas == null) {
            listaMinhasVagas = new ArrayList<VagasVO>(0);
        }
        return listaMinhasVagas;
    }

    public void setListaMinhasVagas(List<VagasVO> listaMinhasVagas) {
        this.listaMinhasVagas = listaMinhasVagas;
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

    public VisaoAlunoControle getVisaoAlunoControle() {
        if (visaoAlunoControle == null) {
            visaoAlunoControle = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get(VisaoAlunoControle.class.getSimpleName());
        }
        return visaoAlunoControle;
    }

    public void setVisaoAlunoControle(VisaoAlunoControle visaoAlunoControle) {
        this.visaoAlunoControle = visaoAlunoControle;
    }

    public Boolean getApresentarResultadoPesquisaVagas() {
        if (apresentarResultadoPesquisaVagas == null) {
            apresentarResultadoPesquisaVagas = Boolean.FALSE;
        }
        return apresentarResultadoPesquisaVagas;
    }

    public void setApresentarResultadoPesquisaVagas(Boolean apresentarResultadoPesquisaVagas) {
        this.apresentarResultadoPesquisaVagas = apresentarResultadoPesquisaVagas;
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

    public String getAbaSelecionada() {
        if (abaSelecionada == null) {
            abaSelecionada = "abaVagasSugeridas";
        }
        return abaSelecionada;
    }

    public void setAbaSelecionada(String abaSelecionada) {
        this.abaSelecionada = abaSelecionada;
    }

    public String selecionarAbaVagasSugeridas() {
        setAbaSelecionada("abaVagasSugeridas");
        return Uteis.getCaminhoRedirecionamentoNavegacao("buscaVagasAluno.xhtml");
    }

    public String selecionarAbaBuscaVaga() {
        setAbaSelecionada("abaBuscaVaga");
        return Uteis.getCaminhoRedirecionamentoNavegacao("buscaVagasAluno.xhtml");
    }

    public String selecionarAbaMinhasVagas() {
        setAbaSelecionada("abaMinhasVagas");
        return Uteis.getCaminhoRedirecionamentoNavegacao("buscaVagasAluno.xhtml");
    }

    public String selecionarAbaMeuCurriculum() {
        setAbaSelecionada("abaMeuCurriculum");
        return Uteis.getCaminhoRedirecionamentoNavegacao("buscaVagasAluno.xhtml");
    }

    /**
     * @return the listaLink
     */
    public List<LinkVO> getListaLink() {
        if (listaLink == null) {
            listaLink = new ArrayList<LinkVO>();
        }
        return listaLink;
    }

    /**
     * @param listaLink the listaLink to set
     */
    public void setListaLink(List<LinkVO> listaLink) {
        this.listaLink = listaLink;
    }
    
    public String getMsgPanelVagas() {
		return msgPanelVagas;
	}

	public void setMsgPanelVagas(String msgPanelVagas) {
		this.msgPanelVagas = msgPanelVagas;
	}
}
