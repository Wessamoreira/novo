package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.GrupoContaPagarVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface GrupoContaPagarInterfaceFacade {

    public GrupoContaPagarVO novo() throws Exception;

    public void incluir(GrupoContaPagarVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(GrupoContaPagarVO obj, UsuarioVO usuario) throws Exception;

    public void excluir(GrupoContaPagarVO obj, UsuarioVO usuarioVO) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public List consultarPorIdentificadorCentroDespesaCentroDespesa(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, String tipoData, boolean controleAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorFuncionarioMatricula(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorFuncionarioNome(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorDataVencimento(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorDataEmissao(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeFornecedor(String valorConsulta, Integer unidadeEnsino, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigoFornecedor(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigoFuncionario(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public GrupoContaPagarVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigoBanco(Integer codigoSacado, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeFavorecido(String nomeFavorecido, Date dataIni, Date dataFim, Integer unidadeEnsino, String tipoData, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void validarDadosAdicionarContaPagar(ContaPagarVO obj) throws Exception;

    public List consultarPorNomeFavorecidoResumido(String nomeFavorecido, Date dataIni, Date dataFim, Integer unidadeEnsino, String tipoData, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorIdentificadorCentroDespesaCentroDespesaResumido(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, String tipoData, boolean controleAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    void realizarVinculoContaReceberComResponsavelFinanceiro(GrupoContaPagarVO contaPagarVO, UsuarioVO usuarioLogado) throws Exception;
}
