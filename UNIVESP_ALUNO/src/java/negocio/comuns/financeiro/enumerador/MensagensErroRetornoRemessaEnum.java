/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.financeiro.enumerador;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Philippe
 */
public enum MensagensErroRetornoRemessaEnum {

    OCORRENCIA_02(2, "02 - ENTRADA CONFIRMADA COM POSSIBILIDADE DE MENSAGEM (NOTA 20 - TABELA 10)"),
    OCORRENCIA_03(3, "03 - ENTRADA REJEITADA (NOTA 20 - TABELA 1)"),
    OCORRENCIA_04(4, "04 - ALTERAÇÃO DE DADOS - NOVA ENTRADA OU ALTERAÇÃO/EXCLUSÃO DE DADOS ACATADA"),
    OCORRENCIA_05(5, "05 - ALTERAÇÃO DE DADOS - BAIXA"),
    OCORRENCIA_06(6, "06 - LIQUIDAÇÃO NORMAL"),
    OCORRENCIA_07(7, "07 - LIQUIDAÇÃO PARCIAL - COBRANÇA INTELIGENTE (B2B)"),
    OCORRENCIA_08(8, "08 - LIQUIDAÇÃO EM CARTÓRIO"),
    OCORRENCIA_09(9, "09 - BAIXA SIMPLES"),
    OCORRENCIA_10(10, "10 - BAIXA POR TER SIDO LIQUIDADO"),
    OCORRENCIA_11(11, "11 - EM SER (SÓ NO RETORNO MENSAL)"),
    OCORRENCIA_12(12, "12 - ABATIMENTO CONCEDIDO"),
    OCORRENCIA_13(13, "13 - ABATIMENTO CANCELADO"),
    OCORRENCIA_14(14, "14 - VENCIMENTO ALTERADO"),
    OCORRENCIA_15(15, "15 - BAIXAS REJEITADAS (NOTA 20 - TABELA 4)"),
    OCORRENCIA_16(16, "16 - INSTRUÇÕES REJEITADAS (NOTA 20 - TABELA 3)"),
    OCORRENCIA_17(17, "17 - ALTERAÇÃO/EXCLUSÃO DE DADOS REJEITADOS (NOTA 20 - TABELA 2)"),
    OCORRENCIA_18(18, "18 - COBRANÇA CONTRATUAL - INSTRUÇÕES/ALTERAÇÕES REJEITADAS/PENDENTES (NOTA 20 - TABELA 5)"),
    OCORRENCIA_19(19, "19 - CONFIRMA RECEBIMENTO DE INSTRUÇÃO DE PROTESTO"),
    OCORRENCIA_20(20, "20 - CONFIRMA RECEBIMENTO DE INSTRUÇÃO DE SUSTAÇÃO DE PROTESTO /TARIFA"),
    OCORRENCIA_21(21, "21 - CONFIRMA RECEBIMENTO DE INSTRUÇÃO DE NÃO PROTESTAR"),
    OCORRENCIA_23(23, "23 - TÍTULO ENVIADO A CARTÓRIO/TARIFA"),
    OCORRENCIA_24(24, "24 - INSTRUÇÃO DE PROTESTO REJEITADA / SUSTADA / PENDENTE (NOTA 20 - TABELA 7)"),
    OCORRENCIA_25(25, "25 - ALEGAÇÕES DO SACADO (NOTA 20 - TABELA 6)"),
    OCORRENCIA_26(26, "26 - TARIFA DE AVISO DE COBRANÇA"),
    OCORRENCIA_27(27, "27 - TARIFA DE EXTRATO POSIÇÃO (B40X)"),
    OCORRENCIA_28(28, "28 - TARIFA DE RELAÇÃO DAS LIQUIDAÇÕES"),
    OCORRENCIA_29(29, "29 - TARIFA DE MANUTENÇÃO DE TÍTULOS VENCIDOS"),
    OCORRENCIA_30(30, "30 - DÉBITO MENSAL DE TARIFAS (PARA ENTRADAS E BAIXAS)"),
    OCORRENCIA_32(32, "32 - BAIXA POR TER SIDO PROTESTADO"),
    OCORRENCIA_33(33, "33 - CUSTAS DE PROTESTO"),
    OCORRENCIA_34(34, "34 - CUSTAS DE SUSTAÇÃO"),
    OCORRENCIA_35(35, "35 - CUSTAS DE CARTÓRIO DISTRIBUIDOR"),
    OCORRENCIA_36(36, "36 - CUSTAS DE EDITAL"),
    OCORRENCIA_37(37, "37 - TARIFA DE EMISSÃO DE BOLETO/TARIFA DE ENVIO DE DUPLICATA"),
    OCORRENCIA_38(38, "38 - TARIFA DE INSTRUÇÃO"),
    OCORRENCIA_39(39, "39 - TARIFA DE OCORRÊNCIAS"),
    OCORRENCIA_40(40, "40 - TARIFA MENSAL DE EMISSÃO DE BOLETO/TARIFA MENSAL DE ENVIO DE DUPLICATA"),
    OCORRENCIA_41(41, "41 - DÉBITO MENSAL DE TARIFAS - EXTRATO DE POSIÇÃO (B4EP/B4OX)"),
    OCORRENCIA_42(42, "42 - DÉBITO MENSAL DE TARIFAS - OUTRAS INSTRUÇÕES"),
    OCORRENCIA_43(43, "43 - DÉBITO MENSAL DE TARIFAS - MANUTENÇÃO DE TÍTULOS VENCIDOS"),
    OCORRENCIA_44(44, "44 - DÉBITO MENSAL DE TARIFAS - OUTRAS OCORRÊNCIAS"),
    OCORRENCIA_45(45, "45 - DÉBITO MENSAL DE TARIFAS - PROTESTO"),
    OCORRENCIA_46(46, "46 - DÉBITO MENSAL DE TARIFAS - SUSTAÇÃO DE PROTESTO"),
    OCORRENCIA_47(47, "47 - BAIXA COM TRANSFERÊNCIA PARA DESCONTO"),
    OCORRENCIA_48(48, "48 - CUSTAS DE SUSTAÇÃO JUDICIAL"),
    OCORRENCIA_51(51, "51 - TARIFA MENSAL REF A ENTRADAS BANCOS CORRESPONDENTES NA CARTEIRA"),
    OCORRENCIA_52(52, "52 - TARIFA MENSAL BAIXAS NA CARTEIRA"),
    OCORRENCIA_53(53, "53 - TARIFA MENSAL BAIXAS EM BANCOS CORRESPONDENTES NA CARTEIRA"),
    OCORRENCIA_54(54, "54 - TARIFA MENSAL DE LIQUIDAÇÕES NA CARTEIRA"),
    OCORRENCIA_55(55, "55 - TARIFA MENSAL DE LIQUIDAÇÕES EM BANCOS CORRESPONDENTES NA CARTEIRA"),
    OCORRENCIA_56(56, "56 - CUSTAS DE IRREGULARIDADE"),
    OCORRENCIA_57(57, "57 - INSTRUÇÃO CANCELADA (NOTA 20 - TABELA 8)"),
    OCORRENCIA_59(59, "59 - BAIXA POR CRÉDITO EM C/C ATRAVÉS DO SISPAG"),
    OCORRENCIA_60(60, "60 - ENTRADA REJEITADA CARNÊ (NOTA 20 - TABELA 1)"),
    OCORRENCIA_61(61, "61 - TARIFA EMISSÃO AVISO DE MOVIMENTAÇÃO DE TÍTULOS (2154)"),
    OCORRENCIA_62(62, "62 - DÉBITO MENSAL DE TARIFA - AVISO DE MOVIMENTAÇÃO DE TÍTULOS (2154)"),
    OCORRENCIA_63(63, "63 = TÍTULO SUSTADO JUDICIALMENTE"),
    OCORRENCIA_64(64, "64 - ENTRADA CONFIRMADA COM RATEIO DE CRÉDITO"),
    OCORRENCIA_69(69, "69 - CHEQUE DEVOLVIDO (NOTA 20 - TABELA 9)"),
    OCORRENCIA_71(71, "71 - ENTRADA REGISTRADA, AGUARDANDO AVALIAÇÃO"),
    OCORRENCIA_72(72, "72 - BAIXA POR CRÉDITO EM C/C ATRAVÉS DO SISPAG SEM TÍTULO CORRESPONDENTE"),
    OCORRENCIA_73(73, "73 - CONFIRMAÇÃO DE ENTRADA NA COBRANÇA SIMPLES - ENTRADA NÃO ACEITA NA COBRANÇA CONTRATUAL"),
    OCORRENCIA_76(76, "76 - CHEQUE COMPENSADO");
    private Integer codigo;
    private String mensagem;

    MensagensErroRetornoRemessaEnum(Integer codigo, String mensagem) {
        this.codigo = codigo;
        this.mensagem = mensagem;
    }

    public static List<String> getMensagem(String codigoRejeicao) {
        MensagensErroRetornoRemessaEnum[] valores = values();
        List<String> listaMensagens = new ArrayList(0);
        if (!codigoRejeicao.substring(0, 2).equals("00")) {
            for (MensagensErroRetornoRemessaEnum obj : valores) {
                if (obj.getCodigo() < 10) {
                    String stringComparacao = "0" + obj.getCodigo().toString();
                    if (stringComparacao.equals(codigoRejeicao.substring(0, 2))) {
                        listaMensagens.add(obj.getMensagem());
                    }
                } else {
                    if (obj.getCodigo().toString().equals(codigoRejeicao.substring(0, 2))) {
                        listaMensagens.add(obj.getMensagem());
                    }
                }
            }
        }
        if (!codigoRejeicao.substring(2, 4).equals("00")) {
            for (MensagensErroRetornoRemessaEnum obj : valores) {
                if (obj.getCodigo() < 10) {
                    String stringComparacao = "0" + obj.getCodigo().toString();
                    if (stringComparacao.equals(codigoRejeicao.substring(2, 4))) {
                        listaMensagens.add(obj.getMensagem());
                    }
                } else {
                    if (obj.getCodigo().toString().equals(codigoRejeicao.substring(2, 4))) {
                        listaMensagens.add(obj.getMensagem());
                    }
                }
            }
        }
        if (!codigoRejeicao.substring(4, 6).equals("00")) {
            for (MensagensErroRetornoRemessaEnum obj : valores) {
                if (obj.getCodigo() < 10) {
                    String stringComparacao = "0" + obj.getCodigo().toString();
                    if (stringComparacao.equals(codigoRejeicao.substring(4, 6))) {
                        listaMensagens.add(obj.getMensagem());
                    }
                } else {
                    if (obj.getCodigo().toString().equals(codigoRejeicao.substring(4, 6))) {
                        listaMensagens.add(obj.getMensagem());
                    }
                }
            }
        }
        if (!codigoRejeicao.substring(6).equals("00")) {
            for (MensagensErroRetornoRemessaEnum obj : valores) {
                if (obj.getCodigo() < 10) {
                    String stringComparacao = "0" + obj.getCodigo().toString();
                    if (stringComparacao.equals(codigoRejeicao.substring(6))) {
                        listaMensagens.add(obj.getMensagem());
                    }
                } else {
                    if (obj.getCodigo().toString().equals(codigoRejeicao.substring(6))) {
                        listaMensagens.add(obj.getMensagem());
                    }
                }
            }
        }

        return listaMensagens;
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
