package negocio.interfaces.biblioteca;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.AssinaturaPeriodicoVO;
import negocio.comuns.biblioteca.ExemplarVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface AssinaturaPeriodicoInterfaceFacade {

    public AssinaturaPeriodicoVO novo() throws Exception;

    public void incluir(AssinaturaPeriodicoVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(AssinaturaPeriodicoVO obj, UsuarioVO usuario) throws Exception;

    public void excluir(AssinaturaPeriodicoVO obj) throws Exception;

    public AssinaturaPeriodicoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorDataCadastro(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorDataInicioAssinatura(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorDataFinalAssinatura(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomePessoa(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    //public List consultarPorExemplar(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public void adicionarExemplaresAssinaturaPeriodico(AssinaturaPeriodicoVO assinaturaPeriodico, ExemplarVO obj) throws Exception;

    public List consultarPorNome(String nome, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorExemplar(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCatalogo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}
