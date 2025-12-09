package negocio.comuns.biblioteca;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Autor. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
//@Entity
//@Table(name = "Autor")
//@Indexed
public class AutorVO extends SuperVO  {

//    @Id
//    @DocumentId
    private Integer codigo;
//    @Field(index = Index.TOKENIZED, store = Store.YES)
    private String nome;
    // private String endereco;
    private String telefone;
    private String email;
    // Atributo Transiente, para auxiliar na tela de catálogo.
//    @Transient
    private String tipoAutoria;
//    @Transient
    private List<AutorVariacaoNomeVO> listaAutorVariacaoNome;
    private String cutter;
    private String anoNascimento;
    private String anoFalecimento;
    public static final long serialVersionUID = 1L;
    private String siglaAutoria;

    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Cidade </code>.
     */
    // private CidadeVO cidade;
    /**
     * Construtor padrão da classe <code>Autor</code>. Cria uma nova instância
     * desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public AutorVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>AutorVO</code>. Todos os tipos de consistência de dados são e devem
     * ser implementadas neste método. São validações típicas: verificação de
     * campos obrigatórios, verificação de valores válidos para os atributos.
     * 
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(AutorVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Autor) deve ser informado.");
        }
        
        if (obj.getAnoNascimento().length() > 0 && obj.getAnoNascimento().length() < 4) {
            throw new ConsistirException("O campo ANO NASCIMENTO (Autor) deve possuir 4 caracteres.");
        }
        
        if (obj.getAnoFalecimento().length() > 0 && obj.getAnoFalecimento().length() < 4) {
            throw new ConsistirException("O campo ANO FALECIMENTO (Autor) deve possuir 4 caracteres.");
        }
        
//        if (obj.getListaAutorVariacaoNome().isEmpty()) {
//            throw new ConsistirException("O campo VARIAÇÃO NOME (Variação nome) deve ser informado.");
//        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
        setNome(getNome().toUpperCase());
        // setEndereco(getEndereco().toUpperCase());
        setTelefone(getTelefone().toUpperCase());
        setEmail(getEmail().toUpperCase());
    }

    // /**
    // * Retorna o objeto da classe <code>Cidade</code> relacionado com (
    // * <code>Autor</code>).
    // */
    // public CidadeVO getCidade() {
    // if (cidade == null) {
    // cidade = new CidadeVO();
    // }
    // return (cidade);
    // }
    //
    // /**
    // * Define o objeto da classe <code>Cidade</code> relacionado com (
    // * <code>Autor</code>).
    // */
    // public void setCidade(CidadeVO obj) {
    // this.cidade = obj;
    // }
    public String getEmail() {
        if (email == null) {
            email = "";
        }
        return (email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        if (telefone == null) {
            telefone = "";
        }
        return (telefone);
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    // public String getEndereco() {
    // if (endereco == null) {
    // endereco = "";
    // }
    // return (endereco);
    // }
    //
    // public void setEndereco(String endereco) {
    // this.endereco = endereco;
    // }
    @XmlElement(name = "Autor")
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

    public List<AutorVariacaoNomeVO> getListaAutorVariacaoNome() {
        if (listaAutorVariacaoNome == null) {
            listaAutorVariacaoNome = new ArrayList<AutorVariacaoNomeVO>(0);
        }
        return listaAutorVariacaoNome;
    }

    public void setListaAutorVariacaoNome(List<AutorVariacaoNomeVO> listaAutorVariacaoNome) {
        this.listaAutorVariacaoNome = listaAutorVariacaoNome;
    }

    public void setTipoAutoria(String tipoAutoria) {
        this.tipoAutoria = tipoAutoria;
    }

    public String getTipoAutoria() {
        if (tipoAutoria == null) {
            tipoAutoria = "";
        }
        return tipoAutoria;
    }

	public String getCutter() {
		if (cutter == null) {
			cutter = "";
		}
		return cutter;
	}

	public void setCutter(String cutter) {
		this.cutter = cutter;
	}

	public String getAnoNascimento() {
		if (anoNascimento == null) {
			anoNascimento = "";
		}
		return anoNascimento;
	}

	public void setAnoNascimento(String anoNascimento) {
		this.anoNascimento = anoNascimento;
	}

	public String getAnoFalecimento() {
		if (anoFalecimento == null) {
			anoFalecimento = "";
		}
		return anoFalecimento;
	}

	public void setAnoFalecimento(String anoFalecimento) {
		this.anoFalecimento = anoFalecimento;
	}

	public String getSiglaAutoria() {
		if(siglaAutoria == null) {
			siglaAutoria = "";
		}
		return siglaAutoria;
	}

	public void setSiglaAutoria(String siglaAutoria) {
		this.siglaAutoria = siglaAutoria;
	}
}
