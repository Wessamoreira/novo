/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

/**
 *
 * @author Diego
 */
public enum TipoOrigemContaReceber {

    INSCRICAO_PROCESSO_SELETIVO("IPS", "Inscrição Processo Seletivo"),
    MATRICULA("MAT", "Matrícula"),
    REQUERIMENTO("REQ", "Requerimento"),
    BIBLIOTECA("BIB", "Biblioteca"),
    MENSALIDADE("MEN", "Mensalidade/Parcela"),
    DEVOLUCAO_CHEQUE("DCH", "Devolução de Cheque"),
    NEGOCIACAO("NCR","Negociação"),
    BOLSA_CUSTEADA_CONVENIO("BCC","Bolsa Custeada Convênio"),
    CONTRATO_RECEITA("CTR", "Contrato Receita"),
    OUTROS("OUT", "Outros"),
    INCLUSAOREPOSICAO("IRE", "Inclusão/Reposição"),
    MATERIAL_DIDATICO("MDI", "Material Didático");
    
    public String valor;
    public String descricao;

    TipoOrigemContaReceber(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TipoOrigemContaReceber getEnum(String valor) {
        TipoOrigemContaReceber[] valores = values();
        for (TipoOrigemContaReceber obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static TipoOrigemContaReceber getEnumPorName(String valor) {
    	TipoOrigemContaReceber[] valores = values();
    	for (TipoOrigemContaReceber obj : valores) {
    		if (obj.name().equals(valor)) {
    			return obj;
    		}
    	}
    	return null;
    }

    public static String getDescricao(String valor) {
        TipoOrigemContaReceber obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }
    
    public static List<SelectItem> getListaSelectItemTipoOrigemContaReceberVOs() {
    	List<SelectItem> itens = new ArrayList<SelectItem>(0);
    	itens.add(new SelectItem("", ""));
        TipoOrigemContaReceber[] valores = values();
        for (TipoOrigemContaReceber obj : valores) {
        	itens.add(new SelectItem(obj, obj.getDescricao()));
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
    
    public boolean isMatricula(){
    	return this.name() != null && this.name().equals(TipoOrigemContaReceber.MATRICULA.name());
    }
    
    public boolean isIncricaoProcessoSeletivo(){
    	return this.name() != null && this.name().equals(TipoOrigemContaReceber.INSCRICAO_PROCESSO_SELETIVO.name());
    }
    
    public boolean isRequerimento(){
    	return this.name() != null && this.name().equals(TipoOrigemContaReceber.REQUERIMENTO.name());
    }
    
    public boolean isBiblioteca(){
    	return this.name() != null && this.name().equals(TipoOrigemContaReceber.BIBLIOTECA.name());
    }
    
    public boolean isDevolucaoCheque(){
    	return this.name() != null && this.name().equals(TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.name());
    }
    
    public boolean isMaterialDidatico(){
    	return this.name() != null && this.name().equals(TipoOrigemContaReceber.MATERIAL_DIDATICO.name());
    }
    
    public boolean isNegociacao(){
    	return this.name() != null && this.name().equals(TipoOrigemContaReceber.NEGOCIACAO.name());
    }
    
    public boolean isContratoReceita(){
    	return this.name() != null && this.name().equals(TipoOrigemContaReceber.CONTRATO_RECEITA.name());
    }
    
    public boolean isBolsaCusteadaConvenio(){
    	return this.name() != null && this.name().equals(TipoOrigemContaReceber.BOLSA_CUSTEADA_CONVENIO.name());
    }
    
    public boolean isOutros(){
    	return this.name() != null && this.name().equals(TipoOrigemContaReceber.OUTROS.name());
    }
    
    public boolean isInclusaoReposicao(){
    	return this.name() != null && this.name().equals(TipoOrigemContaReceber.INCLUSAOREPOSICAO.name());
    }
    
    public boolean isMensalidade(){
    	return this.name() != null && this.name().equals(TipoOrigemContaReceber.MENSALIDADE.name());
    }
}
