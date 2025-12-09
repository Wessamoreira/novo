package relatorio.negocio.comuns.avaliacaoInst.enumeradores;

import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PublicoAlvoAvaliacaoInstitucional;

public enum NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum {
	AVALIADO(null), 
	PROFESSOR(new PublicoAlvoAvaliacaoInstitucional[]{ PublicoAlvoAvaliacaoInstitucional.TODOS_CURSOS, 
			PublicoAlvoAvaliacaoInstitucional.CURSO, PublicoAlvoAvaliacaoInstitucional.TURMA}),
	PROFESSOR_CURSO(new PublicoAlvoAvaliacaoInstitucional[]{ PublicoAlvoAvaliacaoInstitucional.TODOS_CURSOS, 
					PublicoAlvoAvaliacaoInstitucional.CURSO, PublicoAlvoAvaliacaoInstitucional.TURMA}),
	TURMA(new PublicoAlvoAvaliacaoInstitucional[]{ PublicoAlvoAvaliacaoInstitucional.ALUNO_COORDENADOR, PublicoAlvoAvaliacaoInstitucional.TODOS_CURSOS, 
			PublicoAlvoAvaliacaoInstitucional.CURSO, PublicoAlvoAvaliacaoInstitucional.TURMA}),
	CURSO(new PublicoAlvoAvaliacaoInstitucional[]{ PublicoAlvoAvaliacaoInstitucional.ALUNO_COORDENADOR, PublicoAlvoAvaliacaoInstitucional.TODOS_CURSOS, 
			PublicoAlvoAvaliacaoInstitucional.CURSO, PublicoAlvoAvaliacaoInstitucional.TURMA, PublicoAlvoAvaliacaoInstitucional.PROFESSOR_TURMA}),  	
	NOTA_AVALIADO(new PublicoAlvoAvaliacaoInstitucional[]{ }), 
	GERAL(new PublicoAlvoAvaliacaoInstitucional[]{ PublicoAlvoAvaliacaoInstitucional.TODOS_CURSOS, 
			PublicoAlvoAvaliacaoInstitucional.CURSO, PublicoAlvoAvaliacaoInstitucional.TURMA, PublicoAlvoAvaliacaoInstitucional.ALUNO_COORDENADOR,
			PublicoAlvoAvaliacaoInstitucional.PROFESSORES_COORDENADORES, PublicoAlvoAvaliacaoInstitucional.CARGO_CARGO, PublicoAlvoAvaliacaoInstitucional.CARGO_DEPARTAMENTO,
			PublicoAlvoAvaliacaoInstitucional.CARGO_COORDENADORES, PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_CARGO, PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_COORDENADORES,
			PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_DEPARTAMENTO, PublicoAlvoAvaliacaoInstitucional.COORDENADORES_CARGO,
			PublicoAlvoAvaliacaoInstitucional.COORDENADORES_DEPARTAMENTO, PublicoAlvoAvaliacaoInstitucional.COORDENADORES_PROFESSOR,
			PublicoAlvoAvaliacaoInstitucional.PROFESSOR_CURSO, PublicoAlvoAvaliacaoInstitucional.COORDENADORES_CURSO, PublicoAlvoAvaliacaoInstitucional.PROFESSOR_TURMA});
	
	
	private NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum(
			PublicoAlvoAvaliacaoInstitucional[] publicoAlvoPermitido) {
		this.publicoAlvoPermitido = publicoAlvoPermitido;
	}
	
	public String getDescricao(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO){		
		if(this.equals(AVALIADO)){
			if(avaliacaoInstitucionalVO != null){
				if(avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.ALUNO_COORDENADOR)
					|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.PROFESSORES_COORDENADORES)
					|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.CARGO_COORDENADORES)
					|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_COORDENADORES)){
					return "Coordenador Curso";
				}
				if(avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.CARGO_DEPARTAMENTO)
					|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_DEPARTAMENTO)
					|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_DEPARTAMENTO)){
					return "Departamento";
				}
				if(avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_CARGO)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_CARGO)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.CARGO_CARGO)){
						return "Cargo";
				}
				if((avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.CURSO)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.TODOS_CURSOS)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.TURMA))
						&& !avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo().equals("GE")){
						return "Disciplina/Professor";
				}
				if((avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.CURSO)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.TODOS_CURSOS)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.PROFESSOR_CURSO)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_CURSO)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.TURMA))
						&& avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo().equals("GE")){
						return "Curso";
				}
				if((avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.PROFESSORES)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.COLABORADORES_INSTITUICAO)
						|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.FUNCIONARIO_GESTOR))){
						return "Geral";
				}
			}
		}
		return UteisJSF.internacionalizar("enum_NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum_"+this.name());
	}

	private PublicoAlvoAvaliacaoInstitucional[] publicoAlvoPermitido;

	public PublicoAlvoAvaliacaoInstitucional[] getPublicoAlvoPermitido() {		
		return publicoAlvoPermitido;
	}

	public void setPublicoAlvoPermitido(PublicoAlvoAvaliacaoInstitucional[] publicoAlvoPermitido) {
		this.publicoAlvoPermitido = publicoAlvoPermitido;
	}
	
	public boolean getIsPermiteDatalhar(PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional, Boolean questionarioGeral){
		return this.getPublicoAlvoPermitido() == null 
	    || getPublicoAlvoAvaliacaoInstitucional(publicoAlvoAvaliacaoInstitucional, questionarioGeral) != null;
	}
	
	public PublicoAlvoAvaliacaoInstitucional getPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional, Boolean questionarioGeral){
		if(publicoAlvoAvaliacaoInstitucional != null){
		for(PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional2 : getPublicoAlvoPermitido()){			
			if(publicoAlvoAvaliacaoInstitucional2.equals(publicoAlvoAvaliacaoInstitucional)){
				if((publicoAlvoAvaliacaoInstitucional.equals(PublicoAlvoAvaliacaoInstitucional.CURSO)
					|| publicoAlvoAvaliacaoInstitucional.equals(PublicoAlvoAvaliacaoInstitucional.TODOS_CURSOS)
					|| publicoAlvoAvaliacaoInstitucional.equals(PublicoAlvoAvaliacaoInstitucional.TURMA))
					&& questionarioGeral && (this.equals(PROFESSOR) || this.equals(CURSO) || this.equals(PROFESSOR_CURSO)) ){
						return null;
					}
				return publicoAlvoAvaliacaoInstitucional;
			}
		}
		}
		return null;
	}
	
	
}
