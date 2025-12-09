/**
 * 
 */
package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;

/**
 * @author Wellington Rodrigues
 * 
 */
public class ListagemDescontosAlunosPorTipoDescontoRelVO {

	private String tipoDesconto;
	private Integer codigoDesconto;
	private String nomeDesconto;	
	private String agrupador;	
	/**
	 * Usados na listagem sintetica
	 */
	private Integer qtdeMatricula;
	private Integer qtdeAlunoDescontoMatricula;
	private Double valorTotalDescontoMatricula;
	private Integer qtdeAlunoDescontoMensalidade;
	private Double valorTotalDescontoMensalidade;
	private Double valorTotalDesconto;	
	/**
	 * Usados na listagem analitica
	 */
	private Integer qtdeParcela;
	private String origemParcela;
	private Double valorDesconto;
	private Integer diasAntesVencimento;
	private String parcelas;
	private List<String> listaParcela;
	private Integer ordem;
	private List<String> matriculaComDesconto;
	
	public ListagemDescontosAlunosPorTipoDescontoRelVO(String tipoDesconto, Integer codigoDesconto, String nomeDesconto, Double valorDescontoMatricula, Integer qtdeContaMatricula, Double valorDescontoParcela, Integer qtdeContaParcela, Integer qtdeMatricula, Double valorTotalDesconto, String agrupador) {
		super();
		this.tipoDesconto = tipoDesconto;
		this.codigoDesconto = codigoDesconto;
		this.nomeDesconto = nomeDesconto;
		this.valorTotalDescontoMatricula= valorDescontoMatricula;
		this.valorTotalDescontoMensalidade= valorDescontoParcela;
		this.qtdeAlunoDescontoMatricula = qtdeContaMatricula;
		this.qtdeAlunoDescontoMensalidade = qtdeContaParcela;
		this.valorDesconto = valorTotalDesconto;
		this.qtdeMatricula = qtdeMatricula;
		this.agrupador = agrupador;
	}

	public ListagemDescontosAlunosPorTipoDescontoRelVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTipoDesconto() {
		if (tipoDesconto == null) {
			tipoDesconto = "";
		}
		return tipoDesconto;
	}

	public void setTipoDesconto(String tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}

	public Integer getCodigoDesconto() {
		if (codigoDesconto == null) {
			codigoDesconto = 0;
		}
		return codigoDesconto;
	}

	public void setCodigoDesconto(Integer codigoDesconto) {
		this.codigoDesconto = codigoDesconto;
	}

	public String getNomeDesconto() {
		if (nomeDesconto == null) {
			nomeDesconto = "";
		}
		return nomeDesconto;
	}

	public void setNomeDesconto(String nomeDesconto) {
		this.nomeDesconto = nomeDesconto;
	}

	public Integer getQtdeAlunoDescontoMatricula() {
		if (qtdeAlunoDescontoMatricula == null) {
			qtdeAlunoDescontoMatricula = 0;
		}
		return qtdeAlunoDescontoMatricula;
	}

	public void setQtdeAlunoDescontoMatricula(Integer qtdeAlunoDescontoMatricula) {
		this.qtdeAlunoDescontoMatricula = qtdeAlunoDescontoMatricula;
	}

	public Double getValorTotalDescontoMatricula() {
		if (valorTotalDescontoMatricula == null) {
			valorTotalDescontoMatricula = 0.0;
		}
		return valorTotalDescontoMatricula;
	}

	public void setValorTotalDescontoMatricula(Double valorTotalDescontoMatricula) {
		this.valorTotalDescontoMatricula = valorTotalDescontoMatricula;
	}

	public Integer getQtdeAlunoDescontoMensalidade() {
		if (qtdeAlunoDescontoMensalidade == null) {
			qtdeAlunoDescontoMensalidade = 0;
		}
		return qtdeAlunoDescontoMensalidade;
	}

	public void setQtdeAlunoDescontoMensalidade(Integer qtdeAlunoDescontoMensalidade) {
		this.qtdeAlunoDescontoMensalidade = qtdeAlunoDescontoMensalidade;
	}

	public Double getValorTotalDescontoMensalidade() {
		if (valorTotalDescontoMensalidade == null) {
			valorTotalDescontoMensalidade = 0.0;
		}
		return valorTotalDescontoMensalidade;
	}

	public void setValorTotalDescontoMensalidade(Double valorTotalDescontoMensalidade) {
		this.valorTotalDescontoMensalidade = valorTotalDescontoMensalidade;
	}

	public Integer getDiasAntesVencimento() {		
		return diasAntesVencimento;
	}

	public void setDiasAntesVencimento(Integer diasAntesVencimento) {
		this.diasAntesVencimento = diasAntesVencimento;
	}

	public String getParcelas() {
		if (parcelas == null) {
			parcelas = "";
		}
		return parcelas;
	}

	public void setParcelas(String parcelas) {
		this.parcelas = parcelas;
	}

	public List<String> getListaParcela() {
		if (listaParcela == null) {
			listaParcela = new ArrayList<String>(0);
		}
		return listaParcela;
	}

	public void setListaParcela(List<String> listaParcela) {
		this.listaParcela = listaParcela;
	}

	public Integer getOrdem() {
		if (ordem == null) {
			ordem = 0;
		}
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public Double getValorDesconto() {
		if (valorDesconto == null) {
			valorDesconto = 0.0;
		}
		return valorDesconto;
	}

	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public String getOrigemParcela() {
		if (origemParcela == null) {
			origemParcela = "";
		}
		return origemParcela;
	}

	public void setOrigemParcela(String origemParcela) {
		this.origemParcela = origemParcela;
	}

	public Integer getQtdeParcela() {
		if (qtdeParcela == null) {
			qtdeParcela = 0;
		}
		return qtdeParcela;
	}

	public void setQtdeParcela(Integer qtdeParcela) {
		this.qtdeParcela = qtdeParcela;
	}

	public Integer getQtdeMatricula() {
		if(qtdeMatricula == null){
			qtdeMatricula = 0;
		}
		return qtdeMatricula;
	}

	public void setQtdeMatricula(Integer qtdeMatricula) {
		this.qtdeMatricula = qtdeMatricula;
	}

	public Double getValorTotalDesconto() {
		if(valorTotalDesconto == null){
			valorTotalDesconto = 0.0;
		}
		return valorTotalDesconto;
	}

	public void setValorTotalDesconto(Double valorTotalDesconto) {
		this.valorTotalDesconto = valorTotalDesconto;
	}

	public String getAgrupador() {
		if(agrupador == null){
			agrupador = "";
		}
		return agrupador;
	}

	public void setAgrupador(String agrupador) {
		this.agrupador = agrupador;
	}
	

	public List<String> getMatriculaComDesconto() {
		if(matriculaComDesconto == null){
			matriculaComDesconto = new ArrayList<String>();
		}
		return matriculaComDesconto;
	}

	public void setMatriculaComDesconto(List<String> matriculaComDesconto) {
		this.matriculaComDesconto = matriculaComDesconto;
	}

	public Integer getQtdeMatriculaComDesconto() {
		return getMatriculaComDesconto().size();
	}
}
