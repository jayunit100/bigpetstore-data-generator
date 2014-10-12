package com.github.rnowling.bps.datagenerator.builders.transaction;

import java.util.Map;

import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.PurchasingProfile;
import com.github.rnowling.bps.datagenerator.statistics.markovmodels.MarkovModel;
import com.google.common.collect.Maps;

public class PurchasingProfileBuilder
{
	Map<String, MarkovModel<Product>> profiles;
	
	public PurchasingProfileBuilder()
	{
		profiles = Maps.newHashMap();
	}
	
	public void addProfile(String productCategory, MarkovModel<Product> profile)
	{
		profiles.put(productCategory, profile);
	}
	
	public PurchasingProfile build()
	{
		return new PurchasingProfile(profiles);
	}
}