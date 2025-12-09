package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

public class DemonstrativoResultadoFinanceiroTurmaRelVO {
	
	private Integer categoriaDespesa;
	private String unidadeEnsino;
	private Integer codigoUnidadeEnsino;
	private String turma;
	private Integer codigoTurma;
	private String departamento;
	private Integer codigoDepartamento;
	private String curso;
	private Integer qtdeAlunos;
	private Integer totalBolsas;
	private Double receita;
	private Double imposto;
	private Double inadimplencia;
	private Double custoVariavel;
	private Double margemContribuicao;
	private Double custoMedio;
	private Double custoFixo;
	private Double lucro;
	private String nivelCategoriaDespesa;
	private List<DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO> demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs;
	private List<DemonstrativoResultadoFinanceiroTurmaRelVO> listaDemonstrativoResultadoFinanceiroTurmaRelVOs;
	private String identificadorCategoriaDespesa;
	private List<CategoriaDespesaTurmaVO> listaCategoriaDespesaTurmaVOs;
	private Boolean primeiro;

	
	public String getTurma() {
		if(turma == null){
			turma = "";
		}
		return turma;
	}
	public void setTurma(String turma) {
		this.turma = turma;
	}
	public String getCurso() {
		if(curso == null){
			curso = "";
		}
		return curso;
	}
	public void setCurso(String curso) {
		this.curso = curso;
	}
	public Integer getQtdeAlunos() {
		if(qtdeAlunos == null){
			qtdeAlunos = 0;
		}
		return qtdeAlunos;
	}
	public void setQtdeAlunos(Integer qtdeAlunos) {
		this.qtdeAlunos = qtdeAlunos;
	}
	public Double getReceita() {
		if(receita == null){
			receita = 0.0;
		}
		return receita;
	}
	public void setReceita(Double receita) {
		this.receita = receita;
	}
	public Double getImposto() {
		if(imposto == null){
			imposto = 0.0;
		}
		return imposto;
	}
	public void setImposto(Double imposto) {
		this.imposto = imposto;
	}
	public Double getInadimplencia() {
		if(inadimplencia == null){
			inadimplencia = 0.0;
		}
		return inadimplencia;
	}
	public void setInadimplencia(Double inadimplencia) {
		this.inadimplencia = inadimplencia;
	}
	public Double getCustoVariavel() {
		if(custoVariavel == null){
			custoVariavel = 0.0;
		}
		return custoVariavel;
	}
	public void setCustoVariavel(Double custoVariavel) {
		this.custoVariavel = custoVariavel;
	}
	public Double getMargemContribuicao() {
		if(margemContribuicao == null){
			margemContribuicao = 0.0;
		}
		return margemContribuicao;
	}
	public void setMargemContribuicao(Double margemContribuicao) {
		this.margemContribuicao = margemContribuicao;
	}
	public Double getCustoMedio() {
		if(custoMedio == null){
			custoMedio = 0.0;
		}
		return custoMedio;
	}
	public void setCustoMedio(Double custoMedio) {
		this.custoMedio = custoMedio;
	}
	public Double getCustoFixo() {
		if(custoFixo == null){
			custoFixo = 0.0;
		}
		return custoFixo;
	}
	public void setCustoFixo(Double custoFixo) {
		this.custoFixo = custoFixo;
	}
	public Double getLucro() {
		if(lucro == null){
			lucro = 0.0;
		}
		return lucro;
	}
	public void setLucro(Double lucro) {
		this.lucro = lucro;
	}
	public List<DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO> getDemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs() {
		if(demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs == null){
			demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs = new ArrayList<DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO>(0);
		}
		return demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs;
	}
	public void setDemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs(List<DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO> demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs) {
		this.demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs = demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs;
	}
	public Integer getCodigoTurma() {
		if(codigoTurma == null){
			codigoTurma = 0;
		}
		return codigoTurma;
	}
	public void setCodigoTurma(Integer codigoTurma) {
		this.codigoTurma = codigoTurma;
	}
	public String getDepartamento() {
		if(departamento == null){
			departamento = "";
		}
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public Integer getCodigoDepartamento() {
		if(codigoDepartamento == null){
			codigoDepartamento = 0;
		}
		return codigoDepartamento;
	}
	public void setCodigoDepartamento(Integer codigoDepartamento) {
		this.codigoDepartamento = codigoDepartamento;
	}
	public String getNivelCategoriaDespesa() {
		if(nivelCategoriaDespesa == null){
			nivelCategoriaDespesa = "TU";
		}
		return nivelCategoriaDespesa;
	}
	public void setNivelCategoriaDespesa(String nivelCategoriaDespesa) {
		this.nivelCategoriaDespesa = nivelCategoriaDespesa;
	}
	
	
//	public String getOrdenacao(){
//		if(getNivelCategoriaDespesa().equals("TU")){
//			return "1"+getCurso()+getTurma();
//		}else
//		if(getNivelCategoriaDespesa().equals("DE")){
//			return "2"+getDepartamento();
//		}else{
//			return "3";
//		}
//	}
	
	public String getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = "";
		}
		return unidadeEnsino;
	}
	
	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
	
	public Integer getCodigoUnidadeEnsino() {
		if(codigoUnidadeEnsino == null){
			codigoUnidadeEnsino = 0;
		}
		return codigoUnidadeEnsino;
	}
	
	public void setCodigoUnidadeEnsino(Integer codigoUnidadeEnsino) {
		this.codigoUnidadeEnsino = codigoUnidadeEnsino;
	}
	
	public Integer getTotalBolsas() {
		if (totalBolsas == null) {
			totalBolsas = 0;
		}
		return totalBolsas;
	}
	
	public void setTotalBolsas(Integer totalBolsas) {
		this.totalBolsas = totalBolsas;
	}
	
	public Integer getCategoriaDespesa() {
		if (categoriaDespesa == null) {
			categoriaDespesa = 0;
		}
		return categoriaDespesa;
	}
	
	public void setCategoriaDespesa(Integer categoriaDespesa) {
		this.categoriaDespesa = categoriaDespesa;
	}
	public List<DemonstrativoResultadoFinanceiroTurmaRelVO> getListaDemonstrativoResultadoFinanceiroTurmaRelVOs() {
		if (listaDemonstrativoResultadoFinanceiroTurmaRelVOs == null) {
			listaDemonstrativoResultadoFinanceiroTurmaRelVOs = new ArrayList<DemonstrativoResultadoFinanceiroTurmaRelVO>(0);
		}
		return listaDemonstrativoResultadoFinanceiroTurmaRelVOs;
	}
	public void setListaDemonstrativoResultadoFinanceiroTurmaRelVOs(List<DemonstrativoResultadoFinanceiroTurmaRelVO> listaDemonstrativoResultadoFinanceiroTurmaRelVOs) {
		this.listaDemonstrativoResultadoFinanceiroTurmaRelVOs = listaDemonstrativoResultadoFinanceiroTurmaRelVOs;
	}
	public String getIdentificadorCategoriaDespesa() {
		if (identificadorCategoriaDespesa == null) {
			identificadorCategoriaDespesa = "";
		}
		return identificadorCategoriaDespesa;
	}
	public void setIdentificadorCategoriaDespesa(String identificadorCategoriaDespesa) {
		this.identificadorCategoriaDespesa = identificadorCategoriaDespesa;
	}
	public List<CategoriaDespesaTurmaVO> getListaCategoriaDespesaTurmaVOs() {
		if (listaCategoriaDespesaTurmaVOs == null) {
			listaCategoriaDespesaTurmaVOs = new ArrayList<CategoriaDespesaTurmaVO>(0);
		}
		return listaCategoriaDespesaTurmaVOs;
	}
	public void setListaCategoriaDespesaTurmaVOs(List<CategoriaDespesaTurmaVO> listaCategoriaDespesaTurmaVOs) {
		this.listaCategoriaDespesaTurmaVOs = listaCategoriaDespesaTurmaVOs;
	}
	public Boolean getPrimeiro() {
		if (primeiro == null) {
			primeiro = false;
		}
		return primeiro;
	}
	public void setPrimeiro(Boolean primeiro) {
		this.primeiro = primeiro;
	}
	
	public int getQtdeColunas() {
		return getListaDemonstrativoResultadoFinanceiroTurmaRelVOs().size();
	}
}
