/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.crm;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.crm.BuscaProspectVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.InteracaoWorkflowVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.crm.Prospects;
import negocio.facade.jdbc.protocolo.Requerimento;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.controle.crm.InteracaoFollowUpRelControle;

/**
 *
 * @author Philippe
 */
@Controller("BuscaProspectControle")
@Scope("viewScope")
@Lazy
public class BuscaProspectControle extends SuperControleRelatorio {

    private BuscaProspectVO buscaProspectVO;
    private List listaSelectItemUnidadeEnsino;
    private String campoConsultaFuncionario;
    private String valorConsultaFuncionario;
    private List listaConsultaFuncionario;
    private String campoConsultaFuncionarioResponsavel;
    private String valorConsultaFuncionarioResponsavel;
    private List listaConsultaFuncionarioResponsavel;
    private String campoConsultaCidade;
    private String valorConsultaCidade;
    private List listaConsultaCidade;
    private String campoConsultaProspect;
    private String valorConsultaProspect;
    private List listaConsultaProspect;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List listaConsultaCurso;
    private List<BuscaProspectVO> listaProspects;
    private String abrirFecharModalInteracao;
    private InteracaoWorkflowVO interacaoWorkflowVO;
    private Boolean gerarListaEmailProspect;
    private String listaEncadeadaEmailProspect;
    private InteracaoFollowUpRelControle interacaoFollowUpRelControle;
    private Boolean permitirConsultarTodasUnidades;
    private Boolean permitirConsultarProspectsOutroConsultorUltimaInteracao;
    private Boolean permitirConsultarProspectsOutroConsultorResponsavel;
    private ComunicacaoInternaVO comunicacaoEnviar;
    private PersonalizacaoMensagemAutomaticaVO mensagemTemplate;
    private ProspectsVO prospect;
    private String mensagemAvisoExclusao;
    private String autocompleteValorCurso;
    private String autocompleteValorPropspect;
    private String autocompleteValorConsultor;
    private String autocompleteValorEmail;
    private String autocompleteValorConsultorResponsavel;
    private String autocompleteValorCidade;
    private String observacaoCompleta;
    private String nomeProspect;
    private String situacaoProspect;

	public BuscaProspectControle() throws Exception {
        montarListaSelectItemUnidadeEnsino();
        definirEscopoBuscaProspectComBaseNaPermissaoAcesso();
    }
	
	@PostConstruct
	public void init() {
		try {
			LayoutPadraoVO layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo(Requerimento.class.getName()+"_"+getUsuarioLogado().getCodigo(), "VersaoNova", false, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(layoutPadraoVO)) {
				setVersaoNova(Boolean.valueOf(layoutPadraoVO.getValor()));
				setVersaoAntiga(!Boolean.valueOf(layoutPadraoVO.getValor()));
			} else {
				setVersaoAntiga(false);
				setVersaoNova(true);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
			
	}

    public Boolean verificarUsuarioPossuiPermissaoConsulta(String identificadorAcaoPermissao) {
        Boolean liberar = Boolean.FALSE;
        try {
            ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(identificadorAcaoPermissao, getUsuarioLogado());
            liberar = Boolean.TRUE;
        } catch (Exception e) {
            liberar = Boolean.FALSE;
        }
        return liberar;
    }

    private void definirEscopoBuscaProspectComBaseNaPermissaoAcesso() {
        setPermitirConsultarTodasUnidades(verificarUsuarioPossuiPermissaoConsulta("BuscaProspect_consultarTodasUnidades"));
        setPermitirConsultarProspectsOutroConsultorUltimaInteracao(verificarUsuarioPossuiPermissaoConsulta("BuscaProspect_consultarProspectOutrosConsultoresUltimaInteracao"));
        setPermitirConsultarProspectsOutroConsultorResponsavel(verificarUsuarioPossuiPermissaoConsulta("BuscaProspect_consultarProspectOutrosConsultoresResponsaveis"));
        if (!getPermitirConsultarTodasUnidades()) {
            if ((getUnidadeEnsinoLogado() == null)
                    || (getUnidadeEnsinoLogado().getCodigo().equals(new Integer(0)))) {
                // Caso a unidade esteja em branco signigica que o usuário logado é diretor multi campos
                // logo, assumi-se que ele pode ver prospects de todas as unidades = não temos uma unidade
                // específica para limitar a consulta.
                setPermitirConsultarTodasUnidades(Boolean.TRUE);
            } else {
                // Se entrar aqui é por que o usuário nao tem permissao para consultar em outras unidades
                // logo, temos que fixar uma unidade de ensino e nao permitir que a mesma seja alterada
                this.getBuscaProspectVO().setUnidadeEnsino(getUnidadeEnsinoLogadoClone());
            }
        }
        if (!getPermitirConsultarProspectsOutroConsultorUltimaInteracao()) {
            try {
                this.getBuscaProspectVO().setConsultor(
                        getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(
                        getUsuarioLogado().getPessoa().getCodigo(),
                        false, getUsuarioLogado()));
            } catch (Exception e) {
                //setMensagemDetalhada("msg_erro", e.getMessage());
            }
        }
        if (!getPermitirConsultarProspectsOutroConsultorResponsavel()) {
            try {
                this.getBuscaProspectVO().setConsultorResponsavel(
                        getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(
                        getUsuarioLogado().getPessoa().getCodigo(),
                        false, getUsuarioLogado()));
            } catch (Exception e) {
                //setMensagemDetalhada("msg_erro", e.getMessage());
            }
        }
    }
    
    public void realizarGeracaoEmailExcel() {
        try {
        	this.setCaminhoRelatorio(getFacadeFactory().getBuscaProspectFacade().consultarEmailsExcel("", getBuscaProspectVO(), getUsuarioLogado()));
        	if(getCaminhoRelatorio().trim().isEmpty()){
        		setFazerDownload(false);
        		setMensagemID("msg_relatorio_sem_dados", Uteis.ALERTA);
        	}else{
        		setFazerDownload(true);
        		setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        	}
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarProspects() {
        try {
            getControleConsultaOtimizado().setLimitePorPagina(15);
            setLetra("");
            if (getGerarListaEmailProspect()) {
                setListaEncadeadaEmailProspect(getFacadeFactory().getBuscaProspectFacade().consultarEmails("", getBuscaProspectVO(), getUsuarioLogado()));
            } else {
            	limparDadosBuscaProspectCampoAutoComplete();
                getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getBuscaProspectFacade().consultar("", getBuscaProspectVO(), getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
                getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getBuscaProspectFacade().consultarTotalRegistro("", getBuscaProspectVO(), getUsuarioLogado()));
            }
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void limparDadosBuscaProspectCampoAutoComplete() {
    	if (!getBuscaProspectVO().getCurso().getCodigo().equals(0) && getBuscaProspectVO().getCurso().getNome().equals("")) {
    		limparDadosCurso();
    	}
    	if (!getBuscaProspectVO().getConsultor().getCodigo().equals(0) && getBuscaProspectVO().getConsultor().getPessoa().getNome().equals("")) {
    		limparDadosFuncionario();
    	}
    	if (!getBuscaProspectVO().getConsultorResponsavel().getCodigo().equals(0) && getBuscaProspectVO().getConsultorResponsavel().getPessoa().getNome().equals("")) {
    		limparDadosFuncionarioResponsavel();
    	}
    	if (!getBuscaProspectVO().getProspect().getCodigo().equals(0) && getBuscaProspectVO().getProspect().getNome().equals("")) {
    		limparDadosProspect();
    	}
    	if (!getBuscaProspectVO().getCidade().getCodigo().equals(0) && getBuscaProspectVO().getCidade().getNome().equals("")) {
    		limparDadosCidade();
    	}
    }

    public void mensagemProspects() {
        try {
//            setLetra("");
            getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemBuscaProspect(getFacadeFactory().getBuscaProspectFacade().consultar(getLetra(), getBuscaProspectVO(), getUsuarioLogado(), null, null), getMensagemTemplate(), comunicacaoEnviar);
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void abrirMensagemProspects() {
        try {
//            setLetra("");
            setMensagemTemplate(getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_BUSCA_PROSPECT, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getBuscaProspectVO().getUnidadeEnsino().getCodigo(), null, null));
            if (getMensagemTemplate() != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
                getComunicacaoEnviar().setAssunto(getMensagemTemplate().getAssunto());
                getComunicacaoEnviar().setMensagem(getMensagemTemplate().getMensagem());
            } else {
                throw new Exception("Não existe uma mensagem automativa personalizada cadastrada e/ou habilitada!");
            }
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarProspects(String letra) {
        try {
            getControleConsultaOtimizado().setLimitePorPagina(15);
            if (getGerarListaEmailProspect()) {
                setListaEncadeadaEmailProspect(getFacadeFactory().getBuscaProspectFacade().consultarEmails(letra, getBuscaProspectVO(), getUsuarioLogado()));
            } else {
                getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getBuscaProspectFacade().consultar(letra, getBuscaProspectVO(), getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
                getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getBuscaProspectFacade().consultarTotalRegistro(letra, getBuscaProspectVO(), getUsuarioLogado()));

            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void paginarConsultaProspect(DataScrollEvent event) {
        getControleConsultaOtimizado().setPaginaAtual(event.getPage());
        getControleConsultaOtimizado().setPage(event.getPage());
        if (getLetra().trim().isEmpty()) {
            consultarProspects();
        } else {
            consultarProspects(getLetra());
        }

    }
    private String letra;

    public void consultarPorLetraListener(ActionEvent evt) {
        setLetra((String) evt.getComponent().getId());
        getControleConsultaOtimizado().setPaginaAtual(1);
        getControleConsultaOtimizado().setPage(1);
        if (letra.length() > 1) {
            letra = "A";
        }
        consultarProspects(letra);
    }

    public String getLetra() {
        if (letra == null) {
            letra = "";
        }
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public void gravarNovaInteracao() {
        try {
            getFacadeFactory().getInteracaoWorkflowFacade().incluirSemValidarDados(getInteracaoWorkflowVO(), getUsuarioLogado());
            setLetra(getInteracaoWorkflowVO().getProspect().getNome().substring(0, 1));
            consultarProspects(getInteracaoWorkflowVO().getProspect().getNome().substring(0, 1));
            setInteracaoWorkflowVO(new InteracaoWorkflowVO());
            setAbrirFecharModalInteracao("RichFaces.$('panelNovaInteracao').hide();");
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (Exception e) {
            setAbrirFecharModalInteracao("RichFaces.$('panelNovaInteracao').show();");
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void imprimirRelatorioInteracoes() {
        try {
            if ((!getBuscaProspectVO().getUnidadeEnsino().getCodigo().equals(0)) &&
                (!getBuscaProspectVO().getCurso().getCodigo().equals(0)) &&
                 ((!getBuscaProspectVO().getConsultor().getCodigo().equals(0)) ||
                 (!getBuscaProspectVO().getConsultorResponsavel().getCodigo().equals(0)))) {
                List<BuscaProspectVO> listaConsultaProspectRel = getFacadeFactory().getBuscaProspectFacade().consultar(letra, getBuscaProspectVO(), getUsuarioLogado(), 0, 0);
                getInteracaoFollowUpRelControle().setBuscaProspectVOs(listaConsultaProspectRel);
                setMensagemID(getInteracaoFollowUpRelControle().imprimirPDF());
            } else {
                setMensagemDetalhada("msg_erro", "Para emitir o relatório de interações uma unidade, um curso e um consultor responsável deverão estar informados como filtro.");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void inicializarDadosNovaInteracao() {
        BuscaProspectVO obj = (BuscaProspectVO) context().getExternalContext().getRequestMap().get("buscaProspectItens");
        getInteracaoWorkflowVO().setProspect(obj.getProspectNovaInteracao());
        getInteracaoWorkflowVO().setCurso(obj.getCursoNovaInteracao());
        getInteracaoWorkflowVO().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
        getInteracaoWorkflowVO().getResponsavel().getPessoa().setNome(getUsuarioLogado().getPessoa().getNome());
        getInteracaoWorkflowVO().setHoraInicio(Uteis.getHoraAtual());
        getInteracaoWorkflowVO().setDataInicio(new Date());
        setAbrirFecharModalInteracao("");
    }

    public List<FuncionarioVO> autocompleteConsultor(Object suggest) {
        try {
            return getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomePessoaAutoComplete((String) suggest, getBuscaProspectVO().getUnidadeEnsino().getCodigo(), 20, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
            return new ArrayList<FuncionarioVO>();
        }
    }

    public List<CursoVO> autocompleteCurso(Object suggest) {
        try {
            return getFacadeFactory().getCursoFacade().consultaRapidaPorNomeAutoComplete((String) suggest, getBuscaProspectVO().getUnidadeEnsino().getCodigo(), 20, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
            return new ArrayList<CursoVO>();
        }
    }
    
    public List<BuscaProspectVO> autocompleteEmail(Object suggest) {
        try {
            return getFacadeFactory().getBuscaProspectFacade().consultarEmailSuggestionBox((String) suggest, getUsuarioLogado());
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
            return new ArrayList<BuscaProspectVO>();
        }
    }
    
    
    public List<BuscaProspectVO> autocompleteTelefone(Object suggest) {
        try {
            return getFacadeFactory().getBuscaProspectFacade().consultarTelefoneSuggestionBox((String) suggest, getUsuarioLogado());
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
            return new ArrayList<BuscaProspectVO>();
        }
    }

    public List<BuscaProspectVO> autocompleteCpf(Object suggest) {
        try {
            return getFacadeFactory().getBuscaProspectFacade().consultarCpfSuggestionBox((String) suggest, getUsuarioLogado());
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
            return new ArrayList<BuscaProspectVO>();
        }
    }

    public List<ProspectsVO> autocompleteProspect(Object suggest) {
        try {
            return getFacadeFactory().getProspectsFacade().consultaRapidaPorNomeAutoComplete((String) suggest, getBuscaProspectVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
            return new ArrayList<ProspectsVO>();
        }
    }

    public List<CidadeVO> autocompleteCidade(Object suggest) {
        try {
            return getFacadeFactory().getCidadeFacade().consultaRapidaPorNomeAutoComplete((String) suggest, 20, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
            return new ArrayList<CidadeVO>();
        }
    }

    public List getTipoPeriodo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("dataUltimoContato", "Data último contato"));
        itens.add(new SelectItem("dataCadastro", "Data cadastro"));
        return itens;
    }

    public List getTipoSituacao() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", "Todas"));        
        itens.add(new SelectItem("MATRICULADO", "Prospects com Matrícula Ativa"));
        itens.add(new SelectItem("FINALIZADO_SUCESSO", "Finalizado com Sucesso"));
        itens.add(new SelectItem("FINALIZADO_SUCESSO_SEM_MATRICULA", "Finalizado com Sucesso s/ Matrícula Ativa"));
        itens.add(new SelectItem("FINALIZADO_SUCESSO_SEM_MATRICULA_AT_TR_CA", "Finalizado com Sucesso s/ Matrícula (Ati. Tranc. Canc.)"));
        itens.add(new SelectItem("FINALIZADO_SUCESSO_SEM_MATRICULA_AT_TR_CA", "Finalizado com Sucesso s/ Matrícula (Ati. Tranc. Canc.)"));
        itens.add(new SelectItem("FINALIZADO_SUCESSO_COM_MATRICULA", "Finalizado com Sucesso com Matrícula Ativa"));
        itens.add(new SelectItem("NORMAL", "Em Prospecção"));
        itens.add(new SelectItem("NORMAL_COM_MATRICULA", "Em Prospecção com Matrícula Ativa"));
        itens.add(new SelectItem("NORMAL_SEM_MATRICULA", "Em Prospecção s/ Matrícula Ativa"));
        itens.add(new SelectItem("NORMAL_SEM_MATRICULA_AT_TR_CA", "Em Prospecção s/ Matrícula (Ativa, Tranc., Canc.)"));
        itens.add(new SelectItem("FINALIZADO_INSUCESSO", "Finalizado com Insucesso"));
        itens.add(new SelectItem("FINALIZADO_INSUCESSO_COM_MATRICULA", "Finalizado com Insucesso com Matrícula Ativa"));
        itens.add(new SelectItem("FINALIZADO_INSUCESSO_SEM_MATRICULA", "Finalizado com Insucesso sem Matrícula Ativa"));
        itens.add(new SelectItem("FINALIZADO_INSUCESSO_SEM_MATRICULA_AT_TR_CA", "Finalizado com Insucesso s/ Matrícula (Ati. Tranc. Canc.)"));
        return itens;
    }

    public List getTipoOrdenacao() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("data", "Data último contato"));
        itens.add(new SelectItem("dataCadastro", "Data cadastro"));
        itens.add(new SelectItem("consultor", "Por consultor"));
        itens.add(new SelectItem("nomeProspect", "Por prospect"));
        return itens;
    }

    public void consultarProspect() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaProspect().equals("nome")) {
                if (getCampoConsultaProspect().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getProspectsFacade().consultarPorNome(getValorConsultaProspect(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado(), "");
            }
            if (getCampoConsultaProspect().equals("cpf")) {
                if (getCampoConsultaProspect().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getProspectsFacade().consultarPorCpf(getValorConsultaProspect(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado(), "");
            }
            if (getCampoConsultaProspect().equals("cnpj")) {
                if (getCampoConsultaProspect().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getProspectsFacade().consultarPorCnpj(getValorConsultaProspect(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado(), "");
            }
            setListaConsultaProspect(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCidade(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void selecionarProspect() {
        ProspectsVO obj = (ProspectsVO) context().getExternalContext().getRequestMap().get("prospectItens");
        getBuscaProspectVO().setProspect(obj);
		setAutocompleteValorPropspect(getBuscaProspectVO().getProspect().getNome());
		setListaConsultaProspect(new ArrayList<>());
		setValorConsultaProspect("");
		setMensagemID("msg_dados_selecionados");
    }

    public List getTipoConsultaProspect() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("cpf", "CPF"));
        itens.add(new SelectItem("cnpj", "CNPJ"));
        return itens;
    }

    public void limparConsultaProspect() {
        getListaConsultaProspect().clear();
        setValorConsultaProspect("");
    }

    public void limparDadosProspect() {
        getBuscaProspectVO().setProspect(new ProspectsVO());
        setAutocompleteValorPropspect("");
    }

    public void consultarCurso() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCurso().equals("nome")) {
                objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getBuscaProspectVO().getUnidadeEnsino().getCodigo(), false, null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaCurso().equals("unidadeEnsino")) {
                objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeUnidadeEnsino(getValorConsultaCurso(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaCurso(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
        return itens;
    }

    public void limparConsultaCurso() {
        getListaConsultaCurso().clear();
        setValorConsultaCurso("");
    }

    public void limparDadosCurso() {
        getBuscaProspectVO().setCurso(new CursoVO());
        setAutocompleteValorCurso("");
    }

    public void consultarCidade() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCidade().equals("nome")) {
                if (getValorConsultaCidade().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getCidadeFacade().consultaRapidaPorNome(getValorConsultaCidade(), false, getUsuarioLogado());
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
        getBuscaProspectVO().setCidade(obj);
		setAutocompleteValorCidade(getBuscaProspectVO().getCidade().getNome());
		setListaConsultaCidade(new ArrayList<>());
		setValorConsultaCidade("");
		setMensagemID("msg_dados_selecionados");
    }

    public List getTipoConsultaCidade() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }

    public void limparConsultaCidade() {
        getListaConsultaCidade().clear();
        setValorConsultaCidade("");
    }

    public void limparDadosCidade() {
        getBuscaProspectVO().setCidade(null);
        setAutocompleteValorCidade("");
    }

    public void limparDadosVinculadosUnidade() {
        limparDadosProspect();
        if (getPermitirConsultarProspectsOutroConsultorUltimaInteracao()) {
        	limparDadosFuncionario();
        }
        limparDadosCurso();
    }

    public void consultarFuncionario() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaFuncionario().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaFuncionario().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "", getBuscaProspectVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), getBuscaProspectVO().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("CPF")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "", getBuscaProspectVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("cargo")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), getBuscaProspectVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("departamento")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionario(), "FU", getBuscaProspectVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "FU", getBuscaProspectVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaFuncionario(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarFuncionarioResponsavel() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaFuncionarioResponsavel().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaFuncionarioResponsavel().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionarioResponsavel(), "", getBuscaProspectVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionarioResponsavel().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionarioResponsavel(), getBuscaProspectVO().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionarioResponsavel().equals("CPF")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionarioResponsavel(), "", getBuscaProspectVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionarioResponsavel().equals("cargo")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionarioResponsavel(), getBuscaProspectVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionarioResponsavel().equals("departamento")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionarioResponsavel(), "FU", getBuscaProspectVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionarioResponsavel().equals("unidadeEnsino")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionarioResponsavel(), "FU", getBuscaProspectVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaFuncionarioResponsavel(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaFuncionarioResponsavel(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
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

    public void selecionarFuncionarioResponsavel() {
    	try {
            FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioResponsavelItens");
            getBuscaProspectVO().setConsultorResponsavel(obj);
    		setAutocompleteValorConsultorResponsavel(getBuscaProspectVO().getConsultorResponsavel().getPessoa().getNome());
    		setListaConsultaFuncionarioResponsavel(new ArrayList<>());
    		setValorConsultaFuncionarioResponsavel("");
    		setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			e.printStackTrace();
		}

    }

    public void selecionarFuncionario() {
    	try {
            FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
            getBuscaProspectVO().setConsultor(obj);
    		setAutocompleteValorConsultor(getBuscaProspectVO().getConsultor().getPessoa().getNome());
    		setListaConsultaFuncionario(new ArrayList<>());
    		setValorConsultaFuncionario("");
    		setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			e.printStackTrace();
		}

    }

    public void limparConsultaFuncionario() {
        getListaConsultaFuncionario().clear();
        setValorConsultaFuncionario("");
        setValorConsultaFuncionarioResponsavel("");
    }

    public void limparDadosFuncionario() {
        getBuscaProspectVO().setConsultor(new FuncionarioVO());
        setAutocompleteValorConsultor("");
    }

    public void limparDadosFuncionarioResponsavel() {
        getBuscaProspectVO().setConsultorResponsavel(new FuncionarioVO());
        setAutocompleteValorConsultorResponsavel("");
    }

    public void limparDadosPeriodo() {
    	getBuscaProspectVO().setDataFim(null);
    	getBuscaProspectVO().setDataInicio(null);
    }
    
    public void montarListaSelectItemUnidadeEnsino() {
        try {
        	setPermitirConsultarTodasUnidades(verificarUsuarioPossuiPermissaoConsulta("BuscaProspect_consultarTodasUnidades"));
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());
        }
    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
    	List resultadoConsulta = new ArrayList<>();
        Iterator i = null;
        try {
        	if (!getPermitirConsultarTodasUnidades()) {
        		resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuarioUnidadeEnsinoVinculadaAoUsuario(getUsuarioLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        	}
            if (resultadoConsulta.isEmpty()) {
                resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            }
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            if (resultadoConsulta.size() > 1) {
                objs.add(new SelectItem(0, ""));
            }
            objs.add(new SelectItem(-1, "Sem Unidade Ensino Vinculado"));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
                if (resultadoConsulta.size() == 1) {
                    getBuscaProspectVO().setUnidadeEnsino(obj);
                }
            }
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
    	List lista = new ArrayList<>();
    	if (!getPermitirConsultarTodasUnidades()) {
    		lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());	
    	} else {
    		lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
    	}
        return lista;
    }

    public BuscaProspectVO getBuscaProspectVO() {
        if (buscaProspectVO == null) {
            buscaProspectVO = new BuscaProspectVO();
        }
        return buscaProspectVO;
    }

    public void setBuscaProspectVO(BuscaProspectVO buscaProspectVO) {
        this.buscaProspectVO = buscaProspectVO;
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

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
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

    public String getCampoConsultaProspect() {
        if (campoConsultaProspect == null) {
            campoConsultaProspect = "";
        }
        return campoConsultaProspect;
    }

    public void setCampoConsultaProspect(String campoConsultaProspect) {
        this.campoConsultaProspect = campoConsultaProspect;
    }

    public String getValorConsultaProspect() {
        if (valorConsultaProspect == null) {
            valorConsultaProspect = "";
        }
        return valorConsultaProspect;
    }

    public void setValorConsultaProspect(String valorConsultaProspect) {
        this.valorConsultaProspect = valorConsultaProspect;
    }

    public List getListaConsultaProspect() {
        if (listaConsultaProspect == null) {
            listaConsultaProspect = new ArrayList(0);
        }
        return listaConsultaProspect;
    }

    public void setListaConsultaProspect(List listaConsultaProspect) {
        this.listaConsultaProspect = listaConsultaProspect;
    }

    public List<BuscaProspectVO> getListaProspects() {
        if (listaProspects == null) {
            listaProspects = new ArrayList<BuscaProspectVO>(0);
        }
        return listaProspects;
    }

    public void setListaProspects(List<BuscaProspectVO> listaProspects) {
        this.listaProspects = listaProspects;
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

    public String getAbrirFecharModalInteracao() {
        if (abrirFecharModalInteracao == null) {
            abrirFecharModalInteracao = "";
        }
        return abrirFecharModalInteracao;
    }

    public void setAbrirFecharModalInteracao(String abrirFecharModalInteracao) {
        this.abrirFecharModalInteracao = abrirFecharModalInteracao;
    }

    public InteracaoWorkflowVO getInteracaoWorkflowVO() {
        if (interacaoWorkflowVO == null) {
            interacaoWorkflowVO = new InteracaoWorkflowVO();
        }
        return interacaoWorkflowVO;
    }

    public void setInteracaoWorkflowVO(InteracaoWorkflowVO interacaoWorkflowVO) {
        this.interacaoWorkflowVO = interacaoWorkflowVO;
    }

    public Boolean getApresentarResultadoConsultaProspects() {
        if (!getControleConsultaOtimizado().getListaConsulta().isEmpty() || !getListaProspects().isEmpty() || !getListaEncadeadaEmailProspect().equals("")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void verificarPermissaoGerarListaEmail() throws Exception {
		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("BuscaProspect_gerarListaEmail", getUsuarioLogado());
    }
    
    public Boolean getApresentarGerarListaEmail() {
    	try {
    		verificarPermissaoGerarListaEmail();
    		return Boolean.TRUE;
    	} catch (Exception e) {
    		return Boolean.FALSE;
    	}
    }    
    
    public Boolean getGerarListaEmailProspect() {
        if (gerarListaEmailProspect == null) {
            gerarListaEmailProspect = Boolean.FALSE;
        }
        return gerarListaEmailProspect;
    }

    public void setGerarListaEmailProspect(Boolean gerarListaEmailProspect) {
        this.gerarListaEmailProspect = gerarListaEmailProspect;
    }

    public String getListaEncadeadaEmailProspect() {
        if (listaEncadeadaEmailProspect == null) {
            listaEncadeadaEmailProspect = "";
        }
        return listaEncadeadaEmailProspect;
    }

    public void setListaEncadeadaEmailProspect(String listaEncadeadaEmailProspect) {
        this.listaEncadeadaEmailProspect = listaEncadeadaEmailProspect;
    }

    public InteracaoFollowUpRelControle getInteracaoFollowUpRelControle() {
        if (interacaoFollowUpRelControle == null) {
            interacaoFollowUpRelControle = new InteracaoFollowUpRelControle();
        }
        return interacaoFollowUpRelControle;
    }

    public void setInteracaoFollowUpRelControle(
            InteracaoFollowUpRelControle interacaoFollowUpRelControle) {
        this.interacaoFollowUpRelControle = interacaoFollowUpRelControle;
    }

    /**
     * @return the permitirConsultarTodasUnidades
     */
    public Boolean getPermitirConsultarTodasUnidades() {
        if (permitirConsultarTodasUnidades == null) {
            permitirConsultarTodasUnidades = Boolean.FALSE;
        }
        return permitirConsultarTodasUnidades;
    }

    /**
     * @param permitirConsultarTodasUnidades the permitirConsultarTodasUnidades to set
     */
    public void setPermitirConsultarTodasUnidades(Boolean permitirConsultarTodasUnidades) {
        this.permitirConsultarTodasUnidades = permitirConsultarTodasUnidades;
    }

    /**
     * @return the permitirConsultarProspectsOutroConsultorUltimaInteracao
     */
    public Boolean getPermitirConsultarProspectsOutroConsultorUltimaInteracao() {
        if (permitirConsultarProspectsOutroConsultorUltimaInteracao == null) {
            permitirConsultarProspectsOutroConsultorUltimaInteracao = Boolean.FALSE;
        }
        return permitirConsultarProspectsOutroConsultorUltimaInteracao;
    }

    /**
     * @param permitirConsultarProspectsOutroConsultorUltimaInteracao the permitirConsultarProspectsOutroConsultorUltimaInteracao to set
     */
    public void setPermitirConsultarProspectsOutroConsultorUltimaInteracao(Boolean permitirConsultarProspectsOutroConsultorUltimaInteracao) {
        this.permitirConsultarProspectsOutroConsultorUltimaInteracao = permitirConsultarProspectsOutroConsultorUltimaInteracao;
    }

    /**
     * @return the permitirConsultarProspectsOutroConsultorResponsavel
     */
    public Boolean getPermitirConsultarProspectsOutroConsultorResponsavel() {
        if (permitirConsultarProspectsOutroConsultorResponsavel == null) {
            permitirConsultarProspectsOutroConsultorResponsavel = Boolean.FALSE;
        }
        return permitirConsultarProspectsOutroConsultorResponsavel;
    }

    /**
     * @param permitirConsultarProspectsOutroConsultorResponsavel the permitirConsultarProspectsOutroConsultorResponsavel to set
     */
    public void setPermitirConsultarProspectsOutroConsultorResponsavel(Boolean permitirConsultarProspectsOutroConsultorResponsavel) {
        this.permitirConsultarProspectsOutroConsultorResponsavel = permitirConsultarProspectsOutroConsultorResponsavel;
    }

    /**
     * @return the campoConsultaFuncionarioResponsavel
     */
    public String getCampoConsultaFuncionarioResponsavel() {
        if (campoConsultaFuncionarioResponsavel == null) {
            campoConsultaFuncionarioResponsavel = "";
        }
        return campoConsultaFuncionarioResponsavel;
    }

    /**
     * @param campoConsultaFuncionarioResponsavel the campoConsultaFuncionarioResponsavel to set
     */
    public void setCampoConsultaFuncionarioResponsavel(String campoConsultaFuncionarioResponsavel) {
        this.campoConsultaFuncionarioResponsavel = campoConsultaFuncionarioResponsavel;
    }

    /**
     * @return the valorConsultaFuncionarioResponsavel
     */
    public String getValorConsultaFuncionarioResponsavel() {
        if (valorConsultaFuncionarioResponsavel == null) {
            valorConsultaFuncionarioResponsavel = "";
        }
        return valorConsultaFuncionarioResponsavel;
    }

    /**
     * @param valorConsultaFuncionarioResponsavel the valorConsultaFuncionarioResponsavel to set
     */
    public void setValorConsultaFuncionarioResponsavel(String valorConsultaFuncionarioResponsavel) {
        this.valorConsultaFuncionarioResponsavel = valorConsultaFuncionarioResponsavel;
    }

    /**
     * @return the listaConsultaFuncionarioResponsavel
     */
    public List getListaConsultaFuncionarioResponsavel() {
        if (listaConsultaFuncionarioResponsavel == null) {
            listaConsultaFuncionarioResponsavel = new ArrayList(0);
        }
        return listaConsultaFuncionarioResponsavel;
    }

    /**
     * @param listaConsultaFuncionarioResponsavel the listaConsultaFuncionarioResponsavel to set
     */
    public void setListaConsultaFuncionarioResponsavel(List listaConsultaFuncionarioResponsavel) {
        this.listaConsultaFuncionarioResponsavel = listaConsultaFuncionarioResponsavel;
    }

    public void editarProspect() throws Exception {
        try {
            BuscaProspectVO buscaVO = (BuscaProspectVO) context().getExternalContext().getRequestMap().get("buscaProspectItens");
            ProspectsVO prospectsVO = buscaVO.getProspectNovaInteracao();
            context().getExternalContext().getSessionMap().put("prospectsVOItens", prospectsVO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarCompromissoAgendaProspect() {
        BuscaProspectVO buscaVO = (BuscaProspectVO) context().getExternalContext().getRequestMap().get("buscaProspectItens");
        try {
            setBuscaProspectVO(new BuscaProspectVO());
            buscaVO.setCompromissoAgendaPessoaHorarioVOs(getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarTodosCompromissoPorCodigoProspect(buscaVO.getProspectNovaInteracao().getCodigo()));
            setBuscaProspectVO(buscaVO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String selecionarCompromisso() {
        CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO = (CompromissoAgendaPessoaHorarioVO) context().getExternalContext().getRequestMap().get("compromissoItens");
        try {
            context().getExternalContext().getSessionMap().put("compromissoAgendaPessoaHorarioVO", compromissoAgendaPessoaHorarioVO);
            return Uteis.getCaminhoRedirecionamentoNavegacao("agendaPessoaCons.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    /**
     * @return the comunicacaoEnviar
     */
    public ComunicacaoInternaVO getComunicacaoEnviar() {
        if (comunicacaoEnviar == null) {
            comunicacaoEnviar = (inicializarDadosPadrao(new ComunicacaoInternaVO()));
        }
        return comunicacaoEnviar;
    }

    /**
     * @param comunicacaoEnviar the comunicacaoEnviar to set
     */
    public void setComunicacaoEnviar(ComunicacaoInternaVO comunicacaoEnviar) {
        this.comunicacaoEnviar = comunicacaoEnviar;
    }

    /**
     * @return the mensagemTemplate
     */
    public PersonalizacaoMensagemAutomaticaVO getMensagemTemplate() {
        return mensagemTemplate;
    }

    /**
     * @param mensagemTemplate the mensagemTemplate to set
     */
    public void setMensagemTemplate(PersonalizacaoMensagemAutomaticaVO mensagemTemplate) {
        this.mensagemTemplate = mensagemTemplate;
    }
    
    public void consultarMatriculaProspect(){
    	try {
    		setBuscaProspectVO(new BuscaProspectVO());
    		setBuscaProspectVO((BuscaProspectVO) getRequestMap().get("buscaProspectItens"));
            if(getBuscaProspectVO().getMatriculaVOs().isEmpty()){
            	getBuscaProspectVO().setMatriculaVOs(getFacadeFactory().getMatriculaFacade().consultarMatriculaProspectApresentarCompromisso(getBuscaProspectVO().getProspectNovaInteracao().getCodigo(), null, getUsuarioLogado()));
            }
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }

    }
	
    
    public void verificaExcluirProspect() {
		try {
            ProspectsControle prospectsControle = (ProspectsControle) context().getExternalContext().getSessionMap().get(ProspectsControle.class.getSimpleName());
            BuscaProspectVO buscaVO = (BuscaProspectVO) context().getExternalContext().getRequestMap().get("buscaProspectItens");
            setProspect(buscaVO.getProspectNovaInteracao());			
			setMensagemAvisoExclusao(getFacadeFactory().getProspectsFacade().consultarAgendaProspect(getProspect().getCodigo()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
	}

	public String excluirProspect() {
		try {
			getFacadeFactory().getProspectsFacade().excluirProspectERegistrosReferenciados(getProspect(), true, getUsuarioLogado());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
			consultarProspects();
			return Uteis.getCaminhoRedirecionamentoNavegacao("buscaProspect.xhtml");
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			return Uteis.getCaminhoRedirecionamentoNavegacao("buscaProspect.xhtml");
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

	public ProspectsVO getProspect() {
		if (prospect == null) {
			prospect = new ProspectsVO();
		}
		return prospect;
	}

	public void setProspect(ProspectsVO prospect) {
		this.prospect = prospect;
	}    
	
    public String getAutocompleteValorCurso() {
		if (autocompleteValorCurso == null) {
			autocompleteValorCurso = "";
		}
		return autocompleteValorCurso;
	}

	public void setAutocompleteValorCurso(String autocompleteValorCurso) {
		this.autocompleteValorCurso = autocompleteValorCurso;
	}
	
	public String getAutocompleteValorPropspect() {
		if (autocompleteValorPropspect == null) {
			autocompleteValorPropspect = "";
		}
		return autocompleteValorPropspect;
	}

	public void setAutocompleteValorPropspect(String autocompleteValorPropspect) {
		this.autocompleteValorPropspect = autocompleteValorPropspect;
	}
	
	public String getAutocompleteValorConsultor() {
		if (autocompleteValorConsultor == null) {
			autocompleteValorConsultor = "";
		}
		return autocompleteValorConsultor;
	}

	public void setAutocompleteValorConsultor(String autocompleteValorConsultor) {
		this.autocompleteValorConsultor = autocompleteValorConsultor;
	}
	
	public String getAutocompleteValorEmail() {
		if (autocompleteValorEmail == null) {
			autocompleteValorEmail = "";
		}
		return autocompleteValorEmail;
	}

	public void setAutocompleteValorEmail(String autocompleteValorEmail) {
		this.autocompleteValorEmail = autocompleteValorEmail;
	}
	
	public String getAutocompleteValorConsultorResponsavel() {
		if (autocompleteValorConsultorResponsavel == null) {
			autocompleteValorConsultorResponsavel = "";
		}
		return autocompleteValorConsultorResponsavel;
	}

	public void setAutocompleteValorConsultorResponsavel(String autocompleteValorConsultorResponsavel) {
		this.autocompleteValorConsultorResponsavel = autocompleteValorConsultorResponsavel;
	}
	
	public void selecionarConsultorPorCodigo() {
		if (!getAutocompleteValorConsultor().isEmpty()) {
			consultarConsultorPorCodigo(getValorAutoComplete(getAutocompleteValorConsultor()));
		}
	}

	public void consultarConsultorPorCodigo(int codigo) {
		try {
			FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(codigo, null, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getBuscaProspectVO().setConsultor(funcionarioVO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarConsultorResponsavelPorCodigo() {
		if (!getAutocompleteValorConsultorResponsavel().isEmpty()) {
			consultarConsultorResponsavelPorCodigo(getValorAutoComplete(getAutocompleteValorConsultorResponsavel()));
		}
	}
	
	public void consultarConsultorResponsavelPorCodigo(int codigo) {
		try {
			FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(codigo, null, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getBuscaProspectVO().setConsultorResponsavel(funcionarioVO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarProspectPorCodigo() {
		if (!getAutocompleteValorPropspect().isEmpty()) {
			consultarProspectPorCodigo(getValorAutoComplete(getAutocompleteValorPropspect()));
		}
	}

	public void consultarProspectPorCodigo(int codigo) {
		try {
			ProspectsVO prospectsVO = getFacadeFactory().getProspectsFacade().consultarPorChavePrimaria(codigo, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getBuscaProspectVO().setProspect(prospectsVO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarCursoPorCodigo() {
		if (!getAutocompleteValorCurso().isEmpty()) {
			consultarCursoPorCodigo(getValorAutoComplete(getAutocompleteValorCurso()));
		}
	}

	public void consultarCursoPorCodigo(int codigo) {
		try {
			CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(codigo, Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado());
			getBuscaProspectVO().setCurso(curso);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarCidadePorCodigo() {
		if (!getAutocompleteValorCidade().isEmpty()) {
			consultarCidadePorCodigo(getValorAutoComplete(getAutocompleteValorCidade()));
		}
	}

	public void consultarCidadePorCodigo(int codigo) {
		try {
			CidadeVO cidade = getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(codigo, false, getUsuarioLogado());
			getBuscaProspectVO().setCidade(cidade);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String getAutocompleteValorCidade() {
		if (autocompleteValorCidade == null) {
			autocompleteValorCidade = "";
		}
		return autocompleteValorCidade;
	}

	public void setAutocompleteValorCidade(String autocompleteValorCidade) {
		this.autocompleteValorCidade = autocompleteValorCidade;
	}
	
	public void selecionarCurso() throws Exception {
		try {
            UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
            getBuscaProspectVO().setCurso(obj.getCurso());
			setAutocompleteValorCurso(getBuscaProspectVO().getCurso().getNome());
			setListaConsultaCurso(new ArrayList<>());
			setValorConsultaCurso("");
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private int getValorAutoComplete(String valor) {
		if (valor != null) {
			java.util.regex.Pattern p = java.util.regex.Pattern.compile("^.*\\((-?\\d+)\\)[ \\t]*$");
			java.util.regex.Matcher m = p.matcher(valor);
			try {
				if (m.matches()) {
					// save the entity id in the managed bean and strip the
					// entity id from the suggested string
					valor = valor.substring(0, valor.lastIndexOf('('));
					return Integer.parseInt(m.group(1));
				}
			} catch (java.lang.NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	
	/**
	 * 
	 * Sincroniza todos os prospects na lista que ainda nao estao sincronizados com o RD Station
	 * 
	 */
	public void sincronizarTodosProspectsRdStation() {
    	
		try {
			
			ArrayList<ProspectsVO> prospectsVos = filtrarProspectsQueSeraoSincronizados((ArrayList<BuscaProspectVO>) getControleConsultaOtimizado().getListaConsulta());
			
			if(prospectsVos.isEmpty()) {
				setMensagemID("msg_dados_nenhum_registro");
			} else {
				setMensagemID("msg_SolicitacaoProcessadaEmSegundoPlano", Uteis.SUCESSO);
				enviarTodosProspectsRdStation(prospectsVos);
				consultar();
			}
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
 
    }
	
	public ArrayList<ProspectsVO> filtrarProspectsQueSeraoSincronizados(ArrayList<BuscaProspectVO> listaCompleta){
		
		ArrayList<ProspectsVO> prospectsVos = new ArrayList<>();
		
		for(BuscaProspectVO busca : listaCompleta) {
			if(!busca.getProspectNovaInteracao().getSincronizadoRDStation())
				prospectsVos.add(busca.getProspectNovaInteracao());
		}
		
		return prospectsVos;
		
	}

	/**
	 * Envia a lista de prospects para sincronizacao com o RD Station
	 * @param prospectsVos
	 */
    public void enviarTodosProspectsRdStation(List<ProspectsVO> prospectsVos) {
    	getFacadeFactory().getLeadInterfaceFacade().incluirListaDeLeadsNoRdStation(prospectsVos, getConfiguracaoGeralPadraoSistema());
    }

	public String getObservacaoCompleta() {
		if (observacaoCompleta == null) {
			observacaoCompleta = "";
		}
		return observacaoCompleta;
	}

	public void setObservacaoCompleta(String observacaoCompleta) {
		this.observacaoCompleta = observacaoCompleta;
	}
	
    public String getNomeProspect() {
		if (nomeProspect == null) {
			nomeProspect = "";
		}
		return nomeProspect;
	}

	public void setNomeProspect(String nomeProspect) {
		this.nomeProspect = nomeProspect;
	}
	
	public String getSituacaoProspect() {
		if (situacaoProspect == null) {
			situacaoProspect = "";
		}
		return situacaoProspect;
	}

	public void setSituacaoProspect(String situacaoProspect) {
		this.situacaoProspect = situacaoProspect;
	}

	public void realizarMontagemObservacaoCompleta() {
		String situacao = "Descrição não informada";
		String obs = "Observação não informada.";
		String nome = "Nome não informado";
    	try {
    		BuscaProspectVO bpVO = (BuscaProspectVO) context().getExternalContext().getRequestMap().get("buscaProspectItens");
    		if (bpVO != null) {
    			setSituacaoProspect(bpVO.getDescricaoSituacaoProspect().isEmpty() ? situacao : bpVO.getDescricaoSituacaoProspect());
    			setObservacaoCompleta(bpVO.getObservacao().isEmpty() ? obs : bpVO.getObservacao());
    			setNomeProspect(bpVO.getNomeProspect().isEmpty() ? nome : bpVO.getNomeProspect());
    		} else {
    			setSituacaoProspect(situacao);
    			setObservacaoCompleta(obs);
    			setNomeProspect(nome);
    		}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
	
	public boolean getApresentarLetraASelecionada() {
		return getLetra().equalsIgnoreCase("A");
	}
	
	public boolean getApresentarLetraBSelecionada() {
		return getLetra().equalsIgnoreCase("B");
	}

	public boolean getApresentarLetraCSelecionada() {
		return getLetra().equalsIgnoreCase("C");
	}

	public boolean getApresentarLetraDSelecionada() {
		return getLetra().equalsIgnoreCase("D");
	}
	

	public boolean getApresentarLetraESelecionada() {
		return getLetra().equalsIgnoreCase("E");
	}

	public boolean getApresentarLetraFSelecionada() {
		return getLetra().equalsIgnoreCase("F");
	}

	public boolean getApresentarLetraGSelecionada() {
		return getLetra().equalsIgnoreCase("G");
	}

	public boolean getApresentarLetraHSelecionada() {
		return getLetra().equalsIgnoreCase("H");
	}

	public boolean getApresentarLetraISelecionada() {
		return getLetra().equalsIgnoreCase("I");
	}

	public boolean getApresentarLetraJSelecionada() {
		return getLetra().equalsIgnoreCase("J");
	}

	public boolean getApresentarLetraKSelecionada() {
		return getLetra().equalsIgnoreCase("K");
	}

	public boolean getApresentarLetraLSelecionada() {
		return getLetra().equalsIgnoreCase("L");
	}

	public boolean getApresentarLetraMSelecionada() {
		return getLetra().equalsIgnoreCase("M");
	}

	public boolean getApresentarLetraNSelecionada() {
		return getLetra().equalsIgnoreCase("N");
	}

	public boolean getApresentarLetraOSelecionada() {
		return getLetra().equalsIgnoreCase("O");
	}

	public boolean getApresentarLetraPSelecionada() {
		return getLetra().equalsIgnoreCase("P");
	}

	public boolean getApresentarLetraQSelecionada() {
		return getLetra().equalsIgnoreCase("Q");
	}

	public boolean getApresentarLetraRSelecionada() {
		return getLetra().equalsIgnoreCase("R");
	}

	public boolean getApresentarLetraSSelecionada() {
		return getLetra().equalsIgnoreCase("S");
	}

	public boolean getApresentarLetraTSelecionada() {
		return getLetra().equalsIgnoreCase("T");
	}

	public boolean getApresentarLetraUSelecionada() {
		return getLetra().equalsIgnoreCase("U");
	}

	public boolean getApresentarLetraVSelecionada() {
		return getLetra().equalsIgnoreCase("V");
	}

	public boolean getApresentarLetraXSelecionada() {
		return getLetra().equalsIgnoreCase("X");
	}

	public boolean getApresentarLetraWSelecionada() {
		return getLetra().equalsIgnoreCase("W");
	}
	
	public boolean getApresentarLetraYSelecionada() {
		return getLetra().equalsIgnoreCase("Y");
	}
	
	public boolean getApresentarLetraZSelecionada() {
		return getLetra().equalsIgnoreCase("Z");
	}
	
	private Boolean versaoAntiga;
	private Boolean versaoNova;
	
	public Boolean getVersaoAntiga() {
		if (versaoAntiga == null) {
			versaoAntiga = false;
		}
		return versaoAntiga;
	}
	
	public void setVersaoAntiga(Boolean versaoAntiga) {
		this.versaoAntiga = versaoAntiga;
	}
	
	public Boolean getVersaoNova() {
		if (versaoNova == null) {
			versaoNova = false;
		}
		return versaoNova;
	}
	
	public void setVersaoNova(Boolean versaoNova) {
		this.versaoNova = versaoNova;
	}
	
	public void mudarLayoutConsulta() {
		setVersaoAntiga(true);
		setVersaoNova(false);
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getVersaoNova().toString(), Prospects.class.getName()+"_"+getUsuarioLogado().getCodigo(), "VersaoNova", getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void mudarLayoutConsulta2() {
		setVersaoAntiga(false);
		setVersaoNova(true);
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getVersaoNova().toString(), Prospects.class.getName()+"_"+getUsuarioLogado().getCodigo(), "VersaoNova", getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}