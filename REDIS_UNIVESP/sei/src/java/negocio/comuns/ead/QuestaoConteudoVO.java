package negocio.comuns.ead;

import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.arquitetura.SuperVO;

/**
 * @author Victor Hugo 08/01/2014
 */
public class QuestaoConteudoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer codigo;
	private QuestaoVO questaoVO;
	private ConteudoVO conteudoVO;
	
	@Override
	public QuestaoConteudoVO clone() throws CloneNotSupportedException {
		QuestaoConteudoVO clone = (QuestaoConteudoVO) super.clone();
		clone.setCodigo(0);
        clone.setNovoObj(true);
        clone.setQuestaoVO(new QuestaoVO());
		return clone;
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
	public QuestaoVO getQuestaoVO() {
		if (questaoVO == null) {
			questaoVO = new QuestaoVO();
		}
		return questaoVO;
	}
	public void setQuestaoVO(QuestaoVO questaoVO) {
		this.questaoVO = questaoVO;
	}
	public ConteudoVO getConteudoVO() {
		if (conteudoVO == null) {
			conteudoVO = new ConteudoVO();
		}
		return conteudoVO;
	}
	public void setConteudoVO(ConteudoVO conteudoVO) {
		this.conteudoVO = conteudoVO;
	}
}
