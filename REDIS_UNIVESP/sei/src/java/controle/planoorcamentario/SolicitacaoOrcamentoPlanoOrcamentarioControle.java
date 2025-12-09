package controle.planoorcamentario;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas fluxoCaixaForm.jsp fluxoCaixaCons.jsp) com as funcionalidades da classe <code>FluxoCaixa</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see FluxoCaixa
 * @see FluxoCaixaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.SerializationUtils;
import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoOrigemComunicacaoInternaEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.RequisicaoItemVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.planoorcamentario.DetalhamentoPeriodoOrcamentoVO;
import negocio.comuns.planoorcamentario.DetalhamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.ItemSolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.PlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.SolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.enumeradores.SituacaoPlanoOrcamentarioEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("SolicitacaoOrcamentoPlanoOrcamentarioControle")
@Scope("viewScope")
@Lazy
public class SolicitacaoOrcamentoPlanoOrcamentarioControle extends SuperControle implements Serializable {

    private static final long serialVersionUID = 5014946168043133807L;

	private SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO;
    private ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO;

    private List<SelectItem> listaDepartamento;
    private List<SelectItem> listaUnidadeEnsino;
    private List<SelectItem> listaPlanoOrcamentario;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private List<SelectItem> listaSelectItemDepartamentoFornecedor;

    private String valorConsultaCentroDespesa;
    private String campoConsultaCentroDespesa;
    private List<CategoriaDespesaVO> listaConsultaCentroDespesa;

    private List<ComunicacaoInternaVO> listaComunicacaoInterna;
    private List<DetalhamentoPlanoOrcamentarioVO> detalhamentosPlanoOrcamentario;

    private ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado;
    private ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado;

    private Boolean fecharModalComunicacaoInterna;

    private String motivo;
    private DataModelo dataModeloHistoricoRemanejamento;
    private String mensagemPadraoNotificacao;
    private Boolean enviarComunicadoPorEmail;
    
    private Boolean fecharModalRemanejamento;
    
    public SolicitacaoOrcamentoPlanoOrcamentarioControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        getControleConsulta().setCampoConsulta("A");
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>FluxoCaixa</code> para edição pelo usuário da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setSolicitacaoOrcamentoPlanoOrcamentarioVO(new SolicitacaoOrcamentoPlanoOrcamentarioVO());
        inicializarResponsavel();
        inicializarListasSelectItemTodosComboBox(getUsuarioLogado().getPessoa());
        getSolicitacaoOrcamentoPlanoOrcamentarioVO().setNovoObj(Boolean.TRUE);
        montarListaSelectItemUnidadeEnsino();
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("solicitacaoOrcamentoPlanoOrcamentarioForm.xhtml");
    }

    public void inicializarResponsavel() {
        try {
            getSolicitacaoOrcamentoPlanoOrcamentarioVO().setPessoa(getUsuarioLogado().getPessoa());
        } catch (Exception e) {
        	setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>FluxoCaixa</code> para alteração. O objeto desta classe é disponibilizado na session da página (request) para que o
     * JSP correspondente possa disponibilizá-lo para edição.
     *
     * @throws Exception
     */
    public String editar() throws Exception {
    	try {
	        SolicitacaoOrcamentoPlanoOrcamentarioVO obj = (SolicitacaoOrcamentoPlanoOrcamentarioVO) context().getExternalContext().getRequestMap().get("solicitacaoOrcamentoPlanoOrcamentarioItens");
	        setSolicitacaoOrcamentoPlanoOrcamentarioVO(obj);
	        inicializarListasSelectItemTodosComboBox(obj.getPessoa());
	        getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().carregarDados(getSolicitacaoOrcamentoPlanoOrcamentarioVO(), NivelMontarDados.TODOS, getUsuarioLogado());
	        obj.setNovoObj(Boolean.FALSE);
	        consultarDadosHistoricoRemanejamento();
	        ordenarItensSolicitacao();
	        setMensagemID("msg_dados_editar");
	        return Uteis.getCaminhoRedirecionamentoNavegacao("solicitacaoOrcamentoPlanoOrcamentarioForm.xhtml");
    	} catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("solicitacaoOrcamentoPlanoOrcamentarioForm.xhtml");
        }
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>FluxoCaixa</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação
     * <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma
     * mensagem de erro.
     */
    public String gravar() {
        try {
        	validarDadosValorMensalOrcaomentoTotalPrevisto();
            if (getSolicitacaoOrcamentoPlanoOrcamentarioVO().isNovoObj().booleanValue()) {
                getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().incluir(getSolicitacaoOrcamentoPlanoOrcamentarioVO(), getUsuarioLogado());
            } else {
                getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().alterar(getSolicitacaoOrcamentoPlanoOrcamentarioVO(), getUsuarioLogado());
            }
            setMensagemID(MSG_TELA.msg_dados_gravados.name());
            return Uteis.getCaminhoRedirecionamentoNavegacao("solicitacaoOrcamentoPlanoOrcamentarioForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("solicitacaoOrcamentoPlanoOrcamentarioForm.xhtml");
        }
    }

    public void remanejarValorAprovado() {
    	try {
    		getFacadeFactory().getItemSolicitacaoOrcamentoPlanoOrcamentarioFacade().gravarItemSolicitacao(getItemSolicitacaoOrcamentoPlanoOrcamentarioVO(),
    				getSolicitacaoOrcamentoPlanoOrcamentarioVO(), getUsuarioLogado(), false);

    		setMensagemID("msg_SolicitacaoPlanoOrcamentario_remanejarValorAprovado");
	    } catch (Exception e) {
	    	setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
	    }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP FluxoCaixaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste
     * mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            boolean permiteConsultarTodos = permitirAcessarPlanoOrcamentarioTodosDepartamentosUnidadeEnsino();
            List<SolicitacaoOrcamentoPlanoOrcamentarioVO> objs = new ArrayList<>(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().consultaRapidaPorCodigo(valorInt, true, getUsuarioLogado(),permiteConsultarTodos);
            }
            if (getControleConsulta().getCampoConsulta().equals("departamento")) {
                objs = getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().consultaRapidaPorNomeDepartamento(getControleConsulta().getValorConsulta(), true, getUsuarioLogado(), permiteConsultarTodos);
            }
            if (getControleConsulta().getCampoConsulta().equals("planoOrcamentario")) {
                objs = getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().consultaRapidaPorNomePlanoOrcamentario(getControleConsulta().getValorConsulta(), true, getUsuarioLogado(), permiteConsultarTodos);
            }
            if (getControleConsulta().getCampoConsulta().equals("situacao")) {
                objs = getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().consultaRapidaPorSituacao(getControleConsulta().getValorConsulta(), true, getUsuarioLogado(), permiteConsultarTodos);
            }
            if (getControleConsulta().getCampoConsulta().equals("pessoa")) {
                objs = getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().consultaRapidaPorFuncionario(getControleConsulta().getValorConsulta(), true, getUsuarioLogado(), permiteConsultarTodos);
            }
            setListaConsulta(objs);            
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("solicitacaoOrcamentoPlanoOrcamentarioCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList<>(0));
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("solicitacaoOrcamentoPlanoOrcamentarioCons.xhtml");
        }
    }

    public boolean permitirAcessarPlanoOrcamentarioTodosDepartamentosUnidadeEnsino() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidade("PlanoOrcamentario_permitirAcessarPlanoOrcamentarioTodosDepartamentosUnidadeEnsino", getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

    public boolean permitirRealizarManejamento() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidade("PlanoOrcamentario_permitirRealizarManejamento", getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>FluxoCaixaVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
        	getFacadeFactory().getRequisicaoItemFacade().validarExisteRequisicaoItemPorPlanoOrcamentario(getSolicitacaoOrcamentoPlanoOrcamentarioVO().getPlanoOrcamentario());
            getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().excluir(getSolicitacaoOrcamentoPlanoOrcamentarioVO(), getUsuarioLogado());
            setSolicitacaoOrcamentoPlanoOrcamentarioVO(new SolicitacaoOrcamentoPlanoOrcamentarioVO());
            inicializarResponsavel();
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("solicitacaoOrcamentoPlanoOrcamentarioForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("solicitacaoOrcamentoPlanoOrcamentarioForm.xhtml");
        }
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox(PessoaVO pessoa) {
        montarListaSelectItemDepartamento(pessoa);
        montarListaSelectItemPlanoOrcamentario();
        montarSelectItemDepartamentoFornecedor();
        montarListaSelectItemUnidadeEnsino();
    }

    public void alterarAguardandoAprovacao() {
    	SituacaoPlanoOrcamentarioEnum situacaoAnterior = (SituacaoPlanoOrcamentarioEnum) SerializationUtils.clone(getSolicitacaoOrcamentoPlanoOrcamentarioVO().getSituacao());
    	try {
    		getSolicitacaoOrcamentoPlanoOrcamentarioVO().setSituacao(SituacaoPlanoOrcamentarioEnum.AGUARDANDO_APROVACAO);
    		getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().alterarAguardandoAprovacao(getSolicitacaoOrcamentoPlanoOrcamentarioVO(), 
    				SituacaoPlanoOrcamentarioEnum.AGUARDANDO_APROVACAO.getValor(), getUsuarioLogado());	
    		setMensagemID("msg_SolicitacaoPlanoOrcamentario_solicitacaoAguardandoAprovado", Uteis.SUCESSO);
		} catch (Exception e) {
			getSolicitacaoOrcamentoPlanoOrcamentarioVO().setSituacao(situacaoAnterior);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Turno</code>.
     */
    @SuppressWarnings("unchecked")
	public void montarListaSelectItemDepartamento(Integer prm) throws Exception {
        List<DepartamentoVO> resultadoConsulta = null;
        Iterator<DepartamentoVO> i = null;
        try {
            resultadoConsulta = consultarDepartamentoPorPessoa(prm);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                DepartamentoVO obj = i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
            Collections.sort((List<SelectItem>) objs, ordenador);
            setListaDepartamento(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    @SuppressWarnings("unchecked")
	public void montarListaSelectItemPlanoOrcamentario(String prm) throws Exception {
    	if (Uteis.isAtributoPreenchido(getSolicitacaoOrcamentoPlanoOrcamentarioVO().getUnidadeEnsino().getCodigo())) {
	        List<PlanoOrcamentarioVO> resultadoConsulta = null;
	        Iterator<PlanoOrcamentarioVO> i = null;
	        try {
	            resultadoConsulta = consultarPlanoOrcamentarioEmContrucao(prm);
	            i = resultadoConsulta.iterator();
	            List<SelectItem> objs = new ArrayList<>(0);
	            objs.add(new SelectItem(0, ""));
	            while (i.hasNext()) {
	                PlanoOrcamentarioVO obj = i.next();
	                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
	            }
	            SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
	            Collections.sort((List<SelectItem>) objs, ordenador);
	            setListaPlanoOrcamentario(objs);
	        } catch (Exception e) {
	            throw e;
	        } finally {
	            Uteis.liberarListaMemoria(resultadoConsulta);
	            i = null;
	        }
    	} else {
    		setListaPlanoOrcamentario(new ArrayList<>());
    	}
    }

    public List<DepartamentoVO> consultarDepartamentoPorPessoa(Integer codPessoa) throws Exception {
        return getFacadeFactory().getDepartamentoFacade().consultarPorGerenteDpto(codPessoa, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
    }

	public List<PlanoOrcamentarioVO> consultarPlanoOrcamentarioEmContrucao(String situacao) throws Exception {
        return getFacadeFactory().getPlanoOrcamentarioFacade().consultarPorSituacao(situacao, getSolicitacaoOrcamentoPlanoOrcamentarioVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Turno</code>. Buscando todos os objetos
     * correspondentes a entidade <code>Turno</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemDepartamento(PessoaVO pessoaVO) {
        try {
            montarListaSelectItemDepartamento(pessoaVO.getCodigo());
        } catch (Exception e) {
        	setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
    }
    
    public void enviarEmailResposta() {
    	try {
        	getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().enviarEmailResponsavelDepartamento(
        			getSolicitacaoOrcamentoPlanoOrcamentarioVO().getDepartamento(), getSolicitacaoOrcamentoPlanoOrcamentarioVO().getUnidadeEnsino(),
        			getEnviarComunicadoPorEmail(), getMensagemPadraoNotificacao(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(),
        			getComunicacaoInternaVO().getCodigoTipoOrigemComunicacaoInterna(), getUsuarioLogado().getPessoa(), 
        			getComunicacaoInternaVO().getResponsavel());
            setMensagemPadraoNotificacao(null);
            setMensagemID("msg_msg_enviados");
        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
    }

    public void consultarComunicadosInterno() {
    	try {
    		setListaComunicacaoInterna(getFacadeFactory().getComunicacaoInternaFacade().consultarPorTipoOrigemECodigoTipoOrigemEPessoa(
    				TipoOrigemComunicacaoInternaEnum.SOLICITACAO_ORCAMENTO_PLANO_ORCAMENTARIO, getSolicitacaoOrcamentoPlanoOrcamentarioVO().getCodigo(), getUsuarioLogado().getPessoa(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }

    public void montarListaSelectItemPlanoOrcamentario() {
        try {
        	montarListaSelectItemPlanoOrcamentario(SituacaoPlanoOrcamentarioEnum.EM_CONSTRUCAO.getValor());
        } catch (Exception e) {
        	setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
    }

    public void consultarDadosHistoricoRemanejamento() {
		try {
			getDataModeloHistoricoRemanejamento().setValorConsulta(getSolicitacaoOrcamentoPlanoOrcamentarioVO().getCodigo().toString());
			getFacadeFactory().getHistoricoRemanejamentoPlanoOrcamentarioInterfaceFacade().consultarPorEnumCampoConsulta(getDataModeloHistoricoRemanejamento(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

    public boolean apresentarBotaoEmConstrucaoERevisao() {
    	return getSolicitacaoOrcamentoPlanoOrcamentarioVO().getSituacao().equals(SituacaoPlanoOrcamentarioEnum.EM_CONSTRUCAO) ||
    			getSolicitacaoOrcamentoPlanoOrcamentarioVO().getSituacao().equals(SituacaoPlanoOrcamentarioEnum.REVISAO);
    }

    public List<SelectItem> getTipoComboSituacao() {
        return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoPlanoOrcamentarioEnum.class, false);
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List<SelectItem> getTipoConsultaCombo() {
        List<SelectItem> itens = new ArrayList<>(0);
        itens.add(new SelectItem("pessoa", "Funcionário"));
        itens.add(new SelectItem("situacao", "Situação"));
        itens.add(new SelectItem("planoOrcamentario", "Plano Orçamentário"));
        itens.add(new SelectItem("departamento", "Departamento"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public Boolean getApresentarSituacao() {
        return getControleConsulta().getCampoConsulta().equals("situacao");
    }

    public List<SelectItem> getTipoConsultaComboSituacao() {
    	return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoPlanoOrcamentarioEnum.class, false);
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList<>(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("solicitacaoOrcamentoPlanoOrcamentarioCons.xhtml");
    }

    public List<SelectItem> getTipoConsultaComboTurma() {
        List<SelectItem> itens = new ArrayList<>(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
        itens.add(new SelectItem("nomeTurno", "Turno"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public void selecionarCentroDespesa() {
    	try {
	        CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("centroDespesaItens");
	        this.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().setUnidadeEnsino(0);
	        this.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().setTurma(null);
	        this.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().setCursoVO(null);
	        this.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().setTurnoVO(null);
	        this.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().setCategoriaDespesa(obj);
            montarListaSelectItemUnidadeEnsino();
            setMensagemID(MSG_TELA.msg_entre_dados.name());
        } catch (Exception e) {
        	setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
	public void selecionarRemanejarPropriaCategoriaDespesa() {
    	try {
    		
    		if (getItemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado().getRemanejarParaPropriaCategoriaDespesa()) {
    			montarDadosItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejar(Boolean.FALSE);
    			getItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado().setDescricao( (String) SerializationUtils.clone(getItemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado().getDescricao()));
    			getItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado().setCategoriaDespesa( (CategoriaDespesaVO) SerializationUtils.clone(getItemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado().getCategoriaDespesa()));
    			getItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado().setValor( (double) SerializationUtils.clone(getItemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado().getValorDisponivel()));
    			
    			getItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado().setDetalhamentoPeriodoOrcamentoVOs( (List<DetalhamentoPeriodoOrcamentoVO>) SerializationUtils.clone((Serializable) getItemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado().getDetalhamentoPeriodoOrcamentoVOs()));
    			
    			getItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado().getDetalhamentoPeriodoOrcamentoVOs().stream().forEach(item -> item.setValorRemanejar(item.getValorDisponivel()));
    		} else {
    			setItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado(new ItemSolicitacaoOrcamentoPlanoOrcamentarioVO());
    			montarDadosItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejar(Boolean.TRUE);
    		}
    		setFecharModalRemanejamento(Boolean.FALSE);
    	} catch (Exception e) {
    		setFecharModalRemanejamento(Boolean.TRUE);
        	setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
    }
    
    public void montarDadosItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejar(boolean executarDistribuicaoPeriodo) {
    	
		for(DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVO : itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado.getDetalhamentoPeriodoOrcamentoVOs()) {			
			detalhamentoPeriodoOrcamentoVO.setValorRemanejar(detalhamentoPeriodoOrcamentoVO.getValorDisponivel());			
		}
		getItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado().setDetalhamentoPeriodoOrcamentoVOs(new ArrayList<>());
		calcularValorRemanejamento(executarDistribuicaoPeriodo);
    }

    public void selecionarCentroDespesaRemanejado() {
    	CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("centroDespesaItens");
    	getItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado().setCategoriaDespesa(obj);
    }

    public boolean apresentarRemanejamento() {
    	return permitirRealizarManejamento() && getAprovado();
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino(0);
        } catch (Exception e) {
        	setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
    }

    public void montarListaSelectItemUnidadeEnsino(Integer prm) throws Exception {
        List<UnidadeEnsinoVO> resultadoConsulta = null;
        Iterator<UnidadeEnsinoVO> i = null;
        try {
            resultadoConsulta = consultarTodasUnidadeEnsino(prm);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemUnidadeEnsino(objs);
            setListaUnidadeEnsino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por consultar dados da entidade
     * <code><code> e montar o atributo <code>numero</code> Este atributo é uma
     * lista (<code>List</code>) utilizada para definir os valores a serem
     * apresentados no ComboBox correspondente
     */
    public List<UnidadeEnsinoVO> consultarTodasUnidadeEnsino(Integer numeroPrm) throws Exception {
        return getFacadeFactory().getUnidadeEnsinoFacade().consultarTodasUnidades(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
    }

	public void consultarCentroDespesa() {
        try {
            List<CategoriaDespesaVO> objs = new ArrayList<>(0);
            if (getConfiguracaoFinanceiroPadraoSistema().getUsaPlanoOrcamentario()) {
                if (getCampoConsultaCentroDespesa().equals("descricao")) {
                    objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricaoPlanoOrcamentario(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
                }
                if (getCampoConsultaCentroDespesa().equals("identificadorCentroDespesa")) {
                    objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesaPlanoOrcamentario(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
                }
            } else {
                if (getCampoConsultaCentroDespesa().equals("descricao")) {
                    objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
                }
                if (getCampoConsultaCentroDespesa().equals("identificadorCentroDespesa")) {
                    objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
                }
            }
            setListaConsultaCentroDespesa(objs);
            setMensagemID(MSG_TELA.msg_dados_consultados.name());
        } catch (Exception e) {
            setListaConsultaCentroDespesa(new ArrayList<>(0));
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
    }

    public void adicionarSolicitacaoOrcamentoPlanoOrcamentarioRemanejamento() {
    	 try {
             if (Uteis.isAtributoPreenchido(getItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado().getSolicitacaoOrcamentoPlanoOrcamentario())) {
            	 getItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado().setSolicitacaoOrcamentoPlanoOrcamentario(getSolicitacaoOrcamentoPlanoOrcamentarioVO());
             }
             remanejarItemSolicitacaoOrcamentoPlanoOrcamentario();
             setItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado(new ItemSolicitacaoOrcamentoPlanoOrcamentarioVO());
             
             limparRemanejamento();
             getItemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado().setRemanejarParaPropriaCategoriaDespesa(false);
             
             ordenarItensSolicitacao();
            
             setMensagemID(MSG_TELA.msg_dados_adicionados.name());
         } catch (Exception e) {
             setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
         }
    }
    

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void ordenarItensSolicitacao() {
		Collections.sort(getSolicitacaoOrcamentoPlanoOrcamentarioVO().getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs(), new Comparator() {
			@Override
			public int compare(Object p1, Object p2) {
				return ((ItemSolicitacaoOrcamentoPlanoOrcamentarioVO) p1).getCategoriaDespesa().getDescricao()
						.compareTo(((ItemSolicitacaoOrcamentoPlanoOrcamentarioVO) p2).getCategoriaDespesa().getDescricao());
			}
		});
	}

    public void adicionarSolicitacaoOrcamentoPlanoOrcamentario() {
        try {
            if (Uteis.isAtributoPreenchido(getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getSolicitacaoOrcamentoPlanoOrcamentario())) {
                getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().setSolicitacaoOrcamentoPlanoOrcamentario(getSolicitacaoOrcamentoPlanoOrcamentarioVO());
            }
            getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().adicionarObjItemSolicitacaoOrcamentoPlanoOrcamentarioVOs(getSolicitacaoOrcamentoPlanoOrcamentarioVO(), getItemSolicitacaoOrcamentoPlanoOrcamentarioVO(), false);
            setItemSolicitacaoOrcamentoPlanoOrcamentarioVO(new ItemSolicitacaoOrcamentoPlanoOrcamentarioVO());
            setMensagemID(MSG_TELA.msg_dados_adicionados.name());
        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
    }

    public void removerItemSolicitacaoOrcamentarioPlanoOrcamentario() {
        try {
            ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj = (ItemSolicitacaoOrcamentoPlanoOrcamentarioVO) context().getExternalContext().getRequestMap().get("itemSolicitacaoOrcamentoPlanoOrcamentarioItens");
            getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().excluirObjItensProvisaoVOs(getSolicitacaoOrcamentoPlanoOrcamentarioVO(), obj.getCategoriaDespesa().getCodigo());
            setItemSolicitacaoOrcamentoPlanoOrcamentarioVO(new ItemSolicitacaoOrcamentoPlanoOrcamentarioVO());
            setMensagemID(MSG_TELA.msg_dados_excluidos.name());
        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
    }

    public void consultarDadosPlanoOrcamentario() {
    	try {
			getSolicitacaoOrcamentoPlanoOrcamentarioVO().setPlanoOrcamentario(getFacadeFactory().getPlanoOrcamentarioFacade().consultarPorChavePrimaria(
					getSolicitacaoOrcamentoPlanoOrcamentarioVO().getPlanoOrcamentario().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void distribuirOrcamentoPeriodoGeral() {
        try {
        	getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().realizarCriacaoDetalhamentoPeriodoGeral(getSolicitacaoOrcamentoPlanoOrcamentarioVO());
        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
    }
    
    public void distribuirOrcamentoPeriodoPorCategoriaDespesa() {
    	try {
    		setItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado(new ItemSolicitacaoOrcamentoPlanoOrcamentarioVO());
    		setItemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado((ItemSolicitacaoOrcamentoPlanoOrcamentarioVO)getRequestMap().get("itemSolicitacaoOrcamentoPlanoOrcamentarioItens"));
    		getItemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado().setRemanejarParaPropriaCategoriaDespesa(false);
    	} catch (Exception e) {
    		setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
    	}
    }

    public void validarDadosValorMensalOrcaomentoTotalPrevisto() {
        try {
        	setOncompleteModal("");
        	if(getSolicitacaoOrcamentoPlanoOrcamentarioVO().getSituacao().equals(SituacaoPlanoOrcamentarioEnum.EM_CONSTRUCAO) ||
        			getSolicitacaoOrcamentoPlanoOrcamentarioVO().getSituacao().equals(SituacaoPlanoOrcamentarioEnum.REVISAO)) {
        		getFacadeFactory().getDetalhamentoPeriodoOrcamentoFacade().validarDadosValorMensalOrcamentoTotalPrevisto(getItemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado().getValorBaseUtilizar(), getItemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado().getDetalhamentoPeriodoOrcamentoVOs());
        		setMensagemID("msg_dados_validados");
        	}
            setOncompleteModal("RichFaces.$('panelDistribuirOrcamentoPeriodo').hide();");
        } catch (Exception ex) {
        	setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
        	
        }
    }

    public void calcularValorRemanejamento(boolean executarDistribuicao) {
    	try {
    		limparMensagem();
    		getFacadeFactory().getItemSolicitacaoOrcamentoPlanoOrcamentarioFacade().calcularValorRemanejamento(
    				getSolicitacaoOrcamentoPlanoOrcamentarioVO(), 
    				getItemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado(), 
    				getItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado(), executarDistribuicao);
    	} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }

    public void selecionarSolicitacaoOrcamento() {
    	ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj = (ItemSolicitacaoOrcamentoPlanoOrcamentarioVO) context().getExternalContext().getRequestMap().get("itemSolicitacaoOrcamentoPlanoOrcamentarioItens");
    	setItemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado(obj);
    	for(DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVO : itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado.getDetalhamentoPeriodoOrcamentoVOs()) {			
			detalhamentoPeriodoOrcamentoVO.setValorRemanejar(detalhamentoPeriodoOrcamentoVO.getValorDisponivel());			
			
		}
    	setItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado(new ItemSolicitacaoOrcamentoPlanoOrcamentarioVO());    
    	getItemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado().setRemanejarParaPropriaCategoriaDespesa(false);     	
        calcularValorRemanejamento(Boolean.TRUE);
    }
    
    public void selecionarItemSolicitacaoOrcamentoPlanoOrcamentario() {
    	try {
	    	setItemSolicitacaoOrcamentoPlanoOrcamentarioVO((ItemSolicitacaoOrcamentoPlanoOrcamentarioVO) 
	    			context().getExternalContext().getRequestMap().get("itemSolicitacaoOrcamentoPlanoOrcamentarioItens"));
	    	
	    	getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().setDetalhamentoPeriodoOrcamentoVOs(getFacadeFactory().getDetalhamentoPeriodoOrcamentoFacade().consultarDetalhamentoPorItemSolicitacaoPlanoOrcamentario(
						getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
	    	setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }

    public boolean permitirRealizarManejamentoSaldoAprovado() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidade("PlanoOrcamentario_permitirRealizarManejamentoSaldoAprovado", getUsuarioLogado());

			return getAprovado();
		} catch (Exception e) {
			return false;
		}
	}

    public Double getTotalValor() {
    	return getSolicitacaoOrcamentoPlanoOrcamentarioVO().getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs().stream().mapToDouble(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO::getValor).sum();
	}

    public Double getTotalValorConsumido() {
    	return getSolicitacaoOrcamentoPlanoOrcamentarioVO().getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs().stream().mapToDouble(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO::getValorConsumido).sum();
    }

    public Double getTotalValorAutorizado() {
    	return getSolicitacaoOrcamentoPlanoOrcamentarioVO().getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs().stream().mapToDouble(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO::getValorProporcional).sum();
    }
    
    public double getTotalValorRequerido() {
    	return getItemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado().getDetalhamentoPeriodoOrcamentoVOs().stream().mapToDouble(DetalhamentoPeriodoOrcamentoVO::getOrcamentoRequeridoGestor).sum();
    }

    public double getTotalValorAprovado() {
    	return getItemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado().getDetalhamentoPeriodoOrcamentoVOs().stream().mapToDouble(DetalhamentoPeriodoOrcamentoVO::getOrcamentoTotal).sum();
    }

    public double getTotalQuantidadeAutorizada() {
    	return getSolicitacaoOrcamentoPlanoOrcamentarioVO().getRequisicaoItemVOs().stream().mapToDouble(RequisicaoItemVO::getQuantidadeAutorizada).sum();
    }
    
    public double getTotalRequisicao() {
    	return getSolicitacaoOrcamentoPlanoOrcamentarioVO().getRequisicaoItemVOs().stream().mapToDouble(RequisicaoItemVO::getValorTotal).sum();
    }
    
    public double getValorTotalRemanejamento() {
    	double totalInformadoRemanejar = getItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado().getDetalhamentoPeriodoOrcamentoVOs().stream().mapToDouble(DetalhamentoPeriodoOrcamentoVO::getValorRemanejar).sum();

    	return Uteis.arrendondarForcando2CadasDecimais(getItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado().getValor()) - Uteis.arrendondarForcando2CadasDecimais(totalInformadoRemanejar);
    }

    public void consultarRequisicaoItemConsumidoPlanoOrcamentario() {
    	try {
    		ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj = (ItemSolicitacaoOrcamentoPlanoOrcamentarioVO) context().getExternalContext().getRequestMap().get("itemSolicitacaoOrcamentoPlanoOrcamentarioItens");

			getSolicitacaoOrcamentoPlanoOrcamentarioVO().setRequisicaoItemVOs(getFacadeFactory().getRequisicaoItemFacade().consultarRequisicaoItemConsumidoPlanoOrcamentario(
					getSolicitacaoOrcamentoPlanoOrcamentarioVO().getPlanoOrcamentario().getCodigo(), getSolicitacaoOrcamentoPlanoOrcamentarioVO().getCodigo(), null, null, obj.getCategoriaDespesa().getCodigo(), null, null));

			getSolicitacaoOrcamentoPlanoOrcamentarioVO().getRequisicaoItemVOs().stream().forEach(p -> {
				p.getRequisicaoVO().setRequisicaoItemVOs(getSolicitacaoOrcamentoPlanoOrcamentarioVO().getRequisicaoItemVOs());
			});
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }

    public void remanejarItemSolicitacaoOrcamentoPlanoOrcamentario() throws Exception {
    	try {
    		limparMensagem();
    		getFacadeFactory().getHistoricoRemanejamentoPlanoOrcamentarioInterfaceFacade().remanejarItemSolicitacaoOrcamentoPlanoOrcamentario(
    				getItemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado(), getItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado(), getUsuarioLogado(), getSolicitacaoOrcamentoPlanoOrcamentarioVO(), getMotivo());

    		consultarDadosHistoricoRemanejamento();

    		setFecharModalRemanejamento(Boolean.TRUE);
    		setMensagemID("msg_SolicitacaoPlanoOrcamentario_remanejarValorAprovado");
		} catch (Exception e) {
			setFecharModalRemanejamento(Boolean.FALSE);
			throw new Exception(e.getMessage());
		}
    }
    
    public String fecharModalRemanejamento() {
    	if (getFecharModalRemanejamento()) {
    		return "RichFaces.$('panelHistoricoRemanejamento').hide();";
    	} else {
    		return "RichFaces.$('panelHistoricoRemanejamento').show();";
    	}
    }

    public void limparRemanejamento() {
    	setItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado(new ItemSolicitacaoOrcamentoPlanoOrcamentarioVO());
    	setMotivo("");
    	setMensagemID("msg_dados_editar");
    }

   public void montarSelectItemDepartamentoFornecedor() {
        try {
            List<SelectItem> objs = new ArrayList<>(0);
            objs.add(new SelectItem(0, ""));
            List<DepartamentoVO> departamentos = null;
            if (!getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getUnidadeEnsino().equals(0)) {
            	departamentos = getFacadeFactory().getDepartamentoFacade().consultarPorCodigoUnidadeEnsinoESemUE(getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                for (DepartamentoVO depto : departamentos) {
                    objs.add(new SelectItem(depto.getCodigo(), depto.getNome()));
                }
            }
            listaSelectItemDepartamentoFornecedor = objs;
        } catch (Exception e) {
        	setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
    }

    public void verDetalhamentoPlanoOrcamentario() {
    	try {
    		ItemSolicitacaoOrcamentoPlanoOrcamentarioVO item = (ItemSolicitacaoOrcamentoPlanoOrcamentarioVO) context().getExternalContext().getRequestMap().get("itemSolicitacaoOrcamentoPlanoOrcamentarioItens");
    		if (Uteis.isAtributoPreenchido(item.getValorConsumido())) {
    			setDetalhamentosPlanoOrcamentario(getFacadeFactory().getDetalhamentoPlanoOrcamentarioFacade().consultarDetalhamentoPorSolicitacaoOrcamentoPlanoOrcamentario(
					item.getSolicitacaoOrcamentoPlanoOrcamentario().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
    		} else {
    			setDetalhamentosPlanoOrcamentario(new ArrayList<>());
    		}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }
    
    public void responderComunicacaoInterna() {
    	try {
    		ComunicacaoInternaVO comunicacaoInternaVO = (ComunicacaoInternaVO) context().getExternalContext().getRequestMap().get("comunicadoInterno");

			setComunicacaoInternaVO(getFacadeFactory().getComunicacaoInternaFacade().consultarPorChavePrimaria(comunicacaoInternaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }


    public boolean getAprovado() {
    	return getSolicitacaoOrcamentoPlanoOrcamentarioVO().getSituacao().equals(SituacaoPlanoOrcamentarioEnum.APROVADO);
    }

    public boolean getEmConstrucao() {
    	return getSolicitacaoOrcamentoPlanoOrcamentarioVO().getSituacao().equals(SituacaoPlanoOrcamentarioEnum.EM_CONSTRUCAO);
    }

    public void scrollerListenerHistoricoRemanejamento(DataScrollEvent dataScrollerEvent) {
		getDataModeloHistoricoRemanejamento().setPaginaAtual(dataScrollerEvent.getPage());
		getDataModeloHistoricoRemanejamento().setPage(dataScrollerEvent.getPage());
		this.consultarDadosHistoricoRemanejamento();
	}

    /**
     * @return the solicitacaoOrcamentoPlanoOrcamentarioVO
     */
    public SolicitacaoOrcamentoPlanoOrcamentarioVO getSolicitacaoOrcamentoPlanoOrcamentarioVO() {
    	if (solicitacaoOrcamentoPlanoOrcamentarioVO == null) {
    		solicitacaoOrcamentoPlanoOrcamentarioVO = new SolicitacaoOrcamentoPlanoOrcamentarioVO();
    	}
        return solicitacaoOrcamentoPlanoOrcamentarioVO;
    }

    /**
     * @param solicitacaoOrcamentoPlanoOrcamentarioVO the solicitacaoOrcamentoPlanoOrcamentarioVO to set
     */
    public void setSolicitacaoOrcamentoPlanoOrcamentarioVO(SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO) {
        this.solicitacaoOrcamentoPlanoOrcamentarioVO = solicitacaoOrcamentoPlanoOrcamentarioVO;
    }

    /**
     * @return the listaDepartamento
     */
    public List<SelectItem> getListaDepartamento() {
        if (listaDepartamento == null) {
            listaDepartamento = new ArrayList<>();
        }
        return listaDepartamento;
    }

    /**
     * @param listaDepartamento the listaDepartamento to set
     */
    public void setListaDepartamento(List<SelectItem> listaDepartamento) {
        this.listaDepartamento = listaDepartamento;
    }

    /**
     * @return the itemSolicitacaoOrcamentoPlanoOrcamentarioVO
     */
    public ItemSolicitacaoOrcamentoPlanoOrcamentarioVO getItemSolicitacaoOrcamentoPlanoOrcamentarioVO() {
        if (itemSolicitacaoOrcamentoPlanoOrcamentarioVO == null) {
            itemSolicitacaoOrcamentoPlanoOrcamentarioVO = new ItemSolicitacaoOrcamentoPlanoOrcamentarioVO();
        }
        return itemSolicitacaoOrcamentoPlanoOrcamentarioVO;
    }

    /**
     * @param itemSolicitacaoOrcamentoPlanoOrcamentarioVO the itemSolicitacaoOrcamentoPlanoOrcamentarioVO to set
     */
    public void setItemSolicitacaoOrcamentoPlanoOrcamentarioVO(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO) {
        this.itemSolicitacaoOrcamentoPlanoOrcamentarioVO = itemSolicitacaoOrcamentoPlanoOrcamentarioVO;
    }

    public List<SelectItem> getListaSelectItemDepartamentoFornecedor() {
        if (listaSelectItemDepartamentoFornecedor == null) {
            listaSelectItemDepartamentoFornecedor = new ArrayList<>(0);
        }
        return listaSelectItemDepartamentoFornecedor;
    }

    public void setListaSelectItemDepartamentoFornecedor(List<SelectItem> listaSelectItemDepartamentoFornecedor) {
        this.listaSelectItemDepartamentoFornecedor = listaSelectItemDepartamentoFornecedor;
    }

    public boolean getIsApresentarCampoCurso() {
        return getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getCategoriaDespesa().getInformarTurma() != null && getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getCategoriaDespesa().getInformarTurma().equals("CU");
    }

    public boolean getIsApresentarCampoCursoTurno() {
        return getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getCategoriaDespesa().getInformarTurma() != null && getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getCategoriaDespesa().getInformarTurma().equals("CT");
    }

    public boolean getIsApresentarCampoTurma() {
        return getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getCategoriaDespesa().getInformarTurma() != null && getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getCategoriaDespesa().getInformarTurma().equals("TU");
    }

    public boolean getApresentarRecebimento() {
        return getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getCategoriaDespesa().getInformarTurma() != null && getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getCategoriaDespesa().getInformarTurma().equals("TU");
    }

    public List<SelectItem> getTipoConsultaComboCentroDespesa() {
        List<SelectItem> itens = new ArrayList<>(0);
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("identificadorCentroDespesa", "Identificador Centro Despesa"));
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

    public List<CategoriaDespesaVO> getListaConsultaCentroDespesa() {
        if (listaConsultaCentroDespesa == null) {
            listaConsultaCentroDespesa = new ArrayList<>(0);
        }
        return listaConsultaCentroDespesa;
    }

    public void setListaConsultaCentroDespesa(List<CategoriaDespesaVO> listaConsultaCentroDespesa) {
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

    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<>(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    /**
     * @return the listaPlanoOrcamentario
     */
    public List<SelectItem> getListaPlanoOrcamentario() {
        if (listaPlanoOrcamentario == null) {
            listaPlanoOrcamentario = new ArrayList<>();
        }
        return listaPlanoOrcamentario;
    }

    /**
     * @param listaPlanoOrcamentario the listaPlanoOrcamentario to set
     */
    public void setListaPlanoOrcamentario(List<SelectItem> listaPlanoOrcamentario) {
        this.listaPlanoOrcamentario = listaPlanoOrcamentario;
    }

    /**
     * @return the listaUnidadeEnsino
     */
    public List<SelectItem> getListaUnidadeEnsino() {
        if (listaUnidadeEnsino == null) {
            listaUnidadeEnsino = new ArrayList<>();
        }
        return listaUnidadeEnsino;
    }

    /**
     * @param listaUnidadeEnsino the listaUnidadeEnsino to set
     */
    public void setListaUnidadeEnsino(List<SelectItem> listaUnidadeEnsino) {
        this.listaUnidadeEnsino = listaUnidadeEnsino;
    }

    public boolean apresentarBotaoGravar() {
        return !Uteis.isAtributoPreenchido(getSolicitacaoOrcamentoPlanoOrcamentarioVO().getCodigo()) && (
        		getSolicitacaoOrcamentoPlanoOrcamentarioVO().getSituacao().equals(SituacaoPlanoOrcamentarioEnum.EM_CONSTRUCAO)  || 
        		getSolicitacaoOrcamentoPlanoOrcamentarioVO().getSituacao().equals(SituacaoPlanoOrcamentarioEnum.REVISAO));
    }

    public boolean getApresentarSolicitarAprovacao() {
    	
    	return Uteis.isAtributoPreenchido(getSolicitacaoOrcamentoPlanoOrcamentarioVO().getCodigo()) && (
    			getSolicitacaoOrcamentoPlanoOrcamentarioVO().getSituacao().equals(SituacaoPlanoOrcamentarioEnum.EM_CONSTRUCAO)  || 
    			getSolicitacaoOrcamentoPlanoOrcamentarioVO().getSituacao().equals(SituacaoPlanoOrcamentarioEnum.REVISAO));
    }

    public boolean getApresentarValorDistribuirPerioso() {
    	return getSolicitacaoOrcamentoPlanoOrcamentarioVO().getSituacao().equals(SituacaoPlanoOrcamentarioEnum.EM_CONSTRUCAO)  || 
    			getSolicitacaoOrcamentoPlanoOrcamentarioVO().getSituacao().equals(SituacaoPlanoOrcamentarioEnum.REVISAO);
    }
    
    public boolean getApresentarHistoricoRemanjemaneto() {
    	return Uteis.isAtributoPreenchido(getDataModeloHistoricoRemanejamento().getListaConsulta());
    }

	public List<DetalhamentoPlanoOrcamentarioVO> getDetalhamentosPlanoOrcamentario() {
		if (detalhamentosPlanoOrcamentario == null) {
			detalhamentosPlanoOrcamentario = new ArrayList<>();
		}
		return detalhamentosPlanoOrcamentario;
	}

	public void setDetalhamentosPlanoOrcamentario(List<DetalhamentoPlanoOrcamentarioVO> detalhamentosPlanoOrcamentario) {
		this.detalhamentosPlanoOrcamentario = detalhamentosPlanoOrcamentario;
	}

	public ItemSolicitacaoOrcamentoPlanoOrcamentarioVO getItemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado() {
		if (itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado == null) {
			itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado = new ItemSolicitacaoOrcamentoPlanoOrcamentarioVO();
		}
		return itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado;
	}

	public void setItemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado(
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado) {
		this.itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado = itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado;
	}

	public ItemSolicitacaoOrcamentoPlanoOrcamentarioVO getItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado() {
		if (itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado == null) {
			itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado = new ItemSolicitacaoOrcamentoPlanoOrcamentarioVO();
		}
		return itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado;
	}

	public void setItemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado(
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado) {
		this.itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado = itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado;
	}

	public DataModelo getDataModeloHistoricoRemanejamento() {
		if (dataModeloHistoricoRemanejamento == null) {
			dataModeloHistoricoRemanejamento = new DataModelo();
		}
		return dataModeloHistoricoRemanejamento;
	}

	public void setDataModeloHistoricoRemanejamento(DataModelo dataModeloHistoricoRemanejamento) {
		this.dataModeloHistoricoRemanejamento = dataModeloHistoricoRemanejamento;
	}

	public String getMotivo() {
		if (motivo == null) {
			motivo = "";
		}
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public List<ComunicacaoInternaVO> getListaComunicacaoInterna() {
		if (listaComunicacaoInterna == null) {
			listaComunicacaoInterna = new ArrayList<>();
		}
		return listaComunicacaoInterna;
	}

	public void setListaComunicacaoInterna(List<ComunicacaoInternaVO> listaComunicacaoInterna) {
		this.listaComunicacaoInterna = listaComunicacaoInterna;
	}

	public Boolean getFecharModalComunicacaoInterna() {
		if (fecharModalComunicacaoInterna == null) {
			fecharModalComunicacaoInterna = Boolean.FALSE;
		}
		return fecharModalComunicacaoInterna;
	}

	public void setFecharModalComunicacaoInterna(Boolean fecharModalComunicacaoInterna) {
		this.fecharModalComunicacaoInterna = fecharModalComunicacaoInterna;
	}

	public String getMensagemPadraoNotificacao() {
		if (mensagemPadraoNotificacao == null) {
			mensagemPadraoNotificacao = "";
		}
		return mensagemPadraoNotificacao;
	}

	public void setMensagemPadraoNotificacao(String mensagemPadraoNotificacao) {
		this.mensagemPadraoNotificacao = mensagemPadraoNotificacao;
	}

	public Boolean getEnviarComunicadoPorEmail() {
		if (enviarComunicadoPorEmail == null) {
			enviarComunicadoPorEmail = Boolean.TRUE;
		}
		return enviarComunicadoPorEmail;
	}

	public void setEnviarComunicadoPorEmail(Boolean enviarComunicadoPorEmail) {
		this.enviarComunicadoPorEmail = enviarComunicadoPorEmail;
	}

	

	public Boolean getFecharModalRemanejamento() {
		if (fecharModalRemanejamento == null) {
			fecharModalRemanejamento = Boolean.TRUE;
		}
		return fecharModalRemanejamento;
	}

	public void setFecharModalRemanejamento(Boolean fecharModalRemanejamento) {
		this.fecharModalRemanejamento = fecharModalRemanejamento;
	}
}