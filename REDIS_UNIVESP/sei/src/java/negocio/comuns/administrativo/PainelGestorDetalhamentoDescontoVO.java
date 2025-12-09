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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class PainelGestorDetalhamentoDescontoVO implements Serializable {

	private String nomeCategoriaDesconto;
	private String nomeTipoDesconto;
	private Double totalDesconto;
	private Integer numeroAlunoComDesconto;
	private List<PainelGestorDetalhamentoDescontoVO> painelGestorDetalhamentoDescontoVOs;
	private BigDecimal totalDescontoPorCategoria;
	private Integer totalNumeroAlunoPorCategoria;
	
	public static final long serialVersionUID = 1L;

	public String getNomeTipoDesconto() {
		if (nomeTipoDesconto == null) {
			nomeTipoDesconto = "";
		}
		return nomeTipoDesconto;
	}

	public void setNomeTipoDesconto(String nomeTipoDesconto) {
		this.nomeTipoDesconto = nomeTipoDesconto;
	}

	public Integer getNumeroAlunoComDesconto() {
		if (numeroAlunoComDesconto == null) {
			numeroAlunoComDesconto = 0;
		}
		return numeroAlunoComDesconto;
	}

	public void setNumeroAlunoComDesconto(Integer numeroAlunoComDesconto) {
		this.numeroAlunoComDesconto = numeroAlunoComDesconto;
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

	public String getNomeCategoriaDesconto() {
		if (nomeCategoriaDesconto == null) {
			nomeCategoriaDesconto = "";
		}
		return nomeCategoriaDesconto;
	}

	public void setNomeCategoriaDesconto(String nomeCategoriaDesconto) {
		this.nomeCategoriaDesconto = nomeCategoriaDesconto;
	}

	public List<PainelGestorDetalhamentoDescontoVO> getPainelGestorDetalhamentoDescontoVOs() {
		if (painelGestorDetalhamentoDescontoVOs == null) {
			painelGestorDetalhamentoDescontoVOs = new ArrayList<PainelGestorDetalhamentoDescontoVO>(0);
		}
		return painelGestorDetalhamentoDescontoVOs;
	}

	public void setPainelGestorDetalhamentoDescontoVOs(List<PainelGestorDetalhamentoDescontoVO> painelGestorDetalhamentoDescontoVOs) {
		this.painelGestorDetalhamentoDescontoVOs = painelGestorDetalhamentoDescontoVOs;
	}

	public BigDecimal getTotalDescontoPorCategoria() {
		if (totalDescontoPorCategoria == null) {
			totalDescontoPorCategoria = BigDecimal.ZERO;
		}
		return totalDescontoPorCategoria;
	}

	public void setTotalDescontoPorCategoria(BigDecimal totalDescontoPorCategoria) {
		this.totalDescontoPorCategoria = totalDescontoPorCategoria;
	}

	public Integer getTotalNumeroAlunoPorCategoria() {
		if (totalNumeroAlunoPorCategoria == null) {
			totalNumeroAlunoPorCategoria = 0;
		}
		return totalNumeroAlunoPorCategoria;
	}

	public void setTotalNumeroAlunoPorCategoria(Integer totalNumeroAlunoPorCategoria) {
		this.totalNumeroAlunoPorCategoria = totalNumeroAlunoPorCategoria;
	}
	
	public Boolean getIsNomeCategoriaDescontoPreenchido() {
		return !getNomeCategoriaDesconto().equals("");
	}
	
	public String getTotalizadorNomeCategoriaDesconto_Apresentar() {
		if (getNomeCategoriaDesconto().equals("")) {
			return "Totais";
		}
		return "Total de " + getNomeCategoriaDesconto();
	}
	
	public JRDataSource getPainelGestorDetalhamentoDescontoSubordinadaVOs() {
        return new JRBeanArrayDataSource(getPainelGestorDetalhamentoDescontoVOs().toArray());
    }
}
