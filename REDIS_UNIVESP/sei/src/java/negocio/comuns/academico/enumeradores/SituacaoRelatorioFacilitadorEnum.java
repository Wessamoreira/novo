package negocio.comuns.academico.enumeradores;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoRelatorioFacilitadorEnum {
	NENHUM("NH",""),
	ANALISE_SUPERVISOR("AS", "Em Análise Supervisor"),
	CORRECAO_ALUNO("CO", "Em Correção do Aluno"),
	DEFERIDO_SUPERVISOR("DE", "Deferido Supervisor"),
	INDEFERIDO_SUPERVISOR("IN", "Indeferido Supervisor"),
	SUSPENSAO_BOLSA("SB" , "Suspensão de Bolsa");
	
    String key;
    String descricao;

    private SituacaoRelatorioFacilitadorEnum(String key, String descricao) {
        this.key = key;
        this.descricao = descricao;
    }    
    

    public static SituacaoRelatorioFacilitadorEnum getEnum(String key) {
	    for (SituacaoRelatorioFacilitadorEnum situacaoRelatorioFacilitadorEnum : values()) {
	        if (situacaoRelatorioFacilitadorEnum.getKey().equals(key)) {
	            return situacaoRelatorioFacilitadorEnum;
	        }
	    }
	    return SituacaoRelatorioFacilitadorEnum.NENHUM;
    }
    
    public static SituacaoRelatorioFacilitadorEnum getName(String name) {
	    for (SituacaoRelatorioFacilitadorEnum situacaoRelatorioFacilitadorEnum : values()) {
	        if (situacaoRelatorioFacilitadorEnum.name().equals(name)) {
	            return situacaoRelatorioFacilitadorEnum;
	        }
	    }
	    return SituacaoRelatorioFacilitadorEnum.NENHUM;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public static List<SelectItem> getSituacaoRelatorioFacilitadorEnum() {
		return montarListaSelectItem(SituacaoRelatorioFacilitadorEnum.values());
	}

	private static List<SelectItem> montarListaSelectItem(SituacaoRelatorioFacilitadorEnum[] modulos) {
		List<SelectItem> tagList = new ArrayList<SelectItem>();
		tagList.add(new SelectItem("", ""));
		for (SituacaoRelatorioFacilitadorEnum tag : modulos) {
			tagList.add(new SelectItem(tag, tag.getDescricao()));
		}
		return tagList;
	}
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoEstagioEnum_" + this.name());
	}
}