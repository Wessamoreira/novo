package negocio.comuns.moodle;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoOperacaoMoodleEnum;
import webservice.moodle.MensagensRSVO;
import webservice.moodle.NotasRSVO;

@SuppressWarnings("serial")
public class OperacaoMoodleVO extends SuperVO {

	private Integer codigo;
	private String jsonMoodle;
	private Boolean processado;
	private String erro;
	private TipoOperacaoMoodleEnum tipoOperacao;
	// atributo transient
	private MensagensRSVO mensagensRSVO;
	private NotasRSVO notasRSVO;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getJsonMoodle() {
		if (jsonMoodle == null) {
			jsonMoodle = Constantes.EMPTY;
		}
		return jsonMoodle;
	}

	public void setJsonMoodle(String jsonMoodle) {
		this.jsonMoodle = jsonMoodle;
	}

	public Boolean getProcessado() {
		if (processado == null) {
			processado = Boolean.FALSE;
		}
		return processado;
	}

	public void setProcessado(Boolean processado) {
		this.processado = processado;
	}

	public String getErro() {
		if (erro == null) {
			erro = Constantes.EMPTY;
		}
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

	public TipoOperacaoMoodleEnum getTipoOperacao() {
		return tipoOperacao;
	}

	public void setTipoOperacao(TipoOperacaoMoodleEnum tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	public MensagensRSVO getMensagensRSVO() {
		if (mensagensRSVO == null) {
			mensagensRSVO = new MensagensRSVO();
		}
		return mensagensRSVO;
	}

	public void setMensagensRSVO(MensagensRSVO mensagensRSVO) {
		this.mensagensRSVO = mensagensRSVO;
	}
	
	public NotasRSVO getNotasRSVO() {
		if (notasRSVO == null) {
			notasRSVO = new NotasRSVO();
		}
		return notasRSVO;
	}
	
	public void setNotasRSVO(NotasRSVO notasRSVO) {
		this.notasRSVO = notasRSVO;
	}

	public boolean isTipoOperacaoEnvioMensagens() {
		return Uteis.isAtributoPreenchido(getTipoOperacao()) && getTipoOperacao().equals(TipoOperacaoMoodleEnum.MENSAGENS);
	}

	public boolean isTipoOperacaoEnvioNotas() {
		return Uteis.isAtributoPreenchido(getTipoOperacao()) && getTipoOperacao().equals(TipoOperacaoMoodleEnum.NOTAS);
	}
	
	public boolean isOperacaoComErro() {
		return getProcessado() && Uteis.isAtributoPreenchido(getErro());
	}
}