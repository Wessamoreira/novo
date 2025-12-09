package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class SituacaoFinanceiraAlunoRelVO {

	private List<DadosSituacaoContaReceberVO> contaReceberVOs;
	private List<DadosSituacaoContaPagarVO> contaPagarVOs;

	private String aluno;
	private String curso;
	private String cpf;
	private String matricula;
	private String situacaoMatricula;
	private String anoSemestre;
	private String turma;
	private String cnpjEmpresa;
	private String nomeEmpresa;
	private Double valorDevidoTotal;
	private Boolean apresentarContaPagar;
	private String observacaoComplementar;
	private String telefone;
	private String celular;
	private Boolean quite;
	private Boolean parcelaAVencer;
	private Boolean parcelaEmAberto;
	private Boolean parcelaEmNegociacao;
	private String nomeResponsavelFinanceiro;
	private String nomesAgentesNegativacaoCobrancaContaReceberVO;

	public SituacaoFinanceiraAlunoRelVO() {

	}

	public JRDataSource getListaContaReceberVOs() {
		JRDataSource jr = new JRBeanArrayDataSource(getContaReceberVOs().toArray());
		return jr;
	}

	public JRDataSource getListaContaPagarVOs() {
		JRDataSource jr = new JRBeanArrayDataSource(getContaPagarVOs().toArray());
		return jr;
	}	
	
	

	public List<DadosSituacaoContaReceberVO> getContaReceberVOs() {
		if (contaReceberVOs == null) {
			contaReceberVOs = new ArrayList<DadosSituacaoContaReceberVO>(0);
		}
		return contaReceberVOs;
	}

	public void setContaReceberVOs(List<DadosSituacaoContaReceberVO> contaReceberVOs) {
		this.contaReceberVOs = contaReceberVOs;
	}

	public String getAluno() {
		if (aluno == null) {
			aluno = "";
		}
		return aluno;
	}

	public void setAluno(String aluno) {
		this.aluno = aluno;
	}

	public String getCurso() {
		if (curso == null) {
			curso = "";
		}
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
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

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public Double getValorDevidoTotal() {
		return valorDevidoTotal;
	}

	public void setValorDevidoTotal(Double valorDevidoTotal) {
		this.valorDevidoTotal = valorDevidoTotal;
	}

	/**
	 * @return the turma
	 */
	public String getTurma() {
		if (turma == null) {
			turma = "";
		}
		return turma;
	}

	/**
	 * @param turma
	 *            the turma to set
	 */
	public void setTurma(String turma) {
		this.turma = turma;
	}

	/**
	 * @return the cnpjEmpresa
	 */
	public String getCnpjEmpresa() {
		if (cnpjEmpresa == null) {
			cnpjEmpresa = "";
		}
		return cnpjEmpresa;
	}

	/**
	 * @param cnpjEmpresa
	 *            the cnpjEmpresa to set
	 */
	public void setCnpjEmpresa(String cnpjEmpresa) {
		this.cnpjEmpresa = cnpjEmpresa;
	}

	/**
	 * @return the nomeEmpresa
	 */
	public String getNomeEmpresa() {
		if (nomeEmpresa == null) {
			nomeEmpresa = "";
		}
		return nomeEmpresa;
	}

	/**
	 * @param nomeEmpresa
	 *            the nomeEmpresa to set
	 */
	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa;
	}

	public List<DadosSituacaoContaPagarVO> getContaPagarVOs() {
		if (contaPagarVOs == null) {
			contaPagarVOs = new ArrayList<DadosSituacaoContaPagarVO>(0);
		}
		return contaPagarVOs;
	}

	public void setContaPagarVOs(List<DadosSituacaoContaPagarVO> contaPagarVOs) {
		this.contaPagarVOs = contaPagarVOs;
	}

	public Boolean getApresentarContaPagar() {
		if (apresentarContaPagar == null) {
			apresentarContaPagar = Boolean.FALSE;
		}
		return apresentarContaPagar;
	}

	public void setApresentarContaPagar(Boolean apresentarContaPagar) {
		this.apresentarContaPagar = apresentarContaPagar;
	}

	public String getObservacaoComplementar() {
		return observacaoComplementar;
	}

	public void setObservacaoComplementar(String observacaoComplementar) {
		this.observacaoComplementar = observacaoComplementar;
	}

	public String getCelular() {
		if (celular == null) {
			celular = "";
		}
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getTelefone() {
		if (telefone == null) {
			telefone = "";
		}
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getSituacaoMatricula() {
		if (situacaoMatricula == null) {
			situacaoMatricula = "";
		}
		return situacaoMatricula;
	}

	public void setSituacaoMatricula(String situacaoMatricula) {
		this.situacaoMatricula = situacaoMatricula;
	}

	public String getAnoSemestre() {
		if (anoSemestre == null) {
			anoSemestre = "";
		}
		return anoSemestre;
	}

	public void setAnoSemestre(String anoSemestre) {
		this.anoSemestre = anoSemestre;
	}

	public Boolean getQuite() {
		if (quite == null) {
			quite = false;
		}
		return quite;
	}

	public void setQuite(Boolean quite) {
		this.quite = quite;
	}

	public Boolean getParcelaAVencer() {
		if (parcelaAVencer == null) {
			parcelaAVencer = false;
		}
		return parcelaAVencer;
	}

	public void setParcelaAVencer(Boolean parcelaAVencer) {
		this.parcelaAVencer = parcelaAVencer;
	}

	public Boolean getParcelaEmAberto() {
		if (parcelaEmAberto == null) {
			parcelaEmAberto = false;
		}
		return parcelaEmAberto;
	}

	public void setParcelaEmAberto(Boolean parcelaEmAberto) {
		this.parcelaEmAberto = parcelaEmAberto;
	}

	public Boolean getParcelaEmNegociacao() {
		if (parcelaEmNegociacao == null) {
			parcelaEmNegociacao = false;
		}
		return parcelaEmNegociacao;
	}

	public void setParcelaEmNegociacao(Boolean parcelaEmNegociacao) {
		this.parcelaEmNegociacao = parcelaEmNegociacao;
	}

	public String getNomeResponsavelFinanceiro() {
		if (nomeResponsavelFinanceiro == null) {
			nomeResponsavelFinanceiro = "";
		}
		return nomeResponsavelFinanceiro;
	}

	public void setNomeResponsavelFinanceiro(String nomeResponsavelFinanceiro) {
		this.nomeResponsavelFinanceiro = nomeResponsavelFinanceiro;
	}

	

	public String getNomesAgentesNegativacaoCobrancaContaReceberVO() {
		if(nomesAgentesNegativacaoCobrancaContaReceberVO == null ) {
			nomesAgentesNegativacaoCobrancaContaReceberVO ="" ;
		}
		return nomesAgentesNegativacaoCobrancaContaReceberVO;
	}

	public void setNomesAgentesNegativacaoCobrancaContaReceberVO(
			String nomesAgentesNegativacaoCobrancaContaReceberVO) {
		this.nomesAgentesNegativacaoCobrancaContaReceberVO = nomesAgentesNegativacaoCobrancaContaReceberVO;
	}

	
}