package negocio.comuns.arquitetura;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoLdapVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;

import java.util.Date;

public class RegistroLdapVO extends SuperVO {

	public static final long serialVersionUID = 1L;
	private Integer codigo;
	private Date data;
	private UsuarioVO usuario;
	private String operacao;
	private String resumo;
	private Boolean sucesso;
	private String excessao;
	private ConfiguracaoLdapVO configuracaoLdap;
	private MatriculaVO matricula;
	private PessoaVO pessoaVO;
	private PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO;
	

	public RegistroLdapVO() {
		super();
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

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public UsuarioVO getUsuario() {
		if (usuario == null) {
			usuario = new UsuarioVO();
		}
		return usuario;
	}

	public void setUsuario(UsuarioVO usuario) {
		this.usuario = usuario;
	}

	public String getOperacao() {
		if (operacao == null) {
			operacao = "";
		}
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

	public String getResumo() {
		if (resumo == null) {
			resumo = "";
		}
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	public Boolean getSucesso() {
		if (sucesso == null) {
			sucesso = false;
		}
		return sucesso;
	}

	public void setSucesso(Boolean sucesso) {
		this.sucesso = sucesso;
	}

	public String getExcessao() {
		if (excessao == null) {
			excessao = "";
		}
		return excessao;
	}

	public void setExcessao(String excessao) {
		this.excessao = excessao;
	}

	public ConfiguracaoLdapVO getConfiguracaoLdap() {
		if (configuracaoLdap == null) {
			configuracaoLdap = new ConfiguracaoLdapVO();
		}
		return configuracaoLdap;
	}

	public void setConfiguracaoLdap(ConfiguracaoLdapVO configuracaoLdap) {
		this.configuracaoLdap = configuracaoLdap;
	}

	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}
	public String operacao_Apresentar;
	public String getOperacao_Apresentar() {
		if(operacao_Apresentar == null) {
			if (getOperacao().equals("sincronizarComLdapAoCancelarTransferirMatricula") || getOperacao().equals("sincronizarComLdapAoInativarConta") ) {
				operacao_Apresentar = "Inativar Usuário";				
			}else if (getOperacao().equals("sincronizarComLdapAoCancelarTransferirMatriculaEstorno") || getOperacao().equals("sincronizarComLdapAoInativarContaEstorno") ) {
				operacao_Apresentar = "Reativar Usuário";
			}else if (getOperacao().equals("sincronizarComLdapAoLogar")) {
				operacao_Apresentar = "Cadastrar Usuário";
			}else if (getOperacao().equals("sincronizarAlteracaoAcadastral")) {
				operacao_Apresentar = "Alteração Cadatral";
			}else if (getOperacao().equals("sincronizarComLdapAoAlterarSenha")) {
				operacao_Apresentar = "Alteração Senha";
			}else {
				operacao_Apresentar = "";
			}
		}
		
		return operacao_Apresentar;
	}

	public PessoaVO getPessoaVO() {
		if(pessoaVO ==  null) {
			pessoaVO =  new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public PessoaEmailInstitucionalVO getPessoaEmailInstitucionalVO() {
		if(pessoaEmailInstitucionalVO ==  null) {
			pessoaEmailInstitucionalVO =  new PessoaEmailInstitucionalVO();
		}
		return pessoaEmailInstitucionalVO;
	}

	public void setPessoaEmailInstitucionalVO(PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO) {
		this.pessoaEmailInstitucionalVO = pessoaEmailInstitucionalVO;
	}
	
	
}