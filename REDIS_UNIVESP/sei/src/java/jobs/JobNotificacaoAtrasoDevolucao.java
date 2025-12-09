package jobs;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.EmprestimoVO;
import negocio.comuns.biblioteca.ItemEmprestimoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;
/**
 * 
 * @author murillo
 *
 */
@Service
@Lazy
public class JobNotificacaoAtrasoDevolucao extends SuperFacadeJDBC implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void realizarNotificacaoAtrasoLivro(){
		try{
			//System.out.println("Tempo Inicial Antigo: "+new Date().toString());
			final ConfiguracaoGeralSistemaVO  config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			final ConfiguracaoBibliotecaVO bibliotecaConfig = getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarConfiguracaoPadraoFuncionario(true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,false, new UsuarioVO());
			if(bibliotecaConfig.getNumeroDiasAtrazoEmprestimoEnviarPrimeiraNotificacao() != null && bibliotecaConfig.getNumeroDiasAtrazoEmprestimoEnviarPrimeiraNotificacao().intValue() > 0){
				realizarEnvioMensagemNotificacaoAtrasoEmprestimo(config, bibliotecaConfig,"PRIMEIRA",bibliotecaConfig.getNumeroDiasAtrazoEmprestimoEnviarPrimeiraNotificacao());
				
			}
			if(bibliotecaConfig.getNumeroDiasAtrazoEmprestimoEnviarSegundaNotificacao() != null && bibliotecaConfig.getNumeroDiasAtrazoEmprestimoEnviarSegundaNotificacao().intValue() > 0){
				realizarEnvioMensagemNotificacaoAtrasoEmprestimo(config, bibliotecaConfig,"SEGUNDA",bibliotecaConfig.getNumeroDiasAtrazoEmprestimoEnviarSegundaNotificacao());
 
			}
			if(bibliotecaConfig.getNumeroDiasAtrazoEmprestimoEnviarTerceiraNotificacao() != null && bibliotecaConfig.getNumeroDiasAtrazoEmprestimoEnviarTerceiraNotificacao().intValue() > 0){
				realizarEnvioMensagemNotificacaoAtrasoEmprestimo(config, bibliotecaConfig,"TERCEIRA",bibliotecaConfig.getNumeroDiasAtrazoEmprestimoEnviarSegundaNotificacao());
			}
			
			
			//System.out.println("Tempo Final Antigo: "+new Date().toString());
		}
		catch(Exception e){
			//System.out.println(e.getMessage());
            //System.out.println("Erro JobNotificacaoDevolucao...");			
		}
		
		
	}

	private void realizarEnvioMensagemNotificacaoAtrasoEmprestimo(ConfiguracaoGeralSistemaVO config,ConfiguracaoBibliotecaVO bibliotecaConfig, String notificacao, Integer numeroDias) throws Exception {
		List<EmprestimoVO> objs = getFacadeFactory().getEmprestimoFacade().consultarAtrasadosParaNotificacao(numeroDias, notificacao, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS);
		for(EmprestimoVO obj:objs){
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executaEnvioMensagemLivroAtrasadoJob(obj.getItemEmprestimoVOs(), obj.getPessoa(), obj.getTipoPessoa(), obj.getBiblioteca().getNome(), obj.getUnidadeEnsinoVO().getCodigo(), new UsuarioVO(), config, bibliotecaConfig.getFuncionarioPadraoEnvioMensagem().getPessoa());
			realizarAtualizacaoItemEmprestimo(obj.getItemEmprestimoVOs(), notificacao);
		}
	
		
	}
	private void realizarAtualizacaoItemEmprestimo(List<ItemEmprestimoVO> objs, String notificacao) throws Exception {
		if(objs != null && !objs.isEmpty()){
			StringBuilder sql = new StringBuilder("UPDATE itememprestimo SET ");
			if(notificacao.equals("PRIMEIRA")){
				sql.append("dataprimeiranotificacao = '");
				sql.append(Uteis.getDataJDBC(new Date())+"'");
				sql.append(" where itememprestimo.codigo in(");
				boolean vigula = false;
				for(ItemEmprestimoVO item:objs){
					if (vigula) {
						sql.append(",").append(item.getCodigo().intValue());
					} else {
						sql.append(item.getCodigo().intValue());
                        vigula = true;
					}
				}
				sql.append(" )");
				getConexao().getJdbcTemplate().update(sql.toString());
			}
			else if(notificacao.equals("SEGUNDA")){
				sql.append("datasegundanotificacao = '");
				sql.append(Uteis.getDataJDBC(new Date())+"'");
				sql.append(" WHERE itememprestimo.codigo in(");
				boolean vigula = false;
				for(ItemEmprestimoVO item:objs){
					if (vigula) {
						sql.append(",").append(item.getCodigo().intValue());
					} else {
						sql.append(item.getCodigo().intValue());
                        vigula = true;
					}
				}
				sql.append(" )");
				getConexao().getJdbcTemplate().update(sql.toString());
			}
			else if(notificacao.equals("TERCEIRA")){
				sql.append("dataterceiranotificacao = '");
				sql.append(Uteis.getDataJDBC(new Date())+"'");
				sql.append(" WHERE itememprestimo.codigo in(");
				boolean vigula = false;
				for(ItemEmprestimoVO item:objs){
					if (vigula) {
						sql.append(",").append(item.getCodigo().intValue());
					} else {
						sql.append(item.getCodigo().intValue());
                        vigula = true;
					}
				}
				sql.append(" )");
				getConexao().getJdbcTemplate().update(sql.toString());
			
			}
									
		}
				
	}
}
