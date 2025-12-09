package negocio.comuns.administrativo;

import java.io.Serializable;
import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;


public class CampanhaRSLogVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private Date data;
	private Integer campanha;
	private Integer codigoUnidadeEnsino;
	private Integer codigoCurso;
	private Integer codigoTurno;
	private String nome;
	private String email;
	private String telefoneResidencial;
	private String celular;
	private String mensagem;
	private String duvida;
	private Boolean ocorreuErro;
	private PessoaVO colaboradorVO;
	private UsuarioVO usuarioOperacoesExternasVO;
	private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;
	
	public Integer getCodigoUnidadeEnsino() {
		if (codigoUnidadeEnsino == null) {
			codigoUnidadeEnsino = 0;
		}
		return codigoUnidadeEnsino;
	}
	public void setCodigoUnidadeEnsino(Integer codigoUnidadeEnsino) {
		this.codigoUnidadeEnsino = codigoUnidadeEnsino;
	}
	public Integer getCodigoCurso() {
		if (codigoCurso == null) {
			codigoCurso = 0;
		}
		return codigoCurso;
	}
	public void setCodigoCurso(Integer codigoCurso) {
		this.codigoCurso = codigoCurso;
	}
	public Integer getCodigoTurno() {
		if (codigoTurno == null) {
			codigoTurno = 0;
		}
		return codigoTurno;
	}
	public void setCodigoTurno(Integer codigoTurno) {
		this.codigoTurno = codigoTurno;
	}
	
	public String getMensagem() {
		if (mensagem == null) {
			mensagem = "";
		}
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public String getDuvida() {
		if (duvida == null) {
			duvida = "";
		}
		return duvida;
	}
	public void setDuvida(String duvida) {
		this.duvida = duvida;
	}
	public Boolean getOcorreuErro() {
		if (ocorreuErro == null) {
			ocorreuErro = false;
		}
		return ocorreuErro;
	}
	public void setOcorreuErro(Boolean ocorreuErro) {
		this.ocorreuErro = ocorreuErro;
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
		if (data == null) {
			data = new Date();
		}
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public Integer getCampanha() {
		if (campanha == null) {
			campanha = 0;
		}
		return campanha;
	}
	public void setCampanha(Integer campanha) {
		this.campanha = campanha;
	}
	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTelefoneResidencial() {
		if (telefoneResidencial == null) {
			telefoneResidencial = "";
		}
		return telefoneResidencial;
	}
	public void setTelefoneResidencial(String telefoneResidencial) {
		this.telefoneResidencial = telefoneResidencial;
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
	
	public PessoaVO getColaboradorVO() {
		if (colaboradorVO == null) {
			colaboradorVO = new PessoaVO();
		}
		return colaboradorVO;
	}
	public void setColaboradorVO(PessoaVO colaboradorVO) {
		this.colaboradorVO = colaboradorVO;
	}
	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public UsuarioVO getUsuarioOperacoesExternasVO() {
		if (usuarioOperacoesExternasVO == null) {
			usuarioOperacoesExternasVO = new UsuarioVO();
		}
		return usuarioOperacoesExternasVO;
	}
	public void setUsuarioOperacoesExternasVO(UsuarioVO usuarioOperacoesExternasVO) {
		this.usuarioOperacoesExternasVO = usuarioOperacoesExternasVO;
	}
	public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO() {
		if (configuracaoGeralSistemaVO == null) {
			configuracaoGeralSistemaVO = new ConfiguracaoGeralSistemaVO();
		}
		return configuracaoGeralSistemaVO;
	}
	public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
	}

}
