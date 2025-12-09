package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.enumerador.MensagensErroRetornoRemessaPagarEnum;
import negocio.comuns.utilitarias.Uteis;

public class ContaPagarRegistroArquivoVO extends SuperVO implements Comparable<ContaPagarRegistroArquivoVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1143501681297442532L;
	private Integer codigo;
	private ContaPagarVO contaPagarVO;
	private ControleCobrancaPagarVO controleCobrancaPagarVO;
	private RegistroDetalhePagarVO registroDetalhePagarVO;
	private String motivoRejeicao;
	private List<String> listaMotivoRejeicao;

	/**
	 * Utilizado no momento do processamento do arquivo de retorno;
	 */
	private Boolean contaPagarDuplicidade;
	private Integer diasVariacaoDataVencimento;
	private Boolean valorPagamentoDivergente;

	public ContaPagarRegistroArquivoVO() {
		super();
	}

	public ContaPagarRegistroArquivoVO(ContaPagarVO contaPagarVO, ControleCobrancaPagarVO controleCobrancaPagarVO, RegistroDetalhePagarVO registroDetalhePagarVO ,Boolean valorPagamentoDivergente ) {
		super();
		this.contaPagarVO = contaPagarVO;
		this.controleCobrancaPagarVO = controleCobrancaPagarVO;
		this.registroDetalhePagarVO = registroDetalhePagarVO;
		this.motivoRejeicao = registroDetalhePagarVO.getMotivoRegeicao();		
		if(this.motivoRejeicao != null && !this.motivoRejeicao.isEmpty()){
            this.listaMotivoRejeicao = MensagensErroRetornoRemessaPagarEnum.getMensagem(this.motivoRejeicao, registroDetalhePagarVO.getCodigoBanco().toString());
		}		
		this.valorPagamentoDivergente= valorPagamentoDivergente ;
	}

	public String getFavorecido_Apresentar() {
		if (Uteis.isAtributoPreenchido(getContaPagarVO())) {
			return getContaPagarVO().getFavorecido_Apresentar();
		}
		return getRegistroDetalhePagarVO().getNomeFavorecido();
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

	public ContaPagarVO getContaPagarVO() {
		if (contaPagarVO == null) {
			contaPagarVO = new ContaPagarVO();
		}
		return contaPagarVO;
	}

	public void setContaPagarVO(ContaPagarVO contaPagarVO) {
		this.contaPagarVO = contaPagarVO;
	}

	public ControleCobrancaPagarVO getControleCobrancaPagarVO() {
		if (controleCobrancaPagarVO == null) {
			controleCobrancaPagarVO = new ControleCobrancaPagarVO();
		}
		return controleCobrancaPagarVO;
	}

	public void setControleCobrancaPagarVO(ControleCobrancaPagarVO controleCobrancaPagarVO) {
		this.controleCobrancaPagarVO = controleCobrancaPagarVO;
	}

	public RegistroDetalhePagarVO getRegistroDetalhePagarVO() {
		if (registroDetalhePagarVO == null) {
			registroDetalhePagarVO = new RegistroDetalhePagarVO();
		}
		return registroDetalhePagarVO;
	}

	public void setRegistroDetalhePagarVO(RegistroDetalhePagarVO registroDetalhePagarVO) {
		this.registroDetalhePagarVO = registroDetalhePagarVO;
	}

	public String getMotivoRejeicao_Apresentar() {
		StringBuilder sb = new StringBuilder();
		for (String string : getListaMotivoRejeicao()) {
			sb.append(" * ").append(string).append(" </br> ");
		}
		return sb.toString();
	}
	
	public String getMotivoRejeicao() {
		if (motivoRejeicao == null) {
			motivoRejeicao = "";
		}
		return motivoRejeicao;
	}

	public void setMotivoRejeicao(String motivoRejeicao) {
		this.motivoRejeicao = motivoRejeicao;
	}
	
	public boolean isContaPagarEfetivado(){
		return getMotivoRejeicao().length() >= 2 && getMotivoRejeicao().substring(0, 2).equals(MensagensErroRetornoRemessaPagarEnum.OCORRENCIA_00.getCodigoRejeicao());
	}

	public List<String> getListaMotivoRejeicao() {
		if (listaMotivoRejeicao == null) {
			listaMotivoRejeicao = new ArrayList<String>();

		}
		return listaMotivoRejeicao;
	}

	public void setListaMotivoRejeicao(List<String> motivoRejeicao_Apresentar) {
		this.listaMotivoRejeicao = motivoRejeicao_Apresentar;
	}

	public Boolean getContaPagarDuplicidade() {
		if (contaPagarDuplicidade == null) {
			contaPagarDuplicidade = false;
		}
		return contaPagarDuplicidade;
	}

	public void setContaPagarDuplicidade(Boolean contaPagarDuplicidade) {
		this.contaPagarDuplicidade = contaPagarDuplicidade;
	}

	public Integer getDiasVariacaoDataVencimento() {
		if (diasVariacaoDataVencimento == null) {
			diasVariacaoDataVencimento = 0;
		}
		return diasVariacaoDataVencimento;
	}

	public void setDiasVariacaoDataVencimento(Integer diasVariacaoDataVencimento) {
		this.diasVariacaoDataVencimento = diasVariacaoDataVencimento;
	}

	@Override
	public int compareTo(ContaPagarRegistroArquivoVO o) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	

	public Boolean getValorPagamentoDivergente() {
		if(valorPagamentoDivergente == null ) {
			valorPagamentoDivergente = Boolean.FALSE;
		}
		return valorPagamentoDivergente;
	}

	public void setValorPagamentoDivergente(Boolean valorPagamentoDivergente) {
		this.valorPagamentoDivergente = valorPagamentoDivergente;
	}
	
	
	

}
