package com.github.rnowling.bps.datagenerator.generators.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.datamodels.PetSpecies;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.github.rnowling.bps.datagenerator.datareaders.ProductCategoryBuilder;
import com.google.common.collect.Maps;

public class TestProductCategoryInventory
{
	
	@Test
	public void testTrajectory()
	{
		SeedFactory seedFactory = new SeedFactory(1234);
		
		CustomerTransactionParametersGenerator generator = new CustomerTransactionParametersGenerator(seedFactory);
		
		CustomerTransactionParameters parameters = generator.generate();
		
		ProductCategoryBuilder builder = new ProductCategoryBuilder();
		builder.addApplicableSpecies(PetSpecies.DOG);
		builder.setAmountUsedPetPetAverage(1.0);
		builder.setAmountUsedPetPetVariance(1.0);
		builder.setDailyUsageRate(2.0);
		
		
		ProductCategory category = builder.build();
		
		ProductCategoryInventory inventory = new ProductCategoryInventory(category, parameters, seedFactory);
		
		assertEquals(inventory.findExhaustionTime(), 0.0, 0.0001);
		assertEquals(inventory.findRemainingAmount(0.0), 0.0, 0.0001);
		
		Map<String, Object> fields = Maps.newHashMap();
		fields.put(Constants.PRODUCT_CATEGORY, "dog food");
		fields.put(Constants.PRODUCT_QUANTITY, 30.0);
		Product product = new Product(fields);
		
		inventory.simulatePurchase(1.0, product);
		
		assertTrue(inventory.findExhaustionTime() > 1.0);
		assertTrue(inventory.findRemainingAmount(1.0) > 0.0);
	}

}