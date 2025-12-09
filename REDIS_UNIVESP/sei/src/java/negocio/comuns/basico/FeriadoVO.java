package negocio.comuns.basico;

import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Feriado. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 */
@XmlRootElement(name = "feriado")
public class FeriadoVO extends SuperVO {

    protected Integer codigo;
    protected String descricao;
    protected Date data;
    protected Boolean recorrente;
    private Boolean nacional;
    private Boolean considerarFeriadoFinanceiro;
    private Boolean considerarFeriadoBiblioteca;
    private Boolean considerarFeriadoAcademico;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Cidade </code>.*/
    protected CidadeVO cidade;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>Feriado</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public FeriadoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>FeriadoVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     *                               o atributo e o erro ocorrido.
     */
    public static void validarDados(FeriadoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getDescricao().equals("")) {
            throw new ConsistirException("O campo DESCRIÇÃO (Feriado) deve ser informado.");
        }
        if (!obj.getConsiderarFeriadoFinanceiro() && !obj.getConsiderarFeriadoBiblioteca() && !obj.getConsiderarFeriadoAcademico()) {
        	throw new ConsistirException("Deve ser considerado pelo menos uma das opção de feriado. Financeiro, Biblioteca ou Acadêmico.");
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Feriado) deve ser informado.");
        }
        if (!obj.getNacional().booleanValue() && ((obj.getCidade() == null)
                || (obj.getCidade().getCodigo().intValue() == 0))) {
            throw new ConsistirException("O campo CIDADE (Feriado) deve ser informado.");
        }

    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
    public void realizarUpperCaseDados() {
        setDescricao(getDescricao().toUpperCase());
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setDescricao("");
        setNacional(Boolean.FALSE);
        //setData( new Date() );
        setCidade(new CidadeVO());
    }

    /**
     * Retorna o objeto da classe <code>Cidade</code> relacionado com (<code>Feriado</code>).
     */
    @XmlElement(name = "cidade")
    public CidadeVO getCidade() {
        if (cidade == null) {
            cidade = new CidadeVO();
        }
        return (cidade);
    }

    /**
     * Define o objeto da classe <code>Cidade</code> relacionado com (<code>Feriado</code>).
     */
    public void setCidade(CidadeVO obj) {
        this.cidade = obj;
    }

    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return (data);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa. 
     */
    @XmlElement(name = "data")
    public String getData_Apresentar() {
        return Uteis.obterDataFormatoTextoddMMyyyy(getData());
    }

    public String getDataRecorrente_Apresentar() {
        return Uteis.obterDataFormatoTextoddMM(getData());
    }

    public void setData(Date data) {
        this.data = data;
    }

    @XmlElement(name = "descricao")
    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlElement(name = "codigo")
    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @XmlElement(name = "recorrente")
    public Boolean getRecorrente() {
        return (recorrente);
    }

    public Boolean isRecorrente() {
        return (recorrente);
    }

    public void setRecorrente(Boolean recorrente) {
        this.recorrente = recorrente;
    }

    @XmlElement(name = "nacional")
    public Boolean getNacional() {
        return nacional;
    }

    public void setNacional(Boolean nacional) {
        this.nacional = nacional;
    }

    public boolean isHoliday(Date aDate) {
        Calendar dataCalendar1 = Calendar.getInstance();
        dataCalendar1.setTime(aDate);
        int dia1 = dataCalendar1.get(Calendar.DAY_OF_MONTH);
        int mes1 = dataCalendar1.get(Calendar.MONTH);
        int ano1 = dataCalendar1.get(Calendar.YEAR);

        Calendar dataCalendar2 = Calendar.getInstance();
        dataCalendar2.setTime(this.getData());
        int dia2 = dataCalendar2.get(Calendar.DAY_OF_MONTH);
        int mes2 = dataCalendar2.get(Calendar.MONTH);
        int ano2 = dataCalendar2.get(Calendar.YEAR);

        if ((dia1 == dia2) && (mes1 == mes2)) {
            if (getRecorrente()) {
                return true;
            } else {
                if (ano1 == ano2) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
    

	public Boolean getConsiderarFeriadoFinanceiro() {
		if(considerarFeriadoFinanceiro == null){
			considerarFeriadoFinanceiro = true;
		}
		return considerarFeriadoFinanceiro;
	}

	public void setConsiderarFeriadoFinanceiro(Boolean considerarFeriadoFinanceiro) {
		this.considerarFeriadoFinanceiro = considerarFeriadoFinanceiro;
	}

	public Boolean getConsiderarFeriadoBiblioteca() {
		if(considerarFeriadoBiblioteca == null){
			considerarFeriadoBiblioteca = true;
		}
		return considerarFeriadoBiblioteca;
	}

	public void setConsiderarFeriadoBiblioteca(Boolean considerarFeriadoBiblioteca) {
		this.considerarFeriadoBiblioteca = considerarFeriadoBiblioteca;
	}

	public Boolean getConsiderarFeriadoAcademico() {
		if(considerarFeriadoAcademico == null){
			considerarFeriadoAcademico = true;
		}
		return considerarFeriadoAcademico;
	}

	public void setConsiderarFeriadoAcademico(Boolean considerarFeriadoAcademico) {
		this.considerarFeriadoAcademico = considerarFeriadoAcademico;
	}
	
	
    
}
