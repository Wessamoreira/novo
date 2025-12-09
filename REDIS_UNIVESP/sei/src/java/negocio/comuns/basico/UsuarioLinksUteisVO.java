package negocio.comuns.basico;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

public class UsuarioLinksUteisVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private UsuarioVO usuarioVO;
	private LinksUteisVO linksUteisVO;
	private String emailPlanilha;
	private String cpfPlanilha;
	private Boolean erro;	
	private String mensagemErro;
	private Boolean excluir;

	public UsuarioLinksUteisVO() {
		super();
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.NESTED)
	public static void validarDados(UsuarioVO usuarioVO, PessoaVO pessoaVO, Boolean validaCpf, Boolean erro, Boolean validaEndereco) throws ConsistirException {
		if (Uteis.isAtributoPreenchido(pessoaVO.getCPF())) {
			throw new ConsistirException("O Usuario Selecionado já se encontra na lista");
		}
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

	public UsuarioVO getUsuarioVO() {
		if (usuarioVO == null) {
			usuarioVO = new UsuarioVO();
		}
		return usuarioVO;
	}

	public void setUsuario(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}

	public LinksUteisVO getLinksUteisVO() {
		if (linksUteisVO == null) {
			linksUteisVO = new LinksUteisVO();
		}
		return linksUteisVO;
	}

	public void setLinksUteisVO(LinksUteisVO linksUteisVO) {
		this.linksUteisVO = linksUteisVO;
	}

	public String getEmailPlanilha() {
		if (emailPlanilha == null) {
			emailPlanilha = "";
		}
		return emailPlanilha;
	}

	public void setEmailPlanilha(String emailPlanilha) {
		this.emailPlanilha = emailPlanilha;
	}

	public String getCpfPlanilha() {
		if (cpfPlanilha == null) {
			cpfPlanilha = "";
		}
		return cpfPlanilha;
	}
	
	public void setCpfPlanilha(String cpfPlanilha) {
		this.cpfPlanilha = cpfPlanilha;
	}
	
	public String getMensagemErro() {
		if (mensagemErro == null) {
			mensagemErro = "";
		}
		return mensagemErro;
	}

	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
	}

	public Boolean getErro() {
		if (erro == null) {
			erro = Boolean.FALSE;
		}
		return erro;
	}

	public void setErro(Boolean erro) {
		this.erro = erro;
	}
	
	public Boolean getExcluir() {
		if (excluir == null) {
			excluir = false;
		}
		return excluir;
	}
	
	public void setExcluir(Boolean excluir) {
		this.excluir = excluir;
	}

}