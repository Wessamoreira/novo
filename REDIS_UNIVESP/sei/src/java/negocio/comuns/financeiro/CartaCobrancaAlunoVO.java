package negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;

public class CartaCobrancaAlunoVO extends SuperVO {

	private Integer codigo;
	private String aluno;
	private String matricula;
	private String tipoPessoa;
	private String responsavelFinanceiro;
	private String contas;
	private Integer cartacobranca;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
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
	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public Integer getCartacobranca() {
		if (cartacobranca == null) {
			cartacobranca = 0;
		}
		return cartacobranca;
	}
	public void setCartacobranca(Integer cartacobranca) {
		this.cartacobranca = cartacobranca;
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
	public String getResponsavelFinanceiro() {
		if (responsavelFinanceiro == null) {
			responsavelFinanceiro = "";
		}
		return responsavelFinanceiro;
	}
	public void setResponsavelFinanceiro(String responsavelFinanceiro) {
		this.responsavelFinanceiro = responsavelFinanceiro;
	}
	public String getContas() {
		if (contas == null) {
			contas = "";
		}
		return contas;
	}
	public void setContas(String contas) {
		this.contas = contas;
	}
	

	public String nomeApresentar;
	public String getNomeApresentar() {
		if(nomeApresentar == null) {
			if(getTipoPessoa().equals("RF") && !getResponsavelFinanceiro().equals(getAluno())) {
				nomeApresentar =  "Resp. Finan:"+getResponsavelFinanceiro() + (getAluno().trim().isEmpty()? "": " - Aluno: "+getAluno());
			}else {		
				nomeApresentar =  getAluno();
			}
		}
		return nomeApresentar;
	}

}
