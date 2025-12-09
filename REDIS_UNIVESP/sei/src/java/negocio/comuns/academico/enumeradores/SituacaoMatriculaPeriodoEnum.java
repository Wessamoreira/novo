package negocio.comuns.academico.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public enum SituacaoMatriculaPeriodoEnum {

	PRE_MATRICULA("PR", "Pré-Matricula", "da Pré-Matricula"),
	PRE_MATRICULA_CANCELADA("PC", "Pré-Matricula Cancelada", "da Pré-Matricula Cancelada"),
	ABANDONO_CURSO("AC", "Abandono de Curso", "do Abandono de Curso"),
	TRANCADA("TR", "Trancada", "do Trancamento"),
	CANCELADA("CA", "Cancelada", "do Cancelamento"),
	ATIVA("AT", "Ativa", "da Ativação"),
	FINALIZADA("FI", "Concluído", "da Conclusão"),
	FORMADO("FO", "Formado", "da Formatura"),
	TRANFERENCIA_SAIDA("TS", "Transferência de Saída", "da Transfeência de Saída"),
	TRANFERENCIA_INTERNA("TI", "Transferência Interna", "da Tranferência Interna"),
	INDEFINIDA("ER", "Problema Importação", "do Problema de Importação"),
	JUBILADO("JU", "Jubilado", "do Jubilamento");
	
	private String valor;
	private String descricao;
	private String descricaoComPrefixo;
	
	private SituacaoMatriculaPeriodoEnum(String valor, String descricao, String descricaoComPrefixo) {
		this.valor = valor;
		this.descricao = descricao;
		this.descricaoComPrefixo = descricaoComPrefixo;
	}
	
	public static SituacaoMatriculaPeriodoEnum getEnumPorValor(String valor){
		for(SituacaoMatriculaPeriodoEnum situacaoMatriculaPeriodoEnum: SituacaoMatriculaPeriodoEnum.values()){
			if(situacaoMatriculaPeriodoEnum.getValor().equalsIgnoreCase(valor)){
				return situacaoMatriculaPeriodoEnum;
			}
		}
		return null;
	}
	
	public static SituacaoMatriculaPeriodoEnum getEnumPorDescricao(String valor){
		for(SituacaoMatriculaPeriodoEnum situacaoMatriculaPeriodoEnum:SituacaoMatriculaPeriodoEnum.values()){
			if(situacaoMatriculaPeriodoEnum.getValor().equalsIgnoreCase(valor)){
				return situacaoMatriculaPeriodoEnum;
			}
		}
		return null;
	}
	

	public static String getDescricao(String valor){
		for(SituacaoMatriculaPeriodoEnum situacaoMatriculaPeriodoEnum:SituacaoMatriculaPeriodoEnum.values()){
			if(situacaoMatriculaPeriodoEnum.getValor().equalsIgnoreCase(valor)){
				return situacaoMatriculaPeriodoEnum.getDescricao();
			}
		}
		return "";
	}
	
	public static List<SelectItem> getListaSelectItemSituacaoMatriculaPeriodo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		for(SituacaoMatriculaPeriodoEnum situacaoMatriculaPeriodoEnum : SituacaoMatriculaPeriodoEnum.values()){
			itens.add(new SelectItem(situacaoMatriculaPeriodoEnum, situacaoMatriculaPeriodoEnum.getDescricao()));
		}
		return itens;
	}
	
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricaoComPrefixo() {		
		return descricaoComPrefixo;
	}

	public void setDescricaoComPrefixo(String descricaoComPrefixo) {
		this.descricaoComPrefixo = descricaoComPrefixo;
	}
	
	/**
	 * Esse boolean retorna para o DEV se a situacaoMatriculaPeriodo no momento pode
	 * ser utilizada no histórico, no casos essas situações abaixo não serão
	 * utilizadas no histórico
	 * 
	 * @return
	 * @author Felipi Alves
	 */
	public boolean isSituacaoMatriculaPeriodoPresenteHistorico() {
		return !equals(PRE_MATRICULA) && !equals(PRE_MATRICULA_CANCELADA) && !equals(ATIVA) && !equals(FINALIZADA) && !equals(FORMADO) && !equals(INDEFINIDA);
	}
}
