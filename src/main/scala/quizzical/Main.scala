package quizzical

import japgolly.scalajs.react.vdom.html_<^.*
import japgolly.scalajs.react.{Callback, ScalaComponent}
import org.scalajs.dom.document

object Main {
  case class Props(question: String, answers: List[String])

  val Question = ScalaComponent.builder[Props]("Question")
    .render_P { props =>
      <.div(
        ^.className := "question-box",
        <.p(^.className := "question", props.question),
        <.div(
          ^.className := "answers",
          <.div(
            ^.className := "answer", props.answers(0),
          ),
          <.div(
            ^.className := "answer", props.answers(1),
          ),
          <.div(
            ^.className := "answer", props.answers(2),
          ),
          <.div(
            ^.className := "answer", props.answers(3),
          )
        )
      )
    }
    .build

  def main(args: Array[String]): Unit = {
    Question(Props("What is the capital of France?", List("Paris", "London", "Berlin", "Rome"))).renderIntoDOM(document.getElementById("app"))
  }
}
