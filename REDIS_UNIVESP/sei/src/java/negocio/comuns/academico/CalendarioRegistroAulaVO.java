package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;


public class CalendarioRegistroAulaVO extends SuperVO {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 3705593141862029952L;
	private Integer codigo;
    private String ano;    
    
    private UnidadeEnsinoVO unidadeEnsino;
    private UnidadeEnsinoCursoVO unidadeEnsinoCurso;
    private TurmaVO turma;
    private PessoaVO professor;
    private Date dataJaneiro;
    private Date dataFevereiro;
    private Date dataMarco;
    private Date dataAbril;
    private Date dataMaio;
    private Date dataJunho;
    private Date dataJulho;
    private Date dataAgosto;
    private Date dataSetembro;
    private Date dataOutubro;
    private Date dataNovembro;
    private Date dataDezembro;
    
    public String getAno() {
        if(ano == null){
            ano = Uteis.getAnoDataAtual();
        }
        return ano;
    }
    
    public void setAno(String ano) {
        this.ano = ano;
    }
    
    
    
   

    
    public Date getDataJaneiro() {
		return dataJaneiro;
	}

	public void setDataJaneiro(Date dataJaneiro) {
		this.dataJaneiro = dataJaneiro;
	}

	public Date getDataFevereiro() {
		return dataFevereiro;
	}

	public void setDataFevereiro(Date dataFevereiro) {
		this.dataFevereiro = dataFevereiro;
	}

	public Date getDataMarco() {
		return dataMarco;
	}

	public void setDataMarco(Date dataMarco) {
		this.dataMarco = dataMarco;
	}

	public Date getDataAbril() {
		return dataAbril;
	}

	public void setDataAbril(Date dataAbril) {
		this.dataAbril = dataAbril;
	}

	public Date getDataMaio() {
		return dataMaio;
	}

	public void setDataMaio(Date dataMaio) {
		this.dataMaio = dataMaio;
	}

	public Date getDataJunho() {
		return dataJunho;
	}

	public void setDataJunho(Date dataJunho) {
		this.dataJunho = dataJunho;
	}

	public Date getDataJulho() {
		return dataJulho;
	}

	public void setDataJulho(Date dataJulho) {
		this.dataJulho = dataJulho;
	}

	public Date getDataAgosto() {
		return dataAgosto;
	}

	public void setDataAgosto(Date dataAgosto) {
		this.dataAgosto = dataAgosto;
	}

	public Date getDataSetembro() {
		return dataSetembro;
	}

	public void setDataSetembro(Date dataSetembro) {
		this.dataSetembro = dataSetembro;
	}

	public Date getDataOutubro() {
		return dataOutubro;
	}

	public void setDataOutubro(Date dataOutubro) {
		this.dataOutubro = dataOutubro;
	}

	public Date getDataNovembro() {
		return dataNovembro;
	}

	public void setDataNovembro(Date dataNovembro) {
		this.dataNovembro = dataNovembro;
	}

	public Date getDataDezembro() {
		return dataDezembro;
	}

	public void setDataDezembro(Date dataDezembro) {
		this.dataDezembro = dataDezembro;
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

    
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if(unidadeEnsino == null){
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    
    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCurso() {
		if(unidadeEnsinoCurso == null){
			unidadeEnsinoCurso = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCurso;
	}

	public void setUnidadeEnsinoCurso(UnidadeEnsinoCursoVO unidadeEnsinoCurso) {
		this.unidadeEnsinoCurso = unidadeEnsinoCurso;
	}

	public TurmaVO getTurma() {
		if(turma == null){
			turma = new TurmaVO(); 
		}
		return turma;
	}

	public void setTurma(TurmaVO turma) {
		this.turma = turma;
	}

	public PessoaVO getProfessor() {
		if(professor == null){
			professor = new PessoaVO();
		}
		return professor;
	}

	public void setProfessor(PessoaVO professor) {
		this.professor = professor;
	}
    
    
    
    

}
