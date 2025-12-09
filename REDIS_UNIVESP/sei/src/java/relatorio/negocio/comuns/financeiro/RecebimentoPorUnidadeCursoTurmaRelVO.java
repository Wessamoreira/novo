package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class RecebimentoPorUnidadeCursoTurmaRelVO {
	
	private String nomeCurso;
	private String nomeTurma;
	private Integer quantidadeGeralParcelas;
	private Double valorTotalRecebido;
	private Double subTotalRecebido;
	private Integer quantidadeParcelas;
	
	private List<RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO> listaRecebimentoParcelas;
	
	public JRDataSource getRecebimentoParcelas() {
        return new JRBeanArrayDataSource(getListaRecebimentoParcelas().toArray());
    }
	
	public Double executarCalcularSubTotalRecebimento() {
		Double subtotal = 0.0;
		 for (RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO recebimentoParcela : getListaRecebimentoParcelas()) {
			 subtotal = subtotal + recebimentoParcela.getValorRecebido();
	     }
		 return subtotal;
	}
	
	public Integer executarCalcularQuantidadeParcelas() {
		 return getListaRecebimentoParcelas().size();
	}
	
	public String getNomeCurso() {
		if(nomeCurso == null){
			nomeCurso = "";
		}
		return nomeCurso;
	}
	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}
	public String getNomeTurma() {
		if(nomeTurma == null){
			nomeTurma = "";
		}
		return nomeTurma;
	}
	public void setNomeTurma(String nomeTurma) {
		this.nomeTurma = nomeTurma;
	}
	public Integer getQuantidadeGeralParcelas() {
		return quantidadeGeralParcelas;
	}
	public void setQuantidadeGeralParcelas(Integer quantidadeGeralParcelas) {
		this.quantidadeGeralParcelas = quantidadeGeralParcelas;
	}
	public Double getValorTotalRecebido() {
		return valorTotalRecebido;
	}
	public void setValorTotalRecebido(Double valorTotalRecebido) {
		this.valorTotalRecebido = valorTotalRecebido;
	}
	public List<RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO> getListaRecebimentoParcelas() {
		if(listaRecebimentoParcelas == null){
			listaRecebimentoParcelas = new ArrayList<RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO>(0);
		}
		return listaRecebimentoParcelas;
	}
	public void setListaRecebimentoParcelas(List<RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO> listaRecebimentoParcelas) {
		this.listaRecebimentoParcelas = listaRecebimentoParcelas;
	}

	public Double getSubTotalRecebido() {
		if(subTotalRecebido == null){
			subTotalRecebido = 0.0;
		}
		return subTotalRecebido;
	}

	public void setSubTotalRecebido(Double subTotalRecebido) {
		this.subTotalRecebido = subTotalRecebido;
	}

	public Integer getQuantidadeParcelas() {
		if(quantidadeParcelas == null){
			quantidadeParcelas = 0;
		}
		return quantidadeParcelas;
	}

	public void setQuantidadeParcelas(Integer quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

}
