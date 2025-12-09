package controle.processosel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.processosel.CartaoRespostaVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.controle.arquitetura.SuperControleRelatorio;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas segmentacaoProspectForm.jsp segmentacaoProspectCons.jsp).
 * 
 * @see SuperControle
 */


@Controller("CartaoRespostaControle")
@Scope("viewScope")
@Lazy
public class CartaoRespostaControle extends SuperControleRelatorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7902273893934117883L;
	private CartaoRespostaVO cartaoRespostaVO;
	private Boolean imprimirCartao1; 
	private Boolean imprimirCartao2; 
	private Integer linhaInicio; 
	private Integer colunaInicio;
	private List<SelectItem> listaSelectItemLinha;
	private String numeroTeste;
	private List<SelectItem> listaSelectItemColuna;

	public CartaoRespostaControle() {
		inicializarObjetoCartaoRespostaVO();
		setMensagemID("msg_cartao_inicio");
		setMensagemDetalhada("");
	}

	public String persistir() {
		try {

			getFacadeFactory().getCartaoRespostaFacade().alterar(getCartaoRespostaVO(), getUsuarioLogado());
			setCartaoRespostaVO(getFacadeFactory().getCartaoRespostaFacade().consultarCartaoResposta(false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID("msg_cartao_gravado");
		} catch (Exception e) {
			setMensagemID("");
			setMensagemDetalhada(e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("cartaoRespostaForm.xhtml");
	}

	
	public void inicializarObjetoCartaoRespostaVO() {
		try {
			setCartaoRespostaVO(getFacadeFactory().getCartaoRespostaFacade().consultarCartaoResposta(false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			if (getCartaoRespostaVO().getCodigo().equals(0)) {
				getFacadeFactory().getCartaoRespostaFacade().incluir(getCartaoRespostaVO(), false, getUsuarioLogado());
				setCartaoRespostaVO(getFacadeFactory().getCartaoRespostaFacade().consultarCartaoResposta(false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			} else {
				setCartaoRespostaVO(getFacadeFactory().getCartaoRespostaFacade().consultarCartaoResposta(false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemID("");
			setMensagemDetalhada(e.getMessage());
		}
	}

	public String getApresentarMensagemValidacaoCartaoResposta() {

		try {
			if (getFacadeFactory().getCartaoRespostaFacade().validaDados(getCartaoRespostaVO()) > 0) {
				return "alert('Os Campos em brancos serão atribuidos com os valores padrão !');";
			}
		} catch (Exception e) {

		}
		return null;

	}

	public CartaoRespostaVO getCartaoRespostaVO() {

		if (cartaoRespostaVO == null) {
			cartaoRespostaVO = new CartaoRespostaVO();
		}

		return cartaoRespostaVO;
	}

	public void setCartaoRespostaVO(CartaoRespostaVO cartaoRespostaVO) {
		this.cartaoRespostaVO = cartaoRespostaVO;
	}
	
	public void realizarGeracaoPrevisualizacaoCartaoResposta(){
		try{				
	        super.setCaminhoRelatorio(getFacadeFactory().getCartaoRespostaFacade().realizarImpressaoTesteCartaoRespostaLC3000(getCartaoRespostaVO(), getNumeroTeste(), getImprimirCartao1(), getImprimirCartao2(), getLinhaInicio(), getColunaInicio()));
	        super.setFazerDownload(true);	           
	        setMensagemID("msg_relatorio_ok");	        
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * @return the imprimirCartao1
	 */
	public Boolean getImprimirCartao1() {
		if (imprimirCartao1 == null) {
			imprimirCartao1 = true;
		}
		return imprimirCartao1;
	}

	/**
	 * @param imprimirCartao1 the imprimirCartao1 to set
	 */
	public void setImprimirCartao1(Boolean imprimirCartao1) {
		this.imprimirCartao1 = imprimirCartao1;
	}

	/**
	 * @return the imprimirCartao2
	 */
	public Boolean getImprimirCartao2() {
		if (imprimirCartao2 == null) {
			imprimirCartao2 = true;
		}
		return imprimirCartao2;
	}

	/**
	 * @param imprimirCartao2 the imprimirCartao2 to set
	 */
	public void setImprimirCartao2(Boolean imprimirCartao2) {
		this.imprimirCartao2 = imprimirCartao2;
	}

	/**
	 * @return the linhaInicio
	 */
	public Integer getLinhaInicio() {
		if (linhaInicio == null) {
			linhaInicio = 0;
		}
		return linhaInicio;
	}

	/**
	 * @param linhaInicio the linhaInicio to set
	 */
	public void setLinhaInicio(Integer linhaInicio) {
		this.linhaInicio = linhaInicio;
	}

	/**
	 * @return the colunaInicio
	 */
	public Integer getColunaInicio() {
		if (colunaInicio == null) {
			colunaInicio = 0;
		}
		return colunaInicio;
	}

	/**
	 * @param colunaInicio the colunaInicio to set
	 */
	public void setColunaInicio(Integer colunaInicio) {
		this.colunaInicio = colunaInicio;
	}

	

	/**
	 * @param listaSelectItemLinha the listaSelectItemLinha to set
	 */
	public void setListaSelectItemLinha(List<SelectItem> listaSelectItemLinha) {
		this.listaSelectItemLinha = listaSelectItemLinha;
	}

	/**
	 * @return the listaSelectItemColuna
	 */
	public List<SelectItem> getListaSelectItemColuna() {
		if (listaSelectItemColuna == null) {
			listaSelectItemColuna = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemColuna;
	}

	/**
	 * @param listaSelectItemColuna the listaSelectItemColuna to set
	 */
	public void setListaSelectItemColuna(List<SelectItem> listaSelectItemColuna) {
		this.listaSelectItemColuna = listaSelectItemColuna;
	}

	/**
	 * @return the numeroTeste
	 */
	public String getNumeroTeste() {
		if (numeroTeste == null) {
			numeroTeste = "";
		}
		return numeroTeste;
	}

	/**
	 * @param numeroTeste the numeroTeste to set
	 */
	public void setNumeroTeste(String numeroTeste) {
		this.numeroTeste = numeroTeste;
	}
	
	

}
