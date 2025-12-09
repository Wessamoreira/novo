/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum TipoSacado {

    FORNECEDOR("FO", "Fornecedor"),
    BANCO("BA", "Banco"),
    ALUNO("AL", "Aluno"),
    PARCEIRO("PA", "Parceiro"),
    RESPONSAVEL_FINANCEIRO("RF", "Responsável Financeiro"),
    FUNCIONARIO_PROFESSOR("FU", "Funcionário"),
    REQUERENTE("RE", "Requerene"),
    CANDIDATO("CA", "Candidato"),
    OPERADORA_CARTAO("OC","Operadora Cartão");
    
    String valor;
    String descricao;

    TipoSacado(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TipoSacado getEnum(String valor) {
        TipoSacado[] valores = values();
        for (TipoSacado obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoSacado obj = getEnum(valor);
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
    
    public boolean isFornecedor(){
    	return name().equals(TipoSacado.FORNECEDOR.name());
    }
    
    public boolean isBanco(){
    	return name().equals(TipoSacado.BANCO.name());
    }
    
    public boolean isAluno(){
    	return name().equals(TipoSacado.ALUNO.name());
    }
    
    public boolean isResponsavelFinanceiro(){
    	return name().equals(TipoSacado.RESPONSAVEL_FINANCEIRO.name());
    }
    
    public boolean isParceiro(){
    	return name().equals(TipoSacado.PARCEIRO.name());
    }
    
    public boolean isRequerente(){
    	return name().equals(TipoSacado.REQUERENTE.name());
    }
    
    public boolean isCandidato(){
    	return name().equals(TipoSacado.CANDIDATO.name());
    }
    
    public boolean isFuncionario(){
    	return name().equals(TipoSacado.FUNCIONARIO_PROFESSOR.name());
    }
    
    public boolean isOperadoraCartao(){
    	return name().equals(TipoSacado.OPERADORA_CARTAO.name());
    }
}
