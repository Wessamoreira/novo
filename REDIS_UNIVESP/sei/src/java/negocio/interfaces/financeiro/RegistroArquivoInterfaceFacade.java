package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.RegistroArquivoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface RegistroArquivoInterfaceFacade {

    public RegistroArquivoVO novo() throws Exception;

    public void incluir(RegistroArquivoVO obj, boolean controleAcesso, UsuarioVO usuario) throws Exception;
    
    public void alterarSemBaixarContas(final RegistroArquivoVO obj, boolean controleAcesso, UsuarioVO usuario) throws Exception;

    public void alterar(RegistroArquivoVO obj, boolean controleAcesso, UsuarioVO usuario) throws Exception;

    public void excluir(RegistroArquivoVO obj, boolean controleAcesso, UsuarioVO usuario) throws Exception;

    public RegistroArquivoVO consultarPorChavePrimaria(Integer codigo, String identificacaoTituloEmpresa, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, String identificacaoTituloEmpresa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorRegistroHeader(Integer valorConsulta, String identificacaoTituloEmpresa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public List consultarPorIdentificacaoTituloEmpresa(String valorAtualFiltroNossoNumero, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception;

    public List consultarPorDataGeracaoArquivo(Date dataIni, Date dataFim, String string, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception;

    public RegistroArquivoVO consultarPorControleCobranca(Integer controleCobranca, String identificacaoTituloEmpresa, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public RegistroArquivoVO consultarPorChavePrimariaCompleto(Integer codigoPrm, String identificacaoTituloEmpresa, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}
