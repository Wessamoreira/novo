package negocio.interfaces.contabil;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.CalculoMesVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface CalculoMesInterfaceFacade {

    public CalculoMesVO novo() throws Exception;

    public void incluir(CalculoMesVO obj) throws Exception;

    public void alterar(CalculoMesVO obj) throws Exception;

    public void excluir(CalculoMesVO obj) throws Exception;

    public CalculoMesVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados,UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

    public List consultarPorDescricaoPlanoConta(String valorConsulta, int nivelMontarDados,UsuarioVO usuario) throws Exception;

    public List consultarPorNomeFantasiaUnidadeEnsino(String valorConsulta, int nivelMontarDados,UsuarioVO usuario) throws Exception;

    public List consultarPorValorDebito(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

    public List consultarPorValorCredito(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

    public List consultarPorMes(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

    public List consultarPorAno(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

    public List obterCodigosCalculoAnterior(Integer mes, Integer ano, Integer unidadeEnsino) throws Exception;

    public List consultarPorAnoMes(Integer ano, Integer mes, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public Double consultarPorAnoMesPlanoConta(int ano, int mes, int conta, String tipoConta, boolean controlarAcesso, int unidadeEnsinoLogada,UsuarioVO usuario) throws Exception;
}
