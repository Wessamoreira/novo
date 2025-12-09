package negocio.comuns.academico.enumeradores;


public enum MomentoApresentacaoRecursoEducacionalEnum {
    ANTES, DEPOIS, APOIO_PROFESSOR;
    
    public boolean isAntes() {
    	return name().equals(MomentoApresentacaoRecursoEducacionalEnum.ANTES.name());
    }
    
    public boolean isDepois() {
    	return name().equals(MomentoApresentacaoRecursoEducacionalEnum.DEPOIS.name());
    }
    
    public boolean isApoioProfessor() {
    	return name().equals(MomentoApresentacaoRecursoEducacionalEnum.APOIO_PROFESSOR.name());
    }
    
}
