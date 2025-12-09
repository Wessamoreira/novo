package negocio.comuns.administrativo;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author Pedro
 */
public class AtendimentoInteracaoDepartamentoVO extends SuperVO {

	private Integer codigo;
	private AtendimentoVO atendimentoVO;
	private String questionamento;
	private String resposta;
	private Date dataRegistro;
	private Date dataRegistroResposta;	
	private FuncionarioVO funcionarioVO;
	private DepartamentoVO departamento;
	private Boolean atendimentoAtrasado;
	private Boolean mensagemEnviada; 
	private UsuarioVO usuarioquestionamento;
	private UsuarioVO usuariorespostaquestionamento;
	
	//Campos nao persistdo na base de dados somente usado para controle de tela
	private Date dataRegistroRespostaTemp;

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

	public void setAtendimentoVO(AtendimentoVO atendimento) {
		this.atendimentoVO = atendimento;
	}

	public String getQuestionamento() {
		if (questionamento == null) {
			questionamento = "";
		}
		return questionamento;
	}

	public void setQuestionamento(String questionamento) {
		this.questionamento = questionamento;
	}

	public String getResposta() {
		if (resposta == null) {
			resposta = "";
		}
		return resposta;
	}

	public void setResposta(String resposta) {
		this.resposta = resposta;
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
	
	public String getDataRegistroRespostaApresentar(){
		return Uteis.getDataComHora(getDataRegistroResposta());
	}

	public Date getDataRegistroResposta() {
		if (dataRegistroResposta == null) {
			dataRegistroResposta = new Date();
		}
		return dataRegistroResposta;
	}

	public void setDataRegistroResposta(Date dataRegistroResposta) {
		this.dataRegistroResposta = dataRegistroResposta;
	}

	public FuncionarioVO getFuncionarioVO() {
		if (funcionarioVO == null) {
			funcionarioVO = new FuncionarioVO();
		}
		return funcionarioVO;
	}

	public void setFuncionarioVO(FuncionarioVO funcionarioCargoVO) {
		this.funcionarioVO = funcionarioCargoVO;
	}
	
	
	
	public DepartamentoVO getDepartamento() {
		if(departamento == null) {
			departamento = new DepartamentoVO();
		}
		return departamento;
	}

	public void setDepartamento(DepartamentoVO departamento) {
		this.departamento = departamento;
	}

	public Date getDataRegistroRespostaTemp() {
		return dataRegistroRespostaTemp;
	}

	public void setDataRegistroRespostaTemp(Date dataRegistroRespostaTemp) {
		this.dataRegistroRespostaTemp = dataRegistroRespostaTemp;
	}

	public Boolean getExisteDataRegistroRespostaTem(){
		if(getDataRegistroRespostaTemp() == null){
			return false;
		}
		return true;
	}
	
	public Boolean getExisteDataRegistroResposta(){
		if(getDataRegistroResposta() == null){
			return false;
		}
		return true;
	}

	public Boolean getAtendimentoAtrasado() {
		if(atendimentoAtrasado == null){
			atendimentoAtrasado = false;
		}
		return atendimentoAtrasado;
	}

	public void setAtendimentoAtrasado(Boolean atendimentoAtrasado) {
		this.atendimentoAtrasado = atendimentoAtrasado;
	}
	
	public Boolean getMensagemEnviada() {
		if(mensagemEnviada == null){
			mensagemEnviada = false;
		}
		return mensagemEnviada;
	}

	public void setMensagemEnviada(Boolean mensagemEnviada) {
		this.mensagemEnviada = mensagemEnviada;
	}
	
	public UsuarioVO getUsuarioquestionamento() {
		if (usuarioquestionamento == null) {
			usuarioquestionamento = new UsuarioVO();
		}
		return usuarioquestionamento;
	}
	
	public void setUsuarioquestionamento(UsuarioVO usuarioquestionamento) {
		this.usuarioquestionamento = usuarioquestionamento;
	}
	
	public UsuarioVO getUsuariorespostaquestionamento() {
		if (usuariorespostaquestionamento == null) {
			usuariorespostaquestionamento = new UsuarioVO();
		}
		return usuariorespostaquestionamento;
	}
	
	public void setUsuariorespostaquestionamento(UsuarioVO usuariorespostaquestionamento) {
		this.usuariorespostaquestionamento = usuariorespostaquestionamento;
	}

}
