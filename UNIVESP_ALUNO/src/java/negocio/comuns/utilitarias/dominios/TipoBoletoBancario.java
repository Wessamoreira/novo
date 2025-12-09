/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum TipoBoletoBancario {

    MATRICULA("MA", "Matrícula", "modeloBoletoMatricula"),
    MENSALIDADE("ME", "Mensalidade", "modeloBoletoMensalidade"),
    MATERIAL_DIDATICO("MD", "Material Didático", "modeloBoletoMaterialDidatico"),
    PROCESSO_SELETIVO("PC", "Processo Seletivo", "modeloBoletoProcessoSeletivo"),
    REQUERIMENTO("RE", "Requerimento", "modeloBoletoRequerimento"),
    RENEGOCIACAO("NCR", "Renegociacao", "modeloBoletoRenegociacao"),
	BIBLIOTECA("BIB", "Biblioteca", "modeloBoletoBiblioteca"),
    OUTROS("OU", "Outros", "modeloBoletoOutros");
    
    String valor;
    String descricao;
    private String nomeNaBase;

    TipoBoletoBancario(String valor, String descricao, String nomeNaBase) {
        this.valor = valor;
        this.descricao = descricao;
        this.nomeNaBase = nomeNaBase;
    }

    public static TipoBoletoBancario getEnum(String valor) {
        TipoBoletoBancario[] valores = values();
        for (TipoBoletoBancario obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoBoletoBancario obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }

    public static String getNomeNaBase(String valor){
        TipoBoletoBancario obj = getEnum(valor);
        if (obj != null){
            return obj.getNomeNaBase();
        }
        return valor;
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

    /**
     * @return the nomeNaBase
     */
    public String getNomeNaBase() {
        return nomeNaBase;
    }

    /**
     * @param nomeNaBase the nomeNaBase to set
     */
    public void setNomeNaBase(String nomeNaBase) {
        this.nomeNaBase = nomeNaBase;
    }
}
