package negocio.comuns.academico;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade NumeroMatricula. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class NumeroMatriculaVO extends SuperVO {

    private Integer codigo;
    private Integer ano;
    private Integer semestre;
    private Integer unidadeEnsino;
    private Integer curso;
    private Integer incremental;
    private String mascara;
    private String nrMatricula;
    private String cursoAbreviatura;
    private String formaIngresso;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>NumeroMatricula</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public NumeroMatriculaVO() {
        super();
        inicializarDados();
    }

    public NumeroMatriculaVO(MatriculaVO matriculaVO, Integer ano, Integer semestre) {
        super();
        inicializarDados();
        this.mascara = matriculaVO.getCurso().getConfiguracaoAcademico().getMascaraPadraoGeracaoMatricula().toUpperCase();
        if (ano == null || ano == 0) {
            this.ano = Integer.valueOf(Uteis.getAnoDataAtual4Digitos());
        } else {
            this.ano = ano;
        }
        if (semestre == null || semestre == 0) {
            this.semestre = Integer.valueOf(Uteis.getSemestreAtual());
        } else {
            this.semestre = semestre;
        }
        this.unidadeEnsino = new Integer(matriculaVO.getUnidadeEnsino().getCodigo());
        this.curso = new Integer(matriculaVO.getCurso().getCodigo());
        this.formaIngresso = matriculaVO.getFormaIngresso();
        this.cursoAbreviatura = matriculaVO.getCurso().getAbreviatura().trim();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>NumeroMatriculaVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     * 
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */    
    public static void validarDados(NumeroMatriculaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
    }
    
    public String gerarNumeroMatricula(int anoData) {
        setNrMatricula(getMascara());
        // Gerando Ano (Letra Padrão: A)
        if (nrMatricula.toUpperCase().indexOf("A") != -1) {
            int posInicialAno = nrMatricula.indexOf("A");
            int posFinalAno = nrMatricula.lastIndexOf("A") + 1;
            int tamanhoGerar = posFinalAno - posInicialAno;
//			String anoStr = String.valueOf(ano);
            String anoStr = String.valueOf(anoData);

            if (anoStr.length() > tamanhoGerar) {
                anoStr = anoStr.substring(anoStr.length() - tamanhoGerar, anoStr.length());
            } else {
                anoStr = Uteis.preencherComZerosPosicoesVagas(anoStr, posFinalAno - posInicialAno);
            }
            String parteMascaraSubstituir = nrMatricula.substring(posInicialAno, posFinalAno);
            nrMatricula = nrMatricula.replaceFirst(parteMascaraSubstituir, anoStr);
        }
        // Gerando PeriodoLetivo (Letra Padrão: S)
        if (nrMatricula.toUpperCase().indexOf("S") != -1) {
            int posInicialPeriodo = nrMatricula.indexOf("S");
            int posFinalPeriodo = nrMatricula.lastIndexOf("S") + 1;
            int tamanhoGerar = posFinalPeriodo - posInicialPeriodo;

            String semestreStr = String.valueOf(semestre);
            if (semestreStr.length() > tamanhoGerar) {
                semestreStr = semestreStr.substring(semestreStr.length() - tamanhoGerar, semestreStr.length());
            } else {
                semestreStr = Uteis.preencherComZerosPosicoesVagas(semestreStr, posFinalPeriodo - posInicialPeriodo);
            }

            String parteMascaraSubstituir = nrMatricula.substring(posInicialPeriodo, posFinalPeriodo);
            nrMatricula = nrMatricula.replaceFirst(parteMascaraSubstituir, semestreStr);
        }
        // Gerando UnidadeEnsino (Letra Padrão: U)
        if (nrMatricula.toUpperCase().indexOf("U") != -1) {
            int posInicial = nrMatricula.indexOf("U");
            int posFinal = nrMatricula.lastIndexOf("U") + 1;
            int tamanhoGerar = posFinal - posInicial;
            String unidadeEnsinoStr = String.valueOf(unidadeEnsino);

            if (unidadeEnsinoStr.length() > tamanhoGerar) {
                unidadeEnsinoStr = unidadeEnsinoStr.substring(unidadeEnsinoStr.length() - tamanhoGerar, unidadeEnsinoStr.length());
            } else {
                unidadeEnsinoStr = Uteis.preencherComZerosPosicoesVagas(unidadeEnsinoStr, posFinal - posInicial);
            }
            String parteMascaraSubstituir = nrMatricula.substring(posInicial, posFinal);
            nrMatricula = nrMatricula.replaceFirst(parteMascaraSubstituir, unidadeEnsinoStr);
        }
        // Gerando Curso (Letra Padrão: C)
        if (nrMatricula.toUpperCase().indexOf("C") != -1) {
            int posInicial = nrMatricula.indexOf("C");
            int posFinal = nrMatricula.lastIndexOf("C") + 1;
            int tamanhoGerar = posFinal - posInicial;
            String cursoStr = String.valueOf(curso);

            if (cursoStr.length() > tamanhoGerar) {
                cursoStr = cursoStr.substring(cursoStr.length() - tamanhoGerar, cursoStr.length());
            } else {
                cursoStr = Uteis.preencherComZerosPosicoesVagas(cursoStr, posFinal - posInicial);
            }
            String parteMascaraSubstituir = nrMatricula.substring(posInicial, posFinal);
            nrMatricula = nrMatricula.replaceFirst(parteMascaraSubstituir, cursoStr);
        }
        // Gerando Código Incremental (Letra Padrão: I)
        if (nrMatricula.toUpperCase().indexOf("I") != -1) {
            int posInicial = nrMatricula.indexOf("I");
            int posFinal = nrMatricula.lastIndexOf("I") + 1;
            int tamanhoGerar = posFinal - posInicial;
            String proximoIncremental = String.valueOf(++incremental);

            if (proximoIncremental.length() > tamanhoGerar) {
                proximoIncremental = proximoIncremental.substring(proximoIncremental.length() - tamanhoGerar, proximoIncremental.length());
            } else {
                proximoIncremental = Uteis.preencherComZerosPosicoesVagas(proximoIncremental, posFinal - posInicial);
            }
            String parteMascaraSubstituir = nrMatricula.substring(posInicial, posFinal);
            nrMatricula = nrMatricula.replaceFirst(parteMascaraSubstituir, proximoIncremental);
        }
        //Gerando curso abreviatura: (Letra padrão: B)
        if (nrMatricula.toUpperCase().indexOf("B") != -1) {
            int posInicial = nrMatricula.indexOf("B");
            int posFinal = nrMatricula.lastIndexOf("B") + 1;
            int tamanhoGerar = posFinal - posInicial;
            String cursoAbreviaturaStr = String.valueOf(cursoAbreviatura);

            if (cursoAbreviaturaStr.length() > tamanhoGerar) {
                cursoAbreviaturaStr = cursoAbreviaturaStr.substring(cursoAbreviaturaStr.length() - tamanhoGerar, cursoAbreviaturaStr.length());
            } else {
                cursoAbreviaturaStr = Uteis.preencherComZerosPosicoesVagas(cursoAbreviaturaStr, posFinal - posInicial);
            }
            String parteMascaraSubstituir = nrMatricula.substring(posInicial, posFinal);
            nrMatricula = nrMatricula.replaceFirst(parteMascaraSubstituir, cursoAbreviaturaStr);
        }
        //Gerando forma ingresso: (Letra padrão: F)
//		if (nrMatricula.toUpperCase().indexOf("F") != -1) {
//			int posInicial = nrMatricula.indexOf("F");
//			int posFinal = nrMatricula.lastIndexOf("F") + 1;
//			int tamanhoGerar = posFinal - posInicial;
//			String formaIngressoStr = String.valueOf(formaIngresso);
//
//			if (formaIngressoStr.length() > tamanhoGerar) {
//				formaIngressoStr = formaIngressoStr.substring(formaIngressoStr.length() - tamanhoGerar, formaIngressoStr.length());
//			} else {
//				formaIngressoStr = Uteis.preencherComZerosPosicoesVagas(formaIngressoStr, posFinal - posInicial);
//			}
//			String parteMascaraSubstituir = nrMatricula.substring(posInicial, posFinal);
//			nrMatricula = nrMatricula.replaceFirst(parteMascaraSubstituir, formaIngressoStr);
//		}
        return nrMatricula;
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */    
    public void realizarUpperCaseDados() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
        setMascara(getMascara().toUpperCase());
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setAno(0);
        setSemestre(0);
        setUnidadeEnsino(0);
        setCurso(0);
        setIncremental(0);
        setMascara("");
    }

    public String getMascara() {
        if (mascara == null) {
            mascara = "";
        }
        return mascara;
    }

    public void setMascara(String mascara) {
        this.mascara = mascara;
    }

    public Integer getIncremental() {
        if (incremental == null) {
            incremental = 0;
        }
        return (incremental);
    }

    public void setIncremental(Integer incremental) {
        this.incremental = incremental;
    }

    public Integer getCurso() {
        if (curso == null) {
            curso = 0;
        }
        return (curso);
    }

    public void setCurso(Integer curso) {
        this.curso = curso;
    }

    public Integer getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = 0;
        }
        return (unidadeEnsino);
    }

    public void setUnidadeEnsino(Integer unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public Integer getSemestre() {
        if (semestre == null) {
            semestre = 0;
        }
        return (semestre);
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    public Integer getAno() {
        if (ano == null) {
            ano = 0;
        }
        return (ano);
    }

    public void setAno(Integer ano) {
        this.ano = ano;
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

    public String getNrMatricula() {
        if (nrMatricula == null) {
            nrMatricula = "";
        }
        return nrMatricula;
    }

    public void setNrMatricula(String nrMatricula) {
        this.nrMatricula = nrMatricula;
    }

    public String getCursoAbreviatura() {
        if (cursoAbreviatura == null) {
            cursoAbreviatura = "";
        }
        return cursoAbreviatura;
    }

    public void setCursoAbreviatura(String cursoAbreviatura) {
        this.cursoAbreviatura = cursoAbreviatura;
    }

    public String getFormaIngresso() {
        if (formaIngresso == null) {
            formaIngresso = "";
        }
        return formaIngresso;
    }

    public void setFormaIngresso(String formaIngresso) {
        this.formaIngresso = formaIngresso;
    }
}
