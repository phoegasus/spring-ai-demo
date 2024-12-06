package org.phoegasus.sample;

public enum TypeJouet {
  PELUCHE(3, 10),
  CARTES_A_COLLECTIONNER(7, 50),
  ROBOT_TELEGUIDE(5, 20);

  private final int ageMinimum;
  private final int taxe;

  TypeJouet(int ageMinimum, int taxe) {
    this.ageMinimum = ageMinimum;
    this.taxe = taxe;
  }

  @Override
  public String toString() {
    return "TypeJouet{" + "ageMinimum=" + ageMinimum + ", taxe=" + taxe + '}';
  }
}
