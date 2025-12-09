package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ItemTitulacaoCursoVO;
import negocio.comuns.academico.TitulacaoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface TitulacaoCursoInterfaceFacade {

    public void persistir(TitulacaoCursoVO obj, UsuarioVO usuario) throws Exception;

    public void excluir(TitulacaoCursoVO obj, UsuarioVO usuario) throws Exception;

    public List consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public void validarDados(TitulacaoCursoVO obj) throws Exception;

    public TitulacaoCursoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public void adicionarObjItemTitulacaoCursoVOs(TitulacaoCursoVO objTitulacaoCursoVO, ItemTitulacaoCursoVO obj) throws Exception;

    public void excluirObjItemTitulacaoCursoVOs(TitulacaoCursoVO objTitulacaoCursoVO, String titulacao) throws Exception;

    public ItemTitulacaoCursoVO consultarObjItemTitulacaoCursoVO(TitulacaoCursoVO objTitulacaoCursoVO, String titulacao) throws Exception;

    public List<TitulacaoCursoVO> consultarPorCodigoCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public TitulacaoCursoVO consultarPorCodigoCursoUnico(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}
