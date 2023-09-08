package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for the URL mappings for CoffeeMaker. The controller returns
 * the approprate HTML page in the /src/main/resources/templates folder. For a
 * larger application, this should be split across multiple controllers.
 *
 * @author Kai Presler-Marshall
 */
@Controller
public class MappingController {

    /**
     * On a GET request to /index, the IndexController will return
     * /src/main/resources/templates/index.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/index", "/" } )
    public String index ( final Model model ) {
        return "index";
    }

    /**
     * On a GET request to /recipe, the RecipeController will return
     * /src/main/resources/templates/recipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/recipe", "/recipe.html" } )
    public String addRecipePage ( final Model model ) {
        return "recipe";
    }

    /**
     * On a GET request to /recipe, the RecipeController will return
     * /src/main/resources/templates/recipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/addrecipe", "/addrecipe.html" } )
    public String addRecipe ( final Model model ) {
        return "addrecipe";
    }

    /**
     * On a GET request to /recipe, the RecipeController will return
     * /src/main/resources/templates/recipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/UpdateInventory", "/UpdateInventory.html" } )
    public String updateInventory ( final Model model ) {
        return "UpdateInventory";
    }



    /**
     * On a GET request to /deleterecipe, the DeleteRecipeController will return
     * /src/main/resources/templates/deleterecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/deleterecipe", "/deleterecipe.html" } )
    public String deleteRecipeForm ( final Model model ) {
        return "deleterecipe";
    }

    /**
     * On a GET request to /editrecipe, the EditRecipeController will return
     * /src/main/resources/templates/editrecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/editrecipe", "/editrecipe.html" } )
    public String editRecipeForm ( final Model model ) {
        return "editrecipe";
    }

    /**
     * On a GET request to /editindividualrecipe, the EditRecipeController will return
     * /src/main/resources/templates/editindividualrecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/editindividualrecipe", "/editindividualrecipe.html" } )
    public String editIndividualRecipeForm ( final Model model ) {
        return "editindividualrecipe";
    }


    /**
     * Handles a GET request for inventory. The GET request provides a view to
     * the client that includes the list of the current ingredients in the
     * inventory and a form where the client can enter more ingredients to add
     * to the inventory.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/inventory", "/inventory.html" } )
    public String inventoryForm ( final Model model ) {
        return "inventory";
    }

    /**
     * On a GET request to /makecoffee, the MakeCoffeeController will return
     * /src/main/resources/templates/makecoffee.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/makecoffee", "/makecoffee.html" } )
    public String makeCoffeeForm ( final Model model ) {
        return "makecoffee";
    }
   

     /**
     * Handles a GET request for the update inventory page. The GET request provides
     * a view to the client where they can update the inventory ingredients.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping({"/updateinventory", "/updateinventory.html"})
    public String updateInventoryForm(final Model model) {
        return "updateinventory";
    }
    
    /**
     * Handles a GET request for the edit inventory ingredient page. The GET request provides
     * a view to the client where they can update the inventory ingredients.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping({"/EditInventoryIngredient", "/EditInventoryIngredient.html"})
    public String editInventoryIngredient(final Model model) {
        return "EditInventoryIngredient";
    }

    /**
     * Handles a GET request for the edit inventory ingredient page. The GET request
     * provides a view to the client where they can edit the details of an inventory
     * ingredient.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping({"/editinventoryingredient", "/editinventoryingredient.html"})
    public String editInventoryIngredientForm(final Model model) {
        return "editinventoryingredient";
    }

    /**
     * Handles a GET request for the editing recipe page. The GET request provides a
     * view to the client where they can add, remove, or edit the ingredients of a
     * recipe.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping({"editrecipe/editingrecipe", "/editingrecipe.html"})
    public String editingRecipeForm(final Model model) {
        return "editingrecipe";
    }

    
    /**
     * Handles a GET request for the editing recipe page. The GET request provides a
     * view to the client where they can add, remove, or edit the ingredients of a
     * recipe.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping({"editrecipe/editingrecipe/addingredients", "/addingredients.html"})
    public String addIngredientToRecipe(final Model model) {
        return "addingredients";
    }

    /**
     * Handles a GET request for the add ingredients page. The GET request provides a
     * view to the client where they can add ingredients to the inventory.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping({"/addingredients", "/addingredients.html"})
    public String addIngredientsForm(final Model model) {
        return "addingredients";
    }

    /**
     * Handles a GET request for the add ingredient inventory page. The GET request
     * provides a view to the client where they can add a new inventory ingredient.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping({"/addingredientinventory", "/addingredientinventory.html"})
    public String addIngredientInventoryForm(final Model model) {
        return "addingredientinventory";
    }
    

}
