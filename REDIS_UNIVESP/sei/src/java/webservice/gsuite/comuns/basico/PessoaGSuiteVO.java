package webservice.gsuite.comuns.basico;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;

public class PessoaGSuiteVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5823059257943369741L;
	private Integer codigo;
	private PessoaVO pessoaVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private String email;

}
