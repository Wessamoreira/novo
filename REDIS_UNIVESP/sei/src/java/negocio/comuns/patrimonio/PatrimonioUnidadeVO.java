package negocio.comuns.patrimonio;
/**
 * 
 * @author Leonardo Riciolle
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.enumeradores.EstadoBemEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.patrimonio.enumeradores.SituacaoPatrimonioUnidadeEnum;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;

public class PatrimonioUnidadeVO extends SuperVO {

	private static final long serialVersionUID = -8350956946264005912L;
	private Integer codigo;
	private PatrimonioVO patrimonioVO;
	private String codigoBarra;
	private String numeroDeSerie;	
	private SituacaoPatrimonioUnidadeEnum situacaoPatrimonioUnidade;
	private BigDecimal valorRecurso;	
	private Boolean unidadeLocado;
	private Boolean permiteReserva;	
	private LocalArmazenamentoVO localArmazenamento;
	private String estadoBem;
	private List<OcorrenciaPatrimonioVO> listaOcorrencias;
	/**
	 * Transiente - Usado no Mapa de Patrimonio Separado Descarte.
	 */
	private Boolean selecionado;
	

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public PatrimonioVO getPatrimonioVO() {
		if (patrimonioVO == null) {
			patrimonioVO = new PatrimonioVO();
		}
		return patrimonioVO;
	}

	public void setPatrimonioVO(PatrimonioVO patrimonioVO) {
		this.patrimonioVO = patrimonioVO;
	}

	public String getCodigoBarra() {
		if (codigoBarra == null) {
			codigoBarra = "";
		}
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	public String getNumeroDeSerie() {
		if (numeroDeSerie == null) {
			numeroDeSerie = "";
		}
		return numeroDeSerie;
	}

	public void setNumeroDeSerie(String numeroDeSerie) {
		this.numeroDeSerie = numeroDeSerie;
	}

	
	public SituacaoPatrimonioUnidadeEnum getSituacaoPatrimonioUnidade() {
		if (situacaoPatrimonioUnidade == null) {
			situacaoPatrimonioUnidade = SituacaoPatrimonioUnidadeEnum.DISPONIVEL;
		}
		return situacaoPatrimonioUnidade;
	}

	public void setSituacaoPatrimonioUnidade(SituacaoPatrimonioUnidadeEnum situacaoPatrimonioUnidade) {
		this.situacaoPatrimonioUnidade = situacaoPatrimonioUnidade;
	}

	
	public Boolean getUnidadeLocado() {
		if (unidadeLocado == null) {
			unidadeLocado = false;
		}
		return unidadeLocado;
	}

	public void setUnidadeLocado(Boolean unidadeLocado) {
		this.unidadeLocado = unidadeLocado;
	}

	public Boolean getPermiteReserva() {
		if (permiteReserva == null) {
			permiteReserva = false;
		}
		return permiteReserva;
	}

	public void setPermiteReserva(Boolean permiteReserva) {
		this.permiteReserva = permiteReserva;
	}

	public LocalArmazenamentoVO getLocalArmazenamento() {
		if (localArmazenamento == null) {
			localArmazenamento = new LocalArmazenamentoVO();
		}
		return localArmazenamento;
	}

	public void setLocalArmazenamento(LocalArmazenamentoVO localArmazenamento) {
		this.localArmazenamento = localArmazenamento;
	}	

	
	public BigDecimal getValorRecurso() {
		if (valorRecurso == null) {
			valorRecurso = BigDecimal.ZERO;
		}
		return valorRecurso;
	}

	public void setValorRecurso(BigDecimal valorRecurso) {
		this.valorRecurso = valorRecurso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoBarra == null) ? 0 : codigoBarra.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PatrimonioUnidadeVO other = (PatrimonioUnidadeVO) obj;
		if (codigoBarra == null) {
			if (other.codigoBarra != null)
				return false;
		} else if (!Long.valueOf(codigoBarra).equals(Long.valueOf(other.codigoBarra)))
			return false;
		return true;
	}

	/**
	 * @return the selecionado
	 */
	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = true;
		}
		return selecionado;
	}

	/**
	 * @param selecionado the selecionado to set
	 */
	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}
	
	public String getEstadoBem() {
		if(estadoBem == null){
			estadoBem = "";
		}
		return estadoBem;
	}

	public void setEstadoBem(String estadoBem) {
		this.estadoBem = estadoBem;
	}
	

	public List<SelectItem> getListaEstatoBem() {
			return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(EstadoBemEnum.class);
		}


	public List<OcorrenciaPatrimonioVO> getListaOcorrencias() {
		if(listaOcorrencias == null){
			listaOcorrencias = new ArrayList<OcorrenciaPatrimonioVO>(0);
		}
		return listaOcorrencias;
	}

	public void setListaOcorrencias(List<OcorrenciaPatrimonioVO> listaOcorrencias) {
		this.listaOcorrencias = listaOcorrencias;
	}
}
