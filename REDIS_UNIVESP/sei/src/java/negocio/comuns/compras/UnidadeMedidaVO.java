package negocio.comuns.compras;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;


public class UnidadeMedidaVO extends SuperVO {

    private Integer codigo;
    private String nome;
    private String descricao;
    private String sigla;
    private boolean fracionado = false;
    public static final long serialVersionUID = 1L;

   
    public UnidadeMedidaVO() {
    }

    
    public static void validarDados(UnidadeMedidaVO obj) throws ConsistirException {
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (UNIDADE MEDIDA) deve ser informado.");
        }
    }

    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return (nome);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

	public String getSigla() {
		if (sigla == null) {
			sigla = "";
		}
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}


	public boolean isFracionado() {
		return fracionado;
	}


	public void setFracionado(boolean fracionado) {
		this.fracionado = fracionado;
	}


	
	
	

}
