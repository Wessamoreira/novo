package negocio.comuns.recursoshumanos.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoDemissaoEnum {

	EMPREGADOR_COM_JUSTA_CAUSA("EMPREGADOR_COM_JUSTA_CAUSA", "Inic. Empregador com justa causa"),
	EMPREGADOR_SEM_JUSTA_CAUSA("EMPREGADOR_SEM_JUSTA_CAUSA", "Inic. Empregador sem justa causa"),
	EMPREGADO_COM_JUSTA_CAUSA("EMPREGADO_COM_JUSTA_CAUSA", "Inic. Empregado com justa causa"),
	EMPREGADO_SEM_JUSTA_CAUSA("EMPREGADO_SEM_JUSTA_CAUSA", "Inic. Empregado sem justa causa"),
	TRANSFERENCIA_COM_ONUS_CEDENTE("TRANSFERENCIA_COM_ONUS_CEDENTE", "Transferência com ônus p/ Cedente"),
	TRANSFERENCIA_SEM_ONUS_CEDENTE("TRANSFERENCIA_SEM_ONUS_CEDENTE", "Transferência sem ônus p/ Cedente"),
	REFORMA_RESERVA("REFORMA_RESERVA", "Reforma ou Transferência para Reserva"),
	FALECIMENTO("FALECIMENTO", "Falecimento"),
	RESCISAO_DETERMINADA_PELA_JUSTICA("RESCISAO_DETERMINADA_PELA_JUSTICA", "Rescisão determinada pela justica"),
	CULPA_RECIPROCA("CULPA_RECIPROCA", "Culpa Recíproca"),
	APOSENTADORIA_INVALIDEZ_ACIDENTE_TRABALHO("APOSENTADORIA_INVALIDEZ_ACIDENTE_TRABALHO", "Aposentadoria invalidez (Acidente Trabalho)"),
	APOSENTADORIA_INVALIDEZ_DOENCA("APOSENTADORIA_INVALIDEZ_DOENCA", "Aposentadoria invalidez (Doença)"),
	APOSENTADORIA_INVALIDEZ_OUTROS("APOSENTADORIA_INVALIDEZ_OUTROS", "Aposentadoria invalidez (Outros)"),
	APOSENTADORIA_ESPECIAL("APOSENTADORIA_ESPECIAL", "Aposentadoria Especial"),
	FALECIMENTO_ACIDENTE_TRABALHO("FALECIMENTO_ACIDENTE_TRABALHO", "Falecimento por acidente de trabalho"),
	FALECIMENTO_DOENCA("FALECIMENTO_DOENCA", "Falecimento por doença profissional"),
	APOSENTADORIA_IDADE_COM_RESCISAO("APOSTENTADORIA_IDADE_COM_RESCISAO", "Aposentadoria por Idade com rescisão contrato"),
	APOSENTADORIA_IDADE_SEM_RESCISAO("APOSTENTADORIA_IDADE_SEM_RESCISAO", "Aposentadoria por Idade sem rescisão contrato"),
	APOSENTADORIA_TEMPO_SERVICO_COM_RESCISAO("APOSTENTADORIA_TEMPO_SERVICO_COM_RESCISAO", "Aposentadoria por Tempo Serviço com rescisão contrato"),
	APOSENTADORIA_TEMPO_SERVICO_SEM_RESCISAO("APOSTENTADORIA_TEMPO_SERVICO_SEM_RESCISAO", "Aposentadoria por Tempo Serviço sem rescisão contrato"),
	MUDANCA_REGIME_TRABALHISTA("MUDANCA_REGIME_TRABALHISTA", "Mudanca de Regime Trabalhista"),
	RESCISAO_INDIRETA("RESCISAO_INDIRETA", "Rescisão Indireta"),
	TERMINO_CONTRATO_TRABALHO("TERMINO_CONTRATO_TRABALHO", "Termino Contrato de Trabalho"),
	APOSENTADORIA_COMPULSORIA("APOSENTADORIA_COMPULSORIA", "Aposentadoria Compulsória"),
	OUTROS_CASOS("OUTROS_CASOS", "Outros Casos");

	public static List<SelectItem> getTipoValorReferenciaEnum() {
		return montarListaSelectItem(TipoValorReferenciaEnum.values());
	}

	private static List<SelectItem> montarListaSelectItem(TipoValorReferenciaEnum[] modulos) {
		List<SelectItem> tagList = new ArrayList<>();
		for (TipoValorReferenciaEnum tag : modulos) {
			tagList.add(new SelectItem(tag, tag.getValorApresentar()));
		}
		return tagList;
	}

	String valor;
	String descricao;

	private TipoDemissaoEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoDemissaoEnum_" + this.name());
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return UteisJSF.internacionalizar("enum_TipoDemissaoEnum_" + this.valor);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}