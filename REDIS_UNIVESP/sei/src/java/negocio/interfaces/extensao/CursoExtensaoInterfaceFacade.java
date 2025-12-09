package negocio.interfaces.extensao;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.extensao.CursoExtensaoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface CursoExtensaoInterfaceFacade {
	

    public CursoExtensaoVO novo() throws Exception;
    public void incluir(CursoExtensaoVO obj) throws Exception;
    public void alterar(CursoExtensaoVO obj) throws Exception;
    public void excluir(CursoExtensaoVO obj) throws Exception;
    public CursoExtensaoVO consultarPorChavePrimaria(Integer codigo,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorCargaHoraria(Integer valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorDataInicial(Date prmIni, Date prmFim, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorDataFinal(Date prmIni, Date prmFim, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorSituacaoFinanceira(String valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNomeClassificaoCursoExtensao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorDataInicialInscricao(Date prmIni, Date prmFim, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorDataFinalInscricao(Date prmIni, Date prmFim, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorValorComunidade(Double valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorValorAluno(Double valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorValorFuncionario(Double valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorValorProfessor(Double valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorValorInscricaoComunidade(Double valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorValorInscricaoAluno(Double valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorValorInscricaoFuncionario(Double valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorValorInscricaoProfessor(Double valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}