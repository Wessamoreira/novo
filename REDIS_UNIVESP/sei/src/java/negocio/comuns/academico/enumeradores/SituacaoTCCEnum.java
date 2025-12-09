package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoTCCEnum {

	NOVO, EM_ELABORACAO, APROVADO, REPROVADO, REPROVADO_FALTA, AGUARDANDO_APROVACAO_ORIENTADOR, AGUARDANDO_ARQUIVO, AGUARDANDO_AVALIACAO_BANCA;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_SituacaoTCCEnum_"+this.name());
	}
	
	public String getNome(){
		switch (this) {
		case NOVO:
			return "Novo";
		case AGUARDANDO_APROVACAO_ORIENTADOR:
			return "Aguardando Aprovação Avaliador";
		case AGUARDANDO_AVALIACAO_BANCA:
			return "Aguardando Avaliação Comissão";
		case AGUARDANDO_ARQUIVO:
			return "Aguardando Arquivo";
		case APROVADO:
			return "Aprovado";
		case EM_ELABORACAO:
			return "Em Elaboracao";
		case REPROVADO:
			return "Reprovado";
		case REPROVADO_FALTA:
			return "Reprovado por Falta";
		default:
			return "Novo";
		}				
	}
	
	public String getIconeApresentar(){
		return iconeApresentar("../../");				
	}
	
	public String getIconeVisaoApresentar(){
		return iconeApresentar("../");	
	}
	
	public String iconeApresentar(String base){
		switch (this) {
		case NOVO:
			return base+"resources/imagens/tcc/novo.png";
		case AGUARDANDO_APROVACAO_ORIENTADOR:
			return base+"resources/imagens/tcc/aguardandoAprovacaoOrientador.png";
		case AGUARDANDO_AVALIACAO_BANCA:
			return base+"resources/imagens/tcc/aguardandoAvaliacaoBanca.png";
		case AGUARDANDO_ARQUIVO:
			return base+"resources/imagens/tcc/aguardandoArquivo.png";
		case APROVADO:
			return base+"resources/imagens/tcc/aprovado.png";
		case EM_ELABORACAO:
			return base+"resources/imagens/tcc/emElaboracao.png";
		case REPROVADO:
			return base+"resources/imagens/tcc/reprovado.png";
		case REPROVADO_FALTA:
			return base+"resources/imagens/tcc/reprovadoFalta.png";
		default:
			return base+"resources/imagens/tcc/novo.png";
		}				
	}
}
