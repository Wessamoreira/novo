package negocio.comuns.basico;

import java.util.Date;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;

public class CatracaVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String ip;
	private String serie;
	private Integer resolucao;
	private String descricao;
	private String mensagemLiberar;
	private String mensagemBloquear;
	private Integer duracaoMensagem;
	private Integer duracaoDesbloquear;
	private String direcao;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private UsuarioVO responsavelCadastro;
	private Date dataCadastro;
	private String situacao;

	public String getIp() {
		if (ip == null) {
			ip = "";
		}
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSerie() {
		if (serie == null) {
			serie = "";
		}
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public Integer getResolucao() {
		if (resolucao == null) {
			resolucao = 0;
		}
		return resolucao;
	}

	public void setResolucao(Integer resolucao) {
		this.resolucao = resolucao;
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

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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

	public UsuarioVO getResponsavelCadastro() {
		if (responsavelCadastro == null) {
			responsavelCadastro = new UsuarioVO();
		}
		return responsavelCadastro;
	}

	public void setResponsavelCadastro(UsuarioVO responsavelCadastro) {
		this.responsavelCadastro = responsavelCadastro;
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

	public String getMensagemLiberar() {
		if (mensagemLiberar == null) {
			mensagemLiberar = "";
		}
		return mensagemLiberar;
	}

	public void setMensagemLiberar(String mensagemLiberar) {
		this.mensagemLiberar = mensagemLiberar;
	}

	public String getMensagemBloquear() {
		if (mensagemBloquear == null) {
			mensagemBloquear = "";
		}
		return mensagemBloquear;
	}

	public void setMensagemBloquear(String mensagemBloquear) {
		this.mensagemBloquear = mensagemBloquear;
	}

	public Integer getDuracaoMensagem() {
		if (duracaoMensagem == null) {
			duracaoMensagem = 0;
		}
		return duracaoMensagem;
	}

	public void setDuracaoMensagem(Integer duracaoMensagem) {
		this.duracaoMensagem = duracaoMensagem;
	}

	public Integer getDuracaoDesbloquear() {
		if (duracaoDesbloquear == null) {
			duracaoDesbloquear = 0;
		}
		return duracaoDesbloquear;
	}

	public void setDuracaoDesbloquear(Integer duracaoDesbloquear) {
		this.duracaoDesbloquear = duracaoDesbloquear;
	}

	public String getDirecao() {
		if (direcao == null) {
			direcao = "";
		}
		return direcao;
	}

	public void setDirecao(String direcao) {
		this.direcao = direcao;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getSituacao_Apresentar() {
		return SituacaoEnum.valueOf(getSituacao()).getValorApresentar();
	}

}
