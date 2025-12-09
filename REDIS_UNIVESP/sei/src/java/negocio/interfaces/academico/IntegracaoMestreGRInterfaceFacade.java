package negocio.interfaces.academico;

import com.google.gson.JsonArray;
import controle.arquitetura.DataModelo;
import negocio.comuns.academico.*;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.protocolo.RequerimentoDisciplinasAproveitadasVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.dominios.OperacaoTempoRealMestreGREnum;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */

public interface IntegracaoMestreGRInterfaceFacade {

    public void incluirOperacao(final IntegracaoMestreGRVO obj, UsuarioVO usuario) throws Exception;

    public void incluir(final IntegracaoMestreGRVO obj, UsuarioVO usuario) throws Exception;

    public void incluirItemIntegracao(final IntegracaoMestreGRVO integracaoMestreGR, UsuarioVO usuario) throws Exception;

    public void atualizar(final IntegracaoMestreGRVO obj, UsuarioVO usuario) throws Exception;

    public void atualizarItemIntegracao(final IntegracaoMestreGRVO obj, UsuarioVO usuario) throws Exception;

    public void excluir(Integer codigo, UsuarioVO usuarioVO) throws Exception;

    public List consultarCargaLote(OperacaoTempoRealMestreGREnum tipoLote, Map<String, Object> filtros, DataModelo dataModelo, boolean controlarAcesso, UsuarioVO usuario, ProgressBarVO progressBarVO) throws Exception;

    public List consultarIntegracaoDadosEnvioJson(Map<String, Object> filtros, DataModelo dataModelo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarErroIntegracaoJson(Map<String, Object> filtros, DataModelo dataModelo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarLogIntegracaoAluno(Map<String, Object> filtros, DataModelo dataModelo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarItensIntegracaoPorCodigo(Integer codigoIntegracao, DataModelo dataModelo, UsuarioVO usuario) throws Exception;

    public List consultarRegistroIntegracaoLote(Map<String, Object> filtros, DataModelo dataModelo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarPorMatriculaAluno(Map<String, Object> filtros, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

    public List consultarPorAlunoDisciplinaIntegradas(Map<String, Object> filtros, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

    public Boolean consultarIsAlunoIntegrado(Map<String, Object> filtros, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

    
    public List consultarCargaAlunos( Map<String, Object> filtros, DataModelo dataModelo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarCargaAlunosSegundaChamada( Map<String, Object> filtros, DataModelo dataModelo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarCargaAlunosExame( Map<String, Object> filtros, DataModelo dataModelo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public String montarNomeLote(OperacaoTempoRealMestreGREnum tipoLote, Integer ano, Integer semestre, Integer bimestre);

    public JsonArray montarLoteJson(List<IntegracaoMestreGRVO> integracao, String origem);

    public File realizarGeracaoExcelLote(Map<String, Object> filtro, List<IntegracaoMestreGRVO> integracoesCargaIncial, DataModelo dataModelo, ProgressBarVO progressBarVO, UsuarioVO usuario) throws Exception;

    public File realizarGeracaoExcelLog(Map<String, Object> filtro, List<IntegracaoMestreGRVO> integracoesCargaIncial, DataModelo dataModelo, ProgressBarVO progressBarVO, UsuarioVO usuario) throws Exception;

    public File realizarGeracaoExcelLogErro(Map<String, Object> filtro, List<IntegracaoMestreGRVO> integracoesCargaIncial, DataModelo dataModelo, ProgressBarVO progressBarVO, UsuarioVO usuario) throws Exception;

    public List consultarItemErroIntegracaoJson(Map<String, Object> filtros, DataModelo dataModelo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public void incluirTrancamentoOrJubilamentotoAluno(final TrancamentoVO trancamentoVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO);

    public void incluirCancelamentoAluno(final CancelamentoVO cancelamentoVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO);

    public void incluirTransferenciaSaidaAluno(final TransferenciaSaidaVO cancelamentoVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO);

    public void atualizarAlunoIntegrado(PessoaVO pessoaVO, int hashPessoaIncial, int hashPessoalFinal, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO);

    public void atualizarAlunoPorMatricula(MatriculaVO matriculaVO, Map<String, String> hashMatriculasIncial, Map<String, String> hashMatriculasFinal, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO);

    public void verificarAproveitamentoDisciplinaAlunoDelete(final MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO);

    public void verificarAlunoDisciplinaDelete(final MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO);

    public void verificarAlunoDisciplinaInsert(final MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO);


    public void validarOperacoesParaProcessar(RegistroExecucaoJobVO registroExecucaoJobVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO);

    public List consultarPorMatriculaAlunoAtivo(Map<String, Object> filtros, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	void incluirEstornoCancelamentoAluno(CancelamentoVO cancelamentoVO, UsuarioVO usuarioVO,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO);

	void incluirEstornarTrancamentoOrJubilamentotoAluno(TrancamentoVO trancamentoVO, UsuarioVO usuarioVO,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO);
}
