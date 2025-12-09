/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.administrativo;

/**
 *
 * @author Otimize-Not
 */
import java.io.Serializable;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;

public class PainelGestorMonitoramentoDescontoNivelEducacionalVO implements Serializable {

	private String nivel;
	private String mesAno;
	private String curso;
	private Integer codigoCurso;
	private String turma;
	private Integer codigoTurma;
	private Double valorFaturado;
	private Double valorRecebido;
	private Double totalDesconto;
	private Double totalDescontoProgressivo;
	private Double totalDescontoAluno;
	private Double totalDescontoInstituicao;
	private Double totalDescontoConvenio;
	private Double totalDescontoRateio;
	private Double totalDescontoRecebimento;
	private Integer totalAlunoReceberamDesconto;
	private Integer totalAlunoUsouDescontoProgressivoUm;
	private Integer totalAlunoUsouDescontoProgressivoDois;
	private Integer totalAlunoUsouDescontoProgressivoTres;
	private Integer totalAlunoUsouDescontoProgressivoQuatro;
	public static final long serialVersionUID = 1L;

	public Integer getCodigoCurso() {
		if (codigoCurso == null) {
			codigoCurso = 0;
		}
		return codigoCurso;
	}

	public void setCodigoCurso(Integer codigoCurso) {
		this.codigoCurso = codigoCurso;
	}

	public String getMesAno() {
		if (mesAno == null) {
			mesAno = "";
		}
		return mesAno;
	}

	public void setMesAno(String mesAno) {
		this.mesAno = mesAno;
	}

	public String getCurso() {
		if (curso == null) {
			curso = "";
		}
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public Double getPercentualDescontoEfetivado() {
		if (getValorFaturado() != Double.NaN && getValorFaturado() > 0) {
			return Uteis.arrendondarForcando2CadasDecimais(getTotalDesconto() * 100 / getValorFaturado());
		}
		return 0.0;
	}

	public Double getValorFaturado() {
		if (valorFaturado == null) {
			valorFaturado = 0.0;
		}
		return valorFaturado;
	}

	public void setValorFaturado(Double valorFaturado) {
		this.valorFaturado = valorFaturado;
	}

	public Double getValorRecebido() {
		if (valorRecebido == null) {
			valorRecebido = 0.0;
		}
		return valorRecebido;
	}

	public void setValorRecebido(Double valorRecebido) {
		this.valorRecebido = valorRecebido;
	}

	public Integer getTotalAlunoUsouDescontoProgressivoDois() {
		if (totalAlunoUsouDescontoProgressivoDois == null) {
			totalAlunoUsouDescontoProgressivoDois = 0;
		}
		return totalAlunoUsouDescontoProgressivoDois;
	}

	public void setTotalAlunoUsouDescontoProgressivoDois(Integer totalAlunoUsouDescontoProgressivoDois) {
		this.totalAlunoUsouDescontoProgressivoDois = totalAlunoUsouDescontoProgressivoDois;
	}

	public Integer getTotalAlunoUsouDescontoProgressivoQuatro() {
		if (totalAlunoUsouDescontoProgressivoQuatro == null) {
			totalAlunoUsouDescontoProgressivoQuatro = 0;
		}
		return totalAlunoUsouDescontoProgressivoQuatro;
	}

	public void setTotalAlunoUsouDescontoProgressivoQuatro(Integer totalAlunoUsouDescontoProgressivoQuatro) {
		this.totalAlunoUsouDescontoProgressivoQuatro = totalAlunoUsouDescontoProgressivoQuatro;
	}

	public Integer getTotalAlunoUsouDescontoProgressivoTres() {
		if (totalAlunoUsouDescontoProgressivoTres == null) {
			totalAlunoUsouDescontoProgressivoTres = 0;
		}
		return totalAlunoUsouDescontoProgressivoTres;
	}

	public void setTotalAlunoUsouDescontoProgressivoTres(Integer totalAlunoUsouDescontoProgressivoTres) {
		this.totalAlunoUsouDescontoProgressivoTres = totalAlunoUsouDescontoProgressivoTres;
	}

	public Integer getTotalAlunoUsouDescontoProgressivoUm() {
		if (totalAlunoUsouDescontoProgressivoUm == null) {
			totalAlunoUsouDescontoProgressivoUm = 0;
		}
		return totalAlunoUsouDescontoProgressivoUm;
	}

	public void setTotalAlunoUsouDescontoProgressivoUm(Integer totalAlunoUsouDescontoProgressivoUm) {
		this.totalAlunoUsouDescontoProgressivoUm = totalAlunoUsouDescontoProgressivoUm;
	}

	public String getNivel() {
		if (nivel == null) {
			nivel = "";
		}
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public Integer getTotalAlunoReceberamDesconto() {
		if (totalAlunoReceberamDesconto == null) {
			totalAlunoReceberamDesconto = 0;
		}
		return totalAlunoReceberamDesconto;
	}

	public void setTotalAlunoReceberamDesconto(Integer totalAlunoReceberamDesconto) {
		this.totalAlunoReceberamDesconto = totalAlunoReceberamDesconto;
	}

	public Double getTotalDesconto() {
		if (totalDesconto == null) {
			totalDesconto = 0.0;
		}
		return totalDesconto;
	}

	public void setTotalDesconto(Double totalDesconto) {
		this.totalDesconto = totalDesconto;
	}

	public Double getTotalDescontoAluno() {
		if (totalDescontoAluno == null) {
			totalDescontoAluno = 0.0;
		}
		return totalDescontoAluno;
	}

	public void setTotalDescontoAluno(Double totalDescontoAluno) {
		this.totalDescontoAluno = totalDescontoAluno;
	}

	public Double getTotalDescontoConvenio() {
		if (totalDescontoConvenio == null) {
			totalDescontoConvenio = 0.0;
		}
		return totalDescontoConvenio;
	}

	public void setTotalDescontoConvenio(Double totalDescontoConvenio) {
		this.totalDescontoConvenio = totalDescontoConvenio;
	}

	public Double getTotalDescontoInstituicao() {
		if (totalDescontoInstituicao == null) {
			totalDescontoInstituicao = 0.0;
		}
		return totalDescontoInstituicao;
	}

	public void setTotalDescontoInstituicao(Double totalDescontoInstituicao) {
		this.totalDescontoInstituicao = totalDescontoInstituicao;
	}

	public Double getTotalDescontoProgressivo() {
		if (totalDescontoProgressivo == null) {
			totalDescontoProgressivo = 0.0;
		}
		return totalDescontoProgressivo;
	}

	public void setTotalDescontoProgressivo(Double totalDescontoProgressivo) {
		this.totalDescontoProgressivo = totalDescontoProgressivo;
	}

	public Double getTotalDescontoRecebimento() {
		if (totalDescontoRecebimento == null) {
			totalDescontoRecebimento = 0.0;
		}
		return totalDescontoRecebimento;
	}

	public void setTotalDescontoRecebimento(Double totalDescontoRecebimento) {
		this.totalDescontoRecebimento = totalDescontoRecebimento;
	}

	public String getNivelApresentar() {
		if (!getNivel().equals("")) {
			return TipoNivelEducacional.getEnum(getNivel()).getDescricao();
		}
		return "Outras Receitas/Despesas";
	}

	public String getTurma() {
		if (turma == null) {
			return "";
		}
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public Integer getCodigoTurma() {
		if (codigoTurma == null) {
			return 0;
		}
		return codigoTurma;
	}

	public void setCodigoTurma(Integer codigoTurma) {
		this.codigoTurma = codigoTurma;
	}

	public boolean getIsOutrasReceitasDespesas() {
		if (getNivel().equals("")) {
			return true;
		}
		return false;
	}
	

	public Double getTotalDescontoRateio() {
		if(totalDescontoRateio == null){
			totalDescontoRateio = 0.0;
		}
		return totalDescontoRateio;
	}

	public void setTotalDescontoRateio(Double totalDescontoRateio) {
		this.totalDescontoRateio = totalDescontoRateio;
	}
	
	
}
