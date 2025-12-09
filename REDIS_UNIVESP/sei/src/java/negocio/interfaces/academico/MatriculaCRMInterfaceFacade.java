package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.MatriculaCRMVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import webservice.servicos.IntegracaoMatriculaCRMVO;
import webservice.servicos.IntegracaoPessoaVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface MatriculaCRMInterfaceFacade {

    public Boolean verificaSeDeveProcessarJob() throws Exception;

    public MatriculaCRMVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

    public void novaMatricula(MatriculaCRMVO matriculaCRM,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

    public List<MatriculaCRMVO> consultarMatriculaCRMSituacao(Boolean matriculaCRMFinalizada, UsuarioVO usuarioVO) throws Exception;

    public void alterar(final MatriculaCRMVO obj) throws Exception;
    
    public void incluir(final MatriculaCRMVO obj, UsuarioVO usuarioVO) throws Exception;

    public Boolean verificaSeTemRegistro(Boolean matriculaCRMFinalizada, UsuarioVO usuarioVO) throws Exception;
    
    public MatriculaCRMVO consultarAlunoASerMatriculadoPorCodigoPessoa(Integer codigoAluno, Boolean matriculaCRMFinalizada, UsuarioVO usuarioVO) throws Exception;

    MatriculaCRMVO preencherNovaMatriculaAlunoWS(IntegracaoMatriculaCRMVO integracaoMatriculaCRMVO,boolean validarAlunoMatriculadoWebServiceMatriculaCrm) throws Exception;
    
    public UsuarioVO validarDadosUsuarioResponsavel(Integer unidadeEnsino, Integer codigoUsuario) throws Exception;

	void preencherNovaIntegracaoPessoaWS(IntegracaoPessoaVO integracaoPessoaCRMVO) throws Exception;
}