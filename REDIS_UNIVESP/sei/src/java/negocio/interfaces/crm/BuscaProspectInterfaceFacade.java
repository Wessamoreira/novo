/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.crm;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.BuscaProspectVO;

/**
 *
 * @author Philippe
 */
public interface BuscaProspectInterfaceFacade {
    
    public List<BuscaProspectVO> consultar(String letra, BuscaProspectVO buscaProspectVO, UsuarioVO usuario, Integer limit , Integer offset) throws Exception;
    
    public String consultarEmails(String letra, BuscaProspectVO buscaProspectVO, UsuarioVO usuario) throws Exception;

    Integer consultarTotalRegistro(String letra, BuscaProspectVO buscaProspectVO, UsuarioVO usuario) throws Exception;

	String consultarEmailsExcel(String letra, BuscaProspectVO buscaProspectVO, UsuarioVO usuario) throws Exception;
	public List<BuscaProspectVO> consultarEmailSuggestionBox(String info,UsuarioVO usuario) throws Exception;
	public List<BuscaProspectVO> consultarTelefoneSuggestionBox(String info,UsuarioVO usuario) throws Exception;
	List<BuscaProspectVO> consultarCpfSuggestionBox(String info, UsuarioVO usuario) throws Exception;
}
