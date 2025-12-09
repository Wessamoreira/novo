package negocio.interfaces.basico;


import java.io.File;
import java.util.List;

import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.DocumetacaoPessoaVO;
import negocio.comuns.basico.PessoaVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface DocumetacaoPessoaInterfaceFacade {

    public DocumetacaoPessoaVO novo() throws Exception;

    public void incluir(DocumetacaoPessoaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public void alterar(DocumetacaoPessoaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public void excluir(DocumetacaoPessoaVO obj, UsuarioVO usuario) throws Exception;

    public List consultarPorSituacao(String valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarPorTipoDeDocumento(Integer valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public void excluirDocumetacaoPessoas(Integer pessoa, UsuarioVO usuario) throws Exception;

    public void alterarDocumetacaoPessoas(PessoaVO pessoaVO, Integer pessoa, List objetos, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public void incluirDocumetacaoPessoas(PessoaVO pessoaVO, Integer pessoaPrm, List objetos, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public void setIdEntidade(String idEntidade);

    public List consultarPorNomeDoAluno(String valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<DocumetacaoPessoaVO> consultarPorSituacaoPessoa(String string, String pessoa, int nivelMontarDados, boolean b, UsuarioVO usuario) throws Exception;

    public List consultarDocumetacaoPessoaPorPessoa(Integer pessoa, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public void montarDocumentacaoPessoaPorTipoDeDocumentos(Integer pessoa, List<TipoDocumentoVO> listaTipoDocumentoVO, List<DocumetacaoPessoaVO> listaDocumetacaoPessoaVO) throws Exception;

    public List consultarDocumetacaoPessoaPorPessoaEntregue(Integer pessoa, int nivelMontarDados, boolean entregue, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarDocumetacaoPessoaPorPessoaProfessorOuEntregue(Integer pessoa, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<DocumetacaoPessoaVO> consultaRapidaPorPessoa(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public void alterarCodigoArquivo(DocumetacaoPessoaVO obj, Integer codArquivo, UsuarioVO usuario) throws Exception;
    
    public DocumetacaoPessoaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public void excluirDocumentacaoPessoa(DocumetacaoPessoaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;
    
    public void removerTipoDocumento(List<DocumetacaoPessoaVO> listaDocumentacaoPessoaProfessor, List<TipoDocumentoVO> listaTipoDocumento,TipoDocumentoVO tipoDocumentoRemover, UsuarioVO usuario) throws Exception;
    
    public File unificarFrenteVersoDocumentoMatricula(DocumetacaoPessoaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO , UsuarioVO usuarioVO) throws Exception;
}
