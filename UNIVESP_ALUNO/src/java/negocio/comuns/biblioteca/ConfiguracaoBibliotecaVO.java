package negocio.comuns.biblioteca;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.GrupoDestinatariosVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.biblioteca.enumeradores.FrequenciaNotificacaoGestoresEnum;
import negocio.comuns.biblioteca.enumeradores.RegraAplicacaoBloqueioBibliotecaEnum;
import negocio.comuns.biblioteca.enumeradores.TipoClassificacaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa;

/**
 * Reponsável por manter os dados da entidade ConfiguracaoBiblioteca. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ConfiguracaoBibliotecaVO extends SuperVO {

    private Integer codigo;
    private Integer prazoEmpresProfessor;
    private Integer qtdeDiaVencimentoMultaAluno;
    private Integer qtdeDiaVencimentoMultaProfessor;
    private Integer qtdeDiaVencimentoMultaFuncionario;
    private Double valorMultaDiaProfessor;
    private Integer prazoEmpresAluno;
    private Double valorMultaDiaAluno;
    private Integer prazoEmpresFuncionario;
    private Double valorMultaDiaFuncionario;
    private Integer numeroMaximoExemplaresAluno;
    private Integer numeroMaximoExemplaresProfessor;
    private Integer numeroMaximoExemplaresFuncionario;
    private Integer numeroMaximoLivrosReservados;
    private Boolean permiteReserva;
    private Integer numeroRenovacoesAluno;
    private Integer numeroRenovacoesProfessor;
    private Integer numeroRenovacoesFuncionario;
    private Double valorCobrarRessarcimento;
    private Integer prazoValidadeReservaCatalogosIndisponiveis;
    private Integer prazoValidadeReservaCatalogosDisponiveis;
    private Boolean emprestimoRenovacaoComDebitos;
    private Boolean padrao;
    private String nome;
    private Integer percentualExemplaresParaConsulta;
    private String textoPadraoEmprestimo;
    private String textoPadraoDevolucao;
    private String textoPadraoUltimaRenovacao;
    private List<BibliotecaExternaVO> listaBibliotecaExternaVO;
    private Boolean notificarAlunoUmDiaAntesVencimentoPrazo; //
    private Boolean notificarAlunoUmDiaAntesAgendaReservaLivro; //
    private Integer numeroDiasAtrazoEmprestimoEnviarPrimeiraNotificacao; //
    private Integer numeroDiasAtrazoEmprestimoEnviarSegundaNotificacao; //
    private Integer numeroDiasAtrazoEmprestimoEnviarTerceiraNotificacao; //
    private Integer numeroMaximoDiasEmprestimoAtrazadoEnviarNotificacaoGestores; //
    private FrequenciaNotificacaoGestoresEnum frequenciaNotificacaoGestoresEnum; //
    private GrupoDestinatariosVO grupoDestinatariosNotificacao; //
    private TipoClassificacaoEnum tipoClassificacao;
    private FuncionarioVO funcionarioPadraoEnvioMensagem;
    private Boolean solicitarSenhaRealizarEmprestimo;
    private Boolean naoRenovarExemplarIndisponivel;
    private Boolean utilizarApenasDiasUteisEmprestimo;
    private Boolean utilizarDiasUteisCalcularMulta;
    private Integer quantidadeRenovacaoPermitidaVisaoAluno;
    private Integer quantidadeRenovacaoPermitidaVisaoProfessor;
    private Integer quantidadeRenovacaoPermitidaVisaoCoordenador;
    private Boolean permiteRenovarExemplarEmAtrasoVisaoProfessor;
    private Boolean permiteRenovarExemplarEmAtrasoVisaoAluno;
    private Boolean permiteRenovarExemplarEmAtrasoVisaoCoordenador;
    private Integer prazoEmprestimoAlunoFinalDeSemana;
    private Double valorMultaEmprestimoAlunoFinalDeSemana;
    private Integer prazoEmprestimoProfessorFinalDeSemana;
    private Double valorMultaEmprestimoProfessorFinalDeSemana;
    private Integer prazoEmprestimoFuncionarioFinalDeSemana;
    private Double valorMultaEmprestimoFuncionarioFinalDeSemana;
    private Boolean liberaEmprestimoMaisDeUmExemplarMesmoCatalogo;
    private Boolean liberaDevolucaoExemplarOutraBiblioteca;
    private Boolean permiteRealizarReservaCatalogoDisponivel;
    private Integer quantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoAluno;
    private Integer quantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoProfessor;
    private Integer quantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoCoordenador;
    private Integer quantidadeDiasAntesNotificarPrazoDevolucao;
    private Boolean validarMultaOutraBiblioteca;
    private Boolean validarExemplarAtrasadoOutraBiblioteca;
    private Boolean considerarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestado;
    private Boolean permiteRealizarEmprestimoporHoraAluno;
    private Integer limiteMaximoHorasEmprestimoAluno;
    private Double valorMultaEmprestimoPorHoraAluno;
    private Boolean permiteRealizarEmprestimoporHoraProfessor;
    private Integer limiteMaximoHorasEmprestimoProfessor;
    private Double valorMultaEmprestimoPorHoraProfessor;
    private Boolean permiteRealizarEmprestimoporHoraFuncionario;
    private Integer limiteMaximoHorasEmprestimoFuncionario;
    private Double valorMultaEmprestimoPorHoraFuncionario;
    private String textoPadraoReservaCatalogo;
    private Boolean gerarBloqueioPorAtrasoAluno;
    private Boolean gerarBloqueioPorAtrasoProfessor;
    private Boolean gerarBloqueioPorAtrasoCoordenador;
    private Boolean gerarBloqueioPorAtrasoFuncionario;
    private Integer quantidadeDiasGerarBloqueioPorAtrasoAluno;
    private Integer quantidadeDiasGerarBloqueioPorAtrasoProfessor;
    private Integer quantidadeDiasGerarBloqueioPorAtrasoCoordenador;
    private Integer quantidadeDiasGerarBloqueioPorAtrasoFuncionario;
    private RegraAplicacaoBloqueioBibliotecaEnum regraAplicacaoBloqueioAluno;
    private RegraAplicacaoBloqueioBibliotecaEnum regraAplicacaoBloqueioProfessor;
    private RegraAplicacaoBloqueioBibliotecaEnum regraAplicacaoBloqueioCoordenador;
    private RegraAplicacaoBloqueioBibliotecaEnum regraAplicacaoBloqueioFuncionario;
    private Boolean considerarSabadoDiaUtil;
    private Boolean considerarDomingoDiaUtil;
    private Byte tamanhoCodigoBarra;
    
    // Campos Visitante
    private Integer prazoEmpresVisitante;
    private Integer qtdeDiaVencimentoMultaVisitante;
    private Double valorMultaDiaVisitante;
    private Integer numeroMaximoExemplaresVisitante;
    private Integer numeroRenovacoesVisitante;
    private Boolean considerarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestadoVisitante;
    private Integer prazoEmprestimoVisitanteFinalDeSemana;
    private Double valorMultaEmprestimoVisitanteFinalDeSemana;
    private Boolean permiteRealizarEmprestimoporHoraVisitante;
    private Integer limiteMaximoHorasEmprestimoVisitante;
    private Double valorMultaEmprestimoPorHoraVisitante;    
    private Boolean gerarBloqueioPorAtrasoVisitante;
    private RegraAplicacaoBloqueioBibliotecaEnum regraAplicacaoBloqueioVisitante;
    
    private Boolean possuiIntegracaoMinhaBiblioteca;
    private Boolean possuiIntegracaoLexMagister;
    private String chaveAutenticacaoMinhaBiblioteca;
    private String chaveAutenticacaoLexMagister;
    private String informacaoHead;
    
    private Boolean permiteRenovarMatriculaQuandoPossuiBloqueioBiblioteca;
    
    private Boolean possuiIntegracaoEbsco;
    private String hostEbsco;
    private String usuarioEbsco;
    private String senhaEbsco;
    private Boolean habilitarIntegracaoBvPearson ;
    private String linkAcessoBVPerson;
    private String chaveTokenBVPerson;
    
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ConfiguracaoBiblioteca</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public ConfiguracaoBibliotecaVO() {
        super();
        this.grupoDestinatariosNotificacao = new GrupoDestinatariosVO();
        this.funcionarioPadraoEnvioMensagem = new FuncionarioVO();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ConfiguracaoBibliotecaVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ConfiguracaoBibliotecaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
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
    }

    public Double getValorMultaDiaFuncionario() {
        if (valorMultaDiaFuncionario == null) {
            valorMultaDiaFuncionario = 0.0;
        }
        return (valorMultaDiaFuncionario);
    }

    public void setValorMultaDiaFuncionario(Double valorMultaDiaFuncionario) {
        this.valorMultaDiaFuncionario = valorMultaDiaFuncionario;
    }

    public Integer getPrazoEmpresFuncionario() {
        if (prazoEmpresFuncionario == null) {
            prazoEmpresFuncionario = 0;
        }
        return (prazoEmpresFuncionario);
    }

    public void setPrazoEmpresFuncionario(Integer prazoEmpresFuncionario) {
        this.prazoEmpresFuncionario = prazoEmpresFuncionario;
    }

    public Double getValorMultaDiaAluno() {
        if (valorMultaDiaAluno == null) {
            valorMultaDiaAluno = 0.0;
        }
        return (valorMultaDiaAluno);
    }

    public void setValorMultaDiaAluno(Double valorMultaDiaAluno) {
        this.valorMultaDiaAluno = valorMultaDiaAluno;
    }

    public Integer getPrazoEmpresAluno() {
        if (prazoEmpresAluno == null) {
            prazoEmpresAluno = 0;
        }
        return (prazoEmpresAluno);
    }

    public void setPrazoEmpresAluno(Integer prazoEmpresAluno) {
        this.prazoEmpresAluno = prazoEmpresAluno;
    }

    public Double getValorMultaDiaProfessor() {
        if (valorMultaDiaProfessor == null) {
            valorMultaDiaProfessor = 0.0;
        }
        return (valorMultaDiaProfessor);
    }

    public void setValorMultaDiaProfessor(Double valorMultaDiaProfessor) {
        this.valorMultaDiaProfessor = valorMultaDiaProfessor;
    }

    public Integer getPrazoEmpresProfessor() {
        if (prazoEmpresProfessor == null) {
            prazoEmpresProfessor = 0;
        }
        return (prazoEmpresProfessor);
    }

    public void setPrazoEmpresProfessor(Integer prazoEmpresProfessor) {
        this.prazoEmpresProfessor = prazoEmpresProfessor;
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

    public Integer getNumeroMaximoExemplaresAluno() {
        if (numeroMaximoExemplaresAluno == null) {
            numeroMaximoExemplaresAluno = 0;
        }
        return numeroMaximoExemplaresAluno;
    }

    public void setNumeroMaximoExemplaresAluno(Integer numeroMaximoExemplaresAluno) {
        this.numeroMaximoExemplaresAluno = numeroMaximoExemplaresAluno;
    }

    public Integer getNumeroMaximoExemplaresProfessor() {
        if (numeroMaximoExemplaresProfessor == null) {
            numeroMaximoExemplaresProfessor = 0;
        }
        return numeroMaximoExemplaresProfessor;
    }

    public void setNumeroMaximoExemplaresProfessor(Integer numeroMaximoExemplaresProfessor) {
        this.numeroMaximoExemplaresProfessor = numeroMaximoExemplaresProfessor;
    }

    public Integer getNumeroMaximoExemplaresFuncionario() {
        if (numeroMaximoExemplaresFuncionario == null) {
            numeroMaximoExemplaresFuncionario = 0;
        }
        return numeroMaximoExemplaresFuncionario;
    }

    public void setNumeroMaximoExemplaresFuncionario(Integer numeroMaximoExemplaresFuncionario) {
        this.numeroMaximoExemplaresFuncionario = numeroMaximoExemplaresFuncionario;
    }

    public Integer getNumeroMaximoLivrosReservados() {
        if (numeroMaximoLivrosReservados == null) {
            numeroMaximoLivrosReservados = 0;
        }
        return numeroMaximoLivrosReservados;
    }

    public void setNumeroMaximoLivrosReservados(Integer numeroMaximoLivrosReservados) {
        this.numeroMaximoLivrosReservados = numeroMaximoLivrosReservados;
    }

    public Boolean getPermiteReserva() {
        if (permiteReserva == null) {
            permiteReserva = false;
        }
        return permiteReserva;
    }

    public void setPermiteReserva(Boolean permiteReserva) {
        this.permiteReserva = permiteReserva;
    }

    public Integer getNumeroRenovacoesAluno() {
        if (numeroRenovacoesAluno == null) {
            numeroRenovacoesAluno = 0;
        }
        return numeroRenovacoesAluno;
    }

    public void setNumeroRenovacoesAluno(Integer numeroRenovacoesAluno) {
        this.numeroRenovacoesAluno = numeroRenovacoesAluno;
    }

    public Integer getNumeroRenovacoesProfessor() {
        if (numeroRenovacoesProfessor == null) {
            numeroRenovacoesProfessor = 0;
        }
        return numeroRenovacoesProfessor;
    }

    public void setNumeroRenovacoesProfessor(Integer numeroRenovacoesProfessor) {
        this.numeroRenovacoesProfessor = numeroRenovacoesProfessor;
    }

    public Integer getNumeroRenovacoesFuncionario() {
        if (numeroRenovacoesFuncionario == null) {
            numeroRenovacoesFuncionario = 0;
        }
        return numeroRenovacoesFuncionario;
    }

    public void setNumeroRenovacoesFuncionario(Integer numeroRenovacoesFuncionario) {
        this.numeroRenovacoesFuncionario = numeroRenovacoesFuncionario;
    }

    public Double getValorCobrarRessarcimento() {
        if (valorCobrarRessarcimento == null) {
            valorCobrarRessarcimento = 0.0;
        }
        return valorCobrarRessarcimento;
    }

    public void setValorCobrarRessarcimento(Double valorCobrarRessarcimento) {
        this.valorCobrarRessarcimento = valorCobrarRessarcimento;
    }

    public Boolean getEmprestimoRenovacaoComDebitos() {
        if (emprestimoRenovacaoComDebitos == null) {
            emprestimoRenovacaoComDebitos = false;
        }
        return emprestimoRenovacaoComDebitos;
    }

    public void setEmprestimoRenovacaoComDebitos(Boolean emprestimoRenovacaoComDebitos) {
        this.emprestimoRenovacaoComDebitos = emprestimoRenovacaoComDebitos;
    }

    public Boolean getPadrao() {
        if (padrao == null) {
            padrao = false;
        }
        return padrao;
    }

    public void setPadrao(Boolean padrao) {
        this.padrao = padrao;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return nome;
    }

    public void setPercentualExemplaresParaConsulta(Integer percentualExemplaresParaConsulta) {
        this.percentualExemplaresParaConsulta = percentualExemplaresParaConsulta;
    }

    public Integer getPercentualExemplaresParaConsulta() {
        if (percentualExemplaresParaConsulta == null) {
            percentualExemplaresParaConsulta = 0;
        }
        return percentualExemplaresParaConsulta;
    }

    public Integer getPrazoValidadeReservaCatalogosIndisponiveis() {
        if (prazoValidadeReservaCatalogosIndisponiveis == null) {
            prazoValidadeReservaCatalogosIndisponiveis = 0;
        }
        return prazoValidadeReservaCatalogosIndisponiveis;
    }

    public void setPrazoValidadeReservaCatalogosIndisponiveis(Integer prazoValidadeReservaCatalogosIndisponiveis) {
        this.prazoValidadeReservaCatalogosIndisponiveis = prazoValidadeReservaCatalogosIndisponiveis;
    }

    public Integer getPrazoValidadeReservaCatalogosDisponiveis() {
        if (prazoValidadeReservaCatalogosDisponiveis == null) {
            prazoValidadeReservaCatalogosDisponiveis = 0;
        }
        return prazoValidadeReservaCatalogosDisponiveis;
    }

    public void setPrazoValidadeReservaCatalogosDisponiveis(Integer prazoValidadeReservaCatalogosDisponiveis) {
        this.prazoValidadeReservaCatalogosDisponiveis = prazoValidadeReservaCatalogosDisponiveis;
    }

    public String getTextoPadraoDevolucao() {
        if (textoPadraoDevolucao == null) {
            textoPadraoDevolucao = "";
        }
        return textoPadraoDevolucao;
    }

    public void setTextoPadraoDevolucao(String textoPadraoDevolucao) {
        this.textoPadraoDevolucao = textoPadraoDevolucao;
    }

    public String getTextoPadraoEmprestimo() {
        if (textoPadraoEmprestimo == null) {
            textoPadraoEmprestimo = "";
        }
        return textoPadraoEmprestimo;
    }

    public void setTextoPadraoEmprestimo(String textoPadraoEmprestimo) {
        this.textoPadraoEmprestimo = textoPadraoEmprestimo;
    }

    public List<BibliotecaExternaVO> getListaBibliotecaExternaVO() {
        if (listaBibliotecaExternaVO == null) {
            listaBibliotecaExternaVO = new ArrayList<BibliotecaExternaVO>(0);
        }
        return listaBibliotecaExternaVO;
    }

    public void setListaBibliotecaExternaVO(List<BibliotecaExternaVO> listaBibliotecaExternaVO) {
        this.listaBibliotecaExternaVO = listaBibliotecaExternaVO;
    }

    public Boolean getNotificarAlunoUmDiaAntesAgendaReservaLivro() {
        if (notificarAlunoUmDiaAntesAgendaReservaLivro == null) {
            notificarAlunoUmDiaAntesAgendaReservaLivro = false;
        }
        return notificarAlunoUmDiaAntesAgendaReservaLivro;
    }

    public void setNotificarAlunoUmDiaAntesAgendaReservaLivro(Boolean notificarAlunoUmDiaAntesAgendaReservaLivro) {
        this.notificarAlunoUmDiaAntesAgendaReservaLivro = notificarAlunoUmDiaAntesAgendaReservaLivro;
    }

    public Boolean getNotificarAlunoUmDiaAntesVencimentoPrazo() {

        if (notificarAlunoUmDiaAntesVencimentoPrazo == null) {
            notificarAlunoUmDiaAntesVencimentoPrazo = false;
        }
        return notificarAlunoUmDiaAntesVencimentoPrazo;
    }

    public void setNotificarAlunoUmDiaAntesVencimentoPrazo(Boolean notificarAlunoUmDiaAntesVencimentoPrazo) {
        this.notificarAlunoUmDiaAntesVencimentoPrazo = notificarAlunoUmDiaAntesVencimentoPrazo;
    }

    public Integer getNumeroDiasAtrazoEmprestimoEnviarPrimeiraNotificacao() {
        return numeroDiasAtrazoEmprestimoEnviarPrimeiraNotificacao;
    }

    public void setNumeroDiasAtrazoEmprestimoEnviarPrimeiraNotificacao(Integer numeroDiasAtrazoEmprestimoEnviarPrimeiraNotificacao) {
        this.numeroDiasAtrazoEmprestimoEnviarPrimeiraNotificacao = numeroDiasAtrazoEmprestimoEnviarPrimeiraNotificacao;
    }

    public Integer getNumeroDiasAtrazoEmprestimoEnviarSegundaNotificacao() {
        return numeroDiasAtrazoEmprestimoEnviarSegundaNotificacao;
    }

    public void setNumeroDiasAtrazoEmprestimoEnviarSegundaNotificacao(Integer numeroDiasAtrazoEmprestimoEnviarSegundaNotificacao) {
        this.numeroDiasAtrazoEmprestimoEnviarSegundaNotificacao = numeroDiasAtrazoEmprestimoEnviarSegundaNotificacao;
    }

    public Integer getNumeroDiasAtrazoEmprestimoEnviarTerceiraNotificacao() {
        return numeroDiasAtrazoEmprestimoEnviarTerceiraNotificacao;
    }

    public void setNumeroDiasAtrazoEmprestimoEnviarTerceiraNotificacao(Integer numeroDiasAtrazoEmprestimoEnviarTerceiraNotificacao) {
        this.numeroDiasAtrazoEmprestimoEnviarTerceiraNotificacao = numeroDiasAtrazoEmprestimoEnviarTerceiraNotificacao;
    }

    public Integer getNumeroMaximoDiasEmprestimoAtrazadoEnviarNotificacaoGestores() {
        return numeroMaximoDiasEmprestimoAtrazadoEnviarNotificacaoGestores;
    }

    public void setNumeroMaximoDiasEmprestimoAtrazadoEnviarNotificacaoGestores(Integer numeroMaximoDiasEmprestimoAtrazadoEnviarNotificacaoGestores) {
        this.numeroMaximoDiasEmprestimoAtrazadoEnviarNotificacaoGestores = numeroMaximoDiasEmprestimoAtrazadoEnviarNotificacaoGestores;
    }

    public FrequenciaNotificacaoGestoresEnum getFrequenciaNotificacaoGestoresEnum() {
        return frequenciaNotificacaoGestoresEnum;
    }

    public void setFrequenciaNotificacaoGestoresEnum(FrequenciaNotificacaoGestoresEnum frequenciaNotificacaoGestoresEnum) {
        this.frequenciaNotificacaoGestoresEnum = frequenciaNotificacaoGestoresEnum;
    }

    public GrupoDestinatariosVO getGrupoDestinatariosNotificacao() {
        return grupoDestinatariosNotificacao;
    }

    public void setGrupoDestinatariosNotificacao(GrupoDestinatariosVO grupoDestinatariosNotificacao) {
        this.grupoDestinatariosNotificacao = grupoDestinatariosNotificacao;
    }

    public void adicionarObjLinkBibliotecaVOs(BibliotecaExternaVO obj) throws Exception {
        BibliotecaExternaVO.validarDados(obj);
        int index = 0;
        Iterator i = getListaBibliotecaExternaVO().iterator();
        while (i.hasNext()) {
            BibliotecaExternaVO objExistente = (BibliotecaExternaVO) i.next();
            if (objExistente.equals(obj)) {
                getListaBibliotecaExternaVO().set(index, obj);
                return;
            }
            if (!objExistente.equals(obj) && objExistente.getUrl().equals(obj.getUrl())) {
                throw new ConsistirException("A Configuração de Biblioteca já possui esta Url cadastrada.");
            }
            index++;
        }
        getListaBibliotecaExternaVO().add(obj);
    }

    public void excluirObjLinkBibliotecaVOs(String url) throws Exception {
        int index = 0;
        Iterator i = getListaBibliotecaExternaVO().iterator();
        while (i.hasNext()) {
            BibliotecaExternaVO objExistente = (BibliotecaExternaVO) i.next();
            if (objExistente.getUrl().equals(url)) {
                getListaBibliotecaExternaVO().remove(objExistente);
                return;
            }
            index++;
        }
    }

    public TipoClassificacaoEnum getTipoClassificacao() {
        if (tipoClassificacao == null) {
            tipoClassificacao = TipoClassificacaoEnum.CDU;
        }
        return tipoClassificacao;
    }

    public void setTipoClassificacao(TipoClassificacaoEnum tipoClassificacao) {
        this.tipoClassificacao = tipoClassificacao;
    }

    
    public Integer getQtdeDiaVencimentoMultaAluno() {
        if(qtdeDiaVencimentoMultaAluno == null){
            qtdeDiaVencimentoMultaAluno = 0;
        }
        return qtdeDiaVencimentoMultaAluno;
    }

    
    public void setQtdeDiaVencimentoMultaAluno(Integer qtdeDiaVencimentoMultaAluno) {
        this.qtdeDiaVencimentoMultaAluno = qtdeDiaVencimentoMultaAluno;
    }

    
    public Integer getQtdeDiaVencimentoMultaProfessor() {
        if(qtdeDiaVencimentoMultaProfessor == null){
            qtdeDiaVencimentoMultaProfessor = 0;
        }
        return qtdeDiaVencimentoMultaProfessor;
    }

    
    public void setQtdeDiaVencimentoMultaProfessor(Integer qtdeDiaVencimentoMultaProfessor) {
        this.qtdeDiaVencimentoMultaProfessor = qtdeDiaVencimentoMultaProfessor;
    }

    
    public Integer getQtdeDiaVencimentoMultaFuncionario() {
        if(qtdeDiaVencimentoMultaFuncionario == null){
            qtdeDiaVencimentoMultaFuncionario = 0;
        }
        return qtdeDiaVencimentoMultaFuncionario;
    }

    
    public void setQtdeDiaVencimentoMultaFuncionario(Integer qtdeDiaVencimentoMultaFuncionario) {
        this.qtdeDiaVencimentoMultaFuncionario = qtdeDiaVencimentoMultaFuncionario;
    }

	public FuncionarioVO getFuncionarioPadraoEnvioMensagem() {
		return funcionarioPadraoEnvioMensagem;
	}

	public void setFuncionarioPadraoEnvioMensagem(
			FuncionarioVO funcionarioPadraoEnvioMensagem) {
		this.funcionarioPadraoEnvioMensagem = funcionarioPadraoEnvioMensagem;
	}

    
    public Boolean getSolicitarSenhaRealizarEmprestimo() {
        if(solicitarSenhaRealizarEmprestimo == null){
            solicitarSenhaRealizarEmprestimo = false;
        }
        return solicitarSenhaRealizarEmprestimo;
    }

    
    public void setSolicitarSenhaRealizarEmprestimo(Boolean solicitarSenhaRealizarEmprestimo) {
        this.solicitarSenhaRealizarEmprestimo = solicitarSenhaRealizarEmprestimo;
    }

    
    public Boolean getNaoRenovarExemplarIndisponivel() {
        if(naoRenovarExemplarIndisponivel == null){
            naoRenovarExemplarIndisponivel = false;
        }
        return naoRenovarExemplarIndisponivel;
    }

    
    public void setNaoRenovarExemplarIndisponivel(Boolean naoRenovarExemplarIndisponivel) {
        this.naoRenovarExemplarIndisponivel = naoRenovarExemplarIndisponivel;
    }

	public Boolean getUtilizarApenasDiasUteisEmprestimo() {
		if (utilizarApenasDiasUteisEmprestimo == null) {
			utilizarApenasDiasUteisEmprestimo = Boolean.FALSE;
		}
		return utilizarApenasDiasUteisEmprestimo;
	}

	public void setUtilizarApenasDiasUteisEmprestimo(Boolean utilizarApenasDiasUteisEmprestimo) {
		this.utilizarApenasDiasUteisEmprestimo = utilizarApenasDiasUteisEmprestimo;
	}

	public Boolean getUtilizarDiasUteisCalcularMulta() {
		if (utilizarDiasUteisCalcularMulta == null) {
			utilizarDiasUteisCalcularMulta = Boolean.FALSE;
		}
		return utilizarDiasUteisCalcularMulta;
	}

	public void setUtilizarDiasUteisCalcularMulta(Boolean utilizarDiasUteisCalcularMulta) {
		this.utilizarDiasUteisCalcularMulta = utilizarDiasUteisCalcularMulta;
	}

	public Integer getQuantidadeRenovacaoPermitidaVisaoAluno() {
		if(quantidadeRenovacaoPermitidaVisaoAluno == null){
			quantidadeRenovacaoPermitidaVisaoAluno = 0;
		}
		return quantidadeRenovacaoPermitidaVisaoAluno;
	}

	public void setQuantidadeRenovacaoPermitidaVisaoAluno(Integer quantidadeRenovacaoPermitidaVisaoAluno) {
		this.quantidadeRenovacaoPermitidaVisaoAluno = quantidadeRenovacaoPermitidaVisaoAluno;
	}

	public Integer getQuantidadeRenovacaoPermitidaVisaoProfessor() {
		if(quantidadeRenovacaoPermitidaVisaoProfessor == null){
			quantidadeRenovacaoPermitidaVisaoProfessor = 0;
		}
		return quantidadeRenovacaoPermitidaVisaoProfessor;
	}

	public void setQuantidadeRenovacaoPermitidaVisaoProfessor(Integer quantidadeRenovacaoPermitidaVisaoProfessor) {
		this.quantidadeRenovacaoPermitidaVisaoProfessor = quantidadeRenovacaoPermitidaVisaoProfessor;
	}

	public Integer getQuantidadeRenovacaoPermitidaVisaoCoordenador() {
		if(quantidadeRenovacaoPermitidaVisaoCoordenador == null){
			quantidadeRenovacaoPermitidaVisaoCoordenador = 0;
		}
		return quantidadeRenovacaoPermitidaVisaoCoordenador;
	}

	public void setQuantidadeRenovacaoPermitidaVisaoCoordenador(Integer quantidadeRenovacaoPermitidaVisaoCoordenador) {
		this.quantidadeRenovacaoPermitidaVisaoCoordenador = quantidadeRenovacaoPermitidaVisaoCoordenador;
	}

	public Boolean getPermiteRenovarExemplarEmAtrasoVisaoProfessor() {
		if(permiteRenovarExemplarEmAtrasoVisaoProfessor == null){
			permiteRenovarExemplarEmAtrasoVisaoProfessor = Boolean.FALSE;
		}
		return permiteRenovarExemplarEmAtrasoVisaoProfessor;
	}

	public void setPermiteRenovarExemplarEmAtrasoVisaoProfessor(Boolean permiteRenovarExemplarEmAtrasoVisaoProfessor) {
		this.permiteRenovarExemplarEmAtrasoVisaoProfessor = permiteRenovarExemplarEmAtrasoVisaoProfessor;
	}

	public Boolean getPermiteRenovarExemplarEmAtrasoVisaoAluno() {
		if(permiteRenovarExemplarEmAtrasoVisaoAluno == null) {
			permiteRenovarExemplarEmAtrasoVisaoAluno = Boolean.FALSE;
		}
		return permiteRenovarExemplarEmAtrasoVisaoAluno;
	}

	public void setPermiteRenovarExemplarEmAtrasoVisaoAluno(Boolean permiteRenovarExemplarEmAtrasoVisaoAluno) {
		this.permiteRenovarExemplarEmAtrasoVisaoAluno = permiteRenovarExemplarEmAtrasoVisaoAluno;
	}

	public Boolean getPermiteRenovarExemplarEmAtrasoVisaoCoordenador() {
		if(permiteRenovarExemplarEmAtrasoVisaoCoordenador == null){
			permiteRenovarExemplarEmAtrasoVisaoCoordenador = Boolean.FALSE;
		}
		return permiteRenovarExemplarEmAtrasoVisaoCoordenador;
	}

	public void setPermiteRenovarExemplarEmAtrasoVisaoCoordenador(Boolean permiteRenovarExemplarEmAtrasoVisaoCoordenador) {
		this.permiteRenovarExemplarEmAtrasoVisaoCoordenador = permiteRenovarExemplarEmAtrasoVisaoCoordenador;
	}

	public Integer getPrazoEmprestimoAlunoFinalDeSemana() {
		if(prazoEmprestimoAlunoFinalDeSemana == null){
			prazoEmprestimoAlunoFinalDeSemana = 0;
		}
		return prazoEmprestimoAlunoFinalDeSemana;
	}

	public void setPrazoEmprestimoAlunoFinalDeSemana(Integer prazoEmprestimoAlunoFinalDeSemana) {
		this.prazoEmprestimoAlunoFinalDeSemana = prazoEmprestimoAlunoFinalDeSemana;
	}

	public Double getValorMultaEmprestimoAlunoFinalDeSemana() {
		if(valorMultaEmprestimoAlunoFinalDeSemana == null){
			valorMultaEmprestimoAlunoFinalDeSemana = 0.0;
		}
		return valorMultaEmprestimoAlunoFinalDeSemana;
	}

	public void setValorMultaEmprestimoAlunoFinalDeSemana(Double valorMultaEmprestimoAlunoFinalDeSemana) {
		this.valorMultaEmprestimoAlunoFinalDeSemana = valorMultaEmprestimoAlunoFinalDeSemana;
	}

	public Integer getPrazoEmprestimoProfessorFinalDeSemana() {
		if(prazoEmprestimoProfessorFinalDeSemana== null){
			prazoEmprestimoProfessorFinalDeSemana = 0;
		}
		return prazoEmprestimoProfessorFinalDeSemana;
	}

	public void setPrazoEmprestimoProfessorFinalDeSemana(Integer prazoEmprestimoProfessorFinalDeSemana) {
		this.prazoEmprestimoProfessorFinalDeSemana = prazoEmprestimoProfessorFinalDeSemana;
	}

	public Double getValorMultaEmprestimoProfessorFinalDeSemana() {
		if(valorMultaEmprestimoProfessorFinalDeSemana == null){
			valorMultaEmprestimoProfessorFinalDeSemana = 0.0;
		}
		return valorMultaEmprestimoProfessorFinalDeSemana;
	}

	public void setValorMultaEmprestimoProfessorFinalDeSemana(Double valorMultaEmprestimoProfessorFinalDeSemana) {
		this.valorMultaEmprestimoProfessorFinalDeSemana = valorMultaEmprestimoProfessorFinalDeSemana;
	}

	public Integer getPrazoEmprestimoFuncionarioFinalDeSemana() {
		if(prazoEmprestimoFuncionarioFinalDeSemana == null){
			prazoEmprestimoFuncionarioFinalDeSemana  = 0;
		}
		return prazoEmprestimoFuncionarioFinalDeSemana;
	}

	public void setPrazoEmprestimoFuncionarioFinalDeSemana(Integer prazoEmprestimoFuncionarioFinalDeSemana) {
		this.prazoEmprestimoFuncionarioFinalDeSemana = prazoEmprestimoFuncionarioFinalDeSemana;
	}

	public Double getValorMultaEmprestimoFuncionarioFinalDeSemana() {
		if(valorMultaEmprestimoFuncionarioFinalDeSemana == null){
			valorMultaEmprestimoFuncionarioFinalDeSemana = 0.0;
		}
		return valorMultaEmprestimoFuncionarioFinalDeSemana;
	}

	public void setValorMultaEmprestimoFuncionarioFinalDeSemana(Double valorMultaEmprestimoFuncionarioFinalDeSemana) {
		this.valorMultaEmprestimoFuncionarioFinalDeSemana = valorMultaEmprestimoFuncionarioFinalDeSemana;
	}

	public String getTextoPadraoUltimaRenovacao() {
		if(textoPadraoUltimaRenovacao == null){
			textoPadraoUltimaRenovacao = "";
		}
		return textoPadraoUltimaRenovacao;
	}

	public void setTextoPadraoUltimaRenovacao(String textoPadraoUltimaRenovacao) {
		this.textoPadraoUltimaRenovacao = textoPadraoUltimaRenovacao;
	}

	public Boolean getLiberaEmprestimoMaisDeUmExemplarMesmoCatalogo() {
		if (liberaEmprestimoMaisDeUmExemplarMesmoCatalogo == null) {
			liberaEmprestimoMaisDeUmExemplarMesmoCatalogo = Boolean.FALSE;
		}
		return liberaEmprestimoMaisDeUmExemplarMesmoCatalogo;
	}

	public void setLiberaEmprestimoMaisDeUmExemplarMesmoCatalogo(Boolean liberaEmprestimoMaisDeUmExemplarMesmoCatalogo) {
		this.liberaEmprestimoMaisDeUmExemplarMesmoCatalogo = liberaEmprestimoMaisDeUmExemplarMesmoCatalogo;
	}

	public Boolean getLiberaDevolucaoExemplarOutraBiblioteca() {
		if (liberaDevolucaoExemplarOutraBiblioteca == null) {
			liberaDevolucaoExemplarOutraBiblioteca = Boolean.FALSE;
		}
		return liberaDevolucaoExemplarOutraBiblioteca;
	}

	public void setLiberaDevolucaoExemplarOutraBiblioteca(Boolean liberaDevolucaoExemplarOutraBiblioteca) {
		this.liberaDevolucaoExemplarOutraBiblioteca = liberaDevolucaoExemplarOutraBiblioteca;
	}

	public Boolean getPermiteRealizarReservaCatalogoDisponivel() {
		if (permiteRealizarReservaCatalogoDisponivel == null) {
			permiteRealizarReservaCatalogoDisponivel = Boolean.FALSE;
		}
		return permiteRealizarReservaCatalogoDisponivel;
	}

	public void setPermiteRealizarReservaCatalogoDisponivel(Boolean permiteRealizarReservaCatalogoDisponivel) {
		this.permiteRealizarReservaCatalogoDisponivel = permiteRealizarReservaCatalogoDisponivel;
	}

	public Integer getQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoAluno() {
		if (quantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoAluno == null) {
			quantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoAluno = 0;
		}
		return quantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoAluno;
	}

	public void setQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoAluno(Integer quantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoAluno) {
		this.quantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoAluno = quantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoAluno;
	}

	public Integer getQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoProfessor() {
		if (quantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoProfessor == null) {
			quantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoProfessor = 0;
		}
		return quantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoProfessor;
	}

	public void setQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoProfessor(Integer quantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoProfessor) {
		this.quantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoProfessor = quantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoProfessor;
	}

	public Integer getQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoCoordenador() {
		if (quantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoCoordenador == null) {
			quantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoCoordenador = 0;
		}
		return quantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoCoordenador;
	}

	public void setQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoCoordenador(Integer quantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoCoordenador) {
		this.quantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoCoordenador = quantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoCoordenador;
	}

	public Integer getQuantidadeDiasAntesNotificarPrazoDevolucao() {
		if (quantidadeDiasAntesNotificarPrazoDevolucao == null) {
			quantidadeDiasAntesNotificarPrazoDevolucao = 0;
		}
		return quantidadeDiasAntesNotificarPrazoDevolucao;
	}

	public void setQuantidadeDiasAntesNotificarPrazoDevolucao(Integer quantidadeDiasAntesNotificarPrazoDevolucao) {
		this.quantidadeDiasAntesNotificarPrazoDevolucao = quantidadeDiasAntesNotificarPrazoDevolucao;
	}

	public Boolean getValidarMultaOutraBiblioteca() {
		if (validarMultaOutraBiblioteca == null) {
			validarMultaOutraBiblioteca = Boolean.FALSE;
		}
		return validarMultaOutraBiblioteca;
	}

	public void setValidarMultaOutraBiblioteca(Boolean validarMultaOutraBiblioteca) {
		this.validarMultaOutraBiblioteca = validarMultaOutraBiblioteca;
	}

	public Boolean getValidarExemplarAtrasadoOutraBiblioteca() {
		if (validarExemplarAtrasadoOutraBiblioteca == null) {
			validarExemplarAtrasadoOutraBiblioteca = Boolean.FALSE;
		}
		return validarExemplarAtrasadoOutraBiblioteca;
	}

	public void setValidarExemplarAtrasadoOutraBiblioteca(Boolean validarExemplarAtrasadoOutraBiblioteca) {
		this.validarExemplarAtrasadoOutraBiblioteca = validarExemplarAtrasadoOutraBiblioteca;
	}

	public Boolean getConsiderarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestado() {
		if (considerarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestado == null) {
			considerarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestado = false;
		}
		return considerarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestado;
	}

	public void setConsiderarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestado(Boolean considerarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestado) {
		this.considerarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestado = considerarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestado;
	}

	public Boolean getPermiteRealizarEmprestimoporHoraAluno() {
		if (permiteRealizarEmprestimoporHoraAluno == null) {
			permiteRealizarEmprestimoporHoraAluno = Boolean.FALSE;
		}
		return permiteRealizarEmprestimoporHoraAluno;
	}

	public void setPermiteRealizarEmprestimoporHoraAluno(Boolean permiteRealizarEmprestimoporHoraAluno) {
		this.permiteRealizarEmprestimoporHoraAluno = permiteRealizarEmprestimoporHoraAluno;
	}

	public Integer getLimiteMaximoHorasEmprestimoAluno() {
		if (limiteMaximoHorasEmprestimoAluno == null) {
			limiteMaximoHorasEmprestimoAluno = 0;
		}
		return limiteMaximoHorasEmprestimoAluno;
	}

	public void setLimiteMaximoHorasEmprestimoAluno(Integer limiteMaximoHorasEmprestimoAluno) {
		this.limiteMaximoHorasEmprestimoAluno = limiteMaximoHorasEmprestimoAluno;
	}

	public Double getValorMultaEmprestimoPorHoraAluno() {
		if (valorMultaEmprestimoPorHoraAluno == null) {
			valorMultaEmprestimoPorHoraAluno = 0.0;
		}
		return valorMultaEmprestimoPorHoraAluno;
	}

	public void setValorMultaEmprestimoPorHoraAluno(Double valorMultaEmprestimoPorHoraAluno) {
		this.valorMultaEmprestimoPorHoraAluno = valorMultaEmprestimoPorHoraAluno;
	}

	public Boolean getPermiteRealizarEmprestimoporHoraProfessor() {
		if (permiteRealizarEmprestimoporHoraProfessor == null) {
			permiteRealizarEmprestimoporHoraProfessor = Boolean.FALSE;
		}
		return permiteRealizarEmprestimoporHoraProfessor;
	}

	public void setPermiteRealizarEmprestimoporHoraProfessor(Boolean permiteRealizarEmprestimoporHoraProfessor) {
		this.permiteRealizarEmprestimoporHoraProfessor = permiteRealizarEmprestimoporHoraProfessor;
	}

	public Integer getLimiteMaximoHorasEmprestimoProfessor() {
		if (limiteMaximoHorasEmprestimoProfessor == null) {
			limiteMaximoHorasEmprestimoProfessor = 0;
		}
		return limiteMaximoHorasEmprestimoProfessor;
	}

	public void setLimiteMaximoHorasEmprestimoProfessor(Integer limiteMaximoHorasEmprestimoProfessor) {
		this.limiteMaximoHorasEmprestimoProfessor = limiteMaximoHorasEmprestimoProfessor;
	}

	public Double getValorMultaEmprestimoPorHoraProfessor() {
		if (valorMultaEmprestimoPorHoraProfessor == null) {
			valorMultaEmprestimoPorHoraProfessor = 0.0;
		}
		return valorMultaEmprestimoPorHoraProfessor;
	}

	public void setValorMultaEmprestimoPorHoraProfessor(Double valorMultaEmprestimoPorHoraProfessor) {
		this.valorMultaEmprestimoPorHoraProfessor = valorMultaEmprestimoPorHoraProfessor;
	}

	public Boolean getPermiteRealizarEmprestimoporHoraFuncionario() {
		if (permiteRealizarEmprestimoporHoraFuncionario == null) {
			permiteRealizarEmprestimoporHoraFuncionario = Boolean.FALSE;
		}
		return permiteRealizarEmprestimoporHoraFuncionario;
	}

	public void setPermiteRealizarEmprestimoporHoraFuncionario(Boolean permiteRealizarEmprestimoporHoraFuncionario) {
		this.permiteRealizarEmprestimoporHoraFuncionario = permiteRealizarEmprestimoporHoraFuncionario;
	}

	public Integer getLimiteMaximoHorasEmprestimoFuncionario() {
		if (limiteMaximoHorasEmprestimoFuncionario == null) {
			limiteMaximoHorasEmprestimoFuncionario = 0;
		}
		return limiteMaximoHorasEmprestimoFuncionario;
	}

	public void setLimiteMaximoHorasEmprestimoFuncionario(Integer limiteMaximoHorasEmprestimoFuncionario) {
		this.limiteMaximoHorasEmprestimoFuncionario = limiteMaximoHorasEmprestimoFuncionario;
	}

	public Double getValorMultaEmprestimoPorHoraFuncionario() {
		if (valorMultaEmprestimoPorHoraFuncionario == null) {
			valorMultaEmprestimoPorHoraFuncionario = 0.0;
		}
		return valorMultaEmprestimoPorHoraFuncionario;
	}

	public void setValorMultaEmprestimoPorHoraFuncionario(Double valorMultaEmprestimoPorHoraFuncionario) {
		this.valorMultaEmprestimoPorHoraFuncionario = valorMultaEmprestimoPorHoraFuncionario;
	}

	public String getTextoPadraoReservaCatalogo() {
		if(textoPadraoReservaCatalogo == null){
			textoPadraoReservaCatalogo = "";
		}
		return textoPadraoReservaCatalogo;
		
	}

	public void setTextoPadraoReservaCatalogo(String textoPadraoReservaCatalogo) {
		this.textoPadraoReservaCatalogo = textoPadraoReservaCatalogo;
	}

	public Boolean getGerarBloqueioPorAtrasoAluno() {
		if(gerarBloqueioPorAtrasoAluno == null){
			gerarBloqueioPorAtrasoAluno =  false;
		}
		return gerarBloqueioPorAtrasoAluno;
	}

	public void setGerarBloqueioPorAtrasoAluno(Boolean gerarBloqueioPorAtrasoAluno) {
		this.gerarBloqueioPorAtrasoAluno = gerarBloqueioPorAtrasoAluno;
	}

	public Boolean getGerarBloqueioPorAtrasoProfessor() {
		if(gerarBloqueioPorAtrasoProfessor == null){
			gerarBloqueioPorAtrasoProfessor =  false;
		}
		return gerarBloqueioPorAtrasoProfessor;
	}

	public void setGerarBloqueioPorAtrasoProfessor(Boolean gerarBloqueioPorAtrasoProfessor) {
		this.gerarBloqueioPorAtrasoProfessor = gerarBloqueioPorAtrasoProfessor;
	}

	public Boolean getGerarBloqueioPorAtrasoCoordenador() {
		if(gerarBloqueioPorAtrasoCoordenador == null){
			gerarBloqueioPorAtrasoCoordenador =  false;
		}
		return gerarBloqueioPorAtrasoCoordenador;
	}

	public void setGerarBloqueioPorAtrasoCoordenador(Boolean gerarBloqueioPorAtrasoCoordenador) {
		this.gerarBloqueioPorAtrasoCoordenador = gerarBloqueioPorAtrasoCoordenador;
	}

	public Boolean getGerarBloqueioPorAtrasoFuncionario() {
		if(gerarBloqueioPorAtrasoFuncionario == null){
			gerarBloqueioPorAtrasoFuncionario =  false;
		}
		return gerarBloqueioPorAtrasoFuncionario;
	}

	public void setGerarBloqueioPorAtrasoFuncionario(Boolean gerarBloqueioPorAtrasoFuncionario) {
		this.gerarBloqueioPorAtrasoFuncionario = gerarBloqueioPorAtrasoFuncionario;
	}

	public Integer getQuantidadeDiasGerarBloqueioPorAtrasoAluno() {
		if(quantidadeDiasGerarBloqueioPorAtrasoAluno == null){
			quantidadeDiasGerarBloqueioPorAtrasoAluno =  1;
		}
		return quantidadeDiasGerarBloqueioPorAtrasoAluno;
	}

	public void setQuantidadeDiasGerarBloqueioPorAtrasoAluno(Integer quantidadeDiasGerarBloqueioPorAtrasoAluno) {
		this.quantidadeDiasGerarBloqueioPorAtrasoAluno = quantidadeDiasGerarBloqueioPorAtrasoAluno;
	}

	public Integer getQuantidadeDiasGerarBloqueioPorAtrasoProfessor() {
		if(quantidadeDiasGerarBloqueioPorAtrasoProfessor == null){
			quantidadeDiasGerarBloqueioPorAtrasoProfessor =  1;
		}
		return quantidadeDiasGerarBloqueioPorAtrasoProfessor;
	}

	public void setQuantidadeDiasGerarBloqueioPorAtrasoProfessor(Integer quantidadeDiasGerarBloqueioPorAtrasoProfessor) {
		this.quantidadeDiasGerarBloqueioPorAtrasoProfessor = quantidadeDiasGerarBloqueioPorAtrasoProfessor;
	}

	public Integer getQuantidadeDiasGerarBloqueioPorAtrasoCoordenador() {
		if(quantidadeDiasGerarBloqueioPorAtrasoCoordenador == null){
			quantidadeDiasGerarBloqueioPorAtrasoCoordenador =  1;
		}
		return quantidadeDiasGerarBloqueioPorAtrasoCoordenador;
	}

	public void setQuantidadeDiasGerarBloqueioPorAtrasoCoordenador(
			Integer quantidadeDiasGerarBloqueioPorAtrasoCoordenador) {
		this.quantidadeDiasGerarBloqueioPorAtrasoCoordenador = quantidadeDiasGerarBloqueioPorAtrasoCoordenador;
	}

	public Integer getQuantidadeDiasGerarBloqueioPorAtrasoFuncionario() {
		if(quantidadeDiasGerarBloqueioPorAtrasoFuncionario == null){
			quantidadeDiasGerarBloqueioPorAtrasoFuncionario =  1;
		}
		return quantidadeDiasGerarBloqueioPorAtrasoFuncionario;
	}

	public void setQuantidadeDiasGerarBloqueioPorAtrasoFuncionario(
			Integer quantidadeDiasGerarBloqueioPorAtrasoFuncionario) {
		this.quantidadeDiasGerarBloqueioPorAtrasoFuncionario = quantidadeDiasGerarBloqueioPorAtrasoFuncionario;
	}

	public RegraAplicacaoBloqueioBibliotecaEnum getRegraAplicacaoBloqueioAluno() {
		if(regraAplicacaoBloqueioAluno == null){
			regraAplicacaoBloqueioAluno =  RegraAplicacaoBloqueioBibliotecaEnum.QUANTIDADE_DIAS_ATRASO;
		}
		return regraAplicacaoBloqueioAluno;
	}

	public void setRegraAplicacaoBloqueioAluno(RegraAplicacaoBloqueioBibliotecaEnum regraAplicacaoBloqueioAluno) {
		this.regraAplicacaoBloqueioAluno = regraAplicacaoBloqueioAluno;
	}

	public RegraAplicacaoBloqueioBibliotecaEnum getRegraAplicacaoBloqueioProfessor() {
		if(regraAplicacaoBloqueioProfessor == null){
			regraAplicacaoBloqueioProfessor =  RegraAplicacaoBloqueioBibliotecaEnum.QUANTIDADE_DIAS_ATRASO;
		}
		return regraAplicacaoBloqueioProfessor;
	}

	public void setRegraAplicacaoBloqueioProfessor(RegraAplicacaoBloqueioBibliotecaEnum regraAplicacaoBloqueioProfessor) {
		this.regraAplicacaoBloqueioProfessor = regraAplicacaoBloqueioProfessor;
	}

	public RegraAplicacaoBloqueioBibliotecaEnum getRegraAplicacaoBloqueioCoordenador() {
		if(regraAplicacaoBloqueioCoordenador == null){
			regraAplicacaoBloqueioCoordenador =  RegraAplicacaoBloqueioBibliotecaEnum.QUANTIDADE_DIAS_ATRASO;
		}
		return regraAplicacaoBloqueioCoordenador;
	}

	public void setRegraAplicacaoBloqueioCoordenador(
			RegraAplicacaoBloqueioBibliotecaEnum regraAplicacaoBloqueioCoordenador) {
		this.regraAplicacaoBloqueioCoordenador = regraAplicacaoBloqueioCoordenador;
	}

	public RegraAplicacaoBloqueioBibliotecaEnum getRegraAplicacaoBloqueioFuncionario() {
		if(regraAplicacaoBloqueioFuncionario == null){
			regraAplicacaoBloqueioFuncionario =  RegraAplicacaoBloqueioBibliotecaEnum.QUANTIDADE_DIAS_ATRASO;
		}
		return regraAplicacaoBloqueioFuncionario;
	}

	public void setRegraAplicacaoBloqueioFuncionario(
			RegraAplicacaoBloqueioBibliotecaEnum regraAplicacaoBloqueioFuncionario) {
		this.regraAplicacaoBloqueioFuncionario = regraAplicacaoBloqueioFuncionario;
	}
    
	public boolean getControlaBloqueio(String tipoPessoa){
		return (tipoPessoa.equals(TipoPessoa.ALUNO.getValor()) && getGerarBloqueioPorAtrasoAluno()) 
		|| (tipoPessoa.equals(TipoPessoa.MEMBRO_COMUNIDADE.getValor()) && getGerarBloqueioPorAtrasoVisitante()) 
		|| (tipoPessoa.equals(TipoPessoa.PROFESSOR.getValor()) && getGerarBloqueioPorAtrasoProfessor()) 
		|| (tipoPessoa.equals(TipoPessoa.FUNCIONARIO.getValor()) && getGerarBloqueioPorAtrasoFuncionario());
	}
	
	public boolean getControlaBloqueioPorDiaAtraso(String tipoPessoa){
		return (tipoPessoa.equals(TipoPessoa.ALUNO.getValor()) && getGerarBloqueioPorAtrasoAluno() && getRegraAplicacaoBloqueioAluno().equals(RegraAplicacaoBloqueioBibliotecaEnum.QUANTIDADE_DIAS_ATRASO)) 
		|| (tipoPessoa.equals(TipoPessoa.MEMBRO_COMUNIDADE.getValor()) && getGerarBloqueioPorAtrasoVisitante() && getRegraAplicacaoBloqueioVisitante().equals(RegraAplicacaoBloqueioBibliotecaEnum.QUANTIDADE_DIAS_ATRASO)) 
		|| (tipoPessoa.equals(TipoPessoa.PROFESSOR.getValor()) && getGerarBloqueioPorAtrasoProfessor()  && getRegraAplicacaoBloqueioProfessor().equals(RegraAplicacaoBloqueioBibliotecaEnum.QUANTIDADE_DIAS_ATRASO)) 
		|| (tipoPessoa.equals(TipoPessoa.FUNCIONARIO.getValor()) && getGerarBloqueioPorAtrasoFuncionario()  && getRegraAplicacaoBloqueioFuncionario().equals(RegraAplicacaoBloqueioBibliotecaEnum.QUANTIDADE_DIAS_ATRASO));
	}
	
	public boolean getControlaBloqueioPorDiaEspecifico(String tipoPessoa){
		return (tipoPessoa.equals(TipoPessoa.ALUNO.getValor()) && getGerarBloqueioPorAtrasoAluno() && getRegraAplicacaoBloqueioAluno().equals(RegraAplicacaoBloqueioBibliotecaEnum.QUANTIDADE_DIAS_ESPECIFICOS)) 
		|| (tipoPessoa.equals(TipoPessoa.MEMBRO_COMUNIDADE.getValor()) && getGerarBloqueioPorAtrasoVisitante() && getRegraAplicacaoBloqueioVisitante().equals(RegraAplicacaoBloqueioBibliotecaEnum.QUANTIDADE_DIAS_ESPECIFICOS)) 
		|| (tipoPessoa.equals(TipoPessoa.PROFESSOR.getValor()) && getGerarBloqueioPorAtrasoProfessor()  && getRegraAplicacaoBloqueioProfessor().equals(RegraAplicacaoBloqueioBibliotecaEnum.QUANTIDADE_DIAS_ESPECIFICOS)) 
		|| (tipoPessoa.equals(TipoPessoa.FUNCIONARIO.getValor()) && getGerarBloqueioPorAtrasoFuncionario()  && getRegraAplicacaoBloqueioFuncionario().equals(RegraAplicacaoBloqueioBibliotecaEnum.QUANTIDADE_DIAS_ESPECIFICOS));
	}
	
	public Integer getQuantidadeDiaBloqueioEspecificao(String tipoPessoa){
		if(tipoPessoa.equals(TipoPessoa.ALUNO.getValor()) && getGerarBloqueioPorAtrasoAluno() && getRegraAplicacaoBloqueioAluno().equals(RegraAplicacaoBloqueioBibliotecaEnum.QUANTIDADE_DIAS_ESPECIFICOS)){
			return getQuantidadeDiasGerarBloqueioPorAtrasoAluno();
		} 
		if(tipoPessoa.equals(TipoPessoa.MEMBRO_COMUNIDADE.getValor()) && getGerarBloqueioPorAtrasoVisitante() && getRegraAplicacaoBloqueioVisitante().equals(RegraAplicacaoBloqueioBibliotecaEnum.QUANTIDADE_DIAS_ESPECIFICOS)){
			return getQuantidadeDiasGerarBloqueioPorAtrasoAluno();
		} 
		if(tipoPessoa.equals(TipoPessoa.PROFESSOR.getValor()) && getGerarBloqueioPorAtrasoProfessor()  && getRegraAplicacaoBloqueioProfessor().equals(RegraAplicacaoBloqueioBibliotecaEnum.QUANTIDADE_DIAS_ESPECIFICOS)) {
			return getQuantidadeDiasGerarBloqueioPorAtrasoProfessor();
		}
		if(tipoPessoa.equals(TipoPessoa.FUNCIONARIO.getValor()) && getGerarBloqueioPorAtrasoFuncionario()  && getRegraAplicacaoBloqueioFuncionario().equals(RegraAplicacaoBloqueioBibliotecaEnum.QUANTIDADE_DIAS_ESPECIFICOS)){
			return getQuantidadeDiasGerarBloqueioPorAtrasoFuncionario();
		}
		return 0;
	}

	public Boolean getConsiderarSabadoDiaUtil() {
		if (considerarSabadoDiaUtil == null) {
			considerarSabadoDiaUtil = false;
		}
		return considerarSabadoDiaUtil;
	}

	public void setConsiderarSabadoDiaUtil(Boolean considerarSabadoDiaUtil) {
		this.considerarSabadoDiaUtil = considerarSabadoDiaUtil;
	}

	public Boolean getConsiderarDomingoDiaUtil() {
		if (considerarDomingoDiaUtil == null) {
			considerarDomingoDiaUtil = false;
		}
		return considerarDomingoDiaUtil;
	}

	public void setConsiderarDomingoDiaUtil(Boolean considerarDomingoDiaUtil) {
		this.considerarDomingoDiaUtil = considerarDomingoDiaUtil;
	}

	public Integer getPrazoEmpresVisitante() {
		if (prazoEmpresVisitante == null) {
			prazoEmpresVisitante = 0;
		}
		return prazoEmpresVisitante;
	}

	public void setPrazoEmpresVisitante(Integer prazoEmpresVisitante) {
		this.prazoEmpresVisitante = prazoEmpresVisitante;
	}

	public Integer getQtdeDiaVencimentoMultaVisitante() {
		if (qtdeDiaVencimentoMultaVisitante == null) {
			qtdeDiaVencimentoMultaVisitante = 0;
		}
		return qtdeDiaVencimentoMultaVisitante;
	}

	public void setQtdeDiaVencimentoMultaVisitante(Integer qtdeDiaVencimentoMultaVisitante) {
		this.qtdeDiaVencimentoMultaVisitante = qtdeDiaVencimentoMultaVisitante;
	}

	public Double getValorMultaDiaVisitante() {
		if (valorMultaDiaVisitante == null) {
			valorMultaDiaVisitante = 0.0;
		}
		return valorMultaDiaVisitante;
	}

	public void setValorMultaDiaVisitante(Double valorMultaDiaVisitante) {
		this.valorMultaDiaVisitante = valorMultaDiaVisitante;
	}

	public Integer getNumeroMaximoExemplaresVisitante() {
		if (numeroMaximoExemplaresVisitante == null) {
			numeroMaximoExemplaresVisitante = 0;
		}
		return numeroMaximoExemplaresVisitante;
	}

	public void setNumeroMaximoExemplaresVisitante(Integer numeroMaximoExemplaresVisitante) {
		this.numeroMaximoExemplaresVisitante = numeroMaximoExemplaresVisitante;
	}

	public Integer getNumeroRenovacoesVisitante() {
		if (numeroRenovacoesVisitante == null) {
			numeroRenovacoesVisitante = 0;
		}
		return numeroRenovacoesVisitante;
	}

	public void setNumeroRenovacoesVisitante(Integer numeroRenovacoesVisitante) {
		this.numeroRenovacoesVisitante = numeroRenovacoesVisitante;
	}

	public Boolean getConsiderarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestadoVisitante() {
		if (considerarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestadoVisitante == null) {
			considerarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestadoVisitante = Boolean.FALSE;
		}
		return considerarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestadoVisitante;
	}

	public void setConsiderarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestadoVisitante(Boolean considerarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestadoVisitante) {
		this.considerarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestadoVisitante = considerarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestadoVisitante;
	}

	public Integer getPrazoEmprestimoVisitanteFinalDeSemana() {
		if (prazoEmprestimoVisitanteFinalDeSemana == null) {
			prazoEmprestimoVisitanteFinalDeSemana = 0;
		}
		return prazoEmprestimoVisitanteFinalDeSemana;
	}

	public void setPrazoEmprestimoVisitanteFinalDeSemana(Integer prazoEmprestimoVisitanteFinalDeSemana) {
		this.prazoEmprestimoVisitanteFinalDeSemana = prazoEmprestimoVisitanteFinalDeSemana;
	}

	public Double getValorMultaEmprestimoVisitanteFinalDeSemana() {
		if (valorMultaEmprestimoVisitanteFinalDeSemana == null) {
			valorMultaEmprestimoVisitanteFinalDeSemana = 0.0;
		}
		return valorMultaEmprestimoVisitanteFinalDeSemana;
	}

	public void setValorMultaEmprestimoVisitanteFinalDeSemana(Double valorMultaEmprestimoVisitanteFinalDeSemana) {
		this.valorMultaEmprestimoVisitanteFinalDeSemana = valorMultaEmprestimoVisitanteFinalDeSemana;
	}

	public Boolean getPermiteRealizarEmprestimoporHoraVisitante() {
		if (permiteRealizarEmprestimoporHoraVisitante == null) {
			permiteRealizarEmprestimoporHoraVisitante = Boolean.FALSE;
		}
		return permiteRealizarEmprestimoporHoraVisitante;
	}

	public void setPermiteRealizarEmprestimoporHoraVisitante(Boolean permiteRealizarEmprestimoporHoraVisitante) {
		this.permiteRealizarEmprestimoporHoraVisitante = permiteRealizarEmprestimoporHoraVisitante;
	}

	public Integer getLimiteMaximoHorasEmprestimoVisitante() {
		if (limiteMaximoHorasEmprestimoVisitante == null) {
			limiteMaximoHorasEmprestimoVisitante = 0;
		}
		return limiteMaximoHorasEmprestimoVisitante;
	}

	public void setLimiteMaximoHorasEmprestimoVisitante(Integer limiteMaximoHorasEmprestimoVisitante) {
		this.limiteMaximoHorasEmprestimoVisitante = limiteMaximoHorasEmprestimoVisitante;
	}

	public Double getValorMultaEmprestimoPorHoraVisitante() {
		if (valorMultaEmprestimoPorHoraVisitante == null) {
			valorMultaEmprestimoPorHoraVisitante = 0.0;
		}
		return valorMultaEmprestimoPorHoraVisitante;
	}

	public void setValorMultaEmprestimoPorHoraVisitante(Double valorMultaEmprestimoPorHoraVisitante) {
		this.valorMultaEmprestimoPorHoraVisitante = valorMultaEmprestimoPorHoraVisitante;
	}

	public Boolean getGerarBloqueioPorAtrasoVisitante() {
		if (gerarBloqueioPorAtrasoVisitante == null) {
			gerarBloqueioPorAtrasoVisitante = Boolean.FALSE;
		}
		return gerarBloqueioPorAtrasoVisitante;
	}

	public void setGerarBloqueioPorAtrasoVisitante(Boolean gerarBloqueioPorAtrasoVisitante) {
		this.gerarBloqueioPorAtrasoVisitante = gerarBloqueioPorAtrasoVisitante;
	}

	public RegraAplicacaoBloqueioBibliotecaEnum getRegraAplicacaoBloqueioVisitante() {
		if(regraAplicacaoBloqueioVisitante == null){
			regraAplicacaoBloqueioVisitante =  RegraAplicacaoBloqueioBibliotecaEnum.QUANTIDADE_DIAS_ATRASO;
		}
		return regraAplicacaoBloqueioVisitante;
	}

	public void setRegraAplicacaoBloqueioVisitante(RegraAplicacaoBloqueioBibliotecaEnum regraAplicacaoBloqueioVisitante) {
		this.regraAplicacaoBloqueioVisitante = regraAplicacaoBloqueioVisitante;
	}

	public Boolean getPossuiIntegracaoMinhaBiblioteca() {
		if (possuiIntegracaoMinhaBiblioteca == null) {
			possuiIntegracaoMinhaBiblioteca = false;
		}
		return possuiIntegracaoMinhaBiblioteca;
	}

	public void setPossuiIntegracaoMinhaBiblioteca(Boolean possuiIntegracaoMinhaBiblioteca) {
		this.possuiIntegracaoMinhaBiblioteca = possuiIntegracaoMinhaBiblioteca;
	}

	public String getChaveAutenticacaoMinhaBiblioteca() {
		if (chaveAutenticacaoMinhaBiblioteca == null) {
			chaveAutenticacaoMinhaBiblioteca = "";
		}
		return chaveAutenticacaoMinhaBiblioteca;
	}

	public void setChaveAutenticacaoMinhaBiblioteca(String chaveAutenticacaoMinhaBiblioteca) {
		this.chaveAutenticacaoMinhaBiblioteca = chaveAutenticacaoMinhaBiblioteca;
	}
	public Byte getTamanhoCodigoBarra() {
		if (tamanhoCodigoBarra == null) {
			tamanhoCodigoBarra = 0;
		}
		return tamanhoCodigoBarra;
	}

	public void setTamanhoCodigoBarra(Byte tamanhoCodigoBarra) {
		this.tamanhoCodigoBarra = tamanhoCodigoBarra;
	}

	public Boolean getPermiteRenovarMatriculaQuandoPossuiBloqueioBiblioteca() {
		if(permiteRenovarMatriculaQuandoPossuiBloqueioBiblioteca == null) {
			return true;
		}
		return permiteRenovarMatriculaQuandoPossuiBloqueioBiblioteca;
	}

	public void setPermiteRenovarMatriculaQuandoPossuiBloqueioBiblioteca(
			Boolean permiteRenovarMatriculaQuandoPossuiBloqueioBiblioteca) {
		this.permiteRenovarMatriculaQuandoPossuiBloqueioBiblioteca = permiteRenovarMatriculaQuandoPossuiBloqueioBiblioteca;
	}

	public Boolean getPossuiIntegracaoLexMagister() {
		if (possuiIntegracaoLexMagister == null) {
			possuiIntegracaoLexMagister = false;
		}
		return possuiIntegracaoLexMagister;
	}

	public void setPossuiIntegracaoLexMagister(Boolean possuiIntegracaoLexMagister) {
		this.possuiIntegracaoLexMagister = possuiIntegracaoLexMagister;
	}

	public String getChaveAutenticacaoLexMagister() {
		if(chaveAutenticacaoLexMagister == null) {
			chaveAutenticacaoLexMagister = "";
		}
		return chaveAutenticacaoLexMagister;
	}

	public void setChaveAutenticacaoLexMagister(String chaveAutenticacaoLexMagister) {
		this.chaveAutenticacaoLexMagister = chaveAutenticacaoLexMagister;
	}

	public String getInformacaoHead() {
		if(informacaoHead == null) {
			informacaoHead = "";
		}
		return informacaoHead;
	}

	public void setInformacaoHead(String informacaoHead) {
		this.informacaoHead = informacaoHead;
	}

	public Boolean getPossuiIntegracaoEbsco() {
		if(possuiIntegracaoEbsco == null) {
			possuiIntegracaoEbsco = Boolean.FALSE;
		}
		return possuiIntegracaoEbsco;
	}

	public void setPossuiIntegracaoEbsco(Boolean possuiIntegracaoEbsco) {
		this.possuiIntegracaoEbsco = possuiIntegracaoEbsco;
	}

	public String getHostEbsco() {
		if(hostEbsco == null) {
			hostEbsco = "";
		}
		return hostEbsco;
	}

	public void setHostEbsco(String hostEbsco) {
		this.hostEbsco = hostEbsco;
	}

	public String getUsuarioEbsco() {
		if(this.usuarioEbsco ==null ) {
			usuarioEbsco ="";
		}
		return usuarioEbsco;
	}

	public void setUsuarioEbsco(String usuarioEbsco) {
		this.usuarioEbsco = usuarioEbsco;
	}

	public String getSenhaEbsco() {
		if(senhaEbsco == null ) {
			this.senhaEbsco = "";
		}
		return senhaEbsco;
	}

	public void setSenhaEbsco(String senhaEbsco) {		
		this.senhaEbsco = senhaEbsco;
	}
	
	public Boolean getHabilitarIntegracaoBvPearson() {
		if(habilitarIntegracaoBvPearson == null) {
			habilitarIntegracaoBvPearson = Boolean.FALSE;
		}
		return habilitarIntegracaoBvPearson;
	}

	public void setHabilitarIntegracaoBvPearson(Boolean habilitarIntegracaoBvPearson) {
		this.habilitarIntegracaoBvPearson = habilitarIntegracaoBvPearson;
	}

	public String getLinkAcessoBVPerson() {
		if(linkAcessoBVPerson == null ) {
			linkAcessoBVPerson ="" ;
		}
		return linkAcessoBVPerson;
	}

	public void setLinkAcessoBVPerson(String linkAcessoBVPerson) {
		this.linkAcessoBVPerson = linkAcessoBVPerson;
	}

	public String getChaveTokenBVPerson() {
		if(chaveTokenBVPerson ==null) {
			chaveTokenBVPerson ="" ;
		}
		return chaveTokenBVPerson;
	}

	public void setChaveTokenBVPerson(String chaveTokenBVPerson) {
		this.chaveTokenBVPerson = chaveTokenBVPerson;
	}


	
}
