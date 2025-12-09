package relatorio.negocio.jdbc.financeiro;

import java.io.File;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.utilitarias.Extenso;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.financeiro.AutorizacaoPagamentoRelVO;
import relatorio.negocio.interfaces.financeiro.AutorizacaoPagamentoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy

public class AutorizacaoPagamentoRel extends SuperRelatorio implements AutorizacaoPagamentoRelInterfaceFacade{

	private static final long serialVersionUID = 1L;
	
	public AutorizacaoPagamentoRel() {
		
	}
	
	public AutorizacaoPagamentoRelVO criarObjeto(ContaPagarVO contaPagarVO, UsuarioVO usuarioVO) {
		AutorizacaoPagamentoRelVO obj = new AutorizacaoPagamentoRelVO();
		obj.setUnidadeEnsino(contaPagarVO.getUnidadeEnsino().getNome());
		obj.setDataVencimento(contaPagarVO.getDataVencimento_Apresentar());
		obj.setValor(contaPagarVO.getValor());
		obj.setJuro(contaPagarVO.getJuro());
		obj.setMulta(contaPagarVO.getMulta());
		obj.setDesconto(contaPagarVO.getDesconto());
		obj.setValorTotal(contaPagarVO.getPrevisaoValorPago());
		Extenso ext = new Extenso();
		ext.setNumber(contaPagarVO.getPrevisaoValorPago());
		obj.setValorPorExtenso(ext.toString().toUpperCase());
		obj.setFavorecido(realizarDefinicaoFavorecidoContaPagar(contaPagarVO));
		obj.setDataRegistro(contaPagarVO.getData_Apresentar());
		obj.setParcela(contaPagarVO.getParcela());
		obj.setNrDocumento(contaPagarVO.getNrDocumento());
		obj.setDescricao(contaPagarVO.getDescricao());
		obj.setResponsavelLancamento(contaPagarVO.getResponsavel().getNome());
		/*
		 * Devido a altercao da categoria de despesa agora ser uma lista para conta pagar entao a solucao provisoria e pegar a primeira da lista Pedro Andrade.;
		 */
		CategoriaDespesaVO cat  = contaPagarVO.getListaCentroResultadoOrigemVOs().isEmpty() ? null : contaPagarVO.getListaCentroResultadoOrigemVOs().get(0).getCategoriaDespesaVO();
		if(Uteis.isAtributoPreenchido(cat)){
			obj.setCategoriaDespesa(cat.getIdentificadorCategoriaDespesa()  + " - " + cat.getDescricao());	
		}
		return obj;
	}
	
	public String realizarDefinicaoFavorecidoContaPagar(ContaPagarVO contaPagarVO) {
		 if (contaPagarVO.getTipoSacado().equals("AL")) {
			return contaPagarVO.getPessoa().getNome(); 
		 }
		 if (contaPagarVO.getTipoSacado().equals("RF")) {
			 return contaPagarVO.getResponsavelFinanceiro().getNome(); 
		 }
		 if (contaPagarVO.getTipoSacado().equals("BA")) {
			 return contaPagarVO.getBanco().getNome(); 
		 }
		 if (contaPagarVO.getTipoSacado().equals("FO")) {
			 return contaPagarVO.getFornecedor().getNome(); 
		 }
		 if (contaPagarVO.getTipoSacado().equals("FU")) {
			 return contaPagarVO.getFuncionario().getPessoa().getNome(); 
		 }
		 if (contaPagarVO.getTipoSacado().equals("PA")) {
			 return contaPagarVO.getParceiro().getNome(); 
		 }
		 return "";
	}
	
	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
	}
	
	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public static String getIdEntidade() {
		return ("AutorizacaoPagamentoRel");
	}

}
