package negocio.comuns.financeiro.enumerador;

import java.util.ArrayList;
import java.util.List;

import jakarta.faces. model.SelectItem;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.Bancos;

public enum TipoIdentificacaoChavePixEnum {
	
	
	TELEFONE("01","telefone"),
	EMAIL("02","email"),
	CPF_CNPJ("03","cpf/cnpj"),
	CHAVE_ALEATORIA("04","chave aleatoria"),
	DADOS_BANCARIOS("05","dados bancários");
	
	private String valor;
	private String descricao;
	
	
	
	
	private TipoIdentificacaoChavePixEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public boolean isTelefone(){
    	return  Uteis.isAtributoPreenchido(name()) && equals(TipoIdentificacaoChavePixEnum.TELEFONE);
    }
	
	public boolean isEmail(){
		return  Uteis.isAtributoPreenchido(name()) && equals(TipoIdentificacaoChavePixEnum.EMAIL);
	}
	
	public boolean isCpfCnpj(){
		return  Uteis.isAtributoPreenchido(name()) && equals(TipoIdentificacaoChavePixEnum.CPF_CNPJ);
	}
	
	public boolean isChaveAleatoria(){
		return  Uteis.isAtributoPreenchido(name()) && equals(TipoIdentificacaoChavePixEnum.CHAVE_ALEATORIA);
	}
	
	public boolean isDadosBancarios(){
		return  Uteis.isAtributoPreenchido(name()) && equals(TipoIdentificacaoChavePixEnum.DADOS_BANCARIOS);
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
	
	
}



