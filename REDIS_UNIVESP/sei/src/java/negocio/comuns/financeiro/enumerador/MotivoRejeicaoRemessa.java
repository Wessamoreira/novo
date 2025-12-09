package negocio.comuns.financeiro.enumerador;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import negocio.comuns.utilitarias.Uteis;

public enum MotivoRejeicaoRemessa {

	MOTIVO_00(0,"00 - Ocorrência Aceita"),
	MOTIVO_02(2,"02 - Código do registro detalhe inválido"),
	MOTIVO_03(3,"03 - Código da ocorrência inválida"),
	MOTIVO_04(4,"04 - Código de ocorrência não permitida para a carteira"),
	MOTIVO_05(5,"05 - Código de ocorrência não numérico"),
	MOTIVO_07(7,"07 - Agência/conta/Digito Inválido"),
	MOTIVO_08(8,"08 - Nosso número inválido"),
	MOTIVO_09(9,"09 - Nosso número duplicado"),
	MOTIVO_10(10,"10 - Carteira inválida"),
	MOTIVO_13(13,"13 - Identificação da emissão do bloqueto inválida"),
	MOTIVO_16(16,"16 - Data de vencimento inválida"),
	MOTIVO_18(18,"18 - Vencimento fora do prazo de operação"),
	MOTIVO_20(20,"20 - Valor do Título inválido"),
	MOTIVO_21(21,"21 - Espécie do Título inválida"),
	MOTIVO_22(22,"22 - Espécie não permitida para a carteira"),
	MOTIVO_23(23,"23 - Tipo Pagamento não contratado"),
	MOTIVO_24(24,"24 - Data de emissão inválida"),
	MOTIVO_27(27,"27 - Valor/Taxa de Juros Mora Invalido"),
	MOTIVO_28(28,"28 - Código do desconto inválido"),
	MOTIVO_29(29,"29 - Valor Desconto > ou = valor título"),
	MOTIVO_32(32,"32 - Valor do IOF Invalido"),
	MOTIVO_34(34,"34 - Valor do Abatimento Maior ou Igual ao Valor do Título"),
	MOTIVO_38(38,"38 - Prazo para protesto/ Negativação inválido"),
	MOTIVO_39(39,"39 - Pedido de Protesto/Negativação não Permitida para o Título"),
	MOTIVO_44(44,"44 - Código da Moeda Invalido"),
	MOTIVO_45(45,"45 - Nome do pagador não informado"),
	MOTIVO_46(46,"46 - Tipo/número de inscrição do pagador inválidos"),
	MOTIVO_47(47,"47 - Endereço do pagador não informado"),
	MOTIVO_48(48,"48 - CEP Inválido"),
	MOTIVO_49(49,"49 - CEP sem Praça de Cobrança"),
	MOTIVO_50(50,"50 - CEP irregular - Banco Correspondente"),
	MOTIVO_53(53,"53 - Tipo/Número de inscrição do Sacador Avalista Inválido"),
	MOTIVO_59(59,"59 - Valor/Percentual da Multa Inválido"),
	MOTIVO_63(63,"63 - Entrada para Título já cadastrado"),
	MOTIVO_65(65,"65 - Limite excedido"),
	MOTIVO_66(66,"66 - Número autorização inexistente"),
	MOTIVO_68(68,"68 - Débito não agendado - erro nos dados de remessa"),
	MOTIVO_69(69,"69 - Débito não agendado - Pagador não consta no cadastro de autorizante"),
	MOTIVO_70(70,"70 - Débito não agendado - Beneficiário não autorizado pelo Pagador"),
	MOTIVO_71(71,"71 - Débito não agendado - Beneficiário não participa do débito Automático"),
	MOTIVO_72(72,"72 - Débito não agendado - Código de moeda diferente de R$"),
	MOTIVO_73(73,"73 - Débito não agendado - Data de vencimento inválida"),
	MOTIVO_74(74,"74 - Débito não agendado - Conforme seu pedido, Título não registrado"),
	MOTIVO_75(75,"75 - Débito não agendado – Tipo de número de inscrição do debitado inválido"),
	MOTIVO_79(79,"79 - Data de Juros de Mora Invalida"),
	MOTIVO_80(80,"80 - Data do Desconto Invalida"),
	MOTIVO_86(86,"86 - Seu Número Invalido"),
	MOTIVO_NAO_ENCONTRADO(0000," - Motivo Não Encontrado");
	
    private Integer codigo;
    private String mensagem;

    MotivoRejeicaoRemessa(Integer codigo, String mensagem) {
        this.codigo = codigo;
        this.mensagem = mensagem;
    }

    public static String getMensagem(String codigoRejeicao) {
    	MotivoRejeicaoRemessa[] valores = values();
        String MotivoRejeicao = "";
        if (!codigoRejeicao.substring(0, 2).equals("00")) {
            for (MotivoRejeicaoRemessa obj : valores) {
                if (obj.getCodigo() < 10) {
                    String stringComparacao = "0" + obj.getCodigo().toString();
                    if (stringComparacao.equals(codigoRejeicao.substring(0, 2))) {
                    	MotivoRejeicao = MotivoRejeicao.concat(obj.getMensagem()).concat("\n");
                    }
                } else {
                    if (obj.getCodigo().toString().equals(codigoRejeicao.substring(0, 2))) {
                    	MotivoRejeicao = MotivoRejeicao.concat(obj.getMensagem()).concat("\n");
                    }
                }
            }
        }
        if (!codigoRejeicao.substring(2, 4).equals("00")) {
            for (MotivoRejeicaoRemessa obj : valores) {
                if (obj.getCodigo() < 10) {
                    String stringComparacao = "0" + obj.getCodigo().toString();
                    if (stringComparacao.equals(codigoRejeicao.substring(2, 4))) {
                        MotivoRejeicao = MotivoRejeicao.concat(obj.getMensagem()).concat("\n");
                    }
                } else {
                    if (obj.getCodigo().toString().equals(codigoRejeicao.substring(2, 4))) {
                        MotivoRejeicao = MotivoRejeicao.concat(obj.getMensagem()).concat("\n");
                    }
                }
            }
        }
        if (!codigoRejeicao.substring(4, 6).equals("00")) {
            for (MotivoRejeicaoRemessa obj : valores) {
                if (obj.getCodigo() < 10) {
                    String stringComparacao = "0" + obj.getCodigo().toString();
                    if (stringComparacao.equals(codigoRejeicao.substring(4, 6))) {
                        MotivoRejeicao = MotivoRejeicao.concat(obj.getMensagem()).concat("\n");
                    }
                } else {
                    if (obj.getCodigo().toString().equals(codigoRejeicao.substring(4, 6))) {
                        MotivoRejeicao = MotivoRejeicao.concat(obj.getMensagem()).concat("\n");
                    }
                }
            }
        }
        if (!codigoRejeicao.substring(6,8).equals("00")) {
            for (MotivoRejeicaoRemessa obj : valores) {
                if (obj.getCodigo() < 10) {
                    String stringComparacao = "0" + obj.getCodigo().toString();
                    if (stringComparacao.equals(codigoRejeicao.substring(6,8))) {
                        MotivoRejeicao = MotivoRejeicao.concat(obj.getMensagem()).concat("\n");
                    }
                } else {
                    if (obj.getCodigo().toString().equals(codigoRejeicao.substring(6,8))) {
                        MotivoRejeicao = MotivoRejeicao.concat(obj.getMensagem()).concat("\n");
                    }
                }
            }
        }
        if (!codigoRejeicao.substring(8,10).equals("00")) {
            for (MotivoRejeicaoRemessa obj : valores) {
                if (obj.getCodigo() < 10) {
                    String stringComparacao = "0" + obj.getCodigo().toString();
                    if (stringComparacao.equals(codigoRejeicao.substring(8,10))) {
                        MotivoRejeicao = MotivoRejeicao.concat(obj.getMensagem()).concat("\n");
                    }
                } else {
                    if (obj.getCodigo().toString().equals(codigoRejeicao.substring(8,10))) {
                        MotivoRejeicao = MotivoRejeicao.concat(obj.getMensagem()).concat("\n");
                    }
                }
            }
        }
        if (!Uteis.isAtributoPreenchido(MotivoRejeicao)) {
			return codigoRejeicao.concat(MotivoRejeicaoRemessa.MOTIVO_NAO_ENCONTRADO.getMensagem());
		}
        return MotivoRejeicao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
	
	
	

}
