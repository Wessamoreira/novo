package negocio.comuns.basico.enumeradores;

import java.util.Optional;


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
	
	public Optional<Enum<? extends TagTextoPadraoInterfaceEnum>[]> getTags() {
	    switch(this){
	        case LOCAL_ARMAZENAMENTO: 
	        case OCORRENCIA_PATRIMONIO: 
	        case PATRIMONIO: 
	        case PATRIMONIO_UNIDADE: 
	        case TAG_ADICIONAIS: 
	            
	            return Optional.empty();
	        default:
	            return Optional.empty();
	    }
	}

	
	
}
