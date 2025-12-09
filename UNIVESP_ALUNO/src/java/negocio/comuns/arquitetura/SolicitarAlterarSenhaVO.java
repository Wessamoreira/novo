package negocio.comuns.arquitetura;

import java.util.Date;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoUsuario;

/**
 * 
 * @author Leonardo Riciolle
 * @date 18/02/2015 Classe responsavel por alterar a senha dos usuarios no primeiro login do sistema.
 */
public class SolicitarAlterarSenhaVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private Integer codigo;
	/*
	 * Tipo Usuario Aluno - Todos os usuário que possuem matrícula Ativa/Pré-Matricula/Trancada/Abandono de Curso, Professor - Todos os usuário que no cadastro da pessoa esteja marcado o boolean professor, Coordenador - Todos os usuários que possuem vinculo com curso coordenador, Funcionário - Todos os funcionas onde o cadastro da pessoa esteja ativa
	 */
	private TipoUsuario tipoUsuario;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	// Funcional apenas para Aluno e Coordenador
	private CursoVO cursoVO;
	// Funcional Apenas para Funcionario
	private DepartamentoVO departamentoVO;
	private Date dataSolicitacao;
	private UsuarioVO responsavel;
	private String mensagemOrientacao;
	private Boolean enviarEmail;
	// Apenas se marcado notificar por email
	private String assuntoEmail;
	private String mensagemEmail;
	private Boolean solicitarNovaSenha;

	public TipoUsuario getTipoUsuario() {
		if (tipoUsuario == null) {
			tipoUsuario = TipoUsuario.ALUNO;
		}
		return tipoUsuario;
	}

	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
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

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public DepartamentoVO getDepartamentoVO() {
		if (departamentoVO == null) {
			departamentoVO = new DepartamentoVO();
		}
		return departamentoVO;
	}

	public void setDepartamentoVO(DepartamentoVO departamentoVO) {
		this.departamentoVO = departamentoVO;
	}

	public Date getDataSolicitacao() {
		if (dataSolicitacao == null) {
			dataSolicitacao = new Date();
		}
		return dataSolicitacao;
	}

	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public String getMensagemOrientacao() {
		if (mensagemOrientacao == null) {
			mensagemOrientacao = "";
		}
		return mensagemOrientacao;
	}

	public void setMensagemOrientacao(String mensagemOrientacao) {
		this.mensagemOrientacao = mensagemOrientacao;
	}

	public Boolean getEnviarEmail() {
		if (enviarEmail == null) {
			enviarEmail = Boolean.FALSE;
		}
		return enviarEmail;
	}

	public void setEnviarEmail(Boolean enviarEmail) {
		this.enviarEmail = enviarEmail;
	}

	public String getAssuntoEmail() {
		if (assuntoEmail == null) {
			assuntoEmail = "";
		}
		return assuntoEmail;
	}

	public void setAssuntoEmail(String assuntoEmail) {
		this.assuntoEmail = assuntoEmail;
	}

	public String getMensagemEmail() {
		if (mensagemEmail == null) {
			mensagemEmail = "";
		}
		return mensagemEmail;
	}

	public void setMensagemEmail(String mensagemEmail) {
		this.mensagemEmail = mensagemEmail;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}
	
	 public String getData_Apresentar() {
	        if (dataSolicitacao == null) {
	            return "";
	        }
	        return (Uteis.getData(getDataSolicitacao()));
	    }

	public Boolean getSolicitarNovaSenha() {
		if (solicitarNovaSenha == null) {
			solicitarNovaSenha = Boolean.FALSE;
		}
		return solicitarNovaSenha;
	}

	public void setSolicitarNovaSenha(Boolean solicitarNovaSenha) {
		this.solicitarNovaSenha = solicitarNovaSenha;
	}

}
