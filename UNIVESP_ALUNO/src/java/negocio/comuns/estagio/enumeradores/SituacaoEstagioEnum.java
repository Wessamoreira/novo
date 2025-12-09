package negocio.comuns.estagio.enumeradores;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoEstagioEnum {	
	 
	AGUARDANDO_ASSINATURA, 
	REALIZANDO, 
	EM_ANALISE, 
	EM_CORRECAO, 
	DEFERIDO, 
	INDEFERIDO,
	EXIGIDO, 
	PENDENTE;
	
	
	public boolean isAguardandoAssinatura() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoEstagioEnum.AGUARDANDO_ASSINATURA.name());
	}
	
	public boolean isRealizando() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoEstagioEnum.REALIZANDO.name());
	}
	
	public boolean isEmAnalise() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoEstagioEnum.EM_ANALISE.name());
	}
	
	public boolean isEmCorrecao() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoEstagioEnum.EM_CORRECAO.name());
	}
	
	public boolean isDeferido() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoEstagioEnum.DEFERIDO.name());
	}
	
	public boolean isIndeferido() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoEstagioEnum.INDEFERIDO.name());
	}
	
	public boolean isExigido() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoEstagioEnum.EXIGIDO.name());
	}
	
	public boolean isPendente() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoEstagioEnum.PENDENTE.name());
	}
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoEstagioEnum_" + this.name());
	}
	
	public static List<SituacaoEstagioEnum> getListaFiltroSituacaoEstagio() {
		List<SituacaoEstagioEnum> lista = new ArrayList<>();
		lista.add(SituacaoEstagioEnum.AGUARDANDO_ASSINATURA);
		lista.add(SituacaoEstagioEnum.REALIZANDO);
		lista.add(SituacaoEstagioEnum.EM_ANALISE);
		lista.add(SituacaoEstagioEnum.EM_CORRECAO);
		lista.add(SituacaoEstagioEnum.INDEFERIDO);
		lista.add(SituacaoEstagioEnum.DEFERIDO);
		return lista;
	}
	
	public static List<SituacaoEstagioEnum> getListaFiltroSituacaoEstagioVisaoAluno() {
		List<SituacaoEstagioEnum> lista = new ArrayList<>();
		lista.add(SituacaoEstagioEnum.EM_ANALISE);
		lista.add(SituacaoEstagioEnum.EM_CORRECAO);
		lista.add(SituacaoEstagioEnum.INDEFERIDO);
		lista.add(SituacaoEstagioEnum.DEFERIDO);
		return lista;
	}

}
