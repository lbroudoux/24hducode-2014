package org.les24hducode.supercal.fmw.domain;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.BranchState;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.PathEvaluator;

public class EndEvaluator implements PathEvaluator {
	
	protected Node endNode;

	public EndEvaluator(Node persistentState) {
		super();
		endNode = persistentState;
	}

	@Override
	public Evaluation evaluate(Path arg0) {
		if (arg0.reverseNodes().iterator().next().getId() == endNode.getId()) 
				return org.neo4j.graphdb.traversal.Evaluation.INCLUDE_AND_CONTINUE;
		return org.neo4j.graphdb.traversal.Evaluation.EXCLUDE_AND_CONTINUE;
	}

	@Override
	public Evaluation evaluate(Path arg0, BranchState arg1) {
		if (arg0.reverseNodes().iterator().next().getId() == endNode.getId()) 
			return org.neo4j.graphdb.traversal.Evaluation.INCLUDE_AND_CONTINUE;
	return org.neo4j.graphdb.traversal.Evaluation.EXCLUDE_AND_CONTINUE;
	}

}
