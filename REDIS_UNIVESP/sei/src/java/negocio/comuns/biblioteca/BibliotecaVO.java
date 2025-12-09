package negocio.comuns.biblioteca;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PossuiEndereco;
import negocio.comuns.biblioteca.enumeradores.TipoImpressaoComprovanteBibliotecaEnum;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.DiaSemana;

/**
 * Reponsável por manter os dados da entidade Biblioteca. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class BibliotecaVO extends SuperVO implements PossuiEndereco {

    private Integer codigo;
    private String nome;
    private String endereco;
    private String setor;
    private String numero;
    private String CEP;
    private String complemento;
    private String telefone1;
    private String telefone2;
    private String telefone3;
    private String email;
    private TurnoVO turno;	

    public static final long serialVersionUID = 1L;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Cidade </code>.
     */
    private CidadeVO cidade;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>ConfiguracaoBiblioteca </code>.
     */
    private ConfiguracaoBibliotecaVO configuracaoBiblioteca;
    private CentroResultadoVO centroResultadoVO;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Funcionario </code>.
     */
    private FuncionarioVO bibliotecaria;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>UnidadeEnsino </code>.
     */
    private List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs;
    
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     */
	private List<ConfiguracaoBibliotecaNivelEducacionalVO> configuracaoBibliotecaNivelEducacionalVOs;
	
	private TipoImpressaoComprovanteBibliotecaEnum tipoImpressaoComprovanteBiblioteca;
     /**
     * Construtor padrão da classe <code>Biblioteca</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public BibliotecaVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>BibliotecaVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(BibliotecaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getUnidadeEnsinoBibliotecaVOs().isEmpty()) {
            throw new ConsistirException("Por favor informe ao menos UMA unidade de ensino.");
        }
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Biblioteca) deve ser informado.");
        }
        if ((obj.getBibliotecaria() == null) || (obj.getBibliotecaria().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo BIBLIOTECÁRIO (Biblioteca) deve ser informado.");
        }
        if ((obj.getConfiguracaoBiblioteca() == null) || (obj.getConfiguracaoBiblioteca().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CONFIGURAÇÃO BIBLIOTECA (Biblioteca) deve ser informado.");
        }
        if ((obj.getCidade() == null) || (obj.getCidade().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CIDADE (Biblioteca) deve ser informado.");
        }
        if ((obj.getTurno() == null) || (obj.getTurno().getCodigo().intValue() == 0)) {
        	throw new ConsistirException("O campo TURNO (Biblioteca) deve ser informado.");
        }		
        if (obj.getEmail() != null && !obj.getEmail().equals("")) {
            if (!Uteis.getValidaEmail(obj.getEmail())) {
                throw new ConsistirException("O campo EMAIL (Biblioteca) é inválido.");
            }
        }
        if(!Uteis.isAtributoPreenchido(obj.getTurno().getCodigo())){
   	 throw new ConsistirException("O campo TURNO deve ser informado.");
        }
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
        setEndereco(getEndereco().toUpperCase());
        setSetor(getSetor().toUpperCase());
        setNumero(getNumero().toUpperCase());
        setCEP(getCEP().toUpperCase());
        setComplemento(getComplemento().toUpperCase());
        setTelefone1(getTelefone1().toUpperCase());
        setTelefone2(getTelefone2().toUpperCase());
        setTelefone3(getTelefone3().toUpperCase());
        setEmail(getEmail().toUpperCase());
    }

    /**
     * Retorna o objeto da classe <code>Funcionario</code> relacionado com (
     * <code>Biblioteca</code>).
     */
    public FuncionarioVO getBibliotecaria() {
        if (bibliotecaria == null) {
            bibliotecaria = new FuncionarioVO();
        }
        return (bibliotecaria);
    }

    /**
     * Define o objeto da classe <code>Funcionario</code> relacionado com (
     * <code>Biblioteca</code>).
     */
    public void setBibliotecaria(FuncionarioVO obj) {
        this.bibliotecaria = obj;
    }

    /**
     * Retorna o objeto da classe <code>ConfiguracaoBiblioteca</code>
     * relacionado com (<code>Biblioteca</code>).
     */
    public ConfiguracaoBibliotecaVO getConfiguracaoBiblioteca() {
        if (configuracaoBiblioteca == null) {
            configuracaoBiblioteca = new ConfiguracaoBibliotecaVO();
        }
        return (configuracaoBiblioteca);
    }

    /**
     * Define o objeto da classe <code>ConfiguracaoBiblioteca</code> relacionado
     * com (<code>Biblioteca</code>).
     */
    public void setConfiguracaoBiblioteca(ConfiguracaoBibliotecaVO obj) {
        this.configuracaoBiblioteca = obj;
    }

    /**
     * Retorna o objeto da classe <code>Cidade</code> relacionado com (
     * <code>Biblioteca</code>).
     */
    public CidadeVO getCidade() {
        if (cidade == null) {
            cidade = new CidadeVO();
        }
        return (cidade);
    }

    /**
     * Define o objeto da classe <code>Cidade</code> relacionado com (
     * <code>Biblioteca</code>).
     */
    public void setCidade(CidadeVO obj) {
        this.cidade = obj;
    }

    public String getTelefone3() {
        if (telefone3 == null) {
            telefone3 = "";
        }
        return (telefone3);
    }

    public void setTelefone3(String telefone3) {
        this.telefone3 = telefone3;
    }

    public String getTelefone2() {
        if (telefone2 == null) {
            telefone2 = "";
        }
        return (telefone2);
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }

    public String getTelefone1() {
        if (telefone1 == null) {
            telefone1 = "";
        }
        return (telefone1);
    }

    public void setTelefone1(String telefone1) {
        this.telefone1 = telefone1;
    }

    public String getComplemento() {
        if (complemento == null) {
            complemento = "";
        }
        return (complemento);
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCEP() {
        if (CEP == null) {
            CEP = "";
        }
        return (CEP);
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public String getNumero() {
        if (numero == null) {
            numero = "";
        }
        return (numero);
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getSetor() {
        if (setor == null) {
            setor = "";
        }
        return (setor);
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getEndereco() {
        if (endereco == null) {
            endereco = "";
        }
        return (endereco);
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
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

    public String getEmail() {
        if (email == null) {
            email = "";
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	public List<UnidadeEnsinoBibliotecaVO> getUnidadeEnsinoBibliotecaVOs() {
		if (unidadeEnsinoBibliotecaVOs == null) {
			unidadeEnsinoBibliotecaVOs = new ArrayList<UnidadeEnsinoBibliotecaVO>(0);
		}
		return unidadeEnsinoBibliotecaVOs;
	}

	public void setUnidadeEnsinoBibliotecaVOs(List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs) {
		this.unidadeEnsinoBibliotecaVOs = unidadeEnsinoBibliotecaVOs;
	}

	public List<ConfiguracaoBibliotecaNivelEducacionalVO> getConfiguracaoBibliotecaNivelEducacionalVOs() {
		if (configuracaoBibliotecaNivelEducacionalVOs == null) {
			configuracaoBibliotecaNivelEducacionalVOs = new ArrayList<ConfiguracaoBibliotecaNivelEducacionalVO>(0);
		}
		return configuracaoBibliotecaNivelEducacionalVOs;
	}

	public void setConfiguracaoBibliotecaNivelEducacionalVOs(List<ConfiguracaoBibliotecaNivelEducacionalVO> configuracaoBibliotecaNivelEducacionalVOs) {
		this.configuracaoBibliotecaNivelEducacionalVOs = configuracaoBibliotecaNivelEducacionalVOs;
	}
	
	public TurnoVO getTurno() {
		if(turno == null){
			turno = new TurnoVO();
		}
		return turno;
	}

	public void setTurno(TurnoVO turno) {
		this.turno = turno;
	}
	
	public CentroResultadoVO getCentroResultadoVO() {
		centroResultadoVO = Optional.ofNullable(centroResultadoVO).orElse(new CentroResultadoVO());
		return centroResultadoVO;
	}

	public void setCentroResultadoVO(CentroResultadoVO centroResultadoVO) {
		this.centroResultadoVO = centroResultadoVO;
	}

	public boolean calcularDataPrevistaDevolucao(Date dataVerificar, Integer horaAvancar) {
		if (this.getTurno().getCodigo().intValue() > 0) {
			Date dataAvancada = Uteis.getDataAdicionadaEmHoras(dataVerificar, horaAvancar);			
			DiaSemana semana = Uteis.getDiaSemanaEnum(dataAvancada);
			
		}
		return false;
	}
	

	public TipoImpressaoComprovanteBibliotecaEnum getTipoImpressaoComprovanteBiblioteca() {
		if(tipoImpressaoComprovanteBiblioteca == null){
			tipoImpressaoComprovanteBiblioteca = TipoImpressaoComprovanteBibliotecaEnum.APPLET;
		}
		return tipoImpressaoComprovanteBiblioteca;
	}

	public void setTipoImpressaoComprovanteBiblioteca(
			TipoImpressaoComprovanteBibliotecaEnum tipoImpressaoComprovanteBiblioteca) {
		this.tipoImpressaoComprovanteBiblioteca = tipoImpressaoComprovanteBiblioteca;
	}
	
	public Boolean getIsImpressaoPorApplet(){
		return getTipoImpressaoComprovanteBiblioteca().equals(TipoImpressaoComprovanteBibliotecaEnum.APPLET);
	}
	
	public Boolean getIsImpressaoPorPool(){
		return getTipoImpressaoComprovanteBiblioteca().equals(TipoImpressaoComprovanteBibliotecaEnum.POOL);
	}
	
	
	
}
