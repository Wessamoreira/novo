package relatorio.negocio.comuns.financeiro;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ParceiroVO;

public class DeclaracaoImpostoRendaRelVO extends SuperVO {

	private UnidadeEnsinoVO unidadeEnsino;
	private MatriculaVO matricula;
	private ContaReceberVO contaReceber;
	private String nomeResponsavel;
	private String cpfResponsavel;
	private String rgResponsavel;
	private Integer codigoResponsavel;
	private String ano;
	private Boolean utilizarDataVencimentoParaDataRecebimento;
	private Boolean tipoOrigemInscricaoProcessoSeletivo;
	private Boolean tipoOrigemMatricula;
	private Boolean tipoOrigemRequerimento;
	private Boolean tipoOrigemBiblioteca;
	private Boolean tipoOrigemMensalidade;
	private Boolean tipoOrigemDevolucaoCheque;
	private Boolean tipoOrigemNegociacao;
	private Boolean tipoOrigemBolsaCusteadaConvenio;
	private Boolean tipoOrigemContratoReceita;
	private Boolean tipoOrigemOutros;
	private Boolean tipoOrigemInclusaoReposicao;
	private Boolean tipoOrigemMaterialDidatico;
	private ParceiroVO parceiro;
	private PessoaVO pessoa;
	private String parcela;
	private String servico;
	private UnidadeEnsinoVO unidadeEnsinoFinanceira;
	
	private Boolean layoutPadrao;
	private String tipoOrigem;

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}

	public ContaReceberVO getContaReceber() {
		if (contaReceber == null) {
			contaReceber = new ContaReceberVO();
		}
		return contaReceber;
	}

	public void setContaReceber(ContaReceberVO contaReceber) {
		this.contaReceber = contaReceber;
	}

	public String getNomeResponsavel() {
		if (nomeResponsavel == null) {
			nomeResponsavel = "";
		}
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public String getCpfResponsavel() {
		if (cpfResponsavel == null) {
			cpfResponsavel = "";
		}
		return cpfResponsavel;
	}

	public void setCpfResponsavel(String cpfResponsavel) {
		this.cpfResponsavel = cpfResponsavel;
	}

	public String getRgResponsavel() {
		if (rgResponsavel == null) {
			rgResponsavel = "";
		}
		return rgResponsavel;
	}

	public void setRgResponsavel(String rgResponsavel) {
		this.rgResponsavel = rgResponsavel;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public Boolean getTipoOrigemInscricaoProcessoSeletivo() {
		if (tipoOrigemInscricaoProcessoSeletivo == null) {
			tipoOrigemInscricaoProcessoSeletivo = false;
		}
		return tipoOrigemInscricaoProcessoSeletivo;
	}

	public void setTipoOrigemInscricaoProcessoSeletivo(Boolean tipoOrigemInscricaoProcessoSeletivo) {
		this.tipoOrigemInscricaoProcessoSeletivo = tipoOrigemInscricaoProcessoSeletivo;
	}

	public Boolean getTipoOrigemMatricula() {
		if (tipoOrigemMatricula == null) {
			tipoOrigemMatricula = false;
		}
		return tipoOrigemMatricula;
	}

	public void setTipoOrigemMatricula(Boolean tipoOrigemMatricula) {
		this.tipoOrigemMatricula = tipoOrigemMatricula;
	}

	public Boolean getTipoOrigemRequerimento() {
		if (tipoOrigemRequerimento == null) {
			tipoOrigemRequerimento = false;
		}
		return tipoOrigemRequerimento;
	}

	public void setTipoOrigemRequerimento(Boolean tipoOrigemRequerimento) {
		this.tipoOrigemRequerimento = tipoOrigemRequerimento;
	}

	public Boolean getTipoOrigemBiblioteca() {
		if (tipoOrigemBiblioteca == null) {
			tipoOrigemBiblioteca = false;
		}
		return tipoOrigemBiblioteca;
	}

	public void setTipoOrigemBiblioteca(Boolean tipoOrigemBiblioteca) {
		this.tipoOrigemBiblioteca = tipoOrigemBiblioteca;
	}

	public Boolean getTipoOrigemMensalidade() {
		if (tipoOrigemMensalidade == null) {
			tipoOrigemMensalidade = false;
		}
		return tipoOrigemMensalidade;
	}

	public void setTipoOrigemMensalidade(Boolean tipoOrigemMensalidade) {
		this.tipoOrigemMensalidade = tipoOrigemMensalidade;
	}

	public Boolean getTipoOrigemDevolucaoCheque() {
		if (tipoOrigemDevolucaoCheque == null) {
			tipoOrigemDevolucaoCheque = false;
		}
		return tipoOrigemDevolucaoCheque;
	}

	public void setTipoOrigemDevolucaoCheque(Boolean tipoOrigemDevolucaoCheque) {
		this.tipoOrigemDevolucaoCheque = tipoOrigemDevolucaoCheque;
	}

	public Boolean getTipoOrigemNegociacao() {
		if (tipoOrigemNegociacao == null) {
			tipoOrigemNegociacao = false;
		}
		return tipoOrigemNegociacao;
	}

	public void setTipoOrigemNegociacao(Boolean tipoOrigemNegociacao) {
		this.tipoOrigemNegociacao = tipoOrigemNegociacao;
	}

	public Boolean getTipoOrigemBolsaCusteadaConvenio() {
		if (tipoOrigemBolsaCusteadaConvenio == null) {
			tipoOrigemBolsaCusteadaConvenio = false;
		}
		return tipoOrigemBolsaCusteadaConvenio;
	}

	public void setTipoOrigemBolsaCusteadaConvenio(Boolean tipoOrigemBolsaCusteadaConvenio) {
		this.tipoOrigemBolsaCusteadaConvenio = tipoOrigemBolsaCusteadaConvenio;
	}

	public Boolean getTipoOrigemContratoReceita() {
		if (tipoOrigemContratoReceita == null) {
			tipoOrigemContratoReceita = false;
		}
		return tipoOrigemContratoReceita;
	}

	public void setTipoOrigemContratoReceita(Boolean tipoOrigemContratoReceita) {
		this.tipoOrigemContratoReceita = tipoOrigemContratoReceita;
	}

	public Boolean getTipoOrigemOutros() {
		if (tipoOrigemOutros == null) {
			tipoOrigemOutros = false;
		}
		return tipoOrigemOutros;
	}

	public void setTipoOrigemOutros(Boolean tipoOrigemOutros) {
		this.tipoOrigemOutros = tipoOrigemOutros;
	}

	public Boolean getTipoOrigemInclusaoReposicao() {
		if (tipoOrigemInclusaoReposicao == null) {
			tipoOrigemInclusaoReposicao = false;
		}
		return tipoOrigemInclusaoReposicao;
	}

	public void setTipoOrigemInclusaoReposicao(Boolean tipoOrigemInclusaoReposicao) {
		this.tipoOrigemInclusaoReposicao = tipoOrigemInclusaoReposicao;
	}

	public String getParcela() {
		if (parcela == null) {
			parcela = "";
		}
		return parcela;
	}

	public void setParcela(String parcela) {
		this.parcela = parcela;
	}

	public String getServico() {
		if (servico == null) {
			servico = "";
		}
		return servico;
	}

	public void setServico(String servico) {
		this.servico = servico;
	}

	public Boolean getUtilizarDataVencimentoParaDataRecebimento() {
		if (utilizarDataVencimentoParaDataRecebimento == null) {
			utilizarDataVencimentoParaDataRecebimento = false;
		}
		return utilizarDataVencimentoParaDataRecebimento;
	}

	public void setUtilizarDataVencimentoParaDataRecebimento(Boolean utilizarDataVencimentoParaDataRecebimento) {
		this.utilizarDataVencimentoParaDataRecebimento = utilizarDataVencimentoParaDataRecebimento;
	}

	public ParceiroVO getParceiro() {
		if (parceiro == null) {
			parceiro = new ParceiroVO();
		}
		return parceiro;
	}

	public void setParceiro(ParceiroVO parceiro) {
		this.parceiro = parceiro;
	}
	
	
	public PessoaVO getPessoa() {
		if (pessoa == null) {
			pessoa = new PessoaVO();
		}
		return pessoa;
	}

	public void setPessoa(PessoaVO pessoa) {
		this.pessoa = pessoa;
	}

	public Integer getCodigoResponsavel() {
		if (codigoResponsavel == null) {
			codigoResponsavel = 0;
		}
		return codigoResponsavel;
	}

	public void setCodigoResponsavel(Integer codigoResponsavel) {
		this.codigoResponsavel = codigoResponsavel;
	}

	public Boolean getTipoOrigemMaterialDidatico() {
		if(tipoOrigemMaterialDidatico == null) {
			tipoOrigemMaterialDidatico = false;
		}
		return tipoOrigemMaterialDidatico;
	}

	public void setTipoOrigemMaterialDidatico(Boolean tipoOrigemMaterialDidatico) {
		this.tipoOrigemMaterialDidatico = tipoOrigemMaterialDidatico;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoFinanceira() {
		if(unidadeEnsinoFinanceira == null) {
			unidadeEnsinoFinanceira = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoFinanceira;
	}

	public void setUnidadeEnsinoFinanceira(UnidadeEnsinoVO unidadeEnsinoFinanceira) {
		this.unidadeEnsinoFinanceira = unidadeEnsinoFinanceira;
	}

	public Boolean getLayoutPadrao() {
		if(layoutPadrao == null) {
			layoutPadrao = false;
		}
		return layoutPadrao;
	}

	public void setLayoutPadrao(Boolean layoutPadrao) {
		this.layoutPadrao = layoutPadrao;
	}

	public String getTipoOrigem() {
		if(tipoOrigem == null) {
			tipoOrigem = "";
		}
		return tipoOrigem;
	}

	public void setTipoOrigem(String tipoOrigem) {
		this.tipoOrigem = tipoOrigem;
	}
	
	
	
}
