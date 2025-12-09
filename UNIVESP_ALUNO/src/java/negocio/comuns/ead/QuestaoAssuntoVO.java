package negocio.comuns.ead;

import negocio.comuns.academico.TemaAssuntoVO;
import negocio.comuns.arquitetura.SuperVO;

public class QuestaoAssuntoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private QuestaoVO questaoVO;
	private TemaAssuntoVO temaAssuntoVO;
	
	@Override
	public QuestaoAssuntoVO clone() throws CloneNotSupportedException {
		QuestaoAssuntoVO clone = (QuestaoAssuntoVO) super.clone();
		clone.setCodigo(0);
        clone.setNovoObj(true);
        clone.setQuestaoVO(new QuestaoVO());
		return clone;
	}
	
	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public QuestaoVO getQuestaoVO() {
		if(questaoVO == null) {
			questaoVO = new QuestaoVO();
		}
		return questaoVO;
	}
	
	public void setQuestaoVO(QuestaoVO questaoVO) {
		this.questaoVO = questaoVO;
	}
	
	public TemaAssuntoVO getTemaAssuntoVO() {
		if(temaAssuntoVO == null) {
			temaAssuntoVO = new TemaAssuntoVO();
		}
		return temaAssuntoVO;
	}
	
	public void setTemaAssuntoVO(TemaAssuntoVO temaAssuntoVO) {
		this.temaAssuntoVO = temaAssuntoVO;
	}
	
	

}
