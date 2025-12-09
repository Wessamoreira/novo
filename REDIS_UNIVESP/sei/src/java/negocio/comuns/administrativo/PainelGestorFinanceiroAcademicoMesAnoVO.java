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
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.enumeradores.MesAnoEnum;

public class PainelGestorFinanceiroAcademicoMesAnoVO implements Serializable {

	private String mesAno;
	private Integer totalAlunoAtivo;
	private Integer totalAlunoAptoFormar;
	private Integer totalNovoAluno;
	private Integer totalAlunoRenovado;
	private Integer totalAlunoRetornoEvasao;
	private Integer totalAlunoCancelado;
	private Integer totalAlunoTrancado;
	private Integer totalAlunoAbandanado;
	private Integer totalAlunoFormado;
	private Integer totalAlunoNaoRenovado;
	private Integer totalAlunoPreMatricula;
	private Integer totalAlunoTransferenciaSaida;
	private Integer totalAlunoTransferenciaInterna;
	private Double totalReceita;
	private Double totalDespesa;
	private Double totalMediaReceita;
	private Double totalMediaDespesa;
	private List<PainelGestorFinanceiroAcademicoNivelEducacionalVO> painelGestorFinanceiroAcademicoNivelEducacionalVOs;
	private Double totalOutraDespesa;
	private Double totalOutraReceita;
	private String mesAnoApresentar;
	private String dia;
	private Integer ordem;
	private String nivelEducacional;
	private String periodicidade;
	public static final long serialVersionUID = 1L;

	public String getMesAnoApresentar() {
		if ((mesAnoApresentar == null || mesAnoApresentar.isEmpty()) && !getMesAno().isEmpty()) {
			mesAnoApresentar = MesAnoEnum.getEnum(mesAno.substring(0, mesAno.indexOf("/"))).getMesAbreviado() + "/" + getMesAno().substring(getMesAno().length() - 2, getMesAno().length());
		}
		return mesAnoApresentar;
	}

	public Integer getQuantidadeNiveis() {
		return getPainelGestorFinanceiroAcademicoNivelEducacionalVOs().size();
	}

	public Double getTotalOutraDespesa() {
		if (totalOutraDespesa == null) {
			totalOutraDespesa = 0.0;
		}
		return totalOutraDespesa;
	}

	public void setTotalOutraDespesa(Double totalOutraDespesa) {
		this.totalOutraDespesa = totalOutraDespesa;
	}

	public Double getTotalOutraReceita() {
		if (totalOutraReceita == null) {
			totalOutraReceita = 0.0;
		}
		return totalOutraReceita;
	}

	public void setTotalOutraReceita(Double totalOutraReceita) {
		this.totalOutraReceita = totalOutraReceita;
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

	public List<PainelGestorFinanceiroAcademicoNivelEducacionalVO> getPainelGestorFinanceiroAcademicoNivelEducacionalVOs() {
		if (painelGestorFinanceiroAcademicoNivelEducacionalVOs == null) {
			painelGestorFinanceiroAcademicoNivelEducacionalVOs = new ArrayList<PainelGestorFinanceiroAcademicoNivelEducacionalVO>(0);
		}
		return painelGestorFinanceiroAcademicoNivelEducacionalVOs;
	}

	public void setPainelGestorFinanceiroAcademicoNivelEducacionalVOs(List<PainelGestorFinanceiroAcademicoNivelEducacionalVO> painelGestorFinanceiroAcademicoNivelEducacionalVOs) {
		this.painelGestorFinanceiroAcademicoNivelEducacionalVOs = painelGestorFinanceiroAcademicoNivelEducacionalVOs;
	}

	public Integer getTotalAlunoAtivo() {
		if (totalAlunoAtivo == null) {
			totalAlunoAtivo = 0;
		}
		return totalAlunoAtivo;
	}

	public void setTotalAlunoAtivo(Integer totalAlunoAtivo) {
		this.totalAlunoAtivo = totalAlunoAtivo;
	}

	public Integer getTotalAlunoCancelado() {
		if (totalAlunoCancelado == null) {
			totalAlunoCancelado = 0;
		}
		return totalAlunoCancelado;
	}

	public void setTotalAlunoCancelado(Integer totalAlunoCancelado) {
		this.totalAlunoCancelado = totalAlunoCancelado;
	}

	public Integer getTotalAlunoRenovado() {
		if (totalAlunoRenovado == null) {
			totalAlunoRenovado = 0;
		}
		return totalAlunoRenovado;
	}

	public void setTotalAlunoRenovado(Integer totalAlunoRenovado) {
		this.totalAlunoRenovado = totalAlunoRenovado;
	}

	public Integer getTotalAlunoTrancado() {
		if (totalAlunoTrancado == null) {
			totalAlunoTrancado = 0;
		}
		return totalAlunoTrancado;
	}

	public void setTotalAlunoTrancado(Integer totalAlunoTrancado) {
		this.totalAlunoTrancado = totalAlunoTrancado;
	}

	public Integer getTotalAlunoTransferenciaSaida() {
		if (totalAlunoTransferenciaSaida == null) {
			totalAlunoTransferenciaSaida = 0;
		}
		return totalAlunoTransferenciaSaida;
	}

	public void setTotalAlunoTransferenciaSaida(Integer totalAlunoTransferenciaSaida) {
		this.totalAlunoTransferenciaSaida = totalAlunoTransferenciaSaida;
	}

	public Integer getTotalNovoAluno() {
		if (totalNovoAluno == null) {
			totalNovoAluno = 0;
		}
		return totalNovoAluno;
	}

	public void setTotalNovoAluno(Integer totalNovoAluno) {
		this.totalNovoAluno = totalNovoAluno;
	}

	public Double getTotalDespesa() {
		if (totalDespesa == null) {
			totalDespesa = 0.0;
		}
		return totalDespesa;
	}

	public void setTotalDespesa(Double totalDespesa) {
		this.totalDespesa = totalDespesa;
	}

	public Double getTotalMediaDespesa() {
		if (totalMediaDespesa == null) {
			totalMediaDespesa = 0.0;
		}
		return totalMediaDespesa;
	}

	public void setTotalMediaDespesa(Double totalMediaDespesa) {
		this.totalMediaDespesa = totalMediaDespesa;
	}

	public Double getTotalMediaReceita() {
		if (totalMediaReceita == null) {
			totalMediaReceita = 0.0;
		}
		return totalMediaReceita;
	}

	public void setTotalMediaReceita(Double totalMediaReceita) {
		this.totalMediaReceita = totalMediaReceita;
	}

	public Double getTotalReceita() {
		if (totalReceita == null) {
			totalReceita = 0.0;
		}
		return totalReceita;
	}

	public void setTotalReceita(Double totalReceita) {
		this.totalReceita = totalReceita;
	}

	public String getDia() {
		if (dia == null) {
			dia = "";
		}
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public Integer getTotalAlunoAptoFormar() {
		if (totalAlunoAptoFormar == null) {
			totalAlunoAptoFormar = 0;
		}
		return totalAlunoAptoFormar;
	}

	public void setTotalAlunoAptoFormar(Integer totalAlunoAptoFormar) {
		this.totalAlunoAptoFormar = totalAlunoAptoFormar;
	}

	public Integer getTotalAlunoPreMatricula() {
		if (totalAlunoPreMatricula == null) {
			totalAlunoPreMatricula = 0;
		}
		return totalAlunoPreMatricula;
	}

	public void setTotalAlunoPreMatricula(Integer totalAlunoPreMatricula) {
		this.totalAlunoPreMatricula = totalAlunoPreMatricula;
	}

	public Integer getTotalAlunoAbandanado() {
		if (totalAlunoAbandanado == null) {
			totalAlunoAbandanado = 0;
		}
		return totalAlunoAbandanado;
	}

	public void setTotalAlunoAbandanado(Integer totalAlunoAbandanado) {
		this.totalAlunoAbandanado = totalAlunoAbandanado;
	}

	public Integer getTotalAlunoFormado() {
		if (totalAlunoFormado == null) {
			totalAlunoFormado = 0;
		}
		return totalAlunoFormado;
	}

	public void setTotalAlunoFormado(Integer totalAlunoFormado) {
		this.totalAlunoFormado = totalAlunoFormado;
	}

	public Integer getTotalAlunoNaoRenovado() {
		if (totalAlunoNaoRenovado == null) {
			totalAlunoNaoRenovado = 0;
		}
		return totalAlunoNaoRenovado;
	}

	public void setTotalAlunoNaoRenovado(Integer totalAlunoNaoRenovado) {
		this.totalAlunoNaoRenovado = totalAlunoNaoRenovado;
	}

	public Integer getTotalEvasao() {
		return getTotalAlunoAbandanado() + getTotalAlunoCancelado() + getTotalAlunoTrancado() + getTotalAlunoTransferenciaSaida()+getTotalAlunoTransferenciaInterna();
	}

	public Integer getTotalAlunoRetornoEvasao() {
		if(totalAlunoRetornoEvasao == null){
			totalAlunoRetornoEvasao = 0;
		}
		return totalAlunoRetornoEvasao;
	}

	public void setTotalAlunoRetornoEvasao(Integer totalAlunoRetornoEvasao) {
		this.totalAlunoRetornoEvasao = totalAlunoRetornoEvasao;
	}

	public Integer getTotalAlunoTransferenciaInterna() {
		if(totalAlunoTransferenciaInterna == null){
			totalAlunoTransferenciaInterna = 0;
		}
		return totalAlunoTransferenciaInterna;
	}

	public void setTotalAlunoTransferenciaInterna(Integer totalAlunoTransferenciaInterna) {
		this.totalAlunoTransferenciaInterna = totalAlunoTransferenciaInterna;
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

	public String getNivelEducacional() {
		if (nivelEducacional == null) {
			nivelEducacional = "";
		}
		return nivelEducacional;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}

	public String getPeriodicidade() {
		if (periodicidade == null) {
			periodicidade = "";
		}
		return periodicidade;
	}

	public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
	}
	
}
