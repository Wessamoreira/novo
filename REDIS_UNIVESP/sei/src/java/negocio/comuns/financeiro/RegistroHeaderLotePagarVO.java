package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

public class RegistroHeaderLotePagarVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5348867124166785995L;
	private Integer codigo;
	private String codigoBanco;
	private String loteServico;
	private Integer tipoRegistro;
	private String tipoOperacao;
	private String tipoServico;
	private String formaLancamento;
	private String numeroVersaoLote;
	private Integer tipoInscricaoEmpresa;
	private Long numeroInscricaoEmpresa;
	private String codigoConvenioBanco;
	private Integer numeroAgencia;
	private String digitoAgencia;
	private String numeroConta;
	private String digitoConta;
	private Integer digitoAgenciaConta;
	private String nomeEmpresa;
	private ControleCobrancaPagarVO controleCobrancaPagarVO;
	private List<RegistroDetalhePagarVO> listaRegistroDetalhePagarVO;
	private RegistroTrailerLotePagarVO registroTrailerLotePagarVO;
	
	
	

	public ControleCobrancaPagarVO getControleCobrancaPagarVO() {
		if (controleCobrancaPagarVO == null) {
			controleCobrancaPagarVO = new ControleCobrancaPagarVO();
		}
		return controleCobrancaPagarVO;
	}

	public void setControleCobrancaPagarVO(ControleCobrancaPagarVO controleCobrancaPagarVO) {
		this.controleCobrancaPagarVO = controleCobrancaPagarVO;
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

	public String getCodigoBanco() {
		if (codigoBanco == null) {
			codigoBanco = "";
		}
		return codigoBanco;
	}

	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
	}

	public String getLoteServico() {
		if (loteServico == null) {
			loteServico = "";
		}
		return loteServico;
	}

	public void setLoteServico(String loteServico) {
		this.loteServico = loteServico;
	}

	public Integer getTipoRegistro() {
		if (tipoRegistro == null) {
			tipoRegistro = 0;
		}
		return tipoRegistro;
	}

	public void setTipoRegistro(Integer tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getTipoOperacao() {
		if (tipoOperacao == null) {
			tipoOperacao = "";
		}
		return tipoOperacao;
	}

	public void setTipoOperacao(String tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	public String getTipoServico() {
		if (tipoServico == null) {
			tipoServico = "";
		}
		return tipoServico;
	}

	public void setTipoServico(String tipoServico) {
		this.tipoServico = tipoServico;
	}

	public String getFormaLancamento() {
		if (formaLancamento == null) {
			formaLancamento = "";
		}
		return formaLancamento;
	}

	public void setFormaLancamento(String formaLancamento) {
		this.formaLancamento = formaLancamento;
	}

	public String getNumeroVersaoLote() {
		if (numeroVersaoLote == null) {
			numeroVersaoLote = "";
		}
		return numeroVersaoLote;
	}

	public void setNumeroVersaoLote(String numeroVersaoLote) {
		this.numeroVersaoLote = numeroVersaoLote;
	}

	public Integer getTipoInscricaoEmpresa() {
		if (tipoInscricaoEmpresa == null) {
			tipoInscricaoEmpresa = 0;
		}
		return tipoInscricaoEmpresa;
	}

	public void setTipoInscricaoEmpresa(Integer tipoInscricaoEmpresa) {
		this.tipoInscricaoEmpresa = tipoInscricaoEmpresa;
	}

	public Long getNumeroInscricaoEmpresa() {
		if (numeroInscricaoEmpresa == null) {
			numeroInscricaoEmpresa = 0L;
		}
		return numeroInscricaoEmpresa;
	}

	public void setNumeroInscricaoEmpresa(Long numeroInscricaoEmpresa) {
		this.numeroInscricaoEmpresa = numeroInscricaoEmpresa;
	}

	public String getCodigoConvenioBanco() {
		if (codigoConvenioBanco == null) {
			codigoConvenioBanco = "";
		}
		return codigoConvenioBanco;
	}

	public void setCodigoConvenioBanco(String codigoConvenioBanco) {
		this.codigoConvenioBanco = codigoConvenioBanco;
	}

	public Integer getNumeroAgencia() {
		if (numeroAgencia == null) {
			numeroAgencia = 0;
		}
		return numeroAgencia;
	}

	public void setNumeroAgencia(Integer numeroAgencia) {
		this.numeroAgencia = numeroAgencia;
	}

	public String getDigitoAgencia() {
		if (digitoAgencia == null) {
			digitoAgencia = "";
		}
		return digitoAgencia;
	}

	public void setDigitoAgencia(String digitoAgencia) {
		this.digitoAgencia = digitoAgencia;
	}

	public String getNumeroConta() {
		if (numeroConta == null) {
			numeroConta = "";
		}
		return numeroConta;
	}

	public void setNumeroConta(String numeroConta) {
		this.numeroConta = numeroConta;
	}

	public String getDigitoConta() {
		if (digitoConta == null) {
			digitoConta = "";
		}
		return digitoConta;
	}

	public void setDigitoConta(String digitoConta) {
		this.digitoConta = digitoConta;
	}

	public Integer getDigitoAgenciaConta() {
		if (digitoAgenciaConta == null) {
			digitoAgenciaConta = 0;
		}
		return digitoAgenciaConta;
	}

	public void setDigitoAgenciaConta(Integer digitoAgenciaConta) {
		this.digitoAgenciaConta = digitoAgenciaConta;
	}

	public String getNomeEmpresa() {
		if (nomeEmpresa == null) {
			nomeEmpresa = "";
		}
		return nomeEmpresa;
	}

	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa;
	}

	public List<RegistroDetalhePagarVO> getListaRegistroDetalhePagarVO() {
		if (listaRegistroDetalhePagarVO == null) {
			listaRegistroDetalhePagarVO = new ArrayList<RegistroDetalhePagarVO>();
		}
		return listaRegistroDetalhePagarVO;
	}

	public void setListaRegistroDetalhePagarVO(List<RegistroDetalhePagarVO> listaRegistroDetalhePagarVO) {
		this.listaRegistroDetalhePagarVO = listaRegistroDetalhePagarVO;
	}

	public RegistroTrailerLotePagarVO getRegistroTrailerLotePagarVO() {
		if (registroTrailerLotePagarVO == null) {
			registroTrailerLotePagarVO = new RegistroTrailerLotePagarVO();
		}
		return registroTrailerLotePagarVO;
	}

	public void setRegistroTrailerLotePagarVO(RegistroTrailerLotePagarVO registroTrailerLotePagarVO) {
		this.registroTrailerLotePagarVO = registroTrailerLotePagarVO;
	}

}
