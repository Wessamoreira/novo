package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas chqRLogForm.jsp chqRLogCons.jsp)
 * com as funcionalidades da classe <code>ChqRLog</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see ChqRLog
 * @see ChequeVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoCheque;
import negocio.comuns.utilitarias.dominios.SituacaoMovimentacaoFinanceiraEnum;

@Controller("ChequeControle")
@Scope("viewScope")
@Lazy
public class ChequeControle extends SuperControle implements Serializable {

    private ChequeVO chequeVO;
    private MovimentacaoFinanceiraVO movimentacaoFinanceiraVO;
    private Boolean situacaoMovimentacaoFinanceiraChequePendente;

    public ChequeControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>ChqRLog</code> para edição pelo usuário da
     * aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setChequeVO(new ChequeVO());
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("chequeForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>ChqRLog</code> para alteração. O
     * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
     * disponibilizá-lo para edição.
     */
    public String editar() {
        ChequeVO obj = (ChequeVO) context().getExternalContext().getRequestMap().get("chequeItens");
        obj.setNovoObj(Boolean.FALSE);
        setChequeVO(obj);
        montarDadosMovimentacaoFinanceiraItem();
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("chequeForm.xhtml");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>ChqRLog</code>. Caso o
     * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (chequeVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getChequeFacade().incluir(chequeVO, getUsuarioLogado());
            } else {
                getFacadeFactory().getChequeFacade().alterar(chequeVO);
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("chequeForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("chequeForm.xhtml");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ChqRLogCons.jsp. Define o tipo de consulta a ser
     * executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getChequeFacade().consultarPorCodigo(new Integer(valorInt), 0, getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
                if (getControleConsulta().getValorConsulta().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getChequeFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), 0, getUnidadeEnsinoLogado().getCodigo(), true,
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("sacado")) {
                if (getControleConsulta().getValorConsulta().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getChequeFacade().consultarPorSacado(getControleConsulta().getValorConsulta(), 0, getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeBanco")) {
                if (getControleConsulta().getValorConsulta().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getChequeFacade().consultarPorNomeBanco(getControleConsulta().getValorConsulta(), 0, getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("numeroAgenciaAgencia")) {
                if (getControleConsulta().getValorConsulta().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getChequeFacade().consultarPorNumeroAgenciaAgencia(getControleConsulta().getValorConsulta(), 0, getUnidadeEnsinoLogado().getCodigo(), true,
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("numero")) {
                if (getControleConsulta().getValorConsulta().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getChequeFacade().consultarPorNumero(getControleConsulta().getValorConsulta(), 0, getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("dataEmissao")) {
                objs = getFacadeFactory().getChequeFacade().consultarPorDataEmissao(Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0),
                        Uteis.getDateTime(getControleConsulta().getDataIni(), 23, 59, 59), 0, getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("dataPrevisao")) {
                objs = getFacadeFactory().getChequeFacade().consultarPorDataPrevisao(Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0),
                        Uteis.getDateTime(getControleConsulta().getDataIni(), 23, 59, 59), 0, getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("chequeCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("chequeCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>ChequeVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getChequeFacade().excluir(chequeVO);
            setChequeVO(new ChequeVO());
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("chequeForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("chequeForm.xhtml");
        }
    }

    public void irPaginaInicial() throws Exception {
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

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>situacao</code>
     */
    public List getListaSelectItemSituacaoChqRLog() throws Exception {
        List objs = new ArrayList(0);
        UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoCheque.class);
        objs.add(new SelectItem("", ""));
        Hashtable situacaoCheques = (Hashtable) Dominios.getSituacaoCheque();
        Enumeration keys = situacaoCheques.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) situacaoCheques.get(value);
            objs.add(new SelectItem(value, label));
        }
        situacaoCheques = null;
        keys = null;
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
    }

    public String getMascaraConsulta() {
        if (getControleConsulta().getCampoConsulta().equals("dataEmissao") || getControleConsulta().getCampoConsulta().equals("dataPrevisao")) {
            getControleConsulta().setValorConsulta("");
            return "return mascara(this.form,'form:valorConsulta','99/99/9999',event);";
        }
        return "";
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("sacado", "Emitente"));
        itens.add(new SelectItem("nomePessoa", "Sacado"));
        itens.add(new SelectItem("nomeBanco", "Banco"));
        itens.add(new SelectItem("numeroAgenciaAgencia", "Agência"));
        itens.add(new SelectItem("numero", "Número"));
        itens.add(new SelectItem("dataEmissao", "Data Emissão"));
        itens.add(new SelectItem("dataPrevisao", "Data Previsão"));
        return itens;
    }

    public boolean isCampoData() {
        return getControleConsulta().getCampoConsulta().equals("dataEmissao") || getControleConsulta().getCampoConsulta().equals("dataPrevisao");
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
        return Uteis.getCaminhoRedirecionamentoNavegacao("chequeCons.xhtml");
    }

    public ChequeVO getChequeVO() {
        return chequeVO;
    }

    public void setChequeVO(ChequeVO chequeVO) {
        this.chequeVO = chequeVO;
    }

    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        chequeVO = null;
    }
	
	public MovimentacaoFinanceiraVO getMovimentacaoFinanceiraVO() {
		if (movimentacaoFinanceiraVO == null) {
			movimentacaoFinanceiraVO = new MovimentacaoFinanceiraVO();
		}
		return movimentacaoFinanceiraVO;
	}

	public void setMovimentacaoFinanceiraVO(MovimentacaoFinanceiraVO movimentacaoFinanceiraVO) {
		this.movimentacaoFinanceiraVO = movimentacaoFinanceiraVO;
	}
	
	public Boolean getSituacaoMovimentacaoFinanceiraChequePendente() {
		if (situacaoMovimentacaoFinanceiraChequePendente == null) {
			situacaoMovimentacaoFinanceiraChequePendente = false;
		}
		return situacaoMovimentacaoFinanceiraChequePendente;
	}

	public void setSituacaoMovimentacaoFinanceiraChequePendente(Boolean situacaoMovimentacaoFinanceiraChequePendente) {
		this.situacaoMovimentacaoFinanceiraChequePendente = situacaoMovimentacaoFinanceiraChequePendente;
	}

	private void montarDadosMovimentacaoFinanceiraItem() {
		try {
			MovimentacaoFinanceiraVO movimentacaoFinanceiraVO = getFacadeFactory().getMovimentacaoFinanceiraFacade()
					.consultarPorCodigoCheque(getChequeVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(movimentacaoFinanceiraVO)) {
				setMovimentacaoFinanceiraVO(movimentacaoFinanceiraVO);
				setSituacaoMovimentacaoFinanceiraChequePendente(SituacaoMovimentacaoFinanceiraEnum.PENDENTE.getValor().equals(getMovimentacaoFinanceiraVO().getSituacao()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public boolean getApresentarDetalhesMovimentacaoFinanceira() {
		return Uteis.isAtributoPreenchido(getMovimentacaoFinanceiraVO());
	}
	
	public String getMensagemPendenciaMovimentacaoFinanceiraCheque() {
		return "Aguardando Baixa No Mapa de Pendência Financeira";
	}
}
