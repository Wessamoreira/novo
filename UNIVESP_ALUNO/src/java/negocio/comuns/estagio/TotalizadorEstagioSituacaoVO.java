package negocio.comuns.estagio;

import negocio.comuns.academico.enumeradores.TipoEstagioEnum;
import negocio.comuns.estagio.enumeradores.SituacaoAdicionalEstagioEnum;

public class TotalizadorEstagioSituacaoVO {

	private TipoEstagioEnum tipoEstagioEnum;
	private SituacaoAdicionalEstagioEnum situacaoAdicionalEstagioEnum;
	private String nome;
	private Integer total;
	private Integer totalAtrasado;
	private Integer totalNoPrazo;

	public TotalizadorEstagioSituacaoVO(TipoEstagioEnum tipoEstagioEnum, SituacaoAdicionalEstagioEnum situacaoAdicionalEstagioEnum, Integer total, Integer totalAtrasado, Integer totalNoPrazo) {
		super();
		this.tipoEstagioEnum = tipoEstagioEnum;
		this.total = total;
		this.totalAtrasado = totalAtrasado;
		this.totalNoPrazo = totalNoPrazo;
		this.situacaoAdicionalEstagioEnum = situacaoAdicionalEstagioEnum;
		switch (tipoEstagioEnum) {
		case OBRIGATORIO:
			if(situacaoAdicionalEstagioEnum == null || situacaoAdicionalEstagioEnum.isNenhum()) {
				this.nome = "Relatório Estágio";	
			}else if(situacaoAdicionalEstagioEnum.isAssinaturaPendente()) {
				this.nome = "Aguardando Assinatura";
			}else if(situacaoAdicionalEstagioEnum.isPendenteSolicitacaoAssinatura()) {
				this.nome = "Pendente de Solicitação";
			}
			break;
		case OBRIGATORIO_APROVEITAMENTO:
			this.nome = "Aproveitamento";
			break;
		case OBRIGATORIO_EQUIVALENCIA:
			this.nome = "Equivalência";
			break;
		default:
			this.nome = "";
			break;
		}
	}

	public TipoEstagioEnum getTipoEstagioEnum() {
		return tipoEstagioEnum;
	}

	public void setTipoEstagioEnum(TipoEstagioEnum tipoEstagioEnum) {
		this.tipoEstagioEnum = tipoEstagioEnum;
	}
	

	public SituacaoAdicionalEstagioEnum getSituacaoAdicionalEstagioEnum() {
		return situacaoAdicionalEstagioEnum;
	}

	public void setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum situacaoAdicionalEstagioEnum) {
		this.situacaoAdicionalEstagioEnum = situacaoAdicionalEstagioEnum;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getTotal() {
		if (total == null) {
			total = 0;
		}
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getTotalAtrasado() {
		if (totalAtrasado == null) {
			totalAtrasado = 0;
		}
		return totalAtrasado;
	}

	public void setTotalAtrasado(Integer totalAtrasado) {
		this.totalAtrasado = totalAtrasado;
	}

	public Integer getTotalNoPrazo() {
		if (totalNoPrazo == null) {
			totalNoPrazo = 0;
		}
		return totalNoPrazo;
	}

	public void setTotalNoPrazo(Integer totalNoPrazo) {
		this.totalNoPrazo = totalNoPrazo;
	}

}
