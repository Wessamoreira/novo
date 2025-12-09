package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

/**
 * Responsável por manter os dados referentes as <code>GradeDisciplinaCompostaVO</code> cujo determinadas matrículas devem cursar apenas algumas
 * disciplinas da composição.
 * 
 * @author Wellington - 19 de jan de 2016
 *
 */
public class TurmaDisciplinaCompostaVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private TurmaDisciplinaVO turmaDisciplinaVO;
	private GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO;
	private ConfiguracaoAcademicoVO configuracaoAcademicoVO;
	private boolean selecionado = false;
	private boolean editavel = false;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public TurmaDisciplinaVO getTurmaDisciplinaVO() {
		if (turmaDisciplinaVO == null) {
			turmaDisciplinaVO = new TurmaDisciplinaVO();
		}
		return turmaDisciplinaVO;
	}

	public void setTurmaDisciplinaVO(TurmaDisciplinaVO turmaDisciplinaVO) {
		this.turmaDisciplinaVO = turmaDisciplinaVO;
	}

	public GradeDisciplinaCompostaVO getGradeDisciplinaCompostaVO() {
		if (gradeDisciplinaCompostaVO == null) {
			gradeDisciplinaCompostaVO = new GradeDisciplinaCompostaVO();
		}
		return gradeDisciplinaCompostaVO;
	}

	public void setGradeDisciplinaCompostaVO(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO) {
		this.gradeDisciplinaCompostaVO = gradeDisciplinaCompostaVO;
	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	public boolean isEditavel() {
		return editavel;
	}

	public void setEditavel(boolean editavel) {
		this.editavel = editavel;
	}

	public ConfiguracaoAcademicoVO getConfiguracaoAcademicoVO() {
		if(configuracaoAcademicoVO == null) {
			configuracaoAcademicoVO =  new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademicoVO;
	}

	public void setConfiguracaoAcademicoVO(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		this.configuracaoAcademicoVO = configuracaoAcademicoVO;
	}
	
	
}
