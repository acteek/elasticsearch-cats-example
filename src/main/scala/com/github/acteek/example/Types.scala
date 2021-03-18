package com.github.acteek.example

object Types {
  case class Owner(firstName: String, secondName: String)
  case class Cat(id: String, name: String, age: Int, food: List[String], owner: Owner)

  val cat1: Cat = Cat("id001", "Barsik", 5, List("bird", "fish", "apple"), Owner("Ivan", "Petrov"))
  val cat2: Cat = Cat("id002", "Iriska", 10, List("pineapple", "fish"), Owner("Bob", "Doll"))
  val cat3: Cat = Cat("id003", "Bob", 1, List("fish"), Owner("Bob", "Doll"))
}
