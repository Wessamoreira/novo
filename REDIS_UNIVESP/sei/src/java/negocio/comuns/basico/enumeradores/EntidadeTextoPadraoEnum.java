package negocio.comuns.basico.enumeradores;

import negocio.comuns.patrimonio.enumeradores.TextoPadraoPatrimonioTagAdicionalEnum;
import negocio.comuns.patrimonio.enumeradores.TextoPadraoPatrimonioTagLocalArmazenamentoEnum;
import negocio.comuns.patrimonio.enumeradores.TextoPadraoPatrimonioTagOcorrenciaPatrimonioEnum;
import negocio.comuns.patrimonio.enumeradores.TextoPadraoPatrimonioTagPatrimonioEnum;
import negocio.comuns.patrimonio.enumeradores.TextoPadraoPatrimonioTagPatrimonioUnidadeEnum;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.interfaces.basico.TagTextoPadraoInterfaceEnum;

public enum EntidadeTextoPadraoEnum {

	PATRIMONIO("patrimonio"), 
	PATRIMONIO_UNIDADE("patrimoniounidade"),
	OCORRENCIA_PATRIMONIO("ocorrenciapatrimonio"),
	LOCAL_ARMAZENAMENTO("localarmazenamento"),
	TAG_ADICIONAIS("tagadicionais");
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_EntidadeTextoPadraoEnum_"+this.name());			
	}			
	
	private EntidadeTextoPadraoEnum(String tabela) {
		this.tabela = tabela;		
	}
	private String tabela; 

	public String getTabela() {		
		return tabela;
	}
	public void setTabela(String tabela) {
		this.tabela = tabela;
	}
	
	public Enum<? extends TagTextoPadraoInterfaceEnum>[] getTags() {
		Enum<? extends TagTextoPadraoInterfaceEnum>[] tags = null;
		switch(this){
			case LOCAL_ARMAZENAMENTO: tags = TextoPadraoPatrimonioTagLocalArmazenamentoEnum.values(); break;
			case OCORRENCIA_PATRIMONIO: tags = TextoPadraoPatrimonioTagOcorrenciaPatrimonioEnum.values(); break;
			case PATRIMONIO: tags = TextoPadraoPatrimonioTagPatrimonioEnum.values(); break;
			case PATRIMONIO_UNIDADE: tags = TextoPadraoPatrimonioTagPatrimonioUnidadeEnum.values(); break;
			case TAG_ADICIONAIS: tags = TextoPadraoPatrimonioTagAdicionalEnum.values(); break;
		}
		return tags;
	}

	
	
}
