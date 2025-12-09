package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas ReativacaoMatriculaForm.jsp ReativacaoMatriculaCons.jsp) com as funcionalidades da
 * classe <code>ReativacaoMatricula</code>. Implemtação da camada controle (Backing
 * Bean).
 *
 * @see SuperControle
 * @see ReativacaoMatricula
 * @see ReativacaoMatriculaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ReativacaoMatriculaVO;
import negocio.comuns.academico.TrancamentoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.arquitetura.OperacaoFuncionalidadeVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoRequerimento;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados; @Controller("ReativacaoMatriculaControle")
@Scope("viewScope")
@Lazy
public class ReativacaoMatriculaControle extends SuperControle implements Serializable {

    private ReativacaoMatriculaVO reativacaoMatriculaVO;
    private String matricula_Erro;
    private String requerimento_Erro;
    private String responsavelAutorizacao_Erro;
    private String requerimento_valorApresentar;
    private Boolean avisarExclusao;
    private Boolean avisarAlteracao;
    private Boolean avisarInclusao;
    private String apresentarRichMensagem;
    private boolean exibirBotoesSituacaoRequerimento;
    private List listaConsultaAluno;
    private String campoConsultaAluno;
    private String valorConsultaAluno;
    private String userNameLiberarOperacaoFuncionalidade;
    private String senhaLiberarOperacaoFuncionalidade;
    private Boolean possuiPermissaoLiberarReativacaoMatriculaNumeroPeriodoUltrapassadoAoConfigurado;
    private String oncompleteOperacaoFuncionalidade;
    private Boolean apresentarBotaoLiberarFuncionalidade;

    public ReativacaoMatriculaControle() throws Exception {
        //obterUsuarioLogado();
        setExibirBotoesSituacaoRequerimento(false);
        setControleConsulta(new ControleConsulta());
        setControleConsultaOtimizado(new DataModelo());
        getControleConsultaOtimizado().setCampoConsulta("nomePessoa");
        setAvisarExclusao(false);
        setAvisarAlteracao(false);
        setAvisarInclusao(false);
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe
     * <code>ReativacaoMatricula</code> para edição pelo usuário da aplicação.
     */
	public String novo() {
		removerObjetoMemoria(this);
		setReativacaoMatriculaVO(new ReativacaoMatriculaVO());
		setRequerimento_valorApresentar("");
		setRequerimento_Erro("");
		setApresentarRichMensagem("");
		inicializarUsuarioResponsavelTransferenciaSaidaUsuarioLogado();
		setTipoRequerimento("RM");
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("reativacaoMatriculaForm.xhtml");
	}

    public void inicializarUsuarioResponsavelTransferenciaSaidaUsuarioLogado() {
        try {
            reativacaoMatriculaVO.setResponsavelAutorizacao(getUsuarioLogadoClone());
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe
     * <code>ReativacaoMatricula</code> para alteração. O objeto desta classe é
     * disponibilizado na session da página (request) para que o JSP
     * correspondente possa disponibilizá-lo para edição.
     */
    public String editar() {
        try {
            ReativacaoMatriculaVO obj = (ReativacaoMatriculaVO) context().getExternalContext().getRequestMap().get("reativacaoItens");
            obj = getFacadeFactory().getReativacaoMatriculaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(),getConfiguracaoFinanceiroPadraoSistema());
            setRequerimento_valorApresentar("");
            setRequerimento_Erro("");
            obj.setNovoObj(Boolean.FALSE);
            setReativacaoMatriculaVO(obj);
            setTipoRequerimento("RM");
            if (((obj.getRequerimento().getCodigo().equals(0) && obj.getSituacao().equals("EX")) || (!obj.getRequerimento().getCodigo().equals(0) && obj.getRequerimento().getSituacaoFinanceira().equals("PG")) && obj.getRequerimento().getSituacao().equals("EX"))) {
                setExibirBotoesSituacaoRequerimento(false);
            } else {
                setExibirBotoesSituacaoRequerimento(true);
            }
            setMensagemID("msg_dados_editar");
            return Uteis.getCaminhoRedirecionamentoNavegacao("reativacaoMatriculaForm.xhtml");
        } catch (Exception e) {
            setExibirBotoesSituacaoRequerimento(true);
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("reativacaoMatriculaCons.xhtml");
        }
    }

    public String validarGravar() {
        if (getReativacaoMatriculaVO().isNovoObj().booleanValue()) {
            setAvisarAlteracao(false);
            setAvisarExclusao(false);
            setAvisarInclusao(true);
            // setTextoRichModalAviso("Aviso: Ao gravar este ReativacaoMatricula, ele não poderá ser alterado ou excluido!");
        } else {
            setAvisarAlteracao(true);
            setAvisarExclusao(false);
            setAvisarInclusao(false);
        }
        return "";
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto
     * da classe <code>ReativacaoMatricula</code>. Caso o objeto seja novo (ainda não
     * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
     * contrário é acionado o <code>alterar()</code>. Se houver alguma
     * inconsistência o objeto não é gravado, sendo re-apresentado para o
     * usuário juntamente com uma mensagem de erro.
     */
    public void gravar() {
        try {
            if (getReativacaoMatriculaVO().isNovoObj().booleanValue()) {
            	if (!getReativacaoMatriculaVO().getMatricula().getCurso().getConfiguracaoAcademico().getCodigo().equals(0)) {
            		getReativacaoMatriculaVO().getMatricula().getCurso().setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(getReativacaoMatriculaVO().getMatricula().getCurso().getConfiguracaoAcademico().getCodigo(), getUsuarioLogado()));
            	}
                if (getReativacaoMatriculaVO().getMatricula().getCurso().getConfiguracaoAcademico().getDiasMaximoReativacaoMatricula() > 0) {
                    verificarPeriodoReativacaoMatricula(getReativacaoMatriculaVO());
                }
                if (getReativacaoMatriculaVO().getMatricula().getSituacao().equals(SituacaoVinculoMatricula.TRANCADA.getValor()) && getReativacaoMatriculaVO().getMatricula().getCurso().getConfiguracaoAcademico().getQuantidadeMaximaPeriodoTrancadoPermiteReativacaoMatricula() > 0 && !getPossuiPermissaoLiberarReativacaoMatriculaNumeroPeriodoUltrapassadoAoConfigurado()) {
                	validarDadosQuantidadeMaximaPeriodoTrancamentoParaRealizarRenovacaoDeAcordoQuantidadeDefinidaConfiguracaoAcademico(getReativacaoMatriculaVO().getMatricula().getCurso().getConfiguracaoAcademico());
                }
                getReativacaoMatriculaVO().setSituacao("EX");
                getFacadeFactory().getReativacaoMatriculaFacade().incluir(getReativacaoMatriculaVO(), getUsuarioLogado(), getLoginControle().getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getReativacaoMatriculaVO().getMatricula().getUnidadeEnsino().getCodigo()).getPermiteReativacaoMatriculaSemRequerimento());
               
                if(getReativacaoMatriculaVO().getRequerimento().getCodigo() > 0) {
                    getFacadeFactory().getRequerimentoFacade().alterarSituacaoFinanceiraESituacaoExecucao(getReativacaoMatriculaVO().getRequerimento().getCodigo(), false, "EX", "PG", false, getUsuarioLogado());
                    getReativacaoMatriculaVO().getRequerimento().setSituacao("EX");
                }

                if (getPossuiPermissaoLiberarReativacaoMatriculaNumeroPeriodoUltrapassadoAoConfigurado()) {
	                OperacaoFuncionalidadeVO operacaoFuncionalidadeVO = getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.REATIVACAO_MATRICULA, getReativacaoMatriculaVO().getMatricula().getMatricula(), OperacaoFuncionalidadeEnum.LIBERACAO_REATIVAR_MATRICULA_NUMERO_PERIODO_ULTRAPASSADO_AO_CONFIGURADO, getUsuarioLogado(), "");
	                getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(operacaoFuncionalidadeVO);
                }
            } else {
                if (getReativacaoMatriculaVO().getMatricula().getCurso().getConfiguracaoAcademico().getDiasMaximoReativacaoMatricula() > 0) {
                    verificarPeriodoReativacaoMatricula(getReativacaoMatriculaVO());
                }
                getFacadeFactory().getReativacaoMatriculaFacade().alterar(getReativacaoMatriculaVO(), getUsuarioLogado(), getLoginControle().getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getReativacaoMatriculaVO().getMatricula().getUnidadeEnsino().getCodigo()).getPermiteReativacaoMatriculaSemRequerimento());
            }
            setMensagemID("msg_dados_gravados");
            setMensagemDetalhada("Deseja AUTORIZAR ou INDEFERIR a reativação de matrícula? Escolha nos botões abaixo.");
            setExibirBotoesSituacaoRequerimento(false);
        } catch (Exception e) {
            setExibirBotoesSituacaoRequerimento(true);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP
     * ReativacaoMatriculaCons.jsp. Define o tipo de consulta a ser executada, por meio
     * de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
     * resultado, disponibiliza um List com os objetos selecionados na sessao da
     * pagina.
     */
    @Override
    public String consultar() {
        try {
            super.consultar();
            getControleConsultaOtimizado().getListaConsulta().clear();
			getControleConsultaOtimizado().setLimitePorPagina(10);
            if (getControleConsultaOtimizado().getCampoConsulta().equals("codigo")) {
                if (getControleConsultaOtimizado().getValorConsulta().equals("")) {
                	getControleConsultaOtimizado().setValorConsulta("0");
                }
                Integer valorInt = Integer.parseInt(getControleConsultaOtimizado().getValorConsulta());
                getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getReativacaoMatriculaFacade().consultarReativacaoMatricula(valorInt.toString(), null, null, getControleConsultaOtimizado(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema()));
            } else if (getControleConsultaOtimizado().getCampoConsulta().equals("data")) {
                Date valorData = Uteis.getDate(getControleConsultaOtimizado().getValorConsulta());
                getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getReativacaoMatriculaFacade().consultarReativacaoMatricula("", Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getControleConsultaOtimizado(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema()));
            } else {
            	if (!getControleConsultaOtimizado().getCampoConsulta().equals("codigoRequerimento") && getControleConsultaOtimizado().getValorConsulta().length() < 2) {
            		throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
            	}
            	getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getReativacaoMatriculaFacade().consultarReativacaoMatricula(getControleConsultaOtimizado().getValorConsulta(), null, null, getControleConsultaOtimizado(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema()));
            }
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("reativacaoMatriculaCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("reativacaoMatriculaCons.xhtml");
        }
    }
    
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}

    /**
     * Operação responsável por processar a exclusão um objeto da classe
     * <code>ReativacaoMatriculaVO</code> Após a exclusão ela automaticamente aciona a
     * rotina para uma nova inclusão.
     */
    public String excluir() {
        setAvisarExclusao(true);
        setAvisarAlteracao(false);
        setAvisarInclusao(false);
        return "";
    }

    public void irPaginaInicial() throws Exception {
    	getControleConsultaOtimizado().setPaginaAtual(1);
        this.consultar();
    }

    public void irPaginaAnterior() throws Exception {
    	getControleConsultaOtimizado().setPaginaAtual(getControleConsultaOtimizado().getPaginaAtual() - 1);
        this.consultar();
    }

    public void irPaginaPosterior() throws Exception {
    	getControleConsultaOtimizado().setPaginaAtual(getControleConsultaOtimizado().getPaginaAtual() + 1);
        this.consultar();
    }

//    public void irPaginaFinal() throws Exception {
//    	getControleConsultaOtimizado().setPaginaAtual(getControleConsultaOtimizado().getNrTotalPaginas());
//        this.consultar();
//    }

    public List getListaSelectItemSituacaoFinalRequerimento() throws Exception {
        return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoRequerimento.class);
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do
     * ComboBox correspondente ao atributo <code>tipoJustificativa</code>
     */
    public List getListaSelectItemTipoJustificativaReativacao() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoJustificativaAlteracaoMatriculas = (Hashtable) Dominios.getTipoJustificativaAlteracaoMatricula();
        Enumeration keys = tipoJustificativaAlteracaoMatriculas.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoJustificativaAlteracaoMatriculas.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do
     * ComboBox correspondente ao atributo <code>situacao</code>
     */
    public List getListaSelectItemSituacaoReativacao() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable situacaoAcademicoReativacao = (Hashtable) Dominios.getSituacaoProtocolo();
        Enumeration keys = situacaoAcademicoReativacao.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) situacaoAcademicoReativacao.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /**
     * Método responsável por processar a consulta na entidade
     * <code>Pessoa</code> por meio de sua respectiva chave primária. Esta
     * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
     * busca pela chave primária da entidade montando automaticamente o
     * resultado da consulta para apresentação.
     */
    public void consultarPessoaPorChavePrimaria() {
        try {
            Integer campoConsulta = getReativacaoMatriculaVO().getResponsavelAutorizacao().getCodigo();
            PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            getReativacaoMatriculaVO().getResponsavelAutorizacao().setNome(pessoa.getNome());
            this.setResponsavelAutorizacao_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            getReativacaoMatriculaVO().getResponsavelAutorizacao().setNome("");
            getReativacaoMatriculaVO().getResponsavelAutorizacao().setCodigo(0);
            this.setResponsavelAutorizacao_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Método responsável por processar a consulta na entidade
     * <code>Requerimento</code> por meio de sua respectiva chave primária. Esta
     * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
     * busca pela chave primária da entidade montando automaticamente o
     * resultado da consulta para apresentação.
     */
    public void consultarRequerimentoPorChavePrimaria() {
        try {
            Integer campoConsulta = getReativacaoMatriculaVO().getRequerimento().getCodigo();
            RequerimentoVO requerimento = getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimariaFiltrandoPorUnidadeEnsino(campoConsulta, "RM", super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
            getReativacaoMatriculaVO().validarSituacaoRequerimento(requerimento);
            getReativacaoMatriculaVO().setRequerimento(requerimento);
            List<TrancamentoVO> listaTrancamento = getFacadeFactory().getTrancamentoFacade().consultarPorMatriculaSituacaoFinanceira(requerimento.getMatricula().getMatricula(), "PG", 1, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(),getUsuarioLogado());
            if (listaTrancamento.size() > 0) {
                getReativacaoMatriculaVO().setTrancamento(listaTrancamento.get(0));
            }
            getReativacaoMatriculaVO().setMatricula(requerimento.getMatricula());
//            boolean existePendenciaFinanceira = getFacade().getContaReceber().consultarExistenciaPendenciaFinanceiraMatricula(getReativacaoMatriculaVO().getMatricula().getMatricula());
//            String mensagem = "";
//            if (existePendenciaFinanceira) {
//                mensagem = getReativacaoMatriculaVO().getMatricula().validaMatriculaLiberadaFinanceiroCancelamentoTrancamento();
//            }
//            if (!mensagem.equals("")) {
//                getReativacaoMatriculaVO().setRequerimento(new RequerimentoVO());
//                getReativacaoMatriculaVO().setMatricula(new MatriculaVO());
//                setApresentarRichMensagem("Richfaces.showModalPanel('panelMensagem')");
//            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            getReativacaoMatriculaVO().getRequerimento().setCodigo(0);
            getReativacaoMatriculaVO().getRequerimento().setData(null);
            getReativacaoMatriculaVO().getMatricula().setMatricula("");
            getReativacaoMatriculaVO().getMatricula().getAluno().setNome("");
            setMensagemDetalhada("msg_erro", e.getMessage());
            setApresentarRichMensagem("");
        }
    }

    public void selecionarRequerimento() {
        try {
            RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItens");
            obj = getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
            getReativacaoMatriculaVO().validarSituacaoRequerimento(obj);
            getReativacaoMatriculaVO().setRequerimento(obj);
            List<TrancamentoVO> listaTrancamento = getFacadeFactory().getTrancamentoFacade().consultarPorMatriculaSituacaoFinanceira(obj.getMatricula().getMatricula(), "PG", 1, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            if (listaTrancamento.size() > 0) {
                getReativacaoMatriculaVO().setTrancamento(listaTrancamento.get(0));
            }
            getReativacaoMatriculaVO().setMatricula(obj.getMatricula());
//            boolean existePendenciaFinanceira = getFacade().getContaReceber().consultarExistenciaPendenciaFinanceiraMatricula(getReativacaoMatriculaVO().getMatricula().getMatricula());
//            String mensagem = "";
//            if (existePendenciaFinanceira) {
//                mensagem = getReativacaoMatriculaVO().getMatricula().validaMatriculaLiberadaFinanceiroCancelamentoTrancamento();
//            }
//            if (!mensagem.equals("")) {
//                getReativacaoMatriculaVO().setRequerimento(new RequerimentoVO());
//                getReativacaoMatriculaVO().setMatricula(new MatriculaVO());
//                setApresentarRichMensagem("Richfaces.showModalPanel('panelMensagem')");
//            } else {
//                setApresentarRichMensagem("");
//            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            getReativacaoMatriculaVO().getRequerimento().setCodigo(0);
            getReativacaoMatriculaVO().getRequerimento().setData(null);
            getReativacaoMatriculaVO().getMatricula().setMatricula("");
            getReativacaoMatriculaVO().getMatricula().getAluno().setNome("");
            setApresentarRichMensagem("");
        }
    }

    public void deferirRequerimento() throws Exception {
    	try {
    		getFacadeFactory().getReativacaoMatriculaFacade().deferirRequerimento(getReativacaoMatriculaVO(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
    		setMensagemDetalhada("Reativação Deferida, matrícula " + getReativacaoMatriculaVO().getMatricula().getMatricula() + " reativada com SUCESSO.");
    		setExibirBotoesSituacaoRequerimento(true);
    		getReativacaoMatriculaVO().getMatricula().setSituacao(SituacaoMatriculaPeriodoEnum.ATIVA.getValor());
		} catch (Exception e) {
			setMensagemDetalhada("Não foi possível deferir a reativação da matrícula.");
		}
    }

    public void indeferirRequerimento() throws Exception {
        getFacadeFactory().getReativacaoMatriculaFacade().indeferirRequerimento(getReativacaoMatriculaVO());
        setMensagemDetalhada("Reativação Indeferida com SUCESSO.");
        setExibirBotoesSituacaoRequerimento(true);
    }

    /**
     * Método responsável por processar a consulta na entidade
     * <code>Matricula</code> por meio de sua respectiva chave primária. Esta
     * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
     * busca pela chave primária da entidade montando automaticamente o
     * resultado da consulta para apresentação.
     */
    public void consultarMatriculaPorChavePrimaria() {
        try {
            String campoConsulta = getReativacaoMatriculaVO().getMatricula().getMatricula();
            MatriculaVO matricula = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(campoConsulta, this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            getReativacaoMatriculaVO().getMatricula().setMatricula(matricula.getMatricula());
            this.setMatricula_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            getReativacaoMatriculaVO().getMatricula().setMatricula("");
            getReativacaoMatriculaVO().getMatricula().setMatricula("");
            this.setMatricula_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    public String getMascaraConsulta() {
        if (getControleConsultaOtimizado().getCampoConsulta().equals("data")) {
            return "return mascara(this.form,'formCadastro:valorConsulta','99/99/9999',event);";
        }
        return "";
    }

    public Boolean getConsultarPorTipoJustificativa() {
        if (getControleConsultaOtimizado().getCampoConsulta().equals("tipoJustificativa")) {
            return true;
        }
        return false;
    }

    public Boolean getConsultarPorSituacao() {
        if (getControleConsultaOtimizado().getCampoConsulta().equals("situacao")) {
            return true;
        }
        return false;
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        
        itens.add(new SelectItem("nomeAluno", "Nome Aluno"));
        itens.add(new SelectItem("matriculaMatricula", "Matrícula"));
        itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
        itens.add(new SelectItem("data", "Data"));
        // itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("situacao", "Situação"));
        itens.add(new SelectItem("codigoRequerimento", "Código Requerimento"));
//        itens.add(new SelectItem("tipoJustificativa", "Tipo Justificativa"));
        itens.add(new SelectItem("nomePessoa", "Responsável Autorizacão"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes
     * de uma consulta.
     */
    public String inicializarConsultar() {         removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
//        definirVisibilidadeLinksNavegacao(0, 0);
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("reativacaoMatriculaCons.xhtml");
    }

    public void verificarPeriodoReativacaoMatricula(ReativacaoMatriculaVO reativacaoMatriculaVo) throws Exception {
        Integer dias = 0;
        dias = getFacadeFactory().getConfiguracaoAcademicoFacade().verificarPeriodoReativacaoMatricula(reativacaoMatriculaVo.getMatricula().getCurso().getConfiguracaoAcademico().getDiasMaximoReativacaoMatricula(), reativacaoMatriculaVo.getTrancamento().getData(), new Date(), getUsuarioLogado());
        if (dias < 0) {
            throw new ConsistirException("O número máximo de dias para reativação da matrícula expirou!");
        }
    }

    public String renovarMatriculaReativada() throws Exception {
        try {
        	if (Uteis.getObterDiferencaDiasEntreDuasData(new Date(), getReativacaoMatriculaVO().getData()) > 60) {
        		throw new ConsistirException("Reativação expirada! Inicie um novo processo de reativação para a matrícula.");
        	}
            if (!getReativacaoMatriculaVO().getMatricula().getSituacao().equals(SituacaoMatriculaPeriodoEnum.ATIVA.getValor())) {
        		throw new ConsistirException("Esta matrícula não se encontra ativa! Para renová-la é necessário iniciar um novo processo de reativação.");
        	} else {
        		Integer codigo = getFacadeFactory().getReativacaoMatriculaFacade().codigoUltimaReativacaoPorMatricula(getReativacaoMatriculaVO().getMatricula().getMatricula());
        		if (!codigo.equals(getReativacaoMatriculaVO().getCodigo())) {
        			throw new ConsistirException("Não foi possível realizar esta operação, pois existe um processo mais recente para a reativação desta matrícula. Utilize-o.");
        		}
        	}
        	removerControleMemoriaFlashTela("RenovarMatriculaControle");
        	context().getExternalContext().getSessionMap().put("reativarMatriculaItem", getReativacaoMatriculaVO().getMatricula());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("reativacaoMatriculaForm.xhtml");
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
    }

    public String getResponsavelAutorizacao_Erro() {
        return responsavelAutorizacao_Erro;
    }

    public void setResponsavelAutorizacao_Erro(String responsavelAutorizacao_Erro) {
        this.responsavelAutorizacao_Erro = responsavelAutorizacao_Erro;
    }

    public String getRequerimento_Erro() {
        return requerimento_Erro;
    }

    public void setRequerimento_Erro(String requerimento_Erro) {
        this.requerimento_Erro = requerimento_Erro;
    }

    public String getMatricula_Erro() {
        return matricula_Erro;
    }

    public void setMatricula_Erro(String matricula_Erro) {
        this.matricula_Erro = matricula_Erro;
    }

    public ReativacaoMatriculaVO getReativacaoMatriculaVO() {
        if (reativacaoMatriculaVO == null) {
            reativacaoMatriculaVO = new ReativacaoMatriculaVO();
        }
        return reativacaoMatriculaVO;
    }

    public void setReativacaoMatriculaVO(ReativacaoMatriculaVO reativacaoMatriculaVO) {
        this.reativacaoMatriculaVO = reativacaoMatriculaVO;
    }

    public String getRequerimento_valorApresentar() {
        return requerimento_valorApresentar;
    }

    public void setRequerimento_valorApresentar(String requerimento_valorApresentar) {
        this.requerimento_valorApresentar = requerimento_valorApresentar;
    }

    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        reativacaoMatriculaVO = null;
        matricula_Erro = null;
        requerimento_Erro = null;
        responsavelAutorizacao_Erro = null;
        requerimento_valorApresentar = null;
    }

    /**
     * @return the avisarGravacao
     */
    public Boolean getAvisarExclusao() {
        return avisarExclusao;
    }

    /**
     * @param avisarGravacao
     *            the avisarGravacao to set
     */
    public void setAvisarExclusao(Boolean avisarExclusao) {
        this.avisarExclusao = avisarExclusao;
    }

    /**
     * @return the avisarAlteracao
     */
    public Boolean getAvisarAlteracao() {
        return avisarAlteracao;
    }

    /**
     * @param avisarAlteracao
     *            the avisarAlteracao to set
     */
    public void setAvisarAlteracao(Boolean avisarAlteracao) {
        this.avisarAlteracao = avisarAlteracao;
    }

    /**
     * @return the avisarInclusao
     */
    public Boolean getAvisarInclusao() {
        return avisarInclusao;
    }

    /**
     * @param avisarInclusao
     *            the avisarInclusao to set
     */
    public void setAvisarInclusao(Boolean avisarInclusao) {
        this.avisarInclusao = avisarInclusao;
    }

    /**
     * @return the apresentarRichMensagem
     */
    public String getApresentarRichMensagem() {
        return apresentarRichMensagem;
    }

    /**
     * @param apresentarRichMensagem
     *            the apresentarRichMensagem to set
     */
    public void setApresentarRichMensagem(String apresentarRichMensagem) {
        this.apresentarRichMensagem = apresentarRichMensagem;
    }

    public boolean isExibirBotoesSituacaoRequerimento() {
        return exibirBotoesSituacaoRequerimento;
    }

    public void setExibirBotoesSituacaoRequerimento(boolean exibirBotoesSituacaoRequerimento) {
        this.exibirBotoesSituacaoRequerimento = exibirBotoesSituacaoRequerimento;
    }

    public boolean getIsPermitirRenovarMatricula() {
        if (getReativacaoMatriculaVO().getRequerimento().getSituacao().equals("FD")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the listaConsultaAluno
     */
    public List getListaConsultaAluno() {
        return listaConsultaAluno;
    }

    /**
     * @param listaConsultaAluno the listaConsultaAluno to set
     */
    public void setListaConsultaAluno(List listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    /**
     * @return the campoConsultaAluno
     */
    public String getCampoConsultaAluno() {
        return campoConsultaAluno;
    }

    /**
     * @param campoConsultaAluno the campoConsultaAluno to set
     */
    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
    }

    /**
     * @return the valorConsultaAluno
     */
    public String getValorConsultaAluno() {
        return valorConsultaAluno;
    }

    /**
     * @param valorConsultaAluno the valorConsultaAluno to set
     */
    public void setValorConsultaAluno(String valorConsultaAluno) {
        this.valorConsultaAluno = valorConsultaAluno;
    }

    public List getTipoConsultaComboAluno() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public void consultarAluno() {
        try {
            List objs = new ArrayList(0);

            if (getValorConsultaAluno().equals("")) {
                throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("registroAcademico")) {
            	objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            setListaConsultaAluno(objs);
            if (!getListaConsultaAluno().isEmpty()) {
                setMensagemID("msg_dados_consultados");
            }else{
                setMensagemID("msg_erro_dadosnaoencontrados");
            }
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarAluno() {
        try {
            MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
            MatriculaVO objCompleto = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
            if (!objCompleto.getSituacao().equals("TR") && !objCompleto.getSituacao().equals("AC") && !objCompleto.getSituacao().equals("JU")) {
            	throw new Exception(UteisJSF.internacionalizar("msg_reativacaoMatricula_matriculaNaoPodeSerReativada")); 
            }
            this.getReativacaoMatriculaVO().setMatricula(objCompleto);            
            obj = null;
            objCompleto = null;
            setValorConsultaAluno("");
            setCampoConsultaAluno("");
            getListaConsultaAluno().clear();
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public Boolean getApresentarDadosRequerimento() {
        if ((this.getReativacaoMatriculaVO().getCodigo() == null)
                || (this.getReativacaoMatriculaVO().getCodigo().equals(0))) {
            return false;
        } else {
            return true;
        }
    }
    
    public void validarLiberarOperacaoFuncionalidade() {
    	UsuarioVO usuarioVerif;
		try {
			usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUserNameLiberarOperacaoFuncionalidade(), this.getSenhaLiberarOperacaoFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
            ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("ReativarMatricula_liberarReativacaoMatriculaNumeroPeriodoUltrapassadoAoConfigurado", usuarioVerif);
            setPossuiPermissaoLiberarReativacaoMatriculaNumeroPeriodoUltrapassadoAoConfigurado(true);
            setApresentarBotaoLiberarFuncionalidade(false);
            setMensagemID("msg_Liberacao_realizadaComSucesso");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

    }
    
    
    public void validarDadosQuantidadeMaximaPeriodoTrancamentoParaRealizarRenovacaoDeAcordoQuantidadeDefinidaConfiguracaoAcademico(ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception {
    	List<MatriculaPeriodoVO> listaMatriculaPeriodoVOs = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatricula(getReativacaoMatriculaVO().getMatricula().getMatricula(), 0, false, new DataModelo(), getUsuarioLogado());
    	Ordenacao.ordenarLista(listaMatriculaPeriodoVOs, "anoSemestreCodigo");
    	Integer quantidadeMaximaPermitidaDefinidoConfiguracaoAcademico = configuracaoAcademicoVO.getQuantidadeMaximaPeriodoTrancadoPermiteReativacaoMatricula();
		Integer qtdeGeralPeriodoTrancado = 0;
		StringBuilder periodoTrancamentoApresentar = new StringBuilder();
		PeriodicidadeEnum periodicidade = PeriodicidadeEnum.getEnumPorValor(getReativacaoMatriculaVO().getMatricula().getCurso().getPeriodicidade());
		
		qtdeGeralPeriodoTrancado = qtdeGeralPeriodoTrancado + realizarCalculoQuantidadePeriodoAlunoFicouTrancadoDaMatriculaPeriodo(listaMatriculaPeriodoVOs, periodicidade, periodoTrancamentoApresentar);
		qtdeGeralPeriodoTrancado = qtdeGeralPeriodoTrancado + realiarCalculoQuantidadePeriodoUltimoTrancamentoAteAnoSemestreAtual(listaMatriculaPeriodoVOs, periodicidade, periodoTrancamentoApresentar);
		
		if (qtdeGeralPeriodoTrancado > quantidadeMaximaPermitidaDefinidoConfiguracaoAcademico) {
			setApresentarBotaoLiberarFuncionalidade(true);
			throw new Exception("Quantidade de Período(s) em que o aluno ficou trancado ultrapassou o número configurado permitido(Número configurado permitido: "+quantidadeMaximaPermitidaDefinidoConfiguracaoAcademico+"). Para prosseguir deve ser realizado a liberação do responsável. Períodos considerados Trancados: " + periodoTrancamentoApresentar.toString() + ".");
		}
		periodoTrancamentoApresentar = null;
	}
     
	public Integer realizarCalculoQuantidadePeriodoAlunoFicouTrancadoDaMatriculaPeriodo(List<MatriculaPeriodoVO> listaMatriculaPeriodoVOs, PeriodicidadeEnum periodicidade, StringBuilder periodoTrancamentoApresentar) throws Exception {
		Integer anoTrancamento = 0;
		Integer semestreTrancamento = 0;
		Boolean encontradoTrancamento = false;
		Integer qtdeGeralPeriodoTrancado = 0;
		
		for (MatriculaPeriodoVO matriculaPeriodoVO : listaMatriculaPeriodoVOs) {

			if (matriculaPeriodoVO.getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.TRANCADA.getValor())) {
				if (!encontradoTrancamento) {
					if (periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)) {
						semestreTrancamento = Integer.parseInt(matriculaPeriodoVO.getSemestre());
					} 
					anoTrancamento = Integer.parseInt(matriculaPeriodoVO.getAno());
					encontradoTrancamento = true;
				} else {
					qtdeGeralPeriodoTrancado = qtdeGeralPeriodoTrancado + realizarCalculoQuantidadePeriodoTrancamentoAtePeriodoRenovado(anoTrancamento, Integer.parseInt(matriculaPeriodoVO.getAno()), semestreTrancamento, Integer.parseInt(matriculaPeriodoVO.getSemestre()), periodicidade, periodoTrancamentoApresentar);
					if (periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)) {
						semestreTrancamento = Integer.parseInt(matriculaPeriodoVO.getSemestre());
					} 
					anoTrancamento = Integer.parseInt(matriculaPeriodoVO.getAno());
					encontradoTrancamento = true;
				}

			} else if (!matriculaPeriodoVO.getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.TRANCADA.getValor()) && encontradoTrancamento) {
				qtdeGeralPeriodoTrancado = qtdeGeralPeriodoTrancado + realizarCalculoQuantidadePeriodoTrancamentoAtePeriodoRenovado(anoTrancamento, Integer.parseInt(matriculaPeriodoVO.getAno()), semestreTrancamento, Integer.parseInt(matriculaPeriodoVO.getSemestre()), periodicidade, periodoTrancamentoApresentar);
				encontradoTrancamento = false;
			}
		}
		return qtdeGeralPeriodoTrancado;
	}
	
	 public Integer realiarCalculoQuantidadePeriodoUltimoTrancamentoAteAnoSemestreAtual(List<MatriculaPeriodoVO> listaMatriculaPeriodoVOs, PeriodicidadeEnum periodicidade, StringBuilder periodoTrancamentoApresentar) {
	    	Integer anoAtual = Integer.parseInt(Uteis.getAnoDataAtual4Digitos());
	    	Integer semestreAtual = 0;
	    	if (periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)) {
	    		semestreAtual = Integer.parseInt(Uteis.getSemestreAtual());
	    	}
	    	MatriculaPeriodoVO ultimaMatriculaPeriodoVO = listaMatriculaPeriodoVOs.get(listaMatriculaPeriodoVOs.size() - 1);
	    	Integer anoMatriculaPeriodo = Integer.parseInt(ultimaMatriculaPeriodoVO.getAno());
	    	Integer semestreMatriculaPeriodo = 0;
	    	if (periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)) {
	    		semestreMatriculaPeriodo = Integer.parseInt(ultimaMatriculaPeriodoVO.getSemestre());
	    	}
	    	Integer qtdeGeralPeriodoTrancado = 0;
	    	
	    	qtdeGeralPeriodoTrancado = qtdeGeralPeriodoTrancado + realizarCalculoQuantidadePeriodoTrancamentoAtePeriodoRenovado(anoMatriculaPeriodo, anoAtual, semestreMatriculaPeriodo, semestreAtual, periodicidade, periodoTrancamentoApresentar);
	    	return qtdeGeralPeriodoTrancado;
	    }
	
	public Integer realizarCalculoQuantidadePeriodoTrancamentoAtePeriodoRenovado(Integer anoTrancamento, Integer anoMatriculaPeriodo, Integer semestreTrancamento, Integer semestreMatriculaPeriodo, PeriodicidadeEnum periodicidade, StringBuilder periodoTrancamentoApresentar) {
		Integer qtdePeriodo = 0;
		
		if (periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)) {
			//REGRA PARA CURSOS SEMESTRAIS
			while (anoTrancamento <= anoMatriculaPeriodo) {
				if (anoTrancamento.equals(anoMatriculaPeriodo) && semestreTrancamento.equals(semestreMatriculaPeriodo)) {
					break;
				}
				qtdePeriodo++;
				if (periodoTrancamentoApresentar.length() == 0) {
					periodoTrancamentoApresentar.append("(" + anoTrancamento.toString() + "/" + semestreTrancamento.toString() + ")");
				} else {
					periodoTrancamentoApresentar.append(" - ("+ anoTrancamento.toString() + "/" + semestreTrancamento.toString() + ")");
				}
				if (semestreTrancamento == 2) {
					anoTrancamento++;
				}
				if (semestreTrancamento == 1) {
					semestreTrancamento = 2;
				} else {
					semestreTrancamento = 1;
				}
			}
		} else {
//			REGRA PARA CURSOS ANUAIS
			while (anoTrancamento <= anoMatriculaPeriodo) {
				if (anoTrancamento.equals(anoMatriculaPeriodo)) {
					break;
				}
				qtdePeriodo++;
				anoTrancamento++;
			}
		}
		return qtdePeriodo;
	}

	public String getUserNameLiberarOperacaoFuncionalidade() {
		if (userNameLiberarOperacaoFuncionalidade == null) {
			userNameLiberarOperacaoFuncionalidade = "";
		}
		return userNameLiberarOperacaoFuncionalidade;
	}

	public void setUserNameLiberarOperacaoFuncionalidade(String userNameLiberarOperacaoFuncionalidade) {
		this.userNameLiberarOperacaoFuncionalidade = userNameLiberarOperacaoFuncionalidade;
	}

	public String getSenhaLiberarOperacaoFuncionalidade() {
		if (senhaLiberarOperacaoFuncionalidade == null) {
			senhaLiberarOperacaoFuncionalidade = "";
		}
		return senhaLiberarOperacaoFuncionalidade;
	}

	public void setSenhaLiberarOperacaoFuncionalidade(String senhaLiberarOperacaoFuncionalidade) {
		this.senhaLiberarOperacaoFuncionalidade = senhaLiberarOperacaoFuncionalidade;
	}

	public Boolean getPossuiPermissaoLiberarReativacaoMatriculaNumeroPeriodoUltrapassadoAoConfigurado() {
		if (possuiPermissaoLiberarReativacaoMatriculaNumeroPeriodoUltrapassadoAoConfigurado == null) {
			possuiPermissaoLiberarReativacaoMatriculaNumeroPeriodoUltrapassadoAoConfigurado = false;
		}
		return possuiPermissaoLiberarReativacaoMatriculaNumeroPeriodoUltrapassadoAoConfigurado;
	}

	public void setPossuiPermissaoLiberarReativacaoMatriculaNumeroPeriodoUltrapassadoAoConfigurado(Boolean possuiPermissaoLiberarReativacaoMatriculaNumeroPeriodoUltrapassadoAoConfigurado) {
		this.possuiPermissaoLiberarReativacaoMatriculaNumeroPeriodoUltrapassadoAoConfigurado = possuiPermissaoLiberarReativacaoMatriculaNumeroPeriodoUltrapassadoAoConfigurado;
	}

	public String getOncompleteOperacaoFuncionalidade() {
		if (oncompleteOperacaoFuncionalidade == null) {
			oncompleteOperacaoFuncionalidade = "";
		}
		return oncompleteOperacaoFuncionalidade;
	}

	public void setOncompleteOperacaoFuncionalidade(String oncompleteOperacaoFuncionalidade) {
		this.oncompleteOperacaoFuncionalidade = oncompleteOperacaoFuncionalidade;
	}

	public Boolean getApresentarBotaoLiberarFuncionalidade() {
		if (apresentarBotaoLiberarFuncionalidade == null) {
			apresentarBotaoLiberarFuncionalidade = false;
		}
		return apresentarBotaoLiberarFuncionalidade;
	}

	public void setApresentarBotaoLiberarFuncionalidade(Boolean apresentarBotaoLiberarFuncionalidade) {
		this.apresentarBotaoLiberarFuncionalidade = apresentarBotaoLiberarFuncionalidade;
	}
	
	public void limparMensagemParaDigitarUserNameESenha() {
		setMensagemID("msg_entre_prmlogin");
	}
	
	private Boolean permiteReativacaoMatriculaSemRequerimento;
	
	public Boolean getPermiteReativacaoMatriculaSemRequerimento() {
		if (permiteReativacaoMatriculaSemRequerimento == null) {
			try {
				permiteReativacaoMatriculaSemRequerimento = getLoginControle().getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getReativacaoMatriculaVO().getMatricula().getUnidadeEnsino().getCodigo()).getPermiteReativacaoMatriculaSemRequerimento();
			} catch (Exception e) {
				permiteReativacaoMatriculaSemRequerimento = true;
			}
		}
		return permiteReativacaoMatriculaSemRequerimento;
	}
	
	public void setPermiteReativacaoMatriculaSemRequerimento(Boolean permiteReativacaoMatriculaSemRequerimento) {
		this.permiteReativacaoMatriculaSemRequerimento = permiteReativacaoMatriculaSemRequerimento;
	}
	
}
