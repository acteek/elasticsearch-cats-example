package com.github.acteek.example

object Types {
  case class Owner(firstName: String, secondName: String)
  case class Home(address: String)
  case class Cat(id: String, name: String, age: Int, food: List[String], owner: List[Owner], home: Option[Home])

  val cat1: Cat =
    Cat(
      "id001",
      "Barsik",
      5,
      List("bird", "fish", "apple"),
      List(Owner("Ivan", "Petrov"), Owner("Grigory", "M")),
      Some(Home("Linina 10"))
    )
  val cat2: Cat =
    Cat("id002", "Iriska", 10, List("pineapple", "fish"), List(Owner("Bob", "Doll"), Owner("Rick", "Sanchez")), None)
  val cat3: Cat = Cat("id003", "Bob", 1, List("fish"), List(Owner("Bob", "Doll"), Owner("Morty", "P")), None)
}
