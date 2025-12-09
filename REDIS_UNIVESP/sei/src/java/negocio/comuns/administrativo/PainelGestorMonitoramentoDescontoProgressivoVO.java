/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.administrativo;

/**
 *
 * @author Rodrigo
 */
import java.io.Serializable;

public class PainelGestorMonitoramentoDescontoProgressivoVO implements Serializable {

    private String curso;
    private Integer codigoCurso;
    private String turma;
    private Integer codigoTurma;
    private String nivel;
    private Integer totalPessoasUsouDesconto;
    private Integer totalPessoasUsouPrimeiroDesconto;
    private Integer totalPessoasUsouSegundoDesconto;
    private Integer totalPessoasUsouTerceiroDesconto;
    private Integer totalPessoasUsouQuartoDesconto;
    private Double totalDesconto;
    private Double totalPrimeiroDesconto;
    private Double totalSegundoDesconto;
    private Double totalTerceiroDesconto;
    private Double totalQuartoDesconto;
    private Double percentualPrimeiroDescontoProgressivo;
    private Double percentualSegundoDescontoProgressivo;
    private Double percentualTerceiroDescontoProgressivo;
    private Double percentualQuartoDescontoProgressivo;
    private Double percentualTituloPrimeiroDescontoProgressivo;
    private Double percentualTituloSegundoDescontoProgressivo;
    private Double percentualTituloTerceiroDescontoProgressivo;
    private Double percentualTituloQuartoDescontoProgressivo;
    public static final long serialVersionUID = 1L;

    public Double getPercentualTituloPrimeiroDescontoProgressivo() {
        if (percentualTituloPrimeiroDescontoProgressivo == null) {
            percentualTituloPrimeiroDescontoProgressivo = 0.0;
        }
        return percentualTituloPrimeiroDescontoProgressivo;
    }

    public void setPercentualTituloPrimeiroDescontoProgressivo(Double percentualTituloPrimeiroDescontoProgressivo) {
        this.percentualTituloPrimeiroDescontoProgressivo = percentualTituloPrimeiroDescontoProgressivo;
    }

    public Double getPercentualTituloQuartoDescontoProgressivo() {
        if (percentualTituloQuartoDescontoProgressivo == null) {
            percentualTituloQuartoDescontoProgressivo = 0.0;
        }
        return percentualTituloQuartoDescontoProgressivo;
    }

    public void setPercentualTituloQuartoDescontoProgressivo(Double percentualTituloQuartoDescontoProgressivo) {
        this.percentualTituloQuartoDescontoProgressivo = percentualTituloQuartoDescontoProgressivo;
    }

    public Double getPercentualTituloSegundoDescontoProgressivo() {
        if (percentualTituloSegundoDescontoProgressivo == null) {
            percentualTituloSegundoDescontoProgressivo = 0.0;
        }
        return percentualTituloSegundoDescontoProgressivo;
    }

    public void setPercentualTituloSegundoDescontoProgressivo(Double percentualTituloSegundoDescontoProgressivo) {
        this.percentualTituloSegundoDescontoProgressivo = percentualTituloSegundoDescontoProgressivo;
    }

    public Double getPercentualTituloTerceiroDescontoProgressivo() {
        if (percentualTituloTerceiroDescontoProgressivo == null) {
            percentualTituloTerceiroDescontoProgressivo = 0.0;
        }
        return percentualTituloTerceiroDescontoProgressivo;
    }

    public void setPercentualTituloTerceiroDescontoProgressivo(Double percentualTituloTerceiroDescontoProgressivo) {
        this.percentualTituloTerceiroDescontoProgressivo = percentualTituloTerceiroDescontoProgressivo;
    }

    public Double getPercentualPrimeiroDescontoProgressivo() {
        if (percentualPrimeiroDescontoProgressivo == null) {
            percentualPrimeiroDescontoProgressivo = 0.0;
        }
        return percentualPrimeiroDescontoProgressivo;
    }

    public void setPercentualPrimeiroDescontoProgressivo(Double percentualPrimeiroDescontoProgressivo) {
        this.percentualPrimeiroDescontoProgressivo = percentualPrimeiroDescontoProgressivo;
    }

    public Double getPercentualQuartoDescontoProgressivo() {
        if (percentualQuartoDescontoProgressivo == null) {
            percentualQuartoDescontoProgressivo = 0.0;
        }
        return percentualQuartoDescontoProgressivo;
    }

    public void setPercentualQuartoDescontoProgressivo(Double percentualQuartoDescontoProgressivo) {
        this.percentualQuartoDescontoProgressivo = percentualQuartoDescontoProgressivo;
    }

    public Double getPercentualSegundoDescontoProgressivo() {
        if (percentualSegundoDescontoProgressivo == null) {
            percentualSegundoDescontoProgressivo = 0.0;
        }
        return percentualSegundoDescontoProgressivo;
    }

    public void setPercentualSegundoDescontoProgressivo(Double percentualSegundoDescontoProgressivo) {
        this.percentualSegundoDescontoProgressivo = percentualSegundoDescontoProgressivo;
    }

    public Double getPercentualTerceiroDescontoProgressivo() {
        if (percentualTerceiroDescontoProgressivo == null) {
            percentualTerceiroDescontoProgressivo = 0.0;
        }
        return percentualTerceiroDescontoProgressivo;
    }

    public void setPercentualTerceiroDescontoProgressivo(Double percentualTerceiroDescontoProgressivo) {
        this.percentualTerceiroDescontoProgressivo = percentualTerceiroDescontoProgressivo;
    }

    public Integer getCodigoCurso() {
        if (codigoCurso == null) {
            codigoCurso = 0;
        }
        return codigoCurso;
    }

    public void setCodigoCurso(Integer codigoCurso) {
        this.codigoCurso = codigoCurso;
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

	public String getCurso() {
        if (curso == null) {
            curso = "";
        }
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
    
    

    public String getTurma() {
    	 if (turma == null) {
    		 turma = "";
         }
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public Integer getCodigoTurma() {
		   if (codigoTurma == null) {
			   codigoTurma = 0;
	        }
		return codigoTurma;
	}

	public void setCodigoTurma(Integer codigoTurma) {
		this.codigoTurma = codigoTurma;
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

    public Integer getTotalPessoasUsouDesconto() {
        if (totalPessoasUsouDesconto == null) {
            totalPessoasUsouDesconto = 0;
        }
        return totalPessoasUsouDesconto;
    }

    public void setTotalPessoasUsouDesconto(Integer totalPessoasUsouDesconto) {
        this.totalPessoasUsouDesconto = totalPessoasUsouDesconto;
    }

    public Integer getTotalPessoasUsouPrimeiroDesconto() {
        if (totalPessoasUsouPrimeiroDesconto == null) {
            totalPessoasUsouPrimeiroDesconto = 0;
        }
        return totalPessoasUsouPrimeiroDesconto;
    }

    public void setTotalPessoasUsouPrimeiroDesconto(Integer totalPessoasUsouPrimeiroDesconto) {
        this.totalPessoasUsouPrimeiroDesconto = totalPessoasUsouPrimeiroDesconto;
    }

    public Integer getTotalPessoasUsouQuartoDesconto() {
        if (totalPessoasUsouQuartoDesconto == null) {
            totalPessoasUsouQuartoDesconto = 0;
        }
        return totalPessoasUsouQuartoDesconto;
    }

    public void setTotalPessoasUsouQuartoDesconto(Integer totalPessoasUsouQuartoDesconto) {
        this.totalPessoasUsouQuartoDesconto = totalPessoasUsouQuartoDesconto;
    }

    public Integer getTotalPessoasUsouSegundoDesconto() {
        if (totalPessoasUsouSegundoDesconto == null) {
            totalPessoasUsouSegundoDesconto = 0;
        }
        return totalPessoasUsouSegundoDesconto;
    }

    public void setTotalPessoasUsouSegundoDesconto(Integer totalPessoasUsouSegundoDesconto) {
        this.totalPessoasUsouSegundoDesconto = totalPessoasUsouSegundoDesconto;
    }

    public Integer getTotalPessoasUsouTerceiroDesconto() {
        if (totalPessoasUsouTerceiroDesconto == null) {
            totalPessoasUsouTerceiroDesconto = 0;
        }
        return totalPessoasUsouTerceiroDesconto;
    }

    public void setTotalPessoasUsouTerceiroDesconto(Integer totalPessoasUsouTerceiroDesconto) {
        this.totalPessoasUsouTerceiroDesconto = totalPessoasUsouTerceiroDesconto;
    }

    public Double getTotalPrimeiroDesconto() {
        if (totalPrimeiroDesconto == null) {
            totalPrimeiroDesconto = 0.0;
        }
        return totalPrimeiroDesconto;
    }

    public void setTotalPrimeiroDesconto(Double totalPrimeiroDesconto) {
        this.totalPrimeiroDesconto = totalPrimeiroDesconto;
    }

    public Double getTotalQuartoDesconto() {
        if (totalQuartoDesconto == null) {
            totalQuartoDesconto = 0.0;
        }
        return totalQuartoDesconto;
    }

    public void setTotalQuartoDesconto(Double totalQuartoDesconto) {
        this.totalQuartoDesconto = totalQuartoDesconto;
    }

    public Double getTotalSegundoDesconto() {
        if (totalSegundoDesconto == null) {
            totalSegundoDesconto = 0.0;
        }
        return totalSegundoDesconto;
    }

    public void setTotalSegundoDesconto(Double totalSegundoDesconto) {
        this.totalSegundoDesconto = totalSegundoDesconto;
    }

    public Double getTotalTerceiroDesconto() {
        if (totalTerceiroDesconto == null) {
            totalTerceiroDesconto = 0.0;
        }
        return totalTerceiroDesconto;
    }

    public void setTotalTerceiroDesconto(Double totalTerceiroDesconto) {
        this.totalTerceiroDesconto = totalTerceiroDesconto;
    }
}
