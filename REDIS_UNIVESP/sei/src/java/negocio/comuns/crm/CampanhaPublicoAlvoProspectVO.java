/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.crm;

import java.util.Date;

import negocio.comuns.administrativo.CampanhaPublicoAlvoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.enumeradores.TipoDistribuicaoProspectCampanhaPublicoAlvoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.enumerador.PoliticaRedistribuicaoProspectAgendaEnum;
import negocio.comuns.crm.enumerador.TipoSituacaoCompromissoEnum;

/**
 *
 * @author Philippe
 */
public class CampanhaPublicoAlvoProspectVO extends SuperVO {

    private Integer codigo;
    private ProspectsVO prospect;
    private PessoaVO pessoa;
    private CampanhaPublicoAlvoVO campanhaPublicoAlvo;
    private FuncionarioVO consultorDistribuicaoVO;
    private CompromissoCampanhaPublicoAlvoProspectVO compromissoCampanhaPublicoAlvoProspectVO;
    /**
     * Atributos transient, apenas para controle de redistribuição de Agenda
     */
    private PoliticaRedistribuicaoProspectAgendaEnum politicaRedistribuicaoProspectAgenda;
    private TipoSituacaoCompromissoEnum situacaoAtualCompromissoAgendaEnum;
    private TipoDistribuicaoProspectCampanhaPublicoAlvoEnum tipoDistribuicaoProspectCampanhaPublicoAlvo;

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public ProspectsVO getProspect() {
        if (prospect == null) {
            prospect = new ProspectsVO();
        }
        return prospect;
    }

    public void setProspect(ProspectsVO prospect) {
        this.prospect = prospect;
    }

    public CampanhaPublicoAlvoVO getCampanhaPublicoAlvo() {
        if (campanhaPublicoAlvo == null) {
            campanhaPublicoAlvo = new CampanhaPublicoAlvoVO();
        }
        return campanhaPublicoAlvo;
    }

    public void setCampanhaPublicoAlvo(CampanhaPublicoAlvoVO campanhaPublicoAlvo) {
        this.campanhaPublicoAlvo = campanhaPublicoAlvo;
    }

    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return pessoa;
    }

    public void setPessoa(PessoaVO pessoa) {
        this.pessoa = pessoa;
    }
    
    /**
     * Retorna o código do Prospect ou da Pessoa, verificando qual
     * dois dois está preenchido. Somente um dos dois é retornado para controle
     * privilegiando o código do prospect.
     * @return 
     */
    public Integer getCodigoProspectPessoa() {
        Integer codigoPessoaProspect = this.getProspect().getCodigo();
        if (codigoPessoaProspect == null || codigoPessoaProspect == 0) {
            codigoPessoaProspect = this.getPessoa().getCodigo();
        }
        return codigoPessoaProspect;
    }
    
    public FuncionarioVO getConsultorDistribuicaoVO() {
		if (consultorDistribuicaoVO == null) {
			consultorDistribuicaoVO = new FuncionarioVO();
		}
		return consultorDistribuicaoVO;
	}

	public void setConsultorDistribuicaoVO(FuncionarioVO consultorDistribuicaoVO) {
		this.consultorDistribuicaoVO = consultorDistribuicaoVO;
	}

	public CompromissoCampanhaPublicoAlvoProspectVO getCompromissoCampanhaPublicoAlvoProspectVO() {
		if (compromissoCampanhaPublicoAlvoProspectVO == null) {
			compromissoCampanhaPublicoAlvoProspectVO = new CompromissoCampanhaPublicoAlvoProspectVO();
		}
		return compromissoCampanhaPublicoAlvoProspectVO;
	}

	public void setCompromissoCampanhaPublicoAlvoProspectVO(CompromissoCampanhaPublicoAlvoProspectVO compromissoCampanhaPublicoAlvoProspectVO) {
		this.compromissoCampanhaPublicoAlvoProspectVO = compromissoCampanhaPublicoAlvoProspectVO;
	}
	
	public String getNomeProspect() {
		return getProspect().getNome();
	}
	
	public String getDataCompromisso_Apresentar() {
		return getCompromissoCampanhaPublicoAlvoProspectVO().getDataCompromisso_Apresentar();
	}
	
	public Date getDataCompromisso() {
		return getCompromissoCampanhaPublicoAlvoProspectVO().getDataCompromisso();
	}
	
	
	public PoliticaRedistribuicaoProspectAgendaEnum getPoliticaRedistribuicaoProspectAgenda() {
		if (politicaRedistribuicaoProspectAgenda == null) {
			politicaRedistribuicaoProspectAgenda = PoliticaRedistribuicaoProspectAgendaEnum.TODOS;
		}
		return politicaRedistribuicaoProspectAgenda;
	}

	public void setPoliticaRedistribuicaoProspectAgenda(PoliticaRedistribuicaoProspectAgendaEnum politicaRedistribuicaoProspectAgenda) {
		this.politicaRedistribuicaoProspectAgenda = politicaRedistribuicaoProspectAgenda;
	}
	
	public TipoSituacaoCompromissoEnum getSituacaoAtualCompromissoAgendaEnum() {
		if (situacaoAtualCompromissoAgendaEnum == null) {
			situacaoAtualCompromissoAgendaEnum = TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO;
		}
		return situacaoAtualCompromissoAgendaEnum;
	}

	public void setSituacaoAtualCompromissoAgendaEnum(TipoSituacaoCompromissoEnum situacaoAtualCompromissoAgendaEnum) {
		this.situacaoAtualCompromissoAgendaEnum = situacaoAtualCompromissoAgendaEnum;
	}


	public TipoDistribuicaoProspectCampanhaPublicoAlvoEnum getTipoDistribuicaoProspectCampanhaPublicoAlvo() {
		if (tipoDistribuicaoProspectCampanhaPublicoAlvo == null) {
			tipoDistribuicaoProspectCampanhaPublicoAlvo = TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.REDISTRIBUIR;
		}
		return tipoDistribuicaoProspectCampanhaPublicoAlvo;
	}

	public void setTipoDistribuicaoProspectCampanhaPublicoAlvo(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum tipoDistribuicaoProspectCampanhaPublicoAlvo) {
		this.tipoDistribuicaoProspectCampanhaPublicoAlvo = tipoDistribuicaoProspectCampanhaPublicoAlvo;
	}
}
