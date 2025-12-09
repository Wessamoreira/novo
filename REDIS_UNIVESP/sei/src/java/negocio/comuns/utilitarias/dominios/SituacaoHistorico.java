package negocio.comuns.utilitarias.dominios;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author Diego
 */
public enum SituacaoHistorico {

    REPROVADO_FALTA("RF", " Reprovado Falta", false, true, false),
    //DEPENDENCIA("DP", " Dependência"), // nao usa a priori
    REPROVADO_PERIODO_LETIVO("RP", " Reprovado no Período Letivo", false, true, false),
    APROVADO("AP", " Aprovado", true, false, false),
    REPROVADO("RE", " Reprovado", false, true, false),
    APROVADO_APROVEITAMENTO("AA", " Aprovado por Aproveitamento", true, false, false),
    APROVADO_POR_EQUIVALENCIA("AE", " Aprovado por Equivalência", true, false, false),
    //VERIFICACAO_SUPLEMENTAR("VS", " Verificação Suplementar"), // nao usa a priori
    ISENTO("IS", " Isento", true, false, false),
    NAO_CURSADA("NC", " Não Cursada", false, false, false), // utilizada somente para um relatório...
    CURSANDO("CS", "Cursando", false, false, true),
    CURSANDO_POR_EQUIVALENCIA("CE", "Cursando por Equivalência", false, false, true),
    // POR CORRESPONDENCIA INDICA QUE O ALUNO ESTÁ ESTUDANDO UMA DISCIPLINA NUMA GRADE É IDENTICA
    // A DISCIPLINA DE SUA GRADE ATUAL E QUANDO ELE FINALIZAR NESTA GRADE DE ESTUDO, O HISTORICO DA GRADE
    // ATUAL SERÁ ATUALIZADO AUTOMATICAMENTE. UTILIZADO NA TRANSF. MATRIZ CURRICULAR
    CURSANDO_POR_CORRESPONDENCIA("CO", "Cursando por Correspondência", false, false, true),
    TRANCAMENTO("TR", "Trancado", false, false, false),
    ABANDONO_CURSO("AC", "Abandono de Curso", false, false, false),
    CANCELADO("CA", "Cancelado", false, false, false),
    TRANSFERIDO("TF", "Transferido", false, false, false),
    CONCESSAO_CREDITO("CC", "Crédito Concedido", true, false, false),
    CONCESSAO_CARGA_HORARIA("CH", "Carga Horária Concedida", true, false, false),
    PRE_MATRICULA("PR", "Pré-Matriculado", false, false, false),
    /**
     * Situacao Utilizada Somente na Emissão do Historico
     */
    APROVADO_COM_DEPENDENCIA("AD", "Aprov. c/ Dep.", true, false, false),
    APROVEITAMENTO_BANCA("AB", "Aproveitamento por Banca", true, false, false),
    JUBILADO("JU", "Jubilado", false, false, false);
    
    String valor;
    String descricao;
    Boolean historicoAprovado;
    Boolean historicoReprovado;
    private Boolean historicoCursando;

    SituacaoHistorico(String valor, String descricao, Boolean historicoAprovado, Boolean historicoReprovado, Boolean historicoCursando) {
        this.valor = valor;
        this.descricao = descricao;
        this.historicoAprovado = historicoAprovado;
        this.historicoReprovado = historicoReprovado;
        this.historicoCursando = historicoCursando;
    }

    public static SituacaoHistorico getEnum(String valor) {
        SituacaoHistorico[] valores = values();
        for (SituacaoHistorico obj : valores) {
            if (obj.getValor().equals(valor) || obj.getDescricao().equals(valor)) {
                return obj;
            }
        }
        return null;
    }
	
    public static SituacaoHistorico getEnumPorString(String valor) {
        SituacaoHistorico[] valores = values();
        for (SituacaoHistorico obj : valores) {
            if (obj.toString().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        SituacaoHistorico obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }

    public static List<SelectItem> getSituacaoHistoricoEnum(Boolean vazio) {
        return montarListaSelectItem(SituacaoHistorico.values(), vazio);
    }
    
    public static List<SituacaoHistorico> getSituacoesDeReprovacao() {
        List<SituacaoHistorico> tagList = new ArrayList<SituacaoHistorico>();
        for (SituacaoHistorico obj : values()) {
            if (obj.getHistoricoReprovado()) {
                tagList.add(obj);
            }
        }
        return tagList;
    }    
    
    public static List<SituacaoHistorico> getSituacoesDeAprovacao() {
        List<SituacaoHistorico> tagList = new ArrayList<SituacaoHistorico>();
        for (SituacaoHistorico obj : values()) {
            if (obj.getHistoricoAprovado()) {
                tagList.add(obj);
            }
        }
        return tagList;
    }

    private static List<SelectItem> montarListaSelectItem(SituacaoHistorico[] situacao) {
        List<SelectItem> tagList = new ArrayList<SelectItem>();
        for (SituacaoHistorico sit : situacao) {
            tagList.add(new SelectItem(sit.valor, sit.getDescricao()));
        }
        return tagList;
    }
    
    private static List<SelectItem> montarListaSelectItem(SituacaoHistorico[] situacao, Boolean vazio) {
        List<SelectItem> tagList = new ArrayList<SelectItem>();
        if (vazio) {
        	tagList.add(new SelectItem("", ""));
        }
        for (SituacaoHistorico sit : situacao) {
            tagList.add(new SelectItem(sit, sit.getDescricao()));
        }
        return tagList;
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

    public Boolean getHistoricoAprovado() {
        return historicoAprovado;
    }

    /**
     * @return the historicoReprovado
     */
    public Boolean getHistoricoReprovado() {
        return historicoReprovado;
    }

    /**
     * @param historicoReprovado the historicoReprovado to set
     */
    public void setHistoricoReprovado(Boolean historicoReprovado) {
        this.historicoReprovado = historicoReprovado;
    }
    
    public Boolean getHistoricoCursando() {
        return historicoCursando;
    }
    
    public static List<SituacaoHistorico> getSituacoesDeCursando() {
        List<SituacaoHistorico> tagList = new ArrayList<SituacaoHistorico>();
        for (SituacaoHistorico obj : values()) {
            if (obj.getHistoricoCursando()) {
                tagList.add(obj);
            }
        }
        return tagList;
    }
    
    public boolean isAprovada() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoHistorico.APROVADO.name());
	}
    
    public boolean isAprovadaAproveitamento() {
    	return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoHistorico.APROVADO_APROVEITAMENTO.name());
    }
    
    public boolean isCursando() {
    	return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoHistorico.CURSANDO.name());
    }
    
    public boolean isNaoCursada() {
    	return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoHistorico.NAO_CURSADA.name());
    }
    
    public boolean isTrancamento() {
    	return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoHistorico.TRANCAMENTO.name());
    }
    
    public boolean isAbandonoCurso() {
    	return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoHistorico.ABANDONO_CURSO.name());
    }
    
    public boolean isCancelado() {
    	return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoHistorico.CANCELADO.name());
    }
    
    public boolean isTransferido() {
    	return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoHistorico.TRANSFERIDO.name());
    }
    
    public boolean isConcessaoCredito() {
    	return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoHistorico.CONCESSAO_CREDITO.name());
    }
    
    public boolean isConcessaoCargaHorario() {
    	return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoHistorico.CONCESSAO_CARGA_HORARIA.name());
    }
    
    public boolean isPreMatricula() {
    	return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoHistorico.PRE_MATRICULA.name());
    }
    
    public boolean isJubilado() {
    	return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoHistorico.JUBILADO.name());
    }
    
    
    

}
