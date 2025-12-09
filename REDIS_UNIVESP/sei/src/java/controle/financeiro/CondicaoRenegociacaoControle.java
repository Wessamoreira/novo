package controle.financeiro;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.financeiro.CondicaoRenegociacaoFuncionarioVO;
import negocio.comuns.financeiro.CondicaoRenegociacaoUnidadeEnsinoVO;
import negocio.comuns.financeiro.CondicaoRenegociacaoVO;
import negocio.comuns.financeiro.ItemCondicaoRenegociacaoVO;
import negocio.comuns.financeiro.PerfilEconomicoVO;
import negocio.comuns.financeiro.enumerador.LayoutPadraoTermoReconhecimentoDividaCondicaoRenegociacaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.financeiro.CondicaoRenegociacao;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas condicaoRenegociacaoForm.jsp condicaoRenegociacaoCons.jsp) com as funcionalidades da classe
 * <code>CondicaoRenegociacao</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see CondicaoRenegociacao
 * @see CondicaoRenegociacaoVO
 */
@Controller("CondicaoRenegociacaoControle")
@Scope("viewScope")
public class CondicaoRenegociacaoControle extends SuperControle {

    private static final long serialVersionUID = 1L;
    private CondicaoRenegociacaoVO condicaoRenegociacaoVO;
    private ItemCondicaoRenegociacaoVO itemCondicaoRenegociacaoVO;
    protected List<SelectItem> listaSelectItemUnidadeEnsino;
    protected List<SelectItem> listaSelectItemContaCorrentePadrao;
    protected List<SelectItem> listaSelectItemDescontoProgressivo;
    protected List<SelectItem> listaSelectItemGrupoDestinatario;
    protected List<SelectItem> listaSelectItemPerfilEconomico;
    protected List<SelectItem> listaSelectItemTurno;
    protected List<CursoVO> listaConsultarCurso;
    protected List<TurmaVO> listaConsultarTurma;
    private boolean mostrarItensUnidadeEnsino = false;
    private String campoConsultar;
    private String valorConsultar;
    private String campoConsultarTurma;
    private String valorConsultarTurma;
    private String valorConsultarTurmaDireto;
    private String campoConsultarCurso;
    private String valorConsultarCurso;
    private List<SelectItem> listaSelectItemTextoPadraoDeclaracao;
    private List<SelectItem> listaSelectItemLayoutPadraoTermoReconhecimentoDivida;
    private Boolean marcarTodasOrigensContaReceber;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private UnidadeEnsinoVO unidadeEnsinoCondicaoRenegociacaoVO;
    private String campoConsultaFuncionario;
    private String valorConsultaFuncionario;
    private List<FuncionarioVO> listaConsultaFuncionario;
    private CondicaoRenegociacaoFuncionarioVO condicaoRenegociacaoFuncionarioVO;
    private Boolean visualizandoItemCondicaoRenegociacao;
    private String status;
    private List<SelectItem> listaSelectItemStatus;
    private String controleAba;

    public CondicaoRenegociacaoControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        montarListaSelectItemUnidadeEnsino();
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
    }
    
    @PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("CondicaoRenegociacaoControle");
			verificarTodasUnidadesSelecionadas();
			setControleAba("condicaoAtiva");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>CondicaoRenegociacao</code> para edição pelo usuário da aplicação.
     */
    public String novo() {
        try {
            setCondicaoRenegociacaoVO(getFacadeFactory().getCondicaoRenegociacaoFacade().inicializarDadosCondicaoRenegociacaoNovo(getUsuarioLogado()));
            setItemCondicaoRenegociacaoVO(new ItemCondicaoRenegociacaoVO());
            getItemCondicaoRenegociacaoVO().setStatus("EM");
            montarListaSelectItemUnidadeEnsino();
            consultarUnidadeEnsinoFiltroRelatorio("");
            getFacadeFactory().getCondicaoRenegociacaoFacade().inicializarDadosUnidadeEnsinoSelecionada(getCondicaoRenegociacaoVO(), getUnidadeEnsinoVOs(), getUsuarioLogado());
            setMensagemID("msg_entre_dados", Uteis.ALERTA);
            inicializarListasSelectItemComboBox();
            setUnidadeEnsinoVO(null);
            setVisualizandoItemCondicaoRenegociacao(false);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoRenegociacaoForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>CondicaoRenegociacao</code> para alteração. O objeto desta classe é disponibilizado na session da
     * página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() {
        try {
            CondicaoRenegociacaoVO obj = (CondicaoRenegociacaoVO) context().getExternalContext().getRequestMap().get("condicaoRenegociacaoItens");
            obj.setNovoObj(Boolean.FALSE);
            setCondicaoRenegociacaoVO(getFacadeFactory().getCondicaoRenegociacaoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            if (getCondicaoRenegociacaoVO().getUtilizarContaCorrenteEspecifica()) {
            	montarListaSelectItemContaCorrentePadrao(0);
            }
            consultarUnidadeEnsinoFiltroRelatorio("CondicaoRenegociacao");
            getFacadeFactory().getCondicaoRenegociacaoFacade().inicializarDadosUnidadeEnsinoSelecionadaEdicao(getCondicaoRenegociacaoVO(), getUnidadeEnsinoVOs(), getUsuarioLogado());
            verificarTodasUnidadesSelecionadas();
            montarListaSelectItemDescontoProgressivo();       
            montarListaSelectItemGrupoDestinatario();
            montarListaSelectItemTextoPadraoDeclaracao();
            setItemCondicaoRenegociacaoVO(new ItemCondicaoRenegociacaoVO());
            getItemCondicaoRenegociacaoVO().setStatus("EM");
            setVisualizandoItemCondicaoRenegociacao(false);
            setMensagemID("msg_dados_editar", Uteis.ALERTA);
            return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoRenegociacaoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_dados_erro", e.getMessage(), Uteis.ALERTA);
            return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoRenegociacaoCons.xhtml");
        }
    }

    /**
     * Método responsável inicializar objetos relacionados a classe <code>CondicaoRenegociacaoVO</code>. Esta inicialização é necessária por exigência da tecnologia JSF, que não
     * trabalha com valores nulos para estes atributos.
     */

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>CondicaoRenegociacao</code>. Caso o objeto seja novo (ainda não gravado no BD) é
     * acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public void persistir() {
        try {
        	getFacadeFactory().getCondicaoRenegociacaoFacade().persistir(getCondicaoRenegociacaoVO(), getUsuarioLogado());
//          for (SelectItem unidade : getListaSelectItemUnidadeEnsino()) {
//              if (((Integer) unidade.getValue()).intValue() == condicaoRenegociacaoVO.getUnidadeEnsino().getCodigo().intValue()) {
//                  condicaoRenegociacaoVO.getUnidadeEnsino().setNome(unidade.getLabel());
//                  break;
//              }
//          }
            for (SelectItem turno : (List<SelectItem>)getListaSelectItemTurno()) {
                if (((Integer) turno.getValue()).intValue() == condicaoRenegociacaoVO.getTurno().getCodigo().intValue()) {
                    condicaoRenegociacaoVO.getTurno().setNome(turno.getLabel());
                    break;
                }
            }
            for (SelectItem perfil : (List<SelectItem>)getListaSelectItemPerfilEconomico()) {
                if (((Integer) perfil.getValue()).intValue() == condicaoRenegociacaoVO.getPerfilEconomico().getCodigo().intValue()) {
                    condicaoRenegociacaoVO.getPerfilEconomico().setNome(perfil.getLabel());
                    break;
                }
            }
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
        	e.printStackTrace();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {            
        }
    }
    
    public void realizarClonagem(){
        try{
            getFacadeFactory().getCondicaoRenegociacaoFacade().realizarClonagem(getCondicaoRenegociacaoVO());
            montarListaSelectItemPerfilEconomico();
            montarListaSelectItemTurnoPorUnidadeEnsinoCondicaoRenegociacao(getCondicaoRenegociacaoVO().getListaCondicaoRenegociacaoUnidadeEnsinoVOs());
            setMensagemID("msg_dados_clonados", Uteis.SUCESSO);
        }catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    
    public String consultar() {
        try {
            super.consultar();
//            Uteis.checkState(!Uteis.isAtributoPreenchido(getUnidadeEnsinoVO()), "O campos Unidade Ensino deve ser informado.");
            setListaConsulta(getFacadeFactory().getCondicaoRenegociacaoFacade().consultar(getCondicaoRenegociacaoVO().getDescricao(), 
                    getUnidadeEnsinoVO().getCodigo(), getCondicaoRenegociacaoVO().getTurno().getCodigo(), 
                    getCondicaoRenegociacaoVO().getCurso().getCodigo(), getCondicaoRenegociacaoVO().getTurma().getCodigo(), 
                    getCondicaoRenegociacaoVO().getContaCorrentePadrao().getCodigo(), getCondicaoRenegociacaoVO().getPerfilEconomico().getCodigo(), getStatus(),
                    true, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoRenegociacaoCons.xhtml");
    }

    
    public String excluir() {
        try {
            getFacadeFactory().getCondicaoRenegociacaoFacade().excluir(condicaoRenegociacaoVO);
            novo();
            setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }

        return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoRenegociacaoForm.xhtml");
    }

    
    public void adicionarItemCondicaoRenegociacao()  {
        try {
        	if (!getVisualizandoItemCondicaoRenegociacao()) {
        		if (!getCondicaoRenegociacaoVO().getCodigo().equals(0)) {
        			getItemCondicaoRenegociacaoVO().setCondicaoRenegociacao(getCondicaoRenegociacaoVO());
        		}
        		getFacadeFactory().getCondicaoRenegociacaoFacade().adicionarObjItemCondicaoRenegociacaoVOs(getCondicaoRenegociacaoVO(), getItemCondicaoRenegociacaoVO());
        	} else {
        		setVisualizandoItemCondicaoRenegociacao(false);
        	}
            this.setItemCondicaoRenegociacaoVO(new ItemCondicaoRenegociacaoVO());
            getItemCondicaoRenegociacaoVO().setStatus("EM");
            setControleAba("condicaoEmConstrucao");
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

   
    public void editarItemCondicaoRenegociacao() {
    	try {
    		ItemCondicaoRenegociacaoVO obj = (ItemCondicaoRenegociacaoVO) context().getExternalContext().getRequestMap().get("itemCondicaoRenegociacaoItens");
            getCondicaoRenegociacaoVO().getItemCondicaoRenegociacaoVOs().remove(obj);
            setItemCondicaoRenegociacaoVO(obj);	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
        
    }

    public void realizarAtivacaoItemCondicaoRenegociacao() {
        try {
            ItemCondicaoRenegociacaoVO obj = (ItemCondicaoRenegociacaoVO) context().getExternalContext().getRequestMap().get("itemCondicaoRenegociacaoItens");
            getFacadeFactory().getCondicaoRenegociacaoFacade().realizarAtivacaoItemCondicaoRenegociacao(condicaoRenegociacaoVO, obj, getUsuarioLogado());
            setMensagemID("msg_dados_ativado", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void realizarInativacaoItemCondicaoRenegociacao() {
        try {
            ItemCondicaoRenegociacaoVO obj = (ItemCondicaoRenegociacaoVO) context().getExternalContext().getRequestMap().get("itemCondicaoRenegociacaoItens");
            getFacadeFactory().getCondicaoRenegociacaoFacade().realizarInativacaoItemCondicaoRenegociacao(condicaoRenegociacaoVO, obj, getUsuarioLogado());
            setMensagemID("msg_dados_inativado", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void realizarAtivacaoCondicaoRenegociacao() {
        try {
            getFacadeFactory().getCondicaoRenegociacaoFacade().realizarAtivacaoCondicaoRenegociacao(condicaoRenegociacaoVO, getUsuarioLogado());
            setMensagemID("msg_dados_ativado", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void realizarInativacaoCondicaoRenegociacao() {
        try {
            getFacadeFactory().getCondicaoRenegociacaoFacade().realizarInativacaoCondicaoRenegociacao(condicaoRenegociacaoVO, getUsuarioLogado());
            setMensagemID("msg_dados_inativado", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    /*
     * Método responsável por remover um novo objeto da classe <code>ItemCondicaoRenegociacao</code> do objeto <code>condicaoRenegociacaoVO</code> da classe
     * <code>CondicaoRenegociacao</code>
     */
    public void removerItemCondicaoRenegociacao() throws Exception {
        ItemCondicaoRenegociacaoVO obj = (ItemCondicaoRenegociacaoVO) context().getExternalContext().getRequestMap().get("itemCondicaoRenegociacaoItens");
        getFacadeFactory().getCondicaoRenegociacaoFacade().excluirObjItemCondicaoRenegociacaoVOs(getCondicaoRenegociacaoVO(), obj);
        setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>DescontoProgressivo</code>.
     */

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>ContaCorrentePadrao</code>. Buscando todos os objetos correspondentes a entidade
     * <code>ContaCorrente</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
     * requisições Ajax.
     */

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>codigo</code> Este atributo é uma lista (<code>List</code>) utilizada para definir
     * os valores a serem apresentados no ComboBox correspondente
     */

    /**
     * Método responsável por processar a consulta na entidade <code>Turma</code> por meio dos parametros informados no richmodal. Esta rotina é utilizada fundamentalmente por
     * requisições Ajax, que realizam busca pelos parâmentros informados no richModal montando automaticamente o resultado da consulta para apresentação.
     */

    public void limparCampoTurma() {
        this.getCondicaoRenegociacaoVO().setTurma(null);
    }

    public void limparCampoCurso() {
        limparCampoTurma();
        this.getCondicaoRenegociacaoVO().setCurso(null);
    }
    
    public void consultarCursoPorUnidadeEnsinoCondicaoRenegociacao() {
        try {
            setListaConsultarCurso(getFacadeFactory().getCondicaoRenegociacaoFacade().consultarCursoPorUnidadeEnsinoCondicaoRenegociacao(getCampoConsultarCurso(), getValorConsultarCurso(), getCondicaoRenegociacaoVO().getListaCondicaoRenegociacaoUnidadeEnsinoVOs(), getCondicaoRenegociacaoVO().getTurno().getCodigo(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {

            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Curso</code> por meio dos parametros informados no richmodal. Esta rotina é utilizada fundamentalmente por
     * requisições Ajax, que realizam busca pelos parâmentros informados no richModal montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarCurso() {
        try {
            setListaConsultarCurso(getFacadeFactory().getCondicaoRenegociacaoFacade().consultarCurso(getCampoConsultarCurso(), getValorConsultarCurso(), getUnidadeEnsinoVO().getCodigo(), getCondicaoRenegociacaoVO().getTurno().getCodigo(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {

            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void selecionarCurso() throws Exception {
        CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
        if (getMensagemDetalhada().equals("")) {
            this.getCondicaoRenegociacaoVO().setCurso(obj);
        }
        Uteis.liberarListaMemoria(this.getListaConsultarCurso());
        this.setValorConsultarCurso(null);
        this.setCampoConsultarCurso(null);

    }

    public void consultarTurma() {
        try {
            setListaConsultarTurma(getFacadeFactory().getCondicaoRenegociacaoFacade().consultarTurma(campoConsultarTurma, valorConsultarTurma, getUnidadeEnsinoVO().getCodigo(),
                    getCondicaoRenegociacaoVO().getCurso().getCodigo(), getCondicaoRenegociacaoVO().getTurno().getCodigo(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {
            getListaConsultarTurma().clear();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    
    public void consultarFuncionario() {
		try {
			List<FuncionarioVO> objs = new ArrayList<>(0);
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaFuncionario().equals("NOME")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "", 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("MATRICULA")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), 0, null, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCidade(getValorConsultaFuncionario(), 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "", 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CARGO")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionario(), "", 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("UNIDADEENSINO")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "", 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
    
    public void adicionarCondicaoRenegociacaoFuncionario() {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
			getCondicaoRenegociacaoFuncionarioVO().setFuncionarioVO(obj);
			getListaConsultaFuncionario().remove(obj);
			getFacadeFactory().getCondicaoRenegociacaoFacade().adicionarCondicaoRenegociacaoFuncionarVOs(getCondicaoRenegociacaoVO().getListaCondicaoRenegociacaoFuncionarioVOs(), getCondicaoRenegociacaoFuncionarioVO(), getUsuarioLogado());
			setCondicaoRenegociacaoFuncionarioVO(new CondicaoRenegociacaoFuncionarioVO());			
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
    
    public void excluirCondicaoRenegociacaoFuncionario() {
    	CondicaoRenegociacaoFuncionarioVO obj = (CondicaoRenegociacaoFuncionarioVO) context().getExternalContext().getRequestMap().get("condicaoRenegociacaoFuncionarioItens");
    	getFacadeFactory().getCondicaoRenegociacaoFacade().removerCondicaoRenegociacaoFuncionarVOs(getCondicaoRenegociacaoVO().getListaCondicaoRenegociacaoFuncionarioVOs(), obj, getUsuarioLogado());
    }
    
    public void inicializarDadosConsultaFuncionario() {
    	getListaConsultaFuncionario().clear();
    	setValorConsultaFuncionario("");
    }
    
    public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("NOME", "Nome"));
		itens.add(new SelectItem("MATRICULA", "Matrícula"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("CARGO", "Cargo"));
		return itens;
	}
    
    
    public void consultarTurmaPorUnidadeEnsinoCondicaoRenegociacao() {
        try {
            setListaConsultarTurma(getFacadeFactory().getCondicaoRenegociacaoFacade().consultarTurmaPorUnidadeEnsinoCondicaoRenegociacao(campoConsultarTurma, valorConsultarTurma, getCondicaoRenegociacaoVO().getListaCondicaoRenegociacaoUnidadeEnsinoVOs(),
                    getCondicaoRenegociacaoVO().getCurso().getCodigo(), getCondicaoRenegociacaoVO().getTurno().getCodigo(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {
            getListaConsultarTurma().clear();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void selecionarTurma() throws Exception {
        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
        if (getMensagemDetalhada().equals("")) {
            this.getCondicaoRenegociacaoVO().setCurso(obj.getCurso());
            this.getCondicaoRenegociacaoVO().setTurma(obj);
            this.getCondicaoRenegociacaoVO().setTurno(obj.getTurno());
        }
        Uteis.liberarListaMemoria(this.getListaConsultarTurma());
        this.setValorConsultarTurma(null);
        this.setCampoConsultarTurma(null);
    }

    public void consultarTurmaIdentificador() {

    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemComboBox() throws Exception {
      //  montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemDescontoProgressivo();
        montarListaSelectItemPerfilEconomico();
        montarListaSelectItemGrupoDestinatario();
    }

    public void inicializarListasSelectItemComboBoxUnidadeEnsino() throws Exception {
        Integer codigoUnidadeEnsino = getUnidadeEnsinoVO().getCodigo();
        if (codigoUnidadeEnsino != null && codigoUnidadeEnsino > 0) {
            montarListaSelectItemTurno(codigoUnidadeEnsino);
            montarListaSelectItemContaCorrentePadrao(codigoUnidadeEnsino);
            setMostrarItensUnidadeEnsino(true);
        } else {
            getListaSelectItemContaCorrentePadrao().clear();
            getListaSelectItemTurno().clear();
            getCondicaoRenegociacaoVO().setTurno(null);
            getCondicaoRenegociacaoVO().setContaCorrentePadrao(null);
            limparCampoCurso();
            setMostrarItensUnidadeEnsino(false);
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>. Buscando todos os objetos correspondentes a entidade <code>UnidadeEnsino</code>.
     * Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemUnidadeEnsino() throws Exception {
        setListaSelectItemUnidadeEnsino(getFacadeFactory().getCondicaoRenegociacaoFacade().montarListSelectItemUnidadeEnsino(getUsuarioLogado()));
    }
    
    public void montarListaSelectItemGrupoDestinatario() throws Exception {
        setListaSelectItemGrupoDestinatario(getFacadeFactory().getGrupoDestinatariosFacade().consultarDadosListaSelectItem(Obrigatorio.NAO));
    }
    
    
    
    
    public List<SelectItem> getListaSelectItemPerfilEconomico() {
        if(listaSelectItemPerfilEconomico == null){
            listaSelectItemPerfilEconomico = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemPerfilEconomico;
    }

    
    public void setListaSelectItemPerfilEconomico(List<SelectItem> listaSelectItemPerfilEconomico) {
        this.listaSelectItemPerfilEconomico = listaSelectItemPerfilEconomico;
    }

    public void montarListaSelectItemPerfilEconomico() throws Exception {
        List<PerfilEconomicoVO> perfilEconomicoVOs = getFacadeFactory().getPerfilEconomicoFacade().consultarPorCodigo(0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        getListaSelectItemPerfilEconomico().clear();
        getListaSelectItemPerfilEconomico().add(new SelectItem(0, "Todos"));
        for(PerfilEconomicoVO perfilEconomicoVO:perfilEconomicoVOs){
            getListaSelectItemPerfilEconomico().add(new SelectItem(perfilEconomicoVO.getCodigo(), perfilEconomicoVO.getNome()));
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>DescontoProgressivo</code>. Buscando todos os objetos correspondentes a entidade
     * <code>DescontoProgressivo</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por
     * meio requisições Ajax.
     */
    public void montarListaSelectItemDescontoProgressivo() throws Exception {
        setListaSelectItemDescontoProgressivo(getFacadeFactory().getCondicaoRenegociacaoFacade().montarListSelectItemDescontoProgressivo(getUsuarioLogado()));

    }
    
    public void montarListaSelectItemContaCorrentePadrao() {
    	try {
			montarListaSelectItemContaCorrentePadrao(0);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>ContaCorrentePadrao</code>.
     */
    public void montarListaSelectItemContaCorrentePadrao(Integer codigoUnidadeEnsino) throws Exception {
        setListaSelectItemContaCorrentePadrao(getFacadeFactory().getCondicaoRenegociacaoFacade().montarListSelectItemContaCorrente(codigoUnidadeEnsino, false, getUsuarioLogado()));
        if(getListaSelectItemContaCorrentePadrao().size() == 1) {
        	if(!getCondicaoRenegociacaoVO().getContaCorrentePadrao().getCodigo().equals(0)) {
        		if (Uteis.isAtributoPreenchido(getCondicaoRenegociacaoVO().getContaCorrentePadrao().getNomeApresentacaoSistema())) {
            		getListaSelectItemContaCorrentePadrao().add(new SelectItem(getCondicaoRenegociacaoVO().getContaCorrentePadrao().getCodigo(), getCondicaoRenegociacaoVO().getContaCorrentePadrao().getNomeApresentacaoSistema()));
				}else{
					getListaSelectItemContaCorrentePadrao().add(new SelectItem(getCondicaoRenegociacaoVO().getContaCorrentePadrao().getCodigo(), getCondicaoRenegociacaoVO().getContaCorrentePadrao().getNumeroDigito()));
				}
        	}
        } else {
        	if(!getListaSelectItemContaCorrentePadrao().contains(new SelectItem(getCondicaoRenegociacaoVO().getContaCorrentePadrao().getCodigo(), getCondicaoRenegociacaoVO().getContaCorrentePadrao().getNumeroDigito()))) {
        		if(Uteis.isAtributoPreenchido(getCondicaoRenegociacaoVO().getContaCorrentePadrao().getNomeApresentacaoSistema())){
        			getListaSelectItemContaCorrentePadrao().add(new SelectItem(getCondicaoRenegociacaoVO().getContaCorrentePadrao().getCodigo(), getCondicaoRenegociacaoVO().getContaCorrentePadrao().getNomeApresentacaoSistema()));	
        		}else{
        			getListaSelectItemContaCorrentePadrao().add(new SelectItem(getCondicaoRenegociacaoVO().getContaCorrentePadrao().getCodigo(), getCondicaoRenegociacaoVO().getContaCorrentePadrao().getNumeroDigito()));	
        		}
        	}
        }
    }
    
    public void montarListaSelectItemTurnoPorUnidadeEnsinoCondicaoRenegociacao(List<CondicaoRenegociacaoUnidadeEnsinoVO> listaCondicaoUnidadeEnsinoVOs) throws Exception {
        setListaSelectItemTurno(getFacadeFactory().getCondicaoRenegociacaoFacade().montarListSelectItemTurno(listaCondicaoUnidadeEnsinoVOs, getUsuarioLogado()));

    }

    public void montarListaSelectItemTurno(Integer codigoUnidadeEnsino) throws Exception {
        setListaSelectItemTurno(getFacadeFactory().getCondicaoRenegociacaoFacade().montarListSelectItemTurno(codigoUnidadeEnsino, getUsuarioLogado()));

    }

    /**
     * Método responsável por processar a consulta na entidade <code>Usuario</code> por meio de sua respectiva chave primária. Esta rotina é utilizada fundamentalmente por
     * requisições Ajax, que realizam busca pela chave primária da entidade montando automaticamente o resultado da consulta para apresentação.
     */

    /**
     * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
     */
    public String getMascaraConsulta() {
        return "";
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        getListaConsulta().clear();
        setCondicaoRenegociacaoVO(new CondicaoRenegociacaoVO());
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoRenegociacaoCons.xhtml");
    }

    public List<SelectItem> getTipoConsultaCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(2);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("descricao", "Descrição"));
        return itens;
    }

    public List<SelectItem> getTipoConsultaComboCurso() {
        List<SelectItem> itens = new ArrayList<SelectItem>(2);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public List<SelectItem> getTipoConsultaComboTurma() {
        List<SelectItem> itens = new ArrayList<SelectItem>(1);
        itens.add(new SelectItem("identificador", "Identificador"));
        return itens;
    }

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação do Garbage Coletor do Java. A mesma é automaticamente
     * quando realiza o logout.
     */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        condicaoRenegociacaoVO = null;

        Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
        Uteis.liberarListaMemoria(listaSelectItemContaCorrentePadrao);
        Uteis.liberarListaMemoria(listaSelectItemDescontoProgressivo);
        itemCondicaoRenegociacaoVO = null;
    }

    public ItemCondicaoRenegociacaoVO getItemCondicaoRenegociacaoVO() {
    	if (itemCondicaoRenegociacaoVO == null) {
    		itemCondicaoRenegociacaoVO = new ItemCondicaoRenegociacaoVO();
    	}
        return itemCondicaoRenegociacaoVO;
    }

    public void setItemCondicaoRenegociacaoVO(ItemCondicaoRenegociacaoVO itemCondicaoRenegociacaoVO) {
        this.itemCondicaoRenegociacaoVO = itemCondicaoRenegociacaoVO;
    }

    public List<SelectItem> getListaSelectItemDescontoProgressivo() {
        if (listaSelectItemDescontoProgressivo == null) {
            listaSelectItemDescontoProgressivo = new ArrayList<SelectItem>(0);
        }
        return (listaSelectItemDescontoProgressivo);
    }

    public List<SelectItem> getListaSelectItemContaCorrentePadrao() {
        if (listaSelectItemContaCorrentePadrao == null) {
            listaSelectItemContaCorrentePadrao = new ArrayList<SelectItem>(0);
        }
        return (listaSelectItemContaCorrentePadrao);
    }

    public String getCampoConsultarTurma() {
        if (campoConsultarTurma == null) {
            campoConsultarTurma = "";
        }
        return campoConsultarTurma;
    }

    public void setCampoConsultarTurma(String campoConsultarTurma) {
        this.campoConsultarTurma = campoConsultarTurma;
    }

    public String getValorConsultarTurma() {
        if (valorConsultarTurma == null) {
            valorConsultarTurma = "";
        }
        return valorConsultarTurma;
    }

    public void setValorConsultarTurma(String valorConsultarTurma) {
        this.valorConsultarTurma = valorConsultarTurma;
    }

    public CondicaoRenegociacaoVO getCondicaoRenegociacaoVO() {
        if (condicaoRenegociacaoVO == null) {
            ;
            condicaoRenegociacaoVO = new CondicaoRenegociacaoVO();
        }
        return condicaoRenegociacaoVO;
    }

    public void setCondicaoRenegociacaoVO(CondicaoRenegociacaoVO condicaoRenegociacaoVO) {
        this.condicaoRenegociacaoVO = condicaoRenegociacaoVO;
    }

    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
        }

        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(
            List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public List<SelectItem> getListaSelectItemTurno() {
        if (listaSelectItemTurno == null) {
            listaSelectItemTurno = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemTurno;
    }

    public void setListaSelectItemTurno(List<SelectItem> listaSelectItemTurno) {
        this.listaSelectItemTurno = listaSelectItemTurno;
    }

    public List<CursoVO> getListaConsultarCurso() {
        if (listaConsultarCurso == null) {
            listaConsultarCurso = new ArrayList<CursoVO>(0);
        }
        return listaConsultarCurso;
    }

    public void setListaConsultarCurso(List<CursoVO> listaConsultaCurso) {
        this.listaConsultarCurso = listaConsultaCurso;
    }

    public List<TurmaVO> getListaConsultarTurma() {
        if (listaConsultarTurma == null) {
            listaConsultarTurma = new ArrayList<>(0);
        }
        return listaConsultarTurma;
    }

    public void setListaConsultarTurma(List<TurmaVO> listaConsultarTurma) {
        this.listaConsultarTurma = listaConsultarTurma;
    }

    public String getCampoConsultar() {
        if (campoConsultar == null) {
            campoConsultar = "";
        }
        return campoConsultar;
    }

    public void setCampoConsultar(String campoConsultar) {
        this.campoConsultar = campoConsultar;
    }

    public String getValorConsultar() {
        if (valorConsultar == null) {
            valorConsultar = "";
        }
        return valorConsultar;
    }

    public void setValorConsultar(String valorConsultar) {
        this.valorConsultar = valorConsultar;
    }

    public void setListaSelectItemContaCorrentePadrao(
            List<SelectItem> listaSelectItemContaCorrentePadrao) {
        this.listaSelectItemContaCorrentePadrao = listaSelectItemContaCorrentePadrao;
    }

    public void setListaSelectItemDescontoProgressivo(
            List<SelectItem> listaSelectItemDescontoProgressivo) {
        this.listaSelectItemDescontoProgressivo = listaSelectItemDescontoProgressivo;
    }

    public String getCampoConsultarCurso() {
        if (campoConsultarCurso == null) {
            campoConsultarCurso = "";
        }
        return campoConsultarCurso;
    }

    public void setCampoConsultarCurso(String campoConsultarCurso) {
        this.campoConsultarCurso = campoConsultarCurso;
    }

    public String getValorConsultarCurso() {
        if (valorConsultarCurso == null) {
            valorConsultarCurso = "";
        }
        return valorConsultarCurso;
    }

    public void setValorConsultarCurso(String valorConsultarCurso) {
        this.valorConsultarCurso = valorConsultarCurso;
    }

    public String getValorConsultarTurmaDireto() {
        if (valorConsultarTurmaDireto == null) {
            valorConsultarTurmaDireto = "";
        }
        return valorConsultarTurmaDireto;
    }

    public void setValorConsultarTurmaDireto(String valorConsultarTurmaDireto) {
        this.valorConsultarTurmaDireto = valorConsultarTurmaDireto;
    }

    public boolean getMostrarItensUnidadeEnsino() {
        return mostrarItensUnidadeEnsino;
    }

    public void setMostrarItensUnidadeEnsino(boolean mostrarItensUnidadeEnsino) {
        this.mostrarItensUnidadeEnsino = mostrarItensUnidadeEnsino;
    }

    
    public List<SelectItem> getListaSelectItemGrupoDestinatario() {
        if(listaSelectItemGrupoDestinatario == null){
            listaSelectItemGrupoDestinatario = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemGrupoDestinatario;
    }

    
    public void setListaSelectItemGrupoDestinatario(List<SelectItem> listaSelectItemGrupoDestinatario) {
        this.listaSelectItemGrupoDestinatario = listaSelectItemGrupoDestinatario;
    }
        
	public List<SelectItem> getListaSelectItemLayoutPadraoTermoReconhecimentoDivida() {
		if (listaSelectItemLayoutPadraoTermoReconhecimentoDivida == null) {
			listaSelectItemLayoutPadraoTermoReconhecimentoDivida = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(LayoutPadraoTermoReconhecimentoDividaCondicaoRenegociacaoEnum.class);
		}
		return listaSelectItemLayoutPadraoTermoReconhecimentoDivida;
	}
    
	public void montarListaSelectItemTextoPadraoDeclaracao() {
		try {
			if (getCondicaoRenegociacaoVO().getLayoutPadraoTermoReconhecimentoDivida() != null && getCondicaoRenegociacaoVO().getLayoutPadraoTermoReconhecimentoDivida().equals(LayoutPadraoTermoReconhecimentoDividaCondicaoRenegociacaoEnum.TEXTO_PADRAO)) {
				consultarListaSelectItemTipoTextoPadrao(0);
			} else {
				getCondicaoRenegociacaoVO().setTextoPadraoDeclaracaoVO(null);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarListaSelectItemTipoTextoPadrao(Integer unidadeEnsino) {
		try {
			List<TextoPadraoDeclaracaoVO> textoPadraoDeclaracaoVOs = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("RD", unidadeEnsino, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setListaSelectItemTextoPadraoDeclaracao(UtilSelectItem.getListaSelectItem(textoPadraoDeclaracaoVOs, "codigo", "descricao", false));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemTextoPadraoDeclaracao() {
		if (listaSelectItemTextoPadraoDeclaracao == null) {
			listaSelectItemTextoPadraoDeclaracao = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTextoPadraoDeclaracao;
	}

	public void setListaSelectItemTextoPadraoDeclaracao(List<SelectItem> listaSelectItemTextoPadraoDeclaracao) {
		this.listaSelectItemTextoPadraoDeclaracao = listaSelectItemTextoPadraoDeclaracao;
	}

	public void realizarSelecaoCheckboxMarcarDesmarcarTodosOrigensContaReceber() {
		if (getMarcarTodasOrigensContaReceber()) {
			realizarMarcarTodasOrigensContaReceber();
		} else {
			realizarDesmarcarTodasOrigensContaReceber();
		}
	}
	
	public void realizarMarcarTodasOrigensContaReceber(){
		realizarSelecionarTodosOrigensContaReceber(true);
	}
	
	public void realizarDesmarcarTodasOrigensContaReceber(){
		realizarSelecionarTodosOrigensContaReceber(false);
	}
	
	public void realizarSelecionarTodosOrigensContaReceber(Boolean selecionado){
		getItemCondicaoRenegociacaoVO().setTipoOrigemMatricula(selecionado);
		getItemCondicaoRenegociacaoVO().setTipoOrigemMensalidade(selecionado);
		getItemCondicaoRenegociacaoVO().setTipoOrigemBiblioteca(selecionado);
		getItemCondicaoRenegociacaoVO().setTipoOrigemDevolucaoCheque(selecionado);
		getItemCondicaoRenegociacaoVO().setTipoOrigemNegociacao(selecionado);
		getItemCondicaoRenegociacaoVO().setTipoOrigemBolsaCusteadaConvenio(selecionado);
		getItemCondicaoRenegociacaoVO().setTipoOrigemContratoReceita(selecionado);
		getItemCondicaoRenegociacaoVO().setTipoOrigemOutros(selecionado);
		getItemCondicaoRenegociacaoVO().setTipoOrigemInclusaoReposicao(selecionado);
	}
	
	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodosOrigemContaReceber() {
		if (getMarcarTodasOrigensContaReceber()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}
	
	public void marcarTodasUnidadesEnsinoAction() {
		for (CondicaoRenegociacaoUnidadeEnsinoVO condicaoUnidade : getCondicaoRenegociacaoVO().getListaCondicaoRenegociacaoUnidadeEnsinoVOs()) {
			if (getMarcarTodasUnidadeEnsino()) {
				condicaoUnidade.getUnidadeEnsinoVO().setFiltrarUnidadeEnsino(Boolean.TRUE);
			} else {
				condicaoUnidade.getUnidadeEnsinoVO().setFiltrarUnidadeEnsino(Boolean.FALSE);
			}
		}
		verificarTodasUnidadesSelecionadas();
	}
	
	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getCondicaoRenegociacaoVO().getListaCondicaoRenegociacaoUnidadeEnsinoVOs().size() > 1) {
			for (CondicaoRenegociacaoUnidadeEnsinoVO obj : getCondicaoRenegociacaoVO().getListaCondicaoRenegociacaoUnidadeEnsinoVOs()) {
				if (obj.getUnidadeEnsinoVO().getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getUnidadeEnsinoVO().getNome()).append("; ");
					try {
						if (obj.getListaContaCorrenteCondicaoRenegociacaoVOs().isEmpty()) {
							obj.setListaContaCorrenteCondicaoRenegociacaoVOs(getFacadeFactory().getCondicaoRenegociacaoFacade().montarListSelectItemContaCorrente(obj.getUnidadeEnsinoVO().getCodigo(), true, getUsuarioLogado()));
							if(!Uteis.isAtributoPreenchido(obj.getListaContaCorrenteCondicaoRenegociacaoVOs())) {
								obj.getUnidadeEnsinoVO().setFiltrarUnidadeEnsino(Boolean.FALSE);
								continue;
							}
						}
					} catch (Exception e) {
						setMensagemDetalhada("msg_erro", e.getMessage());
					}
				} 
			}
			getUnidadeEnsinoVO().setNome(unidade.toString());
		} else {
			if (!getCondicaoRenegociacaoVO().getListaCondicaoRenegociacaoUnidadeEnsinoVOs().isEmpty()) {
				if (getCondicaoRenegociacaoVO().getListaCondicaoRenegociacaoUnidadeEnsinoVOs().get(0).getUnidadeEnsinoVO().getFiltrarUnidadeEnsino()) {
					getUnidadeEnsinoVO().setNome(getUnidadeEnsinoVOs().get(0).getNome());
					try {
						if (getCondicaoRenegociacaoVO().getListaCondicaoRenegociacaoUnidadeEnsinoVOs().get(0).getListaContaCorrenteCondicaoRenegociacaoVOs().isEmpty()) {
							getCondicaoRenegociacaoVO().getListaCondicaoRenegociacaoUnidadeEnsinoVOs().get(0).setListaContaCorrenteCondicaoRenegociacaoVOs(getFacadeFactory().getCondicaoRenegociacaoFacade().montarListSelectItemContaCorrente(getCondicaoRenegociacaoVO().getListaCondicaoRenegociacaoUnidadeEnsinoVOs().get(0).getUnidadeEnsinoVO().getCodigo(), true, getUsuarioLogado()));
						}
					} catch (Exception e) {
						setMensagemDetalhada("msg_erro", e.getMessage());
					}
				}
			} else {
				getUnidadeEnsinoVO().setNome(unidade.toString());
			}
		}
	}

	public Boolean getMarcarTodasOrigensContaReceber() {
		if (marcarTodasOrigensContaReceber == null) {
			marcarTodasOrigensContaReceber = true;
		}
		return marcarTodasOrigensContaReceber;
	}

	public void setMarcarTodasOrigensContaReceber(Boolean marcarTodasOrigensContaReceber) {
		this.marcarTodasOrigensContaReceber = marcarTodasOrigensContaReceber;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoCondicaoRenegociacaoVO() {
		if (unidadeEnsinoCondicaoRenegociacaoVO == null) {
			unidadeEnsinoCondicaoRenegociacaoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoCondicaoRenegociacaoVO;
	}

	public void setUnidadeEnsinoCondicaoRenegociacaoVO(UnidadeEnsinoVO unidadeEnsinoCondicaoRenegociacaoVO) {
		this.unidadeEnsinoCondicaoRenegociacaoVO = unidadeEnsinoCondicaoRenegociacaoVO;
	}
	
	public void inicializarListasAposSelecionarUnidadeEnsino() throws Exception {
		montarListaSelectItemTurnoPorUnidadeEnsinoCondicaoRenegociacao(getCondicaoRenegociacaoVO().getListaCondicaoRenegociacaoUnidadeEnsinoVOs());
    }
	
	public CondicaoRenegociacaoFuncionarioVO getCondicaoRenegociacaoFuncionarioVO() {
		if (condicaoRenegociacaoFuncionarioVO == null) {
			condicaoRenegociacaoFuncionarioVO = new CondicaoRenegociacaoFuncionarioVO();
		}
		return condicaoRenegociacaoFuncionarioVO;
	}

	public void setCondicaoRenegociacaoFuncionarioVO(CondicaoRenegociacaoFuncionarioVO condicaoRenegociacaoFuncionarioVO) {
		this.condicaoRenegociacaoFuncionarioVO = condicaoRenegociacaoFuncionarioVO;
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

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}
	
	public void visualizarItemCondicaoRenegociacao() throws Exception {
        ItemCondicaoRenegociacaoVO obj = (ItemCondicaoRenegociacaoVO) context().getExternalContext().getRequestMap().get("itemCondicaoRenegociacaoItens");
        setItemCondicaoRenegociacaoVO(obj);
        setVisualizandoItemCondicaoRenegociacao(true);
    }

	public void cancelar() {
		setItemCondicaoRenegociacaoVO(new ItemCondicaoRenegociacaoVO());
		setVisualizandoItemCondicaoRenegociacao(Boolean.FALSE);
	}

	public Boolean getVisualizandoItemCondicaoRenegociacao() {
		if (visualizandoItemCondicaoRenegociacao == null) {
			visualizandoItemCondicaoRenegociacao = false;
		}
		return visualizandoItemCondicaoRenegociacao;
	}

	public void setVisualizandoItemCondicaoRenegociacao(Boolean visualizandoItemCondicaoRenegociacao) {
		this.visualizandoItemCondicaoRenegociacao = visualizandoItemCondicaoRenegociacao;
	}
	
	public String getCssCamposItemCondicaoRengociacao() {
		if (getVisualizandoItemCondicaoRenegociacao()) {
			return "camposSomenteLeitura";
		}
		return "campos";
	}
	
	public String getCssCamposObrigatoriosItemCondicaoRengociacao() {
		if (getVisualizandoItemCondicaoRenegociacao()) {
			return "camposSomenteLeituraObrigatorio";
		}
		return "camposObrigatorios";
	}

	public String getStatus() {
		if(status == null){
			status = "";
		}
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<SelectItem> getListaSelectItemStatus() {
		if(listaSelectItemStatus == null){
			listaSelectItemStatus = new ArrayList<SelectItem>(0);
			listaSelectItemStatus.add(new SelectItem("", ""));
			listaSelectItemStatus.add(new SelectItem("AT", "Ativo"));
			listaSelectItemStatus.add(new SelectItem("ATDP", "Ativo Dentro Prazo Vigência"));
			listaSelectItemStatus.add(new SelectItem("ATFP", "Ativo Fora Prazo Vigência"));
			listaSelectItemStatus.add(new SelectItem("ATPE", "Ativo Prazo Encerrado"));
			listaSelectItemStatus.add(new SelectItem("IN", "Inativo"));
		}
		return listaSelectItemStatus;
	}

	public void setListaSelectItemStatus(List<SelectItem> listaSelectItemStatus) {
		this.listaSelectItemStatus = listaSelectItemStatus;
	}
	
	public String getControleAba() {
		if (controleAba == null) {
			controleAba = "condicaoAtiva";
		}
		return controleAba;
	}

	public void setControleAba(String controleAba) {
		this.controleAba = controleAba;
	}
}