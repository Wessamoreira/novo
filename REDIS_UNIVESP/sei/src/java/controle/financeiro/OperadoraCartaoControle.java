package controle.financeiro;


import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.enumerador.OperadoraCartaoCreditoEnum;
import negocio.comuns.financeiro.enumerador.PermitirCartaoEnum;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoCartaoOperadoraCartaoEnum;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.facade.jdbc.financeiro.OperadoraCartao;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * operadoraCartaoForm.jsp operadoraCartaoCons.jsp) com as funcionalidades da classe <code>OperadoraCartao</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see OperadoraCartao
 * @see OperadoraCartaoVO
 */
@Controller("OperadoraCartaoControle")
@Scope("viewScope")
@Lazy
public class OperadoraCartaoControle extends SuperControle {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OperadoraCartaoVO operadoraCartaoVO;
	private List<SelectItem> listaSelectItemOperadorasCartaoCredito;
	private List<SelectItem> listaSelectItemTipoFinanciamento;

    public OperadoraCartaoControle() throws Exception {
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>OperadoraCartao</code>
     * para edição pelo usuário da aplicação.
     */
    public String novo() {         
    	removerObjetoMemoria(this);
        setOperadoraCartaoVO(new OperadoraCartaoVO());
        realizarAtualizacaoListaSelectItemOperadoraCartao();
        setMensagemID("msg_entre_dados", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("operadoraCartaoForm.xhtml");
    }	

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>OperadoraCartao</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() {
        try {
        	OperadoraCartaoVO obj = (OperadoraCartaoVO) context().getExternalContext().getRequestMap().get("operadoraCartaoItens");
        	obj.setNovoObj(Boolean.FALSE);
			setOperadoraCartaoVO(getFacadeFactory().getOperadoraCartaoFacade().consultarPorChavePrimariaUnica(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			realizarAtualizacaoListaSelectItemOperadoraCartao();
			setMensagemID("msg_dados_editar", Uteis.ALERTA);
		} catch (Exception e) {
			return "";
		}
        return Uteis.getCaminhoRedirecionamentoNavegacao("operadoraCartaoForm.xhtml");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>OperadoraCartao</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
     * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String persistir() {
        try {
            getFacadeFactory().getOperadoraCartaoFacade().persistir(getOperadoraCartaoVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } 
        return Uteis.getCaminhoRedirecionamentoNavegacao("operadoraCartaoForm.xhtml");
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP OperadoraCartaoCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            setListaConsulta(getFacadeFactory().getOperadoraCartaoFacade().consultar(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), false, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } 
        return "";
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>OperadoraCartaoVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getOperadoraCartaoFacade().excluir(operadoraCartaoVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            novo();
            setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } 
        return Uteis.getCaminhoRedirecionamentoNavegacao("operadoraCartaoForm.xhtml");
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {         
    	removerObjetoMemoria(this);
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("operadoraCartaoCons.xhtml");
    }

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean.
     * Garantindo uma melhor atuação do Garbage Coletor do Java. A mesma é automaticamente
     * quando realiza o logout.
     */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        operadoraCartaoVO = null;
    }

    public OperadoraCartaoVO getOperadoraCartaoVO() {
        if (operadoraCartaoVO == null) {
            operadoraCartaoVO = new OperadoraCartaoVO();
        }
        return operadoraCartaoVO;
    }

    public void setOperadoraCartaoVO(OperadoraCartaoVO operadoraCartaoVO) {
        this.operadoraCartaoVO = operadoraCartaoVO;
    }

    public boolean getIsConsultarPorTipo() {
        if (getControleConsulta().getCampoConsulta().equals("TIPO")) {
            return true;
        }
        return false;
    }

    public boolean getIsNaoConsultarPorTipo() {
        if (getControleConsulta().getCampoConsulta().equals("TIPO")) {
            return false;
        }
        return true;
    }

	public List<SelectItem> getListaSelectItemOperadorasCartaoCredito() {
		if(listaSelectItemOperadorasCartaoCredito == null) {
			listaSelectItemOperadorasCartaoCredito = new ArrayList<SelectItem>();
//			listaSelectItemOperadorasCartaoCredito = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(OperadoraCartaoCreditoEnum.class, "name", "valorApresentar", false);
		}
		return listaSelectItemOperadorasCartaoCredito;
	}

	public void setListaSelectItemOperadorasCartaoCredito(List<SelectItem> listaSelectItemOperadorasCartaoCredito) {
		this.listaSelectItemOperadorasCartaoCredito = listaSelectItemOperadorasCartaoCredito;
	}
	
	public void realizarAtualizacaoListaSelectItemOperadoraCartao() {
		try {
			boolean bandeiraPresentaNaLista = false;
			getListaSelectItemOperadorasCartaoCredito().clear();
			for (OperadoraCartaoCreditoEnum obj : OperadoraCartaoCreditoEnum.values()) {
				if (
						PermitirCartaoEnum.AMBOS.equals(obj.getTipo()) ||
						(PermitirCartaoEnum.DEBITO.equals(obj.getTipo()) && TipoCartaoOperadoraCartaoEnum.CARTAO_DEBITO.name().equals(getOperadoraCartaoVO().getTipo())) ||
						(PermitirCartaoEnum.CREDITO.equals(obj.getTipo()) && TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO.name().equals(getOperadoraCartaoVO().getTipo()))) {
					getListaSelectItemOperadorasCartaoCredito().add(new SelectItem(obj.name(), obj.getValorApresentar()));
					if (obj.equals(getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum())) {
						bandeiraPresentaNaLista = true;
					}
				}
			}
			if (!bandeiraPresentaNaLista) {
				if (TipoCartaoOperadoraCartaoEnum.CARTAO_DEBITO.name().equals(getOperadoraCartaoVO().getTipo())) {
					getOperadoraCartaoVO().setOperadoraCartaoCreditoEnum(OperadoraCartaoCreditoEnum.VISA_ELECTRON);
				} else if (TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO.name().equals(getOperadoraCartaoVO().getTipo())) {
					getOperadoraCartaoVO().setOperadoraCartaoCreditoEnum(OperadoraCartaoCreditoEnum.VISA);
				}
			}
		} catch (Exception e) {
			
		}
	}
	
	public List<SelectItem> getListaSelectItemFormaPagamento() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		lista.add(new SelectItem(0, ""));
		try {
			TipoFormaPagamento tipo = TipoFormaPagamento.CARTAO_DE_DEBITO;;
			if (TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO.name().equals(getOperadoraCartaoVO().getTipo())) {
				tipo = TipoFormaPagamento.CARTAO_DE_CREDITO;
			}
			for (FormaPagamentoVO forma : getFacadeFactory().getFormaPagamentoFacade()
					.consultarPorTipo(tipo.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado())) {
				lista.add(new SelectItem(forma.getCodigo(), forma.getNome()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}

	public List<SelectItem> getListaSelectItemTipoFinanciamento() {
		listaSelectItemTipoFinanciamento = TipoFinanciamentoEnum.getListaSelectItemTipoFinanciamentoOperadoraCartao();
		return listaSelectItemTipoFinanciamento;
	}

	public void setListaSelectItemTipoFinanciamento(List<SelectItem> listaSelectItemTipoFinanciamento) {
		this.listaSelectItemTipoFinanciamento = listaSelectItemTipoFinanciamento;
	}
}
