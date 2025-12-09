package negocio.comuns.utilitarias.formula.pool;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import negocio.comuns.utilitarias.formula.Formula;

@Component
@Scope("singleton")
public class FormulaPool extends ObjectPool<Formula> {

}
