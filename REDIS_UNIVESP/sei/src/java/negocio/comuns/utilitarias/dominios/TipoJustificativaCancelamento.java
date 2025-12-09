package negocio.comuns.utilitarias.dominios;

public enum TipoJustificativaCancelamento {

	BAIXA_QUALIDADE_ACADEMICA("BA", "Baixa Qualidade Acadêmica"), 
	INFRA_ESTRUTURA_FRACA("FI", "Infra-estrutura Fraca"), 
	FALTA_DE_TEMPO("FT", "Falta Tempo"), 
	DEFICIENCIA_ADMINISTRATIVA("DD", "Deficiência Administração"), 
	DESMOTIVACAO_CARREIRA("DC", "Desmotivação Carreira"), 
	DEFICIENCIA_ATENDIMENTO("DA", "Deficiência Atendimento"), 
	DIFICULDADE_FINANCEIRA("DF", "Dificuldade Financeira"), 
	INSATISFACAO_PROFESSORES("IP",	"Insatisfação Professores"),
	TRANSFERENCIA_INTERNA("TI", "Transferência Interna"), 
	NAO_JUSTIFICADO_ABANDONO("NJ", "Não Justificado - Abandono"), 
	INSATISFACAO_COM_CURSO("IC", "Insatisfação com o curso"), 
	INSATISFACAO_COM_INSTITUICAO("II", "Insatisfação com a instituição"), 
	MUDANCA_CIDADE("MC", "Mudança de cidade"), 
	DIFICULDADE_LOCOMOÇÃO("DL", "Dificuldade de locomoção"), 
	PROBLEMAS_COM_HORARIO("PH", "Problemas com horário"),
	CONQUISTA_BOLSA("CB", "Conquista de bolsa"), 
	MELHORA_FINANCEIRA("MF", "Melhora Financeira"), 
	NECESSIDADE_PROFISSIONAL("NP", "Necessidade Profissional"), 
	PROBLEMAS_FAMILIARES("PF", "Problemas Familiares"), 
	DESISTENCIA("DE", "Desistência"), 
	INDEFERIMENTO_POR_PENDENCIA_ACADEMICA("PA", "Indeferimento Por Pendência Acadêmica"),
	INDEFERIMENTO_POR_PENDENCIA_FINANCEIRA("PF", "Indeferimento Por Pendência Financeira"), 
	JUBILAMENTO("JU", "Jubilamento"),
	OUTROS("OU", "Outros");

	String valor;
	String descricao;

	TipoJustificativaCancelamento(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static TipoJustificativaCancelamento getEnum(String valor) {
		TipoJustificativaCancelamento[] valores = values();
		for (TipoJustificativaCancelamento obj : valores) {
			if (obj.getValor().equals(valor)) {
				return obj;
			}
		}
		return TipoJustificativaCancelamento.OUTROS;
	}

	public static String getDescricao(String valor) {
		TipoJustificativaCancelamento obj = getEnum(valor);
		if (obj != null) {
			return obj.getDescricao();
		}
		return TipoJustificativaCancelamento.OUTROS.getDescricao();
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
