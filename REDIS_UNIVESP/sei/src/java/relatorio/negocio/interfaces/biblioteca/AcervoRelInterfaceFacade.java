package relatorio.negocio.interfaces.biblioteca;

import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ClassificacaoBibliograficaVO;
import negocio.comuns.biblioteca.EditoraVO;
import negocio.comuns.biblioteca.SecaoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface AcervoRelInterfaceFacade {

    public String caminhoBaseRelatorio() throws Exception;

    public List consultarUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

    public List<SelectItem> consultarBiblioteca(Integer unidadeEnsino, Obrigatorio obrigatorio,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

    public List consultarSecao(String campoConsulta, String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

    public List consultarMatrizCurricular(Integer curso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

    public List consultarClassificacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

    public List realizarBuscaDeObjetosParaMontarRelatorioAcervo(UnidadeEnsinoVO unidadeEnsinoVO, BibliotecaVO bibliotecaVO, SecaoVO secaoVO, CursoVO cursoVO, TurnoVO turnoVO, GradeCurricularVO matrizVO, DisciplinaVO disciplinaVO, EditoraVO editoraVO, ClassificacaoBibliograficaVO classificacaoBibliograficaVO, CatalogoVO catalogoVO, String tipoRelatorio) throws Exception;

    public String designIReportRelatorio(String tipoRelatorio) throws Exception;
    
    public List consultarUnidadeEnsinoPorBiblioteca(Integer biblioteca, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

}
