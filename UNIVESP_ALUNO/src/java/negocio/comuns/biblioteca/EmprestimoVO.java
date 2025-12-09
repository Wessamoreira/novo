package negocio.comuns.biblioteca;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa;

/**
 * Reponsável por manter os dados da entidade Emprestimo. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
//@Entity
//@Table(name = "Emprestimo")
//@Analyzer
public class EmprestimoVO extends SuperVO {

//    @Id
//    @DocumentId
    private Integer codigo;
//    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
//    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    private Date dataTemp;	
    private Double valorTotalMulta;
    private String tipoPessoa;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>ItemEmprestimo</code>.
     */
//    @Transient
    private List<ItemEmprestimoVO> itemEmprestimoVOs;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Usuario </code>.
     */
//    @Transient
    private UsuarioVO atendente;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Biblioteca </code>.
     */
//    @Transient
    private BibliotecaVO biblioteca;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
//    @IndexedEmbedded
//    @ManyToOne
//    @JoinColumn(name = "pessoa")
    private PessoaVO pessoa;
//    @Transient
    private Integer qtdAtivo;
//    @Transient
    private Integer qtdAtrasado;
//    @Transient
    private String nomeCurso;
//    @Transient
    private Integer codigoCurso;
    public static final long serialVersionUID = 1l;
//    @Transient
    private MatriculaVO matricula;
    
    private ImpressoraVO impressoraVO;
    
    /**
     * Construtor padrão da classe <code>Emprestimo</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public EmprestimoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>EmprestimoVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(EmprestimoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Empréstimo) deve ser informado.");
        }
        if ((obj.getAtendente() == null) || (obj.getAtendente().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo ATENDENTE (Empréstimo) deve ser informado.");
        }
        if ((obj.getBiblioteca() == null) || (obj.getBiblioteca().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo BIBLIOTECA (Empréstimo) deve ser informado.");
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
//        setSituacao(getSituacao().toUpperCase());
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setData(new Date());
//        setSituacao("");
        setValorTotalMulta(0.0);
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>ItemEmprestimoVO</code> ao List <code>itemEmprestimoVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>ItemEmprestimo</code> - getExemplar().getCodigo() - como
     * identificador (key) do objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>ItemEmprestimoVO</code> que será
     *            adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjItemEmprestimoVOs(ItemEmprestimoVO obj) throws Exception {
        ItemEmprestimoVO.validarDados(obj);
        int index = 0;
        Iterator<ItemEmprestimoVO> i = getItemEmprestimoVOs().iterator();
        while (i.hasNext()) {
            ItemEmprestimoVO objExistente = (ItemEmprestimoVO) i.next();
            if (objExistente.getExemplar().getCodigo().equals(obj.getExemplar().getCodigo())) {
                getItemEmprestimoVOs().set(index, obj);
                return;
            }
            index++;
        }
        getItemEmprestimoVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>ItemEmprestimoVO</code> no List <code>itemEmprestimoVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>ItemEmprestimo</code> - getExemplar().getCodigo() - como
     * identificador (key) do objeto no List.
     *
     * @param exemplar
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjItemEmprestimoVOs(Integer exemplar) throws Exception {
        int index = 0;
        Iterator<ItemEmprestimoVO> i = getItemEmprestimoVOs().iterator();
        while (i.hasNext()) {
            ItemEmprestimoVO objExistente = (ItemEmprestimoVO) i.next();
            if (objExistente.getExemplar().getCodigo().equals(exemplar)) {
                getItemEmprestimoVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>ItemEmprestimoVO</code> no List <code>itemEmprestimoVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>ItemEmprestimo</code> - getExemplar().getCodigo() - como
     * identificador (key) do objeto no List.
     *
     * @param exemplar
     *            Parâmetro para localizar o objeto do List.
     */
    public ItemEmprestimoVO consultarObjItemEmprestimoVO(Integer exemplar) throws Exception {
        Iterator<ItemEmprestimoVO> i = getItemEmprestimoVOs().iterator();
        while (i.hasNext()) {
            ItemEmprestimoVO objExistente = (ItemEmprestimoVO) i.next();
            if (objExistente.getExemplar().getCodigo().equals(exemplar)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>Emprestimo</code>).
     */
    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return (pessoa);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>Emprestimo</code>).
     */
    public void setPessoa(PessoaVO obj) {
        this.pessoa = obj;
    }

    /**
     * Retorna o objeto da classe <code>Biblioteca</code> relacionado com (
     * <code>Emprestimo</code>).
     */
    public BibliotecaVO getBiblioteca() {
        if (biblioteca == null) {
            biblioteca = new BibliotecaVO();
        }
        return (biblioteca);
    }

    /**
     * Define o objeto da classe <code>Biblioteca</code> relacionado com (
     * <code>Emprestimo</code>).
     */
    public void setBiblioteca(BibliotecaVO obj) {
        this.biblioteca = obj;
    }

    /**
     * Retorna o objeto da classe <code>Usuario</code> relacionado com (
     * <code>Emprestimo</code>).
     */
    public UsuarioVO getAtendente() {
        if (atendente == null) {
            atendente = new UsuarioVO();
        }
        return (atendente);
    }

    /**
     * Define o objeto da classe <code>Usuario</code> relacionado com (
     * <code>Emprestimo</code>).
     */
    public void setAtendente(UsuarioVO obj) {
        this.atendente = obj;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>ItemEmprestimo</code>.
     */
    public List<ItemEmprestimoVO> getItemEmprestimoVOs() {
        if (itemEmprestimoVOs == null) {
            itemEmprestimoVOs = new ArrayList<ItemEmprestimoVO>(0);
        }
        return (itemEmprestimoVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>ItemEmprestimo</code>.
     */
    public void setItemEmprestimoVOs(List<ItemEmprestimoVO> itemEmprestimoVOs) {
        this.itemEmprestimoVOs = itemEmprestimoVOs;
    }

    public Double getValorTotalMulta() {
        if (valorTotalMulta == null) {
            valorTotalMulta = 0.0;
        }
        return (valorTotalMulta);
    }

    public void setValorTotalMulta(Double valorTotalMulta) {
        this.valorTotalMulta = valorTotalMulta;
    }

    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return (data);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getData_Apresentar() {
        return (Uteis.getData(data));
    }

    public String getData_comHora() {
    	return (Uteis.getDataComHora(data));
    }
        
    public void setData(Date data) {
        this.data = data;
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

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getTipoPessoa() {
        if (tipoPessoa == null) {
            tipoPessoa = "";
        }
        return tipoPessoa;
    }
    
    public boolean isTipoPessoaAluno() {
    	return getTipoPessoa().equals(TipoPessoa.ALUNO.getValor());
    }
    
    public boolean isTipoPessoaProfessor() {
    	return getTipoPessoa().equals(TipoPessoa.PROFESSOR.getValor());
    }
    
    public boolean isTipoPessoaFuncionario() {
    	return getTipoPessoa().equals(TipoPessoa.FUNCIONARIO.getValor());
    }
    
    public boolean isTipoPessoaVisitante() {
    	return getTipoPessoa().equals(TipoPessoa.MEMBRO_COMUNIDADE.getValor());
    }

    public Integer getQtdAtivo() {
        if (qtdAtivo == null) {
            qtdAtivo = 0;
        }
        return qtdAtivo;
    }

    public void setQtdAtivo(Integer qtdAtivo) {
        this.qtdAtivo = qtdAtivo;
    }

    public Integer getQtdAtrasado() {
        if (qtdAtrasado == null) {
            qtdAtrasado = 0;
        }
        return qtdAtrasado;
    }

    public void setQtdAtrasado(Integer qtdAtrasado) {
        this.qtdAtrasado = qtdAtrasado;
    }

    public String getNomeCurso() {
        if (nomeCurso == null) {
            nomeCurso = "";
        }
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public Integer getCodigoCurso() {
        if (codigoCurso == null) {
            codigoCurso = 0;
        }
        return codigoCurso;
    }

    public void setCodigoCurso(Integer codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

	public MatriculaVO getMatricula() {
		if(matricula == null){
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}    

    public Date getDataTemp() {
    	if (dataTemp == null) {
    		dataTemp = new Date();
    	}
		return dataTemp;
	}

	public void setDataTemp(Date dataTemp) {
		this.dataTemp = dataTemp;
	}

	public ImpressoraVO getImpressoraVO() {
		if(impressoraVO == null){
			impressoraVO = new ImpressoraVO();
		}
		return impressoraVO;
	}

	public void setImpressoraVO(ImpressoraVO impressoraVO) {
		this.impressoraVO = impressoraVO;
	}
	
	public String getTituloPessoaEmprestimo(){
		if(isTipoPessoaAluno()){
			return "O Aluno ";
		}else if(isTipoPessoaProfessor()){
			return "O Professor ";
		}else if(isTipoPessoaFuncionario()){
			return "O Funcionário ";
		}else if(isTipoPessoaVisitante()){
			return "O Visitante ";
		}
		return "";
	}
}
