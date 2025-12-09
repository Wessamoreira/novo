package negocio.interfaces.basico;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.richfaces.event.FileUploadEvent;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoAtualizacaoCadastralVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.AreaProfissionalVO;
import negocio.comuns.bancocurriculum.CurriculumPessoaVO;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.financeiro.BuscaCandidatoVagaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;
import negocio.comuns.processosel.ImportarCandidatoInscricaoProcessoSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.TipoDeficiencia;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import webservice.servicos.AlunoAutoAtendimentoRSVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em especial com a classe Façade). Com a utilização desta interface é possível
 * substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio de
 * sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface PessoaInterfaceFacade {

    public PessoaVO novo() throws Exception;

    public void incluir(PessoaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno) throws Exception;

    public void incluir(final PessoaVO obj, boolean verificarAcesso, final UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno, boolean deveValidarCPF, boolean validarCandidato, boolean validaCamposEnadeCenso, boolean validarEndereco) throws Exception;

    public void alterar(PessoaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno) throws Exception;

    public void alterar(final PessoaVO obj, boolean verificarAcesso, final UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno, boolean deveValidarCPF, boolean validarCandidato, boolean validarDadosCensoEnade, boolean validarEndereco) throws Exception;

    public void alterarSemFiliacao(final PessoaVO obj, boolean verificarAcesso, final UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno) throws Exception;
    
    public void realizarUnificacaoPessoa(List<PessoaVO> listaPessoaUnificarVOs, PessoaVO pessoaVO, UsuarioVO usuarioVO) throws Exception;

    public void excluir(PessoaVO obj, UsuarioVO usuario) throws Exception;

    public void alterarTipoPessoa(Integer pessoa, Boolean funcionario, Boolean professor, Boolean aluno, Boolean candidato, Boolean membroComunidade, UsuarioVO usuarioVO) throws Exception;

    public List<PessoaVO> consultarAprovadorLiberacaoMatricula(MatriculaPeriodoVO matriculaPeriodoVO, Boolean solicitarLiberacaoFinanceira, Boolean solicitarLiberacaoMatriculaAposInicioXModulos, UsuarioVO usuario) throws Exception;
    
    public PessoaVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public PessoaVO consultarPessoaPorNomeDataNascNomeMae(String nomePessoa, String nomeMae, Date dataNasc, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarPorCodigo(Integer valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarCss(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarPorNome(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarPorNomeCidade(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarPorNomeCidade(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarPorCPF(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarPorCPF(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public PessoaVO consultarPorCPFUnico(String valorConsulta, Integer pessoa, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarAlunosPorCodigoTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarPorRG(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarPorRG(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarPorNecessidadesEspeciais(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarPorNecessidadesEspeciais(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarProfessoresInteressadosDisciplina(Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarAlunosDoCursoPorNome(String valorConsulta, Integer curso, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarProfessorPorNome(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarAlunosDoCursoPorCPF(String valorConsulta, Integer curso, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarColegasDoAlunoPorTurma(Integer codigoAluno, String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarColegasDoAlunoPorNome(Integer codigoAluno, String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarColegasDoAlunoPorCodigoTurma(Integer codigoAluno, Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarProfessoresDoAluno(Integer codigoAluno, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public PessoaVO consultarFuncionarioPorMatricula(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public List consultarAlunoMatriculadoPorNome(String valorConsulta, Integer unidadeEnsino, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public String consultarEmailCodigo(Integer codigoPrm) throws Exception;

    public List consultarAlunoMatriculadoPorCPF(String valorConsulta, Integer unidadeEnsino, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public PessoaVO consultarEnderecoCompletoPorCodigo(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public PessoaVO consultarPorCPFProspects(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarProfessoresDaTurmaDoProfessor(Integer codigoProfessor, List turma, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarProfessoresDaTurmaDoAluno(Integer codigoAluno, Integer turma, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCoordenadoresNome(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarAlunoDoProfessorPorTurma(Integer codigoProfessor, String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarAlunoDoProfessorPorNome(Integer codigoProfessor, String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarProfessoresDaTurmaDisciplinaDoAluno(Integer codigoAluno, Integer turma, Integer disciplina, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarProfessorDisciplina(String matricula, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    //public void alterarFoto(Integer codigoPessoa, byte[] foto) throws Exception;
    public void alterarCss(final Integer codigoPessoa, final String valorCssTopoLogo, final String valorCssBackground, final String valorCssMenu) throws Exception;

    public void alterarCodProspect(final Integer codigoPessoa, final Integer codigoProspect, final Boolean coordenador) throws Exception;

    public void validarIncluir(UsuarioVO usuario) throws Exception;

    public PessoaVO consultarAlunoPorMatricula(String matricula, boolean b, int nivelmontardadosDadosminimos, UsuarioVO usuario) throws Exception;

    public List<BuscaCandidatoVagaVO> consultaRapidaBuscaCandidatoVagaPorVaga(VagasVO obj) throws Exception;

    public List consultarPorNomeTipoPessoa(String valorConsulta, Boolean aluno, Boolean professor, Boolean funcionario, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorMatriculaTipoPessoa(String valorConsulta, Boolean aluno, Boolean professor, Boolean funcionario, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeCurso(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeUnidadeEnsinoTipoPessoa(String valorConsulta, Boolean aluno, Boolean professor, Boolean funcionario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorIdentificadorTurma(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultaRapidaAlunoMatriculadoPorNome(String valorConsulta, Integer unidadeEnsino, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultaRapidaAlunoMatriculadoPorCPF(String valorConsulta, Integer unidadeEnsino, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultaRapidaPorNome(String valorConsulta, String valor, boolean b, int nivelmontardadosDadosminimos, UsuarioVO usuario) throws Exception;

    public List consultaRapidaResumidaPorMatricula(String valorConsulta, String tipoPessoa, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
   
    public List consultaRapidaResumidaPorNome(String valorConsulta, String valor, Integer limite, Integer offset, boolean b, int nivelmontardadosDadosminimos, UsuarioVO usuario) throws Exception;

    public Integer consultaTotalDeRegistroRapidaResumidaPorMatricula(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public Integer consultaTotalDeRegistroRapidaResumidaPorNome(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultaRapidaPorCPF(String valorConsulta, String valor, boolean b, int nivelmontardadosDadosminimos, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultaRapidaPorUnidadeEnsinoPorCPF(String valorConsulta, Integer unidadeEnsino, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultaRapidaResumidaPorCPF(String valorConsulta, String valor, Integer limite, Integer offset, boolean b, int nivelmontardadosDadosminimos, UsuarioVO usuario) throws Exception;

    public Integer consultaTotalDeRegistroRapidaResumidaPorCPF(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultaRapidaPorRG(String valorConsulta, String valor, boolean b, int nivelmontardadosDadosminimos, UsuarioVO usuario) throws Exception;

    public List consultaRapidaResumidaPorRG(String valorConsulta, String valor, Integer limite, Integer offset, boolean b, int nivelmontardadosDadosminimos, UsuarioVO usuario) throws Exception;

    public Integer consultaTotalDeRegistroRapidaResumidaPorRG(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void carregarDados(PessoaVO obj, UsuarioVO usuario) throws Exception;

    public void carregarDados(PessoaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public void carregarDadosPessoaRemessaContaPagar(PessoaVO obj, int nivelmontardadosDadosminimos, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultaRapidaPorCodigo(Integer valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultaRapidaAlunoPorCodigoTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, boolean somenteAtivo) throws Exception;

    public List consultaRapidaAlunoPorNomeVisaoAluno(String valorConsulta, Integer codAluno, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultaRapidaAlunoPorTurmaVisaoAluno(String valorConsulta, Integer codAluno, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarProfessoresDoAlunoVisaoAluno(Integer codigoAluno, String valorConsulta, Integer unidadeEnsino, String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public void consultaRapidaAlunoMatriculado(Integer unidadeEnsino, String tipoPessoa, String situacao, List<PessoaVO> listaAluno, ComunicacaoInternaVO comunicacaoInterna, ComunicadoInternoDestinatarioVO comunicadoInternoDestinatario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void consultaRapidaTodosProfessoresNivelEducacional(Integer unidadeEnsino, String nivelEducacional, String tipoPessoa, List<PessoaVO> listaProfessor, ComunicacaoInternaVO comunicacaoInterna, ComunicadoInternoDestinatarioVO comunicadoInternoDestinatario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void consultaRapidaTodaComunidade(Integer unidadeEnsino, List<PessoaVO> listaPessoa, ComunicacaoInternaVO comunicacaoInterna, ComunicadoInternoDestinatarioVO comunicadoInternoDestinatario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public PessoaVO consultaRapidaPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarAlunosDoProfessorPorNome(Integer codigoProfessor, String nomeAluno, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public List consultarAlunosDoProfessorTutorPorNome(Integer codigoProfessor, String nomeAluno, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarAlunosDoProfessorPorTurma(Integer codigoProfessor, String nomeTurma, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void incluir(final PessoaVO obj, boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno) throws Exception;

    public void alterar(final PessoaVO obj, boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno) throws Exception;

    public PessoaVO consultarPessoaDoEmprestimoPeloExemplar(PessoaVO pessoaVO, Integer codigoExemplar, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarPorTurma(int codigoTurma, Integer disciplina, String ano, String semestre, int nivelMontarDados, boolean trazerTurmaBaseTurmaAgrupada, UsuarioVO usuario) throws Exception;

    public List consultarAlunosDoProfessor(Integer codigoProfessor, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultaRapidaAlunoVisaoAluno(Integer codAluno, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void validarDadosPessoaisVisaoAluno(PessoaVO obj, ConfiguracaoAtualizacaoCadastralVO config) throws Exception;

    public List<PessoaVO> consultarAlunosTurmaSituacao(Integer curso, Integer turma, String ano, String semestre, String situacao, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    public Boolean verificarSeUsuarioIsAluno(Integer codigoPessoa);

    public PessoaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, Boolean funcionario, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultaRapidaFuncionariosPorCodigoDepartamento(Integer departamento, Integer unidadeEnsino, Boolean pessoasAtivas, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultaRapidaFuncionariosPorCodigoCargo(Integer cargo, Integer unidadeEnsino, Boolean pessoasAtivas, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultaRapidaPorCodigoAreaConhecimento(Integer areaConhecimento, Integer unidadeEnsino, Boolean pessoasAtivas, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public void alterarCoordenador(final Integer codigoPessoa, final Boolean coordenador) throws Exception;
    
    public void alterarRegistroAcademico(final Integer codigoPessoa, final String registroAcademico) throws Exception;

    public List consultarAlunosDaTurmaPorNome(String nomeAluno, Integer turma, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarAlunosDaTurmaPorCpf(String cpf, Integer turma, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarAlunosDaTurmaPorCurso(String nomeCurso, Integer turma, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarAlunosDaTurma(Integer turma, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarProfessoresDaTurmaPorTurma(Integer turma, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultaRapidaAlunosPorCodigoTurmaSituacao(Integer codigoTurma, String situacaoMatricula, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultaRapidaAlunosPorCodigoTurmaPadraoSituacao(Integer codigoTurma, String situacaoMatricula, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultaRapidaAlunosPorCodigoTurmaInclusaoSituacao(Integer codigoTurma, String situacaoMatricula, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultaRapidaAlunosPorCodigoTurmaInclusaoSituacaoDisciplinas(Integer codigoTurma, String situacaoMatricula, String ano, String semestre, List<Integer> listaCodigoDisciplinas, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public PessoaVO consultaRapidaPorContaReceberTipoAlunoSemMatricula(Integer contaReceber, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultaRapidaAlunoPorCodigoTurmaSituacaoMatriculaPeriodo(Integer valorConsulta, String situacaoMatriculaPeriodo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultarPorTurmaTurmaAgrupada(int codigoTurma, String ano, String semestre, int nivelMontarDados, UsuarioVO usuario, Boolean trazerTurmaBaseTurmaAgrupada) throws Exception;

    public List consultarProfessoresDaTurmaPorTurmaAgrupada(Integer turma, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<BuscaCandidatoVagaVO> consultaRapidaBuscaCandidatoVaga(BuscaCandidatoVagaVO obj) throws Exception;

    public List<PessoaVO> consultaRapidaResumidaPorNome(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultaRapidaResumidaPorCPF(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultaRapidaResumidaPorRG(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public PessoaVO consultarPorCertidaoNascimentoUnico(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public PessoaVO consultaRapidaCompletaPorChavePrimaria(Integer codigoPrm, Boolean funcionario, Boolean trazerDisciplinasInteresse, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public void alterarCandidato(final Integer codigoPessoa, final Boolean candidato) throws Exception;

    public void executarAtualizacaoDadosPessoaRenovacaoAutomatica(final PessoaVO obj,  final UsuarioVO usuario) throws Exception;
    
    public void executarAtualizacaoDadosPessoaPossuiAcessoVisaoPais(PessoaVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

    public PessoaVO consultarPessoaPorCodigoFuncionario(Integer codigoFuncionario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    void validarDadosFiliacaoResponsavelLegal(PessoaVO obj, FiliacaoVO filiacao) throws ConsistirException;

    PessoaVO consultarResponsavelFinanceiroAluno(Integer codigoAluno, UsuarioVO usuarioLogado) throws Exception;

    List<PessoaVO> consultaRapidaPorNomeResponsavelFinanceiro(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    List<PessoaVO> consultaRapidaPorCpfResponsavelFinanceiro(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    List<PessoaVO> consultaRapidaPorNomeAlunoResponsavelFinanceiro(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    List<PessoaVO> consultarAlunoResponsavelFinanceiro(Integer codigoResponsavelFinanceiro, UsuarioVO usuarioLogado) throws Exception;

//	void incluirPais(PessoaVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;
//
//	void alterarPais(PessoaVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;
    public List consultarAlunosDaTurmaTodos(Integer turma, Integer disciplina, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarAlunosDaTurmaNormal(Integer turma, Integer disciplina, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarAlunosDaTurmaReposicaoInclusao(Integer turma, Integer disciplina, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarColegasDoAlunoPorCodigoTurmaTodos(Integer codigoAluno, Integer turma, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarColegasDoAlunoPorCodigoTurmaNormal(Integer codigoAluno, Integer turma, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarColegasDoAlunoPorCodigoTurmaReposicaoInclusao(Integer codigoAluno, Integer turma, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public void incluirPessoaProspectMatriculaCRM(PessoaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno) throws Exception;

    PessoaVO consultaRapidaCompletaPorCPFUnico(String cpf, Boolean funcionario, Boolean trazerDisciplinasInteresse, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public String consultarNomePorCodigo(Integer codigoPrm) throws Exception;

    public void alterarPessoaRequisitante(final Integer codigoPessoa) throws Exception;

    public Boolean verificarPessoaRequisitante(Integer pessoaCodigo) throws Exception;

    void alterarIsencaoTaxaBoleto(Integer codigoPessoa, Boolean isentarTaxaBoleto) throws Exception;

    Boolean consultaSePessoaAluno(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    void alterarOcultarDadosCRM(Integer pessoa, boolean ocultar);

    public List consultarAlunosDaTurmaTodos(List listaTurma, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    PessoaVO consultarPorEmaiInstitucionallUnico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    PessoaVO consultarPorEmailUnico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    void alterarPessoaConformeProspect(ProspectsVO obj, boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno, Boolean origemPreInscricao) throws Exception;

    public List<PessoaVO> consultarPessoaInteresseAreaProfissinal(List<AreaProfissionalVO> areaProfissional, List<CidadeVO> cidade, List<EstadoVO> estado, CursoVO curso);

    void uploadArquivoCurriculum(FileUploadEvent upload, PessoaVO pessoa, CurriculumPessoaVO curriculumPessoaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

    void deletarArquivoCurriculum(PessoaVO pessoaVO, CurriculumPessoaVO curriculumPessoaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

    void adicionarArquivoCurriculum(PessoaVO pessoa, CurriculumPessoaVO curriculumPessoaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

    List<PessoaVO> consultaRapidaAlunosPorCodigoTurmaSituacaoEDisciplina(TurmaVO turmaVO, String situacaoMatricula, Integer codigoDisciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    List<PessoaVO> consultaAlunosAtivosParaReposicaoDisciplinaEnvioEmail(Integer codigoTurma, String ano, String semestre, List<Integer> listaCodigoDisciplinas, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<PessoaVO> consultaRapidaPessoaGrupoDestinatario(Integer codGrupo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public SqlRowSet consultarAniversariantesDia(boolean professor, boolean funcionario, boolean aluno, boolean exaluno);

	List<PessoaVO> consultaRapidaPorNomePessoaAutoComplete(String valorConsulta, String tipoPessoa, int limit, Integer unidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<PessoaVO> consultaRapidaPorUnidadeEnsinoPorRG(String valorConsulta, Integer unidadeEnsino, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<PessoaVO> consultaRapidaPorUnidadeEnsinoPorNome(String valorConsulta, Integer unidadeEnsino, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void consultaRapidaAlunoMatriculadoSituacaoMatricula(Integer unidadeEnsino, String situacao, List<PessoaVO> listaAluno, ComunicacaoInternaVO comunicacaoInterna, ComunicadoInternoDestinatarioVO comunicadoInternoDestinatario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List consultarPorNomeEProcessoSeletivo(String valorConsulta, int procSeletivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List consultarPorRGEProcessoSeletivo(String valorConsulta, int procSeletivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List consultarPorCPFEProcessoSeletivo(String valorConsulta, int procSeletivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void consultaRapidaTodosCoordenadores(Integer unidadeEnsino, String tipoPessoa, List<PessoaVO> listaCoordenador, ComunicacaoInternaVO comunicacaoInterna, ComunicadoInternoDestinatarioVO comunicadoInternoDestinatario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void consultaRapidaTodosFuncionarios(Integer unidadeEnsino, String tipoPessoa, List<PessoaVO> listaFuncionario, ComunicacaoInternaVO comunicacaoInterna, ComunicadoInternoDestinatarioVO comunicadoInternoDestinatario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	PessoaVO consultarDadosGerarNotaFiscalSaida(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public PessoaVO consultarPorChavePrimariaTipoPessoa(Integer codigo, UsuarioVO usuarioVO);
	
	public List<PessoaVO> consultaRapidaPorNomeResponsavelFinanceiroListaUnidadeEnsinoVOs(String valorConsulta, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<PessoaVO> consultaRapidaPorNomeAlunoResponsavelFinanceiroListaUnidadeEnsinoVOs(String valorConsulta, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<PessoaVO> consultaRapidaPorCpfResponsavelFinanceiroListaUnidadeEnsinoVOs(String valorConsulta, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<PessoaVO> consultaRapidaResumidaPorNomeMae(String valorConsulta, String tipoPessoa, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	Integer consultaTotalDeRegistroRapidaResumidaPorNomeMae(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<PessoaVO> consultarCoordenadorCursoAluno(String matricula, UsuarioVO usuarioVO) throws Exception;
	
	public List<PessoaVO> consultarCoordenadorCursoUnidadeEnsino(Integer unidadeEnsino, Integer pessoa, Boolean ativo,  UsuarioVO usuarioVO) throws Exception;

	public List<PessoaVO> consultarResponsavelLegalAluno(Integer codigoAluno, UsuarioVO usuarioVO) throws Exception;
	
	public List<PessoaVO> consultarFiliacaoPorPessoa(Integer pessoa, UsuarioVO usuarioVO) throws Exception;

	Integer consultarCodigoProfessorPorHorarioTurmaDetalhadoTurmaDisciplinaAnoSemestre(Integer codigoTurma, Integer codigoDisciplina, String ano, String semestre, boolean consultarDisciplinaEquivalente) throws Exception;

	PessoaVO consultarPorMatricula(String matricula) throws Exception;

	/**
	 * @author Wellington Rodrigues - 14/04/2015
	 * @param valorConsulta
	 * @param tipoPessoa
	 * @param limite
	 * @param offset
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<PessoaVO> consultaRapidaResumidaPorResponsavelLegal(String valorConsulta, String tipoPessoa, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington Rodrigues - 14/04/2015
	 * @param valorConsulta
	 * @param tipoPessoa
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	Integer consultaTotalRegistroRapidaResumidaPorResponsavelLegal(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	Integer consultaTotalRegistroRapidaResumidaPorNomePai(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington Rodrigues - 14/04/2015
	 * @param valorConsulta
	 * @param tipoPessoa
	 * @param limite
	 * @param offset
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<PessoaVO> consultaRapidaResumidaPorNomePai(String valorConsulta, String tipoPessoa, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<PessoaVO> consultar(String campoConsulta, String valorConsulta, TipoPessoa tipoPessoa, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	
	/**
	 * @author Rodrigo Wind - 09/06/2015
	 * @param obj
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	ProspectsVO realizarVinculoPessoaProspect(PessoaVO obj, UsuarioVO usuario) throws Exception;

	public void alterarFoto(final PessoaVO obj, final UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
	
	public void alterarPessoaProspectMatriculaCRM(final PessoaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno) throws Exception;	
	
	public Boolean realizarVerificacaoPessoaTipoAluno(Integer codigoPessoa) throws Exception;

	PessoaVO consultarPessoaContaReceber(Integer codigoContaReceber, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario) throws Exception;

	void consultaRapidaAlunoPorCursoTurmaAnoSemestreSitacaoAcademica(Integer unidadeEnsino, List<PessoaVO> listaAluno,
			ComunicacaoInternaVO comunicacaoInterna, ComunicadoInternoDestinatarioVO comunicadoInternoDestinatario,
			boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario,
			FiltroRelatorioAcademicoVO filtroRelatorioAcademico, String nivelEducacional, String identificadorSalaAulaBlackboard, Double percentualIntegralizacaoInicial, Double percentualIntegralizacaoFinal, ProgramacaoFormaturaVO programacaoFormaturaVO, BimestreEnum bimestreEnum, TipoDeficiencia tipoDeficienciaEnum, DiaSemana diaSemana, DisciplinaVO disciplinaVO) throws Exception;

	AlunoAutoAtendimentoRSVO consultarAlunoAutoAtendimentoPorCPF(String cpf) throws Exception;
	
	List<PessoaVO> consultaRapidaAlunoPorDisciplinaTurmaAnoSemestreUnidadeEnsino(Integer disciplina, Integer turma, String ano, String semestre,  UsuarioVO usuarioLogado) throws Exception;
	
	String inicializarDadosFotoUsuario(PessoaVO pessoaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
	
	List<PessoaVO> consultarFilhosPorResponsavelFinanceiro(Integer responsavelFinanceiro, UsuarioVO usuarioVO);
	
	public void resetarDataAtualizacaoCadastralGeral(UsuarioVO usuarioVO) throws Exception;
	
	/**
	 * Realiza a consulta das pessoas e/ou usuarios que tiveram alteracao em seus registros nas ultimas horas @param
	 * @param horas
	 * @return
	 */
	public SqlRowSet consultarPessoasQueSofreramAlteracao(Integer horas);
	
	/**
	 * Altera as informacoes da Pessoa pela teste de Follow Up - Prospect
	 */
	public void alterarPessoaPelaTelaDeFollowUpProspect(ProspectsVO obj, UsuarioVO usuarioVO);
	
	PessoaVO consultarPorMatriculaUnicaTipoPessoa(String matricula, Boolean aluno, Boolean professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	Boolean consultarPessoaPossuiMatriculaPorCodigo(Integer codigo, UsuarioVO usuarioVO);

	void validarDadosPessoaCasoPossuaMatricula(PessoaVO pessoaVO, Boolean origemPreInscricao, UsuarioVO usuario) throws Exception;

	void inicializarDadosPessoaBaseadoProspect(ProspectsVO obj, UsuarioVO usuarioVO);

	String consultarSenhaCertificadoParaDocumento(Integer codigoPrm) throws Exception;

	void executarAtualizacaoSenhaCertificadoParaDocumento(PessoaVO obj, UsuarioVO usuario) throws Exception;

	List<PessoaVO> consultarCoordenadorCursoTurmaUnidadeEnsino(Integer curso, Integer turma, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;
	
	public PessoaVO consultarPorTipoPessoa(String tipoPessoa, PessoaVO pessoa) throws Exception;
	
	public void alterarPessoasSetandoDataAlteracaoInicial();
	
	public List<PessoaVO> consultaCoordenadorPorNome(String valorConsulta, List<UnidadeEnsinoVO> listaUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	boolean consultarSePessoaAtiva(Integer pessoa) throws Exception;

	List<PessoaVO> consultarPessoaUnificacao(PessoaVO pessoaManterVO, String campoUnificar, String valorUnificar, Boolean unificarMesmoNome, UsuarioVO usuarioVO) throws Exception;

	void consultarInformacoesBasicasPessoaUnificar(PessoaVO pessoaVO, UsuarioVO usuarioVO);
	
	public void alterarDadosBasicosFiliacao(final PessoaVO obj, boolean verificarAcesso, final UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, boolean deveValidarCPF) throws Exception;

	void consultaPessoaDataModeloPorNome(DataModelo dataModelo, TipoPessoa tipoPessoa, UsuarioVO usuario)
			throws Exception;
	
	public void incluirMembroComunidade(final PessoaVO obj, boolean verificarAcesso, final UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno) throws Exception;
	
	void removerPessoaGsuite(PessoaVO obj, PessoaGsuiteVO pessoaGsuite, UsuarioVO usuario);

	void adicionarPessoaGsuite(PessoaVO obj, PessoaGsuiteVO pessoaGsuite, UsuarioVO usuario);
	
	void adicionarPessoaEmailInstitucionalVO(PessoaVO obj, PessoaEmailInstitucionalVO pessoaEmailInstitucional, UsuarioVO usuario);
	
	void removerPessoaEmailInstitucionalVO(PessoaVO obj, PessoaEmailInstitucionalVO pessoaEmailInstitucional, UsuarioVO usuario);
	
	public PessoaVO consultaRapidaPorCodigoPessoaCenso(Integer codPessoa, UsuarioVO usuario) throws Exception;
	
	public void carregarDadosPessoaProspect(PessoaVO obj , UsuarioVO usuario) throws Exception;
	
	void carregarUrlFotoAluno(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, PessoaVO pessoaVO);
	
	public void consultaAlunosNaoRenovaramMatriculaUltimoSemestre(PeriodicidadeEnum periodicidadeEnum, String ano, String semestre, Integer unidadeEnsino, Integer curso, Integer turma, Boolean desconsiderarAlunoPreMatriculado, List listaAluno, ComunicacaoInternaVO comunicacaoInterna, ComunicadoInternoDestinatarioVO comunicadoInternoDestinatario, String nivelEducacional , String identificadorSalaAulaBlackboard, Double percentualIntegralizacaoInicial, Double percentualIntegralizacaoFinal, ProgramacaoFormaturaVO programacaoFormaturaVO, BimestreEnum bimestreEnum, TipoDeficiencia tipoDeficienciaEnum, DiaSemana diaSemana, DisciplinaVO disciplinaVO) throws SQLException, Exception;

	List<PessoaVO> consultarCoordenadoresCursoTurmaNotificacaoCronogramaAula(Integer turma,
			UsuarioVO usuario) throws Exception;

	PessoaVO inicializarDadosCandidatoImportacaoCandidatoInscricaoProcessoSeletivo(ImportarCandidatoInscricaoProcessoSeletivoVO importarCandidatoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	void persistirPessoaComEmailInstitucional(PessoaVO obj, boolean verificarAcesso, UsuarioVO usuario,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno, boolean deveValidarCPF,
			boolean validarCandidato, boolean validarDadosCensoEnade, boolean validarEndereco) throws Exception;

	public List consultaRapidaPessoaMatriculadoPorMatricula(String valorConsulta, Integer codigo, String string,
			boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuarioLogado) throws Exception;

	public List consultaRapidaPessoaPorRegistroAcademico(String valorConsulta, Integer codigo, String tipoPessoa, boolean filtrarMatricula, 
			boolean controleAcesso, int nivelmontardadosDadosbasicos, UsuarioVO usuarioLogado) throws Exception;

	public List<PessoaVO> consultaRapidaResumidaPorRegistroAcademico(String valorConsulta, String tipoPessoa, Integer limite,
			Integer offset, boolean controlarAcesso, int nivelMontarDados, DataModelo dataModelo, UsuarioVO usuario)
			throws Exception;
	
	public List<PessoaVO> consultaRapidaResumidaPorEmailInstitucional(String valorConsulta, String tipoPessoa, Integer limite,
			Integer offset, boolean controlarAcesso, int nivelMontarDados, DataModelo dataModelo, UsuarioVO usuario)
			throws Exception;
	
	
	public void alterarDadosBancariosAluno(String banco, String agencia, String contaCorrente, Integer pessoa)	throws Exception;

	void alterarSemValidarDados(PessoaVO obj, boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno) throws Exception;
	
	public List<PessoaVO> consultarPorNome(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, boolean funcionarioObrigatorio, UsuarioVO usuario) throws Exception;
	
	public List<PessoaVO> consultaRapidaPorCPF(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, boolean funcionarioObrigatorio, UsuarioVO usuario) throws Exception;

	PessoaVO consultarRegistroAcademicoPorPessoa(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	void alterarApenasDadosPreenchidos(ProspectsVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	Boolean verificarSeExistePessoaUtilizandoRegistroAcademico(String registroAcademico, Boolean desconsiderarPessoa,
			Integer codigoPessoa);

	public List<PessoaVO> consultaRapidaPorRegistroAcademicoAutoComplete(String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, int limit, boolean contrarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public PessoaVO consultarPorRegistroAcademico(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List consultaRapidaPessoaPorRegistroAcademico(String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String tipoPessoa, boolean filtrarMatricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<PessoaVO> consultaRapidaPorUnidadeEnsinoPorNome(String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<PessoaVO> consultaRapidaPorUnidadeEnsinoPorCurso(String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	PessoaVO consultarPorEmail(String email, String tipoPessoa, boolean forcarUsoEmailInstitucional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	public void adicionarPessoaListaDestinatario(List<PessoaVO> listaPessoa, ComunicacaoInternaVO comunicacaoInterna, ComunicadoInternoDestinatarioVO comunicadoInternoDestinatario) throws Exception;
	public PessoaVO consultarPorMatricula(String matricula, Integer nivelMontarDados) throws Exception;
	
	public void consultarDocentesEstagioOtimizado(DataModelo controleConsultaOtimizado);

	public List<PessoaVO> consultarDestinatariosMensagemMoodlePorEmail(List<String> emails, String tipoPessoa) throws Exception;

	String consultarRegistroAcademicoPorMatricula(String matricula);
}
