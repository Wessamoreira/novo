package negocio.comuns.estagio;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.enumeradores.TipoEstagioEnum;
import negocio.comuns.estagio.enumeradores.SituacaoAdicionalEstagioEnum;
import negocio.comuns.estagio.enumeradores.SituacaoEstagioEnum;


public class DashboardEstagioVO {

	private SituacaoEstagioEnum situacaoEstagioEnum;
	private SituacaoAdicionalEstagioEnum situacaoAdicionalEstagioEnum;
	private TipoEstagioEnum tipoEstagioEnum;
	private boolean estagioNoPrazo = false;
	private boolean estagioAtrasado= false;
	private Integer cargaHorariaExigida;
	private Integer cargaHorariaPendentes;
	private Integer cargaHorariaAguardandoAssinatura;
	private Integer cargaHorariaRealizando;
	private Integer cargaHorariaEmAnalise;
	private Integer cargaHorariaEmCorrecaoAluna;
	private Integer cargaHorariaIndeferido;
	private Integer cargaHorariaDeferido;

	private List<TotalizadorEstagioSituacaoVO> listaTotalizadorAguardandoAnalise;
	private List<TotalizadorEstagioSituacaoVO> listaTotalizadorEmAnalise;
	private List<TotalizadorEstagioSituacaoVO> listaTotalizadorEmCorrecao;
	private List<TotalizadorEstagioSituacaoVO> listaTotalizadorIndeferido;
	private List<TotalizadorEstagioSituacaoVO> listaTotalizadorDeferido;
	
	
	
	
	public boolean isEstagioNoPrazo() {
		return estagioNoPrazo;
	}

	public void setEstagioNoPrazo(boolean estagioNoPrazo) {
		this.estagioNoPrazo = estagioNoPrazo;
	}

	public boolean isEstagioAtrasado() {
		return estagioAtrasado;
	}

	public void setEstagioAtrasado(boolean estagioAtrasado) {
		this.estagioAtrasado = estagioAtrasado;
	}

	public SituacaoEstagioEnum getSituacaoEstagioEnum() {
		if (situacaoEstagioEnum == null) {
			situacaoEstagioEnum = SituacaoEstagioEnum.EXIGIDO;
		}
		return situacaoEstagioEnum;
	}

	public void setSituacaoEstagioEnum(SituacaoEstagioEnum situacaoEstagioEnum) {
		this.situacaoEstagioEnum = situacaoEstagioEnum;
	}

	public SituacaoAdicionalEstagioEnum getSituacaoAdicionalEstagioEnum() {
		if (situacaoAdicionalEstagioEnum == null) {
			situacaoAdicionalEstagioEnum = SituacaoAdicionalEstagioEnum.NENHUM;
		}
		return situacaoAdicionalEstagioEnum;
	}

	public void setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum situacaoAdicionalEstagioEnum) {
		this.situacaoAdicionalEstagioEnum = situacaoAdicionalEstagioEnum;
	}

	public TipoEstagioEnum getTipoEstagioEnum() {
		if (tipoEstagioEnum == null) {
			tipoEstagioEnum = TipoEstagioEnum.NENHUM;
		}
		return tipoEstagioEnum;
	}

	public void setTipoEstagioEnum(TipoEstagioEnum tipoEstagioEnum) {
		this.tipoEstagioEnum = tipoEstagioEnum;
	}

	public Integer getCargaHorariaExigida() {
		if (cargaHorariaExigida == null) {
			cargaHorariaExigida = 0;
		}
		return cargaHorariaExigida;
	}

	public void setCargaHorariaExigida(Integer cargaHorariaExigida) {
		this.cargaHorariaExigida = cargaHorariaExigida;
	}

	public Integer getCargaHorariaPendentes() {
		if (cargaHorariaPendentes == null) {
			cargaHorariaPendentes = 0;
		}
		return cargaHorariaPendentes;
	}

	public void setCargaHorariaPendentes(Integer cargaHorariaPendentes) {
		this.cargaHorariaPendentes = cargaHorariaPendentes;
	}

	public Integer getCargaHorariaAguardandoAssinatura() {
		if (cargaHorariaAguardandoAssinatura == null) {
			cargaHorariaAguardandoAssinatura = 0;
		}
		return cargaHorariaAguardandoAssinatura;
	}

	public void setCargaHorariaAguardandoAssinatura(Integer cargaHorariaAguardandoAssinatura) {
		this.cargaHorariaAguardandoAssinatura = cargaHorariaAguardandoAssinatura;
	}

	public Integer getCargaHorariaRealizando() {
		if (cargaHorariaRealizando == null) {
			cargaHorariaRealizando = 0;
		}
		return cargaHorariaRealizando;
	}

	public void setCargaHorariaRealizando(Integer cargaHorariaRealizando) {
		this.cargaHorariaRealizando = cargaHorariaRealizando;
	}

	public Integer getCargaHorariaEmAnalise() {
		if (cargaHorariaEmAnalise == null) {
			cargaHorariaEmAnalise = 0;
		}
		return cargaHorariaEmAnalise;
	}

	public void setCargaHorariaEmAnalise(Integer cargaHorariaEmAnalise) {
		this.cargaHorariaEmAnalise = cargaHorariaEmAnalise;
	}

	public Integer getCargaHorariaEmCorrecaoAluna() {
		if (cargaHorariaEmCorrecaoAluna == null) {
			cargaHorariaEmCorrecaoAluna = 0;
		}
		return cargaHorariaEmCorrecaoAluna;
	}

	public void setCargaHorariaEmCorrecaoAluna(Integer cargaHorariaEmCorrecaoAluna) {
		this.cargaHorariaEmCorrecaoAluna = cargaHorariaEmCorrecaoAluna;
	}

	public Integer getCargaHorariaIndeferido() {
		if (cargaHorariaIndeferido == null) {
			cargaHorariaIndeferido = 0;
		}
		return cargaHorariaIndeferido;
	}

	public void setCargaHorariaIndeferido(Integer cargaHorariaIndeferido) {
		this.cargaHorariaIndeferido = cargaHorariaIndeferido;
	}

	public Integer getCargaHorariaDeferido() {
		if (cargaHorariaDeferido == null) {
			cargaHorariaDeferido = 0;
		}
		return cargaHorariaDeferido;
	}

	public void setCargaHorariaDeferido(Integer cargaHorariaDeferido) {
		this.cargaHorariaDeferido = cargaHorariaDeferido;
	}

	public List<TotalizadorEstagioSituacaoVO> getListaTotalizadorAguardandoAnalise() {
		if (listaTotalizadorAguardandoAnalise == null) {
			listaTotalizadorAguardandoAnalise = new ArrayList<>();
		}
		return listaTotalizadorAguardandoAnalise;
	}

	public void setListaTotalizadorAguardandoAnalise(List<TotalizadorEstagioSituacaoVO> listaTotalizadorAguardandoAnalise) {
		this.listaTotalizadorAguardandoAnalise = listaTotalizadorAguardandoAnalise;
	}

	public List<TotalizadorEstagioSituacaoVO> getListaTotalizadorEmAnalise() {
		if (listaTotalizadorEmAnalise == null) {
			listaTotalizadorEmAnalise = new ArrayList<>();
		}
		return listaTotalizadorEmAnalise;
	}

	public void setListaTotalizadorEmAnalise(List<TotalizadorEstagioSituacaoVO> listaTotalizadorEmAnalise) {
		this.listaTotalizadorEmAnalise = listaTotalizadorEmAnalise;
	}

	public List<TotalizadorEstagioSituacaoVO> getListaTotalizadorEmCorrecao() {
		if (listaTotalizadorEmCorrecao == null) {
			listaTotalizadorEmCorrecao = new ArrayList<>();
		}
		return listaTotalizadorEmCorrecao;
	}

	public void setListaTotalizadorEmCorrecao(List<TotalizadorEstagioSituacaoVO> listaTotalizadorEmCorrecao) {
		this.listaTotalizadorEmCorrecao = listaTotalizadorEmCorrecao;
	}

	public List<TotalizadorEstagioSituacaoVO> getListaTotalizadorIndeferido() {
		if (listaTotalizadorIndeferido == null) {
			listaTotalizadorIndeferido = new ArrayList<>();
		}
		return listaTotalizadorIndeferido;
	}

	public void setListaTotalizadorIndeferido(List<TotalizadorEstagioSituacaoVO> listaTotalizadorIndeferido) {
		this.listaTotalizadorIndeferido = listaTotalizadorIndeferido;
	}

	public List<TotalizadorEstagioSituacaoVO> getListaTotalizadorDeferido() {
		if (listaTotalizadorDeferido == null) {
			listaTotalizadorDeferido = new ArrayList<>();
		}
		return listaTotalizadorDeferido;
	}

	public void setListaTotalizadorDeferido(List<TotalizadorEstagioSituacaoVO> listaTotalizadorDeferido) {
		this.listaTotalizadorDeferido = listaTotalizadorDeferido;
	}

}
