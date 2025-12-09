/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum TipoPessoa {

    ALUNO("AL", "Aluno"),
    CANDIDATO("CA", "Candidato"),
    MEMBRO_COMUNIDADE("MC", "Membro Comunidade"),
    PROFESSOR("PR", "Professor"),
    REQUERENTE("RE", "Requerente"),
    PARCEIRO("PA", "Parceiro"),
    RESPONSAVEL_LEGAL("RL", "Responsável legal"),
    FUNCIONARIO("FU", "Funcionário"),
    FORNECEDOR("FO", "Fornecedor"),
    RESPONSAVEL_FINANCEIRO("RF", "Responsável Financeiro"),
    COORDENADOR_CURSO("CC", "Coordenador Curso"),
    NENHUM("NE", "Nenhum");
    
    String valor;
    String descricao;

    TipoPessoa(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TipoPessoa getEnum(String valor) {
        TipoPessoa[] valores = values();
        for (TipoPessoa obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoPessoa obj = getEnum(valor);
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
    
    public boolean isAluno(){
    	return name().equals(TipoPessoa.ALUNO.name());
    }
    
    public boolean isFuncionario(){
    	return name().equals(TipoPessoa.FUNCIONARIO.name());
    }
    
    public boolean isProfessor(){
    	return name().equals(TipoPessoa.PROFESSOR.name());
    }
    
    public boolean isParceiro(){
    	return name().equals(TipoPessoa.PARCEIRO.name());
    }
    
    public boolean isFornecedor(){
    	return name().equals(TipoPessoa.FORNECEDOR.name());
    }
    
    public boolean isMembroComunidade(){
    	return name().equals(TipoPessoa.MEMBRO_COMUNIDADE.name());
    }
}
