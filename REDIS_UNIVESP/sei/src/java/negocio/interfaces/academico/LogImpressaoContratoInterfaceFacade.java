/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.LogImpressaoContratoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author Philippe
 */
public interface LogImpressaoContratoInterfaceFacade {

    public void incluir(final LogImpressaoContratoVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(final LogImpressaoContratoVO obj, UsuarioVO usuario) throws Exception;

    public List<LogImpressaoContratoVO> consultarPorMatricula(String matricula, UsuarioVO usuarioVO) throws Exception;

	void alterarPorImpressaoContrato(ImpressaoContratoVO obj, UsuarioVO usuario) throws Exception;
	
	public LogImpressaoContratoVO consultarUltimoContratoPorMatriculaPorTextoPadrao(String matricula, Integer textoPadrao, String tipoContrato, UsuarioVO usuarioVO) throws Exception;

}
