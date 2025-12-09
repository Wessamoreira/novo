package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.enumerador.SituacaoProcessamentoArquivoRetornoEnum;
import negocio.comuns.utilitarias.Uteis;

public class ControleCobrancaPagarVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3024981995736492350L;
	private Integer codigo;
	private UsuarioVO responsavel;
	private BancoVO bancoVO;
	private ContaCorrenteVO contaCorrenteVO;
	private ArquivoVO arquivoRetornoContaPagar;
	private List<RegistroHeaderLotePagarVO> listaRegistroHeaderLotePagarVO;
	private RegistroHeaderPagarVO registroHeaderPagarVO;
	private RegistroTrailerPagarVO registroTrailerPagarVO;
	private Date dataProcessamento;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private Integer qtdeLote;
	private Integer loteAtual;
	private Date dataInicioProcessamento;
	private Date dataTerminoProcessamento;
	private SituacaoProcessamentoArquivoRetornoEnum situacaoProcessamento;
	private String motivoErroProcessamento;
	private List<ContaPagarRegistroArquivoVO> contaPagarRegistroArquivoVOs;
	/**
	 * Transient
	 */
	private List<ContaCorrenteVO> listaContaCorrenteArquivoRetorno;
	private List<ContaPagarHistoricoVO> listaContaPagarHistorico;
	
	
	
	

	public List<ContaPagarHistoricoVO> getListaContaPagarHistorico() {
		if (listaContaPagarHistorico == null) {
			listaContaPagarHistorico = new ArrayList<ContaPagarHistoricoVO>();
		}
		return listaContaPagarHistorico;
	}

	public void setListaContaPagarHistorico(List<ContaPagarHistoricoVO> listaContaPagarHistorico) {
		this.listaContaPagarHistorico = listaContaPagarHistorico;
	}

	public List<ContaPagarRegistroArquivoVO> getContaPagarRegistroArquivoVOs() {
		if (contaPagarRegistroArquivoVOs == null) {
			contaPagarRegistroArquivoVOs = new ArrayList<ContaPagarRegistroArquivoVO>();
		}
		return contaPagarRegistroArquivoVOs;
	}

	public void setContaPagarRegistroArquivoVOs(List<ContaPagarRegistroArquivoVO> contaPagarRegistroArquivoVOs) {
		this.contaPagarRegistroArquivoVOs = contaPagarRegistroArquivoVOs;
	}

	public List<RegistroHeaderLotePagarVO> getListaRegistroHeaderLotePagarVO() {
		if (listaRegistroHeaderLotePagarVO == null) {
			listaRegistroHeaderLotePagarVO = new ArrayList<RegistroHeaderLotePagarVO>();
		}
		return listaRegistroHeaderLotePagarVO;
	}

	public void setListaRegistroHeaderLotePagarVO(List<RegistroHeaderLotePagarVO> listaRegistroHeaderLotePagarVO) {
		this.listaRegistroHeaderLotePagarVO = listaRegistroHeaderLotePagarVO;
	}

	public RegistroHeaderPagarVO getRegistroHeaderPagarVO() {
		if (registroHeaderPagarVO == null) {
			registroHeaderPagarVO = new RegistroHeaderPagarVO();
		}
		return registroHeaderPagarVO;
	}

	public void setRegistroHeaderPagarVO(RegistroHeaderPagarVO registroHeaderPagarVO) {
		this.registroHeaderPagarVO = registroHeaderPagarVO;
	}

	public RegistroTrailerPagarVO getRegistroTrailerPagarVO() {
		if (registroTrailerPagarVO == null) {
			registroTrailerPagarVO = new RegistroTrailerPagarVO();
		}
		return registroTrailerPagarVO;
	}

	public void setRegistroTrailerPagarVO(RegistroTrailerPagarVO registroTrailerPagarVO) {
		this.registroTrailerPagarVO = registroTrailerPagarVO;
	}

	public ArquivoVO getArquivoRetornoContaPagar() {
		if (arquivoRetornoContaPagar == null) {
			arquivoRetornoContaPagar = new ArquivoVO();
		}
		return arquivoRetornoContaPagar;
	}

	public void setArquivoRetornoContaPagar(ArquivoVO arquivoRetornoContaPagar) {
		this.arquivoRetornoContaPagar = arquivoRetornoContaPagar;
	}

	public BancoVO getBancoVO() {
		if (bancoVO == null) {
			bancoVO = new BancoVO();
		}
		return bancoVO;
	}

	public void setBancoVO(BancoVO bancoVO) {
		this.bancoVO = bancoVO;
	}

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return (responsavel);
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
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

	public String getNomeArquivo_Apresentar() {
		return Uteis.getNomeArquivo(getArquivoRetornoContaPagar().getDescricao());
	}

	public Date getDataProcessamento() {
		return dataProcessamento;
	}

	public void setDataProcessamento(Date dataProcessamento) {
		this.dataProcessamento = dataProcessamento;
	}

	public String getDataProcessamento_Apresentar() {
		return Uteis.getDataAno4Digitos(getDataProcessamento());
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

	public Integer getQtdeLote() {
		if (qtdeLote == null) {
			qtdeLote = 0;
		}
		return qtdeLote;
	}

	public void setQtdeLote(Integer qtdeLote) {
		this.qtdeLote = qtdeLote;
	}

	public Integer getLoteAtual() {
		if (loteAtual == null) {
			loteAtual = 0;
		}
		return loteAtual;
	}

	public void setLoteAtual(Integer loteAtual) {
		this.loteAtual = loteAtual;
	}

	public Date getDataInicioProcessamento() {
		return dataInicioProcessamento;
	}

	public void setDataInicioProcessamento(Date dataInicioProcessamento) {
		this.dataInicioProcessamento = dataInicioProcessamento;
	}

	public Date getDataTerminoProcessamento() {
		return dataTerminoProcessamento;
	}

	public void setDataTerminoProcessamento(Date dataTerminoProcessamento) {
		this.dataTerminoProcessamento = dataTerminoProcessamento;
	}

	public SituacaoProcessamentoArquivoRetornoEnum getSituacaoProcessamento() {
		if (situacaoProcessamento == null) {
			situacaoProcessamento = SituacaoProcessamentoArquivoRetornoEnum.AGUARDANDO_PROCESSAMENTO;
		}
		return situacaoProcessamento;
	}

	public void setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum situacaoProcessamento) {
		this.situacaoProcessamento = situacaoProcessamento;
	}

	public String getMotivoErroProcessamento() {
		if (motivoErroProcessamento == null) {
			motivoErroProcessamento = "";
		}
		return motivoErroProcessamento;
	}

	public void setMotivoErroProcessamento(String motivoErroProcessamento) {
		this.motivoErroProcessamento = motivoErroProcessamento;
	}

	public ContaCorrenteVO getContaCorrenteVO() {
		if (contaCorrenteVO == null) {
			contaCorrenteVO = new ContaCorrenteVO();
		}
		return contaCorrenteVO;
	}

	public void setContaCorrenteVO(ContaCorrenteVO contaCorrenteVO) {
		this.contaCorrenteVO = contaCorrenteVO;
	}

	public List<ContaCorrenteVO> getListaContaCorrenteArquivoRetorno() {
		if (listaContaCorrenteArquivoRetorno == null) {
			listaContaCorrenteArquivoRetorno = new ArrayList<ContaCorrenteVO>();
		}
		return listaContaCorrenteArquivoRetorno;
	}

	public void setListaContaCorrenteArquivoRetorno(List<ContaCorrenteVO> listaContaCorrenteArquivoRetorno) {
		this.listaContaCorrenteArquivoRetorno = listaContaCorrenteArquivoRetorno;
	}

	public String getProgressBar() {
		if (getQtdeLote() == 0) {
			return "Iniciando Processamento...";
		}
		return "Processando lote " + getLoteAtual() + " de " + getQtdeLote();
	}

}
