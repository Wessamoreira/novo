package negocio.comuns.utilitarias.dominios;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

/**
 *
 * @author Diego
 */
public enum SituacaoRequerimento {
	/*Atenção !!!
	 Existe um CHECK na tabela requerimento que valida se a situação é 
	 igual a uma das situações abaixo.Caso seja incluído  uma nova situação,será necessário alterar a condição no CHECK também.
	 Nome CHECK:ck_validarsituacao
	*/
    FINALIZADO_DEFERIDO("FD", "Finalizado - Deferido"),
    FINALIZADO_INDEFERIDO("FI", "Finalizado - Indeferido"),
    EM_EXECUCAO("EX", "Em Execução"),
    PENDENTE("PE", "Novo - Aguardando Início Execução"),
    AGUARDANDO_PAGAMENTO("AP", "Aguardando Pagamento"),
    ISENTO("IS", "Isento"),
    PRONTO_PARA_RETIRADA("PR", "Pronto para Retirada"),
    ATRASADO("AT", "Atrasado");
    
    String valor;
    String descricao;

    SituacaoRequerimento(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static SituacaoRequerimento getEnum(String valor) {
        SituacaoRequerimento[] valores = values();
        for (SituacaoRequerimento obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        SituacaoRequerimento obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }
    
    public static List<SelectItem> getListaSelectItemSituacaoRequerimentoVOs(Boolean obrigatorio) {
    	SituacaoRequerimento[] valores = values();
    	List<SelectItem> itens = new ArrayList<SelectItem>(0);
    	if (!obrigatorio) {
    		itens.add(new SelectItem("", ""));
    	}
        for (SituacaoRequerimento obj : valores) {
            itens.add(new SelectItem(obj, obj.getDescricao()));
        }
        return itens;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
