package quizzical

import org.scalajs.dom

@main
def Quizzical(): Unit = {
  dom.document.getElementById("app").innerHTML =
    """
    <h1>Hello Scala.js!</h1>
    <a href="https://www.scala-js.org">Scala.js</a>
    """
}
