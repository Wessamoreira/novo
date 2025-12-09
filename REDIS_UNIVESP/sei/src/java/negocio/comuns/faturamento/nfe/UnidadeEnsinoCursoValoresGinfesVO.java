package negocio.comuns.faturamento.nfe;

import java.math.BigDecimal;

import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.SuperVO;

public class UnidadeEnsinoCursoValoresGinfesVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -934772365696603204L;
	private Integer codigo;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private Integer codigoCursoGinfes;
	private Integer anoIngresso;
	private Integer semestreIngresso;
	private Integer anoCompetenciaParcela;
	private Integer semestreCompetenciaParcela;
	private Integer numeroPeriodoLetivoCompetenciaParcela;
	private BigDecimal valorMensalidade;
	
	

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCursoVO == null) {
			unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoVO;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
	}

	public Integer getCodigoCursoGinfes() {
		if (codigoCursoGinfes == null) {
			codigoCursoGinfes = 0;
		}
		return codigoCursoGinfes;
	}

	public void setCodigoCursoGinfes(Integer codigoCursoGinfes) {
		this.codigoCursoGinfes = codigoCursoGinfes;
	}

	public Integer getAnoIngresso() {
		if (anoIngresso == null) {
			anoIngresso = 0;
		}
		return anoIngresso;
	}

	public void setAnoIngresso(Integer anoIngresso) {
		this.anoIngresso = anoIngresso;
	}

	public Integer getSemestreIngresso() {
		if (semestreIngresso == null) {
			semestreIngresso = 0;
		}
		return semestreIngresso;
	}

	public void setSemestreIngresso(Integer semestreIngresso) {
		this.semestreIngresso = semestreIngresso;
	}

	public Integer getAnoCompetenciaParcela() {
		if (anoCompetenciaParcela == null) {
			anoCompetenciaParcela = 0;
		}
		return anoCompetenciaParcela;
	}

	public void setAnoCompetenciaParcela(Integer anoCompetenciaParcela) {
		this.anoCompetenciaParcela = anoCompetenciaParcela;
	}

	public Integer getSemestreCompetenciaParcela() {
		if (semestreCompetenciaParcela == null) {
			semestreCompetenciaParcela = 0;
		}
		return semestreCompetenciaParcela;
	}

	public void setSemestreCompetenciaParcela(Integer semestreCompetenciaParcela) {
		this.semestreCompetenciaParcela = semestreCompetenciaParcela;
	}

	public Integer getNumeroPeriodoLetivoCompetenciaParcela() {
		if (numeroPeriodoLetivoCompetenciaParcela == null) {
			numeroPeriodoLetivoCompetenciaParcela = 0;
		}
		return numeroPeriodoLetivoCompetenciaParcela;
	}

	public void setNumeroPeriodoLetivoCompetenciaParcela(Integer numeroPeriodoLetivoCompetenciaParcela) {
		this.numeroPeriodoLetivoCompetenciaParcela = numeroPeriodoLetivoCompetenciaParcela;
	}

	public BigDecimal getValorMensalidade() {
		if (valorMensalidade == null) {
			valorMensalidade = BigDecimal.ZERO;
		}
		return valorMensalidade;
	}

	public void setValorMensalidade(BigDecimal valorMensalidade) {
		this.valorMensalidade = valorMensalidade;
	}
	
	public boolean equalsCampoSelecaoLista(UnidadeEnsinoCursoValoresGinfesVO obj) {
		return getCodigoCursoGinfes().equals(obj.getCodigoCursoGinfes())
				&& getAnoIngresso().equals(obj.getAnoIngresso())
				&& getSemestreIngresso().equals(obj.getSemestreIngresso())
				&& getAnoCompetenciaParcela().equals(obj.getAnoCompetenciaParcela())
				&& getSemestreCompetenciaParcela().equals(obj.getSemestreCompetenciaParcela())
				&& getNumeroPeriodoLetivoCompetenciaParcela().equals(obj.getNumeroPeriodoLetivoCompetenciaParcela());
	}
	
	 @Override
	    public String toString() {
	        return "UnidadeEnsinoCursoValoresGinfesVO [codigo=" + codigo + ", codigoCursoGinfes=" + getCodigoCursoGinfes() + ", AnoIngresso=" + getAnoIngresso() + ", SemestreIngresso=" + getSemestreIngresso() + ", AnoCompetenciaParcela=" + getAnoCompetenciaParcela() + ", SemestreCompetenciaParcela="+ getSemestreCompetenciaParcela() + ", NumeroPeriodoLetivoCompetenciaParcela="+ getNumeroPeriodoLetivoCompetenciaParcela() + "]";
	    }

}
