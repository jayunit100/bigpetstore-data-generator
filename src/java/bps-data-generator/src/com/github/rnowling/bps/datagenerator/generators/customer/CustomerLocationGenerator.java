package com.github.rnowling.bps.datagenerator.generators.customer;

import java.util.List;
import java.util.Vector;

import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.Store;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.generators.Generator;

public class CustomerLocationGenerator implements Generator<ZipcodeRecord>
{
	List<Store> stores;
	List<ZipcodeRecord> records;
	Sampler<ZipcodeRecord> sampler;
	
	public CustomerLocationGenerator(List<ZipcodeRecord> zipcodes, List<Store> stores, double averageDistance,
			SeedFactory seedFactory)
	{
		double lambda = 1.0 / averageDistance;
		
		double normalizationFactor = 0.0;
		List<Pair<ZipcodeRecord, Double>> zipcodeWeights = new Vector<Pair<ZipcodeRecord, Double>>();
		for(ZipcodeRecord record : zipcodes)
		{
			Pair<Store, Double> closestStore = findClosestStore(stores, record);
			double dist = closestStore.getSecond();
			
			double weight = lambda * Math.exp(-1.0 * lambda * dist);
			Pair<ZipcodeRecord, Double> pair = new Pair<ZipcodeRecord, Double>(record, weight);
			zipcodeWeights.add(pair);
			
			normalizationFactor += weight;
			
			
		}
		
		List<Pair<ZipcodeRecord, Double>> zipcodeProbs = new Vector<Pair<ZipcodeRecord, Double>>();
		for(Pair<ZipcodeRecord, Double> weighted : zipcodeWeights)
		{
			double prob = weighted.getSecond() / normalizationFactor;
			Pair<ZipcodeRecord, Double> pair = new Pair<ZipcodeRecord, Double>(weighted.getFirst(), prob);
			zipcodeProbs.add(pair);
		}
		
		this.sampler = new RouletteWheelSampler<ZipcodeRecord>(zipcodeProbs, seedFactory);
	}

	private Pair<Store, Double> findClosestStore(List<Store> stores, ZipcodeRecord record)
	{
		Pair<Store, Double> closestStore = new Pair<Store, Double>(null, Double.MAX_VALUE);
		
		for(Store store : stores)
		{
			double dist = record.distance(store.getLocation());
			if(dist < closestStore.getSecond())
			{
				closestStore = new Pair<Store, Double>(store, dist);
			}
		}
		
		return closestStore;
	}
	
	public ZipcodeRecord generate() throws Exception
	{
		return sampler.sample();
	}
}