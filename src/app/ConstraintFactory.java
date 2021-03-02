package app;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import graph.Edge;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearIntExpr;
import ilog.cplex.IloCplex;

public class ConstraintFactory {

	public ConstraintFactory() {

	}

	public void cycleRestrictions(IloCplex model, IloIntVar[][] x, Collection<List<Integer>> combindeds)
			throws IloException {
		for (List<Integer> list : combindeds) {
			IloLinearIntExpr r = model.linearIntExpr();
			for (int i = 1; i <= list.size(); i++) {
				r.addTerm(1, x[list.get(i % list.size())][list.get((i + 1) % list.size())]);
			}
			model.addLe(r, list.size() - 1);
		}
	}

	/*
	 * stack中存放一个子环，该函数作用为在model中约束该子环无法产生
	 */
	public void cycleRestrictions(IloCplex model, IloIntVar[][] x, Stack<Edge> combindeds) throws IloException {
		IloLinearIntExpr r = model.linearIntExpr();
		for (Edge edge : combindeds) {
			r.addTerm(1, x[edge.getFrom()][edge.getTo()]);
		}
		model.addLe(r, combindeds.size() - 1);

		r = model.linearIntExpr();//TODO
		Collections.reverse(combindeds);
		for (Edge edge : combindeds) {
			r.addTerm(1, x[edge.getFrom()][edge.getTo()]);
		}
		model.addLe(r, combindeds.size() - 1);
	}
}
