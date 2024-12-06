package org.phoegasus.springai.part5;

import lombok.Data;

import java.util.List;

@Data
class Recipe {
  private String name;
  private List<String> ingredients;
  private List<String> steps;
  private int cookingTimeInMinutes;
  private int estimatedCostInDollars;
  private String imagePrompt;
  private String imageLink;
  private boolean valid;

  public static Recipe invalid() {
    Recipe recipe = new Recipe();
    recipe.name = "Invalid Recipe";
    return recipe;
  }
}
