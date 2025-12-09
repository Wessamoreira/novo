package negocio.comuns.processosel;

import javax.xml.bind.annotation.XmlElement;

import negocio.comuns.arquitetura.SuperVO;

public class ResultadoProcessoSeletivoProvaRespostaVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO;
	private OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivoMarcadaVO;

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

//	@XmlElement(name = "resultadoProcessoSeletivoVO")
	public ResultadoProcessoSeletivoVO getResultadoProcessoSeletivoVO() {
		if (resultadoProcessoSeletivoVO == null) {
			resultadoProcessoSeletivoVO = new ResultadoProcessoSeletivoVO();
		}
		return resultadoProcessoSeletivoVO;
	}

	public void setResultadoProcessoSeletivoVO(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO) {
		this.resultadoProcessoSeletivoVO = resultadoProcessoSeletivoVO;
	}

	@XmlElement(name = "opcaoRespostaQuestaoProcessoSeletivoMarcadaVO")
	public OpcaoRespostaQuestaoProcessoSeletivoVO getOpcaoRespostaQuestaoProcessoSeletivoMarcadaVO() {
		if (opcaoRespostaQuestaoProcessoSeletivoMarcadaVO == null) {
			opcaoRespostaQuestaoProcessoSeletivoMarcadaVO = new OpcaoRespostaQuestaoProcessoSeletivoVO();
		}
		return opcaoRespostaQuestaoProcessoSeletivoMarcadaVO;
	}

	public void setOpcaoRespostaQuestaoProcessoSeletivoMarcadaVO(OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivoMarcadaVO) {
		this.opcaoRespostaQuestaoProcessoSeletivoMarcadaVO = opcaoRespostaQuestaoProcessoSeletivoMarcadaVO;
	}
}
