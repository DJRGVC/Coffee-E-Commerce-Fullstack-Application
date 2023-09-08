package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;

/**
 * The IngredientRepository is used to handle CRUD operations on the Ingredient model. In addition to all functionality from `Service`, we also have functionality for retrieving a single Ingredient by name.
 */
public interface IngredientRepository extends JpaRepository <Ingredient, Integer> {

	/**
	 * Find a ingredient with the provided name
	 * @param name Name of the ingredient to find
	 * @return found ingredient, null if none
	 */
	Ingredient findByName (String name);
}