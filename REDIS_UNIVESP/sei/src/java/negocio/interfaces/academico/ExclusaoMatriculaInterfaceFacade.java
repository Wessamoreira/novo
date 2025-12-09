package negocio.interfaces.academico;


import java.util.Date;
import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface ExclusaoMatriculaInterfaceFacade {

    public List consultarPorNomeAluno(String nomeAluno, UsuarioVO usuario) throws Exception;

    public List consultarPorMatricula(String nomeAluno, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeCurso(String nomeCurso, UsuarioVO usuario) throws Exception;

    public List consultarPorDataExclusao(Date prmIni, Date prmFim, UsuarioVO usuario) throws Exception;

    public List consultarPorDataMatricula(Date prmIni, Date prmFim, UsuarioVO usuario) throws Exception;

    public void incluirLogExclusaoMatricula(final MatriculaVO obj, final MatriculaPeriodoVO objMatPer, final String motivoExclusao, final UsuarioVO usuario) throws Exception;
}