package negocio.comuns.faturamento.nfe;

import java.util.Date;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author
 */
public class LoteInutilizacaoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer codigo;
	private Integer nrInicial;
	private Integer nrfinal;
	private Date dataInutilizacao;
	private String motivo;
	private String xmlEnvio;
	private String xmlRetorno;
	private UsuarioVO usuarioVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private String mensagemRetorno;
	private String protocoloInutilizacao;

	public String getProtocoloInutilizacao() {
		if(protocoloInutilizacao == null ){
			protocoloInutilizacao = "";
		}
		return protocoloInutilizacao;
	}

	public void setProtocoloInutilizacao(String protocoloInutilizacao) {
		this.protocoloInutilizacao = protocoloInutilizacao;
	}

	public Date getDataInutilizacao() {
		if (dataInutilizacao == null) {
			dataInutilizacao = new Date();
		}
		return dataInutilizacao;
	}

	public void setDataInutilizacao(Date dataInutilizacao) {
		this.dataInutilizacao = dataInutilizacao;
	}

	public String getMotivo() {
		if (motivo == null) {
			motivo = "";
		}
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getXmlEnvio() {
		if (xmlEnvio == null) {
			xmlEnvio = "";
		}
		return xmlEnvio;
	}

	public void setXmlEnvio(String xmlEnvio) {
		this.xmlEnvio = xmlEnvio;
	}

	public String getXmlRetorno() {
		if (xmlRetorno == null) {
			xmlRetorno = "";
		}
		return xmlRetorno;
	}

	public void setXmlRetorno(String xmlRetorno) {
		this.xmlRetorno = xmlRetorno;
	}

	public UsuarioVO getUsuarioVO() {
		if (usuarioVO == null) {
			usuarioVO = new UsuarioVO();
		}
		return usuarioVO;
	}

	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public String getMensagemRetorno() {
		if (mensagemRetorno == null) {
			mensagemRetorno = "";
		}
		return mensagemRetorno;
	}

	public void setMensagemRetorno(String mensagemRetorno) {
		this.mensagemRetorno = mensagemRetorno;
	}

	public Integer getNrInicial() {
		if(nrInicial == null){
			nrInicial = 0;
		}
		return nrInicial;
	}

	public void setNrInicial(Integer nrInicial) {
		this.nrInicial = nrInicial;
	}

	public Integer getNrfinal() {
		if(nrfinal == null){
			nrfinal = 0;
		}
		return nrfinal;
	}

	public void setNrfinal(Integer nrfinal) {
		this.nrfinal = nrfinal;
	}
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

}
