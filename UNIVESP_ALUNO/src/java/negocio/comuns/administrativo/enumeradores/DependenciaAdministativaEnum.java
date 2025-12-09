package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;

public enum DependenciaAdministativaEnum {

	FEDERAL("FE","Federal",1),
	ESTADUAL("ES","Estadual",2),
	MUNICIPAL("MU","Municipal",3),
	PRIVADA("PR","Privada",4);
	
	
	public static Integer getCodigo(String valor) {
		DependenciaAdministativaEnum[] valores = values();
        for (DependenciaAdministativaEnum obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj.getCodigoCenso();
            }
        }
        return null;
    }
	
	
	private DependenciaAdministativaEnum(String valor, String descricao, int codigoCenso) {
		this.valor = valor;
		this.descricao = descricao;
		this.codigoCenso = codigoCenso;
	}
	
	
	String valor;
    String descricao;
    int codigoCenso;
    
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
	public int getCodigoCenso() {
		return codigoCenso;
	}
	public void setCodigoCenso(int codigoCenso) {
		this.codigoCenso = codigoCenso;
	}
	
	
}
