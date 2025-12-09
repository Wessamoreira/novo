package negocio.comuns.administrativo;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author Pedro
 */
public class AtendimentoInteracaoSolicitanteVO extends SuperVO {

	private Integer codigo;
	private AtendimentoVO atendimentoVO;
	private String questionamentoOuvidor;
	private String questionamentoSolicitante;
	private Date dataRegistro;
	private Date dataRegistroRespostaQuestionamento;
	private UsuarioVO usuarioQuestionamento;
	private UsuarioVO usuarioRespostaQuestionamento;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public AtendimentoVO getAtendimentoVO() {
		if (atendimentoVO == null) {
			atendimentoVO = new AtendimentoVO();
		}
		return atendimentoVO;
	}

	public void setAtendimentoVO(AtendimentoVO atendimentoVO) {
		this.atendimentoVO = atendimentoVO;
	}

	public String getQuestionamentoOuvidor() {
		if (questionamentoOuvidor == null) {
			questionamentoOuvidor = "";
		}
		return questionamentoOuvidor;
	}

	public void setQuestionamentoOuvidor(String questionamentoOuvidor) {
		this.questionamentoOuvidor = questionamentoOuvidor;
	}

	public String getQuestionamentoSolicitante() {
		if (questionamentoSolicitante == null) {
			questionamentoSolicitante = "";
		}
		return questionamentoSolicitante;
	}

	public void setQuestionamentoSolicitante(String questionamentoSolicitante) {
		this.questionamentoSolicitante = questionamentoSolicitante;
	}
	
	public String getDataRegistroApresentar(){
		return Uteis.getDataComHora(getDataRegistro());
	}

	public Date getDataRegistro() {
		if (dataRegistro == null) {
			dataRegistro = new Date();
		}
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}
	
	public String getRegistroRespostaQuestionamentoApresentar(){
		return Uteis.getDataComHora(getDataRegistroRespostaQuestionamento());
	}
	
	public Date getDataRegistroRespostaQuestionamento() {
		return dataRegistroRespostaQuestionamento;
	}

	public void setDataRegistroRespostaQuestionamento(Date dataRegistroRespostaQuestionamento) {
		this.dataRegistroRespostaQuestionamento = dataRegistroRespostaQuestionamento;
	}
		

	public Boolean getApresentarQuestionamentoSolicitante(){
		if(getDataRegistroRespostaQuestionamento() == null){
			return false;
		}
		return true;
	}
	
	public UsuarioVO getUsuarioQuestionamento() {
		if (usuarioQuestionamento == null) {
			usuarioQuestionamento = new UsuarioVO();
		}
		return usuarioQuestionamento;
	}
	
	public void setUsuarioQuestionamento(UsuarioVO usuarioQuestionamento) {
		this.usuarioQuestionamento = usuarioQuestionamento;
	}
	
	public UsuarioVO getUsuarioRespostaQuestionamento() {
		if (usuarioRespostaQuestionamento == null) {
			usuarioRespostaQuestionamento = new UsuarioVO();
		}
		return usuarioRespostaQuestionamento;
	}
	
	public void setUsuarioRespostaQuestionamento(UsuarioVO usuarioRespostaQuestionamento) {
		this.usuarioRespostaQuestionamento = usuarioRespostaQuestionamento;
	}

}
