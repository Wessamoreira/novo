package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.SituacaoRecuperacaoNotaEnum;
import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.arquitetura.SuperVO;

@XmlRootElement(name = "historicoNota")
public class HistoricoNotaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1255603609628671387L;
	private Integer codigo;
	private HistoricoVO historico;
	private TipoNotaConceitoEnum tipoNota;
	private Boolean notaRecuperacao;
	private BimestreEnum agrupamentoNota;
	private SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota;
	/**
	 * Transiente
	 * @author Rodrigo Wind - 29/07/2015
	 * @return
	 */
	private String tituloNota;
	private String notaApresentar;
	private Double notaLancada;
	private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceito;
	private List<HistoricoNotaParcialVO> historicoNotaParcialVOs;
	
	
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public HistoricoVO getHistorico() {
		if (historico == null) {
			historico = new HistoricoVO();
		}
		return historico;
	}
	
	public void setHistorico(HistoricoVO historico) {
		this.historico = historico;
	}
	
	@XmlElement(name = "tipoNota")
	public TipoNotaConceitoEnum getTipoNota() {
		return tipoNota;
	}
	
	public void setTipoNota(TipoNotaConceitoEnum tipoNota) {
		this.tipoNota = tipoNota;
	}
	
	public Boolean getNotaRecuperacao() {
		if (notaRecuperacao == null) {
			notaRecuperacao = false;
		}
		return notaRecuperacao;
	}
	public void setNotaRecuperacao(Boolean notaRecuperacao) {
		this.notaRecuperacao = notaRecuperacao;
	}
	
	public BimestreEnum getAgrupamentoNota() {
		return agrupamentoNota;
	}
	
	public void setAgrupamentoNota(BimestreEnum agrupamentoNota) {
		this.agrupamentoNota = agrupamentoNota;
	}
	
	public SituacaoRecuperacaoNotaEnum getSituacaoRecuperacaoNota() {		
		return situacaoRecuperacaoNota;
	}
	
	public void setSituacaoRecuperacaoNota(SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota) {
		this.situacaoRecuperacaoNota = situacaoRecuperacaoNota;
	}
	/**
	 * @return the tituloNota
	 */
	public String getTituloNota() {
		if (tituloNota == null) {
			tituloNota = "";
		}
		return tituloNota;
	}
	/**
	 * @param tituloNota the tituloNota to set
	 */
	public void setTituloNota(String tituloNota) {
		this.tituloNota = tituloNota;
	}
	/**
	 * @return the notaLancada
	 */
	@XmlElement(name = "notaLancada")
	public Double getNotaLancada() {		
		return notaLancada;
	}
	/**
	 * @param notaLancada the notaLancada to set
	 */
	public void setNotaLancada(Double notaLancada) {
		this.notaLancada = notaLancada;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tipoNota == null) ? 0 : tipoNota.hashCode())+((historico == null) ? 0 : historico.getCodigo().hashCode());
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
		HistoricoNotaVO other = (HistoricoNotaVO) obj;
		if (tipoNota != other.tipoNota)
			return false;
		if (!other.getCodigo().equals(getHistorico().getCodigo()))
			return false;
		return true;
	}
	/**
	 * @return the configuracaoAcademicoNotaConceito
	 */
	@XmlElement(name = "configuracaoAcademicoNotaConceito")
	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceito() {
		if(configuracaoAcademicoNotaConceito == null){
			configuracaoAcademicoNotaConceito = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceito;
	}
	/**
	 * @param configuracaoAcademicoNotaConceito the configuracaoAcademicoNotaConceito to set
	 */
	public void setConfiguracaoAcademicoNotaConceito(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceito) {
		this.configuracaoAcademicoNotaConceito = configuracaoAcademicoNotaConceito;
	}
	public String getNotaApresentar() {
		if(notaApresentar == null) {
			notaApresentar =  "";
		}
		return notaApresentar;
	}
	public void setNotaApresentar(String notaApresentar) {
		this.notaApresentar = notaApresentar;
	}
	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialVOs() {
		if(historicoNotaParcialVOs == null) {
			historicoNotaParcialVOs =  new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialVOs;
	}
	public void setHistoricoNotaParcialVOs(List<HistoricoNotaParcialVO> historicoNotaParcialVOs) {
		this.historicoNotaParcialVOs = historicoNotaParcialVOs;
	}
	
	
	
}
