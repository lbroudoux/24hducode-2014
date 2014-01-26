package org.les24hducode.supercal.fmw.domain;

import java.util.ArrayList;
import java.util.Collection;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.BranchState;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.PathEvaluator;
/**
 * 
 * @author laurent
 */
public class EndsEvaluator implements PathEvaluator {
   
   protected Collection<Node> endNodes;
   protected Collection<Long> endNodesId = new ArrayList<Long>();

   public EndsEvaluator(Collection<Node> nodes){
      this.endNodes = nodes;
      for (Node node : nodes) {
         endNodesId.add(node.getId());
      }
   }

   @Override
   public Evaluation evaluate(Path path) {
      long id = path.reverseNodes().iterator().next().getId();
      if (endNodesId.contains(id)){
         return org.neo4j.graphdb.traversal.Evaluation.INCLUDE_AND_CONTINUE;
      }
      return org.neo4j.graphdb.traversal.Evaluation.EXCLUDE_AND_CONTINUE;
   }

   @Override
   public Evaluation evaluate(Path path, BranchState state) {
      long id = path.reverseNodes().iterator().next().getId();
      if (endNodesId.contains(id)){
         return org.neo4j.graphdb.traversal.Evaluation.INCLUDE_AND_CONTINUE;
      }
      return org.neo4j.graphdb.traversal.Evaluation.EXCLUDE_AND_CONTINUE;
   }
}
