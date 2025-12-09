package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;


public class CondicaoPlanoFinanceiroCursoTurmaVO extends SuperVO {

    /**
     * 
     */
    private static final long serialVersionUID = 5974031253298774776L;
    private CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCurso;
    private DisciplinaVO disciplina;    
    private GradeDisciplinaVO gradeDisciplinaVO;
    private Double valor;
    private Integer codigo;
    
    public CondicaoPagamentoPlanoFinanceiroCursoVO getCondicaoPagamentoPlanoFinanceiroCurso() {
        if(condicaoPagamentoPlanoFinanceiroCurso == null){
            condicaoPagamentoPlanoFinanceiroCurso = new CondicaoPagamentoPlanoFinanceiroCursoVO();
        }
        return condicaoPagamentoPlanoFinanceiroCurso;
    }
    
    public void setCondicaoPagamentoPlanoFinanceiroCurso(CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCurso) {
        this.condicaoPagamentoPlanoFinanceiroCurso = condicaoPagamentoPlanoFinanceiroCurso;
    }
    
    public DisciplinaVO getDisciplina() {
        if(disciplina == null){
            disciplina = new DisciplinaVO();
        }
        return disciplina;
    }
    
    public void setDisciplina(DisciplinaVO disciplina) {
        this.disciplina = disciplina;
    }
    
    public Double getValor() {
        if(valor == null){
            valor = 0.0;
        }
        return valor;
    }
    
    public void setValor(Double valor) {
        this.valor = valor;
    }
    
    public Integer getCodigo() {
        if(codigo == null){
            codigo = 0;
        }
        return codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

	public GradeDisciplinaVO getGradeDisciplinaVO() {
		if (gradeDisciplinaVO == null) {
			gradeDisciplinaVO = new GradeDisciplinaVO();
		}
		return gradeDisciplinaVO;
	}

	public void setGradeDisciplinaVO(GradeDisciplinaVO gradeDisciplinaVO) {
		this.gradeDisciplinaVO = gradeDisciplinaVO;
	}
    
    
}
