package negocio.comuns.recursoshumanos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.enumeradores.AtivoInativoEnum;
import negocio.comuns.recursoshumanos.enumeradores.TipoTarifaEnum;

/**
 * Reponsável por manter os dados da entidade LinhaTransporte. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ParametroValeTransporteVO extends SuperVO {

	private static final long serialVersionUID = 5654985322541300989L;

	private Integer codigo;
	private String descricao;
	private TipoTransporteVO tipoLinhaTransporte;
	private TipoTarifaEnum tipoTarifa;
	private BigDecimal valor;
	private String itinerario;
	private AtivoInativoEnum situacao;
	private EventoFolhaPagamentoVO eventoFolhaPagamento;
	private Date inicioVigencia;
	private Date fimVigencia;

	private List<ParametroValeTransporteHistoricoVO> historicoVOs;

	public enum EnumCampoConsultaParametroValeTransporte {
		DESCRICAO, CODIGO;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
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

	public TipoTransporteVO getTipoLinhaTransporte() {
		if (tipoLinhaTransporte == null) {
			tipoLinhaTransporte = new TipoTransporteVO();
		}
		return tipoLinhaTransporte;
	}

	public void setTipoLinhaTransporte(TipoTransporteVO tipoLinhaTransporte) {
		this.tipoLinhaTransporte = tipoLinhaTransporte;
	}

	public TipoTarifaEnum getTipoTarifa() {
		return tipoTarifa;
	}

	public void setTipoTarifa(TipoTarifaEnum tipoTarifa) {
		this.tipoTarifa = tipoTarifa;
	}

	public BigDecimal getValor() {
		if (valor == null) {
			valor = BigDecimal.ZERO;
		}
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getItinerario() {
		if (itinerario == null) {
			itinerario = "";
		}
		return itinerario;
	}

	public void setItinerario(String itinerario) {
		this.itinerario = itinerario;
	}

	public AtivoInativoEnum getSituacao() {
		return situacao;
	}

	public void setSituacao(AtivoInativoEnum situacao) {
		this.situacao = situacao;
	}

	public EventoFolhaPagamentoVO getEventoFolhaPagamento() {
		if (eventoFolhaPagamento == null) {
			eventoFolhaPagamento = new EventoFolhaPagamentoVO();
		}
		return eventoFolhaPagamento;
	}

	public void setEventoFolhaPagamento(EventoFolhaPagamentoVO eventoFolhaPagamento) {
		this.eventoFolhaPagamento = eventoFolhaPagamento;
	}

	public List<ParametroValeTransporteHistoricoVO> getHistoricoVOs() {
		if (historicoVOs == null) {
			historicoVOs = new ArrayList<>();
		}
		return historicoVOs;
	}

	public void setHistoricoVOs(List<ParametroValeTransporteHistoricoVO> historicoVOs) {
		this.historicoVOs = historicoVOs;
	}

	public Date getInicioVigencia() {
		if (inicioVigencia == null) {
			inicioVigencia = new Date();
		}
		return inicioVigencia;
	}

	public void setInicioVigencia(Date inicioVigencia) {
		this.inicioVigencia = inicioVigencia;
	}

	public Date getFimVigencia() {
		return fimVigencia;
	}

	public void setFimVigencia(Date fimVigencia) {
		this.fimVigencia = fimVigencia;
	}
}
