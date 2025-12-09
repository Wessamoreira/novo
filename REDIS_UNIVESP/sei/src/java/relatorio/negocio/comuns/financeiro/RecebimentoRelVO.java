package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class RecebimentoRelVO {

	private String tipoPessoa;
	private Date data;
	private String tipoOrigem;
	private String nrDocumento;
	private Double valor;
	private Double valorRecebido;
	private Double multa;
	private Double juro;
	private Double desconto;
	private String parcela;
	private String numeroContaCorrente;
	private String digito;
	private String nomePessoa;
	private String nomeParceiro;
	private String identificadorTurma;
	private String nomeResponsavel;
	private String recebimentoBancario;
	private Date dataInicio;
	private Date dataFim;
	private String nomeUnidadeEnsino;
	private Double acrescimo;
	private Double valorTotalRecebido;
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
	
    private String recebimentoBoleto;
	
	private List<ContasRecebimentoRelVO> contasRecebimentoRelVOs;
	private Date dataVencimento;
	private String tipoCalouroVeterano;
	private Date dataInicioCompetencia;
	private Date dataFimCompetencia;
	private Double valorTotalRecebidoCalouro;
	private Double valorTotalRecebidoVeterano;
	private List<CentroResultadoOrigemVO> centroResultadoOrigemVOs;
	private String cpf;
	
	
	public JRDataSource getContasRecebimento() {
        return new JRBeanArrayDataSource(getContasRecebimentoRelVOs().toArray());
    }

	public String getTipoPessoa() {
		if (tipoPessoa == null) {
			tipoPessoa = "";
		}
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public Date getData() {
		if (data == null) {
			data = new Date();
		}
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getTipoOrigem() {
		if (tipoOrigem == null) {
			tipoOrigem = "";
		}
		return tipoOrigem;
	}

	public void setTipoOrigem(String tipoOrigem) {
		this.tipoOrigem = tipoOrigem;
	}

	public String getNrDocumento() {
		if (nrDocumento == null) {
			nrDocumento = "";
		}
		return nrDocumento;
	}

	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}

	public Double getValor() {
		if (valor == null) {
			valor = 0.0;
		}
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Double getValorRecebido() {
		if (valorRecebido == null) {
			valorRecebido = 0.0;
		}
		return valorRecebido;
	}

	public void setValorRecebido(Double valorRecebido) {
		this.valorRecebido = valorRecebido;
	}

	public Double getMulta() {
		if (multa == null) {
			multa = 0.0;
		}
		return multa;
	}

	public void setMulta(Double multa) {
		this.multa = multa;
	}

	public Double getJuro() {
		if (juro == null) {
			juro = 0.0;
		}
		return juro;
	}

	public void setJuro(Double juro) {
		this.juro = juro;
	}

	public Double getDesconto() {
		if (desconto == null) {
			desconto = 0.0;
		}
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
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

	public String getNumeroContaCorrente() {
		if (numeroContaCorrente == null) {
			numeroContaCorrente = "";
		}
		return numeroContaCorrente;
	}

	public void setNumeroContaCorrente(String numeroContaCorrente) {
		this.numeroContaCorrente = numeroContaCorrente;
	}

	public String getDigito() {
		if (digito == null) {
			digito = "";
		}
		return digito;
	}

	public void setDigito(String digito) {
		this.digito = digito;
	}

	public String getNomePessoa() {
		if (nomePessoa == null) {
			nomePessoa = "";
		}
		return nomePessoa;
	}

	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
	}

	public String getNomeParceiro() {
		if (nomeParceiro == null) {
			nomeParceiro = "";
		}
		return nomeParceiro;
	}

	public void setNomeParceiro(String nomeParceiro) {
		this.nomeParceiro = nomeParceiro;
	}

	public String getIdentificadorTurma() {
		if (identificadorTurma == null) {
			identificadorTurma = "";
		}
		return identificadorTurma;
	}

	public void setIdentificadorTurma(String identificadorTurma) {
		this.identificadorTurma = identificadorTurma;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public String getNomeResponsavel() {
		if (nomeResponsavel == null) {
			nomeResponsavel = "";
		}
		return nomeResponsavel;
	}

	public void setRecebimentoBancario(String recebimentoBancario) {
		this.recebimentoBancario = recebimentoBancario;
	}

	public String getRecebimentoBancario() {
		if (recebimentoBancario == null) {
			recebimentoBancario = "";
		}
		return recebimentoBancario;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getNomeUnidadeEnsino() {
		if (nomeUnidadeEnsino == null) {
			nomeUnidadeEnsino = "";
		}
		return nomeUnidadeEnsino;
	}

	public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
		this.nomeUnidadeEnsino = nomeUnidadeEnsino;
	}

	public void setContasRecebimentoRelVOs(List<ContasRecebimentoRelVO> contasRecebimentoRelVOs) {
		this.contasRecebimentoRelVOs = contasRecebimentoRelVOs;
	}

	public List<ContasRecebimentoRelVO> getContasRecebimentoRelVOs() {
		if (contasRecebimentoRelVOs == null) {
			contasRecebimentoRelVOs = new ArrayList<ContasRecebimentoRelVO>(0);
		}
		return contasRecebimentoRelVOs;
	}

	public void setAcrescimo(Double acrescimo) {
		this.acrescimo = acrescimo;
	}

	public Double getAcrescimo() {
		if (acrescimo == null) {
			acrescimo = 0.0;
		}
		return acrescimo;
	}

	public void setValorTotalRecebido(Double valorTotalRecebido) {
		this.valorTotalRecebido = valorTotalRecebido;
	}

	public Double getValorTotalRecebido() {
		if (valorTotalRecebido == null) {
			valorTotalRecebido = 0.0;
		}
		return valorTotalRecebido;
	}

    /**
     * @return the recebimentoBoleto
     */
    public String getRecebimentoBoleto() {
        if(recebimentoBoleto == null) {
            recebimentoBoleto = "";
        }
        return recebimentoBoleto;
    }

    /**
     * @param recebimentoBoleto the recebimentoBoleto to set
     */
    public void setRecebimentoBoleto(String recebimentoBoleto) {
        this.recebimentoBoleto = recebimentoBoleto;
    }	

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
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
			tipoOrigemMatricula = true;
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
			tipoOrigemMensalidade = true;
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
	
	public String getTipoCalouroVeterano() {
		if (tipoCalouroVeterano == null) {
			tipoCalouroVeterano = "";
		}
		return tipoCalouroVeterano;
	}

	public void setTipoCalouroVeterano(String tipoCalouroVeterano) {
		this.tipoCalouroVeterano = tipoCalouroVeterano;
	}

	public Date getDataInicioCompetencia() {
		return dataInicioCompetencia;
	}

	public void setDataInicioCompetencia(Date dataInicioCompetencia) {
		this.dataInicioCompetencia = dataInicioCompetencia;
	}

	public Date getDataFimCompetencia() {
		return dataFimCompetencia;
	}

	public void setDataFimCompetencia(Date dataFimCompetencia) {
		this.dataFimCompetencia = dataFimCompetencia;
	}

	public Double getValorTotalRecebidoCalouro() {
		if (valorTotalRecebidoCalouro == null) {
			valorTotalRecebidoCalouro = 0.0;
		}
		return valorTotalRecebidoCalouro;
	}

	public void setValorTotalRecebidoCalouro(Double valorTotalRecebidoCalouro) {
		this.valorTotalRecebidoCalouro = valorTotalRecebidoCalouro;
	}

	public Double getValorTotalRecebidoVeterano() {
		if (valorTotalRecebidoVeterano == null) {
			valorTotalRecebidoVeterano = 0.0;
		}
		return valorTotalRecebidoVeterano;
	}

	public void setValorTotalRecebidoVeterano(Double valorTotalRecebidoVeterano) {
		this.valorTotalRecebidoVeterano = valorTotalRecebidoVeterano;
	}

	public List<CentroResultadoOrigemVO> getCentroResultadoOrigemVOs() {
		if(centroResultadoOrigemVOs == null) {
			centroResultadoOrigemVOs = new ArrayList<CentroResultadoOrigemVO>(0);
		}
		return centroResultadoOrigemVOs;
	}

	public void setCentroResultadoOrigemVOs(List<CentroResultadoOrigemVO> centroResultadoOrigemVOs) {
		this.centroResultadoOrigemVOs = centroResultadoOrigemVOs;
	}

	private Integer codigoContaReceber;
	
	public Integer getCodigoContaReceber() {
		if(codigoContaReceber == null) {
			codigoContaReceber = 0;
		}
		return codigoContaReceber;
	}

	public void setCodigoContaReceber(Integer codigoContaReceber) {
		this.codigoContaReceber = codigoContaReceber;
	}

	public String centroReceita;
	public void setCentroReceita(String centroReceita) {
		this.centroReceita = centroReceita;
	}

	public String getCentroReceita() {
		if(centroReceita == null){
			centroReceita = "";
			for(CentroResultadoOrigemVO centroResultadoOrigemVO: getCentroResultadoOrigemVOs()) {
				if(!centroReceita.contains("centroResultadoOrigemVO.getCentroReceitaVO().getDescricao()")) {
					if(!centroReceita.isEmpty()) {
						centroReceita +=", ";
					}				
					centroReceita += centroResultadoOrigemVO.getCentroReceitaVO().getDescricao();
				}
			}
		}
		return centroReceita;
	}

	public String getCpf() {
		if (cpf == null) {
			cpf = "";
		}
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
}