package negocio.comuns.financeiro.enumerador;

import java.util.ArrayList;
import java.util.List;

public enum MensagensErroRetornoRemessaPagarEnum {

	
	OCORRENCIA_00("33", "00", "Crédito ou Débito Efetivado"),
    OCORRENCIA_01("33", "01", "Insuficiência de Fundos - Débito Não Efetuado"),
	OCORRENCIA_02("33", "02", "Crédito ou Débito Cancelado pelo Pagador/Credor"),
	OCORRENCIA_03("33", "03", "Débito Autorizado pela Agência - Efetuado"),
	OCORRENCIA_AA("33", "AA", "Controle Inválido"),
	OCORRENCIA_AB("33", "AB", "Tipo de Operação Inválido"),
	OCORRENCIA_AC("33", "AC", "Tipo de Serviço Inválido"),
	OCORRENCIA_AD("33", "AD", "Forma de Lançamento Inválida"),
	OCORRENCIA_AE("33", "AE", "Tipo/Número de Inscrição Inválido (gerado na crítica ou para informar rejeição)*"),
	OCORRENCIA_AF("33", "AF", "Código de Convênio Inválido"),
	OCORRENCIA_AG("33", "AG", "Agência/Conta Corrente/DV Inválido"),
	OCORRENCIA_AH("33", "AH", "Número Seqüencial do Registro no Lote Inválido"),
	OCORRENCIA_AI("33", "AI", "Código de Segmento de Detalhe Inválido"),
	OCORRENCIA_AJ("33", "AJ", "Tipo de Movimento Inválido"),
	OCORRENCIA_AK("33", "AK", "Código da Câmara de Compensação do Banco do Favorecido/Depositário Inválido"),
	OCORRENCIA_AL("33", "AL", "Código do Banco do Favorecido ou Depositário Inválido"),
	OCORRENCIA_AM("33", "AM", "Agência Mantenedora da Conta Corrente do Favorecido Inválida"),
	OCORRENCIA_AN("33", "AN", "Conta Corrente/DV do Favorecido Inválido"),
	OCORRENCIA_AO("33", "AO", "Nome do Favorecido não Informado"),
	OCORRENCIA_AP("33", "AP", "Data Lançamento Inválida/Vencimento Inválido"),
	OCORRENCIA_AQ("33", "AQ", "Tipo/Quantidade da Moeda Inválido"),
	OCORRENCIA_AR("33", "AR", "Valor do Lançamento Inválido"),
	OCORRENCIA_AS("33", "AS", "Aviso ao Favorecido - Identificação Inválida"),
	OCORRENCIA_AT("33", "AT", "Tipo/Número de Inscrição do Favorecido/Contribuinte Inválido"),
	OCORRENCIA_AU("33", "AU", "Logradouro do Favorecido não Informado"),
	OCORRENCIA_AV("33", "AV", "Número do Local do Favorecido não Informado"),
	OCORRENCIA_AW("33", "AW", "Cidade do Favorecido não Informada"),
	OCORRENCIA_AX("33", "AX", "CEP/Complemento do Favorecido Inválido"),
	OCORRENCIA_AY("33", "AY", "Sigla do Estado do Favorecido Inválido"),
	OCORRENCIA_AZ("33", "AZ", "Código/Nome do Banco Depositário Inválido"),
	OCORRENCIA_BA("33", "BA", "Código/Nome da Agência Depositário não Informado"),
	OCORRENCIA_BB("33", "BB", "Número do Documento Inválido(Seu Número)"),
	OCORRENCIA_BC("33", "BC", "Nosso Número Invalido"),
	OCORRENCIA_BD("33", "BD", "Inclusão Efetuada com Sucesso"),
	OCORRENCIA_BE("33", "BE", "Alteração Efetuada com Sucesso"),
	OCORRENCIA_BF("33", "BF", "Exclusão Efetuada com Sucesso"),
	OCORRENCIA_BG("33", "BG", "Agência/Conta Impedida Legalmente"),
	OCORRENCIA_B1("33", "B1", "Bloqueado Pendente de Autorização"),
	OCORRENCIA_B3("33", "B3", "Bloqueado pelo cliente"),
	OCORRENCIA_B4("33", "B4", "Bloqueado pela captura de titulo da cobrança"),
	OCORRENCIA_B8("33", "B8", "Bloqueado pela Validação de Tributos"),
	OCORRENCIA_CA("33", "CA", "Código de barras - Código do Banco Inválido"),
	OCORRENCIA_CB("33", "CB", "Código de barras - Código da Moeda Inválido"),
	OCORRENCIA_CC("33", "CC", "Código de barras - Dígito Verificador Geral Inválido"),
	OCORRENCIA_CD("33", "CD", "Código de barras - Valor do Título Inválido"),
	OCORRENCIA_CE("33", "CE", "Código de barras - Campo Livre Inválido"),
	OCORRENCIA_CF("33", "CF", "Valor do Documento/Principal/menor que o minimo Inválido"),
	OCORRENCIA_CH("33", "CH", "Valor do Desconto Inválido"),
	OCORRENCIA_CI("33", "CI", "Valor de Mora Inválido"),
	OCORRENCIA_CJ("33", "CJ", "Valor da Multa Inválido"),
	OCORRENCIA_CK("33", "CK", " Valor do IR Inválido"),
	OCORRENCIA_CL("33", "CL", " Valor do ISS Inválido"),
	OCORRENCIA_CM("33", "CM", " Valor do IOF Inválido"),
	OCORRENCIA_CN("33", "CN", " Valor de Outras Deduções Inválido"),
	OCORRENCIA_CO("33", "CO", " Valor de Outros Acréscimos Inválido"),
	OCORRENCIA_HA("33", "HA", " Lote Não Aceito"),
	OCORRENCIA_HB("33", "HB", " Inscrição da Empresa Inválida para o Contrato"),
	OCORRENCIA_HC("33", "HC", " Convênio com a Empresa Inexistente/Inválido para o Contrato"),
	OCORRENCIA_HD("33", "HD", " Agência/Conta Corrente da Empresa Inexistente/Inválida para o Contrato"),
	OCORRENCIA_HE("33", "HE", " Tipo de Serviço Inválido para o Contrato"),
	OCORRENCIA_HF("33", "HF", " Conta Corrente da Empresa com Saldo Insuficiente"),
	OCORRENCIA_HG("33", "HG", " Lote de Serviço fora de Seqüência"),
	OCORRENCIA_HH("33", "HH", " Lote de Serviço Inválido"),
	OCORRENCIA_HI("33", "HI", " Arquivo não aceito"),
	OCORRENCIA_HJ("33", "HJ", " Tipo de Registro Inválido"),
	OCORRENCIA_HL("33", "HL", " Versão de Layout Inválida"),
	OCORRENCIA_HU("33", "HU", " Hora de Envio Inválida"),
	OCORRENCIA_IJ("33", "IJ", " Competência ou Período de Referencia ou Numero da Parcela invalido"),
	OCORRENCIA_IM("33", "IM", " Município Invalido"),
	OCORRENCIA_IN("33", "IN", " Numero Declaração Invalido"),
	OCORRENCIA_IO("33", "IO", " Numero Etiqueta invalido"),
	OCORRENCIA_IP("33", "IP", " Numero Notificação invalido"),
	OCORRENCIA_IQ("33", "IQ", " Inscrição Estadual invalida"),
	OCORRENCIA_IR("33", "IR", " Divida Ativa Invalida"),
	OCORRENCIA_IS("33", "IS", " Valor Honorários ou Outros Acréscimos invalido"),
	OCORRENCIA_IT("33", "IT", " Período Apuração invalido"),
	OCORRENCIA_IU("33", "IU", " Valor ou Percentual da Receita invalido"),
	OCORRENCIA_IV("33", "IV", " Numero Referencia invalido"),
	OCORRENCIA_TA("33", "TA", " Lote não Aceito - Totais do Lote com Diferença"),
	OCORRENCIA_XB("33", "XB", " Número de Inscrição do Contribuinte Inválido"),
	OCORRENCIA_XC("33", "XC", " Código do Pagamento ou Competência ou Número de Inscrição Inválido"),
	OCORRENCIA_XF("33", "XF", " Código do Pagamento ou Competência não Numérico ou igual á zeros"),
	OCORRENCIA_YA("33", "YA", " Título não Encontrado"),
	OCORRENCIA_YB("33", "YB", " Identificação Registro Opcional Inválido"),
	OCORRENCIA_YC("33", "YC", " Código Padrão Inválido"),
	OCORRENCIA_YD("33", "YD", " Código de Ocorrência Inválido"),
	OCORRENCIA_YE("33", "YE", " Complemento de Ocorrência Inválido"),
	OCORRENCIA_YF("33", "YF", " Alegação já Informada"),
	OCORRENCIA_ZA("33", "ZA", " Transferencia Devolvida"),
	OCORRENCIA_ZB("33", "ZB", " Transferencia mesma titularidade não permitida"),
	OCORRENCIA_ZC("33", "ZC", " Código pagamento Tributo inválido"),
	OCORRENCIA_ZD("33", "ZD", " Competência Inválida"),
	OCORRENCIA_ZE("33", "ZE", " Valor outras entidades inválido"),
	OCORRENCIA_ZF("33", "ZF", " Sistema Origem Inválido"),
	OCORRENCIA_ZG("33", "ZG", " Banco Destino não recebe DOC"),
	OCORRENCIA_ZH("33", "ZH", " Banco Destino inoperante para DOC"),
	OCORRENCIA_ZI("33", "ZI", " Código do Histórico de Credito Invalido"),
	OCORRENCIA_ZK("33", "ZK", " Autorização iniciada no Internet Banking"),
	OCORRENCIA_Z0("33", "Z0", " Conta com bloqueio*"),
	OCORRENCIA_Z1("33", "Z1", " Conta fechada. É necessário ativar a conta*"),
	OCORRENCIA_Z2("33", "Z2", " Conta com movimento controlado*"),
	OCORRENCIA_Z3("33", "Z3", " Conta cancelada*"),
	OCORRENCIA_Z4("33", "Z4", " Registro inconsistente (Título)*"),
	OCORRENCIA_Z5("33", "Z5", " Apresentação indevida (Título)*"),
	OCORRENCIA_Z6("33", "Z6", " Dados do destinatário inválidos*"),
	OCORRENCIA_Z7("33", "Z7", " Agência ou conta destinatária do crédito inválida*"),
	OCORRENCIA_Z8("33", "Z8", " Divergência na titularidade*"),
	OCORRENCIA_Z9("33", "Z9", " Conta destinatária do crédito encerrada*");

	private String codigoRejeicao;
	private String nrBanco;
	private String mensagem;

	MensagensErroRetornoRemessaPagarEnum(String nrBanco, String codigoRejeicao, String mensagem) {
		this.codigoRejeicao = codigoRejeicao;
		this.nrBanco = nrBanco;
		this.mensagem = mensagem;
	}

	public static List<String> getMensagem(String codigoRejeicao, String nrBanco) {
		MensagensErroRetornoRemessaPagarEnum[] valores = values();
		List<String> listaMensagens = new ArrayList<String>(0);
		if(codigoRejeicao.length() >= 2){
			identificacaoCodigoRejeicao(codigoRejeicao.substring(0, 2), nrBanco, valores, listaMensagens);	
		}
		if(codigoRejeicao.length() >= 4){
			identificacaoCodigoRejeicao(codigoRejeicao.substring(2, 4), nrBanco, valores, listaMensagens);
		}
		if(codigoRejeicao.length() >= 6){
			identificacaoCodigoRejeicao(codigoRejeicao.substring(4, 6), nrBanco, valores, listaMensagens);
		}
		if(codigoRejeicao.length() >= 8){
			identificacaoCodigoRejeicao(codigoRejeicao.substring(6, 8), nrBanco, valores, listaMensagens);
		}
		if(codigoRejeicao.length() == 10){
			identificacaoCodigoRejeicao(codigoRejeicao.substring(8, 10), nrBanco, valores, listaMensagens);
		}
		return listaMensagens;
	}

	private static void identificacaoCodigoRejeicao(String codigoRejeicao, String nrBanco, MensagensErroRetornoRemessaPagarEnum[] valores, List<String> listaMensagens) {
		if (!codigoRejeicao.equals("00")) {
			for (MensagensErroRetornoRemessaPagarEnum obj : valores) {
				if (obj.getNrBanco().equals(nrBanco) && obj.getCodigoRejeicao().equals(codigoRejeicao)) {
					listaMensagens.add(obj.getMensagem());
				}
			}
		}
	}

	public String getCodigoRejeicao() {
		return codigoRejeicao;
	}

	public void setCodigoRejeicao(String codigoRejeicao) {
		this.codigoRejeicao = codigoRejeicao;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getNrBanco() {
		return nrBanco;
	}

	public void setNrBanco(String nrBanco) {
		this.nrBanco = nrBanco;
	}

}
