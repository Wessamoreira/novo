package negocio.comuns.utilitarias.dominios;

import negocio.comuns.processosel.enumeradores.TipoEscopoQuestionarioPerguntaEnum;

public enum PublicoAlvoAvaliacaoInstitucional {

    TODOS_CURSOS("TC", "Todos Cursos(Aluno)", TipoEscopoQuestionarioPerguntaEnum.DISCIPLINA),
    CURSO("CU", "Curso(Aluno)", TipoEscopoQuestionarioPerguntaEnum.DISCIPLINA),
    TURMA("TU", "Turma(Aluno)", TipoEscopoQuestionarioPerguntaEnum.DISCIPLINA),
    PROFESSORES("PR", "Professores", TipoEscopoQuestionarioPerguntaEnum.PROFESSOR),    
    PROFESSORES_COORDENADORES("PROFESSORES_COORDENADORES", "Professores Avaliando Coordenador de Curso", TipoEscopoQuestionarioPerguntaEnum.PROFESSORES_COORDENADORES),
    COORDENADORES("CO", "Todos Coordenadores", TipoEscopoQuestionarioPerguntaEnum.COORDENADOR),
    COORDENADORES_PROFESSOR("COORDENADORES_PROFESSOR", "Coordenador de Curso Avaliando Professores", TipoEscopoQuestionarioPerguntaEnum.COORDENADORES_PROFESSOR),
    COORDENADORES_DEPARTAMENTO("COORDENADORES_DEPARTAMENTO", "Coordenador de Curso Avaliando Departamento", TipoEscopoQuestionarioPerguntaEnum.COORDENADORES_DEPARTAMENTO),           
    COORDENADORES_CARGO("COORDENADORES_CARGO", "Coordenador de Curso Avaliando Cargo", TipoEscopoQuestionarioPerguntaEnum.COORDENADORES_CARGO),
    COORDENADORES_CURSO("COORDENADORES_CURSO", "Coordenador de Curso Avaliando Curso", TipoEscopoQuestionarioPerguntaEnum.COORDENADORES_CURSO),
    DEPARTAMENTO_COORDENADORES("DEPARTAMENTO_COORDENADORES", "Departamento Avaliando Coordenador de Curso", TipoEscopoQuestionarioPerguntaEnum.DEPARTAMENTO_COORDENADORES),
    DEPARTAMENTO_DEPARTAMENTO("DEPARTAMENTO_DEPARTAMENTO", "Departamento Avaliando Departamento", TipoEscopoQuestionarioPerguntaEnum.DEPARTAMENTO_DEPARTAMENTO),
    DEPARTAMENTO_CARGO("DEPARTAMENTO_CARGO", "Departamento Avaliando Cargo", TipoEscopoQuestionarioPerguntaEnum.DEPARTAMENTO_CARGO),
    CARGO_DEPARTAMENTO("CARGO_DEPARTAMENTO", "Cargo Avaliando Departamento", TipoEscopoQuestionarioPerguntaEnum.CARGO_DEPARTAMENTO),
    CARGO_CARGO("CARGO_CARGO", "Cargo Avaliando Cargo", TipoEscopoQuestionarioPerguntaEnum.CARGO_CARGO),
    CARGO_COORDENADORES("CARGO_COORDENADORES", "Cargo Avaliando Coordenador de Curso", TipoEscopoQuestionarioPerguntaEnum.CARGO_COORDENADORES),
    FUNCIONARIO_GESTOR("FG", "Funcionário/Gestor", TipoEscopoQuestionarioPerguntaEnum.FUNCIONARIO),
    COLABORADORES_INSTITUICAO("COLABORADORES_INSTITUICAO", "Todos os Colaboradores Avaliando Instituição", TipoEscopoQuestionarioPerguntaEnum.GERAL),
    PROFESSOR_TURMA("PROFESSOR_TURMA", "Professor Avaliando Turma Com Aula Programada", TipoEscopoQuestionarioPerguntaEnum.PROFESSOR_TURMA),
    PROFESSOR_CURSO("PROFESSOR_CURSO", "Professor Avaliando Curso Com Aula Programada", TipoEscopoQuestionarioPerguntaEnum.PROFESSOR_CURSO),
    ALUNO_COORDENADOR("ALUNO_COORDENADOR", "Aluno Avaliando Coordenador do Curso", TipoEscopoQuestionarioPerguntaEnum.ALUNO_COORDENADOR);
    
    String valor;
    String descricao;
    TipoEscopoQuestionarioPerguntaEnum escopoQuestionario;

    PublicoAlvoAvaliacaoInstitucional(String valor, String descricao, TipoEscopoQuestionarioPerguntaEnum escopoQuestionario) {
        this.valor = valor;
        this.descricao = descricao;
        this.escopoQuestionario = escopoQuestionario;
    }

    public static PublicoAlvoAvaliacaoInstitucional getEnum(String valor) {
        PublicoAlvoAvaliacaoInstitucional[] valores = values();
        for (PublicoAlvoAvaliacaoInstitucional obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        PublicoAlvoAvaliacaoInstitucional obj = getEnum(valor);
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

	public TipoEscopoQuestionarioPerguntaEnum getEscopoQuestionario() {		
		return escopoQuestionario;
	}

	public void setEscopoQuestionario(TipoEscopoQuestionarioPerguntaEnum escopoQuestionario) {
		this.escopoQuestionario = escopoQuestionario;
	}
    
    
}
