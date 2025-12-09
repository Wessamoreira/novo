package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.DownloadVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;


/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface DownloadInterfaceFacade {

    public DownloadVO novo() throws Exception;

    public void incluir(DownloadVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(DownloadVO obj, UsuarioVO usuario) throws Exception;

    public void excluir(DownloadVO obj, UsuarioVO usuario) throws Exception;

    public DownloadVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeArquivo(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomePessoa(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorDataDownload(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public List consultarPorCodigoArquivo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer pessoa, String ano, String semestre) throws Exception;

    public void excluirDownloadsArquivo(ArquivoVO obj, UsuarioVO usuario) throws Exception;
    
    public void registrarDownload(ArquivoVO arquivoVO, UsuarioVO usuarioVO, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO  ) throws Exception;
}
