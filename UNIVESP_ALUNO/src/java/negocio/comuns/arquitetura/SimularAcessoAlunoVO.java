package negocio.comuns.arquitetura;
import java.util.Date;

import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

public class SimularAcessoAlunoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6631922002214984119L;
	private Integer codigo;
	private Date dataSimulacao;
	private UsuarioVO usuarioSimulado;
	private UsuarioVO responsavelSimulacaoAluno;
	/**
	 * transient 
	 */
	private String opcaoLogin;

	public static void validarDados(SimularAcessoAlunoVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getDataSimulacao())) {
			throw new ConsistirException("O campo Data Simulação (Simulação Acesso Aluno) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getUsuarioSimulado())) {
			throw new ConsistirException("O campo Usuário Simulado (Simulação Acesso Aluno) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getResponsavelSimulacaoAluno())) {
			throw new ConsistirException("O campo Responsavél Simulação (Simulação Acesso Aluno) deve ser informado.");
		}
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Date getDataSimulacao() {
		return dataSimulacao;
	}

	public void setDataSimulacao(Date dataSimulacao) {
		this.dataSimulacao = dataSimulacao;
	}

	public UsuarioVO getUsuarioSimulado() {
		return usuarioSimulado;
	}

	public void setUsuarioSimulado(UsuarioVO usuarioSimulado) {
		this.usuarioSimulado = usuarioSimulado;
	}

	public UsuarioVO getResponsavelSimulacaoAluno() {
		return responsavelSimulacaoAluno;
	}

	public void setResponsavelSimulacaoAluno(UsuarioVO responsavelSimulacaoAluno) {
		this.responsavelSimulacaoAluno = responsavelSimulacaoAluno;
	}
	
	public String getOpcaoLogin() {
		if(opcaoLogin == null){
			opcaoLogin = "";
		}
		return opcaoLogin;
	}

	public void setOpcaoLogin(String opcaoLogin) {
		this.opcaoLogin = opcaoLogin;
	}

	@Override
	public String toString() {
		return "SimularAcessoAlunoVO [" + getResponsavelSimulacaoAluno().getCodigo() + " ]";
	}
}
