package negocio.comuns.academico;

import java.io.Serializable;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;

public class DisciplinaForaGradeVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
        /**
         * Vinculo com uma disciplina cadastrada no banco. Assim o aproveitamento
         * poderá ser lançado para qualquer disciplina cadastrada no sistema, gerando
         * um histórico que pode ser aproveitado, por exemplo, em um mapa de equivalencia
         */
        private DisciplinaVO disciplinaCadastrada;
	private String disciplina;
	private Double nota;
	private Double frequencia;
	private String situacao;
	private Boolean usarNotaConceito;
	private String notaConceito;
	private String ano;
	private String semestre;
	private Integer cargaHoraria;
	private Integer cargaHorariaCursada;
	private Integer numeroCredito;
	private String instituicaoEnsino;
	private CidadeVO cidade;
	private PeriodoLetivoVO periodoLetivo;
	private InclusaoDisciplinaForaGradeVO inclusaoDisciplinaForaGradeVO;
	
	/**
	 * Transiente usado na migracao de grade
	 */
	private HistoricoVO historicoVO;

	public DisciplinaForaGradeVO() {

	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDisciplina() {
		if (disciplina == null) {
			disciplina = "";
		}
		return disciplina;
	}

	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}

	public Double getFrequencia() {
		if (frequencia == null) {
			frequencia = 0.0;
		}
		return frequencia;
	}

	public void setFrequencia(Double frequencia) {
		this.frequencia = frequencia;
	}

	public Double getNota() {
		if (nota == null) {
			nota = 0.0;
		}
		return nota;
	}

	public void setNota(Double nota) {
		this.nota = nota;
	}

	public InclusaoDisciplinaForaGradeVO getInclusaoDisciplinaForaGradeVO() {
		if (inclusaoDisciplinaForaGradeVO == null) {
			inclusaoDisciplinaForaGradeVO = new InclusaoDisciplinaForaGradeVO();
		}
		return inclusaoDisciplinaForaGradeVO;
	}

	public void setInclusaoDisciplinaForaGradeVO(InclusaoDisciplinaForaGradeVO inclusaoDisciplinaForaGradeVO) {
		this.inclusaoDisciplinaForaGradeVO = inclusaoDisciplinaForaGradeVO;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = SituacaoHistorico.APROVADO.getValor();
		}
		return situacao;
	}

	public String getSituacao_Apresentar() {
		if (situacao == null) {
			situacao = "";
		}
		return SituacaoHistorico.getDescricao(situacao);
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public Integer getCargaHoraria() {
		if (cargaHoraria == null) {
			cargaHoraria = 0;
		}
		return cargaHoraria;
	}

	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}
	
	

	public String getInstituicaoEnsino() {
		if(instituicaoEnsino == null){
			instituicaoEnsino = "";
		}
		return instituicaoEnsino;
	}

	public void setInstituicaoEnsino(String instituicaoEnsino) {
		this.instituicaoEnsino = instituicaoEnsino;
	}

	public CidadeVO getCidade() {
		if(cidade == null){
			cidade = new CidadeVO();
		}
		return cidade;
	}

	public void setCidade(CidadeVO cidade) {
		this.cidade = cidade;
	}

	public PeriodoLetivoVO getPeriodoLetivo() {
		if(periodoLetivo == null){
			periodoLetivo = new PeriodoLetivoVO();
		}
		return periodoLetivo;
	}

	public void setPeriodoLetivo(PeriodoLetivoVO periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

	public HistoricoVO getHistoricoVO() {
		if(historicoVO == null){
			historicoVO = new HistoricoVO();
		}
		return historicoVO;
	}

	public void setHistoricoVO(HistoricoVO historicoVO) {
		this.historicoVO = historicoVO;
	}

	public Boolean getUsarNotaConceito() {
		if(usarNotaConceito==null){
			usarNotaConceito = false;
		}
		return usarNotaConceito;
	}

	public void setUsarNotaConceito(Boolean usarNotaConceito) {
		this.usarNotaConceito = usarNotaConceito;
	}

	public String getNotaConceito() {
		if(notaConceito == null){
			notaConceito = "";
		}
		return notaConceito;
	}

	public void setNotaConceito(String notaConceito) {
		this.notaConceito = notaConceito;
	}

	public Integer getCargaHorariaCursada() {
		if(cargaHorariaCursada == null){
			cargaHorariaCursada = 0;
		}
		return cargaHorariaCursada;
	}

	public void setCargaHorariaCursada(Integer cargaHorariaCursada) {
		this.cargaHorariaCursada = cargaHorariaCursada;
	}

    /**
     * @return the disciplinaCadastrada
     */
    public DisciplinaVO getDisciplinaCadastrada() {
        if (disciplinaCadastrada == null) {
            disciplinaCadastrada = new DisciplinaVO();
        }
        return disciplinaCadastrada;
    }

    /**
     * @param disciplinaCadastrada the disciplinaCadastrada to set
     */
    public void setDisciplinaCadastrada(DisciplinaVO disciplinaCadastrada) {
        this.disciplinaCadastrada = disciplinaCadastrada;
    }
	
    public Integer getNumeroCredito() {
		if (numeroCredito == null) {
			numeroCredito = 0;
		}
		return numeroCredito;
	}
	
    public void setNumeroCredito(Integer numeroCredito) {
		this.numeroCredito = numeroCredito;
	}
}
