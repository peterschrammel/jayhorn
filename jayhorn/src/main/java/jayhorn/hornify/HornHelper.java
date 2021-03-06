package jayhorn.hornify;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Verify;

import jayhorn.solver.Prover;
import jayhorn.solver.ProverExpr;
import jayhorn.solver.ProverFun;
import jayhorn.solver.ProverTupleExpr;
import jayhorn.solver.ProverTupleType;
import jayhorn.solver.ProverType;
import soottocfg.cfg.type.BoolType;
import soottocfg.cfg.type.IntType;
import soottocfg.cfg.type.ReferenceType;
import soottocfg.cfg.type.Type;
import soottocfg.cfg.type.TypeType;
import soottocfg.cfg.variable.Variable;

public class HornHelper {

	public static final int NullValue = 0;
	
	private static HornHelper hh;
	
	public static void resetInstance() {
		hh = null;
	}

	public static HornHelper hh() {
		if (null == hh) {
			hh = new HornHelper();
		}
		return hh;
	}

	private HornHelper() {
	}

	/**
	 * Creates a ProverType from a Type.
	 * TODO: not fully implemented.
	 * 
	 * @param p
	 * @param t
	 * @return
	 */
	public ProverType getProverType(Prover p, Type t) {
		if (t == IntType.instance()) {
			return p.getIntType();
		}
		if (t == BoolType.instance()) {
			return p.getBooleanType();
		}
		if (t instanceof ReferenceType) {
			ReferenceType rt = (ReferenceType) t;
			final ProverType[] subTypes = new ProverType[rt.getElementTypeList().size()];
			for (int i = 0; i < rt.getElementTypeList().size(); i++) {
				subTypes[i] = getProverType(p, rt.getElementTypeList().get(i));
			}
			return p.getTupleType(subTypes);
		}
		if (t instanceof TypeType) {
			return p.getIntType();
		}

		throw new IllegalArgumentException("don't know what to do with " + t);
	}
	
	public ProverTupleExpr mkNullExpression(Prover p, ProverType[] types) {
		ProverExpr[] subExprs = new ProverExpr[types.length];
		for (int i = 0; i < types.length; i++) {
			if (types[i] instanceof jayhorn.solver.BoolType) {
				subExprs[i] = p.mkLiteral(false);
			} else if (types[i] instanceof jayhorn.solver.IntType) {
				subExprs[i] = p.mkLiteral(NullValue);
			} else if (types[i] instanceof ProverTupleType) {				
				subExprs[i] = mkNullExpression(p, ((ProverTupleType)types[i]).getSubTypes());
			} else {
				throw new RuntimeException("Not implemented " + types[i].getClass());
			}
		}
		return (ProverTupleExpr) p.mkTuple(subExprs);
	}

	public ProverFun genHornPredicate(Prover p, String name, List<Variable> sortedVars) {
		final List<ProverType> types = new LinkedList<ProverType>();
		for (Variable v : sortedVars) {
			types.add(getProverType(p, v.getType()));
		}
		return p.mkHornPredicate(name, types.toArray(new ProverType[types.size()]));
	}

	private int varNum = 0;

	public int newVarNum() {
		return varNum++;
	}

	public List<ProverExpr> findOrCreateProverVar(Prover p, List<Variable> cfgVars, Map<Variable, ProverExpr> varMap) {
		List<ProverExpr> res = new LinkedList<ProverExpr>();
		for (Variable v : cfgVars) {
			res.add(findOrCreateProverVar(p, v, varMap));
		}
		return res;
	}

	public ProverExpr findOrCreateProverVar(Prover p, Variable v, Map<Variable, ProverExpr> varMap) {
		if (!varMap.containsKey(v)) {
			varMap.put(v, createVariable(p, v));
		}
		return varMap.get(v);
	}

	public ProverExpr createVariable(Prover p, Variable v) {
		ProverType pt = getProverType(p, v.getType());
		if (pt instanceof ProverTupleType) {
			ProverTupleType ptt = (ProverTupleType) pt;
			ProverExpr[] subExprs = new ProverExpr[ptt.getArity()];
			Verify.verify(v.getType() instanceof ReferenceType);
			for (int i = 0; i < ptt.getArity(); i++) {
				subExprs[i] = createVariable(p,
						new Variable(v.getName() + "#" + i, ((ReferenceType) v.getType()).getElementTypeList().get(i)));
			}
			return p.mkTuple(subExprs);
		} else {
			return p.mkHornVariable(v.getName() + "_" + newVarNum(), pt);
		}
	}

	public ProverExpr createVariable(Prover p, String prefix, Type tp) {
		return p.mkHornVariable(prefix + newVarNum(), getProverType(p, tp));
	}

	public List<Variable> setToSortedList(Set<Variable> set) {
		List<Variable> res = new LinkedList<Variable>(set);
		if (!res.isEmpty()) {
			Collections.sort(res, new Comparator<Variable>() {
				@Override
				public int compare(final Variable object1, final Variable object2) {
					return object1.getName().compareTo(object2.getName());
				}
			});
		}
		return res;
	}
	
	
	public ProverExpr findOrCreateUniqueVariable(Prover p, Variable v, Map<Variable, ProverExpr> varMap, int uniqueNumber) {
		if (!varMap.containsKey(v)) {
			varMap.put(v, createVariable(p, v));
		}
		return varMap.get(v);
	}	
	public ProverExpr createUniqueVariable(Prover p, Variable v, int number) {
		ProverType pt = getProverType(p, v.getType());
		if (pt instanceof ProverTupleType) {
			ProverTupleType ptt = (ProverTupleType) pt;
			ProverExpr[] subExprs = new ProverExpr[ptt.getArity()];
			Verify.verify(v.getType() instanceof ReferenceType);
			subExprs[0] = p.mkLiteral(number);
			for (int i = 1; i < ptt.getArity(); i++) {
				subExprs[i] = createVariable(p,
						new Variable(v.getName() + "#" + i, ((ReferenceType) v.getType()).getElementTypeList().get(i)));
			}
			return p.mkTuple(subExprs);
		} else if (pt instanceof jayhorn.solver.IntType) {
			return p.mkLiteral(number);
		} else {
			return p.mkHornVariable(v.getName() + "_" + newVarNum(), pt);
		}
	}

}
