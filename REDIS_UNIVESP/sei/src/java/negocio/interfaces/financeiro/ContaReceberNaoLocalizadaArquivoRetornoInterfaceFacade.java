/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaReceberNaoLocalizadaArquivoRetornoVO;

/**
 *
 * @author Philippe
 */
public interface ContaReceberNaoLocalizadaArquivoRetornoInterfaceFacade {

    public void incluir(final ContaReceberNaoLocalizadaArquivoRetornoVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(final ContaReceberNaoLocalizadaArquivoRetornoVO obj, UsuarioVO usuario) throws Exception;

    public List consultarPorNossoNumero(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public ContaReceberNaoLocalizadaArquivoRetornoVO consultarPorNossoNumeroUnico(String valorConsulta, Date dataProcessamento, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorPeriodoDataVcto(Date dataInicial, Date dataFinal, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public List consultarPorTratada(Boolean tratada, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public ContaReceberNaoLocalizadaArquivoRetornoVO consultarPorNossoNumeroVinculadoContaReceber(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}
