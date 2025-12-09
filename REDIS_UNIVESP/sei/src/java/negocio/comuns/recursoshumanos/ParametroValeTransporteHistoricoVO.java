package negocio.comuns.recursoshumanos;

import java.math.BigDecimal;
import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoTarifaEnum;

/**
 * Reponsável por manter os dados da entidade ParametroValeTransporteHistorico.
 * Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ParametroValeTransporteHistoricoVO extends SuperVO {

	private static final long serialVersionUID = -8704921613014086945L;

	private Integer codigo;
	private BigDecimal valor;
	private Date inicioVigencia;
	private Date fimVigencia;
	private Date dataAlteracao;
	private UsuarioVO usuarioResponsavelAlteracao;
	private ParametroValeTransporteVO parametroValeTransporte;
	private TipoTransporteVO tipoLinhaTransporte;
	private TipoTarifaEnum tipoTarifa;
	private String descricao;

	public enum EnumCampoConsultaParametroValeTransporteHistorico {
		CODIGO;
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

	public BigDecimal getValor() {
		if (valor == null) {
			valor = BigDecimal.ZERO;
		}
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
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
		if (fimVigencia == null) {
			fimVigencia = new Date();
		}
		return fimVigencia;
	}

	public void setFimVigencia(Date fimVigencia) {
		this.fimVigencia = fimVigencia;
	}

	public Date getDataAlteracao() {
		if (dataAlteracao == null) {
			dataAlteracao = new Date();
		}
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public UsuarioVO getUsuarioResponsavelAlteracao() {
		if (usuarioResponsavelAlteracao == null) {
			usuarioResponsavelAlteracao = new UsuarioVO();
		}
		return usuarioResponsavelAlteracao;
	}

	public void setUsuarioResponsavelAlteracao(UsuarioVO usuarioResponsavelAlteracao) {
		this.usuarioResponsavelAlteracao = usuarioResponsavelAlteracao;
	}

	public ParametroValeTransporteVO getParametroValeTransporte() {
		if (parametroValeTransporte == null) {
			parametroValeTransporte = new ParametroValeTransporteVO();
		}
		return parametroValeTransporte;
	}

	public void setParametroValeTransporte(ParametroValeTransporteVO parametroValeTransporte) {
		this.parametroValeTransporte = parametroValeTransporte;
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

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
