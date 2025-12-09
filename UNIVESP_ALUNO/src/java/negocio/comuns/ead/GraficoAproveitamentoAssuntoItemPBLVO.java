package negocio.comuns.ead;

import negocio.comuns.arquitetura.SuperVO;

public class GraficoAproveitamentoAssuntoItemPBLVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3011982558307606557L;

	private GraficoAproveitamentoAssuntoPBLVO graficoAproveitamentoAssuntoPBLVO;
	private Integer codigoParametro;
	private String descricaoParametro;
	private String quantidadeAlunos;
	private String corGrafico;
	private String corLetraGrafico;
	private String categoriaGrafico;
	private String origemGrafico;

	public Integer getCodigoParametro() {
		if (codigoParametro == null) {
			codigoParametro = 0;
		}
		return codigoParametro;
	}

	public void setCodigoParametro(Integer codigoParametro) {
		this.codigoParametro = codigoParametro;
	}

	public String getDescricaoParametro() {
		if (descricaoParametro == null) {
			descricaoParametro = "";
		}
		return descricaoParametro;
	}

	public void setDescricaoParametro(String descricaoParametro) {
		this.descricaoParametro = descricaoParametro;
	}

	public String getQuantidadeAlunos() {
		if (quantidadeAlunos == null) {
			quantidadeAlunos = "";
		}
		return quantidadeAlunos;
	}

	public void setQuantidadeAlunos(String quantidadeAlunos) {
		this.quantidadeAlunos = quantidadeAlunos;
	}

	public String getCorGrafico() {
		if (corGrafico == null) {
			corGrafico = "";
		}
		return corGrafico;
	}

	public void setCorGrafico(String corGrafico) {
		this.corGrafico = corGrafico;
	}

	public String getCorLetraGrafico() {
		if (corLetraGrafico == null) {
			corLetraGrafico = "";
		}
		return corLetraGrafico;
	}

	public void setCorLetraGrafico(String corLetraGrafico) {
		this.corLetraGrafico = corLetraGrafico;
	}
	

	public String getOrigemGrafico() {
		if (origemGrafico == null) {
			origemGrafico = "";
		}
		return origemGrafico;
	}

	public void setOrigemGrafico(String origemGrafico) {
		this.origemGrafico = origemGrafico;
	}
	

	public GraficoAproveitamentoAssuntoPBLVO getGraficoAproveitamentoAssuntoPBLVO() {
		if (graficoAproveitamentoAssuntoPBLVO == null) {
			graficoAproveitamentoAssuntoPBLVO = new GraficoAproveitamentoAssuntoPBLVO();
		}
		return graficoAproveitamentoAssuntoPBLVO;
	}

	public void setGraficoAproveitamentoAssuntoPBLVO(GraficoAproveitamentoAssuntoPBLVO graficoAproveitamentoAssuntoPBLVO) {
		this.graficoAproveitamentoAssuntoPBLVO = graficoAproveitamentoAssuntoPBLVO;
	}
	

	public String getCategoriaGrafico() {
		if (categoriaGrafico == null) {
			categoriaGrafico = "";
		}
		return categoriaGrafico;
	}

	public void setCategoriaGrafico(String categoriaGrafico) {
		this.categoriaGrafico = categoriaGrafico;
	}

	public String getSerieGraficoPizza() {
		StringBuilder sb = new StringBuilder();
		sb.append("{name: \"").append(getDescricaoParametro()).append("\", y:").append(getQuantidadeAlunos()).append(", color:\"").append(getCorGrafico()).append("\", origem:\"").append(getOrigemGrafico()).append("\", conteudo:").append(getGraficoAproveitamentoAssuntoPBLVO().getMonitorConhecimentoPBLVO().getConteudoVO().getCodigo()).append(", turma:").append(getGraficoAproveitamentoAssuntoPBLVO().getMonitorConhecimentoPBLVO().getTurmaVO().getCodigo()).append(", disciplina:").append(getGraficoAproveitamentoAssuntoPBLVO().getMonitorConhecimentoPBLVO().getDisciplinaVO().getCodigo()).append(", ano:\"").append(getGraficoAproveitamentoAssuntoPBLVO().getMonitorConhecimentoPBLVO().getAno()).append("\", semestre:\"").append(getGraficoAproveitamentoAssuntoPBLVO().getMonitorConhecimentoPBLVO().getSemestre()).append("\", tema:").append(getGraficoAproveitamentoAssuntoPBLVO().getCodigoTemaAssunto()).append(", cupre:\"0\", codigoParametro:").append(getCodigoParametro()).append("}");
		return sb.toString();
	}
	
	public String getSerieGraficoBarra() {
		StringBuilder sb = new StringBuilder();
		sb.append("{name: \"").append(getDescricaoParametro()).append("\", data:[");
		String[] listaCategorias = getCategoriaGrafico().split(",");
		int index = 0;
		for (String data : getQuantidadeAlunos().split(",")) {
				sb.append("{ y:").append(data.trim()).append(", origem:\"").append(getOrigemGrafico()).append("\", conteudo:").append(getGraficoAproveitamentoAssuntoPBLVO().getMonitorConhecimentoPBLVO().getConteudoVO().getCodigo()).append(", turma:").append(getGraficoAproveitamentoAssuntoPBLVO().getMonitorConhecimentoPBLVO().getTurmaVO().getCodigo()).append(", disciplina:").append(getGraficoAproveitamentoAssuntoPBLVO().getMonitorConhecimentoPBLVO().getDisciplinaVO().getCodigo()).append(", ano:\"").append(getGraficoAproveitamentoAssuntoPBLVO().getMonitorConhecimentoPBLVO().getAno()).append("\", semestre:\"").append(getGraficoAproveitamentoAssuntoPBLVO().getMonitorConhecimentoPBLVO().getSemestre()).append("\", tema:").append(getGraficoAproveitamentoAssuntoPBLVO().getCodigoTemaAssunto()).append(", cupre:\"").append(listaCategorias[index]).append("\", codigoParametro:").append(getCodigoParametro()).append("},");
				index++;
		}
		sb.delete((sb.length() - 1), sb.length());
		sb.append("], color:\"").append(getCorGrafico()).append("\"}");
		return sb.toString();
	}


}
