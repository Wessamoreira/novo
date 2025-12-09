package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.academico.enumeradores.SituacaoTipoAdvertenciaEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

public class TipoAdvertenciaVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private UsuarioVO usuarioVO;
	private Date dataCadastro;
	private Integer codigo;
	private String descricao;
	private String descricaoAdvertencia;
	private Boolean gerarSuspensao;
	private Boolean advertenciaVerbal;
	private Boolean visaoPais;
	private Boolean visaoAluno;
	private Boolean enviarEmail;
	
	private SituacaoTipoAdvertenciaEnum situacao;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public SituacaoTipoAdvertenciaEnum getSituacao() {
		if (situacao == null) {
			situacao = SituacaoTipoAdvertenciaEnum.ATIVO;
		}
		return situacao;
	}

	public void setSituacao(SituacaoTipoAdvertenciaEnum situacao) {
		this.situacao = situacao;
	}

	public String getDescricaoAdvertencia() {
		if (descricaoAdvertencia == null) {
			descricaoAdvertencia = "";
		}
		return descricaoAdvertencia;
	}

	public void setDescricaoAdvertencia(String descricaoAdvertencia) {
		this.descricaoAdvertencia = descricaoAdvertencia;
	}

	public Boolean getGerarSuspensao() {
		if (gerarSuspensao == null) {
			gerarSuspensao = false;
		}
		return gerarSuspensao;
	}

	public void setGerarSuspensao(Boolean gerarSuspensao) {
		this.gerarSuspensao = gerarSuspensao;
	}

	public Boolean getAdvertenciaVerbal() {
		if (advertenciaVerbal == null) {
			advertenciaVerbal = false;
		}
		return advertenciaVerbal;
	}

	public void setAdvertenciaVerbal(Boolean advertenciaVerbal) {
		this.advertenciaVerbal = advertenciaVerbal;
	}

	public Boolean getVisaoAluno() {
		if (visaoAluno == null) {
			visaoAluno = false;
		}
		return visaoAluno;
	}

	public void setVisaoAluno(Boolean visaoAluno) {
		this.visaoAluno = visaoAluno;
	}

	public Boolean getEnviarEmail() {
		if (enviarEmail == null) {
			enviarEmail = false;
		}
		return enviarEmail;
	}

	public void setEnviarEmail(Boolean enviarEmail) {
		this.enviarEmail = enviarEmail;
	}

	public Boolean getVisaoPais() {
		if (visaoPais == null) {
			visaoPais = false;
		}
		return visaoPais;
	}

	public void setVisaoPais(Boolean visaoPais) {
		this.visaoPais = visaoPais;
	}

	public UsuarioVO getUsuarioVO() {
		if (usuarioVO == null) {
			usuarioVO = new UsuarioVO();
		}
		return usuarioVO;
	}

	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}

	public Date getDataCadastro() {
		if (dataCadastro == null) {
			dataCadastro = new Date();
		}
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

}
