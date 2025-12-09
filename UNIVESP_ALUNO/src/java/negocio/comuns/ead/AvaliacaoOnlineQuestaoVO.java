package negocio.comuns.ead;

import negocio.comuns.arquitetura.SuperVO;

/**
 * @author Victor Hugo 10/10/2014
 */
public class AvaliacaoOnlineQuestaoVO extends SuperVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private AvaliacaoOnlineVO avaliacaoOnlineVO;
	private QuestaoVO questaoVO;
	private Double nota;
	private Integer ordemApresentacao;
	
	public AvaliacaoOnlineQuestaoVO clone() throws CloneNotSupportedException {
		AvaliacaoOnlineQuestaoVO clone = (AvaliacaoOnlineQuestaoVO) super.clone();
		clone.setCodigo(0);
		clone.setNovoObj(true);
		clone.setAvaliacaoOnlineVO(new AvaliacaoOnlineVO());
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
	public AvaliacaoOnlineVO getAvaliacaoOnlineVO() {
		if (avaliacaoOnlineVO == null) {
			avaliacaoOnlineVO = new AvaliacaoOnlineVO();
		}
		return avaliacaoOnlineVO;
	}
	public void setAvaliacaoOnlineVO(AvaliacaoOnlineVO avaliacaoOnlineVO) {
		this.avaliacaoOnlineVO = avaliacaoOnlineVO;
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
	public Double getNota() {
		if (nota == null) {
			nota = 0.00;
		}
		return nota;
	}
	public void setNota(Double nota) {
		this.nota = nota;
	}
	public Integer getOrdemApresentacao() {
		if (ordemApresentacao == null) {
			ordemApresentacao = 0;
		}
		return ordemApresentacao;
	}
	public void setOrdemApresentacao(Integer ordemApresentacao) {
		this.ordemApresentacao = ordemApresentacao;
	}
}
