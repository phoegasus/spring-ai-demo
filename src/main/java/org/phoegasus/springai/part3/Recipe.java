package org.phoegasus.springai.part3;

import lombok.Value;

import java.util.List;

@Value
class Recipe {
  String name;
  List<String> ingredients;
  List<String> steps;
  int cookingTimeInMinutes;
  int estimatedCostInDollars;
}
