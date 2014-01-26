package org.les24hducode.supercal.fmw.domain;

import static org.junit.Assert.*;

import javax.validation.constraints.AssertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.les24hducode.supercal.fmw.repository.StopRepository;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.kernel.Traversal;
import org.neo4j.kernel.Uniqueness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = "classpath:/applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TraversalDescriptionTest {

	@Autowired
	private StopRepository stopRepository;
	   
	@Test
	public void test() {
		
//		Stop stop = stopRepository.getStopById("0006");
//		
//	    System.err.println("tests");
//	      TraversalDescription traversalDescription = Traversal.description()
//	              .breadthFirst().uniqueness(Uniqueness.NODE_PATH)
//	              .relationships(DynamicRelationshipType.withName("SECTION"))
//	              .evaluator(Evaluators.all())
//	              .evaluator(new EndEvaluator(stop.getPersistentState()));
//	        Traverser t = traversalDescription.traverse(stop.getPersistentState());
//	        for (Path position : t) {
//	           System.err.println("Path from start node to current position is " + position);
//	           assertNotNull(position);
//	        }
//	        System.err.println("tests");
	}

}
