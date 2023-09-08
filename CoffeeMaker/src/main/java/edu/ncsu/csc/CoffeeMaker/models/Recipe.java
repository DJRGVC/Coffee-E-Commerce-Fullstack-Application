package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Recipe extends DomainObject {

	/** Recipe id */ 
	@Id
	@GeneratedValue
	private Long id;

	/** Recipe name */
	private String name;

	/** Recipe price */
	@Min(0)
	private Integer price;

	/** Recipe list of ingredients */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Ingredient> ingredients;

	/**
	 * Creates a default recipe for the coffee maker.
	 */
	public Recipe() {
		this.name = "";
		price = 0;
		ingredients = new ArrayList<Ingredient>();
	}
	
	/**
	 * Creates a recipe for the coffee maker.
	 * @param name the name
	 * @param price the price
	 */
	public Recipe(final String name, final int price) {
		this.name = name;
		this.price = price;
		ingredients = new ArrayList<Ingredient>();
	}

	/**
	 * Check if all ingredient fields in the recipe are 0
	 *
	 * @return true if all ingredient fields are 0, otherwise return false
	 */
	public boolean checkRecipe() {
		for (final Ingredient ing: ingredients) {
			if (ing.getAmount() != 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Get the ID of the Recipe
	 * 
	 * @return the ID
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * Set the ID of the Recipe (Used by Hibernate)
	 *
	 * @param id the ID
	 */
	@SuppressWarnings("unused")
	private void setId(final Long id) {
		this.id = id;
	}
	
	/**
	 * Returns boolean for if the recipe contains an ingredient
	 * 
	 * @param name the name
	 * @return boolean
	 */
	public int getAmount(final String name) {
		for (final Ingredient ing: ingredients) {
			if (ing.getName().equals(name)) {
				return ing.getAmount();
			}
		}
		// or -1 if not found, cleaner than throwing exception in this case.
		throw new IllegalStateException();
	}
	

	/**
	 * Returns name of the recipe.
	 *
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the recipe name.
	 *
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Returns the price of the recipe.
	 *
	 * @return Returns the price.
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * Sets the recipe price.
	 *
	 * @param price The price to set.
	 */
	public void setPrice(final Integer price) {
		this.price = price;
	}

	/**
	 * Updates the fields to be equal to the passed Recipe
	 *
	 * @param inventory the inventory
	 * @param r with updated fields
	 */
	public void updateRecipe(final Recipe r, final List<String> inventory) {
		this.setName(r.getName());
		this.setPrice(r.getPrice());
		if (inventory == null || r.getIngredients().isEmpty()) {
			throw new IllegalArgumentException();
		}
		for(final Ingredient ing: r.getIngredients()) {
			if (inventory.contains(ing.getName())) {
				continue;
			} else {
				throw new IllegalArgumentException();
			}
		}
		this.ingredients = r.getIngredients();
	}

	/**
	 * Add an ingredient to ingredients list
	 * 
	 * @param name the name
	 * @param amount the amount
	 * @param inv the inventory
	 */
	public void addIngredient(final String name, final int amount, final Inventory inv) {
		if (inv.getIngNames() == null) {
			throw new IllegalArgumentException();
		}
		if (inv.getIngNames().contains(name)) {
			ingredients.add(new Ingredient(name, amount));
		} else {
			throw new IllegalArgumentException();
		}
		
	}
	
	/**
	 * Add an ingredient to ingredients list
	 * 
	 * @param ingredient The ingredient to add
	 * @param inv the inventory
	 */
	public void addIngredient(final Ingredient ingredient, final Inventory inv) {
		if (inv.getIngNames() == null) {
			throw new IllegalArgumentException();
		}
		if (inv.getIngNames().contains(ingredient.getName())) {
			ingredients.add(ingredient);
		} else {
			throw new IllegalArgumentException();
		}
			}

	/**
	 * Get the list of ingredients
	 * 
	 * @return List
	 */
	public List<Ingredient> getIngredients() {
		return ingredients;
	}


	/**
	 * Checks to make sure no duplicate names of ingredients
	 * 
	 * @return boolean
	 */
	public boolean validateIngredients() {
		final List<String> names = new ArrayList<String>();
		for (final Ingredient ing: ingredients) {
			if (names.contains(ing.getName())) {
				return false;
			} else {
				names.add(ing.getName());
			}
		}
		return true;
	}

	/**
	 * Returns the name of the recipe, as well as the list of ingredients
	 *
	 * @return String
	 */
	@Override
	public String toString() {
		final StringBuilder retString = new StringBuilder();
		retString.append(this.name + " contains the following ingredients:\n");
		for (int i = 0; i < ingredients.size(); i++) {
			retString.append(ingredients.get(i).toString());
		}
		return retString.toString();
	}

	/**
	 * Returns the hashCode of the Recipe
	 * @return hashCode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		Integer result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * Returns boolean for if the recipe contains an ingredient
	 * @param obj for the object to compare
	 * @return boolean
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Recipe other = (Recipe) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

}
