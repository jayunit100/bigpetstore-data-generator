package com.github.rnowling.bps.datagenerator.generators.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.datamodels.Pair;

public class TestProductCategoryUsageSimulator
{
	
	@Test
	public void testTrajectory()
	{
		SeedFactory seedFactory = new SeedFactory(1234);
		
		ProductCategoryUsageSimulator simulator = new ProductCategoryUsageSimulator(2.0, 1.0, 1.0, seedFactory);
		
		ProductCategoryUsageTrajectory trajectory = simulator.simulate(0.0, 30.0);
		
		assertEquals(0.0, trajectory.getLastAmount(), 0.0001);
		
		Pair<Double, Double> previousEntry = trajectory.getStep(0);
		for(int i = 1; i < trajectory.size(); i++)
		{
			Pair<Double, Double> entry = trajectory.getStep(i);
			// time should move forward
			assertTrue(previousEntry.getFirst() <= entry.getFirst());
			// remaining amounts should go down
			assertTrue(previousEntry.getSecond() >= entry.getSecond());
			previousEntry = entry;
		}
	}

}