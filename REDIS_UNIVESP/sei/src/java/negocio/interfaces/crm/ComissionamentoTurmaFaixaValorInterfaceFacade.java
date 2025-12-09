/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.crm;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.ComissionamentoTurmaFaixaValorVO;

/**
 *
 * @author Otimize-04
 */
public interface ComissionamentoTurmaFaixaValorInterfaceFacade {

    public void incluir(final ComissionamentoTurmaFaixaValorVO obj, UsuarioVO usuario) throws Exception;
    public void alterar(final ComissionamentoTurmaFaixaValorVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(ComissionamentoTurmaFaixaValorVO obj, UsuarioVO usuario) throws Exception;
    public void validarDados(ComissionamentoTurmaFaixaValorVO obj) throws Exception;
    public void incluirComissionamentoFaixaValorVOs(Integer comissionamento, List objetos, UsuarioVO usuario) throws Exception;
    public void alterarComissionamentoFaixaValorVOs(Integer comissionamentoTurma, List objetos, UsuarioVO usuario) throws Exception;
    public void excluirComissionamentoFaixaValorVOs(Integer comissionamento, UsuarioVO usuario) throws Exception;
    public void validarDadosIntervalorQtdeAluno(List<ComissionamentoTurmaFaixaValorVO> listaComissionamentoFaixaVO, ComissionamentoTurmaFaixaValorVO obj) throws Exception;
    public List<ComissionamentoTurmaFaixaValorVO> consultaRapidaPorComissionamentoTurma(Integer comissionamentoTurma,  boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public Double consultarValorComissionamentoPorTurma(Integer turma, Integer qtdeAlunoTurma, UsuarioVO usuarioVO);
    public ComissionamentoTurmaFaixaValorVO consultarComissionamentoPorTurmaFaixa(Integer turma, Integer qtdeAlunoTurma, UsuarioVO usuarioVO) throws Exception;
	ComissionamentoTurmaFaixaValorVO consultarComissionamentoPorComissinamentoTurmaFaixa(Integer comissionamentoturma, Integer qtdeAlunoTurma, UsuarioVO usuarioVO) throws Exception;

}
