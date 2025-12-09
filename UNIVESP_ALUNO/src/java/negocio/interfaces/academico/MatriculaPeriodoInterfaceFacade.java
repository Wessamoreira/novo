package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import controle.arquitetura.DataModelo;
//import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
//import negocio.comuns.academico.HorarioAlunoTurnoVO;
import negocio.comuns.academico.InclusaoHistoricoForaPrazoVO;
//import negocio.comuns.academico.MapaAlunoAptoFormarVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaCursadaVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.academico.MatriculaComHistoricoAlunoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
//import negocio.comuns.academico.PlanoFinanceiroAlunoVO;
import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import negocio.comuns.academico.RegistroAulaVO;
//import negocio.comuns.academico.RenovacaoMatriculaTurmaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.OrigemFechamentoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.TipoContratoMatriculaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.FeriadoVO;

import negocio.comuns.secretaria.MapaRegistroEvasaoCursoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoVencimentoMatriculaPeriodo;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import webservice.servicos.MatriculaRSVO;

public interface MatriculaPeriodoInterfaceFacade {

    /**
     * Operação responsável por retornar um novo objeto da classe <code>MatriculaPeriodoVO</code>.
     */
    public MatriculaPeriodoVO novo() throws Exception;
    
    public MatriculaPeriodoVO consultarPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

 
    
    void montarDadosDisciplina(MatriculaPeriodoTurmaDisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario)
			throws Exception;	
    
    
    void montarDadosPeriodoLetivoMatricula(MatriculaPeriodoVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario)
			throws Exception;
   
    public void realizarLiberacaoModuloTCCAluno(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception;
    public void realizarLiberacaoModuloEstagioAluno(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>MatriculaPeriodoVO</code>.
     * Primeiramente valida os dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>MatriculaPeriodoVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(MatriculaPeriodoVO obj, MatriculaVO matriculaVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO,  UsuarioVO usuario) throws Exception;

    /**
     * Rotina responsável por registrar as disciplinas relativas a matricula período. A mesma trabalha de acordo com a
     * situacao acadêmica da matrícula período letivo. Quando a matriculaPeriodo é do tipo pré-matrícula, temos que
     * gravar as disciplinas na lista de disciplinas pré-matricula, e não na lista definitiva. Pois, caso as disciplinas
     * sejam lançadas na lista definitiva, teríamos impactos em diversos locais, como horário da aula, lotação de turma,
     * dentre outros itens. Quando a matriculaPeriodo está ativa, então grava-se as disciplinas na lista definitiva.
     *
     * @param obj
     */
    public void gravarMatriculaPeriodoTurmaDisciplinaAcordoSituacaoAcademica(MatriculaVO matriculaVO, MatriculaPeriodoVO obj,  UsuarioVO usuario) throws Exception;

    //public void gravarMatriculaPeriodoHistorico(MatriculaPeriodoVO obj) throws Exception;

    /**
     *
     * @param matriculaVO
     * @param matriculaPeriodoVO
     * @param parcelaMatricula
     * @throws Exception
     */
    public void gerarContaReceberComBaseParcelaMatricula(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO,   UsuarioVO usuario) throws Exception;

    /**
     * Rotina responsável por liberar o aluno do pagamento da matrícula, para isto ela gera uma conta a receber com um
     * desconto necessário, para zerar o valor que, a priori, o aluno deveria pagar.
     *
     * @param matriculaPeriodoVO
     * @param parcelaMatricula
     * @throws Exception
     */
    public void gerarLiberacaoPagamentoMatricula_CriandoContaReceber(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO,   UsuarioVO usuario) throws Exception;

    public void alterarContaReceberLiberandoPagamentoMatricula(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO,  UsuarioVO usuario) throws Exception;

    public void alterarMatriculaPeriodoLiberandoPagamentoMatricula(MatriculaPeriodoVO obj) throws Exception;

    public void liberarMatriculaForaDoPrazo(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO, UsuarioVO usuarioResponsavel) throws Exception;

    public void liberarPagamentoMatricula(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO, UsuarioVO usuarioResponsavel,  UsuarioVO usuario) throws Exception;

    public void processarGeracaoContasReceberAposConfirmacaoPagamentoMatricula(MatriculaVO matriculaVO, Integer codigoMatriculaPeriodoVO, SituacaoVencimentoMatriculaPeriodo situacaoMatriculaAtualizada,  Boolean bloqueioPorFechamentoMesLiberado, UsuarioVO usuario) throws Exception;
    
    public void atualizarDataBaseGeracaoParcelaMatriculaPeriodo(Integer codigoMatriculaPeriodo, Date dataBaseGeracaoParcelas) throws Exception;

    /**
     * Rotina responsável por excluir todos os registros de MatriculaPeriodoVencimento, com as respectivas contas a
     * receber geradas para a mesma. Este método deve ser chamado somente, quando uma matrícula está sendo alterada, com
     * o objetivo, de gerar os vencimentos novamente, conforme os novos parâmetros informados.
     *
     * @param matriculaVO
     * @param matriculaPeriodoVO
     * @throws Exception
     */
    public void excluirTodosVencimentosEContasReceberGeradasMatriculaEParcelas(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception;
    

    

    /**
     * Esta rotina é responsável por registrar as parcelas ainda não pagas, considerando o valor de diferença gerado com
     * relação as parcelas já pagas.
     *
     * @param listaVctosGerados
     * @param matriculaPeriodoVO
     * @param valorDiferencaValorCobrarComRelacaoValorJaPago
     * @throws Exception
     */
    /*public void registrarParcelasGeradasAindaNaoPagas_ContabilizandoValorDiferencaCobrarComRelacaoValorPago( MatriculaPeriodoVO matriculaPeriodoVO, Double valorDiferencaValorCobrarComRelacaoValorJaPago,  boolean simulandoAlteracao, UsuarioVO usuarioVO) throws Exception;*/

    /*public void registrarParcelasGeradasParaFuturaGeracaoContasAReceber( MatriculaPeriodoVO matriculaPeriodoVO,
            Double valorDiferencaValorCobrarComRelacaoValorJaPago) throws Exception;*/

    

    public SituacaoVencimentoMatriculaPeriodo processarGeracao_AlteracaoParcelaVencimentoMatricula( MatriculaVO matriculaVO,
            MatriculaPeriodoVO matriculaPeriodoVO,  UsuarioVO usuario) throws Exception;

    public void inicializarDadosParaProcessarGeracaoContasReceberReferentesParcelasMatriculaPeriodo(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO,
            ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO, UsuarioVO usuario ) throws Exception;

    /**
     * Rotina reponsável por gerar as conta a receber relativas aos vencimentos (parcelas) provenientes, de uma
     * matrícula em um período letivo. Ela trabalha de acordo com a configuração definida no PlanoFinanceiroCursoVO -
     * modelo de geração de parcelas. Existem três modelos de geração de parcelas. - "AM",
     * "Gerar Todas as Parcelas no Ato da Matrícula" - "VC", "Gerar as Parcelas somente após o Pagto da Matrícula" -
     * "FC", "Gerar as Parcelas Mês a Mês" Caso, a opção seja gerar mes a mes, esta rotina trabalha com o conceito de
     * gerar a conta a receber de uma única parcela, considerando um mês/ano de referência.
     *
     * @param matriculaVO
     * @param matriculaPeriodoVO
     * @param processoMatriculaCalendarioVO
     * @param mesAnoReferenciaGerar
     * @throws Exception
     */
    public void processarGeracaoContasReceberReferentesParcelasMatriculaPeriodo(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO,
            ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO,  SituacaoVencimentoMatriculaPeriodo situacaoMatriculaAtualizada,  UsuarioVO usuario, Boolean simulacao) throws Exception;

    /*public void criarContaReceberMensalidade(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaPeriodoVencimentoVO parcelaMensalidade, UsuarioVO usuario, Boolean simulacao) throws Exception;*/

    /*public void gerarContasReceberComBaseParcelaMesAnoEspecifico(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO,
    		MatriculaPeriodoVencimentoVO parcelaRegerar, UsuarioVO usuario, Boolean simulacao) throws Exception;*/

    /*public void gerarContasReceberComBaseParcelasMatriculaPeriodo(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO,  UsuarioVO usuario, Boolean simulacao) throws Exception;*/

    /**
     * Rotina responsável por gerar/alterar as parcelas pertinentes a matrícula período. Esta rotina avalia a situação
     * da matrícula período e com base nesta informação gera as parcelas pertinentes. Tanto a parcela pertinente ao
     * pagamento da matrícula, quanto as parcelas referentes as mensalidades. Ela também processa valores já pagos pelo
     * aluno em uma possível pré-matrícula realizada no semestre anterior.
     *
     * @param matriculaVO
     * @param matriculaPeriodoVO
     * @param processoMatriculaCalendarioVO
     * @throws Exception
     */
    public void gerenciarParcelasMatriculaPeriodo(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO,  UsuarioVO usuario) throws Exception;

    /*public MatriculaPeriodoVencimentoVO obterVctoParcelaEspecifica( String parcela);*/

    /*public Double obterValorVctoParcelaEspecifica( String parcela);*/
       
    /**
     * Método responsável por calcular o valor que deve ser adicionado ou excluído nas parcelas ainda não pagas pelo
     * aluno, isto em decorrência de existir alguma displina que foi incluída/excluída em sua grade do periodo letivo.
     * Este método deve considerar os seguintes aspectos. Primeiro, se o valor de disciplina incluídas/excluídas for ser
     * gerado em uma parcela separada, então não existem razões para esta rotina ser utilizada. Pois não iremos alterar
     * o valor das parcelas já geradas.
     *
     * @param parcelaMatricula
     * @param listaVctosGerados
     * @param matriculaPeriodoVO
     * @return
     * @throws Exception
     */
    public Double calcularValorDiferencaValorCobrarComRelacaoValorJaPago(MatriculaVO matriculaVO,  
            MatriculaPeriodoVO matriculaPeriodoVO,  UsuarioVO usuario) throws Exception;

    /**
     * Rotina responsavel por gerar uma conta a receber quitada ou não, relativa a parcela da matricula. O parametro
     * liberarPagamento é que irá indicar se a conta a receber deverá ser gerada quitada, ou não.
     *
     * @param matriculaVO
     * @param matriculaPeriodoVO
     * @param processoMatriculaCalendarioVO
     * @param condicao
     * @throws Exception
     */
    public void criarContaReceberMatricula(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO,  Boolean liberarPagamento,  UsuarioVO usuario) throws Exception;
  
    public void criarContaReceberBolsaCusteadaMatricula(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO,
               UsuarioVO usuario) throws Exception;
    
    void criarContaReceberNaoPagasBolsaCusteadaMensalidades(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario, Boolean simulacao) throws Exception;    

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>MatriculaPeriodoVO</code>. Sempre
     * utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
     * usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>MatriculaPeriodoVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void alterar(MatriculaPeriodoVO obj, MatriculaVO matriculaVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO,  UsuarioVO usuario) throws Exception;

    public void alterarSituacaoFinanceiraMatriculaPeriodo(Integer matriculaPeriodo, String situacao) throws Exception;

    public void alterarSituacaoFinanceiraMatriculaPeriodoProcessandoAtivacaoDaMesma(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO) throws Exception;

    public void gerarMatriculaInclusaoForaPrazo(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodo,  UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>MatriculaPeriodoVO</code>. Sempre
     * utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
     * usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>MatriculaPeriodoVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void alterarMatriculaForaPrazo(MatriculaPeriodoVO obj, MatriculaVO matriculaVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO,  GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception;

    public void gerarContarReceberInclusaoForaPrazo(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodo, List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinaAdicionadaVOs, Integer numParcelas, Double valorTotalParcela, InclusaoHistoricoForaPrazoVO inclusao, TurmaVO turma,  UsuarioVO usuario) throws Exception;

    /**
     * Metodo retorna uma lista de MatriculaPeridoTurmaDisciplinaVO que não o nr de vagas já ultrapassaram o limite
     * disponivel de vagas tanto de vagas normais como o nr maximo de vagas
     *
     * @param lista
     *            = Lista de MatriculaPeridoTurmaDisciplinaVO
     * @return
     * @throws java.lang.Exception
     */
    public List verificarExistenciaVagasTurmaDisciplina(List lista, UsuarioVO usuario) throws Exception;

    /**
     * Metodo retorna uma lista de MatriculaPeridoTurmaDisciplinaVO que não o nr de vagas já ultrapassaram o limite
     * disponivel de vagas mas não ultrapassaram o limite máximo exigindo Assim senha de autorização para matricula
     *
     * @param lista
     *            = Lista de MatriculaPeridoTurmaDisciplinaVO
     * @return
     * @throws java.lang.Exception
     */
    public List verificarExistenciaVagasMaximaTurmaDisciplina(List lista, UsuarioVO usuario) throws Exception;

//    public void incluirExcluirDisciplina(MatriculaPeriodoVO obj,  UsuarioVO usuario) throws Exception;

    public void alterarSituacaoMatriculaPeriodo(MatriculaPeriodoVO obj, OrigemFechamentoMatriculaPeriodoEnum origemFechamentoMatriculaPeriodoEnum, Integer codigoOrigemFechamentoMatriculaPeriodo, Date dataFechamentoMatriculaPeriodo) throws Exception;

    public void inicializarDadosAnoSemestreMatricula(MatriculaPeriodoVO obj, ProcessoMatriculaCalendarioVO processoMatricula, Boolean forcarInicializacao) throws Exception;

    public void incluirDadosAnoSemestreMatricula(MatriculaPeriodoVO obj, Integer periodoLetivoAtivo) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>MatriculaPeriodoVO</code>. Sempre localiza o
     * registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
     * <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>MatriculaPeriodoVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(MatriculaPeriodoVO obj, UsuarioVO usuario) throws Exception;

    public List<MatriculaPeriodoVO> consultarPorAnoSemestreUnidadeEnsino(String ano, String semestre, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public List consultarPorCursoTurmaAnoSemestre(Integer curso, Integer turma, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public List consultarPorCursoTurmaAnoSemestreBasica(Integer curso, Integer turma, Integer disciplina, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public List consultarPorMatriculaMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public List consultarPorNomePessoa(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public List consultarPorNomeCurso(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public List consultarPorMatriculaMatriculaSemestreAno(String valorConsulta, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public List<MatriculaPeriodoVO> executarEfetivacaoMatriculasPeriodoPreMatriculadas(List<MatriculaPeriodoVO> listaPreMatriculados,  UsuarioVO usuario) throws Exception;

    public MatriculaPeriodoVO efetivarMatriculaPeriodoPreMatriculada(MatriculaPeriodoVO m,  UsuarioVO usuario, boolean realizandoRecebimento) throws Exception;

    public List consultarPorUnidadeEnsino_ProcMatricula_Matricula_Situacao(Integer unidadeEnsino, Integer procMatricula, Integer curso, Integer turno, Integer turma, String situacao,
            String matricula, boolean somenteParcelaMatricula, String situacaoPreMatricula, boolean ocorreuPrimeiraAula, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>MatriculaPeriodo</code> através do valor do atributo
     * <code>matricula</code> da classe <code>Matricula</code> Faz uso da operação <code>montarDadosConsulta</code> que
     * realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>MatriculaPeriodoVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */

    public MatriculaPeriodoVO consultarPorMatriculaPeriodoLetivoSemestreAno(String matricula, Integer periodoLetivo, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public MatriculaPeriodoVO consultarPorMatriculaSemestreAno(String matricula, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public List consultarAlunosParticipamBancoCurriculoPorNome(String nomeAluno, UsuarioVO usuario) throws Exception;

    public List consultarAlunosParticipamBancoCurriculoPorTurma(String identificadorTurma, UsuarioVO usuario) throws Exception;

    public List<MatriculaPeriodoVO> consultarUltimaMatriculaPeriodoPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public MatriculaPeriodoVO consultarUltimaMatriculaPeriodoAtivaPorMatricula(String matricula, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public List consultaMatriculaPeriodoUnidadeEnsinoCurso(Integer unidadeEnsinoCurso, String semestre, String ano, String situacaoMatriculaPeriodo, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>MatriculaPeriodo</code> através do valor do atributo
     * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro. Faz uso da
     * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>MatriculaPeriodoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>MatriculaPeriodo</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>MatriculaPeriodoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public String consultarSituacaoAcademicaMatriculaPeriodo(Integer codigo) throws Exception;

    public MatriculaPeriodoVO consultarPorCodigoSemestreAno(Integer valorConsulta, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public MatriculaPeriodoVO consultarPorCodigoTranferenciaEntrada(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public Integer consultarQuantidadeAlunoMatriculadoTurmaPorSituacao(Integer codigoTurma, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
    public Integer consultarQuantidadeAlunoEmReposicaoPorTurma(Integer codigoTurma, boolean isAlunoQueEstaoFazendoReposicaoNessaTurma, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
    public Integer consultarQuantidadeAlunoMatriculadoTurma(Integer codigoTurma, Integer processoMatricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

//    public void transferirDisciplinasPreMatriculaParaMatriculaAtiva(MatriculaPeriodoVO obj,  UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir todos os objetos da <code>MatriculaPeriodoVO</code> no BD. Faz uso da operação
     * <code>excluir</code> disponível na classe <code>MatriculaPeriodo</code>.
     *
     * @param <code>matricula</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     * , 
     */
    public void excluirMatriculaPeriodos(String matricula,  UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar todos os objetos da <code>MatriculaPeriodoVO</code> contidos em um Hashtable no
     * BD. Faz uso da operação <code>excluirMatriculaPeriodos</code> e <code>incluirMatriculaPeriodos</code> disponíveis
     * na classe <code>MatriculaPeriodo</code>.
     *
     * @param objetos
     *            List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    // public void alterarMatriculaPeriodos(MatriculaVO matricula,
    // ProcessoMatriculaCalendarioVO processoMatriculaCalendario,
    // CondicaoPagamentoPlanoFinanceiroCursoVO condicao) throws Exception {
    // excluirMatriculaPeriodos(matricula.getMatricula());
    // incluirMatriculaPeriodos(matricula, processoMatriculaCalendario,
    // condicao);
    // }
    public void alterarMatriculaPeriodos(MatriculaVO matricula, ProcessoMatriculaCalendarioVO processoMatriculaCalendario,   UsuarioVO usuario) throws Exception;

    public void alterarMatriculaPeriodoVOEspecifico(MatriculaPeriodoVO obj, MatriculaVO matricula, ProcessoMatriculaCalendarioVO processoMatriculaCalendario,
              UsuarioVO usuario) throws Exception;

    /**
     * Método responsável por alterar o Periodo Letivo ativo, referente a matricula. Vale ressaltar que esta rotina não
     * gera alterações em períodos inavitos ou concluídos, pois não existem razões para que os mesmos sejam alterados
     * dentro de um fluxo normal de processamento.
     *
     * @param matricula
     * @param processoMatriculaCalendario
     * @throws Exception
     */
    public void incluirMatriculaPeriodosSemExcluir(MatriculaVO matricula, ProcessoMatriculaCalendarioVO processoMatriculaCalendario,  UsuarioVO usuario) throws Exception;

    public void criarHistoricoTransferenciaEntrada(MatriculaPeriodoVO obj,  UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por incluir objetos da <code>MatriculaPeriodoVO</code> no BD. Garantindo o relacionamento
     * com a entidade principal <code>academico.Matricula</code> através do atributo de vínculo.
     *
     * @param objetos
     *            List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirMatriculaPeriodos(MatriculaVO matricula, ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO,   UsuarioVO usuario) throws Exception;

    public void incluirMatriculaPeriodoVOEspecifico(MatriculaPeriodoVO obj, MatriculaVO matricula, ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO,
              UsuarioVO usuario) throws Exception;

    public void inicializarMatriculaPeriodoTurmaDisciplinaGradeCurso(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs,Boolean considerarVagaReposicao, Boolean inicializarMatriculaPeriodoTurmaDisciplinaGradeCurso) throws Exception;

    public void realizarCalculoValorMatriculaEMensalidade(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception;

    public void obterListaMatriculaPeriodoTurmaDisciplinaVO(MatriculaPeriodoVO matriculaPeriodo, MatriculaVO matricula, UsuarioVO usuario, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs, Boolean considerarVagaReposicao, Boolean liberarRealizarMatriculaDisciplinaPreRequisito) throws Exception;

    public boolean verificarAlunoJaAprovadoNaDisciplina_Cursando(MatriculaPeriodoVO mat, GradeDisciplinaVO grade,  UsuarioVO usuario) throws Exception;

    //public void adicionarMatriculaPeriodoTurmaDisciplinaObjEspecifico(String matricula, Integer transferencia, MatriculaPeriodoVO matriculaPeriodo, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuario) throws Exception;

    public void removerMatriculaPeriodoTurmaDisciplinaObjEspecifico(MatriculaPeriodoVO matriculaPeriodo, MatriculaVO matricula, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuario) throws Exception;

    public void adicionarMatriculaPeriodoTurmaDisciplina(String matricula, Integer transferencia, MatriculaPeriodoVO matriculaPeriodo,  UsuarioVO usuario) throws Exception;

    public void atualizarNrDisciplinasIncluidasExcluidas(MatriculaPeriodoVO matriculaPeriodo, MatriculaVO matricula, UsuarioVO usuario) throws Exception;

    //public MatriculaPeriodoTurmaDisciplinaVO gerarMatriculaPeriodoTurmaDisciplinaVO(GradeDisciplinaVO obj, MatriculaPeriodoVO matriculaPeriodo, Integer turma, Integer nrVagas);

    public void criarMatriculaPeriodoTurmaDisciplina(GradeDisciplinaVO obj, MatriculaPeriodoVO matriculaPeriodo, MatriculaVO matriculaVO, TurmaVO turma, Integer nrVagas, UsuarioVO usuario) throws Exception;

    public Boolean verificarAlunoAprovadoDisciplina(Integer disciplina, Integer transferencia, String matricula, MatriculaPeriodoVO matriculaPeriodo,  UsuarioVO usuario) throws Exception;

    public Boolean verificarAlunoAprovadoTodosPreRequisitos(List listaPreRequisitos, String matricula,  UsuarioVO usuario) throws Exception;

    public List consultarHistoricoAlunoDisciplina(String matricula, Integer disciplina,  UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por localizar um objeto da classe <code>MatriculaPeriodoVO</code> através de sua chave
     * primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public MatriculaPeriodoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public List<MatriculaPeriodoVO> consultarPorUnidadeCursoTurmaAluno(Integer unidadeEnsino, Integer curso, Integer turma, String aluno, boolean bloqueioFinanceiro, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public List<MatriculaPeriodoVO> consultarPorNomeCursoNomeTurmaAnoSemestre(String nomeCurso, String nomeTurma, String ano, String semestre, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade);

    public void emitirBoletoMatricula(MatriculaPeriodoVO obj, UsuarioVO usuarioResponsavel ) throws Exception;


    public void inicializarDescontoProgressivoPadraoMatriculaPeriodo(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario ) throws Exception;

    /**
     * Método responsável por determinar se estamos tratanto de uma pré-matricula ou de uma matrícula de fato. A
     * pré-matrícula ocorre quando estamos matriculando um aluno, em periodo letivo futuro, isto, enquanto atualmente
     * está rolando outro período letivo. Por exemplo, estamos no meio do período letivo 1/2010 e estamos registrando
     * uma matrícula periodo para 2/2010. Neste caso, estamos fazendo uma pré-matrícula. Outro aspecto importante a ser
     * considerado, refere-se ao tipo de curso. Cursos de pós-graduação, por exemplo, não estão vinculados a calendários
     * letivos fixos- cada turma tem um início e um termíno variável, não podendo ser caracterizado por um processo
     * específico.
     */
    public void definirSituacaoMatriculaPeriodoComBaseProcesso(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO,  UsuarioVO usuario) throws Exception;

    public void validarMatriculaPeriodoPodeSerAtivada(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO,  UsuarioVO usuario) throws Exception;

    public MatriculaPeriodoVO obterNovoMatriculaPeriodoBaseadoUltimoPeriodoLetivo(MatriculaVO matricula,  UsuarioVO usuario) throws Exception;

    public void validarJaExisteMatriculaPeriodo(MatriculaVO matriculaVO, MatriculaPeriodoVO obj, UsuarioVO usuario) throws ConsistirException, Exception;

    public Boolean getPermitirGerarContaReceber(MatriculaPeriodoVO matriculaPeriodoVo) throws Exception;

    public Boolean verificarMatriculaPeriodoPossuiContasReceberParaGerar(Integer codMatriculaPeriodo,  UsuarioVO usuario) throws Exception;

    public List consultarMatriculaPeriodos(String matricula, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public void executarVerificacaoPermiteCursarDisciplinaEPreRequisito(Boolean permiteCursarDisciplinaEPreRequisito, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, UsuarioVO usuario) throws Exception;

    public void verificarDisciplinasAprovadasDaGrade(MatriculaPeriodoVO matriculaPeriodoVO) throws Exception;

    public void executarFinalizarMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodoVo, OrigemFechamentoMatriculaPeriodoEnum origemFechamentoMatriculaPeriodoEnum, Integer codigoOrigemFechamentoMatriculaPeriodo) throws Exception;

    public void montarDadosProcessoMatriculaCalendarioVO(MatriculaVO matriculaVO, MatriculaPeriodoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void montarDadosProcessoMatriculaCalendarioVO(MatriculaPeriodoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<MatriculaPeriodoVO> consultaRapidaPorMatricula(String matricula, Integer unidadeEnsino, boolean controlarAcesso, DataModelo dataModelo, UsuarioVO usuario) throws Exception;
    
    public List<MatriculaPeriodoVO> consultaRapidaPorMatricula(String matricula, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
    public Integer consultaNrMatriculaPeriodo(String matricula, UsuarioVO usuario) throws Exception;
    
    public MatriculaPeriodoVO consultaRapidaBasicaMatriculaPeriodoMaiorPeriodoLetivoPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<MatriculaPeriodoVO> consultaRapidaPorNomePessoa(String nomePessoa, Integer unidadeEnsino, boolean controlarAcesso, String situacao, String ano, String semestre, UsuarioVO usuario,DataModelo dataModelo) throws Exception;

    public List consultaRapidaPorNomeCurso(String nomeCurso, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<MatriculaPeriodoVO> consultaRapidaPorData(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario,DataModelo dataModelo) throws Exception;

    public void carregarDados(MatriculaPeriodoVO obj,  UsuarioVO usuario) throws Exception;

    public void carregarDados(MatriculaPeriodoVO obj, NivelMontarDados nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public void validarMatriculaPeriodoPodeSerRealizada(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception;

    public void inicializarDadosDefinirDisciplinasMatriculaPeriodo(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs, Boolean considerarVagaReposicao, Boolean liberarRealizarMatriculaDisciplinaPreRequisito) throws Exception;
    
	public void realizarVerificacaoDeDistribuicaoDisciplinaDependenciaAutomatica(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, List<GradeDisciplinaVO> listaDisciplinasPeriodoLetivoAlunoPendente,  boolean liberadoInclusaoTurmaOutroUnidadeEnsino, boolean liberadoInclusaoTurmaOutroCurso, boolean liberadoInclusaoTurmaOutroMatrizCurricular,  boolean permitirRealizarMatriculaDisciplinaPreRequisito,  UsuarioVO usuario) throws Exception;

    public PeriodoLetivoVO executarObterPeriodoLetivoAnteriorAoPeriodoLetivoMatriculaPeriodo(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO) throws Exception;

    public void adicionarMatriculaPeriodoTurmaDisciplinaVO(MatriculaPeriodoVO matriculaPeriodo, MatriculaVO matriculaVO, TurmaVO turma, DisciplinaVO disciplina, DisciplinaVO equivale, Boolean permitirRealizarMatriculaDisciplinaPreRequisito, List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaRemovidaTemporariamenteVOs, ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuario) throws Exception;

    public void definirPeriodoLetivoNovaMatriculaAluno(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception;

    public void montarDadosProcessoMatriculaCalendarioVO(MatriculaVO matriculaVO, MatriculaPeriodoVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

    public MatriculaPeriodoVO consultaRapidaFichaAlunoPorMatriculaPeriodo(MatriculaPeriodoVO obj, Integer matriculaPeriodo) throws Exception;

    public Boolean consultarSeExisteMatriculaPeriodoVinculadaAGradeCurricular(Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

//    public void gerarMatriculaInclusaoForaPrazoLista(MatriculaVO matricula, List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinaAdicionadaVOs, List<MatriculaPeriodoVO> lista, InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO,  UsuarioVO usuario) throws Exception;

    public void processarGeracaoContasReceberMesReferenciaEspecifica(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO,   UsuarioVO usuario, Boolean simulacao) throws Exception;

    public List<MatriculaPeriodoVO> consultaRapidaPorCursoGradeCurricularPeriodoLetivoTurmaAnoSemestre(Integer codCurso, Integer gradeCurricular, Integer periodoLetivo, Integer turma, String ano, String semestre, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<MatriculaPeriodoVO> consultaRapidaMatriculaPeriodoAtivaSemMatriculaPeriodoVencimento(String parcela, String semestre, String ano, UsuarioVO usuario) throws Exception;

    public void alterarPeriodoLetivoGradeCurricular(final Integer matriculaPeriodo, final Integer periodoLetivo, final Integer gradeCurricularMigrar) throws Exception;

    public List<MatriculaPeriodoTurmaDisciplinaVO> executarObterListaDisciplinasExcluidasGradeAluno(MatriculaPeriodoVO matriculaPeriodo, MatriculaVO matricula, UsuarioVO usuario);

    /**
     * Método que altera o campo <code>reconheceuDivida</code>.
     * É passado um objeto do tipo <code>MatriculaVO</code>, e é alterado toda lista <code>matriculaVO.getMatriculaPeriodoVOs()</code>;
     * @param matriculaVO
     * @throws Exception
     */
    void alterarReconheceuDivida(MatriculaVO matriculaVO) throws Exception;

    public MatriculaPeriodoVO consultaUltimaMatriculaPeriodoPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public List<MatriculaPeriodoVO> consultarPorUnidadeCursoTurmaAlunoEntreDatasSituacao(Integer unidadeEnsino, Integer curso, Integer turma, String aluno, Date dataInicio, Date dataFim, String situacao, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public void alterarTurmaBaseMatriculaPeriodo(Integer matriculaPeriodo, Integer turma) throws Exception;

    public MatriculaPeriodoVO consultaRapidaPorMatriculaSemestreAno(String matricula, String semestre, String ano, boolean retornarExcecao, boolean b, Optional<Date> dataInicio, Optional<Date> dataFim, UsuarioVO usuario) throws Exception;

    public List consultaRapidaPorNomeCursoTurnoSituacao(String nomeCurso, Integer unidadeEnsino, Integer turno, String situacao, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario,DataModelo dataModelo) throws Exception;

    public MatriculaPeriodoVO consultarPorMatriculaPeriodoLetivoMatricula(String matricula, Integer periodoLetivo, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public void processarRotinaSetarCondicaoPagamentoAluno(UsuarioVO usuario) throws Exception;

    public MatriculaPeriodoVO consultarPorTurmaCursoAnoSemestre(Integer turma, Integer curso, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public MatriculaPeriodoVO consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public MatriculaPeriodoVO consultarRapidaPorMatriculaPeriodoLetivoMatricula(String matricula, Integer periodoLetivo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public void atualizarUnidadeEnsinoCursoETurma(Integer codigoMatriculaPeriodo, Integer codigoTurma, Integer codigoUnidadeEnsinoCurso) throws Exception;

    public void atualizarPlanoFinanceiroCursoECondicaoPagamentoDaMatricula(Integer codigoMatriculaPeriodo, Integer codigoPlanoFinanceiroCurso, Integer condicaoPagamento) throws Exception;

    public List<MatriculaPeriodoVO> consultarPorContaReceberMatriculaPeriodo(Integer contaReceber,  UsuarioVO usuario) throws Exception;

    public void atualizarContaReceberMatriculaPeriodo(Integer codigoMatriculaPeriodo) throws Exception;

    public List consultaRapidaPorUnidadeEnsinoCursoAnoSemestreSituacao(Integer unidadeEnsinoCurso, String semestre, String ano, Integer turma, String situacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public MatriculaPeriodoVO consultarUltimaMatriculaPeriodoAtivaPorMatriculaSemExcecao(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void validarDadosRenovacaoProcessoMatriculaPeriodoLetivo(MatriculaVO matriculaVO, MatriculaPeriodoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO conf) throws Exception;

    public void alterarSituacaoMatriculaPeriodoPorCodigo(final Integer matriculaPeriodo, final String situacaoMatriculaPeriodo, final OrigemFechamentoMatriculaPeriodoEnum origemFechamentoMatriculaPeriodoEnum, final Integer codigoOrigemFechamentoMatriculaPeriodo, final Boolean alunoTransferidoUnidade, Date dataFechamentoMatriculaPeriodo) throws Exception;

    public MatriculaPeriodoVO consultarUltimaMatriculaPeriodoAtivaOuTrancadaPorMatriculaSemExcecao(String matricula, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public void executarFinalizarMatriculaPeriodoTrancamento(MatriculaPeriodoVO matriculaPeriodoVO, OrigemFechamentoMatriculaPeriodoEnum origemFechamentoMatriculaPeriodoEnum, Integer codigoOrigemFechamentoMatriculaPeriodo, UsuarioVO usuario) throws Exception;

    public List consultaRapidaPorNomeCursoAnoSemestre(String nomeCurso, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public Boolean consultarSeAlunoPossuiMaisDeUmaMatriculaPeriodoAtiva(String matricula) throws Exception;

    public Boolean consultarExistenciaMatriculaPeriodoPorSituacao(String matricula, String situacao) throws Exception;

    public MatriculaPeriodoVO consultaRapidaPorMatriculaNrPeriodoLetivo(String matricula, Integer periodoLetivo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<MatriculaPeriodoVO> consultaRapidaMatriculaNrPeriodoLetivo(String matricula, Integer periodoLetivo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<MatriculaPeriodoVO> consultaRapidaPorUnidadeCursoTurmaAlunoEntreDatasSituacao(Integer unidadeEnsino, Integer curso, Integer turma, String aluno, Date dataInicio, Date dataFim, String situacao, UsuarioVO usuario) throws Exception;

    public List<MatriculaPeriodoVO> consultaRapidaPorUnidadeCursoTurmaAlunoEntreDatasSituacaoParaSetransp(Integer unidadeEnsino, Integer curso, Integer turma, String aluno, Date dataInicio, Date dataFim, String situacao, String ano, String semestre, String tipoFiltroPeriodicidade) throws Exception;

    public MatriculaPeriodoVO consultarPorCodigo(Integer codigoPrm, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public Integer consultaQuantidadeAlunoPorTurmaTurmaSituacaoMatriculaPeriodoESituacao( boolean controlarReprovacaoPeriodoLetivo,   Integer numeroDisciplinaConsiderarReprovadoPeriodoLetivo,  UsuarioVO usuario) throws Exception;
    
    public List<MatriculaPeriodoVO> consultaRapidaPorTurmaTurmaSituacaoMatriculaPeriodoESituacao( boolean controlarReprovacaoPeriodoLetivo,   Integer numeroDisciplinaConsiderarReprovadoPeriodoLetivo,  UsuarioVO usuario) throws Exception;

    public MatriculaPeriodoVO consultaRapidaBasicaMatriculaPeriodoPorMatriculaAnoSemestre(String valorConsulta, String ano, String semestre, Integer turma, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String situacaoMatricula) throws Exception;

    //public List processarListaMatriculasRenovar(List listaMatriculasProcessar) throws Exception;
    /**
     * Método responsável por cancelar uma Pré-Matrícula. Primeiro consulta uma contaReceberVO referente a matriculaPeridoVO. Caso a contaReceberVO esteja em situação
     * REcebido ou NEgociado é lançado uma exceção. Caso contrário, a MatriculaperiodovencimentoVO e contaReceberVO  são excluidas e a matriculaPeriodoVO é alterado.
     * Os campos alterados do objeto matriculaPeriodoVO são: situacaoMatriculaPeriodo, dataFechamentoMatriculaPeriodo, origemFechamentoMatriculaPeriodo,
     * codigoOrigemFechamentoMatriculaPeriodo, usuarioResponsavelConfirmacaoOuCancelamentoPreMatricula. Após alterar os dados no BD, a matriculaPeriodoVO é
     * removido da lista;
     * @param matriculaPerido
     * @param usuaroLogado
     * @throws Exception
     */
    public void executarCancelamentoPreMatricula(MatriculaPeriodoVO matriculaPerido, Integer usuaroLogado, List<MatriculaPeriodoVO> listaAlunosPreMatriculados, UsuarioVO usuario) throws Exception;

    public List criarContaReceberBolsaCusteadaMensalidade( MatriculaVO matriculaVO,
            MatriculaPeriodoVO matriculaPeriodoVO,  UsuarioVO usuarioVO) throws Exception;

//    public List<CondicaoPagamentoPlanoFinanceiroCursoVO> executarMontagemComboCondicaoPagamentoPlanoFinanceiroCurso(Integer planoFinanceiroCurso, 
//            Integer condicaoPagamentoPlanoFinanceiroCurso, 
//            MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuarioVO) throws Exception;
    

    /**
     * 
     * Metodo que retorna a lista de CondicaoPagamentoPlanoFinanceiroCursoVO já validado as condicoes da aba Outras Configuracoes no cadastro da Condicao de Pagamento 
     *  
     * @param planoFinanceiroCurso
     * @param categoria
     * @param matriculaVO
     * @param matriculaPeriodoVO
     * @param usuarioVO
     * @return
     * @throws Exception
     */
//	public List<CondicaoPagamentoPlanoFinanceiroCursoVO> executarMontagemComboCondicaoPagamentoPlanoFinanceiroCurso(Integer planoFinanceiroCurso, String categoria, MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuarioVO) throws Exception;

    /**
     * Método que vare a lista de matriculaPerido montando uma outra lista de matriculaPeriodo que não podem serem ativadas.
     * As que não podem serem ativadas são as que o aluno possuir uma outra matriculaPeriodo já ATiva.
     * Caso não haja nenhuma pré-matrícula que não possoa ser efetiva, todas as pré-matrículas já são efetivadas.
     * @param listaPreMatriculados
     * @return List
     * @throws Exception
     */
    public void executarValidacaoParaEfetivarMatriculasPeriodoPreMatriculadas(List<MatriculaPeriodoVO> listaPreMatriculados,  ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

    public void alterarSituacaoMatriculaPeriodoComUsuarioResponsavel(MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception;

    public List executarValidacaoParaCancelarMatriculasPeriodoPreMatriculadas(List listaPreMatriculados,  UsuarioVO usuario) throws Exception;

    public List executarCancelamentoMatriculasPeriodoPreMatriculadas(List listaPreMatriculados,  UsuarioVO usuario) throws Exception;

    public void excluirComBaseNaMatricula(String matricula,  UsuarioVO usuarioLogado) throws Exception;

    public String consultarSituacaoFinanceiraMatriculaPeriodo(Integer codigo) throws Exception;

    public List consultaRapidaPorUnidadeEnsinoCursoTurmaPeriodoLetivoAtivoSituacao(Integer curso, Integer turno,  List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer periodoLetivoAtivoUnidadeEnsinoCurso, Integer turma, String situacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultaRapidaPorIdentificadorTurmaSituacaoMatriculaPeriodo(String identificadorTurma, Integer unidadeEnsino, String situacao, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario,DataModelo dataModelo) throws Exception;

    public void alterarContaReceberLiberandoPagamentoParcela(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, String parcela, String justificativa,  UsuarioVO usuario) throws Exception;

    public void liberarContasTransferenciaUnidade(MatriculaVO matriculaVoNova, MatriculaPeriodoVO matriculaPeriodoVoOrigem, MatriculaPeriodoVO matriculaPeriodoVoDestino, ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO, UsuarioVO usuarioResponsavel,  UsuarioVO usuario) throws Exception;

    public Boolean consultarExistenciaMatriculaPeriodoPorProcessoMatriculaSituacao(Integer processoMatricula, String situacao) throws Exception;

    public Integer consultarQuantidadeAlunosMatriculadosProcessoMatricula(Integer processoMatricula) throws Exception;

    public List<MatriculaPeriodoVO> consultaRapidaPorProcessoMatricula(Integer processoMatricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public Integer consultarQuantidadeParcelaContratoMatriculaNaoControlada(Integer matriculaperiodo, String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public MatriculaPeriodoVO consultaRapidaMatriculaPeriodoUnica(String matricula, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public Integer consultarQuantidadeAlunoMatriculadoPeriodoAtivo(String nivelEducacional, Integer unidadeEnsino, Integer curso, Integer disciplina, Integer turma, UsuarioVO usuario) throws Exception;

    public Integer consultarQuantidadeAlunosAtivos() throws Exception;

    public void alterarMatriculaPeriodoFinanceiroManual(MatriculaPeriodoVO matriculaPeriodoVo, Integer responsavel, Boolean financeiroManual) throws Exception;

    public void mudarMatriculaPeridoFinanceiroParaManual(MatriculaPeriodoVO matriculaPeriodoVo, Integer responsavel) throws Exception;

    public MatriculaPeriodoVO consultaUltimaMatriculaPeriodoPorMatriculaConsultaBasica(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public List<MatriculaPeriodoVO> consultaRapidaMatriculaPeriodoUnicaPorTurma(List<MatriculaVO> listaMatriculaVO, Integer unidadeEnsino, Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
    public List<MatriculaPeriodoVO> consultaRapidaMatriculaPeriodoUnicaParaReposicaoAlunoTurma(Integer unidadeEnsino, Integer turma, boolean isAlunoQueEstaoFazendoReposicaoNessaTurma, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<MatriculaPeriodoVO> consultaRapidaMatriculaPeriodoUnicaPorTurmaSituacao(TurmaVO turma, List<TurmaDisciplinaVO> listaTurmaDisciplinaVO, List<String> listaMensagemErro, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public void alterarPeriodoLetivoGradeCurricularPorMatriculaPeriodoTurma(TurmaVO turmaVO, String situacaoMatricula,  String situacaoMatriculaPeriodo, List<MatriculaPeriodoVO> listaMatriculaPeriodoVO, UsuarioVO usuarioVO, Boolean realizandoTransferenciaMatrizCurricularPelaTurma) throws Exception;

    public MatriculaPeriodoVO consultaRapidaPorHistoricoDadosGradeCurricular(Integer historico, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public void removerMatriculaPeriodoTurmaDisciplinaComposta(List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVOs, MatriculaPeriodoTurmaDisciplinaVO obj, UsuarioVO usuario) throws Exception;

    public List consultarMatriculaSuspensaParaSerListadaParaCancelamento(List<UnidadeEnsinoVO> listaUnidadeEnsino, String matricula, Integer curso, Integer turma, Integer nrdiaslistarmatriculapendenciadocumentosparacancelamento, int nivelMontarDados, UsuarioVO usuario, String tipoSuspensaoMatricula, String situacaoSolicitacao, Boolean permitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira, Boolean permitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira, Boolean permitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica, Boolean permitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica, Boolean permitirUsuarioVisualizarApenasSuasSolicitacoes, Boolean permitirVisualizarPendenciasDeferidasIndeferidasOutrosUsuariosMesmaUnidade) throws Exception;

    public List consultarMatriculaSerasa(Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public MatriculaPeriodoVO consultaRapidaPorMatriculaTurma(String matricula, Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public void consultarPorMatriculaPeriodoLetivoAtivoAnoSemestre(String matricula, String ano, String semestre, Integer codigoDesconsiderar, UsuarioVO usuario) throws Exception;
    
    public MatriculaPeriodoVO consultaRapidaBasicaUltimaMatriculaPeriodoAtivaPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public MatriculaPeriodoVO consultaRapidaPorMatriculaAnoSemestre(String matricula, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public MatriculaPeriodoVO consultarUltimaMatriculaPeriodoPorMatriculaConclusaoCurso(String matricula, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public Boolean consultarExistenciaMaisDeUmaMatriculaPeriodo(String matricula) throws Exception;

    public List<MatriculaPeriodoVO> consultaPreMatriculaParaExclusao() throws Exception;

    public void verificarPreRequisitoDisciplina(MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, Boolean permitirUsuaroRealizarMatriculaComDisciplinaPreRequisito, UsuarioVO usuario) throws Exception;

    public MatriculaPeriodoVO consultaRapidaBasicaPrimeiraMatriculaPeriodoPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public Boolean consultarAlunoAtivoPorRegistroAula(Integer registroAula, Integer turma, UsuarioVO usuarioVO) throws Exception;

    public MatriculaPeriodoVO consultarUltimaMatriculaPeriodoPorMatriculaAnoSemestre(String matricula, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public Boolean consultarAlunoBolsistaUltimaMatriculaPeriodo(String matricula) throws Exception;

    public MatriculaPeriodoVO consultaRapidaBasicaUltimaMatriculaPeriodoPorMatriculaOrdenandoPorAnoSemestrePeriodoLetivo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public Boolean consultarPorCodigoProcessoMatriculaUnidadeEnsinoCurso(Integer processoMatricula, Integer curso, Integer turno) throws Exception;

    public List consultarAlunosParticipamBancoCurriculoContratadosPorTurma(String identificadorTurma, UsuarioVO usuario) throws Exception;

    public List consultarAlunosParticipamBancoCurriculoSelecionadosPorTurma(String identificadorTurma, UsuarioVO usuario) throws Exception;

    public void consultaRapidaPorTurmaMatriculaDocumentoPendente(DataModelo controleConsultaOtimizado, Integer unidadeEnsino, Integer turma, String matricula, Boolean trazerDocumentosIndeferidos, Boolean trazerDocumentosDeferidos, Boolean trazerDocumentosPendentes, Integer chamada,  String nivelEducacional, UsuarioVO usuarioVO) throws Exception;

 

	void realizarRegistroNotificacaoAbandonoCurso(Integer matriculaPeriodo) throws Exception;

	List<MatriculaPeriodoVO> consultarMatriculaPeriodoNotificacaoAbandonoCurso() throws Exception;

	void realizarRegistroAbandonoCurso() throws Exception ;

	List<MatriculaPeriodoVO> consultarMatriculaPeriodoAptoRegistrarAbandonoCurso() throws Exception;

    public void verificarGradeAlunosMatriculadosTurma(Integer codGrade, Integer periodoLetivo, Integer codTurma, UsuarioVO usuarioVO) throws Exception;

    public void validarDadosExistenciaMatriculaPeriodo(String matricula, String ano, String semestre, UsuarioVO usuarioVO) throws Exception;

    MatriculaPeriodoVO consultarUltimaMatriculaPeriodoPorMatriculaSituacoes(String matricula, String situacoes, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    List<String> consultarAnosMatriculaPeriodo() throws Exception;

    public List consultarMatriculaSerasaPagaramParcela(Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public Integer consultarQuantidadeAlunoPreMatriculadoPeriodoAtivo(String nivelEducacional, Integer unidadeEnsino, Integer curso, Integer disciplina, Integer turma, UsuarioVO usuario) throws Exception;
    
    public Boolean verificaPossuiMatriculaPreMatricula(String matricula) throws Exception;
    
    public List<MatriculaPeriodoVO> consultarAnoSemstrePeriodoLetivoMatriculaPeriodoPorMatriucla(String matricula, Integer periodoLetivo, UsuarioVO usuarioVO) throws Exception;
    
    public MatriculaPeriodoVO consultarAnoSemestreMatriculaPeriodoPorCodigo(Integer matriculaPeriodo, UsuarioVO usuarioVO) throws Exception;

	public List<MatriculaPeriodoVO> consultarPorUndiadeEnsinoProcessoMatriculaCursoTurnoTurmaSituacaoMatricula(String matricula, Integer unidadeEnsino, Integer procMatricula, Integer curso, Integer turno, Integer turma, String situacao, boolean somenteParcelaMatricula, String situacaoPreMatricula, boolean ocorreuPrimeiraAula, Boolean trazerPreMatriculasCanceladas, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;
	
	public void executarVerificarSeHaIncompatibilidadeHorarioDeDisciplinas(MatriculaPeriodoVO matriculaPeriodo, TurmaVO turmaInclusao, DisciplinaVO disciplinaInclusao,  Boolean validarChoqueHorarioOutraMatricula,  UsuarioVO usuario) throws Exception;
	
    public void executarVerificarSeHaIncompatibilidadeHorarioDeDisciplinas(MatriculaPeriodoVO matriculaPeriodo, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO,  Boolean validarChoqueHorarioOutraMatricula, UsuarioVO usuario) throws Exception;
        
	public void validarMatriculaPeriodoPodeSerRealizadaSemValidarDataMatriculaCalendario(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception;

	MatriculaPeriodoVO consultarUltimaMatriculaPeriodoAtivaPreMatriculaPorMatricula(String matricula, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;
        
        public void calcularTotalCHCreditoMatrizAteDeterminadoPeriodo(PeriodoLetivoVO periodoFinalCalcular, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuarioVO) throws Exception;
        
        public void calcularTotalCHCreditoAlunoCursouAteDeterminadoPeriodo(PeriodoLetivoVO periodoFinalCalcular, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuarioVO) throws Exception;
        
        public List<PeriodoLetivoVO> obterListaPeriodosLetivosValidosParaRenovacaoMatriculaInicializandoPeriodoLetivoPadrao(
            MatriculaVO matriculaVO,
            MatriculaPeriodoVO matriculaPeriodoVO,
            ConfiguracaoAcademicoVO configuracaoAcademicoVO,
            MatriculaComHistoricoAlunoVO matriculaComHistoricoAlunoVO,           
            UsuarioVO usuario) throws Exception;
        
        public List<PeriodoLetivoVO> obterListaPeriodosLetivosValidosParaRenovacaoMatriculaInicializandoPeriodoLetivoPadrao(
        		MatriculaVO matriculaVO,
        		MatriculaPeriodoVO matriculaPeriodoVO,
        		ConfiguracaoAcademicoVO configuracaoAcademicoVO,
        		MatriculaComHistoricoAlunoVO matriculaComHistoricoAlunoVO,
        		Boolean realizandoNovaMatriculaAlunoPartindoTransferenciaInterna,
        		UsuarioVO usuario) throws Exception;
        
        public Boolean validarAlunoPodeCursarDisciplinaPorMapaEquivalencia(
            MatriculaVO matriculaVO,
            MatriculaPeriodoVO matriculaPeriodoVO,
            MapaEquivalenciaDisciplinaVO mapa, Boolean retornarExcecao) throws Exception;
        
        public void adicionarEValidarMatriculaPeriodoTurmaDisciplinaVO(
            MatriculaPeriodoVO matriculaPeriodo,
            MatriculaVO matricula,
            Boolean liberarRealizarMatriculaDisciplinaPreRequisito, 
            TurmaVO turma,
            TurmaDisciplinaVO turmaDisciplina,Boolean considerarVagaReposicao,
            UsuarioVO usuario) throws Exception;
        
        public void adicionarEValidarMatriculaPeriodoTurmaDisciplinaVO(
            MatriculaPeriodoVO matriculaPeriodoVO,
            MatriculaVO matriculaVO,
            Boolean liberarRealizarMatriculaDisciplinaPreRequisito, // liberado via senha pelo usuário que está renovando...
            MatriculaPeriodoTurmaDisciplinaVO matricaPeriodoTurmaDisciplina,  Boolean considerarVagaReposicao,
            UsuarioVO usuario) throws Exception;
        
        public void alterarSituacaoMatriculaPeriodoFormada(final MatriculaVO obj, final String situacao, UsuarioVO usuarioVO) throws Exception;
		
//		public List<MapaAlunoAptoFormarVO> consultarMapaAlunoAptoFormar(String matricula, Integer curso, Integer turma , String situacaoCurricular) throws Exception;
		
		public MatriculaPeriodoVO consultarMatriculaPeriodoRenovadaVisaoAlunoPorSituacaoUnidadeEnsinoCursoTurnoSituacaoPeriodoLetivoAtivoUnidadeEnsinoCurso(Integer turno, Integer curso, Integer unidadeEnsino, String situacaoPeriodoLetivoAtivoUnidadeEnsinoCurso, String situacao, boolean visaoAluno, boolean controlarAcesso, int nivelMontarDados, String matricula, UsuarioVO usuarioVO) throws Exception;

		List<GradeDisciplinaCompostaVO> realizarObtencaoMatriculaPeriodoTurmaDisciplinaFazParteComposicao(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, MatriculaPeriodoVO matriculaPeriodoVO) throws Exception;
		
		public void verificarDisciplinaPodeSerEstudaEmRegimeEspecial(MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, MatriculaPeriodoTurmaDisciplinaVO matricaPeriodoTurmaDisciplina, UsuarioVO usuario) throws Exception;
		
		public MatriculaPeriodoTurmaDisciplinaVO gerarMatriculaPeriodoTurmaDisciplinaVOValidado(GradeDisciplinaVO gradeDisciplinaVO, Boolean disciplinaPorEquivalencia, 
				MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplina,
				MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursada, 
				Boolean disciplinaReferenteAUmGrupoOptativa, GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, Boolean disciplinaEmRegimeEspecial,
				Boolean disciplinaDeUmaComposicao, GradeDisciplinaCompostaVO disciplinaCompostaVO, ModalidadeDisciplinaEnum modalidadeDisciplinaEnum, MatriculaPeriodoVO matriculaPeriodo, MatriculaVO matricula, TurmaVO turma, TurmaVO turmaPratica, TurmaVO turmaTeorica, UsuarioVO usuario) throws Exception;
		
		public void validarMatriculaPeriodoTurmaDisciplinaVOParaAluno(MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, Boolean liberarRealizarMatriculaDisciplinaPreRequisito, MatriculaPeriodoTurmaDisciplinaVO matricaPeriodoTurmaDisciplina, UsuarioVO usuario, Boolean realizandoRecebimento) throws Exception;
		
		public List<MatriculaPeriodoTurmaDisciplinaVO> gerarEAdicionarMatriculaPeriodoTurmaDisciplinaVOParaDisciplinasComposicao(MatriculaPeriodoTurmaDisciplinaVO novaMatriculaPeriodoTurmaDisciplina, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, UsuarioVO usuario, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception;
        
		public void validarDisciplinaIncluidasPodeSerAdicionadaMatriculaPeriodoConformeLimiteMinimoPeriodoLetivo(MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO,  UsuarioVO usuario);
		
		public void validarDisponibilidadeVagasMatriculaPeriodoTurmaDisciplina(MatriculaVO matriculaVO, 
                MatriculaPeriodoVO matriculaPeriodoVO, Boolean considerarVagaReposicao, UsuarioVO usuario) throws Exception;
        
        public MatriculaPeriodoVO consultaRapidaPorMatriculaSemestreAnoSemExcecao(String matricula, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
        
        public void gerarInclusaoForaPrazo(boolean incluindaPeloPagamentoRequerimento, InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO, List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVOs, MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO,  GradeCurricularVO gradeCurricularVO, UsuarioVO usuarioVO) throws Exception;
        
        public MatriculaPeriodoVO consultarDadosCompletosMatriculaPeriodoPorMatriculaAnoSemestre(String matricula, String ano, String semestre,  UsuarioVO usuarioVO) throws Exception;
        
	public List<MatriculaPeriodoVO> consultaRapidaBasicaUltimaMatriculaPeriodoPorGradeCurricularAtualTransferenciaMatrizCurricular(Integer matrizCurricular, Integer unidadeEnsino, Integer nrPeriodoLetivoInicial, Integer nrPeriodoLetivoFinal, String situacaoMatricula, Integer turma, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
        
        public void alterarPeriodoLetivo(Integer periodoLetivo, Integer matriculaPeriodo) throws Exception;
        
        public Boolean consultarExistenciaMatriculaPeriodoAtivaPorSituacao(String matricula, Integer matriculaPeriodo, String situacao) throws Exception;

		void executarExclusaoPreMatricula(MatriculaPeriodoVO matriculaPeriodo,  ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  List<MatriculaPeriodoVO> listaPreMatriculados, UsuarioVO usuario) throws Exception;

		List<MatriculaPeriodoVO> executarExclusaoMatriculasPeriodoPreMatriculadas(List<MatriculaPeriodoVO> listaPreMatriculados,  ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  UsuarioVO usuario) throws Exception;

		List<MatriculaPeriodoVO> executarValidacaoParaExcluirMatriculasPeriodoPreMatriculadas(List<MatriculaPeriodoVO> listaPreMatriculados,  ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  UsuarioVO usuario) throws Exception;
		public void executarCalculoAtualizarTotaisCargaHorariaCreditoMatriculaPeriodoAluno(MatriculaPeriodoVO matriculaPeriodoVO, List<MatriculaPeriodoTurmaDisciplinaVO> disciplinasAluno) throws Exception;

		void verificarOrdemEQtdeEstudandoMatriculaPeriodo(Integer codigoMatriculaPeriodo, Map<String, Integer> auxiliar) throws Exception;

		Date realizarConsultaDataParcelaMatricula(Integer codigoMatriculaPeriodo) throws Exception;

		public void incluirObservacaoCriterioAvaliacaoAluno(MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuarioVO) throws Exception;
		
		public String consultarAnoIngressoMatriculaPeriodoPorMatricula(String matricula, UsuarioVO usuarioVO) throws Exception;
		
		public String consultarSemestreIngressoMatriculaPeriodoPorMatricula(String matricula, UsuarioVO usuarioVO) throws Exception;

		void validarExistenciaMatriculaPeriodoAptaRealizarTrancamentoCancelamentoTransferencia(String matricula) throws Exception;

		/**
		 * @author Wellington Rodrigues
		 * 03/03/2015
		 * @param nrMensalidade
		 * @param matriculaPeriodoVO
		 * @param usuario
		 * @return
		 * @throws Exception
		 */
		Date montarDataVencimento(Integer nrMensalidade, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception;

		boolean realizarVerificacaoMatriculaPeriodoEstaPendenteFinanceiroAoEstornarMensalidade(Integer matriculaPeriodo) throws Exception;
		boolean realizarVerificacaoMatriculaPeriodoControlaMatricula(Integer matriculaPeriodo) throws Exception;

		void realizarEstornoSituacaoMatriculaPeriodoAoEstornarContaReceber(  UsuarioVO usuario) throws Exception;

		public Integer consultaRapidaPorCodigoMatPer_CodigoAlternativoGeracaoBoletoSicoob(Integer codigo) throws Exception;
		
		public List<MatriculaPeriodoVO> consultarMatriculaPeriodoPorMatriculaSituacoes(String matricula, String situacoes, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;
		
		public MatriculaPeriodoVO consultarCodigoUnidadeEnsinoCursoDataPorMatriculaPeriodoLetivoSemestreAno(String matricula, Integer periodoLetivo, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;
		
		public MatriculaPeriodoVO consultarFinanceiroManulaPorChavePrimaria(Integer matriculaPeriodo, UsuarioVO usuarioVO);
		
		/** 
		 * @author Wellington - 23 de set de 2015 
		 * @param matriculaVO
		 * @param matriculaPeriodoVO
		 * @param processoMatriculaCalendarioVO
		 * @param configuracaoFinanceiroVO
		 * @param usuarioVO
		 * @return
		 * @throws Exception 
		 */
		Double executarValidacaoSeraGeradoParcelaExtraRepassarMatricula(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO,  UsuarioVO usuarioVO) throws Exception;

		void atualizarSituacaoMatriculaPeriodo(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO,  UsuarioVO usuarioVO) throws Exception;
                
    public Boolean verificarMatriculaDeCalouro(String valorConsulta, Integer codigoMatriculaPeriodoAtual, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public Boolean verificarMatriculaDeVeterano(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Wellington - 5 de jan de 2016 
	 * @param nomePessoa
	 * @param unidadeEnsino
	 * @param ano
	 * @param semestre
	 * @param controlarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<MatriculaPeriodoVO> consultaRapidaPorNomePessoaAnoSemestre(String nomePessoa, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Wellington - 5 de jan de 2016 
	 * @param identificadorTurma
	 * @param unidadeEnsino
	 * @param situacao
	 * @param ano
	 * @param semestre
	 * @param controlarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<MatriculaPeriodoVO> consultaRapidaPorIdentificadorTurmaSituacaoMatriculaPeriodoAnoSemestre(String identificadorTurma, Integer unidadeEnsino, String situacao, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Wellington - 7 de jan de 2016 
	 * @param unidadeEnsinoCurso
	 * @param semestre
	 * @param ano
	 * @param situacao
	 * @param controlarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<MatriculaPeriodoVO> consultaRapidaPorUnidadeEnsinoCursoAnoSemestre(Integer unidadeEnsinoCurso, String semestre, String ano, String situacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Wellington - 11 de jan de 2016 
	 * @param matricula
	 * @param unidadeEnsino
	 * @param situacao
	 * @param ano
	 * @param semestre
	 * @param controlarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<MatriculaPeriodoVO> consultaRapidaPorMatriculaAnoSemestre(String matricula, Integer unidadeEnsino, String situacao, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception;



	/** 
	 * @author Wellington - 13 de jan de 2016 
	 * @param matriculaPeriodo
	 * @param controlarAcesso
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
	boolean executarVerificacaoExisteMatriculaPeriodoRenovadaAposMatriculaPeriodoEspecifica(Integer matriculaPeriodo, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	Integer consultaCodigoUltimaMatriculaPeriodoPorMatricula(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	void alterarSituacaoMatriculaPeriodoEstornoCancelamento(final Integer matriculaPeriodo, final UsuarioVO usuarioVO) throws Exception;
	
	void alterarSituacaoMatriculaPeriodoEstornoTrancamento(final Integer matriculaPeriodo, final UsuarioVO usuarioVO) throws Exception;
        
        public Boolean consultarMatriculaIntegracaoFinanceiraPodeRenovarVisaoAluno(String matriculaAluno, Date dataCompetenciaMatricula) throws Exception;        

	/**
	 * @author Rodrigo Wind - 22/01/2016
	 * @param matriculaPeriodoVO
	 * @param configuracaoAcademicoVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	void realizarRemocaoAutomaticaDisciplinaSemVaga(MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Wellington - 26 de fev de 2016 
	 * @param matricula
	 * @param controlarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<String> consultarAnoSemestreCursadoAluno(String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public Boolean consultarExistenciaMatriculaPeriodoPorMatriculaAnoSemestre(String matricula, String ano, String semestre,String periodicidadeCurso, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Wellington - 20 de abr de 2016 
	 * @param matriculaPeriodoVO
	 * @param mptd
	 * @param subturmaPraticaDestino
	 * @param visaoAluno
	 * @param usuarioVO
	 * @param ce
	 * @return
	 * @throws Exception 
	 */
	boolean executarVerificacaoQtdeMaximaAlunosTurmaPraticaChoqueHorarioRegistroAulaTransferir(MatriculaPeriodoVO matriculaPeriodoVO, MatriculaPeriodoTurmaDisciplinaVO mptd, TurmaVO subturmaPraticaDestino, boolean visaoAluno, Boolean validarChoqueHorarioOutraMatricula, UsuarioVO usuarioVO, ConsistirException ce) throws Exception;

	/** 
	 * @author Wellington - 20 de abr de 2016 
	 * @param matriculaPeriodoVO
	 * @param mptd
	 * @param subturmaTeoricaDestino
	 * @param visaoAluno
	 * @param usuarioVO
	 * @param ce
	 * @return
	 * @throws Exception 
	 */
	boolean executarVerificacaoQtdeMaximaAlunosTurmaTeoricaChoqueHorarioRegistroAulaTransferir(MatriculaPeriodoVO matriculaPeriodoVO, MatriculaPeriodoTurmaDisciplinaVO mptd, TurmaVO subturmaTeoricaDestino, boolean visaoAluno, Boolean validarChoqueHorarioOutraMatricula, UsuarioVO usuarioVO, ConsistirException ce) throws Exception;

	/**
	 * @author Rodrigo Wind - 28/04/2016
	 * @param matriculaPeriodo
	 * @param condicaoPlano
	 * @return
	 * @throws Exception
	 */
	Boolean consultarMatriculaPeriodoAlterouCondicaoPlanoFinanceiroCurso(Integer matriculaPeriodo, Integer condicaoPlano) throws Exception;
	
	public void carregarDadosConsultorUsuarioResp(MatriculaVO matri, MatriculaPeriodoVO obj, UsuarioVO usuario) throws Exception;
	
	/**
	 * @author Carlos Eugênio - 30/05/2016
	 * @param matricula
	 * @param usuarioVO
	 * @return
	 */
	MatriculaPeriodoVO consultarUltimoAnoSemestrePorMatricula(String matricula, UsuarioVO usuarioVO);

	/**
	 * @author Carlos Eugênio - 30/05/2016
	 * @param matricula
	 * @param usuarioVO
	 * @return
	 */
	String consultarSituacaoAcademicaUltimoPeriodoPorMatricula(String matricula, UsuarioVO usuarioVO);

	/**
	 * @author Rodrigo Wind - 03/06/2016
	 * @param matriculaPeriodoVO
	 * @throws Exception
	 */
	void executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudarNaoInformado(MatriculaPeriodoVO matriculaPeriodoVO) throws Exception;
	
	/**
	 * @author Jean Pierre - 17/06/2016
	 * @param cpf
	 * @param unidadeEnsino
	 * @param controlarAcesso
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	List<MatriculaPeriodoVO> consultaRapidaPorCpfAluno(String cpf, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuarioVO,DataModelo dataModelo) throws Exception;
	
	/**
	 * @author Jean Pierre - 21/06/2016
	 * @param matricula
	 * @return
	 * @throws Exception
	 */
	public List<Integer> consultarMatriculaPeriodoMesmoPeriodoLetivoDesconsiderandoCanceladaAbandonoCurso(String matricula) throws Exception;
	
	/**
	 * @author Jean Pierre - 24/06/2016
	 * @param matricula
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	public Integer consultarCodigoMatriculaPeriodoUltimoPeriodoPorMatricula(String matricula) throws Exception;
	
	/**
	 * @author Jean Pierre - 27/06/2016
	 * @param matriculaPeriodoVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	public void executarAtivacaoUltimaMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodoVO,UsuarioVO usuarioVO) throws Exception;

	Date consultarDataMatriculaPeriodoPorMatriculaAnoSemestre(String matricula, String ano, String semestre)
			throws Exception;

	void realizarValidacaoMatriculaPeriodoIncluiuQuantidadeExigidaDisciplinasGrupoOptativas(MatriculaVO matriculaVO,
			MatriculaPeriodoVO matriculaPeriodoVO) throws Exception;

	void realizarDefinicaoNumeroVagaDisciplinaCompostaComLimitacaoDisciplina(MatriculaPeriodoVO matriculaPeriodoVO,
			MatriculaPeriodoTurmaDisciplinaVO matricaPeriodoTurmaDisciplina);
			
	void validarDisciplinaSemVaga(MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuarioVO) throws Exception;

//	void realizarSugestaoTurmaPraticaTeorica(MatriculaPeriodoVO matriculaPeriodoVO,
//			ConfiguracaoAcademicoVO configuracaoAcademicoVO, List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs, Boolean considerarVagaReposicao,
//			UsuarioVO usuarioVO) throws Exception;

	void realizarVerificacaoRestricoesParaExclusaoMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodoVO,
			UsuarioVO usuarioVO) throws Exception;
	
	public List<MatriculaPeriodoVO> consultarMatriculaPeriodosComContrato(String matricula, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario, Boolean trazerApenasUltimoContratoMatricula) throws Exception;
	
	/**
	 * @author Carlos Eugênio - 25/01/2017
	 * @param matricula
	 * @param situacoes
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	MatriculaPeriodoVO consultaRapidaBasicaUltimaMatriculaPeriodoAptaEmprestimoBiblioteca(String matricula, String situacoes, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	List<MatriculaPeriodoVO> consultarDadosFichaAlunoPorMatricula(MatriculaVO matriculaVO, UsuarioVO usuarioVO);

	List<MatriculaPeriodoVO> consultarMatriculaPeriodoPrecisaReprovarPeriodoLetivo(List<HistoricoVO> historicoVOs,
			String ano, String semestre, UsuarioVO usuarioVO) throws Exception;

	List<MatriculaPeriodoVO> consultarMatriculaPeriodoPrecisaReprovarPeriodoLetivoPorRegistroAula(
			List<RegistroAulaVO> registroAulaVOs, String ano, String semestre, UsuarioVO usuarioVO) throws Exception;

	void gravarContratoMatricula(Integer matriculaPeriodo, Integer contrato) throws Exception;

	void gravarContratoFiador(Integer matriculaPeriodo, Integer contrato) throws Exception;
	
	public void alterarCondicaoPagamentoPlanoFinanceiroCurso(Integer condicaoPagamentoPlanoFinanceiroCursoVO, Integer planoFinanceiroCurso,  Integer codigoContratoMatricula, Integer matriculaPeriodo, UsuarioVO usuario) throws Exception;
	
	void removerHistoricoListaCursandoAdicionandoListaPendenteObjEspecifico(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodoVO, MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, UsuarioVO usuario) throws Exception;

	void removerHistoricoGrupoOptativaListaCursandoAdicionandoListaPendenteObjEspecifico(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodoVO, MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, UsuarioVO usuario) throws Exception;

	void removerMapaEquivalenciaDisciplinaCursadaAposDesistenciaEquivalencia(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVOs, DisciplinaVO disciplinaCursadaVO, UsuarioVO usuarioVO) throws Exception;
	
	//public MatriculaPeriodoVencimentoVO gerarVencimentoParcelas(MatriculaPeriodoVO matriculaPeriodoVO, Date dataVctoMatricula, Double valorVcto, Double valorDesconto, Double valorDescontoConvenio, Double valorDescontoInstituicao, String parcela, TipoOrigemContaReceber tipoOrigem, Boolean parcelaExtra) throws Exception;
	
	public Date montarDataCompetencia(Integer nrMensalidade, Date dataVencimento, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception;
	
	public Date montarDadosDataVencimentoMesSubsequenteMatricula(Integer nrMensalidade, String diaVencimento, Date dataMatricula, List<FeriadoVO> listaFeriadoVOs) throws Exception;
	
	public Date montarDadosDataVencimentoDataBaseGeracaoParcelas(Integer nrMensalidade, String diaVencimento, Date dataMatricula, List<FeriadoVO> listaFeriadoVOs) throws Exception;

	
	
	
	
	void realizarVerificacaoAceiteTermoRenovacaoOnline(MatriculaPeriodoVO obj, MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception;
	
	MatriculaPeriodoVO consultaUltimaMatriculaPeriodoPorMatriculaDadosNegociacao(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void verificarDisciplinaAlunoAdaptacao(MatriculaPeriodoTurmaDisciplinaVO disciplinaIncluida, MatriculaVO matricula,MatriculaPeriodoVO matriculaPeriodoVO);
	
	void verificarAlunoDependenciaDisciplinaComBaseEmHistoricosAnteriores(MatriculaPeriodoTurmaDisciplinaVO disciplinaIncluida, MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodoVO);

	void realizarDefinicaoContratoPadraoSerUsadoMatriculaPeriodo(TipoContratoMatriculaEnum tipoContratoMatriculaEnum,
			MatriculaPeriodoVO matriculaPeriodoVO, boolean forcarTrocarContrato, boolean gravarContrato, UsuarioVO usuarioVO) throws Exception;

	void gravarContratoExtensao(Integer matriculaPeriodo, Integer contrato) throws Exception;
	
	public List<MatriculaPeriodoVO> consultarMatriculaPeriodo_MatriculaConfirmada() throws Exception;
	
	public MatriculaPeriodoVO consultarMatriculaPorCpfAlunoTurma(String cpf, Integer turma, UsuarioVO usuario) throws Exception;
	
	public void alterarSituacaoMatriculaPeriodoEstornoCancelamentoSolicitarReconsideracaoSolicitacao(final Integer matriculaPeriodo, final UsuarioVO usuarioVO) throws Exception;
	
	void verificarDisciplinaCursandoMapaEquivalencia(DisciplinaVO disciplinaVO, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTumaDisciplinaVOs) throws Exception;

	MatriculaPeriodoVO consultaRapidaBasicaUltimaMatriculaPeriodoAtivaPorMatriculaDadosJobDCC(String valorConsulta, UsuarioVO usuario) throws Exception;
	
public void removerConstraintValidacaoMatriculaPeriodo() throws Exception;
	
	public void incluirConstraintValidacaoMatriculaPeriodo() throws Exception;
	
	public void alterarUnidadeEnsinoCursoPorTurma (TurmaVO turmaVO,UsuarioVO usuarioVO) throws Exception;
	
	public void alterarProcessoMatriculaAlteracaoUnidadeEnsinoTurma (TurmaVO turmaVO,boolean controlarAcesso,UsuarioVO usuarioVO) throws Exception;

	void realizarAlteracaoSituacaoMatriculaPeriodoManual(MatriculaPeriodoVO matriculaPeriodoVO, Boolean alterarSituacaoManualMatriculaPeriodo, SituacaoMatriculaPeriodoEnum situacaoMatriculaPeriodoAlterar, UsuarioVO usuarioVO) throws Exception;

	void realizarExecucaoRegrasContaIntegracaoPorMatricula(MatriculaVO matriculaVO,
			MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario, boolean geraContaGsuite);

	List<MatriculaPeriodoVO> consultaRapidaPorMatriculaUtilizandoLike(String matricula, Integer unidadeEnsino, boolean controlarAcesso, DataModelo dataModelo, UsuarioVO usuario) throws Exception;


	public Boolean validarMatriculaPodeSerAtivadaPrematriculaAposEntregaDocumentosObrigatorios(String matricula, UsuarioVO usuarioVO) throws Exception;


	public void realizarAtivacaoMatriculaValidandoRegrasEntregaDocumentoAssinaturaContratoMatricula(String matricula,  Boolean retornarException, UsuarioVO usuarioVO) throws Exception ;


	List<MatriculaPeriodoVO> consultaRapidaPorRegistroAcademicoPessoa(String nomePessoa, Integer unidadeEnsino,
			boolean controlarAcesso, String situacao, String ano, String semestre, UsuarioVO usuario,
			DataModelo dataModelo) throws Exception;
	
	boolean validarExistenciaDisciplinaMapaEquivalenciaMatriculaPeriodoTurmaDisciplinaVOs(DisciplinaVO disciplinaVO, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTumaDisciplinaVOs);


	void validarExistenciaMatriculaPeriodoAptaRealizarTransferenciaPorMatriculaSituacoesAtivaPreMatriculaAbandonoCursoTrancado(	String matricula) throws Exception;


	MatriculaPeriodoVO consultaAnoSemestreUltimaMatriculaPeriodoPorMatriculaConsultaBasica(String valorConsulta,boolean controlarAcesso, UsuarioVO usuario) throws Exception;


	MatriculaPeriodoVO realizarConsultaMatriculaAptaVoltarSituacaoPrematriculaAposIndeferirDocumentosObrigatorios(String matriculaAluno,	Boolean controlaAcesso ,  UsuarioVO usuarioVO)  throws Exception;	

	Boolean realizarVerificacaoMatriculaPrecisaVoltarSituacaoPrematriculaAposIndeferirDocumentosObrigatorios(String matriculaAluno, Boolean libaradoVoltarPrematricula, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;


	public void inicializarDadosDefinirDisciplinasDependenciaFuturasMatriculaPeriodo(MatriculaVO matriculaVO,
			MatriculaPeriodoVO matriculaPeriodoVO, List<GradeDisciplinaVO> listaDisciplinasPeriodoLetivoAlunoPendente,
			PeriodoLetivoVO periodoAnterior, Integer periodoLetivoVisualizarHistoricoAlunoIncluirDisciplina,
			boolean liberadoInclusaoTurmaOutroUnidadeEnsino, Boolean liberadoInclusaoTurmaOutroCurso,
			Boolean liberadoInclusaoTurmaOutroMatrizCurricular, Boolean permitirRealizarMatriculaDisciplinaPreRequisito, Boolean carregarDisciplinasFuturo,
			 UsuarioVO usuarioLogadoClone) throws Exception;




	Boolean validarMatriculaPodeSerAtivadaAposAssinaturaContratoMatricula(String matriculaAluno,UsuarioVO usuarioVO)	throws Exception;


	Boolean realizarValidacaoRegraAtivacaoMatriculaPorEntregaDocumentoEContratoMatricula(String matricula ,UsuarioVO usuarioVO) throws Exception;


	void realizarVerificacaoOfertaDisciplinaGrupoOptativa(MatriculaVO matriculaVO,
			MatriculaPeriodoVO matriculaPeriodoVO,
			GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO,
			boolean liberadoInclusaoTurmaOutroUnidadeEnsino, boolean liberadoInclusaoTurmaOutroCurso,
			boolean liberadoInclusaoTurmaOutroMatrizCurricular, boolean permitirRealizarMatriculaDisciplinaPreRequisito,
			 Boolean apresentarRenovacaoOnline, UsuarioVO usuario)
			throws Exception;


	void realizarVerificacaoPermissaoExclusaoDisciplina(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodo,
			MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTumaDisciplina, UsuarioVO usuario) throws Exception;


	public void realizarAdicionarObjetoDisciplinasMatricula(MatriculaRSVO matriculaRSVO,
			List<MatriculaPeriodoTurmaDisciplinaVO> disciplinasAlunoJaEstaEstudando,
			MatriculaPeriodoVO matriculaPeriodoVOInicializadaDadosPadrao) throws Exception;


	public boolean executarVerificacaoExisteMatriculaPeriodoRenovadaPorAnoSemestreEvasao(Integer matriculaPeriodo, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	MatriculaPeriodoVO consultarPorMatriculaDadosMensagemAtivacaoMatricula(String valorConsulta,
			boolean controlarAcesso, int nivelMontarDados, 
			UsuarioVO usuario) throws Exception;
	
	public void alterarSituacaoMatriculaPeriodo(Integer codigo, String situacao, UsuarioVO usuario) throws Exception;
	
	public MatriculaPeriodoVO consultarListaMatriculasCanceladas(String[] matriculas) throws Exception;
	
	List<MatriculaPeriodoVO> consultaRapidaPorMatricula(String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
}
