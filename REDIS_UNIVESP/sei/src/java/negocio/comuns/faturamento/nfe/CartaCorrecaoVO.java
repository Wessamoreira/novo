package negocio.comuns.faturamento.nfe;

import java.util.Date;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

/**
 *
 * @author Kennedy Souza
 */
public class CartaCorrecaoVO extends SuperVO {

	/**
	 * VO responsavel por dados de carta de correção NFE
	 */
	private static final long serialVersionUID = 1L;

	private Integer codigo;
	private Date dataCorrecao;
	private String motivo;
	private String xmlEnvio;
	private String xmlRetorno;
	private UsuarioVO usuarioVO;
	private NotaFiscalSaidaVO notafiscalsaidaVO;
	private Integer sequenciaCorrecao;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private String mensagemRetorno;

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
	
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Date getDataCorrecao() {
		return dataCorrecao;
	}

	public void setDataCorrecao(Date dataCorrecao) {
		this.dataCorrecao = dataCorrecao;
	}

	public NotaFiscalSaidaVO getNotafiscalsaidaVO() {
		if(notafiscalsaidaVO == null){
			notafiscalsaidaVO = new NotaFiscalSaidaVO();
		}
		return notafiscalsaidaVO;
	}

	public void setNotafiscalsaidaVO(NotaFiscalSaidaVO notafiscalsaidaVO) {
		this.notafiscalsaidaVO = notafiscalsaidaVO;
	}

	public Integer getSequenciaCorrecao() {
		if(sequenciaCorrecao == null){
			sequenciaCorrecao = 0;
		}
		return sequenciaCorrecao;
	}

	public void setSequenciaCorrecao(Integer sequenciaCorrecao) {
		this.sequenciaCorrecao = sequenciaCorrecao;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if(unidadeEnsinoVO == null){
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public String getMensagemRetorno() {
		if(mensagemRetorno == null){
			mensagemRetorno = "";
		}
		return mensagemRetorno;
	}

	public void setMensagemRetorno(String mensagemRetorno) {
		this.mensagemRetorno = mensagemRetorno;
	}

	public String getDataEmissao_Apresentar() {
		return (UteisData.getData(dataCorrecao));
	}
	
}
