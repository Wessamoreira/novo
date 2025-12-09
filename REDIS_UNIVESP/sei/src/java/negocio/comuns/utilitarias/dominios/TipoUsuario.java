package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum TipoUsuario {

	DIRETOR_MULTI_CAMPUS("DM", "Multi-Campus"),
	DIRETOR_CAMPUS("DC", "Diretor Campus"),
	COORDENADOR("CO", "Coordenador"),
	ALUNO("AL", "Aluno"),
	PROFESSOR("PR", "Professor"),
	FUNCIONARIO("FU", "Funcionário"),
	VISITANTE("VI", "Visitante"),
	OUVIDORIA("OU", "Ouvidoria"),
	PARCEIRO("PA", "Parceiro"),
	RESPONSAVEL_LEGAL("RL", "Responsável Legal"),
	CANDIDATO("CA", "Candidato");
    
    String valor;
    String descricao;

    TipoUsuario(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TipoUsuario getEnum(String valor) {
        TipoUsuario[] valores = values();
        for (TipoUsuario obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoUsuario obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
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
