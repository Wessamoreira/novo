package negocio.comuns.processosel.enumeradores;

import java.util.ArrayList;
import java.util.List;

import jakarta.faces.model.SelectItem;

public enum TipoEscopoQuestionarioPerguntaEnum {
	
	ALUNO_COORDENADOR("ALUNO_COORDENADOR", "Aluno Avaliando Coordenador de Curso"),
	GERAL("GE", "Geral"),
	DISCIPLINA("DI", "Aluno Avaliando Disciplina do Professor"),
    ULTIMO_MODULO("UM", "Último Módulo"),
    FUNCIONARIO("FG", "Funcionário/Gestor"),
    COORDENADOR("CO", "Coordenadores"),	
    PROFESSOR("PR", "Professores"),
    PROFESSORES_COORDENADORES("PROFESSORES_COORDENADORES", "Professores Avaliando Coordenador de Curso"),
    PROFESSOR_TURMA("PROFESSOR_TURMA", "Professor Avaliando Turma Com Aula Programada"),
    PROFESSOR_CURSO("PROFESSOR_CURSO", "Professor Avaliando Curso Com Aula Programada"),
    COORDENADORES_PROFESSOR("COORDENADORES_PROFESSOR", "Coordenador de Curso Avaliando Professores"),
    DEPARTAMENTO_COORDENADORES("DEPARTAMENTO_COORDENADORES", "Departamento Avaliando Coordenador de Curso"),
    COORDENADORES_DEPARTAMENTO("COORDENADORES_DEPARTAMENTO", "Coordenador de Curso Avaliando Departamento"),
    COORDENADORES_CURSO("COORDENADORES_CURSO", "Coordenador de Curso Avaliando Curso"),
    CARGO_COORDENADORES("CARGO_COORDENADORES", "Cargo Avaliando Coordenador de Curso"),        
    COORDENADORES_CARGO("COORDENADORES_CARGO", "Coordenador de Curso Avaliando Cargo"),
    DEPARTAMENTO_DEPARTAMENTO("DEPARTAMENTO_DEPARTAMENTO", "Departamento Avaliando Departamento"),
    DEPARTAMENTO_CARGO("DEPARTAMENTO_CARGO", "Departamento Avaliando Cargo"),
    CARGO_DEPARTAMENTO("CARGO_DEPARTAMENTO", "Cargo Avaliando Departamento"),
    CARGO_CARGO("CARGO_CARGO", "Cargo Avaliando Cargo"),
    BANCO_CURRICULUM("BC", "Banco Curriculum"),	
    PROCESSO_SELETIVO("PS", "Processo Seletivo"),
    REQUERIMENTO("RE", "Requerimento"),
    REQUISICAO("RQ", "Requisição"),
    ESTAGIO("ES", "Estágio"),
    PLANO_ENSINO("PE", "Plano Ensino"),
	RELATORIO_FACILITADOR("RF","Relatório Facilitador");
	
	private String key;
	private String value;
	
	
	private TipoEscopoQuestionarioPerguntaEnum(String key, String value) {
		this.key = key;
		this.value = value;
		
	}
	
	private static List<SelectItem> listaSelectItemAvaliacaoInstitucional;
	public static List<SelectItem> getListaSelectItemAvaliacaoInstitucional(){
		if(TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional == null){			
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional = new ArrayList<SelectItem>(0);
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.ALUNO_COORDENADOR.getKey(), TipoEscopoQuestionarioPerguntaEnum.ALUNO_COORDENADOR.getValue()));
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.DISCIPLINA.getKey(), TipoEscopoQuestionarioPerguntaEnum.DISCIPLINA.getValue()));
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.GERAL.getKey(), TipoEscopoQuestionarioPerguntaEnum.GERAL.getValue()));
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.ULTIMO_MODULO.getKey(), TipoEscopoQuestionarioPerguntaEnum.ULTIMO_MODULO.getValue()));
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.FUNCIONARIO.getKey(), TipoEscopoQuestionarioPerguntaEnum.FUNCIONARIO.getValue()));
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.COORDENADOR.getKey(), TipoEscopoQuestionarioPerguntaEnum.COORDENADOR.getValue()));
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.COORDENADORES_CARGO.getKey(), TipoEscopoQuestionarioPerguntaEnum.COORDENADORES_CARGO.getValue()));		
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.COORDENADORES_DEPARTAMENTO.getKey(), TipoEscopoQuestionarioPerguntaEnum.COORDENADORES_DEPARTAMENTO.getValue()));
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.COORDENADORES_PROFESSOR.getKey(), TipoEscopoQuestionarioPerguntaEnum.COORDENADORES_PROFESSOR.getValue()));		
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.COORDENADORES_CURSO.getKey(), TipoEscopoQuestionarioPerguntaEnum.COORDENADORES_CURSO.getValue()));		
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.CARGO_COORDENADORES.getKey(), TipoEscopoQuestionarioPerguntaEnum.CARGO_COORDENADORES.getValue()));
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.CARGO_CARGO.getKey(), TipoEscopoQuestionarioPerguntaEnum.CARGO_CARGO.getValue()));
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.CARGO_DEPARTAMENTO.getKey(), TipoEscopoQuestionarioPerguntaEnum.CARGO_DEPARTAMENTO.getValue()));				
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.DEPARTAMENTO_DEPARTAMENTO.getKey(), TipoEscopoQuestionarioPerguntaEnum.DEPARTAMENTO_DEPARTAMENTO.getValue()));
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.DEPARTAMENTO_COORDENADORES.getKey(), TipoEscopoQuestionarioPerguntaEnum.DEPARTAMENTO_COORDENADORES.getValue()));
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.DEPARTAMENTO_CARGO.getKey(), TipoEscopoQuestionarioPerguntaEnum.DEPARTAMENTO_CARGO.getValue()));
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.PROFESSOR.getKey(), TipoEscopoQuestionarioPerguntaEnum.PROFESSOR.getValue()));
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.PROFESSORES_COORDENADORES.getKey(), TipoEscopoQuestionarioPerguntaEnum.PROFESSORES_COORDENADORES.getValue()));
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.PROFESSOR_TURMA.getKey(), TipoEscopoQuestionarioPerguntaEnum.PROFESSOR_TURMA.getValue()));
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.PROFESSOR_CURSO.getKey(), TipoEscopoQuestionarioPerguntaEnum.PROFESSOR_CURSO.getValue()));
		}		
		return TipoEscopoQuestionarioPerguntaEnum.listaSelectItemAvaliacaoInstitucional;
		
	}
	
	private static List<SelectItem> listaSelectItemRequerimento;
	public static List<SelectItem> getListaSelectItemRequerimento(){
		if(TipoEscopoQuestionarioPerguntaEnum.listaSelectItemRequerimento == null){			
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemRequerimento = new ArrayList<SelectItem>(0);
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemRequerimento.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.REQUERIMENTO.getKey(), TipoEscopoQuestionarioPerguntaEnum.REQUERIMENTO.getValue()));			
		}		
		return TipoEscopoQuestionarioPerguntaEnum.listaSelectItemRequerimento;		
	}
		
	private static List<SelectItem> listaSelectItemProcessoSeletivo;
	public static List<SelectItem> getListaSelectItemProcessoSeletivo(){
		if(TipoEscopoQuestionarioPerguntaEnum.listaSelectItemProcessoSeletivo == null){			
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemProcessoSeletivo = new ArrayList<SelectItem>(0);
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemProcessoSeletivo.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.PROCESSO_SELETIVO.getKey(), TipoEscopoQuestionarioPerguntaEnum.PROCESSO_SELETIVO.getValue()));			
		}		
		return TipoEscopoQuestionarioPerguntaEnum.listaSelectItemProcessoSeletivo;		
	}
	
	
	private static List<SelectItem> listaSelectItemBancoCurriculum;
	public static List<SelectItem> getListaSelectItemBancoCurriculum(){
		if(TipoEscopoQuestionarioPerguntaEnum.listaSelectItemBancoCurriculum == null){			
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemBancoCurriculum = new ArrayList<SelectItem>(0);
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemBancoCurriculum.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.BANCO_CURRICULUM.getKey(), TipoEscopoQuestionarioPerguntaEnum.BANCO_CURRICULUM.getValue()));			
		}		
		return TipoEscopoQuestionarioPerguntaEnum.listaSelectItemBancoCurriculum;		
	}
	
	private static List<SelectItem> listaSelectItemPlanoEnsino;
	public static List<SelectItem> getListaSelectItemPlanoEnsino(){
		if(TipoEscopoQuestionarioPerguntaEnum.listaSelectItemPlanoEnsino == null){			
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemPlanoEnsino = new ArrayList<SelectItem>(0);
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemPlanoEnsino.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.PLANO_ENSINO.getKey(), TipoEscopoQuestionarioPerguntaEnum.PLANO_ENSINO.getValue()));			
		}		
		return TipoEscopoQuestionarioPerguntaEnum.listaSelectItemPlanoEnsino;		
	}
	
	private static List<SelectItem> listaSelectItemEstagio;
	public static List<SelectItem> getListaSelectItemEstagio(){
		if(TipoEscopoQuestionarioPerguntaEnum.listaSelectItemEstagio == null){			
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemEstagio = new ArrayList<SelectItem>(0);
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemEstagio.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.ESTAGIO.getKey(), TipoEscopoQuestionarioPerguntaEnum.ESTAGIO.getValue()));			
		}		
		return TipoEscopoQuestionarioPerguntaEnum.listaSelectItemEstagio;		
	}
	
	private static List<SelectItem> listaSelectItemRequisicao;
	public static List<SelectItem> getListaSelectItemRequisicao(){
		if(TipoEscopoQuestionarioPerguntaEnum.listaSelectItemRequisicao == null){			
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemRequisicao = new ArrayList<SelectItem>(0);
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemRequisicao.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.REQUISICAO.getKey(), TipoEscopoQuestionarioPerguntaEnum.REQUISICAO.getValue()));			
		}		
		return TipoEscopoQuestionarioPerguntaEnum.listaSelectItemRequisicao;		
	}
	
	private static List<SelectItem> listaSelectItemRelatorioFacilitaddor;
	public static List<SelectItem> getListaSelectItemRelatorioFacilitaddor(){
		if(TipoEscopoQuestionarioPerguntaEnum.listaSelectItemRelatorioFacilitaddor == null){			
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemRelatorioFacilitaddor = new ArrayList<SelectItem>(0);
			TipoEscopoQuestionarioPerguntaEnum.listaSelectItemRelatorioFacilitaddor.add(new SelectItem(TipoEscopoQuestionarioPerguntaEnum.RELATORIO_FACILITADOR.getKey(), TipoEscopoQuestionarioPerguntaEnum.RELATORIO_FACILITADOR.getValue()));			
		}		
		return TipoEscopoQuestionarioPerguntaEnum.listaSelectItemRelatorioFacilitaddor;		
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	
    
}
