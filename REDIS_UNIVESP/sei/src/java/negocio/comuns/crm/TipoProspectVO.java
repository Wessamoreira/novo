package negocio.comuns.crm;

public class TipoProspectVO {
    private Integer codigo;
    private String nome;
    private String nomeBatismo;
    private String  perfisProspect;
    private String email;
    private String cnpj;
    private String cpf;

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getPerfisProspect() {
        if (perfisProspect == null) {
            perfisProspect = "";
        }
        return perfisProspect;
    }

    public void setPerfisProspect(String perfisProspect) {
        this.perfisProspect = perfisProspect;
    }

    public String getCnpj() {
        if (cnpj == null) {
            cnpj = "";
        }
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (cpf == null) {
            cpf = "";
        }
        this.cpf = cpf;
    }

    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getNomeBatismo() {
        if (nomeBatismo == null) {
        	nomeBatismo = "";
        }
        return nomeBatismo;
    }

    public void setNomeBatismo(String nomeBatismo) {
        this.nomeBatismo = nomeBatismo;
    }

	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
