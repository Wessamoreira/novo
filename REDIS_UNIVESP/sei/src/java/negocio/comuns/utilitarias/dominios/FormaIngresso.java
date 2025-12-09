package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum FormaIngresso {

	NENHUM(-1, "", "", false),
    ENTREVISTA(1, "ET", "Entrevista", true),
    PORTADOR_DE_DIPLOMA(2, "PD", "Portador(a) de Diploma", true),
    TRANSFERENCIA_INTERNA(3, "TI", "Transferência Interna", true),
    PROCESSO_SELETIVO(4, "PS", "Processo Seletivo", true),
    VESTIBULAR(4, "VE", "Vestibular", true),
    TRANSFERENCIA_EXTERNA(5, "TE", "Transferência Externa", true),
    REINGRESSO(6, "RE" , "Reingresso", true),
    PROUNI(7, "PR", "Prouni", true),
    ENEM(8, "EN", "Enem", true),
    DECISAO_JUDICIAL(9, "DJ", "Decisão Judicial", true),
    OUTROS_TIPOS_SELECAO(10, "OS", "Outros Tipos de Seleção", true),
	AVALIACAO_SERIADA(11, "AS", "Avaliação Seriada", true),
	SELECAO_SIMPLIFICADA(12, "SS", "Seleção Simplificada", true),
	PECG(13, "PE", "PEC-G", true),
	TRANSFERENCIA_EXTERNA_OFICIO(14, "TO", "Transferência Ex Officio", true),
	VAGAS_REMANESCENTES(15, "VR", "Vagas Remanescentes", true),
	VAGAS_PROGRAMAS_ESPECIAIS(16, "VP", "Vagas de Programas Especiais", true),
	VAGAS_PROGRAMAS_ESPECIAIS_FIES(16, "FI", "Vagas de Programas Especiais - FIES", true),
	REFUGIADOS(17, "RF", "Refugiado, Apátrida ou Portador de visto humanitário ", false),
	REINTEGRACAO_DE_CURSO(18, "RC", "Reintegração de Curso", false);
    int codigoCenso;
    String valor;
    String descricao;
    private Boolean apresentarNaMatricula;

    FormaIngresso(int codigoCenso, String valor, String descricao, Boolean apresentarNaMatricula) {
        this.valor = valor;
        this.descricao = descricao;
        this.codigoCenso = codigoCenso;
        this.apresentarNaMatricula = apresentarNaMatricula;
    }
	
	public static FormaIngresso getEnum(Integer codigo) {
        if (codigo == null) {
            codigo = 0;
        }
        FormaIngresso[] valores = values();
        for (FormaIngresso obj : valores) {
            if (obj.getCodigoCenso() == codigo.intValue()) {
                return obj;
            }
        }
        return null;
    }

    public static FormaIngresso getEnum(String valor) {
        if (valor == null) {
            valor = "";
        }
        FormaIngresso[] valores = values();
        for (FormaIngresso obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        FormaIngresso obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }
	
	public int getCodigoCenso() {
        return codigoCenso;
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

    public Boolean getApresentarNaMatricula() {
        return apresentarNaMatricula;
    }

    public void setApresentarNaMatricula(Boolean apresentarNaMatricula) {
        this.apresentarNaMatricula = apresentarNaMatricula;
    }
    
    
}
