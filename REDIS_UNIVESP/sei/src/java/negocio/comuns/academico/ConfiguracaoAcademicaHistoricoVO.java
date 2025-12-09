package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

public class ConfiguracaoAcademicaHistoricoVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private Date dataAlteracao;
	private UsuarioVO usuarioVO;
	private HistoricoVO historicoVO;
	private ConfiguracaoAcademicoVO configuracaoAntiga;
	private ConfiguracaoAcademicoVO configuracaoAtualizada;

	/*
	 * Atributo que seleciona os alunos para modificar sua Configuração Academica
	 */
	private Boolean selecionarConfiguracaoAcademicaHistorico;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Date getDataAlteracao() {
		if (dataAlteracao == null) {
			dataAlteracao = new Date();
		}
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
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



	public HistoricoVO getHistoricoVO() {
		if (historicoVO == null) {
			historicoVO = new HistoricoVO();
		}
		return historicoVO;
	}

	public void setHistoricoVO(HistoricoVO historicoVO) {
		this.historicoVO = historicoVO;
	}

	public Boolean getSelecionarConfiguracaoAcademicaHistorico() {
		if (selecionarConfiguracaoAcademicaHistorico == null) {
			selecionarConfiguracaoAcademicaHistorico = Boolean.TRUE;
		}
		return selecionarConfiguracaoAcademicaHistorico;
	}

	public void setSelecionarConfiguracaoAcademicaHistorico(Boolean selecionarConfiguracaoAcademicaHistorico) {
		this.selecionarConfiguracaoAcademicaHistorico = selecionarConfiguracaoAcademicaHistorico;
	}

	public ConfiguracaoAcademicoVO getConfiguracaoAntiga() {
		if (configuracaoAntiga == null) {
			configuracaoAntiga = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAntiga;
	}

	public void setConfiguracaoAntiga(ConfiguracaoAcademicoVO configuracaoAntiga) {
		this.configuracaoAntiga = configuracaoAntiga;
	}

	public ConfiguracaoAcademicoVO getConfiguracaoAtualizada() {
		if (configuracaoAtualizada == null) {
			configuracaoAtualizada = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAtualizada;
	}

	public void setConfiguracaoAtualizada(ConfiguracaoAcademicoVO configuracaoAtualizada) {
		this.configuracaoAtualizada = configuracaoAtualizada;
	}

}
