import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.*
import org.scalajs.dom.document

object Main {
  val Hello =
    ScalaComponent.builder[String]
      .render_P(name => <.div("Hello there ", name))
      .build

  def main(args: Array[String]): Unit = {
    Hello("World").renderIntoDOM(document.getElementById("app"))
  }
}
