package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

	/** id for inventory entry */
	@Id
	@GeneratedValue
	private Long id;
	// @Min ( 0 )

	/** this is a lsit of all ingredients int he invenory */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Ingredient> ingredients;

	/**
	 * Empty constructor for Hibernate
	 */
	public Inventory() {
		// Intentionally empty so that Hibernate can instantiate
		// Inventory object.
		// ingredients = new ArrayList of ingredients
	}

	/**
	 * Returns the ID of the entry in the DB
	 *
	 * @return long
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * Set the ID of the Inventory (Used by Hibernate)
	 *
	 * @param id the ID
	 * 
	 */
	public void setId(final Long id) {
		this.id = id;
	}
	
	/**
	 * Updates the inventory with the amounts in the list
	 * @param ivt Inventory to update
	 */
	public void updateIngredientList(final Inventory ivt) {
		this.ingredients = ivt.getIngredients();
	}

	/**
	 * Checks if ingredient exists, and returns amount if it does otherwise, throws
	 * IAE
	 *
	 * @param name String the name of the ingredient
	 * 
	 * @return int the amount of the ingredient
	 */
	public int checkIngredient(final String name) {
		for (final Ingredient ing : ingredients) {
			if (ing.getName().equals(name)) {
				return ing.getAmount();
			}
		}
		throw new IllegalArgumentException("The ingredient does not exist");
	}

	/** 
	 * Returns the list of ingredients in the inventory
	 * 
	 * @return List of Ingredient
	 * 
	 */
	public List<Ingredient> getIngredients() {
		return this.ingredients;
	}


	/**
	 * Sets the list of ingredients in the inventory
	 * 
	 * @param list List of Ingredient
	 * 
	 */
	public void addIngredients(final List<Ingredient> list) {
		this.ingredients = list;

		// if ingredients have duplicate names, throw IAE
		// create hashset of names
		// if size of hashset != size of list, throw IAE
		final HashSet<String> names = new HashSet<String>();
		for (final Ingredient ing : ingredients) {
			names.add(ing.getName());
		}
		if (names.size() != ingredients.size()) {
			throw new IllegalArgumentException("Duplicate ingredients");
		}
	}

	/**
	 * Returns true if there are enough ingredients to make the beverage.
	 *
	 * @param r recipe to check if there are enough ingredients
	 * @return true if enough ingredients to make the beverage
	 */
	public boolean enoughIngredients(final Recipe r) {
		for (final Ingredient ing : ingredients) {
			try {
				final int recipeIngAmount = r.getAmount(ing.getName());
				if (recipeIngAmount != -1) {
					if (ing.getAmount() < recipeIngAmount) {
						return false;
					}
				}
			}
			catch (final Exception e) {
				continue;
			}
			
		}
		return true;
	}

	/**
	 * Removes the ingredients used to make the specified recipe. Assumes that the
	 * user has checked that there are enough ingredients to make
	 *
	 * @param r recipe to make
	 * @return true if recipe is made.
	 */
	public boolean useIngredients(final Recipe r) {
		if (enoughIngredients(r)) {
			for (final Ingredient ing : ingredients) {
			try {
				final int recipeIngAmount = r.getAmount(ing.getName());
				if (recipeIngAmount != -1) {
					ing.setAmount(ing.getAmount() - recipeIngAmount);
				}
			} catch (final Exception e) {
				continue;
			}
			}
			return true;
		}
		return false;

	}

	/**
	 * Adds ingredients to the inventory
	 *
	 * @param newIng - Ingredient to add
	 * @return true if successful, false if not
	 */
	public boolean addIngredient(final Ingredient newIng) {
		if (ingredients == null) {
			ingredients = new ArrayList<Ingredient>();
		}

//    	Ingredient validations happens in Ingredient class

		for (final Ingredient ing : ingredients) {

			if (ing.equals(newIng)) {
				throw new IllegalArgumentException("Ingredient already exists");
			}

		}

		ingredients.add(newIng);
		return true;
	}
	
	/**
	 * Returns a list of the names of the ingredients in the inventory
	 * @return List for the names of the ingredients
	 */
	public List<String> getIngNames() {
		final List<String> ret = new ArrayList<String>();
		if (this.ingredients == null) {
			return null;
		}
		for (final Ingredient ing: ingredients) {
			ret.add(ing.getName());
		}
		return ret;
	}

	/**
	 * 
	 * Sets given ingredient to a given amount.
	 * 
	 * Throw an IAE if: the name is null the amount is negative the ingredient does
	 * not exist
	 * 
	 * 
	 * @param name   - name of ingredient to update
	 * @param amount - new amount of ingredient
	 * @return
	 */
	public void updateInventory(final String name, final int amount) {
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null.");
		}
		if (amount < 0) {
			throw new IllegalArgumentException("Amount cannot be negative.");
		}
		boolean containsIngredient = false;
		for (final Ingredient ing : ingredients) {
			if (ing.getName().equals(name)) {
				ing.setAmount(amount);
				containsIngredient = true;
			}
		}
		if (!containsIngredient) {
			throw new IllegalArgumentException("Ingredient does not exist");
		}
	}

	/**
	 * Returns a string describing the current contents of the inventory.
	 *
	 * @return String
	 */
	@Override
	public String toString() {
		return "Inventory [id=" + id + ", ingredients=" + ingredients + "]";
	}

}