package negocio.interfaces.academico;

import java.util.HashMap;
import java.util.List;

import jakarta.faces.model.SelectItem;

import negocio.comuns.academico.ItemTitulacaoCursoVO;
import negocio.comuns.academico.TitulacaoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface ItemTitulacaoCursoInterfaceFacade {

    public void validarDados(ItemTitulacaoCursoVO obj) throws Exception;

    public ItemTitulacaoCursoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigoTitulacaoCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorTitulacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorQuantidade(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public void incluirItemTitulacaoCursos(Integer titulacaoCursoPrm, List objetos) throws Exception;

    public void alterarItemTitulacaoCursos(Integer titulacaoCurso, List objetos) throws Exception;

    public void excluirItemTitulacaoCursos(Integer titulacaoCurso) throws Exception;

    public List<SelectItem> consultarListaSelectItem(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void adicionarQtdeNivelEscolaridade(HashMap<Integer, Integer> hashMapQtdeNivelEscolaridade, PessoaVO professor) throws Exception;

    public void calcularQtdeProfessorNivelEscolaridade(HashMap<Integer, Integer> hashMapQtdeNivelEscolaridade, Integer qtdeProfessores, TitulacaoCursoVO titulacaoCursoVO) throws Exception;
}
