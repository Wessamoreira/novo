/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Extenso;
import negocio.comuns.utilitarias.Uteis;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author Philippe
 */
public class TermoReconhecimentoDividaRelVO {

	private List<TermoReconhecimentoDividaRelSub1VO> sub1;
	private List<TermoReconhecimentoDividaRelSub2VO> sub2;
	private List<TermoReconhecimentoDividaRelSub3VO> sub3;
	private List<TermoReconhecimentoDividaRelVOSubReport> subReport;
	private Double juroReal; 
	private Double juro;
	private Double desconto;
	private String tipoDesconto;
	private Double acrescimo;
	private Double valorFinal;
	private Double valor;
	private Double entrada;
	private Integer renegociacaoContaReceber;
	/**
	 * Inicio Transient utilizado no Layout 3
	 */
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private PessoaVO aluno;
	private Double valorTotalDebitoAluno;
	private List<TermoReconhecimentoDividaDebitoRelVO> termoReconhecimentoDividaDebitoRelVOs;
	private List<TermoReconhecimentoDividaParcelasRelVO> termoReconhecimentoDividaParcelasRelVOs;
	private String nomeCurso;
	private String matricula;
	private String justificativa;
	private String observacaoComplementar;

	/**
	 * Fim Transient utilizado no Layout 3
	 */

	public List<TermoReconhecimentoDividaRelVOSubReport> getSubReport() {
		if (subReport == null) {
			subReport = new ArrayList<TermoReconhecimentoDividaRelVOSubReport>(0);
		}
		return subReport;
	}

	public void setSubReport(List<TermoReconhecimentoDividaRelVOSubReport> subReport) {
		this.subReport = subReport;
	}

	public List<TermoReconhecimentoDividaRelSub1VO> getSub1() {
		if (sub1 == null) {
			sub1 = new ArrayList<TermoReconhecimentoDividaRelSub1VO>(0);
		}
		return sub1;
	}

	public void setSub1(List<TermoReconhecimentoDividaRelSub1VO> sub1) {
		this.sub1 = sub1;
	}

	public List<TermoReconhecimentoDividaRelSub2VO> getSub2() {
		if (sub2 == null) {
			sub2 = new ArrayList<TermoReconhecimentoDividaRelSub2VO>(0);
		}
		return sub2;
	}

	public void setSub2(List<TermoReconhecimentoDividaRelSub2VO> sub2) {
		this.sub2 = sub2;
	}

	public List<TermoReconhecimentoDividaRelSub3VO> getSub3() {
		if (sub3 == null) {
			sub3 = new ArrayList<TermoReconhecimentoDividaRelSub3VO>(0);
		}
		return sub3;
	}

	public void setSub3(List<TermoReconhecimentoDividaRelSub3VO> sub3) {
		this.sub3 = sub3;
	}

	public JRDataSource getSubReportJR() {
		JRDataSource jr = new JRBeanArrayDataSource(getSubReport().toArray());
		return jr;
	}

	public JRDataSource getSub3JR() {
		JRDataSource jr = new JRBeanArrayDataSource(getSub3().toArray());
		return jr;
	}

	public JRDataSource getSub2JR() {
		JRDataSource jr = new JRBeanArrayDataSource(getSub2().toArray());
		return jr;
	}

	public JRDataSource getSub1JR() {
		JRDataSource jr = new JRBeanArrayDataSource(getSub1().toArray());
		return jr;
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

	public String getTipoDesconto() {
		if (tipoDesconto == null) {
			tipoDesconto = "";
		}
		return tipoDesconto;
	}

	public void setTipoDesconto(String tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}

	public Double getAcrescimo() {
		if (acrescimo == null) {
			acrescimo = 0.0;
		}
		return acrescimo;
	}

	public void setAcrescimo(Double acrescimo) {
		this.acrescimo = acrescimo;
	}

	public Double getValorFinal() {
		if (valorFinal == null) {
			valorFinal = 0.0;
		}
		return valorFinal;
	}

	public void setValorFinal(Double valorFinal) {
		this.valorFinal = valorFinal;
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

	public Double getEntrada() {
		if (entrada == null) {
			entrada = 0.0;
		}
		return entrada;
	}

	public void setEntrada(Double entrada) {
		this.entrada = entrada;
	}

	public Integer getRenegociacaoContaReceber() {
		if (renegociacaoContaReceber == null) {
			renegociacaoContaReceber = 0;
		}
		return renegociacaoContaReceber;
	}

	public void setRenegociacaoContaReceber(Integer renegociacaoContaReceber) {
		this.renegociacaoContaReceber = renegociacaoContaReceber;
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

	public PessoaVO getAluno() {
		if (aluno == null) {
			aluno = new PessoaVO();
		}
		return aluno;
	}

	public void setAluno(PessoaVO aluno) {
		this.aluno = aluno;
	}

	public Double getValorTotalDebitoAluno() {
		if (valorTotalDebitoAluno == null) {
			valorTotalDebitoAluno = 0.0;
		}
		return valorTotalDebitoAluno;
	}

	public void setValorTotalDebitoAluno(Double valorTotalDebitoAluno) {
		this.valorTotalDebitoAluno = valorTotalDebitoAluno;
	}

	public String getValorTotalDebitoAlunoPoExtenso() {
		Extenso ext = new Extenso();
		ext.setNumber(getValorTotalDebitoAluno());
		return ext.toString();
	}

	public List<TermoReconhecimentoDividaDebitoRelVO> getTermoReconhecimentoDividaDebitoRelVOs() {
		if (termoReconhecimentoDividaDebitoRelVOs == null) {
			termoReconhecimentoDividaDebitoRelVOs = new ArrayList<TermoReconhecimentoDividaDebitoRelVO>(0);
		}
		return termoReconhecimentoDividaDebitoRelVOs;
	}

	public void setTermoReconhecimentoDividaDebitoRelVOs(List<TermoReconhecimentoDividaDebitoRelVO> termoReconhecimentoDividaDebitoRelVOs) {
		this.termoReconhecimentoDividaDebitoRelVOs = termoReconhecimentoDividaDebitoRelVOs;
	}

	public JRDataSource getTermoReconhecimentoDividaDebitoRelVO() {
		return new JRBeanArrayDataSource(getTermoReconhecimentoDividaDebitoRelVOs().toArray());
	}

	public List<TermoReconhecimentoDividaParcelasRelVO> getTermoReconhecimentoDividaParcelasRelVOs() {
		if (termoReconhecimentoDividaParcelasRelVOs == null) {
			termoReconhecimentoDividaParcelasRelVOs = new ArrayList<TermoReconhecimentoDividaParcelasRelVO>(0);
		}
		return termoReconhecimentoDividaParcelasRelVOs;
	}

	public void setTermoReconhecimentoDividaParcelasRelVOs(List<TermoReconhecimentoDividaParcelasRelVO> termoReconhecimentoDividaParcelasRelVOs) {
		this.termoReconhecimentoDividaParcelasRelVOs = termoReconhecimentoDividaParcelasRelVOs;
	}

	public JRDataSource getTermoReconhecimentoDividaParcelasRelVO() {
		return new JRBeanArrayDataSource(getTermoReconhecimentoDividaParcelasRelVOs().toArray());
	}

	public String getNomeCurso() {
		if (nomeCurso == null) {
			nomeCurso = "";
		}
		return nomeCurso;
	}

	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	
	public String getJustificativa() {
		if (justificativa == null) {
			justificativa = "";
		}
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public Double getJuroReal() {
		juroReal =0.0;
		if(Uteis.isAtributoPreenchido(getValor()) && Uteis.isAtributoPreenchido(getJuro())){
			return Uteis.arrendondarForcando2CadasDecimais((getJuro() * getValor()) /100 );
		}
		return juroReal;
	}

	public void setJuroReal(Double juroReal) {
		this.juroReal = juroReal;
	}

	public String getObservacaoComplementar() {
		if (observacaoComplementar == null) {
			observacaoComplementar = "";
		}
		return observacaoComplementar;
	}

	public void setObservacaoComplementar(String observacaoComplementar) {
		this.observacaoComplementar = observacaoComplementar;
	}	
}
