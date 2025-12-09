/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.LogTurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author Philippe
 */
public interface LogTurmaInterfaceFacade {

    public void incluir(final LogTurmaVO obj, UsuarioVO usuario) throws Exception;

    public List consultarPorPeriodo(Date dataInicial, Date dataFinal, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorIdentificadorTurma(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorAcao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;


}
