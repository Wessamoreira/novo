package negocio.comuns.basico;


/**
 * Reponsável por manter os dados da entidade Empresa. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see SuperEmpresaVO
 */
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;

import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

public class EmpresaVO extends SuperEmpresaVO implements Serializable {

	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)	
	protected String tipoEmpresa;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)	
	protected String RG;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)	
	protected String CPF;
	protected Boolean permiteenviarremessa;        
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>Empresa</code>. Cria uma nova instância
     * desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public EmpresaVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>EmpresaVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(EmpresaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Empresa) deve ser informado.");
        }
        if (obj.getRazaoSocial().equals("")) {
            throw new ConsistirException("O campo RAZÃO SOCIAL (Empresa) deve ser informado.");
        }
        if (obj.getEndereco().equals("")) {
            throw new ConsistirException("O campo ENDEREÇO (Empresa) deve ser informado.");
        }
        if (obj.getCidade().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo CIDADE (Empresa) deve ser informado.");
        }
        if (obj.getCNPJ().equals("")) {
            throw new ConsistirException("O campo CNPJ (Empresa) deve ser informado.");
        }
    }

    public String getCPF() {
        if (CPF == null) {
            CPF = "";
        }
        return (CPF);
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getRG() {
        if (RG == null) {
            RG = "";
        }
        return (RG);
    }

    public void setRG(String RG) {
        this.RG = RG;
    }

    public String getTipoEmpresa() {
        if (tipoEmpresa == null) {
            tipoEmpresa = "JU";
        }
        return (tipoEmpresa);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoEmpresa_Apresentar() {
        if (tipoEmpresa.equals("FI")) {
            return "Física";
        }
        if (tipoEmpresa.equals("JU")) {
            return "Jurídica";
        }
        return (tipoEmpresa);
    }

    public void setTipoEmpresa(String tipoEmpresa) {
        this.tipoEmpresa = tipoEmpresa;
    }
    
    public String getCnpjOuCfp_Apresentar() {
    	return Uteis.isAtributoPreenchido(getTipoEmpresa()) && getTipoEmpresa().equals("JU") ? getCNPJ(): getCPF();
    }
    
    public String getMascaraCnpjOuCfp() {
    	return Uteis.isAtributoPreenchido(getTipoEmpresa()) && getTipoEmpresa().equals("JU") ? "return mascara(this.form, '99.999.999/9999-99', event);": "return mascara(this.form, '999.999.999-99', event);";
	}
        
	public Boolean getPermiteenviarremessa() {
		if (permiteenviarremessa == null) {
			permiteenviarremessa = Boolean.TRUE;
		}
		return permiteenviarremessa;
	}

	public void setPermiteenviarremessa(Boolean permiteenviarremessa) {
		this.permiteenviarremessa = permiteenviarremessa;
	}

}
