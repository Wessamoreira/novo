package negocio.interfaces.eventos;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.eventos.PalestraEventoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface PalestraEventoInterfaceFacade {
	

    public PalestraEventoVO novo() throws Exception;
    public void incluir(PalestraEventoVO obj) throws Exception;
    public void alterar(PalestraEventoVO obj) throws Exception;
    public void excluir(PalestraEventoVO obj) throws Exception;
    public PalestraEventoVO consultarPorChavePrimaria(Integer codigo,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNomeEvento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorPalestrante(String valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorTipoPalestra(String valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNrVagasPalestra(Integer valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNrMaximoVagasExcedentes(Integer valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorLocalPalestra(String valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorValorAluno(Double valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorValorProfessor(Double valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorValorFuncionario(Double valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorValorComunidade(Double valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}