package negocio.comuns.eventos;

import java.util.Date;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Evento. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class EventoVO extends SuperVO {

    private Integer codigo;
    private String nome;
    private Date dataInicioRealizacao;
    private Date dataFinalRealizacao;
    private Date dataInicioInscricao;
    private Date dataFinalInscricao;
    private Double valorAluno;
    private Double valorProfessor;
    private Double valorFuncionario;
    private Double valorComunidade;
    private String localEvento;
    private String site;
    private String emailSuporte;
    private String telefoneSuporte;
    private String tipoInscricao;
    private Integer nrVagas;
    private Integer nrMaximoVagasExcedentes;
    private String formaInscricao;
    private String localInscricao;
    private Boolean aceitarSubmissaoTrabalho;
    private String regrasFormatacaoTrabalho;
    private Date dataInicialSubmissao;
    private Date dataFinalSubmissao;
    private String tipoSubmissao;
    private Double valorSubmissaoAluno;
    private Double valorSubmissaoProfessor;
    private Double valorSubmissaoFuncionario;
    private Double valorSubmissaoComunidade;
    private Boolean ocultarAutorComissaoAvaliadora;
    private Boolean exigirParecerComissaoAvaliadora;
    private Boolean pagamentoPorAutorSubmissao;
    private String situacaoFinanceira;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO responsavel;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Curso </code>.
     */
    private CursoVO curso;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>UnidadeEnsino </code>.
     */
    private UnidadeEnsinoVO unidadeEnsino;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>Evento</code>. Cria uma nova instância
     * desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public EventoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>EventoVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(EventoVO obj) throws ConsistirException {
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Evento) deve ser informado.");
        }
        if (obj.getDataInicioRealizacao() == null) {
            throw new ConsistirException("O campo DATA INÍCIO REALIZAÇÃO (Evento) deve ser informado.");
        }
        if (obj.getDataFinalRealizacao() == null) {
            throw new ConsistirException("O campo DATA FINAL REALIZAÇÃO (Evento) deve ser informado.");
        }
        if (obj.getDataInicioInscricao() == null) {
            throw new ConsistirException("O campo DATA INÍCIO INSCRIÇÃO (Evento) deve ser informado.");
        }
        if (obj.getDataFinalInscricao() == null) {
            throw new ConsistirException("O campo DATA FINAL INSCRIÇÃO (Evento) deve ser informado.");
        }
        if (obj.getValorAluno().intValue() == 0) {
            throw new ConsistirException("O campo VALOR ALUNO (Evento) deve ser informado.");
        }
        if (obj.getValorProfessor().intValue() == 0) {
            throw new ConsistirException("O campo VALOR PROFESSOR (Evento) deve ser informado.");
        }
        if (obj.getValorFuncionario().intValue() == 0) {
            throw new ConsistirException("O campo VALOR FUNCIONÁRIO (Evento) deve ser informado.");
        }
        if ((obj.getResponsavel() == null) || (obj.getResponsavel().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo RESPONSÁVEL (Evento) deve ser informado.");
        }
        if ((obj.getCurso() == null) || (obj.getCurso().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CURSO (Evento) deve ser informado.");
        }
        if ((obj.getUnidadeEnsino() == null) || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo UNIDADE ENSINO (Evento) deve ser informado.");
        }
        if (obj.getTipoInscricao().equals("")) {
            throw new ConsistirException("O campo TIPO INSCRIÇÃO (Evento) deve ser informado.");
        }
        if (obj.getNrVagas().intValue() == 0) {
            throw new ConsistirException("O campo NÚMERO DE VAGAS (Evento) deve ser informado.");
        }
        if (obj.getNrMaximoVagasExcedentes().intValue() == 0) {
            throw new ConsistirException("O campo NÚMERO MÁXIMO DE VAGAS EXCEDENTES (Evento) deve ser informado.");
        }
        if (obj.getRegrasFormatacaoTrabalho().equals("")) {
            throw new ConsistirException("O campo REGRAS FORMATAÇÃO TRABALHO (Evento) deve ser informado.");
        }
        if (obj.getDataInicialSubmissao() == null) {
            throw new ConsistirException("O campo DATA INICIAL SUBMISSÃO (Evento) deve ser informado.");
        }
        if (obj.getDataFinalSubmissao() == null) {
            throw new ConsistirException("O campo DATA FINAL SUBMISSÃO (Evento) deve ser informado.");
        }
        if (obj.getTipoSubmissao().equals("")) {
            throw new ConsistirException("O campo TIPO SUBMISSÃO (Evento) deve ser informado.");
        }
        if (obj.getValorSubmissaoAluno().intValue() == 0) {
            throw new ConsistirException("O campo VALOR SUBMISSÃO ALUNO (Evento) deve ser informado.");
        }
        if (obj.getValorSubmissaoProfessor().intValue() == 0) {
            throw new ConsistirException("O campo VALOR SUBMISSÃO PROFESSOR (Evento) deve ser informado.");
        }
        if (obj.getValorSubmissaoFuncionario().intValue() == 0) {
            throw new ConsistirException("O campo VALOR SUBMISSÃO FUNCIONÁRIO (Evento) deve ser informado.");
        }
        if (obj.getValorSubmissaoComunidade().intValue() == 0) {
            throw new ConsistirException("O campo VALOR SUBMISSÃO COMUNIDADE (Evento) deve ser informado.");
        }
        if (obj.getSituacaoFinanceira().equals("")) {
            throw new ConsistirException("O campo SITUAÇÃO FINANCEIRA (Evento) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setNome("");
        setDataInicioRealizacao(new Date());
        setDataFinalRealizacao(new Date());
        setDataInicioInscricao(new Date());
        setDataFinalInscricao(new Date());
        setValorAluno(0.0);
        setValorProfessor(0.0);
        setValorFuncionario(0.0);
        setValorComunidade(0.0);
        setLocalEvento("");
        setSite("");
        setEmailSuporte("");
        setTelefoneSuporte("");
        setTipoInscricao("");
        setNrVagas(0);
        setNrMaximoVagasExcedentes(0);
        setFormaInscricao("");
        setLocalInscricao("");
        setAceitarSubmissaoTrabalho(Boolean.FALSE);
        setRegrasFormatacaoTrabalho("");
        setDataInicialSubmissao(new Date());
        setDataFinalSubmissao(new Date());
        setTipoSubmissao("");
        setValorSubmissaoAluno(0.0);
        setValorSubmissaoProfessor(0.0);
        setValorSubmissaoFuncionario(0.0);
        setValorSubmissaoComunidade(0.0);
        setOcultarAutorComissaoAvaliadora(Boolean.FALSE);
        setExigirParecerComissaoAvaliadora(Boolean.FALSE);
        setPagamentoPorAutorSubmissao(Boolean.FALSE);
        setSituacaoFinanceira("");
    }

    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>Evento</code>).
     */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>Evento</code>).
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
    }

    /**
     * Retorna o objeto da classe <code>Curso</code> relacionado com (
     * <code>Evento</code>).
     */
    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return (curso);
    }

    /**
     * Define o objeto da classe <code>Curso</code> relacionado com (
     * <code>Evento</code>).
     */
    public void setCurso(CursoVO obj) {
        this.curso = obj;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>Evento</code>).
     */
    public PessoaVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new PessoaVO();
        }
        return (responsavel);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>Evento</code>).
     */
    public void setResponsavel(PessoaVO obj) {
        this.responsavel = obj;
    }

    public String getSituacaoFinanceira() {
        return (situacaoFinanceira);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getSituacaoFinanceira_Apresentar() {
        if (situacaoFinanceira.equals("QI")) {
            return "Quitado";
        }
        if (situacaoFinanceira.equals("EA")) {
            return "Em Aberto";
        }
        return (situacaoFinanceira);
    }

    public void setSituacaoFinanceira(String situacaoFinanceira) {
        this.situacaoFinanceira = situacaoFinanceira;
    }

    public Boolean getPagamentoPorAutorSubmissao() {
        return (pagamentoPorAutorSubmissao);
    }

    public Boolean isPagamentoPorAutorSubmissao() {
        return (pagamentoPorAutorSubmissao);
    }

    public void setPagamentoPorAutorSubmissao(Boolean pagamentoPorAutorSubmissao) {
        this.pagamentoPorAutorSubmissao = pagamentoPorAutorSubmissao;
    }

    public Boolean getExigirParecerComissaoAvaliadora() {
        return (exigirParecerComissaoAvaliadora);
    }

    public Boolean isExigirParecerComissaoAvaliadora() {
        return (exigirParecerComissaoAvaliadora);
    }

    public void setExigirParecerComissaoAvaliadora(Boolean exigirParecerComissaoAvaliadora) {
        this.exigirParecerComissaoAvaliadora = exigirParecerComissaoAvaliadora;
    }

    public Boolean getOcultarAutorComissaoAvaliadora() {
        return (ocultarAutorComissaoAvaliadora);
    }

    public Boolean isOcultarAutorComissaoAvaliadora() {
        return (ocultarAutorComissaoAvaliadora);
    }

    public void setOcultarAutorComissaoAvaliadora(Boolean ocultarAutorComissaoAvaliadora) {
        this.ocultarAutorComissaoAvaliadora = ocultarAutorComissaoAvaliadora;
    }

    public Double getValorSubmissaoComunidade() {
        return (valorSubmissaoComunidade);
    }

    public void setValorSubmissaoComunidade(Double valorSubmissaoComunidade) {
        this.valorSubmissaoComunidade = valorSubmissaoComunidade;
    }

    public Double getValorSubmissaoFuncionario() {
        return (valorSubmissaoFuncionario);
    }

    public void setValorSubmissaoFuncionario(Double valorSubmissaoFuncionario) {
        this.valorSubmissaoFuncionario = valorSubmissaoFuncionario;
    }

    public Double getValorSubmissaoProfessor() {
        return (valorSubmissaoProfessor);
    }

    public void setValorSubmissaoProfessor(Double valorSubmissaoProfessor) {
        this.valorSubmissaoProfessor = valorSubmissaoProfessor;
    }

    public Double getValorSubmissaoAluno() {
        return (valorSubmissaoAluno);
    }

    public void setValorSubmissaoAluno(Double valorSubmissaoAluno) {
        this.valorSubmissaoAluno = valorSubmissaoAluno;
    }

    public String getTipoSubmissao() {
        return (tipoSubmissao);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoSubmissao_Apresentar() {
        if (tipoSubmissao.equals("SI")) {
            return "Submissão Independente";
        }
        if (tipoSubmissao.equals("SP")) {
            return "Submissão Paga";
        }
        if (tipoSubmissao.equals("VI")) {
            return "Submissão Vinculada a Inscrição";
        }
        return (tipoSubmissao);
    }

    public void setTipoSubmissao(String tipoSubmissao) {
        this.tipoSubmissao = tipoSubmissao;
    }

    public Date getDataFinalSubmissao() {
        return (dataFinalSubmissao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataFinalSubmissao_Apresentar() {
        return (Uteis.getData(dataFinalSubmissao));
    }

    public void setDataFinalSubmissao(Date dataFinalSubmissao) {
        this.dataFinalSubmissao = dataFinalSubmissao;
    }

    public Date getDataInicialSubmissao() {
        return (dataInicialSubmissao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataInicialSubmissao_Apresentar() {
        return (Uteis.getData(dataInicialSubmissao));
    }

    public void setDataInicialSubmissao(Date dataInicialSubmissao) {
        this.dataInicialSubmissao = dataInicialSubmissao;
    }

    public String getRegrasFormatacaoTrabalho() {
        return (regrasFormatacaoTrabalho);
    }

    public void setRegrasFormatacaoTrabalho(String regrasFormatacaoTrabalho) {
        this.regrasFormatacaoTrabalho = regrasFormatacaoTrabalho;
    }

    public Boolean getAceitarSubmissaoTrabalho() {
        return (aceitarSubmissaoTrabalho);
    }

    public Boolean isAceitarSubmissaoTrabalho() {
        return (aceitarSubmissaoTrabalho);
    }

    public void setAceitarSubmissaoTrabalho(Boolean aceitarSubmissaoTrabalho) {
        this.aceitarSubmissaoTrabalho = aceitarSubmissaoTrabalho;
    }

    public String getLocalInscricao() {
        return (localInscricao);
    }

    public void setLocalInscricao(String localInscricao) {
        this.localInscricao = localInscricao;
    }

    public String getFormaInscricao() {
        return (formaInscricao);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getFormaInscricao_Apresentar() {
        if (formaInscricao.equals("PR")) {
            return "Presencial";
        }
        if (formaInscricao.equals("IN")) {
            return "Internet";
        }
        if (formaInscricao.equals("PI")) {
            return "Presencial e Internet";
        }
        return (formaInscricao);
    }

    public void setFormaInscricao(String formaInscricao) {
        this.formaInscricao = formaInscricao;
    }

    public Integer getNrMaximoVagasExcedentes() {
        return (nrMaximoVagasExcedentes);
    }

    public void setNrMaximoVagasExcedentes(Integer nrMaximoVagasExcedentes) {
        this.nrMaximoVagasExcedentes = nrMaximoVagasExcedentes;
    }

    public Integer getNrVagas() {
        return (nrVagas);
    }

    public void setNrVagas(Integer nrVagas) {
        this.nrVagas = nrVagas;
    }

    public String getTipoInscricao() {
        return (tipoInscricao);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoInscricao_Apresentar() {
        if (tipoInscricao.equals("EC")) {
            return "Inscrição no Evento e nos Cursos";
        }
        if (tipoInscricao.equals("PC")) {
            return "Inscrição nas Palestras e nos Cursos";
        }
        if (tipoInscricao.equals("IE")) {
            return "Inscrição no Evento";
        }
        return (tipoInscricao);
    }

    public void setTipoInscricao(String tipoInscricao) {
        this.tipoInscricao = tipoInscricao;
    }

    public String getTelefoneSuporte() {
        return (telefoneSuporte);
    }

    public void setTelefoneSuporte(String telefoneSuporte) {
        this.telefoneSuporte = telefoneSuporte;
    }

    public String getEmailSuporte() {
        return (emailSuporte);
    }

    public void setEmailSuporte(String emailSuporte) {
        this.emailSuporte = emailSuporte;
    }

    public String getSite() {
        return (site);
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getLocalEvento() {
        return (localEvento);
    }

    public void setLocalEvento(String localEvento) {
        this.localEvento = localEvento;
    }

    public Double getValorComunidade() {
        return (valorComunidade);
    }

    public void setValorComunidade(Double valorComunidade) {
        this.valorComunidade = valorComunidade;
    }

    public Double getValorFuncionario() {
        return (valorFuncionario);
    }

    public void setValorFuncionario(Double valorFuncionario) {
        this.valorFuncionario = valorFuncionario;
    }

    public Double getValorProfessor() {
        return (valorProfessor);
    }

    public void setValorProfessor(Double valorProfessor) {
        this.valorProfessor = valorProfessor;
    }

    public Double getValorAluno() {
        return (valorAluno);
    }

    public void setValorAluno(Double valorAluno) {
        this.valorAluno = valorAluno;
    }

    public Date getDataFinalInscricao() {
        return (dataFinalInscricao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataFinalInscricao_Apresentar() {
        return (Uteis.getData(dataFinalInscricao));
    }

    public void setDataFinalInscricao(Date dataFinalInscricao) {
        this.dataFinalInscricao = dataFinalInscricao;
    }

    public Date getDataInicioInscricao() {
        return (dataInicioInscricao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataInicioInscricao_Apresentar() {
        return (Uteis.getData(dataInicioInscricao));
    }

    public void setDataInicioInscricao(Date dataInicioInscricao) {
        this.dataInicioInscricao = dataInicioInscricao;
    }

    public Date getDataFinalRealizacao() {
        return (dataFinalRealizacao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataFinalRealizacao_Apresentar() {
        return (Uteis.getData(dataFinalRealizacao));
    }

    public void setDataFinalRealizacao(Date dataFinalRealizacao) {
        this.dataFinalRealizacao = dataFinalRealizacao;
    }

    public Date getDataInicioRealizacao() {
        return (dataInicioRealizacao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataInicioRealizacao_Apresentar() {
        return (Uteis.getData(dataInicioRealizacao));
    }

    public void setDataInicioRealizacao(Date dataInicioRealizacao) {
        this.dataInicioRealizacao = dataInicioRealizacao;
    }

    public String getNome() {
        return (nome);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
