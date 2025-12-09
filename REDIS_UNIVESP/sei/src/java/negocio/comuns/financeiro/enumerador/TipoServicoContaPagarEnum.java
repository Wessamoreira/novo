package negocio.comuns.financeiro.enumerador;

import java.util.List;

import static java.util.Arrays.asList;
import static negocio.comuns.utilitarias.Uteis.isAtributoPreenchido;

/**
 * 
 * @author PedroOtimize
 *
 */
public enum TipoServicoContaPagarEnum  {
	BLOQUETO_ELETRONICO(asList("033","237"),"03","Bloqueto Eletrônico"),
	PAGAMENTO_DIVIDENDO(asList("033", "104","237"),"10","Pagamento Dividendos"),
	PAGAMENTO_FORNECEDOR(asList("033", "104" ,"237"),"20", "Pagamento Fornecedor"),
	PAGAMENTO_CONTAS_TRIBUTOS_IMPOSTO(asList("033", "237"),"22", "Pagamento de Contas, Tributos e Impostos"),
	ALEGACAO_SACADO(asList("033" ,"237"),"29", "Alegação do Sacado"),
	PAGAMENTO_SINISTRO_SEGURADOS(asList("033", "104" ,"237"),"50", "Pagamento Sinistros Segurados"),
	PAGAMENTO_DESPESAS_VIAJANTE_TRANSITO(asList("033", "104" , "237"),"60", "Pagamento Despesas Viajante em Trânsito"),
	PAGAMENTO_AUTORIZADO(asList("033", "104", "237"),"70", "Pagamento Autorizado"),
	PAGAMENTO_CREDENCIADOS(asList("033", "104", "237"),"75", "Pagamento Credenciados"),
	PAGAMENTO_REPRESENTANTES_VENDEDORES_AUTORIZADOS(asList("033", "104", "237"),"80", "Pagamento Representantes / Vendedores Autorizados"),
	PAGAMENTO_BENDEFICIOS(asList("033", "104" ,"237"),"90", "Pagamento Benefícios"),
	PAGAMENTO_DIVERSOS(asList("033", "104","237"),"98", "Pagamentos Diversos"),	
	BRADESCO_FATURA(asList("237"),"02","Fatura"),
	BRADESCO_NOTAFISCAL(asList("237"),"03", "Nota Fiscal"),
	BRADESCO_DUPLICATA(asList("237"),"04", "Duplicata"),
	BRADESCO_OUTROS(asList("237"),"05", "Outros"),
	CAIXA_ECONOMICA_PAGAMENTO_SALARIO(asList("104"), "30", "Pagamento Salários"),
	OPTANTES(asList("104"), "00", "Optantes"),
	DEBITO_RECEBIMENTO(asList("104","237"), "05", "Débitos/Recebimento"),	
	ITAU_PAGAMENTO_DIVIDENDO(asList("341"),"10","Pagamento Dividendos"),	
    ITAU_PAGAMENTO_DEBeNTURES(asList("341"),"15", "Pagamento Debêntures"),
    ITAU_PAGAMENTO_FORNECEDOR(asList("341"),"20", "Pagamento Fornecedor"),
    ITAU_PAGAMENTO_CONTAS_TRIBUTOS_IMPOSTO(asList("341"),"22", "Pagamento de Contas, Tributos e Impostos"),
    ITAU_PAGAMENTO_SALARIOS(asList("341"),"30", "Pagamento de Salários"),
    ITAU_PAGAMENTO_FUNDOS_INVESTIMENTOS(asList("341"),"40", "Pagamento Fundos De Investimentos"),
    ITAU_PAGAMENTO_SINISTRO_SEGURADOS(asList("341"),"50", "Pagamento Sinistros Segurados"),
    ITAU_PAGAMENTO_DESPESAS_VIAJANTE_TRANSITO(asList("341"),"60", "Pagamento Despesas Viajante em Trânsito"),    
    ITAU_PAGAMENTO_REPRESENTANTES_VENDEDORES_AUTORIZADOS(asList("341"),"80", "Pagamento Representantes / Vendedores Autorizados"),
    ITAU_PAGAMENTO_BENEFICIOS(asList("341"),"90", "Pagamento Benefícios"),
    ITAU_PAGAMENTO_DIVERSOS(asList("341"),"98", "Pagamentos Diversos"),
    CONCILIACAO_BANCARIA(asList("237"),"04" ,"Conciliação Bancária"),
    CUSTODIA_CHEQUE(asList("237"),"06" ,"Custódia de Cheques"),
    GESTAO_CAIXA(asList("237"),"07" ,"Gestão de Caixa"),
    CONSULTA_INFORMACAO_MARGEM(asList("237"),"08" ,"Consulta/Informação Margem"),
    AVERBACAO_CONSIGINACAO_RETENCAO(asList("237"),"09","Averbação da Consignação/Retenção"),
    MANUTENCAO_CONSIGNACAO(asList("237"),"11","Manutenção da Consignação"),
    CONSIGNACAO_PARCELAS(asList("237"),"12","Consignação de Parcelas"),
    GLOSA_CONSIGNACAO(asList("237"),"13","Glosa da Consignação"),
    CONSULTA_TRIBUTOS(asList("237"),"14","Consulta de Tributos"),
    COMPROR(asList("237"),"25","Compror"),
    COMPROR_ROTATIVO(asList("237"),"26","Compror Rotativo"),
    PAGAMENTO_HONORAIOS(asList("237"),"32","Pagamento de honorários"),
    PAGAMENTO_BOLSA_AUXILIO(asList("237"),"33","Pagamento de bolsa e auxilio"),
    PAGAMENTO_PREBENDA(asList("237"),"34","Pagamento de prebenda"),
    VENDOR(asList("237"),"40","Vendor"),
    VENDOR_TERMO(asList("237"),"41","Vendor a Termo"),
    PAGAMENTO_REMUNERACAO(asList("237"),"77","Pagamento de Remuneração"),
    EXCLUSIVO_BRADESCO(asList("237"),"99","Exclusivo Bradesco");	
	
	private final List<String> nrBancos;
	private final String valor;
	private final String descricao;

	private TipoServicoContaPagarEnum(List<String> nrBancos, String valor, String descricao) {
		this.nrBancos = nrBancos;
		this.valor = valor;
		this.descricao = descricao;
	}

	public String getValor() {
		return this.valor;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public List<String> getNrBancos() {
		return this.nrBancos;
	}

	public boolean isTipoServicoPorNrBanco(String nrBanco) {
		return isAtributoPreenchido(nrBanco) ? this.getNrBancos().stream().anyMatch(nrBanco::equals) : false;
	}
}