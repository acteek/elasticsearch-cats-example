package com.github.acteek.example

object Types {
  case class Owner(firstName: String, secondName: String)
  case class Cat(id: String, name: String, age: Int, food: List[String], owner: Owner)

  val cat1 = Cat("id20901", "Barsik", 5, List("bird", "fish", "apple"), Owner("Ivan", "Petrov"))
  val cat2 = Cat("id00002", "Iriska", 10, List("pineapple", "fish"), Owner("Bob", "Doll"))
  val cat3 = Cat("id00003", "Bob", 1, List("fish"), Owner("Bob", "Doll"))
}
