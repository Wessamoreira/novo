/**
 * 
 */
package negocio.comuns.patrimonio.enumeradores;

import negocio.comuns.basico.enumeradores.EntidadeTextoPadraoEnum;
import negocio.comuns.basico.enumeradores.TipoCampoTagTextoPadraoEnum;
import negocio.interfaces.basico.TagTextoPadraoInterfaceEnum;

/**
 * @author Rodrigo Wind
 *
 */
public enum TextoPadraoPatrimonioTagOcorrenciaPatrimonioEnum implements TagTextoPadraoInterfaceEnum {
	
	PATRIMONIO_OCORRENCIA_CODIGO(EntidadeTextoPadraoEnum.OCORRENCIA_PATRIMONIO, "codigo", "[(20){}PATRIMONIO_OCORRENCIA_CODIGO]", "Código Ocorrência", TipoCampoTagTextoPadraoEnum.INTEGER, null),
	PATRIMONIO_OCORRENCIA_DATA_OCORRENCIA(EntidadeTextoPadraoEnum.OCORRENCIA_PATRIMONIO, "dataOcorrencia", "[(14){}PATRIMONIO_OCORRENCIA_DATA_OCORRENCIA]", "Data Ocorrência", TipoCampoTagTextoPadraoEnum.DATA_COM_HORA, null),
	PATRIMONIO_OCORRENCIA_DATA_FINALIZACAO(EntidadeTextoPadraoEnum.OCORRENCIA_PATRIMONIO, "dataFinalizacao", "[(14){}PATRIMONIO_OCORRENCIA_DATA_FINALIZACAO]", "Data Finalização", TipoCampoTagTextoPadraoEnum.DATA_COM_HORA, null),
	PATRIMONIO_OCORRENCIA_DATA_PREVISAO(EntidadeTextoPadraoEnum.OCORRENCIA_PATRIMONIO, "dataPrevisao", "[(14){}PATRIMONIO_OCORRENCIA_DATA_PREVISAO]", "Data Previsão", TipoCampoTagTextoPadraoEnum.DATA_COM_HORA, null),
	PATRIMONIO_OCORRENCIA_TIPO_OCORRENCIA(EntidadeTextoPadraoEnum.OCORRENCIA_PATRIMONIO, "tipoOcorrenciaPatrimonio", "[(40){}PATRIMONIO_OCORRENCIA_TIPO_OCORRENCIA]", "Tipo Ocorrência", TipoCampoTagTextoPadraoEnum.ENUM, null),
	PATRIMONIO_OCORRENCIA_SITUACAO_OCORRENCIA(EntidadeTextoPadraoEnum.OCORRENCIA_PATRIMONIO, "situacao", "[(30){}PATRIMONIO_OCORRENCIA_SITUACAO_OCORRENCIA]", "Situação Ocorrência", TipoCampoTagTextoPadraoEnum.ENUM, null),
	PATRIMONIO_OCORRENCIA_RESPONSAVEL_OCORRENCIA(EntidadeTextoPadraoEnum.OCORRENCIA_PATRIMONIO, "responsavelOcorrencia.nome", "[(30){}PATRIMONIO_OCORRENCIA_RESPONSAVEL_OCORRENCIA]", "Resp. Ocorrência", TipoCampoTagTextoPadraoEnum.STRING, null),
	PATRIMONIO_OCORRENCIA_SOLICITANTE_EMPRESTIMO(EntidadeTextoPadraoEnum.OCORRENCIA_PATRIMONIO, "solicitanteEmprestimo.nome", "[(30){}PATRIMONIO_OCORRENCIA_SOLICITANTE_EMPRESTIMO]", "Solicitante Empréstimo", TipoCampoTagTextoPadraoEnum.STRING, null),
	PATRIMONIO_OCORRENCIA_OBSERVACAO(EntidadeTextoPadraoEnum.OCORRENCIA_PATRIMONIO, "observacao", "[(){}PATRIMONIO_OCORRENCIA_OBSERVACAO]", "Observação", TipoCampoTagTextoPadraoEnum.STRING, null),
	PATRIMONIO_OCORRENCIA_MOTIVO(EntidadeTextoPadraoEnum.OCORRENCIA_PATRIMONIO, "motivo", "[(){}PATRIMONIO_OCORRENCIA_MOTIVO]", "Motivo", TipoCampoTagTextoPadraoEnum.STRING, null),
	PATRIMONIO_OCORRENCIA_LOCAL_ORIGEM(EntidadeTextoPadraoEnum.OCORRENCIA_PATRIMONIO, "localArmazenamentoOrigem.descricao", "[(30){}PATRIMONIO_OCORRENCIA_LOCAL_ORIGEM]", "Local Origem", TipoCampoTagTextoPadraoEnum.STRING, null),
	PATRIMONIO_OCORRENCIA_LOCAL_DESTINO(EntidadeTextoPadraoEnum.OCORRENCIA_PATRIMONIO, "localArmazenamentoOrigem.descricao", "[(30){}PATRIMONIO_OCORRENCIA_LOCAL_DESTINO]", "Local Destino", TipoCampoTagTextoPadraoEnum.STRING, null),
	PATRIMONIO_OCORRENCIA_DATA_INICIO(EntidadeTextoPadraoEnum.OCORRENCIA_PATRIMONIO, "dataInicioReserva", "[(14){}PATRIMONIO_OCORRENCIA_DATA_INICIO]", "Data Inicio", TipoCampoTagTextoPadraoEnum.DATA_COM_HORA, null),
	PATRIMONIO_OCORRENCIA_DATA_TERMINO(EntidadeTextoPadraoEnum.OCORRENCIA_PATRIMONIO, "dataTerminoReserva", "[(14){}PATRIMONIO_OCORRENCIA_DATA_TERMINO]", "Data Termino", TipoCampoTagTextoPadraoEnum.DATA_COM_HORA, null),
	PATRIMONIO_OCORRENCIA_LOCAL_RESERVADO(EntidadeTextoPadraoEnum.OCORRENCIA_PATRIMONIO, "localReservado.descricao", "[(30){}PATRIMONIO_OCORRENCIA_LOCAL_RESERVADO]", "Local Reservado", TipoCampoTagTextoPadraoEnum.STRING, null),
	;

	private TextoPadraoPatrimonioTagOcorrenciaPatrimonioEnum(EntidadeTextoPadraoEnum entidade, String campo, String tag, String value,  TipoCampoTagTextoPadraoEnum tipoCampo, Enum<? extends TagTextoPadraoInterfaceEnum>[] subTags) {
		this.entidade = entidade;
		this.tag = tag;
		this.campo = campo;
		this.value = value;
		this.tipoCampo = tipoCampo;
		this.subTags = subTags;
	}
	
	private EntidadeTextoPadraoEnum entidade;
	private String tag;
	private String value;	
	private String campo;	
	private Enum<? extends TagTextoPadraoInterfaceEnum>[] subTags;
	private TipoCampoTagTextoPadraoEnum tipoCampo;
		
	public String getTag() {
		if (tag == null) {
			tag = "";
		}
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getCampo() {
		if (campo == null) {
			campo = "";
		}
		return campo;
	}
	public void setCampo(String campo) {
		this.campo = campo;
	}
	public String getValue() {
		if (value == null) {
			value = "";
		}
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	public Enum<? extends TagTextoPadraoInterfaceEnum>[] getSubTags() {		
		return subTags;
	}
	
	
	public void setSubTags(Enum<? extends TagTextoPadraoInterfaceEnum>[] subTags) {
		this.subTags = subTags;
	}
	
	public TipoCampoTagTextoPadraoEnum getTipoCampo() {		
		return tipoCampo;
	}
	public void setTipoCampo(TipoCampoTagTextoPadraoEnum tipoCampo) {
		this.tipoCampo = tipoCampo;
	}
	public EntidadeTextoPadraoEnum getEntidade() {	
		return entidade;
	}
	public void setEntidade(EntidadeTextoPadraoEnum entidade) {
		this.entidade = entidade;
	}
	
	
	
	

}
