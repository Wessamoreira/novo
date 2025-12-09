package negocio.comuns.processosel.enumeradores;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

public enum EscopoPerguntaEnum {
	
	AVALIACAO_INSTITUCIONAL("AI", "Avaliação Institucional"),
	BANCO_CURRICULUM("BC", "Banco Curriculum"),  
	REQUERIMENTO("RE", "Requerimento"), 
	PROCESSO_SELETIVO("PS", "Processo Seletivo"),
	REQUISICAO("RQ", "Requisição"),
	ESTAGIO("ES", "Estágio"),
	PLANO_ENSINO("PE", "Plano Ensino"),
	RELATORIO_FACILITADOR("RF","Relatório Facilitador");
	
	private String valor;
	private String descricao;
	
	private EscopoPerguntaEnum(String valor , String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static EscopoPerguntaEnum getEnumCorrespondenteEscopoBase(String escopoBase){
		EscopoPerguntaEnum escopoPerguntaEnum = EscopoPerguntaEnum.getEscopoPerguntaEnum(escopoBase);
		if (Uteis.isAtributoPreenchido(escopoPerguntaEnum)) {
			switch (escopoPerguntaEnum) {
			case AVALIACAO_INSTITUCIONAL:
				return EscopoPerguntaEnum.AVALIACAO_INSTITUCIONAL;
			case REQUERIMENTO:
				return EscopoPerguntaEnum.REQUERIMENTO;
			case BANCO_CURRICULUM:
				return EscopoPerguntaEnum.BANCO_CURRICULUM;
			case REQUISICAO:
				return EscopoPerguntaEnum.REQUISICAO;
			case PLANO_ENSINO:
				return EscopoPerguntaEnum.PLANO_ENSINO;
			case ESTAGIO:
				return EscopoPerguntaEnum.ESTAGIO;
			case RELATORIO_FACILITADOR:
				return EscopoPerguntaEnum.RELATORIO_FACILITADOR;
			default:
				return EscopoPerguntaEnum.PROCESSO_SELETIVO;
			}
		}  else {
			return EscopoPerguntaEnum.PROCESSO_SELETIVO;
		}
	}
	
	public String getEscopoBase(){
		switch (this) {
		case AVALIACAO_INSTITUCIONAL:
			return "AI";
		case PLANO_ENSINO:
			return "PE";
		case BANCO_CURRICULUM:
			return "BC";
		case PROCESSO_SELETIVO:
			return "PS";
		case REQUERIMENTO:
			return "RE";
		case ESTAGIO:
			return "ES";
		case RELATORIO_FACILITADOR:
			return "RF";
		default:
			return "AI";
		}
		
	}
	
	
	public static EscopoPerguntaEnum getEscopoPerguntaCoincidenteEscopoQuestionario(TipoEscopoQuestionarioPerguntaEnum escopoQuestionario){
		if(escopoQuestionario != null){
			switch (escopoQuestionario) {
			case BANCO_CURRICULUM:
				return EscopoPerguntaEnum.BANCO_CURRICULUM;				
			case COORDENADOR:
				return EscopoPerguntaEnum.AVALIACAO_INSTITUCIONAL;				
			case DISCIPLINA:
				return EscopoPerguntaEnum.AVALIACAO_INSTITUCIONAL;				
			case GERAL:
				return EscopoPerguntaEnum.AVALIACAO_INSTITUCIONAL;				
			case FUNCIONARIO:
				return EscopoPerguntaEnum.AVALIACAO_INSTITUCIONAL;				
			case PROCESSO_SELETIVO:
				return EscopoPerguntaEnum.PROCESSO_SELETIVO;				
			case PROFESSOR:
				return EscopoPerguntaEnum.AVALIACAO_INSTITUCIONAL;				
			case REQUERIMENTO:
				return EscopoPerguntaEnum.REQUERIMENTO;				
			case ULTIMO_MODULO:
				return EscopoPerguntaEnum.AVALIACAO_INSTITUCIONAL;	
			case PLANO_ENSINO:
				return EscopoPerguntaEnum.PLANO_ENSINO;
			case ESTAGIO:
				return EscopoPerguntaEnum.ESTAGIO;
			case RELATORIO_FACILITADOR:
				return EscopoPerguntaEnum.RELATORIO_FACILITADOR;
			default:
				return null;
			}
		}
		return null;
		
	}
	
	public static EscopoPerguntaEnum getEscopoPerguntaEnum(String escopo) {
		if (!Uteis.isAtributoPreenchido(escopo)) {
			return null;
		}
		
		for (EscopoPerguntaEnum escopoEnum : values()) {
			if (escopoEnum.getValor().equals(escopo)) {
				return escopoEnum;
			}
		}
		return null;
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

	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_EscopoPerguntaEnum_"+this.name());				
	}
}
