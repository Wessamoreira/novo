package negocio.comuns.academico;

import negocio.comuns.academico.enumeradores.AvaliarNaoAvaliarEnum;
import negocio.comuns.academico.enumeradores.OrigemCriterioAvaliacaoIndicadorEnum;
import negocio.comuns.arquitetura.SuperVO;

public class CriterioAvaliacaoIndicadorVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5122597612024318298L;
	
	private Integer codigo;
	private OrigemCriterioAvaliacaoIndicadorEnum origemCriterioAvaliacaoIndicador;
	private AvaliarNaoAvaliarEnum avaliarPrimeiroBimestre;
	private AvaliarNaoAvaliarEnum avaliarSegundoBimestre;
	private AvaliarNaoAvaliarEnum avaliarTerceiroBimestre;
	private AvaliarNaoAvaliarEnum avaliarQuartoBimestre;
	private CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivo;
	private CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicador;
	private String descricao;
	private Integer ordem;
	private Double nota1Bimestre;
	private Double nota2Bimestre;
	private Double nota3Bimestre;
	private Double nota4Bimestre;
	private String descricaoAnt;
	private Integer totalFalta1Bimestre;
	private Integer totalFalta2Bimestre;
	private Integer totalFalta3Bimestre;
	private Integer totalFalta4Bimestre;	
	private Integer totalFaltaGeral;	
	
	//Transiente
	private CriterioAvaliacaoAlunoVO criterioAvaliacaoAlunoVO;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public OrigemCriterioAvaliacaoIndicadorEnum getOrigemCriterioAvaliacaoIndicador() {
		if (origemCriterioAvaliacaoIndicador == null) {
			origemCriterioAvaliacaoIndicador = OrigemCriterioAvaliacaoIndicadorEnum.DISCIPLINA;
		}
		return origemCriterioAvaliacaoIndicador;
	}
	public void setOrigemCriterioAvaliacaoIndicador(OrigemCriterioAvaliacaoIndicadorEnum origemCriterioAvaliacaoIndicador) {
		this.origemCriterioAvaliacaoIndicador = origemCriterioAvaliacaoIndicador;
	}
	public AvaliarNaoAvaliarEnum getAvaliarPrimeiroBimestre() {
		if (avaliarPrimeiroBimestre == null) {
			avaliarPrimeiroBimestre = AvaliarNaoAvaliarEnum.AVALIAR;
		}
		return avaliarPrimeiroBimestre;
	}
	public void setAvaliarPrimeiroBimestre(AvaliarNaoAvaliarEnum avaliarPrimeiroBimestre) {
		this.avaliarPrimeiroBimestre = avaliarPrimeiroBimestre;
	}
	public AvaliarNaoAvaliarEnum getAvaliarSegundoBimestre() {
		if (avaliarSegundoBimestre == null) {
			avaliarSegundoBimestre = AvaliarNaoAvaliarEnum.AVALIAR;
		}
		return avaliarSegundoBimestre;
	}
	public void setAvaliarSegundoBimestre(AvaliarNaoAvaliarEnum avaliarSegundoBimestre) {
		this.avaliarSegundoBimestre = avaliarSegundoBimestre;
	}
	public AvaliarNaoAvaliarEnum getAvaliarTerceiroBimestre() {
		if (avaliarTerceiroBimestre == null) {
			avaliarTerceiroBimestre = AvaliarNaoAvaliarEnum.AVALIAR;
		}
		return avaliarTerceiroBimestre;
	}
	public void setAvaliarTerceiroBimestre(AvaliarNaoAvaliarEnum avaliarTerceiroBimestre) {
		this.avaliarTerceiroBimestre = avaliarTerceiroBimestre;
	}
	public AvaliarNaoAvaliarEnum getAvaliarQuartoBimestre() {
		if (avaliarQuartoBimestre == null) {
			avaliarQuartoBimestre = AvaliarNaoAvaliarEnum.AVALIAR;
		}
		return avaliarQuartoBimestre;
	}
	public void setAvaliarQuartoBimestre(AvaliarNaoAvaliarEnum avaliarQuartoBimestre) {
		this.avaliarQuartoBimestre = avaliarQuartoBimestre;
	}
	public CriterioAvaliacaoPeriodoLetivoVO getCriterioAvaliacaoPeriodoLetivo() {
		if (criterioAvaliacaoPeriodoLetivo == null) {
			criterioAvaliacaoPeriodoLetivo = new CriterioAvaliacaoPeriodoLetivoVO();
		}
		return criterioAvaliacaoPeriodoLetivo;
	}
	public void setCriterioAvaliacaoPeriodoLetivo(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivo) {
		this.criterioAvaliacaoPeriodoLetivo = criterioAvaliacaoPeriodoLetivo;
	}
	public Integer getOrdem() {
		if (ordem == null) {
			ordem = 0;
		}
		return ordem;
	}
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	public CriterioAvaliacaoDisciplinaEixoIndicadorVO getCriterioAvaliacaoDisciplinaEixoIndicador() {
		if (criterioAvaliacaoDisciplinaEixoIndicador == null) {
			criterioAvaliacaoDisciplinaEixoIndicador = new CriterioAvaliacaoDisciplinaEixoIndicadorVO();
		}
		return criterioAvaliacaoDisciplinaEixoIndicador;
	}
	public void setCriterioAvaliacaoDisciplinaEixoIndicador(CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicador) {
		this.criterioAvaliacaoDisciplinaEixoIndicador = criterioAvaliacaoDisciplinaEixoIndicador;
	}
	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Double getNota1Bimestre() {
		if (nota1Bimestre == null) {
			nota1Bimestre = 0.0;
		}
		return nota1Bimestre;
	}
	public void setNota1Bimestre(Double nota) {
		this.nota1Bimestre = nota;
	}
	
	public String getDescricaoAnt() {
		if (descricaoAnt == null) {
			descricaoAnt = "";
		}
		return descricaoAnt;
	}
	public void setDescricaoAnt(String descricaoAnt) {
		this.descricaoAnt = descricaoAnt;
	}
	public Double getNota2Bimestre() {
		if (nota2Bimestre == null) {
			nota2Bimestre =0.0;
		}
		return nota2Bimestre;
	}
	public void setNota2Bimestre(Double nota2Bimestre) {
		this.nota2Bimestre = nota2Bimestre;
	}
	public Double getNota3Bimestre() {
		if (nota3Bimestre == null) {
			nota3Bimestre = 0.0;
		}
		return nota3Bimestre;
	}
	public void setNota3Bimestre(Double nota3Bimestre) {
		this.nota3Bimestre = nota3Bimestre;
	}
	public Double getNota4Bimestre() {
		if (nota4Bimestre == null) {
			nota4Bimestre = 0.0;
		}
		return nota4Bimestre;
	}
	public void setNota4Bimestre(Double nota4Bimestre) {
		this.nota4Bimestre = nota4Bimestre;
	}
	
	public CriterioAvaliacaoAlunoVO getCriterioAvaliacaoAlunoVO() {
		if (criterioAvaliacaoAlunoVO == null) {
			criterioAvaliacaoAlunoVO = new CriterioAvaliacaoAlunoVO();
		}
		return criterioAvaliacaoAlunoVO;
	}
	
	public void setCriterioAvaliacaoAlunoVO(CriterioAvaliacaoAlunoVO criterioAvaliacaoAlunoVO) {
		this.criterioAvaliacaoAlunoVO = criterioAvaliacaoAlunoVO;
	}
	
	
	public Integer getTotalFalta1Bimestre() {
		if (totalFalta1Bimestre == null) {
			totalFalta1Bimestre = 0;
		}
		return totalFalta1Bimestre;
	}

	public void setTotalFalta1Bimestre(Integer totalFalta1Bimestre) {
		this.totalFalta1Bimestre = totalFalta1Bimestre;
	}

	public Integer getTotalFalta2Bimestre() {
		if (totalFalta2Bimestre == null) {
			totalFalta2Bimestre = 0;
		}
		return totalFalta2Bimestre;
	}

	public void setTotalFalta2Bimestre(Integer totalFalta2Bimestre) {
		this.totalFalta2Bimestre = totalFalta2Bimestre;
	}

	public Integer getTotalFalta3Bimestre() {
		if (totalFalta3Bimestre == null) {
			totalFalta3Bimestre = 0;
		}
		return totalFalta3Bimestre;
	}

	public void setTotalFalta3Bimestre(Integer totalFalta3Bimestre) {
		this.totalFalta3Bimestre = totalFalta3Bimestre;
	}

	public Integer getTotalFalta4Bimestre() {
		if (totalFalta4Bimestre == null) {
			totalFalta4Bimestre = 0;
		}
		return totalFalta4Bimestre;
	}

	public void setTotalFalta4Bimestre(Integer totalFalta4Bimestre) {
		this.totalFalta4Bimestre = totalFalta4Bimestre;
	}
	
	public Integer getTotalFaltaGeral() {
		if (totalFaltaGeral == null) {
			totalFaltaGeral = 0;
		}
		return totalFaltaGeral;
	}
	
	public void setTotalFaltaGeral(Integer totalFaltaGeral) {
		this.totalFaltaGeral = totalFaltaGeral;
	}	
	
	
}
