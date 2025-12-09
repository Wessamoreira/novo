package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaEnum;
import negocio.comuns.utilitarias.Uteis;

public class ControleRemessaContaPagarVO extends SuperVO {

	private Integer codigo;
	private Date dataGeracao;
	private Date dataInicio;
	private Date dataFim;
	private UsuarioVO responsavel;
	private ArquivoVO arquivoRemessaContaPagar;
	private BancoVO bancoVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private ContaCorrenteVO contaCorrenteVO;
	private Integer numeroIncremental;
	private Integer numeroSegmentoIncremental;
	private String tipoRemessa;
	private SituacaoControleRemessaEnum situacaoControleRemessa;
	private Double valorTotalRemessa;
	//Campo incrementar para remessa conta receber.
	private Integer incrementalMX;
	//Campo incremental para remessa conta pagar(codigo MX).
	private Integer incrementalMXCP;
	private ArquivoVO arquivoRemessaPixContaPagar;
	public static final long serialVersionUID = 1L;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDataGeracao_Apresentar() {
		return Uteis.getData(getDataGeracao());
	}

	public Date getDataGeracao() {
		if (dataGeracao == null) {
			dataGeracao = new Date();
		}
		return dataGeracao;
	}

	public void setDataGeracao(Date dataGeracao) {
		this.dataGeracao = dataGeracao;
	}

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
		}
		return dataInicio;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = Uteis.getDataUltimoDiaMes(new Date());
		}
		return dataFim;
	}

	public void setArquivoRemessaContaPagar(ArquivoVO arquivoRemessaContaPagar) {
		this.arquivoRemessaContaPagar = arquivoRemessaContaPagar;
	}

	public ArquivoVO getArquivoRemessaContaPagar() {
		if (arquivoRemessaContaPagar == null) {
			arquivoRemessaContaPagar = new ArquivoVO();
		}
		return arquivoRemessaContaPagar;
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

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
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

	public Integer getNumeroIncremental() {
		if (numeroIncremental == null) {
			numeroIncremental = 0;
		}
		return numeroIncremental;
	}

	public void setNumeroIncremental(Integer numeroIncremental) {
		this.numeroIncremental = numeroIncremental;
	}

	public Integer getNumeroSegmentoIncremental() {
		if (numeroSegmentoIncremental == null) {
			numeroSegmentoIncremental = 0;
		}
		return numeroSegmentoIncremental;
	}

	public void setNumeroSegmentoIncremental(Integer numeroSegmentoIncremental) {
		this.numeroSegmentoIncremental = numeroSegmentoIncremental;
	}

	public Integer getIncrementalMX() {
		if (incrementalMX == null) {
			incrementalMX = 0;
		}
		return incrementalMX;
	}

	public void setIncrementalMX(Integer incrementalMX) {
		this.incrementalMX = incrementalMX;
	}
	
	public String getTipoRemessa_Apresentar() {
		if (getTipoRemessa().equals("RE")) {
			return "Registro de Conta";
		} else {
			return "Atualização de Conta";
		}
	}

	public String getTipoRemessa() {
		if (tipoRemessa == null) {
			tipoRemessa = "RE";
		}
		return tipoRemessa;
	}

	public void setTipoRemessa(String tipoRemessa) {
		this.tipoRemessa = tipoRemessa;
	}

	public SituacaoControleRemessaEnum getSituacaoControleRemessa() {
		if (situacaoControleRemessa == null) {
			situacaoControleRemessa = SituacaoControleRemessaEnum.AGUARDANDO_PROCESSAMENTO_RETORNO_REMESSA;
		}
		return situacaoControleRemessa;
	}

	public void setSituacaoControleRemessa(SituacaoControleRemessaEnum situacaoControleRemessa) {
		this.situacaoControleRemessa = situacaoControleRemessa;
	}

	public Double getValorTotalRemessa() {
		if (valorTotalRemessa == null) {
			valorTotalRemessa = 0.0;
		}
		return valorTotalRemessa;
	}

	public void setValorTotalRemessa(Double valorTotalRemessa) {
		this.valorTotalRemessa = valorTotalRemessa;
	}

	public Integer getIncrementalMXCP() {
		if (incrementalMXCP == null) {
			incrementalMXCP = 0;
		}
		return incrementalMXCP;
	}

	public void setIncrementalMXCP(Integer incrementalMXCP) {
		this.incrementalMXCP = incrementalMXCP;
	}	

	public void setArquivoRemessaPixContaPagar(ArquivoVO arquivoRemessaPixContaPagar) {
		this.arquivoRemessaPixContaPagar = arquivoRemessaPixContaPagar;
	}

	public ArquivoVO getArquivoRemessaPixContaPagar() {
		if (arquivoRemessaPixContaPagar == null) {
			arquivoRemessaPixContaPagar = new ArquivoVO();
		}
		return arquivoRemessaPixContaPagar;
	}

}
