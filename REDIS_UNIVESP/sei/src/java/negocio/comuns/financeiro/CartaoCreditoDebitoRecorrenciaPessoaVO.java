package negocio.comuns.financeiro;

import java.io.Serializable;
import java.util.Date;

import com.amazonaws.services.ec2.model.Status;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.enumerador.FormaPadraoDataBaseCartaoRecorrenteEnum;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

public class CartaoCreditoDebitoRecorrenciaPessoaVO extends SuperVO implements Serializable {

	private Integer codigo;
	private PessoaVO pessoaVO;
	private MatriculaVO matriculaVO;
	private PessoaVO responsavelFinanceiro;
	private OperadoraCartaoVO operadoraCartaoVO;
	private String numeroCartao;
	private String numeroCartaoMascarado;
	private String nomeCartao;
	private Integer mesValidade;
	private Integer anoValidade;
	private String codigoSeguranca;
	private FormaPadraoDataBaseCartaoRecorrenteEnum formaPadraoDataBaseCartaoRecorrente;
	private Integer diaPagamentoPadrao;
	private SituacaoEnum situacao;
	private Date dataCadastro;
	
	
	private static final long serialVersionUID = 1L;
	
	public CartaoCreditoDebitoRecorrenciaPessoaVO() {
		
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

	public PessoaVO getPessoaVO() {
		if (pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public PessoaVO getResponsavelFinanceiro() {
		if (responsavelFinanceiro == null) {
			responsavelFinanceiro = new PessoaVO();
		}
		return responsavelFinanceiro;
	}

	public void setResponsavelFinanceiro(PessoaVO responsavelFinanceiro) {
		this.responsavelFinanceiro = responsavelFinanceiro;
	}

	public OperadoraCartaoVO getOperadoraCartaoVO() {
		if (operadoraCartaoVO == null) {
			operadoraCartaoVO = new OperadoraCartaoVO();
		}
		return operadoraCartaoVO;
	}

	public void setOperadoraCartaoVO(OperadoraCartaoVO operadoraCartaoVO) {
		this.operadoraCartaoVO = operadoraCartaoVO;
	}

	public String getNumeroCartao() {
		if (numeroCartao == null) {
			numeroCartao = "";
		}
		return numeroCartao;
	}

	public void setNumeroCartao(String numeroCartao) {
		this.numeroCartao = numeroCartao;
	}

	public String getNumeroCartaoMascarado() {
		if (numeroCartaoMascarado == null) {
			numeroCartaoMascarado = "";
		}
		return numeroCartaoMascarado;
	}

	public void setNumeroCartaoMascarado(String numeroCartaoMascarado) {
		this.numeroCartaoMascarado = numeroCartaoMascarado;
	}

	public String getNomeCartao() {
		if (nomeCartao == null) {
			nomeCartao = "";
		}
		return nomeCartao.toUpperCase();
	}

	public void setNomeCartao(String nomeCartao) {
		this.nomeCartao = nomeCartao;
	}

	public String getCodigoSeguranca() {
		if (codigoSeguranca == null) {
			codigoSeguranca = "";
		}
		return codigoSeguranca;
	}

	public void setCodigoSeguranca(String codigoSeguranca) {
		this.codigoSeguranca = codigoSeguranca;
	}

	public FormaPadraoDataBaseCartaoRecorrenteEnum getFormaPadraoDataBaseCartaoRecorrente() {
		if (formaPadraoDataBaseCartaoRecorrente == null) {
			formaPadraoDataBaseCartaoRecorrente = FormaPadraoDataBaseCartaoRecorrenteEnum.DIA_FIXO;
		}
		return formaPadraoDataBaseCartaoRecorrente;
	}

	public void setFormaPadraoDataBaseCartaoRecorrente(FormaPadraoDataBaseCartaoRecorrenteEnum formaPadraoDataBaseCartaoRecorrente) {
		this.formaPadraoDataBaseCartaoRecorrente = formaPadraoDataBaseCartaoRecorrente;
	}

	public Integer getDiaPagamentoPadrao() {
		if (diaPagamentoPadrao == null) {
			diaPagamentoPadrao = 0;
		}
		return diaPagamentoPadrao;
	}

	public void setDiaPagamentoPadrao(Integer diaPagamentoPadrao) {
		this.diaPagamentoPadrao = diaPagamentoPadrao;
	}

	public Integer getMesValidade() {
		if (mesValidade == null) {
			mesValidade = 0;
		}
		return mesValidade;
	}

	public void setMesValidade(Integer mesValidade) {
		this.mesValidade = mesValidade;
	}

	public Integer getAnoValidade() {
		if (anoValidade == null) {
			anoValidade = 0;
		}
		return anoValidade;
	}

	public void setAnoValidade(Integer anoValidade) {
		this.anoValidade = anoValidade;
	}

	public SituacaoEnum getSituacao() {
		if (situacao == null) {
			situacao = SituacaoEnum.ATIVO;
		}
		return situacao;
	}

	public void setSituacao(SituacaoEnum situacao) {
		this.situacao = situacao;
	}
	
	public Date getDataCadastro() {
		if (dataCadastro == null) {
			dataCadastro = new Date();
		}
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}


	public String getDataCadastro_ApresentarComHora() {
		return UteisData.getDataComHora(getDataCadastro());
	}

	

}
