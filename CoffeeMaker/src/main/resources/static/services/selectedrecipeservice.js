// Define the 'myApp' module
var app = angular.module("myApp", []);

app.service("selectedRecipeService", function () {
  this.getRecipe = function (key) {
    return JSON.parse(localStorage.getItem(key));
  };

  this.setRecipe = function (key, data) {
    localStorage.setItem(key, JSON.stringify(data));
  };

  this.removeRecipe = function (key) {
    localStorage.removeItem(key);
  };
});
