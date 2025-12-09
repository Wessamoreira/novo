/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum TipoDestinatarioComunicadoInterno {

        ALUNO("AL", "Aluno"),
        PROFESSOR("PR", "Professor"),
        DEPARTAMENTO("DE", "Departamento"),
        CARGO("CA", "Cargo"),
        AREA_CONHECIMENTO("AR", "Área Conhecimento"),
        TURMA("TU", "Turma"),
        COORDENADORES("CO", "Coordenadores"),
        FUNCIONARIO("FU", "Funcionário");
    
    String valor;
    String descricao;

    TipoDestinatarioComunicadoInterno(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TipoDestinatarioComunicadoInterno getEnum(String valor) {
        TipoDestinatarioComunicadoInterno[] valores = values();
        for (TipoDestinatarioComunicadoInterno obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoDestinatarioComunicadoInterno obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
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
}
